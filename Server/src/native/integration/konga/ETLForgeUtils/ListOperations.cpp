/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <iostream>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include "ListOperations.h"

using namespace konga;
using std::string;
using std::cout;
using std::endl;

void konga::ListOperations(const std::string& sProjectName) {

	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

	SmartDbPtr<DynamicDbRecordSet> pProjectTab_rs( DynamicDbRecordSet::MakeInstance(pTranDb) );

	string sql(
			"SELECT ipo.InterchangeProject_ID, ip.InterchangeProject_Name, o.Operation_Name from OperationsTab o " \
			"JOIN InterchangeProjectOperationsTab ipo " \
			"on o.Operation_ID = ipo.Item_Id " \
			"JOIN InterchangeProjectsTab ip " \
			"ON ip.InterchangeProject_ID = ipo.InterchangeProject_ID"
			);

	if ( !sProjectName.empty() ) {
		string sProjectName_Lower(sProjectName);
		ToLower(sProjectName_Lower);
		sql += " WHERE LOWER(ip.InterchangeProject_Name) = '" + SqlEscape(sProjectName_Lower) + "'";
	}
	sql += " ORDER BY InterchangeProject_ID";

	pProjectTab_rs->SetSqlStatement(sql);
	pProjectTab_rs->Open();

	if ( !pProjectTab_rs->Next() ) {
		if ( !sProjectName.empty() )
			throw CrException("There is no project called '" + sProjectName + "' deployed on this server");
		else
			throw CrException("There are no projects deployed on this server.");
	}

	long currentProjectId = -9876;
	do {
		const long projectId = atol( pProjectTab_rs->GetFieldValue(0).c_str() );
		if ( projectId != currentProjectId ) {
			cout << "Project: " << pProjectTab_rs->GetFieldValue(1) << endl;
			currentProjectId = projectId;
		}
		cout << "  " << pProjectTab_rs->GetFieldValue(2) << endl;
	}
	while ( pProjectTab_rs->Next() );
}

