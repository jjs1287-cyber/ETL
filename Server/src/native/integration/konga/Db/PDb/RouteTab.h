/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_ROUTETAB_H
#define KONGA_DB_CRPDB_ROUTETAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace PDb
{

class RouteTabRow
{
public:
	RouteTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
			field_OriginTaskId(0),
			IsOriginTaskIdNull(true),
			IsOriginTaskIdPrimaryKey(true),
			IsOriginTaskIdDirty(false),
			field_DestinationTaskId(0),
			IsDestinationTaskIdNull(true),
			IsDestinationTaskIdPrimaryKey(true),
			IsDestinationTaskIdDirty(false),
			IsDataElementListNull(true),
			IsDataElementListPrimaryKey(false),
			IsDataElementListDirty(false),
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

	void Copy(const RouteTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_OriginTaskId = r.field_OriginTaskId;
		IsOriginTaskIdNull = r.IsOriginTaskIdNull;
		IsOriginTaskIdDirty = r.IsOriginTaskIdDirty;

		field_DestinationTaskId = r.field_DestinationTaskId;
		IsDestinationTaskIdNull = r.IsDestinationTaskIdNull;
		IsDestinationTaskIdDirty = r.IsDestinationTaskIdDirty;

		field_DataElementList = r.field_DataElementList;
		IsDataElementListNull = r.IsDataElementListNull;
		IsDataElementListDirty = r.IsDataElementListDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const RouteTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_OriginTaskId = r.field_OriginTaskId;
		IsOriginTaskIdNull = r.IsOriginTaskIdNull;
		IsOriginTaskIdDirty = r.IsOriginTaskIdDirty;

		field_DestinationTaskId = r.field_DestinationTaskId;
		IsDestinationTaskIdNull = r.IsDestinationTaskIdNull;
		IsDestinationTaskIdDirty = r.IsDestinationTaskIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsOriginTaskIdDirty = b;
		IsDestinationTaskIdDirty = b;
		IsDataElementListDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsOriginTaskIdDirty ||
			IsDestinationTaskIdDirty ||
			IsDataElementListDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_OriginTaskId;
	bool IsOriginTaskIdNull;
	bool IsOriginTaskIdDirty;

	int field_DestinationTaskId;
	bool IsDestinationTaskIdNull;
	bool IsDestinationTaskIdDirty;

	std::string field_DataElementList;
	bool IsDataElementListNull;
	bool IsDataElementListDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsOriginTaskIdPrimaryKey;
	bool IsDestinationTaskIdPrimaryKey;
	bool IsDataElementListPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class RouteTab : public konga::DbRecordSet {
public:
	RouteTab() {}

	static RouteTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "RouteTab";
	}

	void DoFieldExchange(const RouteTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetProcessId() {
		if( m_row.IsProcessIdNull )
			throw konga::DbNullException("ProcessId");
		return m_row.field_ProcessId;
	}
	void SetProcessId(int i) {
		m_row.field_ProcessId = i;
		m_row.IsProcessIdNull = false;
		m_row.IsProcessIdDirty = true;
	}
	bool IsProcessIdNull() const {
		return m_row.IsProcessIdNull;
	}
	void SetProcessIdToNull() {
		m_row.IsProcessIdNull = true;
		m_row.IsProcessIdDirty = true;
	}

	int GetProcessVersion() {
		if( m_row.IsProcessVersionNull )
			throw konga::DbNullException("ProcessVersion");
		return m_row.field_ProcessVersion;
	}
	void SetProcessVersion(int i) {
		m_row.field_ProcessVersion = i;
		m_row.IsProcessVersionNull = false;
		m_row.IsProcessVersionDirty = true;
	}
	bool IsProcessVersionNull() const {
		return m_row.IsProcessVersionNull;
	}
	void SetProcessVersionToNull() {
		m_row.IsProcessVersionNull = true;
		m_row.IsProcessVersionDirty = true;
	}

	int GetOriginTaskId() {
		if( m_row.IsOriginTaskIdNull )
			throw konga::DbNullException("OriginTaskId");
		return m_row.field_OriginTaskId;
	}
	void SetOriginTaskId(int i) {
		m_row.field_OriginTaskId = i;
		m_row.IsOriginTaskIdNull = false;
		m_row.IsOriginTaskIdDirty = true;
	}
	bool IsOriginTaskIdNull() const {
		return m_row.IsOriginTaskIdNull;
	}
	void SetOriginTaskIdToNull() {
		m_row.IsOriginTaskIdNull = true;
		m_row.IsOriginTaskIdDirty = true;
	}

	int GetDestinationTaskId() {
		if( m_row.IsDestinationTaskIdNull )
			throw konga::DbNullException("DestinationTaskId");
		return m_row.field_DestinationTaskId;
	}
	void SetDestinationTaskId(int i) {
		m_row.field_DestinationTaskId = i;
		m_row.IsDestinationTaskIdNull = false;
		m_row.IsDestinationTaskIdDirty = true;
	}
	bool IsDestinationTaskIdNull() const {
		return m_row.IsDestinationTaskIdNull;
	}
	void SetDestinationTaskIdToNull() {
		m_row.IsDestinationTaskIdNull = true;
		m_row.IsDestinationTaskIdDirty = true;
	}

	std::string GetDataElementList() {
		if( m_row.IsDataElementListNull )
			throw konga::DbNullException("DataElementList");
		return m_row.field_DataElementList;
	}
	void SetDataElementList(const std::string& st) {
		m_row.field_DataElementList = st;
		m_row.IsDataElementListNull = false;
		m_row.IsDataElementListDirty = true;
	}
	bool IsDataElementListNull() const {
		return m_row.IsDataElementListNull;
	}
	void SetDataElementListToNull() {
		m_row.field_DataElementList = "";
		m_row.IsDataElementListNull = true;
		m_row.IsDataElementListDirty = true;
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
	RouteTabRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsOriginTaskIdDirty() const {
		return m_row.IsOriginTaskIdDirty;
	};
	bool IsDestinationTaskIdDirty() const {
		return m_row.IsDestinationTaskIdDirty;
	};
	bool IsDataElementListDirty() const {
		return m_row.IsDataElementListDirty;
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
