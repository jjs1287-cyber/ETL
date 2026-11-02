@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cd /d %ETLFORGE_ROOT%\Server\build-probe
cl /nologo /EHsc /MD /O2 /std:c++17 /utf-8 /DNOMINMAX /I%ETLFORGE_ROOT%\Server\src\native\compression /I%ETLFORGE_ROOT%\Tools\vcpkg\installed\x64-windows\include %ETLFORGE_ROOT%\Server\src\native\compression\EndpointArchive.cpp %ETLFORGE_ROOT%\Server\tests\EndpointArchiveTests.cpp /Fe:EndpointArchiveTests.exe /link %ETLFORGE_ROOT%\Tools\vcpkg\installed\x64-windows\lib\archive.lib user32.lib
exit /b %errorlevel%
