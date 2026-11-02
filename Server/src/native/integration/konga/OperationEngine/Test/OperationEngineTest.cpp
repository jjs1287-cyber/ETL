/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <iostream>
#include <sstream>
#include <boost/regex.hpp> 

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/OperationObjects/RenameFile.h>
#include <konga/OperationObjects/Operation.h>
#include <konga/OperationObjects/OperationEngineException.h>

#include <konga/OperationEngine/OperationEngine.h>

#include <konga/OperationContext/DefaultOperationContext.h>

using namespace std;
using namespace konga;
using namespace boost;

int testRename();
int testOperationResolver();

int testResolveOperation(const long lOpId);


int main (int argc, char *argv[])
{
                         
                                  

                                   

}

int testResolveOperation(const long lOpId) {
	try {
		OperationEngine opRes;
		DBConnectionInfo dbInfo("TranDb");
		opRes.Resolve(lOpId, 0, dbInfo.connectionString());
	}
	catch( const CrException& ex ) {
		cout << "OperationId = "<< lOpId << " failed:" << endl
			 << ex.GetErrorString() << endl;
		return -1;
	}
	return 0;
}

int testCreateDir() {

	try {
		kCreateDirectory("/tmp/OP_11/asdfasdfasdfasdfasdfasdfasdfasdf");
	}
	catch ( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return -1;
	}
	return 0;
}


namespace konga {
	class OperationResolverTester {
	public:
		OperationResolverTester(void) {
		}
		~OperationResolverTester(void) {
		}
	};
}

int testOperationResolver() {

	string stTranDbDsn("DSN=CRTRANDB_P;UID=edgeways;PWD=****;");
	try {
		OperationEngine oe;
		long lOpId = 131;
		oe.Resolve(lOpId, 0, stTranDbDsn);
		cout << "Successfully resolved Operation with id = " << lOpId << endl;
	}
	catch( const OperationEngineException& ex ) {
		cout << ex.GetErrorString() << endl;
		return -1;
	}

	return 0;
}

int testRename() {
	RenameFile renamer("[File]_moved_at_[date].[eXt]");
	cout << renamer.getRenamedFileName("MyFile.txt") << endl;

	renamer = RenameFile("movedFile.txt");
	cout << renamer.getRenamedFileName("MyFile.txt") << endl;

	renamer = RenameFile("[File]-[date]-[tiMe].[EXT]");
	cout << renamer.getRenamedFileName("MyFile.txt") << endl;
	cout << renamer.getRenamedFileName("MyFile.") << endl;
	cout << renamer.getRenamedFileName(".txt") << endl;
	cout << renamer.getRenamedFileName("file") << endl;

	return 0;
}
