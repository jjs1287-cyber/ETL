/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_ROUTEDATAELEMENTTEXTTAB_H
#define KONGA_DB_CRIDB_ROUTEDATAELEMENTTEXTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace IDb
{

class RouteDataElementTextTabRow
{
public:
	RouteDataElementTextTabRow() : 
			field_RouteInstanceId(0),
			IsRouteInstanceIdNull(true),
			IsRouteInstanceIdPrimaryKey(true),
			IsRouteInstanceIdDirty(false),
			field_DataElementId(0),
			IsDataElementIdNull(true),
			IsDataElementIdPrimaryKey(true),
			IsDataElementIdDirty(false),
			IsStringValueNull(true),
			IsStringValuePrimaryKey(false),
			IsStringValueDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false)
	{
		field_InsertTime.year = 0;
		field_InsertTime.month = 0;
		field_InsertTime.day = 0;
		field_InsertTime.hour = 0;
		field_InsertTime.minute = 0;
		field_InsertTime.second = 0;
		field_InsertTime.fraction = 0;
		field_ModTime.year = 0;
		field_ModTime.month = 0;
		field_ModTime.day = 0;
		field_ModTime.hour = 0;
		field_ModTime.minute = 0;
		field_ModTime.second = 0;
		field_ModTime.fraction = 0;
	}

	void Copy(const RouteDataElementTextTabRow& r)
	{
		field_RouteInstanceId = r.field_RouteInstanceId;
		IsRouteInstanceIdNull = r.IsRouteInstanceIdNull;
		IsRouteInstanceIdDirty = r.IsRouteInstanceIdDirty;

		field_DataElementId = r.field_DataElementId;
		IsDataElementIdNull = r.IsDataElementIdNull;
		IsDataElementIdDirty = r.IsDataElementIdDirty;

		field_StringValue = r.field_StringValue;
		IsStringValueNull = r.IsStringValueNull;
		IsStringValueDirty = r.IsStringValueDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const RouteDataElementTextTabRow& r)
	{
		field_RouteInstanceId = r.field_RouteInstanceId;
		IsRouteInstanceIdNull = r.IsRouteInstanceIdNull;
		IsRouteInstanceIdDirty = r.IsRouteInstanceIdDirty;

		field_DataElementId = r.field_DataElementId;
		IsDataElementIdNull = r.IsDataElementIdNull;
		IsDataElementIdDirty = r.IsDataElementIdDirty;

	}

	void SetDirty(bool b)
	{
		IsRouteInstanceIdDirty = b;
		IsDataElementIdDirty = b;
		IsStringValueDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsRouteInstanceIdDirty ||
			IsDataElementIdDirty ||
			IsStringValueDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_RouteInstanceId;
	bool IsRouteInstanceIdNull;
	bool IsRouteInstanceIdDirty;

	int field_DataElementId;
	bool IsDataElementIdNull;
	bool IsDataElementIdDirty;

	std::string field_StringValue;
	bool IsStringValueNull;
	bool IsStringValueDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsRouteInstanceIdPrimaryKey;
	bool IsDataElementIdPrimaryKey;
	bool IsStringValuePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class RouteDataElementTextTab : public konga::DbRecordSet {
public:
	RouteDataElementTextTab() {}

	static RouteDataElementTextTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "RouteDataElementTextTab";
	}

	void DoFieldExchange(const RouteDataElementTextTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetRouteInstanceId() {
		if( m_row.IsRouteInstanceIdNull )
			throw konga::DbNullException("RouteInstanceId");
		return m_row.field_RouteInstanceId;
	}
	void SetRouteInstanceId(int i) {
		m_row.field_RouteInstanceId = i;
		m_row.IsRouteInstanceIdNull = false;
		m_row.IsRouteInstanceIdDirty = true;
	}
	bool IsRouteInstanceIdNull() const {
		return m_row.IsRouteInstanceIdNull;
	}
	void SetRouteInstanceIdToNull() {
		m_row.IsRouteInstanceIdNull = true;
		m_row.IsRouteInstanceIdDirty = true;
	}

	int GetDataElementId() {
		if( m_row.IsDataElementIdNull )
			throw konga::DbNullException("DataElementId");
		return m_row.field_DataElementId;
	}
	void SetDataElementId(int i) {
		m_row.field_DataElementId = i;
		m_row.IsDataElementIdNull = false;
		m_row.IsDataElementIdDirty = true;
	}
	bool IsDataElementIdNull() const {
		return m_row.IsDataElementIdNull;
	}
	void SetDataElementIdToNull() {
		m_row.IsDataElementIdNull = true;
		m_row.IsDataElementIdDirty = true;
	}

	std::string GetStringValue() {
		if( m_row.IsStringValueNull )
			throw konga::DbNullException("StringValue");
		return m_row.field_StringValue;
	}
	void SetStringValue(const std::string& st) {
		m_row.field_StringValue = st;
		m_row.IsStringValueNull = false;
		m_row.IsStringValueDirty = true;
	}
	bool IsStringValueNull() const {
		return m_row.IsStringValueNull;
	}
	void SetStringValueToNull() {
		m_row.field_StringValue = "";
		m_row.IsStringValueNull = true;
		m_row.IsStringValueDirty = true;
	}

	TIMESTAMP_STRUCT GetInsertTime() {
		if( m_row.IsInsertTimeNull )
			throw konga::DbNullException("InsertTime");
		return m_row.field_InsertTime;
	}
	void SetInsertTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_InsertTime = ts;
		m_row.IsInsertTimeNull = false;
		m_row.IsInsertTimeDirty = true;
	}
	bool IsInsertTimeNull() const {
		return m_row.IsInsertTimeNull;
	}
	void SetInsertTimeToNull() {
		m_row.IsInsertTimeNull = true;
		m_row.IsInsertTimeDirty = true;
	}

	TIMESTAMP_STRUCT GetModTime() {
		if( m_row.IsModTimeNull )
			throw konga::DbNullException("ModTime");
		return m_row.field_ModTime;
	}
	void SetModTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_ModTime = ts;
		m_row.IsModTimeNull = false;
		m_row.IsModTimeDirty = true;
	}
	bool IsModTimeNull() const {
		return m_row.IsModTimeNull;
	}
	void SetModTimeToNull() {
		m_row.IsModTimeNull = true;
		m_row.IsModTimeDirty = true;
	}

protected:
	RouteDataElementTextTabRow m_row;

private:
	bool IsRouteInstanceIdDirty() const {
		return m_row.IsRouteInstanceIdDirty;
	};
	bool IsDataElementIdDirty() const {
		return m_row.IsDataElementIdDirty;
	};
	bool IsStringValueDirty() const {
		return m_row.IsStringValueDirty;
	};
	bool IsInsertTimeDirty() const {
		return m_row.IsInsertTimeDirty;
	};
	bool IsModTimeDirty() const {
		return m_row.IsModTimeDirty;
	};


	std::string m_stUpdateFilter;

};

}                      


#endif
