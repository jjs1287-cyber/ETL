@echo off
call C:\ETL\Tools\Build-NativeXslt.cmd
if errorlevel 1 exit /b %errorlevel%
powershell.exe -NoProfile -ExecutionPolicy Bypass -File C:\ETL\Tools\Install-NativeXslt.ps1
if errorlevel 1 exit /b %errorlevel%
powershell.exe -NoProfile -ExecutionPolicy Bypass -File C:\ETL\Tools\Build-XsltRuntime.ps1
if errorlevel 1 exit /b %errorlevel%
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
set "PATH=C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
cmake -S C:\ETL\Server -B C:\ETL\Server\build\windows-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release
if errorlevel 1 exit /b %errorlevel%
cmake --build C:\ETL\Server\build\windows-x64 --target etlforge_server_executables --parallel 4 -- -k 0
if errorlevel 1 exit /b %errorlevel%
exit /b %errorlevel%

