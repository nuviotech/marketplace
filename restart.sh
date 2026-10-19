#!/usr/bin/env bash
set -Eeuo pipefail

ROOT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
bash "$ROOT_DIR/shutdown.sh"
sleep 2
exec bash "$ROOT_DIR/startup.sh" "$@"
