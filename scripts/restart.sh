#!/bin/bash

set -e

if [ $# -eq 0 ]; then
    echo "Please specify at least one service name"
    exit -1
fi

docker-compose kill "$@"
docker-compose rm -f "$@"
docker-compose up -d "$@
