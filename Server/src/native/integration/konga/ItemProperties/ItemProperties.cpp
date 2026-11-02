/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string>
#include <cassert>

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/StringUtils.h>	                   

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/DbException.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include "ItemProperties.h"

using namespace konga;
using std::string;
using std::map;

namespace {
	DbConnection* GetTranDbConnection() {
		DBConnectionInfo dbInfo("TranDb");
		return DbConnection::MakeInstance(dbInfo.connectionString());
	}

	string GetItemWhereIncludingWhere(int iEntityTypeId, long lEntityId) {
		return string(" WHERE " + getIdName(GetEntityNameByEntityId(iEntityTypeId))
			+ " = " + toString(lEntityId));
	}

	string GetSelectSql(int iEntityTypeId) {
		const string& id_name = getIdName(GetEntityNameByEntityId(iEntityTypeId));
		return string("SELECT " + id_name + ", KeyName, KeyValue FROM "
			+ GetPropertyTableName(iEntityTypeId)
			+ " ORDER BY " + id_name
			);
	}

	string GetSelectSql(int iEntityTypeId, long lEntityId) {
		return string("SELECT KeyName, KeyValue FROM "
			+ GetPropertyTableName(iEntityTypeId)
			+ GetItemWhereIncludingWhere(iEntityTypeId, lEntityId)
			);
	}

	string GetSelectSql(int iEntityTypeId, long lEntityId, const string& keyName) {
		return string(GetSelectSql(iEntityTypeId, lEntityId)
			+ " AND LOWER(KeyName) = LOWER('" + SqlEscape(keyName) + "')"
			);
	}

	string GetDeleteSql(int iEntityTypeId, long lEntityId) {
		return string("DELETE FROM " + GetPropertyTableName(iEntityTypeId)
			+ GetItemWhereIncludingWhere(iEntityTypeId, lEntityId)
			);
	}

	string GetInsertSql(int iEntityTypeId, long lEntityId, const string& keyName, const string& keyValue) {
		return string("INSERT INTO " + GetPropertyTableName(iEntityTypeId)
			+ " (" + getIdName(GetEntityNameByEntityId(iEntityTypeId))
			+ ", KeyName, KeyValue) VALUES (" + toString(lEntityId)
			+ ", '" + SqlEscape(keyName) + "', '" + SqlEscape(keyValue) + "')"
			);
	}

	string GetUpdateSql(int iEntityTypeId, long lEntityId, const string& keyName, const string& keyValue) {
		return string("UPDATE " + GetPropertyTableName(iEntityTypeId) + " SET KeyValue = '" + SqlEscape(keyValue) + "'"
			+ GetItemWhereIncludingWhere(iEntityTypeId, lEntityId)
			+ " AND LOWER(KeyName) = LOWER('" + keyName + "')"
			);
	}
}

bool ItemProperties::hasItemProperties(int iEntityTypeId) {
	switch(iEntityTypeId) {
		case SourceEntityId:
		case TargetEntityId:
		case TransformationEntityId:
		case ScheduleEntityId:
		case OperationEntityId:
		case WebServiceEntityId:
		case DocumentEntityId:
		case SalesforceConnectorEntityId:
		case SalesforceQueryEntityId:
		case SalesforceUpsertEntityId:
		case SalesforceCreateEntityId:
		case SalesforceUpdateEntityId:
		case SalesforceDeleteEntityId:
		case SalesforceBulkLoadEntityId:
		case ScriptEntityId:
			return true;
		default:
			return false;
	};
}

ItemProperties::ItemProperties(DbConnection* pTranDbConnection) : DbConnectionHolder(pTranDbConnection) {
	this->SetInternalDbName("TranDb");
}

void ItemProperties::GetItemProperties(int iEntityTypeId, long lEntityId,
									   KeyValues& properties) const
{
	properties.Clear();
	try {
		const string& sql = GetSelectSql(iEntityTypeId, lEntityId);

		SmartDbPtr<DynamicDbRecordSet> pTab(
			DynamicDbRecordSet::MakeInstance(&GetDbConnection(), sql)
			);

		pTab->Open();
		while( pTab->Next() ) {
			const string& key = pTab->GetFieldValue(0);
			const string& value = pTab->GetFieldValue(1);
			properties.AddString(key, value);
		}
	}
	catch( const DbException& ex ) {
		throw InternalErrorException("Database error while accessing a property table:\n"
			+ ex.GetErrorString());
	}
	catch( const CrException& ex ) {
		throw InternalErrorException("Unexpected exception while looking up item properties."
			+ ex.GetErrorString());
	}
}

void ItemProperties::GetItemPropertiesForEntityType(int iEntityTypeId, map<long, KeyValues>& allProperties) {
	allProperties.clear();
	if ( !ItemProperties::hasItemProperties(iEntityTypeId) )
		return;

	try {
		const string& sql = GetSelectSql(iEntityTypeId);
		SmartDbPtr<DynamicDbRecordSet> pTab(
			DynamicDbRecordSet::MakeInstance(&GetDbConnection(), sql)
			);

		pTab->Open();
		while( pTab->Next() ) {
			const string& id_as_string = pTab->GetFieldValue(0);
			const long lItemId = atol(id_as_string.c_str());
			KeyValues* properties = 0;
			map<long, KeyValues>::iterator it = allProperties.find(lItemId);
			if ( it == allProperties.end() ) {
				allProperties.insert( map<long, KeyValues>::value_type(lItemId, KeyValues()) );
				properties = &allProperties.find(lItemId)->second;
			}
			else {
				properties = &it->second;
			}
			assert(properties);
			const string& key = pTab->GetFieldValue(1);
			const string& value = pTab->GetFieldValue(2);
			properties->AddString(key, value);
		}
	}
	catch( const DbException& ex ) {
		throw InternalErrorException("Database error while accessing a property table:\n"
			+ ex.GetErrorString());
	}
	catch( const CrException& ex ) {
		throw InternalErrorException("Unexpected exception while looking up item properties."
			+ ex.GetErrorString());
	}
}


bool ItemProperties::GetItemProperty(int iEntityTypeId, long lEntityId,
									 const string& keyName, string& keyValue) const
{
	const string& sql = GetSelectSql(iEntityTypeId, lEntityId, keyName);

	try {
		SmartDbPtr<DynamicDbRecordSet> pTab(
			DynamicDbRecordSet::MakeInstance(&GetDbConnection(), sql)
			);
		pTab->Open();
		if ( !pTab->Next() )
			return false;

		keyValue = pTab->GetFieldValue(1);
		return true;
	}
	catch( const DbException& ex ) {
		throw InternalErrorException("Database error while accessing a property table:\n"
			+ ex.GetErrorString());
	}
	catch( const CrException& ex ) {
		throw InternalErrorException("Unexpected exception while looking up item properties."
			+ ex.GetErrorString());
	}
}

void ItemProperties::Delete(int iEntityTypeId, long lEntityId) {
	const string& sql = GetDeleteSql(iEntityTypeId, lEntityId);
	this->GetDbConnection().ExecuteSql(sql);
}

void ItemProperties::Add(int iEntityTypeId, long lEntityId, const string& keyName, const string& keyValue)
{
	const string& sql = GetInsertSql(iEntityTypeId, lEntityId, keyName, keyValue);
	this->GetDbConnection().ExecuteSql(sql);
}

bool ItemProperties::Update(int iEntityTypeId, long lEntityId, const string& keyName, const string& keyValue) {

	{                                     
	SmartDbPtr<DynamicDbRecordSet> pTab( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), GetSelectSql(iEntityTypeId, lEntityId, keyName)) );
	pTab->Open();
	const unsigned int index = 1;
	if ( pTab->Next() ) {
		const string currentValue( pTab->IsFieldNull(index) ? "" : pTab->GetFieldValue(index) );
		if ( EqualsIgnoreCase(keyValue, currentValue) )
			return false;
	}
	else {
		Add(iEntityTypeId, lEntityId, keyName, keyValue);
		return true;
	}
	}   

	const string& sql = GetUpdateSql(iEntityTypeId, lEntityId, keyName, keyValue);
	this->GetDbConnection().ExecuteSql(sql);
	return true;
}

