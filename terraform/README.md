Fargate Reference:
https://github.com/scalablescripts/node-terraform
Ec2 Reference:
https://github.com/meshhq/terraform-meshhq-ecs-cluster/tree/master

Install:
https://developer.hashicorp.com/terraform/downloads?product_intent=terraform

1.Create terraform package, and init terraform  
#terraform init  
2.create config.tf file, define aws credential  
Verify terraform config  
#terraform plan  
3.create vpc.tf, define aws vpc resource  
Execute terraform apply, to create aws resource  
#terraform apply --auto-approve  
4.create repository.tf, ecs.tf  
5.use variables.tf replace hard code  
https://developer.hashicorp.com/terraform/tutorials/aws-get-started/aws-variables  
#terraform plan -var  aws_uid=[] -var aws_access_key=[] -var aws_secret_key=[]  
#terraform apply -var  aws_uid=[] -var aws_access_key=[] -var aws_secret_key=[] --auto-approve  
6.add terraform.tfvars for local variable setting  
aws_uid = "---"  
aws_access_key = "---"  
aws_secret_key = "---"  
Task definition env setting:  
https://repost.aws/zh-Hans/knowledge-center/ecs-data-security-container-task  
