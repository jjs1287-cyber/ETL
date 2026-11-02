$ErrorActionPreference='Stop'
$out='C:\ETL\Server\build-probe\postgresql18-jdbc'
$driver='C:\ETL\Dependencies\java\postgresql-42.7.14.jar'
New-Item -ItemType Directory -Force $out|Out-Null
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -source 8 -target 8 -cp $driver -d $out C:\ETL\Server\tests\java\etlforge\database\PostgreSQL18JdbcSmoke.java
if($LASTEXITCODE -ne 0){throw 'JDBC smoke test compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe -cp "$out;$driver" etlforge.database.PostgreSQL18JdbcSmoke C:\ETL\Server\private\postgresql-18-admin-password.txt
if($LASTEXITCODE -ne 0){throw 'PostgreSQL 18 JDBC smoke test failed'}
