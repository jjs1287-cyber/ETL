/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_PIPELINE_PLUGIN_ENTITY_MANAGER_H
#define ETLFORGE_PIPELINE_PLUGIN_ENTITY_MANAGER_H

#include <map>
#include <string>

namespace konga {

	class DbConnection;

	                                                 
	class PipelinePluginEntityManager {
	public:
		PipelinePluginEntityManager(DbConnection& dbConnection) :
		m_dbConnection(dbConnection) {}
		~PipelinePluginEntityManager(void) {}

		                                               
		                                                    
		                 
		long GetPluginId(const std::string& sPluginGuid) const;

		                                                                    
		                     
		void AddPluginInstance(int entityTypeId,
			long entityId, int pipelineEntityTypeId,
			int relativePositionId, long pluginId);

		void RemovePlugins(int entityTypeId, long entityId);

	private:
		DbConnection& m_dbConnection;

		typedef std::map<std::string, long> PluginGuidIdMapT;
		mutable PluginGuidIdMapT m_pluginIdCache;
	};
}

#endif                                             

