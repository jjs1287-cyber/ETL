@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
set "PATH=C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
cmake -S C:\ETL\Server\src\native\audit -B C:\ETL\Server\build-probe\audit-cmake -G Ninja -DCMAKE_BUILD_TYPE=Release
if errorlevel 1 exit /b %errorlevel%
cmake --build C:\ETL\Server\build-probe\audit-cmake --parallel 4
if errorlevel 1 exit /b %errorlevel%
ctest --test-dir C:\ETL\Server\build-probe\audit-cmake --output-on-failure
exit /b %errorlevel%
