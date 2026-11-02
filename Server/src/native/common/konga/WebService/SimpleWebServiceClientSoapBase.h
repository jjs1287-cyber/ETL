/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SIMPLE_WEBSERVICE_CLIENT_SOAP_BASE_H
#define KONGA_SIMPLE_WEBSERVICE_CLIENT_SOAP_BASE_H

#include <vector>
#include <string>

#include <boost/shared_ptr.hpp>

namespace konga {
	class SoapFault;
	class HttpProxyConfiguration;
	class HttpOptions;
	
	class SimpleWebServiceClientSoapBase
	{
	public:
		SimpleWebServiceClientSoapBase(void);
		virtual ~SimpleWebServiceClientSoapBase(void);
	
		void setUrl(const std::string& stUrl);
	
		void setUserName(const std::string& stUserName);
		void setPassword(const std::string& stPassword);
	
		void setSoapAction(const std::string& stSoapAction);
		std::string getSoapAction() const;
		
		    
                                               
                                                            
                                                                         
                                                                                              
     
		void setMaxRequestSizeBytes(long lMaxSizeBytes);

		   
                                                                      
                                                                      
                                                                     
     
		void setCookiejarFileName(const std::string& stFileName);

		void setMaxRedirs(int max_redirs);

		void setTimeOutInSeconds(long time_out_secs);
	
		long postSoapEnvelope(const std::string& stSoapEnvelopeFileName, const std::string& stResponseFileName,
			HttpProxyConfiguration* pGlobalProxyConfig, const HttpOptions& httpOptions);

		long postSoapEnvelopeWithRetries(const std::string& stSoapEnvelopeFileName, const std::string& stResponseFileName,
			HttpProxyConfiguration* pGlobalProxyConfig, const HttpOptions& httpOptions,
			int numberOfRetries, int retryWaitTimeSeconds);
	
		                                                            
		                                                                                    
		                                                                
		SoapFault* getSoapFault();
	
		std::string getLastErrorString();
		std::string getWarnings();
	
	protected:
		void setMajorSoapVersion(int iMajor);
		void setMinorSoapVersion(int iMinor);
		const std::string BR;
	
		virtual std::string getHttpHeaders() const = 0;
		virtual bool validateContentType(const std::string& stHeaderValue, std::string& stErrors) const = 0;
	
	private:
		void addWarning(const std::string& stWarning);
		void clearErrors();
	
	private:
		boost::shared_ptr<SoapFault> m_pSoapFault;
		int m_iSoapVersionMajor;
		int m_iSoapVersionMinor;
		long m_lLastErrorNumber;
		long m_lMaxSizeBytes;
		int m_iMaxRedirs;
		long m_iTimeOutSeconds;

		std::string m_stLastError;
		std::string m_stWarnings;
	
		std::string m_stUrl;
		std::string m_stUserName;
		std::string m_stPassword;
		std::string m_stSoapAction;
		std::string m_stCookieJarFileName;
	};
}                   
#endif

