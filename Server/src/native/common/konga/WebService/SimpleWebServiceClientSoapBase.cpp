/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <memory>	           

#include <konga/Utils/ProcessUtils.h>	               

#include <konga/FileTransfer/Http.h>
#include <konga/FileTransfer/Https.h>

#include <konga/Soap/SoapEnvelope.h>
#include <konga/Soap/SoapFault.h>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

#include "RFC822HeadersWithParameters.h"
#include "SimpleWebServiceClientSoapBase.h"

using namespace konga;
using std::string;
using std::vector;
using std::auto_ptr;

SimpleWebServiceClientSoapBase::SimpleWebServiceClientSoapBase(void) :
	BR("\r\n"), m_iSoapVersionMajor(1), m_iSoapVersionMinor(1), m_lLastErrorNumber(0), m_lMaxSizeBytes(0), m_iMaxRedirs(0), m_iTimeOutSeconds(3600) {
}

SimpleWebServiceClientSoapBase::~SimpleWebServiceClientSoapBase(void) {
	clearErrors();
}

                 
  
void SimpleWebServiceClientSoapBase::setMajorSoapVersion(int iMajor) {
	                                                                                  
	m_iSoapVersionMajor = 1;
}

void SimpleWebServiceClientSoapBase::setMinorSoapVersion(int iMinor) {
	                                             
	if ( iMinor >=1 && iMinor <= 2 )
		m_iSoapVersionMinor = iMinor;
}

void SimpleWebServiceClientSoapBase::setUrl(const std::string& stUrl) {
	m_stUrl = TrimString(stUrl);
}

void SimpleWebServiceClientSoapBase::setUserName(const std::string& stUserName)
{
	m_stUserName = stUserName;
}

void SimpleWebServiceClientSoapBase::setPassword(const std::string& stPassword) {
	m_stPassword = stPassword;
}

void SimpleWebServiceClientSoapBase::setSoapAction(const std::string& stSoapAction) {
	m_stSoapAction = stSoapAction;
}

std::string SimpleWebServiceClientSoapBase::getSoapAction() const {
	return m_stSoapAction;
}

void SimpleWebServiceClientSoapBase::setMaxRequestSizeBytes(long lMaxSizeBytes) {
	m_lMaxSizeBytes = (lMaxSizeBytes <=0 ) ? 0 : lMaxSizeBytes;
}

void SimpleWebServiceClientSoapBase::setCookiejarFileName(const std::string& stFileName) {
	m_stCookieJarFileName = stFileName;
}

void SimpleWebServiceClientSoapBase::setMaxRedirs(int max_redirs) {
	m_iMaxRedirs = max_redirs;
}

void SimpleWebServiceClientSoapBase::setTimeOutInSeconds(long time_out_secs) {
	m_iTimeOutSeconds = time_out_secs;
}

std::string SimpleWebServiceClientSoapBase::getLastErrorString() {
	return m_stLastError;
}

std::string SimpleWebServiceClientSoapBase::getWarnings() {
	return 	m_stWarnings;
}

SoapFault* SimpleWebServiceClientSoapBase::getSoapFault() {
	return m_pSoapFault.get();
}

namespace {
	const unsigned int MAX_CHARS_ON_ERROR = 1000;

	void AddLimitedResponseToError(const string& stResponseFileName, string& stErr) {
		string stResponse;
		read_file_to_string(stResponseFileName, stResponse);
		if ( stResponse.size() > MAX_CHARS_ON_ERROR ) {
			stErr += "\nThe first " + toString(MAX_CHARS_ON_ERROR) + " characters of the response were:\n";
			stErr += stResponse.substr(0, MAX_CHARS_ON_ERROR);
		}
		else {
			stErr += "\nThe response was:\n";
			stErr += stResponse;
		}
	}
}

long SimpleWebServiceClientSoapBase::postSoapEnvelopeWithRetries(const std::string& stSoapEnvelopeFileName,
																 const std::string& stResponseFileName,
																 HttpProxyConfiguration* pGlobalProxyConfig,
																 const HttpOptions& httpOptions,
																 int numberOfRetries,
																 int retryWaitTimeSeconds)
{
	                                    
	if ( numberOfRetries < 0 ) numberOfRetries = 0;
	if ( retryWaitTimeSeconds < 0 ) retryWaitTimeSeconds = 0;
	string stLastError;

	int retry = 0;
	long retVal = 0;
	while( retry <= numberOfRetries ) {
		if ( retry > 0 ) {
			konga::MilliSleep(retryWaitTimeSeconds*1000);
		}
		retVal = this->postSoapEnvelope(stSoapEnvelopeFileName, stResponseFileName, pGlobalProxyConfig, httpOptions);
		if ( retVal == 0 ) {
			if ( retry > 0 && !stLastError.empty() ) {
				m_stWarnings = "Web service call succeeded after retries. Tried " + konga::toString(retry + 1) +
					" times. Last error was: " + stLastError;
			}
			return retVal;
		}
		stLastError = getLastErrorString();
		++retry;
	}

	if ( numberOfRetries > 0 ) {
		m_stLastError += "\nTried to call the web service " + konga::toString(retry) + " times.";
	}

	return retVal;
}

long SimpleWebServiceClientSoapBase::postSoapEnvelope(const std::string& stSoapEnvelopeFileName, const std::string& stResponseFileName,
													  HttpProxyConfiguration* pGlobalProxyConfig, const HttpOptions& httpOptions) {
	clearErrors();

	auto_ptr<HttpBase> pHttp;
	if ( strncmp(m_stUrl.c_str(), "https://",8) == 0 ) {
		pHttp.reset( new Https(pGlobalProxyConfig) );
		                                                            
	}
	else {
		pHttp.reset( new Http(pGlobalProxyConfig) );
	}

	pHttp->SetHttpVerb(HttpPost);
	pHttp->setRequestHeaders(getHttpHeaders());
	pHttp->SetUrl(m_stUrl);
	pHttp->SetHttpOptions(httpOptions);
	pHttp->followRedirects(m_iMaxRedirs);
	pHttp->SetTimeOutSec(m_iTimeOutSeconds);

	if ( m_stUserName.size() > 0 ) {
		pHttp->SetUserName(m_stUserName);
		if ( m_stPassword.size() > 0 ) {
			pHttp->SetPassword(m_stPassword);
		}
	}

	                                                      
	if ( m_lMaxSizeBytes > 0 ) {
		long lErr = 0;
		long long size = GetFileSize(stSoapEnvelopeFileName, lErr);
		if ( lErr != 0 ) {
			m_stLastError += "Failed to check the size of the file \"" + stSoapEnvelopeFileName + "\".\n";
			return m_lLastErrorNumber = lErr;
		}

		                                                                                                     
                                                   
                                                                        
                                                                                                  
                        
     
		if ( size > m_lMaxSizeBytes ) {
			m_stLastError += "Failed to post the soap envelope. The size exceeds the specified max size by ";
			char buf[32];
			m_stLastError += ::itoa(size-m_lMaxSizeBytes, buf, 10);
			m_stLastError += " bytes.\n";
			m_lLastErrorNumber = -1;
			return m_lLastErrorNumber;
		}
	}

	                                
	string stInputCookieFile;
	if ( !m_stCookieJarFileName.empty() ) {
		                                                                                    
		stInputCookieFile = konga::getTmpFileName("cookie-input");
		if ( FileExists(m_stCookieJarFileName) ) {
			try {
				kMoveFile(m_stCookieJarFileName, stInputCookieFile, true);
			}
			catch(...) {
				                                
			}
		}
		                                                                                                       
		pHttp->SetInputCookieFile(stInputCookieFile);
		pHttp->SetOutputCookieFile(m_stCookieJarFileName);
	}

	const long lRetVal = pHttp->PutAsFileGetRespAsFile(stSoapEnvelopeFileName, stResponseFileName);

	                                             
	                                                                       
	std::string stErr;
	if ( lRetVal != 0 ) {
		stErr = "Failed to call the web service at \"" + m_stUrl + "\".\nReason: ";
		stErr += pHttp->GetLastError();

		long lLastErrorNumber = pHttp->GetLastErrorNumber();
		m_lLastErrorNumber = (lLastErrorNumber != 0) ? lLastErrorNumber : lRetVal;
	}

	                                                                                               
	if ( !stInputCookieFile.empty() ) {
		try {
			kDeleteFile(stInputCookieFile);
		}
		catch(...) {
			                                
		}
	}

	                     
	std::vector<std::string> vResponseHeaders = pHttp->GetResponseHeaders();
	RFC822HeadersWithParameters headers;
 	const string stCrLf("\r\n");
	string stAllHeaders;
	for( std::vector<std::string>::iterator it = vResponseHeaders.begin(); it != vResponseHeaders.end(); ++it ) {
		try {
			const string& stHeader = *it;
			stAllHeaders += stHeader;
			if ( stHeader.compare(0, 8, "HTTP/1.1") != 0 && stHeader != stCrLf ) {
					headers.AddHeader(stHeader);
			}
		}
		catch( const CrException& ) {
			                                                                      
		}
	}

	                                               
	                                                                   
	                                   
	bool bValidContentType = false;
	string stContentTypeHeaderValue;
	{
	const vector<HeaderValue>& vContentTypeHeaders = headers.GetHeaderValues("Content-Type");
	for( vector<HeaderValue>::const_iterator cit = vContentTypeHeaders.begin();
		cit != vContentTypeHeaders.end(); ++cit ) {
			stContentTypeHeaderValue = cit->GetValue();
			string stHeaderErrors;                                                             
			bValidContentType = validateContentType(stContentTypeHeaderValue, stHeaderErrors);
	}
	}

	                                                      
	                                     
	string stErrorSoapFault;
	if ( NonEmptyFileExists(stResponseFileName) ) {
		if ( bValidContentType ) {

			std::string err;
			boost::shared_ptr<SoapFault> pSoapFault( SoapEnvelope::CheckForSoapFault(stResponseFileName, err) );
			if ( pSoapFault == 0 ) {
				                   
				if ( !err.empty() ) {
					std::string stWarning = "Failed to check for Soap Fault in the response:\n";
					stWarning += err;
					addWarning(stWarning);

					if ( !stErr.empty() )
						stErr += "\n";
					stErr += "Failed to check for a SoapFault in the response: \n";
					stErr += stResponseFileName;
					stErr += "\n";
					stErr += stWarning;
					AddLimitedResponseToError(stResponseFileName, stErr);

					if ( m_lLastErrorNumber == 0 )
						m_lLastErrorNumber = -1;
				}
			}
			else {
				                                     
				if ( !stErr.empty() )
					stErr += "\n";

				stErrorSoapFault = "The web service returned a SOAP Fault:\n";
				stErrorSoapFault += pSoapFault->GetFormattedErrorMessage();
				if ( m_lLastErrorNumber == 0 )
					m_lLastErrorNumber = -1;

				                     
				m_pSoapFault = pSoapFault;
			}
		}
		else {
			                                                              
			string stWarning;
			if ( stContentTypeHeaderValue.empty() )
				stWarning = "The Content-Type header in the response is not present or is empty.";
			else
				stWarning = "The Content-Type header value \"" + stContentTypeHeaderValue + "\" is not valid for a SOAP response.";
			addWarning(stWarning);
			if ( !stErr.empty() ) {
				stErr += "\n";
				stErr += stWarning;
			}

			if ( !stErr.empty() ) {
				                                                                              
				AddLimitedResponseToError(stResponseFileName, stErr);
			}
		}
	}
	else {
		if ( !stErr.empty() )
			stErr += "\n";
		if ( lRetVal == 0 ) {
			stErr += "The SOAP body was successfully posted but the server returned an empty response." \
				" No reason was given.";
		}
		else {
			stErr += "The server returned no response.";
		}
	}

	if(stErrorSoapFault.empty()) {
		                                         
		m_stLastError = stErr;
	}
	else {
		m_stLastError = stErrorSoapFault + "\n\nAdditional details:\n" + stErr;
	}

	if ( !m_stLastError.empty() && !stSoapEnvelopeFileName.empty() ) {
		m_stLastError += "\n(The posted soap envelope was: '" + stSoapEnvelopeFileName + "'.)";
	}

	return m_lLastErrorNumber;
}

                  
  
void SimpleWebServiceClientSoapBase::addWarning(const std::string& stWarning) {
	if ( !m_stWarnings.empty() ) {
		m_stWarnings += "\n";
	}
	m_stWarnings += stWarning;
}

void SimpleWebServiceClientSoapBase::clearErrors() {
	m_stLastError.clear();
	m_lLastErrorNumber = 0;
	m_pSoapFault.reset();
}

