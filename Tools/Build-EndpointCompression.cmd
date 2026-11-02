@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Server -B %ETLFORGE_ROOT%\Server\build\windows-x64 -DETLFORGE_JAVASCRIPT_STAGE_BIN=%ETLFORGE_ROOT%/Server/build/javascript-staging/bin
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_server_executables etlforge_frontend_services etlforge_endpoint_archive_tests --parallel 4
exit /b %errorlevel%
