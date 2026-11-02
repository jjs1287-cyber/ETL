/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <iostream>

#include <konga/Db/TranDb/SpInsertOperationInstanceTab.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDbUtils/TranDbUtils.h>


using namespace std;
using namespace konga;

int compileTest();

int tranDbTests();

int main (int argc, char *argv[])
{
                        
	return tranDbTests();
}

int tranDbTests() {
	konga::SmartDbPtr<DbConnection> pConn( konga::GetBackEndDbConnection("TranDb") );

	long long parentOperationInstanceId = 2;
	long operationId = 2;
	long long operationInstanceId = 0;
	try {
		while ( parentOperationInstanceId > 0 ) {
			konga::SmartDbPtr<TranDb::SpInsertOperationInstanceTab> sp( TranDb::SpInsertOperationInstanceTab::MakeInstance(pConn) );
			sp->SetOperationId(operationId);
			sp->SetParentOperationInstanceId(parentOperationInstanceId);

			sp->Execute();

			operationInstanceId = sp->GetOperationInstanceId();
			cout << operationInstanceId << endl;

			parentOperationInstanceId *= 2;
			operationId += 2;
		}
	}
	catch( const DbException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int compileTest() {
	string stGuid;
	int iEntityTypeId = 1;
	konga::getDecryptedPasswordFromDb(1, iEntityTypeId);
	konga::getEncryptedPasswordForSourceFromDb(1);
	konga::getEncryptedPasswordForTargetFromDb(2);

	return 0;
}

