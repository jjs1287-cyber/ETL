$ErrorActionPreference='Stop'
$service='ETLForge.Dev.PostgreSQL18'
$account="NT SERVICE\$service"
$instance='C:\ETL\Server\instances\dev'
$binary='C:\ETL\Server\dist\bin'
$pgsql='C:\ETL\Server\runtime\postgresql-18\pgsql'
$cluster='C:\ETL\Server\data\postgresql-18'
$logs='C:\ETL\Log\postgresql'
if((Get-Content "$cluster\PG_VERSION") -ne '18'){throw 'Unexpected PostgreSQL data directory'}
$content=@"
[Service]
Executable=$pgsql\bin\postgres.exe
Arguments=-D "$cluster"
WorkingDirectory=$cluster
Home=$instance
BinaryDirectory=$pgsql\bin
ReadyPort=6543
StartupTimeoutSeconds=90
StopTimeoutSeconds=60
StopExecutable=$pgsql\bin\pg_ctl.exe
StopArguments=-D "$cluster" -m fast -w stop
Stdout=$logs\postgresql-18-service.stdout.log
Stderr=$logs\postgresql-18-service.stderr.log
"@
$path="$instance\services\PostgreSQL18.ini";[IO.File]::WriteAllText($path,$content,[Text.Encoding]::Unicode)
$command='"'+$binary+'\ETLForgeServiceHost.exe" --name '+$service+' --config "'+$path+'"'
$existing=Get-CimInstance Win32_Service -Filter "Name='$service'"
if($existing){
 if($existing.PathName -ne $command){throw 'Existing PostgreSQL service identity differs'}
 if($existing.State -eq 'Running'){$existing|Select-Object Name,State,StartName,ProcessId;exit}
}else{New-Service -Name $service -DisplayName 'ETLForge Development PostgreSQL 18 (x64)' -BinaryPathName $command -StartupType Manual -Description 'Isolated PostgreSQL 18 cluster on localhost:6543; dedicated virtual service identity.'|Out-Null}
# Virtual service accounts require a NULL password, rather than an empty credential string.
& sc.exe config $service obj= $account *> C:\ETL\Reports\postgresql-service-identity.log
if($LASTEXITCODE -ne 0){throw 'Could not configure the dedicated virtual service identity'}
$sid=([Security.Principal.NTAccount]::new($account)).Translate([Security.Principal.SecurityIdentifier]).Value
foreach($directory in @($cluster,$logs)){
 & icacls.exe $directory /grant "*$($sid):(OI)(CI)(M)" *> C:\ETL\Reports\postgresql-service-acl.log
 if($LASTEXITCODE -ne 0){throw "Could not grant the service identity access to its directory: $directory"}
}
$listeners=@(Get-NetTCPConnection -LocalPort 6543 -State Listen -ErrorAction SilentlyContinue)
if($listeners.Count){
 $clusterProcess=[int](Get-Content "$cluster\postmaster.pid" -First 1)
 foreach($listener in $listeners){if($listener.OwningProcess -ne $clusterProcess){throw 'Port 6543 belongs to a different cluster'}}
 $process=Get-CimInstance Win32_Process -Filter "ProcessId=$clusterProcess"
 if($process.ExecutablePath -ne "$pgsql\bin\postgres.exe"){throw 'PostgreSQL executable identity differs'}
 & "$pgsql\bin\pg_ctl.exe" -D $cluster -m fast -w stop *> C:\ETL\Reports\postgresql-development-process-stop.log
 if($LASTEXITCODE -ne 0){throw 'Could not stop the owned development cluster before service startup'}
}
Start-Service $service
Get-CimInstance Win32_Service -Filter "Name='$service'"|Select-Object Name,State,StartName,ProcessId
