@echo off
call "%~dp0Set-DevEnvironment.cmd"
cmake -S %ETLFORGE_ROOT%\Server -B %ETLFORGE_ROOT%\Server\build\windows-x64 -DETLFORGE_JAVASCRIPT_STAGE_BIN=%ETLFORGE_ROOT%/Server/build/screen-loading-staging/bin
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_frontend_admin etlforge_server_admin_tests --parallel 4
