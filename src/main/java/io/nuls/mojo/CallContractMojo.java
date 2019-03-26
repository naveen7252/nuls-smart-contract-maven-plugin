package io.nuls.mojo;

import io.nuls.helper.NulsSDKHelper;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.contract.model.ContractTransactionCreatedReturnInfo;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.LongUtils;
import io.nuls.sdk.core.utils.StringUtils;
import io.nuls.sdk.tool.NulsSDKTool;
import io.nuls.util.Util;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;

/**
 * @author naveen
 */
@Mojo(name="call-contract")
public class CallContractMojo extends BaseNulsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String sender = Util.getPropery("address");
        if(StringUtils.isBlank(sender)){
            getLog().error("Contract caller address is required");
            printUsage("call-contract");
            throw new MojoExecutionException("Invalid args. address is required");
        }
        String password = Util.getPropery("password");
        boolean isEncrypted = NulsSDKHelper.isAccountEncrypted(sender);
        if(StringUtils.isBlank(password) && isEncrypted){
            getLog().error("Password is required for encrypted account to perform the action");
            printUsage("call-contract");
            throw new MojoExecutionException("Invalid args. password is required");
        }
        String privateKey = Util.getPropery("privateKey");
        if(StringUtils.isBlank(privateKey) && !isEncrypted){
            privateKey = NulsSDKHelper.getPrivateKey(sender,password);
            if(StringUtils.isBlank(privateKey)){
                getLog().error("Private Key is required to perform the action");
                printUsage("call-contract");
                throw new MojoExecutionException("Invalid args. Private Key is required");
            }
        }
        String contractAddress = Util.getPropery("contractAddress");
        if(StringUtils.isBlank(contractAddress)){
            getLog().error("Contract Address is required");
            printUsage("call-contract");
            throw new MojoExecutionException("Invalid args. contract address is required");
        }
        long gasLimit = Long.valueOf(StringUtils.isNotBlank(Util.getPropery("gasLimit")) ? Util.getPropery("gasLimit") : "0");
        if(gasLimit == 0L){
            gasLimit = getDefaultGasLimit();
        }
        long gasPrice = Long.valueOf(StringUtils.isNotBlank(Util.getPropery("gasPrice")) ? Util.getPropery("gasPrice") : "0");
        if(gasPrice == 0L){
            gasPrice = getDefaultGasPrice();
        }
        long value = 0L;
        if(StringUtils.isNotBlank(Util.getPropery("value"))){
            value = Long.valueOf(Util.getPropery("value"));
        }
        String methodName = Util.getPropery("methodName");
        if(StringUtils.isBlank(methodName)){
            getLog().error("Method Name is required");
            printUsage("call-contract");
            throw new MojoExecutionException("Invalid args. Method Name is required");
        }
        String methodDesc = Util.getPropery("methodDesc");
        String argsString = Util.getPropery("args");
        Object[] args = Util.parseArgs(argsString);
        String remarks = Util.getPropery("remarks");

        long totalGas = LongUtils.mul(gasLimit, gasPrice);
        List<Input> utxos = null;
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
        ContractTransactionCreatedReturnInfo info = NulsSDKHelper.callContract(sender,value,gasLimit,gasPrice,contractAddress,methodName,methodDesc,args,remarks,utxos);
        if(null == info){
            getLog().error(" Failed to call the contract ");
            throw new MojoExecutionException("Error in calling the contract");
        }
        getLog().info("****** Call Transaction Details ****");
        signAndBroadcastTx(info.getTxHex(),sender,privateKey,password,isEncrypted);
    }
}
