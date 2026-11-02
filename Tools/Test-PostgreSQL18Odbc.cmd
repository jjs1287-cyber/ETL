@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
if not exist %ETLFORGE_ROOT%\Server\build-probe\postgresql18 mkdir %ETLFORGE_ROOT%\Server\build-probe\postgresql18
cl.exe /nologo /EHsc /std:c++14 /MD /Fe:%ETLFORGE_ROOT%\Server\build-probe\postgresql18\etlforge_postgresql18_odbc_tests.exe /Fo:%ETLFORGE_ROOT%\Server\build-probe\postgresql18\postgresql18_odbc_tests.obj %ETLFORGE_ROOT%\Server\tests\native\postgresql18_odbc_tests.cpp odbc32.lib
if errorlevel 1 exit /b %errorlevel%
%ETLFORGE_ROOT%\Server\build-probe\postgresql18\etlforge_postgresql18_odbc_tests.exe %ETLFORGE_ROOT%\Server\private\postgresql-18-admin-password.txt
exit /b %errorlevel%
