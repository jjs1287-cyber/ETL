$ErrorActionPreference='Stop'
$env:PGPASSFILE='C:\ETL\Server\private\pgbouncer-dev.pgpass'
$psql='C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe'
$java='C:\ETL\Tools\jdk8u504-b01\bin\java.exe'
$classpath='C:\ETL\Server\build-probe;C:\ETL\Studio\configuration;C:\ETL\Studio\dist\lib\*;C:\ETL\Studio\lib\*'
$fixture=Join-Path 'C:\ETL\Server\private' ('cluster-scheduler-test-'+[Guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $fixture | Out-Null
$identity=[Guid]::NewGuid().ToString()
$text=[IO.File]::ReadAllText('C:\ETL\Server\instances\dev\etlforge.conf',[Text.Encoding]::UTF8)
$text=$text -replace '(?m)^NodeId\s*=.*$',"NodeId=$identity"
$text=$text -replace '(?m)^CommandPort\s*=\s*47106\s*$', 'CommandPort = 47206'
[IO.File]::WriteAllText((Join-Path $fixture 'etlforge.conf'),$text,(New-Object Text.UTF8Encoding($false)))
$child=$null
$priorInstance=$env:ETLFORGE_HOME
try{
 & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke disable | Out-Null
 if($LASTEXITCODE -ne 0){throw 'Could not disable the owned schedule fixture'}
 $baseline=& $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d etlforge_dev_tran -At -c 'SELECT coalesce(max(operation_instance_id),0) FROM operationlogtab'
 if($LASTEXITCODE -ne 0){throw 'Cannot read baseline'}
 $env:ETLFORGE_HOME=$fixture
 $child=Start-Process -FilePath 'C:\ETL\Server\dist\bin\ETLForgeScheduler.exe' -WorkingDirectory $fixture -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $fixture 'stdout.log') -RedirectStandardError (Join-Path $fixture 'stderr.log')
 $env:ETLFORGE_HOME=$priorInstance
 & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke
 if($LASTEXITCODE -ne 0){throw 'Scheduler console checks failed'}
 $due=[datetime]::Parse((Get-Content 'C:\ETL\Reports\scheduler-console-test-due.txt')[0])
 $slot=$due.ToString('yyyy-MM-dd HH:mm:ss')
 $complete=$false
 for($step=0;$step -lt 65;$step++){
  $result=& $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d etlforge_dev_tran -At -c "SELECT count(*) FROM operationlogtab l JOIN guidsforoperationstab g ON g.item_id=l.operation_id WHERE g.guid='42430000-0000-4000-8000-000000000003' AND l.operation_instance_id>$baseline AND status IN (3,4,5)"
  if($LASTEXITCODE -ne 0){throw 'Cannot read completion state'}
  if($result -eq '1'){$complete=$true;break}
  if([int]$result -gt 1){throw 'Duplicate scheduled operation instances'}
  Start-Sleep -Seconds 1
 }
 if(!$complete){throw 'Scheduled operation did not complete'}
 $count=& $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d etlforge_dev_msg -At -c "SELECT count(*) FROM etlforge_cluster.schedule_occurrence WHERE scheduled_local=timestamp '$slot' AND outcome='enqueued'"
 if($LASTEXITCODE -ne 0 -or $count -ne '1'){throw 'Expected exactly one durable occurrence'}
 $online=& $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d etlforge_dev_tran -At -c "SELECT count(*) FROM etlforge_cluster.service_heartbeat WHERE service='Scheduler' AND heartbeat_at>now()-interval '15 seconds'"
 if($LASTEXITCODE -ne 0 -or [int]$online -lt 2){throw 'Two native schedulers were not reporting heartbeats'}
 Write-Output 'PASS: two real native Scheduler processes on this physical laptop, one durable occurrence, one successful operation instance.'
}finally{
 $env:ETLFORGE_HOME=$priorInstance
 if($child -and !$child.HasExited){try{$env:ETLFORGE_HOME=$fixture;& C:\ETL\Server\dist\bin\ETLForgeControl.exe Scheduler 47206 Shutdown | Out-Null;if(!$child.WaitForExit(20000)){throw 'Test scheduler did not shut down normally'}}finally{$env:ETLFORGE_HOME=$priorInstance}}
 & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke disable | Out-Null
 $released=& $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d etlforge_dev_tran -At -c "SELECT count(*) FROM etlforge_cluster.service_heartbeat WHERE node_id='$identity'"
 if($LASTEXITCODE -ne 0 -or $released -ne '0'){throw 'Graceful Scheduler shutdown did not release its heartbeat'}
 Write-Output 'PASS: graceful Scheduler shutdown releases its own registration without waiting for lease expiry.'
 & $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d etlforge_dev_tran -v ON_ERROR_STOP=1 -c "DELETE FROM etlforge_cluster.service_heartbeat WHERE node_id='$identity'; DELETE FROM etlforge_cluster.node WHERE node_id='$identity'" | Out-Null
 Remove-Item -LiteralPath (Join-Path $fixture 'etlforge.conf')
}
