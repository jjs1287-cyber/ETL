@echo off
call "%~dp0Set-DevEnvironment.cmd"
"C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin\cmake.exe" -S %ETLFORGE_ROOT%\Server -B %ETLFORGE_ROOT%\Server\build\windows-x64 -DETLFORGE_QUEUE_ASAN=OFF
"C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin\cmake.exe" --build %ETLFORGE_ROOT%\Server\build\windows-x64 --target etlforge_operation_queue_smoke etlforge_dtl_tests --parallel 4
