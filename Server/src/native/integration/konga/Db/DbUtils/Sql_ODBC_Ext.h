/*
 * Copyright (c) 2011-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SQL_ODBC_EXT_H
#define KONGA_SQL_ODBC_EXT_H

#include <sql.h>

namespace konga
{

	class ODBC_ResultSetInfo
	{
		SQLHANDLE m_hSqlStmt;
		SQLRETURN rc;
		SQLSMALLINT SQLNumCols;
	public:
		ODBC_ResultSetInfo(SQLHANDLE m_hSqlStmt, SQLRETURN rc):m_hSqlStmt(m_hSqlStmt),rc(rc){
		}
		SQLRETURN moveToQueryResultSet();
	public:
		SQLRETURN readNumCols();
		bool isQueryResultSet() { return SQLNumCols>0;}
		SQLSMALLINT getNumCols() { return SQLNumCols;}
	};

	inline SQLRETURN SQLFreeHandle_STMT(SQLHANDLE m_hSqlStmt)
	{                                                                                                
		while (SQL_SUCCEEDED(::SQLMoreResults(m_hSqlStmt))){
		}
		return ::SQLFreeHandle(SQL_HANDLE_STMT, m_hSqlStmt);
	}


}                       

#endif                        
