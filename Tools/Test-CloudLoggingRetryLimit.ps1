$ErrorActionPreference='Stop'
. C:\ETL\Tools\CloudLoggingTestCommon.ps1
$env:PGPASSFILE='C:\ETL\Server\private\pgbouncer-dev.pgpass'
function Sql([string]$Statement){
 $result=& C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe -h 127.0.0.1 -p 5432 -U etlforge_dev -d etlforge_dev_tran -X -At -v ON_ERROR_STOP=1 -c $Statement
 if($LASTEXITCODE -ne 0){throw 'Retry test SQL failed'}
 return ($result -join "`n").Trim()
}
$original=(Invoke-CloudAdmin @{action='cloud.settings'}).settings
$wasRunning=(Get-Service ETLForge.Dev.CloudLogging).Status -eq 'Running'
try {
 Stop-Service ETLForge.Dev.CloudLogging
 $test=$original.Clone();$test.enabled=$true;$test.type='http';$test.url='http://127.0.0.1:47199/retry-limit-test';$test.includeHistory=$false
 Save-CloudTestSettings $test | Out-Null
 $output=Invoke-CloudExample
 $match=[regex]::Match(($output -join "`n"),'instance=(\d+)')
 if(!$match.Success){throw 'Could not identify retry test instance'}
 $id=$match.Groups[1].Value
 $deadline=(Get-Date).AddSeconds(30)
 while((Sql "SELECT status FROM operationlogtab WHERE operation_instance_id=$id") -notin @('1','3','4','5','6')){if((Get-Date) -gt $deadline){throw 'Test operation did not finish'};Start-Sleep -Milliseconds 200}
 for($attempt=1;$attempt -le 5;$attempt++){
  Sql "UPDATE cloudlogging.delivery SET next_attempt_at=now() WHERE operation_instance_id=$id" | Out-Null
  & C:\ETL\Server\runtime\cloud-logging\ETLForgeCloudLogging.exe --home C:\ETL\Server\instances\dev --once
  if($LASTEXITCODE -ne 0){throw 'Uploader failed before the transport attempt'}
  $actual=Sql "SELECT attempts FROM cloudlogging.delivery WHERE operation_instance_id=$id"
  if([int]$actual -ne $attempt){throw "Expected $attempt attempts, got $actual"}
 }
 Sql "UPDATE cloudlogging.delivery SET next_attempt_at=now() WHERE operation_instance_id=$id" | Out-Null
 & C:\ETL\Server\runtime\cloud-logging\ETLForgeCloudLogging.exe --home C:\ETL\Server\instances\dev --once
 $actual=Sql "SELECT attempts FROM cloudlogging.delivery WHERE operation_instance_id=$id"
 if([int]$actual -ne 5){throw 'Sixth attempt was not blocked'}
 Start-Service ETLForge.Dev.CloudLogging
 Start-Sleep -Seconds 12
 $actual=Sql "SELECT attempts FROM cloudlogging.delivery WHERE operation_instance_id=$id"
 if([int]$actual -ne 5){throw 'Restart reset the attempt limit'}
 $state=Sql "SELECT cloud_upload_status FROM operationinstancetab WHERE operation_instance_id=$id"
 if($state -ne '2'){throw 'Exhausted delivery did not retain error status'}
 Write-Output "PASS: instance $id stopped at five total attempts; sixth attempt and service restart did not retry; upload status remains 2."
} finally {
 Save-CloudTestSettings $original | Out-Null
 if($wasRunning){Start-Service ETLForge.Dev.CloudLogging}else{Stop-Service ETLForge.Dev.CloudLogging}
}
