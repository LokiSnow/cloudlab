resource "aws_iam_role" "ecs_instance_role" {
  name                = "ecs_instance_role"
  path                = "/"
  assume_role_policy  = data.aws_iam_policy_document.ecs-instance-policy.json
  managed_policy_arns = ["arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role",
    "arn:aws:iam::aws:policy/AmazonEC2FullAccess",
    "arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess",
    "arn:aws:iam::aws:policy/AmazonSSMManagedEC2InstanceDefaultPolicy"
  ]
}

data "aws_iam_policy_document" "ecs-instance-policy" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

output "ecs_instance_role_name" {
  value = aws_iam_role.ecs_instance_role.name
}