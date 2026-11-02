/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/TransformationDataStore/TransformationDataStore.h>

#include "TransformationDataStoreCleanupRule.h"

using namespace konga;
using std::string;
using std::vector;

bool TransformationDataStoreRule::ValidateRule(std::vector<std::string>& warnings) {
	if ( m_iAgeDays < 0 ) m_iAgeDays = 0;
	return true;
}

bool TransformationDataStoreRule::Execute(std::vector<std::string>& warnings) {
	if ( m_iAgeDays < 1 ) return true;

	SmartDbPtr<DbConnection> pTranDb(GetBackEndDbConnection("TranDb"));
	TransformationDataStore store(pTranDb);
	store.DeleteOld(m_iAgeDays);
	return true;
}

