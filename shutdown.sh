#!/usr/bin/env bash
set -uo pipefail

ROOT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
PID_DIR="$ROOT_DIR/.run"
services=(web admin api cms)

for service in "${services[@]}"; do
  pid_file="$PID_DIR/$service.pid"
  [[ -f "$pid_file" ]] || continue
  pid="$(cat "$pid_file")"
  if [[ "$pid" =~ ^[0-9]+$ ]] && kill -0 "$pid" 2>/dev/null; then
    kill "$pid" 2>/dev/null || true
    for _ in {1..10}; do
      kill -0 "$pid" 2>/dev/null || break
      sleep 1
    done
    kill -9 "$pid" 2>/dev/null || true
    printf 'Stopped %s (pid %s)\n' "$service" "$pid"
  fi
  rm -f "$pid_file"
done
