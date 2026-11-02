@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
if not exist %ETLFORGE_ROOT%\Server\build-probe\utf8 mkdir %ETLFORGE_ROOT%\Server\build-probe\utf8
cl /nologo /EHsc /MD /O2 /Gy /std:c++17 /DNOMINMAX /DBOOST_ALL_NO_LIB /I %ETLFORGE_ROOT%\Dependencies\boost_1_86_0 /I %ETLFORGE_ROOT%\Server\src\native\common /I %ETLFORGE_ROOT%\Dependencies\icu-78.3-x64\include %ETLFORGE_ROOT%\Server\tests\native\utf8_text_tests.cpp %ETLFORGE_ROOT%\Server\src\native\common\konga\CharacterEncodingConversion\Utf8TextInput.cpp %ETLFORGE_ROOT%\Server\src\native\common\konga\CharacterEncodingConversion\Utf8Conv.cpp %ETLFORGE_ROOT%\Server\src\native\common\konga\CharacterEncodingConversion\EncodingStringUtils.cpp %ETLFORGE_ROOT%\Server\src\native\common\konga\WinUtils\WinStringUtils.cpp /Fo%ETLFORGE_ROOT%\Server\build-probe\utf8\ /Fe%ETLFORGE_ROOT%\Server\build-probe\utf8\utf8_text_tests.exe /link /OPT:REF /LIBPATH:%ETLFORGE_ROOT%\Dependencies\icu-78.3-x64\lib64 %ETLFORGE_ROOT%\Dependencies\boost-x64\lib\libboost_filesystem-vc143-mt-x64-1_86.lib icuuc.lib oleaut32.lib rpcrt4.lib advapi32.lib ws2_32.lib %ETLFORGE_ROOT%\Server\build\windows-x64\etlforge_legacy_utils_probe.lib
if errorlevel 1 exit /b %errorlevel%
set "PATH=%ETLFORGE_ROOT%\Dependencies\icu-78.3-x64\bin64;%PATH%"
%ETLFORGE_ROOT%\Server\build-probe\utf8\utf8_text_tests.exe
exit /b %errorlevel%
