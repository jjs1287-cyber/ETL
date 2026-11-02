$ErrorActionPreference='Stop'
$tomcatHome='C:\ETL\Server\runtime\apache-tomcat-9.0.123'
$base='C:\ETL\Server\instances\dev\tomcat'
if(Get-NetTCPConnection -LocalPort 47112 -State Listen -ErrorAction SilentlyContinue){throw 'Port 47112 is already occupied'}
if(!(Test-Path "$base\conf")){
 New-Item -ItemType Directory -Force $base|Out-Null
 Copy-Item -LiteralPath "$tomcatHome\conf" -Destination "$base\conf" -Recurse
 foreach($dir in @('logs','temp','work','webapps','conf\Catalina\localhost')){New-Item -ItemType Directory -Force "$base\$dir"|Out-Null}
 $xml=[Xml.XmlDocument]::new();$xml.Load("$base\conf\server.xml");$xml.Server.SetAttribute('port','-1')
 foreach($connector in @($xml.SelectNodes('/Server/Service/Connector'))){$connector.SetAttribute('address','127.0.0.1');$connector.SetAttribute('port','47112')}
 $xml.Server.Service.Engine.Host.SetAttribute('autoDeploy','false');$xml.Save("$base\conf\server.xml")
 '<Context docBase="C:/ETL/Server/dist/webapps/axis" />' | Set-Content "$base\conf\Catalina\localhost\axis.xml"
}
$argsList=@('-Dfile.encoding=UTF-8','-Xms128m','-Xmx1g',"-Dcatalina.home=$tomcatHome","-Dcatalina.base=$base","-Djava.io.tmpdir=$base\temp",'-Djava.library.path=C:\ETL\Server\dist\bin',"-Djava.util.logging.config.file=$base\conf\logging.properties",'-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager','-cp',"$tomcatHome\bin\bootstrap.jar;$tomcatHome\bin\tomcat-juli.jar",'org.apache.catalina.startup.Bootstrap','start')
$previous=$env:ETLFORGE_HOME
try{
 $env:ETLFORGE_HOME='C:\ETL\Server\instances\dev'
 $process=Start-Process -FilePath C:\ETL\Tools\jdk8u504-b01\bin\java.exe -ArgumentList $argsList -WorkingDirectory $base -WindowStyle Hidden -RedirectStandardOutput "$base\logs\stdout.log" -RedirectStandardError "$base\logs\stderr.log" -PassThru
 $process.Id|Set-Content "$base\tomcat-java.pid"
 "Started isolated ETLForge Tomcat PID $($process.Id), HTTP localhost:47112; no Windows service registered."
}finally{$env:ETLFORGE_HOME=$previous}
