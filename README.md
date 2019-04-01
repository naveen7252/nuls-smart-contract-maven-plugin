# NULS Smart Contract Maven Plug-in
Maven Plugin to interact with NULS smart contracts

This is a custom maven plugin which enables developers to deploy and interact with NULS smart contracts which are written in Java.

## What does it Do ?


1. Contract Deployment:
    - Developer can deploy smart contract to NULS block chain with just simple mavan commands like - mvn clean install
    - No need to manually generate byte code or hexa decimal code for the deployment
    
2. Contract Execution:
    - Developer can call smart contract methods with simple maven commands
    - Can verify the contract execution status

3. Simple Block Chain operations:
    - Using this plugin, developer
    
          - Can create an account in NULS block chain
          - Can get balance of an account
          - Can query the details of a transaction
          
## How does it work?

- This plugin works along with [nuls-maven-archetype](https://github.com/naveen7252/nuls-maven-archetype) , it is a maven archetype to   create smart contract project with the template.
- Archetype adds this plugin automatically to the generated pom.xml file
- If developer is creating smart contract project without the archetype, add the following section in pom.xml file under builds section
  
            <plugin>
                <groupId>io.nuls</groupId>
                <artifactId>nuls-sc-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <phase>install</phase>
                        <goals>
                            <goal>deploy-contract</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
         
        
        
- By defauly, plugin runs agaisnt NULS testnet but can be asked to run against Mainnet with <mode> parameter. More information on the mode in goals section        

### Plug-in Goals

1. deploy-contract

    - To deploy smart contract to NULS block chain
    
    Usage : 
    
`mvn nuls-sc:deploy-contract -Dsender=<senderAddress> [-Dchain-mode=<testnet|mainnet>] [-DgasLimit=limit] [-DgasPrice=price] -Dpassword=<password> [-DprivateKey=<privKey>] [-Dargs=<-T text,-I number,-Z true>] [-Dremarks=<remarks>]`

  Explanation: All parameter within <> are mandatory parameter and [] are optional parameters
   
   - nuls-sc - name of the plugin
   - deploy-contract - name of the goal
   - senderAddress  - address of the account from which contract is being deployed/created
   - chain-mode - contract to be deployed on testnet or mainnet. By default, contact is deployed to testnet
   - gasLimit  - gasLimit, an optional parameter, if not provided, default value is taken and any unused gas will be refunded after execution
   - gasPrice  - gasPrice, an optional parameter, if not provided, default value is taken
   - password  - password of the account, mandatory for encrypted accounts
   - privateKey - Private key of the account, mandatory only for non-encrypted accounts
   - args  - arguements to the contract creation, optional but depends on contact design.If contract needs arguements while creating it, args need to be passed
   
            - formate to send arguements: <-argType argValue,-argType argvalue ...>
            - T - to send string value as arguement
            - I - to send integer value as arguement
            - L - to send long value as arguement
            - S - to send short value as arguement
            - C - to send char value as arguement
            - Z - to send boolean value as arguement
            - B - to send byte value as arguement
            - D - to send double value as arguement
            - F - to send float value as arguement


2. call-contract

   - To call NULS smart contract
   
   Usage: 
   
   `mvn nuls-sc:call-contract [-Dchain-mode=<testnet|mainnet>] -Dsender=<senderAddress> -DcontractAddress=<address> -DmethodName=<name> [-DgasLimit=limit] [-DgasPrice=price] -Dpassword=<password>"  [-DprivateKey=<privKey>] [-Dargs=<-T text,-I number,-Z true>] [-Dremarks=<remarks>] `
   
    - senderAddress  - address of the account from which contract is being deployed/created
    - contractAddress - address of the contract
    - methodName  - method to call
    - chain-mode - contract to be deployed on testnet or mainnet. By default, contact is deployed to testnet
    - gasLimit  - gasLimit, an optional parameter, if not provided, default value is taken and any unused gas will be refunded after execution
    - gasPrice  - gasPrice, an optional parameter, if not provided, default value is taken
    - password  - password of the account, mandatory for encrypted accounts
    - privateKey - Private key of the account, mandatory only for non-encrypted accounts
    - args  - arguements to the contract creation, optional but depends on contact design.If contract needs arguements while creating it, args need to be passed


3. create-account
  
   - To create account in NULS
   
   Usage:
   
   `mvn nuls-sc:create-account [-Dchain-mode=<testnet|mainnet>] [-Dpassword=<password>]`
   
   - If no password is provided, account will not be encrypted
