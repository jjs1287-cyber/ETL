/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_TASKDATAELEMENTSTAB_H
#define KONGA_DB_CRPDB_TASKDATAELEMENTSTAB_H

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

class TaskDataElementsTabRow
{
public:
	TaskDataElementsTabRow() : 
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
			field_DataElementId(0),
			IsDataElementIdNull(true),
			IsDataElementIdPrimaryKey(true),
			IsDataElementIdDirty(false),
			IsDataElementNameNull(true),
			IsDataElementNamePrimaryKey(false),
			IsDataElementNameDirty(false),
			IsDataElementDescriptionNull(true),
			IsDataElementDescriptionPrimaryKey(false),
			IsDataElementDescriptionDirty(false),
			IsDataElementTypeNull(true),
			IsDataElementTypePrimaryKey(false),
			IsDataElementTypeDirty(false),
			IsOriginTypeNull(true),
			IsOriginTypePrimaryKey(false),
			IsOriginTypeDirty(false),
			IsInitialValueNull(true),
			IsInitialValuePrimaryKey(false),
			IsInitialValueDirty(false),
			field_IsRequired(0),
			IsIsRequiredNull(true),
			IsIsRequiredPrimaryKey(false),
			IsIsRequiredDirty(false),
			IsDataElementDisplayNameNull(true),
			IsDataElementDisplayNamePrimaryKey(false),
			IsDataElementDisplayNameDirty(false),
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

	void Copy(const TaskDataElementsTabRow& r)
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

		field_DataElementId = r.field_DataElementId;
		IsDataElementIdNull = r.IsDataElementIdNull;
		IsDataElementIdDirty = r.IsDataElementIdDirty;

		field_DataElementName = r.field_DataElementName;
		IsDataElementNameNull = r.IsDataElementNameNull;
		IsDataElementNameDirty = r.IsDataElementNameDirty;

		field_DataElementDescription = r.field_DataElementDescription;
		IsDataElementDescriptionNull = r.IsDataElementDescriptionNull;
		IsDataElementDescriptionDirty = r.IsDataElementDescriptionDirty;

		field_DataElementType = r.field_DataElementType;
		IsDataElementTypeNull = r.IsDataElementTypeNull;
		IsDataElementTypeDirty = r.IsDataElementTypeDirty;

		field_OriginType = r.field_OriginType;
		IsOriginTypeNull = r.IsOriginTypeNull;
		IsOriginTypeDirty = r.IsOriginTypeDirty;

		field_InitialValue = r.field_InitialValue;
		IsInitialValueNull = r.IsInitialValueNull;
		IsInitialValueDirty = r.IsInitialValueDirty;

		field_IsRequired = r.field_IsRequired;
		IsIsRequiredNull = r.IsIsRequiredNull;
		IsIsRequiredDirty = r.IsIsRequiredDirty;

		field_DataElementDisplayName = r.field_DataElementDisplayName;
		IsDataElementDisplayNameNull = r.IsDataElementDisplayNameNull;
		IsDataElementDisplayNameDirty = r.IsDataElementDisplayNameDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const TaskDataElementsTabRow& r)
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

		field_DataElementId = r.field_DataElementId;
		IsDataElementIdNull = r.IsDataElementIdNull;
		IsDataElementIdDirty = r.IsDataElementIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsTaskIdDirty = b;
		IsDataElementIdDirty = b;
		IsDataElementNameDirty = b;
		IsDataElementDescriptionDirty = b;
		IsDataElementTypeDirty = b;
		IsOriginTypeDirty = b;
		IsInitialValueDirty = b;
		IsIsRequiredDirty = b;
		IsDataElementDisplayNameDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsTaskIdDirty ||
			IsDataElementIdDirty ||
			IsDataElementNameDirty ||
			IsDataElementDescriptionDirty ||
			IsDataElementTypeDirty ||
			IsOriginTypeDirty ||
			IsInitialValueDirty ||
			IsIsRequiredDirty ||
			IsDataElementDisplayNameDirty ||
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

	int field_DataElementId;
	bool IsDataElementIdNull;
	bool IsDataElementIdDirty;

	std::string field_DataElementName;
	bool IsDataElementNameNull;
	bool IsDataElementNameDirty;

	std::string field_DataElementDescription;
	bool IsDataElementDescriptionNull;
	bool IsDataElementDescriptionDirty;

	std::string field_DataElementType;
	bool IsDataElementTypeNull;
	bool IsDataElementTypeDirty;

	std::string field_OriginType;
	bool IsOriginTypeNull;
	bool IsOriginTypeDirty;

	std::string field_InitialValue;
	bool IsInitialValueNull;
	bool IsInitialValueDirty;

	int field_IsRequired;
	bool IsIsRequiredNull;
	bool IsIsRequiredDirty;

	std::string field_DataElementDisplayName;
	bool IsDataElementDisplayNameNull;
	bool IsDataElementDisplayNameDirty;

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
	bool IsDataElementIdPrimaryKey;
	bool IsDataElementNamePrimaryKey;
	bool IsDataElementDescriptionPrimaryKey;
	bool IsDataElementTypePrimaryKey;
	bool IsOriginTypePrimaryKey;
	bool IsInitialValuePrimaryKey;
	bool IsIsRequiredPrimaryKey;
	bool IsDataElementDisplayNamePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class TaskDataElementsTab : public konga::DbRecordSet {
public:
	TaskDataElementsTab() {}

	static TaskDataElementsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TaskDataElementsTab";
	}

	void DoFieldExchange(const TaskDataElementsTabRow& row) {
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

	std::string GetDataElementName() {
		if( m_row.IsDataElementNameNull )
			throw konga::DbNullException("DataElementName");
		return m_row.field_DataElementName;
	}
	void SetDataElementName(const std::string& st) {
		m_row.field_DataElementName = st;
		m_row.IsDataElementNameNull = false;
		m_row.IsDataElementNameDirty = true;
	}
	bool IsDataElementNameNull() const {
		return m_row.IsDataElementNameNull;
	}
	void SetDataElementNameToNull() {
		m_row.field_DataElementName = "";
		m_row.IsDataElementNameNull = true;
		m_row.IsDataElementNameDirty = true;
	}

	std::string GetDataElementDescription() {
		if( m_row.IsDataElementDescriptionNull )
			throw konga::DbNullException("DataElementDescription");
		return m_row.field_DataElementDescription;
	}
	void SetDataElementDescription(const std::string& st) {
		m_row.field_DataElementDescription = st;
		m_row.IsDataElementDescriptionNull = false;
		m_row.IsDataElementDescriptionDirty = true;
	}
	bool IsDataElementDescriptionNull() const {
		return m_row.IsDataElementDescriptionNull;
	}
	void SetDataElementDescriptionToNull() {
		m_row.field_DataElementDescription = "";
		m_row.IsDataElementDescriptionNull = true;
		m_row.IsDataElementDescriptionDirty = true;
	}

	std::string GetDataElementType() {
		if( m_row.IsDataElementTypeNull )
			throw konga::DbNullException("DataElementType");
		return m_row.field_DataElementType;
	}
	void SetDataElementType(const std::string& st) {
		m_row.field_DataElementType = st;
		m_row.IsDataElementTypeNull = false;
		m_row.IsDataElementTypeDirty = true;
	}
	bool IsDataElementTypeNull() const {
		return m_row.IsDataElementTypeNull;
	}
	void SetDataElementTypeToNull() {
		m_row.field_DataElementType = "";
		m_row.IsDataElementTypeNull = true;
		m_row.IsDataElementTypeDirty = true;
	}

	std::string GetOriginType() {
		if( m_row.IsOriginTypeNull )
			throw konga::DbNullException("OriginType");
		return m_row.field_OriginType;
	}
	void SetOriginType(const std::string& st) {
		m_row.field_OriginType = st;
		m_row.IsOriginTypeNull = false;
		m_row.IsOriginTypeDirty = true;
	}
	bool IsOriginTypeNull() const {
		return m_row.IsOriginTypeNull;
	}
	void SetOriginTypeToNull() {
		m_row.field_OriginType = "";
		m_row.IsOriginTypeNull = true;
		m_row.IsOriginTypeDirty = true;
	}

	std::string GetInitialValue() {
		if( m_row.IsInitialValueNull )
			throw konga::DbNullException("InitialValue");
		return m_row.field_InitialValue;
	}
	void SetInitialValue(const std::string& st) {
		m_row.field_InitialValue = st;
		m_row.IsInitialValueNull = false;
		m_row.IsInitialValueDirty = true;
	}
	bool IsInitialValueNull() const {
		return m_row.IsInitialValueNull;
	}
	void SetInitialValueToNull() {
		m_row.field_InitialValue = "";
		m_row.IsInitialValueNull = true;
		m_row.IsInitialValueDirty = true;
	}

	int GetIsRequired() {
		if( m_row.IsIsRequiredNull )
			throw konga::DbNullException("IsRequired");
		return m_row.field_IsRequired;
	}
	void SetIsRequired(int i) {
		m_row.field_IsRequired = i;
		m_row.IsIsRequiredNull = false;
		m_row.IsIsRequiredDirty = true;
	}
	bool IsIsRequiredNull() const {
		return m_row.IsIsRequiredNull;
	}
	void SetIsRequiredToNull() {
		m_row.IsIsRequiredNull = true;
		m_row.IsIsRequiredDirty = true;
	}

	std::string GetDataElementDisplayName() {
		if( m_row.IsDataElementDisplayNameNull )
			throw konga::DbNullException("DataElementDisplayName");
		return m_row.field_DataElementDisplayName;
	}
	void SetDataElementDisplayName(const std::string& st) {
		m_row.field_DataElementDisplayName = st;
		m_row.IsDataElementDisplayNameNull = false;
		m_row.IsDataElementDisplayNameDirty = true;
	}
	bool IsDataElementDisplayNameNull() const {
		return m_row.IsDataElementDisplayNameNull;
	}
	void SetDataElementDisplayNameToNull() {
		m_row.field_DataElementDisplayName = "";
		m_row.IsDataElementDisplayNameNull = true;
		m_row.IsDataElementDisplayNameDirty = true;
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
	TaskDataElementsTabRow m_row;

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
	bool IsDataElementIdDirty() const {
		return m_row.IsDataElementIdDirty;
	};
	bool IsDataElementNameDirty() const {
		return m_row.IsDataElementNameDirty;
	};
	bool IsDataElementDescriptionDirty() const {
		return m_row.IsDataElementDescriptionDirty;
	};
	bool IsDataElementTypeDirty() const {
		return m_row.IsDataElementTypeDirty;
	};
	bool IsOriginTypeDirty() const {
		return m_row.IsOriginTypeDirty;
	};
	bool IsInitialValueDirty() const {
		return m_row.IsInitialValueDirty;
	};
	bool IsIsRequiredDirty() const {
		return m_row.IsIsRequiredDirty;
	};
	bool IsDataElementDisplayNameDirty() const {
		return m_row.IsDataElementDisplayNameDirty;
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
