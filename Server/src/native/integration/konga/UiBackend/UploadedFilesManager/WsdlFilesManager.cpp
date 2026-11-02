/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <boost/scoped_array.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>

#include <konga/Zip/ZipUtils.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/Db/TranDb/WsdlTab.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include "UploadedFilesManager.h"
#include "FileUploadUtils.h"
#include "WsdlFilesManager.h"

using namespace konga;
using std::string;
using std::vector;

long konga::AddWsdlFile(long wsdlId, const std::string& stGuid, const std::string& stWsdlName,
		const std::string& stUrl, UploadedWsdlFileType wsdlType, ZippedUploadedFile& incomingFile, DbConnection& tranDbConnection,
		UserAccess& userAccess, UploadedFilesManager& fileManager, GuidToIdMapper& guidMapper,
		bool bAdd, bool bExists, int iStatus, const string& stApplicationMessage,
		const std::string& stUserComment)
{
	if ( tranDbConnection.GetTransactionIsolationLevel() != konga::SERIALIZABLE )
		throw CrException("Internal Error: The connection has to be serializable");

	if ( bAdd ) {
		if ( bExists ) {
			                    
			if ( wsdlId < 1 ) {
				                                                            
				                                                                  
				wsdlId = guidMapper.GetId(stGuid, WsdlFileEntityId);
				if ( wsdlId == GuidToIdMapper::invalid_id ) {
					throw CrException("Internal Error: The uploaded WSDL file \""
						+ stWsdlName + "\" is flagged as existing but there is no WSDL id corresponding to the " \
						" GUID " + stGuid + ". This is probably a bug.");
				}
			}
			if ( !userAccess.HasWritePermission(wsdlId, WsdlFileEntityId) )
				throw CrException("The current user does not have permission to overwrite this WSDL file.");

			fileManager.UpdateFile(incomingFile);
		}
		else {
			fileManager.AddFile(incomingFile);

			                      
			const string& stWsdlPath = getRelativePath(incomingFile.GetFullAbsolutePath());
			if ( stWsdlPath.length() > 1024 )
				throw CrException("The path for the WSDL file \"" + stWsdlName + "\" is too long for the system." \
				" Please limit the number of characters in the path.");

			SmartDbPtr<TranDb::WsdlTab> pWsdlTab( TranDb::WsdlTab::MakeInstance(&tranDbConnection) );
			pWsdlTab->AddNew();
			pWsdlTab->SetWsdlName(stWsdlName.substr(0,1024));
			                                                                               
			                                               
			pWsdlTab->SetWsdlURL(stUrl.empty() ? "(none)" : stUrl);
			pWsdlTab->SetWsdlPath(stWsdlPath);

			pWsdlTab->SetStatus(iStatus);
			pWsdlTab->SetApplicationMessage(stApplicationMessage.substr(0, 4096));
			pWsdlTab->SetUserComment(stUserComment.substr(0,4096));
			wsdlId = konga::GetNextId(getTableName(nameWsdl), getIdName(nameWsdl), tranDbConnection);
			pWsdlTab->SetWsdl_ID(wsdlId);
			pWsdlTab->SetWsdlType( static_cast<int>(wsdlType) );
			pWsdlTab->Update();

			                           
			userAccess.AddItemOwner(wsdlId, WsdlFileEntityId);
			guidMapper.SetGuidForItem(wsdlId, WsdlFileEntityId, stGuid);
		}
	}
	return wsdlId;
}

namespace {
	void CloneDependencies(const long lParentWsdlId, const long lClonedParentWsdlId,
		DbConnection& dbConnection, UploadedFilesManager& fileManager, const UserInfo& userInfo,
		UserAccess& userAccess, GuidToIdMapper& guidMapper)
	{
		if ( lParentWsdlId == lClonedParentWsdlId )
			return;

		vector<UploadedFile> dependencies;
		GetWsdlDependencies(lParentWsdlId, dependencies, guidMapper, userAccess, fileManager, dbConnection);
		
		for( vector<UploadedFile>::const_iterator cit = dependencies.begin(); cit != dependencies.end(); ++cit ) {
			const UploadedFile& uploadedFile = *cit;
			const string& guid = uploadedFile.GetGuid();
			const long lChildWsdlId = guidMapper.GetId(guid, WsdlFileEntityId);
			if ( lChildWsdlId == GuidToIdMapper::invalid_id ) {
				throw InternalErrorException("An existing dependency for WSDL_ID = " + toString(lParentWsdlId)
					+ " with GUID = " + guid + " has no corresponding database id (this should not happen).");
			}

			                        
			const long lClonedChildWsdlId = konga::CloneWsdl(lChildWsdlId, dbConnection, userInfo, userAccess, fileManager, guidMapper);

			                             
			AddWsdlDependency(lClonedParentWsdlId, lClonedChildWsdlId, dbConnection);
		}
	}
}

long konga::CloneWsdl(const long wsdlId, DbConnection& tranDbConnection,
					  const UserInfo& userInfo, UserAccess& userAccess, UploadedFilesManager& fileManager,
					  GuidToIdMapper& guidMapper)
{
	SmartDbPtr<TranDb::WsdlTab> pWsdlTab( TranDb::WsdlTab::MakeInstance(&tranDbConnection) );
	pWsdlTab->Select("where wsdl_id = " + toString(wsdlId));
	if ( !pWsdlTab->Next() )
		throw InternalErrorException("Failed to clone WSDL with WSDL_ID = " + toString(wsdlId)
		+ ". The specified WSDL does not exist on the system.");

	                                                                 
	const string& wsdlName = pWsdlTab->GetWsdlName();
	const string& wsdlUrl = pWsdlTab->GetWsdlURL();
	const string& wsdlPath = pWsdlTab->GetWsdlPath();
	const int iStatus = pWsdlTab->GetStatus();
	const string applicationMsg( pWsdlTab->IsApplicationMessageNull() ? "" : pWsdlTab->GetApplicationMessage() );
	const string userComment( pWsdlTab->IsUserCommentNull() ? "" : pWsdlTab->GetUserComment() );
	const UploadedWsdlFileType wsdlType = static_cast<UploadedWsdlFileType>(pWsdlTab->GetWsdlType());

	const string& absoluteWsdlPath = konga::getAbsolutePath(wsdlPath, true);
	const string& stUserGuid = konga::GetUserGuid(userInfo, guidMapper);
	string stPath, stName;
	konga::GetServerPath(wsdlName, stUserGuid, stPath, stName);

	char* pcZ = 0;
	const string& stMD5 = ZipUtils::ZipAndBase64EncodeFile(absoluteWsdlPath, pcZ, true);
	assert(pcZ);
	boost::scoped_array<char> adopt_pcZ(pcZ);

	const UploadedFile incomingFile(stPath, stName, "", WSDL_FILE_TYPE, "");
	UploadedFile clonedFile;
	const bool bExists = fileManager.ExistsFile(incomingFile, clonedFile, true);
	long newWsdlId = -1;
	string newWsdlGuid;
	if ( bExists ) {
		newWsdlGuid = clonedFile.GetGuid();
		newWsdlId = guidMapper.GetId(newWsdlGuid, WsdlFileEntityId);
		                                                                       
		if ( clonedFile.GetMd5() == stMD5 )
			return newWsdlId;
	}
	else {
		                                                                    
		konga::GetGUIDString(newWsdlGuid);
		clonedFile.SetFileType(WSDL_FILE_TYPE);
		clonedFile.SetGuid(newWsdlGuid);
		clonedFile.SetModifiedNow();
		clonedFile.SetName(stName);
		clonedFile.SetPath(stPath);
	}
	clonedFile.SetMd5(stMD5);

	                                         
	ZippedUploadedFile zippedFile(clonedFile);
	zippedFile.SetZippedBase64Contents(pcZ);
	newWsdlId = AddWsdlFile(newWsdlId, newWsdlGuid, wsdlName, wsdlUrl, wsdlType, zippedFile, tranDbConnection,
		userAccess, fileManager, guidMapper, true, bExists, iStatus, applicationMsg, userComment);

	adopt_pcZ.reset();	                                                             
	CloneDependencies(wsdlId, newWsdlId, tranDbConnection, fileManager, userInfo, userAccess, guidMapper);

	return newWsdlId;
}

void konga::AddWsdlDependency(long lParentId, long lChildId,
							  DbConnection& dbConnection)
{
	const string& sParentId = toString(lParentId);
	const string& sChildId = toString(lChildId);
	string sql("DELETE FROM WsdlDependenciesTab WHERE Parent_Wsdl_Id = "
		+ sParentId + " AND Child_Wsdl_Id = " + sChildId);
	dbConnection.ExecuteSql(sql);

	sql = "INSERT INTO WsdlDependenciesTab (Parent_Wsdl_Id, Child_Wsdl_Id) VALUES ("
		+ sParentId + ", " + sChildId + ")";
	dbConnection.ExecuteSql(sql);
}

void konga::GetWsdlDependencies(const long lWsdlId, vector<UploadedFile>& dependencies,
	const GuidToIdMapper& guidMapper, const UserAccess& userAccess,
	const UploadedFilesManager& fileManager, DbConnection& dbConnection)
{
	dependencies.clear();
	const string stSelectWsdlDeps("SELECT Child_Wsdl_Id FROM " + getTableName(nameWsdlDependencies)
		+ " WHERE Parent_Wsdl_Id = " + toString(lWsdlId));

	SmartDbPtr<DynamicDbRecordSet> pRecordSet( DynamicDbRecordSet::MakeInstance(&dbConnection, stSelectWsdlDeps) );
	pRecordSet->Open();
	while ( pRecordSet->Next() ) {
		const long lChildWsdlId = atoi(pRecordSet->GetFieldValue(0).c_str());
		const string& stChildGuid = guidMapper.GetGuid(lChildWsdlId, WsdlFileEntityId);
		if ( stChildGuid.empty() )
			throw CrException("Internal Error: A child WSDL does not have corresponding GUID defined on the system.");

		if ( !userAccess.HasReadPermission(lChildWsdlId, WsdlFileEntityId) )
			continue;

		UploadedFile child("", "", stChildGuid), existingChild;
		if ( !fileManager.ExistsFile(child, existingChild, true) )
			throw CrException("Internal Error: Child schema file with GUID = " + stChildGuid
			+ " does not exist on the server.");

		dependencies.push_back( existingChild );
	}
}

void konga::ValidateExistingAndMissingWsdlDependencies(vector<string>& vExistingIncludes,
	vector<string>& vMissingIncludes, const UploadedFile& parentWsdl,
	const GuidToIdMapper& guidToIdMapper,
	DbConnection& dbConnection)
{
	if ( vExistingIncludes.empty() ) return;

	const string& sPath = parentWsdl.GetPath();
	UploadedFilesManager fileManager(dbConnection);

	long lParentWsdlId = guidToIdMapper.GetId(parentWsdl.GetGuid(), WsdlFileEntityId);
	if ( lParentWsdlId == GuidToIdMapper::invalid_id )
		throw CrException("Internal Error: The parent WSDL with GUID '" + parentWsdl.GetGuid()
		+ "' does not exist.");

	                                                                      
	                                                                           
	vector<string>::iterator existingIt = vExistingIncludes.begin();
	for( ; existingIt != vExistingIncludes.end(); ) {
		const string stExistingName(*existingIt);
		UploadedFile reportedDependency(sPath, stExistingName, "", WSDL_FILE_TYPE, ""),
			existingDependency;
		if ( !fileManager.ExistsFile(reportedDependency, existingDependency, false) ) {
			vMissingIncludes.push_back(stExistingName);
			existingIt = vExistingIncludes.erase(existingIt);
		}
		else {
			                                                    
			long lChildWsdlId = guidToIdMapper.GetId(existingDependency.GetGuid(), WsdlFileEntityId);
			if ( lChildWsdlId == GuidToIdMapper::invalid_id ) {
				throw CrException("Internal Error: The child WSDL with GUID '" + existingDependency.GetGuid()
					+ "' does not exist.");
			}
			AddWsdlDependency(lParentWsdlId, lChildWsdlId, dbConnection);
			++existingIt;
		}
	}
}
