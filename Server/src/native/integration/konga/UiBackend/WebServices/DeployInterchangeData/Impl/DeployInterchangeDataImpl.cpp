/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <memory>		                  
#include <string>
#include <vector>

#include <konga/Utils/DBConnectionInfo.h>			                         
#include <konga/Utils/AutoFileCleanup.h>			                    
#include <konga/Utils/Settings.h>					                 

#include <konga/InterchangeProject/InterchangeProject.h>

#include <konga/Db/DbUtils/AutoDbCleanup.h>			                                
#include <konga/Db/DbUtils/DbUtils.h>				                  

#include <konga/Server/lmcheck.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/GuidMappings/CachedGuidToIdMapper.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "ExtendedItemProperties.h"
#include "TableHandler.h"
#include "PermissionsHandler.h"
#include "PipelinePluginHandler.h"

#include "InterchangeDataStructs.h"
#include "FolderItemStruct.h"

#include "DeployInterchangeDataImpl.h"
#include "AuditDeployment.h"

using namespace konga;
using namespace konga::interchange_deploy;

using std::string;
using std::vector;
using std::deque;
using std::auto_ptr;

DeployInterchangeDataImpl::DeployInterchangeDataImpl(const char* user, const char* password)
: WsImplBaseAuthenticator(user, password),
  m_pUserAccess(0), m_pGuidToIdMapper(0), m_pDbGuidToIdMapper(0),
  m_pTranDbRelations(DbRelations::GetTranDbRelations()),
  m_pAutoDeleteFileSet( new AutoDeleteFileSet(false, false) ), m_pTranDb(0),
  m_pHighestIdFinder(0), m_pTypeInfoLoader(0), m_pInterchangeProject(0),
  m_bCallSchedulerUpdate(false), m_bCallJmsEngineUpdate(false)
{
}

DeployInterchangeDataImpl::~DeployInterchangeDataImpl() {
	                                   
	  
	if ( m_pTypeInfoLoader ) delete m_pTypeInfoLoader;
	if ( m_pUserAccess ) delete m_pUserAccess;
	if ( m_pGuidToIdMapper ) delete m_pGuidToIdMapper;
	if ( m_pDbGuidToIdMapper ) delete m_pDbGuidToIdMapper;
	if ( m_pTranDbRelations ) delete m_pTranDbRelations;
	if ( m_pAutoDeleteFileSet ) {
		try {
			delete m_pAutoDeleteFileSet;
		}
		catch( const CrException& ex ) {
			                                                                          
			                                 
			UIBACKEND_TRACE("Failed to delete file: " + ex.GetErrorString());
		}
	}
	if ( m_pInterchangeProject ) delete m_pInterchangeProject;
	if ( m_pHighestIdFinder ) delete m_pHighestIdFinder;
}

void DeployInterchangeDataImpl::init() {
	m_pTypeInfoLoader = new TypeInfoLoader(DBConnectionInfo("TranDb").connectionString());

	m_pTranDb.Attach( konga::GetBackEndDbConnection("TranDb") );
	m_pHighestIdFinder = new NextIdFinder(*m_pTranDb);

	m_pUserAccess = UserAccess::GetUserAccessWithGroup(m_UserInfo.GetId(), m_pTranDb);

	m_pDbGuidToIdMapper = new DbGuidToIdMapper(m_pTranDb);
	m_pGuidToIdMapper = new CachedGuidToIdMapper(*m_pDbGuidToIdMapper);
}

InterchangeProject* DeployInterchangeDataImpl::GetInterchangeProject(const string& stGuid, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper)
{
		int lId = guidToIdMapper.GetId(stGuid, InterchangeProjectEntityId);
		if ( lId == GuidToIdMapper::invalid_id )                                 
			lId = -1;
		return new InterchangeProject(lId, "", &dbConnection);
}

namespace {
	CIntegrationEntity* AllocateIntegrationEntity(const int iEntityTypeId, const std::string& stGuid) {
		auto_ptr<CIntegrationEntity> pIE(new CIntegrationEntity());
		pIE->EntityTypeId = iEntityTypeId;
		pIE->setId(const_cast<xsd__string>(stGuid.c_str()), true);

		return pIE.release();
	}
}

InterchangeProject* DeployInterchangeDataImpl::GetInterchangeProject(const InterchangeDataHolder* const pChangedData,
	DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper)
{
	if ( pChangedData == 0 )
		return 0;
	if ( pChangedData->InterchangeProjectsTab == 0 )
		return 0;

	InterchangeDataTableStruct interchangeProjectsTab;
	FillInterchangeDataTableStruct(*pChangedData->InterchangeProjectsTab, interchangeProjectsTab);

	if ( interchangeProjectsTab.m_vColumnNames.size() < 2 )
		throw CrException("Bad Data: Size of InterchangeProjectsTab column names vector is less than 2.");
	if ( interchangeProjectsTab.m_vRows.size() != 1 )
		throw CrException("Bad Data: Only one InterchangeProject can be deployed at a time.");

	const string& stGuid = interchangeProjectsTab.m_vRows.at(0).m_vFields.at(0).str().c_str();
	string stName;
	if ( interchangeProjectsTab.m_vRows.at(0).m_vFields.at(1) != 0 )
		stName = interchangeProjectsTab.m_vRows.at(0).m_vFields.at(1).str();
	else
		interchangeProjectsTab.m_vRows.at(0).m_vFields.at(1) = "";
	InterchangeProject* p = GetInterchangeProject(stGuid, dbConnection, guidToIdMapper);
	if ( !stName.empty() ) p->SetName(stName);
	return p;
}

CDeployResults* DeployInterchangeDataImpl::AllocateDeployResults(int iStatus, const ItemWarningHolder& itemWarningHolder) const {
	vector<RenamedItemStruct> vRenamedItems;
	return AllocateDeployResults(iStatus, itemWarningHolder, vRenamedItems);
}

CDeployResults* DeployInterchangeDataImpl::AllocateDeployResults(int iStatus, const ItemWarningHolder& itemWarningHolder,
	const vector<RenamedItemStruct>& vRenamedItems) const {
	auto_ptr<CDeployResults> pDeployResults( new CDeployResults() );

	const std::vector<ItemWarning>& vWarnings = itemWarningHolder.GetWarningsRef();

	{
	const int nWarnings = vWarnings.size();
	CDeployWarning** arrCDeployWarning = allocate_pointer_array<CDeployWarning>(nWarnings);
	AutoDeletePointerArray<CDeployWarning> adopt_arrCDeployWarning(arrCDeployWarning, nWarnings);

	for( int i = 0; i < nWarnings; ++i ) {
		const ItemWarning& warning = vWarnings.at(i);
		if ( warning.GetEntityTypeId() < 1 ) {
			UIBACKEND_TRACE(warning.GetWarning());
		}
		else {
			UIBACKEND_TRACE("Warning for " + GetEntityNameByEntityId(warning.GetEntityTypeId())
				+ " with GUID = " + warning.GetGuid() + " : " + warning.GetWarning())
		}
		auto_ptr<CDeployWarning> pCDeployWarning( new CDeployWarning() );
		pCDeployWarning->Entity = AllocateIntegrationEntity(warning.GetEntityTypeId(), warning.GetGuid());
		pCDeployWarning->setWarning(const_cast<xsd__string>(warning.GetWarning().c_str()), true);
		arrCDeployWarning[i] = pCDeployWarning.release();
	}
	pDeployResults->Warnings = new ArrayOfCDeployWarning();
	pDeployResults->Warnings->item->set(arrCDeployWarning, nWarnings);
	}

	{
	const int nRenamed = vRenamedItems.size();
	CDeployRename** arrCDeployRename = allocate_pointer_array<CDeployRename>(nRenamed);
	AutoDeletePointerArray<CDeployRename> adopt_arrCDeployRename(arrCDeployRename, nRenamed);
	for( int i = 0; i < nRenamed; ++i ) {
		const RenamedItemStruct& renamedItem = vRenamedItems.at(i);
		auto_ptr<CDeployRename> pDeployRename( new CDeployRename() );
		pDeployRename->Entity = AllocateIntegrationEntity(renamedItem.m_iEntityTypeId, renamedItem.m_stGuid);
		pDeployRename->setOldName(const_cast<xsd__string>(renamedItem.m_stOldName.c_str()), true);
		pDeployRename->setNewName(const_cast<xsd__string>(renamedItem.m_stNewName.c_str()), true);
		arrCDeployRename[i] = pDeployRename.release();
	}
	pDeployResults->RenamedItems = new ArrayOfCDeployRename();
	pDeployResults->RenamedItems->item->set(arrCDeployRename, nRenamed);
	}

	pDeployResults->setDeployStatus(iStatus);

	return pDeployResults.release();
}

CDeployResults* DeployInterchangeDataImpl::DeployInterchangeData(const InterchangeIdHolder* const pDeletedData,
																 const InterchangeDataHolder* const pChangedData)
{
	init();
	m_pTranDb->SetTransactionIsolationLevel(konga::READ_COMMITTED);
	m_pTranDb->SetAutoCommit(false);
	AutoRollbackTransaction adopt_transaction(m_pTranDb);

	m_pInterchangeProject = GetInterchangeProject(pChangedData, *m_pTranDb, *m_pGuidToIdMapper);
	if ( m_pInterchangeProject == 0 )
		throw InternalErrorException("No interchange project was specified. " \
		"Client and server versions may be incompatible, please make sure that the client and the server have compatible version numbers.");

	                                                          
	if ( m_pInterchangeProject->GetProjectId() > 0 ) {
		                               
		if ( !m_pUserAccess->HasWritePermission(m_pInterchangeProject->GetProjectId(), InterchangeProjectEntityId) ) {
			throw CrException("The current user does not have write permission for the \""
				+ m_pInterchangeProject->GetName() + "\" project.");
		}
	}

	m_bCallSchedulerUpdate = false;
	m_bCallJmsEngineUpdate = false;

    InterchangeDataTableStruct auditProject;
    FillInterchangeDataTableStruct(*pChangedData->InterchangeProjectsTab,auditProject);
    const std::string auditProjectGuid=auditProject.m_vRows.at(0).m_vFields.at(0).str();
    etlforge::audit::Database auditDatabase(m_pTranDb->GetConnectionHandle());
    etlforge::audit::DeploymentCapture auditCapture(auditDatabase,auditProjectGuid);

	ExtendedItemPropertiesCollection itemPropertiesCollection;
	if ( pChangedData && pChangedData->EntityProperties ) {
		FillExtendedItemProperties(itemPropertiesCollection, pChangedData->EntityProperties);
	}

	HandleExtendedItemPropertiesPreDeploy(itemPropertiesCollection);

	                                                                                  
	                                                
	                                                                               
	if( m_pInterchangeProject->GetProjectId() > 0 && pDeletedData )
		HandleDeletedData(pDeletedData);

	if( pChangedData ) {
		HandleChangedData(pChangedData, itemPropertiesCollection);
		if ( pChangedData->Permissions ) {
			PermissionsHandler permissionsHandler(*m_pTranDb, *m_pUserAccess, *m_pGuidToIdMapper);
			permissionsHandler.HandlePermissions( *(pChangedData->Permissions) );
			AddWarnings(permissionsHandler);
		}

		InsertExtendedItemProperties(itemPropertiesCollection);

		if ( pChangedData->PipelinePluginEntityAssociations ) {
			InterchangeDataTableStruct pipelinePluginAssociations;
			FillInterchangeDataTableStruct(*pChangedData->PipelinePluginEntityAssociations,
				pipelinePluginAssociations);
			PipelinePluginHandler ppHandler(*m_pTranDb, *m_pGuidToIdMapper);
			ppHandler.HandlePipelinePlugins(pipelinePluginAssociations);
			AddWarnings(ppHandler);
		}
	}

    auditCapture.complete(m_UserInfo.GetId(),m_UserInfo.GetLogin(),m_pInterchangeProject->GetName());
	adopt_transaction.Commit();
	UIBACKEND_TRACE("Committed to the database");

	                            
	m_pAutoDeleteFileSet->SetAutoDelete(true);

	                                                           
	                                                                             

	                                        
	const long lInterchangeProjectId = m_pInterchangeProject->GetProjectId();
	if ( m_pUserAccess->HasDeletePermission(lInterchangeProjectId, InterchangeProjectEntityId) ) {
		if ( m_pInterchangeProject->DeleteProjectIfEmpty() ) {
			m_pGuidToIdMapper->DeleteItem(lInterchangeProjectId, InterchangeProjectEntityId);
			AccessControl::DeleteAccessControlForItem(InterchangeProjectEntityId, lInterchangeProjectId, *m_pTranDb);
		}
	}

	if ( m_bCallSchedulerUpdate )
		UpdateScheduler();

	if ( m_bCallJmsEngineUpdate )
		UpdateJmsEngine();

	Check();

	UIBACKEND_TRACE("Returning from DeployInterchangeData");
	const int iStatus = GetWarningsRef().empty() ? 0 : 1;
	return AllocateDeployResults(iStatus, *this, m_vRenamedItems);
}

void DeployInterchangeDataImpl::Check() {
	try {
		lm* plm = konga::get_lm(m_pTranDb);
		if ( plm != 0 ) {
			std::auto_ptr<lm> adopt_plm(plm);
			string warning;
			plm->check(warning, konga::GetOptionAsInt("Settings", "LicenseWarningDays", 10));
			if ( !warning.empty() ) {
				AddWarning(0, "", warning);
			}
		}
	}
	catch( const CrException& ex ) {
		string msg(ex.GetErrorString());
		msg += " If the license has expired you can still deploy your project but operation will not run.";
		AddWarning(0, "", msg);
	}
}

#ifndef JB_PRIVATE
CDeployProjectCheckResult* DeployInterchangeDataImpl::CheckProjectForDeploy(const char* projectGUID,
																			const CIntegrationEntity_Array* const entities)
{
	return 0;
}

CDeployResults* DeployInterchangeDataImpl::DeleteInterchangeProjects(xsd__string_Array * projectIds) {
	throw CrException("Deleting a project from the server is not available in this version, it requires at least an Enterprise license.\nYou can delete a project by deleting all the items in the project and deploying the empty project.");
}
#endif

