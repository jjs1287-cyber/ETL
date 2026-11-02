/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_DATAELEMENTTAB_H
#define KONGA_DB_CRIDB_DATAELEMENTTAB_H

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

class DataElementTabRow
{
public:
	DataElementTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(true),
			IsProcessInstanceIdDirty(false),
			field_TaskId(0),
			IsTaskIdNull(true),
			IsTaskIdPrimaryKey(true),
			IsTaskIdDirty(false),
			field_TaskInstanceId(0),
			IsTaskInstanceIdNull(true),
			IsTaskInstanceIdPrimaryKey(true),
			IsTaskInstanceIdDirty(false),
			field_DataElementId(0),
			IsDataElementIdNull(true),
			IsDataElementIdPrimaryKey(true),
			IsDataElementIdDirty(false),
			field_DataType(0),
			IsDataTypeNull(true),
			IsDataTypePrimaryKey(false),
			IsDataTypeDirty(false),
			IsDataElementNameNull(true),
			IsDataElementNamePrimaryKey(false),
			IsDataElementNameDirty(false),
			field_IntValue(0),
			IsIntValueNull(true),
			IsIntValuePrimaryKey(false),
			IsIntValueDirty(false),
			field_DoubleValue(0.),
			IsDoubleValueNull(true),
			IsDoubleValuePrimaryKey(false),
			IsDoubleValueDirty(false),
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

	void Copy(const DataElementTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_TaskInstanceId = r.field_TaskInstanceId;
		IsTaskInstanceIdNull = r.IsTaskInstanceIdNull;
		IsTaskInstanceIdDirty = r.IsTaskInstanceIdDirty;

		field_DataElementId = r.field_DataElementId;
		IsDataElementIdNull = r.IsDataElementIdNull;
		IsDataElementIdDirty = r.IsDataElementIdDirty;

		field_DataType = r.field_DataType;
		IsDataTypeNull = r.IsDataTypeNull;
		IsDataTypeDirty = r.IsDataTypeDirty;

		field_DataElementName = r.field_DataElementName;
		IsDataElementNameNull = r.IsDataElementNameNull;
		IsDataElementNameDirty = r.IsDataElementNameDirty;

		field_IntValue = r.field_IntValue;
		IsIntValueNull = r.IsIntValueNull;
		IsIntValueDirty = r.IsIntValueDirty;

		field_DoubleValue = r.field_DoubleValue;
		IsDoubleValueNull = r.IsDoubleValueNull;
		IsDoubleValueDirty = r.IsDoubleValueDirty;

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

	void CopyPKs(const DataElementTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_TaskInstanceId = r.field_TaskInstanceId;
		IsTaskInstanceIdNull = r.IsTaskInstanceIdNull;
		IsTaskInstanceIdDirty = r.IsTaskInstanceIdDirty;

		field_DataElementId = r.field_DataElementId;
		IsDataElementIdNull = r.IsDataElementIdNull;
		IsDataElementIdDirty = r.IsDataElementIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessInstanceIdDirty = b;
		IsTaskIdDirty = b;
		IsTaskInstanceIdDirty = b;
		IsDataElementIdDirty = b;
		IsDataTypeDirty = b;
		IsDataElementNameDirty = b;
		IsIntValueDirty = b;
		IsDoubleValueDirty = b;
		IsStringValueDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessInstanceIdDirty ||
			IsTaskIdDirty ||
			IsTaskInstanceIdDirty ||
			IsDataElementIdDirty ||
			IsDataTypeDirty ||
			IsDataElementNameDirty ||
			IsIntValueDirty ||
			IsDoubleValueDirty ||
			IsStringValueDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

	int field_TaskId;
	bool IsTaskIdNull;
	bool IsTaskIdDirty;

	int field_TaskInstanceId;
	bool IsTaskInstanceIdNull;
	bool IsTaskInstanceIdDirty;

	int field_DataElementId;
	bool IsDataElementIdNull;
	bool IsDataElementIdDirty;

	int field_DataType;
	bool IsDataTypeNull;
	bool IsDataTypeDirty;

	std::string field_DataElementName;
	bool IsDataElementNameNull;
	bool IsDataElementNameDirty;

	int field_IntValue;
	bool IsIntValueNull;
	bool IsIntValueDirty;

	double field_DoubleValue;
	bool IsDoubleValueNull;
	bool IsDoubleValueDirty;

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
	bool IsProcessIdPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsTaskIdPrimaryKey;
	bool IsTaskInstanceIdPrimaryKey;
	bool IsDataElementIdPrimaryKey;
	bool IsDataTypePrimaryKey;
	bool IsDataElementNamePrimaryKey;
	bool IsIntValuePrimaryKey;
	bool IsDoubleValuePrimaryKey;
	bool IsStringValuePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class DataElementTab : public konga::DbRecordSet {
public:
	DataElementTab() {}

	static DataElementTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "DataElementTab";
	}

	void DoFieldExchange(const DataElementTabRow& row) {
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

	int GetTaskInstanceId() {
		if( m_row.IsTaskInstanceIdNull )
			throw konga::DbNullException("TaskInstanceId");
		return m_row.field_TaskInstanceId;
	}
	void SetTaskInstanceId(int i) {
		m_row.field_TaskInstanceId = i;
		m_row.IsTaskInstanceIdNull = false;
		m_row.IsTaskInstanceIdDirty = true;
	}
	bool IsTaskInstanceIdNull() const {
		return m_row.IsTaskInstanceIdNull;
	}
	void SetTaskInstanceIdToNull() {
		m_row.IsTaskInstanceIdNull = true;
		m_row.IsTaskInstanceIdDirty = true;
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

	int GetDataType() {
		if( m_row.IsDataTypeNull )
			throw konga::DbNullException("DataType");
		return m_row.field_DataType;
	}
	void SetDataType(int i) {
		m_row.field_DataType = i;
		m_row.IsDataTypeNull = false;
		m_row.IsDataTypeDirty = true;
	}
	bool IsDataTypeNull() const {
		return m_row.IsDataTypeNull;
	}
	void SetDataTypeToNull() {
		m_row.IsDataTypeNull = true;
		m_row.IsDataTypeDirty = true;
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

	int GetIntValue() {
		if( m_row.IsIntValueNull )
			throw konga::DbNullException("IntValue");
		return m_row.field_IntValue;
	}
	void SetIntValue(int i) {
		m_row.field_IntValue = i;
		m_row.IsIntValueNull = false;
		m_row.IsIntValueDirty = true;
	}
	bool IsIntValueNull() const {
		return m_row.IsIntValueNull;
	}
	void SetIntValueToNull() {
		m_row.IsIntValueNull = true;
		m_row.IsIntValueDirty = true;
	}

	double GetDoubleValue() {
		if( m_row.IsDoubleValueNull )
			throw konga::DbNullException("DoubleValue");
		return m_row.field_DoubleValue;
	}
	void SetDoubleValue(double d) {
		m_row.field_DoubleValue = d;
		m_row.IsDoubleValueNull = false;
		m_row.IsDoubleValueDirty = true;
	}
	bool IsDoubleValueNull() const {
		return m_row.IsDoubleValueNull;
	}
	void SetDoubleValueToNull() {
		m_row.IsDoubleValueNull = true;
		m_row.IsDoubleValueDirty = true;
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
	DataElementTabRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessInstanceIdDirty() const {
		return m_row.IsProcessInstanceIdDirty;
	};
	bool IsTaskIdDirty() const {
		return m_row.IsTaskIdDirty;
	};
	bool IsTaskInstanceIdDirty() const {
		return m_row.IsTaskInstanceIdDirty;
	};
	bool IsDataElementIdDirty() const {
		return m_row.IsDataElementIdDirty;
	};
	bool IsDataTypeDirty() const {
		return m_row.IsDataTypeDirty;
	};
	bool IsDataElementNameDirty() const {
		return m_row.IsDataElementNameDirty;
	};
	bool IsIntValueDirty() const {
		return m_row.IsIntValueDirty;
	};
	bool IsDoubleValueDirty() const {
		return m_row.IsDoubleValueDirty;
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
