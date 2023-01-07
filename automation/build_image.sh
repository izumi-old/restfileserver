#!/bin/bash
docker rmi restfileserver
docker build -t restfileserver ../
