@ECHO OFF
COLOR 1F
CLS

call wsdl_generation_settings.bat


rem This is the path of the ETLForge/Konga .jar file containing the above Java class.
rem NOTE: Make sure this jar file is current (run ant in Eclipse on the build.xml node).
SET KONGA_JAR=%ETLFORGE_JAVA_DEV%\lib\etlforge\integration\common\etlforge-uibackend-webservice-wsdl.jar

rem This is a urn, we can use the full Java class name which should be relatively unique (don't edit).
SET URN=urn:%JAVA_CLASS%

echo Generating the WSDL file from a Java interface/class:
@ECHO ON
java -cp "%AXIS_CLASSPATH%;%KONGA_JAR%" org.apache.axis.wsdl.Java2WSDL -yWRAPPED -AOPERATION -o%WSDL_FILE% -l%AXIS_URL% -n%URN% -p%JAVA_PACKAGE%=%URN% %JAVA_CLASS%
@ECHO OFF

IF ERRORLEVEL = 1 GOTO ByeByeBad

GOTO ByeByeGood

:ByeByeBad
COLOR 4F
PAUSE
EXIT

:ByeByeGood
COLOR 0F
PAUSE
