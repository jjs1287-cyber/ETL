/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <windows.h>
	#include <konga/Utils/PortDefs.h>		                            
#endif

#include <sqlext.h>
#include <cassert>

#include <boost/scoped_array.hpp>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/DateUtils.h>		             
#include <konga/Utils/date.h>			          
#include <konga/Utils/CrException.h>

#include "DbUtils.h"
#include "DbProcedureColumn.h"
#include "OdbcBoundValue.h"

using namespace konga;
using std::string;

namespace {
	void FillDateTimeStruct(SQL_TIMESTAMP_STRUCT& s, const string& value) {
		string err;
		CrDateTime dt = konga::GetDate(value.c_str(), err);
		                                                         
		if ( !err.empty() ) {
			s.day = 1;
			s.month = 1;
			s.year = 1970;
		}
		else {
			s.day = dt.GetDay();
			s.fraction = 0;
			s.hour = dt.GetHour();
			s.minute = dt.GetMinute();
			s.month = dt.GetMonth();
			s.second = dt.GetSecond();
			s.year = dt.GetYear();
		}
	}

	void FillDateStruct(SQL_DATE_STRUCT& s, const string& value) {
		string err;
		CrDateTime dt = konga::GetDate(value.c_str(), err);
		if ( !err.empty() ) {
			s.day = 1;
			s.month = 1;
			s.year = 1970;
		}
		else {
			s.day = dt.GetDay();
			s.month = dt.GetMonth();
			s.year = dt.GetYear();
		}
	}

	void FillTimeStruct(SQL_TIME_STRUCT& s, const string& value) {
		string err;
		CrDateTime dt = konga::GetDate(value.c_str(), err);
		if ( !err.empty() ) {
			                                 
		}
		else {
			s.hour = dt.GetHour();
			s.minute = dt.GetMinute();
			s.second = dt.GetSecond();
		}
	}

#ifdef _DEBUG
	const int MAX_ALLOC_SIZE = 1024;
#else
	const int MAX_ALLOC_SIZE = 1024;
#endif

	string GetStringValue(const SQL_TIMESTAMP_STRUCT& s) {
		CrDateTime dt(s.year, s.month, s.day, s.hour, s.minute, s.second);
		if ( s.fraction == 0 ) return dt.toString("yyyy-mm-dd HH:MM:SS");
		return string( dt.toString("yyyy-mm-dd HH:MM:SS") + "." + konga::toString(s.fraction) );
	}

	string GetStringValue(const SQL_TIME_STRUCT& s) {
		const CrDateTime& now = CrDateTime::GetCurrentTime();
		CrDateTime dt(now.GetYear(), now.GetMonth(), now.GetDay(), s.hour, s.minute, s.second);
		return dt.toString("HH:MM:SS");
	}

	string GetStringValue(const SQL_DATE_STRUCT& s) {
		CrDateTime dt(s.year, s.month, s.day, 0, 0, 0);
		return dt.toString("yyyy-mm-dd");
	}
}

namespace konga {

	class AllocatedDataImpl {
	public:
		AllocatedDataImpl(const int sqlDataType_, const int sqlDateTimeSub_,
			const int bufferSize_, const string& value, bool isNull_, bool isOutput_) :
		  isStringType(false),
		  isBinaryType(false),
		  isNull(isNull_),
		  isOutput(isOutput_),
		  sqlDataType(sqlDataType_),
		  sqlDateTimeSub(sqlDateTimeSub_),
		  cDataType(0),
		  bufferSize(bufferSize_)
		  {
			memset(&data, 0, sizeof(data));
			                                                                    
			                            
			if ( !(isNull && !isOutput) ) {
			isNull = false;
			switch(sqlDataType) {
				case SQL_BIT:
					data.sqlchar = atoi(value.c_str());
					cDataType = SQL_C_BIT;
					break;
				case SQL_TINYINT:
					data.sqlschar = atoi(value.c_str());
					cDataType = SQL_C_STINYINT;
					break;
				case SQL_SMALLINT:
					data.sqlsmallint = atoi(value.c_str());
					cDataType = SQL_C_SSHORT;
					break;
				case SQL_INTEGER:
					data.sqlinteger = atol(value.c_str());
					cDataType = SQL_C_SLONG;
					break;
				case SQL_BIGINT:
					data.sqlbigint = atoll(value.c_str());
					cDataType = SQL_C_SBIGINT;
					break;
				case SQL_REAL:
					data.sqlreal = atof(value.c_str());
					cDataType = SQL_C_FLOAT;
					break;
				case SQL_FLOAT:
				case SQL_DOUBLE:
				case SQL_NUMERIC:
				case SQL_DECIMAL:
					try {
						data.sqldouble = toDouble(value);
					} catch( const CrException& ) {
						data.sqldouble = 0;
					}
					cDataType = SQL_C_DOUBLE;
					break;
				case SQL_TYPE_DATE:
				case SQL_TYPE_TIME:
				case SQL_TYPE_TIMESTAMP:
				case SQL_DATETIME:
				case SQL_TIME:
				case SQL_TIMESTAMP:
					if ( sqlDateTimeSub == SQL_CODE_TIMESTAMP ||
						(sqlDateTimeSub == 0 &&
						(sqlDataType == SQL_DATETIME || sqlDataType == SQL_TYPE_TIMESTAMP || sqlDataType == SQL_TIMESTAMP )) )
					{
						FillDateTimeStruct(data.sql_timestamp_struct, value);
						cDataType = SQL_C_TIMESTAMP;
						sqlDataType = SQL_TIMESTAMP;
						bufferSize = sizeof(data.sql_timestamp_struct);
					}
					else if ( sqlDateTimeSub == SQL_CODE_TIME ||
						(sqlDateTimeSub == 0 &&
						(sqlDataType == SQL_TYPE_TIME || sqlDataType == SQL_TIME )) )
					{
						FillTimeStruct(data.sql_time_struct, value);
						cDataType = SQL_C_TIME;
						sqlDataType = SQL_TIME;
						bufferSize = sizeof(data.sql_time_struct);
					}
					else {
						                          
						                                                    
						   
						FillDateStruct(data.sql_date_struct, value);
						cDataType = SQL_C_DATE;
						sqlDataType = SQL_DATE;
						bufferSize = sizeof(data.sql_date_struct);
					}
					break;
				case SQL_BINARY:
				case SQL_VARBINARY:
				case SQL_LONGVARBINARY:
					{
					int allocSize = 0;
					unsigned char* bvalue = 0;
					size_t bsize = 0;
					if ( !value.empty() ) {
						                        
						bvalue = konga::fromHexString(value, bsize);
					}
					if ( bufferSize > 0 ) {
						if ( bsize >= (size_t)bufferSize )
							allocSize = bsize;
						else {
							if ( bufferSize > MAX_ALLOC_SIZE ) {
								allocSize = MAX_ALLOC_SIZE;
							}
							else {
								allocSize = bufferSize;
							}
						}
					}
					else
						allocSize = bsize;

					data.psqlchar = new SQLCHAR[allocSize];
					if ( bvalue != 0 && bsize > 0 ) {
						memcpy(data.psqlchar, (SQLCHAR*)bvalue, bsize);
						memset(data.psqlchar + bsize, 0, allocSize-bsize);
					}
					else {
						memset(data.psqlchar, 0, allocSize);
					}
					bufferSize = allocSize;
					}
					cDataType = SQL_C_BINARY;
					isBinaryType = true;
					break;
				default:
					                                                 
					                                                                                   
					{
					int allocSize = 0;
					if ( bufferSize > 0 ) {
						if ( value.size() >= (string::size_type)bufferSize )
							allocSize = value.size() + 1;
						else {
							if ( bufferSize > MAX_ALLOC_SIZE ) {
								allocSize = MAX_ALLOC_SIZE;
							}
							else {
								allocSize = bufferSize + 1;
							}
						}
					}
					else {
						allocSize = value.size() + 1;
					}

					data.psqlchar = new SQLCHAR[allocSize];
					memcpy(data.psqlchar, (SQLCHAR*)value.c_str(), value.size());
					memset(data.psqlchar + value.size(), 0, allocSize-value.size());
					bufferSize = allocSize;
					}
					cDataType = SQL_C_CHAR;
					isStringType = true;
					break;
			};
			}
			else {
				                                                             
				if ( sqlDataType == SQL_DATETIME ) {
					switch ( this->sqlDateTimeSub ) {
						case SQL_CODE_TIMESTAMP:
							FillDateTimeStruct(data.sql_timestamp_struct, value);
							cDataType = SQL_C_TIMESTAMP;
							sqlDataType = SQL_TIMESTAMP;
							bufferSize = sizeof(data.sql_timestamp_struct);
							break;
						case SQL_CODE_TIME:
							FillTimeStruct(data.sql_time_struct, value);
							cDataType = SQL_C_TIME;
							sqlDataType = SQL_TIME;
							bufferSize = sizeof(data.sql_time_struct);
							break;
						default:                 
							FillDateStruct(data.sql_date_struct, value);
							cDataType = SQL_C_DATE;
							sqlDataType = SQL_DATE;
							bufferSize = sizeof(data.sql_date_struct);
							break;
					};
				}
				else {
					cDataType = SQL_C_DEFAULT;
				}
			}
		}

		~AllocatedDataImpl(void) {
			if ( (isStringType||isBinaryType) && data.psqlchar ) {
				delete[] data.psqlchar;
				data.psqlchar = 0;
			}
		}

		string getValueAsString(SQLINTEGER StrLen_or_IndPtr) const {
			if ( isStringType ) {
				if ( data.psqlchar != 0 )
					return string((const char*)data.psqlchar);
				else
					return string("");
			}
			switch( this->cDataType ) {
				case SQL_C_BIT:
					{
						int i = static_cast<int>(data.sqlchar);
						return toString(i);
					}
				case SQL_C_TINYINT:
					{
						int i = static_cast<int>(data.sqlschar);
						return toString(i);
					}
					break;
				case SQL_C_SSHORT:
					return toString(data.sqlsmallint);
				case SQL_C_SLONG:
					return toString(data.sqlinteger);
				case SQL_C_SBIGINT:
					return toString(data.sqlbigint);
				case SQL_C_FLOAT:
					return toString(data.sqlreal);
				case SQL_C_DOUBLE:
					return toString(data.sqldouble);
				case SQL_C_TIMESTAMP:
					return GetStringValue(data.sql_timestamp_struct);
				case SQL_C_TIME:
					return GetStringValue(data.sql_time_struct);
				case SQL_C_DATE:
					return GetStringValue(data.sql_date_struct);
				case SQL_BINARY:
				case SQL_VARBINARY:
				case SQL_LONGVARBINARY:
					if ( data.psqlchar ) {
						if ( StrLen_or_IndPtr == SQL_NULL_DATA )
							return "";
						return konga::getHexString((const char*)data.psqlchar, (StrLen_or_IndPtr > bufferSize) ? bufferSize : StrLen_or_IndPtr);
					}
					return string("");
				default:
					return string("");
			}
		}

		SQLPOINTER GetSqlPointer() {
			if ( isNull )
				return 0;
			if ( isStringType || isBinaryType )
				return static_cast<SQLPOINTER>(data.psqlchar);
			else
				return static_cast<SQLPOINTER>(&data);
		}

		bool isStringType;
		bool isBinaryType;
		bool isNull;
		bool isOutput;
		int sqlDataType;
		int sqlDateTimeSub;
		int cDataType;
		int bufferSize;

		union AllocatedData {
			                                           
			SQLCHAR* psqlchar;			                                                         

			SQLSMALLINT sqlsmallint;	               
			SQLUSMALLINT sqlusmallint;	               
			SQLINTEGER sqlinteger;		              
			SQLUINTEGER sqluinteger;	              
			SQLREAL sqlreal;			              
			SQLDOUBLE sqldouble;		               
                                                                                          
			SQLCHAR sqlchar;			                            
			SQLSCHAR sqlschar;			                 
			SQLBIGINT sqlbigint;		                
			SQLUBIGINT sqlubigint;		                

			                     
			SQL_TIMESTAMP_STRUCT sql_timestamp_struct;	                       
			SQL_DATE_STRUCT sql_date_struct;
			SQL_TIME_STRUCT sql_time_struct;
		} data;
	};

}

OdbcBoundValue::OdbcBoundValue(const DbProcedureColumn& procCol,
							   const string& value, bool isNull) :
DataType(procCol.DataType),
ColumnSize(procCol.ColumnSize),
IsReturnValue(procCol.isReturnValue()),
ColumnType(procCol.ColumnType),
OrdinalPosition(procCol.OrdinalPosition),
DecimalDigits(procCol.DecimalDigits),
StrLen_or_IndPtr(0),
m_pImpl(0)
{
	m_pImpl = new AllocatedDataImpl(
		procCol.SqlDataType!=SQL_UNKNOWN_TYPE ? procCol.SqlDataType : procCol.DataType, procCol.DateTimeSub, procCol.BufferLength, value, isNull, hasOutput()
		);
}

OdbcBoundValue::OdbcBoundValue(SQLSMALLINT DataType_, SQLUINTEGER ColumnSize_,
							   SQLUSMALLINT OrdinalPosition_, SQLSMALLINT DecimalDigits_) :
DataType(DataType_), ColumnSize(ColumnSize_), IsReturnValue(false), ColumnType(SQL_RESULT_COL),
OrdinalPosition(OrdinalPosition_), DecimalDigits(DecimalDigits_), StrLen_or_IndPtr(0),
m_pImpl(0)
{
	m_pImpl = new AllocatedDataImpl(
		DataType, 0, ColumnSize, "", false, false
		);
}

OdbcBoundValue::~OdbcBoundValue(void) {
	Deallocate();
}

int OdbcBoundValue::getSqlType() const {
	assert(m_pImpl);
	return m_pImpl->sqlDataType;
}

void OdbcBoundValue::BindParameter(SQLHSTMT hstmt, unsigned int parameterNumber) {
	assert(m_pImpl);
	SQLPOINTER p = m_pImpl->GetSqlPointer();
	SQLSMALLINT InputOutputType = ColumnType;

	                                                                                               
	                                                                        
	                                 
	if ( IsReturnValue )
		InputOutputType = SQL_PARAM_OUTPUT;
	else if ( InputOutputType != SQL_PARAM_INPUT &&
			  InputOutputType != SQL_PARAM_INPUT_OUTPUT &&
			  InputOutputType != SQL_PARAM_OUTPUT )
		InputOutputType = SQL_PARAM_INPUT;

	StrLen_or_IndPtr = 0;
	if ( m_pImpl->isStringType )
		StrLen_or_IndPtr = SQL_NTS;
	if ( m_pImpl->isNull && !m_pImpl->isOutput ) {
		                                                                                     
		StrLen_or_IndPtr = SQL_NULL_DATA;
	}

	SQLRETURN retval = ::SQLBindParameter(hstmt, parameterNumber, InputOutputType,
		m_pImpl->cDataType, m_pImpl->sqlDataType, ColumnSize, DecimalDigits,
		p, m_pImpl->bufferSize, &StrLen_or_IndPtr);
	if ( !SQL_SUCCEEDED(retval) ) {
		string err("Failed to bind parameter number " + toString(parameterNumber) + ". Reason: ");
		err += konga::GetError(SQL_HANDLE_STMT, hstmt);
		throw CrException(err);
	}
}

void OdbcBoundValue::BindColumn(SQLHSTMT hstmt, unsigned int columnNumber) {
	assert(m_pImpl);
	SQLPOINTER p = m_pImpl->GetSqlPointer();
	StrLen_or_IndPtr = 0;

	SQLRETURN retval = ::SQLBindCol(hstmt, columnNumber, m_pImpl->cDataType, p, m_pImpl->bufferSize, &StrLen_or_IndPtr);
	if( !SQL_SUCCEEDED(retval) ) {
		string err("Failed to bind column number " + toString(columnNumber) + ". Reason: ");
		err += konga::GetError(SQL_HANDLE_STMT, hstmt);
		throw CrException(err);
	}
}

bool OdbcBoundValue::GetMoreData(SQLHSTMT hstmt, unsigned int columnNumber) {
	assert(m_pImpl);
	SQLPOINTER p = m_pImpl->GetSqlPointer();
	StrLen_or_IndPtr = 0;

	SQLRETURN retval = ::SQLGetData(hstmt, columnNumber, m_pImpl->cDataType, p, m_pImpl->bufferSize, &StrLen_or_IndPtr);
	if ( retval == SQL_NO_DATA || StrLen_or_IndPtr == 0 ) {
		return false;
	}
	else if( !SQL_SUCCEEDED(retval) ) {
		string err("Failed to get more data (SQLGetData) for column number " + konga::toString(columnNumber)
			+ ". This may indicate that the column has not been bound correctly. "
			+ "Please report this issue to support@etlforge.invalid and include as much information about the table as you can.\nReported Error: ");
		err += konga::GetError(SQL_HANDLE_STMT, hstmt);
		throw CrException(err);
	}
	else {
		return true;
	}
}

std::string OdbcBoundValue::getValueAsString() const {
	assert(m_pImpl);
	return m_pImpl->getValueAsString(StrLen_or_IndPtr);
}

bool OdbcBoundValue::hasOutput() const {
	return ( ColumnType == SQL_PARAM_INPUT_OUTPUT ||
			 ColumnType == SQL_PARAM_OUTPUT ||
			 IsReturnValue );
}

bool OdbcBoundValue::isNull() const {
	assert(m_pImpl);
	return m_pImpl->isNull || StrLen_or_IndPtr == SQL_NULL_DATA;
}

bool OdbcBoundValue::isScalar() const {
	assert(m_pImpl);
	return !(m_pImpl->isBinaryType || m_pImpl->isStringType );
}

void OdbcBoundValue::Deallocate() {
	if ( m_pImpl ) {
		delete m_pImpl;
		m_pImpl = 0;
	}
}

