@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
if not exist %ETLFORGE_ROOT%\Studio\dist\bin mkdir %ETLFORGE_ROOT%\Studio\dist\bin
cl /nologo /LD /EHsc /O2 /MD /I %ETLFORGE_ROOT%\Tools\jdk8u504-b01\include /I %ETLFORGE_ROOT%\Tools\jdk8u504-b01\include\win32 %ETLFORGE_ROOT%\Studio\src\main\native\WindowTheme.cpp /Fo%ETLFORGE_ROOT%\Studio\build\WindowTheme.obj /link %ETLFORGE_ROOT%\Tools\jdk8u504-b01\lib\jawt.lib dwmapi.lib /IMPLIB:%ETLFORGE_ROOT%\Studio\build\ETLForgeWindowTheme.lib /OUT:%ETLFORGE_ROOT%\Studio\dist\bin\ETLForgeWindowTheme.dll
exit /b %errorlevel%

