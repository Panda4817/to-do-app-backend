#!/bin/bash

docker rm -f dev-mongo dev-postgres
docker volume prune -f