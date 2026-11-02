/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */


#include "JsonEngineWrapperC.h"

using namespace konga;
using std::string;

axis2_stub_t* JsonEngineWrapperC::create_axis2_stub_t() {
	return axis2_stub_create_JsonEngineService(this->m_env, this->m_home, this->m_uri);
}

JsonEngineWrapper* JsonEngineWrapper::createJsonEngineWrapper(DbConnection& tranDbConn, const UserInfo& userInfo) {
	string userName, md5pwd;
	konga::GetCredentials(tranDbConn, userInfo, userName, md5pwd);
	return new JsonEngineWrapperC(userName, md5pwd);
}

JsonEngineWrapperC::JsonEngineWrapperC(const std::string& userName, const std::string& md5pwd) : Axis2CImplBase(JSON_ENGINE) {
	m_userName = userName;
	m_md5Pwd = md5pwd;
}

JsonEngineWrapperC::~JsonEngineWrapperC(void) {
	closeSession();
}

void JsonEngineWrapperC::handle_json_error(adb_WsJsonError_t* wsError, const string& header) const {
	axutil_env_t* env = this->m_env;
	if ( wsError ) {
		const int errorCode = adb_WsJsonError_get_ErrorCode(wsError, env);
		if ( errorCode != 0 ) {
			axis2_char_t* error = adb_WsJsonError_get_Error(wsError, env);
			if ( error && *error) {
				throw CrException(header + ".\n" +  string(error));
			}
			else {
				throw CrException(header + ". No error was reported. Error Code: " + konga::toString(errorCode)
					+ "\n. See log file for more information about the possible causes of the error: " + this->m_logFile);
			}
		}
	}
}

void JsonEngineWrapperC::startNewSession() {
	if ( !m_sessionGuid.empty() ) {
		closeSession();
	}

	this->InitCurrent();
	axutil_env_t* env = this->m_env;

	adb_startNewJsonSession_t* startSession = adb_startNewJsonSession_create(env);
	adb_startNewJsonSession_set_ETLForgeUser(startSession, env, allocate_axis2_char_t(m_userName));
	adb_startNewJsonSession_set_Md5Pwd(startSession, env, allocate_axis2_char_t(m_md5Pwd));

	adb_startNewJsonSessionResponse_t* response = axis2_stub_op_JsonEngineService_startNewJsonSession(m_stub, env, startSession);
	adb_startNewJsonSession_free(startSession, env); startSession = 0;

	const string errHeader("Failed to start new Json Engine session");
	throwOnError(env, errHeader);
	throwOnNullResponse(response, errHeader);

	adb_WsStartNewJsonSessionResult_t* wsResult = adb_startNewJsonSessionResponse_get_startNewJsonSessionReturn(response, env);

	adb_WsJsonError_t* wsError = adb_WsStartNewJsonSessionResult_get_Error(wsResult, env);
	handle_json_error(wsError, errHeader);

	axis2_char_t* sessionId = adb_WsStartNewJsonSessionResult_get_SessionId(wsResult, env);
	if ( sessionId == 0 || *sessionId == 0 ) {
		throw InternalErrorException(errHeader + ". The call was successful but no SessionId was returned.");
	}
	m_sessionGuid = sessionId;

	this->CleanCurrent();
}

adb_WsJsonSessionId_t* JsonEngineWrapperC::create_adb_WsJsonSessionId_t() const {
	axutil_env_t* env = this->m_env;
    adb_WsJsonSessionId_t* wsSessionId = adb_WsJsonSessionId_create(env);
	adb_WsJsonSessionId_set_ETLForgeUser(wsSessionId, env, allocate_axis2_char_t(m_userName));
	adb_WsJsonSessionId_set_Md5Pwd(wsSessionId, env, allocate_axis2_char_t(m_md5Pwd));
	adb_WsJsonSessionId_set_SessionGuid(wsSessionId, env, allocate_axis2_char_t(m_sessionGuid));
	return wsSessionId;
}

void JsonEngineWrapperC::closeSession() {
	if ( m_sessionGuid.empty() ) return;

	this->InitCurrent();
	axutil_env_t* env = this->m_env;

	adb_closeJsonSession_t* closeSession = adb_closeJsonSession_create(env);
    adb_WsJsonSessionId_t* wsSessionId = create_adb_WsJsonSessionId_t();
	adb_closeJsonSession_set_SessionId(closeSession, env, wsSessionId);

	adb_closeJsonSessionResponse_t* response = axis2_stub_op_JsonEngineService_closeJsonSession(m_stub, env, closeSession);
	
	adb_closeJsonSession_free(closeSession, env); closeSession = 0;

	const string errHeader("Failed to close Json Engine session for GUID " + m_sessionGuid);
	throwOnError(env, errHeader);
	throwOnNullResponse(response, errHeader);

	adb_WsJsonError_t* wsError = adb_closeJsonSessionResponse_get_closeJsonSessionReturn(response, env);
	handle_json_error(wsError, errHeader);

	m_sessionGuid.clear();

	this->CleanCurrent();
}

adb_WsConversionParams_t* JsonEngineWrapperC::create_adb_WsConversionParams_t(const string& jsonFilePath, const string& xmlFilePath, long transformationId) const {
	axutil_env_t* env = this->m_env;
	adb_WsConversionParams_t* wsConversationParams = adb_WsConversionParams_create(env);

	adb_WsConversionParams_set_JsonFilePath(wsConversationParams, env, allocate_axis2_char_t(jsonFilePath));
	adb_WsConversionParams_set_XmlFilePath(wsConversationParams, env, allocate_axis2_char_t(xmlFilePath));
	adb_WsConversionParams_set_TransformationId(wsConversationParams, env, transformationId);
	return wsConversationParams;
}

void JsonEngineWrapperC::jsonToXml(const std::string& jsonFilePath, const std::string& xmlFilePath, long transformationId) {
	if ( this->m_sessionGuid.empty() ) {
		startNewSession();
	}

	this->InitCurrent();
	axutil_env_t* env = this->m_env;

	adb_jsonToXml_t* jsonToXml = adb_jsonToXml_create(env);

	adb_WsJsonSessionId_t* wsSessionId = create_adb_WsJsonSessionId_t();
	adb_jsonToXml_set_SessionId(jsonToXml, env, wsSessionId);

	adb_WsConversionParams_t* wsConversationParams = create_adb_WsConversionParams_t(jsonFilePath, xmlFilePath, transformationId);

	adb_jsonToXml_set_ConversionParams(jsonToXml, env, wsConversationParams);
	adb_jsonToXmlResponse_t* response = axis2_stub_op_JsonEngineService_jsonToXml(m_stub, env, jsonToXml);

	adb_jsonToXml_free(jsonToXml, env); jsonToXml = 0; wsSessionId = 0; wsConversationParams = 0;

	const string errHeader("Failed to convert JSON file to XML");
	throwOnError(env, errHeader);
	throwOnNullResponse(response, errHeader);

	adb_WsJsonError_t* wsError = adb_jsonToXmlResponse_get_jsonToXmlReturn(response, env);
	handle_json_error(wsError, errHeader);

	this->CleanCurrent();
}

void JsonEngineWrapperC::xmlToJson(const std::string& xmlFilePath, const std::string& jsonFilePath, long transformationId) {
	if ( this->m_sessionGuid.empty() ) {
		startNewSession();
	}

	this->InitCurrent();
	axutil_env_t* env = this->m_env;
	adb_xmlToJson_t* xmlToJson = adb_xmlToJson_create(env);

	adb_WsJsonSessionId_t* wsSessionId = create_adb_WsJsonSessionId_t();
	adb_xmlToJson_set_SessionId(xmlToJson, env, wsSessionId);

	adb_WsConversionParams_t* wsConversationParams = create_adb_WsConversionParams_t(jsonFilePath, xmlFilePath, transformationId);

	adb_xmlToJson_set_ConversionParams(xmlToJson, env, wsConversationParams);
	adb_xmlToJsonResponse_t* response = axis2_stub_op_JsonEngineService_xmlToJson(m_stub, env, xmlToJson);

	adb_xmlToJson_free(xmlToJson, env); xmlToJson = 0; wsSessionId = 0; wsConversationParams = 0;

	const string errHeader("Failed to convert XML file to JSON");
	throwOnError(env, errHeader);
	throwOnNullResponse(response, errHeader);

	adb_WsJsonError_t* wsError = adb_xmlToJsonResponse_get_xmlToJsonReturn(response, env);
	handle_json_error(wsError, errHeader);

	this->CleanCurrent();
}

