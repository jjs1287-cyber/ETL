/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_PIPELINE_PLUGIN_SELECTOR_H
#define KONGA_PIPELINE_PLUGIN_SELECTOR_H

#include <vector>
#include <map>
#include <string>

#include <boost/shared_ptr.hpp>

#include "PluginManifest.h"
#include "PluginManifestStore.h"
#include "PluginManagerApi.h"

namespace konga {

	class DbConnection;

	typedef std::vector< boost::shared_ptr<PluginManifest> > PluginManifestListT;

	struct PipelinePluginAssociationInfo {
		long PluginId;	                                      
		boost::shared_ptr<konga::PluginManifest> PluginManifest;		                                                         

		int RelativePositionId;
		int EntityTypeId;
		long EntityId;
	};

	                                                                            
	                                                                                    
	                                               
	PLUGIN_MANAGER_API void SortPipelinePlugins(PluginManifestListT& manifests,
		int iEntityType);

	                                                       
	class PLUGIN_MANAGER_API PipelinePluginSelector {
	public:
		PipelinePluginSelector(DbConnection* pDbConn = 0);
		virtual ~PipelinePluginSelector(void);

	private:
		PipelinePluginSelector( const PipelinePluginSelector& );
		const PipelinePluginSelector& operator=( const PipelinePluginSelector& );

	public:

		   
                                                                                                        
                                                        
     
		PluginManifestListT GetPipelinePluginInstances(long lOperationId,
			long lTransformationId, int iPipelineEntityTypeId,
			long lPipelineEntityId, int iRelativePosition);

		std::vector<PipelinePluginAssociationInfo> GetPipelinePluginAssociations(int iEntityTypeId, long lEntityId, DbConnection& dbConn);

	private:
		DbConnection* m_pDbConn;
		PluginManifestStore* m_pManifestCache;
	};
}

#endif                                    

