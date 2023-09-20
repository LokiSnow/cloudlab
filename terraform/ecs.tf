resource "aws_ecs_cluster" "ecs" {
  name = "${var.resource_tags["project"]}_cluster"
}

resource "aws_ecs_service" "service" {
  name = "${var.resource_tags["project"]}_service"
  cluster                = aws_ecs_cluster.ecs.arn
  launch_type            = "FARGATE"
  enable_execute_command = true

  deployment_maximum_percent         = 200
  deployment_minimum_healthy_percent = 100
  desired_count                      = 1
  task_definition                    = aws_ecs_task_definition.td.arn

  network_configuration {
    assign_public_ip = true
    security_groups  = [aws_security_group.sg.id]
    subnets          = [aws_subnet.pubSN1.id, aws_subnet.pubSN2.id, aws_subnet.pubSN3.id]
  }
}

resource "aws_iam_policy" "param_policy" {
  #name = "param_policy"
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

resource "aws_ssm_parameter" "access_key" {
  name = "ACCESS_KEY_ID"
  value = var.aws_access_key
  type = "String"
}

resource "aws_ssm_parameter" "secret_key" {
  name = "SECRET_ACCESS_KEY"
  value = var.aws_secret_key
  type = "String"
}

resource "aws_ecs_task_definition" "td" {
  container_definitions = jsonencode([
    {
      name         = "${var.resource_tags["project"]}_td"
      image        = "${var.aws_uid}.dkr.ecr.${var.aws_region}.amazonaws.com/${var.resource_tags["project"]}"
      cpu          = 256
      memory       = 512
      essential    = true
      portMappings = [
        {
          containerPort = 8089
          hostPort      = 8089
        }
      ]
      environment = [
        {
          name = "AWS_REGION"
          value = var.aws_region
        },
        {
          name = "ACCESS_KEY_ID"
          value = "arn:aws:ssm:${var.aws_region}:${var.aws_uid}:parameter/ACCESS_KEY_ID"
        },
        {
          name = "SECRET_ACCESS_KEY"
          value = "arn:aws:ssm:${var.aws_region}:${var.aws_uid}:parameter/SECRET_ACCESS_KEY"
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-create-group = "true"
          awslogs-group = "/ecs/${var.resource_tags["project"]}_td"
          awslogs-region = var.aws_region
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])
  family                   = "${var.resource_tags["project"]}_td"
  requires_compatibilities = ["FARGATE"]

  cpu                = "256"
  memory             = "512"
  network_mode       = "awsvpc"
  task_role_arn      = aws_iam_role.ecs_tasks_role.arn
  execution_role_arn = aws_iam_role.ecs_tasks_role.arn
}
