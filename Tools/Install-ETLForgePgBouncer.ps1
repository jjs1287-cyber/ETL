New-Item -ItemType Directory -Force 'C:\ETL\Log\pgbouncer' | Out-Null
$ErrorActionPreference='Stop'
$instance='C:\ETL\Server\instances\dev'
$private='C:\ETL\Server\private'
$binary='C:\ETL\Server\runtime\pgbouncer-1.26.1\bin\pgbouncer.exe'
$service='ETLForge.Dev.PgBouncer'
$port=5432
New-Item -ItemType Directory -Force "$instance\pgbouncer"|Out-Null
$secret="$private\etlforge-dev-pool-admin-password.txt"
if(!(Test-Path $secret)){
 $bytes=New-Object byte[] 32;$generator=[Security.Cryptography.RandomNumberGenerator]::Create();try{$generator.GetBytes($bytes)}finally{$generator.Dispose()}
 [IO.File]::WriteAllText($secret,[BitConverter]::ToString($bytes).Replace('-','').ToLowerInvariant())
 Set-Acl $secret (Get-Acl "$private\etlforge-dev-db-password.txt")
}
$app=([IO.File]::ReadAllText("$private\etlforge-dev-db-password.txt")).Trim()
$admin=([IO.File]::ReadAllText($secret)).Trim()
$users="$private\pgbouncer-dev-users.txt"
[IO.File]::WriteAllText($users,'"etlforge_dev" "'+$app+'"'+"`r`n"+'"etlforge_pool_admin" "'+$admin+'"'+"`r`n")
Set-Acl $users (Get-Acl "$private\etlforge-dev-db-password.txt")
$passfile="$private\pgbouncer-dev.pgpass"
[IO.File]::WriteAllText($passfile,"127.0.0.1:$($port):*:etlforge_dev:$app`r`n127.0.0.1:$($port):pgbouncer:etlforge_pool_admin:$admin`r`n")
Set-Acl $passfile (Get-Acl "$private\etlforge-dev-db-password.txt")
$config="$instance\pgbouncer\pgbouncer.ini"
$content=@"
[databases]
etlforge_dev_tran = host=127.0.0.1 port=6543 dbname=etlforge_dev_tran
etlforge_dev_msg = host=127.0.0.1 port=6543 dbname=etlforge_dev_msg
etlforge_dev_pdb = host=127.0.0.1 port=6543 dbname=etlforge_dev_pdb
etlforge_dev_idb = host=127.0.0.1 port=6543 dbname=etlforge_dev_idb

[pgbouncer]
listen_addr = 127.0.0.1
listen_port = $port
auth_type = scram-sha-256
auth_file = C:/ETL/Server/private/pgbouncer-dev-users.txt
admin_users = etlforge_pool_admin
pool_mode = session
max_client_conn = 400
default_pool_size = 20
server_reset_query = DISCARD ALL
ignore_startup_parameters = extra_float_digits
logfile = C:/ETL/Log/pgbouncer/PgBouncer.log
pidfile = C:/ETL/Server/instances/dev/pgbouncer/pgbouncer.pid
service_name = $service
"@
$existing=Get-CimInstance Win32_Service -Filter "Name='$service'"
if($existing){
 if($existing.PathName -notlike ('*'+$binary+'*') -or $existing.PathName -notlike '*instances\dev\pgbouncer\pgbouncer.ini*'){throw 'Existing pooler service identity differs'}
 if([IO.File]::ReadAllText($config).Trim() -ne $content.Trim()){throw 'Inspect existing pooler configuration before replacing it'}
}else{
 if(Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue){throw 'Pooler staging port is occupied'}
 [IO.File]::WriteAllText($config,$content,[Text.UTF8Encoding]::new($false))
 & $binary --regservice $config
 if($LASTEXITCODE){throw 'PgBouncer service registration failed'}
 Set-Service $service -StartupType Manual
}
$account=New-Object Security.Principal.NTAccount("NT SERVICE\$service")
$sid=$account.Translate([Security.Principal.SecurityIdentifier]).Value
& sc.exe config $service obj= "NT SERVICE\$service"
if($LASTEXITCODE){throw 'Pooler virtual service account configuration failed'}
& icacls.exe $users /grant "*$($sid):(R)" *> "$private\pgbouncer-users-acl.log"
if($LASTEXITCODE){throw 'Pooler credential read permission failed'}
& icacls.exe "C:\ETL\Log\pgbouncer" /grant "*$($sid):(OI)(CI)(M)" *> "$private\pgbouncer-logs-acl.log"
if($LASTEXITCODE){throw 'Pooler log permission failed'}
& icacls.exe "$instance\pgbouncer" /grant "*$($sid):(OI)(CI)(M)" *> "$private\pgbouncer-instance-acl.log"
if($LASTEXITCODE){throw 'Pooler PID/config permission failed'}
Start-Service $service
Get-Service $service
