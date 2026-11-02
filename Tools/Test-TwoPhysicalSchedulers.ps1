$ErrorActionPreference = 'Stop'
$env:PGPASSFILE = 'C:\ETL\Server\private\pgbouncer-dev.pgpass'
$psql = 'C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe'
$java = 'C:\ETL\Tools\jdk8u504-b01\bin\java.exe'
$classpath = 'C:\ETL\Server\build-probe;C:\ETL\Studio\configuration;C:\ETL\Studio\dist\lib\*;C:\ETL\Studio\lib\*'
$fixtureOperationGuid = '42430000-0000-4000-8000-000000000003'

function Query([string]$database, [string]$sql) {
    $result = & $psql -h 127.0.0.1 -p 5432 -U etlforge_dev -d $database -At -v ON_ERROR_STOP=1 -c $sql
    if ($LASTEXITCODE -ne 0) { throw "Database query failed against $database." }
    return $result
}

try {
    $nodes = @(Query 'etlforge_dev_tran' "SELECT n.hostname||'|'||h.node_id FROM etlforge_cluster.service_heartbeat h JOIN etlforge_cluster.node n USING(node_id) WHERE h.service='Scheduler' AND h.heartbeat_at>now()-interval '15 seconds' ORDER BY n.hostname")
    if ($nodes.Count -ne 2) { throw "Expected two current Scheduler heartbeats; found $($nodes.Count)." }
    if (($nodes | ForEach-Object { ($_ -split '\|')[0] } | Select-Object -Unique).Count -ne 2) {
        throw 'The two Scheduler heartbeats are not from two physical hostnames.'
    }

    & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke disable | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'Could not reset the isolated schedule fixture.' }

    $baseline = Query 'etlforge_dev_tran' 'SELECT coalesce(max(operation_instance_id),0) FROM operationlogtab'
    & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke
    if ($LASTEXITCODE -ne 0) { throw 'Could not configure the isolated due schedule.' }

    $due = [datetime]::Parse((Get-Content 'C:\ETL\Reports\scheduler-console-test-due.txt')[0])
    $slot = $due.ToString('yyyy-MM-dd HH:mm:ss')
    $completed = $false
    for ($step = 0; $step -lt 75; $step++) {
        $count = [int](Query 'etlforge_dev_tran' "SELECT count(*) FROM operationlogtab l JOIN guidsforoperationstab g ON g.item_id=l.operation_id WHERE g.guid='$fixtureOperationGuid' AND l.operation_instance_id>$baseline AND status IN (3,4,5)")
        if ($count -gt 1) { throw 'Duplicate scheduled operation instances were created.' }
        if ($count -eq 1) { $completed = $true; break }
        Start-Sleep -Seconds 1
    }
    if (-not $completed) { throw 'The scheduled operation did not reach a terminal state.' }

    $occurrences = Query 'etlforge_dev_msg' "SELECT count(*) FROM etlforge_cluster.schedule_occurrence WHERE scheduled_local=timestamp '$slot' AND outcome='enqueued'"
    if ($occurrences -ne '1') { throw "Expected one durable occurrence; found $occurrences." }
    $instances = Query 'etlforge_dev_tran' "SELECT count(*) FROM operationlogtab l JOIN guidsforoperationstab g ON g.item_id=l.operation_id WHERE g.guid='$fixtureOperationGuid' AND l.operation_instance_id>$baseline"
    if ($instances -ne '1') { throw "Expected one operation instance; found $instances." }

    Write-Output "PASS: physical scheduler nodes: $($nodes -join ', ')"
    Write-Output "PASS: one durable occurrence for $slot"
    Write-Output 'PASS: one operation instance was created and completed.'
} finally {
    & $java '-Dfile.encoding=UTF-8' -cp $classpath SchedulerConsoleSmoke disable | Out-Null
}
