/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include "BulkLoadEngineWrapperC.h"

using namespace konga;

using std::string;

axis2_stub_t* BulkLoadEngineWrapperC::create_axis2_stub_t() {
	return axis2_stub_create_BulkLoadEngineService(this->m_env, this->m_home, this->m_uri);
}

BulkLoadEngineWrapper* BulkLoadEngineWrapper::createBulkLoadEngineWrapper(DbConnection& tranDbConn, const UserInfo& userInfo) {
	string userName, md5pwd;
	konga::GetCredentials(tranDbConn, userInfo, userName, md5pwd);
	return new BulkLoadEngineWrapperC(userName, md5pwd);
}

BulkLoadEngineWrapperC::BulkLoadEngineWrapperC(const string& userName, const string& md5pwd) : Axis2CImplBase(BULK_LOAD_ENGINE) {
	m_userName = userName;
	m_md5Pwd = md5pwd;
}

namespace {
	                                                    
	string handleWsBulkLoadError(axutil_env_t* env, adb_WsBulkLoadError_t* error, const string& header) {
		if ( error == 0 ) {
			throw InternalErrorException(header + ". The call was successful but no error information was returned. The error is required to get the warnings even if there are none.");
		}

		const int errorCode = adb_WsBulkLoadError_get_errorCode(error, env);
		if ( errorCode != 0 ) {
			const axis2_char_t* err = adb_WsBulkLoadError_get_error(error, env);
			if ( err == 0 || *err == 0 ) {
				throw CrException(header + ". Error code " + konga::toString(errorCode) + " was reported but no error was provided in WsBulkLoadError.");
			}
			throw CrException(header + ". " + string(err));
		}
		const axis2_char_t* warnings = adb_WsBulkLoadError_get_warnings(error, env);
		if ( warnings != 0 && *warnings != 0 ) {
			return string(warnings);
		}
		return string();
	}
}

BulkLoadEngineWrapperC::~BulkLoadEngineWrapperC(void) {
	closeSessionNoThrow();
}

void BulkLoadEngineWrapperC::closeSessionNoThrow() {
	try {
		closeSession();
	} catch (...) {
		                                 
	}
}


void BulkLoadEngineWrapperC::startNewSession() {
	closeSessionNoThrow();

	this->InitCurrent();
	axutil_env_t* env = this->m_env;

	adb_startNewBulkLoadSession_t* _startSession = adb_startNewBulkLoadSession_create(env);
	adb_startNewBulkLoadSession_set_etlforgeUser(_startSession, env, m_userName.c_str());
	adb_startNewBulkLoadSession_set_md5Pwd(_startSession, env, m_md5Pwd.c_str());

	adb_startNewBulkLoadSessionResponse_t* response = axis2_stub_op_BulkLoadEngineService_startNewBulkLoadSession(this->m_stub, env, _startSession);
	adb_startNewBulkLoadSession_free(_startSession, env); _startSession = 0;

	const string errHeader("Failed to start new Bulk Engine");
	throwOnError(env, errHeader);
	throwOnNullResponse(response, errHeader);

	adb_WsStartNewBulkLoadSessionResult_t* result = adb_startNewBulkLoadSessionResponse_get_startNewBulkLoadSessionReturn(response, env);
	if ( result == 0 ) {
		throw InternalErrorException(errHeader + ". The call failed but the result is 0.");
	}

	adb_WsBulkLoadError_t* error = adb_WsStartNewBulkLoadSessionResult_get_returnValue(result, env);
	handleWsBulkLoadError(env, error, errHeader);

	const axis2_char_t* sessionId = adb_WsStartNewBulkLoadSessionResult_get_sessionId(result, env);
	if ( sessionId == 0 || *sessionId == 0 ) {
		throw InternalErrorException(errHeader + ". The call was successful but an empty Session ID was returned.");
	}
	m_sessionGuid = sessionId;

	this->CleanCurrent();
}

adb_WsBulkLoadSessionId_t* BulkLoadEngineWrapperC::createWsBulkLoadSessionId() {
	axutil_env_t* env = this->m_env;
	adb_WsBulkLoadSessionId_t* _sessionId = adb_WsBulkLoadSessionId_create(env);
	adb_WsBulkLoadSessionId_set_etlforgeUser(_sessionId, env, m_userName.c_str());
	adb_WsBulkLoadSessionId_set_md5Pwd(_sessionId, env, m_md5Pwd.c_str());
	adb_WsBulkLoadSessionId_set_sessionGuid(_sessionId, env, m_sessionGuid.c_str());
	return _sessionId;
}

void BulkLoadEngineWrapperC::closeSession() {
	if ( m_sessionGuid.empty() ) return;

	this->InitCurrent();
	axutil_env_t* env = this->m_env;

	adb_closeBulkLoadSession_t* _closeSession = adb_closeBulkLoadSession_create(env);

	adb_closeBulkLoadSession_set_sessionId(_closeSession, env, createWsBulkLoadSessionId());

	adb_closeBulkLoadSessionResponse_t* response = axis2_stub_op_BulkLoadEngineService_closeBulkLoadSession(this->m_stub, env, _closeSession);
	adb_closeBulkLoadSession_free(_closeSession, env); _closeSession = 0;

	const string errorHeader("Failed to close Bulk Load Session " + m_sessionGuid);
	m_sessionGuid.clear();

	throwOnError(env, errorHeader);
	throwOnNullResponse(response, errorHeader);

	adb_WsBulkLoadError_t* error = adb_closeBulkLoadSessionResponse_get_closeBulkLoadSessionReturn(response, env);
	handleWsBulkLoadError(env, error, errorHeader);

	this->CleanCurrent();
}

adb_WsBulkLoadParams_t* BulkLoadEngineWrapperC::createWsBulkLoadParams(long long sfdcBulkLoadId, long long operationInstanceId,
																	   long long lastOperationInstanceId, const std::string& stSourceFile)
{
	axutil_env_t* env = this->m_env;

	adb_WsBulkLoadParams_t* _params = adb_WsBulkLoadParams_create(env);

	adb_WsBulkLoadParams_set_operationInstanceId(_params, env, operationInstanceId);
	adb_WsBulkLoadParams_set_lastOperationInstanceId(_params, env, lastOperationInstanceId);
	adb_WsBulkLoadParams_set_bulkLoadId(_params, env, sfdcBulkLoadId);
	adb_WsBulkLoadParams_set_sourceFilePath(_params, env, stSourceFile.c_str());

	return _params;
}

adb_WsSalesforceEndpoint_t* BulkLoadEngineWrapperC::createWsSalesforceEndpoint(const string& stSFDCSessionId, const string& stSFDCUrl) {
	axutil_env_t* env = this->m_env;

	adb_WsSalesforceEndpoint_t* _endpoint = adb_WsSalesforceEndpoint_create(env);

	adb_WsSalesforceEndpoint_set_url(_endpoint, env, stSFDCUrl.c_str());
	adb_WsSalesforceEndpoint_set_sessionId(_endpoint, env, stSFDCSessionId.c_str());

	return _endpoint;
}

namespace {
	adb_WsKeyValue_t* createWsKeyValue(axutil_env_t* env, const string& key, const string& value) {
		adb_WsKeyValue_t* wsKeyValue = adb_WsKeyValue_create(env);
		adb_WsKeyValue_set_key(wsKeyValue, env, key.c_str());
		adb_WsKeyValue_set_value(wsKeyValue, env, value.c_str());
		return wsKeyValue;
	}
}

BulkLoadResult BulkLoadEngineWrapperC::runBulkLoad(const string& stSFDCSessionId, const string& stSFDCUrl,
												   long long sfdcBulkLoadId, long long operationInstanceId,
												   long long lastOperationInstanceId, const string& stSourceFile)
{
	this->InitCurrent();
	axutil_env_t* env = this->m_env;

	adb_runBulkLoad_t* _runBulkLoad = adb_runBulkLoad_create(env);

	adb_runBulkLoad_set_sessionId(_runBulkLoad, env, createWsBulkLoadSessionId());

	adb_runBulkLoad_set_bulkloadParams(_runBulkLoad, env, createWsBulkLoadParams(sfdcBulkLoadId, operationInstanceId, lastOperationInstanceId, stSourceFile));
	adb_runBulkLoad_set_salesforceEndpoint(_runBulkLoad, env, createWsSalesforceEndpoint(stSFDCSessionId, stSFDCUrl));

	                                                                                                 
	adb_runBulkLoad_reset_in3(_runBulkLoad, env);
	adb_runBulkLoad_add_in3(_runBulkLoad, env, createWsKeyValue(env, "client","Axis2C"));

	adb_runBulkLoadResponse_t* response = axis2_stub_op_BulkLoadEngineService_runBulkLoad(this->m_stub, env, _runBulkLoad);
	adb_runBulkLoad_free(_runBulkLoad, env); _runBulkLoad = 0;

	const string errorHeader("Failed to run bulk load");
	throwOnError(env, errorHeader);
	throwOnNullResponse(response, errorHeader);

	adb_WsBulkLoadResult_t* _result = adb_runBulkLoadResponse_get_runBulkLoadReturn(response, env);
	adb_WsBulkLoadError_t* error = adb_WsBulkLoadResult_get_error(_result, env);
	m_stWarnings = handleWsBulkLoadError(env, error, errorHeader);

	axis2_bool_t allBatchesWereSent = adb_WsBulkLoadResult_get_allBatchesWereSent(_result, env);
	axis2_char_t* logFile = adb_WsBulkLoadResult_get_logFile(_result, env);

	BulkLoadResult result;
	result.allBatchesSent = (allBatchesWereSent == AXIS2_TRUE);
	result.logFile = (logFile == 0 ) ? "" : logFile;
	result.warnings = m_stWarnings;

	return result;
}

string BulkLoadEngineWrapperC::getWarnings() const {
	return m_stWarnings;
}

