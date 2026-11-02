/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/FileUtils.h>	             
#include <konga/Utils/Settings.h>	                    
#include <konga/Utils/util.h>		                  
#include <konga/Utils/CrException.h>

#include <konga/Crypto/Crypto.h>		                

#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/FunctionIdMapper.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/SchemaManager/SchemaManager.h>

#include <konga/ItemAccess/UserInfo.h>

#include <konga/Server/Server.h>

#include <konga/TransformationEngine/CDTDParser.h>	                             

#include <konga/UiBackend/UploadedFilesManager/FileUploadUtils.h>
#include <konga/UiBackend/UploadedFilesManager/WsdlFilesManager.h>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>	                                   
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "../ArrayOf_xsd_string.hpp"
#include "../InterchangeDataRow.hpp"

#include "ExtendedItemProperties.h"
#include "SpecialHandlingDeployWebService.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;

void WebService::init(const ArrayOf_xsd_string& columnNames) {

	assert(columnNames.item);

	                                       
	vector<string> vColumnNamesToGet(11);
	vColumnNamesToGet[0] = "webservice_id";
	vColumnNamesToGet[1] = "wsdl_id";
	vColumnNamesToGet[2] = "binding";
	vColumnNamesToGet[3] = "operation";
	vColumnNamesToGet[4] = "soapstyle";
	vColumnNamesToGet[5] = "soapbodyuse";
	vColumnNamesToGet[6] = "inputxsdfilename";
	vColumnNamesToGet[7] = "outputxsdfilename";
	vColumnNamesToGet[8] = "httpusername";
	vColumnNamesToGet[9] = "httppassword";
	vColumnNamesToGet[10] = "webservice_name";
	vColPosT vColumnPositions = GetFieldIndexes(*columnNames.item, vColumnNamesToGet);

	m_iIdIndex = vColumnPositions[0];
	m_iWsdlIdIndex = vColumnPositions[1];
	m_iBindingIndex = vColumnPositions[2];
	m_iOperationIndex = vColumnPositions[3];
	m_iSoapStyleIndex = vColumnPositions[4];
	m_iBodyUseIndex = vColumnPositions[5];
	m_iInputXsdIndex = vColumnPositions[6];
	m_iOutputXsdIndex = vColumnPositions[7];
	m_iHttpUserNameIndex = vColumnPositions[8];
	m_iHttpPasswordIndex = vColumnPositions[9];
	m_iWebServiceNameIndex = vColumnPositions[10];

	                                                          
	                                               
	m_lNextFunctionId = GetNextId(getTableName(nameFunctions), getIdName(nameFunctions));
	m_lNextTransformationId = GetNextId(getTableName(nameTransformationsTab_New),
		getIdName(nameTransformationsTab_New));
}

void WebService::ExecuteSql(const std::string& stSql) {
	UIBACKEND_TRACE("WebService::ExecuteSql - " + stSql);
	m_dbConnection.ExecuteSql(stSql);
}

bool WebService::HandleRow(InterchangeDataRowStruct& row) {
	vFieldsT& fields = row.m_vFields;

	string stWebServiceName;
	if ( row.m_vFields.at(m_iWebServiceNameIndex) != 0 )
		stWebServiceName = row.m_vFields.at(m_iWebServiceNameIndex).str();

	const NullableString& wsId = fields.at(m_iIdIndex);
	if ( wsId == 0 || wsId.str().empty() )
		throw CrException("No WebServiceId specified for the \""
		+ stWebServiceName + "\" web service call.");

	const string& wsGuid = wsId.str();

	                                                                  
	const NullableString& httpUserName = fields.at(m_iHttpUserNameIndex);
	const NullableString& pwd = fields.at(m_iHttpPasswordIndex);
	if ( httpUserName != 0 && !httpUserName.str().empty() ) {
		if ( pwd == 0 ) {
			AddWarning(WebServiceEntityId, wsId.const_ref(),
				"No password was specified for http user name \"" + httpUserName.const_ref()
				+ "\". If a call to this web service method requires a password you need to enter it" \
				" and deploy again.");
		}
	}

	                                         
	const long lWsId = m_guidToIdMapper.GetId(wsGuid, WebServiceEntityId);
	if ( lWsId != GuidToIdMapper::invalid_id ) {
		const long lWsTranId = m_functionIdMapper.GetTransformationIdForWebService(lWsId, false);
		if ( lWsTranId > 0 ) {
			const string& srcJtr = konga::GetSourceJtrFileName(lWsTranId);
			const string& tgtJtr = konga::GetTargetJtrFileName(lWsTranId);
			if ( konga::FileExists(srcJtr) ) {
				konga::kDeleteFile(srcJtr);
			}
			if ( konga::FileExists(tgtJtr) ) {
				konga::kDeleteFile(tgtJtr);
			}
		}
	}

	                                                           
	if ( pwd != 0 && !pwd.str().empty() ) {
		const string& stPassword = pwd.str();
		row.m_vFields[m_iHttpPasswordIndex] = EncryptString(stPassword);
	}

	                    
	                                                
	                   
	                                                                             
	const NullableString& wsdlLocator = row.m_vFields.at(m_iWsdlIdIndex);
	if ( wsdlLocator == 0 || wsdlLocator.const_ref().empty()) {
		throw CrException("No WSDL locator specified for the \""
		+ stWebServiceName + "\" web service call.");
	}

	                                                                      
	                                                                           
	                            
	const string& stBinding = fields.at(m_iBindingIndex).str();
	const string& stOperation = fields.at(m_iOperationIndex).str();
	const string stSoapStyle(fields.at(m_iSoapStyleIndex)==0 ? "" : fields.at(m_iSoapStyleIndex).str());
	const string stBodyUse(fields.at(m_iBodyUseIndex)==0 ? "" : fields.at(m_iBodyUseIndex).str());

	const string& stWsdlLocator = wsdlLocator.const_ref();
	const string& stUserGuid = m_guidToIdMapper.GetGuid(m_userInfo.GetId(), UserEntityId);
	if ( stUserGuid.empty() )
		throw CrException("Internal Error: Failed to find GUID for user " + m_userInfo.GetLogin());

	string stPath, stName;
	GetServerPath(stWsdlLocator, stUserGuid, stPath, stName);

	UploadedFile existingFile(stPath, stName);
	if ( !m_fileManager.ExistsFile(existingFile, existingFile, true) ) {
		throw CrException("Internal Error: Failed to find the WSDL file with locator '"
			+ stWsdlLocator + "'. Try to deploy your whole project again.");
	}

	const string& stWsdlGuid = existingFile.GetGuid();
	row.m_vFields[m_iWsdlIdIndex] = stWsdlGuid;
	const long lWsdlId = m_guidToIdMapper.GetId(stWsdlGuid, WsdlFileEntityId);
	if ( lWsdlId == GuidToIdMapper::invalid_id )
		throw CrException("Internal Error: The WSDL file with locator '"
		+ stWsdlLocator + "' does not exist on the server. Try to deploy the whole project again.");
	
	const string& stAbsoluteWsdlFilePath = existingFile.GetFullAbsolutePath();
	if ( !FileExists(stAbsoluteWsdlFilePath) )
		throw CrException("The WSDL file \"" + stAbsoluteWsdlFilePath +
		"\" for the web service \"" + stWebServiceName + "\" no longer exists on the server.");

	                                                             
	                                 
	string stOperationInput, stOperationOutput;
	ExtendedItemProperties* extendedProperties = m_itemPropertiesCollection.GetExtendedItemPropertiesForGuid(wsGuid);
	if ( extendedProperties != 0 ) {
		stOperationInput = extendedProperties->m_properties.GetString("operation_input");
		stOperationOutput = extendedProperties->m_properties.GetString("operation_output");
	}

	string stInputXsdFile;
	string stOutputXsdFile;
	const IOSchemasKey key(stAbsoluteWsdlFilePath, stBinding, stOperation, stSoapStyle, stBodyUse, stOperationInput, stOperationOutput);
	IOSchemasMapT::iterator itIOSchemas = m_IOSchemasCache.find(key);
	if ( itIOSchemas == m_IOSchemasCache.end() ) {

		                                    
		string stWsdlBaseDir(konga::GetUploadedWsdlBaseDirectoryWithSlash());
		stWsdlBaseDir += GetGUIDString();
		kCreateDirectory(stWsdlBaseDir);
		AppendPathSeparator(stWsdlBaseDir);

		                            
		stInputXsdFile = stWsdlBaseDir + "input.xsd";
		stOutputXsdFile = stWsdlBaseDir + "output.xsd";

		SchemaManager schmaManager(&m_dbConnection);
		const string stWsdlFileName(
			schmaManager.copyRequiredFilesForWsdl(lWsdlId, stWsdlBaseDir));
		
		UIBACKEND_TRACE("About to call CreateXSDFromWSDLForBothIO with the following input:");
		UIBACKEND_TRACE("WSDL file: " + stWsdlFileName);
		UIBACKEND_TRACE("Binding: " + stBinding);
		UIBACKEND_TRACE("Operation: " + stOperation);
		UIBACKEND_TRACE("Operation Input: " + stOperationInput);
		UIBACKEND_TRACE("Operation Output: " + stOperationOutput);
		UIBACKEND_TRACE("Soap Style: " + stSoapStyle);
		UIBACKEND_TRACE("Body Use: " + stBodyUse);
		UIBACKEND_TRACE("Input XSD: " + stInputXsdFile);
		UIBACKEND_TRACE("Output XSD: " + stOutputXsdFile);

		CDTDParser parser(m_userInfo);
		const unsigned long lErr = parser.CreateXSDFromWSDLForBothIO(stWsdlFileName, stBinding, stOperation,
			stOperationInput, stOperationOutput, stSoapStyle, stBodyUse,
			stInputXsdFile, stOutputXsdFile);
		if ( lErr != S_OK ) {
			throw CrException("Failed to generate XSD files for WSDL at \"" + stWsdlFileName + "\". Error: " + parser.get_LastError());
		}
		
		                   
		m_IOSchemasCache.insert( IOSchemasMapT::value_type(key, IOSchemas(stInputXsdFile, stOutputXsdFile)) );
	}
	else {
		itIOSchemas->second.GetIOSchemas(stInputXsdFile, stOutputXsdFile);
		UIBACKEND_TRACE("The same WSDL/parameter combination was found, use cached I/O Schemas:");
		UIBACKEND_TRACE("Input XSD: " + stInputXsdFile);
		UIBACKEND_TRACE("Output XSD: " + stOutputXsdFile);
	}

	                                       
	row.m_vFields[m_iInputXsdIndex] = getRelativePath(stInputXsdFile);
	row.m_vFields[m_iOutputXsdIndex] = getRelativePath(stOutputXsdFile);

	return true;
}

long WebService::GetNextFunctionId() {
	return m_lNextFunctionId++;
}

long WebService::GetNextTransformationId() {
	return m_lNextTransformationId++;
}

bool WebService::HandleRowAfterInsert(long newWebServiceId, InterchangeDataRowStruct& row) {
	                                               
	  
	const long newFunctionId = GetNextFunctionId();

	const string& stInsertFunctionSql = "INSERT INTO " + getTableName(nameFunctions)
		+ " VALUES (" + toString(newFunctionId) + ", 1, "
		+ toString(newWebServiceId) + ")" ;
	ExecuteSql(stInsertFunctionSql);

	UIBACKEND_TRACE("Mapping WebServiceId = " + toString(newWebServiceId) + " to FunctionId = " +  toString(newFunctionId)); 

	                                           
	  
	const long newTransformationId = GetNextTransformationId();

	const string& stInsertTransformationSql = "INSERT INTO " + getTableName(nameTransformationsTab_New)
		+ " (" + getIdName(nameTransformationsTab_New)
		+ ") VALUES (" + toString(newTransformationId) + ")";
	ExecuteSql(stInsertTransformationSql);

	                                              
	  
	const string& stInsertSql = "INSERT INTO TransformationFunctionTab VALUES (" + toString(newTransformationId)
		+ ", " + toString(newFunctionId) + ")";
	ExecuteSql(stInsertSql);

	m_functionIdMapper.AddWebServiceToCache(newWebServiceId, newFunctionId, newTransformationId);

	return true;
}

