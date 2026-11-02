@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cl /nologo /EHsc /std:c++14 /I %ETLFORGE_ROOT%\Server\src\native\integration\konga\TransformationEngine %ETLFORGE_ROOT%\Server\tests\native\xslt_runtime_tests.cpp /Fe:%ETLFORGE_ROOT%\Server\build-probe\xslt\xslt_runtime_tests.exe /Fo:%ETLFORGE_ROOT%\Server\build-probe\xslt\xslt_runtime_tests.obj ole32.lib
if errorlevel 1 exit /b %errorlevel%
%ETLFORGE_ROOT%\Server\build-probe\xslt\xslt_runtime_tests.exe
