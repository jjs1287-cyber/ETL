$ErrorActionPreference='Stop'
$bin='C:\ETL\Server\runtime\postgresql-18\pgsql\bin'
$data='C:\ETL\Server\data\postgresql-18'
$private='C:\ETL\Server\private'
$logs='C:\ETL\Server\logs'
New-Item -ItemType Directory -Force $private,$logs,$data|Out-Null
$sid=[Security.Principal.WindowsIdentity]::GetCurrent().User.Value
foreach($folder in @($private,$data)){
 & icacls.exe $folder /inheritance:r /grant:r "*$($sid):(OI)(CI)F" '*S-1-5-18:(OI)(CI)F' | Out-Null
 if($LASTEXITCODE -ne 0){throw "Cannot protect $folder"}
}
$passwordFile=Join-Path $private 'postgresql-18-admin-password.txt'
if(!(Test-Path -LiteralPath $passwordFile)){
 $bytes=New-Object byte[] 32;$rng=[Security.Cryptography.RandomNumberGenerator]::Create();try{$rng.GetBytes($bytes)}finally{$rng.Dispose()}
 [IO.File]::WriteAllText($passwordFile,[Convert]::ToBase64String($bytes),[Text.UTF8Encoding]::new($false))
}
& "$bin\postgres.exe" --version
if($LASTEXITCODE -ne 0){throw 'PostgreSQL binary verification failed'}
if(!(Test-Path -LiteralPath "$data\PG_VERSION")){
 if(@(Get-ChildItem $data -Force).Count -gt 0){throw 'Refusing to initialize a nonempty PostgreSQL data directory'}
 & "$bin\initdb.exe" -D $data -U etlforge_admin "--pwfile=$passwordFile" '--auth-local=scram-sha-256' '--auth-host=scram-sha-256' '--encoding=UTF8' '--locale=C'
 if($LASTEXITCODE -ne 0){throw 'PostgreSQL initialization failed'}
}
if((Get-Content "$data\PG_VERSION" -Raw).Trim() -ne '18'){throw 'Unexpected PostgreSQL data version'}
$config="$data\postgresql.conf"
if(!([IO.File]::ReadAllText($config).Contains('# ETLFORGE_DEVELOPMENT_INSTANCE'))){
 Add-Content $config "`n# ETLFORGE_DEVELOPMENT_INSTANCE`nlisten_addresses = '127.0.0.1'`nport = 6543`nmax_connections = 410`npassword_encryption = 'scram-sha-256'"
}
& "$bin\pg_ctl.exe" status -D $data | Out-Null
if($LASTEXITCODE -ne 0){
 if(@(Get-NetTCPConnection -State Listen -LocalPort 6543 -ErrorAction SilentlyContinue).Count -gt 0){throw 'Port 6543 is already occupied; no existing listener will be stopped'}
 $process=Start-Process -FilePath "$bin\pg_ctl.exe" -ArgumentList @('start','-D',$data,'-l',"$logs\postgresql-18.log",'-w','-t','60') -WindowStyle Hidden -PassThru -RedirectStandardOutput "$logs\pg_ctl-start.out.log" -RedirectStandardError "$logs\pg_ctl-start.err.log"
 $process.WaitForExit()
 if($process.ExitCode -ne 0){throw 'PostgreSQL development instance did not start'}
}
$pgpass=Join-Path $private 'postgresql-18.pgpass'
[IO.File]::WriteAllText($pgpass,'127.0.0.1:6543:*:etlforge_admin:'+([IO.File]::ReadAllText($passwordFile).Trim()),[Text.UTF8Encoding]::new($false))
$previousPassFile=$env:PGPASSFILE;$previousPassword=$env:PGPASSWORD
try{
 $env:PGPASSFILE=$pgpass;Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue
 & "$bin\psql.exe" -h 127.0.0.1 -p 6543 -U etlforge_admin -d postgres -X -w -v ON_ERROR_STOP=1 -c "SELECT version(); CREATE TEMP TABLE etlforge_smoke(id bigint,value text); INSERT INTO etlforge_smoke VALUES (1,'ETLForge'); SELECT id,value FROM etlforge_smoke;"
 if($LASTEXITCODE -ne 0){throw 'PostgreSQL authenticated SQL smoke test failed'}
}finally{$env:PGPASSFILE=$previousPassFile;$env:PGPASSWORD=$previousPassword}
'PostgreSQL 18 development instance verified on 127.0.0.1:6543. No Windows service was registered.'
