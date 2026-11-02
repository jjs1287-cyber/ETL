@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
if not exist C:\ETL\Studio\dist\bin mkdir C:\ETL\Studio\dist\bin
cl /nologo /LD /EHsc /O2 /MD /I C:\ETL\Tools\jdk8u504-b01\include /I C:\ETL\Tools\jdk8u504-b01\include\win32 C:\ETL\Studio\src\main\native\WindowTheme.cpp /FoC:\ETL\Studio\build\WindowTheme.obj /link C:\ETL\Tools\jdk8u504-b01\lib\jawt.lib dwmapi.lib /IMPLIB:C:\ETL\Studio\build\ETLForgeWindowTheme.lib /OUT:C:\ETL\Studio\dist\bin\ETLForgeWindowTheme.dll
exit /b %errorlevel%

