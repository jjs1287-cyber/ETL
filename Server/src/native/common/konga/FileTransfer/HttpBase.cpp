/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <sstream>		               
#include <algorithm>	            

#include <konga/Utils/StringUtils.h>                      
#include <konga/Utils/CrException.h>
#include <konga/Utils/Url.h>
#include <konga/Utils/PortDefs.h>	             
#include <konga/Utils/Settings.h>	                     

#include <konga/SysInfo/SysInfo.h>

#include "HttpBase.h"

using namespace konga;
using namespace std;

typedef vector<string> vstrings;

HttpBase::HttpBase(HttpProxyConfiguration* pProxyConfiguration) :
m_pProxyConfiguration(pProxyConfiguration),
m_bAppendToFile(false),
m_iHttpStatus(0),
m_HttpVerb(HttpGet),
m_iMaxRedirectsToFollow(0),
m_stUserAgent("ETLForge Server/4.1 (Language=C++; Platform=" + GetOsName() + ")") {
	                           
	m_httpOptions.enableChunkedTransferEncoding( konga::GetOptionAsBoolean("Settings", "EnableHttpChunkedTransferEncoding", false) );
	m_httpOptions.enableContentEncoding( !konga::GetOptionAsBoolean("Settings", "DisableHttpContentEncoding", true) );
	m_httpOptions.enableExpect100ContinueHeader( konga::GetOptionAsBoolean("Settings", "EnableHttpExpect100Continue", false) );
}

HttpBase::~HttpBase() { }

char* HttpBase::GetHttpStatus(unsigned int Code)
{
	switch (Code)
	{
	case 400: return "The request could not be processed by the server due to invalid syntax.";
	case 401: return "The requested resource requires user authentication.";
	case 402: return "Not currently implemented in the HTTP protocol.";
	case 403: return "The server understood the request, but is refusing to fulfill it.";
	case 404: return "The server has not found anything matching the requested URI.";
	case 405: return "The method used is not allowed.";
	case 406: return "No responses acceptable to the client were found.";
	case 407: return "Proxy authentication required.";
	case 408: return "The server timed out waiting for the request.";
	case 409: return "The request could not be completed due to a conflict with the current state of the resource. The user should resubmit with more information.";
	case 410: return "The requested resource is no longer available at the server, and no forwarding address is known.";
	case 411: return "The server refuses to accept the request without a defined content length.";
	case 412: return "The precondition given in one or more of the request header fields evaluated to false when it was tested on the server.";
	case 413: return "The server is refusing to process a request because the request entity is larger than the server is willing or able to process.";
	case 414: return "The server is refusing to service the request because the request URI (Uniform Resource Identifier) is longer than the server is willing to interpret.";
	case 415: return "The server is refusing to service the request because the entity of the request is in a format not supported by the requested resource for the requested method.";
	case 449: return "The request should be retried after doing the appropriate action.";
	case 500: return "The server encountered an unexpected condition that prevented it from fulfilling the request.";
	case 501: return "The server does not support the functionality required to fulfill the request.";
	case 502: return "The server, while acting as a gateway or proxy, received an invalid response from the upstream server it accessed in attempting to fulfill the request.";
	case 503: return "The service is temporarily overloaded.";
	case 504: return "The request was timed out waiting for a gateway.";
	case 505: return "The server does not support, or refuses to support, the HTTP protocol version that was used in the request message.";
	default:  return "Unknown Response Code";
	}
}

void HttpBase::useRFC1867MultipartFileUpload(const string& stName, const string& stFileName, const std::string& stContentType) {
	m_pTransferLib->rfc1867FileUpload(stName, stFileName, stContentType);
}

  
                                                                                  
 
                       
                                                                  
                                                           
                                                                
                          
                                                                                  
                                                                                                  
                                    
                                                                                             
                                                            
                       
 

                                       
 
                               
 
  

namespace {
	bool isErrorStatusCode(int httpStatus) {
		return httpStatus >= 400;
	}

	bool shouldHaveLocation(int httpStatus) {
		switch(httpStatus) {
			case 301:
			case 302:
			case 303:
			case 305:
			case 307:
				return true;
			default:
				return false;
		}
	}
}

long HttpBase::CheckHeadersForErrors() {
	vstrings headers = GetResponseHeaders();

	                                                        
	                                                       
	                                   
	                                                                        
	                                             
	int httpStatus = -1;
	string stAllHttpErrors;
	string stLastHttpError;
	string stAllHeaders;
	string stLocationHeader;
	bool bShouldHaveLocation = false;
	vector<int> reportedErrorCodes;
	for( vstrings::const_iterator it = headers.begin(); it != headers.end(); ++ it ) {
		string header(*it);
		stAllHeaders += header;
		string tok = konga::strtok(header, ' ', '\\');
		if ( strcasecmp(tok.c_str(), "HTTP/1.1") == 0 || strcasecmp(tok.c_str(), "HTTP/1.0") == 0 ) {
			tok = konga::strtok(header, ' ', '\\');
			httpStatus = atoi(tok.c_str());
			                                                          
			if ( isErrorStatusCode(httpStatus) &&
				std::find(reportedErrorCodes.begin(), reportedErrorCodes.end(), httpStatus) == reportedErrorCodes.end() )
			{
				if ( !stLastHttpError.empty() ) {
					stAllHttpErrors += "\n";
					stAllHttpErrors += stLastHttpError;
				}
				stLastHttpError = "The server returned HTTP Status Code : " + toString(httpStatus) +  " " + header;
				stLastHttpError += "\nError is: " + string(GetHttpStatus(httpStatus));

				reportedErrorCodes.push_back(httpStatus);
			}
			else if ( !bShouldHaveLocation && shouldHaveLocation(httpStatus) ) {
				bShouldHaveLocation = true;
			}
		}
		else if ( bShouldHaveLocation ) {
			header = *it;
			tok = konga::strtok(header, ':', '\\');
			if ( strcasecmp(tok.c_str(), "Location") == 0 ) {
				stLocationHeader = konga::TrimCharsFromString(header, " \t\r\n");
			}
		}
	}

	                                    
	if ( httpStatus == -1 ) {
		AddError("The server did not return a valid HTTP status code.");
		return -1;
	}
	else if ( isErrorStatusCode(httpStatus) ) {
		if ( !stLastHttpError.empty() ) {
			AddError("The last (and probably most relevant) error was:\n" + stLastHttpError);
		}
		if ( !stAllHttpErrors.empty() ) {
			AddError("Other errors (authentication errors here may be part of the normal http authentication process):\n" + stAllHttpErrors);
		}

		if ( !stAllHeaders.empty() )
			AddError("Headers sent by the server:\n" + stAllHeaders);
		else
			AddError("No HTTP headers were returned.");
		return (m_iHttpStatus = httpStatus);
	}
	else if ( shouldHaveLocation(httpStatus) ) {
		switch(httpStatus) {
			case 301:
				AddError("This resource has moved permanently.");
				if ( stLocationHeader.empty() ) {
					AddError("No new location was specified.");
				}
				else {
					AddError("You should use this URL instead: " + stLocationHeader);
				}
				return (m_iHttpStatus = httpStatus);
				break;
			case 302:
			case 303:
			case 307:
				AddError("This resource has been moved temporarily. New location:" + stLocationHeader);
				AddError("To enable automatic redirect for web service calls, set the global data element $etlforge.web_service_call.max_redirs to a positive integer value.");
				return (m_iHttpStatus = httpStatus);
				break;
			case 305:
				AddError("This resource requires a proxy, please configure ETLForge to use a proxy server.");
				if ( stLocationHeader.empty() ) {
					AddError("No proxy location was specified, please contact the server administrator as this is most likely a server side error.");
				}
				else {
					AddError("Proxy location: " + stLocationHeader);
				}
				return (m_iHttpStatus = httpStatus);
				break;
		}
	}
	return 0;
}

long HttpBase::ParseURL() {
	string stUrl;
	if ( !UsingUrl()) {
		stringstream stream;
		stream << (isHttps() ? "https" : "http");
		stream << "://" << getHost();
		long lPort = getPort();
		if ( lPort > 0 )
			stream << ':' << lPort;
		stream << getPath();
		stUrl = stream.str();
	}
	else
		stUrl = GetUrl();

	try {
		Url url;
		url.Parse(stUrl);

		string stProtocol = url.getProtocol();
		if ( strcasecmp(stProtocol.c_str(), "http") != 0 && strcasecmp(stProtocol.c_str(), "https") != 0 ) {
			AddError("The protocol of the URL is not http/https.");
			return -1;				
		}

		                                                                                      
		setHost(url.getHost());
		setPort(url.getPort());
		setPath(url.getPath());

		m_stParsedUrl = url.getParsedUrl();
	}
	catch ( const CrException& ex ) {
		AddError(ex.GetErrorString());
		return -1;
	}
#ifndef _DEBUG
	catch( ... ) {
		AddError("Unexpected exception while parsing URL " + stUrl);
		return -1;
	}
#endif
	return 0;
}

long HttpBase::doPreTransfer() {
	long l = InetBase::doPreTransfer();
	if ( l != Protocol::SUCCESS )
		return l;

	                                                               
	try {
		m_pTransferLib->setUserAgent(m_stUserAgent);

		                                                                                            
		const vector<string> custom_headers = m_httpOptions.getCustomRequestHeaders();
		for( vector<string>::const_iterator cit = custom_headers.begin(); cit != custom_headers.end(); ++cit ) {
			this->addRequestHeader(*cit);
		}

		for ( vector<string>::const_iterator it = m_vRequestHeaders.begin();
			  it != m_vRequestHeaders.end(); ++it ) {
			m_pTransferLib->addHttpRequestHeader(*it);
		}

		if ( !m_stInputCookieFileName.empty() ) {
			m_pTransferLib->setInputCookieFileName(m_stInputCookieFileName);
		}
		if ( !m_stOutputCookieFileName.empty() ) {
			m_pTransferLib->setOutputCookieFileName(m_stOutputCookieFileName);
		}

		                                            
		if ( m_httpOptions.isContentEncodingEnabled() ) m_pTransferLib->requestAllSupportedEncodings();
		m_pTransferLib->enableExpect100Continue(m_httpOptions.isExpect100ContinueHeaderEnabled());
		m_pTransferLib->enableHttpChunkedTransferEncoding(m_httpOptions.isChunkedTransferEncodingEnabled());

		string sContentType;
		if ( m_httpOptions.isContentTypeSet(sContentType) ) {
			m_pTransferLib->overrideContentType(sContentType);
		}

		if ( m_pProxyConfiguration != 0 ) {
			string sProxyHostName, sProxyUserName, sProxyPassword;
			long lProxyPort = -1;
			if ( m_pProxyConfiguration->isProxyConfiguredForHost(getHost(), sProxyHostName, lProxyPort, sProxyUserName, sProxyPassword) ) {
				m_pTransferLib->setProxySettings(sProxyHostName, lProxyPort, sProxyUserName, sProxyPassword);
			}
		}

		if ( m_iMaxRedirectsToFollow != 0 ) {
			m_pTransferLib->followRedirects(m_iMaxRedirectsToFollow);
		}
	}
	catch ( const CrException& ex ) {
		AddError(ex.GetErrorString());
		m_lLastError = ex.GetErrorNumber();
		return Protocol::FAILURE;
	}
	return Protocol::SUCCESS;
}

long HttpBase::doPreGet() {
	long l = InetBase::doPreGet();
	if ( l != Protocol::SUCCESS )
		return l;

	const konga::HttpVerb verb = GetHttpVerb();
	switch(verb) {
		case HttpGet:
			                                                
			break;
		case HttpPost:
			m_pTransferLib->isHttpPost(true);
			break;
		case HttpHead:
			m_pTransferLib->setNoBody(true);
			break;
		default:
			                                         
			m_pTransferLib->setExplicitHttpVerb(toString(verb));
			break;
	};

	return Protocol::SUCCESS;
}

long HttpBase::doPrePut() {
	long l = InetBase::doPrePut();
	if ( l != Protocol::SUCCESS )
		return l;

	const konga::HttpVerb verb = GetHttpVerb();
	switch(verb) {
		case HttpPut:
			                                                
			break;
		case HttpPost:
			m_pTransferLib->isHttpPost(true);
			break;
		case HttpHead:
			m_pTransferLib->setNoBody(true);
			break;
		default:
			                                         
			m_pTransferLib->setExplicitHttpVerb(toString(verb));
			break;
	};

	return Protocol::SUCCESS;
}
