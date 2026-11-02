/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                                                    
  
#include <iostream>

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/CrException.h>

#include <konga/InterchangeProject/InterchangeProject.h>

#ifdef WIN32
#include <konga/Utils/PortDefs.h>	                                               
#endif
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/DbConnection.h>

using namespace konga;
using namespace std;

void testInterchangeProject();
void migrateOldProjects();

int main(int argc, char* argv[])
{
	                            
	migrateOldProjects();
}

void migrateOldProjects() {

	try {

		DBConnectionInfo dbInfo("TranDb");
		SmartDbPtr<DbConnection> pConn(DbConnection::MakeInstance(dbInfo.connectionString()));
		SmartDbPtr<DynamicDbRecordSet> rs(DynamicDbRecordSet::MakeInstance(pConn));
	
		string stSql = "select * from interchangeprojectitemstab order by interchangeproject_id";
		rs->SetSqlStatement(stSql);
		rs->Open();
		long lCurrentId = -1;
		InterchangeProject ip(lCurrentId);
		while ( rs->Next() ) {
			long lId = atol( rs->GetFieldValue(0).c_str() );
			if ( lId != lCurrentId ) {
				lCurrentId = lId;
				ip.SetId(lCurrentId);
	
				if ( !ip.Exists() ) {
					cout << "Warning: Interchange Project with Id = " << lCurrentId << " does not exist!" << endl;
					continue;
				}
			}
			int iEntityTypeId = atoi( rs->GetFieldValue(1).c_str() );
			long lItemId = atol( rs->GetFieldValue(2).c_str() );
	
			ip.AddItem(iEntityTypeId, lItemId);
	
		}
	}
	catch( const CrException& e ) {
		cout << e.GetErrorString() << endl;
	}

}

void testInterchangeProject() {
	InterchangeProject ip(1000000, "Test Project");
	if ( !ip.Exists() ) {
		cout << "Project does not exist." << endl;
		cout << "You need to create the project before running this test!!" << endl;
		return;
	}
	else {
		if ( !ip.IsEmpty() ) {
			cout << "Make sure the project is empty before running the test." << endl;
			return;
		}
		cout << "Name: " << ip.GetName() << endl;
		cout << "Description: " << ip.GetDescription() << endl;
	}

	const vector<int>& vEntityIds = ip.GetValidEntityTypes();

	vector<int>::const_iterator cit = vEntityIds.begin();
	for(; cit != vEntityIds.end(); ++cit ) {
		ip.AddItem(*cit, 1);
	}

	cit = vEntityIds.begin();
	for(; cit != vEntityIds.end(); ++cit ) {
		ip.DeleteItem(*cit, 1);
	}

	if ( !ip.DeleteProjectIfEmpty() ) {
		cout << "The project should be empty!!!" << endl;
		return;
	}
}

