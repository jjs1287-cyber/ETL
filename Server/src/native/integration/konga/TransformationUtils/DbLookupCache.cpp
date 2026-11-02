/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cstdio>
#ifdef _DEBUG
#include <cassert>
#endif

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDbUtils/DatabaseDriverType.h>

#include <konga/JdbcUtils/JdbcDbLookup.h>

#include "TranDBUtils.h"
#include "DbLookupCache.h"

using namespace konga;
using std::string;

namespace {

	class DbLookupCreator {
	public:
		DbLookupCreator(const UserInfo& currentUser, const string& stSourceTargetGuid, OperationDataElementHolder* pOperationDataElementHolder)
			: m_pOperationDataElementHolder(pOperationDataElementHolder),
			m_CurrentUser(currentUser),
			m_stSourceTargetGuid(stSourceTargetGuid)
		{}

		DbLookup* operator()() const {
			SmartDbPtr<DbConnection> pTranDb( konga::GetTranDbConnection() );
			DbConnectionInfoStruct dbConnInfo;
			const string& connectionString = getConnectionStringFromDb(pTranDb, m_CurrentUser, m_stSourceTargetGuid, m_pOperationDataElementHolder,
				dbConnInfo);
			if ( dbConnInfo.driverType == ODBC ) {
				return CreateDbLookup(connectionString, dbConnInfo.txnIsolationLevel);
			}
			else {
				return JdbcDbLookup::createJdbcDbLookup(*pTranDb, m_CurrentUser, m_stSourceTargetGuid, dbConnInfo);
			}
		}

	private:
		OperationDataElementHolder* m_pOperationDataElementHolder;
		const UserInfo& m_CurrentUser;
		string m_stSourceTargetGuid;
	};
};

DbLookupCache::~DbLookupCache(void) {
	                                                                                     
	try {
		m_DbLookupCache.Clear();
	}
	catch( const CrException& ex ) {
		printf("This error should not happen here: %s", ex.GetErrorString().c_str());
#ifdef _DEBUG
		assert(false);
#endif
	}
}

DbLookup& DbLookupCache::getDbLookup(const UserInfo& currentUser, const string& stSourceTargetGuid) {
	return m_DbLookupCache.Get(stSourceTargetGuid, DbLookupCreator(currentUser, stSourceTargetGuid, m_pOperationDataElementHolder));
}

void DbLookupCache::closeAll() {
	m_DbLookupCache.Clear();
}

void DbLookupCache::closeOne(DbLookup& dbLookup) {
	dbLookup.CommitTransaction();
	m_DbLookupCache.Remove(dbLookup);
}

void DbLookupCache::rollbackOne(DbLookup& dbLookup) {
	dbLookup.RollbackTransaction();
	m_DbLookupCache.Remove(dbLookup);
}
