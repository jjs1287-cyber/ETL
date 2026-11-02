$ErrorActionPreference='Stop'
$stage='C:\ETL\Server\build\javascript-staging\bin'
$live='C:\ETL\Server\dist\bin'
$backup='C:\ETL\Archives\endpoint-compression-20261029\bin'
New-Item -ItemType Directory -Force $backup | Out-Null
$items=@(Get-ChildItem -LiteralPath $stage -File | Where-Object {$_.Extension -in '.dll','.exe','.so'})
if($items.Count -lt 20){throw 'Incomplete server staging directory'}
$copies=@{}
foreach($item in $items){$copies[$item.Name]=$item.FullName}
foreach($name in @('archive.dll','liblzma.dll')){$copies[$name]=Join-Path 'C:\ETL\Tools\vcpkg\installed\x64-windows\bin' $name}
foreach($name in $copies.Keys){
 if(!(Test-Path -LiteralPath $copies[$name])){throw "Missing staged component $name"}
 $old=Join-Path $live $name
 if((Test-Path -LiteralPath $old) -and !(Test-Path -LiteralPath (Join-Path $backup $name))){Copy-Item -LiteralPath $old -Destination (Join-Path $backup $name)}
}
$services=@('ETLForge.Dev.NativeAPI','ETLForge.Dev.Scheduler','ETLForge.Dev.FileCleanup','ETLForge.Dev.ProcessEngine')
$running=@($services | Where-Object {(Get-Service -Name $_).Status -eq 'Running'})
try {
 foreach($name in $running){Stop-Service -Name $name;(Get-Service -Name $name).WaitForStatus('Stopped',[TimeSpan]::FromSeconds(30))}
 try{foreach($name in $copies.Keys){Copy-Item -LiteralPath $copies[$name] -Destination (Join-Path $live $name) -Force}}
 catch{foreach($name in $copies.Keys){$old=Join-Path $backup $name;if(Test-Path -LiteralPath $old){Copy-Item -LiteralPath $old -Destination (Join-Path $live $name) -Force}};throw}
}finally{
 foreach($name in @('ETLForge.Dev.ProcessEngine','ETLForge.Dev.FileCleanup','ETLForge.Dev.Scheduler','ETLForge.Dev.NativeAPI')){if($running -contains $name){Start-Service -Name $name;(Get-Service -Name $name).WaitForStatus('Running',[TimeSpan]::FromSeconds(30))}}
 & C:\ETL\Tools\Build-NativeJavaScript.cmd restore
}
Write-Output "Installed $($copies.Count) server components"
