resource "aws_launch_template" "ecs-launch-template" {
  name_prefix   = var.project
  image_id      = "ami-0a40ed7e422ec3f7b"
  instance_type = "t3.micro" # 2cpu, 1G mem, 30GiB EBS, 750 hours
  key_name      = "${var.project}_pair"
  user_data     = base64encode(templatefile("${path.module}/user-data.sh", {ecs_cluster_name = var.ecs_cluster_name}))
  iam_instance_profile {
    arn = var.ecs_instance_profile_arn
  }
  network_interfaces {
    associate_public_ip_address = true
    security_groups             = [var.security_group]
  }

}

resource "aws_autoscaling_group" "ecs-autoscaling-group" {
  depends_on = [var.ecs_cluster, aws_launch_template.ecs-launch-template, aws_alb.ecs_load_balancer]
  name                        = "${var.project}_autoscaling-group"
  max_size                    = "1"
  min_size                    = "1"
  desired_capacity            = "1"
  vpc_zone_identifier         = [var.pubSN1, var.pubSN2, var.pubSN3]
  health_check_type           = "EC2"
  launch_template {
    id = aws_launch_template.ecs-launch-template.id
    version = "$Latest"
  }

  tag {
    key                 = "AmazonECSManaged"
    value               = true
    propagate_at_launch = true
  }
}

output "ecs_autoscaling_group_arn" {
  value = aws_autoscaling_group.ecs-autoscaling-group.arn
}

output "ecs_autoscaling_group" {
  value = aws_autoscaling_group.ecs-autoscaling-group
}