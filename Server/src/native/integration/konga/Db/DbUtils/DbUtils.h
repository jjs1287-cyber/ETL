/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBUTILS_H
#define KONGA_DBUTILS_H

#include <string>

#ifdef WIN32
	#include <windows.h>
#endif

#include <sqltypes.h>

#include <boost/function.hpp>

#include "DbConnection.h"
#include "DbException.h"
#include "SmartDbPtr.h"

const int MODE_INSERT_UPDATE=1, MODE_INSERT=2, MODE_UPDATE=3, 
	MODE_UPDATE_INSERT=4, MODE_DELETE=5, MODE_SOURCE_UPDATE=6;

#define EXCEL_DRIVER "ODBCJT32.DLL"
#define FILEMAKER_DRIVER_1 "FMFMP50.DLL"
#define FILEMAKER_DRIVER_2 "FILEMAKER"

namespace konga
{
    class DynamicDbRecordSet;
    class DbLookup;

                                                                      
	                                                                      
    DbConnection* GetDbConnection(const std::string& stConnectString = "");

	                                         
	DbConnection* GetBackEndDbConnection(const std::string& stDbName);

	DbConnection* GetDbConnectionWithRetries(const std::string& connectionString,
			boost::function1<void,const std::string&> statusCallback, unsigned long nRetries = 10, unsigned long nMilliSleep = 30000);

	                           
	void SetClientEncoding(DbConnection& conn);

	DynamicDbRecordSet* CreateDynamicDbRecordSet(DbConnection* pConn, const std::string& stSql = "");

	DbLookup* CreateDbLookup(const std::string& stConnectionString, const TransactionIsolationLevel txnIsolationLevel);

	                               
    bool DriverIsOracle(const std::string& stDriverName);

	                                             
	bool DriverIsOracleODBC(const std::string& stDriverName);

	bool DriverIsMSOracleODBC(const std::string& stDriverName);

    void SpaceInNameHandler(std::string& stName, const std::string& stDriverName = "");

    bool ModifyDBSourceSelect(const char* szSqlStmt);    

	std::string GetQuoteBeginForDriver(const std::string& sDriverName, const std::string& defaultValue);
	std::string GetQuoteEndForDriver(const std::string& sDriverName, const std::string& defaultValue);

	                                                                                    
	void defineDbDriverDependentStr(const std::string& sConnectString, std::string& sDriver);

	bool IsDbColumnDefaultValueIgnored(const std::string& sDriverName);

	                                                                     
	bool IsDataTypeNumeric(int sqlDataType);

	                                                                                     
	bool IsDataTypeDateTime(int sqlDataType);

	bool IsBinaryData(int sqlDataType);
	bool IsWideCharData(const int sqlDataType);
	
	                                                                                     
	                                                                                   
	                                                                                             
	                                                                                    
	                                                                   
	void LockTableForUpdate(const std::string& stTableName, DbConnection& dbConn);

	                                                                                       
	                                                               
	                                                                                 
	long GetNextId(const std::string& stTableName, const std::string& stIdColName, DbConnection& dbConn);

	                                                           
	                                                                   
	                                        
	std::string GetOdbcVersionAsString(SQLHDBC h);

	void SetTransactionIsolationLevel(SQLHDBC h, TransactionIsolationLevel txnIsolationLevel);

	                                                                           
	std::string GetSqlIdentifierQuoteCharacter(SQLHDBC h);

	float GetOdbcVersion(SQLHDBC h);

	std::string GetDatabaseVersionAsString(SQLHDBC h);
	std::string GetDatabaseName(SQLHDBC h);

    std::string GetError(SQLSMALLINT handle_type, SQLHANDLE handle);

	std::string GetDatabaseDriverName();

	   
                                                                 
                       
                                             
                                                                               
    
	void SqlEscape(const std::string& stInput, std::string& stSqlString);
	std::string SqlEscape(const std::string& stSql);
	bool requireSqlEscape(const std::string& stInput);

	   
                                                                 
                                                                         
                                                                         
                                                                          
                                                                              
   
    
	void SqlLikeEscape(const std::string& stInput, std::string& stSqlString, char escapeChar = '\\');
	std::string SqlLikeEscape(const std::string& stSql, char escapeChar = '\\');

	                                                                                                   
	bool IsSerializationFailure(const DbException& ex);

}                       


#endif                   
