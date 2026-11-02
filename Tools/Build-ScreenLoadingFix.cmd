@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
set "PATH=C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
cmake -S C:\ETL\Server -B C:\ETL\Server\build\windows-x64 -DETLFORGE_JAVASCRIPT_STAGE_BIN=C:/ETL/Server/build/screen-loading-staging/bin
if errorlevel 1 exit /b %errorlevel%
cmake --build C:\ETL\Server\build\windows-x64 --target etlforge_frontend_admin etlforge_server_admin_tests --parallel 4
