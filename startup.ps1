$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
$logDir = Join-Path $root 'log'
$runDir = Join-Path $root '.run'
$dbHost = if ($env:MYSQL_HOST) { $env:MYSQL_HOST } else { '127.0.0.1' }
$dbPort = if ($env:MYSQL_PORT) { [int]$env:MYSQL_PORT } else { 3306 }
$dbName = if ($env:MYSQL_DATABASE) { $env:MYSQL_DATABASE } else { 'marketplace' }
$dbUser = if ($env:MYSQL_USER) { $env:MYSQL_USER } else { 'marketplace' }
$dbPassword = if ($env:MYSQL_PASSWORD) { $env:MYSQL_PASSWORD } else { 'marketplace' }
$ports = @(8084, 9091, 1337, 3003)

function Stop-WithError([string]$message) {
    throw $message
}

function Get-NodeVersionDirectory([string]$version) {
    $nvmHome = $env:NVM_HOME
    if (-not $nvmHome) {
        $nvmHome = Join-Path $env:APPDATA 'nvm'
    }
    $directory = Join-Path $nvmHome "v$version"
    if (-not (Test-Path (Join-Path $directory 'node.exe'))) {
        Stop-WithError "Node $version was not found at $directory. Install it with nvm-windows and rerun this script."
    }
    return $directory
}

function Start-LoggedCommand([string]$name, [string]$workingDirectory, [string]$command, [string]$nodeDirectory = '') {
    $oldPath = $env:PATH
    if ($nodeDirectory) {
        $env:PATH = "$nodeDirectory;$env:APPDATA\npm;$oldPath"
    }
    try {
        $process = Start-Process -FilePath $env:ComSpec `
            -ArgumentList @('/d', '/c', $command) `
            -WorkingDirectory $workingDirectory `
            -RedirectStandardOutput (Join-Path $logDir "marketplace-$name.out.log") `
            -RedirectStandardError (Join-Path $logDir "marketplace-$name.err.log") `
            -WindowStyle Hidden -PassThru
    }
    finally {
        $env:PATH = $oldPath
    }
    Set-Content -Path (Join-Path $runDir "$name.pid") -Value $process.Id
    Write-Host "Started $name (PID $($process.Id)); logs: log/marketplace-$name.out.log and .err.log"
    return $process
}

function Wait-ForHttp([string]$name, [string]$url, $process) {
    for ($attempt = 0; $attempt -lt 60; $attempt++) {
        $process.Refresh()
        if ($process.HasExited) {
            Stop-WithError "$name exited during startup. See log/marketplace-$name.err.log."
        }
        try {
            $response = Invoke-WebRequest -Uri $url -TimeoutSec 2 -UseBasicParsing
            if ([int]$response.StatusCode -lt 500) {
                Write-Host "Ready $name at $url (HTTP $($response.StatusCode))"
                return
            }
        }
        catch {
            $statusCode = 0
            if ($_.Exception.Response) {
                $statusCode = [int]$_.Exception.Response.StatusCode
            }
            if ($statusCode -ge 400 -and $statusCode -lt 500) {
                Write-Host "Ready $name at $url (HTTP $statusCode)"
                return
            }
        }
        Start-Sleep -Seconds 2
    }
    Stop-WithError "$name did not become ready at $url. See log/marketplace-$name.err.log."
}

New-Item -ItemType Directory -Force -Path $logDir, $runDir | Out-Null

foreach ($command in @('java', 'mysql.exe', 'mysqladmin.exe', 'nvm.exe')) {
    if (-not (Get-Command $command -ErrorAction SilentlyContinue)) {
        Stop-WithError "Required command '$command' was not found. Install the Windows prerequisites listed in README.md."
    }
}

$javaVersion = (& java -version 2>&1 | Select-Object -First 1)
if ($javaVersion -notmatch '"(\d+)') {
    Stop-WithError 'Could not determine the Java version.'
}
if ([int]$Matches[1] -lt 21) {
    Stop-WithError "Java 21 or newer is required (found $javaVersion)."
}

$mysqlPasswordBefore = $env:MYSQL_PWD
$env:MYSQL_PWD = $dbPassword
try {
    & mysqladmin.exe -h $dbHost -P $dbPort --silent ping | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Stop-WithError "MySQL is not accepting connections at ${dbHost}:${dbPort}. Start the MySQL service and check MYSQL_* settings."
    }
    if ($dbName -notmatch '^[A-Za-z0-9_]+$') {
        Stop-WithError 'MYSQL_DATABASE may contain only letters, numbers, and underscores.'
    }
    & mysql.exe -h $dbHost -P $dbPort -u $dbUser -e "CREATE DATABASE IF NOT EXISTS ``$dbName`` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    if ($LASTEXITCODE -ne 0) {
        Stop-WithError "Could not create/access database '$dbName' as '$dbUser'. Check MYSQL_* settings."
    }
}
finally {
    $env:MYSQL_PWD = $mysqlPasswordBefore
}

foreach ($port in $ports) {
    if (Get-NetTCPConnection -State Listen -LocalPort $port -ErrorAction SilentlyContinue) {
        Stop-WithError "Port $port is already in use. Stop its process before starting the marketplace."
    }
}

Write-Host 'Building marketplace API and admin from the checked-in source...'
Push-Location (Join-Path $root 'marketplace-api')
try {
    & .\mvnw.cmd -DskipTests package
    if ($LASTEXITCODE -ne 0) { Stop-WithError 'Marketplace API build failed.' }
}
finally { Pop-Location }

Push-Location (Join-Path $root 'marketplace-admin')
try {
    & .\mvnw.cmd -DskipTests package
    if ($LASTEXITCODE -ne 0) { Stop-WithError 'Marketplace admin build failed.' }
}
finally { Pop-Location }

$apiJar = Get-ChildItem (Join-Path $root 'marketplace-api\target') -Filter '*.jar' -File |
    Where-Object { $_.Name -notlike '*-original.jar' } | Select-Object -First 1
$adminJar = Get-ChildItem (Join-Path $root 'marketplace-admin\target') -Filter '*.jar' -File |
    Where-Object { $_.Name -notlike '*-original.jar' } | Select-Object -First 1
if (-not $apiJar -or -not $adminJar) { Stop-WithError 'The Java builds did not produce both service jars.' }

& nvm.exe install 14.21.3 64
if ($LASTEXITCODE -ne 0) { Stop-WithError 'Could not install Node 14.21.3 using nvm-windows.' }
& nvm.exe use 14.21.3
if ($LASTEXITCODE -ne 0) { Stop-WithError 'Could not select Node 14.21.3 using nvm-windows.' }
$node14 = Get-NodeVersionDirectory '14.21.3'
$env:PATH = "$node14;$env:APPDATA\npm;$env:PATH"
& npm.cmd install --global yarn@1.22.22
if ($LASTEXITCODE -ne 0) { Stop-WithError 'Could not install Yarn for Node 14.' }
Push-Location (Join-Path $root 'marketplace-cms')
try {
    & yarn.cmd install --frozen-lockfile
    if ($LASTEXITCODE -ne 0) { Stop-WithError 'CMS dependency installation failed.' }
    & yarn.cmd build
    if ($LASTEXITCODE -ne 0) { Stop-WithError 'CMS build failed.' }
}
finally { Pop-Location }
$cms = Start-LoggedCommand 'cms' (Join-Path $root 'marketplace-cms') 'yarn.cmd start' $node14
Wait-ForHttp 'cms' 'http://127.0.0.1:1337/admin' $cms

& nvm.exe install 16.20.2 64
if ($LASTEXITCODE -ne 0) { Stop-WithError 'Could not install Node 16.20.2 using nvm-windows.' }
& nvm.exe use 16.20.2
if ($LASTEXITCODE -ne 0) { Stop-WithError 'Could not select Node 16.20.2 using nvm-windows.' }
$node16 = Get-NodeVersionDirectory '16.20.2'
$env:PATH = "$node16;$env:APPDATA\npm;$env:PATH"
& npm.cmd install --global yarn@1.22.22
if ($LASTEXITCODE -ne 0) { Stop-WithError 'Could not install Yarn for Node 16.' }
$env:NEXT_PUBLIC_STRAPI_PANEL_URL = 'http://localhost:1337'
$env:NEXT_PUBLIC_MARKETPLACE_API_BASE_URL = 'http://localhost:8084'
$env:NEXT_PUBLIC_MARKETPLACE_ADMIN_BASE_URL = 'http://localhost:9091'
Push-Location (Join-Path $root 'marketplace-web')
try {
    & yarn.cmd install --frozen-lockfile
    if ($LASTEXITCODE -ne 0) { Stop-WithError 'Storefront dependency installation failed.' }
    & yarn.cmd build
    if ($LASTEXITCODE -ne 0) { Stop-WithError 'Storefront build failed.' }
}
finally { Pop-Location }

$apiConfig = @{
    server = @{ port = 8084 }
    spring = @{
        datasource = @{
            url = "jdbc:mysql://${dbHost}:${dbPort}/${dbName}"
            username = $dbUser
            password = $dbPassword
        }
        web = @{
            cors = @{
                'allowed-origins' = 'http://localhost:3003,http://127.0.0.1:3003'
                'allowed-origins1' = 'http://localhost:3003,http://127.0.0.1:3003'
            }
        }
    }
    marketplace = @{ web = @{ 'base.url' = 'http://localhost:3003' } }
} | ConvertTo-Json -Depth 8 -Compress
$adminConfig = @{
    server = @{ port = 9091 }
    spring = @{
        datasource = @{
            url = "jdbc:mysql://${dbHost}:${dbPort}/${dbName}"
            username = $dbUser
            password = $dbPassword
        }
    }
    marketplace = @{ api = @{ url = 'http://localhost:8084' } }
    website = @{ url = 'http://localhost:9091' }
} | ConvertTo-Json -Depth 8 -Compress

$oldSpringJson = $env:SPRING_APPLICATION_JSON
try {
    $env:SPRING_APPLICATION_JSON = $apiConfig
    $api = Start-LoggedCommand 'api' $root "java -jar `"$($apiJar.FullName)`""
    Wait-ForHttp 'api' 'http://127.0.0.1:8084/' $api
    $env:SPRING_APPLICATION_JSON = $adminConfig
    $admin = Start-LoggedCommand 'admin' $root "java -jar `"$($adminJar.FullName)`""
    Wait-ForHttp 'admin' 'http://127.0.0.1:9091/login' $admin
}
finally {
    $env:SPRING_APPLICATION_JSON = $oldSpringJson
}

$web = Start-LoggedCommand 'web' (Join-Path $root 'marketplace-web') '.\node_modules\.bin\next.cmd start -p 3003' $node16
Wait-ForHttp 'web' 'http://127.0.0.1:3003/' $web

Write-Host ''
Write-Host 'All marketplace services are responding:'
Write-Host '  Storefront: http://localhost:3003'
Write-Host '  CMS:        http://localhost:1337/admin'
Write-Host '  API:        http://localhost:8084'
Write-Host '  Admin:      http://localhost:9091/login'
