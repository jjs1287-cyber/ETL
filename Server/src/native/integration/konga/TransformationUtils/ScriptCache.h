/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_UTILS_SCRIPT_CACHE_H
#define TRANSFORMATION_UTILS_SCRIPT_CACHE_H

#include <boost/noncopyable.hpp>
#include <boost/thread/recursive_mutex.hpp>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include <konga/Utils/PointerCache.h>

#include <map>
#include <string>

namespace konga {

	                                                                         
	class ScriptCache : private DbConnectionHolder {
	public:
		ScriptCache();
		explicit ScriptCache(DbConnection* pDbConnection);
		~ScriptCache(void) {}

		void getScript(const std::string& sScriptGuid, std::string& sScriptName, std::string& sScript, long& lScriptId);

		void getScript(long scriptId, std::string& sScriptName, std::string& sScript);

	private:
		                                                                             
		void getScriptImpl(long& lScriptId, const std::string& sScriptGuid, std::string& sScriptName, std::string& sScript);

	private:
		                                     
		typedef std::map< std::string, std::pair<std::string, std::string> > ScriptCacheT;
		ScriptCacheT m_ScriptCache;
		mutable boost::recursive_mutex m_ThreadSyncLock;
	};
}                   

#endif                                       
