/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <sstream>		                    

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/InterchangeProject/InterchangeProject.h>
#include <konga/ItemAccess/UserAccess.h>
#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "SpecialHandlingDeployFolder.h"				                                           
#include "SpecialHandlingDeployDelete.h"				                                                         
#include "SpecialHandlingDeployDeleteWebService.h"		                                  
#include "SpecialHandlingDeployDeleteTransformation.h"	                                            
#include "SpecialHandlingDeployDeleteOperation.h"		                                                
#include "SpecialHandlingDeployDeleteSourceTarget.h"	                                                       
#include "SpecialHandlingDeployDeleteJmsMessage.h"		                                                      
#include "SpecialHandlingDeployDeleteHttpEndpoint.h"	                        
#include "SpecialHandlingDeployDeleteFolder.h"			                                       
#include "DeployInterchangeDataImpl.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::stringstream;
using std::map;

namespace {
	void FillIdMap(const int entityTypeId, ArrayOf_xsd_string* properties, map<int, vector<long> >& m, GuidToIdMapper& guidMapper) {
		if ( m.find(entityTypeId) == m.end() ) {
			m.insert( map<int, vector<long> >::value_type(entityTypeId, vector<long>()) );
		}
		vector<long>& v = m.find(entityTypeId)->second;

		if ( properties == 0 )
			return;

		xsd__string_Array* pxsd__string_Array = properties->getitem();
		if ( pxsd__string_Array == 0 )
			return;

		int size = 0;
		const xsd__string* arrProperties = pxsd__string_Array->get(size);
		if ( arrProperties == 0 || size == 0 )
			return;

		for( int i = 0; i < size; ++i ) {
			if ( arrProperties[i] == 0 )
				continue;
			const string guid( toString(arrProperties[i]) );
			if ( guid.empty() )
				continue;

			const long lId = guidMapper.GetId(guid, entityTypeId);
			if ( lId == GuidToIdMapper::invalid_id ) {
				UIBACKEND_TRACE("Item '" + guid + "' of type " + toString(entityTypeId) + " does not exist on this server, item ignored.");
				continue;
			}

			v.push_back(lId);
		}
	}
}

void DeployInterchangeDataImpl::HandleDeletedData(const InterchangeIdHolder* const pDeletedData) {
	if ( !pDeletedData ) return;
	if ( !m_pInterchangeProject )
		throw CrException("Internal Error: The InterchangeProject has not been set.");

	map<int, vector<long> > m;
	FillIdMap(HttpEndpointEntityId, pDeletedData->HttpEndpointsTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(OperationEntityId, pDeletedData->OperationsTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(WebServiceEntityId, pDeletedData->WebServicesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(TransformationEntityId, pDeletedData->TransformationsTab_New_ids, m, *m_pGuidToIdMapper);
	FillIdMap(JmsMessageEntityId, pDeletedData->JmsMessagesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(EmailMessageEntityId, pDeletedData->MessagesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(ScheduleEntityId, pDeletedData->SchedulesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(ScriptEntityId, pDeletedData->ScriptsTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(SourceEntityId, pDeletedData->SourcesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(TargetEntityId, pDeletedData->TargetsTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(DocumentEntityId, pDeletedData->DocumentsTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(SalesforceConnectorEntityId, pDeletedData->SalesforceConnectorsTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(SalesforceQueryEntityId, pDeletedData->SalesforceQueriesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(SalesforceUpsertEntityId, pDeletedData->SalesforceUpsertsTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(SalesforceCreateEntityId, pDeletedData->SalesforceCreatesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(SalesforceUpdateEntityId, pDeletedData->SalesforceUpdatesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(SalesforceDeleteEntityId, pDeletedData->SalesforceDeletesTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(SalesforceBulkLoadEntityId, pDeletedData->SalesforceBulkLoadsTab_ids, m, *m_pGuidToIdMapper);
	FillIdMap(FolderEntityId,  pDeletedData->FoldersTab_ids, m, *m_pGuidToIdMapper);

	HandleDeletedDataImpl(m);
}

void DeployInterchangeDataImpl::HandleDeletedDataImpl(const std::map<int, std::vector<long> >& deletedData) {
	                                                                    
	                                                  
	  
	{
		SpecialHandlingDeployDeleteHttpEndpoint httpEndpointSH(*m_pTranDb,
			m_pTranDbRelations->GetDependentTables(getTableName(nameHttpEndpoints)));
		DeleteFromTable(nameHttpEndpoints, deletedData.find(HttpEndpointEntityId)->second, &httpEndpointSH);
		httpEndpointSH.Delete();
	}

	{
	SpecialHandlingDeployDeleteOperation specialHandlingOperation(*m_pTranDb,
		m_pTranDbRelations->GetDependentTables(getTableName(nameOperations)));
	if ( DeleteFromTable(nameOperations, deletedData.find(OperationEntityId)->second, &specialHandlingOperation) > 0 ) {
		specialHandlingOperation.Delete();
		if ( specialHandlingOperation.ScheduledOperationWasDeleted() )
			m_bCallSchedulerUpdate = true;
		if ( specialHandlingOperation.JmsOperationWasDeleted() )
			m_bCallJmsEngineUpdate = true;
	}
	}

	{
	SpecialHandlingDeployDeleteWebService specialHandlingWs(*m_pTranDb,
		m_pTranDbRelations->GetDependentTables(getTableName(nameWebServices)), *m_pGuidToIdMapper);
	DeleteFromTable(nameWebServices, deletedData.find(WebServiceEntityId)->second, &specialHandlingWs);
	specialHandlingWs.Delete();
	}

	                                                                                        

	{
	SpecialHandlingDeployDeleteTransformation specialHandlingTran(*m_pTranDb, *m_pTranDbRelations, *m_pAutoDeleteFileSet);
	DeleteFromTable(nameTransformationsTab_New, deletedData.find(TransformationEntityId)->second, &specialHandlingTran);
	specialHandlingTran.Delete();
	}

	{
	SpecialHandlingDeployDeleteJmsMessage shJmsMessage;
	DeleteFromTable(nameJmsMessage, deletedData.find(JmsMessageEntityId)->second, &shJmsMessage);
	shJmsMessage.DeleteDependents(*m_pTranDb);
	}

	DeleteFromTable(nameMessages, deletedData.find(EmailMessageEntityId)->second);

	DeleteFromTable(nameSchedules, deletedData.find(ScheduleEntityId)->second);

	DeleteFromTable(nameScripts, deletedData.find(ScriptEntityId)->second);

	{
	SpecialHandlingDeployDeleteSource sourceSH(*m_pTranDb, m_pTranDbRelations->GetDependentTables(getTableName(nameSources)));
	DeleteFromTable(nameSources, deletedData.find(SourceEntityId)->second, &sourceSH);
	sourceSH.Delete();
	}

	{
	SpecialHandlingDeployDeleteTarget targetSH(*m_pTranDb, m_pTranDbRelations->GetDependentTables(getTableName(nameTargets)));
	DeleteFromTable(nameTargets, deletedData.find(TargetEntityId)->second, &targetSH);
	targetSH.Delete();
	}

	DeleteFromTable(nameDocuments, deletedData.find(DocumentEntityId)->second);

	DeleteFromTable(nameSalesforceQueries, deletedData.find(SalesforceQueryEntityId)->second);
	DeleteFromTable(nameSalesforceUpserts, deletedData.find(SalesforceUpsertEntityId)->second);
	DeleteFromTable(nameSalesforceCreates, deletedData.find(SalesforceCreateEntityId)->second);
	DeleteFromTable(nameSalesforceUpdates, deletedData.find(SalesforceUpdateEntityId)->second);
	DeleteFromTable(nameSalesforceDeletes, deletedData.find(SalesforceDeleteEntityId)->second);
	DeleteFromTable(nameSalesforceBulkLoads, deletedData.find(SalesforceBulkLoadEntityId)->second);

	{
	SpecialHandlingDeployDeleteFolder folderSH(*m_pTranDb);
	DeleteFromTable(nameFolders, deletedData.find(FolderEntityId)->second, &folderSH);
	}
	                                                   
}

namespace {
	void AddToDeleteInSql(stringstream& s, bool& bFirst, const long lId ) {
		if ( bFirst )
			bFirst = false;
		else {
			s << ",";
		}
		s << lId;
	}
}

int DeployInterchangeDataImpl::DeleteFromTable(const std::string& stName, const vector<long>& idsToDelete,
											   SpecialHandlingDeployDelete* pSpecialHandling)
{
	const string stTableName( getTableName(stName) );
	  
	                                                                          
	                                                                 
	                                                   
	auto_ptr<SpecialHandlingDeployDelete> adopt_default_specialhandling(0);
	if ( pSpecialHandling == 0 ) {
		                                            
		const std::vector<DependentTableInfo>& depTables =
			m_pTranDbRelations->GetDependentTables(stTableName);

			pSpecialHandling =
				new SpecialHandlingDeployDeleteDependents(*m_pTranDb, depTables);
			adopt_default_specialhandling.reset(pSpecialHandling);	                          
	}

	const int iEntityTypeId = GetEntityIdByName(stName);

	stringstream ssDeleteSql;
	ssDeleteSql << "DELETE FROM " << stTableName << " WHERE " + getIdName(stName) + " IN (";

	bool bFirst = true;
	int nDeletedItems = 0;
	for(vector<long>::const_iterator cit = idsToDelete.begin(); cit != idsToDelete.end(); ++cit ) {
		const long lId = *cit;

		                                          
		if ( iEntityTypeId > 0 ) {
			if ( !m_pUserAccess->HasDeletePermission(lId, iEntityTypeId) ) {
				const string& stGuid = m_pGuidToIdMapper->GetGuid(lId, iEntityTypeId);
				AddWarning(iEntityTypeId, stGuid, "Access Denied: The current user does not have delete permission for this item.");
				continue;
			}
			else {
				                                                                                                
				interchange_deploy::DeleteFolderItem(iEntityTypeId, lId, *m_pInterchangeProject, *m_pTranDb);
				if ( m_pInterchangeProject->DeleteItem(iEntityTypeId, lId) ) {
					m_pGuidToIdMapper->DeleteItem(lId, iEntityTypeId);
					AccessControl::DeleteAccessControlForItem(iEntityTypeId, lId, *m_pTranDb);
				}
				else {
					                                                
					                                                                                              
					continue;
				}
			}
		}
		else {
			                                                                                                
			throw CrException("Internal Error: Trying to delete from a table that does not represent an entity type, \
				which is not allowed.");
		}

		if ( pSpecialHandling != 0 )
			pSpecialHandling->HandleRow(lId);

		AddToDeleteInSql(ssDeleteSql, bFirst, lId);
		++nDeletedItems;
	}

	                                                     
	                                                     
	if ( !bFirst ) {
		ssDeleteSql << ")";
		const string stSql( ssDeleteSql.str() );
		UIBACKEND_TRACE("DELETE sql: " + stSql);
		m_pTranDb->ExecuteSql(stSql);
	}
	if ( adopt_default_specialhandling.get() != 0 )	                                                                  
		pSpecialHandling->Delete();

	return nDeletedItems;
}
