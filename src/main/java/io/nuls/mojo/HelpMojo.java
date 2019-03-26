package io.nuls.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author naveen
 */
@Mojo(name="help")
public class HelpMojo extends BaseNulsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Maven NULS smart contract Plug-in");
        getLog().info("\t\t\t\t\t\t Helps to build and deploy NULS smart contract and call contract methods");
        getLog().info("\n");
        getLog().info("**** GOAL = create-account ******");
        printUsage("create-account");
        getLog().info("\n");
        getLog().info("\n");
        getLog().info("**** GOAL = get-balance *********");
        printUsage("get-balance");
        getLog().info("\n");
        getLog().info("\n");
        getLog().info("**** GOAL = deploy-contract**********");
        printUsage("deploy-contract");
        getLog().info("\n");
        getLog().info("\n");
        getLog().info("**** GOAL = call-contract **********");
        printUsage("call-contract");
        getLog().info("\n");
        getLog().info("\n");
        getLog().info("**** GOAL = tx-by-hash********");
        printUsage("tx-by-hash");
        getLog().info("\n");
        getLog().info("\n");
        getLog().info("**** GOAL = help*********");
        printUsage("help");
    }
}
