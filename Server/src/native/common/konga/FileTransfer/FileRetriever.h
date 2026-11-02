/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef FILE_RETRIEVER_H
#define FILE_RETRIEVER_H

#include <string>

#include <konga/CertManagement/CertInfo.h>

#include "Protocol.h"

namespace konga {

	class FileRetriever
	{
	public:
		                                                 
		static void SaveUrlToFile(const std::string& stUrl, const std::string& stFileName);

	public:
		FileRetriever();
		~FileRetriever();

		enum WhatToReturn { aNothing = 0, aString, aFile};
	
		void SetUserName(const std::string& UserName) { m_UserName = UserName;}
		void SetPassWord(const std::string& Passwd) { m_PassWord = Passwd;}
		void SetCertificateInfo(const CrSecurity::CertInfo& certInfo) { m_CertInfo = certInfo; }                                                    
	
		void SetProxyUserName(const std::string& ProxyUserName) { m_stProxyUserName = ProxyUserName; }
		void SetProxyPassword(const std::string& ProxyPassword) { m_stProxyPassword = ProxyPassword; }
		void SetProxyServer(const std::string& ProxyServer) { m_stProxyServer = ProxyServer; }
	
		std::string GetUserName() const {return m_UserName;}
		std::string GetPassword() const {return m_PassWord;}
    	std::string GetCertificateName() const { return m_CertInfo.CertificateName(); }
	
		std::string GetProxyUserName() const {return m_stProxyUserName;}
		std::string GetProxyPassword() const {return m_stProxyPassword;}
		std::string GetProxyServer() const {return m_stProxyServer;}
	
		long PutAsString(const std::string& url, std::string& Str);
		long PutAsString(const std::string& url, const std::string& Str, std::string& respStr, WhatToReturn respWhat);
		
		long PutAsFile(const std::string& url, const std::string& FileName);
		long PutAsFile(const std::string& url, const std::string& FileName, std::string& respStr, WhatToReturn respWhat);
	
		long GetFile(const std::string& url, std::string& FileName)
		{
			return GetAsFile(url, FileName);
		}
		std::string GetFileAsString(const std::string& url)
		{
			std::string Str;
			if(GetAsString(url, Str) == 0)
				return Str;
			else
				return "";
		}
		long GetAsString(const std::string& url, std::string& Str);                                                
		long GetAsFile(const std::string& url, std::string& localFile);                                              
	
		long PutResponseType(long type){m_Type = type; return 0;}                              
																	                                     
																	                                
		long GetResponseType() const {return m_Type;}                                                    
	
		std::string LastError() const { return m_LastError; }                                              
		long LastErrorNumber() const {return m_LastErrorNumber;}
	
		enum FileMode { Get_ASCII_File = 1, Get_ASCII_String, Get_UNICODE_File, Get_UNICODE_String, Put_ASCII_File, Put_ASCII_String, Put_UNICODE_File, Put_UNICODE_String};
		enum ResponseMode { NoResp = 0, DATA, FILENAME};
	
		void setFileSecurity(FileSecurity* fs) { if(m_pFileSecurity) delete m_pFileSecurity; m_pFileSecurity = fs; }
		FileSecurity* getFileSecurity() const { return m_pFileSecurity; }
		bool HasAccessToFile(const std::string& stFileName) const
		{
			return m_pFileSecurity ? m_pFileSecurity->HasAccess(stFileName) : true;
		}
	
		void SetByteRange(long lStartByte, long lEndByte) { m_lStartByte=lStartByte; m_lEndByte=lEndByte; }
		void Append(bool bAppend) { m_bAppend = bAppend; }

		static bool IsSupportedUrl(const std::string& s) {
			return  strnicmp(s.c_str(), "http://", 7) == 0 ||
					strnicmp(s.c_str(), "https://", 8) == 0 ||
					strnicmp(s.c_str(), "ftp://", 6) == 0 ||
					                                            
					                                           
					strnicmp(s.c_str(), "smb://", 6) == 0;
		}
	
	protected:
		long GetFirstFileName(std::string& stFileName);
		long AuthenticateUNC(const std::string& stFileName);
	
		long GetFile(const std::string& url, WhatToReturn what);
		long DoGetFile(const std::string& url, WhatToReturn what);
		long PutFile(const std::string& url, WhatToReturn what, 
								std::string& respStr, WhatToReturn respWhat);
		std::string m_LastError;
	
	private:
		CrSecurity::CertInfo m_CertInfo;
		FileSecurity* m_pFileSecurity;
		Protocol* m_Protocol;
		long m_LastErrorNumber;
		long m_Type;
		long m_lStartByte, m_lEndByte;
		bool m_bAppend;
		std::string m_LocalFile, m_TheContents;
		std::string m_UserName, m_PassWord;
		std::string m_stProxyUserName, m_stProxyPassword, m_stProxyServer;	
	};
}

#endif                    

