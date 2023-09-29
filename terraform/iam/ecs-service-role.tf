resource "aws_iam_role" "ecs_service_role" {
  name                = "ecs_service_role"
  path                = "/"
  assume_role_policy  = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs.amazonaws.com"
        }
      },
    ]
  })
  managed_policy_arns = [aws_iam_policy.ecs_service_policy.arn]
}

resource "aws_iam_policy" "ecs_service_policy" {
  policy = file("${path.module}/AmazonEC2ContainerServiceforEC2Role.json")
}

output "ecs_service_role_arn" {
  value = aws_iam_role.ecs_service_role.arn
}

output "ecs_service_role" {
  value = aws_iam_role.ecs_service_role
}