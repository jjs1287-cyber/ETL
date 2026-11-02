

REM ####################################################################
REM                   Don't modify anything below!
REM ####################################################################


REM AXIS_CLASSPATH need to be set in your environment.

rem wsdl file name and default url are generated from the name (don't edit).
SET WSDL_FILE=%WS_NAME%.wsdl
SET AXIS_URL=http://localhost/axis/%WS_NAME%

rem This is a urn, we can use the full Java class name which should be relatively unique (don't edit).
SET URN=urn:%JAVA_CLASS%

rem This is the package that the generated Java CLIENT code will belong to (don't edit)
SET JAVA_PACKAGE_CLIENT=%JAVA_PACKAGE%.client
