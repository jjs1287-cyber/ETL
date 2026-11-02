@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
cl /nologo /EHsc /std:c++14 /I C:\ETL\Server\src\native\integration\konga\TransformationEngine C:\ETL\Server\tests\native\xslt_runtime_tests.cpp /Fe:C:\ETL\Server\build-probe\xslt\xslt_runtime_tests.exe /Fo:C:\ETL\Server\build-probe\xslt\xslt_runtime_tests.obj ole32.lib
if errorlevel 1 exit /b %errorlevel%
C:\ETL\Server\build-probe\xslt\xslt_runtime_tests.exe
