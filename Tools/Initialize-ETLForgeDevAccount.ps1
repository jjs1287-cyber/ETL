$ErrorActionPreference='Stop'
$private='C:\ETL\Server\private'
$instance='C:\ETL\Server\instances\dev'
$psql='C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe'
function Get-DevSecret([string]$name){
 $path=Join-Path $private $name
 if(!(Test-Path -LiteralPath $path)){
  $bytes=New-Object byte[] 16;$rng=[Security.Cryptography.RandomNumberGenerator]::Create();try{$rng.GetBytes($bytes)}finally{$rng.Dispose()}
  [IO.File]::WriteAllText($path,([BitConverter]::ToString($bytes).Replace('-','').ToLowerInvariant()),[Text.UTF8Encoding]::new($false))
 }
 return ([IO.File]::ReadAllText($path)).Trim()
}
function Get-LegacyPasswordHash([string]$value){
 $md5=[Security.Cryptography.MD5]::Create();try{return [BitConverter]::ToString($md5.ComputeHash([Text.Encoding]::UTF8.GetBytes($value))).Replace('-','').ToLowerInvariant()}finally{$md5.Dispose()}
}
$admin=Get-DevSecret 'etlforge-dev-studio-admin-password.txt'
$internal=Get-DevSecret 'etlforge-dev-internal-password.txt'
$anonymous=Get-DevSecret 'etlforge-dev-disabled-anonymous-password.txt'
$template=@'
INSERT INTO UsersTab(User_ID,First_Name,Last_Name,Login,Password,Access_Level) VALUES
(-2,'Disabled','Anonymous','Anonymous','__ANONYMOUS__',0),
(-1,'ETLForge','Internal','__SYS_CR_INTERNAL','__INTERNAL__',0),
(0,'ETLForge','Administrator','Admin','__ADMIN__',0)
ON CONFLICT(User_ID) DO NOTHING;
INSERT INTO GroupsTab(Group_ID,Group_Name,Description) VALUES(0,'Administrators','ETLForge administrators') ON CONFLICT(Group_ID) DO NOTHING;
INSERT INTO User_GroupTab(User_GroupID,User_ID,Group_ID) VALUES(0,0,0) ON CONFLICT(User_GroupID) DO NOTHING;
INSERT INTO GuidsForUsersTab(Guid,Item_ID) SELECT gen_random_uuid()::text,User_ID FROM UsersTab WHERE User_ID IN (-2,-1,0) ON CONFLICT(Item_ID) DO NOTHING;
INSERT INTO GuidsForGroupsTab(Guid,Item_ID) SELECT gen_random_uuid()::text,Group_ID FROM GroupsTab WHERE Group_ID=0 ON CONFLICT(Item_ID) DO NOTHING;
'@
$sql=$template.Replace('__ADMIN__',(Get-LegacyPasswordHash $admin)).Replace('__INTERNAL__',(Get-LegacyPasswordHash $internal)).Replace('__ANONYMOUS__',(Get-LegacyPasswordHash $anonymous))
$seedFile="$private\etlforge-dev-account-seed.sql"
[IO.File]::WriteAllText($seedFile,$sql,[Text.UTF8Encoding]::new($false))
$previous=$env:PGPASSFILE
try{
 $env:PGPASSFILE="$private\etlforge-dev.pgpass"
 & $psql -X -w -1 -v ON_ERROR_STOP=1 -h 127.0.0.1 -p 6543 -U etlforge_dev -d etlforge_dev_tran -f $seedFile *> "$private\etlforge-dev-account-seed.log"
 if($LASTEXITCODE -ne 0){throw 'Application account seed failed; inspect the private seed log with credentials redacted'}
}finally{$env:PGPASSFILE=$previous}
$config=@'
[DbInfo]
Driver = 'PostgreSQL Unicode(x64)'
JdbcDriverClass = org.postgresql.Driver
JdbcDriverName = postgresql
Server = 127.0.0.1
Port = 6543
User = etlforge_dev
Password = __DB_PASSWORD__
TranDb = etlforge_dev_tran
MsgDb = etlforge_dev_msg
PDb = etlforge_dev_pdb
IDb = etlforge_dev_idb
ClientEncoding = UTF8

[Scheduler]
AutoStart = false
CommandPort = 47106

[ProcessEngine]
AutoStart = false
CommandPort = 47103

[JavaBackend]
Port = 47112

[JmsEngine]
CommandPort = 47104
'@
$configFile="$instance\etlforge.conf"
if(!(Test-Path -LiteralPath $configFile)){
 $password=([IO.File]::ReadAllText("$private\etlforge-dev-db-password.txt")).Trim()
 [IO.File]::WriteAllText($configFile,$config.Replace('__DB_PASSWORD__',$password),[Text.UTF8Encoding]::new($false))
 $sid=[Security.Principal.WindowsIdentity]::GetCurrent().User.Value
 & icacls.exe $configFile /inheritance:r /grant:r "*$($sid):(F)" '*S-1-5-18:(F)' *> "$private\etlforge-dev-config-acl.log"
 if($LASTEXITCODE -ne 0){throw 'Could not restrict development configuration permissions'}
}
if(Test-Path -LiteralPath $configFile){
 $existing=[IO.File]::ReadAllText($configFile)
 if($existing -notmatch '(?m)^PDb\s*='){$existing=$existing.Replace('MsgDb = etlforge_dev_msg','MsgDb = etlforge_dev_msg'+"`r`nPDb = etlforge_dev_pdb`r`nIDb = etlforge_dev_idb");[IO.File]::WriteAllText($configFile,$existing,[Text.UTF8Encoding]::new($false))}
}
foreach($dir in @('log','DataInterchange','DataInterchange\Temp','DataInterchange\UserFiles','DataInterchange\UserFiles\ResultFiles')){New-Item -ItemType Directory -Force "$instance\$dir"|Out-Null}
'Initialized the development Admin account with a generated private password and application database configuration.'
