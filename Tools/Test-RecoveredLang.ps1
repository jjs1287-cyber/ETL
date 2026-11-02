$ErrorActionPreference='Stop'
$jdk='C:\ETL\Tools\jdk8u504-b01'
$out='C:\ETL\Server\build-probe\v5-lang-repaired'
$source='C:\ETL\Server\src\java\lang-utils'
$lib=Get-ChildItem C:\ETL\Reference\v5\server-windows -Recurse -Filter guava*.jar | Select-Object -First 1 -ExpandProperty DirectoryName
New-Item -ItemType Directory -Force "$out\classes","$out\tests" | Out-Null
Get-ChildItem $source -Recurse -Filter *.java | ForEach-Object {'"'+$_.FullName.Replace('\','/')+'"'} | Set-Content "$out\sources.txt"
& "$jdk\bin\javac.exe" -source 7 -target 7 -cp "$lib\*" -d "$out\classes" "@$out\sources.txt" 2>&1 | Tee-Object "$out\compile.log"
if($LASTEXITCODE -ne 0){throw 'Module compilation failed'}
& "$jdk\bin\javac.exe" -cp "$out\classes;$lib\*" -d "$out\tests" "$out\RecoverySmoke.java"
if($LASTEXITCODE -ne 0){throw 'Test compilation failed'}
& "$jdk\bin\java.exe" -cp "$out\tests;$out\classes;$lib\*" RecoverySmoke | Tee-Object "$out\smoke-repaired.log"
if($LASTEXITCODE -ne 0){throw 'Repaired test failed'}
& "$jdk\bin\java.exe" -cp "$out\tests;$lib\*" RecoverySmoke | Tee-Object "$out\smoke-original.log"
if($LASTEXITCODE -ne 0){throw 'Original test failed'}
