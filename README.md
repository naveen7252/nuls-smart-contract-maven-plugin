## NULS Smart Contract Maven Plug-in
Maven Plugin to interact with NULS smart contracts

This is a custom maven plugin which enables developers to deploy and interact with NULS smart contracts with simple maven commands.

### What does it Do ?


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
          
          
### How does it work?

- This plugin works along with [nuls-maven-archetype](https://github.com/naveen7252/nuls-maven-archetype) , it is a maven archetype to   create smart contract project with the template.
- Archetype adds this plugin automatically to the generated pom.xml file for the smart contract project
- If developer is creating smart contract project without the archetype, add the following section in pom.xml file under builds->plugins section
  
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
         
        
        
- By default, plug-in runs agaisnt NULS testnet but can be asked to run against Mainnet with <mode> parameter. More information on the mode in goals section        

### Plug-in Goals

1. deploy-contract

    - To deploy smart contract to NULS block chain
    
    Usage : 
    
`mvn nuls-sc:deploy-contract -Dsender=<senderAddress> -Dchain-mode=[testnet|mainnet] -DgasLimit=[limit] -DgasPrice=[price] -Dpassword=<password> -DprivateKey=[privKey] -Dargs=[-T text,-I number,-Z true] -Dremarks=[remarks]`

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
   
   `mvn nuls-sc:call-contract -Dchain-mode=[testnet|mainnet] -Dsender=<senderAddress> -DcontractAddress=<address> -DmethodName=<name> -DgasLimit=[limit] -DgasPrice=[price] -Dpassword=<password>"  -DprivateKey=[privKey] -Dargs=[-T text,-I number,-Z true] -Dremarks=[remarks] `
   
    - senderAddress  - address of the account from which contract is being deployed/created
    - contractAddress - address of the contract
    - methodName  - method to call
    - chain-mode - contract to be deployed on testnet or mainnet. By default, contact is deployed to testnet
    - gasLimit  - gasLimit, an optional parameter, if not provided, default value is taken and any unused gas will be refunded after execution
    - gasPrice  - gasPrice, an optional parameter, if not provided, default value is taken
    - password  - password of the account, mandatory for encrypted accounts
    - privateKey - Private key of the account, mandatory only for non-encrypted accounts
    - args  - arguements to the contract method, optional but depends on method design.If contract method needs arguements while calling it, args need to be passed



3. create-account
  
   - To create account in NULS
   
   Usage:
   
   `mvn nuls-sc:create-account -Dchain-mode=[testnet|mainnet] -Dpassword=[password]`
   
   - If no password is provided, account will not be encrypted
   
   
   
4. get-balance

    - To get balance of an account
    
    Usage:
    
    `mvn nuls-sc:get-balance -Daddress=<address> -Dchain-mode=[testnet|mainnet]`
    
    
    
5. tx-by-hash

    - Get transaction information by hash
    
    Usage:
    `mvn nuls-sc:tx-by-hash -DtxHash=<txHash> -Dchain-mode=[testnet|mainnet]`
    
    
    
6. help

      - prints all commands usage
      
     Usage:
     `mvn nuls-sc:help`
     
    
### Change NULS node IP and Port:

 - By default,plugin connectst to following address
 
          TestNet: IP: 127.0.0.1 and Port: 8001
           Mainnet: IP:1270.0.01  and Port: 6001
                
 - Changing default values:
 
      `mvn nuls-sc:create-account -Dnuls-rpc-host=[ipaddress] -Dnuls-rpc-port=[port]`
             
             Where, nuls-rpc-host = IP address of running NULS node
                    nuls-rpc-port = Port of running NULS node
                    
 ### Ways to provide default values to parameters:
 
 - Most used parameters while working with the Plug-in are i.senderAddress  ii. Password iii.Private Key
 - One time set up can be done to provide values to these parameters, so that develper doesn't need to provide them every time executing the goal.
 
 Follwoing are the ways to provide  values:
 
 1. Pass values to parameter while executing the goal. It has highest priority and values provided by other ways are ignored.
     Example: `mvn nuls-sc:deploy-contract -Dsender=<senderAddress> ...`
     
 2. Provide values in the plugin section of pom.xml file. This has second highest priority.
        
        <plugin>
                <groupId>io.nuls</groupId>
                <artifactId>nuls-sc-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <configuration>
                    <sender>${sender.address}</sender>
                    <password>${sender.password}</password>
                    <privateKey>${sender.privateKey}</privateKey>
                </configuration>
                <executions>
                    <execution>
                        <phase>install</phase>
                        <goals>
                            <goal>deploy-contract</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            
  3. Set environemnts variables.This has least priority.
  
        Env variables can be set up for sender, password and privatekey
        
    Windows:  
        set sender=<senderAddress>
        set password=<password>
        set privateKey=<privateKey>
    
    Linux:
           export sender=<senderAddress>
           export password=<password>
           export privateKey=<privateKey>
           
           
