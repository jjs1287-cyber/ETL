/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <memory>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/FunctionIdMapper.h>
#include <konga/InterchangeProject/InterchangeProject.h>

#include <konga/OperationContext/ProjectVariableDefaults.h>
#include <konga/Crypto/Crypto.h>
#include "ExtendedItemProperties.h"
#include <konga/DbRelations/EntityIdConstants.h>
#include "DeployUtils.h"
#include "TableHandler.h"

#include "SpecialHandlingDeploy.h"
#include "DefaultSpecialHandlingDeploy.h"
#include "SpecialHandlingDeployWebService.h"		                                              
#include "SpecialHandlingDeployCrypto.h"			                             
#include "SpecialHandlingDeployTransformation.h"	                                          
#include "SpecialHandlingDeployBase64Col.h"			                                                    
#include "SpecialHandlingDeployFolder.h"			                                                                                   
#include "SpecialHandlingDeployOperation.h"			                                                                                         
#include "SpecialHandlingDeployCallRestrictions.h"	                                                
#include "SpecialHandlingDeployTranChainLinks.h"	                                                        
#include "SpecialHandlingDeployDocumentUpdate.h"	                                             
#include "SpecialHandlingDeploySourceTarget.h"		                                                                        
#include "SpecialHandlingDeployMessage.h"			                                                      
#include "SpecialHandlingDeployJmsMessage.h"		                                                                   
#include "SpecialHandlingDeployHttpEndpoint.h"		                                    
#include "SpecialHandlingDeploySalesforceQuery.h"	                                                               
#include "SpecialHandlingDeploySalesforceUpsert.h"	                                                               
#include "SpecialHandlingDeploySalesforceCreate.h"	                                                               
#include "SpecialHandlingDeploySalesforceUpdate.h"	                                                               
#include "SpecialHandlingDeploySalesforceDelete.h"	                                                               
#include "SpecialHandlingDeploySalesforceBulkLoad.h"                                                               

#include "SourceTargetXmlHandler.h"					                                                                              

#include "DeployInterchangeDataImpl.h"

using namespace konga;
using namespace konga::interchange_deploy;

void DeployInterchangeDataImpl::HandleChangedData(const InterchangeDataHolder* const pChangedData, const ExtendedItemPropertiesCollection& itemPropertiesCollection) {
	if ( !pChangedData )
		return;
	const InterchangeDataHolder& changedData = *pChangedData;

	if ( !m_pInterchangeProject )
		throw CrException("Internal Error: No Interchange Project has been set.");

	                                                                                   
	                                                   
	  
	InterchangeDataTableStruct folderItemTab;
	if ( changedData.FolderItemTab != 0 )
		FillInterchangeDataTableStruct(*(changedData.FolderItemTab), folderItemTab);
	FolderItemStruct folderItemStruct(folderItemTab);

	TableHandler tableHandler(*m_pTranDb, *m_pTranDbRelations, *m_pGuidToIdMapper,
		*m_pTypeInfoLoader, *m_pHighestIdFinder, m_UserInfo, *m_pUserAccess, folderItemStruct,
		*m_pInterchangeProject);

	DefaultSpecialHandlingDeployInsert defaultInsertSpecialHandling;
	DefaultSpecialHandlingDeployUpdate defaultUpdateSpecialHandling;

	                                                                                                  
	                                                       

	                                                                                     
	                                      
	InterchangeDataTableStruct interchangeProjectsTab;
	FillInterchangeDataTableStruct(*changedData.InterchangeProjectsTab, interchangeProjectsTab);
	tableHandler.HandleTable(nameInterchangeProjects, interchangeProjectsTab,
		&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);

	                                                                 
	                                                                           
	const long lInterchangeProjectId =
		atol( interchangeProjectsTab.m_vRows.at(0).m_vFields.at(0).str().c_str() );
	m_pInterchangeProject->SetProjectId(lInterchangeProjectId);
	if ( !m_pInterchangeProject->Exists() )
		throw CrException("Internal Error: The Interchange Project was not inserted.");

	                                                                     
    InterchangeDataTableStruct variables;
    variables.m_vColumnNames.push_back("projectvariable_id");
    variables.m_vColumnNames.push_back("projectvariable_name");
    variables.m_vColumnNames.push_back("default_value");
    variables.m_vColumnNames.push_back("is_password");
    variables.m_vColumnNames.push_back("description");
    for (std::map<std::string, ExtendedItemProperties*>::const_iterator it = itemPropertiesCollection.m_PropertiesMap.begin();
         it != itemPropertiesCollection.m_PropertiesMap.end(); ++it) {
        if (it->second->GetEntityTypeId() != ProjectVariableEntityId) continue;
        if (it->second->m_properties.GetString("__deleted", "") == "1") {
            const long id = m_pGuidToIdMapper->GetId(it->first, ProjectVariableEntityId);
            if (id != GuidToIdMapper::invalid_id) DeleteFromTable(nameProjectVariables, std::vector<long>(1, id));
            continue;
        }
        const std::string name = it->second->m_properties.GetString("projectvariable_name", "");
        const std::string encoded = it->second->m_properties.GetString("default_value_utf8", "");
        if (encoded.size() > 1398104) throw CrException("Project variable preset exceeds 1 MiB.");
        int decodedLength=0;
        std::unique_ptr<unsigned char[]> decoded(konga::DecodeBase64(encoded, decodedLength));
        const std::string value(reinterpret_cast<const char*>(decoded.get()), decodedLength);
        etlforge::validateProjectVariable(name, value);
        const std::string descriptionEncoded = it->second->m_properties.GetString("description_utf8", "");
        if (descriptionEncoded.size() > 1398104) throw CrException("Project variable description exceeds 1 MiB.");
        int descriptionLength = 0;
        std::unique_ptr<unsigned char[]> descriptionBytes(konga::DecodeBase64(descriptionEncoded, descriptionLength));
        etlforge::validateProjectVariable(name, std::string(reinterpret_cast<const char*>(descriptionBytes.get()), descriptionLength));
        const std::string password = it->second->m_properties.GetString("is_password", "0");
        if (password != "0" && password != "1") throw CrException("Invalid project variable password flag.");
        InterchangeDataRowStruct row;
        row.m_vFields.push_back(ClientField(it->first.c_str()));
        row.m_vFields.push_back(ClientField(name.c_str()));
        row.m_vFields.push_back(ClientField(encoded.c_str()));
        row.m_vFields.push_back(ClientField(password.c_str()));
        row.m_vFields.push_back(ClientField(descriptionEncoded.c_str()));
        variables.m_vRows.push_back(row);
    }
    if (!variables.m_vRows.empty()) {
        tableHandler.HandleTable(nameProjectVariables, variables, &defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
        for (std::map<std::string, ExtendedItemProperties*>::const_iterator it = itemPropertiesCollection.m_PropertiesMap.begin();
             it != itemPropertiesCollection.m_PropertiesMap.end(); ++it) {
            if (it->second->GetEntityTypeId() != ProjectVariableEntityId || it->second->m_properties.GetString("__deleted", "") == "1") continue;
            const long id = m_pGuidToIdMapper->GetId(it->first, ProjectVariableEntityId);
            if (id != GuidToIdMapper::invalid_id && m_pUserAccess->HasWritePermission(id, ProjectVariableEntityId))
                m_pTranDb->ExecuteSql("UPDATE projectvariablestab SET default_value=convert_from(decode(default_value,'base64'),'UTF8'), description=convert_from(decode(description,'base64'),'UTF8') WHERE projectvariable_id=" + konga::toString(id));
        }
        etlforge::validateProjectVariableNames(*m_pTranDb, lInterchangeProjectId);
    }
    InterchangeDataTableStruct xsltAssets;
    xsltAssets.m_vColumnNames.push_back("xslttransform_id");
    xsltAssets.m_vColumnNames.push_back("xslttransform_name");
    xsltAssets.m_vColumnNames.push_back("xslt");
    for (std::map<std::string, ExtendedItemProperties*>::const_iterator xslt = itemPropertiesCollection.m_PropertiesMap.begin();
         xslt != itemPropertiesCollection.m_PropertiesMap.end(); ++xslt) {
        if (xslt->second->GetEntityTypeId() != XsltTransformEntityId) continue;
        if (xslt->second->m_properties.GetString("__deleted", "") == "1") {
            const long id = m_pGuidToIdMapper->GetId(xslt->first, XsltTransformEntityId);
            if (id != GuidToIdMapper::invalid_id) DeleteFromTable(nameXsltTransforms, std::vector<long>(1, id));
            continue;
        }
        InterchangeDataRowStruct row;
        row.m_vFields.push_back(ClientField(xslt->first.c_str()));
        row.m_vFields.push_back(ClientField(xslt->second->m_properties.GetString("xslttransform_name", "").c_str()));
        row.m_vFields.push_back(ClientField(xslt->second->m_properties.GetString("xslt", "").c_str()));
        xsltAssets.m_vRows.push_back(row);
    }
    if (!xsltAssets.m_vRows.empty())
        tableHandler.HandleTable(nameXsltTransforms, xsltAssets, &defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
tableHandler.HandleTable(nameFolders, changedData.FoldersTab,
		&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);

	if ( changedData.DocumentsTab &&
		 changedData.DocumentsTab->columnNames &&
		 changedData.DocumentsTab->columnNames->item ) {
		SpecialHandlingDeployDocumentUpdate
			deployDocumentUpdate(*(changedData.DocumentsTab->columnNames->item), *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameDocuments, changedData.DocumentsTab,
			&defaultInsertSpecialHandling, &deployDocumentUpdate);
		m_vUpdatedDocuments = deployDocumentUpdate.GetUpdatedDocumentIds();
	}

	                        
	tableHandler.HandleTable(nameDocumentDefinitions, changedData.Document_DefinitionsTab,
		&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);

	if ( changedData.MessagesTab && changedData.MessagesTab->columnNames && changedData.MessagesTab->columnNames->item ) {
		SpecialHandlingDeployMessage messagesSpecialHandling(*changedData.MessagesTab->columnNames->item, *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameMessages, changedData.MessagesTab,
			&messagesSpecialHandling, &messagesSpecialHandling);
		AddWarnings(messagesSpecialHandling);
	}

	if ( changedData.SchedulesTab &&
		 changedData.SchedulesTab->columnNames &&
		 changedData.SchedulesTab->columnNames->item ) {
		tableHandler.HandleTable(nameSchedules, changedData.SchedulesTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
		if ( tableHandler.GetNumberOfChangedItems() > 0 )
			m_bCallSchedulerUpdate = true;
	}

	                  
	                        
	InterchangeDataTableStruct jmsLocationPropertiesTab;
	if ( changedData.JmsLocationPropertiesTab )
		FillInterchangeDataTableStruct(*changedData.JmsLocationPropertiesTab, jmsLocationPropertiesTab);
	if ( changedData.SourcesTab && changedData.SourcesTab->columnNames ) {
		SpecialHandlingDeploySource sourceHandler(*(changedData.SourcesTab->columnNames->item),
			m_databaseDriverManager, jmsLocationPropertiesTab, *m_pGuidToIdMapper, *m_pTranDb);
		tableHandler.HandleTable(nameSources, changedData.SourcesTab,
			&sourceHandler, &sourceHandler);
		sourceHandler.UpdateJmsLocationProperties();
		AddWarnings(sourceHandler);
		if ( sourceHandler.JmsLocationsWereUpdated() )
			m_bCallJmsEngineUpdate = true;
	}

	if ( changedData.TargetsTab && changedData.TargetsTab->columnNames ) {
		SpecialHandlingDeployTarget targetHandler(*(changedData.TargetsTab->columnNames->item),
			m_databaseDriverManager, jmsLocationPropertiesTab, *m_pGuidToIdMapper, *m_pTranDb);
		tableHandler.HandleTable(nameTargets, changedData.TargetsTab,
			&targetHandler, &targetHandler);
		targetHandler.Update(*m_pTranDb, *m_pGuidToIdMapper);
		targetHandler.UpdateJmsLocationProperties();
		AddWarnings(targetHandler);
		if ( targetHandler.JmsLocationsWereUpdated() )
			m_bCallJmsEngineUpdate = true;
	}

	                                    
	if ( changedData.JmsMessagesTab && changedData.JmsMessagesTab->columnNames ) {
		InterchangeDataTableStruct dataElementsTab;
		if ( changedData.DataElementsTab )
			FillInterchangeDataTableStruct(*changedData.DataElementsTab, dataElementsTab);
		SpecialHandlingDeployJmsMessage jmsMsg(*m_pGuidToIdMapper, m_UserInfo,
			dataElementsTab, *m_pTranDb);
		tableHandler.HandleTable(nameJmsMessage, changedData.JmsMessagesTab,
			&jmsMsg, &jmsMsg);
	}

	          
	if ( changedData.ScriptsTab &&
		 changedData.ScriptsTab->columnNames &&
		 changedData.ScriptsTab->columnNames->item ) {
		SpecialHandlingDeployBase64Col scriptSH(2);
		tableHandler.HandleTable(nameScripts, changedData.ScriptsTab,
			&scriptSH, &scriptSH);
	}

	                                                                                  
	                              
	if ( changedData.TransformationsTab_New &&
		 changedData.TransformationsTab_New->columnNames &&
		 changedData.TransformationsTab_New->columnNames->item ) {

		                              
		                                                
		vector<string> vDocumentColNames(2);
		vDocumentColNames[0] = "source_text_doc";
		vDocumentColNames[1] = "target_text_doc";

		vColPosT vDocumentColPos = GetFieldIndexes(*(changedData.TransformationsTab_New->columnNames->item),
			vDocumentColNames);

		vector<string> vTranSourceTargetIdColumnNames(2);
		vTranSourceTargetIdColumnNames[0] = "source_sourceid";
		vTranSourceTargetIdColumnNames[1] = "target_targetid";
		vColPosT vSourceTargetIdColPos = GetFieldIndexes(*(changedData.TransformationsTab_New->columnNames->item), vTranSourceTargetIdColumnNames);
		
		SpecialHandlingTransformation tranSpecialHandling(vDocumentColPos, vSourceTargetIdColPos, *m_pGuidToIdMapper);
		FillInterchangeDataTableStruct(*(changedData.TransformationsTab_New), m_tranTabStruct);
		tableHandler.HandleTable(nameTransformationsTab_New, m_tranTabStruct,
			&tranSpecialHandling, &tranSpecialHandling);

		                                          
		tableHandler.HandleTable(nameDBFields, changedData.DBFieldsTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
		tableHandler.HandleTable(nameDBTables, changedData.DBTablesTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
		tableHandler.HandleTable(nameSqlScript, changedData.SqlScriptTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
		tableHandler.HandleTable(nameTranRelations, changedData.Tran_RelationsTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);

		tableHandler.HandleTable(nameTransFilter, changedData.Trans_FilterTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
		SpecialHandlingDeployBase64Col tranDefs(2);
		tableHandler.HandleTable(nameTransformationDefinitions, changedData.Transformation_DefinitionsTab,
			&tranDefs, &tranDefs);

		tableHandler.HandleTable(nameLdapSearchParameters, changedData.LdapFiltersTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
		tableHandler.HandleTable(nameLdapStructureEntries, changedData.LdapStructureEntriesTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
	}

	FunctionIdMapper functionIdMapper(*m_pTranDb);

	                                    
	if ( changedData.WebServicesTab && changedData.WebServicesTab->columnNames )
	{
		FunctionIdMapper functionIdMapper(*m_pTranDb);
		WebService wsSpecialHandler(*m_pTranDb, *m_pGuidToIdMapper,
			*changedData.WebServicesTab->columnNames, *m_pHighestIdFinder, functionIdMapper,
			m_UserInfo, itemPropertiesCollection);

		tableHandler.HandleTable(nameWebServices, changedData.WebServicesTab,
			&wsSpecialHandler, &wsSpecialHandler);
		AddWarnings(wsSpecialHandler);

		CallRestrictionsInsert callRestrictionInsert(functionIdMapper, *m_pGuidToIdMapper, *m_pTranDb);
		CallRestrictionsUpdate callRestrictionUpdate(functionIdMapper, *m_pGuidToIdMapper, *m_pTranDb);
		tableHandler.HandleTable(nameCallRestrictions, changedData.CallRestrictionsTab,
			&callRestrictionInsert, &callRestrictionUpdate);
	}

	              
	                                                                         
	                                                                                                     
	  
	if ( changedData.OperationsTab &&
		 changedData.OperationsTab->columnNames &&
		 changedData.OperationsTab->columnNames->item &&
		 changedData.OperationsTab->columnNames->item )
	{
		InterchangeDataTableStruct commonSourceOperations;
		if ( changedData.CommonSourceOperationsTab )
			FillInterchangeDataTableStruct(*changedData.CommonSourceOperationsTab, commonSourceOperations);

		OperationInsert operationSpecialHandlerInsert(*m_pTranDb, *m_pGuidToIdMapper, *(changedData.OperationsTab->columnNames->item), commonSourceOperations, m_oldSchedulePolicyForOperation);
		OperationUpdate operationSpecialHandlerUpdate(*m_pTranDb, *m_pGuidToIdMapper, *(changedData.OperationsTab->columnNames->item), commonSourceOperations, m_oldSchedulePolicyForOperation);
		tableHandler.HandleTable(nameOperations, changedData.OperationsTab,
			&operationSpecialHandlerInsert, &operationSpecialHandlerUpdate);
		operationSpecialHandlerInsert.Update(*m_pTranDb, getTableName(nameOperations), getIdName(nameOperations));
		operationSpecialHandlerUpdate.Update(*m_pTranDb, getTableName(nameOperations), getIdName(nameOperations));
		this->AddWarnings(operationSpecialHandlerInsert);
		this->AddWarnings(operationSpecialHandlerUpdate);

		if ( operationSpecialHandlerInsert.SchedulesWereUpdated() ||
			operationSpecialHandlerUpdate.SchedulesWereUpdated() )
			m_bCallSchedulerUpdate = true;
		if ( operationSpecialHandlerInsert.JmsSourcesWereUpdated() ||
			operationSpecialHandlerUpdate.JmsSourcesWereUpdated() )
			m_bCallJmsEngineUpdate = true;
	}

	                         
	tableHandler.HandleTable(nameOperationGraphLayout, changedData.OperationGraphLayoutTab,
		&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);

	                         
	if ( changedData.HttpEndpointsTab && changedData.HttpEndpointsTab->columnNames && changedData.HttpEndpointsTab->columnNames->item ) {
		SpecialHandlingDeployHttpEndpoint httpEndpointSH(*changedData.HttpEndpointsTab->columnNames->item, *m_pTranDb, *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameHttpEndpoints, changedData.HttpEndpointsTab,
			&httpEndpointSH, &httpEndpointSH);
		AddWarnings(httpEndpointSH);
	}

	                                           
	if ( changedData.HttpEndpointOperationsTab && changedData.HttpEndpointOperationsTab->columnNames && changedData.HttpEndpointOperationsTab->columnNames->item ) {
		tableHandler.HandleTable(nameHttpEndpointOperations, changedData.HttpEndpointOperationsTab,
			&defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
	}

	                                                                                               
   tableHandler.HandleTable(nameSalesforceConnectors, changedData.SalesforceConnectorsTab,
      &defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
                                               
	if ( changedData.SalesforceQueriesTab && changedData.SalesforceQueriesTab->columnNames && 
					changedData.SalesforceQueriesTab->columnNames->item ) {
		SpecialHandlingDeploySalesforceQuery salesforceQuerySH(*changedData.SalesforceQueriesTab->columnNames->item, *m_pTranDb, *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameSalesforceQueries, changedData.SalesforceQueriesTab,
			&salesforceQuerySH, &salesforceQuerySH);
	}

	                                            
	if ( changedData.SalesforceUpsertsTab && changedData.SalesforceUpsertsTab->columnNames && 
					changedData.SalesforceUpsertsTab->columnNames->item ) {
		SpecialHandlingDeploySalesforceUpsert salesforceUpsertSH(*changedData.SalesforceUpsertsTab->columnNames->item, *m_pTranDb, *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameSalesforceUpserts, changedData.SalesforceUpsertsTab,
			&salesforceUpsertSH, &salesforceUpsertSH);
	}

	                                            
	if ( changedData.SalesforceCreatesTab && changedData.SalesforceCreatesTab->columnNames && 
					changedData.SalesforceCreatesTab->columnNames->item ) {
		SpecialHandlingDeploySalesforceCreate salesforceCreateSH(*changedData.SalesforceCreatesTab->columnNames->item, *m_pTranDb, *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameSalesforceCreates, changedData.SalesforceCreatesTab,
			&salesforceCreateSH, &salesforceCreateSH);
	}

	                                            
	if ( changedData.SalesforceUpdatesTab && changedData.SalesforceUpdatesTab->columnNames && 
					changedData.SalesforceUpdatesTab->columnNames->item ) {
		SpecialHandlingDeploySalesforceUpdate salesforceUpdateSH(*changedData.SalesforceUpdatesTab->columnNames->item, *m_pTranDb, *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameSalesforceUpdates, changedData.SalesforceUpdatesTab,
			&salesforceUpdateSH, &salesforceUpdateSH);
	}

	                                            
	if ( changedData.SalesforceDeletesTab && changedData.SalesforceDeletesTab->columnNames && 
					changedData.SalesforceDeletesTab->columnNames->item ) {
		SpecialHandlingDeploySalesforceDelete salesforceDeleteSH(*changedData.SalesforceDeletesTab->columnNames->item, *m_pTranDb, *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameSalesforceDeletes, changedData.SalesforceDeletesTab,
			&salesforceDeleteSH, &salesforceDeleteSH);
	}

	                                              
	if ( changedData.SalesforceBulkLoadsTab && changedData.SalesforceBulkLoadsTab->columnNames && 
					changedData.SalesforceBulkLoadsTab->columnNames->item ) {
		SpecialHandlingDeploySalesforceBulkLoad salesforceBulkLoadSH(*changedData.SalesforceBulkLoadsTab->columnNames->item, *m_pTranDb, *m_pGuidToIdMapper);
		tableHandler.HandleTable(nameSalesforceBulkLoads, changedData.SalesforceBulkLoadsTab,
			&salesforceBulkLoadSH, &salesforceBulkLoadSH);
	}

	                                        
	if ( changedData.OperationPipelines ) {
		HandleOperationPipelines(changedData.OperationPipelines);
	}

	                                                                                           
	                                                                                                    
	                                                                    
	                                                                   
	SpecialHandlingDeployTranChainLinks specialHandlingTranChainLinks(*m_pTranDb, *m_pGuidToIdMapper, functionIdMapper);
	tableHandler.HandleTable(nameTransformationChainLinks, changedData.TransformationChainLinksTab,
							 &specialHandlingTranChainLinks, &specialHandlingTranChainLinks);

	                                                                                           
	SourceTargetXmlHandler wsTransformationHandler(*m_pTranDb, functionIdMapper, m_UserInfo,
		*m_pUserAccess, *m_pGuidToIdMapper);
	wsTransformationHandler.HandleTransformations(m_tranTabStruct);


	                                                                                      
	  
	assert(m_pInterchangeProject);
	FolderItem folderItemSpecialHandling(0, 1, 2, *m_pGuidToIdMapper, *m_pTranDb, *m_pInterchangeProject);
	tableHandler.HandleTable(nameFolderItem, changedData.FolderItemTab,
		&folderItemSpecialHandling, &folderItemSpecialHandling);

	                                        
	  
	const vector<RenamedItemStruct>& vRenamedItemsRef = tableHandler.GetRenamedItems();
	vector<RenamedItemStruct>::const_iterator cit = vRenamedItemsRef.begin();
	for(; cit != vRenamedItemsRef.end(); ++cit ) {
		m_vRenamedItems.push_back(*cit);
	}
	AddWarnings( tableHandler );
}
