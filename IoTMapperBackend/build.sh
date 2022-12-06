#!/bin/bash
./mvnw clean package
sudo docker build . -f docker/Dockerfile -t vitorinojp/iotmapperbackend