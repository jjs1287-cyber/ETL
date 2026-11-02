/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_DEPLOY_PIPELINE_PLUGIN_HANDLER_H
#define ETLFORGE_DEPLOY_PIPELINE_PLUGIN_HANDLER_H

#include "ItemWarningHolder.h"

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

	namespace interchange_deploy {
	
		struct InterchangeDataTableStruct;

		class PipelinePluginHandler : public ItemWarningHolder {
		public:
			PipelinePluginHandler(DbConnection& dbConnection,
				const GuidToIdMapper& guidToIdMapper) :
			m_dbConnection(dbConnection),
				m_guidToIdMapper(guidToIdMapper)
			{}

			~PipelinePluginHandler(void) {}

                                                                                  
			void HandlePipelinePlugins(const InterchangeDataTableStruct& pipelinePluginAssociations);

		private:
			DbConnection& m_dbConnection;
			const GuidToIdMapper& m_guidToIdMapper;
		};
	}                      
}                   

#endif                                             

