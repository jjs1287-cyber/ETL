@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64 >nul
cd /d C:\ETL\Server\build-probe
cl /nologo /EHsc /MD /O2 /std:c++17 /utf-8 /DNOMINMAX C:\ETL\Server\src\native\ssh\PgpFiles.cpp C:\ETL\Server\tests\native\pgp_files_tests.cpp /Fe:pgp_files_tests.exe /link advapi32.lib ole32.lib
