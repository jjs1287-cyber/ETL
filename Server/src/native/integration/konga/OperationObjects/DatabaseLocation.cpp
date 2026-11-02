/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <sstream>	                

#include <konga/Utils/OdbcConnectionStringUtils.h>	                  

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/ConnectionString.h>

#include <konga/ItemProperties/Definitions.h>

#include "DatabaseLocation.h"

using namespace konga;
using std::string;
using std::stringstream;

TransactionIsolationLevel DatabaseLocation::fromClientIdentifier(int iClientId) {
	switch(iClientId) {
		case 0:
			return UNDEFINED;
		case 1:
			return READ_UNCOMMITTED;
		case 2:
			return READ_COMMITTED;
		case 4:
			return REPEATABLE_READ;
		case 8:
			return SERIALIZABLE;
	};
	return UNDEFINED;
}

std::string DatabaseLocation::getConnectString() const {
	string stConnectString;

	                            
	const string& stServer = getServer();
	const string& stDriver = getDriver();
	const string& stDbName = getDatabaseName();
	const string& stUid = getUid();
	const string& stPwd = getDecryptedPassword();
	const string& stDsn = getDSN();
	const long port = getPort();

	const string& stNoPwdConnectionString =  m_CommonData.m_stConnectString;
	const string& stAdditionalConnectParameters = getAdditionalConnectStringParameters();

	string stRetVal;
	GetConnectionString(stServer, port, stDriver, stDbName, stUid, stPwd, "", stDsn,
		"", stNoPwdConnectionString, stAdditionalConnectParameters, false, stRetVal);
	return replace_de(stRetVal);
}

TransactionIsolationLevel DatabaseLocation::getTransactionIsolationLevel() const {
	if ( !m_extendedProperties.IsKeyDefined(DB_LOCATION_TRANSACTION_ISOLATION_LEVEL) )
		return konga::UNDEFINED;

	return fromClientIdentifier(m_extendedProperties.GetInt(DB_LOCATION_TRANSACTION_ISOLATION_LEVEL));
}

void DatabaseLocation::testConnection(StatusReceiver& sr) const {
	SmartDbPtr<DbConnection> pConnection(DbConnection::MakeInstance() );
	sr.Status("MakeInstance()");

	pConnection->SetLoginTimeout(getLoginTimeOut());
	const string& cs = getConnectString();
	sr.Status("About to connect using connection string " + konga::ReplacePassword(cs, "***"));
	pConnection->Open(cs);
	sr.Status("Connection successful");

	const TransactionIsolationLevel txnLevel = getTransactionIsolationLevel();
	if ( txnLevel != konga::UNDEFINED ) {
		sr.Status("About to set transaction isolation level to " + toString(txnLevel));
		pConnection->SetTransactionIsolationLevel(txnLevel);
	}
	sr.Status("About to close the connection");
}
