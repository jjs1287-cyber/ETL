$ErrorActionPreference='Stop'
$services=@('ETLForge.Dev.Scheduler','ETLForge.Dev.ProcessEngine','ETLForge.Dev.FileCleanup','ETLForge.Dev.NativeAPI','ETLForge.Dev.JavaBackend')
$running=@($services | Where-Object {(Get-Service $_).Status -eq 'Running'})
try {
 foreach($serviceName in $running){Stop-Service $serviceName}
 & C:\ETL\Tools\Build-Utf8Server.cmd *> C:\ETL\Reports\utf8-full-native-build.log
 if($LASTEXITCODE){throw 'UTF8 native build failed; see utf8-full-native-build.log'}
 & C:\ETL\Tools\Build-ServerJava.ps1 *> C:\ETL\Reports\utf8-server-java-build.log
 & C:\ETL\Tools\Package-ServerJava.ps1 *> C:\ETL\Reports\utf8-server-java-package.log
 foreach($moduleName in @('etlforge-util','etlforge-io-utils','etlforge-integration-structure','etlforge-server-backend-ws','etlforge-integration-server-common')) {
  Copy-Item -LiteralPath "C:\ETL\Server\dist\lib\$moduleName.jar" -Destination C:\ETL\Server\dist\webapps\axis\WEB-INF\lib -Force
 }
} finally {
 foreach($serviceName in @('ETLForge.Dev.JavaBackend','ETLForge.Dev.FileCleanup','ETLForge.Dev.ProcessEngine','ETLForge.Dev.Scheduler','ETLForge.Dev.NativeAPI')) {
  if($serviceName -in $running -and (Get-Service $serviceName).Status -ne 'Running'){Start-Service $serviceName}
 }
}
Get-Service ETLForge* | Select-Object Name,Status
