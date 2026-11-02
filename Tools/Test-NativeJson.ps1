$ErrorActionPreference='Stop'
$classes='C:\ETL\Server\build-probe\native-json\java'
New-Item -ItemType Directory -Force $classes | Out-Null
$cp='C:\ETL\Server\dist\lib\*;C:\ETL\Server\dist\webapps\axis\WEB-INF\lib\*;C:\ETL\Studio\lib\jackson-annotations-2.18.12.jar;C:\ETL\Studio\lib\jackson-core-2.18.12.jar;C:\ETL\Studio\lib\jackson-databind-2.18.12.jar'
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -encoding UTF-8 -cp $cp -d $classes C:\ETL\Server\tests\integration\NativeJsonParityTest.java
if($LASTEXITCODE){throw 'JSON parity test compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe -Xmx1g -cp "$classes;$cp" NativeJsonParityTest 2>&1 | Tee-Object C:\ETL\Reports\native-json-parity.log
if($LASTEXITCODE){throw 'JSON parity test failed'}
