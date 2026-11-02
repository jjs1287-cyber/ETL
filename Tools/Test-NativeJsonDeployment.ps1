param([string]$Phase='Java')
$ErrorActionPreference='Stop'
$classes='C:\ETL\Server\build-probe\native-json\deployment'
New-Item -ItemType Directory -Force $classes | Out-Null
$cp='C:\ETL\Studio\dist\lib\*;C:\ETL\Studio\lib\*'
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -encoding UTF-8 -cp $cp -d $classes C:\ETL\Server\tests\integration\StudioFileCopySmoke.java C:\ETL\Server\tests\integration\NativeJsonDeploymentTest.java
if($LASTEXITCODE){throw 'JSON deployment test compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe '-Dfile.encoding=UTF-8' -cp "$classes;$cp" NativeJsonDeploymentTest $Phase
if($LASTEXITCODE){throw 'JSON deployment test failed'}
