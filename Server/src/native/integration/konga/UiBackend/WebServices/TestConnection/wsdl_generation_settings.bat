rem Modify this file for your web service. The Authentication web service is used as an example.
rem

REM !!!!!!!!!!!!!!!!!!!
REM Remeber to add the following line just before the class definition of
REM each generated Java class:
REM @SuppressWarnings(value={"unchecked"})
REM !!!!!!!!!!!!!!!!!!!

REM !!!MODIFY HERE!!!
SET WS_NAME=konga_test_connection

rem This is the package that the web service resides in.
REM !!!MODIFY HERE!!!
SET JAVA_PACKAGE=org.etlforge.integration.server.implementation.webservice.interchange.connection

rem This is the name of the Java class/interface used as the starting point for the web service.
REM !!!MODIFY HERE!!!
SET JAVA_CLASS=%JAVA_PACKAGE%.TestConnection

REM AXIS_CPP_CLASSPATH needs to be set in your environment.

call ..\wsdl_generation_settings_common.bat
