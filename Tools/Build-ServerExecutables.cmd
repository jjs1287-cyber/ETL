@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
call %ETLFORGE_ROOT%\Tools\Build-NativeXslt.cmd
if errorlevel 1 exit /b %errorlevel%
powershell.exe -NoProfile -ExecutionPolicy Bypass -File %ETLFORGE_ROOT%\Tools\Install-NativeXslt.ps1
if errorlevel 1 exit /b %errorlevel%
powershell.exe -NoProfile -ExecutionPolicy Bypass -File %ETLFORGE_ROOT%\Tools\Build-XsltRuntime.ps1
if errorlevel 1 exit /b %errorlevel%
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Server -B %ETLFORGE_ROOT%\Server\build\windows-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_server_executables --parallel 4 -- -k 0
if errorlevel 1 exit /b %errorlevel%
exit /b %errorlevel%

