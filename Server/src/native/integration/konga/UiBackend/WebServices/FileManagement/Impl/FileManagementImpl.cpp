/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>
#include <memory>		           

#include <boost/scoped_array.hpp>

#include <konga/Utils/StringUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Utils/PortDefs.h>	                                             
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/UiBackend/UploadedFilesManager/XmlFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/WsdlFilesManager.h>

#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>	                           

#include <konga/UiBackend/UploadedFilesManager/UploadedFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/FileUploadUtils.h>
#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include "../CFileDownloadResult.hpp"

#include "FileManagementImpl.h"

using namespace konga;
using std::string;
using std::auto_ptr;

namespace {
	void FillDownloadedFile(const UploadedFile& uploadedFile, CDownloadedFile& downloadedFile) {
		downloadedFile.setGuid( const_cast<char*>(uploadedFile.GetGuid().c_str()) );
		downloadedFile.setMD5( const_cast<char*>(uploadedFile.GetMd5().c_str()) );
		downloadedFile.setModifiedTime( uploadedFile.GetModifiedDateAsLong() );

		const string& stClientPath = GetClientPath(uploadedFile.GetCanonicalPath(), uploadedFile.GetName());

		downloadedFile.setPath(const_cast<char*>(stClientPath.c_str()));
	}

	void FillDownloadedFileWithContents(const ZippedUploadedFile& uploadedFile, CDownloadedFile& downloadedFile) {
		FillDownloadedFile(uploadedFile, downloadedFile);
		downloadedFile.setZippedBase64EncodedContents(const_cast<char*>(uploadedFile.GetZippedBase64Contents()));
	}

	string GetUserGuid(const UserInfo& user, const GuidToIdMapper& guidMapper) {
		const string& stGuid = guidMapper.GetGuid(user.GetId(), UserEntityId);
		if ( stGuid.empty() )
			throw CrException("Internal Error: Failed to find the GUID for user \""
				+ user.GetLogin() + "\".");
		return stGuid;
	}
}

namespace {

	const string stXmlLinksTab("xmldtdlinks");

}


CFileDownloadResult* FileManagementImpl::downloadFile(const CDownloadedFile* const pRequestedFile) {
	if ( !pRequestedFile )
		throw CrException("Bad Data: File to download is NULL.");
	const CDownloadedFile& requestedFile = *pRequestedFile;

	SmartDbPtr<DbConnection> pConnection( konga::GetBackEndDbConnection("TranDb") );

	UserAccess userAccess(m_UserInfo, *pConnection);
	DbGuidToIdMapper guidMapper(pConnection);
	
	UploadedFilesManager fileManager(*pConnection);

	const string& stUserGuid = GetUserGuid(m_UserInfo, guidMapper);
	string stGuid( toString(requestedFile.Guid) );
	const string& stClientPath = toString(requestedFile.Path);
	string stPath, stName;
	GetServerPath(stClientPath, stUserGuid, stPath, stName);

	ZippedUploadedFile existingFile;
	{
		UploadedFile requestedFile(stPath, stName, stGuid);
		                                                                                                   
		if ( !fileManager.ExistsFile(requestedFile, existingFile, false) ) {
			throw CrException("The requested file does not exist on this server.");
		}
	}
	stGuid = existingFile.GetGuid();

	int iEntityTypeId = -1;
	switch( existingFile.GetFileType() ) {
		case XML_DTD_FILE_TYPE:
		case XML_SCHEMA_FILE_TYPE:
			iEntityTypeId = XmlEntityId;
			break;
		case WSDL_FILE_TYPE:
			iEntityTypeId = WsdlFileEntityId;
			break;
		default:
			throw CrException("Internal Error: Unknown file type: " + toString(existingFile.GetFileType()));
	}
	long lId = guidMapper.GetId(stGuid, iEntityTypeId);
	if ( lId == GuidToIdMapper::invalid_id ) {
		throw CrException("Internal Error: The existing file with GUID = " + stGuid + " of type "
			+ toString(iEntityTypeId) + " does not have a corresponding ID.");
	}

	if ( !userAccess.HasReadPermission(lId, iEntityTypeId) ) {
		throw CrException("The current user does not have read access for the requested file.");
	}

	vector<UploadedFile> dependencies;
	if ( iEntityTypeId == XmlEntityId ) {
		                                    
		GetXmlDependencies(lId, dependencies, guidMapper, userAccess, fileManager, *pConnection);
	}
	else {
		GetWsdlDependencies(lId, dependencies, guidMapper, userAccess, fileManager, *pConnection);
	}

	                         
	char* pchZippedAndBase64 = 0;
	fileManager.GetFile(existingFile, pchZippedAndBase64);
	if ( pchZippedAndBase64 == 0 )
		throw CrException("Internal Error: existing file " + existingFile.GetCanonicalPath() + existingFile.GetName() + " is null!");
	boost::scoped_array<char> adopt_pchZippedAndBase64(pchZippedAndBase64);
	existingFile.SetZippedBase64Contents(pchZippedAndBase64);

	                          
	auto_ptr<CFileDownloadResult> pRetVal( new CFileDownloadResult() );

	pRetVal->setDownloadedFile( new CDownloadedFile() );
	FillDownloadedFileWithContents(existingFile, *pRetVal->DownloadedFile);

	pRetVal->setDependencies( new ArrayOfCDownloadedFile() );
	const int nDeps = dependencies.size();
	if ( nDeps > 0 ) {
		CDownloadedFile** arrDownloadedFile = allocate_pointer_array<CDownloadedFile>(nDeps);
		AutoDeletePointerArray<CDownloadedFile> adopt_arrDownloadedFile(arrDownloadedFile, nDeps);
		for( int i = 0; i < nDeps; ++i ) {
			auto_ptr<CDownloadedFile> pFile( new CDownloadedFile() );			
			FillDownloadedFile(dependencies.at(i), *pFile);
			arrDownloadedFile[i] = pFile.release();
		}
		pRetVal->getDependencies()->item->set(arrDownloadedFile, nDeps);
	}

	return pRetVal.release();
}

