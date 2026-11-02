/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef InetBase_h
#define InetBase_h

#include <string>
#include <vector>

#include <boost/thread/mutex.hpp>
#include <boost/noncopyable.hpp>

#include "TransferLibWrapper.h"
#include "Protocol.h"

namespace konga {

	  
                                                                                   
    
    class InetBase: public Protocol, private boost::noncopyable
    {
	protected:
    	InetBase(void);
	public:
    	virtual ~InetBase(void);

		virtual long Connect() {
			return Protocol::SUCCESS;
		}

		virtual long DisConnect();
    
    	virtual long GetAsFile(const std::string& stLocalFile);                                            
    	virtual long GetAsString(std::string& stResult);		                                                                    

		virtual long PutAsFile(const std::string& stLocalFile);
		virtual long PutAsFileGetRespAsString(const std::string& stLocalFile, std::string& stResponse);	
		virtual long PutAsFileGetRespAsFile(const std::string& LocalFile, const std::string& stResponseFile);

		virtual long PutAsString(const std::string& stLocalResource);
		virtual long PutAsStringGetRespAsString(const std::string& stLocalResource, std::string& stResponse);
		virtual long PutAsStringGetRespAsFile(const std::string& stLocalResource, const std::string& stResponseFile);

		                                          
		virtual long Delete(const std::string& stFileName);

		long TimeOutSec() const;
		void SetTimeOutSec(long lTimeOut_sec);                                                        

		long MaxRetries() const;
		void SetMaxRetries(long lMaxRetries);

		long RetryDelaySec() const;
		void SetRetryDelaySec(long lRetryDelaySec);

		std::string getHost() const { return m_stHost; }
		void setHost(const std::string& stHost) { m_stHost = stHost; }

		long getPort() const { return m_lPort; }
		void setPort(long lPort) { m_lPort = lPort; }

		std::string getPath() const { return m_stPath; }
		void setPath(const std::string& stPath);

		std::string getParsedUrl() { return m_stParsedUrl; }

		virtual long Reset();
        
    	std::string GetProxyServerName() const { return m_stProxyServerName; }
    	void SetProxyServerName(const std::string& Name) { m_stProxyServerName = Name; }
    
    	std::string GetProxyUserName() const { return m_stProxyUserName; }
    	void SetProxyUserName(const std::string& UserName) { m_stProxyUserName = UserName; }
    
    	std::string GetProxyPassword() const { return m_stProxyPassword;}
    	void SetProxyPassword(const std::string& Password) { m_stProxyPassword = Password; }

		                                                                                                 
		void setRequestHeaders(const std::string& stRequestHeaders);
		void addRequestHeader(const std::string& stRequestHeader);
		void clearRequestHeaders();

		void setVerifyPeer(bool bVerify) { m_bVerifyPeer = bVerify; }
		bool getVerifyPeer() const { return m_bVerifyPeer; }

		std::vector<std::string> GetResponseHeaders() const { return m_vResponseHeaders; }

    protected:
		virtual long ParseURL() = 0;				                                                                                                              
		virtual long CheckHeadersForErrors() = 0;	                                                 

		                                                                
		virtual bool isSslEnabled() const { return false; }

		                                                                                       
		                                                                                                                     
		virtual long doPreTransfer();
		virtual long doPreGet();
		virtual long doPrePut();
		virtual long doPostTransfer();

		std::string m_stParsedUrl;					                                                                  

        virtual long Get();
		virtual long Put();

		void AppendRequestAndResponseHeadersToString(std::string& str) const;

	protected:
		TransferLibWrapper* m_pTransferLib;
		std::vector<std::string> m_vRequestHeaders;

	private:
		bool m_bVerifyPeer;
		long m_lTimeOut_sec;
		long m_lMaxRetries, m_lRetryDelay_sec;
		FILE* m_debug_file;
		boost::mutex m_MasterLock;

        std::string m_stProxyServerName, m_stProxyUserName, m_stProxyPassword;
		std::vector<std::string> m_vResponseHeaders;
		std::string m_stHost, m_stPath;
		long m_lPort;
    };
}
#endif              

