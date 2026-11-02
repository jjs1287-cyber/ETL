@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
set "PATH=C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
cmake -S C:\ETL\Server\src\native\xslt -B C:\ETL\Server\build-probe\native-xslt -G Ninja -DCMAKE_BUILD_TYPE=Release
if errorlevel 1 exit /b %errorlevel%
cmake --build C:\ETL\Server\build-probe\native-xslt --parallel 4
if errorlevel 1 exit /b %errorlevel%
copy /y C:\ETL\Dependencies\saxonc-12.10.1\SaxonCCoreHE\bin\saxonc-core-he.dll C:\ETL\Server\build-probe\native-xslt\bin\ >nul
