$ErrorActionPreference='Stop'
. "$PSScriptRoot\DevEnvironment.ps1"
$root="${script:ETLRoot}\Server";$build="$root\build-probe\etlforge-server-java"
$cp="$root\dist\lib\*;$root\dist\lib\xslt\*;"+(Get-Content "$build\classpath.txt" -Raw).Trim()
New-Item -ItemType Directory -Force "$build\test-classes"|Out-Null
Get-ChildItem "$root\tests\java" -Recurse -Filter *.java|ForEach-Object {'"'+$_.FullName.Replace('\','/')+'"'}|Set-Content "$build\test-sources.txt"
& "${script:ETLRoot}\Tools\jdk8u504-b01\bin\javac.exe" -source 8 -target 8 -cp $cp -d "$build\test-classes" "@$build\test-sources.txt"
if($LASTEXITCODE -ne 0){throw 'Server test compilation failed'}
& "${script:ETLRoot}\Tools\jdk8u504-b01\bin\java.exe" -cp "$build\test-classes;$cp" com.etlforge.integration.server.implementation.webservice.bulkloadinfo.ServerJavaSmoke
if($LASTEXITCODE -ne 0){throw 'Server Java smoke test failed'}
