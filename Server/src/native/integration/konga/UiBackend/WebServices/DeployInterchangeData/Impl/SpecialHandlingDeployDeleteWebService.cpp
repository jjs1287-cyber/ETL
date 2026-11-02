/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/Utils/StringUtils.h>	           
#include <konga/Utils/FileUtils.h>		              

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "SpecialHandlingDeployDeleteWebService.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;

SpecialHandlingDeployDeleteWebService::~SpecialHandlingDeployDeleteWebService(void)
{
}

bool SpecialHandlingDeployDeleteWebService::HandleRow(long webServiceId) {
	SpecialHandlingDeployDeleteDependents::HandleRow(webServiceId);
	const string& stWebServiceId = toString(webServiceId);

	                                        
	  
	const string& stSelectFunctionsTab(
		"SELECT " + getIdName(nameFunctions) + " FROM " + getTableName(nameFunctions)
		+ " WHERE FunctionType_ID = 1 AND ForeignKeyFunction_ID = " + stWebServiceId
	);
	UIBACKEND_TRACE("SpecialHandlingDeployDeleteWebService 1: " + stSelectFunctionsTab);
	SmartDbPtr<DynamicDbRecordSet>
		rsFunctionsTab( DynamicDbRecordSet::MakeInstance(&m_dbConnection) );
	rsFunctionsTab->SetSqlStatement(stSelectFunctionsTab);
	rsFunctionsTab->Open();
	if ( !rsFunctionsTab->Next() ) {
		throw CrException("Failed to find the Function_ID corresponding to WebService_ID = " + stWebServiceId);
	}
	const string& stFunctionId = rsFunctionsTab->GetFieldValue(0);
	rsFunctionsTab->Close();

	                            
	const string& stWhereFunctionId = " WHERE " + getIdName(nameFunctions) + "=" + stFunctionId
		+ " AND FunctionType_ID = 1";
	const string& stDeleteFunction = "DELETE FROM " + getTableName(nameFunctions)
		+ stWhereFunctionId;
	UIBACKEND_TRACE("SpecialHandlingDeployDeleteWebService 2: " + stDeleteFunction);
	m_dbConnection.ExecuteSql(stDeleteFunction);

	                                   
	const string& stDeleteCallRestriction = "DELETE FROM " + getTableName(nameCallRestrictions)
		+ stWhereFunctionId;
	UIBACKEND_TRACE("SpecialHandlingDeployDeleteWebService 3: " + stDeleteCallRestriction);
	m_dbConnection.ExecuteSql(stDeleteCallRestriction);

	                                               
	const string& stSelectTransformation = "SELECT " + getIdName(nameTransformationsTab_New)
		+ " FROM TransformationFunctionTab"
		+ " WHERE " + getIdName(nameFunctions) + " = " + stFunctionId;
	UIBACKEND_TRACE("SpecialHandlingDeployDeleteWebService 4: " + stSelectTransformation);
	SmartDbPtr<DynamicDbRecordSet>
		rsTranFuncTab( DynamicDbRecordSet::MakeInstance(&m_dbConnection) );
	rsTranFuncTab->SetSqlStatement(stSelectTransformation);
	rsTranFuncTab->Open();
	if ( !rsTranFuncTab->Next() )
		throw CrException("Failed to find Transformation_ID corresponding to Function_ID = " + stFunctionId + " and WebService_Id = " + stWebServiceId);

	const string& stTransformationId = rsTranFuncTab->GetFieldValue(0);
	rsTranFuncTab->Close();
	const string& stWhereTranId = " WHERE " + getIdName(nameTransformationsTab_New)
		+ "=" + stTransformationId;

	                                         
	const string& stDeleteTranFunc = "DELETE FROM TransformationFunctionTab" + stWhereTranId;
	UIBACKEND_TRACE("SpecialHandlingDeployDeleteWebService 5: " + stDeleteTranFunc);
	m_dbConnection.ExecuteSql(stDeleteTranFunc);

	                                 
	const string& stDeleteTransformation = "DELETE FROM " + getTableName(nameTransformationsTab_New)
		+ stWhereTranId;
	UIBACKEND_TRACE("SpecialHandlingDeployDeleteWebService 6: " + stDeleteTransformation);
	m_dbConnection.ExecuteSql(stDeleteTransformation);

	                                           
	const string& stDeleteChainLinks = "DELETE FROM " + getTableName(nameTransformationChainLinks)
		+ " WHERE " + getIdName(nameTransformationsTab_New) + " = " + stTransformationId
		+ " OR nexttransformation_id = " + stTransformationId;
	UIBACKEND_TRACE("SpecialHandlingDeployDeleteWebService 7: " + stDeleteChainLinks);
	m_dbConnection.ExecuteSql(stDeleteChainLinks);

	return true;
}

bool SpecialHandlingDeployDeleteWebService::Delete() {
	return SpecialHandlingDeployDeleteDependents::Delete();
}

