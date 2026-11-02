/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/util.h>	                  
#include <konga/Utils/DateUtils.h>

#include <konga/Zip/ZipUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/TranDb/WebServicesTab.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserInfo.h>
#include <konga/ItemAccess/UserAccess.h>

#include <konga/SchemaManager/SchemaManager.h>

#include <konga/TransformationEngine/XsdFromWsdlCreator.h>
#include <konga/TransformationEngine/CDTDParser.h>

#include <konga/UiBackend/UploadedFilesManager/UploadedFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/FileUploadUtils.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "TreeMapperInfoProviderImpl.h"

using namespace konga;
using namespace TranDb;
using std::string;

namespace {

	string GetUserGuid(const UserInfo& user, const GuidToIdMapper& guidMapper) {
		const string& stGuid = guidMapper.GetGuid(user.GetId(), UserEntityId);
		if ( stGuid.empty() )
			throw CrException("Internal Error: Failed to find the GUID for user \""
				+ user.GetLogin() + "\".");
		return stGuid;
	}

	string getHostedXsdFileName(const string& stUnhostedName) {
		return konga::RemoveFileExtension(stUnhostedName) + XsdFromWsdlCreator::HOST_POSTFIX + ".xsd";
	}

	unsigned long getWebServiceInfo(DbConnection& tranDbConn, long ws_id,std::string&stWSName,std::string&stWSDLFileName,std::string&stInputXSDFileName,std::string&stOutputXSDFileName,
									std::string&stBinding_,std::string&stOpName_,std::string&stOpStyle_,std::string&stSoapBodyUse_,std::string&stErr){
		try
		{
			SmartDbPtr<WebServicesTab> tab( WebServicesTab::MakeInstance(&tranDbConn) );
			std::string stTablesTabFilter = "WHERE (WebService_ID = " + konga::toString(ws_id) + ")";
			tab->Select(stTablesTabFilter);
			if( tab->Next() ){
				if(!tab->IsWebService_NameNull())stWSName=tab->GetWebService_Name();
				if(!tab->IsWebService_UrlNull())stWSDLFileName=tab->GetWebService_Url();
				if(!tab->IsBindingNull())stBinding_=tab->GetBinding();
				if(!tab->IsOperationNull())stOpName_=tab->GetOperation();
				if(!tab->IsSoapStyleNull())stOpStyle_=tab->GetSoapStyle();
				if(!tab->IsSoapBodyUseNull())stSoapBodyUse_=tab->GetSoapBodyUse();
				if(!tab->IsInputXSDFileNameNull())stInputXSDFileName=tab->GetInputXSDFileName();
				if(!tab->IsOutputXSDFileNameNull())stOutputXSDFileName=tab->GetOutputXSDFileName();
			} else {
				throw CrException("Cannot find web service with id " + konga::toString(ws_id));
			}
		}
		catch( const CrException& e )
		{
			stErr = std::string("getWebServiceInfo() failed, ")+e.GetErrorString();
			return E_FAIL;
		}
		return S_OK;
	}

}

xsd__string TreeMapperInfoProviderImpl::getJtrForStructure(const string& stUploadedFilePath, const string& stRootName, bool bIsTarget) {

	SmartDbPtr<DbConnection> pTranDB( konga::GetBackEndDbConnection("TranDb") );
	DbGuidToIdMapper guidMapper(pTranDB);

	const string& stUserGuid = GetUserGuid(m_UserInfo, guidMapper);
	string stPath, stName;
	GetServerPath(stUploadedFilePath, stUserGuid, stPath, stName);

	UploadedFilesManager filesMgr(*pTranDB); 
	UploadedFile requestedFile(stPath, stName);
	if( !filesMgr.ExistsFile(requestedFile, requestedFile, true) )
		throw CrException("Failed to create structure tree. Failed to find structure file '" + stUploadedFilePath +
		"'. Try to deploy your entire project again to this server. If that does not work, please report this problem " \
		"on http://www.etlforge.invalid/forums.");
	const string& stSchemaFileName = requestedFile.GetFullAbsolutePath();

	const string stTitle(stUploadedFilePath);

	                      
	const string ext=(stSchemaFileName.length()>3)?stSchemaFileName.substr(stSchemaFileName.length()-3):"";

	string tempFileName;
	GetGUIDString(tempFileName);
	const string stJtrFileName( getTmpDir() + tempFileName );	                         

	string stClick;
	if(ext=="dtd"||ext=="xsd")stClick="Xml";
	if(bIsTarget)
		stClick+="Target";
	else
		stClick+="Source";

	CDTDParser dtdParser(m_UserInfo, "CDTDParser_getJtrForStructure");
	AutoDeleteFileSet auto_delete_tmp_file(false);
	auto_delete_tmp_file.AddFile(stJtrFileName);
	unsigned long success=dtdParser.CreateBrowserTree(stSchemaFileName, stRootName, stJtrFileName, stTitle, stClick);
	if ( success != 0 ) {
		throw CrException( dtdParser.get_LastError() + "\n\n(getJtrForStructure failed when calling dtdParser.CreateBrowserTree)");
	}

	                             
	try {
		char* pchZippedBase64EncodedString = 0;
		ZipUtils::ZipAndBase64EncodeFile(stJtrFileName, pchZippedBase64EncodedString, false);
		assert(pchZippedBase64EncodedString);
		return pchZippedBase64EncodedString;
	}
	catch( const exception& ex ) {
		throw CrException("getJtrForStructure() failed to read from \"" + stJtrFileName + "\". " + ex.what());
	}
}

namespace {
	string toStringCheckNull(xsd__string s) {
		if ( s == 0 ) return string();
		return toString(s);
	}
}

xsd__string TreeMapperInfoProviderImpl::getJtrForWS(WebServiceMethodData* const wsMethodData)
{
	assert(wsMethodData);
	if ( !wsMethodData )
		throw CrException("Client Error: WebServiceMethodData is null!");

	const string stOpInput( toStringCheckNull(wsMethodData->getOpInput()) );                                                                                                  
	const string stOpOutput( toStringCheckNull(wsMethodData->getOpOutput()) );                                                                                                              

	const string stWebServiceGuid( toString(wsMethodData->getWsGuid()) );
	const string stWsdlLocator( toString(wsMethodData->getWsdlGuid()) );
	const string stBinding( toString(wsMethodData->getBinding()) );
	const string stOpName( toString(wsMethodData->getOpName()) );
	const string stOpStyle( toString(wsMethodData->getOpStyle()) );
	const string stSoapBodyUse( toString(wsMethodData->getSoapBodyUse()) );

	const bool bIsTarget( wsMethodData->getIsTarget() == true_ );
	const bool bIsWSRequest( wsMethodData->getIsWSRequest() == true_ );

	UIBACKEND_TRACE("getJtrForWS called with " + stWebServiceGuid + ", " + stWsdlLocator + ", " + stBinding + ", " + stOpName
			 + ", " + stOpStyle + ", " + stSoapBodyUse + ", " + string(bIsTarget?"true":"false")
			 + ", " + string(bIsWSRequest?"true":"false") );

	CDTDParser dtdParser(m_UserInfo, "CDTDParser_getJtrForWS");
	CViewLog* viewLog=dtdParser.getViewLog();
	CErrorLog errorLog;
	CrDateTime SysTime;
	errorLog.SetViewLog(viewLog);
	errorLog.Initialize();

	                                                                                     
	AutoCallTerminate<CViewLog> adopt_viewLog(viewLog);
	AutoCallTerminate<CErrorLog> adopt_errLog(&errorLog);

	unsigned long success = 0;
	const bool isHosted = bIsTarget ? !bIsWSRequest : bIsWSRequest;

	                                                   
	SmartDbPtr<DbConnection> pTranDB( konga::GetBackEndDbConnection("TranDb") );
	DbGuidToIdMapper guidMapper(pTranDB);

	const string& stUserGuid = GetUserGuid(m_UserInfo, guidMapper);
	string stPath, stName;
	GetServerPath(stWsdlLocator, stUserGuid, stPath, stName);

	UploadedFilesManager fileManager(*pTranDB);
	UploadedFile requestedFile(stPath, stName);
	if ( !fileManager.ExistsFile(requestedFile, requestedFile, true) ) {
		throw CrException("Failed to find the WSDL file with locator '" + stWsdlLocator +
			"'. Try to deploy your whole project again.");
	}
	const string& stWsdlGuid = requestedFile.GetGuid();

	string stInputXSDFileName,stOutputXSDFileName,stBinding_,stOpName_, stOpStyle_, stSoapBodyUse_;

	const long ws_id = guidMapper.GetId(stWebServiceGuid, WebServiceEntityId);
	const bool wsDeployed = ( ws_id != GuidToIdMapper::invalid_id );
	if ( wsDeployed ) {
		if ( !konga::UserHasReadPermission(m_UserInfo.GetId(), ws_id, WebServiceEntityId) ) {
			throw CrException("The current user does not have read access for this web service call (WSDL locator: " + stWsdlLocator + ").");
		}
		viewLog->DebugString("TreeMapperInfoProviderImpl::getJtrForWS() has been deployed, ws_id=%ld\n", ws_id);
	}
	else {
		viewLog->DebugString("TreeMapperInfoProviderImpl::getJtrForWS() has not yet deployed\n");
	}

	                                                   
	                                  
	const string stXSDDir( getTmpDir() + GetGUIDString() );
    viewLog->DebugString("creating directory \"%s\"\n",stXSDDir.c_str());
	try {
		kCreateDirectory(stXSDDir);
	}
	catch( const CrException& ex ) {
		string stErr("Failed to create temporary directory \"" + stXSDDir + "\". Reason: %s\n");
		viewLog->DebugString(stErr.c_str(), ex.GetErrorString().c_str());
		throw CrException("Failed to create temporary directory \"" + stXSDDir + "\". Reason: "
			+ ex.GetErrorString());
	}
	AutoCleanupTempDir auto_delete_dir(stXSDDir,true);
	string stAbsolutePath, stRootName="root",
		stJtrFileName=stXSDDir+"/tmp.jtr",
		stClick=bIsTarget?"Xmltarget":"Xmlsource";

	string stWSName;
	if(wsDeployed){
		string stErr;
		viewLog->DebugString("Entering getWebServiceInfo(ws_id=%ld)\n", ws_id);

		string stWSDLFileName;
		success=getWebServiceInfo(*pTranDB, ws_id,stWSName,stWSDLFileName,stInputXSDFileName,stOutputXSDFileName,stBinding_,stOpName_, stOpStyle_, stSoapBodyUse_, stErr);
		if (isHosted) {
			stInputXSDFileName = getHostedXsdFileName(stInputXSDFileName);
			stOutputXSDFileName = getHostedXsdFileName(stOutputXSDFileName);
		}
		if(success==0)viewLog->DebugString("\tsuccess\n");
		else viewLog->DebugString("\terror=%s\n",stErr.c_str());

		if( success != 0 ){
			throw CrException("getJtrForWS(...) failed, " + stErr);
		}
		if(stBinding!=stBinding_||
			stOpName!=stOpName_||
			stOpStyle!=stOpStyle_||
			stSoapBodyUse!=stSoapBodyUse_){
				throw CrException("getJtrForWS(...) failed, some inputs are inconsistent with the data in the TranDb");
		}
		stAbsolutePath=getAbsolutePath((bIsWSRequest?stInputXSDFileName:stOutputXSDFileName),true);
	} else {
		stInputXSDFileName=stXSDDir+"/inputSchema.xsd";
		stOutputXSDFileName=stXSDDir+"/outputSchema.xsd";
		stAbsolutePath = bIsWSRequest ? stInputXSDFileName : stOutputXSDFileName;
		if (isHosted) {
			stAbsolutePath = getHostedXsdFileName(stAbsolutePath);
		}

		const long wsdl_id = guidMapper.GetId(stWsdlGuid, WsdlFileEntityId);
		if ( wsdl_id == GuidToIdMapper::invalid_id )
			throw CrException("Can't find wsdl_id for WSDL Locator '"+stWsdlLocator+", wsdl file was not uploaded to current server.");

		SchemaManager schemaManager(pTranDB);
		const string stWSDLFileName(
			schemaManager.copyRequiredFilesForWsdl(wsdl_id, stXSDDir));
		viewLog->DebugString("Entering dtdParser.CreateXSDFromWSDLForBothIO(wsdl=\"%s\",\n\tbinding=\"%s\",\n\toperation=\"%s\",\n\tstyle=\"%s\",\n\tuse=\"%s\",\n\tinputXSD=\"%s\",\n\toutputXSD=\"%s\")\n",
											stWSDLFileName.c_str(),stBinding.c_str(), stOpName.c_str(), stOpStyle.c_str(), stSoapBodyUse.c_str(), stInputXSDFileName.c_str(), stOutputXSDFileName.c_str());
		success=dtdParser.CreateXSDFromWSDLForBothIO(stWSDLFileName, stBinding, stOpName, stOpInput, stOpOutput, stOpStyle, stSoapBodyUse, stInputXSDFileName, stOutputXSDFileName);
		if(success==0)viewLog->DebugString("\tsuccess\n");
		else viewLog->DebugString("\terror=%s\n",dtdParser.get_LastError().c_str());

		if( success != 0 ){
			throw CrException("getJtrForWS() failed,\n" + dtdParser.get_LastError());
		}
	}


	viewLog->DebugString("Entering dtdParser.CreateBrowserTree(path=\"%s\",\n\troot=\"%s\",\n\tjtr=\"%s\",\n\tws_name=\"%s\",\n\tclick=\"%s\")\n",
									 stAbsolutePath.c_str(), stRootName.c_str(), stJtrFileName.c_str(), stWSName.c_str(), stClick.c_str());

	success=dtdParser.CreateBrowserTree(stAbsolutePath, stRootName, stJtrFileName, stWSName, stClick);
	if(success==0)viewLog->DebugString("\tsuccess\n");
	else viewLog->DebugString("\terror=%s\n",dtdParser.get_LastError().c_str());

	if( success != 0 ){
		throw CrException("getJtrForWS() failed,\n" + dtdParser.get_LastError());
	}

	                                 
	try {
		char* pchZippedBase64Encoded = 0;
		ZipUtils::ZipAndBase64EncodeFile(stJtrFileName, pchZippedBase64Encoded, false);
		assert(pchZippedBase64Encoded);
		return pchZippedBase64Encoded;
	}
	catch( const exception& ex ) {
		throw CrException("getWSDLJtrs() failed to read from \"" + stJtrFileName + "\". " + ex.what());
	}
}

xsd__string_Array* TreeMapperInfoProviderImpl::getWSDLJtrs(const char* const wsdl, const char* const binding,
														   const char* const operationName,
														   const char* const operationStyle,
														   const char* const soapBodyUse)
{
	throw InternalErrorException("TreeMapperInfoProviderImpl::getWSDLJtrs has not been implemented! (and probably never will...)");
}

