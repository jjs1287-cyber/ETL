/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	                                  
#include <konga/Utils/FileUtils.h>		              
#include <konga/Utils/Settings.h>
#include <konga/Utils/util.h>			                  

#include "CurlTransferLibWrapper.h"
#include "InetBase.h"

typedef std::vector<std::string> vstrings;

using namespace std;
using namespace konga;

namespace {
	const long lDefaultTimeOut = 3600;                            
	const long lDefaultRetryCount = 0;                             
	const long lDefaultRetryDelay = 300;                                                
}

InetBase::InetBase(void): m_pTransferLib(new CurlTransferLibWrapper), m_bVerifyPeer(false),
	m_lTimeOut_sec(lDefaultTimeOut), m_lMaxRetries(lDefaultRetryCount), m_lRetryDelay_sec(lDefaultRetryDelay),
	m_debug_file(0), m_lPort(0)
{
	string debugFile(konga::GetOptionAsString("Settings", "CurlDebugDir", ""));
	if ( !debugFile.empty() && IsDirectory(debugFile) ) {
		AppendPathSeparator(debugFile);
		debugFile += GetGUIDString() + ".txt";
		m_debug_file = fopen(debugFile.c_str(), "a");
		                   
		if ( m_debug_file ) m_pTransferLib->setDebugFile(m_debug_file);
	}
}

InetBase::~InetBase(void)
{	
	                                              
	if ( m_pTransferLib != 0 )
		delete m_pTransferLib;
	if ( m_debug_file ) {
		fclose(m_debug_file);
		m_debug_file = 0;
	}
}

long InetBase::DisConnect()
{
	return Reset();
}

long InetBase::Reset() {
	try {
		m_pTransferLib->reset();
		return Protocol::SUCCESS;
	}
	catch( const CrException& ex ) {
		AddError(ex.GetErrorString());
		m_lLastError = ex.GetErrorNumber();
		return Protocol::FAILURE;
	}
}

void InetBase::setPath(const string& stPath) {
	string stTmp(TrimString(stPath));
	if ( stTmp.empty() ) {
		m_stPath = '/';
		return;
	}

	if ( stTmp.at(0) == '\\') {
		stTmp[0] = '/';
	}
	else if ( stTmp.at(0) != '/' ) {
		stTmp.insert(stTmp.begin(), '/');
	}
	m_stPath = stTmp;
}

void InetBase::addRequestHeader(const string &stRequestHeader) {
	                                                     
	string::size_type iPos = stRequestHeader.find_last_not_of("\r\n");
	if ( iPos != string::npos && iPos != stRequestHeader.size()-1 ) {
		if ( iPos == 0 )
			return;                                                 
		m_vRequestHeaders.push_back(stRequestHeader.substr(0, iPos+1));
	}
	else {
		m_vRequestHeaders.push_back(stRequestHeader);
	}
}

void InetBase::setRequestHeaders(const string& stRequestHeaders) {
	konga::ParseRFC2822Headers(stRequestHeaders, m_vRequestHeaders);
}

void InetBase::clearRequestHeaders() {
	m_vRequestHeaders.clear();
}

long InetBase::GetAsFile(const string& stLocalFile)
{
	                                    
	boost::mutex::scoped_lock master_lock(m_MasterLock);
	m_pTransferLib->setResponseFile(stLocalFile);
	return Get();
}

long InetBase::GetAsString(string& stResult)
{
	                                    
	boost::mutex::scoped_lock master_lock(m_MasterLock);
	m_pTransferLib->setResponseString(stResult);
	return Get();
}

long InetBase::Get() {
	try {
		long retval = doPreTransfer();
		if ( retval != Protocol::SUCCESS )
			return retval;

		if ( (retval = doPreGet()) != Protocol::SUCCESS )
			return retval;

		m_pTransferLib->Get();
		doPostTransfer();

		if ( (retval=CheckHeadersForErrors()) != Protocol::SUCCESS )
			return retval;
		return Protocol::SUCCESS;
	} catch( const CrException& ex ) {
		AddError(ex.GetErrorString());
		string str;
		AppendRequestAndResponseHeadersToString(str);
		AddError(str);
		m_lLastError = ex.GetErrorNumber();
		return Protocol::FAILURE;
	}
}

long InetBase::PutAsFile(const string& stLocalFile) {
	                                    
	boost::mutex::scoped_lock master_lock(m_MasterLock);

	m_pTransferLib->setRequestFile(stLocalFile);
	return Put();
}

long InetBase::PutAsFileGetRespAsString(const string& stLocalFile, string& stResponse) {
	                                    
	boost::mutex::scoped_lock master_lock(m_MasterLock);

	m_pTransferLib->setRequestFile(stLocalFile);
	m_pTransferLib->setResponseString(stResponse);
	return Put();
}

long InetBase::PutAsFileGetRespAsFile(const string& stLocalFile, const string& stResponseFile) {
	                                    
	boost::mutex::scoped_lock master_lock(m_MasterLock);

	m_pTransferLib->setRequestFile(stLocalFile);
	m_pTransferLib->setResponseFile(stResponseFile);
	return Put();
}

long InetBase::PutAsString(const string& stLocalResource) {
	                                    
	boost::mutex::scoped_lock master_lock(m_MasterLock);

	m_pTransferLib->setRequestString(stLocalResource);
	return Put();
}

long InetBase::PutAsStringGetRespAsString(const string& stLocalResource, string& stResponse) {
	                                    
	boost::mutex::scoped_lock master_lock(m_MasterLock);

	m_pTransferLib->setRequestString(stLocalResource);
	m_pTransferLib->setResponseString(stResponse);
	return Put();
}

long InetBase::PutAsStringGetRespAsFile(const string& stLocalResource, const string& stResponseFile) {
	                                    
	boost::mutex::scoped_lock master_lock(m_MasterLock);

	m_pTransferLib->setRequestString(stLocalResource);
	m_pTransferLib->setResponseFile(stResponseFile);
	return Put();
}

long InetBase::Delete(const std::string& stFileName) {
	try {
		long retval = doPreTransfer();
		if ( retval != Protocol::SUCCESS )
			return retval;

		m_pTransferLib->Delete(stFileName);
		return doPostTransfer();
	}
	catch( const CrException& ex ) {
		AddError(ex.GetErrorString());
		string str;
		AppendRequestAndResponseHeadersToString(str);
		AddError(str);
		m_lLastError = ex.GetErrorNumber();
		return Protocol::FAILURE;
	}
}

long InetBase::Put() {
	try {
		long retval = doPreTransfer();
		if ( retval != Protocol::SUCCESS )
			return retval;

		if ( (retval=doPrePut()) != Protocol::SUCCESS )
			return retval;

		m_pTransferLib->Put();
		doPostTransfer();

		if ( (retval=CheckHeadersForErrors()) != Protocol::SUCCESS )
			return retval;
		return Protocol::SUCCESS;
	} catch( const CrException& ex ) {
		AddError(ex.GetErrorString());
		string str;
		AppendRequestAndResponseHeadersToString(str);
		AddError(str);
		m_lLastError = ex.GetErrorNumber();
		return Protocol::FAILURE;
	}
}

long InetBase::doPreTransfer() {
	try {
		long retval = 0;
		if ( (retval=ParseURL()) != Protocol::SUCCESS )
			return retval;

		m_pTransferLib->setUrl(m_stParsedUrl);
		m_pTransferLib->setTimeOut(m_lTimeOut_sec);

		const string& stUser = GetUserName();
		if ( !stUser.empty()) {
			m_pTransferLib->setUserAndPassword(stUser, GetPassword());
		}

		if ( isSslEnabled() ) {
			try {
				m_pTransferLib->setVerifyPeerCert(m_bVerifyPeer);
			}
			catch ( const CrException& ex ) {
				AddError(ex.GetErrorString());
				m_lLastError = ex.GetErrorNumber();
				return Protocol::FAILURE;
			}
		}

		return Protocol::SUCCESS;
	}
	catch( const CrException& ex ) {
		AddError(ex.GetErrorString());
		m_lLastError = ex.GetErrorNumber();
		return Protocol::FAILURE;
	}
}

long InetBase::doPreGet() {
	return Protocol::SUCCESS;
}

long InetBase::doPrePut() {
	return Protocol::SUCCESS;
}

long InetBase::doPostTransfer() {
	m_vResponseHeaders = m_pTransferLib->getResponseHeaders();
	return Protocol::SUCCESS;
}

long InetBase::TimeOutSec() const { return m_lTimeOut_sec; }

void InetBase::SetTimeOutSec(long lTimeOut_sec) {
	m_lTimeOut_sec = (lTimeOut_sec <= 0) ? 0 : lTimeOut_sec;
}

long InetBase::MaxRetries() const { return m_lMaxRetries; }

void InetBase::SetMaxRetries(long lMaxRetries) {
	m_lMaxRetries = (lMaxRetries <= 0) ? 0 : lMaxRetries;
}

long InetBase::RetryDelaySec() const { return m_lRetryDelay_sec; }
void InetBase::SetRetryDelaySec(long lRetryDelaySec) {
	m_lRetryDelay_sec = (lRetryDelaySec <= 0) ? 0 : lRetryDelaySec;
}

namespace {
	void AppendAllStringsInArray(const vector<string>& v, string& str) {
		for( vector<string>::const_iterator cit = v.begin();
			cit != v.end(); ++cit ) {
				str += "\n";
				str += *cit;
		}
	}
}

void InetBase::AppendRequestAndResponseHeadersToString(std::string& str) const {
	if ( !m_vRequestHeaders.empty() ) {
		if ( !str.empty() ) str += "\n";
		str += "Request headers:";
		AppendAllStringsInArray(m_vRequestHeaders, str);
	}

	if ( !m_vResponseHeaders.empty() ) {
		if ( !str.empty() ) str += "\n";
		str += "Response headers:";
		AppendAllStringsInArray(m_vResponseHeaders, str);
	}
}
