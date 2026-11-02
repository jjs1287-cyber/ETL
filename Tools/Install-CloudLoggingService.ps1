param([string]$Instance='C:\ETL\Server\instances\dev',[string]$Name='ETLForge.Dev.CloudLogging')
$ErrorActionPreference='Stop'
$exe='C:\ETL\Server\runtime\cloud-logging\ETLForgeCloudLogging.exe'
if(!(Test-Path -LiteralPath $exe)){throw 'Build the cloud logging service first.'}
if(!(Test-Path -LiteralPath (Join-Path $Instance 'services\CloudLogging.connection'))){throw 'Create the restricted CloudLogging.connection file first.'}
$command='"'+$exe+'" --home "'+$Instance+'" --name "'+$Name+'"'
$existing=Get-CimInstance Win32_Service -Filter "Name='$Name'"
if($existing){if($existing.PathName -ne $command){throw 'Cloud logging service path differs from the requested instance.'}}
else {New-Service -Name $Name -DisplayName 'ETLForge Development Cloud Logging (x64)' -BinaryPathName $command -StartupType Automatic -Description 'Native C++ write-only operation log uploader. Local logging remains independent.' | Out-Null}
& sc.exe config $Name start= delayed-auto | Out-Null
& sc.exe failure $Name reset= 86400 actions= restart/30000/restart/60000/restart/120000 | Out-Null
Start-Service $Name
Get-Service $Name | Select-Object Name,Status
