/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_ROUTETAB_H
#define KONGA_DB_CRIDB_ROUTETAB_H

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

class RouteTabRow
{
public:
	RouteTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(true),
			IsProcessInstanceIdDirty(false),
			field_DestinationTaskId(0),
			IsDestinationTaskIdNull(true),
			IsDestinationTaskIdPrimaryKey(true),
			IsDestinationTaskIdDirty(false),
			field_DestinationTaskInstanceId(0),
			IsDestinationTaskInstanceIdNull(true),
			IsDestinationTaskInstanceIdPrimaryKey(true),
			IsDestinationTaskInstanceIdDirty(false),
			field_OriginProcessId(0),
			IsOriginProcessIdNull(true),
			IsOriginProcessIdPrimaryKey(true),
			IsOriginProcessIdDirty(false),
			field_OriginProcessInstanceId(0),
			IsOriginProcessInstanceIdNull(true),
			IsOriginProcessInstanceIdPrimaryKey(true),
			IsOriginProcessInstanceIdDirty(false),
			field_OriginTaskId(0),
			IsOriginTaskIdNull(true),
			IsOriginTaskIdPrimaryKey(true),
			IsOriginTaskIdDirty(false),
			field_OriginTaskInstanceId(0),
			IsOriginTaskInstanceIdNull(true),
			IsOriginTaskInstanceIdPrimaryKey(true),
			IsOriginTaskInstanceIdDirty(false),
			IsRouteUserNull(true),
			IsRouteUserPrimaryKey(false),
			IsRouteUserDirty(false),
			field_SentTime(0),
			IsSentTimeNull(true),
			IsSentTimePrimaryKey(false),
			IsSentTimeDirty(false),
			field_Status(0),
			IsStatusNull(true),
			IsStatusPrimaryKey(false),
			IsStatusDirty(false),
			field_RouteInstanceId(0),
			IsRouteInstanceIdNull(true),
			IsRouteInstanceIdPrimaryKey(false),
			IsRouteInstanceIdDirty(false),
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

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_DestinationTaskId = r.field_DestinationTaskId;
		IsDestinationTaskIdNull = r.IsDestinationTaskIdNull;
		IsDestinationTaskIdDirty = r.IsDestinationTaskIdDirty;

		field_DestinationTaskInstanceId = r.field_DestinationTaskInstanceId;
		IsDestinationTaskInstanceIdNull = r.IsDestinationTaskInstanceIdNull;
		IsDestinationTaskInstanceIdDirty = r.IsDestinationTaskInstanceIdDirty;

		field_OriginProcessId = r.field_OriginProcessId;
		IsOriginProcessIdNull = r.IsOriginProcessIdNull;
		IsOriginProcessIdDirty = r.IsOriginProcessIdDirty;

		field_OriginProcessInstanceId = r.field_OriginProcessInstanceId;
		IsOriginProcessInstanceIdNull = r.IsOriginProcessInstanceIdNull;
		IsOriginProcessInstanceIdDirty = r.IsOriginProcessInstanceIdDirty;

		field_OriginTaskId = r.field_OriginTaskId;
		IsOriginTaskIdNull = r.IsOriginTaskIdNull;
		IsOriginTaskIdDirty = r.IsOriginTaskIdDirty;

		field_OriginTaskInstanceId = r.field_OriginTaskInstanceId;
		IsOriginTaskInstanceIdNull = r.IsOriginTaskInstanceIdNull;
		IsOriginTaskInstanceIdDirty = r.IsOriginTaskInstanceIdDirty;

		field_RouteUser = r.field_RouteUser;
		IsRouteUserNull = r.IsRouteUserNull;
		IsRouteUserDirty = r.IsRouteUserDirty;

		field_SentTime = r.field_SentTime;
		IsSentTimeNull = r.IsSentTimeNull;
		IsSentTimeDirty = r.IsSentTimeDirty;

		field_Status = r.field_Status;
		IsStatusNull = r.IsStatusNull;
		IsStatusDirty = r.IsStatusDirty;

		field_RouteInstanceId = r.field_RouteInstanceId;
		IsRouteInstanceIdNull = r.IsRouteInstanceIdNull;
		IsRouteInstanceIdDirty = r.IsRouteInstanceIdDirty;

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

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_DestinationTaskId = r.field_DestinationTaskId;
		IsDestinationTaskIdNull = r.IsDestinationTaskIdNull;
		IsDestinationTaskIdDirty = r.IsDestinationTaskIdDirty;

		field_DestinationTaskInstanceId = r.field_DestinationTaskInstanceId;
		IsDestinationTaskInstanceIdNull = r.IsDestinationTaskInstanceIdNull;
		IsDestinationTaskInstanceIdDirty = r.IsDestinationTaskInstanceIdDirty;

		field_OriginProcessId = r.field_OriginProcessId;
		IsOriginProcessIdNull = r.IsOriginProcessIdNull;
		IsOriginProcessIdDirty = r.IsOriginProcessIdDirty;

		field_OriginProcessInstanceId = r.field_OriginProcessInstanceId;
		IsOriginProcessInstanceIdNull = r.IsOriginProcessInstanceIdNull;
		IsOriginProcessInstanceIdDirty = r.IsOriginProcessInstanceIdDirty;

		field_OriginTaskId = r.field_OriginTaskId;
		IsOriginTaskIdNull = r.IsOriginTaskIdNull;
		IsOriginTaskIdDirty = r.IsOriginTaskIdDirty;

		field_OriginTaskInstanceId = r.field_OriginTaskInstanceId;
		IsOriginTaskInstanceIdNull = r.IsOriginTaskInstanceIdNull;
		IsOriginTaskInstanceIdDirty = r.IsOriginTaskInstanceIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessInstanceIdDirty = b;
		IsDestinationTaskIdDirty = b;
		IsDestinationTaskInstanceIdDirty = b;
		IsOriginProcessIdDirty = b;
		IsOriginProcessInstanceIdDirty = b;
		IsOriginTaskIdDirty = b;
		IsOriginTaskInstanceIdDirty = b;
		IsRouteUserDirty = b;
		IsSentTimeDirty = b;
		IsStatusDirty = b;
		IsRouteInstanceIdDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessInstanceIdDirty ||
			IsDestinationTaskIdDirty ||
			IsDestinationTaskInstanceIdDirty ||
			IsOriginProcessIdDirty ||
			IsOriginProcessInstanceIdDirty ||
			IsOriginTaskIdDirty ||
			IsOriginTaskInstanceIdDirty ||
			IsRouteUserDirty ||
			IsSentTimeDirty ||
			IsStatusDirty ||
			IsRouteInstanceIdDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

	int field_DestinationTaskId;
	bool IsDestinationTaskIdNull;
	bool IsDestinationTaskIdDirty;

	int field_DestinationTaskInstanceId;
	bool IsDestinationTaskInstanceIdNull;
	bool IsDestinationTaskInstanceIdDirty;

	int field_OriginProcessId;
	bool IsOriginProcessIdNull;
	bool IsOriginProcessIdDirty;

	int field_OriginProcessInstanceId;
	bool IsOriginProcessInstanceIdNull;
	bool IsOriginProcessInstanceIdDirty;

	int field_OriginTaskId;
	bool IsOriginTaskIdNull;
	bool IsOriginTaskIdDirty;

	int field_OriginTaskInstanceId;
	bool IsOriginTaskInstanceIdNull;
	bool IsOriginTaskInstanceIdDirty;

	std::string field_RouteUser;
	bool IsRouteUserNull;
	bool IsRouteUserDirty;

	int field_SentTime;
	bool IsSentTimeNull;
	bool IsSentTimeDirty;

	int field_Status;
	bool IsStatusNull;
	bool IsStatusDirty;

	int field_RouteInstanceId;
	bool IsRouteInstanceIdNull;
	bool IsRouteInstanceIdDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsDestinationTaskIdPrimaryKey;
	bool IsDestinationTaskInstanceIdPrimaryKey;
	bool IsOriginProcessIdPrimaryKey;
	bool IsOriginProcessInstanceIdPrimaryKey;
	bool IsOriginTaskIdPrimaryKey;
	bool IsOriginTaskInstanceIdPrimaryKey;
	bool IsRouteUserPrimaryKey;
	bool IsSentTimePrimaryKey;
	bool IsStatusPrimaryKey;
	bool IsRouteInstanceIdPrimaryKey;
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

	int GetProcessInstanceId() {
		if( m_row.IsProcessInstanceIdNull )
			throw konga::DbNullException("ProcessInstanceId");
		return m_row.field_ProcessInstanceId;
	}
	void SetProcessInstanceId(int i) {
		m_row.field_ProcessInstanceId = i;
		m_row.IsProcessInstanceIdNull = false;
		m_row.IsProcessInstanceIdDirty = true;
	}
	bool IsProcessInstanceIdNull() const {
		return m_row.IsProcessInstanceIdNull;
	}
	void SetProcessInstanceIdToNull() {
		m_row.IsProcessInstanceIdNull = true;
		m_row.IsProcessInstanceIdDirty = true;
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

	int GetDestinationTaskInstanceId() {
		if( m_row.IsDestinationTaskInstanceIdNull )
			throw konga::DbNullException("DestinationTaskInstanceId");
		return m_row.field_DestinationTaskInstanceId;
	}
	void SetDestinationTaskInstanceId(int i) {
		m_row.field_DestinationTaskInstanceId = i;
		m_row.IsDestinationTaskInstanceIdNull = false;
		m_row.IsDestinationTaskInstanceIdDirty = true;
	}
	bool IsDestinationTaskInstanceIdNull() const {
		return m_row.IsDestinationTaskInstanceIdNull;
	}
	void SetDestinationTaskInstanceIdToNull() {
		m_row.IsDestinationTaskInstanceIdNull = true;
		m_row.IsDestinationTaskInstanceIdDirty = true;
	}

	int GetOriginProcessId() {
		if( m_row.IsOriginProcessIdNull )
			throw konga::DbNullException("OriginProcessId");
		return m_row.field_OriginProcessId;
	}
	void SetOriginProcessId(int i) {
		m_row.field_OriginProcessId = i;
		m_row.IsOriginProcessIdNull = false;
		m_row.IsOriginProcessIdDirty = true;
	}
	bool IsOriginProcessIdNull() const {
		return m_row.IsOriginProcessIdNull;
	}
	void SetOriginProcessIdToNull() {
		m_row.IsOriginProcessIdNull = true;
		m_row.IsOriginProcessIdDirty = true;
	}

	int GetOriginProcessInstanceId() {
		if( m_row.IsOriginProcessInstanceIdNull )
			throw konga::DbNullException("OriginProcessInstanceId");
		return m_row.field_OriginProcessInstanceId;
	}
	void SetOriginProcessInstanceId(int i) {
		m_row.field_OriginProcessInstanceId = i;
		m_row.IsOriginProcessInstanceIdNull = false;
		m_row.IsOriginProcessInstanceIdDirty = true;
	}
	bool IsOriginProcessInstanceIdNull() const {
		return m_row.IsOriginProcessInstanceIdNull;
	}
	void SetOriginProcessInstanceIdToNull() {
		m_row.IsOriginProcessInstanceIdNull = true;
		m_row.IsOriginProcessInstanceIdDirty = true;
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

	int GetOriginTaskInstanceId() {
		if( m_row.IsOriginTaskInstanceIdNull )
			throw konga::DbNullException("OriginTaskInstanceId");
		return m_row.field_OriginTaskInstanceId;
	}
	void SetOriginTaskInstanceId(int i) {
		m_row.field_OriginTaskInstanceId = i;
		m_row.IsOriginTaskInstanceIdNull = false;
		m_row.IsOriginTaskInstanceIdDirty = true;
	}
	bool IsOriginTaskInstanceIdNull() const {
		return m_row.IsOriginTaskInstanceIdNull;
	}
	void SetOriginTaskInstanceIdToNull() {
		m_row.IsOriginTaskInstanceIdNull = true;
		m_row.IsOriginTaskInstanceIdDirty = true;
	}

	std::string GetRouteUser() {
		if( m_row.IsRouteUserNull )
			throw konga::DbNullException("RouteUser");
		return m_row.field_RouteUser;
	}
	void SetRouteUser(const std::string& st) {
		m_row.field_RouteUser = st;
		m_row.IsRouteUserNull = false;
		m_row.IsRouteUserDirty = true;
	}
	bool IsRouteUserNull() const {
		return m_row.IsRouteUserNull;
	}
	void SetRouteUserToNull() {
		m_row.field_RouteUser = "";
		m_row.IsRouteUserNull = true;
		m_row.IsRouteUserDirty = true;
	}

	int GetSentTime() {
		if( m_row.IsSentTimeNull )
			throw konga::DbNullException("SentTime");
		return m_row.field_SentTime;
	}
	void SetSentTime(int i) {
		m_row.field_SentTime = i;
		m_row.IsSentTimeNull = false;
		m_row.IsSentTimeDirty = true;
	}
	bool IsSentTimeNull() const {
		return m_row.IsSentTimeNull;
	}
	void SetSentTimeToNull() {
		m_row.IsSentTimeNull = true;
		m_row.IsSentTimeDirty = true;
	}

	int GetStatus() {
		if( m_row.IsStatusNull )
			throw konga::DbNullException("Status");
		return m_row.field_Status;
	}
	void SetStatus(int i) {
		m_row.field_Status = i;
		m_row.IsStatusNull = false;
		m_row.IsStatusDirty = true;
	}
	bool IsStatusNull() const {
		return m_row.IsStatusNull;
	}
	void SetStatusToNull() {
		m_row.IsStatusNull = true;
		m_row.IsStatusDirty = true;
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
	bool IsProcessInstanceIdDirty() const {
		return m_row.IsProcessInstanceIdDirty;
	};
	bool IsDestinationTaskIdDirty() const {
		return m_row.IsDestinationTaskIdDirty;
	};
	bool IsDestinationTaskInstanceIdDirty() const {
		return m_row.IsDestinationTaskInstanceIdDirty;
	};
	bool IsOriginProcessIdDirty() const {
		return m_row.IsOriginProcessIdDirty;
	};
	bool IsOriginProcessInstanceIdDirty() const {
		return m_row.IsOriginProcessInstanceIdDirty;
	};
	bool IsOriginTaskIdDirty() const {
		return m_row.IsOriginTaskIdDirty;
	};
	bool IsOriginTaskInstanceIdDirty() const {
		return m_row.IsOriginTaskInstanceIdDirty;
	};
	bool IsRouteUserDirty() const {
		return m_row.IsRouteUserDirty;
	};
	bool IsSentTimeDirty() const {
		return m_row.IsSentTimeDirty;
	};
	bool IsStatusDirty() const {
		return m_row.IsStatusDirty;
	};
	bool IsRouteInstanceIdDirty() const {
		return m_row.IsRouteInstanceIdDirty;
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
