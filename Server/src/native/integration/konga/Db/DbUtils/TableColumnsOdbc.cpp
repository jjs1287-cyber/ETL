/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <algorithm>
#include <map>
#include <string>
#include <vector>
#include <cassert>

#ifdef WIN32
	#include <windows.h>
	#include <konga/Utils/PortDefs.h>		                        
#endif

#include <sqlext.h>

#include <konga/Utils/StringUtils.h>

#include "DbUtils.h"
#include "TableColumnsOdbc.h"

using namespace std;                           
using namespace konga;


bool OdbcColumns::GetTables(std::vector<DbTable>& vTables, const std::string& sTableSearchString,
							const std::string& sSchemaSearchString, bool bSqlAttrMetaDataId, bool bSecondTry              )
{
    if( !IsConnected() && !Connect() )
		return false;

	vTables.clear();

	string sFinalTableSearchString(sTableSearchString), sFinalSchemaSearchString(sSchemaSearchString);
	                                
	                                                                                              
	                                                                                                  
	                                                                        
	if( bSqlAttrMetaDataId ) {
		ReplaceCharWithString(sTableSearchString, sFinalTableSearchString, '%', "");                                             
		ReplaceCharWithString(sSchemaSearchString, sFinalSchemaSearchString, '%', "");                                             
	}

    SQLCHAR* pcSchemaName = sFinalSchemaSearchString.empty() ? 0 : (SQLCHAR*)sFinalSchemaSearchString.c_str();
    SQLCHAR* pcTableName = sFinalTableSearchString.empty() ? 0 : (SQLCHAR*)sFinalTableSearchString.c_str();

	string stSetAttrErr;
	RETCODE retcode = ::SQLSetStmtAttr(m_hSqlStmt, SQL_ATTR_METADATA_ID, reinterpret_cast<SQLPOINTER>(static_cast<SQLULEN>(bSqlAttrMetaDataId ? SQL_TRUE : SQL_FALSE)), SQL_IS_UINTEGER);
    if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO) )
    {
		stSetAttrErr = "Failed to set SQL_ATTR_METADATA_ID = " + string(bSqlAttrMetaDataId?"SQL_TRUE":"SQL_FALSE") + ". ";
		stSetAttrErr += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
	}

	retcode = ::SQLTables(m_hSqlStmt, NULL, 0, pcSchemaName, pcSchemaName?SQL_NTS:0, pcTableName, pcTableName?SQL_NTS:0, NULL, 0);
    if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO) )
    {
		if( !bSecondTry ) {
			                                                                                                     
			return GetTables(vTables, sFinalTableSearchString, sFinalSchemaSearchString, !bSqlAttrMetaDataId, true);
		}
		if( !m_stLastError.empty() ) m_stLastError += '\n';
        m_stLastError += "Failed to retrieve table information. ";
        m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
		if ( !stSetAttrErr.empty() )
			m_stLastError += "\nAdditional Error: " + stSetAttrErr;
        return false;
    }

    return GetTableData(vTables);
 }

bool OdbcColumns::GetTableData(vector<DbTable>& vTables)
{
    const int STR_LEN_1 = 129;
    SQLCHAR pcCatalog[STR_LEN_1] = "";
    SQLCHAR pcSchema[STR_LEN_1] = "";
    SQLCHAR pcTableName[STR_LEN_1] = "";
    SQLCHAR pcTableType[STR_LEN_1] = "";

	SQLLEN cbCatalog, cbSchema, cbTableName, cbTableType;

    ::SQLBindCol(m_hSqlStmt, 1, SQL_C_CHAR, pcCatalog, sizeof(pcCatalog), &cbCatalog);
    ::SQLBindCol(m_hSqlStmt, 2, SQL_C_CHAR, pcSchema, sizeof(pcSchema), &cbSchema);
    ::SQLBindCol(m_hSqlStmt, 3, SQL_C_CHAR, pcTableName, sizeof(pcTableName), &cbTableName);
    ::SQLBindCol(m_hSqlStmt, 4, SQL_C_CHAR, pcTableType, sizeof(pcTableType), &cbTableType);

    std::map<std::string, bool> mTables;
    while( ::SQLFetch(m_hSqlStmt) != SQL_NO_DATA )
    {
        std::string stTableName((const char*)pcTableName);
                                                          
        {
            DbTable tab;
            tab.Catalog = (const char*)pcCatalog;
            tab.Schema = (const char*)pcSchema;
            tab.Name = stTableName;
            tab.Type = (const char*)pcTableType;
            vTables.push_back(tab);
            mTables[stTableName] = true;
        }
    }   

    return true;
}

namespace {

	bool IsSameName(const string& n1, const string n2) {
		return konga::EqualsIgnoreCase(n1, n2);
	}

}


bool OdbcColumns::GetColumns(std::vector<DbColumn>& vColumns, const std::string& stTableName, const std::string& stSchemaName           )
{
	if( !IsConnected() ) {
		if ( !Connect(false) ) {
			return false;
		}
	}

	if ( !ReAllocateStatementHandle() ) {
		return false;
	}

	vColumns.clear();

    SQLCHAR* pcSchemaName = stSchemaName.empty() ? 0 : (SQLCHAR*)stSchemaName.c_str();
    SQLSMALLINT iSchemaLenFlag = stSchemaName.empty() ? 0 : SQL_NTS;
    SQLCHAR* pcTableName = stTableName.empty() ? 0 : (SQLCHAR*)stTableName.c_str();
    SQLSMALLINT iTableLenFlag = stTableName.empty() ? 0 : SQL_NTS;

    RETCODE retcode = ::SQLColumns(m_hSqlStmt, NULL, 0, pcSchemaName, iSchemaLenFlag, pcTableName, iTableLenFlag, NULL, 0);
    if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO || retcode == SQL_STILL_EXECUTING) )
    {
        m_stLastError += "Failed to retrieve column information.\n";
        m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
        m_stLastError += "\n";
        return false;
    }

	                                                               

	                                                                                          
	                                            
	                                                                       
	std::vector<DbColumn> allColumns;
    bool success = GetColumnData(allColumns);
	if ( success ) {
		for(vector<DbColumn>::const_iterator it = allColumns.begin(); it != allColumns.end(); ++it) {
			if (IsSameName(it->Table, stTableName)) {
				vColumns.push_back(*it);
			}
		}
	}
	FreeStatementHandle();
	return success;
}

bool OdbcColumns::GetProcedureParameters(std::vector<DbProcedureColumn>& vColumns,
										 DbProcedureColumn& retVal,
										 const std::string& stProcName,
										 const std::string& stSchemaName)
{
    if( !IsConnected() && !Connect() )
            return false;

    SQLCHAR* pcSchemaName = stSchemaName.empty() ? 0 : (SQLCHAR*)stSchemaName.c_str();
    SQLSMALLINT iSchemaLenFlag = stSchemaName.empty() ? 0 : SQL_NTS;
    SQLCHAR* pcProcName = stProcName.empty() ? 0 : (SQLCHAR*)stProcName.c_str();
    SQLSMALLINT iProcLenFlag = stProcName.empty() ? 0 : SQL_NTS;

	                                 
	RETCODE retcode = ::SQLProcedures(m_hSqlStmt, NULL, 0,
		pcSchemaName, iSchemaLenFlag, pcProcName, iProcLenFlag);

	retcode = ::SQLFetch(m_hSqlStmt);
	if ( retcode == SQL_NO_DATA ) {
		m_stLastError = "Failed to find the stored procedure \""
			+ stProcName + "\".";
		return false;
	}

	if ( !ReAllocateStatementHandle() )
		return false;

	retcode = ::SQLProcedureColumns(m_hSqlStmt, NULL, 0,
		pcSchemaName, iSchemaLenFlag, pcProcName, iProcLenFlag, NULL, 0);
    if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO || retcode == SQL_STILL_EXECUTING) )
    {
        m_stLastError += "Failed to retrieve procedure information.\n";
        m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
        m_stLastError += "\n";
        return false;
    }

	return GetProcedureColumnData(vColumns, retVal);
}

namespace {
	struct OrdinalSort
	{
		 bool operator()(const DbProcedureColumn& c1, const DbProcedureColumn& c2)
		 {
			 return c1.OrdinalPosition < c2.OrdinalPosition;
		 }
	};
}

bool OdbcColumns::GetProcedureColumnData(std::vector<DbProcedureColumn>& vColumns,
										 DbProcedureColumn& retVal)
{
    const int STR_LEN_1 = 512;
    const int STR_LEN_2 = 1024;

    SQLCHAR pcCatalog[STR_LEN_1] = "";
    SQLCHAR pcSchema[STR_LEN_1] = "";
    SQLCHAR pcProcName[STR_LEN_1] = "";
    SQLCHAR pcColumnName[STR_LEN_1] = "";
    SQLSMALLINT nDataType = SQL_UNKNOWN_TYPE;
    SQLSMALLINT nColumnType = 0;	                                   
    SQLCHAR pcDataTypeName[STR_LEN_1] = "";
    SQLINTEGER nColumnSize = 0;
    SQLINTEGER nBufferLength = 0;
    SQLSMALLINT nDecimalDigits = 0;
    SQLSMALLINT nRadix = 0;
    SQLSMALLINT nNullable = 0;
    SQLCHAR pcRemarks[STR_LEN_2] = "";
    SQLCHAR pcDefault[STR_LEN_2] = "";
    SQLSMALLINT nSqlDataType = SQL_UNKNOWN_TYPE;
    SQLSMALLINT nDateTimeSub = SQL_UNKNOWN_TYPE;
    SQLINTEGER nOctetLength = 0;
    SQLINTEGER nOrdinalPosition = 0;
    SQLCHAR pcIsNullable[STR_LEN_1] = "";

    SQLLEN cbCatalog=0, cbSchema=0, cbProcName=0, cbColumnName=0, cbColumnType=0, cbDataType=0, cbDataTypeName=0,
				cbColumnSize=0, cbBufferLength=0, cbDecimalDigits=0, cbRadix=0, cbNullable=0, cbRemarks=0,
				cbDefault=0, cbSqlDataType=0, cbDateTimeSub=0, cbOctetLength=0, cbOrdinalPosition=0, cbIsNullable=0;

	                   
    RETCODE retcode = ::SQLBindCol(m_hSqlStmt, 1, SQL_C_CHAR, pcCatalog, sizeof(pcCatalog), &cbCatalog);
    retcode = ::SQLBindCol(m_hSqlStmt, 2, SQL_C_CHAR, pcSchema, sizeof(pcSchema), &cbSchema);
    retcode = ::SQLBindCol(m_hSqlStmt, 3, SQL_C_CHAR, pcProcName, sizeof(pcProcName), &cbProcName);

    retcode = ::SQLBindCol(m_hSqlStmt, 4, SQL_C_CHAR, pcColumnName, sizeof(pcColumnName), &cbColumnName);
	retcode = ::SQLBindCol(m_hSqlStmt, 5, SQL_C_SSHORT, &nColumnType, 0, &cbColumnType);
    retcode = ::SQLBindCol(m_hSqlStmt, 6, SQL_C_SSHORT, &nDataType, 0, &cbDataType);

	retcode = ::SQLBindCol(m_hSqlStmt, 7, SQL_C_CHAR, pcDataTypeName, sizeof(pcDataTypeName), &cbDataTypeName);
    retcode = ::SQLBindCol(m_hSqlStmt, 8, SQL_C_SLONG, &nColumnSize, 0, &cbColumnSize);
    retcode = ::SQLBindCol(m_hSqlStmt, 9, SQL_C_SLONG, &nBufferLength, 0, &cbBufferLength);

    retcode = ::SQLBindCol(m_hSqlStmt, 10, SQL_C_SSHORT, &nDecimalDigits, 0, &cbDecimalDigits);
    retcode = ::SQLBindCol(m_hSqlStmt, 11, SQL_C_SSHORT, &nRadix, 0, &cbRadix);
    retcode = ::SQLBindCol(m_hSqlStmt, 12, SQL_C_SSHORT, &nNullable, 0, &cbNullable);
    retcode = ::SQLBindCol(m_hSqlStmt, 13, SQL_C_CHAR, pcRemarks, sizeof(pcRemarks), &cbRemarks);

	if( this->GetDriverVersion() >= 3.0 ) {
		retcode = ::SQLBindCol(m_hSqlStmt, 14, SQL_C_CHAR, pcDefault, sizeof(pcDefault), &cbDefault);
		retcode = ::SQLBindCol(m_hSqlStmt, 15, SQL_C_SSHORT, &nSqlDataType, 0, &cbSqlDataType);
		retcode = ::SQLBindCol(m_hSqlStmt, 16, SQL_C_SSHORT, &nDateTimeSub, 0, &cbDateTimeSub);
		retcode = ::SQLBindCol(m_hSqlStmt, 17, SQL_C_SLONG, &nOctetLength, 0, &cbOctetLength);
		retcode = ::SQLBindCol(m_hSqlStmt, 18, SQL_C_SLONG, &nOrdinalPosition, 0, &cbOrdinalPosition);
		retcode = ::SQLBindCol(m_hSqlStmt, 19, SQL_C_CHAR, pcIsNullable, sizeof(pcIsNullable), &cbIsNullable);
	}

    while( (retcode = ::SQLFetch(m_hSqlStmt)) == SQL_SUCCESS )
    {
        DbProcedureColumn col;
        col.Catalog = (const char*)pcCatalog;
        col.Schema = (const char*)pcSchema;
        col.ProcedureName = (const char*)pcProcName;
        col.Name = (const char*)pcColumnName;	                                              
		col.ColumnType = nColumnType;
		if ( nColumnType == SQL_RETURN_VALUE )
			col.IsReturnValue = true;

		col.DataType = nDataType;
        col.DataTypeName = (const char*)pcDataTypeName;
        col.ColumnSize = nColumnSize;
        col.BufferLength = nBufferLength;
        col.DecimalDigits = nDecimalDigits;
        col.Radix = nRadix;
		col.Nullable = nNullable;

        col.Remarks = (const char*)pcRemarks;

		                   
		if( this->GetDriverVersion() >= 3.0 ) {
			col.HasDefaultValue = ( cbDefault > 0 );
			if( col.HasDefaultValue )
				col.DefaultValue = (const char*)pcDefault;
			col.SqlDataType = nSqlDataType;
			col.SqlDataTypeName = (const char*)pcDataTypeName;
			col.DateTimeSub = nDateTimeSub;
			col.OctetLength = nOctetLength;
			col.OrdinalPosition = nOrdinalPosition;
			
			if ( strcasecmp((const char*)pcIsNullable, "NO") == 0 )
				col.IsNullable = false;
			else if ( strcasecmp((const char*)pcIsNullable, "YES") == 0 )
				col.IsNullable = true;
			else
				col.IsNullable = false;
		}
		else {
			                                                                           
			col.SqlDataType = col.DataType;
		}

		if ( col.IsReturnValue ) {
			retVal = col;
		}
		else {
			vColumns.push_back(col);
		}

		                     
		nDataType = SQL_UNKNOWN_TYPE;
		nSqlDataType = SQL_UNKNOWN_TYPE;
		nDateTimeSub = SQL_UNKNOWN_TYPE;
    }

    if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO || 
			retcode == SQL_STILL_EXECUTING || retcode == SQL_NO_DATA) )
    {
        m_stLastError += "Failed to retrieve column information.\n";
        m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
        m_stLastError += "\n";
        return false;
    }

	                    
	std::sort(vColumns.begin(), vColumns.end(), OrdinalSort());
	return true;
}

bool OdbcColumns::GetColumnData(vector<DbColumn>& vColumns)
{
    const int STR_LEN_1 = 512;
    const int STR_LEN_2 = 1024;
    SQLCHAR pcCatalog[STR_LEN_1] = "";
    SQLCHAR pcSchema[STR_LEN_1] = "";
    SQLCHAR pcTableName[STR_LEN_1] = "";
    SQLCHAR pcColumnName[STR_LEN_1] = "";
    SQLSMALLINT nDataType = 0;
    SQLCHAR pcDataTypeName[STR_LEN_1] = "";
    SQLINTEGER nColumnSize = 0;
    SQLINTEGER nBufferLength = 0;
    SQLSMALLINT nDecimalDigits = 0;
    SQLSMALLINT nRadix = 0;
    SQLSMALLINT nNullable = 0;
    SQLCHAR pcRemarks[STR_LEN_2] = "";
    SQLCHAR pcDefault[STR_LEN_2] = "";
    SQLSMALLINT nSqlDataType = 0;
    SQLSMALLINT nDateTimeSub = 0;
    SQLINTEGER nOctetLength = 0;
    SQLINTEGER nOrdinalPosition = 0;
    SQLCHAR pcIsNullable[STR_LEN_2] = "";

    SQLLEN cbCatalog=0, cbSchema=0, cbTableName=0, cbColumnName=0, cbDataType=0, cbDataTypeName=0,
				cbColumnSize=0, cbBufferLength=0, cbDecimalDigits=0, cbRadix=0, cbNullable=0, cbRemarks=0,
				cbDefault=0, cbSqlDataType=0, cbDateTimeSub=0, cbOctetLength=0, cbOrdinalPosition=0, cbIsNullable=0;
	RETCODE retcode;

	                   
    retcode = ::SQLBindCol(m_hSqlStmt, 1, SQL_C_CHAR, pcCatalog, sizeof(pcCatalog), &cbCatalog);
    retcode = ::SQLBindCol(m_hSqlStmt, 2, SQL_C_CHAR, pcSchema, sizeof(pcSchema), &cbSchema);
    retcode = ::SQLBindCol(m_hSqlStmt, 3, SQL_C_CHAR, pcTableName, sizeof(pcTableName), &cbTableName);
    retcode = ::SQLBindCol(m_hSqlStmt, 4, SQL_C_CHAR, pcColumnName, sizeof(pcColumnName), &cbColumnName);
    retcode = ::SQLBindCol(m_hSqlStmt, 5, SQL_C_SSHORT, &nDataType, 0, &cbDataType);
    retcode = ::SQLBindCol(m_hSqlStmt, 6, SQL_C_CHAR, pcDataTypeName, sizeof(pcDataTypeName), &cbDataTypeName);
    retcode = ::SQLBindCol(m_hSqlStmt, 7, SQL_C_SLONG, &nColumnSize, 0, &cbColumnSize);
    retcode = ::SQLBindCol(m_hSqlStmt, 8, SQL_C_SLONG, &nBufferLength, 0, &cbBufferLength);
    retcode = ::SQLBindCol(m_hSqlStmt, 9, SQL_C_SSHORT, &nDecimalDigits, 0, &cbDecimalDigits);
    retcode = ::SQLBindCol(m_hSqlStmt, 10, SQL_C_SSHORT, &nRadix, 0, &cbRadix);
    retcode = ::SQLBindCol(m_hSqlStmt, 11, SQL_C_SSHORT, &nNullable, 0, &cbNullable);
    retcode = ::SQLBindCol(m_hSqlStmt, 12, SQL_C_CHAR, pcRemarks, sizeof(pcRemarks), &cbRemarks);

	                   
	if( this->GetDriverVersion() >= 3.0 ) {
		retcode = ::SQLBindCol(m_hSqlStmt, 13, SQL_C_CHAR, pcDefault, sizeof(pcDefault), &cbDefault);
		retcode = ::SQLBindCol(m_hSqlStmt, 14, SQL_C_SSHORT, &nSqlDataType, 0, &cbSqlDataType);
		retcode = ::SQLBindCol(m_hSqlStmt, 15, SQL_C_SSHORT, &nDateTimeSub, 0, &cbDateTimeSub);
		retcode = ::SQLBindCol(m_hSqlStmt, 16, SQL_C_SLONG, &nOctetLength, 0, &cbOctetLength);
		retcode = ::SQLBindCol(m_hSqlStmt, 17, SQL_C_SLONG, &nOrdinalPosition, 0, &cbOrdinalPosition);
		retcode = ::SQLBindCol(m_hSqlStmt, 18, SQL_C_CHAR, pcIsNullable, sizeof(pcIsNullable), &cbIsNullable);
	}

	                                                                                 
                                                                                           
    std::map<std::string, bool> mColumns;
    while( (retcode = ::SQLFetch(m_hSqlStmt)) == SQL_SUCCESS )
    {
        std::string stColumnName((const char*)pcColumnName);
        if( mColumns.find(stColumnName) == mColumns.end() )
        {
            DbColumn col;
            col.Catalog = (const char*)pcCatalog;
            col.Schema = (const char*)pcSchema;
            col.Name = stColumnName;
            col.Table = (const char*)pcTableName;
            col.DataType = nDataType;
            col.DataTypeName = (const char*)pcDataTypeName;
            col.Size = nColumnSize;
            col.BufferLength = nBufferLength;
            col.DecimalDigits = nDecimalDigits;
            col.Radix = nRadix;
            col.Nullable = nNullable;
            col.Remarks = (const char*)pcRemarks;

			                   
			if( this->GetDriverVersion() >= 3.0 ) {
				col.HasDefaultValue = ( cbDefault > 0 );
				if( col.HasDefaultValue )
					col.DefaultValue = (const char*)pcDefault;
				col.SqlDataType = nSqlDataType;
				col.SqlDataTypeName = (const char*)pcDataTypeName;
				col.DateTimeSub = nDateTimeSub;
				col.OctetLength = nOctetLength;
				col.OrdinalPosition = nOrdinalPosition;
				col.IsNullable = nNullable != 0;
			}

			  
                                                     

                  
                                                                                                   

              
                  
             
     

            vColumns.push_back(col);
            mColumns[stColumnName] = true;
        }
    }

    if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO || 
			retcode == SQL_STILL_EXECUTING || retcode == SQL_NO_DATA) )
    {
        m_stLastError += "Failed to retrieve column information.\n";
        m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
        m_stLastError += "\n";
        return false;
    }
    return true;
}

bool OdbcColumns::GetPrimaryKeys(std::vector<std::string>& vstPKs, const std::string& stTableName, const std::string& stSchemaName           )
{
    if( !IsConnected() && !Connect() )
            return false;

	vstPKs.clear();

    SQLUSMALLINT supported;
    RETCODE retcode = ::SQLGetFunctions(m_hSqlDBC, SQL_API_SQLPRIMARYKEYS, &supported);
    if( retcode == SQL_SUCCESS && supported == SQL_TRUE )
    {
        SQLCHAR* pcTableName = stTableName.empty() ? 0 : (SQLCHAR*)stTableName.c_str();
        SQLCHAR* pcSchemaName = stSchemaName.empty() ? 0 : (SQLCHAR*)stSchemaName.c_str();

        retcode = ::SQLPrimaryKeys(m_hSqlStmt, NULL, 0, pcSchemaName, SQL_NTS, pcTableName, SQL_NTS);
        if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO) )
        {
            m_stLastError += "Failed to retrieve primary key information.\n";
            m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
            m_stLastError += "\n";
            return false;
        }

        const int STR_LEN = 129;
        SQLCHAR pcColumnName[STR_LEN] = "";
        SQLLEN cbColumnName = 0;
        retcode = ::SQLBindCol(m_hSqlStmt, 4, SQL_C_CHAR, pcColumnName, STR_LEN, &cbColumnName);
		if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO) ) {
            m_stLastError += "Failed to retrieve primary key information. SQLBindCol failed.\n";
            m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
		}

                                                                                                          
                      
        std::map<std::string, bool> mPKs;
        while( (retcode=::SQLFetch(m_hSqlStmt)) != SQL_NO_DATA )
        {
			if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO) ) {
				m_stLastError += "Failed to retrieve primary key information. SQLFetch failed.\n";
				m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
			}
            std::string stPK((const char*)pcColumnName);
            if( !stPK.empty() && mPKs.find(stPK) == mPKs.end() )
            {
                vstPKs.push_back(stPK);
                mPKs[stPK] = true;
            }
        }
        return true;
    }
    else
    {
		return true;                                                                                        
    }
}

bool OdbcColumns::GetDrivers(vector<string>& vDriverNames)
{
    if( !AllocateEnvironmentHandle() )
		return false;

	vDriverNames.clear();

	const int DRIVER_NAME_MAX_LENGTH = 1024;
    SQLCHAR pcDriverName[DRIVER_NAME_MAX_LENGTH];
	SQLSMALLINT cbDriverName;

	int nSqlFetch = SQL_FETCH_FIRST;
	RETCODE retcode;
	while(true) {
		retcode = ::SQLDrivers(m_hSqlEnv, nSqlFetch, pcDriverName, DRIVER_NAME_MAX_LENGTH, &cbDriverName, 0, 0, 0);
		nSqlFetch = SQL_FETCH_NEXT;

		if( retcode == SQL_NO_DATA ) break;

		if( !(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO) )
		{
			m_stLastError += "Failed to retrieve driver information.\n";
			m_stLastError += GetError(SQL_HANDLE_STMT, m_hSqlStmt);
			m_stLastError += "\n";
			return false;
		}

		if( cbDriverName > 0 && pcDriverName )
			vDriverNames.push_back(reinterpret_cast<const char*>(pcDriverName));
	}
	return true;
}

