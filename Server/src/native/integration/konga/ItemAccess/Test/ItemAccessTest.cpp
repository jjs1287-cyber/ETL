/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>	       

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/Utils/DBConnectionInfo.h>

#include <konga/ItemAccess/UserAccess.h>
#include <konga/ItemAccess/GroupAccess.h>

using namespace konga;
using namespace std;

void testAccessControl();

void testAllEntityTypes();

void testExecutePermission();

int main(int argc, char* argv[])
{
	testAccessControl();
                        
                           
	return 0;
}

void testExecutePermission() {

	if( !UserHasExecutePermission(900, 11, 6) ) {
		cout << "oops" << endl;
	}
}

void testAllEntityTypes() {
	DBConnectionInfo dbInfo("TranDb");
	SmartDbPtr<DbConnection> pDbConnection( DbConnection::MakeInstance(dbInfo.dsn()) );
	UserAccess user = UserAccess::GetUserAccessWithGroup(3, *pDbConnection);
	GroupAccess group(1, *pDbConnection);

	const string stSelectEt("SELECT entitytype_id from entitytypestab");
	SmartDbPtr<DynamicDbRecordSet> pSelectEt(DynamicDbRecordSet::MakeInstance(pDbConnection, stSelectEt));
	pSelectEt->Open();
	long lItemId = -99;
	vector<long> vFailed;
	while( pSelectEt->Next() ) {
		int iEntityTypeId = atoi(pSelectEt->GetFieldValue(0).c_str());
		try {
			switch(iEntityTypeId) {
				case 7:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					continue;
				default:
					break;
			}
			user.AddItem(lItemId, iEntityTypeId, 8,0);
			user.DeleteItem(lItemId, iEntityTypeId);

			group.AddItem(lItemId, iEntityTypeId, 7, 2);
			group.DeleteItem(lItemId, iEntityTypeId);
		}
		catch( const CrException& ex ) {
			cout << ex.GetErrorString() << endl;
			vFailed.push_back(iEntityTypeId);
		}
	}

	if ( !vFailed.empty() ) {
		cout << "Failed entity types:" << endl;
		vector<long>::const_iterator cit = vFailed.begin();
		for(; cit != vFailed.end(); ++cit ) {
			if ( cit != vFailed.begin() )
				cout << ", ";
			cout << *cit;
		}
		cout << endl;
	}
	cout << "done..." << endl;
}

void testAccessControl() {
	DBConnectionInfo dbInfo("TranDb");
	SmartDbPtr<DbConnection> pDbConnection( DbConnection::MakeInstance(dbInfo.dsn()) );
	UserAccess user = UserAccess::GetUserAccessWithGroup(3, *pDbConnection);
	GroupAccess group(1, *pDbConnection);

	const long lItemId = -99;
	const long iEntityTypeId = 1;

	                                           
	user.AddItem(lItemId, iEntityTypeId,AccessControl::ACCESS_LEVEL_OWNER);
	if ( !user.IsOwner(lItemId, iEntityTypeId) )
		cout << "Problem, should be owner" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_READ) )
		cout << "Problem, should have read permission" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_WRITE) )
		cout << "Problem, should have write permission" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_EXECUTE) )
		cout << "Problem, should have execute permission" << endl;

	                                     
	user.ChangeItem(lItemId, iEntityTypeId, AccessControl::ACCESS_LEVEL_OWNER, AccessControl::ACCESS_BIT_READ + AccessControl::ACCESS_BIT_WRITE);
	if ( !user.IsOwner(lItemId, iEntityTypeId) )
		cout << "Problem, should be owner" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_READ) )
		cout << "Problem, should have read permission" << endl;
	if ( user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_WRITE) )
		cout << "Problem, should not have write permission" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_EXECUTE) )
		cout << "Problem, should not have execute permission" << endl;

	user.ChangeItem(lItemId, iEntityTypeId,AccessControl::ACCESS_LEVEL_RW, 0);
	if ( user.IsOwner(lItemId, iEntityTypeId) )
		cout << "Problem, should not be owner" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_READ) )
		cout << "Problem, should have read permission" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_WRITE) )
		cout << "Problem, should have write permission" << endl;
	if ( user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_EXECUTE) )
		cout << "Problem, should not have execute permission" << endl;

	user.ChangeItem(lItemId, iEntityTypeId,AccessControl::ACCESS_LEVEL_R, 0);
	if ( user.IsOwner(lItemId, iEntityTypeId) )
		cout << "Problem, should not be owner" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_READ) )
		cout << "Problem, should have read permission" << endl;
	if ( user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_WRITE) )
		cout << "Problem, should not have write permission" << endl;
	if ( user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_EXECUTE) )
		cout << "Problem, should not have execute permission" << endl;

	                                   
	  
	group.AddItem(lItemId, iEntityTypeId,AccessControl::ACCESS_LEVEL_RWX);
	if ( !group.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_READ) )
		cout << "Problem, should have read permission" << endl;
	if ( !group.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_WRITE) )
		cout << "Problem, should have write permission" << endl;
	if ( !group.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_EXECUTE) )
		cout << "Problem, should have execute permission" << endl;

	if ( user.IsOwner(lItemId, iEntityTypeId) )
		cout << "Problem, should not be owner" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_READ) )
		cout << "Problem, should have read permission through the group" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_WRITE) )
		cout << "Problem, should have write permission through the group" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_EXECUTE) )
		cout << "Problem, should have execute permission through the group" << endl;

	group.ChangeItem(lItemId, iEntityTypeId,AccessControl::ACCESS_LEVEL_RW, 0);
	if ( user.IsOwner(lItemId, iEntityTypeId) )
		cout << "Problem, should not be owner" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_READ) )
		cout << "Problem, should have read permission through the group" << endl;
	if ( !user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_WRITE) )
		cout << "Problem, should have write permission through the group" << endl;
	if ( user.HasPermission(lItemId, iEntityTypeId,AccessControl::ACCESS_BIT_EXECUTE) )
		cout << "Problem, should not have execute permission" << endl;

	                            
	if ( !user.HasReadPermission(lItemId, iEntityTypeId) )
		cout << "Problem, should have read permission through the group" << endl;
	if ( !user.HasWritePermission(lItemId, iEntityTypeId) )
		cout << "Problem, should have write permission through the group" << endl;
	if ( user.HasExecutePermission(lItemId, iEntityTypeId) )
		cout << "Problem, should not have execute permission" << endl;
	if ( user.HasChangePermissionPermission(lItemId, iEntityTypeId) )
		cout << "Problem, should not have change permission permission" << endl;

	                      
	group.ChangeItem(lItemId, iEntityTypeId,AccessControl::ACCESS_LEVEL_RWX, AccessControl::ACCESS_BIT_WRITE);
	if ( !user.HasReadPermission(lItemId, iEntityTypeId) )
		cout << "Should have read access..." << endl;
	if ( user.HasWritePermission(lItemId, iEntityTypeId) )
		cout << "Should not have write access..." << endl;

	group.DeleteItem(lItemId, iEntityTypeId);

	                                                                    
	if ( user.IsOwner(lItemId, iEntityTypeId) )
		cout << "Problem, should not be owner" << endl;
	if ( !user.HasReadPermission(lItemId, iEntityTypeId) )
		cout << "Problem, should have read permission" << endl;
	if ( user.HasWritePermission(lItemId, iEntityTypeId) )
		cout << "Problem, should not have write permission" << endl;
	if ( user.HasExecutePermission(lItemId, iEntityTypeId) )
		cout << "Problem, should not have execute permission" << endl;

	user.DeleteItem(lItemId, iEntityTypeId);

	if ( user.HasReadPermission(lItemId, iEntityTypeId) )
		cout << "The permissions should be gone..." << endl;
	if ( user.HasDeletePermission(lItemId, iEntityTypeId) )
		cout << "The permissions should be gone..." << endl;

	cout << "done..." << endl;
}

