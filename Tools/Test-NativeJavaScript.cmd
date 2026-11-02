@echo off
call "%~dp0Set-DevEnvironment.cmd"
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_javascript_tests etlforge_studio_javascript --parallel 4
if errorlevel 1 exit /b %errorlevel%
%ETLFORGE_ROOT%\Server\build\windows-x64\etlforge_javascript_tests.exe

