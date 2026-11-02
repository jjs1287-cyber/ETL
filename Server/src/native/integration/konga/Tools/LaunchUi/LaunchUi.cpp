/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>
#include <iostream>	             

#include <konga/Utils/CrException.h>
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/FileUtils.h>		                      
#include <konga/Utils/Settings.h>

#include <konga/JavaUtils/JavaVersionUtils.h>	                                     

using namespace konga;
using namespace std;

int main(int argc, const char* argv[])

try 
{
	string sMaxVersion;
	try {
		                                                           
		sMaxVersion = GetMaxJavaVersion(true);
	} catch(CrException& e) {
		if( e.GetErrorNumber() != 2 )
			throw;
	}

	if( sMaxVersion.empty() || sMaxVersion < "1.6" ) {
		string sMsg("This program requires Java Runtime Environment (JRE) 1.6 or later\n(also referred to as Java 6).\n");
		if ( sMaxVersion.empty() ) {
			sMsg += "No JRE installation could be found.\n";
		}
		else {
			sMsg += "You have version " + sMaxVersion + " installed but it's too old.\n";
		}
		sMsg += "Do you wish to open the download page in your browser?";
		long lRet = ::MessageBox(0, sMsg.c_str(), "ETLForge Integration Environment", MB_YESNOCANCEL);
		if( lRet != IDYES )
			return 2;
		                                             
		ShellExecute(0, "open", "http://java.com/getjava", 0, ".", SW_SHOW);
		return 0;
	}

	string sFileName = getModuleFileName();
	string sPath = GetBranchPath( sFileName );
	                                                                       
	AppendPathSeparator(sPath);

	                                                   
	string sJavaPath = GetJavaPath(sMaxVersion, true) + "\\bin\\javaw.exe";

	SetOptionsFromFile(sPath + "configuration\\client.properties");
	string sJavaArgs(GetOptionAsString("", "STARTUP_ARGUMENTS", "-Xmx512m -Dswing.boldMetal=false"));
	sJavaArgs += " -jar \"" + sPath + "lib\\etlforge\\integration\\client\\etlforge-integration-client.jar\"";

	                             
	for(int i=1; i<argc; ++i) {
		                                                
		sJavaArgs += string(" \"") + argv[i] + string("\"");
	}

	cout << "Launching: " << sJavaPath << " " << sJavaArgs << endl;
	RunProc(sJavaPath, sJavaArgs);
	return 0;

} catch(CrException& e) {
	#ifdef WIN32
	::MessageBox(0, ("An error occurred when launching the integration environment. " + 
		e.GetErrorString()).c_str(), "Integration Environment", MB_ICONERROR);
	#endif         
	return 1;
}
