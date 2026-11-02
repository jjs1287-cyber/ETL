/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_DOCUMENTSTAB_H
#define KONGA_DB_CRTRANDB_DOCUMENTSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class DocumentsTabRow
{
public:
	DocumentsTabRow() : 
			field_Document_ID(0),
			IsDocument_IDNull(true),
			IsDocument_IDPrimaryKey(true),
			IsDocument_IDDirty(false),
			IsDocument_NameNull(true),
			IsDocument_NamePrimaryKey(false),
			IsDocument_NameDirty(false),
			field_Format_Type_ID(0),
			IsFormat_Type_IDNull(true),
			IsFormat_Type_IDPrimaryKey(false),
			IsFormat_Type_IDDirty(false),
			IsDelimiterNull(true),
			IsDelimiterPrimaryKey(false),
			IsDelimiterDirty(false),
			IsText_QualIFierNull(true),
			IsText_QualIFierPrimaryKey(false),
			IsText_QualIFierDirty(false),
			IsNumeric_QualIFierNull(true),
			IsNumeric_QualIFierPrimaryKey(false),
			IsNumeric_QualIFierDirty(false),
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

	void Copy(const DocumentsTabRow& r)
	{
		field_Document_ID = r.field_Document_ID;
		IsDocument_IDNull = r.IsDocument_IDNull;
		IsDocument_IDDirty = r.IsDocument_IDDirty;

		field_Document_Name = r.field_Document_Name;
		IsDocument_NameNull = r.IsDocument_NameNull;
		IsDocument_NameDirty = r.IsDocument_NameDirty;

		field_Format_Type_ID = r.field_Format_Type_ID;
		IsFormat_Type_IDNull = r.IsFormat_Type_IDNull;
		IsFormat_Type_IDDirty = r.IsFormat_Type_IDDirty;

		field_Delimiter = r.field_Delimiter;
		IsDelimiterNull = r.IsDelimiterNull;
		IsDelimiterDirty = r.IsDelimiterDirty;

		field_Text_QualIFier = r.field_Text_QualIFier;
		IsText_QualIFierNull = r.IsText_QualIFierNull;
		IsText_QualIFierDirty = r.IsText_QualIFierDirty;

		field_Numeric_QualIFier = r.field_Numeric_QualIFier;
		IsNumeric_QualIFierNull = r.IsNumeric_QualIFierNull;
		IsNumeric_QualIFierDirty = r.IsNumeric_QualIFierDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const DocumentsTabRow& r)
	{
		field_Document_ID = r.field_Document_ID;
		IsDocument_IDNull = r.IsDocument_IDNull;
		IsDocument_IDDirty = r.IsDocument_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsDocument_IDDirty = b;
		IsDocument_NameDirty = b;
		IsFormat_Type_IDDirty = b;
		IsDelimiterDirty = b;
		IsText_QualIFierDirty = b;
		IsNumeric_QualIFierDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsDocument_IDDirty ||
			IsDocument_NameDirty ||
			IsFormat_Type_IDDirty ||
			IsDelimiterDirty ||
			IsText_QualIFierDirty ||
			IsNumeric_QualIFierDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Document_ID;
	bool IsDocument_IDNull;
	bool IsDocument_IDDirty;

	std::string field_Document_Name;
	bool IsDocument_NameNull;
	bool IsDocument_NameDirty;

	int field_Format_Type_ID;
	bool IsFormat_Type_IDNull;
	bool IsFormat_Type_IDDirty;

	std::string field_Delimiter;
	bool IsDelimiterNull;
	bool IsDelimiterDirty;

	std::string field_Text_QualIFier;
	bool IsText_QualIFierNull;
	bool IsText_QualIFierDirty;

	std::string field_Numeric_QualIFier;
	bool IsNumeric_QualIFierNull;
	bool IsNumeric_QualIFierDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsDocument_IDPrimaryKey;
	bool IsDocument_NamePrimaryKey;
	bool IsFormat_Type_IDPrimaryKey;
	bool IsDelimiterPrimaryKey;
	bool IsText_QualIFierPrimaryKey;
	bool IsNumeric_QualIFierPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class DocumentsTab : public konga::DbRecordSet {
public:
	DocumentsTab() {}

	static DocumentsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "DocumentsTab";
	}

	void DoFieldExchange(const DocumentsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetDocument_ID() {
		if( m_row.IsDocument_IDNull )
			throw konga::DbNullException("Document_ID");
		return m_row.field_Document_ID;
	}
	void SetDocument_ID(int i) {
		m_row.field_Document_ID = i;
		m_row.IsDocument_IDNull = false;
		m_row.IsDocument_IDDirty = true;
	}
	bool IsDocument_IDNull() const {
		return m_row.IsDocument_IDNull;
	}
	void SetDocument_IDToNull() {
		m_row.IsDocument_IDNull = true;
		m_row.IsDocument_IDDirty = true;
	}

	std::string GetDocument_Name() {
		if( m_row.IsDocument_NameNull )
			throw konga::DbNullException("Document_Name");
		return m_row.field_Document_Name;
	}
	void SetDocument_Name(const std::string& st) {
		m_row.field_Document_Name = st;
		m_row.IsDocument_NameNull = false;
		m_row.IsDocument_NameDirty = true;
	}
	bool IsDocument_NameNull() const {
		return m_row.IsDocument_NameNull;
	}
	void SetDocument_NameToNull() {
		m_row.field_Document_Name = "";
		m_row.IsDocument_NameNull = true;
		m_row.IsDocument_NameDirty = true;
	}

	int GetFormat_Type_ID() {
		if( m_row.IsFormat_Type_IDNull )
			throw konga::DbNullException("Format_Type_ID");
		return m_row.field_Format_Type_ID;
	}
	void SetFormat_Type_ID(int i) {
		m_row.field_Format_Type_ID = i;
		m_row.IsFormat_Type_IDNull = false;
		m_row.IsFormat_Type_IDDirty = true;
	}
	bool IsFormat_Type_IDNull() const {
		return m_row.IsFormat_Type_IDNull;
	}
	void SetFormat_Type_IDToNull() {
		m_row.IsFormat_Type_IDNull = true;
		m_row.IsFormat_Type_IDDirty = true;
	}

	std::string GetDelimiter() {
		if( m_row.IsDelimiterNull )
			throw konga::DbNullException("Delimiter");
		return m_row.field_Delimiter;
	}
	void SetDelimiter(const std::string& st) {
		m_row.field_Delimiter = st;
		m_row.IsDelimiterNull = false;
		m_row.IsDelimiterDirty = true;
	}
	bool IsDelimiterNull() const {
		return m_row.IsDelimiterNull;
	}
	void SetDelimiterToNull() {
		m_row.field_Delimiter = "";
		m_row.IsDelimiterNull = true;
		m_row.IsDelimiterDirty = true;
	}

	std::string GetText_QualIFier() {
		if( m_row.IsText_QualIFierNull )
			throw konga::DbNullException("Text_QualIFier");
		return m_row.field_Text_QualIFier;
	}
	void SetText_QualIFier(const std::string& st) {
		m_row.field_Text_QualIFier = st;
		m_row.IsText_QualIFierNull = false;
		m_row.IsText_QualIFierDirty = true;
	}
	bool IsText_QualIFierNull() const {
		return m_row.IsText_QualIFierNull;
	}
	void SetText_QualIFierToNull() {
		m_row.field_Text_QualIFier = "";
		m_row.IsText_QualIFierNull = true;
		m_row.IsText_QualIFierDirty = true;
	}

	std::string GetNumeric_QualIFier() {
		if( m_row.IsNumeric_QualIFierNull )
			throw konga::DbNullException("Numeric_QualIFier");
		return m_row.field_Numeric_QualIFier;
	}
	void SetNumeric_QualIFier(const std::string& st) {
		m_row.field_Numeric_QualIFier = st;
		m_row.IsNumeric_QualIFierNull = false;
		m_row.IsNumeric_QualIFierDirty = true;
	}
	bool IsNumeric_QualIFierNull() const {
		return m_row.IsNumeric_QualIFierNull;
	}
	void SetNumeric_QualIFierToNull() {
		m_row.field_Numeric_QualIFier = "";
		m_row.IsNumeric_QualIFierNull = true;
		m_row.IsNumeric_QualIFierDirty = true;
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
	DocumentsTabRow m_row;

private:
	bool IsDocument_IDDirty() const {
		return m_row.IsDocument_IDDirty;
	};
	bool IsDocument_NameDirty() const {
		return m_row.IsDocument_NameDirty;
	};
	bool IsFormat_Type_IDDirty() const {
		return m_row.IsFormat_Type_IDDirty;
	};
	bool IsDelimiterDirty() const {
		return m_row.IsDelimiterDirty;
	};
	bool IsText_QualIFierDirty() const {
		return m_row.IsText_QualIFierDirty;
	};
	bool IsNumeric_QualIFierDirty() const {
		return m_row.IsNumeric_QualIFierDirty;
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
