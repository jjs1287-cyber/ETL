/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Db/TranDb/PluginsTab.h>
#include <konga/Db/TranDb/PipelinePluginInstanceTab.h>
#include <konga/Db/TranDb/PipelinePluginPositionTab.h>

#include "PipelinePluginEntityManager.h"

using namespace konga;

long PipelinePluginEntityManager::GetPluginId(const std::string& sPluginGuid) const {
	PluginGuidIdMapT::const_iterator citPluginGuid = m_pluginIdCache.find(sPluginGuid);
	if ( citPluginGuid != m_pluginIdCache.end() )
		return citPluginGuid->second;

	SmartDbPtr<TranDb::PluginsTab> pPluginsTab( TranDb::PluginsTab::MakeInstance(&m_dbConnection) );
	pPluginsTab->Select("WHERE PluginName = '" + SqlEscape(sPluginGuid) + "'");
	if ( pPluginsTab->Next() ) {
		const long lPluginId = pPluginsTab->GetPluginId();
		m_pluginIdCache.insert( PluginGuidIdMapT::value_type(sPluginGuid, lPluginId) );
		return lPluginId;
	}
	return 0;
}

void PipelinePluginEntityManager::AddPluginInstance(int entityTypeId,
												  long entityId,
												  int pipelineEntityTypeId,
												  int relativePositionId,
												  long pluginId)
{
	if ( pluginId < 1 )
		return;

	SmartDbPtr<TranDb::PipelinePluginInstanceTab> pPipelinePluginInstanceTab(
		TranDb::PipelinePluginInstanceTab::MakeInstance(&m_dbConnection));

	pPipelinePluginInstanceTab->Select("WHERE EntityTypeId = " + toString(entityTypeId)
		+ " AND EntityId = " + toString(entityId) + " AND PluginId = " + toString(pluginId));

	long pipelinePluginInstanceId = -1;
	if ( pPipelinePluginInstanceTab->Next() ) {
		pipelinePluginInstanceId = pPipelinePluginInstanceTab->GetPipelinePluginInstanceId();
	}
	else {
		pipelinePluginInstanceId = GetNextId(pPipelinePluginInstanceTab->GetTableName(),
			"PipelinePluginInstanceId", m_dbConnection);
		pPipelinePluginInstanceTab->AddNew();
		pPipelinePluginInstanceTab->SetPipelinePluginInstanceId(pipelinePluginInstanceId);
		pPipelinePluginInstanceTab->SetPluginId(pluginId);
		pPipelinePluginInstanceTab->SetEntityTypeId(entityTypeId);
		pPipelinePluginInstanceTab->SetEntityId(entityId);
		pPipelinePluginInstanceTab->Update();
	}
	pPipelinePluginInstanceTab.CloseAndDelete();

	SmartDbPtr<TranDb::PipelinePluginPositionTab> pPipelinePluginPositionTab(
		TranDb::PipelinePluginPositionTab::MakeInstance(&m_dbConnection));

	pPipelinePluginPositionTab->Select("WHERE PipelinePluginInstanceId = " + toString(pipelinePluginInstanceId)
		+ " AND PluginId = " + toString(pluginId)
		+ " AND PipelineEntityTypeId = " + toString(pipelineEntityTypeId)
		+ " AND RelativePositionId = " + toString(relativePositionId));
	
	if ( !pPipelinePluginPositionTab->Next() ) {
		const long pipelinePluginPositionId = GetNextId(pPipelinePluginPositionTab->GetTableName(),
			"PipelinePluginPositionId", m_dbConnection);
		pPipelinePluginPositionTab->AddNew();
		pPipelinePluginPositionTab->SetPipelinePluginPositionId(pipelinePluginPositionId);
		pPipelinePluginPositionTab->SetPluginId(pluginId);
		pPipelinePluginPositionTab->SetPipelinePluginInstanceId(pipelinePluginInstanceId);
		pPipelinePluginPositionTab->SetPipelineEntityTypeId(pipelineEntityTypeId);
		pPipelinePluginPositionTab->SetRelativePositionId(relativePositionId);
		pPipelinePluginPositionTab->Update();
	}

}

void PipelinePluginEntityManager::RemovePlugins(int entityTypeId, long entityId) {
	SmartDbPtr<TranDb::PipelinePluginInstanceTab> pPipelinePluginInstanceTab(
		TranDb::PipelinePluginInstanceTab::MakeInstance(&m_dbConnection));

	pPipelinePluginInstanceTab->Select("WHERE EntityTypeId = " + toString(entityTypeId)
		+ " AND EntityId = " + toString(entityId));

	if ( !pPipelinePluginInstanceTab->Next() )
		return;

	const long pipelineplugininstanceid = pPipelinePluginInstanceTab->GetPipelinePluginInstanceId();
	pPipelinePluginInstanceTab->Delete();

	SmartDbPtr<TranDb::PipelinePluginPositionTab> pPipelinePluginPositionTab(
		TranDb::PipelinePluginPositionTab::MakeInstance(&m_dbConnection));

	m_dbConnection.ExecuteSql("DELETE FROM " + pPipelinePluginPositionTab->GetTableName()
		+ " WHERE PipelinePluginInstanceId = " + toString(pipelineplugininstanceid));
}
