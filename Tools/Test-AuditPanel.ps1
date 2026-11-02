$ErrorActionPreference='Stop'
& C:\ETL\Tools\Build-Studio.ps1 -Target compile
if($LASTEXITCODE -ne 0){throw 'Studio compilation failed'}
$auditTestClasses='C:\ETL\Server\build-probe\audit-ui'
New-Item -ItemType Directory -Force -Path $auditTestClasses | Out-Null
$auditTestClasspath='C:\ETL\Studio\build\classes;C:\ETL\Studio\lib\*'
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -encoding UTF-8 -cp $auditTestClasspath -d $auditTestClasses C:\ETL\Server\tests\integration\AuditLogPanelTest.java
if($LASTEXITCODE -ne 0){throw 'Audit panel test compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe -cp "$auditTestClasses;$auditTestClasspath" AuditLogPanelTest
if($LASTEXITCODE -ne 0){throw 'Audit panel test failed'}
