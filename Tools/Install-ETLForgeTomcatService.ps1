New-Item -ItemType Directory -Force 'C:\ETL\Log\tomcat' | Out-Null
$ErrorActionPreference='Stop'
$instance='C:\ETL\Server\instances\dev';$base="$instance\tomcat"
$tomcatHome='C:\ETL\Server\runtime\apache-tomcat-9.0.123'
$binary='C:\ETL\Server\dist\bin';$service='ETLForge.Dev.JavaBackend'
if(Get-Service $service -ErrorAction SilentlyContinue){throw 'Java backend service already exists; use normal service controls for its lifecycle'}
if(!(Test-Path -LiteralPath "$instance\JdbcDrivers.conf")){
 Copy-Item -LiteralPath 'C:\ETL\Server\features\jdbc\JdbcDrivers.conf' -Destination "$instance\JdbcDrivers.conf"
}
$listeners=@(Get-NetTCPConnection -LocalPort 47112 -State Listen -ErrorAction SilentlyContinue)
foreach($listener in $listeners){
 $process=Get-CimInstance Win32_Process -Filter "ProcessId=$($listener.OwningProcess)"
 if($process.CommandLine -notlike "*-Dcatalina.base=$base *"){throw 'Port 47112 belongs to a different application'}
 Stop-Process -Id $listener.OwningProcess
}
$tokenFile='C:\ETL\Server\private\tomcat-dev-shutdown-token.txt'
if(!(Test-Path $tokenFile)){
 $bytes=New-Object byte[] 16;$random=[Security.Cryptography.RandomNumberGenerator]::Create();try{$random.GetBytes($bytes)}finally{$random.Dispose()}
 [IO.File]::WriteAllText($tokenFile,[BitConverter]::ToString($bytes).Replace('-',''),[Text.UTF8Encoding]::new($false))
}
$xml=[Xml.XmlDocument]::new();$xml.Load("$base\conf\server.xml")
$xml.Server.SetAttribute('port','47115');$xml.Server.SetAttribute('address','127.0.0.1');$xml.Server.SetAttribute('shutdown',([IO.File]::ReadAllText($tokenFile)).Trim());$xml.Save("$base\conf\server.xml")
Set-Acl -LiteralPath "$base\conf\server.xml" -AclObject (Get-Acl "$instance\etlforge.conf")
$common=@('-Dfile.encoding=UTF-8','-Xms128m','-Xmx1g',"-Dcatalina.home=$tomcatHome","-Dcatalina.base=$base","-Djava.io.tmpdir=$base\temp","-Djava.library.path=$binary","-Djava.util.logging.config.file=$base\conf\logging.properties",'-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager','-cp',('"'+$tomcatHome+'\bin\bootstrap.jar;'+$tomcatHome+'\bin\tomcat-juli.jar"'),'org.apache.catalina.startup.Bootstrap') -join ' '
$content=@"
[Service]
Executable=C:\ETL\Tools\jdk8u504-b01\bin\java.exe
Arguments=$common start
WorkingDirectory=$base
Home=$instance
BinaryDirectory=$binary
ReadyPort=47112
StartupTimeoutSeconds=90
StopTimeoutSeconds=30
StopExecutable=C:\ETL\Tools\jdk8u504-b01\bin\java.exe
StopArguments=$common stop
Stdout=C:\ETL\Log\tomcat\service-stdout.log
Stderr=C:\ETL\Log\tomcat\service-stderr.log
"@
$path="$instance\services\JavaBackend.ini";[IO.File]::WriteAllText($path,$content,[Text.Encoding]::Unicode)
$command='"'+$binary+'\ETLForgeServiceHost.exe" --name '+$service+' --config "'+$path+'"'
New-Service -Name $service -DisplayName 'ETLForge Development Java Backend (x64)' -BinaryPathName $command -StartupType Manual -Description 'Rebuilt ETLForge Java SOAP backend on isolated x64 Tomcat.'|Out-Null
Start-Service $service
Get-Service $service|Select-Object Name,Status
