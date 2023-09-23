resource "aws_subnet" "pubSN1" {
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone = "ap-southeast-1a"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "pubSN2" {
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.2.0/24"
  availability_zone = "ap-southeast-1b"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "pubSN3" {
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.3.0/24"
  availability_zone = "ap-southeast-1c"
  map_public_ip_on_launch = true
}

output "pubSN1" {
  value = aws_subnet.pubSN1.id
}

output "pubSN2" {
  value = aws_subnet.pubSN2.id
}

output "pubSN3" {
  value = aws_subnet.pubSN3.id
}