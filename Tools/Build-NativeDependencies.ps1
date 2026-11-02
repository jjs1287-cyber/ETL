$ErrorActionPreference='Stop'
$env:VCPKG_MAX_CONCURRENCY='2'
$env:VCPKG_VISUAL_STUDIO_PATH='C:\Program Files\Microsoft Visual Studio\18\Community'
& C:\ETL\Tools\vcpkg\vcpkg.exe install 'curl[openssl,sspi]:x64-windows' 'openssl:x64-windows' --disable-metrics 2>&1 | Tee-Object C:\ETL\Reports\native-dependency-install.log
if($LASTEXITCODE -ne 0){throw 'Native dependency build failed; see native-dependency-install.log'}
