resource "aws_iam_instance_profile" "ecs_instance_profile" {
  name = "ecs_instance_profile"
  path = "/"
  role = aws_iam_role.ecs_instance_role.name
}

output "instance_profile_arn" {
  value = aws_iam_instance_profile.ecs_instance_profile.arn
}

