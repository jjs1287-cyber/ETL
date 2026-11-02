/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifdef WIN32
#include "windows.h"
#endif

#include <iostream>

#include <konga/Utils/PortDefs.h>	                    
#include <GetPot>

#include <konga/Utils/CrException.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>		                              

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/WinUtils/WinErrorUtils.h>	                        

#include <konga/OperationEngine/OperationEngine.h>
#include <konga/OperationEngine/SignalHandler.h>

#include <konga/OperationObjects/OperationEngineException.h>
#include <konga/Log/logging.h>


using namespace konga;
using namespace std;

namespace {
	const char operationId[] = "--operation-id";
	const char operationIid[] = "--operation-iid";
	const char sessionId[] = "--session-id";
	const char userId[] = "--user-id";
	const char empty[] = "";

	void PrintUsage(char *argv[]) {
		cerr << "Usage: " << GetLeafPath(argv[0]) << " " << operationId <<  " <operation id> ["
			<< sessionId << " <session id> " << userId << " <user id> " << operationIid << "<operation-instance-id>]" << endl;
	}

}

int main (int argc, char *argv[]) {
	KongaExceptionHandler handler(Logger::GetLogFileName("OperationEngine"));

	                                                                     
#ifdef WIN32
	UINT oldMode = SetErrorMode(SEM_FAILCRITICALERRORS);
#endif         

	                                    
	GetPot cl(argc, argv);

	long lOpId = -1;
	if ( !cl.search(operationId) ) {
		cerr << "No OperationId was specified" << endl;
		PrintUsage(argv);
		return 1;
	}
	lOpId = cl.next((int)lOpId);
	if ( lOpId < 1 ) {
		cerr << "The OperationId has to be a positive integer" << endl;
		PrintUsage(argv);
		return 2;
	}

	long lSessionId = -1;
	if ( cl.search(sessionId) ) {
		lSessionId = cl.next((int)lSessionId);
	}

	long lUserId = UserInfo::UNDEFINED_UID;
	if ( cl.search(userId) ) {
		lUserId = cl.next((int)lUserId);
	}

	long long llOperationInstanceId = -1;
	if ( cl.search(operationIid) ) {
		const char* c = cl.next("");
		if ( c == 0 || *c == 0 ) {
			cerr << "If the " << operationIid << " argument is specified it must be followed by a valid operation instance id." << endl;
			PrintUsage(argv);
			return 3;
		}

		llOperationInstanceId = atoll(c);
		if ( llOperationInstanceId < 1 ) {
			cerr << "If the " << operationIid << " argument is specified it must be followed by a valid positive operation instance id." << endl
				<< c << " is not a valid value invalid." << endl;
			PrintUsage(argv);
			return 4;
		}

	}

	konga::InstallSignalHandler();

	Logger log("OperationEngine");
	try {
		UserInfo currentUser;
		if ( lUserId != UserInfo::UNDEFINED_UID ) {
			SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
			currentUser = konga::GetUserInfo(lUserId, *pTranDb, true);
		}
		OperationEngine opEngine(currentUser, 0);
		opEngine.RunOperation(lOpId, lSessionId, llOperationInstanceId);
	}
	catch( const OperationEngineException& ) {
		                                                                                   
		                                                         
	}
	catch( const CrException& ex) {
		                                                  
		string stErr("Internal Error: The Operation Engine threw an unexpected CrException\n");
		stErr += "Failed to invoke operation " + toString(lOpId) + 
			". Error: " + ex.GetErrorString();
		log.Log(stErr);
	}
#ifndef _DEBUG
	catch( const std::exception& ex ) {
		                                                  
		string stErr("Internal Error: The Operation Engine threw an unexpected std::exception\n");
		stErr += "Failed to invoke operation " + toString(lOpId) + 
			". Error: " + string(ex.what());
		log.Log(stErr);
		if ( konga::shouldThrowOnError() ) throw;
	}
	catch( ... ) {
		                                                  
		string stErr("Internal Error: The Operation Engine threw an unexpected unknown exception.");
		log.Log(stErr);
		if ( konga::shouldThrowOnError() ) throw;
	}
#endif
	                                              
	                                                                                                
	return 0;
}
