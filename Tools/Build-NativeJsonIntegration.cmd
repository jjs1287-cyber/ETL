@echo off
call "C:\Program Files\Microsoft Visual Studio\18\Community\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b %errorlevel%
set "PATH=C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin;C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja;%PATH%"
if "%1"=="compile" (
cmake --build C:\ETL\Server\build\windows-x64 --target etlforge_soap_jsonengine etlforge_transformation_engine_probe --parallel 4
) else (
cmake --build C:\ETL\Server\build\windows-x64 --target etlforge_server_executables etlforge_frontend_treemapperinfoprovider --parallel 4
)
exit /b %errorlevel%
