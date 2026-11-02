/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>

#include <konga/Server/Server.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/ItemAccess/UserAccess.h>

#include "ExtendedItemProperties.h"
#include "DeployInterchangeDataImpl.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::map;

void DeployInterchangeDataImpl::FillExtendedItemProperties(ExtendedItemPropertiesCollection& propertiesCollection,
														   const InterchangeDataTable* const pEntityProperties)
{
	if ( pEntityProperties == 0 )
		return;

	InterchangeDataTableStruct extendedProperties;
	FillInterchangeDataTableStruct(*pEntityProperties, extendedProperties);
	if ( extendedProperties.m_vRows.empty() )
		return;

	for( vInterchangeDataRowStructT::const_iterator cit = extendedProperties.m_vRows.begin();
		cit != extendedProperties.m_vRows.end(); ++cit )
	{
		const InterchangeDataRowStruct& row = *cit;
		if ( row.m_vFields.at(0) == 0 )
			continue;
		const string& sEntityTypeId = row.m_vFields.at(0).str();
		const int iEntityTypeId = konga::toLong(sEntityTypeId);
		
		if ( row.m_vFields.at(1) == 0 )
			continue;
		const string& guid = row.m_vFields.at(1).str();

		if ( row.m_vFields.at(2) == 0 )
			continue;
		const string& keyName = row.m_vFields.at(2).str();
		if ( keyName.empty() )
			continue;

		if ( row.m_vFields.at(3) == 0 )
			continue;
		const string& keyValue = row.m_vFields.at(3).str();

		ExtendedItemProperties* pProperties = propertiesCollection.GetExtendedItemPropertiesForGuid(guid);
		if ( pProperties == 0 ) {
			pProperties = new ExtendedItemProperties(iEntityTypeId, guid);
			propertiesCollection.AddExtendedItemProperties(guid, pProperties);
		}
		assert(pProperties);

		pProperties->m_properties.AddString(keyName, keyValue);
	}
}

namespace {
	const string NO_LABEL("None");
}

void DeployInterchangeDataImpl::HandleExtendedItemPropertiesPreDeploy(const ExtendedItemPropertiesCollection& propertiesCollection) {
	const string sServerLabel( konga::GetServerLabel() );
	if ( sServerLabel.empty() || EqualsIgnoreCase(sServerLabel, NO_LABEL) )
		return;

	map<string, ExtendedItemProperties*>::const_iterator cit = propertiesCollection.m_PropertiesMap.begin();
	for(; cit != propertiesCollection.m_PropertiesMap.end(); ++cit ) {
		const int iEntityTypeId = cit->second->GetEntityTypeId();
		if ( iEntityTypeId == OperationEntityId ) {
			const string& sItemLabel = cit->second->m_properties.GetString(OPERATION_PROPERTY_LABEL, NO_LABEL);
			if ( !EqualsIgnoreCase(sItemLabel, sServerLabel) )
				throw CrException("Only operations labeled \"" + sServerLabel + "\" can be deployed to this server.");
		}
	}
}

void DeployInterchangeDataImpl::InsertExtendedItemProperties(const ExtendedItemPropertiesCollection& propertiesCollection)
{
	if ( propertiesCollection.empty() )
		return;

	ItemProperties ip(m_pTranDb);
	std::map<string, int> deleted;

	map<string, ExtendedItemProperties*>::const_iterator itemIt = propertiesCollection.m_PropertiesMap.begin();

	                      
	for(; itemIt != propertiesCollection.m_PropertiesMap.end(); ++itemIt ) {
		const string& guid = itemIt->first;
		ExtendedItemProperties* properties = itemIt->second;
        if (properties->GetEntityTypeId() == ProjectVariableEntityId) continue;
        if ((properties->GetEntityTypeId() == XsltTransformEntityId || properties->GetEntityTypeId() == ProjectVariableEntityId) && properties->m_properties.GetString("__deleted", "") == "1") continue;
		const int iEntityTypeId = properties->GetEntityTypeId();

		const long lEntityId = m_pDbGuidToIdMapper->GetId(guid, iEntityTypeId);
		if ( lEntityId == GuidToIdMapper::invalid_id )
			throw CrException("Client Error: No " + GetEntityNameByEntityIdSingular(iEntityTypeId)
				+ " with GUID = " + guid + " has been deployed to this server.");	                                                                            

		properties->SetItemId(lEntityId);
		                           
		if ( !m_pUserAccess->HasWritePermission(lEntityId, iEntityTypeId) ) {
			                                                                               
			                                                                 
			continue;
		}
		ip.Delete(iEntityTypeId, lEntityId);

		                                      
		KeyValues::const_iterator keyValuesIt = properties->m_properties.begin();
		for(; keyValuesIt != properties->m_properties.end(); ++keyValuesIt ) {

			const string& keyName = keyValuesIt->first;
			const string& keyValue = keyValuesIt->second;

			                                                  
			if ( !m_bCallSchedulerUpdate &&
				 iEntityTypeId == OperationEntityId &&
				 EqualsIgnoreCase(keyName, OPERATION_PROPERTY_SCHEDULE_POLICY))
			{
				string sOldSchedulePolicy;
				map<int, std::string>::const_iterator cit = m_oldSchedulePolicyForOperation.find(lEntityId);
				if ( cit != m_oldSchedulePolicyForOperation.end() )
					sOldSchedulePolicy = cit->second;

				if ( !EqualsIgnoreCase(keyValue, sOldSchedulePolicy) )
					m_bCallSchedulerUpdate = true;	                                 
			}
			ip.Add(iEntityTypeId, lEntityId, keyName, keyValue);
		}
	}
}

