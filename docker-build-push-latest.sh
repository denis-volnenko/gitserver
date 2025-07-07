#!/bin/sh

docker build -t volnenko/gitserver:latest .

docker push volnenko/gitserver:latest