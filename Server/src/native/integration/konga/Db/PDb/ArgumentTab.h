/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_ARGUMENTTAB_H
#define KONGA_DB_CRPDB_ARGUMENTTAB_H

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

class ArgumentTabRow
{
public:
	ArgumentTabRow() : 
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
			field_CallId(0),
			IsCallIdNull(true),
			IsCallIdPrimaryKey(true),
			IsCallIdDirty(false),
			field_ArgumentId(0),
			IsArgumentIdNull(true),
			IsArgumentIdPrimaryKey(true),
			IsArgumentIdDirty(false),
			IsDataTypeNull(true),
			IsDataTypePrimaryKey(false),
			IsDataTypeDirty(false),
			field_InputParam(0),
			IsInputParamNull(true),
			IsInputParamPrimaryKey(false),
			IsInputParamDirty(false),
			field_OutputParam(0),
			IsOutputParamNull(true),
			IsOutputParamPrimaryKey(false),
			IsOutputParamDirty(false),
			field_ReturnValue(0),
			IsReturnValueNull(true),
			IsReturnValuePrimaryKey(false),
			IsReturnValueDirty(false),
			field_OptionalParam(0),
			IsOptionalParamNull(true),
			IsOptionalParamPrimaryKey(false),
			IsOptionalParamDirty(false),
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

	void Copy(const ArgumentTabRow& r)
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

		field_CallId = r.field_CallId;
		IsCallIdNull = r.IsCallIdNull;
		IsCallIdDirty = r.IsCallIdDirty;

		field_ArgumentId = r.field_ArgumentId;
		IsArgumentIdNull = r.IsArgumentIdNull;
		IsArgumentIdDirty = r.IsArgumentIdDirty;

		field_DataType = r.field_DataType;
		IsDataTypeNull = r.IsDataTypeNull;
		IsDataTypeDirty = r.IsDataTypeDirty;

		field_InputParam = r.field_InputParam;
		IsInputParamNull = r.IsInputParamNull;
		IsInputParamDirty = r.IsInputParamDirty;

		field_OutputParam = r.field_OutputParam;
		IsOutputParamNull = r.IsOutputParamNull;
		IsOutputParamDirty = r.IsOutputParamDirty;

		field_ReturnValue = r.field_ReturnValue;
		IsReturnValueNull = r.IsReturnValueNull;
		IsReturnValueDirty = r.IsReturnValueDirty;

		field_OptionalParam = r.field_OptionalParam;
		IsOptionalParamNull = r.IsOptionalParamNull;
		IsOptionalParamDirty = r.IsOptionalParamDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ArgumentTabRow& r)
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

		field_CallId = r.field_CallId;
		IsCallIdNull = r.IsCallIdNull;
		IsCallIdDirty = r.IsCallIdDirty;

		field_ArgumentId = r.field_ArgumentId;
		IsArgumentIdNull = r.IsArgumentIdNull;
		IsArgumentIdDirty = r.IsArgumentIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsTaskIdDirty = b;
		IsCallIdDirty = b;
		IsArgumentIdDirty = b;
		IsDataTypeDirty = b;
		IsInputParamDirty = b;
		IsOutputParamDirty = b;
		IsReturnValueDirty = b;
		IsOptionalParamDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsTaskIdDirty ||
			IsCallIdDirty ||
			IsArgumentIdDirty ||
			IsDataTypeDirty ||
			IsInputParamDirty ||
			IsOutputParamDirty ||
			IsReturnValueDirty ||
			IsOptionalParamDirty ||
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

	int field_CallId;
	bool IsCallIdNull;
	bool IsCallIdDirty;

	int field_ArgumentId;
	bool IsArgumentIdNull;
	bool IsArgumentIdDirty;

	std::string field_DataType;
	bool IsDataTypeNull;
	bool IsDataTypeDirty;

	int field_InputParam;
	bool IsInputParamNull;
	bool IsInputParamDirty;

	int field_OutputParam;
	bool IsOutputParamNull;
	bool IsOutputParamDirty;

	int field_ReturnValue;
	bool IsReturnValueNull;
	bool IsReturnValueDirty;

	int field_OptionalParam;
	bool IsOptionalParamNull;
	bool IsOptionalParamDirty;

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
	bool IsCallIdPrimaryKey;
	bool IsArgumentIdPrimaryKey;
	bool IsDataTypePrimaryKey;
	bool IsInputParamPrimaryKey;
	bool IsOutputParamPrimaryKey;
	bool IsReturnValuePrimaryKey;
	bool IsOptionalParamPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ArgumentTab : public konga::DbRecordSet {
public:
	ArgumentTab() {}

	static ArgumentTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ArgumentTab";
	}

	void DoFieldExchange(const ArgumentTabRow& row) {
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

	int GetCallId() {
		if( m_row.IsCallIdNull )
			throw konga::DbNullException("CallId");
		return m_row.field_CallId;
	}
	void SetCallId(int i) {
		m_row.field_CallId = i;
		m_row.IsCallIdNull = false;
		m_row.IsCallIdDirty = true;
	}
	bool IsCallIdNull() const {
		return m_row.IsCallIdNull;
	}
	void SetCallIdToNull() {
		m_row.IsCallIdNull = true;
		m_row.IsCallIdDirty = true;
	}

	int GetArgumentId() {
		if( m_row.IsArgumentIdNull )
			throw konga::DbNullException("ArgumentId");
		return m_row.field_ArgumentId;
	}
	void SetArgumentId(int i) {
		m_row.field_ArgumentId = i;
		m_row.IsArgumentIdNull = false;
		m_row.IsArgumentIdDirty = true;
	}
	bool IsArgumentIdNull() const {
		return m_row.IsArgumentIdNull;
	}
	void SetArgumentIdToNull() {
		m_row.IsArgumentIdNull = true;
		m_row.IsArgumentIdDirty = true;
	}

	std::string GetDataType() {
		if( m_row.IsDataTypeNull )
			throw konga::DbNullException("DataType");
		return m_row.field_DataType;
	}
	void SetDataType(const std::string& st) {
		m_row.field_DataType = st;
		m_row.IsDataTypeNull = false;
		m_row.IsDataTypeDirty = true;
	}
	bool IsDataTypeNull() const {
		return m_row.IsDataTypeNull;
	}
	void SetDataTypeToNull() {
		m_row.field_DataType = "";
		m_row.IsDataTypeNull = true;
		m_row.IsDataTypeDirty = true;
	}

	int GetInputParam() {
		if( m_row.IsInputParamNull )
			throw konga::DbNullException("InputParam");
		return m_row.field_InputParam;
	}
	void SetInputParam(int i) {
		m_row.field_InputParam = i;
		m_row.IsInputParamNull = false;
		m_row.IsInputParamDirty = true;
	}
	bool IsInputParamNull() const {
		return m_row.IsInputParamNull;
	}
	void SetInputParamToNull() {
		m_row.IsInputParamNull = true;
		m_row.IsInputParamDirty = true;
	}

	int GetOutputParam() {
		if( m_row.IsOutputParamNull )
			throw konga::DbNullException("OutputParam");
		return m_row.field_OutputParam;
	}
	void SetOutputParam(int i) {
		m_row.field_OutputParam = i;
		m_row.IsOutputParamNull = false;
		m_row.IsOutputParamDirty = true;
	}
	bool IsOutputParamNull() const {
		return m_row.IsOutputParamNull;
	}
	void SetOutputParamToNull() {
		m_row.IsOutputParamNull = true;
		m_row.IsOutputParamDirty = true;
	}

	int GetReturnValue() {
		if( m_row.IsReturnValueNull )
			throw konga::DbNullException("ReturnValue");
		return m_row.field_ReturnValue;
	}
	void SetReturnValue(int i) {
		m_row.field_ReturnValue = i;
		m_row.IsReturnValueNull = false;
		m_row.IsReturnValueDirty = true;
	}
	bool IsReturnValueNull() const {
		return m_row.IsReturnValueNull;
	}
	void SetReturnValueToNull() {
		m_row.IsReturnValueNull = true;
		m_row.IsReturnValueDirty = true;
	}

	int GetOptionalParam() {
		if( m_row.IsOptionalParamNull )
			throw konga::DbNullException("OptionalParam");
		return m_row.field_OptionalParam;
	}
	void SetOptionalParam(int i) {
		m_row.field_OptionalParam = i;
		m_row.IsOptionalParamNull = false;
		m_row.IsOptionalParamDirty = true;
	}
	bool IsOptionalParamNull() const {
		return m_row.IsOptionalParamNull;
	}
	void SetOptionalParamToNull() {
		m_row.IsOptionalParamNull = true;
		m_row.IsOptionalParamDirty = true;
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
	ArgumentTabRow m_row;

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
	bool IsCallIdDirty() const {
		return m_row.IsCallIdDirty;
	};
	bool IsArgumentIdDirty() const {
		return m_row.IsArgumentIdDirty;
	};
	bool IsDataTypeDirty() const {
		return m_row.IsDataTypeDirty;
	};
	bool IsInputParamDirty() const {
		return m_row.IsInputParamDirty;
	};
	bool IsOutputParamDirty() const {
		return m_row.IsOutputParamDirty;
	};
	bool IsReturnValueDirty() const {
		return m_row.IsReturnValueDirty;
	};
	bool IsOptionalParamDirty() const {
		return m_row.IsOptionalParamDirty;
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
