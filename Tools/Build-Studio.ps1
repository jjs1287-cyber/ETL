param([string]$Target='compile')
$ErrorActionPreference='Stop'
& 'C:\Tools\aider-env\Scripts\python.exe' C:\ETL\Tools\Generate-JavaScriptPolicy.py
if($LASTEXITCODE -ne 0){throw 'JavaScript policy generation failed'}
if($Target -eq 'jar'){
 $studioProcesses=Get-CimInstance Win32_Process -Filter "Name='java.exe' OR Name='javaw.exe'" | Where-Object {$_.CommandLine -match 'org\.etlforge\.integration\.client\.main\.IntegrationMain'}
 if($studioProcesses){throw 'Close ETLForge before packaging its JARs so the running Java session cannot cache outdated ZIP entries. Compiling sources without packaging remains available.'}
}
if($Target -eq 'jar'){
 $jsLibrary='C:\ETL\Studio\dist\bin\ETLForgeJavaScript.dll'
 $jsSource=Get-ChildItem -LiteralPath C:\ETL\Server\src\native\javascript -File | Sort-Object LastWriteTimeUtc -Descending | Select-Object -First 1
 if(!(Test-Path -LiteralPath $jsLibrary) -or $jsSource.LastWriteTimeUtc -gt (Get-Item -LiteralPath $jsLibrary).LastWriteTimeUtc){
  & C:\ETL\Tools\Build-StudioJavaScript.cmd
  if($LASTEXITCODE -ne 0){throw 'Studio native JavaScript validator build failed'}
  Copy-Item -LiteralPath C:\ETL\Server\build\windows-x64\ETLForgeJavaScript.dll -Destination $jsLibrary -Force
 }
 $nativeSource='C:\ETL\Studio\src\main\native\WindowTheme.cpp'
 $nativeLibrary='C:\ETL\Studio\dist\bin\ETLForgeWindowTheme.dll'
 if(!(Test-Path -LiteralPath $nativeLibrary) -or (Get-Item -LiteralPath $nativeSource).LastWriteTimeUtc -gt (Get-Item -LiteralPath $nativeLibrary).LastWriteTimeUtc){
  & 'C:\ETL\Tools\Build-StudioWindowTheme.cmd'
  if($LASTEXITCODE -ne 0){throw 'Studio x64 window theme build failed'}
 }
}
$env:JAVA_HOME='C:\ETL\Tools\jdk8u504-b01'
$env:ANT_HOME='C:\ETL\Tools\apache-ant-1.10.15'
$log='C:\ETL\Studio\build\ant-'+$Target+'.log'
& "$env:ANT_HOME\bin\ant.bat" -f C:\ETL\Studio\build.xml $Target 2>&1 | Tee-Object $log
$code=$LASTEXITCODE
Set-Content C:\ETL\Studio\build\last-exit-code.txt $code
if($code -ne 0){throw "Studio build failed ($code). See $log"}


