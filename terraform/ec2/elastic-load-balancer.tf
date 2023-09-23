resource "aws_alb" "ecs_load_balancer" {
  name                = "${var.project}-load-balancer"
  security_groups     = [var.security_group]
  subnets             = [var.pubSN1, var.pubSN2, var.pubSN3]
}

resource "aws_alb_target_group" "ecs_target_group" {
  name                = "${var.project}-alb-target-group"
  port                = "8089"
  protocol            = "HTTP"
  vpc_id              = var.vpc_id

  health_check {
    healthy_threshold   = "5"
    unhealthy_threshold = "2"
    interval            = "30"
    matcher             = "200"
    path                = "/actuator/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = "5"
  }
}

resource "aws_alb_listener" "alb-listener" {
  load_balancer_arn = aws_alb.ecs_load_balancer.arn
  port              = "8089"
  protocol          = "HTTP"

  default_action {
    target_group_arn = aws_alb_target_group.ecs_target_group.arn
    type             = "forward"
  }
}

output "ecs_load_balancer_name" {
  value = aws_alb.ecs_load_balancer.name
}

output "ecs_target_group_arn" {
  value = aws_alb_target_group.ecs_target_group.arn
}