$ErrorActionPreference='Stop'
$classes='C:\ETL\Server\build-probe\native-json\java'
$cp='C:\ETL\Server\dist\lib\*;C:\ETL\Server\dist\webapps\axis\WEB-INF\lib\*;C:\ETL\Studio\lib\jackson-annotations-2.18.12.jar;C:\ETL\Studio\lib\jackson-core-2.18.12.jar;C:\ETL\Studio\lib\jackson-databind-2.18.12.jar'
New-Item -ItemType Directory -Force $classes | Out-Null
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -encoding UTF-8 -cp $cp -d $classes C:\ETL\Server\tests\integration\NativeJsonParityTest.java C:\ETL\Server\tests\integration\NativeJsonBenchmark.java
if($LASTEXITCODE){throw 'JSON benchmark compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe -Xmx1g '-Dfile.encoding=UTF-8' -cp "$classes;$cp" NativeJsonBenchmark
if($LASTEXITCODE){throw 'JSON benchmark failed'}
