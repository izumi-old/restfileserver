#!/bin/bash
docker stop restfileserver
docker run --name restfileserver -p 40045:40045 -d restfileserver
