/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Db/PDb/ProcessTab.h>		                            
#include <konga/Db/PDb/TaskTab.h>			                         

#include <konga/Db/DbUtils/DbUtils.h>		                                    

#include <konga/DbRelations/EntityIdConstants.h>               
#include <konga/ItemAccess/UserAccess.h>		                             

#include "WorkflowValidation.h"

using namespace konga;
using namespace std;

bool konga::VerifyProcessForUserExecution(const std::string& sProcessName, int iUserId) {
	try {
		SmartDbPtr<konga::DbConnection> pPDb( konga::GetBackEndDbConnection("PDb") );
		SmartDbPtr<PDb::ProcessTab> pPDbProcessTab( PDb::ProcessTab::MakeInstance(pPDb) );	

		std::string stFilter = "WHERE ProcessName = '" + SqlEscape(sProcessName) + "'";
		pPDbProcessTab->Select(stFilter);

		if( !pPDbProcessTab->Next() )
			throw CrException("No such workflow process: " + sProcessName);

		return UserHasExecutePermission(iUserId, pPDbProcessTab->GetProcessId(), UserEntityId);
	} catch(CrException&) {
		throw;
	}
}
