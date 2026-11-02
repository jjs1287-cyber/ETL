/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_OPERATIONDATAELEMENTTAB_H
#define KONGA_DB_CRIDB_OPERATIONDATAELEMENTTAB_H

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

class OperationDataElementTabRow
{
public:
	OperationDataElementTabRow() : 
			field_SessionId(0),
			IsSessionIdNull(true),
			IsSessionIdPrimaryKey(true),
			IsSessionIdDirty(false),
			IsDataElementNameNull(true),
			IsDataElementNamePrimaryKey(true),
			IsDataElementNameDirty(false),
			IsArrayIndexNull(true),
			IsArrayIndexPrimaryKey(true),
			IsArrayIndexDirty(false),
			IsStringValueNull(true),
			IsStringValuePrimaryKey(false),
			IsStringValueDirty(false)
	{
	}

	void Copy(const OperationDataElementTabRow& r)
	{
		field_SessionId = r.field_SessionId;
		IsSessionIdNull = r.IsSessionIdNull;
		IsSessionIdDirty = r.IsSessionIdDirty;

		field_DataElementName = r.field_DataElementName;
		IsDataElementNameNull = r.IsDataElementNameNull;
		IsDataElementNameDirty = r.IsDataElementNameDirty;

		field_ArrayIndex = r.field_ArrayIndex;
		IsArrayIndexNull = r.IsArrayIndexNull;
		IsArrayIndexDirty = r.IsArrayIndexDirty;

		field_StringValue = r.field_StringValue;
		IsStringValueNull = r.IsStringValueNull;
		IsStringValueDirty = r.IsStringValueDirty;
	}

	void CopyPKs(const OperationDataElementTabRow& r)
	{
		field_SessionId = r.field_SessionId;
		IsSessionIdNull = r.IsSessionIdNull;
		IsSessionIdDirty = r.IsSessionIdDirty;

		field_DataElementName = r.field_DataElementName;
		IsDataElementNameNull = r.IsDataElementNameNull;
		IsDataElementNameDirty = r.IsDataElementNameDirty;

		field_ArrayIndex = r.field_ArrayIndex;
		IsArrayIndexNull = r.IsArrayIndexNull;
		IsArrayIndexDirty = r.IsArrayIndexDirty;
	}

	void SetDirty(bool b)
	{
		IsSessionIdDirty = b;
		IsDataElementNameDirty = b;
		IsArrayIndexDirty = b;
		IsStringValueDirty = b;
	}

	bool IsDirty() const
	{
		return IsSessionIdDirty ||
			IsDataElementNameDirty ||
			IsArrayIndexDirty ||
			IsStringValueDirty;
	}

	int field_SessionId;
	bool IsSessionIdNull;
	bool IsSessionIdDirty;

	std::string field_DataElementName;
	bool IsDataElementNameNull;
	bool IsDataElementNameDirty;

	std::string field_ArrayIndex;
	bool IsArrayIndexNull;
	bool IsArrayIndexDirty;

	std::string field_StringValue;
	bool IsStringValueNull;
	bool IsStringValueDirty;

private:
	bool IsSessionIdPrimaryKey;
	bool IsDataElementNamePrimaryKey;
	bool IsArrayIndexPrimaryKey;
	bool IsStringValuePrimaryKey;
};


class OperationDataElementTab : public konga::DbRecordSet {
public:
	OperationDataElementTab() {}

	static OperationDataElementTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "OperationDataElementTab";
	}

	void DoFieldExchange(const OperationDataElementTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetSessionId() {
		if( m_row.IsSessionIdNull )
			throw konga::DbNullException("SessionId");
		return m_row.field_SessionId;
	}
	void SetSessionId(int i) {
		m_row.field_SessionId = i;
		m_row.IsSessionIdNull = false;
		m_row.IsSessionIdDirty = true;
	}
	bool IsSessionIdNull() const {
		return m_row.IsSessionIdNull;
	}
	void SetSessionIdToNull() {
		m_row.IsSessionIdNull = true;
		m_row.IsSessionIdDirty = true;
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
		m_row.IsDataElementNameNull = true;
		m_row.IsDataElementNameDirty = true;
	}

	std::string GetArrayIndex() {
		if( m_row.IsArrayIndexNull )
			throw konga::DbNullException("ArrayIndex");
		return m_row.field_ArrayIndex;
	}
	void SetArrayIndex(const std::string& st) {
		m_row.field_ArrayIndex = st;
		m_row.IsArrayIndexNull = false;
		m_row.IsArrayIndexDirty = true;
	}
	bool IsArrayIndexNull() const {
		return m_row.IsArrayIndexNull;
	}
	void SetArrayIndexToNull() {
		m_row.IsArrayIndexNull = true;
		m_row.IsArrayIndexDirty = true;
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

protected:
	OperationDataElementTabRow m_row;

private:
	bool IsSessionIdDirty() const {
		return m_row.IsSessionIdDirty;
	};
	bool IsDataElementNameDirty() const {
		return m_row.IsDataElementNameDirty;
	};
	bool IsArrayIndexDirty() const {
		return m_row.IsArrayIndexDirty;
	};
	bool IsStringValueDirty() const {
		return m_row.IsStringValueDirty;
	};

	std::string m_stUpdateFilter;
};

}                      


#endif                                            
