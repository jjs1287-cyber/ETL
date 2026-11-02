/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>		               

#include <konga/Db/DbUtils/DbUtils.h>		               
#include <konga/Db/DtlDbUtils/DtlDbUtils.h>                    

#include <konga/Utils/PortDefs.h>			           

#include "DtlDbLookup.h"
#include "DtlTable.h"

using namespace dtl;
using namespace konga;
using namespace konga_dtl;
using namespace std;

DtlDbLookup::~DtlDbLookup() {
	Close();
}

bool DtlDbLookup::IsOpen()
{
	return m_pConn != 0 && m_pConn->IsConnected();
}

void DtlDbLookup::Connect() {
	if( !IsOpen() ) {
		try {
			const string& stConnectionString = GetConnectionString();
			if ( stConnectionString.empty() ) {
				throw CrException("No connection string has been specified for looking up data in database.");
			}
			m_pConn = new RefCountedDtlDbConnection();
			m_pConn->RefCountedConnect(stConnectionString, true);
			if ( m_txnIsolationLevel != konga::UNDEFINED ) {
				konga::SetTransactionIsolationLevel(m_pConn->GetHDBC(), m_txnIsolationLevel);
			}
		}
		catch( const RootException& e ) {
			throw CrException( e.what() );
		}
	}
}

void DtlDbLookup::Close()
{
	if( m_pConn != 0 )
	{
		if ( IsOpen() )
			m_pConn->RefCountedRelease();
		delete m_pConn;
		m_pConn = 0;
	}
}

long DtlDbLookup::UseAutoCommit(bool b) {
	SQLRETURN rc = SQL_SUCCESS;
	if ( m_iAutoCommitFlag == -1 || ( b && m_iAutoCommitFlag == 0) || ( !b && m_iAutoCommitFlag == 1 ) ) {
		Connect();
		rc = SQLSetConnectAttr(m_pConn->GetHDBC(), SQL_AUTOCOMMIT, (SQLPOINTER)(b?SQL_AUTOCOMMIT_ON:SQL_AUTOCOMMIT_OFF), 0);
		m_iAutoCommitFlag = b ? 1 : 0;
	}
	return rc;
}

void DtlDbLookup::CommitTransaction() {
	Connect();
	::SQLEndTran(SQL_HANDLE_DBC, m_pConn->GetHDBC(), SQL_COMMIT);
}

void DtlDbLookup::RollbackTransaction() {
	Connect();
	::SQLEndTran(SQL_HANDLE_DBC, m_pConn->GetHDBC(), SQL_ROLLBACK);
}

long DtlDbLookup::GetRowCount() {
	return m_lRows;
}

DbLookupResultState DtlDbLookup::LookupSingleValue(const std::string& stSql, string& value) {
	DbRowSetData rows;
	DbLookupResultState resultState = this->Lookup(stSql, rows, 1);
	if ( resultState == DBLRS_LOOKUP_SUCCESS ) {
		if ( !rows.rowset.empty() ) {
			DbRowData* row = rows.rowset.at(0);
			if ( row != 0 && !row->row.empty() ) {
				DbData* data = row->row.at(0);
				if ( data == 0 || data->isNull ) {
					                              
					return DBLRS_NULL_RESULT;
				}
				else {
					value = data->data;
				}
			}
		}
	}

	return resultState;
}

DbLookupResultState DtlDbLookup::Lookup(const string& stSql,
										DbRowSetData& rows,
										unsigned long nMaxRows,
										long timeOutInSeconds           )
{
	rows.clear();
	Connect();
	
	DbLookupResultState resState = DBLRS_LOOKUP_SUCCESS;
	try  {
		DynamicDBView<>::Args args;
		args.tables(stSql);
		args.conn(*m_pConn);
		DynamicDBView<> view(args);
		DynamicDBView<>::sql_iterator sql_it(view);

		                                                                       
		*sql_it = view.GetDataObj();

		m_lRows = sql_it.GetStmt().RowCount();

		if( sql_it == view.end() || sql_it->size() == 0 ) {
			resState = DBLRS_NULL_RESULT;
		}
		else {
			for(; sql_it != view.end() && rows.rowset.size() < nMaxRows; ++sql_it) {
				DbRowData* pRow = new DbRowData();
				const variant_row& r = *sql_it;
				const size_t rSize = r.size();
				for(size_t nCol = 0; nCol < rSize; ++nCol) {
					if ( r.IsNull(nCol) ) {
						pRow->push_back( new DbData() );
						continue;
					}

					                                                
					const variant_field& f = r[nCol];
					if ( f.is_type<dtl::blob>() ) {
						const dtl::blob& blb = f.get_blob();
						const char* bytes = (const char*)blb.data();
						const time_t size = blb.size();
						pRow->push_back( new DbData(konga::getHexString(bytes, size)) );
					}
					else {
						                                             
						pRow->push_back( new DbData(f.get_string()) );
					}
				}
				rows.push_back(pRow);
			}
		}
	}
	catch( const RootException& e )
	{
		ThrowDbException(e, "Failed to lookup value from database: ", 1);
	}
	return resState;
}
