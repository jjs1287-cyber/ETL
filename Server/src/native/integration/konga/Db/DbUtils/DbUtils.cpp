/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string.h>
#include <cstdio>
#ifdef WIN32
#include <windows.h>
#endif
#include <sqlext.h>		                      

#include <string>
#include <vector>

#include <cassert>

#include <boost/function.hpp>

#include <konga/Utils/PortDefs.h>		                                      
#include <konga/Utils/KString.h>
#include <konga/Utils/Settings.h>		                      
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/ProcessUtils.h>	               
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DtlDbUtils/DtlDbLookup.h>

#include "DbUtils.h"
#include "DbLookup.h"
#include "DynamicDbRecordSet.h"
#include "DbConnection.h"
#include "DbException.h"
#include "TableColumnsOdbc.h"	                    
#include "OdbcDbLookup.h"

using namespace konga;
using std::string;
using std::vector;

konga::DbConnection* konga::GetDbConnection(const string& stConnectString          )
{
	return DbConnection::MakeInstance(stConnectString);
}

konga::DbConnection* konga::GetBackEndDbConnection(const std::string& stDbName) {
	try {
		SmartDbPtr<DbConnection> pDbConn( konga::GetDbConnection(DBConnectionInfo(stDbName).connectionString()) );
		SetClientEncoding(*pDbConn);

		const string dbVersion( GetDatabaseVersionAsString(pDbConn->GetConnectionHandle()) );
		unsigned int majorVersion = 0, minorVersion = 0;
		const int versionParts = std::sscanf(dbVersion.c_str(), "%u.%u", &majorVersion, &minorVersion);
		if ( versionParts >= 1 && (majorVersion > 8 || (majorVersion == 8 && minorVersion >= 3)) ) {
			                                                                                           
			                                                                                   
			pDbConn->ExecuteSql("SET standard_conforming_strings = off; SET escape_string_warning = off");
		}
		return pDbConn.Release();
	}
	catch ( const DbException& dbEx ) {
		string stErr = "Failed to connect to back-end database \"" + stDbName + "\".\n"
			+ dbEx.GetErrorString() + "\nMake sure the database is running and etlforge.conf has the correct connection information."
			+ "\n[" + dbEx.GetStateNativeOrigin() + "]";
		throw CrException(stErr);
	}
}

DbConnection* konga::GetDbConnectionWithRetries(const string& connectionString,
					boost::function1<void,const string&> statusCallback, unsigned long nRetries            , unsigned long nMilliSleep             ) {
	string sConnectionError;
	DbException dbE;
	for(unsigned long iConnectionAttempt=0; iConnectionAttempt<nRetries; ++iConnectionAttempt) {
		try
		{
			return konga::GetDbConnection(connectionString);
		}
		catch(const konga::DbException& e )
		{
			dbE = e;
			sConnectionError = "Failed to connect to " + ReplacePassword(connectionString, "<REMOVED>") + 
				": " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "].";

			                                    
			if( (iConnectionAttempt % 10) == 0 )
				statusCallback("Waiting for the database system to respond. Last error was: " + sConnectionError);
			if(iConnectionAttempt + 1 < nRetries)
				konga::MilliSleep(nMilliSleep);
		}
	}
	throw dbE;
}

void konga::SetClientEncoding(DbConnection& conn) {
	                  
	conn.ExecuteSql("SET CLIENT_ENCODING TO 'UTF8'");
}

konga::DynamicDbRecordSet* konga::CreateDynamicDbRecordSet(DbConnection* pConn, const string& stSql           )
{
    return DynamicDbRecordSet::MakeInstance(pConn, stSql);
}

konga::DbLookup* konga::CreateDbLookup(const string& stConnectionString, const TransactionIsolationLevel txnIsolationLevel)
{
	if ( konga::GetOptionAsBoolean("TransformationEngine", "UseOldDbLookup", false) ) {
		return new DtlDbLookup(stConnectionString, txnIsolationLevel);
	}
	else {
		return new OdbcDbLookup(stConnectionString, txnIsolationLevel);
	}
}

                                    
                          
#define ORA_DRIVER1	"MSORCL32.DLL"
#define ORA_DRIVER2 "ORACL"
#define ORA_DRIVER3 "SQORA"

bool konga::DriverIsOracle(const string& stDriverName)
{
	string tmp(stDriverName);
	ToUpper(tmp);
	return tmp.find(ORA_DRIVER3)!=string::npos ||
		   tmp.find(ORA_DRIVER2)!=string::npos||
		   tmp.find(ORA_DRIVER1)!=string::npos;
}

bool konga::DriverIsOracleODBC(const string& stDriverName) {
	string tmp(stDriverName);
	ToUpper(tmp);
	return tmp.find(ORA_DRIVER3) != string::npos;
}

bool konga::DriverIsMSOracleODBC(const string& stDriverName) {
	string tmp(stDriverName);
	ToUpper(tmp);
	return tmp.find(ORA_DRIVER1) != string::npos;
}

void konga::SpaceInNameHandler(string& stName, const string& stDriverName)
{
                           
}

                                         
namespace {
	void CollapseString(KString& str)
	{
		int iPos = str.Find("  ");
		while(iPos != -1)
		{
			str.Delete(iPos);
			iPos = str.Find("  ");
		}
	}
}

bool konga::ModifyDBSourceSelect(const char* szSqlStmt)
{
	if(strlen(szSqlStmt)==0)return true;	                                                        

	KString csSql(szSqlStmt);
	CollapseString(csSql);
	csSql.MakeUpper();
	csSql.TrimLeft();

	if(csSql.Find(" UNION ")!=-1)return false;
	if(csSql.Find(" MINUS ")!=-1)return false;
	if(csSql.Find(" INTERSECT ")!=-1)return false;
	if(csSql.Find("SELECT *")==0)return true;
	if(csSql.Find("SELECT ALL *")==0)return true;
	if(csSql.Find("SELECT DISTINCT *")==0)return true;
	int iPos;
	if((iPos=csSql.Find("SELECT TOP "))==0)
	{
		int k = iPos + strlen("SELECT TOP ");
		while(isdigit(csSql[k]) || csSql[k]=='.' || csSql[k]==' ')
			++k;
		csSql.Delete(0, k);
		if(csSql.Find("*")==0)return true;
		if(csSql.Find("WITH TIES *")==0)return true;
		if(csSql.Find("PERCENT *")==0)return true;
		if(csSql.Find("PERCENT WITH TIES *")==0)return true;
	}

	return false;
}

namespace {
	char* defaultDriverName[]={"PostgreSQL","SQL Server","MySQL","Oracle","Access","Excel", 0};
}

string konga::GetQuoteBeginForDriver(const string& sDriverName, const string& defaultValue) {
	string beginQuote = GetOptionAsString("DbDrivers", sDriverName + " Quote Begin", "");
	if(beginQuote.empty()){
		string sDriverName_upper=sDriverName;
		konga::ToUpper(sDriverName_upper);
		char*def;
		for(int i=0; def=defaultDriverName[i]; ++i){
			string sDef_upper(def);
			konga::ToUpper(sDef_upper);
			if(sDriverName_upper.find(sDef_upper)!=string::npos){
				return GetOptionAsString("DbDrivers", string(def) + " Quote Begin", defaultValue);
			}
		}
		return defaultValue;
	}
	return beginQuote;
}

string konga::GetQuoteEndForDriver(const string& sDriverName, const string& defaultValue) {
	string endQuote = GetOptionAsString("DbDrivers", sDriverName + " Quote End", "");
	if(endQuote.empty()){
		string sDriverName_upper=sDriverName;
		konga::ToUpper(sDriverName_upper);
		char*def;
		for(int i=0; def=defaultDriverName[i]; ++i){
			string sDef_upper(def);
			konga::ToUpper(sDef_upper);
			if(sDriverName_upper.find(sDef_upper)!=string::npos){
				return GetOptionAsString("DbDrivers", string(def) + " Quote End", defaultValue);
			}
		}
		return defaultValue;
	}
	return endQuote;
}

namespace {
	                                                                                   
	void defineDbDriverDependentStrImpl(const KString& csConnectString, string& sDriver) {
		sDriver.clear();
		if(csConnectString.IsEmpty())return;

		const char*driverTag="DRIVER=";
		KString ksConnectStringUpper(csConnectString);
		ksConnectStringUpper.MakeUpper();
		const int iDriverPos = ksConnectStringUpper.Find(driverTag);
		if (iDriverPos >= 0){
			int iSemiColon = csConnectString.Find(';', iDriverPos);
			int iStartPos = iDriverPos + strlen(driverTag);;
			if(iSemiColon>iStartPos){
				sDriver=(const char*)csConnectString.Mid(iStartPos,(iSemiColon-iStartPos));
				size_t len=sDriver.length();
				if(len>2 && sDriver.at(0)=='{' && sDriver.at(len-1)=='}'){
					sDriver=sDriver.substr(1,len-2);
				}
			}
		}
	}
}

void konga::defineDbDriverDependentStr(const string& sConnectString, string& sDriver) {
	defineDbDriverDependentStrImpl(KString(sConnectString), sDriver);
}

bool konga::IsDbColumnDefaultValueIgnored(const string& sDriverName){
	string tmp=GetOptionAsString("DbDrivers", sDriverName + " IgnoreDefault", "Yes");
	return stricmp(tmp.c_str(),"yes")==0;
}

bool konga::IsDataTypeNumeric(int sqlDataType) {
	switch(sqlDataType) {
		                                                    
		case SQL_NUMERIC:
		case SQL_DECIMAL:
		case SQL_INTEGER:
		case SQL_SMALLINT:
		case SQL_FLOAT:
		case SQL_REAL:
		case SQL_DOUBLE:
		case SQL_BIGINT:
		case SQL_TINYINT:
		case SQL_BIT:
			return true;
		default:
			return false;
	}
}

bool konga::IsDataTypeDateTime(int sqlDataType) {
	switch(sqlDataType) {
		case SQL_DATETIME:
		case SQL_TYPE_DATE:
		case SQL_TYPE_TIME:
		case SQL_TYPE_TIMESTAMP:
		case SQL_TIME:
		case SQL_TIMESTAMP:
			return true;
		default:
			return false;
	}
}

bool konga::IsBinaryData(int type){
	return type==SQL_BINARY || type==SQL_VARBINARY || type==SQL_LONGVARBINARY;
}

bool konga::IsWideCharData(const int type){
	return type==SQL_WCHAR || type==SQL_WVARCHAR || type==SQL_WLONGVARCHAR;
}

void konga::LockTableForUpdate(const string& stTableName, DbConnection& dbConn) {
	dbConn.ExecuteSql("LOCK " + stTableName + " IN SHARE UPDATE EXCLUSIVE MODE");
}

long konga::GetNextId(const string& stTableName, const string& stIdColName, DbConnection& dbConn) {
	if ( dbConn.GetAutoCommit() != false )
		throw CrException("Internal Error: GetNextId cannot be called using a connection with auto commit = true.");

	const string stGetHighestIdSql(
		"SELECT MAX(" + stIdColName + ") FROM " + stTableName
	);

	                                                          
	LockTableForUpdate(stTableName, dbConn);

	SmartDbPtr<DynamicDbRecordSet> rs(DynamicDbRecordSet::MakeInstance(&dbConn, stGetHighestIdSql));
	rs->Open();
	if( !rs->Next() )
		throw CrException("Well, it did happen after all! "
			+ string(__FILE__) + " " + toString(__LINE__));
	const long lMaxId = atol(rs->GetFieldValue(0).c_str());
	return (( lMaxId > 0 ) ? lMaxId : 0) + 1;
}

string konga::GetOdbcVersionAsString(SQLHDBC h) {
	assert(h);
	SQLCHAR pcVersion[50];
	memset(pcVersion, 0, sizeof(pcVersion));
	SQLSMALLINT nLen=0;
	RETCODE retcode = ::SQLGetInfo(h, SQL_DRIVER_ODBC_VER, pcVersion, 50, &nLen);
	return retcode == SQL_SUCCESS ? reinterpret_cast<const char*>(pcVersion) : "";
}

void konga::SetTransactionIsolationLevel(SQLHDBC h, TransactionIsolationLevel txnIsolationLevel) {
	if ( txnIsolationLevel == konga::UNDEFINED )
		return;

	assert(h);

	SQLUINTEGER val = txnIsolationLevel;
	SQLRETURN retcode = ::SQLSetConnectAttr(h, SQL_ATTR_TXN_ISOLATION, reinterpret_cast<SQLPOINTER>(val), SQL_IS_UINTEGER);
	if ( retcode == SQL_INVALID_HANDLE )
		throw DbException("Failed to set Transaction Isolation Level: Invalid handle.");
	if ( retcode == SQL_ERROR )
		throw DbException("Failed to set Transaction Isolation Level: " + GetError(SQL_HANDLE_DBC, h));
}

string konga::GetSqlIdentifierQuoteCharacter(SQLHDBC h) {
	assert(h);
	SQLCHAR pcQuoteChar[5];
	memset(pcQuoteChar, 0, sizeof(pcQuoteChar));
	SQLSMALLINT nLen=0;
	RETCODE retcode = ::SQLGetInfo(h, SQL_IDENTIFIER_QUOTE_CHAR, pcQuoteChar, 5, &nLen);
	if ( retcode != SQL_SUCCESS || nLen == 0 )
		return string();
	return string( reinterpret_cast<const char*>(pcQuoteChar) );
}

float konga::GetOdbcVersion(SQLHDBC h) {
	const string& sVersion = GetOdbcVersionAsString(h);
	if( sVersion.empty() ) return 0.f;
	return atof(sVersion.c_str());
}

                                                                                            
                                                                                               
                                                                                        
                                         
string konga::GetDatabaseVersionAsString(SQLHDBC h) {
	SQLSMALLINT nLen=0;
	SQLCHAR pcVersion[1024];
	memset(pcVersion, 0, sizeof(pcVersion));
	RETCODE retcode = ::SQLGetInfo(h, SQL_DBMS_VER, pcVersion, 1024, &nLen);
	                               
	                                                                  
	return retcode == SQL_SUCCESS ? reinterpret_cast<const char*>(pcVersion) : "";
}

string konga::GetDatabaseName(SQLHDBC h) {
	SQLSMALLINT nLen=0;
	SQLCHAR pcName[1024];
	memset(pcName, 0, sizeof(pcName));
	RETCODE retcode = ::SQLGetInfo(h, SQL_DBMS_NAME, pcName, 1024, &nLen);
	return retcode == SQL_SUCCESS ? reinterpret_cast<const char*>(pcName) : "";
}

namespace {
	const int MAXBUFLEN = 1024;
}

string konga::GetError(SQLSMALLINT handle_type, SQLHANDLE handle)
{
	UCHAR szSqlState[MAXBUFLEN] = "", szErrorMsg[MAXBUFLEN] = "";
	SDWORD nativeError = 0L;
	SWORD errorMsg = 0;
	SQLSMALLINT recNmbr = 1;

	const SQLRETURN rc = ::SQLGetDiagRec(handle_type, 
								  handle,
								  recNmbr,
								  szSqlState,
								  &nativeError,
								  szErrorMsg,
								  MAXBUFLEN - 1,
								  &errorMsg);
	if ( !SQL_SUCCEEDED(rc) ) return string("Failed to get the SQL error from ODBC.");

	string stError;
	if( szErrorMsg && *szErrorMsg ) {
		stError = string(reinterpret_cast<const char*>(szErrorMsg));
	}
	if( szSqlState && *szSqlState ) {
		const string stSqlState(reinterpret_cast<const char*>(szSqlState));
		if ( stError.empty() ) {
			stError = "Sql State: " + stSqlState;
		}
		else {
			stError += " (SqlState: " + stSqlState + ").";
		}
	}
	return stError;
}

namespace {

	const int not_preferred = 999;

	                                                     
	                                                      
	                                                                        
	int isPreferredDriver(const string& driver_name, const vector<string>& preferred_drivers) {
		int i = 0;
		for( vector<string>::const_iterator cit = preferred_drivers.begin(); cit != preferred_drivers.end(); ++cit, ++i ) {
			if( EqualsIgnoreCase(*cit, driver_name) ) {
				return i;
			}
		}
		return not_preferred;
	}
}

string konga::GetDatabaseDriverName() {

	const string stDefaultDriverSignature("PostgreSQL");
	vector<string> preferred_drivers;
#ifdef WIN32
	preferred_drivers.push_back("PostgreSQL ODBC Driver(UNICODE)");
	preferred_drivers.push_back("PostgreSQL Unicode");
	preferred_drivers.push_back(stDefaultDriverSignature);
#else
	preferred_drivers.push_back("PostgreSQL-psql");
	preferred_drivers.push_back(stDefaultDriverSignature);
#endif

	OdbcColumns odbc;
	vector<string> vsDriverNames;
	odbc.GetDrivers(vsDriverNames);

	string sDriverName;
	int preferred_level = not_preferred;
	vector<string>::const_iterator citDriverName = vsDriverNames.begin();
	for(; citDriverName != vsDriverNames.end(); ++citDriverName) {
		const string& stThisDriver = *citDriverName;
		const int tmp = isPreferredDriver(stThisDriver, preferred_drivers);
		if ( tmp < preferred_level ) {
			                                                  
			if ( tmp == 0 ) {
				                                              
				sDriverName = stThisDriver;
				break;
			}
			sDriverName = stThisDriver;
			preferred_level = tmp;
		}
		else if( preferred_level == not_preferred && stThisDriver.find(stDefaultDriverSignature) != string::npos ) {
			                                                                                         
			                                  
			sDriverName = stThisDriver;
		}
	}

	if( sDriverName.empty() )
		sDriverName = "NO_SUITABLE_DRIVER_FOUND";

	return sDriverName;
}

namespace {
	const char SqlEscapeEscapeChars[] = "'\\";
}

bool konga::requireSqlEscape(const string& stInput){
	return stInput.find_first_of(SqlEscapeEscapeChars, 0) != string::npos;
}

void konga::SqlEscape(const string& stInput, string& stSqlString) {

	string::size_type ipos, startpos = 0;
	stSqlString.clear();
	while ( (ipos = stInput.find_first_of(SqlEscapeEscapeChars, startpos)) != string::npos ) {
		stSqlString += stInput.substr(startpos, ipos-startpos+1);
		stSqlString += stInput.at(ipos);
		startpos=ipos+1;
	}

	if ( startpos == 0 ) {
		                                                                  
		                             
		stSqlString = stInput;
	}
	else if ( startpos < stInput.size() ) {
		stSqlString += stInput.substr(startpos);
	}
}

string konga::SqlEscape(const string& stSql) {
	string stSql_escaped;
	SqlEscape(stSql, stSql_escaped);
	return stSql_escaped;
}

namespace {
	const char SqlLikeEscapeEscapeChars[] = "%_'\\";
	const string SQL_STATE_40001("40001");
}

void konga::SqlLikeEscape(const string& stInput, string& stSqlString, char escapeChar) {
	string::size_type ipos, startpos = 0;
	stSqlString.clear();
	while ( (ipos = stInput.find_first_of(SqlLikeEscapeEscapeChars, startpos)) != string::npos ) {
		if ( stInput.at(ipos) == '%' || stInput.at(ipos) == '_' ) {
			                                           
			stSqlString += stInput.substr(startpos, ipos-startpos);
			stSqlString += escapeChar;
			if ( escapeChar == '\\' )
				stSqlString += escapeChar;	                                                                       
			stSqlString += stInput.at(ipos);
		}
		else if ( stInput.at(ipos) == '\\' ) {
			stSqlString += stInput.substr(startpos, ipos-startpos+1);
			                                                                               
			if ( escapeChar == '\\' )
				stSqlString += "\\\\\\";	                                                                        
			else
				stSqlString += "\\";		                                              
		}
		else {
			                                 
			stSqlString += stInput.substr(startpos, ipos-startpos+1);
			stSqlString += stInput.at(ipos);
		}
		startpos=ipos+1;
	}

	if ( startpos == 0 ) {
		                                                                  
		                             
		stSqlString = stInput;
	}
	else if ( startpos < stInput.size() ) {
		stSqlString += stInput.substr(startpos);
	}
}

string konga::SqlLikeEscape(const string& stSql, char escapeChar) {
	string stSql_escaped;
	SqlLikeEscape(stSql, stSql_escaped, escapeChar);
	return stSql_escaped;
}

bool konga::IsSerializationFailure(const DbException& e) {
	const string& sqlState = e.GetSqlState();
	const string& sqlError = e.GetErrorString();
	if ( sqlState == SQL_STATE_40001 || sqlError.find("could not serialize access due to concurrent update") != string::npos ) {
		return true;
	}
	return false;
}

