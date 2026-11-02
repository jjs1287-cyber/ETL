@ECHO OFF
COLOR 1F
CLS

call wsdl_generation_settings.bat


echo .
echo Generating C++ code for the server:
@ECHO ON
java -cp "%AXIS_CLASSPATH%" org.apache.axis.wsdl.wsdl2ws.WSDL2Ws -o. -lc++ -sserver %WSDL_FILE%
@ECHO OFF

IF ERRORLEVEL = 1 GOTO ByeByeBad

rem Generates Java code for the client. This code needs to be imported into the Java source tree.
echo .
echo Generating Java code for the client. This code needs to be imported into the Java source tree.
@ECHO ON
java -cp "%AXIS_CLASSPATH%" org.apache.axis.wsdl.WSDL2Java -o./client -N%URN%=%JAVA_PACKAGE_CLIENT% %WSDL_FILE%
@ECHO OFF

IF ERRORLEVEL = 1 GOTO ByeByeBad

rem Creates directory structure.
mkdir project
mkdir project\vc-9_0
mkdir project\SlickEdit

mkdir Wrapper
mkdir Wrapper\project
mkdir Wrapper\project\vc-9_0

GOTO ByeByeGood

:ByeByeBad
COLOR 4F
PAUSE
EXIT

:ByeByeGood
COLOR 0F

REM !!!!!!!!!!!!!!!!!!!
REM Remeber to add the following line just before the class definition of
REM each generated Java class:
REM @SuppressWarnings(value={"all"})
REM !!!!!!!!!!!!!!!!!!!
PAUSE
