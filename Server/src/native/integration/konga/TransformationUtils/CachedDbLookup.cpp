/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifdef WIN32
#include <windows.h>
#endif
#include <sql.h>	             

#include <konga/Db/DbUtils/DbException.h>

#include "DbLookupCache.h"
#include "CachedDbLookup.h"

using namespace konga;
using std::string;
using std::map;
using std::vector;


CachedDbLookup::~CachedDbLookup(void) {
	for( CacheMapT::iterator it = m_cache.begin(); it != m_cache.end(); ++it ) {
		if ( it->second ) {
			delete it->second;
			it->second = 0;
		}
	}
	m_cache.clear();
}

DbLookupResultState CachedDbLookup::lookupValue(const UserInfo& currentUser,
												DbLookupCache& dbLookupCache,
												const string& stSourceTargetGuid,
												const string& stSql,
												std::string& stValue,
												bool bAutoCommit,
												bool bTransaction)
{
	const string key(stSourceTargetGuid + "-" + stSql);

	                                                  
	boost::mutex::scoped_lock cacheLock(m_CacheMutex);

	                            
	CacheMapT::const_iterator citValue = m_cache.find(key);
	if ( citValue != m_cache.end() ) {
		if ( citValue->second == 0 ) {
			stValue.clear();
			return konga::DBLRS_NULL_RESULT;
		}
		else {
			stValue = *(citValue->second);
			return konga::DBLRS_LOOKUP_SUCCESS;
		}
	}

	                                                                             
	DbLookup& dbLookup = dbLookupCache.getDbLookup(currentUser, stSourceTargetGuid);
	try {
		dbLookup.UseAutoCommit(bAutoCommit);
		const konga::DbLookupResultState resState = dbLookup.LookupSingleValue(stSql, stValue);
		if( !bAutoCommit && !bTransaction )
			dbLookup.CommitTransaction();

		if(resState==konga::DBLRS_LOOKUP_SUCCESS ) {
			m_cache.insert( CacheMapT::value_type(key, new string(stValue)) );
		}
		else if ( resState == konga::DBLRS_NULL_RESULT ) {
			stValue.clear();
			m_cache.insert( CacheMapT::value_type(key, 0) );
		}
		else {
			stValue.clear();
		}
		return resState;
	}
	catch( const CrException& ) {
		dbLookup.Close();
		throw;
	}
}

