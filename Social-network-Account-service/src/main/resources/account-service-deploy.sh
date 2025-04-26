#!/bin/bash
IMAGE_NAME="account-service"
CONTAINER_NAME="account-service"

# Останавливаем контейнер, если он работает
docker stop $CONTAINER_NAME || true

# Удаляем контейнер (если есть)
docker rm -f $CONTAINER_NAME || true

# Удаляем все образы с этим именем, независимо от тега
docker images | awk -v image="$IMAGE_NAME" '$1 == image { print $3 }' | xargs -r docker rmi

# Запускаем новый контейнер (с latest или нужным тегом)
docker run --name $CONTAINER_NAME --network infrastructure_app-network $IMAGE_NAME:latest