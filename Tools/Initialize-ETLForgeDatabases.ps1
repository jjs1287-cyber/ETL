$ErrorActionPreference='Stop'
$psql='C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe'
$private='C:\ETL\Server\private'
$working='C:\ETL\Server\src\database\postgresql'
$role='etlforge_dev'
$passwordFile="$private\etlforge-dev-db-password.txt"
if(!(Test-Path $passwordFile)){
 $bytes=New-Object byte[] 32;$rng=[Security.Cryptography.RandomNumberGenerator]::Create();try{$rng.GetBytes($bytes)}finally{$rng.Dispose()}
 [IO.File]::WriteAllText($passwordFile,([BitConverter]::ToString($bytes).Replace('-','').ToLowerInvariant()),[Text.UTF8Encoding]::new($false))
}
$password=([IO.File]::ReadAllText($passwordFile)).Trim()
if($password -notmatch '^[a-f0-9]{64}$'){throw 'Unexpected database credential format'}
$previous=$env:PGPASSFILE
try{
 $env:PGPASSFILE="$private\postgresql-18.pgpass"
 $version=& $psql -X -w -h 127.0.0.1 -p 6543 -U etlforge_admin -d postgres -Atc "SELECT current_setting('port')||':'||current_setting('server_version_num')"
 if($LASTEXITCODE -ne 0 -or $version -notmatch '^6543:18\d{4}$'){throw 'Expected the isolated PostgreSQL 18 cluster on port 6543'}
 $template=@'
DO $etlforge$
BEGIN
 IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname='etlforge_dev') THEN
  CREATE ROLE etlforge_dev LOGIN PASSWORD '__PASSWORD__' NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION;
 END IF;
END
$etlforge$;
ALTER ROLE etlforge_dev SET standard_conforming_strings TO 'off';
ALTER ROLE etlforge_dev SET escape_string_warning TO 'off';
'@
 $setupFile="$private\etlforge-dev-role.sql"
 [IO.File]::WriteAllText($setupFile,$template.Replace('__PASSWORD__',$password),[Text.UTF8Encoding]::new($false))
 & $psql -X -w -v ON_ERROR_STOP=1 -h 127.0.0.1 -p 6543 -U etlforge_admin -d postgres -f $setupFile *> C:\ETL\Reports\etlforge-db-role-setup.log
 if($LASTEXITCODE -ne 0){throw 'Application database role setup failed; see role setup log'}
 foreach($database in @('etlforge_dev_tran','etlforge_dev_msg','etlforge_dev_pdb','etlforge_dev_idb')){
  $exists=& $psql -X -w -h 127.0.0.1 -p 6543 -U etlforge_admin -d postgres -Atc "SELECT 1 FROM pg_database WHERE datname='$database'"
  if($LASTEXITCODE -ne 0){throw 'Could not inspect application database names'}
  if($exists -ne '1'){
   & $psql -X -w -v ON_ERROR_STOP=1 -h 127.0.0.1 -p 6543 -U etlforge_admin -d postgres -c "CREATE DATABASE $database OWNER etlforge_dev ENCODING 'UTF8' TEMPLATE template0" *> "C:\ETL\Reports\$database-create.log"
   if($LASTEXITCODE -ne 0){throw "Failed to create isolated database $database"}
  }
 }
 [IO.File]::WriteAllText("$private\etlforge-dev.pgpass",('127.0.0.1:6543:*:etlforge_dev:'+$password+"`n"),[Text.UTF8Encoding]::new($false))
 $env:PGPASSFILE="$private\etlforge-dev.pgpass"
 foreach($entry in @(@('etlforge_dev_tran','TranDb'),@('etlforge_dev_msg','MsgDb'),@('etlforge_dev_pdb','PDb'),@('etlforge_dev_idb','IDb'))){
  $database=$entry[0];$module=$entry[1]
  $marker=& $psql -X -w -h 127.0.0.1 -p 6543 -U $role -d $database -Atc "SELECT to_regclass('public.etlforge_bootstrap_state') IS NOT NULL"
  if($LASTEXITCODE -ne 0){throw "Could not inspect $database"}
  if($marker -eq 't'){"Schema already initialized: $database";continue}
  $tables=& $psql -X -w -h 127.0.0.1 -p 6543 -U $role -d $database -Atc "SELECT count(*) FROM pg_tables WHERE schemaname='public'"
  if($LASTEXITCODE -ne 0 -or $tables -ne '0'){throw "Refusing to bootstrap nonempty database $database without an ETLForge marker"}
  $names=if($module -eq 'TranDb'){@('InterchangeProjectEntityTabs.sql','CreateDb.sql','PermissionTabs.sql','GuidTabs.sql','GuidViews.sql')}else{@('CreateDb.sql')}
  $sql=($names|ForEach-Object {[IO.File]::ReadAllText("$working\$module\$_")}) -join "`r`n"
  if($module -eq 'MsgDb'){$sql+="`r`n"+[IO.File]::ReadAllText("$working\MsgDb\CompatibilityFunctions.sql")}
  if($module -eq 'PDb'){$sql+="`r`n"+[IO.File]::ReadAllText("$working\PDb\NativeProcessSeed.sql")}
  $sourceVersion=if($module -in @('PDb','IDb')){'4.1.0.2'}else{'5.6.6.35'}
  $sql+="`r`nCREATE TABLE etlforge_bootstrap_state(id integer PRIMARY KEY CHECK(id=1), source_version text NOT NULL, created_at timestamptz NOT NULL DEFAULT now());`r`nINSERT INTO etlforge_bootstrap_state(id,source_version) VALUES(1,'$sourceVersion');`r`n"
  $script="C:\ETL\Reports\$database-schema.sql"
  [IO.File]::WriteAllText($script,$sql,[Text.UTF8Encoding]::new($false))
  & $psql -X -w -1 -v ON_ERROR_STOP=1 -h 127.0.0.1 -p 6543 -U $role -d $database -f $script *> "C:\ETL\Reports\$database-schema.log"
  if($LASTEXITCODE -ne 0){throw "Schema transaction rolled back for $database; see Reports/$database-schema.log"}
  "Initialized $database from editable schema sources"
 }
 & $psql -X -w -v ON_ERROR_STOP=1 -h 127.0.0.1 -p 6543 -U $role -d etlforge_dev_tran -f C:\ETL\Server\database\migrations\004-xslt-assets.sql *> C:\ETL\Reports\etlforge-xslt-schema.log
 if($LASTEXITCODE -ne 0){throw 'XSLT schema migration failed; see Reports/etlforge-xslt-schema.log'}
 'XSLT asset schema is ready'
}finally{$env:PGPASSFILE=$previous}