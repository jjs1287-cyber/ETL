@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
cl /nologo /EHsc /std:c++14 /DWIN32_LEAN_AND_MEAN /DNOMINMAX C:\ETL\Server\tests\native\OperationTempRetentionTest.cpp /FoC:\ETL\Server\build\admin-tests\OperationTempRetentionTest.obj /FeC:\ETL\Server\build\admin-tests\OperationTempRetentionTest.exe