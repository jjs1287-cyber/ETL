param([string]$InstanceHome='C:\ETL\Server\instances\dev',[int]$BackendPort=47112,[string]$ServiceName='ETLForge.Dev.NativeAPI')
$ErrorActionPreference='Stop'
if($BackendPort -lt 1 -or $BackendPort -gt 65535){throw 'Invalid Java backend port'}
$apache='C:\ETL\Server\runtime\apache-httpd-2.4.69\Apache24\bin\httpd.exe'
$configuration=Join-Path $InstanceHome 'apache\conf\httpd.conf'
$include=Join-Path $InstanceHome 'apache\conf\studio-java-proxy.conf'
$template='C:\ETL\Server\src\resources\apache\studio-java-proxy.conf.in'
$backup=Join-Path 'C:\ETL\Archives' ('studio-single-port-'+(Get-Date -Format 'yyyyMMdd-HHmmss'))
New-Item -ItemType Directory -Path $backup | Out-Null
Copy-Item -LiteralPath $configuration -Destination (Join-Path $backup 'httpd.conf')
$hadInclude=Test-Path -LiteralPath $include
if($hadInclude){Copy-Item -LiteralPath $include -Destination (Join-Path $backup 'studio-java-proxy.conf')}
$wasRunning=(Get-Service -Name $ServiceName).Status -eq 'Running'
try{
 $text=[IO.File]::ReadAllText($template,[Text.Encoding]::UTF8).Replace('@BACKEND_PORT@',$BackendPort.ToString())
 [IO.File]::WriteAllText($include,$text,(New-Object Text.UTF8Encoding($false)))
 $text=[IO.File]::ReadAllText($configuration,[Text.Encoding]::UTF8)
 $directive='Include "'+$include.Replace('\','/')+'"'
 if(!$text.Contains($directive)){$text+="`r`n$directive`r`n"}
 [IO.File]::WriteAllText($configuration,$text,(New-Object Text.UTF8Encoding($false)))
 & $apache -t -f $configuration
 if($LASTEXITCODE -ne 0){throw 'Single-port Apache configuration is invalid'}
 if($wasRunning){Restart-Service -Name $ServiceName;(Get-Service -Name $ServiceName).WaitForStatus('Running',[TimeSpan]::FromSeconds(45))}
 Write-Output "Configured one Studio entry point; Java backend remains on 127.0.0.1:$BackendPort. Backup: $backup"
}catch{
 Copy-Item -LiteralPath (Join-Path $backup 'httpd.conf') -Destination $configuration -Force
 if($hadInclude){Copy-Item -LiteralPath (Join-Path $backup 'studio-java-proxy.conf') -Destination $include -Force}else{Remove-Item -LiteralPath $include -ErrorAction SilentlyContinue}
 if($wasRunning){Restart-Service -Name $ServiceName;(Get-Service -Name $ServiceName).WaitForStatus('Running',[TimeSpan]::FromSeconds(45))}
 throw
}
