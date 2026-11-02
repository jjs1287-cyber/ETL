
REM !!!MODIFY HERE!!!
SET WS_NAME=konga_odbc_infoprovider

rem This is the package that the web service resides in.
REM !!!MODIFY HERE!!!
SET JAVA_PACKAGE=org.etlforge.integration.server.implementation.webservice.interchange.db

rem This is the name of the Java class/interface used as the starting point for the web service.
REM !!!MODIFY HERE!!!
SET JAVA_CLASS=%JAVA_PACKAGE%.OdbcInfoProvider


REM AXIS_CPP_CLASSPATH has to be set in your environment.

call ..\wsdl_generation_settings_common.bat

