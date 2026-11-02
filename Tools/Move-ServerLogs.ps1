$ErrorActionPreference='Stop'
$root='C:\ETL\Log'
$homePath='C:\ETL\Server\instances\dev'
$backup='C:\ETL\Archives\central-logs-20261028'
New-Item -ItemType Directory -Path $backup -Force | Out-Null
$maps=@(
 @("$homePath\logs","$root\services"),
 @("$homePath\log","$root\native"),
 @("$homePath\tomcat\logs","$root\tomcat"),
 @('C:\ETL\Server\logs',"$root\postgresql")
)
$configs=@(Get-ChildItem "$homePath\services\*.ini")+@(Get-Item "$homePath\apache\conf\httpd.conf","$homePath\pgbouncer\pgbouncer.ini","$homePath\tomcat\conf\logging.properties","$homePath\tomcat\conf\server.xml")
foreach($f in $configs){Copy-Item -LiteralPath $f.FullName -Destination (Join-Path $backup $f.Name)}
$start=@('PostgreSQL18','PgBouncer','JavaBackend','NativeAPI','ProcessEngine','Scheduler','FileCleanup','CloudLogging')
$running=@(Get-Service 'ETLForge.Dev.*' | Where-Object Status -eq Running | Select-Object -ExpandProperty Name)
try {
 foreach($suffix in @('CloudLogging','Scheduler','ProcessEngine','NativeAPI','JavaBackend','FileCleanup','PgBouncer','PostgreSQL18')){Stop-Service "ETLForge.Dev.$suffix" -ErrorAction Stop}
 foreach($map in $maps){
  $source=[IO.Path]::GetFullPath($map[0]);$target=[IO.Path]::GetFullPath($map[1])
  if(!$source.StartsWith('C:\ETL\Server\',[StringComparison]::OrdinalIgnoreCase) -or !$target.StartsWith('C:\ETL\Log\',[StringComparison]::OrdinalIgnoreCase)){throw 'Unexpected migration path'}
  if((Get-Item -LiteralPath $source).Attributes -band [IO.FileAttributes]::ReparsePoint){throw "Already redirected: $source"}
  if(Test-Path -LiteralPath $target){throw "Target exists: $target"}
  New-Item -ItemType Directory -Path (Split-Path $target) -Force | Out-Null
  Move-Item -LiteralPath $source -Destination $target
  New-Item -ItemType Junction -Path $source -Target $target | Out-Null
 }
 foreach($component in @('apache','pgbouncer')){New-Item -ItemType Directory -Path "$root\$component" -Force | Out-Null}
 foreach($pair in @(@('NativeAPI.apache.log','apache'),@('PgBouncer.log','pgbouncer'))){$old="$root\services\$($pair[0])";if(Test-Path $old){Move-Item -LiteralPath $old -Destination "$root\$($pair[1])\$($pair[0])"}}
 foreach($f in $configs){
  $text=[IO.File]::ReadAllText($f.FullName)
  $text=$text.Replace('C:\ETL\Server\instances\dev\tomcat\logs\',"$root\tomcat\").Replace('C:\ETL\Server\instances\dev\logs\',"$root\services\").Replace('C:\ETL\Server\logs\',"$root\postgresql\")
  $text=$text.Replace('C:/ETL/Server/instances/dev/logs/NativeAPI.apache.log','C:/ETL/Log/apache/NativeAPI.apache.log').Replace('C:/ETL/Server/instances/dev/logs/PgBouncer.log','C:/ETL/Log/pgbouncer/PgBouncer.log')
  $text=$text.Replace('${catalina.base}/logs','C:/ETL/Log/tomcat').Replace('AccessLogValve" directory="logs"','AccessLogValve" directory="C:/ETL/Log/tomcat"')
  [IO.File]::WriteAllText($f.FullName,$text,[Text.UTF8Encoding]::new($false))
 }
 & icacls.exe "$root\pgbouncer" /grant 'NT SERVICE\ETLForge.Dev.PgBouncer:(OI)(CI)(M)'
 if($LASTEXITCODE){throw 'PgBouncer ACL failed'}
 & 'C:\ETL\Server\runtime\apache-httpd-2.4.69\Apache24\bin\httpd.exe' -t -f "$homePath\apache\conf\httpd.conf"
 if($LASTEXITCODE){throw 'Apache config validation failed'}
} finally {
 foreach($suffix in $start){if($running -contains "ETLForge.Dev.$suffix"){Start-Service "ETLForge.Dev.$suffix" -ErrorAction Continue}}
}
Get-Service 'ETLForge.Dev.*' | Select-Object Name,Status
