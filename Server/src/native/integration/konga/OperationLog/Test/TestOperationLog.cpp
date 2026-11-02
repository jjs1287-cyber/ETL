/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/DateUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/OperationLog/OperationLog.h>

using namespace konga;
using namespace std;

int testCleanup();

int testArchive();

int main(int argc, char* argv[])
{
                        
	return testArchive();
}

int testArchive() {
	try {
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance( DBConnectionInfo("TranDb").connectionString() ) );
		OperationLog log(*pConn);
		CrDateTime dt(2007, 1, 1, 0, 0, 0); 

		log.ArchiveOldLogs(dt, "Error");
	}
	catch( const CrException& e ) {
		cerr << e.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testCleanup() {

	SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance( DBConnectionInfo("TranDb").connectionString() ) );

	OperationLog log(*pConn);
	CrDateTime dt(2007, 1, 1, 0, 0, 0); 
	
	log.DeleteOldLogs(dt);

	log.DeleteLogsForOperation(3, dt);

	log.DeleteLogsForOperation(3);

	return 0;
}

