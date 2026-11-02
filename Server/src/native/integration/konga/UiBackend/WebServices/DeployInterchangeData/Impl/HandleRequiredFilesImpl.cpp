/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/util.h>		                  
#include <konga/Utils/FileUtils.h>	              
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/AutoFileCleanup.h>	                    

#include <konga/Utils/DateUtils.h>

#include <konga/Db/DbUtils/AutoDbCleanup.h>			                                

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/FileTransfer/FileRetriever.h>

#include <konga/Zip/ZipUtils.h>

#include <konga/UiBackend/UploadedFilesManager/UploadedFileTypes.h>
#include <konga/UiBackend/UploadedFilesManager/XmlFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/WsdlFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/FileUploadUtils.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>
#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "DeployInterchangeDataImpl.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::vector;
using std::string;

namespace {
	CFileCheckResult* GetFileCheckResult(int iStatus, const vector<UploadedFile>& existingFiles) {
		auto_ptr<CFileCheckResult> pRetVal( new CFileCheckResult() );
		pRetVal->setFileCheckStatus( iStatus );
		pRetVal->setAvailableRequiredFiles( new ArrayOfCRequiredFile() );

		const int nAvailableFiles = existingFiles.size();
		if ( nAvailableFiles > 0 ) {
			CRequiredFile** arrRequiredFile = allocate_pointer_array<CRequiredFile>(nAvailableFiles);
			AutoDeletePointerArray<CRequiredFile> adopt_arrRequiredFile(arrRequiredFile, nAvailableFiles);

			for( int i = 0; i < nAvailableFiles; ++i ) {
				const UploadedFile& existingFile = existingFiles.at(i);
				auto_ptr<CRequiredFile> pRequiredFile( new CRequiredFile() );
				string stPath(existingFile.GetPath()), stFile(existingFile.GetName());
				const string& stClientPath =
					GetClientPath(existingFile.GetCanonicalPath(), existingFile.GetName());
				pRequiredFile->setPath( const_cast<xsd__string>(stClientPath.c_str()) );
				pRequiredFile->setMD5( const_cast<xsd__string>(existingFile.GetMd5().c_str()) );
				pRequiredFile->setModifiedTime( GetClientTimeFromServerTime(existingFile.GetModifiedDateAsLong()) );
				pRequiredFile->setType( existingFile.GetFileType() );

				arrRequiredFile[i] = pRequiredFile.release();
			}
			pRetVal->getAvailableRequiredFiles()->item->set(arrRequiredFile, nAvailableFiles);
		}

		return pRetVal.release();
	}
}

CFileCheckResult* DeployInterchangeDataImpl::CheckRequiredFiles(
	const CFileDependencies* const pFileDependencies,
	const CRequiredFiles* pRequiredFiles)
{
	UIBACKEND_TRACE("DeployInterchangeDataImpl::CheckRequiredFiles was called.");
	vector<UploadedFile> existingFiles;
	if ( pRequiredFiles == 0 )
		return GetFileCheckResult(0, existingFiles);

	             
	init();
	m_pTranDb->SetTransactionIsolationLevel(konga::SERIALIZABLE);
	m_pTranDb->SetAutoCommit(false);
	AutoRollbackTransaction adopt_transaction(m_pTranDb);

	int iStatus = 0;
	if ( !HandleRequiredFiles(*pRequiredFiles, pFileDependencies, existingFiles) )
		iStatus = 1;

	adopt_transaction.Commit();

	UIBACKEND_TRACE("About to return from DeployInterchangeDataImpl::CheckRequiredFiles.");
	return GetFileCheckResult(iStatus, existingFiles);
}

namespace {
	void AddDependencies(const CFileDependencies& fileDependencies, const UploadedFilesManager& fileManager,
		const GuidToIdMapper& guidMapper, DbConnection& dbConnection, const string& stUserGuid)
	{

		if ( fileDependencies.ChildFilePath &&
			 fileDependencies.ChildFilePath->item &&
			 fileDependencies.ParentFilePath &&
			 fileDependencies.ParentFilePath->item )
		{
			int nParents = 0;
			const xsd__string* arrParents = fileDependencies.ParentFilePath->item->get(nParents);
			int nDeps = 0;
			const xsd__string* arrChildren = fileDependencies.ChildFilePath->item->get(nDeps);
			if ( nParents != nDeps ) {
				throw CrException("Bad Dependency Data: The Parent and Child vectors are of different sizes. Please report this bug!");
			}

			for( int i = 0; i < nDeps; ++i ) {
				const string stParent( toString(arrParents[i]) );
				if ( stParent.empty() )
					continue;
				const string stChild( toString(arrChildren[i]) );
				if ( stChild.empty() )
					continue;

				string stParentPath, stParentName;
				GetServerPath(stParent, stUserGuid, stParentPath, stParentName);
				UploadedFile existingParent;
				{
					UploadedFile incomingParent(stParentPath, stParentName);
					if ( !fileManager.ExistsFile(incomingParent, existingParent, true) ) {
						throw CrException("Failed to satisfy dependency " + stParent + " -> "
							+ stChild + ". The 'parent' file does not exist on the server.");
					}
				}

				string stChildPath, stChildName;
				GetServerPath(stChild, stUserGuid, stChildPath, stChildName);
				UploadedFile existingChild;
				{
					UploadedFile incomingChild(stChildPath, stChildName);
					if ( !fileManager.ExistsFile(incomingChild, existingChild, true) ) {
						throw CrException("Failed to satisfy dependency " + stParent + " -> "
							+ stChild + ". The 'child' file does not exist on the server.");
					}
				}

				int entityType = -1;
				const UploadedFileType fileType = existingParent.GetFileType();
				switch(fileType) {
					case XML_SCHEMA_FILE_TYPE:
					case XML_DTD_FILE_TYPE:
						entityType = XmlEntityId;
						break;
					case WSDL_FILE_TYPE:
						entityType = WsdlFileEntityId;
						break;
					default:
						throw CrException("Internal Error: Invalid uploaded file type: "
							+ toString(fileType) + ". This file type has not corresponding entity type.");
				};

				const long lParentId = guidMapper.GetId(existingParent.GetGuid(), entityType);
				if ( lParentId == GuidToIdMapper::invalid_id ) {
					throw CrException("Internal Error: Failed to satisfy dependency " + stParent + " -> "
							+ stChild + ". The 'parent' file does not have any id on the server.");
				}

				const long lChildId = guidMapper.GetId(existingChild.GetGuid(), entityType);
				if ( lChildId == GuidToIdMapper::invalid_id ) {
					throw CrException("Internal Error: Failed to satisfy dependency " + stParent + " -> "
							+ stChild + ". The 'child' file does not have any id on the server");
				}

				if ( entityType == XmlEntityId ) {
					AddXmlDependency(lParentId, lChildId, dbConnection);
				}
				else if ( entityType == WsdlFileEntityId ) {
					AddWsdlDependency(lParentId, lChildId, dbConnection);
				}
				else {
					assert(false && "This should never happen...");
				}
			}
		}
	}
}

bool DeployInterchangeDataImpl::HandleRequiredFiles(const CRequiredFiles& requiredFiles,
													const CFileDependencies* const pFileDependencies,
													vector<UploadedFile>& existingFiles)
{
	bool bAllRequiredFilesOK = true;

	                            
	if ( requiredFiles.RequiredFiles == 0 || requiredFiles.RequiredFiles->item == 0 )
		return bAllRequiredFilesOK;

	                          
	int nRequiredFiles = 0;
	CRequiredFile** const arrRequiredFiles =
		requiredFiles.RequiredFiles->item->get(nRequiredFiles);

	if ( nRequiredFiles < 1 )
		return bAllRequiredFilesOK;

	                                          
	  

	                  
	long lUserId = m_UserInfo.GetId();
	const string& stUserGuid = m_pGuidToIdMapper->GetGuid(lUserId, UserEntityId);
	if ( stUserGuid.empty() )
		throw CrException("Internal Error or Bad Data: User " + m_UserInfo.GetLogin()
		+ " does not exist on this server.");

	UploadedFilesManager fileManager(*m_pTranDb);

	                       
	for( int i = 0; i < nRequiredFiles; ++i ) {
		if ( !arrRequiredFiles[i] )
			continue;

		CRequiredFile& requiredFile = *arrRequiredFiles[i];

		string stMD5 = toString( requiredFile.getMD5() );

		const string& stClientPath = toString(requiredFile.getPath());

		UploadedFileType type = static_cast<UploadedFileType>(requiredFile.getType());

		const time_t modifiedTime = GetServerTimeFromClientTime( requiredFile.getModifiedTime() );

		int iFileEntityType = -1;
		string stPath, stName;
		bool bIsUrl = false;
		if ( type == XML_SCHEMA_FILE_TYPE || type == XML_DTD_FILE_TYPE ) {
			GetServerPath(stClientPath, stUserGuid, stPath, stName);
			iFileEntityType = XmlEntityId;
		}
		else if (type == WSDL_FILE_TYPE ) {
			bIsUrl = GetServerPath(stClientPath, stUserGuid, stPath, stName);
			iFileEntityType = WsdlFileEntityId;
		}
		else {
			throw CrException("Invalid input for required files. The uploaded file type "
				+ toString(type) + " is not valid.");
		}

		                                     
		ZippedUploadedFile incomingFile(stPath, stName, "", type, stMD5, modifiedTime);
		bool bAdd = true;
		bool bExists = false;
		{
			UploadedFile existingFile;
			if ( fileManager.ExistsFile(incomingFile, existingFile, false) ) {
				bExists = true;
				incomingFile.SetGuid(existingFile.GetGuid());

				                                               
				                                                                
				if ( existingFile.GetMd5() == stMD5 || bIsUrl ) {
					existingFiles.push_back(existingFile);
					bAdd = false;
				}
			}
			else {
				incomingFile.SetGuid(GetGUIDString());
			}
		}

		AutoArrayDelete<char> adopt_contents(0);
		const char* pcContents = requiredFile.getZippedBase64Contents();
		if ( bAdd && bIsUrl ) {
			                    
			const string stTmpFile( getTmpDir() + stName );
			FileRetriever::SaveUrlToFile(stClientPath, stTmpFile);
			konga::AutoDeleteFileSet adopt_tmpfile;
			adopt_tmpfile.AddFile(stTmpFile);
			char* pcTmp = 0;
			stMD5 = ZipUtils::ZipAndBase64EncodeFile(stTmpFile, pcTmp);
			if ( pcTmp ) {
				adopt_contents.Reset(pcTmp);
				pcContents = pcTmp;
				incomingFile.SetMd5(stMD5);
				incomingFile.SetModifiedDate( CrDateTime::GetCurrentTime().GetTime() );
			}
			else {
				throw CrException("Internal Error: Failed to read downloaded URL from the file " + stTmpFile
					+ ". ZipAndBase64EncodeFile returned NULL.");
			}
		}

		if ( bAdd ) {
			if ( pcContents && *pcContents ) {
				incomingFile.SetZippedBase64Contents(pcContents);

				if ( iFileEntityType == XmlEntityId ) {
					AddXmlFile(incomingFile, bExists, bAdd, fileManager,
						*m_pGuidToIdMapper, *m_pUserAccess, *m_pTranDb);
				}
				else if ( iFileEntityType == WsdlFileEntityId ) {

					long wsdlId = -1;
					const string& stGuid = incomingFile.GetGuid();
					const string stUrl( bIsUrl ? stClientPath : "" );
					const string& stWsdlName = stClientPath;
					                                 
					UploadedWsdlFileType wsdlType = INVALID_WSDL_FILE_TYPE;
					                                                                             
					                                         
					if ( bIsUrl )
						wsdlType = WSDL_WSDL_FILE_TYPE;
					else if ( EqualsIgnoreCase(GetFileExtension(stClientPath), "xsd") )
						wsdlType = INCLUDED_SCHEMA_WSDL_FILE_TYPE;
					else
						wsdlType = WSDL_WSDL_FILE_TYPE;

					wsdlId = AddWsdlFile(wsdlId, stGuid, stWsdlName, stUrl, wsdlType, incomingFile, *m_pTranDb,
						*m_pUserAccess, fileManager, *m_pGuidToIdMapper, bAdd, bExists);
				}
				else {
					throw CrException("Internal Error: Entity " + toString(iFileEntityType) + " cannot be a file dependency.");
				}
				existingFiles.push_back( incomingFile );
			}
			else {
				bAllRequiredFilesOK = false;
			}
		}
	}

	                                                                                          
	                                
	if ( bAllRequiredFilesOK && pFileDependencies ) {
		AddDependencies(*pFileDependencies, fileManager, *m_pGuidToIdMapper, *m_pTranDb, stUserGuid);
	}
	return bAllRequiredFilesOK;
}
