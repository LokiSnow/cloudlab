resource "aws_autoscaling_group" "ecs-autoscaling-group" {
  name                        = "${var.project}_autoscaling-group"
  max_size                    = "2"
  min_size                    = "1"
  desired_capacity            = "2"
  vpc_zone_identifier         = [var.pubSN1, var.pubSN2, var.pubSN3]
  launch_configuration        = aws_launch_configuration.ecs-launch-configuration.name
  health_check_type           = "ELB"
}

resource "aws_launch_configuration" "ecs-launch-configuration" {
  name                        = "${var.project}_launch-configuration"
  image_id                    = "ami-0eeadc4ab092fef70"
  instance_type               = "t3.micro" # 2cpu, 1G mem, 30GiB EBS, 750 hours
  iam_instance_profile        = var.ecs_instance_profile_name
  security_groups             = [var.security_group]
  associate_public_ip_address = "true"
  key_name                    = "${var.project}_pair"
}

