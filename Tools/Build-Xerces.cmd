@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
set "PATH=C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
cmake -S C:\ETL\Dependencies\xerces-c-3.2.5 -B C:\ETL\Dependencies\build\xerces-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=C:/ETL/Dependencies/windows-x64 -DBUILD_TESTING=OFF -DCMAKE_DISABLE_FIND_PACKAGE_ICU=TRUE -DCMAKE_DISABLE_FIND_PACKAGE_CURL=TRUE -Dnetwork-accessor=winsock -Dtranscoder=windows -Dmessage-loader=inmemory
if errorlevel 1 exit /b %errorlevel%
cmake --build C:\ETL\Dependencies\build\xerces-x64 --parallel 2
if errorlevel 1 exit /b %errorlevel%
cmake --install C:\ETL\Dependencies\build\xerces-x64
exit /b %errorlevel%

