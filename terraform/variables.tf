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

variable "aws_access_key" {
  description = "AWS access key"
  type        = string
  default     = ""
}

variable "aws_secret_key" {
  description = "AWS access key"
  type        = string
  default     = ""
}

variable "project" {
  description = "Tags to set for all resources"
  type        = string
  default     = "cloudlab"
}

