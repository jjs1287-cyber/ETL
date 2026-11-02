$ErrorActionPreference='Stop'
$source='C:\ETL\Server\src\java\modules\etlforge-xslt'
$classes='C:\ETL\Server\build-probe\xslt\classes'
$out='C:\ETL\Server\dist\lib\xslt'
New-Item -ItemType Directory -Force $classes,$out | Out-Null
$files=@(Get-ChildItem -LiteralPath $source -Recurse -Filter '*.java' | ForEach-Object FullName)
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -encoding UTF-8 -source 8 -target 8 -d $classes @files
if($LASTEXITCODE){throw 'XSLT Java build failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\jar.exe cf "$out\etlforge-xslt.jar" -C $classes org
if($LASTEXITCODE){throw 'XSLT JAR packaging failed'}
Copy-Item C:\ETL\Dependencies\java\xslt\*.jar -Destination $out
Copy-Item -LiteralPath "$out\etlforge-xslt.jar" -Destination C:\ETL\Server\build-probe\xslt\etlforge-xslt.jar
