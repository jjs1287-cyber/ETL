@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
cd /d C:\ETL\Server\build-probe
cl /nologo /EHsc /MD /O2 /std:c++17 /utf-8 /DNOMINMAX /IC:\ETL\Server\src\native\compression /IC:\ETL\Tools\vcpkg\installed\x64-windows\include C:\ETL\Server\src\native\compression\EndpointArchive.cpp C:\ETL\Server\tests\EndpointArchiveTests.cpp /Fe:EndpointArchiveTests.exe /link C:\ETL\Tools\vcpkg\installed\x64-windows\lib\archive.lib user32.lib
exit /b %errorlevel%
