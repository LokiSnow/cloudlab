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