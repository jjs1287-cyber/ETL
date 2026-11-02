/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef KONGA_ODBC_BOUND_VALUE_H
#define KONGA_ODBC_BOUND_VALUE_H

#ifdef WIN32
#include <windows.h>
#endif
#include <sql.h>

#include <boost/noncopyable.hpp>

#include <string>

namespace konga {

	class DbProcedureColumn;
	class AllocatedDataImpl;

	class OdbcBoundValue : private boost::noncopyable {
	public:
		                                                           
		OdbcBoundValue(const DbProcedureColumn& procCol,
			const std::string& value, bool isNull);

		                                                                
		OdbcBoundValue(SQLSMALLINT DataType, SQLUINTEGER ColumnSize,
			SQLUSMALLINT OrdinalPosition, SQLSMALLINT DecimalDigits);

		~OdbcBoundValue(void);

		void BindParameter(SQLHSTMT hstmt, unsigned int parameterNumber);

		void BindColumn(SQLHSTMT hstmt, unsigned int columnNumber);

		                                          
		                                                    
		                                                              
		bool GetMoreData(SQLHSTMT hstmt, unsigned int columnNumber);

		                                                         
		                                                                                                         
		std::string getValueAsString() const;

		                                        
		                                                 
		bool hasOutput() const;

		bool isNull() const;

		                                                                                        
		                                                   
		bool isScalar() const;

		int getSqlType() const;

	private:
		void Deallocate();

	private:
		SQLSMALLINT DataType;
		SQLUINTEGER ColumnSize;
		bool IsReturnValue;
		SQLSMALLINT ColumnType;	                             
		SQLUSMALLINT OrdinalPosition;
		SQLSMALLINT DecimalDigits;

		SQLLEN StrLen_or_IndPtr;

		AllocatedDataImpl* m_pImpl;
	};

}                   

#endif                            

