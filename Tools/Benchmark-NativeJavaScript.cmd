@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_javascript_benchmark --parallel 4
if errorlevel 1 exit /b %errorlevel%
%ETLFORGE_ROOT%\Server\build\windows-x64\etlforge_javascript_benchmark.exe
exit /b %errorlevel%
