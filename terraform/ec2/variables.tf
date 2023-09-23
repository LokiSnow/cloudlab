
variable "vpc_id" {}
variable "pubSN1" {}
variable "pubSN2" {}
variable "pubSN3" {}
variable "security_group" {}
variable "ecs_instance_role_name" {}
variable "ecs_instance_profile_name" {}

variable "aws_uid" {
  description = "AWS user id"
  type        = string
  default     = ""
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-southeast-1"
}

variable "project" {
  description = "Tags to set for all resources"
  type        = string
  default     = "cloudlab"
}

