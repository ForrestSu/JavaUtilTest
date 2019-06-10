#!/bin/bash
export factor=192.168.8.192:5000/fms/factor
docker pull $factor
docker-compose -f docker-compose-factor.yml -p fms up -d
