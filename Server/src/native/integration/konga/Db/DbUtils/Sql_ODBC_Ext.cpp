/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	#include <windows.h>
	#include <konga/Utils/PortDefs.h>		                        
#endif
#include <sqlext.h>
#include "Sql_ODBC_Ext.h"
namespace konga
{
	SQLRETURN ODBC_ResultSetInfo::readNumCols(){
			if (SQL_SUCCEEDED(rc))
			{
				SQLNumCols  = -100;
				rc = ::SQLNumResultCols(m_hSqlStmt, &SQLNumCols);
			}
			return rc;
		}
	SQLRETURN ODBC_ResultSetInfo::moveToQueryResultSet() {
			while (SQL_SUCCEEDED(readNumCols()))
			{
				if (isQueryResultSet()) return rc;
				rc=::SQLMoreResults(m_hSqlStmt);
				if (SQL_SUCCEEDED(rc)) continue;
				return rc;
			}
			return rc;
		}

}