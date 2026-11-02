/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRANSFER_LIB_WRAPPER_H
#define KONGA_TRANSFER_LIB_WRAPPER_H

#include <vector>
#include <string>

namespace konga {

	   
                                                
    
	class TransferLibWrapper {
	public:
		TransferLibWrapper(void) {}
		virtual ~TransferLibWrapper(void) {};

		virtual void reset() = 0;

		virtual void setUrl(const std::string& stUrl) = 0;

		virtual void setTimeOut(long lTimeOut_sec) = 0;

		virtual void setUserAndPassword(const std::string& stUser, const std::string& stPassword) = 0;

		virtual void setProxySettings(const std::string& sHostName, long lPort, const std::string& sUserName, const std::string& sPassword) = 0;

		virtual void setVerifyPeerCert(bool bVerify) = 0;

		virtual void setResponseFile(const std::string& stLocalResultFile) = 0;
		virtual void setResponseString(std::string& pstLocalResultString) = 0;

		virtual void setRequestFile(const std::string& stLocalRequestFile) = 0;
		virtual void setRequestString(const std::string& stLocalRequestString) = 0;

		virtual void setInputCookieFileName(const std::string& stFileName) = 0;
		virtual void setOutputCookieFileName(const std::string& stFileName) = 0;

		                
		virtual void addHttpRequestHeader(const std::string& stRequestHeader) = 0;

		                          
		virtual void isHttpPost(bool bIsPost = false) = 0;

		                                       
		virtual void rfc1867FileUpload(const std::string& stName,
			const std::string& stFileName, const std::string& stContentType = "") = 0;

		                                                                                                                 
		virtual void setNoBody(bool bNoBody) = 0;

		                                             
		virtual void setExplicitHttpVerb(const std::string& stVerb) = 0;

		                                                                                          
		virtual void requestAllSupportedEncodings() = 0;
		virtual void requestSpecificEncoding(const std::string& encoding) = 0;

		virtual void enableHttpChunkedTransferEncoding(bool bEnable) = 0;
		virtual void enableExpect100Continue(bool bEnable) = 0;

		virtual void overrideContentType(const std::string& contentType) = 0;

		virtual void setUserAgent(const std::string& stUserAgent) = 0;

		                                                    
		                                                 
		                                                               
		                                                         
		virtual void followRedirects(int max_redir) = 0;

		                   
		virtual void setFtpAscii() = 0;
		virtual void setFtpBinary() = 0;                               

		virtual void setFtpRenameTo(const std::string& stRenameFrom, const std::string& stRenameTo) = 0;

		virtual void setFtpPassiveMode(bool bPassiveMode) = 0;	                                     
		virtual void ftpRename(const std::string& stOldFileName, const std::string& stNewFileName) = 0;

		                                      
		virtual void setFtpSslNone() = 0;
		virtual void setFtpSslTry() = 0;
		virtual void setFtpSslControl() = 0;
		virtual void setFtpSslAll() = 0;

		virtual void preferAUTH_TLS(bool preferAUTH_TLS) = 0;

		                   
		virtual void configureSftp(const std::string& keyFile, const std::string& publicFile, const std::string& passphrase, const std::string& fingerprint) = 0;
		virtual void setSshPublicKeyFileName(const std::string& stPubKeyFile) = 0;
		virtual void setSshPrivateKeyFileName(const std::string& stPrivateKeyFile, const std::string& stPassphrase) = 0;

		                             
		virtual void Delete(const std::string& stFileName) = 0;

		                                                                            
		virtual void Get() = 0;

		                                                                            
		virtual void Put() = 0;

		virtual std::vector<std::string> getResponseHeaders() const = 0;

		virtual std::string getFtpDirectoryListing(bool bNlstOnly) = 0;

		                                                            
		virtual void setDebugFile(FILE* f) = 0;
	};
}
#endif                                

