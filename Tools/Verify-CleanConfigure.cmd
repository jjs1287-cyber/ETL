@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S "%ETLFORGE_ROOT%\Server" -B "%ETLFORGE_ROOT%\Reports\configure-clean" -G Ninja -DCMAKE_BUILD_TYPE=Release "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
exit /b %errorlevel%
