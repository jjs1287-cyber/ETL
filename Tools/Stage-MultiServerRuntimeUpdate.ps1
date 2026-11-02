param([string]$OutputDirectory='C:\SFTP\endowance\AWS\ETLForge-MultiServer-Update')
$ErrorActionPreference='Stop'
$files=[ordered]@{
 'bin\ETLForgeAdmin.dll'='C:\ETL\Server\dist\bin\ETLForgeAdmin.dll'
 'bin\ETLForgePluginManager.dll'='C:\ETL\Server\dist\bin\ETLForgePluginManager.dll'
 'bin\ETLForgeProcessEngine.exe'='C:\ETL\Server\dist\bin\ETLForgeProcessEngine.exe'
 'bin\ETLForgeOperationEngineProc.exe'='C:\ETL\Server\dist\bin\ETLForgeOperationEngineProc.exe'
 'cloud\ETLForgeCloudLogging.exe'='C:\ETL\Server\runtime\cloud-logging\ETLForgeCloudLogging.exe'
 'migrations\015-key-vault.sql'='C:\ETL\Server\database\migrations\015-key-vault.sql'
 'migrations\016-cloud-delivery-leases.sql'='C:\ETL\Server\database\migrations\016-cloud-delivery-leases.sql'
 'Join-ETLForgeCluster.ps1'='C:\ETL\Tools\Join-ETLForgeCluster.ps1'
}
if(Test-Path -LiteralPath $OutputDirectory){throw "Output directory already exists: $OutputDirectory"}
New-Item -ItemType Directory -Path $OutputDirectory | Out-Null
$manifest=@()
foreach($entry in $files.GetEnumerator()){
 if(!(Test-Path -LiteralPath $entry.Value)){throw "Missing runtime file: $($entry.Value)"}
 $destination=Join-Path $OutputDirectory $entry.Key
 New-Item -ItemType Directory -Path (Split-Path $destination) -Force | Out-Null
 Copy-Item -LiteralPath $entry.Value -Destination $destination
 $manifest+=[pscustomobject]@{Path=$entry.Key;SHA256=(Get-FileHash -LiteralPath $destination -Algorithm SHA256).Hash}
}
$manifest|ConvertTo-Json|Set-Content -LiteralPath (Join-Path $OutputDirectory 'manifest.json') -Encoding utf8
Copy-Item -LiteralPath 'C:\ETL\Tools\Install-MultiServerRuntimeUpdate.ps1' -Destination $OutputDirectory
Write-Output "Staged $($files.Count) verified runtime files at $OutputDirectory"

