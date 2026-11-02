/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <string>
#include <map>
#include <memory>		                

#ifdef USE_DTL_FOR_SCHEMA_INFO
#include <dtl/DTL.h>
#endif                           

#include <konga/Db/DbUtils/DbUtils.h>	               
#include <konga/Db/DbUtils/TableColumnsOdbc.h>	              
#include <konga/Db/DbUtils/ConnectionString.h>

#include "DatabaseTable.h"
#include "DatabaseView.h"
#include "SchemaInfo.h"

using namespace std;
using namespace dtl;
using namespace konga;

#ifdef USE_DTL_FOR_SCHEMA_INFO
  
                                                                                             
                          
                                                                                       
  
void SchemaInfo::GetObjects(list<DatabaseObject*>& objs, map<string, bool> mObjTypes, const std::string& stSearchString) {
	if( !m_db.IsConnected() )
		m_db.Connect(m_stConnectionString);

	string stQuery = "_SQLTables" + (stSearchString.empty() ? "" : ("," + stSearchString));
	DynamicDBView<>::Args args(DynamicDBView<>::Args().tables(stQuery).conn(m_db));
	DynamicDBView<> view(args);
	
	DynamicDBView<>::sql_iterator it = view.begin(), end_it = view.end();
	string stObjType, stObjName;
	for(;it != end_it; ++it) {
		stObjType = static_cast<const char*>( it.GetDataObj()["TABLE_TYPE"] );

		if( mObjTypes.find( stObjType ) != mObjTypes.end() ) {
			DatabaseObject* pObj = new DatabaseObject( stObjType );
			stObjName = static_cast<const char*>( it.GetDataObj()["TABLE_NAME"] );
			pObj->SetName(stObjName);

			                   
			vector<DatabaseColumn*> vCols;
			GetColumns(vCols, stObjName);
			pObj->SetColumns(vCols);

			objs.push_back(pObj);
		}
	}
}

void SchemaInfo::GetColumns(vector<DatabaseColumn*>& vCols, const std::string& stTableName) {
	                                                                      
	DynamicDBView<>::Args args(DynamicDBView<>::Args().tables("_SQLColumns," + stTableName).conn(m_db));
	DynamicDBView<> view(args);
	
	DynamicDBView<>::sql_iterator it = view.begin(), end_it = view.end();

	string stColName;
	DatabaseColumn* pCol;
	for(;it != end_it; ++it) {
		pCol = new DatabaseColumn();
		stColName = static_cast<const char*>( it.GetDataObj()["COLUMN_NAME"] );
		pCol->SetName(stColName);
		vCols.push_back(pCol);
	}
}

#elif defined(USE_ODBC_FOR_SCHEMA_INFO)

namespace {
	const string ROWID_DRIVER("informix");
	const string ROWID_NAME("rowid");

	                                   
                                           
                                  

	bool IsInformixDriver(const string& sDriverName) {
		string lowerName(sDriverName);
		konga::ToLower(lowerName);
		return lowerName.find(ROWID_DRIVER) != string::npos;
	}

	void AddInformixRowIdColumn(vector<DatabaseColumn*>& vCols) {
		auto_ptr<DatabaseColumn> pCol( new DatabaseColumn() );
		pCol->SetName(ROWID_NAME);
		pCol->SetType("int");
		pCol->SetSqlType(SQL_INTEGER);
		pCol->SetDefaultValue("");
		pCol->SetNullable(true);
		pCol->SetCharacterSize(10);
		pCol->SetDecimalDigits(0);
		vCols.push_back(pCol.release());
	}
}

void SchemaInfo::GetObjects(vector<DatabaseObject*>& objs, map<string, bool> mObjTypes,
							const string& stTableSearchString,
							bool bGetColumnInformation,
							const string& stSchemaSearchString,
							bool bSqlAttrMetaDataId)
{
	OdbcColumns odbcSchemaInfo(m_stConnectionString);

	vector<DbTable> vTables;
	if( !odbcSchemaInfo.GetTables(vTables, stTableSearchString, stSchemaSearchString, bSqlAttrMetaDataId) )
		throw CrException( odbcSchemaInfo.GetLastError() + " Failed to get tables information.");

	m_stQuoteChar = odbcSchemaInfo.getQuoteString();

	OdbcConnectionString cs(m_stConnectionString);
	bool isInformix = false;
	if ( bGetColumnInformation ) {
		isInformix = IsInformixDriver(cs.getDriver());;
	}

	vector<DbTable>::const_iterator it = vTables.begin(), end_it = vTables.end();
	string stObjType, stObjName;
	for(;it != end_it; ++it) {
		const string& objectType = it->Type;
		if( mObjTypes.find( objectType ) != mObjTypes.end() ) {
			auto_ptr<DatabaseObject> pObj( new DatabaseObject( objectType ) );
			pObj->SetName(it->Name);
			pObj->SetSchemaName(it->Schema);

			if ( bGetColumnInformation ) {
				                   
				OdbcColumns odbcColInfo(m_stConnectionString);
				vector<DbColumn> vCols;
				if( !odbcColInfo.GetColumns(vCols, it->Name, it->Schema) )
					throw CrException("Failed to get column information for table '" + it->Name +
										"'. Reason: " + odbcColInfo.GetLastError());

				                                                             
				vector<string> vPrimaryKeys;
				if ( !vCols.empty() ) {
					OdbcColumns odbcPrimaryKeys(m_stConnectionString);
					if( !odbcPrimaryKeys.GetPrimaryKeys(vPrimaryKeys, it->Name, it->Schema) ) {
						vPrimaryKeys.clear();
					}
				}

				vector<DatabaseColumn*> vCols1;
				if ( isInformix && (objectType == DBTABLE_TYPE_TABLE || objectType == DBTABLE_TYPE_SYSTEM_TABLE) ) {
					AddInformixRowIdColumn(vCols1);
				}

				for(vector<DbColumn>::const_iterator col_it = vCols.begin(); col_it != vCols.end(); ++col_it) {
					auto_ptr<DatabaseColumn> pCol( new DatabaseColumn() );
					pCol->SetName(col_it->Name);
					pCol->SetType(col_it->DataTypeName);
					pCol->SetSqlType(col_it->SqlDataType);
					pCol->SetDefaultValue(col_it->DefaultValue);
					pCol->SetNullable(col_it->IsNullable);
					pCol->SetCharacterSize(col_it->Size);
					pCol->SetDecimalDigits(col_it->DecimalDigits);

					                                                                 
					pCol->SetPrimaryKey(false);
					for(vector<string>::const_iterator pk_it = vPrimaryKeys.begin(); pk_it != vPrimaryKeys.end(); ++pk_it){
						if( col_it->Name == *pk_it ) {
							pCol->SetPrimaryKey(true);
							break;
						}
					}
					vCols1.push_back(pCol.release());
				}
				pObj->SetColumns(vCols1);
			}

			objs.push_back(pObj.release());
		}
	}
}

void SchemaInfo::GetDrivers(vector<string>& vDriverNames) {
	OdbcColumns odbcSchemaInfo;

	if( !odbcSchemaInfo.GetDrivers(vDriverNames) )
		throw CrException("Failed to get list of database drivers. Reason: " + odbcSchemaInfo.GetLastError());
}

#else
	#error "Must define either USE_DTL_FOR_SCHEMA_INFO or USE_ODBC_FOR_SCHEMA_INFO."
#endif

