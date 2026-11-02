/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "CrException.h"
#include "Settings.h"

#include "DBConnectionInfo.h"
#include "PortDefs.h"	             

using namespace konga;
using namespace std;


void DBConnectionInfo::LoadConnectionInfo(const std::string& dbName, DBConnectionInfo& dbConnInfo) {
	const string stDbInfoSection("DbInfo");

	const string& stUser = GetOptionAsString(stDbInfoSection, "User", "");
	if ( stUser.empty() )
		throw CrException("No database user specified in the " + stDbInfoSection + " section of the configuration file.\n" \
		" Cannot establish connection to the backend database. Please check the configuration file.");

	const string& stPassword = GetOptionAsString(stDbInfoSection, "Password", "");
	const string& stServer = GetOptionAsString(stDbInfoSection, "Server", "127.0.0.1");

	const string& stDriver = GetOptionAsString(stDbInfoSection, "Driver", "");
	if ( stDriver.empty() )
		throw CrException("No database driver specified in the " + stDbInfoSection + " section of the configuration file.\n" \
		" Cannot establish connection to the backend database. Please check the configuration file.");

	const int iPort = GetOptionAsInt(stDbInfoSection, "Port", 5432);

	string stAdditional = GetOptionAsString(stDbInfoSection, "AdditionalParameters", "");

	if( iPort ) {
		if( !stAdditional.empty() && *(stAdditional.end()-1) != ';' )
			stAdditional += ';';
		stAdditional += "Port=";
		stAdditional += toString(iPort);
	}

	if( strcasecmp(dbName.c_str(), "PDb") == 0 ) {
		const string& stPDb =GetOptionAsString(stDbInfoSection, "PDb", "PDb");
		dbConnInfo.setInfo(stUser, stPassword, stServer, stPDb, stDriver, stAdditional);
	} else if( strcasecmp(dbName.c_str(), "IDb") == 0 ) {
		const string& stIDb = GetOptionAsString(stDbInfoSection, "IDb", "IDb");
		dbConnInfo.setInfo(stUser, stPassword, stServer, stIDb, stDriver, stAdditional);
	} else if( strcasecmp(dbName.c_str(), "MsgDb") == 0 ) {
		const string& stMsgDb = GetOptionAsString(stDbInfoSection, "MsgDb", "MsgDb");
		dbConnInfo.setInfo(stUser, stPassword, stServer, stMsgDb, stDriver, stAdditional);
	} else if( strcasecmp(dbName.c_str(), "TranDb") == 0 ) {
		const string& stTranDb = GetOptionAsString(stDbInfoSection, "TranDb", "TranDb");
		dbConnInfo.setInfo(stUser, stPassword, stServer, stTranDb, stDriver, stAdditional);
	} else if( strcasecmp(dbName.c_str(), "B2BIDb") == 0 ) {
		const string& stB2BIDb = GetOptionAsString(stDbInfoSection, "B2BIDb", "B2BIDb");
		dbConnInfo.setInfo(stUser, stPassword, stServer, stB2BIDb, stDriver, stAdditional);
	} else
		dbConnInfo.setInfo(stUser, stPassword, stServer, dbName, stDriver, stAdditional);
}

void DBConnectionInfo::buildDsn() {
	m_sConnect = "UID=";
	if( !m_uid.empty() ) m_sConnect += m_uid;
	m_sConnect += ";PWD=";
	if( !m_pwd.empty() ) m_sConnect += m_pwd;
	m_sConnect += ";SERVER=" + m_dbServer + ";DRIVER={" + m_driver + "};DATABASE=" + m_dbName + ";";
	if ( !m_additional.empty() ) m_sConnect +=  m_additional + ";";
}

string DBConnectionInfo::PrettyConnectInfoString() const
{
	string res = "UID=\"" + m_uid;
	res += "\", DB Server Name=\"" + m_dbServer;
	res += "\", DB Name=\"" + m_dbName;
	if( !m_driver.empty() )
		res += "\", Driver=\"" + m_driver;
	if ( !m_additional.empty() )
		res += "\", Additional Parameters=\"" + m_additional;
	res += "\". (Password not shown for security reasons.)";
	return res;
}
