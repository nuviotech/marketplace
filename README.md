# Marketplace

This repository starts the storefront, Strapi CMS, marketplace API, and marketplace admin against one local MySQL database. The Windows and Linux launchers build the checked-in Java projects, install and build the Node projects, start all four services, and wait for their HTTP endpoints to respond.

## Service URLs

| Service | URL | Runtime |
| --- | --- | --- |
| Storefront | http://localhost:3003 | Next.js, Node 16 |
| CMS | http://localhost:1337 | Strapi 3, Node 14 |
| Marketplace API | http://localhost:8084 | Spring Boot, Java 21+ |
| Marketplace admin | http://localhost:9091 | Spring Boot, Java 21+ |

The API and admin share the `marketplace` MySQL database. The storefront connects to the API, admin, and CMS above. Strapi uses SQLite under `marketplace-cms`.

## Windows (PowerShell)

In an elevated PowerShell window, install Java 21, MySQL Server, and nvm-windows:

```powershell
winget install --id EclipseAdoptium.Temurin.21.JDK -e
winget install --id Oracle.MySQL -e
winget install --id CoreyButler.NVMforWindows -e
```

Finish MySQL Server configuration in its installer and note the root password. If the installer does not add MySQL Server's `bin` directory to `PATH`, add it so `mysql.exe` and `mysqladmin.exe` are available. Close and reopen PowerShell after installation. The launcher installs the needed Node 14 and Node 16 versions and Yarn 1 using nvm-windows.

Start the MySQL Windows service from an elevated PowerShell window. The service name varies by MySQL version; find it with:

```powershell
Get-Service *mysql*
```

Start the service (replace `MySQL80` with the service name shown above):

```powershell
Start-Service MySQL80
```

Then create the local app account (enter the MySQL root password when prompted):

```powershell
mysql.exe -u root -p -e "CREATE USER IF NOT EXISTS 'marketplace'@'localhost' IDENTIFIED BY 'marketplace'; CREATE USER IF NOT EXISTS 'marketplace'@'127.0.0.1' IDENTIFIED BY 'marketplace'; GRANT ALL PRIVILEGES ON marketplace.* TO 'marketplace'@'localhost'; GRANT ALL PRIVILEGES ON marketplace.* TO 'marketplace'@'127.0.0.1'; FLUSH PRIVILEGES;"
```

Open a new PowerShell window, go to the project root, and start all services:

```powershell
cd $env:USERPROFILE\marketplace
Set-ExecutionPolicy -Scope Process Bypass
.\startup.ps1
```

If nvm-windows cannot switch Node versions because it cannot update its symlink, reopen PowerShell as Administrator and run the startup command again. The first run downloads dependencies and builds the applications; it may take several minutes. Logs are written to `log\marketplace-<service>.out.log` and `.err.log`.

Stop or restart the services with:

```powershell
.\shutdown.ps1
# or
.\restart.ps1
```

If your MySQL credentials differ from the defaults, set these before starting:

```powershell
$env:MYSQL_HOST = '127.0.0.1'
$env:MYSQL_PORT = '3306'
$env:MYSQL_DATABASE = 'marketplace'
$env:MYSQL_USER = 'marketplace'
$env:MYSQL_PASSWORD = 'marketplace'
.\startup.ps1
```

## Linux or Ubuntu on WSL

Install prerequisites and nvm:

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk mysql-server git curl unzip iproute2
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.5/install.sh | bash
```

Close and reopen the terminal, start MySQL, then create the database account:

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

From the repository root, start the applications:

```bash
bash startup.sh
```

Stop or restart them with `bash shutdown.sh` or `bash restart.sh`.

## First-run notes

Open the storefront at http://localhost:3003. Create a Strapi administrator at http://localhost:1337/admin. The API/admin database starts empty, so add catalog and admin data through the CMS and admin interface. The service ports are fixed because the current storefront source uses those same addresses for server-side connections.

If a service does not become ready, inspect its log in `log/`. Check that MySQL is running and that ports 3003, 1337, 8084, and 9091 are free. The first run needs network access for Maven, Node, and Yarn dependencies.
