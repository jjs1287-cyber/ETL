/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <boost/scoped_array.hpp>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/AutoFileCleanup.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/AutoDbCleanup.h>			                                

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ProjectBackup/ProjectBackup.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>

#include "DeployInterchangeDataImpl.h"

using namespace konga;
using namespace konga::interchange_deploy;

CDeployResults* DeployInterchangeDataImpl::ProjectBackup(CUploadedJitterpak* const pUploadedJitterpak) {
	                                 
	assert(pUploadedJitterpak);
	assert(pUploadedJitterpak->Base64EncodedJitterpak);
	assert(pUploadedJitterpak->ProjectGuid);

	if ( !pUploadedJitterpak )
		throw CrException("Client Error: Uploaded Jitterpack is null!");

	if ( !pUploadedJitterpak->ProjectGuid )
		throw CrException("Client Error: Project GUID is null!");
	if ( !pUploadedJitterpak->Base64EncodedJitterpak )
		throw CrException("Client Error: Jitterpak is null!");

	this->init();
	assert(m_pGuidToIdMapper);
	assert(m_pTranDb);

	const string stProjectGuid( toString(pUploadedJitterpak->ProjectGuid) );
	const string stTag( toString(pUploadedJitterpak->Tag) );

	m_pTranDb->SetTransactionIsolationLevel(konga::READ_COMMITTED);
	m_pTranDb->SetAutoCommit(false);
	AutoRollbackTransaction adopt_transaction(m_pTranDb);

	const long lInterchangeProjectId = m_pGuidToIdMapper->GetId(stProjectGuid, InterchangeProjectEntityId);
	if ( lInterchangeProjectId == GuidToIdMapper::invalid_id ) {
		AddWarning(InterchangeProjectEntityId, "", "The project has not been deployed to this server.");
		return AllocateDeployResults(1, *this);
	}

	UserAccess userAccess(m_UserInfo, *m_pTranDb);
	if ( !userAccess.HasWritePermission(lInterchangeProjectId, InterchangeProjectEntityId) )
		throw CrException("The current user does not have write permission for this project.");

	const string& stUserGuid = m_pGuidToIdMapper->GetGuid(m_UserInfo.GetId(), UserEntityId);
	if ( stUserGuid.empty() ) {
		throw InternalErrorException("Failed to find User GUID. This should not happen since the user has already been authenticated!");
	}

	int jitterpakSize = 0;
	unsigned char* pchJitterpak =
		konga::DecodeBase64(pUploadedJitterpak->Base64EncodedJitterpak, jitterpakSize);
	assert( pchJitterpak && jitterpakSize > 0 );
	boost::scoped_array<unsigned char> adopt_pchJitterpak(pchJitterpak);

	                                                   
	const string stTmpFile(getTmpFileName("jitterpak-upload"));
	write_to_binary_file(stTmpFile, pchJitterpak, jitterpakSize);
	AutoDeleteFileSet auto_delete_files;
	auto_delete_files.AddFile(stTmpFile);
	adopt_pchJitterpak.reset(0);

	konga::ProjectBackup backupper(*m_pTranDb);

	backupper.SaveBackup(stProjectGuid, stUserGuid, stTag, stTmpFile);

	adopt_transaction.Commit();
	
	return AllocateDeployResults(0, *this);
}
