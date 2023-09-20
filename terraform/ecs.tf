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
          value = var.aws_access_key
        },
        {
          name = "SECRET_ACCESS_KEY"
          value = var.aws_secret_key
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
  task_role_arn      = "arn:aws:iam::${var.aws_uid}:role/ecsTaskExecutionRole"
  execution_role_arn = "arn:aws:iam::${var.aws_uid}:role/ecsTaskExecutionRole"
}
