$ErrorActionPreference='Stop'
$psql='C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe'
$previousPass=$env:PGPASSFILE;$previousPassword=$env:PGPASSWORD;$previousEncoding=$env:PGCLIENTENCODING;$consoleEncoding=[Console]::OutputEncoding
function Query([string]$database,[string]$user,[string]$sql){
 $queryFile='C:\ETL\Server\build-probe\pgbouncer-query.sql'
 [IO.File]::WriteAllText($queryFile,$sql,[Text.UTF8Encoding]::new($false))
 $lines=@(& $psql -X -w -A -t -v ON_ERROR_STOP=1 -h 127.0.0.1 -p 5432 -U $user -d $database -f $queryFile 2>&1)
 if($LASTEXITCODE){throw "Pooler SQL failed for $database : $($lines -join ' ')"}
 return ($lines -join "`n").Trim()
}
try{
 $env:PGPASSFILE='C:\ETL\Server\private\pgbouncer-dev.pgpass';$env:PGPASSWORD=$null;$env:PGCLIENTENCODING='UTF8';[Console]::OutputEncoding=[Text.UTF8Encoding]::new($false)
 foreach($database in @('etlforge_dev_tran','etlforge_dev_msg','etlforge_dev_pdb','etlforge_dev_idb')){
  $result=Query $database etlforge_dev "SELECT current_database() || '|' || current_user || '|' || (current_setting('server_version_num')::int >= 180000)::text"
  if($result -ne "$database|etlforge_dev|true"){throw 'Unexpected pooled database/role/version'}
 }
 $first=Query etlforge_dev_tran etlforge_dev "SELECT pg_backend_pid(); CREATE TEMP TABLE etlforge_pool_session_probe(value text); INSERT INTO etlforge_pool_session_probe VALUES ('ETLForge 日本'); SELECT value FROM etlforge_pool_session_probe;"
 if($first -notmatch 'ETLForge 日本'){throw 'Pooled UTF-8 roundtrip failed'}
 $second=Query etlforge_dev_tran etlforge_dev "SELECT pg_backend_pid()::text || '|' || (to_regclass('pg_temp.etlforge_pool_session_probe') IS NULL)::text"
 $firstPid=($first -split "`n")[0].Trim()
 if($second -ne "$firstPid|true"){throw 'Backend reuse or session-state reset failed'}
 $version=Query pgbouncer etlforge_pool_admin 'SHOW VERSION'
 if($version -notmatch '1.26.1'){throw 'Unexpected pooler version'}
 for($cycle=1;$cycle -le 2;$cycle++){
  $child=(Get-NetTCPConnection -LocalPort 5432 -State Listen).OwningProcess
  $timer=[Diagnostics.Stopwatch]::StartNew();Stop-Service ETLForge.Dev.PgBouncer
  if(Get-Process -Id $child -ErrorAction SilentlyContinue){throw 'PgBouncer remains after stop'}
  if(Get-NetTCPConnection -LocalPort 5432 -State Listen -ErrorAction SilentlyContinue){throw 'Pooler port remains after stop'}
  if($timer.Elapsed.TotalSeconds -ge 20){throw 'Pooler stop exceeded timeout'}
  Start-Service ETLForge.Dev.PgBouncer
  $result=Query etlforge_dev_tran etlforge_dev 'SELECT 1'
  if($result -ne '1'){throw 'Post-restart pooler query failed'}
 }
 'PASS: native x64 PgBouncer 1.26.1, SCRAM authentication, all four PostgreSQL 18 database aliases, UTF-8, backend connection reuse, temporary-state reset, admin console and two graceful service restarts'
}finally{$env:PGPASSFILE=$previousPass;$env:PGPASSWORD=$previousPassword;$env:PGCLIENTENCODING=$previousEncoding;[Console]::OutputEncoding=$consoleEncoding}
