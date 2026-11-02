@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Server\src\native\xslt -B %ETLFORGE_ROOT%\Server\build-probe\native-xslt -G Ninja -DCMAKE_BUILD_TYPE=Release "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build-probe\native-xslt --parallel 4
if errorlevel 1 exit /b %errorlevel%
copy /y %ETLFORGE_ROOT%\Dependencies\saxonc-12.10.1\SaxonCCoreHE\bin\saxonc-core-he.dll %ETLFORGE_ROOT%\Server\build-probe\native-xslt\bin\ >nul
