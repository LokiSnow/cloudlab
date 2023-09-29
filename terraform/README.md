Fargate Reference:
https://github.com/scalablescripts/node-terraform
Ec2 Reference:
https://github.com/meshhq/terraform-meshhq-ecs-cluster/tree/master

Install:
https://developer.hashicorp.com/terraform/downloads?product_intent=terraform
Docs:
https://registry.terraform.io/providers/hashicorp/aws/latest/docs

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

auto scaling group -> launch template -> user_data setting:
https://docs.aws.amazon.com/AmazonECS/latest/developerguide/launch_container_instance.html


--------
### Trouble Shouting:

#### Cannot attach a Service Role Policy to a Customer Role.
```less
Error: attaching policy arn:aws:iam::aws:policy/aws-service-role/AmazonECSServiceRolePolicy to IAM Role ecs_service_role: PolicyNotAttachable: Cannot attach a Service Role Policy to a Customer Role.
│       status code: 400, request id: fe90c6cd-b39b-4af5-8559-d05b8de0fb52
```
Solution: create a custom policy according amazon's policy AmazonEC2ContainerServiceforEC2Role
```terraform
resource "aws_iam_policy" "ecs_service_policy" {
  policy = file("${path.module}/AmazonEC2ContainerServiceforEC2Role.json")
}
```
---
#### Instance can not register to cluster
Check ecs agent status  
`#sudo systemctl status ecs`  
Check agent log:  
`#sudo cat /var/log/ecs/ecs-agent.log`
```less
level=info time=2023-09-29T15:16:46Z msg="Registering Instance with ECS"
level=info time=2023-09-29T15:16:46Z msg="Remaining mem: 904" module=client.go
level=error time=2023-09-29T15:16:46Z msg="Unable to register as a container instance with ECS: ClientException: Cluster not found." module=client.go
level=error time=2023-09-29T15:16:46Z msg="Error registering container instance" error="ClientException: Cluster not found."
```
Solution: config terraform resource `depends_on`, to control resource creating sequence  
`depends_on = [var.ecs_cluster, aws_launch_template.ecs-launch-template, aws_alb.ecs_load_balancer]`

Reference:  
https://repost.aws/knowledge-center/ecs-instance-unable-join-cluster

---

#### Task role policy lack:
```less
[ec2-user@ip-10-0-2-203 cloudlab]$ pwd
/var/log/ecs/exec/085834c623f94319bf3fed1a112f499a/cloudlab

[ec2-user@ip-10-0-2-203 cloudlab]$ cat errors.log
2023-09-29 16:35:36 ERROR [SetWebSocket @ controlchannel.go.93] [ssm-agent-worker] [MessageService] [MGSInteractor] 
Failed to get controlchannel token, error: CreateControlChannel failed with error: createControlChannel request failed: unexpected response from the service <AccessDeniedException>
  <Message>User: arn:aws:sts::***:assumed-role/ecs_tasks_role/085834c623f94319bf3fed1a112f499a is not authorized to perform: ssmmessages:CreateControlChannel on resource: arn:aws:ecs:ap-southeast-1:***:task/cloudlab_cluster/085834c623f94319bf3fed1a112f499a 
because no identity-based policy allows the ssmmessages:CreateControlChannel action</Message>
</AccessDeniedException>
```
Solution: add "arn:aws:iam::aws:policy/AmazonSSMManagedEC2InstanceDefaultPolicy" to task role
```less
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.citi.cloudlab.dao.config.DynamodbInitialization]: Factory method 'dynamodbInitialization' 
threw exception with message: User: *** is not authorized to perform: dynamodb:ListTables on resource: arn:aws:dynamodb:ap-southeast-1:***:table
/* because no identity-based policy allows the dynamodb:ListTables action (Service: DynamoDb, Status Code: 400, Request ID: 7G4V3ICBHET0L1DAE8CJT4437VVV4KQNSO5AEMVJF66Q9ASUAAJG)
```
Solution: add "arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess" to task role