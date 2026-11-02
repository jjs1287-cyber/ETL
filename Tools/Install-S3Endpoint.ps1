$ErrorActionPreference='Stop'
$stage='C:\ETL\Server\build\javascript-staging\bin'
$live='C:\ETL\Server\dist\bin'
$backup=Join-Path 'C:\ETL\Archives' ('s3-endpoint-install-'+(Get-Date -Format 'yyyyMMdd-HHmmss'))
$files=@(Get-ChildItem -LiteralPath $stage -File | Where-Object {$_.Extension -in '.dll','.exe','.so'})
if($files.Count -lt 20){throw 'Server staging output is incomplete'}
$env:PGPASSFILE='C:\ETL\Server\private\pgbouncer-dev.pgpass'
$psql='C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe'
$queued=& $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d etlforge_dev_msg -At -v ON_ERROR_STOP=1 -c 'SELECT count(*) FROM incomingmsgtab'
if($LASTEXITCODE -ne 0 -or $queued -ne '0'){throw 'Wait for the operation queue to empty before installing this development update'}
New-Item -ItemType Directory -Path $backup | Out-Null
foreach($file in $files){$old=Join-Path $live $file.Name;if(Test-Path -LiteralPath $old){Copy-Item -LiteralPath $old -Destination (Join-Path $backup $file.Name)}}
$services=@('ETLForge.Dev.NativeAPI','ETLForge.Dev.Scheduler','ETLForge.Dev.FileCleanup','ETLForge.Dev.ProcessEngine')
$running=@($services | Where-Object {(Get-Service -Name $_).Status -eq 'Running'})
try {
 foreach($name in $running){Stop-Service -Name $name;(Get-Service -Name $name).WaitForStatus('Stopped',[TimeSpan]::FromSeconds(30))}
 try{
  foreach($file in $files){$destination=Join-Path $live $file.Name;Copy-Item -LiteralPath $file.FullName -Destination $destination -Force;if((Get-FileHash -LiteralPath $file.FullName).Hash -ne (Get-FileHash -LiteralPath $destination).Hash){throw 'Installed binary checksum mismatch'}}
 }catch{foreach($file in $files){$old=Join-Path $backup $file.Name;if(Test-Path -LiteralPath $old){Copy-Item -LiteralPath $old -Destination (Join-Path $live $file.Name) -Force}};throw}
}finally{
 foreach($name in @('ETLForge.Dev.ProcessEngine','ETLForge.Dev.FileCleanup','ETLForge.Dev.Scheduler','ETLForge.Dev.NativeAPI')){if($running -contains $name){Start-Service -Name $name;(Get-Service -Name $name).WaitForStatus('Running',[TimeSpan]::FromSeconds(30))}}
 & C:\ETL\Tools\Build-NativeJavaScript.cmd restore
}
Write-Output "Installed $($files.Count) checksummed server components. Backup: $backup"
