package io.nuls.mojo;

import io.nuls.helper.NulsSDKHelper;
import io.nuls.sdk.core.model.Result;
import io.nuls.util.Util;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;
import java.util.Map;

/**
 * @author naveen
 */
@Mojo(name = "create-account")
public class CreateAccountMojo extends BaseNulsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Create NULS account");
        String password = Util.getPropery("password");
        Result result = NulsSDKHelper.createAccount(password);
        String address;
        if (result.isSuccess()){
            Map<String,Object> dataMap = (Map<String,Object>)result.getData();
            List<String> accList = (List<String>) dataMap.get("list");
            address = accList.get(0);
            String privateKey = NulsSDKHelper.getPrivateKey(address,password);
            getLog().info("********** Account Details **************");
            getLog().info(" Address ="+address);
            getLog().info("Private Key ="+privateKey);
        }else{
            getLog().info("********* Unable to create account ******");
        }
    }
}
