/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <iostream>
#include <cassert>

#include <konga/Utils/util.h>			                  
#include <konga/Utils/CrException.h>
#include <konga/Utils/AutoFileCleanup.h>                     
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>		                 

#include "DefaultOperationContext.h"
#include <konga/FileCleanup/OperationTempRetention.h>

using namespace konga;
using std::string;
using std::vector;

namespace {
	                    
	const string stOperationEngineCfgSection("OperationEngine");
	const string stDebugOption("Debug");
}

DefaultOperationContext::DefaultOperationContext(const UserInfo& currentUser,
												 Debuggee* pDebuggee,
												 long lOpId,
												 const string& sOpName,
												 const string& sOpGuid) :
	m_bOperationChainCancelled(false),
	m_bDebug(false),
	m_lOperationId(lOpId),
	m_llOperationInstanceId(-1),	                               
	m_CurrentUser(currentUser),
	m_stOperationName(sOpName),
	m_stOperationGuid(sOpGuid),
	m_pAutoCleanupTempDir(0),
	m_bDeleteLocalTmpDir(true),
	m_bCompletedSuccessfully(false),
	m_pDebuggee(pDebuggee),
	m_pDbConn(0)
{
}

DefaultOperationContext::~DefaultOperationContext(void) {
	if (m_bCompletedSuccessfully) {
		for (size_t i = 0; i < m_temporaryLogFiles.size(); ++i) {
			try {
				if (!etlforge::cleanupDeleteFile(etlforge::cleanupWidePath(m_temporaryLogFiles[i])))
					std::cerr << "ETLForge successful-operation log cleanup failed: " << m_temporaryLogFiles[i] << std::endl;
			}
			catch (const std::exception& ex) {
				std::cerr << "ETLForge successful-operation log cleanup failed: " << ex.what() << std::endl;
			}
		}
	}
	CleanupTmpDir();
#ifdef _DEBUG
	assert(m_ErrorReporters.empty());	                
#endif
}

void DefaultOperationContext::setLocalTmpDirForOperationDeleteFlag(bool bDeleteIt) {
	m_bDeleteLocalTmpDir = bDeleteIt;
}

void DefaultOperationContext::registerTemporaryLogFile(const string& path) {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	if (!path.empty()) m_temporaryLogFiles.push_back(path);
}

void DefaultOperationContext::completeSuccessfulOperation() {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	m_bCompletedSuccessfully = true;
	m_bDeleteLocalTmpDir = true;
}

string DefaultOperationContext::getLocalTmpDirForOperation() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	if ( m_stLocalTmpDir.empty() ) {

		                                                                             
		                                                             
		                                                                           
		                                     
		string stTmpDir(getTmpDir());
		AppendPathSeparator(stTmpDir);
		stTmpDir += "OpId_" + toString(getOperationId()) + "_" + GetGUIDString();
		AppendPathSeparator(stTmpDir);

		                                  
		try {
			kCreateDirectory(stTmpDir);
		}
		catch( const CrException& ex ) {
			string stErr("Failed to create temporary directory for operation: " + stTmpDir);
			stErr += "\nThe reported error was:\n";
			stErr += ex.GetErrorString();
			throw CrException(stErr, getOperationId());
		}

		m_stLocalTmpDir = stTmpDir;
		{
			CleanupTmpDir();
			m_pAutoCleanupTempDir = new AutoCleanupTempDir(m_stLocalTmpDir);
		}
	}
	return string(m_stLocalTmpDir);
}

void DefaultOperationContext::CleanupTmpDir() const {
	if ( m_pAutoCleanupTempDir != 0 ) {
		try {
			m_pAutoCleanupTempDir->SetAutoDelete(false);
			delete m_pAutoCleanupTempDir;
			m_pAutoCleanupTempDir = 0;
			if (m_bDeleteLocalTmpDir && !etlforge::cleanupOwnedOperationDirectory(etlforge::cleanupWidePath(m_stLocalTmpDir)))
				std::cerr << "ETLForge operation temporary-directory cleanup failed: " << m_stLocalTmpDir << std::endl;
		}
		catch( const CrException& ex ) {
			std::cerr << "Failed to delete temporary directory for operation: " << ex.GetErrorString() << std::endl;
		}
#ifndef _DEBUG
		catch(...) {
			                                                                
			std::cerr << "Failed to delete temporary directory for operation: Unknown exception " << std::endl;
		}
#endif
	}
}

void DefaultOperationContext::setErrorReporter(ErrorReporter* pErrorReporter) {
	if ( pErrorReporter != 0 ) {
		if( (m_ErrorReporters.empty() || m_ErrorReporters.top() != pErrorReporter) ) {
			m_ErrorReporters.push(pErrorReporter);
		}
	}
	else {
#ifdef _DEBUG
		assert(!m_ErrorReporters.empty());
#endif
		if ( m_ErrorReporters.empty() ) return;
		m_ErrorReporters.pop();
	}
}

const ErrorReporter* DefaultOperationContext::getErrorReporter() const {
#ifdef _DEBUG
	assert( !m_ErrorReporters.empty() );
#endif
	if ( m_ErrorReporters.empty() ) return 0;
	return &*m_ErrorReporters.top();
}

string DefaultOperationContext::getOperationName() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	return string(m_stOperationName);
}

void DefaultOperationContext::setOperationName(const string& stOperationName) {
	m_stOperationName = stOperationName;
}

long DefaultOperationContext::getOperationId() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	return m_lOperationId;
}

long long DefaultOperationContext::getOperationInstanceId() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	return m_llOperationInstanceId;
}

void DefaultOperationContext::setOperationInstanceId(long long llOperationInstanceId) {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	m_llOperationInstanceId = llOperationInstanceId;
}

string DefaultOperationContext::getOperationGuid() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	return string(m_stOperationGuid);
}

void DefaultOperationContext::SetOperationGuid(const string& stOperationGuid) {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	m_stOperationGuid = stOperationGuid;
}

UserInfo DefaultOperationContext::getCurrentUser() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	return m_CurrentUser;
}

DbConnection* DefaultOperationContext::getTranDbConnection() const {
	if ( m_pDbConn == 0 ) throw InternalErrorException("No TranDb connection has been set. DefaultOperationContext::getTranDbConnection() was called without a TranDb being set. This is a bug!");
	return m_pDbConn;
}

void DefaultOperationContext::CancelOperationChain(const string& stMsg) {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	m_bOperationChainCancelled = true;
	m_stOperationChainCancelMsg = stMsg;
}

bool DefaultOperationContext::isDebugEnabled() const {
	return m_bDebug;
}

string DefaultOperationContext::getOperationChainInstanceGuid() {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	if( m_stOperationChainInstanceId.empty() ) {
		konga::GetGUIDString(m_stOperationChainInstanceId);
	}
	return m_stOperationChainInstanceId;
}

void DefaultOperationContext::setOperationChainInstanceGuid(const std::string& guid) {
	m_stOperationChainInstanceId = guid;
}

void DefaultOperationContext::setDebugFlag(bool flag) {
	m_bDebug = flag;
}

bool DefaultOperationContext::wasOperationChainCancelled(string& stMsg) {
	if ( m_bOperationChainCancelled ) {
		stMsg = m_stOperationChainCancelMsg;
		return true;
	}
	return false;
}

void DefaultOperationContext::setCurrentUser(const UserInfo& userInfo) {
	m_CurrentUser = userInfo;
}

Debuggee* DefaultOperationContext::getDebuggee() const {
	return m_pDebuggee;
}
