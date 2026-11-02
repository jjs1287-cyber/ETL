@echo off
call "%~dp0Set-DevEnvironment.cmd"
cl /nologo /EHsc /std:c++14 /DWIN32_LEAN_AND_MEAN /DNOMINMAX %ETLFORGE_ROOT%\Server\tests\native\OperationTempRetentionTest.cpp /Fo%ETLFORGE_ROOT%\Server\build\admin-tests\OperationTempRetentionTest.obj /Fe%ETLFORGE_ROOT%\Server\build\admin-tests\OperationTempRetentionTest.exe
