/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	#include <windows.h>
	#include <sqlext.h>
#else
	#include <sqlext.h>
#endif

#include <konga/Utils/StringUtils.h>				             

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbUtils.h>				             
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/TableColumnsOdbc.h>		                    

#include <konga/DbRelations/SourceTypeIdConstants.h>

#include "DeployUtils.h"

#include <konga/Utils/PortDefs.h>					                       

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::vector;

NextIdFinder::NextIdFinder(DbConnection& dbConnection) : m_dbConnection(dbConnection) { }

long NextIdFinder::GetNextId(const std::string& stTableName, const std::string& stIdName) {
                                                                                      
	if ( stIdName.empty() )
		return -1;

	return konga::GetNextId(stTableName, stIdName, m_dbConnection);
}

namespace {                    
	                                                                                      
	bool IsSqlStringType(long lSqlType) {
		return lSqlType == SQL_VARCHAR || lSqlType == SQL_CHAR || lSqlType == SQL_LONGVARCHAR ||
			lSqlType == SQL_WVARCHAR || lSqlType == SQL_WCHAR || lSqlType == SQL_WLONGVARCHAR ||
			lSqlType == SQL_TYPE_DATE || lSqlType == SQL_TYPE_TIME || lSqlType == SQL_TYPE_TIMESTAMP ||
			lSqlType == SQL_TIMESTAMP;
	}
}

void TypeInfoLoader::LoadTypeInformation(deque<bool>& IsColString,
													const vector<string>& vsColNames,
													const string& stTableName,
													bool& bHasModTimeCol) const
{
	OdbcColumns odbcCols(m_stConnectionString);
	typedef vector<DbColumn> DbColumnVectorT;
	DbColumnVectorT vColumns;
	if ( !odbcCols.GetColumns(vColumns, stTableName) )
		throw CrException("Failed to get the column information for table '" + stTableName + "'. " + odbcCols.GetLastError());

	vector<string>::const_iterator citColumnNames = vsColNames.begin();
	for( ; citColumnNames != vsColNames.end(); ++citColumnNames ) {
		const string& stInputColumnName( *citColumnNames );
		DbColumnVectorT::const_iterator citDbColumn = vColumns.begin();
		for( ; citDbColumn != vColumns.end(); ++citDbColumn ) {
			const DbColumn& dbColumn( *citDbColumn );
			if ( strcasecmp(stInputColumnName.c_str(), dbColumn.Name.c_str()) == 0 ) {
				IsColString.push_back( IsSqlStringType(dbColumn.DataType) );
				goto next;
			}
		}
		                                                                                       
		IsColString.push_back(false);
		  
                                                                                                 
                                                                                   
                                                                                                                
     

next:	continue;
	}

	                                            

	bHasModTimeCol = false;
	DbColumnVectorT::const_iterator citDbColumn = vColumns.begin();
	for( ; citDbColumn != vColumns.end(); ++citDbColumn ) {
		const DbColumn& dbColumn( *citDbColumn );
		if ( strcasecmp(MODTIME_COLNAME.c_str(), dbColumn.Name.c_str()) == 0 ) {
			bHasModTimeCol = true;
			break;
		}
	}
}

vColPosT konga::interchange_deploy::GetFieldIndexes(const xsd__string_Array& columnNames,
													const std::vector<std::string>& vColumnNamesToGet)
{
	int nColumns = -1;
	const xsd__string* arrColumnNames = columnNames.get(nColumns);
	vColPosT retval;
	vector<string>::const_iterator citColNames = vColumnNamesToGet.begin();
	for(; citColNames != vColumnNamesToGet.end(); ++citColNames) {
		const std::string& stColumnName = *citColNames;
		for( int index=0; index < nColumns; ++index ) {
			if ( !arrColumnNames[index] )	                                         
				continue;
			if ( strcasecmp(stColumnName.c_str(), arrColumnNames[index]) == 0 ) {
                                                                                              
				retval.push_back(index);
				goto next;	                                 
			}
		}
		                                                                                            
		throw CrException("Internal Error: Failed to find the index of column '" + stColumnName + "'.");

next:	continue;
	}
	return retval;
}

bool konga::interchange_deploy::IsJmsSource(const long sourceId, DbConnection& dbConnection) {
	const string sql("SELECT Source_Type_Id FROM SourcesTab WHERE Source_Id = "
		+ toString(sourceId));
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConnection, sql) );
	rs->Open();
	if ( !rs->Next() )
		return false;
	if ( rs->IsFieldNull(0) )
		return false;
	const int sourceTypeId = atoi(rs->GetFieldValue(0).c_str());
	return ( sourceTypeId == JmsSourceTypeId );
}
