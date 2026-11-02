$ErrorActionPreference = 'Stop'
$env:PGPASSFILE = 'C:\ETL\Server\private\pgbouncer-dev.pgpass'
$psql = 'C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe'
$java = 'C:\ETL\Tools\jdk8u504-b01\bin\java.exe'
$classpath = 'C:\ETL\Server\build-probe;C:\ETL\Studio\configuration;C:\ETL\Studio\dist\lib\*;C:\ETL\Studio\lib\*;C:\ETL\Server\dist\jms-lib\postgresql-42.7.14.jar'
$ssh = 'C:\Windows\System32\OpenSSH\ssh.exe'
$sshKey = 'C:\ETL\Tools\remote-test-access\etlforge_multiserver_ed25519'
$remote = 'jjs1287@192.168.99.148'

function Query([string]$database, [string]$sql) {
    $result = & $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d $database -At -v ON_ERROR_STOP=1 -c $sql
    if ($LASTEXITCODE -ne 0) { throw "Database query failed against $database." }
    return $result
}

function Remote-Worker([string]$action) {
    $expectedStatus = if ($action -eq 'Stop') { 'Stopped' } else { 'Running' }
    $command = "$action-Service ETLForge.Dev.ProcessEngine; (Get-Service ETLForge.Dev.ProcessEngine).WaitForStatus('$expectedStatus',[TimeSpan]::FromSeconds(60))"
    $encoded = [Convert]::ToBase64String([Text.Encoding]::Unicode.GetBytes($command))
    & $ssh -i $sshKey -o BatchMode=yes $remote "powershell -NoProfile -EncodedCommand $encoded" | Out-Null
    if ($LASTEXITCODE -ne 0) { throw "Remote Process Engine $action failed." }
}

function Run-Case([string]$expectedOwner) {
    $baseline = [long](Query 'etlforge_dev_tran' 'SELECT coalesce(max(operation_instance_id),0) FROM operationinstancetab')
    & $java '-Dfile.encoding=UTF-8' -cp $classpath ClusterRuntimeSmoke 20 | Out-Null
    if ($LASTEXITCODE -ne 0) { throw "The $expectedOwner workload failed." }
    $instances = @(Query 'etlforge_dev_tran' "SELECT operation_instance_id||'|'||msgid FROM operationinstancetab WHERE operation_instance_id>$baseline ORDER BY operation_instance_id")
    if ($instances.Count -ne 20) { throw "Expected 20 new instances for $expectedOwner; found $($instances.Count)." }
    $msgids = $instances | ForEach-Object { ($_ -split '\|')[1] }
    $owners = @(Query 'etlforge_dev_msg' "SELECT coalesce(owner,'<blank>')||'|'||count(*) FROM archivemsgtab WHERE msgid IN ($($msgids -join ',')) GROUP BY owner ORDER BY owner")
    if ($owners.Count -ne 1 -or $owners[0] -ne "$expectedOwner|20") {
        throw "Expected only $expectedOwner; actual ownership: $($owners -join ', ')."
    }
    Write-Output "PASS: $expectedOwner alone completed 20/20 operations with one owner per message."
}

$watchdogWasRunning = (Get-Service ETLForge.Dev.Watchdog).Status -eq 'Running'
try {
    if ($watchdogWasRunning) { Stop-Service ETLForge.Dev.Watchdog }

    Remote-Worker 'Stop'
    Run-Case 'DESKTOP-33T8ST4'
    Remote-Worker 'Start'

    Stop-Service ETLForge.Dev.ProcessEngine
    (Get-Service ETLForge.Dev.ProcessEngine).WaitForStatus('Stopped', [TimeSpan]::FromSeconds(60))
    Run-Case 'DESKTOP-HT9BS5M'
    Start-Service ETLForge.Dev.ProcessEngine
    (Get-Service ETLForge.Dev.ProcessEngine).WaitForStatus('Running', [TimeSpan]::FromSeconds(60))
} finally {
    if ((Get-Service ETLForge.Dev.ProcessEngine).Status -ne 'Running') { Start-Service ETLForge.Dev.ProcessEngine }
    try { Remote-Worker 'Start' } catch {}
    if ($watchdogWasRunning -and (Get-Service ETLForge.Dev.Watchdog).Status -ne 'Running') { Start-Service ETLForge.Dev.Watchdog }
}

if ((Query 'etlforge_dev_msg' 'SELECT count(*) FROM incomingmsgtab') -ne '0') { throw 'The queue is not empty after worker testing.' }
Write-Output 'PASS: both workers restored and the shared queue is empty.'
