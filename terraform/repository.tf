resource "aws_ecr_repository" "repo" {
  name                 = var.resource_tags["project"]
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}