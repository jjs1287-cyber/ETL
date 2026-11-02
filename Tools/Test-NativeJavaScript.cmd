@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
set "PATH=C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
cmake --build C:\ETL\Server\build\windows-x64 --target etlforge_javascript_tests etlforge_studio_javascript --parallel 4
if errorlevel 1 exit /b %errorlevel%
C:\ETL\Server\build\windows-x64\etlforge_javascript_tests.exe

