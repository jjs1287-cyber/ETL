/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>		            
#include <konga/Utils/util.h>			                

#include <konga/Crypto/Crypto.h>		        

#include <konga/Zip/ZipUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>	                                     

#include <konga/ItemAccess/UserAccess.h>

#include <konga/Messaging/OperationMessageUtils.h>

#include <konga/Authentication/Authentication.h>

#include <konga/OperationContext/DefaultOperationDataElementHolder.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>
#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		           

#include "JobControlImpl.h"

using namespace konga;
using namespace std;

void JobControlImpl::RunOperation(const xsd__string& UserName, const xsd__string& Password, const xsd__string& OperationGuid) {

	                                                                           
	const string& stPassword = toString(Password);
	const string& stMD5Password = konga::md5(stPassword);

	xsd__string md5Pwd = konga::allocate_xsd__string(stMD5Password);
	AutoDeleteOur_xsd__string adopt_md5Pwd(md5Pwd);
	RunOperationMD5Password(UserName, md5Pwd, OperationGuid);
}

void JobControlImpl::RunOperationMD5Password(const xsd__string& UserName, const xsd__string& MD5Password, const xsd__string& OperationGuid) {
	WsRunOperationResult* tmp = RunOperationImpl(toString(UserName), toString(MD5Password), toString(OperationGuid), 0);
	delete tmp;
}

WsRunOperationResult* JobControlImpl::RunOperationWithSourceData(const xsd__string& UserName, const xsd__string MD5Password, const xsd__string& OperationGuid,
		const xsd__string& CompressedAndbase64EncodedSourceData)
{
	return RunOperationImpl(toString(UserName), toString(MD5Password), toString(OperationGuid), CompressedAndbase64EncodedSourceData);
}

WsRunOperationResult* JobControlImpl::RunOperationInstanceMD5Password(const xsd__string& UserName, const xsd__string& MD5Password, const xsd__string& OperationGuid) {
	return RunOperationImpl(toString(UserName), toString(MD5Password), toString(OperationGuid), 0);
}

namespace {
	string getOverrideFileName() {
		string dir(konga::getTmpDir());
		dir += "DoOperationSourceOverride";
		konga::AppendPathSeparator(dir);
		dir += GetGUIDString();	                                                            
		konga::kCreateDirectory(dir);
		konga::AppendPathSeparator(dir);
		return string(dir + konga::GetGUIDString());
	}
}

                                                                       
#ifdef GetUserName
#undef GetUserName
#endif

WsRunOperationResult* JobControlImpl::RunOperationImpl(const std::string& stUserName, const std::string& stMD5Password,
					  const std::string& stOperationGuid, const xsd__string& CompressedAndbase64EncodedSourceData)
{
	int iUserId = 0;
	try {
		AuthenticateUserOfWebService(stUserName.c_str(), stMD5Password.c_str(), iUserId);
	}
	catch ( const Authentication::AuthenticationException& e ) {
		auditLog("Authentication failed for user " + e.GetUserName()
			+ ": " + e.GetErrorString());
		throw;
	}

	const long lOperationId = DbGuidToIdMapper().GetId(stOperationGuid, OperationEntityId);
	if( lOperationId == DbGuidToIdMapper::invalid_id )
		throw CrException("The requested operation has not been deployed to this server.");

	if( !UserHasExecutePermission(iUserId, lOperationId, OperationEntityId) ) {
		string stErr("User '" + stUserName + 
			"' is not allowed to execute the operation with guid '" + stOperationGuid + "'.");
		auditLog(stErr);
		throw CrException(stErr);
	}

	long long llOperationInstanceId = -1;
	if ( CompressedAndbase64EncodedSourceData == 0 ) {
		llOperationInstanceId = konga::AddOperationToQueue(lOperationId, UNDEFINED_SESSION_ID, UNDEFINED_OPERATION_INSTANCE_ID, "JobControl", "RunOperationMD5Password", iUserId);
	}
	else {
		const string& stOverrideFileName = getOverrideFileName();
		konga::ZipUtils::DecodeUnzipAndWriteToFile(stOverrideFileName, CompressedAndbase64EncodedSourceData, false);

		UserInfo user(iUserId);
		DefaultOperationDataElementHolder deHolder(user);
		llOperationInstanceId = 
			konga::AddOperationToQueueOverrideSource(lOperationId, "JobControl", "Added by RunOperationWithSourceData",
			stOverrideFileName, true, deHolder, iUserId);
	}

	std::auto_ptr<WsRunOperationResult> wsRunOperationResult( new WsRunOperationResult() );
	wsRunOperationResult->setOperationInstanceId(llOperationInstanceId);
	wsRunOperationResult->setOperationInstanceInfo("", true);

	auditLog(stUserName + " submitted operation " + stOperationGuid + " to the process queue. Operation Instance Id = " + toString(llOperationInstanceId));

	return wsRunOperationResult.release();
}
