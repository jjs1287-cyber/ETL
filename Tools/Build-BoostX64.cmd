@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cd /d %ETLFORGE_ROOT%\Dependencies\boost_1_86_0
if not exist b2.exe call bootstrap.bat vc143
if errorlevel 1 exit /b %errorlevel%
b2.exe toolset=msvc address-model=64 architecture=x86 variant=release link=static runtime-link=shared threading=multi --with-filesystem --with-thread --with-regex --with-date_time --with-chrono --with-atomic --with-system --stagedir=%ETLFORGE_ROOT%\Dependencies\boost-x64 stage -j2
exit /b %errorlevel%
