#!/bin/bash

ROOT_PATH="/home/ubuntu"
DEPLOY_PATH="/home/ubuntu/deploy"
JAR=$(find $ROOT_PATH -name "*.jar" | head -n 1)

STOP_LOG="$DEPLOY_PATH/stop.log"
SERVICE_PID=$(pgrep -f $JAR)

if [ -z "$SERVICE_PID" ]; then
  echo "Service not found" >> $STOP_LOG
else
  echo "Terminate service " >> $STOP_LOG
  kill -9 "$SERVICE_PID"
fi
