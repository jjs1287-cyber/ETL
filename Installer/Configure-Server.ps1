$ErrorActionPreference = 'Stop'
$installRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$configuration = Join-Path $env:ProgramData 'ETLForge\default\configuration\installation.json'
if (!(Test-Path -LiteralPath $configuration)) {
    throw 'The ETLForge Server installation metadata was not found. Run Repair from Apps & features.'
}
$settings = Get-Content -Raw -LiteralPath $configuration | ConvertFrom-Json
Write-Host 'ETLForge Server configuration' -ForegroundColor Cyan
Write-Host "Application: $($settings.applicationRoot)"
Write-Host "Data:        $($settings.dataRoot)"
Write-Host "Logs:        $($settings.logRoot)"
Write-Host ''
Write-Warning 'This development installer lays down the verified x64 runtime and directory structure. Fresh database provisioning and Windows service registration are not enabled until the native configuration helper is completed.'
Write-Host 'The existing development server in C:\ETL is unchanged.'
Read-Host 'Press Enter to close'
