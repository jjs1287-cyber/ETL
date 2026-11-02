$ErrorActionPreference='Stop'
$out='C:\ETL\Server\build-probe\etlforge-server-java'
New-Item -ItemType Directory -Force "$out\classes" | Out-Null
$jars=@(Get-ChildItem C:\ETL\Reference\v5\server-windows -Recurse -Filter *.jar)
$thirdParty=@(Get-ChildItem C:\ETL\Reference\v5\studio-windows -Recurse -Filter *.jar | Where-Object {$_.Name -notlike 'jitterbit*'})
$cp=(@($jars | Select-Object -ExpandProperty DirectoryName -Unique | ForEach-Object {Join-Path $_ '*'}) + @($thirdParty | ForEach-Object FullName) + @('C:\ETL\Dependencies\java\jsr305-3.0.2.jar')) -join ';'
Set-Content "$out\classpath.txt" $cp
Get-ChildItem C:\ETL\Server\src\java\modules -Recurse -Filter *.java | ForEach-Object {'"'+$_.FullName.Replace('\','/')+'"'} | Set-Content "$out\sources.txt"
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -J-Xmx1g -encoding UTF-8 -source 8 -target 8 -Xmaxerrs 1000 -cp $cp -d "$out\classes" "@$out\sources.txt" 2>&1 | Tee-Object "$out\compile.log"
$code=$LASTEXITCODE
Set-Content "$out\exit-code.txt" $code
if($code -ne 0){throw "Server Java compilation failed ($code). See $out\compile.log"}
