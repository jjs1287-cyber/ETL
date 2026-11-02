@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
set "PATH=C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
set "PATH=C:\ETL\Dependencies\windows-x64\bin;%PATH%"
cmake -S C:\ETL\Dependencies\xalan_c-1.12 -B C:\ETL\Dependencies\build\xalan-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release -DCMAKE_POLICY_VERSION_MINIMUM=3.5 -DCMAKE_PREFIX_PATH=C:/ETL/Dependencies/windows-x64 -DCMAKE_INSTALL_PREFIX=C:/ETL/Dependencies/windows-x64 -DCMAKE_DISABLE_FIND_PACKAGE_ICU=TRUE -Dmessage-loader=inmemory
if errorlevel 1 exit /b %errorlevel%
cmake --build C:\ETL\Dependencies\build\xalan-x64 --target xalan-c --parallel 3
exit /b %errorlevel%

