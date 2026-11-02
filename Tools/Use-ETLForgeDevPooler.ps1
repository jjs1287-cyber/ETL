param([ValidateSet(6544,5432)][int]$Port=5432)
$ErrorActionPreference='Stop'
$pool=Get-CimInstance Win32_Service -Filter "Name='ETLForge.Dev.PgBouncer'"
$listener=Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
if(!$pool -or $pool.State -ne 'Running' -or !$listener -or $listener.LocalAddress -ne '127.0.0.1' -or $listener.OwningProcess -ne $pool.ProcessId){throw 'Selected port is not owned by the ETLForge pooler'}
$config='C:\ETL\Server\instances\dev\etlforge.conf'
$backup='C:\ETL\Server\private\etlforge-dev-before-pooling.conf'
$text=[IO.File]::ReadAllText($config)
$pattern='(?ms)(^\[DbInfo\][^\[]*?^Port\s*=\s*)\d+'
if(![regex]::IsMatch($text,$pattern)){throw 'Database port setting not found'}
if(!(Test-Path $backup)){Copy-Item $config $backup;Set-Acl $backup (Get-Acl $config)}
$updated=[regex]::Replace($text,$pattern,('${1}'+$Port))
$services=@('NativeAPI','JmsEngine','FileCleanup','Scheduler','ProcessEngine','JavaBackend')
foreach($name in $services){if(Get-Service "ETLForge.Dev.$name" -ErrorAction SilentlyContinue){Stop-Service "ETLForge.Dev.$name"}}
[IO.File]::WriteAllText($config,$updated,[Text.UTF8Encoding]::new($false))
foreach($name in @('JavaBackend','ProcessEngine','Scheduler','FileCleanup','JmsEngine','NativeAPI')){if(Get-Service "ETLForge.Dev.$name" -ErrorAction SilentlyContinue){Start-Service "ETLForge.Dev.$name"}}
"ETLForge development services now use PgBouncer on loopback port $Port."
