/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <iostream>
#include <fstream>
#include <sstream>	               
#include <sys/stat.h>

#ifdef WIN32
	#include <comutil.h>	               
#endif

#include <konga/CrNet/CrNet.h>
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/FileUtils.h>		                 
#include <konga/Utils/util.h>			              
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CrException.h>

#include "Protocol.h"
#include "Https.h"
#include "Http.h"
#include "Ftp.h"

#ifndef WIN32
#include "Samba.h"
#endif

#include "FileRetriever.h"

using namespace konga;
using std::string;

FileRetriever::FileRetriever() : m_pFileSecurity(0), m_Protocol(0), m_LastErrorNumber(1), m_Type(1),
	m_lStartByte(0), m_lEndByte(-1), m_bAppend(false) {}

FileRetriever::~FileRetriever()
{
	if(m_Protocol) 
		delete m_Protocol;
	if(m_pFileSecurity)
		delete m_pFileSecurity;
}

void FileRetriever::SaveUrlToFile(const string& stUrl, const string& stFileName_in) {
	FileRetriever retriever;
	string stFileName(stFileName_in);
	if ( retriever.GetAsFile(stUrl, stFileName) != Protocol::SUCCESS ) {
		throw CrException("Failed to download file from url '" + stUrl + "'. Reason: "
			+ retriever.LastError());
	}
}

  
                                                                        
                                   
  
long FileRetriever::GetFirstFileName(std::string& stFileName)
{
	if( stFileName.find_first_of("?*") == std::string::npos )
		return 0;
	else
	{
		                                                 
		  
                              
                                                                
                      
   
                             
                                                                                            
                                                            
            
   

                             
    
		return 0;
	}
}

long FileRetriever::AuthenticateUNC(const std::string& stFileName)
{
	  
	                                                                 
	                                                                 
	                                                                    
	                           
	  
	                                                                                
#ifdef WIN32
	if( !m_UserName.empty() && !m_PassWord.empty() &&
		stFileName.length() > 4 && stFileName[0]=='\\' && stFileName[1]=='\\' )
	{
		size_t iPathStartPos = stFileName.substr(3).find('\\');
		std::string stMachineName = stFileName.substr(2,iPathStartPos+1);
		if( !stMachineName.empty() )
		{
			_bstr_t Domain;
			if( m_UserName.find('\\') == std::string::npos )                                                           
				Domain = stMachineName.c_str();
			else
				Domain = m_UserName.substr(0,m_UserName.find('\\')).c_str();                  
			_bstr_t User = m_UserName.substr(m_UserName.find('\\')+1).c_str();
			long err = ::CreateSession(_bstr_t(stMachineName.c_str()), Domain, User, _bstr_t(m_PassWord.c_str()));
			m_LastError = "Failed to connect to " + stMachineName + " with user '";
			m_LastError += (const char*)User;
			m_LastError += "' in domain '";
			m_LastError += (const char*)Domain;
			m_LastError += "'. Reason: ";
			m_LastError += ErrorResolver().Resolve(err);
			return m_LastErrorNumber = err;
		}
	}
	m_LastError = "UNC not implemented yet.";
	return -1;                                 
#endif         
	return 0;
}

  
                                                
  
long FileRetriever::GetAsString(const std::string& url, std::string& Str)
{
	long res = GetFile(url,aString);
	Str = m_TheContents;
	return res;
}

long FileRetriever::GetAsFile(const std::string& url, std::string& localFile)
{
	m_LocalFile = localFile;
	long res = GetFile(url,aFile);
	if( !m_LocalFile.empty() && FileExists(m_LocalFile) )
		localFile = m_LocalFile;
	else
		localFile = "";
	return res;
}

  
                                                                                  
  
long FileRetriever::GetFile(const std::string& url, WhatToReturn what)
{
	long res = DoGetFile(url, what);
	if( res != Protocol::SUCCESS || m_LocalFile.empty() || m_LocalFile == "(null)" )
		return res;
	return res;
}

long FileRetriever::DoGetFile(const std::string& url, WhatToReturn what)
{
	long res = Protocol::SUCCESS;
	m_LastErrorNumber=0;
	m_TheContents="";
	if( !url.length() )
	{
		m_LastError = "Must specify a URL or a filename to be transferred.";
		return Protocol::FAILURE;
	}

	  
	                                                                       
	                                 
	  
	if( url.find("://") != std::string::npos &&
		strncasecmp(url.c_str(), "file://",7) !=0 &&	                     
		strncasecmp(url.c_str(), "smb://",6) !=0)		              
	{
		if ( m_Protocol != 0 ) {
			 delete m_Protocol;
			 m_Protocol = 0;
		}
		
		if( strncasecmp(url.c_str(),"https://",8) == 0 )
		{
			                                 
			Https* pHttps = new Https(0);
			pHttps->SetProxyUserName(m_stProxyUserName);
			pHttps->SetProxyPassword(m_stProxyPassword);
			pHttps->SetProxyServerName(m_stProxyServer);
			m_Protocol = pHttps;
		}
		else if( strncasecmp(url.c_str(),"http://",7) == 0 )
		{
			                                 
			Http* pHttp = new Http(0);
			pHttp->SetProxyUserName(m_stProxyUserName);
			pHttp->SetProxyPassword(m_stProxyPassword);
			pHttp->SetProxyServerName(m_stProxyServer);
			pHttp->SetUserName(m_UserName);
			pHttp->SetPassword(m_PassWord);
			m_Protocol = pHttp;
		}
		else if( strncasecmp(url.c_str(),"ftp://",6) == 0 )
		{
			Ftp* pFtp = new Ftp();
			pFtp->SetUserName(m_UserName);
			pFtp->SetPassword(m_PassWord);
			pFtp->SetProxyUserName(m_stProxyUserName);
			pFtp->SetProxyPassword(m_stProxyPassword);
			pFtp->SetProxyServerName(m_stProxyServer);
			m_Protocol = pFtp;
		}
                                                       
     
                                                    
                          
     
		else
		{
			m_LastError = "Protocol not supported. The URL was: ";
			m_LastError += url;
			return Protocol::FAILURE;
		}

                                                 

                                                   

		if((res = m_Protocol->SetUrl(url)) != Protocol::SUCCESS)
		{
			m_LastError = m_Protocol->GetLastError();
			m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
			return res;
		}
		if((res = m_Protocol->Connect()) != Protocol::SUCCESS)
		{
			m_LastError = m_Protocol->GetLastError();
			m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
			return res;
		}
		if((res = m_Protocol->PutResponseType(m_Type)) != Protocol::SUCCESS)
		{
			m_LastError = m_Protocol->GetLastError();
			m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
			return res;
		}
		if( what == aFile )
		{
			res = m_Protocol->GetAsFile(m_LocalFile);
			if(res != Protocol::SUCCESS)
			{
				m_LastError = m_Protocol->GetLastError();
				m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
			}
			return res;
		}
		else
		{
			if((res = m_Protocol->GetAsString(m_TheContents)) != Protocol::SUCCESS)
			{
				m_LastError = m_Protocol->GetLastError();
				m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
				return res;
			}
			m_Protocol->DisConnect();
			return Protocol::SUCCESS;
		}
	}
	else                                                                                 
	{
		std::string filename = url;
		if( strncasecmp(url.c_str(),"file://", 7) == 0 )
			filename.erase(0,7);

		if ( strncasecmp(url.c_str(), "smb://",6) == 0 )
			filename.erase(0,4);

		if( !HasAccessToFile(filename) )
		{
			m_LastError = "Access to \"" + filename + "\" was denied due to policy setting.";
			return m_LastErrorNumber = -1;
		}

#ifndef WIN32	                                                                                             
		if ( strncasecmp(filename.c_str(), "//", 2) == 0 ||
			 strncasecmp(filename.c_str(), "\\\\", 2) == 0 ) {
			try {
				Samba smb(false, false);
				smb.setUserName(m_UserName);
				smb.setPassword(m_PassWord);

				if ( what == aFile ) {
					smb.CopyFromShareToLocalFile(filename, m_LocalFile);
				}
				else {
					m_TheContents.clear();
					smb.CopyFromUncToLocalString(filename, m_TheContents);
				}
			}
			catch( const CrException& ex ) {
				std::stringstream sErr;
				sErr << "Failed to get file from file share using Samba. " << std::endl << ex.GetErrorString();
				m_LastError = sErr.str();
				m_LastErrorNumber = -1;
				return Protocol::FAILURE;
			}
			return Protocol::SUCCESS;
		}
#endif        

		                                                              
		long err = AuthenticateUNC(filename);
		if( err ) return err;                                       

		long hr = GetFirstFileName(filename);
		if( hr != 0 ) return hr;

		char *pc;
		long bytesRead;
		std::string stReadErr;
		pc = read_file(filename.c_str(), bytesRead, stReadErr);
		AutoArrayDelete<char> adopt_pc(pc);
		if( !stReadErr.empty() ){
			m_LastError = stReadErr;
			m_LastErrorNumber = -1;
			return Protocol::FAILURE;
		}
		if( what == aFile )
		{
			FILE* out;
			out = fopen(m_LocalFile.c_str(),"wb");
			if( !out )
			{
				m_LastErrorNumber = ::GetLastError();
				m_LastError = "Failed to open \"" + m_LocalFile + "\" for writing";
				m_LastError = ErrorResolver().Resolve(m_LastErrorNumber);
				return m_LastErrorNumber;
			}
			AutoFileHandle adopt_out(out);
			fwrite(pc,bytesRead,1,out);
		}
		else
		{
			if( memchr(pc, 0, bytesRead) )
			{
				m_TheContents = "";
				m_LastError = "The file you are trying to download ('" +
					filename + "') is either binary or a text file containing wide characters. "
					"Such files cannot be loaded into strings. Change the file transfer "
					"to download into a file instead of into a string.\n";
				return Protocol::FAILURE;
			}

			m_TheContents = pc;
		}
	}
	return Protocol::SUCCESS;
}

                                        
long FileRetriever::PutAsString(const std::string& url, std::string& Str)
{
	m_TheContents = Str.c_str();
	std::string astr;
	long res = PutFile(url, aString, astr, aNothing);
	                      
	return res;
}

                                     
long FileRetriever::PutAsString(const std::string& url, const std::string& Str, 
								std::string& respStr, WhatToReturn respWhat)
{
	m_TheContents = Str.c_str();
	long res = PutFile(url,aString, respStr, respWhat);
	                      
	return res;
}

long FileRetriever::PutAsFile(const std::string& url, const std::string& localFile)
{
	m_LocalFile = localFile;
	std::string astr;
	return PutFile(url,aFile, astr, aNothing);
}

long FileRetriever::PutAsFile(const std::string& url, const std::string& localFile, 
								std::string& respStr, WhatToReturn respWhat)
{
	m_LocalFile = localFile;
	return PutFile(url,aFile, respStr, respWhat);
}
  
                                                                                 
  
long FileRetriever::PutFile(const std::string& url, WhatToReturn what, 
							std::string& respStr, WhatToReturn respWhat)
{
	long res = Protocol::SUCCESS;
	m_LastErrorNumber=0;
	if( url.empty() )
	{
		m_LastError = "Empty URL";
		return Protocol::FAILURE;
	}

	if( what == aFile && m_LocalFile.empty() || m_LocalFile == "(null)" )
	{
		m_LastError = "Local file name is null.";
		return Protocol::FAILURE;
	}

	  
	                                                                       
	                                 
	  
	if( url.find("://") != std::string::npos && strncasecmp(url.c_str(), "file://", 7) !=0 )
	{
		if ( m_Protocol != 0 ) {
			delete m_Protocol;
			m_Protocol = 0;
		}
		if( strncasecmp(url.c_str(),"https://",8) == 0 )
		{
			                                 
			m_Protocol = new Https(0);
		}
		else if( strncasecmp(url.c_str(),"http://",7) == 0 )
		{
			                                 
			m_Protocol = new Http(0);
			m_Protocol->SetUserName(m_UserName);
			m_Protocol->SetPassword(m_PassWord);
		}
		else if(strncasecmp(url.c_str(),"ftp://",6) == 0 )
		{
			m_Protocol = new Ftp();
			m_Protocol->SetUserName(m_UserName);
			m_Protocol->SetPassword(m_PassWord);
		}
                                                        
     
                                        
                                                    
                          
     
		else
		{
			m_LastError = "Protocol Not Supported";
			return Protocol::FAILURE;
		}

		m_Protocol->Append(m_bAppend);

		if((res = m_Protocol->SetUrl(url)) != Protocol::SUCCESS)
		{
			m_LastError = m_Protocol->GetLastError();
			m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
			return res;
		}
		if((res = m_Protocol->Connect()) != Protocol::SUCCESS)
		{
			m_LastError = m_Protocol->GetLastError();
			m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
			return res;
		}
		if((res = m_Protocol->PutResponseType(m_Type)) != Protocol::SUCCESS)
		{
			m_LastError = m_Protocol->GetLastError();
			m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
			return res;
		}

		if( what == aFile )
		{
              
			switch(respWhat)
			{
			case aString:
				res = m_Protocol->PutAsFileGetRespAsString(m_LocalFile, respStr);
				break;
			case aFile:
				res = m_Protocol->PutAsFileGetRespAsFile(m_LocalFile, respStr);
				break;
			default:
				res = m_Protocol->PutAsFile(m_LocalFile);
				break;
			}

			if(res != Protocol::SUCCESS)
			{
				m_LastError = m_Protocol->GetLastError();
				m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
			}
			return res;
		}
		else
		{
              
			switch(respWhat)
			{
			case aString:
				res = m_Protocol->PutAsStringGetRespAsString(m_TheContents, respStr);
				break;
			case aFile:
				if( !HasAccessToFile(respStr) )
				{
					m_LastError = "Access to \"" + respStr + "\" was denied due to policy setting.";
					return m_LastErrorNumber = -1;
				}

				res = m_Protocol->PutAsStringGetRespAsFile(m_TheContents, respStr);
				break;
			default:
				res = m_Protocol->PutAsString(m_TheContents);
				break;
			}

			if(res != Protocol::SUCCESS)
			{
				m_LastError = m_Protocol->GetLastError();
				m_LastErrorNumber = m_Protocol->GetLastErrorNumber();
				return res;
			}
			m_Protocol->DisConnect();
			return Protocol::SUCCESS;
		}
	}
	else                               
	{
		std::string filename = url;
		if( strncasecmp(url.c_str(),"file://",7) ==0 )
			filename.erase(0,7);

		if ( strncasecmp(url.c_str(), "smb://",6) == 0 )
			filename.erase(0,4);

		if( !HasAccessToFile(filename) )
		{
			struct stat statBuf;
			std::string path = filename.substr(0, filename.find_last_of('\\') + 1);
			if ( stat(path.c_str(), &statBuf) != 0)
				m_LastError = "File does not exist, check file name \"" + filename + "\".";
			else
				m_LastError = "Access to \"" + filename + "\" was denied due to policy setting.";
			return m_LastErrorNumber = -1;
		}

#ifndef WIN32	                                                                                             
		if ( strncasecmp(filename.c_str(), "//", 2) == 0 ||
			 strncasecmp(filename.c_str(), "\\\\", 2) == 0 ) {
			try {
				Samba smb(false, false);
				smb.setUserName(m_UserName);
				smb.setPassword(m_PassWord);

				if ( what == aFile ) {
					smb.CopyFromLocalFileToUnc(m_LocalFile, filename);
				}
				else {
					smb.CopyFromLocalStringToUnc(m_TheContents, filename);
				}
			}
			catch( const CrException& ex ) {
				std::stringstream sErr;
				sErr << "Failed to copy data to file share using Samba. " << std::endl << ex.GetErrorString();
				m_LastError = sErr.str();
				m_LastErrorNumber = -1;
				return Protocol::FAILURE;
			}
			return Protocol::SUCCESS;
		}
#endif        

		long err = AuthenticateUNC(filename);
		if( err ) return err;                                       

		char *pc;
		int size = 0;
		FILE* out = fopen(filename.c_str(), "w+b");
		if( !out )
		{
			m_LastErrorNumber = ::GetLastError();
			m_LastError = "Failed to open \"" + filename + "\" for writing. ";
			m_LastError += ErrorResolver().Resolve(m_LastErrorNumber);;
			return m_LastErrorNumber;
		}
		AutoFileHandle adopt_out(out);
		if( what == aFile )
		{
			FILE* fd;
			fd = fopen(m_LocalFile.c_str(),"rb");
			if( !fd )
			{
				m_LastErrorNumber = ::GetLastError();
				m_LastError = "Failed to open local file \"" + m_LocalFile + "\" for reading. ";
				m_LastError += ErrorResolver().Resolve(m_LastErrorNumber);
				return m_LastErrorNumber;
			}
			AutoFileHandle adopt_fd(fd);
			if(fseek(fd, 0, SEEK_END) != 0)
			{
				m_LastErrorNumber = ::GetLastError();
				m_LastError = "Failed to work properly with the temporary file. ";
				m_LastError += ErrorResolver().Resolve(m_LastErrorNumber);
				return m_LastErrorNumber;
			}
			if((size = ftell(fd)) < 0)
			{
				m_LastErrorNumber = ::GetLastError();
				m_LastError = "Failed to work properly with the temporary file. ";
				m_LastError += ErrorResolver().Resolve(m_LastErrorNumber);
				return m_LastErrorNumber;
			}
			if(fseek(fd, 0, SEEK_SET) != 0)
			{
				m_LastErrorNumber = ::GetLastError();
				m_LastError = "Failed to work properly with the temporary file. ";
				m_LastError += ErrorResolver().Resolve(m_LastErrorNumber);
				return m_LastErrorNumber;
			}
			pc = new char[size+1];
			if ( pc == 0 ) {
				m_LastError = "Failed to allocate enough memory to read file '" + m_LocalFile + "' into memory.";
				m_LastErrorNumber = E_FAIL;
				return Protocol::FAILURE;
			}
			else {
				fread(pc, sizeof(char), size/sizeof(char), fd);
			}
		}
		else{
			size = static_cast<int>(m_TheContents.length());
			pc = new char[size+1];
			if ( pc == 0 ) {
				m_LastError = "Failed to allocate memory for string to write to file.";
				m_LastErrorNumber = E_FAIL;
				return Protocol::FAILURE;
			}
			strcpy(pc, m_TheContents.c_str());
		}
		if (pc)
		{
			fwrite(pc, sizeof(char), size/sizeof(char), out);
			delete[] pc;
		}
	}
	return Protocol::SUCCESS;
}

                                                    
                         
                            
   
