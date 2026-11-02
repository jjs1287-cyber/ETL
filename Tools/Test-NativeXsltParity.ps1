$ErrorActionPreference='Stop'
$classes='C:\ETL\Server\build-probe\native-xslt'
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -encoding UTF-8 -d $classes C:\ETL\Server\tests\integration\NativeXsltParityTest.java
if($LASTEXITCODE){throw 'XSLT parity harness compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe -cp $classes NativeXsltParityTest 2>&1 | Tee-Object C:\ETL\Reports\native-xslt-parity-speed.log
if($LASTEXITCODE){throw 'XSLT parity or benchmark failed'}
