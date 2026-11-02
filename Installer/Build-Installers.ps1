[CmdletBinding()]
param([switch]$KeepStage)
$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

$etlRoot = Split-Path -Parent $PSScriptRoot
$versionFile = Join-Path $etlRoot 'product-version.json'
$outputRoot = Join-Path $etlRoot 'Installers'
$workRoot = Join-Path $etlRoot 'Installer\work'
$studioStage = Join-Path $workRoot 'studio'
$serverStage = Join-Path $workRoot 'server'
$isccCandidates = @(
    (Join-Path ${env:ProgramFiles(x86)} 'Inno Setup 6\ISCC.exe'),
    (Join-Path $env:LOCALAPPDATA 'Programs\Inno Setup 6\ISCC.exe')
)
$iscc = $isccCandidates | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1
if (!$iscc) { throw 'Inno Setup 6.7.3 or later is required.' }

$product = Get-Content -Raw -LiteralPath $versionFile | ConvertFrom-Json
$parts = @($product.version -split '\.')
if ($parts.Count -ne 4 -or ($parts | Where-Object { $_ -notmatch '^\d+$' })) { throw 'Version must have four numeric components.' }
$parts[3] = ([int]$parts[3] + 1).ToString()
$version = $parts -join '.'

Remove-Item -LiteralPath $workRoot -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -LiteralPath $outputRoot -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $studioStage,$serverStage,$outputRoot | Out-Null

function Copy-Tree([string]$Source,[string]$Destination) {
    if (!(Test-Path -LiteralPath $Source)) { throw "Missing payload source: $Source" }
    New-Item -ItemType Directory -Force -Path $Destination | Out-Null
    Copy-Item -Path (Join-Path $Source '*') -Destination $Destination -Recurse -Force
}

# Studio: compiled product only. User projects and development/build trees are never staged.
Copy-Tree (Join-Path $etlRoot 'Studio\configuration') (Join-Path $studioStage 'configuration')
Copy-Tree (Join-Path $etlRoot 'Studio\dist') (Join-Path $studioStage 'dist')
Copy-Tree (Join-Path $etlRoot 'Studio\lib') (Join-Path $studioStage 'lib')
Copy-Item -LiteralPath (Join-Path $PSScriptRoot 'StudioLauncher.cmd') -Destination (Join-Path $studioStage 'ETLForge.cmd')
Copy-Item -LiteralPath (Join-Path $etlRoot 'Studio\NOTICE-UPSTREAM.txt') -Destination $studioStage

# Server: compiled ETLForge payload and required non-downloaded runtime components.
foreach ($name in @('bin','lib','licenses','Resources','webapps','DataInterchange')) {
    Copy-Tree (Join-Path $etlRoot "Server\dist\$name") (Join-Path $serverStage $name)
}
foreach ($name in @('apache-httpd-2.4.69','apache-tomcat-9.0.123','cloud-logging','gnupg','pgbouncer-1.26.1','xslt-native')) {
    Copy-Tree (Join-Path $etlRoot "Server\runtime\$name") (Join-Path $serverStage "runtime\$name")
}
Copy-Item -LiteralPath (Join-Path $PSScriptRoot 'ServerPostInstall.ps1') -Destination $serverStage
Copy-Item -LiteralPath (Join-Path $PSScriptRoot 'Configure-Server.ps1') -Destination $serverStage
Copy-Item -LiteralPath (Join-Path $etlRoot 'Tools\Join-ETLForgeCluster.ps1') -Destination $serverStage
Copy-Item -LiteralPath (Join-Path $etlRoot 'Server\NOTICE-UPSTREAM.txt') -Destination $serverStage

# Remove upstream development/reference content while retaining runtime licenses, binaries and configuration.
$prune = @(
    'runtime\apache-httpd-2.4.69\Apache24\cgi-bin',
    'runtime\apache-httpd-2.4.69\Apache24\htdocs',
    'runtime\apache-httpd-2.4.69\Apache24\icons',
    'runtime\apache-httpd-2.4.69\Apache24\include',
    'runtime\apache-httpd-2.4.69\Apache24\lib',
    'runtime\apache-httpd-2.4.69\Apache24\manual',
    'runtime\apache-tomcat-9.0.123\webapps',
    'runtime\cloud-logging\include',
    'runtime\cloud-logging\etlforge_cloud_logging_tests.exe',
    'lib\etlforge-integration-server-jms.jar',
    'lib\etlforge-jms-util.jar'
)
foreach ($relative in $prune) { Remove-Item -LiteralPath (Join-Path $serverStage $relative) -Recurse -Force -ErrorAction SilentlyContinue }

$forbiddenSegments = @('private','run-user-home','run-user-home-etlforge','tests','src','build','build-probe','jms-lib')
foreach ($stage in @($studioStage,$serverStage)) {
    $bad = Get-ChildItem -LiteralPath $stage -Force -Recurse | Where-Object {
        $relative = $_.FullName.Substring($stage.Length).TrimStart('\')
        $segments = @($relative -split '[\\/]')
        ($segments.Count -gt 0 -and $segments[0] -in $forbiddenSegments) -or $relative -like 'data\postgresql*'
    } | Select-Object -First 1
    if ($bad) { throw "Forbidden payload content was staged: $($bad.FullName)" }
}

$pgArchive = Join-Path $etlRoot 'Downloads\postgresql-18.6-5-windows-x64-binaries.zip'
if (!(Test-Path -LiteralPath $pgArchive)) { throw 'Pinned PostgreSQL archive is missing from the release input cache.' }
$pgHash = (Get-FileHash -LiteralPath $pgArchive -Algorithm SHA256).Hash.ToLowerInvariant()

$env:ETLFORGE_INSTALLER_VERSION = $version
$env:ETLFORGE_INSTALLER_OUTPUT = $outputRoot
$env:ETLFORGE_STUDIO_STAGE = $studioStage
$env:ETLFORGE_SERVER_STAGE = $serverStage
$env:ETLFORGE_POSTGRESQL_HASH = $pgHash

& $iscc (Join-Path $PSScriptRoot 'Studio.iss')
if ($LASTEXITCODE -ne 0) { throw 'Studio installer compilation failed.' }
& $iscc (Join-Path $PSScriptRoot 'Server.iss')
if ($LASTEXITCODE -ne 0) { throw 'Server installer compilation failed.' }

$artifacts = @(Get-ChildItem -LiteralPath $outputRoot -File)
if ($artifacts.Count -ne 2) { throw "Expected exactly two installer files; found $($artifacts.Count)." }
foreach ($artifact in $artifacts) {
    if ($artifact.Extension -ne '.exe' -or $artifact.Name -notlike "*$version*") { throw "Unexpected installer artifact: $($artifact.Name)" }
}

$product.version = $version
$product.release = $parts[0]
$product | ConvertTo-Json -Compress | Set-Content -LiteralPath $versionFile -Encoding UTF8

$hashes = $artifacts | ForEach-Object {
    [pscustomobject]@{ File=$_.Name; Bytes=$_.Length; SHA256=(Get-FileHash -LiteralPath $_.FullName -Algorithm SHA256).Hash }
}
$hashes | Format-Table -AutoSize
if (!$KeepStage) { Remove-Item -LiteralPath $workRoot -Recurse -Force }
Write-Host "Built ETLForge $version installers in $outputRoot"
