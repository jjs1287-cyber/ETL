@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
if not exist %ETLFORGE_ROOT%\Server\build-probe\native-audit mkdir %ETLFORGE_ROOT%\Server\build-probe\native-audit
cd /d %ETLFORGE_ROOT%\Server\build-probe\native-audit
cl /nologo /EHsc /std:c++17 /utf-8 /O2 /MD /DBOOST_ALL_NO_LIB /DNOMINMAX /I %ETLFORGE_ROOT%\Dependencies\boost_1_86_0 /I %ETLFORGE_ROOT%\Server\src\native\audit %ETLFORGE_ROOT%\Server\src\native\audit\AuditComparison.cpp %ETLFORGE_ROOT%\Server\src\native\audit\AuditJson.cpp %ETLFORGE_ROOT%\Server\tests\native\audit_comparison_tests.cpp /Fe:AuditComparisonTests.exe /link bcrypt.lib
if errorlevel 1 exit /b %errorlevel%
AuditComparisonTests.exe
exit /b %errorlevel%
