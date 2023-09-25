resource "aws_ecs_cluster" "ecs" {
  name = "ecs_cluster"
  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}
resource "aws_ecs_cluster_capacity_providers" "capacity_providers" {
  cluster_name = aws_ecs_cluster.ecs.name

  capacity_providers = [aws_ecs_capacity_provider.capacity-provider.name]

  default_capacity_provider_strategy {
    base              = 1
    weight            = 2
    capacity_provider = aws_ecs_capacity_provider.capacity-provider.name
  }
}

resource "aws_ecs_capacity_provider" "capacity-provider" {
  name = "${var.project}-capacity-provider"

  auto_scaling_group_provider {
    auto_scaling_group_arn         = var.ecs_autoscaling_group_arn

    managed_scaling {
      maximum_scaling_step_size = 1000
      minimum_scaling_step_size = 1
      status                    = "ENABLED"
      target_capacity           = 10
    }
  }
}

resource "aws_ecs_service" "service" {
  name = "${var.project}_service"
  cluster                = aws_ecs_cluster.ecs.arn
  enable_execute_command = true
  iam_role = var.ecs_service_role_arn

  deployment_maximum_percent         = 200
  deployment_minimum_healthy_percent = 100
  desired_count                      = 1
  task_definition                    = aws_ecs_task_definition.td.arn

  load_balancer {
    target_group_arn  = var.ecs_target_group_arn
    container_port    = 8089
    container_name    = var.project
  }
}

resource "aws_ecs_task_definition" "td" {
  container_definitions = jsonencode([
    {
      name         = var.project
      image        = "${var.aws_uid}.dkr.ecr.${var.aws_region}.amazonaws.com/${var.project}"
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
          awslogs-group = "/ecs/${var.project}"
          awslogs-region = var.aws_region
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])
  family                   = var.project
  task_role_arn      = var.ecs_tasks_role_arn
  execution_role_arn = var.ecs_tasks_role_arn
}
