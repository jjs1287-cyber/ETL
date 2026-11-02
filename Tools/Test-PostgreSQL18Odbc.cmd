@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
if not exist C:\ETL\Server\build-probe\postgresql18 mkdir C:\ETL\Server\build-probe\postgresql18
cl.exe /nologo /EHsc /std:c++14 /MD /Fe:C:\ETL\Server\build-probe\postgresql18\etlforge_postgresql18_odbc_tests.exe /Fo:C:\ETL\Server\build-probe\postgresql18\postgresql18_odbc_tests.obj C:\ETL\Server\tests\native\postgresql18_odbc_tests.cpp odbc32.lib
if errorlevel 1 exit /b %errorlevel%
C:\ETL\Server\build-probe\postgresql18\etlforge_postgresql18_odbc_tests.exe C:\ETL\Server\private\postgresql-18-admin-password.txt
exit /b %errorlevel%
