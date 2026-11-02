$ErrorActionPreference='Stop'
$original='C:\ETL\Reference\v5\server-windows\Jitterbit Integration Server\db\PostgreSQL'
$working='C:\ETL\Server\src\database\postgresql'
if(Test-Path -LiteralPath $working){throw 'Working database sources already exist; edit them instead of overwriting them.'}
foreach($module in @('TranDb','MsgDb')){
 New-Item -ItemType Directory -Force "$working\$module"|Out-Null
 foreach($file in Get-ChildItem "$original\$module" -File -Filter *.sql){
  $sql=[IO.File]::ReadAllText($file.FullName).Replace('JITTERBIT','ETLFORGE').Replace('Jitterbit','ETLForge').Replace('jitterbit','etlforge')
  $sql=[regex]::Replace($sql,'(?im)^(DROP\s+(?:TABLE|VIEW|INDEX|SEQUENCE|TRIGGER|FUNCTION))\s+(?!IF\s+EXISTS)','$1 IF EXISTS ')
  $sql=$sql.Replace('IF ( SELECT COUNT(*) FROM pg_tables WHERE tablename=table_name ) THEN','IF ( SELECT COUNT(*) > 0 FROM pg_tables WHERE tablename=table_name AND schemaname=current_schema() ) THEN')
  $sql=$sql.Replace('IF ( SELECT COUNT(*) FROM pg_views WHERE viewname=view_name ) THEN','IF ( SELECT COUNT(*) > 0 FROM pg_views WHERE viewname=view_name AND schemaname=current_schema() ) THEN')
  [IO.File]::WriteAllText((Join-Path "$working\$module" $file.Name),$sql,[Text.UTF8Encoding]::new($false))
 }
}
"Staged editable v5 schema with PostgreSQL 18 bootstrap compatibility changes."
