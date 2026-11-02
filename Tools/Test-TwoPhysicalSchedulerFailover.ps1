$ErrorActionPreference = 'Stop'
$env:PGPASSFILE = 'C:\ETL\Server\private\pgbouncer-dev.pgpass'
$psql = 'C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe'
$java = 'C:\ETL\Tools\jdk8u504-b01\bin\java.exe'
$classpath = 'C:\ETL\Server\build-probe;C:\ETL\Studio\configuration;C:\ETL\Studio\dist\lib\*;C:\ETL\Studio\lib\*'
$ssh = 'C:\Windows\System32\OpenSSH\ssh.exe'
$sshKey = 'C:\ETL\Tools\remote-test-access\etlforge_multiserver_ed25519'
$remote = 'jjs1287@192.168.99.148'
$fixtureOperationGuid = '42430000-0000-4000-8000-000000000003'

function Query([string]$database, [string]$sql) {
    $result = & $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d $database -At -v ON_ERROR_STOP=1 -c $sql
    if ($LASTEXITCODE -ne 0) { throw "Database query failed against $database." }
    return $result
}

function Remote-Service([string]$action) {
    $expectedStatus = if ($action -eq 'Stop') { 'Stopped' } else { 'Running' }
    $remoteCommand = "$action-Service ETLForge.Dev.Scheduler; (Get-Service ETLForge.Dev.Scheduler).WaitForStatus('$expectedStatus',[TimeSpan]::FromSeconds(45))"
    $encodedCommand = [Convert]::ToBase64String([Text.Encoding]::Unicode.GetBytes($remoteCommand))
    & $ssh -i $sshKey -o BatchMode=yes $remote "powershell -NoProfile -EncodedCommand $encodedCommand"
    if ($LASTEXITCODE -ne 0) { throw "Remote Scheduler $action failed." }
}

function Arm-Fixture {
    & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke disable | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'Could not reset the isolated schedule fixture.' }
    $baseline = Query 'etlforge_dev_tran' 'SELECT coalesce(max(operation_instance_id),0) FROM operationlogtab'
    & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'Could not arm the isolated schedule fixture.' }
    $due = [datetime]::Parse((Get-Content 'C:\ETL\Reports\scheduler-console-test-due.txt')[0])
    return [pscustomobject]@{ Baseline = $baseline; Due = $due }
}

function Verify-Fixture($fixture, [string]$expectedHost) {
    $slot = $fixture.Due.ToString('yyyy-MM-dd HH:mm:ss')
    $currentHosts = @(Query 'etlforge_dev_tran' "SELECT n.hostname FROM etlforge_cluster.service_heartbeat h JOIN etlforge_cluster.node n USING(node_id) WHERE h.service='Scheduler' AND h.heartbeat_at>now()-interval '15 seconds' ORDER BY n.hostname")
    if ($currentHosts.Count -ne 1 -or $currentHosts[0] -ne $expectedHost) {
        throw "Expected only $expectedHost before due time; current hosts: $($currentHosts -join ', ')."
    }
    $completed = $false
    for ($step = 0; $step -lt 75; $step++) {
        $count = [int](Query 'etlforge_dev_tran' "SELECT count(*) FROM operationlogtab l JOIN guidsforoperationstab g ON g.item_id=l.operation_id WHERE g.guid='$fixtureOperationGuid' AND l.operation_instance_id>$($fixture.Baseline) AND status IN (3,4,5)")
        if ($count -gt 1) { throw 'Failover created duplicate terminal operation instances.' }
        if ($count -eq 1) { $completed = $true; break }
        Start-Sleep -Seconds 1
    }
    if (-not $completed) { throw "The fixture did not complete with $expectedHost as survivor." }
    $occurrences = Query 'etlforge_dev_msg' "SELECT count(*) FROM etlforge_cluster.schedule_occurrence WHERE scheduled_local=timestamp '$slot' AND outcome='enqueued'"
    $instances = Query 'etlforge_dev_tran' "SELECT count(*) FROM operationlogtab l JOIN guidsforoperationstab g ON g.item_id=l.operation_id WHERE g.guid='$fixtureOperationGuid' AND l.operation_instance_id>$($fixture.Baseline)"
    if ($occurrences -ne '1' -or $instances -ne '1') {
        throw "Failover mismatch for ${expectedHost}: occurrences=$occurrences instances=$instances."
    }
    Write-Output "PASS: $expectedHost alone scheduled one occurrence and one operation instance for $slot."
}

$watchdogWasRunning = (Get-Service ETLForge.Dev.Watchdog).Status -eq 'Running'
try {
    if ($watchdogWasRunning) { Stop-Service ETLForge.Dev.Watchdog }

    $fixture = Arm-Fixture
    Stop-Service ETLForge.Dev.Scheduler
    (Get-Service ETLForge.Dev.Scheduler).WaitForStatus('Stopped', [TimeSpan]::FromSeconds(45))
    Verify-Fixture $fixture 'DESKTOP-HT9BS5M'
    Start-Service ETLForge.Dev.Scheduler
    (Get-Service ETLForge.Dev.Scheduler).WaitForStatus('Running', [TimeSpan]::FromSeconds(45))
    Start-Sleep -Seconds 7

    $fixture = Arm-Fixture
    Remote-Service 'Stop'
    Verify-Fixture $fixture 'DESKTOP-33T8ST4'
    Remote-Service 'Start'
    Start-Sleep -Seconds 7
} finally {
    & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke disable | Out-Null
    if ((Get-Service ETLForge.Dev.Scheduler).Status -ne 'Running') { Start-Service ETLForge.Dev.Scheduler }
    try { Remote-Service 'Start' } catch {}
    if ($watchdogWasRunning -and (Get-Service ETLForge.Dev.Watchdog).Status -ne 'Running') { Start-Service ETLForge.Dev.Watchdog }
}

$online = @(Query 'etlforge_dev_tran' "SELECT n.hostname FROM etlforge_cluster.service_heartbeat h JOIN etlforge_cluster.node n USING(node_id) WHERE h.service='Scheduler' AND h.heartbeat_at>now()-interval '15 seconds' ORDER BY n.hostname")
if ($online.Count -ne 2) { throw 'Both Scheduler nodes did not rejoin after failover testing.' }
Write-Output "PASS: both Scheduler nodes rejoined: $($online -join ', ')."
