/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <stdlib.h>		              

#include <map>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>
#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/PluginManager/PipelinePosition.h>
#include <konga/PipelinePluginEntityManager/PipelinePluginEntityManager.h>

#include "InterchangeDataStructs.h"
#include "PipelinePluginHandler.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::map;

namespace {
	                                                                            
	                                            
	class MapEntity {
	public:
		MapEntity(int entityTypeId, long entityId) :
		  m_iEntityTypeId(entityTypeId), m_lEntityId(entityId)
		  {}
		~MapEntity(void) {}

	private:
		friend bool operator< (const MapEntity& lhs, const MapEntity& rhs);
		int m_iEntityTypeId;
		long m_lEntityId;
	};

	bool operator< (const MapEntity& lhs, const MapEntity& rhs) {
		if ( lhs.m_iEntityTypeId < rhs.m_iEntityTypeId )
			return true;
		if ( lhs.m_iEntityTypeId > rhs.m_iEntityTypeId )
			return false;

		return (lhs.m_lEntityId < rhs.m_lEntityId);
	}
}
                                                                                                       
void PipelinePluginHandler::HandlePipelinePlugins(const InterchangeDataTableStruct& pipelinePluginAssociations) {

	PipelinePluginEntityManager mgr(m_dbConnection);
	typedef map<MapEntity, int> EntityMapT;
	EntityMapT newEntityInstances;	                                                        
	for( vInterchangeDataRowStructT::const_iterator cit = pipelinePluginAssociations.m_vRows.begin();
		cit != pipelinePluginAssociations.m_vRows.end(); ++cit )
	{
		const vFieldsT& fields = cit->m_vFields;
		if ( fields.size() != 5 )
			continue;

		const int entityTypeId = atoi( fields.at(0).str().c_str() );
		const string sEntityGuid(fields.at(1).str());
		const int pipelineEntityTypeId = atoi( fields.at(2).str().c_str() );
		const string sRelativePositionId( fields.at(3) );
		const string sPluginGuid( fields.at(4) );

		const long entityId = m_guidToIdMapper.GetId(sEntityGuid, entityTypeId);
		if ( entityId == GuidToIdMapper::invalid_id ) {
			AddWarning(0, "", "Client Error: No entity (EntityTypeId = " + toString(entityTypeId)
				+ ") with GUID=" + sEntityGuid + " exists on the server. You may want to deploy your whole project again.");
			continue;
		}

		                                
		const MapEntity entity(entityTypeId, entityId);
		if ( newEntityInstances.find(entity) == newEntityInstances.end() ) {
			mgr.RemovePlugins(entityTypeId, entityId);
			newEntityInstances.insert( EntityMapT::value_type(entity, 1) );
		}

		                                                                       
		if ( sPluginGuid.empty() && pipelineEntityTypeId == -1 && sRelativePositionId.empty() )
			continue;

		const long pluginId = mgr.GetPluginId(sPluginGuid);
		if ( pluginId == 0 ) {
			AddWarning(entityTypeId, sEntityGuid,
				"There is no plugin on this server with the unique name \""
				+ sPluginGuid + "\". You need to install the plugin and deploy your project again. " \
				"This item has been deployed but no pipeline plugin will be executed.");
			continue;
		}

		int relativePositionId = -1;
		try {
			relativePositionId = PipelinePosition::GetRelativePositionId(sRelativePositionId, entityTypeId);
		}
		catch( const CrException& ex ) {
			AddWarning(entityTypeId, sEntityGuid,
				"Failed to add filter \"" + sPluginGuid + "\" to this entity. "
				+ ex.GetErrorString());
			continue;
		}
		mgr.AddPluginInstance(entityTypeId, entityId, pipelineEntityTypeId,
			relativePositionId, pluginId);
	}
}
