/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <memory>		                
#include <algorithm>	            
#include <exception>

#include <konga/Utils/CrException.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Utils/PortDefs.h>	                                                                
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/DbRelations/DbRelations.h>

#include <konga/InterchangeProject/InterchangeProject.h>

#include <konga/ItemProperties/ItemProperties.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/GuidMappings/CachedGuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "../InterchangeDataHolder.hpp"
#include "../CInterchangeProjects.hpp"

#include "OperationTreeCheckoutSpecialHandling.h"
#include "OperationTreeCheckoutSqlGeneratorForOperation.h"
#include "OperationTreeCheckoutSpecialHandlingForOperation.h"
#include "OperationTreeCheckoutSqlGeneratorForFolder.h"
#include "OperationTreeCheckoutSqlGeneratorForInterchangeProject.h"
#include "OperationTreeCheckoutSpecialHandlingForInterchangeProject.h"
#include "OperationTreeCheckoutSqlGeneratorForSchedule.h"

#include "FolderSpecialHandling.h"

#include "CheckoutInterchangeDataImpl.h"

using namespace checkout_interchange_data;
using namespace konga;
using namespace konga::interchange_checkout;
using std::string;
using std::vector;

CheckoutInterchangeDataImpl::CheckoutInterchangeDataImpl(const char* user, const char* password)
: WsImplBaseAuthenticator(user, password), m_pDbConnection(0), m_pTranDbRelations(0), m_pGuidToIdMapper(0), m_pInterchangeProject(0)
{
	try {
		m_pTranDbRelations = DbRelations::GetTranDbRelations();

		m_pDbConnection = konga::GetBackEndDbConnection("TranDb");

		m_pNonCachedGuidToIdMapper = new DbGuidToIdMapper(m_pDbConnection);
		m_pGuidToIdMapper = new CachedGuidToIdMapper(*m_pNonCachedGuidToIdMapper);
	} catch(...) {
		                                                                    
		try {
			CleanUp();
		} catch(...) {
			                                                         
		}
		throw;
	}
}

CheckoutInterchangeDataImpl::~CheckoutInterchangeDataImpl(void) {
	try {
		CleanUp();
	}
	catch( const std::exception& ex) {
		string sErr("Unexpected std::exception thrown in ~CheckoutInterchangeDataImpl: " + string(ex.what()));
		UIBACKEND_TRACE(sErr);
		throw CrException(sErr);
	}
}

void CheckoutInterchangeDataImpl::CleanUp() {
	                                         
	  
	if ( m_pTranDbRelations != 0 ) {
		delete m_pTranDbRelations;
		m_pTranDbRelations = 0;
	}

	if ( m_pGuidToIdMapper != 0 ) {
		delete m_pGuidToIdMapper;
		m_pGuidToIdMapper = 0;
	}

	if ( m_pNonCachedGuidToIdMapper != 0 ) {
		delete m_pNonCachedGuidToIdMapper;
		m_pNonCachedGuidToIdMapper = 0;
	}

	if ( m_pInterchangeProject != 0 ) {
		delete m_pInterchangeProject;
		m_pInterchangeProject = 0;
	}

	SmartDbPtr<DbConnection> adopt_connection(m_pDbConnection);
}

InterchangeDataHolder* CheckoutInterchangeDataImpl::CheckoutInterchangeData(const char* const projectId) {
	throw InternalErrorException("This method is deprecated. Call downloadIntegrationProject instead.");
}

InterchangeDataHolder* CheckoutInterchangeDataImpl::GetProjectTree() {

	std::auto_ptr<InterchangeDataHolder> pInterchangeDataHolder(new InterchangeDataHolder());

	            
	{
	OperationTreeCheckoutSqlGeneratorForSchedule sqlGeneratorSchedule(m_UserInfo);
	DefaultCheckoutSpecialHandling shSchedule(*m_pGuidToIdMapper, getIdName(nameSchedules));
	pInterchangeDataHolder->SchedulesTab = GetInterchangeDataTable(nameSchedules, sqlGeneratorSchedule, shSchedule);
	}

	             
	{
	OperationTreeCheckoutSqlGeneratorForOperation sqlGeneratorOperation(m_UserInfo);
	OperationTreeCheckoutSpecialHandlingForOperation specialHandlingOperation(*m_pGuidToIdMapper);
	pInterchangeDataHolder->OperationsTab = GetInterchangeDataTable(nameOperations, sqlGeneratorOperation, specialHandlingOperation);
	}

	           
	{
	OperationTreeCheckoutSqlGeneratorForInterchangeProject sqlGenInterchangeProject(m_UserInfo);
	OperationTreeCheckoutSpecialHandlingForInterchangeProject specialHandlingInterchangeProject;
	pInterchangeDataHolder->InterchangeProjectsTab = GetInterchangeDataTable(nameInterchangeProjects,
		sqlGenInterchangeProject, specialHandlingInterchangeProject);
	}

	                                   
	OperationTreeCheckoutSqlGeneratorForFolder folderSqlGenerator(m_UserInfo);
	FolderSpecialHandling folderSpecialHandling(*m_pGuidToIdMapper);
	folderSqlGenerator.SetIsItem(false);
	folderSpecialHandling.SetIsItem(false);
	pInterchangeDataHolder->FoldersTab = GetInterchangeDataTable(nameFolders, folderSqlGenerator, folderSpecialHandling);

	folderSqlGenerator.SetIsItem(true);
	folderSpecialHandling.SetIsItem(true);
	pInterchangeDataHolder->FolderItemTab = GetInterchangeDataTable(nameFolderItem, folderSqlGenerator, folderSpecialHandling);

	return pInterchangeDataHolder.release();
}

namespace {
	struct InterchangeProjectStruct {
		std::string m_stId;
		std::string m_stName;
		std::string m_stDescription;
		std::string m_stOwnerUserId;
		std::string m_stOwnerUserName;
	};

	CInterchangeProjects* AllocateCInterchangeProjects(const vector<InterchangeProjectStruct>& projects) {
		const int nProjects = projects.size();
		CInterchangeProject** arrCInterchangeProject =
			allocate_pointer_array<CInterchangeProject>(nProjects);
		AutoDeletePointerArray<CInterchangeProject> adopt_arrCInterchangeProject(arrCInterchangeProject, nProjects);

		for(int i = 0; i < nProjects; ++i ) {
			const InterchangeProjectStruct& project = projects.at(i);
			auto_ptr<CInterchangeProject> pCInterchangeProject(new CInterchangeProject() );

			pCInterchangeProject->setDescription(const_cast<xsd__string>(project.m_stDescription.c_str()), true);
			pCInterchangeProject->setId(const_cast<xsd__string>(project.m_stId.c_str()), true);
			pCInterchangeProject->setName(const_cast<xsd__string>(project.m_stName.c_str()), true);
			pCInterchangeProject->setOwnerUserId(const_cast<xsd__string>(project.m_stOwnerUserId.c_str()), true);
			pCInterchangeProject->setOwnerUserName(const_cast<xsd__string>(project.m_stOwnerUserName.c_str()), true);
			arrCInterchangeProject[i] = pCInterchangeProject.release();
		}

		                           
		std::auto_ptr<CInterchangeProjects> pProjects( new CInterchangeProjects() );
		pProjects->InterchangeProjects = new ArrayOfCInterchangeProject();
		pProjects->InterchangeProjects->item->set(arrCInterchangeProject, nProjects);
		return pProjects.release();
	}
}
namespace {
	const string stDescrField("description");
	const string stUserIdField("user_id");
	const string stNameField("login");
}

CInterchangeProjects* CheckoutInterchangeDataImpl::GetInterchangeProjects() {
	throw InternalErrorException("Method has been deprecated. Call getOperationTree instead.");
}

namespace {
	typedef map<long, KeyValues> exPropsT;
	vector<string> GetUniqueKeys(const exPropsT& properties) {
		vector<string> keys;
		for( exPropsT::const_iterator cit = properties.begin(); cit != properties.end(); ++cit ) {
			const KeyValues& keyValues = cit->second;

			KeyValues::const_iterator keyIt = keyValues.begin();
			for( ; keyIt != keyValues.end(); ++keyIt ) {
				const string& key = keyIt->first;
				if ( std::find(keys.begin(), keys.end(), key) == keys.end() ) {
					keys.push_back(key);
				}
			}
		}
		return keys;
	}
}

InterchangeDataTable* CheckoutInterchangeDataImpl::GetInterchangeDataTable(const string& stName,
		SelectSqlGenerator& sqlGenerator,
		vector<CheckoutSpecialHandling*>& vSpecialHandling)
{
	const string& stSql = sqlGenerator.GetSql();
	m_pDbConnection->ExecuteSql("SET DATESTYLE TO ISO");
	SmartDbPtr<DynamicDbRecordSet> rsSelect( DynamicDbRecordSet::MakeInstance(m_pDbConnection, stSql) );
	UIBACKEND_TRACE("GetInterchangeDataTable SQL for " + stName + ": " + stSql);
	rsSelect->Open();
	if ( !rsSelect->Next() )
		return 0;	                                                      

	exPropsT exProperties;
	const int iEntityTypeId = GetEntityIdByName(stName);
	if ( ItemProperties::hasItemProperties(iEntityTypeId) ) {
		ItemProperties itemProps(m_pDbConnection);
		itemProps.GetItemPropertiesForEntityType(iEntityTypeId, exProperties);
	}

	vector<string> exKeys;
	if ( !exProperties.empty() ) {
		exKeys = GetUniqueKeys(exProperties);
	}
	vector<string> vColumnNames;
	int nColumns = rsSelect->GetFieldCount();

	for( int iCol = 0; iCol < nColumns; ++iCol ) {
		const string& stColumnName = rsSelect->GetFieldInfo(iCol).Name;
		if ( strcasecmp(stColumnName.c_str(), "InsertTime") == 0 ||
			 strcasecmp(stColumnName.c_str(), "UserSubjID") == 0 ) {
			nColumns = iCol;
			break;
		}
		vColumnNames.push_back(stColumnName);
	}

	                           
	for( vector<string>::const_iterator keyIt = exKeys.begin(); keyIt != exKeys.end(); ++keyIt ) {
		vColumnNames.push_back(*keyIt);
	}

	for( vshT::iterator it = vSpecialHandling.begin(); it != vSpecialHandling.end(); ++it )
	{
		if ( *it != 0 ) (*it)->SetColumnNames(&vColumnNames);
	}

	typedef vector<vColumnValuesT> vRowsT;
	vRowsT vRows;

	                                                                                                                     
	do {
		vColumnValuesT vColumnValues;
		for( int iCol = 0; iCol < nColumns; ++iCol ) {
			if ( rsSelect->IsFieldNull(iCol) )
				vColumnValues.push_back(NullableString(0));
			else
				vColumnValues.push_back(rsSelect->GetFieldValue(iCol));
		}

		                                            
		for( vector<string>::const_iterator keyIt = exKeys.begin(); keyIt != exKeys.end(); ++keyIt ) {
			vColumnValues.push_back( NullableString(0) );
		}

		                                 
		if ( !exKeys.empty() ) {
			const NullableString& id = CheckoutSpecialHandling::GetColumnValueRef(getIdName(stName), vColumnNames, vColumnValues);
			const long lItemId = atol(id.str().c_str());
			exPropsT::const_iterator itExProps = exProperties.find(lItemId);
			if ( itExProps != exProperties.end() ) {
				const KeyValues& exKeyValues = itExProps->second;
				for( vector<string>::const_iterator keyIt = exKeys.begin(); keyIt != exKeys.end(); ++keyIt ) {
					if ( exKeyValues.IsKeyDefined(*keyIt) ) {
						NullableString& val = CheckoutSpecialHandling::GetColumnValueRef(*keyIt, vColumnNames, vColumnValues);
						val = exKeyValues.GetString(*keyIt);
					}
				}
			}			
		}
		
		for( vshT::iterator it = vSpecialHandling.begin(); it != vSpecialHandling.end(); ++it )
		{
			if ( *it != 0 ) (*it)->HandleColumn(vColumnValues);
		}

		vRows.push_back(vColumnValues);
	} while( rsSelect->Next() );

	                     
	  
	std::auto_ptr<InterchangeDataTable> pInterchangeDataTable( new InterchangeDataTable() );
	pInterchangeDataTable->columnNames = new ArrayOf_xsd_string();
	allocate_xsd__string_Array(vColumnNames, *pInterchangeDataTable->columnNames->item);

	const int nRows = vRows.size();
	                          
	InterchangeDataRow** arrInterchangeDataRow = allocate_pointer_array<InterchangeDataRow>(nRows);
	AutoDeletePointerArray<InterchangeDataRow> adopt_arrInterchangeDataRow(arrInterchangeDataRow, nRows);
	for( int iRow = 0; iRow < nRows; ++iRow ) {
		const vColumnValuesT& vColumnValues = vRows.at(iRow);
		auto_ptr<ArrayOf_xsd_string> pArrayOf_xsd_string( new ArrayOf_xsd_string() );
		
		allocate_xsd__string_Array(vColumnValues, *pArrayOf_xsd_string->item);
		auto_ptr<InterchangeDataRow> pInterchangeDataRow( new InterchangeDataRow() );
		pInterchangeDataRow->fields = pArrayOf_xsd_string.release();

		arrInterchangeDataRow[iRow] = pInterchangeDataRow.release();
	}
	pInterchangeDataTable->rows = new ArrayOfInterchangeDataRow();
	pInterchangeDataTable->rows->item->set(arrInterchangeDataRow, nRows);

	return pInterchangeDataTable.release();
}

#ifndef JB_PRIVATE
DownloadDataHolder* CheckoutInterchangeDataImpl::DownloadIntegrationProject(const char* const projectId) {
	throw CrException("This method is not implemented in the community version.");
}

RequiredFiles* CheckoutInterchangeDataImpl::DownloadRequiredFiles(int iEntityTypeId, const char* const entityGuid) {
	throw CrException("This method is not implemented in the community version.");
}

#endif
