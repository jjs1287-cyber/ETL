/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
  
                                              

#include <iostream>
#include <sstream>
#include <iomanip>
#include <string>

#include <konga/OperationEngine/OperationResolver.h>
#include <konga/OperationEngine/OperationEngineException.h>
#include <konga/OperationEngine/RenameFile.h>

using namespace std;
using namespace konga;

int resolveOperation(long);
int testRename();
int test();

int main (int argc, char *argv[])
{
	return resolveOperation(80);
                       
                 
}

int test() {
	stringstream stream;
	stream << setfill('0') << setw(2) << 1 << "-" << setw(2) << 2 << "-" << 3 << "-" << setw(2) << 123 << "-" << setw(3) << 7 << endl;

	cout << stream.str();
}

int resolveOperation(long lOperationId) {

	string stTranDbDsn("DSN=CRTRANDB_P;UID=edgeways;PWD=****;");
	try {
		OperationResolver OR;
		OR.Resolve(lOperationId, stTranDbDsn);
		cout << "Successfully resolved Operation with id = " << lOperationId << endl;
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

	renamer = RenameFile("[File]_[date]_[unique]_[tiMe].[EXT]");
	cout << renamer.getRenamedFileName("MyFile.txt") << endl;
	cout << renamer.getRenamedFileName("MyFile.") << endl;
	cout << renamer.getRenamedFileName(".txt") << endl;
	cout << renamer.getRenamedFileName("file") << endl;

	return 0;
}

