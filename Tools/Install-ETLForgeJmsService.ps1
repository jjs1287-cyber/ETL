$ErrorActionPreference='Stop'
$instance='C:\ETL\Server\instances\dev'
$binary='C:\ETL\Server\dist\bin'
$name='ETLForge.Dev.JmsEngine'
$library='C:\ETL\Server\dist\jms-lib'
New-Item -ItemType Directory -Force $library|Out-Null
Get-ChildItem C:\ETL\Server\dist\webapps\axis\WEB-INF\lib -Filter *.jar | Where-Object Name -NotLike 'etlforge*' | Copy-Item -Destination $library
Copy-Item C:\ETL\Dependencies\java\javax.jms-api-2.0.1.jar $library -Force
$content=@"
[Service]
Executable=C:\ETL\Tools\jdk8u504-b01\bin\java.exe
Arguments=-Xmx256m -Detlforge-edition=UNKNOWN -cp "C:\ETL\Server\dist\lib\*;$library\*" org.etlforge.integration.server.jms.main.JmsMain
WorkingDirectory=$instance
Home=$instance
BinaryDirectory=$binary
ReadyPort=47104
StartupTimeoutSeconds=60
StopTimeoutSeconds=20
StopExecutable=$binary\ETLForgeControl.exe
StopArguments=JmsEngine 47104 Shutdown
Stdout=$instance\logs\JmsEngine.stdout.log
Stderr=$instance\logs\JmsEngine.stderr.log
"@
$configuration="$instance\services\JmsEngine.ini"
[IO.File]::WriteAllText($configuration,$content,[Text.Encoding]::Unicode)
$command='"'+$binary+'\ETLForgeServiceHost.exe" --name '+$name+' --config "'+$configuration+'"'
$existing=Get-CimInstance Win32_Service -Filter "Name='$name'"
if($existing){if($existing.PathName -ne $command){throw 'Existing JMS service identity differs'}}
else{New-Service -Name $name -DisplayName 'ETLForge Development JMS Engine (Java x64)' -StartupType Manual -BinaryPathName $command|Out-Null}
$applicationConfig=Join-Path $instance 'etlforge.conf'
$settings=[IO.File]::ReadAllText($applicationConfig)
$settings=[regex]::Replace($settings,'(?ms)(^\[JmsEngine\][^\[]*?^AutoStart\s*=\s*)false','${1}true')
[IO.File]::WriteAllText($applicationConfig,$settings,[Text.UTF8Encoding]::new($false))
Start-Service $name
