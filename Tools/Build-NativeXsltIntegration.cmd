@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_operation_worker etlforge_frontend_treemapperinfoprovider --parallel 4
exit /b %errorlevel%
