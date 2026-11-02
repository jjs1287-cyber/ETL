@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
cd /d C:\ETL\Dependencies\boost_1_86_0
if not exist b2.exe call bootstrap.bat vc143
if errorlevel 1 exit /b %errorlevel%
b2.exe toolset=msvc address-model=64 architecture=x86 variant=release link=static runtime-link=shared threading=multi --with-filesystem --with-thread --with-regex --with-date_time --with-chrono --with-atomic --with-system --stagedir=C:\ETL\Dependencies\boost-x64 stage -j2
exit /b %errorlevel%
