package io.nuls.mojo;

import io.nuls.helper.NulsSDKHelper;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.model.dto.BalanceInfo;
import io.nuls.sdk.core.utils.StringUtils;
import io.nuls.util.Util;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author naveen
 */
@Mojo(name = "get-balance")
public class GetBalanceMojo extends BaseNulsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String address = Util.getPropery("address");
        if(StringUtils.isBlank(address)){
            getLog().info("Address is required to get the balance");
            printUsage("get-balance");
        }
        Result result = NulsSDKHelper.getAccountBalance(address);
        if((result).isSuccess()){
            BalanceInfo balanceInfo = (BalanceInfo)result.getData();
            long balance = balanceInfo.getBalance();
            long usable = balanceInfo.getUsable();
            long locked = balanceInfo.getLocked();
            getLog().info("************* Account Balance Information ************");
            getLog().info("Balance = "+balance/100000000);
            getLog().info("usable = "+usable/100000000);
            getLog().info("locked = "+locked/100000000);
        }
    }
}
