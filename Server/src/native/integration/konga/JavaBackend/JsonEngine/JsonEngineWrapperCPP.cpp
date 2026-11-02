/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>

#include <konga/Utils/CrException.h>

#define USE_AXIS_CPP 1
#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>
#include <konga/JavaBackend/JavaBackendConfiguration/ServiceNames.h>

#include "JsonEngineWrapperCPP.h"
#include <konga/NativeJson/NativeJsonBackend.h>
#include <konga/Utils/Settings.h>

using namespace konga;

JsonEngineWrapper* JsonEngineWrapper::createJsonEngineWrapper(DbConnection& tranDbConn, const UserInfo& userInfo) {
	const string backend=GetOptionAsString("JsonEngine","Backend","Native");
	if(backend=="Native")return createNativeJsonBackend(tranDbConn,userInfo);
	if(backend!="Java")throw CrException("Unknown JsonEngine Backend. Use Native or Java.");
	string userName, md5pwd;
	konga::GetCredentials(tranDbConn, userInfo, userName, md5pwd);
	return new JsonEngineWrapperCPP(userName, md5pwd);
}

JsonEngineWrapperCPP::JsonEngineWrapperCPP(const string& userName, const string& md5pwd)
: m_wsImpl( new JsonEngine() ), m_stUserName(userName), m_stMD5Password(md5pwd) {
	konga::ConfigureStub(*m_wsImpl, JSON_ENGINE);
}

JsonEngineWrapperCPP::~JsonEngineWrapperCPP(void) {
	if ( m_wsImpl ) {
		this->closeSession();
		delete m_wsImpl;
	}
}

namespace {
	void handleJsonError(WsJsonError* error, const string& header = "") {
		if ( error && error->getErrorCode() != 0 ) {
			throw CrException(header + string(error->getError() ? error->getError() : "No error reported. Error code: " + konga::toString(error->getErrorCode())));
		}
	}
}

void JsonEngineWrapperCPP::startNewSession() {
	closeSession();
	std::auto_ptr<WsStartNewJsonSessionResult> result (
		m_wsImpl->startNewJsonSession(const_cast<xsd__string>(m_stUserName.c_str()), const_cast<xsd__string>(m_stMD5Password.c_str()))
		);
	WsJsonError* error = result->getError();
	handleJsonError(error, "Failed to start new JsonEngine session: ");
	m_stSessionId = result->getSessionId() ? result->getSessionId() : "";
	if ( m_stSessionId.empty() ) {
		throw InternalErrorException("JsonEngine::startNewSession reported no error but the session is empty.");
	}
}

WsJsonSessionId* JsonEngineWrapperCPP::createWsJsonSessionId() {
	std::auto_ptr<WsJsonSessionId> wsSessionId( new WsJsonSessionId() );
	wsSessionId->setETLForgeUser(const_cast<xsd__string>(m_stUserName.c_str()), true);
	wsSessionId->setMd5Pwd(const_cast<xsd__string>(m_stMD5Password.c_str()), true);
	wsSessionId->setSessionGuid(const_cast<xsd__string>(m_stSessionId.c_str()), true);
	return wsSessionId.release();
}

void JsonEngineWrapperCPP::closeSession() {
	if ( m_stSessionId.empty() ) return;

	std::auto_ptr<WsJsonSessionId> wsSessionId( createWsJsonSessionId() );
	try {
		std::auto_ptr<WsJsonError> error( m_wsImpl->closeJsonSession(wsSessionId.get()) );
	}
	catch( const std::exception& ex ) {
		m_stSessionId.clear();
		printf("std::exception: %s", ex.what());
	}
	catch(...) {
		m_stSessionId.clear();
	}

	m_stSessionId.clear();
}

namespace {
	WsConversionParams* createWsConversionParams(const std::string& jsonFilePath, const std::string& xmlFilePath, long transformationId) {
		std::auto_ptr<WsConversionParams> wsParams( new WsConversionParams() );
		wsParams->setJsonFilePath(const_cast<xsd__string>(jsonFilePath.c_str()), true);
		wsParams->setXmlFilePath(const_cast<xsd__string>(xmlFilePath.c_str()), true);
		wsParams->setTransformationId(transformationId);
		return wsParams.release();
	}
}

void JsonEngineWrapperCPP::jsonToXml(const std::string& jsonFilePath, const std::string& xmlFilePath, long transformationId) {
	if ( m_stSessionId.empty() ) startNewSession();
	std::auto_ptr<WsJsonSessionId> wsSessionId( createWsJsonSessionId() );
	std::auto_ptr<WsConversionParams> wsParams( createWsConversionParams(jsonFilePath, xmlFilePath, transformationId) );
	std::auto_ptr<WsJsonError> error( m_wsImpl->jsonToXml(wsSessionId.get(), wsParams.get()) );
	handleJsonError(error.get());
}

void JsonEngineWrapperCPP::xmlToJson(const std::string& xmlFilePath, const std::string& jsonFilePath, long transformationId) {
	if ( m_stSessionId.empty() ) startNewSession();
	std::auto_ptr<WsJsonSessionId> wsSessionId( createWsJsonSessionId() );
	std::auto_ptr<WsConversionParams> wsParams( createWsConversionParams(jsonFilePath, xmlFilePath, transformationId) );
	std::auto_ptr<WsJsonError> error( m_wsImpl->xmlToJson(wsSessionId.get(), wsParams.get()) );
	handleJsonError(error.get());
}

