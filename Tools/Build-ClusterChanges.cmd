@echo off
call "%~dp0Set-DevEnvironment.cmd"
if errorlevel 1 exit /b %errorlevel%
"C:\Program Files\Microsoft Visual Studio\18\Community\Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja\ninja.exe" -C %ETLFORGE_ROOT%\Server\build\windows-x64 ETLForgeAdmin.dll ETLForgeProcessEngine.exe -j 4
exit /b %errorlevel%
