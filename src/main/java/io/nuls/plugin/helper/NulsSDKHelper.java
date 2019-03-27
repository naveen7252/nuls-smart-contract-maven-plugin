package io.nuls.plugin.helper;

import io.nuls.plugin.util.Util;
import io.nuls.plugin.constant.Constants;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.contract.model.ContractTransactionCreatedReturnInfo;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.model.JsonRPCResult;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.*;
import io.nuls.sdk.tool.NulsSDKTool;

import java.util.List;
import java.util.Map;

/**
 * @author naveen
 */
public class NulsSDKHelper {

    static {
        String host = Util.getPropery("nuls-rpc-host");
        String port = Util.getPropery("nuls-rpc-port");
        String chainMode = Util.getPropery("chain-mode");
        if(chainMode != null && chainMode.equals(Constants.CHAIN_MODE_MAIN_NET)){
            if(null != host && null != port){
                boostStrap(host,port,Constants.CHAIN_ID_MAIN_NET,Constants.NULS_SCAN_URL_MAIN_NET);
            }else{
                boostStrap(Constants.DEFAULT_IP,Constants.DEFAULT_MAIN_NET_PORT,Constants.CHAIN_ID_MAIN_NET,Constants.NULS_SCAN_URL_MAIN_NET);
            }
        }else{
            if(null != host && null != port){
                boostStrap(host,port,Constants.CHAIN_ID_TEST_NET,Constants.NULS_SCAN_URL_TEST_NET);
            }else{
                boostStrap(Constants.DEFAULT_IP,Constants.DEFAULT_PORT,Constants.CHAIN_ID_TEST_NET,Constants.NULS_SCAN_URL_TEST_NET);
            }
        }

    }

    private static void boostStrap(String ip,String port,int chainId,String nulScanUrl){
        SDKBootstrap.init(ip,port,chainId,nulScanUrl);
    }
    public static Result createAccount(String password){
        if(StringUtils.isNotBlank(password)){
            return NulsSDKTool.createAccount(password);
        }
        return NulsSDKTool.createAccount();
    }

    public static String getPrivateKey(String address,String password) {
        Result result = null;
        if (StringUtils.isNotBlank(password)) {
            result = NulsSDKTool.getPrikey(address, password);
        } else {
            result = NulsSDKTool.getPrikey(address);
        }
        if (result.isSuccess()) {
            Map<String, Object> dataMap = getDataMap(result);
            return (String) dataMap.get("value");
        }
        return null;
    }

    public static boolean isAccountEncrypted(String address){
        Result result = NulsSDKTool.isEncrypted(address);
        if(result.isSuccess()){
            Map<String, Object> dataMap = getDataMap(result);
            return (Boolean) dataMap.get("value");
        }
        return false;
    }

    public static Result getAccountBalance(String address){
        return NulsSDKTool.getBalance(address);
    }

    public static Result getTxByHash(String hash){
        return NulsSDKTool.getTxByHash(hash);
    }

    public static List<Input> getUtxos(String address,long amount) throws Exception {
        JsonRPCResult rpcResult =  NulsSDKTool.getUtxo(address,amount);
        if (rpcResult.getError() != null) {
            throw new Exception("Couldn't get UTXOs for the account :"+address);
        }
        List<Input> inputs = (List<Input>) rpcResult.getResult();
        return inputs;
    }

    public static ContractTransactionCreatedReturnInfo createContract(String address,long gasPrice,long gasLimit,String contractCode,Object[] args,String remarks,List<Input> utxos){
        Result result = NulsSDKTool.createContractTransaction(address,gasLimit,gasPrice,contractCode,args,remarks,utxos);
        if(result.isSuccess()){
            Map<String,Object> dataMap = getDataMap(result);
            return (ContractTransactionCreatedReturnInfo)dataMap.get("value");
        }
        return null;
    }

    public static ContractTransactionCreatedReturnInfo callContract(String sender, Long value, Long gasLimit, Long gasPrice, String contractAddress, String methodName, String methodDesc, Object[] args, String remark, List<Input> utxos){
        Result result = NulsSDKTool.callContractTransaction(sender,value,gasLimit,gasPrice,contractAddress,methodName,methodDesc,args,remark,utxos);
        if(result.isSuccess()){
            Map<String,Object> dataMap = getDataMap(result);
            return (ContractTransactionCreatedReturnInfo)dataMap.get("value");
        }
        return null;
    }
    public static Map<String,Object> getDataMap(Result result){
        return (Map<String,Object>)result.getData();
    }

    public static Result signTransaction(String txHex,String address,String privKey,String password){
        return NulsSDKTool.signTransaction(txHex,privKey,address,password);
    }

    public static Result broadcastTransaction(String txHex){
        return NulsSDKTool.broadcastTransaction(txHex);
    }
}
