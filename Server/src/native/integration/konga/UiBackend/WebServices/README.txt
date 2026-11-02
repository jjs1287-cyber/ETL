Instructions for creating web service stubs on the server (in C++) and the client (in Java). 

- Run ant script in the "ETLForge Integration - UiBackend - WSDL" project.

- Create a new folder to hold the web service and copy these files (from another web service) to the new folder: 
    wsdl_generation_settings.bat
    generate_wsdl.bat
    generate_stubs.bat

- Edit the variables in wsdl_generation_settings.bat.

- Make sure you have C:\dev\etlforge\trunk\other up-to-date.

- The environment variable AXIS_CLASSPATH should be set to:
  C:\dev\etlforge\trunk\other\axis\lib\axisjava\axis.jar;C:\dev\etlforge\trunk\other\axis\lib\axisjava\commons-discovery-0.2.jar;C:\dev\etlforge\trunk\other\axis\lib\axisjava\commons-logging-1.0.4.jar;C:\dev\etlforge\trunk\other\axis\lib\axisjava\jaxrpc.jar;C:\dev\etlforge\trunk\other\axis\lib\axisjava\saaj.jar;C:\dev\etlforge\trunk\other\axis\lib\axisjava\log4j-1.5.1.jar;C:\dev\etlforge\trunk\other\axis\lib\axis\wsdl2ws.jar;C:\dev\etlforge\trunk\other\axis\lib\axisjava\wsdl4j-1.5.1.jar

- Execute generate_wsdl.bat and check the wsdl file. Make changes if necessary,
  e.g. add nillable where needed, and changing names of variables since these are set to in0, in1, in2, etc.
  change soapAction in the wsdl: add "webservice_name#" before operation name
	where webservice_name is define in the wsdl_generation_settings.bat
	(if this is done after the stubs is generated, the stub file in the client site need to change
		replace all of "_call.setSOAPActionURI(" to  "_call.setSOAPActionURI(webservice_name#"
	)

- Execute generate_stubs.bat to generate the server/client stubs.

- The server C++ files are generated in the root of the folder
  and the client code is generated in the �client� directory.
  A directory structure reflecting the package name will have been created as well. Move to:
	integration\java\client\webservices\client\src\org\etlforge\integration\server\implementation\webservice\...

- On Windows: Create an empty DLL project in VS and add the *Wrapper.cpp, *Wrapper.hpp and the *Service.cpp files.
  Close the solution and fix the directory structure to our standard one (project/vc-9_0 etc).
  Create another empty �static library� VS project with the rest of the files.
  The DLL project should reference the static library (the *.dll will then be copied to the axis deploy directory).
  Then fix the directory structure for the .lib project as well.
  Make sure both projects use Multi-threaded [Debug] DLL and has RTT (Run-Time type info) set to Yes.

- On Linux: Create a standard .so project and add all the files to it. The resulting .so file is the one to copy to the axis deploy directory on Linux.

- Import the java source code in the correct package (*.client).

- Add the correct SOAPAction to the generated java stub and write the implementation which actually calls the web service.

Note on the C++ implementation. The datastructures that are returned often contain dynamically allocated memory.
Care has to be taken that this memory is deleted.
If a method allocates memory a parallell method should be created which takes care of deallocating it.
Make sure that the deallocation is done in the correct order and that the method is actually called.
Methods called something like Axis_Delete_InterchangeDataHolder will have been created by axis.
These should be implemented because they are called automatically by the wrapper.

