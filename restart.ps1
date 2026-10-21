$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
& (Join-Path $root 'shutdown.ps1')
Start-Sleep -Seconds 2
& (Join-Path $root 'startup.ps1')
