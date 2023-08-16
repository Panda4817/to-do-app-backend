#!/bin/bash

docker run -d -p 27017:27017 --name dev-mongo -v mongo-data:/data/db mongo
docker run -d -p 5432:5432 --name dev-postgres -v postgres-data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres postgres