/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <vector>
#include <memory>
#include <cassert>

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>
#include <sqlext.h>

#include <boost/shared_ptr.hpp>

#include <konga/Utils/CrException.h>

#include "DbUtils.h"
#include "OdbcBoundValue.h"
#include "OdbcDbLookup.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	const int auto_commit_not_set = -1;
	const int auto_commit_on = 1;	          
	const int auto_commit_off = 0;	
}

OdbcDbLookup::OdbcDbLookup(const string& stConnectionString, const TransactionIsolationLevel txnIsolationLevel)
: OdbcConnectionHolder(stConnectionString), m_bUseAutoCommit(true),
m_iCurrentAutoCommitSetting(auto_commit_not_set), m_RowCount(0), m_txnIsolationLevel(txnIsolationLevel)
{
}

OdbcDbLookup::~OdbcDbLookup(void) {
	Close();
}

bool OdbcDbLookup::IsOpen() {
	return OdbcConnectionHolder::IsConnected();
}

void OdbcDbLookup::Close() {
	CommitTransaction();
	OdbcConnectionHolder::Close();
}

DbLookupResultState OdbcDbLookup::LookupSingleValue(const string& stSql, string& value) {
	DbRowSetData rowset;
	DbLookupResultState rs;
	try {
		rs = LookupImpl(stSql, rowset, 1, 1, -1);
	}
	catch(...) {
		FreeStatementHandle();
		throw;
	}

	if ( rs == DBLRS_OPEN_DB_FAILED ) return DBLRS_OPEN_DB_FAILED;
	if ( rowset.rowset.empty() )
		return DBLRS_NULL_RESULT;
	else {
		DbRowData* rd = rowset.rowset.at(0);
		if ( rd == 0 || rd->row.empty() ) return DBLRS_NULL_RESULT;
		DbData* d = rd->row.at(0);
		if ( d == 0 || d->isNull ) return DBLRS_NULL_RESULT;
		value = d->data;
		return DBLRS_LOOKUP_SUCCESS;
	}
}

DbLookupResultState OdbcDbLookup::Lookup(const std::string& stSql, DbRowSetData& rowset,
										 unsigned long nMaxRows,
										 long timeOutInSeconds)
{
	try {
		return LookupImpl(stSql, rowset, nMaxRows, -1, timeOutInSeconds);
	}
	catch( ... ) {
		FreeStatementHandle();
		throw;
	}
}

                                                        
                                                                                      
                                                                                                                   
DbLookupResultState OdbcDbLookup::LookupImpl(const string& stSql, DbRowSetData& rowset,
										 unsigned long nMaxRows,
										 long nMaxCols,
										 long timeOutInSeconds)
{
	ConnectImpl();
	SetAutoCommitAttr();
	m_RowCount = 0;

	assert(m_hSqlStmt);
	SQLRETURN sqlResult = ::SQLExecDirect(m_hSqlStmt, (SQLCHAR*)stSql.c_str(), SQL_NTS);
	if ( sqlResult == SQL_NO_DATA ) {
		                                                                      
		m_RowCount = 0;
		FreeStatementHandle();
		return DBLRS_LOOKUP_SUCCESS;
	}

	if ( !SQL_SUCCEEDED(sqlResult) ) {
		throw CrException("SQLExecDirect failed to execute SQL statement. " + konga::GetError(SQL_HANDLE_STMT, m_hSqlStmt));
	}

	                                             
	if ( nMaxRows < 1 || nMaxCols == 0 ) {
		FreeStatementHandle();
		return DBLRS_LOOKUP_SUCCESS;
	}

	                                       
	SQLSMALLINT SQLNumCols = 0;
	sqlResult = ::SQLNumResultCols(m_hSqlStmt, &SQLNumCols);
	if ( !SQL_SUCCEEDED(sqlResult) ) {
		throw CrException("Failed to get the number of columns in the result set. " + konga::GetError(SQL_HANDLE_STMT, m_hSqlStmt));
	}
	if( nMaxCols > -1 && nMaxCols < SQLNumCols ) SQLNumCols = nMaxCols;

	if ( SQLNumCols == 0 ) {
		                                                        
		SQLLEN RowCount = 0;
		sqlResult = ::SQLRowCount(m_hSqlStmt, &RowCount);
		if ( SQL_SUCCEEDED(sqlResult) ) {
			m_RowCount = RowCount;
		}
		else {
			m_RowCount = 0;
		}

		FreeStatementHandle();
		return DBLRS_LOOKUP_SUCCESS;
	}

	                   
	SQLCHAR ColumnName[1024] = {0};
	const SQLSMALLINT BufferLength = 1024;
	typedef vector< boost::shared_ptr<OdbcBoundValue> > OdbcBoundValueList;
	OdbcBoundValueList columns;
	SQLSMALLINT iCol = 1;
	for( ; iCol <= SQLNumCols; ++iCol ) {
		ColumnName[0] = 0;
		SQLSMALLINT NameLength = 0;
		SQLSMALLINT DataType = 0;
		SQLULEN ColumnSize = 0;
		SQLSMALLINT DecimalDigits = 0;
		SQLSMALLINT Nullable = 0;
		sqlResult = ::SQLDescribeCol(m_hSqlStmt, iCol, ColumnName, BufferLength, &NameLength, &DataType, &ColumnSize, &DecimalDigits, &Nullable);
		if ( !SQL_SUCCEEDED(sqlResult) ) {
			throw CrException("Failed to describe column " + konga::toString(iCol) + " of the result set. " + konga::GetError(SQL_HANDLE_STMT, m_hSqlStmt));
		}

		boost::shared_ptr<OdbcBoundValue> val( new OdbcBoundValue(DataType, ColumnSize, iCol, DecimalDigits) );
		columns.push_back( val );
	}

	              
	unsigned long iRows = 0;
	while( (sqlResult = ::SQLFetch(m_hSqlStmt)) != SQL_NO_DATA ) {
		if ( !SQL_SUCCEEDED(sqlResult) ) {
			throw CrException("Failed to fetch data. " + konga::GetError(SQL_HANDLE_STMT, m_hSqlStmt));
		}
		std::auto_ptr<DbRowData> row( new DbRowData() );

		iCol = 1;
		for( OdbcBoundValueList::const_iterator cit = columns.begin(); cit != columns.end(); ++cit, ++iCol) {
			OdbcBoundValue& value = *(*cit);
			std::auto_ptr<DbData> d( new DbData("") );

			while( value.GetMoreData(m_hSqlStmt, iCol) ) {
				if ( d->data.empty() && value.isNull() ) {
					d->isNull = true;
					break;
				}
				else if ( value.isScalar() ) {
					                                                                                                                     
					d->data = value.getValueAsString();
					break;
				}
				d->data.append( value.getValueAsString() );
			}

			row->push_back(d.release());
		}

		rowset.push_back( row.release() );
		++iRows;
		if(iRows >= nMaxRows) break;
	}
	m_RowCount = iRows;

	FreeStatementHandle();
	return DBLRS_LOOKUP_SUCCESS;
}

long OdbcDbLookup::GetRowCount() {
	return m_RowCount;
}

long OdbcDbLookup::UseAutoCommit(bool b) {
	m_bUseAutoCommit = b;
	return 0;
}

void OdbcDbLookup::CommitTransaction() {
	if( !IsConnected() ) return;
	assert(m_hSqlDBC);
	SQLRETURN rc = ::SQLEndTran(SQL_HANDLE_DBC, m_hSqlDBC, SQL_COMMIT);
	if ( !SQL_SUCCEEDED(rc) ) {
		throw CrException("Failed commit transaction. " + konga::GetError(SQL_HANDLE_DBC, m_hSqlDBC));
	}
}

void OdbcDbLookup::RollbackTransaction() {
	if( !IsConnected() ) return;
	assert(m_hSqlDBC);
	SQLRETURN rc = ::SQLEndTran(SQL_HANDLE_DBC, m_hSqlDBC, SQL_ROLLBACK);
	if ( !SQL_SUCCEEDED(rc) ) {
		throw CrException("Failed to roll back transaction. " + konga::GetError(SQL_HANDLE_DBC, m_hSqlDBC));
	}
}

void OdbcDbLookup::ConnectImpl() {
	if( !OdbcConnectionHolder::IsConnected() ) {
		if ( !OdbcConnectionHolder::Connect(true) ) {
			throw CrException(OdbcConnectionHolder::GetLastError());
		}

		if ( m_txnIsolationLevel != UNDEFINED ) {
			konga::SetTransactionIsolationLevel(m_hSqlDBC, m_txnIsolationLevel);
		}
	}
	else {
		ReAllocateStatementHandle();
	}
}

void OdbcDbLookup::SetAutoCommitAttr() {
	assert(m_hSqlDBC);
	if( !IsConnected() ) return;

	SQLRETURN rc = SQL_SUCCESS;
	if ( m_bUseAutoCommit ) {
		if ( m_iCurrentAutoCommitSetting == auto_commit_off ) {
			                                                                                                     
			rc = ::SQLSetConnectAttr(m_hSqlDBC, SQL_ATTR_AUTOCOMMIT, reinterpret_cast<SQLPOINTER>(SQL_AUTOCOMMIT_ON), 0);
			m_iCurrentAutoCommitSetting = auto_commit_on;
		}
	}
	else {
		if( m_iCurrentAutoCommitSetting != auto_commit_off ) {
			                                                              
			                                                          
			rc = ::SQLSetConnectAttr(m_hSqlDBC, SQL_ATTR_AUTOCOMMIT, reinterpret_cast<SQLPOINTER>(SQL_AUTOCOMMIT_OFF), 0);
			if ( !SQL_SUCCEEDED(rc) ) {
				throw CrException("Failed to turn off auto commit. " + konga::GetError(SQL_HANDLE_DBC, m_hSqlDBC));
			}
			m_iCurrentAutoCommitSetting = auto_commit_off;
		}
	}
}
