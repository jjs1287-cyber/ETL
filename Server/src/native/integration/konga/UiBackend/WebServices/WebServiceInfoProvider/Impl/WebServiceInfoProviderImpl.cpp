/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <vector>
#include <sstream>	               
#include <fstream>	           
#include <memory>	           
#include <cassert>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/util.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/DateUtils.h>

#include <konga/Xml/XmlSchemaUtils.h>

#include <konga/Zip/ZipUtils.h>		                   

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>			                                

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/ItemAccess/UserAccess.h>
#include <konga/ItemAccess/AccessSqlGenerator.h>

#include <konga/FileTransfer/FileRetriever.h>

#include <konga/SchemaManager/SchemaManager.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/GuidMappings/GuidMappings.h>
#include <konga/UiBackend/UploadedFilesManager/FileUploadUtils.h>
#include <konga/UiBackend/UploadedFilesManager/WsdlFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/UploadedFilesManager.h>

#include <konga/TransformationEngine/CDTDParser.h>

#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>	                           
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

                                                              
#include "../CWsdlFile.hpp"
#include "../CWsdlFiles.hpp"
#include "../CWsdlFileUploadResult.hpp"
#include "../CWsdlFileDownloadResult.hpp"
#include "../WebServiceJtr.hpp"
#include "../WebServiceOperationData.hpp"

#include "WebServiceInfoProviderImpl.h"


using namespace konga;
using namespace std;

namespace {
	ArrayOfCWsdlFile* convertToWsdlFileArray(const vector<string>& v) {
		if ( !v.empty() ) {
			std::auto_ptr<ArrayOfCWsdlFile> pArrayOfCWsdlFile(new ArrayOfCWsdlFile() );
			std::auto_ptr<CWsdlFile_Array> pCWsdlFile_Array(new CWsdlFile_Array() );
			const int size = v.size();
			CWsdlFile** arrCWsdlFile = allocate_pointer_array<CWsdlFile>(size);
			AutoDeletePointerArray<CWsdlFile> adopt_arrCWsdlFile(arrCWsdlFile, size);
			int i = 0;
			for( vector<string>::const_iterator cit = v.begin(); cit != v.end(); ++cit, ++i ) {
				std::auto_ptr<CWsdlFile> wsdlFile(new CWsdlFile());
				wsdlFile->setWsdlLocator(const_cast<xsd__string>(cit->c_str()), true);
				arrCWsdlFile[i] = wsdlFile.release();
			}
			pCWsdlFile_Array->set(arrCWsdlFile, size);
			pArrayOfCWsdlFile->setitem(pCWsdlFile_Array.get());                                   
			return pArrayOfCWsdlFile.release();
		}

		return 0;
	}
}

CWsdlFileUploadResult* WebServiceInfoProviderImpl::UploadWsdl(CWsdlFile* const wsdlFile,
															  CWsdlFile* const parentWsdlFile) {
	assert(wsdlFile);
	if ( !wsdlFile )
		throw CrException("Bad input data: wsdlFile is null!");

	m_stLocator = toString(wsdlFile->getWsdlLocator());
	if ( m_stLocator.empty() )
		throw CrException("Bad Data: WSDL locator is empty.");

	SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );
	pTranDbConn->SetTransactionIsolationLevel(konga::SERIALIZABLE);
	AutoRollbackTransaction adopt_transaction(pTranDbConn);

	DbGuidToIdMapper guidMapper(pTranDbConn);
	UserAccess userAccess(m_UserInfo, *pTranDbConn);

	const string& stUserGuid = GetUserGuid(m_UserInfo, guidMapper);

	                                                
	                                         
	const time_t modifiedTime = CrDateTime::GetCurrentTime().GetTime();

	string stPath, stName;
	const bool bIsUrl = GetServerPath(m_stLocator, stUserGuid, stPath, stName);
	string stTmpWsdlPath, stMD5;
	const char* pcContents = 0;
	AutoArrayDelete<char> adopt_contents(0);
	UploadedWsdlFileType wsdlType = INVALID_WSDL_FILE_TYPE;
	if ( bIsUrl ) {
		                                                                   
		m_stUrl = m_stLocator;
		const string stTmpFile( getTmpDir() + stName );
		FileRetriever::SaveUrlToFile(m_stUrl, stTmpFile);
		AutoDeleteFileSet adopt_tmpfile;
		adopt_tmpfile.AddFile(stTmpFile);
		string stError;
		if ( !XmlSchemaUtils::IsWsdlFile(stTmpFile, stError) ) {
			string stMsg("The specified URL (" + m_stUrl
				+ ") does not seem to point at a valid WSDL.");
			if ( !stError.empty() ) {
				stMsg += "\n" + stError;
			}
			throw CrException(stMsg);
		}
		wsdlType = WSDL_WSDL_FILE_TYPE;

		char* pcTmp = 0;
		stMD5 = ZipUtils::ZipAndBase64EncodeFile(stTmpFile, pcTmp);
		assert(pcTmp);
		if ( pcTmp ) {
			adopt_contents.Reset(pcTmp);
			pcContents = pcTmp;
		}
		else {
			throw CrException("Internal Error: Failed to read downloaded URL from the file " + stTmpFile
				+ ". ZipAndBase64EncodeFile returned NULL.");
		}
	}
	else {
		                                       
		m_stUrl.clear();
		assert( wsdlFile->ZippedBase64Contents && *wsdlFile->ZippedBase64Contents != 0 );
		if ( wsdlFile->ZippedBase64Contents == 0 || *wsdlFile->ZippedBase64Contents == 0 ) {
			throw CrException("Bad Data: No WSDL contents was specified for a non-URL WSDL: " + m_stLocator);
		}
		pcContents = wsdlFile->getZippedBase64Contents();

		const string stTmpFile( getTmpDir() + stName );
		AutoDeleteFileSet adopt_tmpfile;
		adopt_tmpfile.AddFile(stTmpFile);
		ZipUtils::DecodeUnzipAndWriteToFile(stTmpFile, pcContents, false);
		string stError1, stError2;
		if ( XmlSchemaUtils::IsWsdlFile(stTmpFile, stError1) ) {
			wsdlType = WSDL_WSDL_FILE_TYPE;
		}
		else if ( XmlSchemaUtils::IsSchemaFile(stTmpFile, stError2) ) {
			wsdlType = INCLUDED_SCHEMA_WSDL_FILE_TYPE;
			                                      
			if ( parentWsdlFile==0 ) {
				throw CrException("The uploaded file seems to be an XSD ("
					+ m_stLocator + ") but XSDs can only be uploaded as a dependency, not as a WSDL file.");
			}
		}
		else {
			string stMsg("The uploaded WSDL (" + m_stLocator
				+ ") does not seem to either a valid WSDL nor a valid XML Schema file.");
			if ( !stError1.empty() )
				stMsg += "\n" + stError1;
			if ( !stError2.empty() )
				stMsg += "\n" + stError2;
			throw CrException(stMsg);
		}

		assert( wsdlFile->MD5 && *wsdlFile->MD5 != 0);
		if ( wsdlFile->MD5 == 0 || *wsdlFile->MD5 == 0 ) {
			throw CrException("Bad Data: No MD5 was specified for a non-URL WSDL: " + m_stLocator);
		}
		stMD5 = wsdlFile->getMD5();
	}
	assert( wsdlType==WSDL_WSDL_FILE_TYPE || wsdlType == INCLUDED_SCHEMA_WSDL_FILE_TYPE );

	                                                                                                   

	bool bAdd = true;
	bool bExists = false;
	UploadedFilesManager fileManager(*pTranDbConn);
	ZippedUploadedFile incomingFile(stPath, stName, "", WSDL_FILE_TYPE, stMD5, modifiedTime);
	{
		UploadedFile existingFile;
		if ( fileManager.ExistsFile(incomingFile, existingFile, false) ) {
			bExists = true;
			if ( incomingFile.GetMd5() == existingFile.GetMd5() )
				bAdd = false;

			incomingFile = existingFile;
			incomingFile.SetMd5(stMD5);

			m_lWsdlId = guidMapper.GetId(incomingFile.GetGuid(), WsdlFileEntityId);
			if ( m_lWsdlId == GuidToIdMapper::invalid_id ) {
				throw CrException("Internal Error: The existing WSDL file " + existingFile.GetFullAbsolutePath()
					+ " does not have a corresponding id.");
			}
		}
		else {
			incomingFile.SetGuid( GetGUIDString() );
			incomingFile.SetFileType( WSDL_FILE_TYPE );
		}
	}

	m_stGuid = incomingFile.GetGuid();
	incomingFile.SetZippedBase64Contents(pcContents);

	m_lWsdlId = AddWsdlFile(m_lWsdlId, m_stGuid, m_stLocator, m_stUrl, wsdlType,
		incomingFile, *pTranDbConn, userAccess, fileManager, guidMapper,
		bAdd, bExists);

	if ( parentWsdlFile && parentWsdlFile->WsdlLocator ) {
		                                              
		if ( bIsUrl )                                                               
			throw CrException("Client Error: The uploaded WSDL is in a URL but a parent file was specified.");

		const string stParentLocator( toString(parentWsdlFile->getWsdlLocator()) );
		if ( stParentLocator.empty() )
			throw CrException("Client Error: Parent locator is NULL.");

		string stParentPath, stParentName;
		const bool bIsParentUrl = GetServerPath(stParentLocator, stUserGuid, stParentPath, stParentName);
		if ( bIsParentUrl )               
			throw CrException("The parent " + stParentLocator + " is a URL and cannot have dependencies (not currently supported).");

		UploadedFile parentFile(stParentPath, stParentName, "", WSDL_FILE_TYPE, "", modifiedTime);
		if ( !fileManager.ExistsFile(parentFile, parentFile, true) )                            
			throw CrException("the parent file " + stParentLocator + " does not exist on the server.");

		const string& stParentGuid = parentFile.GetGuid();
		long parentWsdlId = guidMapper.GetId(stParentGuid, WsdlFileEntityId);
		if ( parentWsdlId == GuidToIdMapper::invalid_id )
			throw CrException("Internal Error: The requested parent with locator " + stParentLocator
			+ " does not exist on this server.");

		AddWsdlDependency(parentWsdlId, m_lWsdlId, *pTranDbConn);
	}

	                                      
	vector<string> vExistingIncludes, vMissingIncludes;
	if ( !bIsUrl ) {
		try {
			const string& stAbsolutePath = incomingFile.GetFullAbsolutePath();
			if ( wsdlType == WSDL_WSDL_FILE_TYPE ) {
				XmlSchemaUtils::FindWsdlIncludesAndImports(stAbsolutePath,
					vExistingIncludes, vMissingIncludes);
			}
			else {
				XmlSchemaUtils::FindXsdIncludesAndImports(stAbsolutePath,
					vExistingIncludes, vMissingIncludes);
			}
		}
		catch( CrException& ex ) {
			ex.PrependErrorString("Failed to parse the uploaded file:\n");
			throw;
		}
	}

	                                               
	ValidateExistingAndMissingWsdlDependencies(vExistingIncludes, vMissingIncludes,
		incomingFile, guidMapper, *pTranDbConn);

	adopt_transaction.Commit();

	std::auto_ptr<CWsdlFileUploadResult> uploadResult(new CWsdlFileUploadResult());
	uploadResult->setExistingDependencies(convertToWsdlFileArray(vExistingIncludes));                           

	std::auto_ptr<ArrayOf_xsd_string> pArrayOf_xsd_string(new ArrayOf_xsd_string);
	std::auto_ptr<xsd__string_Array> pxsd__string_Array(new xsd__string_Array());
	allocate_xsd__string_Array<string>(vMissingIncludes, *pxsd__string_Array);
	pArrayOf_xsd_string->setitem(pxsd__string_Array.get());                                   
	uploadResult->setMissingWsdlFiles(pArrayOf_xsd_string.release());                          

	std::auto_ptr<CWsdlFile> uploadedWsdlFile(new CWsdlFile());
	uploadedWsdlFile->setWsdlLocator(const_cast<char*>(m_stLocator.c_str()), true);
	uploadResult->setWsdlFile(uploadedWsdlFile.release());	                                         

	auditLog(m_UserInfo.GetLogin() + " uploaded the WSDL file " + incomingFile.GetFullAbsolutePath());
	return uploadResult.release();
}

CWsdlFileDownloadResult* WebServiceInfoProviderImpl::DownloadWsdl(CWsdlFile* const wsdlFile) {
	throw CrException("WebServiceInfoProviderImpl::DownloadWsdl does not work here anymore, please call FileManagementImpl::downloadFile instead.");
}

xsd__string_Array* WebServiceInfoProviderImpl::DeleteWsdl(CWsdlFile* const wsdlFile, bool bForce) {
	throw CrException("WebServiceInfoProviderImpl::DeleteWsdl has not been implemented.");
}

WebServiceJtr* WebServiceInfoProviderImpl::GetJtrs(const WebServiceOperationData* const pOperationData) {
	if ( !pOperationData )
		throw CrException("Invalid Argument: Operation data is NULL.");

	const WebServiceOperationData& data = *pOperationData;

	m_stLocator = toString(data.WsdlLocator);
	if ( m_stLocator.empty() )
		throw CrException("Bad Data: WSDL locator is empty.");

	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

	DbGuidToIdMapper guidMapper(pTranDb);
	UserAccess userAccess(m_UserInfo, *pTranDb);

	string stPath, stName;
	const string& stUserGuid = GetUserGuid(m_UserInfo, guidMapper);
	GetServerPath(m_stLocator, stUserGuid, stPath, stName);

	UploadedFilesManager fileManager(*pTranDb);
	UploadedFile requestedFile(stPath, stName);
	{
		UploadedFile existingFile;
		if ( fileManager.ExistsFile(requestedFile, existingFile, true) ) {
			requestedFile = existingFile;
		}
		else {
			throw CrException("The requested WSDL file (" + m_stLocator
				+ ") does not exist on the system. Please try to upload it again by deploying the whole project again.");
		}
	}
	m_stGuid = requestedFile.GetGuid();

	const long wsdlId = guidMapper.GetId(m_stGuid, WsdlFileEntityId);
	if ( wsdlId == GuidToIdMapper::invalid_id ) {
		throw CrException("The requested WSDL file (" + m_stLocator
			+ ") does not exist on the system.");
	}

	                                                                          
	                                                                          
	                                   

	const string& stBindingName(toString(data.BindingName));
	const string& stOperationName(toString(data.OperationName));
	const string& stSoapStyle(toString(data.SoapStyle));
	const string& stBodyUse(toString(data.BodyUse));
	const string& stOperationInputName(toString(data.OperationInputName));
	const string& stOperationOutputName(toString(data.OperationOutputName));

	UIBACKEND_TRACE("WebServiceInfoProviderImpl::GetJtrs called with BindingName= " + stBindingName
		+ ", OperationName= " + stOperationName + ", SoapStyle= " + stSoapStyle
		+ ", BodyUse= " + stBodyUse + ", WsdlGuid = " + m_stGuid);

	string stTmpDir( getTmpDir() + GetGUIDString() );
	AutoCleanupTempDir auto_delete_tmpdir(stTmpDir);
	AppendPathSeparator(stTmpDir);
	kCreateDirectory(stTmpDir);

	const string& stTmpWsdlPath = SchemaManager(pTranDb).copyRequiredFilesForWsdl(wsdlId, stTmpDir);

	const string stJtrIn( stTmpDir + "ws" + m_stGuid + "_in.jtr");
	const string stJtrOut( stTmpDir + "ws" + m_stGuid + "_out.jtr");

	CDTDParser parser(m_UserInfo);
	UIBACKEND_TRACE("About to call CDTDParser::CreateJtrForWS(" + stTmpWsdlPath
		+ ", " + stBindingName + ", " + stOperationName + ", "
		+ stSoapStyle + ", " + stBodyUse + ", " + stJtrIn + ", " + stJtrOut + ", " + stTmpDir
		+ ")");
	if ( parser.CreateJtrForWS(stTmpWsdlPath, stBindingName, stOperationName,
		stOperationInputName, stOperationOutputName, stSoapStyle, stBodyUse, stJtrIn, stJtrOut, stTmpDir) )
	{
		throw CrException("Failed to create jtr files. " + parser.get_LastError());
	}
	UIBACKEND_TRACE("CDTDParser::CreateJtrForWS returned success");

	string stJtrIn_contents, stJtrOut_contents;
	read_file_to_string(stJtrIn, stJtrIn_contents);
	read_file_to_string(stJtrOut, stJtrOut_contents);

	auto_ptr<WebServiceJtr> retval( new WebServiceJtr() );
	retval->setInputJtr(const_cast<xsd__string>(stJtrIn_contents.c_str()), true);
	retval->setOutputJtr(const_cast<xsd__string>(stJtrOut_contents.c_str()), true);
	UIBACKEND_TRACE("About to return from WebServiceInfoProviderImpl::GetJtrs");
	return retval.release();
}

namespace {

	struct WsdlFileStruct {
		std::string	Locator;
		long Status;
		std::string	ApplicationMessage;
		std::string	UserComment;
	};

}

CWsdlFiles* WebServiceInfoProviderImpl::GetWsdlFiles() {
	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

	stringstream ssSql;
	ssSql << "SELECT * FROM " << GetGuidViewName(WsdlFileEntityId)
		<< " WHERE WsdlType = " << WSDL_WSDL_FILE_TYPE << " AND WsdlName != 'konga_usage_stats.wsdl'"
		" AND (" << GetAccessControlWhereClause(m_UserInfo, getIdName(nameWsdl), WsdlFileEntityId) << ")";

	UIBACKEND_TRACE("GetWsdlFiles SQL: " + ssSql.str());

	DbGuidToIdMapper guidMapper(pTranDb);
	UserAccess userAccess(m_UserInfo, *pTranDb);

	SmartDbPtr<DynamicDbRecordSet> selectRs(DynamicDbRecordSet::MakeInstance(pTranDb, ssSql.str()));
	selectRs->Open();

	const string stUserFolder( GetUserGuid(m_UserInfo, guidMapper) + "/" );
	UploadedFilesManager fileManager(*pTranDb);
	vector<WsdlFileStruct> vWsdlFiles;
	while ( selectRs->Next() ) {
		const string& stWsdlId = selectRs->GetFieldValue("Wsdl_Id");
		const long lWsdlId = atol(stWsdlId.c_str());

		const string& stGuid = guidMapper.GetGuid(lWsdlId, WsdlFileEntityId);
		if ( stGuid.empty() )
			continue;

		UploadedFile existingFile("", "", stGuid);
		if ( !fileManager.ExistsFile(existingFile, existingFile, true) ) {
			UIBACKEND_TRACE("The WSDL file with GUID = " + stGuid + " has not been added to the uploaded files." \
				" This is probably a file that was uploaded with a pre 1.0.0 version or the file was removed from disk.");
			continue;
		}

		        
		                                                                     
		                                            
		                                                                 
		if ( m_UserInfo.IsAdministrator() ) {
			if ( existingFile.GetPath() != stUserFolder )
				continue;
		}

		WsdlFileStruct wsdl;
		string stLocator;
		const string& stUrl = selectRs->GetFieldValue("WsdlUrl");
		if ( !stUrl.empty() && stUrl != "(none)" ) {
			stLocator = stUrl;
		}
		else {
			stLocator = selectRs->GetFieldValue("WsdlName");
		}

		                                                      
		wsdl.ApplicationMessage = selectRs->GetFieldValue("ApplicationMessage");
		wsdl.Locator = stLocator;
		wsdl.Status = atol( selectRs->GetFieldValue("Status").c_str() );
		wsdl.UserComment = selectRs->GetFieldValue("UserComment");
		vWsdlFiles.push_back( wsdl );
	}

	const vector<WsdlFileStruct>::size_type N = vWsdlFiles.size();
	
	CWsdlFile** arrWsdlFiles = allocate_pointer_array<CWsdlFile>(N);
	AutoDeletePointerArray<CWsdlFile> adopt_arrWsdlFiles(arrWsdlFiles, N);
	for( vector<WsdlFileStruct>::size_type i = 0; i < N; ++i ) {
		auto_ptr<CWsdlFile> pWsdlFile( new CWsdlFile() );

		const WsdlFileStruct& wsdlStruct = vWsdlFiles.at(i);
		pWsdlFile->setWsdlLocator( const_cast<xsd__string>(wsdlStruct.Locator.c_str()) );
		pWsdlFile->Status = wsdlStruct.Status;
		pWsdlFile->setApplicationMessage(const_cast<xsd__string>(wsdlStruct.ApplicationMessage.c_str()), true);
		pWsdlFile->setUserComment(const_cast<xsd__string>(wsdlStruct.UserComment.c_str()), true);

		arrWsdlFiles[i] = pWsdlFile.release();
	}
	auto_ptr<CWsdlFiles> pWsdlFiles( new CWsdlFiles() );
	pWsdlFiles->WsdlFiles = new ArrayOfCWsdlFile();
	pWsdlFiles->WsdlFiles->item->set(arrWsdlFiles, N);

	return pWsdlFiles.release();
}
