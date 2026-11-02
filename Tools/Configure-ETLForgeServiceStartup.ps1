$ErrorActionPreference='Stop'
$database='ETLForge.Dev.PostgreSQL18'
$pooler='ETLForge.Dev.PgBouncer'
$api='ETLForge.Dev.NativeAPI'
$hosted=@('ETLForge.Dev.JavaBackend','ETLForge.Dev.Scheduler','ETLForge.Dev.ProcessEngine','ETLForge.Dev.FileCleanup')
if(Get-Service ETLForge.Dev.JmsEngine -ErrorAction SilentlyContinue){$hosted+='ETLForge.Dev.JmsEngine'}
foreach($name in @($database)+$hosted){
 $service=Get-CimInstance Win32_Service -Filter "Name='$name'"
 if(!$service -or !$service.PathName.StartsWith('"C:\ETL\Server\dist\bin\ETLForgeServiceHost.exe" --name '+$name+' --config "C:\ETL\Server\instances\dev\services\')){throw "Unrecognized service identity: $name"}
 if($service.State -ne 'Running'){throw "Verify service health before enabling startup: $name"}
}
$pool=Get-CimInstance Win32_Service -Filter "Name='$pooler'"
if(!$pool -or $pool.PathName -notlike '*C:\ETL\Server\runtime\pgbouncer-1.26.1\bin\pgbouncer.exe*' -or $pool.PathName -notlike '*instances\dev\pgbouncer\pgbouncer.ini*'){throw 'Unrecognized pooler service'}
$native=Get-CimInstance Win32_Service -Filter "Name='$api'"
if(!$native -or $native.PathName -ne '"C:\ETL\Server\runtime\apache-httpd-2.4.69\Apache24\bin\httpd.exe" -k runservice'){throw 'Unrecognized native API service'}
$config=(Get-ItemProperty -LiteralPath "HKLM:\SYSTEM\CurrentControlSet\Services\$api\Parameters" -Name ConfigArgs).ConfigArgs
if(($config -join ' ') -ne '-f C:/ETL/Server/instances/dev/apache/conf/httpd.conf'){throw 'Native API instance differs'}
& sc.exe config $database start= auto
if($LASTEXITCODE){throw 'Database startup configuration failed'}
& sc.exe config $pooler depend= $database start= delayed-auto
if($LASTEXITCODE){throw 'Pooler startup configuration failed'}
foreach($name in $hosted){
 & sc.exe config $name depend= "$database/$pooler" start= delayed-auto
 if($LASTEXITCODE){throw "Cannot configure startup: $name"}
}
& sc.exe config $api depend= "$database/$pooler/ETLForge.Dev.JavaBackend" start= delayed-auto
if($LASTEXITCODE){throw 'Native API startup configuration failed'}
foreach($name in @($database,$pooler,$api)+$hosted){
 & sc.exe failure $name reset= 86400 actions= restart/10000/restart/30000/restart/60000
 if($LASTEXITCODE){throw "Cannot configure recovery: $name"}
 & sc.exe failureflag $name 1
 if($LASTEXITCODE){throw "Cannot enable non-crash recovery actions: $name"}
}
$watchdog='ETLForge.Dev.Watchdog'
if(Get-Service $watchdog -ErrorAction SilentlyContinue){
 & sc.exe config $watchdog start= delayed-auto
 if($LASTEXITCODE){throw 'Cannot configure watchdog startup'}
 & sc.exe failure $watchdog reset= 86400 actions= restart/10000/restart/30000/restart/60000
 if($LASTEXITCODE){throw 'Cannot configure watchdog recovery'}
 & sc.exe failureflag $watchdog 1
 if($LASTEXITCODE){throw 'Cannot enable watchdog non-crash recovery actions'}
}
Get-CimInstance Win32_Service | Where-Object Name -Like 'ETLForge.Dev.*' | Select-Object Name,State,StartMode,DelayedAutoStart,StartName | Export-Csv C:\ETL\Reports\windows-services-status.csv -NoTypeInformation
