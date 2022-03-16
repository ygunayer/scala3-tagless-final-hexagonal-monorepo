#!/usr/bin/env sh

echo "This tool removes the entire Docker Compose stack in the current directory."
echo "This means that all data contained in the volumes created by this stack will be wiped."
echo "Also, all orphaned Docker networks and volumes will also be pruned, even if they're outside of this stack"
read -p "Are you sure you want to continue? (y/N) " answer
case "$answer" in
    [Yy]* ) docker-compose down && docker-compose rm && docker volume prune -f && docker network prune -f;;
    * ) echo "Cancelled";;
esac
