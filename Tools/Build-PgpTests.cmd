@echo off
call "%~dp0Set-DevEnvironment.cmd"
cd /d %ETLFORGE_ROOT%\Server\build-probe
cl /nologo /EHsc /MD /O2 /std:c++17 /utf-8 /DNOMINMAX %ETLFORGE_ROOT%\Server\src\native\ssh\PgpFiles.cpp %ETLFORGE_ROOT%\Server\tests\native\pgp_files_tests.cpp /Fe:pgp_files_tests.exe /link advapi32.lib ole32.lib
