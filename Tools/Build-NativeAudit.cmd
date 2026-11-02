@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Server\src\native\audit -B %ETLFORGE_ROOT%\Server\build-probe\audit-cmake -G Ninja -DCMAKE_BUILD_TYPE=Release "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build-probe\audit-cmake --parallel 4
if errorlevel 1 exit /b %errorlevel%
ctest --test-dir %ETLFORGE_ROOT%\Server\build-probe\audit-cmake --output-on-failure
exit /b %errorlevel%
