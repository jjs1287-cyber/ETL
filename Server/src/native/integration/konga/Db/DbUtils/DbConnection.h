/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef DBCONNECTION_H_
#define DBCONNECTION_H_

#ifdef WIN32
	#include <windows.h>
#endif
#include <sql.h>
#include <string>

namespace konga {

	enum TransactionIsolationLevel {
		UNDEFINED = SQL_TXN_READ_UNCOMMITTED - 1,
		READ_UNCOMMITTED = SQL_TXN_READ_UNCOMMITTED,
		READ_COMMITTED = SQL_TXN_READ_COMMITTED,	                            
		REPEATABLE_READ = SQL_TXN_REPEATABLE_READ,
		SERIALIZABLE = SQL_TXN_SERIALIZABLE
	};

	class DbConnection {

	public:
		                                                                  
		                                                                      
		                                      

		static DbConnection* MakeInstance(const std::string& stConnectString = "");

		DbConnection() {}
		virtual ~DbConnection() {}
		
		virtual void Open(const std::string& stConnectString) = 0;
		virtual bool IsOpen() = 0;
		virtual void Close() = 0;
		virtual void SetAutoCommit(bool b) = 0;
		virtual bool GetAutoCommit() = 0;
		virtual void Commit() = 0;
		virtual void Rollback() = 0;
		virtual void ExecuteSql(const std::string& stSql) = 0;
		virtual long GetAffectedRowCount() = 0;

		virtual std::string GetConnectionString() = 0;

		virtual SQLHENV GetEnvironmentHandle() = 0;
		virtual SQLHDBC GetConnectionHandle() = 0;
		virtual void SetTransactionIsolationLevel(TransactionIsolationLevel level) = 0;
		virtual TransactionIsolationLevel GetTransactionIsolationLevel() = 0;

		virtual void SetLoginTimeout(unsigned long nTimeoutInSecs) = 0;
		virtual void SetQueryTimeout(unsigned long nTimeoutInSecs) = 0;

	};

}                       


#endif                  
