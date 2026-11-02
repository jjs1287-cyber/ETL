$ErrorActionPreference='Stop'
$original='C:\ETL\Reference\v4\jitterbit-code-r45309-branches-v4-4.1.x\integration\install\server\files\KONGA_HOME\Db\PostgreSQL'
$working='C:\ETL\Server\src\database\postgresql'
foreach($module in @('PDb','IDb')){
 if(Test-Path "$working\$module"){throw "Editable schema already exists: $module"}
 New-Item -ItemType Directory -Force "$working\$module"|Out-Null
 $sql=[IO.File]::ReadAllText("$original\$module\CreateDb.sql").Replace('JITTERBIT','ETLFORGE').Replace('Jitterbit','ETLForge').Replace('jitterbit','etlforge')
 $sql=[regex]::Replace($sql,'(?im)^(DROP\s+(?:TABLE|VIEW|INDEX|SEQUENCE|TRIGGER|FUNCTION))\s+(?!IF\s+EXISTS)','$1 IF EXISTS ')
 $sql=[regex]::Replace($sql,'(?im)^\s*(?:START\s+TRANSACTION|COMMIT|END\s+TRANSACTION)\s*;\s*$','')
 $sql=$sql.Replace('IF ( SELECT COUNT(*) FROM pg_tables WHERE tablename=table_name ) THEN','IF ( SELECT COUNT(*) > 0 FROM pg_tables WHERE tablename=table_name AND schemaname=current_schema() ) THEN')
 $sql=$sql.Replace('IF ( SELECT COUNT(*) FROM pg_views WHERE viewname=view_name ) THEN','IF ( SELECT COUNT(*) > 0 FROM pg_views WHERE viewname=view_name AND schemaname=current_schema() ) THEN')
 [IO.File]::WriteAllText("$working\$module\CreateDb.sql",$sql,[Text.UTF8Encoding]::new($false))
}
