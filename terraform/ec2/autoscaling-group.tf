resource "aws_launch_template" "ecs-launch-template" {
  name_prefix   = var.project
  image_id      = "ami-0eeadc4ab092fef70"
  instance_type = "t3.micro" # 2cpu, 1G mem, 30GiB EBS, 750 hours
  key_name      = "${var.project}_pair"
  user_data     = filebase64("${path.module}/user-data.sh")
  iam_instance_profile {
    name = var.ecs_instance_profile_name
  }
  network_interfaces {
    associate_public_ip_address = true
    security_groups             = [var.security_group]
  }
}

resource "aws_autoscaling_group" "ecs-autoscaling-group" {
  name                        = "${var.project}_autoscaling-group"
  max_size                    = "2"
  min_size                    = "1"
  desired_capacity            = "2"
  vpc_zone_identifier         = [var.pubSN1, var.pubSN2, var.pubSN3]
  health_check_type           = "ELB"
  launch_template {
    id = aws_launch_template.ecs-launch-template.id
    version = "$Latest"
  }
}
