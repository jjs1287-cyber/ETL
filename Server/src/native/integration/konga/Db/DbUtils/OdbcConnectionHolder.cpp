/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <cassert>

#ifdef WIN32
	#include <windows.h>
	#include <konga/Utils/PortDefs.h>		                        
#endif

#include <sqlext.h>
#include <time.h>

#include "DbUtils.h"
#include "Sql_ODBC_Ext.h"
#include "OdbcConnectionHolder.h"

using namespace konga;
using std::string;

void OdbcConnectionHolder::Close()
{
    DisconnectAndCleanUp();
}

bool OdbcConnectionHolder::AllocateEnvironmentHandle() {
	if( !m_hSqlEnv ) {
		RETCODE rc = ::SQLAllocHandle(SQL_HANDLE_ENV, NULL, &m_hSqlEnv);
		if( !SQL_SUCCEEDED(rc) ) {
			AppendLastError("Failed to allocate the environment handle. " + GetError(SQL_HANDLE_ENV, m_hSqlEnv));
			return false;
		}

		                                             
		rc = ::SQLSetEnvAttr(m_hSqlEnv, SQL_ATTR_ODBC_VERSION, (SQLPOINTER)SQL_OV_ODBC3, 0);
		if( !SQL_SUCCEEDED(rc) ) {
			AppendLastError("Failed to set the ODBC version environment attribute. " + GetError(SQL_HANDLE_ENV, m_hSqlEnv));
			DisconnectAndCleanUp();
			return false;
		}
	}

	assert(m_hSqlEnv);
	return true;
}

bool OdbcConnectionHolder::AllocateStatementHandle() {
	assert(m_hSqlDBC);
	FreeStatementHandle();

	RETCODE rc = ::SQLAllocHandle(SQL_HANDLE_STMT, m_hSqlDBC, &m_hSqlStmt);
	if( !SQL_SUCCEEDED(rc) ) {
		AppendLastError("Failed to allocate the statement handle. " + GetError(SQL_HANDLE_STMT, m_hSqlStmt));
		DisconnectAndCleanUp();
		return false;
	}

	assert(m_hSqlStmt);
	return true;
}

bool OdbcConnectionHolder::Connect(bool bAllocateStmt)
{
	if( m_stConnectionString.empty() ) {
		AppendLastError("The connection string has not been specified.");
		return false;
	}

	if( !AllocateEnvironmentHandle() )
		return false;

	if( !m_hSqlDBC ) {
		                                 
		RETCODE rc = ::SQLAllocHandle(SQL_HANDLE_DBC, m_hSqlEnv, &m_hSqlDBC);
		if( !SQL_SUCCEEDED(rc) ) {
			AppendLastError("Failed to allocate the connection handle. " + GetError(SQL_HANDLE_DBC, m_hSqlDBC));
			DisconnectAndCleanUp();
			return false;
		}
	}

	                          
	SQLCHAR szConnStrIn[2048] = {};
	SQLCHAR szConnStrOut[2048] = {};
	SQLSMALLINT	StringLength2 = 0;
	strcpy((char*)szConnStrIn, m_stConnectionString.c_str());

	RETCODE rc = ::SQLDriverConnect(m_hSqlDBC, NULL, szConnStrIn, SQL_NTS,
		szConnStrOut, 2048, &StringLength2, SQL_DRIVER_NOPROMPT);
	if( !SQL_SUCCEEDED(rc) ) {
		AppendLastError("Failed to connect to database. " + GetError(SQL_HANDLE_DBC, m_hSqlDBC));
		DisconnectAndCleanUp();
		return false;
	}
	if ( rc == SQL_SUCCESS_WITH_INFO ) {
		m_stLastWarning = GetError(SQL_HANDLE_DBC, m_hSqlDBC);
	}
	m_bIsConnected = true;

	m_fDriverVersion = GetOdbcVersion(m_hSqlDBC);
	m_stQuoteChar = GetSqlIdentifierQuoteCharacter(m_hSqlDBC);

	if ( bAllocateStmt ) {
		if ( !AllocateStatementHandle() )
			return false;
	}
	return true;
}

bool OdbcConnectionHolder::ReAllocateStatementHandle() {
	if ( !FreeStatementHandle() )
		return false;
	if ( !AllocateStatementHandle() )
		return false;

	return true;
}

bool OdbcConnectionHolder::FreeStatementHandle() {
	if( m_hSqlStmt != 0 ) {
		                                               
		konga::SQLFreeHandle_STMT(m_hSqlStmt);
		m_hSqlStmt = 0;
	}
	return true;
}

void OdbcConnectionHolder::DisconnectAndCleanUp(bool bFreeStatementHandle             )
{
	FreeStatementHandle();

	if( m_bIsConnected && m_hSqlDBC ) {
		::SQLDisconnect(m_hSqlDBC);
	}
	m_bIsConnected = false;

	if( m_hSqlDBC != 0 ) {
		::SQLFreeHandle(SQL_HANDLE_DBC, m_hSqlDBC);
		m_hSqlDBC = 0;
	}

	if( m_hSqlEnv != 0 ) {
		::SQLFreeHandle(SQL_HANDLE_ENV, m_hSqlEnv);
		m_hSqlEnv = 0;
	}
}

void OdbcConnectionHolder::AppendLastError(const string& err) {
	if ( !m_stLastError.empty() ) m_stLastError += "\n";
	m_stLastError += err;
}

