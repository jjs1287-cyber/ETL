/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>	                
#include <vector>

#include <boost/scoped_array.hpp>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>

#include <konga/ItemAccess/UserAccess.h>
#include <konga/ItemAccess/AccessSqlGenerator.h>

#include <konga/Utils/PortDefs.h>	                                             

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>			                          

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/UiBackend/UploadedFilesManager/UploadedFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/XmlFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/UploadedFileTypes.h>
#include <konga/UiBackend/UploadedFilesManager/FileUploadUtils.h>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>	                                                        
#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include <konga/Xml/XmlSchemaUtils.h>

#include <konga/TransformationEngine/CDTDParser.h>

#include "../CXmlStructureFileUploadResult.hpp"

#include "XmlInfoProviderImpl.h"

using namespace konga;
using std::string;
using std::vector;
using std::stringstream;
using std::ofstream;
using std::auto_ptr;


XmlInfoProviderImpl::~XmlInfoProviderImpl(void) {
}

                     
namespace {
	const string stXmlSelect("SELECT x.xml_id, x.xml_filename, x.xml_path FROM xmldtdtab x WHERE ");
	const string stMainId("x.xml_id");

	void FillXmlStructureFileBase(const UploadedFile& uploadedFile, CXmlStructureFile& structureFile) {
		structureFile.setMD5( const_cast<char*>(uploadedFile.GetMd5().c_str()) );
		structureFile.setModifiedTime(GetClientTimeFromServerTime(uploadedFile.GetModifiedDateAsLong()));
		structureFile.setSchemaType(uploadedFile.GetFileType());
	}

	void FillXmlStructureFileWithServerPath(const UploadedFile& uploadedFile, CXmlStructureFile& structureFile) {
		const string stServerPath( uploadedFile.GetCanonicalPath() + uploadedFile.GetName() );
		structureFile.setPath( const_cast<char*>(stServerPath.c_str()) );
		FillXmlStructureFileBase(uploadedFile, structureFile);
	}

	void FillXmlStructureFileWithClientPath(const UploadedFile& uploadedFile, CXmlStructureFile& structureFile) {
		const string& stPath = uploadedFile.GetCanonicalPath();
		const string& stName = uploadedFile.GetName();
		const string& stClientPath = GetClientPath(stPath, stName);
		structureFile.setPath( const_cast<char*>(stClientPath.c_str()) );
		FillXmlStructureFileBase(uploadedFile, structureFile);
	}

	void FillZippedXmlStructureFileWithClientPath(const ZippedUploadedFile& uploadedFile, CXmlStructureFile& structureFile) {
		FillXmlStructureFileWithClientPath(uploadedFile, structureFile);
		structureFile.setZippedBase64Contents( const_cast<char*>(uploadedFile.GetZippedBase64Contents()) );
	}

	string GetUserGuid(const UserInfo& user, const GuidToIdMapper& guidMapper) {
		const string& stGuid = guidMapper.GetGuid(user.GetId(), UserEntityId);
		if ( stGuid.empty() )
			throw CrException("Internal Error: Failed to find the GUID for user \""
				+ user.GetLogin() + "\".");
		return stGuid;
	}
}

CXmlStructureFile_Array* XmlInfoProviderImpl::GetXmlStructureFiles() {
	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

	UserAccess userAccess(m_UserInfo, *pTranDb);
	DbGuidToIdMapper guidMapper(pTranDb);

	string stSelectSql( stXmlSelect + "(" + GetAccessControlWhereClause(m_UserInfo, stMainId, GetEntityIdByName(nameXMLDtd)) + ")");

	UIBACKEND_TRACE("getCXmlStructureFiles Sql - " + stSelectSql);
	SmartDbPtr<DynamicDbRecordSet> pSelectRs( DynamicDbRecordSet::MakeInstance(pTranDb, stSelectSql) );
	pSelectRs->Open();

	const string stUserFolder( GetUserGuid(m_UserInfo, guidMapper) + "/" );
	UploadedFilesManager fileManager(*pTranDb);
	vector<UploadedFile> files;
	while ( pSelectRs->Next() ) {
		const string& value = pSelectRs->GetFieldValue(0);
		const long lXmlId = atol(value.c_str());

		const string& stGuid = guidMapper.GetGuid(lXmlId, XmlEntityId);
		if ( stGuid.empty() )
			continue;

		UploadedFile existingFile("", "", stGuid);
		if ( !fileManager.ExistsFile(existingFile, existingFile, true) ) {
			UIBACKEND_TRACE("The Xml Schema/dtd file with GUID = " + stGuid + " has not been added to the uploaded files." \
				" This is probably a file that was uploaded with a pre 1.0.0 version.");
			continue;
		}

		        
		                                                                     
		                                            
		                                                                 
		if ( m_UserInfo.IsAdministrator() ) {
			if ( existingFile.GetPath() != stUserFolder )
				continue;
		}
		files.push_back( existingFile );
	}

	                          
	auto_ptr<CXmlStructureFile_Array> retVal( new CXmlStructureFile_Array() );
	const int nSize = files.size();
	if ( nSize < 1 )
		return retVal.release();

	CXmlStructureFile** arrStructureFile = allocate_pointer_array<CXmlStructureFile>(nSize);
	AutoDeletePointerArray<CXmlStructureFile> adopt_arrStructureFile(arrStructureFile, nSize);
	for( int i = 0; i < nSize; ++i ) {
		const UploadedFile& file = files.at(i);
		auto_ptr<CXmlStructureFile> pStructureFile( new CXmlStructureFile() );
		FillXmlStructureFileWithServerPath(file, *pStructureFile);

		arrStructureFile[i] = pStructureFile.release();
	}

	retVal->set(arrStructureFile, nSize);
	return retVal.release();
}

namespace {
	const string stXmlLinksTab("xmldtdlinks");

	                                            
	void GetExistingDependencies(const long lXmlId, vector<UploadedFile>& dependencies,
		const GuidToIdMapper& guidMapper, const UserAccess& userAccess,
		const UploadedFilesManager& fileManager, DbConnection& dbConnection) {

		dependencies.clear();
		const string stSelectLinks("SELECT child_dtd FROM " + stXmlLinksTab
			+ " WHERE parent_dtd = " + toString(lXmlId));

		SmartDbPtr<DynamicDbRecordSet> pRecordSet( DynamicDbRecordSet::MakeInstance(&dbConnection, stSelectLinks) );
		pRecordSet->Open();
		while ( pRecordSet->Next() ) {
			const long lChildXmlId = atoi(pRecordSet->GetFieldValue(0).c_str());
			const string& stChildGuid = guidMapper.GetGuid(lChildXmlId, XmlEntityId);
			if ( stChildGuid.empty() )
				continue;	                                                                
                                                                                                               

			if ( !userAccess.HasReadPermission(lChildXmlId, XmlEntityId) )
				continue;

			UploadedFile child("", "", stChildGuid), existingChild;
			if ( !fileManager.ExistsFile(child, existingChild, true) ) {
				continue;	                                                                
                                                                                      
                                                                                                         
			}

			dependencies.push_back( existingChild );
		}
	}
}

CXmlStructureFileDownloadResult* XmlInfoProviderImpl::GetXmlStructureFile(CXmlStructureFile* const pXmlStructureFile) {
	throw CrException("XmlInfoProviderImpl::GetXmlStructureFile does not work here anymore...");
	return 0;
}

namespace {
	void CheckMissingIncludePaths(const vector<string>& vMissingIncludes) {

		string stCurrent;
		try {
			vector<string>::const_iterator cit = vMissingIncludes.begin();
			for(; cit != vMissingIncludes.end(); ++cit ) {
				stCurrent = *cit;
				VerifyPathForUploadedFile(stCurrent);
			}
		}
		catch ( const CrException& ex ) {
			throw CrException("The uploaded file includes a file which uses an invalid path. "
				+ ex.GetErrorString());
		}
	}

	const string stSelectMaxSql("SELECT MAX(xmldtdid) AS max1, MAX(xml_id) AS max2 FROM XMLDtdTab");
}

CXmlStructureFileUploadResult* XmlInfoProviderImpl::UploadStructureFile(CXmlStructureFile* const pStructureFile,
																		CXmlStructureFile* const pParentStructureFile)
{
	if ( !pStructureFile )
		throw CrException("Bad Data: Structure file is NULL.");

	CXmlStructureFile& structureFile = *pStructureFile;
	
	if ( !structureFile.ZippedBase64Contents )
		throw CrException("Bad Data: contents is NULL.");
	const char* const pchZippedBase64Contents = structureFile.ZippedBase64Contents;

	const string& stMD5 = toString(structureFile.MD5);
	if ( stMD5.size() != 32 )
		throw CrException("Bad Data: The specified hash ('" + stMD5 + "') is invalid.");

	const time_t modifiedDate  = GetServerTimeFromClientTime(structureFile.getModifiedTime());

	                                         
	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	AutoRollbackTransaction adopt_transaction(pTranDb);
	pTranDb->SetTransactionIsolationLevel(konga::SERIALIZABLE);

	DbGuidToIdMapper guidMapper(pTranDb);
	UserAccess userAccess(m_UserInfo, *pTranDb);

	const string& stUserGuid = GetUserGuid(m_UserInfo, guidMapper);
	const string& stClientPath = toString(structureFile.getPath());
	string stPath, stName;
	GetServerPath(stClientPath, stUserGuid, stPath, stName);

	XmlSchemaUtils::XmlSchemaType type = XmlSchemaUtils::GetSchemaType(stName);
	UploadedFileType fileType = (type == XmlSchemaUtils::XML_DTD) ? XML_DTD_FILE_TYPE : XML_SCHEMA_FILE_TYPE;

	UploadedFilesManager fileManager(*pTranDb);

	bool bAdd = true;
	bool bExists = false;
	ZippedUploadedFile incomingFile(stPath, stName, "", fileType, stMD5, modifiedDate);
	{
		UploadedFile existingFile;
		if ( fileManager.ExistsFile(incomingFile, existingFile, false) ) {
			bExists = true;
			incomingFile.SetGuid( existingFile.GetGuid() );

			                                 
			if ( incomingFile.GetMd5() == existingFile.GetMd5() ) {
				bAdd = false;
			}
		}
		else {
			                                     
			incomingFile.SetGuid(GetGUIDString());
		}
	}
	incomingFile.SetZippedBase64Contents(pchZippedBase64Contents);

	const long lXmlId = AddXmlFile(incomingFile, bExists, bAdd, fileManager, guidMapper, userAccess, *pTranDb);

	                                         
	const string& stAbsolutePath = incomingFile.GetFullAbsolutePath();

	                             
	   
	vector<string> vExistingIncludes, vMissingIncludes;
	if ( type == XmlSchemaUtils::XML_DTD ) {
		                  
		CDTDParser parser(m_UserInfo);
		if ( parser.ReadDtdDefinition(stAbsolutePath, vExistingIncludes, vMissingIncludes) != 0 ) {
			throw CrException("Failed to parse the uploaded DTD: " + parser.get_LastError());
		}
	}
	else {
		try {
			XmlSchemaUtils::FindXsdIncludesAndImports(stAbsolutePath,
				vExistingIncludes, vMissingIncludes);
		}
		catch( CrException& ex ) {
			ex.PrependErrorString("Failed to parse the uploaded Schema: ");
			throw;
		}
	}

	                                                  
	try {
		CheckMissingIncludePaths(vMissingIncludes);
		CheckMissingIncludePaths(vExistingIncludes);
	}
	catch( const CrException& ex ) {
		throw CrException("The uploaded file named \"" + konga::GetLeafPath(stAbsolutePath)
			+ "\" has a problem: " + ex.GetErrorString());
	}

	                                                                         
	                                 
	  
	for( vector<string>::size_type index = 0; index < vExistingIncludes.size();                              ) {
		const string& stExisting = vExistingIncludes.at(index);

		                                             
		UploadedFile existingInclude(incomingFile.GetCanonicalPath(), stExisting);
		if ( !fileManager.ExistsFile(existingInclude, existingInclude, true) ) {
			                                                                                   
			                                                                                                            
			                                   
			vMissingIncludes.push_back(stExisting);
			vExistingIncludes.erase(vExistingIncludes.begin()+index);

			                                                                        
			continue;
		}

		const string& stParentGuid = incomingFile.GetGuid();
		const long lParentId = guidMapper.GetId(stParentGuid, XmlEntityId);
		if ( lParentId == GuidToIdMapper::invalid_id )
			throw CrException("Internal Error: XML Schema \"" + incomingFile.GetFullAbsolutePath()
			+ "\" does not have any id (GUID = \"" + stParentGuid + "\". Plese report this bug.");

		const string& stChildGuid = existingInclude.GetGuid();
		const long lChildId = guidMapper.GetId(stChildGuid, XmlEntityId);
		if ( lChildId == GuidToIdMapper::invalid_id )
			throw CrException("Internal Error: XML Schema \"" + existingInclude.GetFullAbsolutePath()
			+ "\" does not have any id (GUID = \"" + stChildGuid + "\". Plese report this bug.");

		AddXmlDependency(lParentId, lChildId, *pTranDb);
		++index;
	}

	                                                                                             
	if ( pParentStructureFile ) {
		CXmlStructureFile& parentStructureFile = *pParentStructureFile;
		string stParentClientPath( toString(parentStructureFile.getPath()) );
		string stParentPath, stParentName;
		GetServerPath(stParentClientPath, stUserGuid, stParentPath, stParentName);
		UploadedFile existingParent;
		{
			UploadedFile parent(stParentPath, stParentName);
			if ( !fileManager.ExistsFile(parent, existingParent, true) ) {
				throw CrException("Bad Data (or Internal Error): The specified parent does not exist on the server" \
					" (" + stParentPath + ").");
			}
		}
		const string& stParentGuid = existingParent.GetGuid();

		const long lParentXmlId = guidMapper.GetId(stParentGuid, XmlEntityId);
		if ( lParentXmlId == GuidToIdMapper::invalid_id ) {
			throw CrException("Internal Error: No XmlId could be found for existing parent schema/dtd file with GUID = "
				+ stParentGuid);
		}

		if ( !userAccess.HasWritePermission(lParentXmlId, XmlEntityId) ) {
			throw CrException("You do not have permission to set the dependency between these files:\n" \
				"Parent: " + existingParent.GetCanonicalPath() + existingParent.GetName() + "\n" +
				"Child: " + incomingFile.GetCanonicalPath() + existingParent.GetName());
		}

		AddXmlDependency(lParentXmlId, lXmlId, *pTranDb);
	}

	                                                                
	vector<UploadedFile> existingDependencies;
	GetExistingDependencies(lXmlId, existingDependencies, guidMapper, userAccess, fileManager, *pTranDb);

	                          
	auto_ptr<CXmlStructureFileUploadResult> pRetVal( new CXmlStructureFileUploadResult() );

	auto_ptr<CXmlStructureFile> pRetFile( new CXmlStructureFile() );
	FillXmlStructureFileWithClientPath(incomingFile, *pRetFile);

	pRetVal->setXmlStructureFile(pRetFile.release());

	                
	pRetVal->MissingStructureFiles = new ArrayOf_xsd_string();
	allocate_xsd__string_Array(vMissingIncludes, *(pRetVal->MissingStructureFiles->item));

	                
	pRetVal->setExistingDependencies( new ArrayOfCXmlStructureFile() );
	const int nDeps = existingDependencies.size();
	if ( nDeps > 0 ) {
		CXmlStructureFile** arrDependencies = allocate_pointer_array<CXmlStructureFile>(nDeps);
		AutoDeletePointerArray<CXmlStructureFile> adopt_arrDependencies(arrDependencies, nDeps);
		for( int i = 0; i < nDeps; ++i ) {
			auto_ptr<CXmlStructureFile> pFile( new CXmlStructureFile() );
			FillXmlStructureFileWithClientPath(existingDependencies.at(i), *pFile);
			arrDependencies[i] = pFile.release();
		}
		pRetVal->ExistingDependencies->item->set(arrDependencies, nDeps);
	}

	                                     
	adopt_transaction.Commit();

	auditLog(m_UserInfo.GetLogin() + " uploaded the file XML Schema/DTD " + string(structureFile.getPath()));
	return pRetVal.release();
}

xsd__string_Array* XmlInfoProviderImpl::GetPossibleRootNames(CXmlStructureFile* const pCXmlStructureFile) {
	if ( pCXmlStructureFile == 0 )
		throw CrException("Invalid NULL value as input.");

	CXmlStructureFile& structureFile = *pCXmlStructureFile;

	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

	DbGuidToIdMapper guidMapper(pTranDb);

	const string& stUserGuid = GetUserGuid(m_UserInfo, guidMapper);
	const string& stClientPath = toString( structureFile.getPath() );
	string stPath, stName;
	GetServerPath(stClientPath, stUserGuid, stPath, stName);

	UploadedFilesManager fileManager(*pTranDb);
	UploadedFile existingFile;
	{
		UploadedFile incomingFile(stPath, stName);
		                                                                     
		if ( !fileManager.ExistsFile(incomingFile, existingFile, false) ) {
			throw CrException("Internal Error: The specified structure file does not exist on the server (" + stClientPath + ").");
		}
	}

	long lXmlId = guidMapper.GetId(existingFile.GetGuid(), XmlEntityId);
	if ( lXmlId == GuidToIdMapper::invalid_id )
		throw CrException("Internal Error: Could not find GUID mapping for requested structure file (" + stClientPath + ").");

	UserAccess userAccess(m_UserInfo, *pTranDb);
	if ( !userAccess.HasReadPermission(lXmlId, XmlEntityId) ) {
		throw CrException("The current user does not have read permission on the specified file.");
	}


	const string& stAbsolutePath = existingFile.GetFullAbsolutePath();
	if ( !FileExists(stAbsolutePath) )
		throw CrException("The specified XML structure file does not exist on the server. Try to upload it again.");

	CDTDParser parser(m_UserInfo);
	vector<string> mostLikely, lessLikely;
	if ( parser.GetRootSelectionList(stAbsolutePath, mostLikely, lessLikely) != 0 ) {
		throw CrException("An error occurred while searching for possible root names: " 
			+ parser.get_LastError());
	}

	                              
	vector<string> vRootNames;
	for( vector<string>::const_iterator it = mostLikely.begin(); it != mostLikely.end(); ++it ) {
		vRootNames.push_back(*it);
	}
	for( vector<string>::const_iterator it = lessLikely.begin(); it != lessLikely.end(); ++it ) {
		vRootNames.push_back(*it);
	}

	return allocate_xsd__string_Array(vRootNames);
}
