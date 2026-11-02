$ErrorActionPreference='Stop'
$names=@('ETLForge.Dev.NativeAPI','ETLForge.Dev.Scheduler','ETLForge.Dev.FileCleanup','ETLForge.Dev.ProcessEngine')
$running=@($names | Where-Object {(Get-Service -Name $_).Status -eq 'Running'})
try {
 foreach($name in $running){Stop-Service -Name $name; (Get-Service -Name $name).WaitForStatus('Stopped',[TimeSpan]::FromSeconds(30))}
 & C:\ETL\Tools\Build-NativeJsonIntegration.cmd
 if($LASTEXITCODE -ne 0){throw "Native local-variable build failed ($LASTEXITCODE)"}
} finally {
 foreach($name in @('ETLForge.Dev.ProcessEngine','ETLForge.Dev.FileCleanup','ETLForge.Dev.Scheduler','ETLForge.Dev.NativeAPI')){
  if($running -contains $name){Start-Service -Name $name; (Get-Service -Name $name).WaitForStatus('Running',[TimeSpan]::FromSeconds(30))}
 }
}
