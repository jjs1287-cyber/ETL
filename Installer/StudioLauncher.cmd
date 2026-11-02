@echo off
setlocal
set "APPDIR=%~dp0"
set "USERDATA=%LOCALAPPDATA%\ETLForge\Studio"
if not exist "%USERDATA%\application" mkdir "%USERDATA%\application" >nul 2>&1
start "ETLForge" "%APPDIR%runtime\java\bin\javaw.exe" -Xmx2g -DRunUpgradeScripts=false -Dfile.encoding=UTF-8 -Duser.home="%USERDATA%" -cp "%APPDIR%configuration;%APPDIR%dist\lib\*;%APPDIR%lib\*" org.etlforge.integration.client.main.IntegrationMain -d "%USERDATA%\application" %*
endlocal
