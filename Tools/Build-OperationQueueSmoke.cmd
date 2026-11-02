@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
"C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin\cmake.exe" -S C:\ETL\Server -B C:\ETL\Server\build\windows-x64 -DETLFORGE_QUEUE_ASAN=OFF
"C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin\cmake.exe" --build C:\ETL\Server\build\windows-x64 --target etlforge_operation_queue_smoke etlforge_dtl_tests --parallel 4