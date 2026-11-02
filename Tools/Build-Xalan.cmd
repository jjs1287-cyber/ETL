@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
set "PATH=%ETLFORGE_ROOT%\Dependencies\windows-x64\bin;%PATH%"
cmake -S %ETLFORGE_ROOT%\Dependencies\xalan_c-1.12 -B %ETLFORGE_ROOT%\Dependencies\build\xalan-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release -DCMAKE_POLICY_VERSION_MINIMUM=3.5 -DCMAKE_PREFIX_PATH=%ETLFORGE_ROOT%/Dependencies/windows-x64 -DCMAKE_INSTALL_PREFIX=%ETLFORGE_ROOT%/Dependencies/windows-x64 -DCMAKE_DISABLE_FIND_PACKAGE_ICU=TRUE -Dmessage-loader=inmemory "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Dependencies\build\xalan-x64 --target xalan-c --parallel 3
exit /b %errorlevel%

