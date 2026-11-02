$ErrorActionPreference='Stop'
$name='ETLForge.Dev.NativeAPI'
$executable='C:\ETL\Server\runtime\apache-httpd-2.4.69\Apache24\bin\httpd.exe'
$configuration='C:/ETL/Server/instances/dev/apache/conf/httpd.conf'
if(!(Test-Path $executable) -or !(Test-Path $configuration)){throw 'Stage Apache x64 and the ETLForge instance configuration first'}
& $executable -t -f $configuration
if($LASTEXITCODE){throw 'Apache configuration validation failed'}
$existing=Get-CimInstance Win32_Service -Filter "Name='$name'"
if($existing){
 if($existing.PathName -ne ('"'+$executable+'" -k runservice')){throw 'Existing service executable differs'}
 $arguments=(Get-ItemProperty -LiteralPath "HKLM:\SYSTEM\CurrentControlSet\Services\$name\Parameters" -Name ConfigArgs).ConfigArgs
 if(($arguments -join ' ') -ne ('-f '+$configuration)){throw 'Existing service instance configuration differs'}
}else{
 & $executable -k install -n $name -f $configuration
 if($LASTEXITCODE){throw 'Native API service installation failed'}
}
& sc.exe config $name DisplayName= 'ETLForge Development Native API (Apache x64)' depend= 'ETLForge.Dev.PostgreSQL18/ETLForge.Dev.JavaBackend' start= delayed-auto
if($LASTEXITCODE){throw 'Native API startup configuration failed'}
& sc.exe failure $name reset= 86400 actions= restart/10000/restart/30000/restart/60000
if($LASTEXITCODE){throw 'Native API recovery configuration failed'}
if((Get-Service $name).Status -ne 'Running'){Start-Service $name}
Get-Service $name
