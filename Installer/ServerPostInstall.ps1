param(
    [Parameter(Mandatory=$true)][string]$InstallRoot,
    [Parameter(Mandatory=$true)][string]$DataRoot,
    [Parameter(Mandatory=$true)][string]$LogRoot
)
$ErrorActionPreference = 'Stop'
$paths = @(
    $DataRoot,
    (Join-Path $DataRoot 'configuration'),
    (Join-Path $DataRoot 'private'),
    (Join-Path $DataRoot 'data'),
    (Join-Path $DataRoot 'temp'),
    $LogRoot,
    (Join-Path $LogRoot 'apache'),
    (Join-Path $LogRoot 'tomcat'),
    (Join-Path $LogRoot 'postgresql'),
    (Join-Path $LogRoot 'pgbouncer'),
    (Join-Path $LogRoot 'watchdog'),
    (Join-Path $LogRoot 'services')
)
foreach ($path in $paths) { New-Item -ItemType Directory -Force -Path $path | Out-Null }

$metadata = [ordered]@{
    product = 'ETLForge Server'
    architecture = 'x64'
    applicationRoot = $InstallRoot
    dataRoot = $DataRoot
    logRoot = $LogRoot
    gatewayPort = 47100
    tomcatAddress = '127.0.0.1'
    tomcatPort = 47112
    pgbouncerPort = 5432
    pgbouncerMaxClientConnections = 400
    postgresqlPort = 6543
    postgresqlMaxConnections = 410
    databaseReconnectAttempts = 10
    databaseReconnectDelaySeconds = 30
    watchdogIntervalSeconds = 20
    watchdogFailureThreshold = 3
    watchdogRestartCooldownSeconds = 120
    configurationState = 'Files installed; complete instance provisioning before starting services.'
}
$metadata | ConvertTo-Json | Set-Content -LiteralPath (Join-Path $DataRoot 'configuration\installation.json') -Encoding UTF8

$acl = Get-Acl -LiteralPath $DataRoot
$rule = New-Object System.Security.AccessControl.FileSystemAccessRule('BUILTIN\Users','ReadAndExecute','ContainerInherit,ObjectInherit','None','Allow')
$acl.SetAccessRule($rule)
Set-Acl -LiteralPath $DataRoot -AclObject $acl

Write-Output 'ETLForge Server program files and writable directory structure installed.'
Write-Output 'Database initialization, credentials, certificates, and Windows service registration intentionally require the Server Configuration step.'
