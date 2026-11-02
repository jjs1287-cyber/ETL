$ErrorActionPreference='Stop'
$service='ETLForge.Dev.NativeAPI'
$classpath='C:\ETL\Server\build-probe\studio-native-integration;C:\ETL\Studio\configuration;C:\ETL\Studio\dist\lib\*;C:\ETL\Studio\lib\*'
$results=@()
for($cycle=1;$cycle -le 2;$cycle++){
 if((Get-Service $service).Status -ne 'Running'){Start-Service $service}
 & C:\ETL\Tools\jdk8u504-b01\bin\java.exe '-Detlforge.native.api=http://127.0.0.1:47100/axis/' -cp $classpath StudioNativeLoginSmoke
 if($LASTEXITCODE){throw 'Studio-to-native API service test failed'}
 $listener=Get-NetTCPConnection -LocalPort 47100 -State Listen
 if($listener.LocalAddress -ne '127.0.0.1'){throw 'Native API listener is not loopback'}
 $parentId=$listener.OwningProcess
 $ownedChildren=@(Get-CimInstance Win32_Process -Filter "ParentProcessId=$parentId" | Where-Object Name -eq 'httpd.exe' | ForEach-Object ProcessId)
 $timer=[Diagnostics.Stopwatch]::StartNew();Stop-Service $service
 foreach($processId in @($parentId)+$ownedChildren){if(Get-Process -Id $processId -ErrorAction SilentlyContinue){throw 'Apache process remains after service stop'}}
 if(Get-NetTCPConnection -LocalPort 47100 -State Listen -ErrorAction SilentlyContinue){throw 'Native API listener remains after service stop'}
 if($timer.Elapsed.TotalSeconds -ge 30){throw 'Native API stop exceeded timeout'}
 $results+=[pscustomobject]@{Cycle=$cycle;Result='PASS';StopSeconds=[Math]::Round($timer.Elapsed.TotalSeconds,2);Test='Studio native SOAP login, operation tree, graceful service stop/restart'}
 Start-Service $service
}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe '-Detlforge.native.api=http://127.0.0.1:47100/axis/' -cp $classpath StudioNativeLoginSmoke
if($LASTEXITCODE){throw 'Post-restart native API call failed'}
$results|Export-Csv C:\ETL\Reports\native-api-service-tests.csv -NoTypeInformation
$results|Format-Table
