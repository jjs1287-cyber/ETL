/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_ERRORDATAELEMENTTAB_H
#define KONGA_DB_CRMSGDB_ERRORDATAELEMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace MsgDb
{

class ErrorDataElementTabRow
{
public:
	ErrorDataElementTabRow() : 
			field_MsgId(0),
			IsMsgIdNull(true),
			IsMsgIdPrimaryKey(false),
			IsMsgIdDirty(false),
			IsDataElementNameNull(true),
			IsDataElementNamePrimaryKey(false),
			IsDataElementNameDirty(false),
			IsDataTypeNull(true),
			IsDataTypePrimaryKey(false),
			IsDataTypeDirty(false),
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
			field_ErrorId(0),
			IsErrorIdNull(true),
			IsErrorIdPrimaryKey(true),
			IsErrorIdDirty(false),
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

	void Copy(const ErrorDataElementTabRow& r)
	{
		field_MsgId = r.field_MsgId;
		IsMsgIdNull = r.IsMsgIdNull;
		IsMsgIdDirty = r.IsMsgIdDirty;

		field_DataElementName = r.field_DataElementName;
		IsDataElementNameNull = r.IsDataElementNameNull;
		IsDataElementNameDirty = r.IsDataElementNameDirty;

		field_DataType = r.field_DataType;
		IsDataTypeNull = r.IsDataTypeNull;
		IsDataTypeDirty = r.IsDataTypeDirty;

		field_IntValue = r.field_IntValue;
		IsIntValueNull = r.IsIntValueNull;
		IsIntValueDirty = r.IsIntValueDirty;

		field_DoubleValue = r.field_DoubleValue;
		IsDoubleValueNull = r.IsDoubleValueNull;
		IsDoubleValueDirty = r.IsDoubleValueDirty;

		field_StringValue = r.field_StringValue;
		IsStringValueNull = r.IsStringValueNull;
		IsStringValueDirty = r.IsStringValueDirty;

		field_ErrorId = r.field_ErrorId;
		IsErrorIdNull = r.IsErrorIdNull;
		IsErrorIdDirty = r.IsErrorIdDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ErrorDataElementTabRow& r)
	{
		field_ErrorId = r.field_ErrorId;
		IsErrorIdNull = r.IsErrorIdNull;
		IsErrorIdDirty = r.IsErrorIdDirty;

	}

	void SetDirty(bool b)
	{
		IsMsgIdDirty = b;
		IsDataElementNameDirty = b;
		IsDataTypeDirty = b;
		IsIntValueDirty = b;
		IsDoubleValueDirty = b;
		IsStringValueDirty = b;
		IsErrorIdDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsMsgIdDirty ||
			IsDataElementNameDirty ||
			IsDataTypeDirty ||
			IsIntValueDirty ||
			IsDoubleValueDirty ||
			IsStringValueDirty ||
			IsErrorIdDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_MsgId;
	bool IsMsgIdNull;
	bool IsMsgIdDirty;

	std::string field_DataElementName;
	bool IsDataElementNameNull;
	bool IsDataElementNameDirty;

	std::string field_DataType;
	bool IsDataTypeNull;
	bool IsDataTypeDirty;

	int field_IntValue;
	bool IsIntValueNull;
	bool IsIntValueDirty;

	double field_DoubleValue;
	bool IsDoubleValueNull;
	bool IsDoubleValueDirty;

	std::string field_StringValue;
	bool IsStringValueNull;
	bool IsStringValueDirty;

	int field_ErrorId;
	bool IsErrorIdNull;
	bool IsErrorIdDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsMsgIdPrimaryKey;
	bool IsDataElementNamePrimaryKey;
	bool IsDataTypePrimaryKey;
	bool IsIntValuePrimaryKey;
	bool IsDoubleValuePrimaryKey;
	bool IsStringValuePrimaryKey;
	bool IsErrorIdPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ErrorDataElementTab : public konga::DbRecordSet {
public:
	ErrorDataElementTab() {}

	static ErrorDataElementTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ErrorDataElementTab";
	}

	void DoFieldExchange(const ErrorDataElementTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetMsgId() {
		if( m_row.IsMsgIdNull )
			throw konga::DbNullException("MsgId");
		return m_row.field_MsgId;
	}
	void SetMsgId(int i) {
		m_row.field_MsgId = i;
		m_row.IsMsgIdNull = false;
		m_row.IsMsgIdDirty = true;
	}
	bool IsMsgIdNull() const {
		return m_row.IsMsgIdNull;
	}
	void SetMsgIdToNull() {
		m_row.IsMsgIdNull = true;
		m_row.IsMsgIdDirty = true;
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

	int GetErrorId() {
		if( m_row.IsErrorIdNull )
			throw konga::DbNullException("ErrorId");
		return m_row.field_ErrorId;
	}
	void SetErrorId(int i) {
		m_row.field_ErrorId = i;
		m_row.IsErrorIdNull = false;
		m_row.IsErrorIdDirty = true;
	}
	bool IsErrorIdNull() const {
		return m_row.IsErrorIdNull;
	}
	void SetErrorIdToNull() {
		m_row.IsErrorIdNull = true;
		m_row.IsErrorIdDirty = true;
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
	ErrorDataElementTabRow m_row;

private:
	bool IsMsgIdDirty() const {
		return m_row.IsMsgIdDirty;
	};
	bool IsDataElementNameDirty() const {
		return m_row.IsDataElementNameDirty;
	};
	bool IsDataTypeDirty() const {
		return m_row.IsDataTypeDirty;
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
	bool IsErrorIdDirty() const {
		return m_row.IsErrorIdDirty;
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
