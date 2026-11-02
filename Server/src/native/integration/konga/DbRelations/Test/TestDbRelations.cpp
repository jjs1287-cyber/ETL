/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/DbRelations.h>
#include <konga/DbRelations/TableKeyInfo.h>
#include <konga/DbRelations/DependentTableInfo.h>

using namespace konga;
using namespace std;

int main(int argc, char* argv[])
{
	DbRelations* pTranDbRelations = DbRelations::GetTranDbRelations();

	const TableKeyInfo& operationsDbKeyInfo = pTranDbRelations->GetTableKeyInfo(getTableName(nameOperations));
	cout << "Primary Key: " << operationsDbKeyInfo.GetPrimaryKey()->GetColumnName() << endl;
	cout << endl;

	const vector<KeyInfo>& keys = operationsDbKeyInfo.GetKeyInfo();
	for( vector<KeyInfo>::const_iterator cit = keys.begin(); cit != keys.end(); ++cit ) {
		cout << cit->GetColumnName();
		if ( !cit->IsPrimary() )
			cout << " --> " << cit->GetParentTableName();
		cout << endl;
	}

	const vector<DependentTableInfo>& deps = pTranDbRelations->GetDependentTables(getTableName(nameTransformationsTab_New));
	cout << endl << "Tables depending on " << getTableName(nameTransformationsTab_New) << endl;
	for( vector<DependentTableInfo>::const_iterator cit = deps.begin(); cit != deps.end(); ++cit ) {
		cout << "  " << cit->GetTableName() << "." << cit->GetForeignKeyName() << endl;
	}

	                                            
	const vector<DependentTableInfo>& deps2 = pTranDbRelations->GetDependentTables(getTableName(nameTransformationsTab_New));

	const vector<DependentTableInfo>& deps3 = pTranDbRelations->GetDependentTables(getTableName(nameDocuments));
	cout << endl << "Tables depending on " << getTableName(nameDocuments) << endl;
	for( vector<DependentTableInfo>::const_iterator cit = deps3.begin(); cit != deps3.end(); ++cit ) {
		cout << "  " << cit->GetTableName() << "." << cit->GetForeignKeyName() << endl;
	}

	delete pTranDbRelations;
}

