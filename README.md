# Marketplace

This repository runs the storefront, Strapi CMS, marketplace API, and marketplace admin against one local MySQL database. The root scripts build the checked-in Java projects, install/build the two Node projects, and start all four services with matching local URLs.

## Services

| Service | URL | Runtime |
| --- | --- | --- |
| Storefront | http://localhost:3003 | Next.js, Node 16 |
| CMS | http://localhost:1337 | Strapi 3, Node 14 |
| Marketplace API | http://localhost:8084 | Spring Boot, Java 21+ |
| Marketplace admin | http://localhost:9091 | Spring Boot, Java 21+ |

The API and admin share the `marketplace` MySQL database. The storefront uses the API, admin, and CMS URLs above. Strapi keeps its configured SQLite database and uploaded files under `marketplace-cms`.

## Prerequisites

Use Ubuntu/Linux with MySQL, Java 21 or newer, Git, `curl`, `unzip`, and `nvm`. The launcher selects Node 14.21.3 for Strapi 3 and Node 16.20.2 for Next.js, and installs Yarn 1 for each version. Install nvm for the account that will run the services. For example:

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk mysql-server git curl unzip iproute2
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.5/install.sh | bash
```

Start MySQL and create the database account used by default:

```bash
sudo systemctl enable --now mysql
sudo mysql <<'SQL'
CREATE USER IF NOT EXISTS 'marketplace'@'localhost' IDENTIFIED BY 'marketplace';
CREATE USER IF NOT EXISTS 'marketplace'@'127.0.0.1' IDENTIFIED BY 'marketplace';
GRANT ALL PRIVILEGES ON marketplace.* TO 'marketplace'@'localhost';
GRANT ALL PRIVILEGES ON marketplace.* TO 'marketplace'@'127.0.0.1';
FLUSH PRIVILEGES;
SQL
```

The launcher creates the `marketplace` database. To use another MySQL account, export the matching `MYSQL_*` values before startup.

## Start all services

From the repository root, configure the database connection if the defaults do not match your MySQL setup, then run:

```bash
export MYSQL_HOST=127.0.0.1
export MYSQL_PORT=3306
export MYSQL_DATABASE=marketplace
export MYSQL_USER=marketplace
export MYSQL_PASSWORD=marketplace
bash startup.sh
```

`startup.sh` checks MySQL and required ports, creates the database if needed, packages the current API and admin sources with tests skipped, installs/builds the CMS and storefront dependencies, starts all four processes, and waits for each HTTP endpoint to respond. Spring creates or updates its tables on startup. The first run needs network access for Maven, nvm, and Yarn dependencies and may take several minutes.

Open the storefront at http://localhost:3003. Strapi's first-run admin account can be created at http://localhost:1337/admin. Logs and process IDs are written under the root `log/` and `.run/` directories.

To use different MySQL credentials, export the corresponding `MYSQL_*` values before startup. Keep the service ports at the documented values because the current storefront source uses those ports for its server-side connections.

## Restart and stop

```bash
bash restart.sh
bash shutdown.sh
```

`shutdown.sh` stops the processes started by the root startup script. Run these scripts from the repository root or any directory; they resolve their own location.

## Troubleshooting

- Check the service logs in `log/` if a page does not load.
- If a service port is already occupied, stop the existing process or choose an available port before starting.
- If Maven or Yarn cannot resolve dependencies, check network access and rerun `bash startup.sh`.
- The API/admin database is created automatically, but it starts empty. Add catalog and admin data through the CMS and admin interface as needed.
