/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_UTILS_CACHE_DB_LOOKUP_H
#define TRANSFORMATION_UTILS_CACHE_DB_LOOKUP_H

#include <string>
#include <map>

#include <boost/noncopyable.hpp>
#include <boost/thread/mutex.hpp>

#include <konga/Utils/HashTable.h>

namespace konga {

	class DbLookupCache;
	class UserInfo;

	                             
	                                                         
	class CachedDbLookup : boost::noncopyable {
	public:
		CachedDbLookup(void) {}
		~CachedDbLookup(void);

		DbLookupResultState lookupValue(const UserInfo& currentUser, DbLookupCache& dbLookupCache, const std::string& stSourceTargetGuid,
			const std::string& stSql, std::string& stValue, bool bAutoCommit, bool bTransaction);

	private:
		typedef std::map<std::string, std::string*> CacheMapT;
		CacheMapT m_cache;
		boost::mutex m_CacheMutex;	                                                            
	};
}

#endif                                          

