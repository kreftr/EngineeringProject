#!/bin/bash

# $1 <- docker container
# $2 <- path to database backup folder


docker exec -t "$1" pg_dumpall -c -U admin > "$2"//dump_"$(date +%d-%m-%Y"_"%H_%M_%S)".sql