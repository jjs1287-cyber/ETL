@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
set "PATH=%ETLFORGE_ROOT%\Server\dist\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_utf8_engine_tests --parallel 4
if errorlevel 1 exit /b %errorlevel%
set "ETLFORGE_HOME=%ETLFORGE_ROOT%\Server\instances\dev"
%ETLFORGE_ROOT%\Server\build-probe\utf8\etlforge_utf8_engine_tests.exe
exit /b %errorlevel%
