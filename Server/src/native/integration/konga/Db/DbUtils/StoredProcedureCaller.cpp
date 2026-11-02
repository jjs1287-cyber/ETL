/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>		                
#include <cassert>
#include <map>
#include <vector>

#ifdef WIN32
#include "windows.h"
#endif
#include <sqlext.h>

#include <boost/shared_ptr.hpp>

#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include "OdbcBoundValue.h"
#include "TableColumnsOdbc.h"
#include "StoredProcedureCaller.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;
using boost::shared_ptr;

namespace {
	typedef boost::shared_ptr<OdbcBoundValue> SharedOdbcBoundValue;

	class DbProcedureColumnOrdinalLookupMap {
	private:
		typedef vector<const DbProcedureColumn*> MapValueTypeT;
		typedef map<long, MapValueTypeT > DbProcedureColumnOrdinalMapT;

	public:
		DbProcedureColumnOrdinalLookupMap(const vector<DbProcedureColumn>& vColumns) : m_debug("ODBC Column Info:\n") {
			vector<DbProcedureColumn>::const_iterator cit = vColumns.begin();
			for( ; cit != vColumns.end(); ++cit ) {
				const DbProcedureColumn& col = *cit;
				const long ordinalPosition = col.OrdinalPosition;
				m_debug += "Type Name=" + col.DataTypeName + ";OrdinalPos=" + toString(col.OrdinalPosition);
				m_debug += "\n";
				DbProcedureColumnOrdinalMapT::iterator dataIt = m_data.find(ordinalPosition);
				if( dataIt == m_data.end() ) {
					MapValueTypeT v;
					v.push_back(&col);
					m_data.insert(DbProcedureColumnOrdinalMapT::value_type(ordinalPosition, v));
				}
				else {
					dataIt->second.push_back(&col);
				}
			}
		}

		~DbProcedureColumnOrdinalLookupMap(void) {
			vector<DbProcedureColumn*>::iterator it = m_tmp.begin();
			for( ; it != m_tmp.end(); ++it ) {
				if ( *it ) {
					delete *it;
					*it = 0;
				}
			}
			m_tmp.clear();
		}

		const DbProcedureColumn& getDbProcedureColumn(long ordinalPosition, const StoredProcedureCaller::SPParam& param) {
			DbProcedureColumnOrdinalMapT::const_iterator cit = m_data.find(ordinalPosition);
			if ( cit == m_data.end() ) {
				return getDbProcedureColumnFromSPParam(ordinalPosition, param);
			}
			const MapValueTypeT& v = cit->second;
			assert(!v.empty());

			                                                                                                 
			                                                                               
			return *v.at(0);
		}

		const DbProcedureColumn& getDbProcedureColumnFromSPParam(long ordinalPosition, const StoredProcedureCaller::SPParam& param) {
			                                                                          
			std::auto_ptr<DbProcedureColumn> pDbProcedureColumn(new DbProcedureColumn());
			if ( !m_data.empty() ) {
				if ( !m_data.begin()->second.empty() ) {
					*pDbProcedureColumn = *(*m_data.begin()->second.begin());
				}
			}
			pDbProcedureColumn->OrdinalPosition = ordinalPosition;
			pDbProcedureColumn->ColumnType = SQL_PARAM_INPUT;
			pDbProcedureColumn->DateTimeSub = 0;
			pDbProcedureColumn->DecimalDigits = 0;
			pDbProcedureColumn->DefaultValue = "";
			pDbProcedureColumn->HasDefaultValue = false;
			pDbProcedureColumn->IsNullable = false;
			pDbProcedureColumn->IsReturnValue = false;
			pDbProcedureColumn->Nullable = SQL_NULLABLE_UNKNOWN;
			pDbProcedureColumn->Remarks = "";
			pDbProcedureColumn->OctetLength = 0;
			pDbProcedureColumn->Radix = 10;
			switch(param.type) {
				case StoredProcedureCaller::PARAM_TYPE_INTEGER:
					pDbProcedureColumn->BufferLength = 4;
					pDbProcedureColumn->ColumnSize = 10;
					pDbProcedureColumn->DataType = SQL_INTEGER;
					pDbProcedureColumn->DataTypeName = "int4";
					pDbProcedureColumn->SqlDataType = pDbProcedureColumn->DataType;
					pDbProcedureColumn->SqlDataTypeName = pDbProcedureColumn->DataTypeName;
					break;
				case StoredProcedureCaller::PARAM_TYPE_REAL:
					pDbProcedureColumn->BufferLength = 30;
					pDbProcedureColumn->ColumnSize = 28;
					pDbProcedureColumn->DataType = SQL_NUMERIC;
					pDbProcedureColumn->DataTypeName = "numeric";
					pDbProcedureColumn->DecimalDigits = 6;
					pDbProcedureColumn->SqlDataType = pDbProcedureColumn->DataType;
					pDbProcedureColumn->SqlDataTypeName = pDbProcedureColumn->DataTypeName;
					break;
				case StoredProcedureCaller::PARAM_TYPE_DATETIME:
					pDbProcedureColumn->BufferLength = 6;
					pDbProcedureColumn->ColumnSize = 10;
					pDbProcedureColumn->DataType = SQL_DATETIME;
					pDbProcedureColumn->DataTypeName = "date";
					pDbProcedureColumn->SqlDataType = pDbProcedureColumn->DataType;
					pDbProcedureColumn->SqlDataTypeName = pDbProcedureColumn->DataTypeName;
					break;
				default:
					                                         
					pDbProcedureColumn->BufferLength = 8190;
					pDbProcedureColumn->ColumnSize = 8190;
					pDbProcedureColumn->DataType = -10;	                                                                                               
					pDbProcedureColumn->DataTypeName = "text";
					pDbProcedureColumn->SqlDataType = pDbProcedureColumn->DataType;
					pDbProcedureColumn->SqlDataTypeName = pDbProcedureColumn->DataTypeName;
					break;
			}
			m_tmp.push_back( pDbProcedureColumn.release() );
			return *(*(m_tmp.end()-1));
		}

	private:
		string m_debug;
		vector<DbProcedureColumn*> m_tmp;
		DbProcedureColumnOrdinalMapT m_data;
	};

	namespace {
		                                             
		vector<DbProcedureColumn> for_debugging(const vector<DbProcedureColumn>& vColumns_in) {
			vector<DbProcedureColumn> vColumns;
			for( vector<DbProcedureColumn>::const_iterator cit = vColumns_in.begin(); cit != vColumns_in.end(); ++cit ) {
				DbProcedureColumn col = *cit;
				col.OrdinalPosition = -1;
				vColumns.push_back(col);
			}
			return vColumns;
		}
	}

	void BindParameters(bool hasReturnValue, vector< SharedOdbcBoundValue >& boundValues,
		const StoredProcedureCaller::SPParamsT& parameters, const vector<DbProcedureColumn>& vColumns_in,
		SQLHSTMT& hSqlStmt,
		string& sql)
	{
		                                          
		assert( parameters.size()<=vColumns_in.size() );
		DbProcedureColumnOrdinalLookupMap columnLookup(vColumns_in);

		long parameterNumber = hasReturnValue ? 2 : 1;
		long currentOrdinal = 1;
		StoredProcedureCaller::SPParamsT::const_iterator citParamValues = parameters.begin();
		for( ; citParamValues != parameters.end(); ++citParamValues, ++currentOrdinal, ++parameterNumber ) {
			                               
			if ( citParamValues != parameters.begin() )
				sql += ",";
			sql += "?";
			
			                          
			  
			                    
			const DbProcedureColumn& column = columnLookup.getDbProcedureColumn(currentOrdinal, *citParamValues);

			                                   
			const string& value = citParamValues->value;
			SharedOdbcBoundValue boundValue(
				new OdbcBoundValue(column, value, citParamValues->type==StoredProcedureCaller::PARAM_TYPE_NULL)
				);

			boundValue->BindParameter(hSqlStmt, parameterNumber);
			boundValues.push_back(boundValue);
		}
	}

	StoredProcedureCaller::SPParamType SPParamTypeFromSqlType(int sqlType) {
		switch(sqlType) {
			case SQL_BIT:
			case SQL_TINYINT:
			case SQL_SMALLINT:
			case SQL_INTEGER:
			case SQL_BIGINT:
				return StoredProcedureCaller::PARAM_TYPE_INTEGER;
			case SQL_REAL:
			case SQL_FLOAT:
			case SQL_DOUBLE:
				return StoredProcedureCaller::PARAM_TYPE_REAL;
			case SQL_TYPE_DATE:
			case SQL_TYPE_TIME:
			case SQL_TYPE_TIMESTAMP:
			case SQL_TIMESTAMP:
			case SQL_TIME:
			case SQL_DATE:
                       
				return StoredProcedureCaller::PARAM_TYPE_DATETIME;
			default:
				return StoredProcedureCaller::PARAM_TYPE_STRING;
		}
	}
}

void StoredProcedureCaller::CallStoredProcedure(const string& spName,
												  SPParamsT& parameters,
												  SPParam& returnVal)
{
	const string sConnectionString(GetConnectionString());
	vector<DbProcedureColumn> vColumns;
	DbProcedureColumn retVal;
	this->GetProcessColumns(sConnectionString, spName, vColumns, retVal);

               
                                      
        

	if ( parameters.size() > vColumns.size() )
		throw CrException("Too many parameters specified for stored procedure \""
		+ spName + "\". " + toString(vColumns.size()) + " required but "
		+ toString(parameters.size()) + " were specified.");

	PrivateConnect(sConnectionString);

	string sql("{");
	const bool hasReturnValue = retVal.isReturnValue();
	if ( hasReturnValue )
		sql += "? = ";

	const string quoteChar(this->getQuoteString());
	sql += "CALL " + quoteChar + spName + quoteChar;
	if ( !parameters.empty()  )
		sql += "(";

	                             
	std::auto_ptr<OdbcBoundValue> retValBoundValue(0);
	if ( retVal.isReturnValue() ) {
		retValBoundValue.reset( new OdbcBoundValue(retVal, "", true) );
		retValBoundValue->BindParameter(m_hSqlStmt, 1);
	}

	vector< SharedOdbcBoundValue > boundValues;
	BindParameters(hasReturnValue, boundValues, parameters, vColumns, m_hSqlStmt, sql);

	if ( !vColumns.empty() )
		sql += ")";
	sql += "}";

	                     
	SQLRETURN rc = SQLExecDirect(m_hSqlStmt, (SQLCHAR*)sql.c_str(), SQL_NTS);
	if ( !SQL_SUCCEEDED(rc) && rc != SQL_NO_DATA ) {
		string err("Failed to execute SQL statement: " + sql);
		err += "\nReason: ";
		err += konga::GetError(SQL_HANDLE_STMT, m_hSqlStmt);
		throw CrException(err);
	}

	                         
	while(true) {
		rc = SQLMoreResults(m_hSqlStmt);
		if ( rc == SQL_NO_DATA ) {
			break;
		}
		else if ( rc == SQL_STILL_EXECUTING ) {
			continue;
		}
		else if ( !SQL_SUCCEEDED(rc) ) {
			string err("Failed to get procedure results: ");
			err += "\nReason: ";
			err += konga::GetError(SQL_HANDLE_STMT, m_hSqlStmt);
			throw CrException(err);
		}
		        
		                                       
		   
	}

	                         
	assert( parameters.size()==boundValues.size() );
	SPParamsT::iterator itParamValues = parameters.begin();
	vector< SharedOdbcBoundValue >::const_iterator citBoundValues = boundValues.begin();
	for( ; itParamValues != parameters.end(); ++itParamValues, ++citBoundValues ) {
		const OdbcBoundValue& boundValue = *(citBoundValues->get());
		if ( boundValue.hasOutput() ) {
			                             
			if ( boundValue.isNull() ) {
				itParamValues->type = PARAM_TYPE_NULL;
				itParamValues->value = "";
			}
			else {
				itParamValues->type = SPParamTypeFromSqlType(boundValue.getSqlType());
				itParamValues->value = boundValue.getValueAsString();
			}
		}
	}

	                    
	if ( retValBoundValue.get() != 0 && retValBoundValue->hasOutput() ) {
		returnVal.type = SPParamTypeFromSqlType(retValBoundValue->getSqlType());
		returnVal.value = retValBoundValue->getValueAsString();
	}
}

void StoredProcedureCaller::PrivateConnect(const std::string& sConnectionString) {
	if ( IsConnected() && sConnectionString != GetConnectionString() ) {
		Close();
	}
	SetConnectionString(sConnectionString);

	if( !IsConnected() && !Connect() )
		throw CrException("Failed to connect to database: " + GetLastError());

	if ( m_txnIsolationLevel != konga::UNDEFINED ) {
		konga::SetTransactionIsolationLevel(m_hSqlDBC, m_txnIsolationLevel);
	}
}

void StoredProcedureCaller::GetProcessColumns(const string& sConnectionString,
											  const string& spName,
											  vector<DbProcedureColumn>& vColumns,
											  DbProcedureColumn& retVal)
{
	                                 
	OdbcColumns odbc(sConnectionString);
	if ( !odbc.GetProcedureParameters(vColumns, retVal, spName) ) {
		string err("Failed to get parameters for stored procedure \""
			+ spName + "\". ");
		err += odbc.GetLastError();
		throw CrException(err);
	}
}
