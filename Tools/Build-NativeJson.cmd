@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
if not exist C:\ETL\Server\build-probe\native-json mkdir C:\ETL\Server\build-probe\native-json
cd /d C:\ETL\Server\build-probe\native-json
cl /nologo /utf-8 /EHsc /std:c++17 /O2 /MD /DBOOST_ALL_NO_LIB /DNOMINMAX /I C:\ETL\Dependencies\boost_1_86_0 /I C:\ETL\Dependencies\windows-x64\include /I C:\ETL\Server\src\native\integration C:\ETL\Server\src\native\integration\konga\NativeJson\EtlForgeJsonConversion.cpp C:\ETL\Server\tests\native\JsonConversionCli.cpp /Fe:JsonConversionCli.exe /link /LIBPATH:C:\ETL\Dependencies\windows-x64\lib xerces-c_3.lib ole32.lib psapi.lib
if errorlevel 1 exit /b %errorlevel%
copy /y C:\ETL\Dependencies\windows-x64\bin\xerces-c_3_2.dll . >nul
