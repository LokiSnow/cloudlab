provider "aws" {
  region = var.aws_region
  access_key = var.aws_access_key
  secret_key = var.aws_secret_key
}

resource "aws_ecs_cluster" "ecs" {
  name = "${var.project}_cluster"
}

module "iam" {
  source = "./iam"
  aws_uid = var.aws_uid
  aws_region = var.aws_region
}

module "ssm" {
  source = "./ssm"
  aws_access_key = var.aws_access_key
  aws_secret_key = var.aws_secret_key
}

module "vpc" {
  source = "./vpc"
}

module "ec2" {
  source = "./ec2"
  project = var.project
  vpc_id                      = module.vpc.id
  security_group              = module.vpc.security_group
  pubSN1                      = module.vpc.pubSN1
  pubSN2                      = module.vpc.pubSN2
  pubSN3                      = module.vpc.pubSN3
  ecs_instance_role_name      = module.iam.ecs_instance_role_name
  ecs_instance_profile_arn    = module.iam.instance_profile_arn
  ecs_cluster                 = aws_ecs_cluster.ecs
  ecs_cluster_name                 = aws_ecs_cluster.ecs.name
}

module "ecs" {
  source = "./ecs"
  project = var.project
  aws_uid = var.aws_uid
  aws_region = var.aws_region
  vpc_id                      = module.vpc.id
  security_group              = module.vpc.security_group
  pubSN1                      = module.vpc.pubSN1
  pubSN2                      = module.vpc.pubSN2
  pubSN3                      = module.vpc.pubSN3
  ecs_target_group_arn        = module.ec2.ecs_target_group_arn
  ecs_autoscaling_group_arn   = module.ec2.ecs_autoscaling_group_arn
  ecs_service_role_arn        = module.iam.ecs_service_role_arn
  ecs_service_role            = module.iam.ecs_service_role
  ecs_tasks_role_arn          = module.iam.ecs_tasks_role_arn
  ecs_cluster_arn             = aws_ecs_cluster.ecs.arn
  ecs_cluster_name            = aws_ecs_cluster.ecs.name
  ecs_autoscaling_group       = module.ec2.ecs_autoscaling_group
}