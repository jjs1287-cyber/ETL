/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_CURL_TRANSFER_LIB_WRAPPER_H
#define KONGA_CURL_TRANSFER_LIB_WRAPPER_H

#include <curl/curl.h>
#include <boost/noncopyable.hpp>

#include "TransferLibWrapper.h"

namespace konga {

	   
                                                                              
    
              
                                                                                   
                                          
    
                                                                                                   
    
	class CurlTransferLibWrapper : public TransferLibWrapper, private boost::noncopyable {
	public:
		CurlTransferLibWrapper(void);
		~CurlTransferLibWrapper(void);

		void reset();

		void setUrl(const std::string &stUrl);
		void setTimeOut(long lTimeOut_sec);

		void addHttpRequestHeader(const std::string& stRequestHeader);

		void isHttpPost(bool bIsPost = false);

		void rfc1867FileUpload(const std::string& stName,
			const std::string& stFileName, const std::string& stContentType = "");

		void setNoBody(bool bNoBody);
		void setExplicitHttpVerb(const std::string& stVerb);

		void requestAllSupportedEncodings();
		void requestSpecificEncoding(const std::string& encoding);

		void enableHttpChunkedTransferEncoding(bool bEnable);
		void enableExpect100Continue(bool bEnable);

		void overrideContentType(const std::string& contentType);

		void setUserAndPassword(const std::string& stUser, const std::string& stPassword);

		void setProxySettings(const std::string& sHostName, long lPort, const std::string& sUserName, const std::string& sPassword);

		void setVerifyPeerCert(bool bVerify);

		void setResponseFile(const std::string& stLocalResultFile);
		void setResponseString(std::string& pstLocalResultString);

		void setRequestFile(const std::string& stLocalRequestFile);
		void setRequestString(const std::string& stLocalRequestString);

		void setInputCookieFileName(const std::string& stFileName);
		void setOutputCookieFileName(const std::string& stFileName);

		void setUserAgent(const std::string& stUserAgent);

		void followRedirects(int max_redir);

		void setFtpAscii();
		void setFtpBinary();                               

		void setFtpRenameTo(const std::string& stRenameFrom, const std::string& stRenameTo);

		void setFtpPassiveMode(bool bPassiveMode);	                                     

		void setFtpSslNone();
		void setFtpSslTry();
		void setFtpSslControl();
		void setFtpSslAll();

		void preferAUTH_TLS(bool preferAUTH_TLS);

		void configureSftp(const std::string& keyFile, const std::string& publicFile, const std::string& passphrase, const std::string& fingerprint);
		void setSshPublicKeyFileName(const std::string& stPubKeyFile);
		void setSshPrivateKeyFileName(const std::string& stPrivateKeyFile, const std::string& stPassphrase);

		                                                             
		                                                                                                               
		void ftpRename(const std::string& stOldFileName, const std::string& stNewFileName);

		void Delete(const std::string& stFileName);

		                                                                                                  
		std::string getFtpDirectoryListing(bool bNlstOnly);

		void Get();
		void Put();

		void setDebugFile(FILE* f);

		std::vector<std::string> getResponseHeaders() const;

	private:
		void Cleanup();		                                                    
		void SetupWriteStream(bool bIgnore = false);	                                                      
		void SetupReadStream();
		void SetupHeaderStream();

		void setFtpSslLevel(long level);

	private:
		void checkHandle();
		void initDebug();
		bool isFtp();
		bool isSftp();
		bool isHttp();

		                                                                     
		                                                                                   
		CURLcode CurlEasyPerformWrapper();

		                                                          
		                              
		void ftpsWorkAround();

		void setHeadersIfNeeded();
		void overrideHttpVerbIfRequested();
	private:
		CURL* m_handle;
		class CurlPersistedData;
		CurlPersistedData* m_pCurlData;
		
		bool m_bPost;
		bool m_bUseRfc1867;
		bool m_bUseHttpChunkedTransferEncoding;
		bool m_bEnableExpect100Continue;

		FILE* m_DebugFile;

		std::string m_stFtpRNFR, m_stFtpRNTO;
		std::string m_stFormName, m_stFormFileName, m_stFormContentType;
		std::string* m_pstLocalResponseString;
		std::string m_stLocalResponseFileName;

		const std::string* m_pstLocalRequestString;
		std::string m_stLocalRequestFileName;

		std::string m_stOverrideHttpVerb;

		std::vector<std::string> m_vResponseHeaders;
	};
}

#endif                                     

