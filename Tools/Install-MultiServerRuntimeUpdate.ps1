param([string]$PackageDirectory=$PSScriptRoot,[string]$ServerRoot='C:\ETL\Server',[string]$JoinServer,[pscredential]$JoinCredential,[switch]$AcceptTrustedNetworkBootstrapRisk)
$ErrorActionPreference='Stop'
$manifest=Get-Content -Raw -LiteralPath (Join-Path $PackageDirectory 'manifest.json')|ConvertFrom-Json
foreach($entry in $manifest){$source=Join-Path $PackageDirectory $entry.Path;if(!(Test-Path -LiteralPath $source) -or (Get-FileHash -LiteralPath $source -Algorithm SHA256).Hash -ne $entry.SHA256){throw "Package checksum failed: $($entry.Path)"}}
$config=Join-Path $ServerRoot 'instances\dev\etlforge.conf'
if(!(Select-String -Quiet -LiteralPath $config -Pattern '^KeyVaultMasterKey\s*=\s*.{32,}$')){
 if([string]::IsNullOrWhiteSpace($JoinServer)){throw 'This node has no vault key. Supply -JoinServer, -JoinCredential and -AcceptTrustedNetworkBootstrapRisk to synchronize required cluster values from an active server.'}
 & (Join-Path $PSScriptRoot 'Join-ETLForgeCluster.ps1') -ExistingServer $JoinServer -InstanceHome (Join-Path $ServerRoot 'instances\dev') -Credential $JoinCredential -AcceptTrustedNetworkBootstrapRisk:$AcceptTrustedNetworkBootstrapRisk
}
& icacls.exe $config /inheritance:r /grant:r '*S-1-5-32-544:(F)' '*S-1-5-18:(F)' | Out-Null
if($LASTEXITCODE -ne 0){throw 'Could not restrict the server configuration to Administrators and LocalSystem'}
$backup=Join-Path $ServerRoot ('backups\multi-server-runtime-'+(Get-Date -Format yyyyMMdd-HHmmss));New-Item -ItemType Directory -Path $backup -Force|Out-Null
$mapping=[ordered]@{
 'bin\ETLForgeAdmin.dll'='dist\bin\ETLForgeAdmin.dll'
 'bin\ETLForgePluginManager.dll'='dist\bin\ETLForgePluginManager.dll'
 'bin\ETLForgeProcessEngine.exe'='dist\bin\ETLForgeProcessEngine.exe'
 'bin\ETLForgeOperationEngineProc.exe'='dist\bin\ETLForgeOperationEngineProc.exe'
 'cloud\ETLForgeCloudLogging.exe'='runtime\cloud-logging\ETLForgeCloudLogging.exe'
}
$services=@('ETLForge.Dev.Watchdog','ETLForge.Dev.CloudLogging','ETLForge.Dev.NativeAPI','ETLForge.Dev.ProcessEngine')
$running=@($services|Where-Object{(Get-Service $_ -ErrorAction SilentlyContinue).Status -eq 'Running'})
try{
 foreach($name in $running){Stop-Service $name -Force;(Get-Service $name).WaitForStatus('Stopped',[TimeSpan]::FromSeconds(30))}
 foreach($entry in $mapping.GetEnumerator()){
  $source=Join-Path $PackageDirectory $entry.Key;$destination=Join-Path $ServerRoot $entry.Value
  if(Test-Path -LiteralPath $destination){Copy-Item -LiteralPath $destination -Destination (Join-Path $backup ([IO.Path]::GetFileName($destination)))}
  New-Item -ItemType Directory -Path (Split-Path -Parent $destination) -Force | Out-Null
  Copy-Item -LiteralPath $source -Destination $destination -Force
  if((Get-FileHash $source).Hash -ne (Get-FileHash $destination).Hash){throw "Installed checksum failed: $destination"}
 }
} finally {
 foreach($name in @('ETLForge.Dev.ProcessEngine','ETLForge.Dev.NativeAPI','ETLForge.Dev.CloudLogging','ETLForge.Dev.Watchdog')){if($running -contains $name){Start-Service $name;(Get-Service $name).WaitForStatus('Running',[TimeSpan]::FromSeconds(30))}}
}
Write-Output "Installed database-key and cloud-delivery runtime update. Backup: $backup"
