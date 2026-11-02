@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Dependencies\xerces-c-3.2.5 -B %ETLFORGE_ROOT%\Dependencies\build\xerces-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=%ETLFORGE_ROOT%/Dependencies/windows-x64 -DBUILD_TESTING=OFF -DCMAKE_DISABLE_FIND_PACKAGE_ICU=TRUE -DCMAKE_DISABLE_FIND_PACKAGE_CURL=TRUE -Dnetwork-accessor=winsock -Dtranscoder=windows -Dmessage-loader=inmemory "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Dependencies\build\xerces-x64 --parallel 2
if errorlevel 1 exit /b %errorlevel%
cmake --install %ETLFORGE_ROOT%\Dependencies\build\xerces-x64
exit /b %errorlevel%

