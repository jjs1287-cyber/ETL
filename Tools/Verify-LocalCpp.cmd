@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
cmake -S %ETLFORGE_ROOT%\Server -B %ETLFORGE_ROOT%\Server\build\windows-x64 -G Ninja -DCMAKE_BUILD_TYPE=Release "-DCMAKE_MAKE_PROGRAM:FILEPATH=%ETLFORGE_NINJA%\ninja.exe"
if errorlevel 1 exit /b %errorlevel%
cmake --build %ETLFORGE_ROOT%\Server\build\windows-x64 --parallel 2 --target etlforge_memutils_tests etlforge_vectorutils_tests etlforge_tokenizer_tests etlforge_xmlstrings_tests
if errorlevel 1 exit /b %errorlevel%
ctest --test-dir %ETLFORGE_ROOT%\Server\build\windows-x64 --output-on-failure -R "^native\.(memutils|vectorutils|tokenizer|xmlstrings)$"
exit /b %errorlevel%
