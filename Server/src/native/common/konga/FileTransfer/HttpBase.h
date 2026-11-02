/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef KONGA_FILE_TRANSFER_HTTPBASE_H
#define KONGA_FILE_TRANSFER_HTTPBASE_H

#include <string>


#include "HttpVerbEnum.h"
#include "HttpProxyConfiguration.h"
#include "HttpOptions.h"
#include "InetBase.h"

namespace konga {

	class HttpBase: public InetBase
	{
	protected:
		HttpBase(HttpProxyConfiguration* pProxyConfiguration);

	public:
		virtual ~HttpBase();
	
		static char* GetHttpStatus(unsigned int Code);

		void SetHttpOptions(const HttpOptions& options) { m_httpOptions = options; }

		                                                               
		void SetUserAgent(const std::string& stUserAgent) {
			m_stUserAgent = stUserAgent;
		}

		void SetHttpVerb(HttpVerb v) { m_HttpVerb = v; }
		HttpVerb GetHttpVerb() const { return m_HttpVerb; }

		void useRFC1867MultipartFileUpload(const std::string& stFormName,
			const std::string& stFormFileName, const std::string& stContentType = "");

		                                            
		                                                             
		void SetInputCookieFile(const std::string& stFileName) {
			m_stInputCookieFileName = stFileName;
		}

		                                                              
		                                                
		void SetOutputCookieFile(const std::string& stFileName) {
			m_stOutputCookieFileName = stFileName;
		}
	
		int GetHttpStatusCode() const { return m_iHttpStatus; }
		std::string GetHttpStatusAsString() const { return GetHttpStatus(m_iHttpStatus); }
	
		bool AppendToFile() const { return m_bAppendToFile; }
		void AppendToFile(bool bAppendToFile) { m_bAppendToFile = bAppendToFile; }

		                                                                                 
		void followRedirects(long level) { m_iMaxRedirectsToFollow = level; }
	
	protected:
		virtual long doPreTransfer();
		virtual long doPreGet();
		virtual long doPrePut();

		virtual bool isHttps() const = 0;
		virtual long ParseURL();
		virtual long CheckHeadersForErrors();
	
		                               
	                                                                                      
	                                      
	
	private:
		HttpProxyConfiguration* m_pProxyConfiguration;
		bool m_bAppendToFile;
		int m_iHttpStatus;
		HttpVerb m_HttpVerb;

		int m_iMaxRedirectsToFollow;

		std::string m_stUserAgent;
		std::string m_LocalResponseFile;
		std::string m_stInputCookieFileName, m_stOutputCookieFileName;

		HttpOptions m_httpOptions;
	};
}
#endif                                  

