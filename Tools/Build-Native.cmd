@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Server -B %ETLFORGE_ROOT%\Server\build\windows-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --parallel 4 --target all etlforge_server_executables
if errorlevel 1 exit /b %errorlevel%
ctest --test-dir %ETLFORGE_ROOT%\Server\build\windows-x64 --output-on-failure
exit /b %errorlevel%
