$ErrorActionPreference='Stop'
$binary='C:\ETL\Server\dist\bin'
$instance='C:\ETL\Server\instances\dev'
$results=[Collections.Generic.List[object]]::new()
function Invoke-Control([string]$component,[int]$port,[string]$command){
 $arguments=@($component,$port,$command);if($command -eq 'GetStatus'){$arguments+='0'}
 $lines=@(& "$binary\ETLForgeControl.exe" @arguments 2>&1)
 if($LASTEXITCODE -ne 0){throw "Control failed: $component/$command : $($lines -join ' ')"}
 return $lines
}
function Child-Id([int]$port){
 $listeners=@(Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue)
 if($listeners.Count -ne 1 -or $listeners[0].LocalAddress -ne '127.0.0.1'){throw "Expected one loopback listener on $port"}
 return $listeners[0].OwningProcess
}
$previousHome=$env:ETLFORGE_HOME;$previousPath=$env:PATH
$testService='ETLForge.Test.SchedulerIsolation'
try{
 $env:ETLFORGE_HOME=$instance;$env:PATH=$binary+';'+$previousPath
 foreach($entry in @(@('Scheduler',47106),@('ProcessEngine',47103),@('FileCleanup',47105),@('JmsEngine',47104))){
  $component=$entry[0];$port=$entry[1];$service="ETLForge.Dev.$component"
  if($component -eq 'JmsEngine' -and !(Get-Service $service -ErrorAction SilentlyContinue)){continue}
  for($cycle=1;$cycle -le 2;$cycle++){
   if((Get-Service $service).Status -ne 'Running'){Start-Service $service}
   $child=Child-Id $port
   Invoke-Control $component $port Start|Out-Null
   $status=Invoke-Control $component $port GetStatus
   Start-Sleep -Seconds 2
   $status=Invoke-Control $component $port GetStatus
   if(([int]$status[0] -band 2) -and !([int]$status[0] -band 8)){}else{throw "$component did not enter a healthy running state: $status"}
   $timer=[Diagnostics.Stopwatch]::StartNew();Stop-Service $service
   if(Get-Process -Id $child -ErrorAction SilentlyContinue){throw "Owned child remains after stopping $service"}
   if(Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue){throw "Port remained occupied after stopping $service"}
   if($timer.Elapsed.TotalSeconds -ge 20){throw "Stop reached the forced-termination timeout: $service"}
   $results.Add([pscustomobject]@{Test='start/stop/restart';Component=$component;Cycle=$cycle;StopSeconds=[math]::Round($timer.Elapsed.TotalSeconds,2);Result='PASS'})
   Start-Service $service
  }
 }
 $isolated='C:\ETL\Server\instances\isolation-check'
 if((Test-Path $isolated) -and !(Test-Path "$isolated\.etlforge-service-isolation-test")){throw 'Refusing to overwrite an unrecognized instance directory'}
 New-Item -ItemType Directory -Force "$isolated\logs","$isolated\log"|Out-Null
 'Managed ETLForge service isolation test'|Set-Content "$isolated\.etlforge-service-isolation-test"
 $config=[IO.File]::ReadAllText("$instance\etlforge.conf").Replace('CommandPort = 47106','CommandPort = 47206')
 [IO.File]::WriteAllText("$isolated\etlforge.conf",$config,[Text.UTF8Encoding]::new($false))
 Set-Acl -LiteralPath "$isolated\etlforge.conf" -AclObject (Get-Acl "$instance\etlforge.conf")
 $serviceConfig=@"
[Service]
Executable=$binary\ETLForgeScheduler.exe
WorkingDirectory=$isolated
Home=$isolated
AxisHome=$instance\axis
BinaryDirectory=$binary
ReadyPort=47206
StopTimeoutSeconds=20
StopExecutable=$binary\ETLForgeControl.exe
StopArguments=Scheduler 47206 Shutdown
Stdout=$isolated\logs\Scheduler.stdout.log
Stderr=$isolated\logs\Scheduler.stderr.log
"@
 $path="$isolated\Scheduler.ini";[IO.File]::WriteAllText($path,$serviceConfig,[Text.Encoding]::Unicode)
 if(Get-Service $testService -ErrorAction SilentlyContinue){throw 'Test service already exists; inspect its identity before reuse'}
 New-Service -Name $testService -DisplayName 'ETLForge temporary instance isolation check' -StartupType Manual -BinaryPathName ('"'+$binary+'\ETLForgeServiceHost.exe" --name '+$testService+' --config "'+$path+'"')|Out-Null
 $primary=Child-Id 47106
 Start-Service $testService;$secondary=Child-Id 47206
 if($primary -eq $secondary){throw 'Instances do not have separate processes'}
 Stop-Service $testService
 if(!(Get-Process -Id $primary -ErrorAction SilentlyContinue)){throw 'Stopping the second instance stopped the primary'}
 Invoke-Control Scheduler 47106 GetStatus|Out-Null
 if(Get-Process -Id $secondary -ErrorAction SilentlyContinue){throw 'Second instance child was not reaped'}
 $results.Add([pscustomobject]@{Test='instance isolation';Component='Scheduler';Cycle=1;StopSeconds=$null;Result='PASS'})
 $results|Export-Csv C:\ETL\Reports\windows-service-lifecycle-tests.csv -NoTypeInformation
 $results|Format-Table
}finally{
 $candidate=Get-CimInstance Win32_Service -Filter "Name='$testService'"
 if($candidate -and $candidate.PathName -like '*C:\ETL\Server\instances\isolation-check\Scheduler.ini*'){
  if($candidate.State -ne 'Stopped'){Stop-Service $testService}
  & sc.exe delete $testService|Out-Null
 }
 $env:ETLFORGE_HOME=$previousHome;$env:PATH=$previousPath
}
