package io.nuls.mojo;

import io.nuls.Constants;
import io.nuls.helper.NulsSDKHelper;
import io.nuls.sdk.core.model.Result;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.Map;

/**
 * @author naveen
 */
public abstract class BaseNulsMojo extends AbstractMojo {

    @Parameter(property = "contract-lib-dir", defaultValue = "${project.basedir}/lib")
    protected String contractLibJar;

    @Parameter(property = "dapp-jar", defaultValue = "${project.build.directory}/${project.build.finalName}.jar")
    protected String dappJar;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Parameter(property = "nuls-rpc-host", defaultValue = Constants.DEFAULT_IP)
    protected String nulsRpcHost;

    @Parameter(property = "nuls-rpc-port", defaultValue = Constants.DEFAULT_PORT)
    protected String nulsRpcPort;

    @Parameter(property = "chain-mode",defaultValue = Constants.CHAIN_MODE_TEST_NET)
    protected String chainMode;

    @Parameter(property = "nuls-scan-url",defaultValue = Constants.NULS_SCAN_URL_TEST_NET)
    protected String nulScanUrl;

    @Parameter(property = "gasLimit",defaultValue = "")
    protected String gasLimit;

    @Parameter(property = "gasPrice",defaultValue = "")
    protected String gasPrice;

    protected long defaultGasLimit = 100000L;
    protected long defaultGasPrice = 100L;

    public String signAndBroadcastTx(String txHex,String address,String privKey,String password,boolean isEncrypted) throws MojoExecutionException{
        if(isEncrypted){
            privKey = NulsSDKHelper.getPrivateKey(address,password);
        }
        Result result = NulsSDKHelper.signTransaction(txHex,address,privKey,null);
        if(result.isSuccess()){
            Map<String,Object> dataMap = NulsSDKHelper.getDataMap(result);
            String txHex1 = (String)dataMap.get("value");
            Result result1 = NulsSDKHelper.broadcastTransaction(txHex1);
            if(result1.isSuccess()){
                Map<String,Object> data = NulsSDKHelper.getDataMap(result1);
                String txHash = (String)data.get("value");
                getLog().info("Transaction Hash: "+ txHash);
                return txHash;
            }
        }else{
            getLog().error("Operation failed : "+result.toString());
            throw new MojoExecutionException("Operation failed");
        }
        return null;
    }

    protected void printUsage(String goal){
        getLog().info("************* USAGE ***************");
        switch (goal){
            case "create-account":
                getLog().info("mvn nuls-sc:create-account [-Dpassword=<password>]");
                break;
            case "get-balance":
                getLog().info("mvn nuls-sc:get-balance -Daddress=<address>");
                break;
            case "deploy-contract":
                getLog().info("mvn nuls-sc:deploy-contract -Dsender=<senderAddress> [-DgasLimit=limit] [-DgasPrice=price] -Dpassword=<password>" +
                        " [-DprivateKey=<privKey>] [-Dargs=<-T text,-I number,-Z true>] [-Dremarks=<remarks>]");
                break;
            case "call-contract":
                getLog().info("mvn nuls-sc:call-contract -Dsender=<senderAddress> -DcontractAddress=<address> -DmethodName=<name> [-DgasLimit=limit] [-DgasPrice=price] -Dpassword=<password>" +
                        " [-DprivateKey=<privKey>] [-Dargs=<-T text,-I number,-Z true>] [-Dremarks=<remarks>]");
                break;
            case "tx-by-hash":
                getLog().info("mvn nuls-sc:tx-by-hash -DtxHash=<txHash>");
                break;
            default:
                getLog().info("mvn nuls-sc:help");
        }
    }

    public String getContractLibJar() {
        return contractLibJar;
    }

    public void setContractLibJar(String contractLibJar) {
        this.contractLibJar = contractLibJar;
    }

    public String getDappJar() {
        return dappJar;
    }

    public void setDappJar(String dappJar) {
        this.dappJar = dappJar;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public String getNulsRpcHost() {
        return nulsRpcHost;
    }

    public void setNulsRpcHost(String nulsRpcHost) {
        this.nulsRpcHost = nulsRpcHost;
    }

    public String getNulsRpcPort() {
        return nulsRpcPort;
    }

    public void setNulsRpcPort(String nulsRpcPort) {
        this.nulsRpcPort = nulsRpcPort;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public long getDefaultGasLimit() {
        return defaultGasLimit;
    }

    public void setDefaultGasLimit(long defaultGasLimit) {
        this.defaultGasLimit = defaultGasLimit;
    }

    public long getDefaultGasPrice() {
        return defaultGasPrice;
    }

    public void setDefaultGasPrice(long defaultGasPrice) {
        this.defaultGasPrice = defaultGasPrice;
    }
}
