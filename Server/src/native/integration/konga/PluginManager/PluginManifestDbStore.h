/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_PLUGIN_MANIFEST_DB_STORE_H
#define KONGA_PLUGIN_MANIFEST_DB_STORE_H

#include <string>
#include <map>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include "PluginManagerApi.h"
#include "PluginManifestStore.h"

namespace TranDb {
	class PluginsTab;
}

namespace konga {

	class DbConnection;

	                                         
	                                       
	class PLUGIN_MANAGER_API PluginManifestDbStore : public PluginManifestStore, protected DbConnectionHolder {
	public:
		PluginManifestDbStore(void);
		PluginManifestDbStore(DbConnection* pDbConnection) : DbConnectionHolder(pDbConnection) {
			this->SetInternalDbName("TranDb");	                                                              
		};

		virtual ~PluginManifestDbStore(void) {}

		boost::shared_ptr<PluginManifest> GetManifest(long lPluginId);

		boost::shared_ptr<PluginManifest> GetManifest(const std::string& stUniqueId);

	private:
		boost::shared_ptr<PluginManifest> AddManifestToCache(TranDb::PluginsTab& pPluginsTab);

	private:
		typedef std::map<long, boost::shared_ptr<PluginManifest> > ManifestCacheMapT;
		ManifestCacheMapT m_ManifestCache;
		std::map<std::string, long> m_GuidMapCache;	                                                  
	};
}

#endif                                    

