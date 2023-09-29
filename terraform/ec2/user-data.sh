#!/bin/bash
mkdir /etc/ecs
echo ECS_CLUSTER=${ecs_cluster_name} >> /etc/ecs/ecs.config
echo ECS_DISABLE_PRIVILEGED=true >> /etc/ecs/ecs.config