/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBUTILS_CONNECTION_STRING_H
#define KONGA_DBUTILS_CONNECTION_STRING_H

#include <string>
#include <vector>

#include <konga/Utils/MapUtils.h>

namespace konga {

	                                                                     
	                                                                                             
	                                                                            
	                                                                    
	             
	                                                                                     
	                                        
	                                                                      
	void GetConnectionString(const std::string& sServer, const long port, const std::string& sDriver,
		const std::string& sDbName, const std::string& sLogin,
		const std::string& sPassword, const std::string& sPath,
		const std::string& sDSN, const std::string& sFile_Name,
		const std::string& sNoPwdConnectString, const std::string& sAdditionalConnectParameters,
		bool bPSQueryType,
		std::string& sConnectStr);


	class OdbcConnectionString {
	public:
		OdbcConnectionString(const std::string& sConnectionString);
		~OdbcConnectionString(void) {}

		std::string get() const;

		std::string getServer() const;
		std::string getDriver() const;
		std::string getUid() const;
		std::string getDatabase() const;
		std::string getPwd() const;

		void setServer(const std::string& sServer);
		void setDriver(const std::string& sDriver);
		void setUid(const std::string& sUid);
		void setDatabase(const std::string& sDatabase);
		void setPwd(const std::string& sPwd);

	private:
		std::string get(const std::string& sKey) const;
		void set(const std::string& sKey, const std::string& sValue);

	private:
		std::vector<std::string> m_ordered_keys;
		IcaseStringMapT m_data;
	};

}

#endif                                     

