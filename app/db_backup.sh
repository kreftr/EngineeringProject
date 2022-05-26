#!/bin/bash

# $1 <- path to database backup folder

docker_container=$(docker ps -f "name=app_db_1" --format '{{.ID}}')

docker exec -t "${docker_container}" pg_dumpall -c -U admin > "$1"//dump_"$(date +%d-%m-%Y"_"%H_%M_%S)".sql