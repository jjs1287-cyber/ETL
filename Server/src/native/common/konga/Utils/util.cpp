/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <stdlib.h>
#include <cmath>

#include <string>
#include <cctype>		               

#include <boost/scoped_array.hpp>

#ifdef WIN32
	#include <windows.h>

	#define SECURITY_WIN32
	#include <security.h>                 
	#include <lmcons.h>         
#endif

#ifndef WIN32
	#include <uuid/uuid.h>		                
	#include <sys/utsname.h>	        
#endif

#ifdef max
	#undef max
#endif

#include "util.h"
#include "CrException.h"
#include "FileUtils.h"
#include "Settings.h"
#include "StringUtils.h"
#include "Random.h"

using namespace konga;
using std::string;

std::string konga::getEnvironmentVariableValue(const std::string& name) {
	if ( name.empty() )
		return string("");
                                       
#ifdef __STDC_WANT_SECURE_LIB__
	size_t retSize = 0;
	int retval = getenv_s(&retSize, 0, 0, name.c_str());
	if ( retval != 0 || retSize == 0 )
		return string("");
	boost::scoped_array<char> buf(new char[retSize]);
	retval = getenv_s(&retSize, buf.get(), retSize, name.c_str());
	if ( retval != 0 || retSize == 0 )
		return string("");
	return string(buf.get());
#else
	char* result = getenv(name.c_str());
	if ( result != 0 )
		return string(result);
	return string("");
#endif
}

void konga::setEnvironmentVariableValue(const std::string& name, const std::string& value) {
	if ( name.empty() )
		return;

#ifdef WIN32
	::SetEnvironmentVariable(name.c_str(), value.c_str());
#else
	setenv(name.c_str(), name.c_str(), 1);
#endif
}


  
                                     
 
             
                            
                           
                                                  
  
                   
                                                                    

                
   
                                       

             
    
                                                              
     
                             
                    
                                                                                      
      
                    
                                                                                                          
                                                                                 
                       
      
     
                     
    
   
  
                   
      
                                         
       
 
  

void konga::GetGUIDString(std::string& stGuid)
{
                                                    
#ifndef WIN32
	uuid_t uu;
	uuid_generate(uu);
	char buf[100];
	uuid_unparse(uu, buf);
	stGuid = buf;
	return;
#else
	GUID uu;
	unsigned char* pChar = 0;
	if ( ::CoCreateGuid(&uu) != S_OK)
		goto backup_plan;

	if ( ::UuidToStringA(&uu, &pChar) != RPC_S_OK )
		goto backup_plan;

	stGuid = (const char*)pChar;
	::RpcStringFreeA(&pChar);
	return;

backup_plan:

	int rn=getRandomNumber();
	char buf[100];
	sprintf(buf,"%x",rn);
	stGuid=buf;
	return;
#endif         
}

string konga::GetGUIDString() {
	string s;
	GetGUIDString(s);
	return s;
}

namespace {
	const char validGuidChars[] = "abcdef";
	bool isValidGuidChar(const char c) {
		if ( std::isdigit(c) ) return true;
		string tmp; tmp += c;
		return ( strstr(validGuidChars, tmp.c_str()) != 0 );
	}
}

bool konga::IsGUIDString(const std::string& stGuid) {
	if ( stGuid.size() != 36 ) return false;

	                            
	string stGuidLower(stGuid);
	ToLower(stGuidLower);
	const char dash = '-';
	const string::size_type size = stGuidLower.size();
	for( string::size_type i = 0; i < size; ++i ) {
		const char c = stGuidLower.at(i);
		if ( i==8||i==13||i==18||i==23 ) {
			                                    
			if ( c != dash ) return false;
		}
		else {
			if ( !isValidGuidChar(c) ) return false;
		}
	}
	return true;
}

bool konga::shouldThrowOnError() {
	return GetOptionAsBoolean("Debug", "ThrowOnError", false);
}

                     
namespace {
	inline void nativize_path(string& stPath) {
		                                                                      
		ReplaceCharWithChar(stPath, '\\', '/');
	}
}

string konga::getAbsolutePath(const string& stRelativePath, bool bThrowIfNotExisting            ) {
	string stAbsolutePath(GetServerHomeWithSlash() + stRelativePath);
	nativize_path(stAbsolutePath);
	if ( bThrowIfNotExisting && !FileExists(stAbsolutePath) ) {
		throw CrException("The file \"" + stAbsolutePath + "\" does not exist.\"");
	}
	return stAbsolutePath;
}

string konga::getRelativePath(const string& stAbsolutePath_in) {
	string stInstallPath(GetServerHomeWithSlash());
	if ( stInstallPath.empty())
		return stAbsolutePath_in;

	nativize_path(stInstallPath);
	string stAbsolutePath(stAbsolutePath_in);
	nativize_path(stAbsolutePath);

	if ( stAbsolutePath.find(stInstallPath) == 0 )
		return stAbsolutePath.substr(stInstallPath.size());
	else
		return stAbsolutePath;
}

konga::Clock::Clock(void) : m_startClock(clock()) {}

double konga::Clock::getSeconds() const {
	const clock_t nowClock = clock();
	if ( m_startClock <= nowClock ) {
		const double d = (nowClock-m_startClock);
		return d/CLOCKS_PER_SEC;
	}
	return -1.0;
}

bool konga::isNumeric(const char*str)
{
	if(str==0)return false;
	unsigned char c=*str;
	if(c==0)return false;
	int idot=0;
	bool hasDigit=false;
	if(c=='+' || c=='-')str++;
	for(;(c=*str) != 0; str++){
		if(isdigit(c)){
			hasDigit=true;
			continue;
		}
		if(c=='.'){
			idot++;
			if(idot>1)return false;
			continue;
		}
		c=toupper(c);
		if((c=='E' || c=='D')&& hasDigit){
			hasDigit=false;
			c=*(++str);
			if(c=='+' || c=='-')str++;
			for(int i=0; (c=*str)!=0 && i<3; i++, str++){
				if(isdigit(c)){
					hasDigit=true;
					continue;
				} else return false;
			}
			return c==0 && hasDigit;
		}
		else return false;
	}
	return hasDigit;
}

namespace {
	const double half = 0.5;
}

double konga::roundDouble(double d) {
	double intPart = 0;
	const double fracPart = modf(d, &intPart);
	return ( fracPart > half ) ? intPart + 1 : intPart;
}
