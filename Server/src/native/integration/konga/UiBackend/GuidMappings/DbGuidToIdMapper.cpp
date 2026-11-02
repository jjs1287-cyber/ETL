/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include "DbGuidToIdMapper.h"

using namespace konga;
using std::string;
using std::stringstream;


DbGuidToIdMapper::DbGuidToIdMapper(void) : DbConnectionHolder(0)
{
	this->SetInternalDbName("TranDb");
}

DbGuidToIdMapper::DbGuidToIdMapper(DbConnection* pDbConnection) : DbConnectionHolder(pDbConnection)
{
	if(pDbConnection==0) {
		this->SetInternalDbName("TranDb");
	}
}

long DbGuidToIdMapper::GetId(const std::string& stGuid, const int iEntityType, const long lDefaultId) const {
	const string& stSelectSql = "SELECT Item_Id FROM " + GetGuidTableName(iEntityType)
		+ " WHERE Guid = '" + SqlEscape(stGuid) + "'";

	SmartDbPtr<DynamicDbRecordSet> pSelect( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), stSelectSql) );
	pSelect->Open();
	if ( pSelect->Next() )
		return atol(pSelect->GetFieldValue(0).c_str());

	return lDefaultId;
}

std::string DbGuidToIdMapper::GetGuid(const long lItemId, const int iEntityType, const std::string& stDefaultGuid) const {
	const string& stSelectSql = "SELECT Guid FROM " + GetGuidTableName(iEntityType)
		+ " WHERE Item_ID = " + toString(lItemId);

	SmartDbPtr<DynamicDbRecordSet> pSelect( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), stSelectSql) );
	pSelect->Open();
	if ( pSelect->Next() )
		return pSelect->GetFieldValue(0).c_str();

	return stDefaultGuid;
}

void DbGuidToIdMapper::SetGuidForItem(const long lItemId, const int iEntityType, const std::string& stGuid) {
	const string& stTableName = GetGuidTableName(iEntityType);
	                                        
	stringstream ssDelete;
	ssDelete << "DELETE FROM " << stTableName << " WHERE Item_ID = " << lItemId << " OR Guid = '" << SqlEscape(stGuid) << "'";
	ExecuteSql(ssDelete.str());

	                      
	stringstream ssInsert;
	ssInsert << "INSERT INTO " << stTableName << " (Guid, Item_ID) VALUES('" << SqlEscape(stGuid) << "', " << lItemId << ")";
	ExecuteSql(ssInsert.str());
}

void DbGuidToIdMapper::DeleteItem(const long lItemId, const int iEntityType) {
	ExecuteSql("DELETE FROM " + GetGuidTableName(iEntityType) + " WHERE Item_ID = " + toString(lItemId));
}

void DbGuidToIdMapper::DeleteItem(const std::string& stGuid, const int iEntityType) {
	ExecuteSql("DELETE FROM " + GetGuidTableName(iEntityType) + " WHERE Guid = '" + SqlEscape(stGuid) + "'");
}

