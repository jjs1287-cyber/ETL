/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>		          
#include <konga/Utils/OdbcConnectionStringUtils.h>

#include "ConnectionString.h"

using namespace konga;
using std::string;

namespace {
	const string sDSN_LESS_CONNECTION("(dsnless)");

	                                                                               
	bool isOldStyleNoPwdConnectionString(const string& sNoPwdConnectString) {
		string sNoPwdConnectStringLower(sNoPwdConnectString);
		ToLower(sNoPwdConnectStringLower);
		return (sNoPwdConnectStringLower.find("pwd=***") != string::npos );
	}

	void addSemiColonIfNeeded(string& str) {
		if ( str.empty() || *(str.end()-1) == ';' )
			return;
		str += ';';
	}
}

void konga::GetConnectionString(const string& sServer, const long port, const string& sDriver,
								  const string& sDbName, const string& sLogin,
								  const string& sPassword, const string& sPath,
								  const string& sDSN, const string& sFile_Name,
								  const string& sNoPwdConnectString, const string& sAdditionalConnectParameters,
								  bool bPSQueryType,
								  string& sConnectStr)
{
	if (bPSQueryType)
	{
		                                                        

		if (sPath == "APPSRV")
		{
			                                            
			sConnectStr = "ODBC;DSN=" + sDSN + ";DBTYPE=" + sPath + ";APPNAME=" + sDriver + 
				";UID=" + sLogin + ";PWD=" + sPassword + ";";
		}
		else
		{
			                                            
			sConnectStr = "ODBC;DSN=" + sDSN + ";DBTYPE=" + sPath + ";DBNAME=" + sFile_Name +
					";UID=" + sLogin + ";PWD=" + sPassword + ";";
		}
	}
	else if( sNoPwdConnectString.empty() )
	{
		sConnectStr="SERVER="+sServer+";DRIVER="+sDriver;
		if ( !sDbName.empty() )
			sConnectStr += ";DATABASE="+sDbName;
		if ( port > 0 )
			sConnectStr += ";PORT=" + toString(port);
		AppendConnectString(sConnectStr, sAdditionalConnectParameters);
		sConnectStr += ";UID="+sLogin+";PWD="+sPassword;
	}
	else 
	{
		                                                                             
		                                                
		if ( sDSN_LESS_CONNECTION == sDSN || sDSN.empty() )
		{
                  
                               
			                               
                          
			                           
                                                      
                                                                         
                        
                 

     
        
			if ( isOldStyleNoPwdConnectionString(sNoPwdConnectString) ) {
				                                                           
				sConnectStr = konga::ReplacePassword(sNoPwdConnectString, sPassword);
			}
			else {
				                                                            
				if ( !sPassword.empty() ) {
					sConnectStr = sNoPwdConnectString;
					addSemiColonIfNeeded(sConnectStr);
					if ( !konga::HasPwd(sConnectStr) ) {
						sConnectStr += ("Pwd=" + sPassword + ";");
					}
				}
				else
					sConnectStr = sNoPwdConnectString;
			}
		}
		else {
			sConnectStr = "DSN=" + sDSN + ";UID=" + sLogin + ";PWD=" + sPassword;
		}
	}

	                                              
	                                                                    
	                                                   
	addSemiColonIfNeeded(sConnectStr);

	                                   
               
                                                                                      
                                                                                          
                  
}

                                            

                                                   
                                                               

OdbcConnectionString::OdbcConnectionString(const std::string& sConnectionString) {
	string::size_type istart = 0, iend = 0;

	string sKey;
	while ( (iend = sConnectionString.find("=", istart)) != string::npos ) {
		sKey += sConnectionString.substr(istart, iend-istart);
		if ( sConnectionString.at(iend+1) == '=' ) {
			sKey += "=";
			istart = iend + 2;
			continue;
		}

		                                            
		if ( sKey.empty() ) {
			istart = iend+1;
			continue;
		}

		                                                                           
		string::size_type iNonBlank = sConnectionString.find_first_not_of(" ", iend + 1);
		if ( iNonBlank == string::npos ) {
			                                           
			set(sKey, "");
			break;
		}

		const char c = sConnectionString.at(iNonBlank);
		                                                                    
		                                                                             
		  
                                 
                                               
   
                        
                                
                 
                
                          
            
   
         {
			               
			iend = sConnectionString.find(";", iNonBlank);
			if ( iend == string::npos ) {
				set(sKey, sConnectionString.substr(iNonBlank));
				break;
			}
			else {
				set(sKey, sConnectionString.substr(iNonBlank, iend-iNonBlank));
				sKey.clear();
				istart = iend + 1;
				continue;
			}
		}
	}
}

namespace {
	const string KEY_SERVER("SERVER");
	const string KEY_DRIVER("DRIVER");
	const string KEY_UID("UID");
	const string KEY_DATABASE("DATABASE");
	const string KEY_PWD("PWD");
}

string OdbcConnectionString::getServer() const {
	return get(KEY_SERVER);
}

string OdbcConnectionString::getDriver() const {
	return get(KEY_DRIVER);
}

string OdbcConnectionString::getUid() const {
	return get(KEY_UID);
}

string OdbcConnectionString::getDatabase() const {
	return get(KEY_DATABASE);
}

string OdbcConnectionString::getPwd() const {
	return get(KEY_PWD);
}

void OdbcConnectionString::setServer(const string& sServer) {
	set(KEY_SERVER, sServer);
}

void OdbcConnectionString::setDriver(const string& sDriver) {
	set(KEY_DRIVER, sDriver);
}

void OdbcConnectionString::setUid(const string& sUid) {
	set(KEY_UID, sUid);
}

void OdbcConnectionString::setDatabase(const string& sDatabase) {
	set(KEY_DATABASE, sDatabase);
}

void OdbcConnectionString::setPwd(const std::string& sPwd) {
	set(KEY_PWD, sPwd);
}

string OdbcConnectionString::get(const string& sKey) const {
	IcaseStringMapT::const_iterator cit = m_data.find(sKey);
	if ( cit ==m_data.end() )
		return string();
	
	return cit->second;
}

void OdbcConnectionString::set(const string& sKey_in, const string& sValue_in) {
	const string sKey(TrimString(sKey_in)), sValue(TrimString(sValue_in));
	IcaseStringMapT::iterator it = m_data.find(sKey);
	if ( it == m_data.end() ) {
		                   
		m_data.insert(IcaseStringMapT::value_type(sKey, sValue));
		m_ordered_keys.push_back(sKey);
	}
	else {
		it->second = sValue;
	}
}

string OdbcConnectionString::get() const {
	string retVal;
	for( std::vector<std::string>::const_iterator keyIt = m_ordered_keys.begin();
		keyIt != m_ordered_keys.end(); ++keyIt )
	{
		const string& sKey = *keyIt;
		if ( sKey.empty() )
			continue;

		IcaseStringMapT::const_iterator valueIt = m_data.find(sKey);
		if ( valueIt == m_data.end() )
			continue;

		if ( !retVal.empty() ) retVal += ";";
		retVal += sKey;
		retVal += "=";
		const string& sValue = valueIt->second;
		if ( sValue.empty() )
			continue;

		                                                          
                               
                                  
                                    
                                    
          
                                            
				retVal += sValue;
         
                                     
     
	}
	return retVal;
}
