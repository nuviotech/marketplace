#!/usr/bin/env bash
set -Eeuo pipefail

ROOT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
LOG_DIR="$ROOT_DIR/log"
PID_DIR="$ROOT_DIR/.run"

MYSQL_HOST="${MYSQL_HOST:-127.0.0.1}"
MYSQL_PORT="${MYSQL_PORT:-3306}"
MYSQL_DATABASE="${MYSQL_DATABASE:-marketplace}"
MYSQL_USER="${MYSQL_USER:-marketplace}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-marketplace}"

# These ports match the URLs used by the current service and storefront source.
API_PORT=8084
ADMIN_PORT=9091
CMS_PORT=1337
WEB_PORT=3003

fail() { printf 'ERROR: %s\n' "$*" >&2; exit 1; }
require_command() { command -v "$1" >/dev/null 2>&1 || fail "Required command '$1' was not found."; }

require_command java
require_command curl
require_command ss
require_command mysql
require_command mysqladmin

JAVA_MAJOR="$(java -version 2>&1 | awk -F '[".]' '/version/ { if ($2 == "1") print $3; else print $2; exit }')"
[[ "${JAVA_MAJOR:-0}" -ge 21 ]] || fail "Java 21 or newer is required by the current Spring Boot projects (found Java ${JAVA_MAJOR:-unknown})."

if [[ -n "${NVM_DIR:-}" && -s "$NVM_DIR/nvm.sh" ]]; then
  # shellcheck source=/dev/null
  source "$NVM_DIR/nvm.sh"
elif [[ -s "$HOME/.nvm/nvm.sh" ]]; then
  # shellcheck source=/dev/null
  source "$HOME/.nvm/nvm.sh"
fi

command -v nvm >/dev/null 2>&1 || fail "nvm is required to select Node 14 for Strapi and Node 16 for Next.js. Install nvm as described in README.md."

mkdir -p "$LOG_DIR" "$PID_DIR"

if ! mysqladmin ping -h "$MYSQL_HOST" -P "$MYSQL_PORT" --silent >/dev/null 2>&1; then
  fail "MySQL is not accepting connections at ${MYSQL_HOST}:${MYSQL_PORT}. Start MySQL and check MYSQL_HOST/MYSQL_PORT."
fi

MYSQL_PWD="$MYSQL_PASSWORD" mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" \
  -e "CREATE DATABASE IF NOT EXISTS \`$MYSQL_DATABASE\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" \
  || fail "Could not create/access database '$MYSQL_DATABASE' as '$MYSQL_USER'. Check MYSQL_* settings."

for port in "$API_PORT" "$ADMIN_PORT" "$CMS_PORT" "$WEB_PORT"; do
  [[ -z "$(ss -H -ltn "sport = :$port")" ]] || fail "Port $port is already in use. Stop its process before starting the marketplace."
done

start_background() {
  local name="$1" workdir="$2" logfile="$3"
  shift 3
  (
    cd "$workdir"
    nohup "$@" >>"$logfile" 2>&1 </dev/null &
    echo $! >"$PID_DIR/$name.pid"
  )
  printf 'Started %-5s (pid %s; log %s)\n' "$name" "$(cat "$PID_DIR/$name.pid")" "$logfile"
}

wait_for_http() {
  local name="$1" url="$2" pid_file="$PID_DIR/$name.pid" status="" attempt
  for attempt in $(seq 1 60); do
    if [[ -f "$pid_file" ]] && ! kill -0 "$(cat "$pid_file")" 2>/dev/null; then
      fail "$name exited during startup. See $LOG_DIR/marketplace-$name.log."
    fi
    status="$(curl --silent --output /dev/null --write-out '%{http_code}' --max-time 2 "$url" || true)"
    if [[ "$status" =~ ^[234][0-9][0-9]$ ]]; then
      printf 'Ready %-5s at %s (HTTP %s)\n' "$name" "$url" "$status"
      return 0
    fi
    sleep 2
  done
  fail "$name did not become ready at $url. See $LOG_DIR/marketplace-$name.log."
}

printf 'Building marketplace API and admin with the current checked-in source...\n'
(cd "$ROOT_DIR/marketplace-api" && bash ./mvnw -DskipTests package)
(cd "$ROOT_DIR/marketplace-admin" && bash ./mvnw -DskipTests package)

API_JAR="$(find "$ROOT_DIR/marketplace-api/target" -maxdepth 1 -type f -name '*.jar' ! -name '*-original.jar' | head -n 1)"
ADMIN_JAR="$(find "$ROOT_DIR/marketplace-admin/target" -maxdepth 1 -type f -name '*.jar' ! -name '*-original.jar' | head -n 1)"
[[ -n "$API_JAR" ]] || fail "Marketplace API build did not produce a jar."
[[ -n "$ADMIN_JAR" ]] || fail "Marketplace admin build did not produce a jar."

if [[ -f "$HOME/.nvm/nvm.sh" || -n "${NVM_DIR:-}" ]]; then
  nvm install 14.21.3
  nvm use 14.21.3
else
  fail "nvm is required to run Strapi with Node 14."
fi
require_command npm
[[ "$(node -p 'process.versions.node.split(".")[0]')" == 14 ]] || fail "Could not select Node 14 for Strapi."
npm install --global yarn@1.22.22
(cd "$ROOT_DIR/marketplace-cms" && yarn install --frozen-lockfile && yarn build)
start_background cms "$ROOT_DIR/marketplace-cms" "$LOG_DIR/marketplace-cms.log" \
  env HOST=0.0.0.0 PORT="$CMS_PORT" yarn start
wait_for_http cms "http://127.0.0.1:$CMS_PORT/admin"

nvm install 16.20.2
nvm use 16.20.2
[[ "$(node -p 'process.versions.node.split(".")[0]')" == 16 ]] || fail "Could not select Node 16 for Next.js."
npm install --global yarn@1.22.22
export NEXT_PUBLIC_STRAPI_PANEL_URL="http://localhost:$CMS_PORT"
export NEXT_PUBLIC_MARKETPLACE_API_BASE_URL="http://localhost:$API_PORT"
export NEXT_PUBLIC_MARKETPLACE_ADMIN_BASE_URL="http://localhost:$ADMIN_PORT"
(cd "$ROOT_DIR/marketplace-web" && yarn install --frozen-lockfile && yarn build)

start_background api "$ROOT_DIR" "$LOG_DIR/marketplace-api.log" \
  java -jar "$API_JAR" \
  "--server.port=$API_PORT" \
  "--spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DATABASE}" \
  "--spring.datasource.username=$MYSQL_USER" "--spring.datasource.password=$MYSQL_PASSWORD" \
  "--marketplace.web.base.url=http://localhost:$WEB_PORT" \
  "--spring.web.cors.allowed-origins=http://localhost:$WEB_PORT,http://127.0.0.1:$WEB_PORT" \
  "--spring.web.cors.allowed-origins1=http://localhost:$WEB_PORT,http://127.0.0.1:$WEB_PORT"
wait_for_http api "http://127.0.0.1:$API_PORT/"
start_background admin "$ROOT_DIR" "$LOG_DIR/marketplace-admin.log" \
  java -jar "$ADMIN_JAR" \
  "--server.port=$ADMIN_PORT" \
  "--spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DATABASE}" \
  "--spring.datasource.username=$MYSQL_USER" "--spring.datasource.password=$MYSQL_PASSWORD" \
  "--marketplace.api.url=http://localhost:$API_PORT" \
  "--website.url=http://localhost:$ADMIN_PORT" \
  "--manageEcom.api.url=https://manageecom.com:8001"
wait_for_http admin "http://127.0.0.1:$ADMIN_PORT/login"
start_background web "$ROOT_DIR/marketplace-web" "$LOG_DIR/marketplace-web.log" \
  env PORT="$WEB_PORT" NEXT_PUBLIC_STRAPI_PANEL_URL="http://localhost:$CMS_PORT" \
  NEXT_PUBLIC_MARKETPLACE_API_BASE_URL="http://localhost:$API_PORT" \
  NEXT_PUBLIC_MARKETPLACE_ADMIN_BASE_URL="http://localhost:$ADMIN_PORT" \
  ./node_modules/.bin/next start -p "$WEB_PORT"
wait_for_http web "http://127.0.0.1:$WEB_PORT/"

printf '\nMarketplace services launched. See README.md for URLs, logs, and shutdown.\n'
