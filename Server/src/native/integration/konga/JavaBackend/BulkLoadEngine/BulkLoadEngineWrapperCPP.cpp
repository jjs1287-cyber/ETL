/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>
#include <memory>
#include <cassert>

#include <konga/Utils/CrException.h>

#define USE_AXIS_CPP 1
#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>
#include <konga/JavaBackend/JavaBackendConfiguration/ServiceNames.h>

#include "BulkLoadEngineWrapperCPP.h"

using namespace konga;
using std::string;

BulkLoadEngineWrapper* BulkLoadEngineWrapper::createBulkLoadEngineWrapper(DbConnection& tranDbConn, const UserInfo& userInfo) {
	string userName, md5pwd;
	konga::GetCredentials(tranDbConn, userInfo, userName, md5pwd);

	return new BulkLoadEngineWrapperCPP(userName, md5pwd);
}

BulkLoadEngineWrapperCPP::BulkLoadEngineWrapperCPP(const string& stUserName, const string& stMD5Password) :
	m_wsImpl(new BulkLoadEngine()), m_stUserName(stUserName), m_stMD5Password(stMD5Password) {

	konga::ConfigureStub(*m_wsImpl, BULK_LOAD_ENGINE);
}

BulkLoadEngineWrapperCPP::~BulkLoadEngineWrapperCPP(void) {
	if ( m_wsImpl ) delete m_wsImpl;
}

string BulkLoadEngineWrapperCPP::getWarnings() const {
	return m_stWarnings;
}

void BulkLoadEngineWrapperCPP::startNewSession() {
	assert(m_wsImpl);
	std::auto_ptr<WsStartNewBulkLoadSessionResult> result (
		m_wsImpl->startNewBulkLoadSession(const_cast<xsd__string>(m_stUserName.c_str()), const_cast<xsd__string>(m_stMD5Password.c_str()))
		);

	WsBulkLoadError* error = result->getreturnValue();
	const int error_code = error->geterrorCode();
	if ( error_code != 0 ) {
		throw CrException(string("Failed to start new bulk load session: ") + error->geterror());
	}
	if ( error->getwarnings() ) m_stWarnings = error->getwarnings();

	m_stSessionId = result->getsessionId();
}

void BulkLoadEngineWrapperCPP::closeSession() {
	assert(m_wsImpl);

	std::auto_ptr<WsBulkLoadSessionId> sessionId( new WsBulkLoadSessionId() );
	configureWsBulkLoadSessionId(*sessionId);
	std::auto_ptr<WsBulkLoadError> error(
		m_wsImpl->closeBulkLoadSession(sessionId.get())
		);
	m_stSessionId.clear();	                     

	const int error_code = error->geterrorCode();
	if ( error_code != 0 ) {
		throw CrException(string("Failed to close bulk load session: ") + error->geterror());
	}
	if ( error->getwarnings() ) m_stWarnings = error->getwarnings();
}

void BulkLoadEngineWrapperCPP::configureWsBulkLoadSessionId(WsBulkLoadSessionId& sessionId) {
	sessionId.setetlforgeUser(const_cast<xsd__string>(m_stUserName.c_str()), true);
	sessionId.setmd5Pwd(const_cast<xsd__string>(m_stMD5Password.c_str()), true);
	sessionId.setsessionGuid(const_cast<xsd__string>(m_stSessionId.c_str()), true);
}

BulkLoadResult BulkLoadEngineWrapperCPP::runBulkLoad(const string& stSFDCSessionId, const string& stSFDCUrl,
										long long sfdcBulkLoadId, long long operationInstanceId, long long lastOperationInstanceId,
										const string& stSourceFile) {
	assert(m_wsImpl);

	std::auto_ptr<WsBulkLoadSessionId> sessionId( new WsBulkLoadSessionId() );
	configureWsBulkLoadSessionId(*sessionId);

	std::auto_ptr<WsBulkLoadParams> bulkLoadParams( new WsBulkLoadParams() );
	bulkLoadParams->setbulkLoadId(sfdcBulkLoadId);
	bulkLoadParams->setoperationInstanceId(operationInstanceId);
	bulkLoadParams->setsourceFilePath(const_cast<xsd__string>(stSourceFile.c_str()), true);
	bulkLoadParams->setlastOperationInstanceId(lastOperationInstanceId);

	std::auto_ptr<WsSalesforceEndpoint> salesforceEndPoint( new WsSalesforceEndpoint() );
	salesforceEndPoint->setsessionId(const_cast<xsd__string>(stSFDCSessionId.c_str()), true);
	salesforceEndPoint->seturl(const_cast<xsd__string>(stSFDCUrl.c_str()), true);

	                                           
	std::auto_ptr<WsKeyValue_Array> key_values( new WsKeyValue_Array() );

	std::auto_ptr<WsBulkLoadResult> result (
		m_wsImpl->runBulkLoad(sessionId.get(), bulkLoadParams.get(), salesforceEndPoint.get(), key_values.get())
		);

	WsBulkLoadError* error = result->geterror();
	const int long errorCode = error->geterrorCode();
	if ( errorCode != 0 ) {
		throw CrException( string("Bulk load failed: ") + error->geterror() );
	}

	if ( error->getwarnings() ) m_stWarnings = error->getwarnings();
	BulkLoadResult retVal;
	retVal.warnings = m_stWarnings;
	retVal.allBatchesSent = ( result->getallBatchesWereSent() == true_ );

	if ( result->getlogFile() ) {
		retVal.logFile = result->getlogFile();
	}

	return retVal;
}

