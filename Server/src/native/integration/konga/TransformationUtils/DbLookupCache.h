/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_UTILS_DB_LOOKUP_CACHE_H
#define TRANSFORMATION_UTILS_DB_LOOKUP_CACHE_H

#include <boost/noncopyable.hpp>

#include <konga/Utils/PointerCache.h>

#include <konga/Db/DbUtils/DbLookup.h>

#include <string>

namespace konga {
	class DbLookup;
	class OperationDataElementHolder;
	class UserInfo;

	class DbLookupCache : boost::noncopyable {
	public:
		DbLookupCache(OperationDataElementHolder& deHolder) : m_pOperationDataElementHolder(&deHolder) {}
		~DbLookupCache(void);

		                                                                                             
		DbLookup& getDbLookup(const UserInfo& currentUser, const std::string& stSourceTargetGuid);

		                                                                        
		void closeAll();

		void closeOne(DbLookup& dbLookup);

		void rollbackOne(DbLookup& dbLookup);

	private:
		OperationDataElementHolder* m_pOperationDataElementHolder;
		PointerCache<std::string, konga::DbLookup> m_DbLookupCache;
	};
}

#endif                                          

