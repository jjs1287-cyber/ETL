/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <algorithm>		              

#include <konga/Utils/StringUtils.h>		             

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDb/PipelinePluginInstanceTab.h>
#include <konga/Db/TranDb/PipelinePluginPositionTab.h>

#include "PipelinePluginSelector.h"
#include "PipelinePluginManifest.h"
#include "PluginManifestDbStore.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

namespace {
	struct PipelinePluginInstance {
		long InstanceId;
		long PluginId;
		long EntityTypeId;
		long EntityId;
	};
}

PipelinePluginSelector::PipelinePluginSelector(DbConnection* pDbConn) : m_pDbConn(pDbConn), m_pManifestCache(new PluginManifestDbStore(pDbConn)) {
}

PipelinePluginSelector::~PipelinePluginSelector(void) {
	delete m_pManifestCache;
	m_pManifestCache = 0;
}

PluginManifestListT PipelinePluginSelector::GetPipelinePluginInstances(
	long lOperationId,
	long lTransformationId,
	int iPipelineEntityTypeId,
	long lPipelineEntityId,
	int iRelativePosition)
{
	                                                                   
	DbConnection* pConn = m_pDbConn;
	SmartDbPtr<DbConnection> adopt_pConn;
	if ( pConn == 0 ) {
		adopt_pConn.Attach( pConn = konga::GetBackEndDbConnection("TranDb") );
	}

	SmartDbPtr<TranDb::PipelinePluginInstanceTab> pInstanceTab( TranDb::PipelinePluginInstanceTab::MakeInstance(pConn) );

	string sSql(
		"WHERE (EntityTypeId = " + toString(OperationEntityId) +
		" AND EntityId = " + toString(lOperationId) +
		") OR (EntityTypeId = " + toString(iPipelineEntityTypeId) +
		" AND EntityId = " + toString(lPipelineEntityId) + ")"
		);
	pInstanceTab->Select(sSql);

	typedef map<long, PipelinePluginInstance> InstanceMapT;

	InstanceMapT instances;

	string sPipelinePluginInstanceIdSql;
	while( pInstanceTab->Next() ) {
		PipelinePluginInstance instance;
		instance.EntityId = pInstanceTab->GetEntityId();
		instance.EntityTypeId = pInstanceTab->GetEntityTypeId();
		instance.InstanceId = pInstanceTab->GetPipelinePluginInstanceId();
		instance.PluginId = pInstanceTab->GetPluginId();
		instances.insert( InstanceMapT::value_type(instance.InstanceId, instance) );

		if ( !sPipelinePluginInstanceIdSql.empty() )
			sPipelinePluginInstanceIdSql += ",";
		sPipelinePluginInstanceIdSql += toString(instance.InstanceId);
	}

	sSql.clear();
	if ( !instances.empty() )
		sSql += " (PipelinePluginInstanceId IN (" + sPipelinePluginInstanceIdSql +
		") AND PipelineEntityTypeId = " + toString(iPipelineEntityTypeId) + ")";

	if ( !sSql.empty() )
		sSql += " OR";

	sSql += " (PipelinePluginInstanceId = 0 AND PipelineEntityTypeId = " + toString(iPipelineEntityTypeId) + ")";
	sSql = " ( " + sSql + ") AND relativepositionid = "
		+ toString(iRelativePosition);

	SmartDbPtr<TranDb::PipelinePluginPositionTab> pPositionTab( TranDb::PipelinePluginPositionTab::MakeInstance(pConn) );
	sSql = ("WHERE" + sSql);
	pPositionTab->Select(sSql);

	PluginManifestListT manifests;
	vector<long> addedFilters;
	while( pPositionTab->Next() ) {
		long lPipelinePluginInstanceId = pPositionTab->GetPipelinePluginInstanceId();
		long lPluginId = pPositionTab->GetPluginId();

		InstanceMapT::const_iterator cit = instances.find(lPipelinePluginInstanceId);
		if ( lPipelinePluginInstanceId!=0 ) {
			                                           
			if ( cit == instances.end() )
				throw CrException("Internal Error: Pipeline plugin tables are in an inconsistent state. " \
					"PluginInstance referenced in position tab does not exist.");

			const PipelinePluginInstance& instance = cit->second;
			if ( instance.PluginId != lPluginId ) {
				throw CrException("Internal Error: Pipeline plugin tables are in an inconsisten state. " \
					"PluginInstance has different.");
			}
		}

		if ( std::find(addedFilters.begin(), addedFilters.end(), lPluginId) == addedFilters.end() ) {
			boost::shared_ptr<PluginManifest> manifest = m_pManifestCache->GetManifest(lPluginId);
			manifests.push_back(manifest);
			addedFilters.push_back(lPluginId);
		}
	}

	return manifests;
}

vector<PipelinePluginAssociationInfo> PipelinePluginSelector::GetPipelinePluginAssociations(int iEntityTypeId, long lEntityId, DbConnection& dbConn) {
	vector<PipelinePluginAssociationInfo> retVal;

	string sql("select ppp.pluginid, ppp.pipelineentitytypeid, ppp.relativepositionid, ppi.entityid, p.pluginname " \
		"from pipelinepluginpositiontab as ppp " \
		"join pipelineplugininstancetab as ppi on ppp.pipelineplugininstanceid = ppi.pipelineplugininstanceid " \
		"join pluginstab as p on ppp.pluginid = p.pluginid where ppi.entitytypeid = "
		+ toString(iEntityTypeId) + " and ppi.entityid = " + toString(lEntityId));

	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConn, sql) );
	rs->Open();
	if ( !rs->Next() )
		return retVal;

	do {
		const long lPluginId = atol(rs->GetFieldValue(0).c_str());

		                                                 
		boost::shared_ptr<PluginManifest> pManifest(m_pManifestCache->GetManifest(lPluginId));
		if ( pManifest.get() == 0 )
			continue;

		PipelinePluginManifest* ptmp = dynamic_cast<PipelinePluginManifest*>(pManifest.get());
		if ( ptmp == 0 )
			continue;

		PipelinePluginAssociationInfo pluginInfo;
		pluginInfo.PluginManifest = pManifest;
		pluginInfo.PluginId = atol(rs->GetFieldValue(0).c_str());
		pluginInfo.EntityTypeId = atoi(rs->GetFieldValue(1).c_str());
		pluginInfo.RelativePositionId = atoi(rs->GetFieldValue(2).c_str());
		pluginInfo.EntityId = atol(rs->GetFieldValue(3).c_str());

		retVal.push_back( pluginInfo );
	}
	while ( rs->Next() );

	return retVal;
}

namespace {
	class ComparePipelinePluginManifests {
	public:
		ComparePipelinePluginManifests(int iEntityType) : m_iEntityType(iEntityType) {}
		~ComparePipelinePluginManifests(void) {}

		                            
		bool operator()(const boost::shared_ptr<PluginManifest>& _lhs, const boost::shared_ptr<PluginManifest>& _rhs) const {
			PipelinePluginManifest* lhs = dynamic_cast<PipelinePluginManifest*>(_lhs.get());
			if ( lhs == 0 )
				return true;
			PipelinePluginManifest* rhs = dynamic_cast<PipelinePluginManifest*>(_rhs.get());
			if ( rhs == 0 )
				return true;

			vector<PipelinePosition> positions_lhs = lhs->GetPipelinePositions();
			vector<PipelinePosition>::const_iterator citPosLhs = positions_lhs.begin();
			for(; citPosLhs != positions_lhs.end(); ++citPosLhs ) {
				const PipelinePosition& posLhs = *citPosLhs;
				if ( posLhs.GetEntityTypeId() == m_iEntityType ) {
					vector<PipelinePosition> positions_rhs = rhs->GetPipelinePositions();
					vector<PipelinePosition>::const_iterator citPosRhs = positions_rhs.begin();
					for(; citPosRhs != positions_rhs.end(); ++citPosRhs ) {
						const PipelinePosition& posRhs = *citPosRhs;
						if ( posRhs.GetEntityTypeId() == m_iEntityType ) {
							if ( posLhs.GetRelativePositionId() == posRhs.GetRelativePositionId() ) {
								return posLhs.getExecutionOrder() < posRhs.getExecutionOrder();
							}
							else {
								return posLhs.GetRelativePositionId() < posRhs.GetRelativePositionId();
							}
						}
					}
				}
			}

			return true;
		}

	private:
		int m_iEntityType;
	};
}

void konga::SortPipelinePlugins(PluginManifestListT& manifests, int iEntityType) {
	ComparePipelinePluginManifests comparer(iEntityType);
	std::sort<PluginManifestListT::iterator, ComparePipelinePluginManifests>(
		manifests.begin(), manifests.end(), comparer);
}
