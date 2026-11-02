@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Dependencies\axis-cpp-1.6b -B %ETLFORGE_ROOT%\Dependencies\build\axis-cpp-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Dependencies\build\axis-cpp-x64 --parallel 4 -- -k 0
exit /b %errorlevel%
