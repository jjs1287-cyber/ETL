@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
if not exist C:\ETL\Server\build-probe\utf8 mkdir C:\ETL\Server\build-probe\utf8
cl /nologo /EHsc /MD /O2 /Gy /std:c++17 /DNOMINMAX /DBOOST_ALL_NO_LIB /I C:\ETL\Dependencies\boost_1_86_0 /I C:\ETL\Server\src\native\common /I C:\ETL\Dependencies\icu-78.3-x64\include C:\ETL\Server\tests\native\utf8_text_tests.cpp C:\ETL\Server\src\native\common\konga\CharacterEncodingConversion\Utf8TextInput.cpp C:\ETL\Server\src\native\common\konga\CharacterEncodingConversion\Utf8Conv.cpp C:\ETL\Server\src\native\common\konga\CharacterEncodingConversion\EncodingStringUtils.cpp C:\ETL\Server\src\native\common\konga\WinUtils\WinStringUtils.cpp /FoC:\ETL\Server\build-probe\utf8\ /FeC:\ETL\Server\build-probe\utf8\utf8_text_tests.exe /link /OPT:REF /LIBPATH:C:\ETL\Dependencies\icu-78.3-x64\lib64 C:\ETL\Dependencies\boost-x64\lib\libboost_filesystem-vc143-mt-x64-1_86.lib icuuc.lib oleaut32.lib rpcrt4.lib advapi32.lib ws2_32.lib C:\ETL\Server\build\windows-x64\etlforge_legacy_utils_probe.lib
if errorlevel 1 exit /b %errorlevel%
set "PATH=C:\ETL\Dependencies\icu-78.3-x64\bin64;%PATH%"
C:\ETL\Server\build-probe\utf8\utf8_text_tests.exe
exit /b %errorlevel%
