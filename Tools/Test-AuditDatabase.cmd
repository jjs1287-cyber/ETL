@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
if not exist C:\ETL\Server\build-probe\native-audit mkdir C:\ETL\Server\build-probe\native-audit
cd /d C:\ETL\Server\build-probe\native-audit
cl /nologo /EHsc /std:c++17 /utf-8 /O2 /MD /DBOOST_ALL_NO_LIB /DNOMINMAX /I C:\ETL\Dependencies\boost_1_86_0 /I C:\ETL\Server\src\native\audit C:\ETL\Server\src\native\audit\AuditComparison.cpp C:\ETL\Server\src\native\audit\AuditDatabase.cpp C:\ETL\Server\src\native\audit\AuditObjectReader.cpp C:\ETL\Server\src\native\audit\AuditJson.cpp C:\ETL\Server\tests\native\audit_database_tests.cpp /Fe:AuditDatabaseTests.exe /link bcrypt.lib odbc32.lib
if errorlevel 1 exit /b %errorlevel%
AuditDatabaseTests.exe
exit /b %errorlevel%
