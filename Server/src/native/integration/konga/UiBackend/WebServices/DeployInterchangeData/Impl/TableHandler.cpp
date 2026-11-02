/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbUtils.h>	              

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/KeyInfo.h>
#include <konga/DbRelations/TableKeyInfo.h>
#include <konga/DbRelations/DependentTableInfo.h>

#include <konga/InterchangeProject/InterchangeProject.h>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>	             

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "SpecialHandlingDeployFolder.h"				                                           
#include "DeployHistory.h"
#include "TableHandler.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::vector;
using std::stringstream;
using std::deque;
using std::map;

namespace {
	string GetColumnNameList(const InterchangeDataTableStruct& table, vector<string>& vsColNames) {

		stringstream ssColNames;
		vector<string>::const_iterator cit = table.m_vColumnNames.begin();
		for(; cit != table.m_vColumnNames.end(); ++cit ) {
			vsColNames.push_back(*cit);
			if ( cit != table.m_vColumnNames.begin() )
				ssColNames << ",";
			ssColNames << *cit;
		}
		return ssColNames.str();
	}
}

void TableHandler::HandleTable(const string& stName, const InterchangeDataTable* const pTable,
					SpecialHandlingDeploy* const pSpecialHandlingInsert,
					SpecialHandlingDeploy* const pSpecialHandlingUpdate)
{
	m_nChangedItems = 0;
	if ( pTable == 0 )
		return;

	InterchangeDataTableStruct tableStruct;
	FillInterchangeDataTableStruct(*pTable, tableStruct);

	return HandleTable(stName, tableStruct, pSpecialHandlingInsert, pSpecialHandlingUpdate);
}

void TableHandler::HandleTable(const string& stName, InterchangeDataTableStruct& tableStruct,
							   SpecialHandlingDeploy* const pSpecialHandlingInsert,
							   SpecialHandlingDeploy* const pSpecialHandlingUpdate)
{
	m_nChangedItems = 0;
	m_stName = stName;
	m_pSpecialHandlingInsert = pSpecialHandlingInsert;
	m_pSpecialHandlingUpdate = pSpecialHandlingUpdate;

	m_iEntityTypeId = GetEntityIdByName(m_stName);

	m_stTableName = getTableName(m_stName);
	m_vsColNames.clear();
	const string stColNames( GetColumnNameList(tableStruct, m_vsColNames) );

	m_vIsColString.clear();
	m_typeInfoLoader.LoadTypeInformation(m_vIsColString, m_vsColNames, m_stTableName, m_bHasModTimeCol);

	m_lNextId = m_nextIdFinder.GetNextId(m_stTableName, getIdName(stName));

	                              
	const TableKeyInfo& tabKeyInfo = m_tranDbRelations.GetTableKeyInfo(m_stTableName);

	                          
	m_iPrimaryKeyIndex = -1;
	m_keyIndexMap.clear();
	const vector<KeyInfo>& keys = tabKeyInfo.GetKeyInfo();
	for( vector<KeyInfo>::const_iterator citKeyInfo = keys.begin(); citKeyInfo != keys.end(); ++citKeyInfo ) {
		const string stColumnName( citKeyInfo->GetColumnName() );
		for( unsigned int index = 0; index < m_vsColNames.size(); ++index ) {
			if ( strcasecmp(stColumnName.c_str(), m_vsColNames[index].c_str()) == 0 ) {
				if ( citKeyInfo->IsPrimary() ) {
					m_iPrimaryKeyIndex = index;
					m_keyIndexMap[index] = KeyIndex(index, true, false, citKeyInfo->GetEntityTypeId());
				}
				else {
					bool bIsDependent = citKeyInfo->IsDependent();
					int iForeignKeyEntityType = GetEntityTypeForPrimaryKey(citKeyInfo->GetParentTableName());
					m_keyIndexMap[index] = KeyIndex(index, false, bIsDependent, iForeignKeyEntityType);
				}
				goto next;
			}
		}
		throw CrException("Failed to find index of update id column " + stColumnName + " for table " + m_stTableName);
next:	continue;
	}

	                                   
	vInterchangeDataRowStructT::iterator citRow = tableStruct.m_vRows.begin();
	for(; citRow != tableStruct.m_vRows.end(); ++citRow ) {
		InterchangeDataRowStruct& row = *citRow;

		                                  
		  
		                                                   
		if ( m_iPrimaryKeyIndex < 0 ) {
			InsertRow(row);
		}
		else {
			                                           
			const string& stGuid = row.m_vFields.at(m_iPrimaryKeyIndex);
			if ( m_iEntityTypeId < 1 )
				throw CrException("Internal Error: Trying to find GUID " + stGuid + " for non-entity type " + m_stName);

			long lId = m_guidToIdMapper.GetId(stGuid, m_iEntityTypeId);
			bool bInsert = false;
			if ( lId == GuidToIdMapper::invalid_id ) {
				bInsert = true;
			}
			
			if ( bInsert )
				InsertRow(row);
			else
				UpdateRow(row);
		}
	}
}

void TableHandler::ExecuteSql(const string& stSql) {
	UIBACKEND_TRACE("TableHandler::ExecuteSql " + stSql);
	m_dbConnection.ExecuteSql(stSql);
}

int TableHandler::GetEntityTypeForPrimaryKey(const string& stTableName) {
	const TableKeyInfo& tableKeyInfo = m_tranDbRelations.GetTableKeyInfo(stTableName);
	const KeyInfo* pPrimaryKey = tableKeyInfo.GetPrimaryKey();
	if ( pPrimaryKey == 0 )
		throw CrException("Internal Error: The table " + stTableName + " does not have a primary key.");

	return pPrimaryKey->GetEntityTypeId();
}

const TableHandler::KeyIndex* TableHandler::GetKeyIndex(unsigned int index) const {
	map<unsigned int, KeyIndex>::const_iterator cit = m_keyIndexMap.find(index);
	if ( cit == m_keyIndexMap.end() )
		return 0;

	return &(cit->second);
}

void TableHandler::InsertRow(InterchangeDataRowStruct& row) {
	m_pSpecialHandlingInsert->HandleRow(row);

	stringstream ssFieldNameClause;
	stringstream ssValuesClause;

	                                          
	bool bFirst = true;
	long primaryKeyId = -1;
	string stPrimaryGuid;
	vFieldsT::iterator itFields = row.m_vFields.begin();
	int iCol = 0;
	for(; itFields != row.m_vFields.end(); ++itFields, ++iCol ) {
		if ( *itFields == 0 || itFields->skip() )
			continue;

		const string& stRawColumnValue = itFields->str();
		string stSqlValue;
		if( m_vIsColString[iCol] ) {
			SqlEscape(stRawColumnValue, stSqlValue);
			stSqlValue.insert(0, 1, '\'');
			stSqlValue.insert(stSqlValue.size(), 1, '\'');
		} else {
			long long lColumnValue = -1;

			                                                
			const KeyIndex* pKeyIndex = GetKeyIndex(iCol);
			if ( pKeyIndex != 0 ) {
				if ( pKeyIndex->m_bIsPrimary ) {
					                                                                        
					lColumnValue = m_lNextId++;
					primaryKeyId = lColumnValue;

					const string stGuid(stRawColumnValue);
					stPrimaryGuid = stGuid;

					                                                                     
					m_guidToIdMapper.SetGuidForItem(lColumnValue, m_iEntityTypeId, stGuid);
					m_userAccess.AddItemOwner(primaryKeyId, m_iEntityTypeId);
					m_interchangeProject.AddItem(m_iEntityTypeId, primaryKeyId);

					                         
					AddToDeployHistory(m_iEntityTypeId, primaryKeyId, m_userInfo, m_dbConnection, m_guidToIdMapper);

					(*itFields) = toString(lColumnValue);	                          
				}
				else {
					                                     
					const int iEntityTypeId = pKeyIndex->m_iEntityTypeId;
					lColumnValue = m_guidToIdMapper.GetId(stRawColumnValue, iEntityTypeId);
					if ( lColumnValue == GuidToIdMapper::invalid_id ) {
						this->AddWarning(m_iEntityTypeId, stPrimaryGuid, "Failed to find foreign key for GUID="
						+ stRawColumnValue + " of type " + GetEntityNameByEntityId(iEntityTypeId) +
						". A possible cause for this error is that some dependent objects"\
						" have not been deployed to this server. Try to deploy everything in your current project to this server."\
						" If this does not work it could be a bug in either the server or the client.");
						continue;
					}

					if ( pKeyIndex->m_bIsDependent ) {
						                                                                
						if ( !m_userAccess.HasWritePermission(lColumnValue, iEntityTypeId) ) {
							UIBACKEND_TRACE("Warning: This might be a bug but maybe not. User does not have access to the parent item with Guid = " + stRawColumnValue);
							return;
						}
					}
					(*itFields) = toString(lColumnValue);	                         
				}
			}
			else {
				                                 
				lColumnValue = atoll(stRawColumnValue.c_str());
			}

			stSqlValue = toString(lColumnValue);
		}

		                                         
		if ( bFirst) {
			bFirst = false;
		}
		else {
			ssFieldNameClause << ",";
			ssValuesClause << ",";
		}
		ssValuesClause << stSqlValue;
		ssFieldNameClause << m_vsColNames[iCol];
	}                     

	stringstream ssSql;
	ssSql << "INSERT INTO " << m_stTableName << " (" << ssFieldNameClause.str() << ")"
			<< " VALUES(" << ssValuesClause.str() << ")";
	const string& sSql = ssSql.str();
	UIBACKEND_TRACE(sSql);
	ExecuteSql(sSql);
	++m_nChangedItems;

	if ( m_pSpecialHandlingInsert != 0 && primaryKeyId != -1 ) {
		m_pSpecialHandlingInsert->HandleRowAfterInsert(primaryKeyId, row);
	}
}

namespace {
	template<class T>
	void addToValuesClause(const string& stName, const T& value, stringstream& ssValueClause, bool& bFirst) {
		if ( bFirst )
			bFirst = false;
		else
			ssValueClause << ",";
		ssValueClause << stName << "=" << value;
	}
}

void TableHandler::UpdateRow(InterchangeDataRowStruct& row) {

	m_pSpecialHandlingUpdate->HandleRow(row);

	stringstream ssValuesClause;

	                                          
	bool bFirst = true;
	vector<string> vUpdateKeys;
	vector<string> vUpdateValues;
	int iCol = 0;
	long primaryKeyId = -1;
	string stPrimaryGUID;
	vFieldsT::iterator itFields = row.m_vFields.begin();
	for(; itFields != row.m_vFields.end(); ++itFields, ++iCol ) {
		const ClientField& columnValue = *itFields;
		if ( columnValue.skip() )
			continue;
		if( m_vIsColString[iCol] ) {
			                          
			if ( columnValue == 0 )
				continue;

			string stTemp;
			SqlEscape(columnValue.str(), stTemp);
			addToValuesClause<string>(m_vsColNames[iCol], "'" + stTemp + "'", ssValuesClause, bFirst);
		} else {
			                      
			                                                                
			const KeyIndex* pKeyIndex = GetKeyIndex(iCol);
			if ( pKeyIndex != 0 ) {
				const bool bIsPrimary = pKeyIndex->m_bIsPrimary;
				const int iKeyEntityType = pKeyIndex->m_iEntityTypeId;

				                    
				if ( bIsPrimary ) {
					if ( columnValue == 0 || columnValue.str().empty() )
						throw CrException("No value was given for primary key " + m_vsColNames[iCol] + " for table " + m_stTableName);
				}
				else
				{
					                                                                 
					if ( columnValue == 0 || columnValue.str().empty() ) {
						addToValuesClause<string>(m_vsColNames[iCol], "null", ssValuesClause, bFirst);
						continue;
					}
				}

				                            
				const string stGuid(columnValue.str());
				long lValue = m_guidToIdMapper.GetId(stGuid, iKeyEntityType);
				if ( lValue == GuidToIdMapper::invalid_id ) {
					this->AddWarning(m_iEntityTypeId, stPrimaryGUID, "Failed to look up the database id for item with GUID=" + stGuid +
						" of type " + GetEntityNameByEntityId(iKeyEntityType) +
						". A possible cause for this error is that some dependent object has not been"\
						" deployed to this server. Try to deploy everything in your project."\
						" If that does not work it is probably a bug in the server or the client.");
					addToValuesClause<string>(m_vsColNames[iCol], "null", ssValuesClause, bFirst);
					continue;
				}
				(*itFields) = toString(lValue);

				if ( bIsPrimary ) {
					stPrimaryGUID = stGuid;
					if ( vUpdateKeys.empty() )
						primaryKeyId = lValue;
					else if ( vUpdateKeys.size() > 1 )
						primaryKeyId = -1;
					vUpdateKeys.push_back(m_vsColNames[iCol]);
					vUpdateValues.push_back(toString(lValue));
					if ( !m_userAccess.HasWritePermission(lValue, m_iEntityTypeId) ) {
						AddWarning(m_iEntityTypeId, stGuid, "Access Denied: The user does not have write permission for this item.");
						return;
					}

					                                                                              
					m_interchangeProject.AddItem(m_iEntityTypeId, primaryKeyId);

					                                                                       
					interchange_deploy::DeleteFolderItem(m_iEntityTypeId, primaryKeyId, m_interchangeProject, m_dbConnection);

					AddToDeployHistory(m_iEntityTypeId, primaryKeyId, m_userInfo, m_dbConnection, m_guidToIdMapper);
				}
				else {
					addToValuesClause<long>(m_vsColNames[iCol], lValue, ssValuesClause, bFirst);
				}
			}
			else {
				                                       
				                                                                              
				if ( columnValue == 0 || columnValue.str().empty() )
					addToValuesClause<string>(m_vsColNames[iCol], "null", ssValuesClause, bFirst);
				else
					addToValuesClause<string>(m_vsColNames[iCol], columnValue.str(), ssValuesClause, bFirst);
			}
		}
	}

	                              
	if ( HasModColumn() ) {
		addToValuesClause<string>(MODTIME_COLNAME, "now()", ssValuesClause, bFirst);
	}

	                                                          
	stringstream ssWhere;
	ssWhere << " WHERE ";
	vector<string>::const_iterator citUpdateKeys = vUpdateKeys.begin();
	vector<string>::const_iterator citUpdateKeyValues = vUpdateValues.begin();
	for(; citUpdateKeys != vUpdateKeys.end(); ++citUpdateKeys, ++citUpdateKeyValues ) {
		if( citUpdateKeys != vUpdateKeys.begin() )
			ssWhere << " AND ";
		ssWhere << *citUpdateKeys << "=" << *citUpdateKeyValues;
	}
	stringstream ssSql;
	ssSql << "UPDATE " << m_stTableName << " SET " << ssValuesClause.str() << ssWhere.str();
	UIBACKEND_TRACE("TableHandler::UpdateRow, Updating Main: " + m_stName);
	ExecuteSql(ssSql.str());
	++m_nChangedItems;

	                                                    
	const vector<DependentTableInfo>& vDependentTables = m_tranDbRelations.GetDependentTables(m_stTableName);
	vector<DependentTableInfo>::const_iterator citDepTables = vDependentTables.begin();
	for(; citDepTables != vDependentTables.end(); ++citDepTables ) {
		const DependentTableInfo& depTableInfo = *citDepTables;
		stringstream ssDeleteSql;
		ssDeleteSql << "DELETE FROM " << depTableInfo.GetTableName()
			        << ssWhere.str();
		UIBACKEND_TRACE("TableHandler::UpdateRow, Deleting Dependent...");
		ExecuteSql(ssDeleteSql.str());
	}

	if ( m_pSpecialHandlingUpdate != 0 && primaryKeyId != -1 ) {
		m_pSpecialHandlingUpdate->HandleRowAfterUpdate(primaryKeyId, row);
	}
}

bool TableHandler::HasModColumn() const {
	return m_bHasModTimeCol;
}

