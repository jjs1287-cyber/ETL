/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>
#include <iostream>
#include <konga/Utils/PortDefs.h>	              
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>

#include <konga/FileTransfer/Http.h>
#include <konga/FileTransfer/Https.h>

#include <konga/Server/ServerHttpProxyConfiguration.h>

#include <konga/TransformationUtils/TranDBUtils.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include "OperationEngineException.h"

#include "HttpLocation.h"

using namespace std;
using namespace konga;

HttpLocation::HttpLocation(void) : m_pProxyConfigCache(0) {}

HttpLocation::~HttpLocation(void) {
	if ( m_pProxyConfigCache ) {
		delete m_pProxyConfigCache;
		m_pProxyConfigCache = 0;
	}
}

std::vector<std::string> HttpLocation::getFileListImpl() const {
	auto_ptr<HttpBase> pHttp( getHttpForLocation() );
	pHttp->SetHttpVerb(HttpHead);

	                                                                                                                      
	pHttp->SetTimeOutSec(600);

	string stResponse;
	if ( pHttp->GetAsString(stResponse) != Protocol::SUCCESS ) {
		                                           
		string stErr("Could not connect to url '" + getHttpUrl());
		stErr += "' . " + pHttp->GetLastError();
		throw OperationEngineException(stErr);
	}

	vector<string> vFileList;
	vFileList.push_back(pHttp->getPath());

	return vFileList;
}

HttpProxyConfiguration* HttpLocation::getProxyConfigCache() const {
	if ( m_pProxyConfigCache )
		return m_pProxyConfigCache;

	return ( m_pProxyConfigCache = konga::GetHttpProxyConfiguration() );
}

std::string HttpLocation::getHttpUrl() const {
	string stHttpUrl = replace_de( m_CommonData.m_stHttpUrl);
	string stHttpCol = stHttpUrl.substr(0,4);
	if (EqualsIgnoreCase(stHttpCol,"http"))
		return stHttpUrl;
	else
		return ("http://" + stHttpUrl);	
}

string HttpLocation::getLogin() const {
	return replace_de( m_CommonData.m_stLogin );
}

HttpVerb HttpLocation::getHttpVerb() const {
	return static_cast<HttpVerb>(m_CommonData.m_iHttpVerb);
}

string HttpLocation::getFileName() const {
	const string& stUrl = getHttpUrl();

	string::size_type iPos = stUrl.find_last_of('/');
	if ( iPos == string::npos ) 
		throw OperationEngineException("The URL \"" + stUrl + "\" does not appear to be valid. It contains no /.");

	if ( iPos == stUrl.length()-1 )
		return "";
	else
		return stUrl.substr(iPos+1);
}

HttpBase* HttpLocation::getHttpForLocation() const {
	                                                                                                                              
	const string& stUrl = getHttpUrl();
	if ( stUrl.length() < 9 ) {
		throw OperationEngineException("The url " + stUrl + " is not valid.");
	}

	auto_ptr<HttpBase> pHttpBase;
	if ( strncasecmp(stUrl.c_str(), "https://", 8) == 0  ) {
		auto_ptr<Https> pHttps(new Https(getProxyConfigCache()));
		                                                    
		pHttpBase = pHttps;
	}
	else {
		auto_ptr<Http> pHttp(new Http(getProxyConfigCache()));
		pHttpBase = pHttp;
	}

	                         
	pHttpBase->SetUrl(stUrl);
	pHttpBase->SetHttpOptions(konga::GetRuntimeHttpOptions(m_extendedProperties, *m_pDataElementHolder));
	pHttpBase->SetTimeOutSec(getTransferTimeOutSeconds());

	const string& stLogin = getLogin();
	if ( !stLogin.empty() ) {
		pHttpBase->SetUserName(stLogin);
		pHttpBase->SetPassword( getDecryptedPassword() );
	}
	return pHttpBase.release();
}

namespace {
	const size_t MAX_RESPONSE_SIZE = 2048;
}

void HttpLocation::appendPartOfErrorResponse(const std::string& stResponseFileName, std::string& stErr) {
	if ( NonEmptyFileExists(stResponseFileName) ) {
		stErr += "\nThe response was:\n";
		FILE* f = fopen(stResponseFileName.c_str(), "rb");
		size_t n = 0;
		if ( f != 0 ) {
			char* buf = new char[MAX_RESPONSE_SIZE+1];
			n = fread(buf, 1, MAX_RESPONSE_SIZE, f);
			buf[n]=0;
			stErr.append(buf);
			delete[] buf;
			fclose(f);
			f = 0;
		}

		if ( n == MAX_RESPONSE_SIZE ) {
			stErr += "\n... omitting the rest of the response.";
		}
	}
}
