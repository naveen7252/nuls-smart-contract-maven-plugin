package io.nuls.plugin.mojo;


import io.nuls.plugin.util.Util;
import io.nuls.plugin.helper.NulsSDKHelper;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "tx-by-hash")
public class GetTxByHashMojo extends BaseNulsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String txHash = Util.getPropery("txHash");
        if(StringUtils.isBlank(txHash)){
            getLog().error("***** Transaction hash is required to get details*********");
            printUsage("tx-by-hash");
            throw new MojoExecutionException("Invalid args. txHash is required");
        }else{
            Result result = NulsSDKHelper.getTxByHash(txHash);
            if(result.isFailed()){
                getLog().info("*********** Couldn't get transaction details by hash ********");
                throw new MojoExecutionException("Couldn't get transaction details");
            }
            getLog().info("********** Transaction Details ********");
            getLog().info(result.toString());
        }

    }
}
