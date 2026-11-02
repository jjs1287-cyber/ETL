/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_TASKDISPLAYATTRIBUTES_H
#define KONGA_DB_CRPDB_TASKDISPLAYATTRIBUTES_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace PDb
{

class TaskDisplayAttributesRow
{
public:
	TaskDisplayAttributesRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
			field_TaskId(0),
			IsTaskIdNull(true),
			IsTaskIdPrimaryKey(true),
			IsTaskIdDirty(false),
			field_TopX(0),
			IsTopXNull(true),
			IsTopXPrimaryKey(false),
			IsTopXDirty(false),
			field_TopY(0),
			IsTopYNull(true),
			IsTopYPrimaryKey(false),
			IsTopYDirty(false),
			field_BottomX(0),
			IsBottomXNull(true),
			IsBottomXPrimaryKey(false),
			IsBottomXDirty(false),
			field_BottomY(0),
			IsBottomYNull(true),
			IsBottomYPrimaryKey(false),
			IsBottomYDirty(false),
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

	void Copy(const TaskDisplayAttributesRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_TopX = r.field_TopX;
		IsTopXNull = r.IsTopXNull;
		IsTopXDirty = r.IsTopXDirty;

		field_TopY = r.field_TopY;
		IsTopYNull = r.IsTopYNull;
		IsTopYDirty = r.IsTopYDirty;

		field_BottomX = r.field_BottomX;
		IsBottomXNull = r.IsBottomXNull;
		IsBottomXDirty = r.IsBottomXDirty;

		field_BottomY = r.field_BottomY;
		IsBottomYNull = r.IsBottomYNull;
		IsBottomYDirty = r.IsBottomYDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const TaskDisplayAttributesRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsTaskIdDirty = b;
		IsTopXDirty = b;
		IsTopYDirty = b;
		IsBottomXDirty = b;
		IsBottomYDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsTaskIdDirty ||
			IsTopXDirty ||
			IsTopYDirty ||
			IsBottomXDirty ||
			IsBottomYDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_TaskId;
	bool IsTaskIdNull;
	bool IsTaskIdDirty;

	int field_TopX;
	bool IsTopXNull;
	bool IsTopXDirty;

	int field_TopY;
	bool IsTopYNull;
	bool IsTopYDirty;

	int field_BottomX;
	bool IsBottomXNull;
	bool IsBottomXDirty;

	int field_BottomY;
	bool IsBottomYNull;
	bool IsBottomYDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsTaskIdPrimaryKey;
	bool IsTopXPrimaryKey;
	bool IsTopYPrimaryKey;
	bool IsBottomXPrimaryKey;
	bool IsBottomYPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class TaskDisplayAttributes : public konga::DbRecordSet {
public:
	TaskDisplayAttributes() {}

	static TaskDisplayAttributes* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TaskDisplayAttributes";
	}

	void DoFieldExchange(const TaskDisplayAttributesRow& row) {
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

	int GetTaskId() {
		if( m_row.IsTaskIdNull )
			throw konga::DbNullException("TaskId");
		return m_row.field_TaskId;
	}
	void SetTaskId(int i) {
		m_row.field_TaskId = i;
		m_row.IsTaskIdNull = false;
		m_row.IsTaskIdDirty = true;
	}
	bool IsTaskIdNull() const {
		return m_row.IsTaskIdNull;
	}
	void SetTaskIdToNull() {
		m_row.IsTaskIdNull = true;
		m_row.IsTaskIdDirty = true;
	}

	int GetTopX() {
		if( m_row.IsTopXNull )
			throw konga::DbNullException("TopX");
		return m_row.field_TopX;
	}
	void SetTopX(int i) {
		m_row.field_TopX = i;
		m_row.IsTopXNull = false;
		m_row.IsTopXDirty = true;
	}
	bool IsTopXNull() const {
		return m_row.IsTopXNull;
	}
	void SetTopXToNull() {
		m_row.IsTopXNull = true;
		m_row.IsTopXDirty = true;
	}

	int GetTopY() {
		if( m_row.IsTopYNull )
			throw konga::DbNullException("TopY");
		return m_row.field_TopY;
	}
	void SetTopY(int i) {
		m_row.field_TopY = i;
		m_row.IsTopYNull = false;
		m_row.IsTopYDirty = true;
	}
	bool IsTopYNull() const {
		return m_row.IsTopYNull;
	}
	void SetTopYToNull() {
		m_row.IsTopYNull = true;
		m_row.IsTopYDirty = true;
	}

	int GetBottomX() {
		if( m_row.IsBottomXNull )
			throw konga::DbNullException("BottomX");
		return m_row.field_BottomX;
	}
	void SetBottomX(int i) {
		m_row.field_BottomX = i;
		m_row.IsBottomXNull = false;
		m_row.IsBottomXDirty = true;
	}
	bool IsBottomXNull() const {
		return m_row.IsBottomXNull;
	}
	void SetBottomXToNull() {
		m_row.IsBottomXNull = true;
		m_row.IsBottomXDirty = true;
	}

	int GetBottomY() {
		if( m_row.IsBottomYNull )
			throw konga::DbNullException("BottomY");
		return m_row.field_BottomY;
	}
	void SetBottomY(int i) {
		m_row.field_BottomY = i;
		m_row.IsBottomYNull = false;
		m_row.IsBottomYDirty = true;
	}
	bool IsBottomYNull() const {
		return m_row.IsBottomYNull;
	}
	void SetBottomYToNull() {
		m_row.IsBottomYNull = true;
		m_row.IsBottomYDirty = true;
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
	TaskDisplayAttributesRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsTaskIdDirty() const {
		return m_row.IsTaskIdDirty;
	};
	bool IsTopXDirty() const {
		return m_row.IsTopXDirty;
	};
	bool IsTopYDirty() const {
		return m_row.IsTopYDirty;
	};
	bool IsBottomXDirty() const {
		return m_row.IsBottomXDirty;
	};
	bool IsBottomYDirty() const {
		return m_row.IsBottomYDirty;
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
