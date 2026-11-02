@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Server -B %ETLFORGE_ROOT%\Server\build\windows-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_frontend_services etlforge_frontend_auth_smoke etlforge_frontend_checkout_smoke etlforge_frontend_http_fixture --parallel 4 -- -k 0
if errorlevel 1 exit /b %errorlevel%
exit /b %errorlevel%

