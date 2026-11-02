@echo off
rem Shared tool discovery. Calling scripts inherit these variables.
for %%I in ("%~dp0..") do set "ETLFORGE_ROOT=%%~fI"
if defined VSINSTALLDIR goto tools
if not exist "%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe" (
    echo ERROR: Visual Studio Installer/vswhere.exe is missing. 1>&2
    exit /b 1
)
for /f "usebackq tokens=*" %%I in (`"%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do set "ETLFORGE_VS=%%I"
if not defined ETLFORGE_VS (
    echo ERROR: Install the Visual Studio Desktop development with C++ workload. 1>&2
    exit /b 1
)
call "%ETLFORGE_VS%\Common7\Tools\VsDevCmd.bat" -arch=x64 -host_arch=x64
if errorlevel 1 exit /b 1
:tools
set "ETLFORGE_CMAKE=%VSINSTALLDIR%Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin"
set "ETLFORGE_NINJA=%VSINSTALLDIR%Common7\IDE\CommonExtensions\Microsoft\CMake\Ninja"
if not exist "%ETLFORGE_CMAKE%\cmake.exe" exit /b 1
if not exist "%ETLFORGE_NINJA%\ninja.exe" exit /b 1
set "PATH=%ETLFORGE_CMAKE%;%ETLFORGE_NINJA%;%PATH%"
if not defined JAVA_HOME set "JAVA_HOME=%ETLFORGE_ROOT%\Tools\jdk8u504-b01"
if not defined ANT_HOME set "ANT_HOME=%ETLFORGE_ROOT%\Tools\apache-ant-1.10.15"
if not defined ETLFORGE_PYTHON (
  for /d %%I in (C:\Tools\Python\cpython-3.12.*-windows-x86_64-none) do if exist "%%I\python.exe" set "ETLFORGE_PYTHON=%%I\python.exe"
)
if not defined ETLFORGE_PYTHON if exist C:\Tools\aider-env\Scripts\python.exe set "ETLFORGE_PYTHON=C:\Tools\aider-env\Scripts\python.exe"
if not defined ETLFORGE_PYTHON (
  for /f "delims=" %%I in ('where python.exe 2^>nul') do if not defined ETLFORGE_PYTHON set "ETLFORGE_PYTHON=%%I"
)
if not exist "%ETLFORGE_PYTHON%" (
  echo ERROR: Set ETLFORGE_PYTHON to a Python 3 interpreter. 1>&2
  exit /b 1
)
set "PYTHONUTF8=1"
exit /b 0
