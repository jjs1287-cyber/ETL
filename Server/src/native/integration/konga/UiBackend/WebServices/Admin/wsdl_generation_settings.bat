REM !!!!!!!!!!!!!!!!!!!
REM Remeber to add the following line just before the class definition of
REM each generated Java class:
REM @SuppressWarnings(value={"unchecked"})
REM !!!!!!!!!!!!!!!!!!!

REM !!!MODIFY HERE!!!
SET WS_NAME=admin

rem This is the package that the web service resides in.
REM !!!MODIFY HERE!!!
SET JAVA_PACKAGE=org.etlforge.integration.server.implementation.webservice.admin

rem This is the name of the Java class/interface used as the starting point for the web service.
REM !!!MODIFY HERE!!!
SET JAVA_CLASS=%JAVA_PACKAGE%.Admin

REM AXIS_CPP_CLASSPATH has to be set in your environment.

call ..\wsdl_generation_settings_common.bat
