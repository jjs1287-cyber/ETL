@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
set "PATH=C:\ETL\Server\dist\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
cmake --build C:\ETL\Server\build\windows-x64 --target etlforge_utf8_engine_tests --parallel 4
if errorlevel 1 exit /b %errorlevel%
set "ETLFORGE_HOME=C:\ETL\Server\instances\dev"
C:\ETL\Server\build-probe\utf8\etlforge_utf8_engine_tests.exe
exit /b %errorlevel%
