$ErrorActionPreference = 'Continue'
$runDir = Join-Path $PSScriptRoot '.run'

foreach ($service in @('web', 'admin', 'api', 'cms')) {
    $pidFile = Join-Path $runDir "$service.pid"
    if (-not (Test-Path $pidFile)) { continue }

    $processId = 0
    if ([int]::TryParse((Get-Content $pidFile -Raw).Trim(), [ref]$processId)) {
        & taskkill.exe /PID $processId /T /F 2>$null | Out-Null
        Write-Host "Stopped $service (PID $processId)"
    }
    Remove-Item -LiteralPath $pidFile -Force -ErrorAction SilentlyContinue
}
