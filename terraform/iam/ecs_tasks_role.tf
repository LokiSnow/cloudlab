resource "aws_iam_policy" "param_policy" {
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid = "VisualEditor0",
        Effect    = "Allow"
        Action   = "ssm:GetParameters"
        Resource = "arn:aws:ssm:${var.aws_region}:${var.aws_uid}:parameter/*"
      }
    ]
  })
}

resource "aws_iam_role" "ecs_tasks_role" {
  name = "ecs_tasks_role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      },
    ]
  })
  managed_policy_arns = [aws_iam_policy.param_policy.arn,
    "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy",
    "arn:aws:iam::aws:policy/AmazonS3FullAccess"
  ]
}

output "ecs_tasks_role_arn" {
  value = aws_iam_role.ecs_tasks_role.arn
}