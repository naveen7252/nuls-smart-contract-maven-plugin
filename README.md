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
         
        
        

### Plug-in Goals

