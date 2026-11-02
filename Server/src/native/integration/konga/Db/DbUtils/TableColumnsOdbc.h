/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef TABLECOLUMNSODBC_H
#define TABLECOLUMNSODBC_H

#ifdef WIN32
	#include <windows.h>	          
#endif

#include <sql.h>
#include <string>
#include <vector>

#include "DbColumn.h"
#include "DbTable.h"
#include "DbProcedureColumn.h"
#include "OdbcConnectionHolder.h"

namespace konga
{

#define SYBASE_SP_INT		28265
#define SYBASE_SP_CHAR		26723
#define SYBASE_SP_DATETIME	24932
#define SYBASE_SP_DECIMAL	25956
#define SYBASE_SP_FLOAT		27750
#define SYBASE_SP_MONEY		28525
#define SYBASE_SP_NCHAR		25454
#define SYBASE_SP_NUMERIC	30062
#define SYBASE_SP_NVARCHAR	30318
#define SYBASE_SP_REAL		25970
#define SYBASE_SP_SMALLDATETIME		28019
#define SYBASE_SP_SMALLINT			28019
#define SYBASE_SP_SMALLMONEY		28019
#define SYBASE_SP_TINYINT	26996
#define SYBASE_SP_VARCHAR	24950

                                                      
#define DTI_SQL_DATE		"Date"
#define DTI_SQL_TIME		"Time"
#define DTI_SQL_TIMESTAMP	"Time stamp"


class OdbcColumns : public OdbcConnectionHolder {
public:
    OdbcColumns(const std::string& stConnectionString = "")
		: OdbcConnectionHolder(stConnectionString) {}
    virtual ~OdbcColumns() { }

	bool GetTables(std::vector<DbTable>& vTables, const std::string& stTableSearchString,
		const std::string& stSchemaSearchString = "", bool bSqlAttrMetaDataId = false, bool bSecondTry = false);
    bool GetColumns(std::vector<DbColumn>& vColumns, const std::string& stTableName, const std::string& stSchemaName = "");
	bool GetDrivers(std::vector<std::string>& vDriverNames);

                                                                                                       
    bool GetPrimaryKeys(std::vector<std::string>& vstPKs, const std::string& stTableName, const std::string& stSchemaName = "");

	bool GetProcedureParameters(std::vector<DbProcedureColumn>& vColumns, DbProcedureColumn& retVal,
		const std::string& stProcName, const std::string& stSchemaName = "");


protected:

    bool GetColumnData(std::vector<DbColumn>& vColumns);
    bool GetTableData(std::vector<DbTable>& vTables);
	bool GetProcedureColumnData(std::vector<DbProcedureColumn>& vColumns, DbProcedureColumn& retVal);
};

}                   

#endif                      

