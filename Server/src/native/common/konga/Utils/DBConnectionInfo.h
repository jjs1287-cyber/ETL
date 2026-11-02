/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef DB_CONNECTION_INFO_H
#define DB_CONNECTION_INFO_H

#include <string>

class DBConnectionInfo
{
public:
	DBConnectionInfo() {}

	                                               
	DBConnectionInfo(const std::string& dbName) : m_dbName(dbName) {
		LoadConnectionInfo(dbName, *this);
	}

	DBConnectionInfo(const std::string& sUid, const std::string& sPwd,
		const std::string& sDbServer, const std::string& sDbName, const std::string& sDriver) :
		m_uid(sUid), m_pwd(sPwd), m_dbServer(sDbServer), m_dbName(sDbName), m_driver(sDriver)
	{
		buildDsn();
	}

	virtual ~DBConnectionInfo() {}

	static void LoadConnectionInfo(const std::string& dbName, DBConnectionInfo& dbConnInfo);

	void setInfo(const std::string& sUid, const std::string& sPwd, const std::string& sDbServer,
		const std::string& sDbName, const std::string& sDriver, const std::string& sAdditionalParameters)
	{
		m_uid=sUid, m_pwd=sPwd, m_dbName=sDbName, m_dbServer=sDbServer, m_driver=sDriver, m_additional=sAdditionalParameters;
		buildDsn();
	}

	const char* connectionString() const { return m_sConnect.c_str(); }
	const char* uid() const { return m_uid.c_str(); }
                                                                     
	const char* pwd() const { return m_pwd.c_str(); }
	const char* dbServer() const { return m_dbServer.c_str(); }
	const char* dbName() const { return m_dbName.c_str(); }
	const char* driver() const { return m_driver.c_str(); }
	const char* additionalParameters() const { return m_additional.c_str(); }

	void uid(const std::string& sUid)  { m_uid = sUid; buildDsn(); }
	void pwd(const std::string& sPwd)  { m_pwd = sPwd; buildDsn(); }
	void dbServer(const std::string& sDbServer)  { m_dbServer = sDbServer; buildDsn(); }
	void dbName(const std::string& sDbName)  { m_dbName = sDbName; buildDsn(); }
	void driver(const std::string& sDriver)  { m_driver = sDriver; buildDsn(); }
	void additionalParameters(const std::string& sAdditional) { m_additional = sAdditional; buildDsn(); }

	bool IsSet() const { return !m_uid.empty(); }

	std::string PrettyConnectInfoString() const;

	std::string VerbosePrettyConnectInfoString() const
	{
		return "The Connection information was: " + PrettyConnectInfoString();
	}

protected:
	void buildDsn();

private:
	std::string m_uid, m_pwd, m_dbServer, m_dbName, m_sConnect, m_driver, m_additional;
};

#endif                        

