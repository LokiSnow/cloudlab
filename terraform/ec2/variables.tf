
variable "vpc_id" {}
variable "pubSN1" {}
variable "pubSN2" {}
variable "pubSN3" {}
variable "security_group" {}
variable "ecs_instance_role_name" {}
variable "ecs_instance_profile_name" {}

variable "project" {
  description = "Tags to set for all resources"
  type        = string
  default     = "cloudlab"
}

