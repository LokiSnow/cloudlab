## Aws Serverless Deploy Sprinboot3 application
[serverless-java-container](https://github.com/aws/serverless-java-container)

[Quick start Spring Boot3](https://github.com/aws/serverless-java-container/wiki/Quick-start---Spring-Boot3)

https://aws.amazon.com/cn/blogs/compute/re-platforming-java-applications-using-the-updated-aws-serverless-java-container/

Samples:  
https://github.com/aws/serverless-java-container/blob/main/samples/springboot3/alt-pet-store

https://github.com/Vadym79/AWSLambdaJavaSnapStart/blob/main/spring-boot-3.2/template.yaml

Install SAM  
https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html

SAM's template.yml :  
https://docs.aws.amazon.com/zh_cn/serverless-application-model/latest/developerguide/serverless-policy-templates.html

Deploy by github action:  
https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/deploying-using-github.html

![img.png](img.png)
## Build
```shell
➜  cloudlab git:(dev) ✗ sam build  
Building codeuri: /Users/loki/Develop/projects/citi/cloudlab_github/cloudlab runtime: java21 metadata: {} architecture: x86_64 functions: CloudLabFunction
Running JavaGradleWorkflow:GradleBuild
Running JavaGradleWorkflow:JavaGradleCopyArtifacts

Build Succeeded

Built Artifacts  : .aws-sam/build
Built Template   : .aws-sam/build/template.yaml

Commands you can use next
=========================
[*] Validate SAM template: sam validate
[*] Invoke Function: sam local invoke
[*] Test Function in the Cloud: sam sync --stack-name {{stack-name}} --watch
[*] Deploy: sam deploy --guided

➜  cloudlab git:(dev) ✗ ls -alt .aws-sam/build
total 8
-rw-r--r--@ 1 loki  staff  1136 May  8 01:22 template.yaml
drwxr-xr-x@ 4 loki  staff   128 May  8 01:22 .
drwxr-xr-x@ 8 loki  staff   256 May  8 01:22 CloudLabFunction
drwxr-xr-x@ 4 loki  staff   128 May  8 01:21 ..

➜  cloudlab git:(dev) ✗ sam validate
/Users/loki/Develop/projects/citi/cloudlab_github/cloudlab/template.yaml is a valid SAM Template. This is according to basic SAM Validation, for additional validation, please run with "--lint" option

➜  cloudlab git:(dev) ✗ sam deploy --guided

Configuring SAM deploy
======================

	Looking for config file [samconfig.toml] :  Not found

	Setting default arguments for 'sam deploy'
	=========================================
	Stack Name [sam-app]: sam-cloudlab
	AWS Region [ap-southeast-1]:
	#Shows you resources changes to be deployed and require a 'Y' to initiate deploy
	Confirm changes before deploy [y/N]: Y
	#SAM needs permission to be able to create roles to connect to the resources in your template
	Allow SAM CLI IAM role creation [Y/n]: Y
	#Preserves the state of previously provisioned resources when an operation fails
	Disable rollback [y/N]: N
	CloudLabFunction has no authentication. Is this okay? [y/N]: y
	Save arguments to configuration file [Y/n]: Y
	SAM configuration file [samconfig.toml]:
	SAM configuration environment [default]:

	Looking for resources needed for deployment:
	Creating the required resources...
	Successfully created!

	Managed S3 bucket: aws-sam-cli-managed-default-samclisourcebucket-g9u2erfvekgk
	A different default S3 bucket can be set in samconfig.toml and auto resolution of buckets turned off by setting resolve_s3=False

	Saved arguments to config file
	Running 'sam deploy' for future deployments will use the parameters saved above.
	The above parameters can be changed by modifying samconfig.toml
	Learn more about samconfig.toml syntax at
	https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-config.html

	Uploading to sam-cloudlab/2c6fa2efc74f76c6634d439a8d87542f  41848453 / 41848453  (100.00%)

	Deploying with following values
	===============================
	Stack name                   : sam-cloudlab
	Region                       : ap-southeast-1
	Confirm changeset            : True
	Disable rollback             : False
	Deployment s3 bucket         : aws-sam-cli-managed-default-samclisourcebucket-g9u2erfvekgk
	Capabilities                 : ["CAPABILITY_IAM"]
	Parameter overrides          : {}
	Signing Profiles             : {}

Initiating deployment
=====================

	Uploading to sam-cloudlab/cb9176ee77d1af3ab017fdc0375d205a.template  1230 / 1230  (100.00%)


Waiting for changeset to be created..

CloudFormation stack changeset
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Operation                                            LogicalResourceId                                    ResourceType                                         Replacement
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
+ Add                                                CloudLabFunctionHttpApiEventPermission               AWS::Lambda::Permission                              N/A
+ Add                                                CloudLabFunctionRole                                 AWS::IAM::Role                                       N/A
+ Add                                                CloudLabFunction                                     AWS::Lambda::Function                                N/A
+ Add                                                ServerlessHttpApiApiGatewayDefaultStage              AWS::ApiGatewayV2::Stage                             N/A
+ Add                                                ServerlessHttpApi                                    AWS::ApiGatewayV2::Api                               N/A
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Changeset created successfully. arn:aws:cloudformation:ap-southeast-1:497680840552:changeSet/samcli-deploy1715102750/524831b0-e7db-4705-b640-f26082b880c8


Previewing CloudFormation changeset before deployment
======================================================
Deploy this changeset? [y/N]: N
```

## view stacks
```shell
➜  cloudlab git:(dev) ✗ aws cloudformation help
➜  cloudlab git:(dev) ✗ aws cloudformation list-stacks
```

## Clean up:
```shell
#aws cloudformation delete-stack --stack-name sam-cloudlab
```


## Deployment Problems:
### 1. template.yml add dynamodb's policy for function  
```
   Caused by: org.springframework.beans.factory.BeanCreationException: 
   Error creating bean with name 'dynamodbInitialization' defined in class path resource [com/citi/cloudlab/dao/config/DynamodbConfiguration.class]: 
   Failed to instantiate [com.citi.cloudlab.dao.config.DynamodbInitialization]: Factory method 'dynamodbInitialization' threw exception with message: User: arn:aws:sts::<...>:assumed-role/sam-cloudlab-CloudLabFunctionRole-aFAcmkXpAWD4/CloudLabFunction 
   is not authorized to perform: dynamodb:ListTables on resource: arn:aws:dynamodb:ap-southeast-1:<...>:table/* because no identity-based policy allows the dynamodb:ListTables action (Service: DynamoDb, Status Code: 400, Request ID: EN2BHQFFLVO0RA4K53NBVSPTBNVV4KQNSO5AEMVJF66Q9ASUAAJG)
```

### 2.CORS ISSUE investigation
#### 1).add @EnableWebFluxSecurity
then got error:
```
Parameter 0 of method securityWebFilterChain in com.citi.cloudlab.CloudlabApplication required a bean of type 'org.springframework.security.config.web.server.ServerHttpSecurity' that could not be found.
```

#### 2).add spring.main.allow-bean-definition-overriding=true  in application.yml
then got error:
```
The bean 'conversionServicePostProcessor', defined in class path resource [org/springframework/security/config/annotation/web/configuration/WebSecurityConfiguration.class], could not be registered. A bean with that name has already been defined in org.springframework.security.config.annotation.web.reactive.WebFluxSecurityConfiguration and overriding is disabled.
```
#### 3).add add spring.main.web-application-type=reactive
then got error:
```
Exception in thread "Thread-0" java.lang.IllegalStateException: java.lang.ClassCastException: class org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext cannot be cast to class org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext (org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext and org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext are in unnamed module of loader com.amazonaws.services.lambda.runtime.api.client.CustomerClassLoader @58372a00)
```

#### 4).final solution -> Change cors of aws api gateway
https://docs.aws.amazon.com/apigateway/latest/developerguide/how-to-cors-console.html

Conclusion:  got used to find solution on Application level first, could seek within Cloud tech stack in priority
