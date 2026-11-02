@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
if "%1"=="compile" (
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_soap_jsonengine etlforge_transformation_engine_probe --parallel 4
) else (
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_server_executables etlforge_frontend_treemapperinfoprovider --parallel 4
)
exit /b %errorlevel%
