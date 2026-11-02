$ErrorActionPreference='Stop'
$out='C:\ETL\Server\build-probe\etlforge-server-java'
$classes=[IO.Path]::GetFullPath((Join-Path $out 'classes'))
if($classes -cne 'C:\ETL\Server\build-probe\etlforge-server-java\classes'){throw 'Unexpected server build output'}
if(Test-Path -LiteralPath $classes){Remove-Item -LiteralPath $classes -Recurse -Force}
New-Item -ItemType Directory -Force $classes | Out-Null
$jars=@(Get-ChildItem C:\ETL\Reference\v5\server-windows -Recurse -Filter *.jar | Where-Object {$_.Name -notlike 'jitterbit*' -and $_.Name -notlike '*sqlite*' -and $_.Name -notlike 'postgresql*' -and $_.Name -notin @('wsc-22.jar','sfdc-partner-wsdl-24.jar','force-wsc-29.0.0.jar','sfdc-partner-wsdl-29.jar') -and $_.Name -notmatch '^(adapter_core|ebs_concurrent|sapcommon|sapidoc|sapidoc3|sapjco3|saprfc)\.jar$'})
$thirdParty=@(Get-ChildItem C:\ETL\Reference\v5\studio-windows -Recurse -Filter *.jar | Where-Object {$_.Name -notlike 'jitterbit*' -and $_.Name -notlike '*sqlite*' -and $_.Name -notlike 'postgresql*' -and $_.Name -notin @('wsc-22.jar','sfdc-partner-wsdl-24.jar','force-wsc-29.0.0.jar','sfdc-partner-wsdl-29.jar') -and $_.Name -notmatch '^(adapter_core|ebs_concurrent|sapcommon|sapidoc|sapidoc3|sapjco3|saprfc)\.jar$'})
$cp=(@($jars | ForEach-Object FullName) + @($thirdParty | ForEach-Object FullName) + @(Get-ChildItem C:\ETL\Dependencies\java\salesforce -Filter *.jar | ForEach-Object FullName) + @('C:\ETL\Dependencies\java\javax.jms-api-2.0.1.jar','C:\ETL\Dependencies\java\jsr305-3.0.2.jar','C:\ETL\Dependencies\java\postgresql-42.7.14.jar')) -join ';'
Set-Content "$out\classpath.txt" $cp
Get-ChildItem C:\ETL\Server\src\java\modules -Recurse -Filter *.java | ForEach-Object {'"'+$_.FullName.Replace('\','/')+'"'} | Set-Content "$out\sources.txt"
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -J-Xmx1g -encoding UTF-8 -source 8 -target 8 -Xmaxerrs 1000 -cp $cp -d "$out\classes" "@$out\sources.txt" 2>&1 | Tee-Object "$out\compile.log"
$code=$LASTEXITCODE
Set-Content "$out\exit-code.txt" $code
if($code -ne 0){throw "Server Java compilation failed ($code). See $out\compile.log"}
