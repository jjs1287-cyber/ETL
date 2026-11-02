@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
if not exist %ETLFORGE_ROOT%\Server\build-probe\native-json mkdir %ETLFORGE_ROOT%\Server\build-probe\native-json
cd /d %ETLFORGE_ROOT%\Server\build-probe\native-json
cl /nologo /utf-8 /EHsc /std:c++17 /O2 /MD /DBOOST_ALL_NO_LIB /DNOMINMAX /I %ETLFORGE_ROOT%\Dependencies\boost_1_86_0 /I %ETLFORGE_ROOT%\Dependencies\windows-x64\include /I %ETLFORGE_ROOT%\Server\src\native\integration %ETLFORGE_ROOT%\Server\src\native\integration\konga\NativeJson\EtlForgeJsonConversion.cpp %ETLFORGE_ROOT%\Server\tests\native\JsonConversionCli.cpp /Fe:JsonConversionCli.exe /link /LIBPATH:%ETLFORGE_ROOT%\Dependencies\windows-x64\lib xerces-c_3.lib ole32.lib psapi.lib
if errorlevel 1 exit /b %errorlevel%
copy /y %ETLFORGE_ROOT%\Dependencies\windows-x64\bin\xerces-c_3_2.dll . >nul
