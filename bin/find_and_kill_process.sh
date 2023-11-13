#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: $0 <port>"
  exit 1
fi

port=$1

pid=$(lsof -t -i :$port)

if [ -n "$pid" ]; then
  echo "Process with port $port is running with PID $pid."
  kill "$pid"
  echo "Process with PID $pid has been killed."
else
  echo "No process found running on port $port."
fi

