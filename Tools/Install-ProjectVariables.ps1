$ErrorActionPreference='Stop'
$files=@('ETLForgePluginManager.dll','ETLForgeTreeMapperInfoProvider.dll','ETLForgeProcessEngine.exe','ETLForgeOperationEngineProc.exe','ETLForgeScheduler.exe','ETLForgeFileCleanup.exe','ETLForgeControl.exe','ETLForgeDeployInterchangeData.dll','ETLForgeProjectAdministration.dll','ETLForgeAdmin.dll')
$stage='C:\ETL\Server\build\javascript-staging\bin'
$live='C:\ETL\Server\dist\bin'
$backup='C:\ETL\Archives\project-variables-20261029\bin'
New-Item -ItemType Directory -Force $backup | Out-Null
foreach($file in $files){
 if(!(Test-Path -LiteralPath (Join-Path $stage $file))){throw "Missing staged component $file"}
 if(!(Test-Path -LiteralPath (Join-Path $backup $file))){Copy-Item -LiteralPath (Join-Path $live $file) -Destination (Join-Path $backup $file)}
}
$services=@('ETLForge.Dev.NativeAPI','ETLForge.Dev.Scheduler','ETLForge.Dev.FileCleanup','ETLForge.Dev.ProcessEngine')
$running=@($services | Where-Object {(Get-Service -Name $_).Status -eq 'Running'})
try {
 foreach($name in $running){Stop-Service -Name $name;(Get-Service -Name $name).WaitForStatus('Stopped',[TimeSpan]::FromSeconds(30))}
 try {foreach($file in $files){Copy-Item -LiteralPath (Join-Path $stage $file) -Destination (Join-Path $live $file) -Force}}
 catch {foreach($file in $files){Copy-Item -LiteralPath (Join-Path $backup $file) -Destination (Join-Path $live $file) -Force};throw}
} finally {
 foreach($name in @('ETLForge.Dev.ProcessEngine','ETLForge.Dev.FileCleanup','ETLForge.Dev.Scheduler','ETLForge.Dev.NativeAPI')){
  if($running -contains $name){Start-Service -Name $name;(Get-Service -Name $name).WaitForStatus('Running',[TimeSpan]::FromSeconds(30))}
 }
 & C:\ETL\Tools\Build-NativeJavaScript.cmd restore
}
