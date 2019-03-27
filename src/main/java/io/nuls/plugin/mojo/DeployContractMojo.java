package io.nuls.plugin.mojo;

import io.nuls.plugin.helper.NulsSDKHelper;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.contract.model.ContractTransactionCreatedReturnInfo;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.utils.LongUtils;
import io.nuls.sdk.core.utils.StringUtils;
import io.nuls.plugin.util.Util;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author naveen
 */
@Mojo(name = "deploy-contract")
public class DeployContractMojo extends BaseNulsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String sender = Util.getPropery("sender");
        if(StringUtils.isBlank(sender)){
            sender = getSenderEnv();
            if(StringUtils.isBlank(sender)){
                getLog().error("Contract deployer address is required");
                printUsage("deploy-contract");
                throw new MojoExecutionException("Invalid args. address is required");
            }
        }
        String password = Util.getPropery("password");
        boolean isEncrypted = NulsSDKHelper.isAccountEncrypted(sender);
        if(StringUtils.isBlank(password) && isEncrypted){
            password = getPasswordEnv();
            if(StringUtils.isBlank(password)){
                getLog().error("Password is required for encrypted accounts to perform the action");
                printUsage("deploy-contract");
                throw new MojoExecutionException("Invalid args. password is required");
            }
        }
        String privateKey = Util.getPropery("privateKey");
        if(StringUtils.isBlank(privateKey) && !isEncrypted){
            privateKey = NulsSDKHelper.getPrivateKey(sender,password);
            if(StringUtils.isBlank(privateKey)){
                privateKey = getPrivateKeyEnv();
                if(StringUtils.isBlank(privateKey)){
                    getLog().error("Private Key is required to perform the action");
                    printUsage("deploy-contract");
                    throw new MojoExecutionException("Invalid args. Private Key is required");
                }
            }
        }
        long gasLimit = Long.valueOf(StringUtils.isNotBlank(Util.getPropery("gasLimit")) ? Util.getPropery("gasLimit") : "0");
        if(gasLimit == 0L){
            gasLimit = getDefaultGasLimit();
        }
        long gasPrice = Long.valueOf(StringUtils.isNotBlank(Util.getPropery("gasPrice")) ? Util.getPropery("gasPrice") : "0");
        if(gasPrice == 0L){
            gasPrice = getDefaultGasPrice();
        }
        Path path = Paths.get(getDappJar());
        if (!Files.exists(path)) {
            throw new MojoExecutionException(String.format("Dapp jar file doesn't exist : %s \n"
                    + "Please make sure you have built the project.", dappJar));
        }
        byte[] bytes = Util.readBytes(path);
        String contractHex = Hex.encode(bytes);
        if(StringUtils.isBlank(contractHex)){
            getLog().error("Error in contract deployment");
            throw new MojoExecutionException("Couldn't generate HEX code for the contract");
        }
        String argsString = Util.getPropery("args");
        Object[] args = Util.parseArgs(argsString);
        String remarks = Util.getPropery("remarks");
        long totalGas = LongUtils.mul(gasLimit, gasPrice);
        List<Input> utxos;
        try {
           utxos = NulsSDKHelper.getUtxos(sender,totalGas);
           if(utxos == null || utxos.size() == 0){
               getLog().error("UTXos are not available for the account :"+sender);
               throw new MojoExecutionException("UTXos are not available for the account :"+sender);
           }
        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new MojoExecutionException(e.getMessage());
        }
        ContractTransactionCreatedReturnInfo contractInfo = NulsSDKHelper.createContract(sender,gasPrice,gasLimit,contractHex,args,remarks,utxos);
        if(null != contractInfo){
            getLog().info("************ Contract Details **************");
            getLog().info("Contract Address :" +contractInfo.getContractAddress());
            signAndBroadcastTx(contractInfo.getTxHex(),sender,privateKey,password,isEncrypted);
        }
    }
}
