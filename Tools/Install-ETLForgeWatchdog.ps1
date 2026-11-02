param(
 [string]$Name='ETLForge.Dev.Watchdog',
 [string]$Configuration='C:\ETL\Server\instances\dev\services\Watchdog.ini'
)
$ErrorActionPreference='Stop'
$executable='C:\ETL\Server\dist\bin\ETLForgeWatchdog.exe'
if(!(Test-Path -LiteralPath $executable)){throw 'Build ETLForgeWatchdog.exe first.'}
if(!(Test-Path -LiteralPath $Configuration)){throw "Watchdog configuration does not exist: $Configuration"}
$command='"'+$executable+'" --name "'+$Name+'" --config "'+$Configuration+'"'
$existing=Get-CimInstance Win32_Service -Filter "Name='$Name'"
if($existing){
 if($existing.PathName -ne $command){throw 'The existing watchdog service points to a different executable or configuration.'}
 if($existing.State -eq 'Running'){Stop-Service -Name $Name -Force}
} else {
 New-Service -Name $Name -DisplayName 'ETLForge Service Watchdog (x64)' -BinaryPathName $command -StartupType Automatic -Description 'Independent ETLForge health probes and dependency-aware service recovery.' | Out-Null
}
& sc.exe config $Name start= delayed-auto | Out-Null
& sc.exe failure $Name reset= 86400 actions= restart/10000/restart/30000/restart/60000 | Out-Null
Start-Service -Name $Name
Get-Service -Name $Name | Select-Object Name,Status,StartType
