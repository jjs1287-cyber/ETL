New-Item -ItemType Directory -Force 'C:\ETL\Log\services' | Out-Null
$ErrorActionPreference='Stop'
$instance='C:\ETL\Server\instances\dev'
$binary='C:\ETL\Server\dist\bin'
& C:\ETL\Tools\Prepare-ETLForgeNativeRuntime.ps1 -Instance $instance
New-Item -ItemType Directory -Force "$instance\services","$instance\logs"|Out-Null
foreach($component in @(@('Scheduler',47106),@('ProcessEngine',47103),@('FileCleanup',47105))){
 $name=$component[0];$port=$component[1];$serviceName="ETLForge.Dev.$name"
 $configuration="$instance\services\$name.ini"
 $content=@"
[Service]
Executable=$binary\ETLForge$name.exe
WorkingDirectory=$instance
Home=$instance
AxisHome=$instance\axis
BinaryDirectory=$binary
ReadyPort=$port
StartupTimeoutSeconds=360
StopTimeoutSeconds=20
StopExecutable=$binary\ETLForgeControl.exe
StopArguments=$name $port Shutdown
Stdout=C:\ETL\Log\services\$name.stdout.log
Stderr=C:\ETL\Log\services\$name.stderr.log
"@
 [IO.File]::WriteAllText($configuration,$content,[Text.Encoding]::Unicode)
 $command='"'+$binary+'\ETLForgeServiceHost.exe" --name '+$serviceName+' --config "'+$configuration+'"'
 $existing=Get-CimInstance Win32_Service -Filter "Name='$serviceName'"
 if($existing){if($existing.PathName -ne $command){throw "Existing service identity differs: $serviceName"}}
 else{New-Service -Name $serviceName -DisplayName "ETLForge Development $name (x64)" -BinaryPathName $command -StartupType Manual -Description 'ETLForge isolated development instance; native x64 service host and owned child process tree.'|Out-Null}
 "Installed/verified $serviceName"
}
