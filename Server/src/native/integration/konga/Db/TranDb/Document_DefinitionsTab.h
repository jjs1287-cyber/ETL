/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_DOCUMENT_DEFINITIONSTAB_H
#define KONGA_DB_CRTRANDB_DOCUMENT_DEFINITIONSTAB_H

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

class Document_DefinitionsTabRow
{
public:
	Document_DefinitionsTabRow() : 
			field_Document_ID(0),
			IsDocument_IDNull(true),
			IsDocument_IDPrimaryKey(true),
			IsDocument_IDDirty(false),
			field_Field_ID(0),
			IsField_IDNull(true),
			IsField_IDPrimaryKey(true),
			IsField_IDDirty(false),
			field_Parent_ID(0),
			IsParent_IDNull(true),
			IsParent_IDPrimaryKey(true),
			IsParent_IDDirty(false),
			IsField_NameNull(true),
			IsField_NamePrimaryKey(false),
			IsField_NameDirty(false),
			field_Sequence(0),
			IsSequenceNull(true),
			IsSequencePrimaryKey(false),
			IsSequenceDirty(false),
			field_Var_Type_ID(0),
			IsVar_Type_IDNull(true),
			IsVar_Type_IDPrimaryKey(false),
			IsVar_Type_IDDirty(false),
			field_Start_Pos(0),
			IsStart_PosNull(true),
			IsStart_PosPrimaryKey(false),
			IsStart_PosDirty(false),
			field_End_Pos(0),
			IsEnd_PosNull(true),
			IsEnd_PosPrimaryKey(false),
			IsEnd_PosDirty(false),
			field_Required(0),
			IsRequiredNull(true),
			IsRequiredPrimaryKey(false),
			IsRequiredDirty(false),
			IsDefault_valueNull(true),
			IsDefault_valuePrimaryKey(false),
			IsDefault_valueDirty(false),
			IsFormatNull(true),
			IsFormatPrimaryKey(false),
			IsFormatDirty(false),
			field_Uni_ID(0),
			IsUni_IDNull(true),
			IsUni_IDPrimaryKey(false),
			IsUni_IDDirty(false),
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

	void Copy(const Document_DefinitionsTabRow& r)
	{
		field_Document_ID = r.field_Document_ID;
		IsDocument_IDNull = r.IsDocument_IDNull;
		IsDocument_IDDirty = r.IsDocument_IDDirty;

		field_Field_ID = r.field_Field_ID;
		IsField_IDNull = r.IsField_IDNull;
		IsField_IDDirty = r.IsField_IDDirty;

		field_Parent_ID = r.field_Parent_ID;
		IsParent_IDNull = r.IsParent_IDNull;
		IsParent_IDDirty = r.IsParent_IDDirty;

		field_Field_Name = r.field_Field_Name;
		IsField_NameNull = r.IsField_NameNull;
		IsField_NameDirty = r.IsField_NameDirty;

		field_Sequence = r.field_Sequence;
		IsSequenceNull = r.IsSequenceNull;
		IsSequenceDirty = r.IsSequenceDirty;

		field_Var_Type_ID = r.field_Var_Type_ID;
		IsVar_Type_IDNull = r.IsVar_Type_IDNull;
		IsVar_Type_IDDirty = r.IsVar_Type_IDDirty;

		field_Start_Pos = r.field_Start_Pos;
		IsStart_PosNull = r.IsStart_PosNull;
		IsStart_PosDirty = r.IsStart_PosDirty;

		field_End_Pos = r.field_End_Pos;
		IsEnd_PosNull = r.IsEnd_PosNull;
		IsEnd_PosDirty = r.IsEnd_PosDirty;

		field_Required = r.field_Required;
		IsRequiredNull = r.IsRequiredNull;
		IsRequiredDirty = r.IsRequiredDirty;

		field_Default_value = r.field_Default_value;
		IsDefault_valueNull = r.IsDefault_valueNull;
		IsDefault_valueDirty = r.IsDefault_valueDirty;

		field_Format = r.field_Format;
		IsFormatNull = r.IsFormatNull;
		IsFormatDirty = r.IsFormatDirty;

		field_Uni_ID = r.field_Uni_ID;
		IsUni_IDNull = r.IsUni_IDNull;
		IsUni_IDDirty = r.IsUni_IDDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const Document_DefinitionsTabRow& r)
	{
		field_Document_ID = r.field_Document_ID;
		IsDocument_IDNull = r.IsDocument_IDNull;
		IsDocument_IDDirty = r.IsDocument_IDDirty;

		field_Field_ID = r.field_Field_ID;
		IsField_IDNull = r.IsField_IDNull;
		IsField_IDDirty = r.IsField_IDDirty;

		field_Parent_ID = r.field_Parent_ID;
		IsParent_IDNull = r.IsParent_IDNull;
		IsParent_IDDirty = r.IsParent_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsDocument_IDDirty = b;
		IsField_IDDirty = b;
		IsParent_IDDirty = b;
		IsField_NameDirty = b;
		IsSequenceDirty = b;
		IsVar_Type_IDDirty = b;
		IsStart_PosDirty = b;
		IsEnd_PosDirty = b;
		IsRequiredDirty = b;
		IsDefault_valueDirty = b;
		IsFormatDirty = b;
		IsUni_IDDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsDocument_IDDirty ||
			IsField_IDDirty ||
			IsParent_IDDirty ||
			IsField_NameDirty ||
			IsSequenceDirty ||
			IsVar_Type_IDDirty ||
			IsStart_PosDirty ||
			IsEnd_PosDirty ||
			IsRequiredDirty ||
			IsDefault_valueDirty ||
			IsFormatDirty ||
			IsUni_IDDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Document_ID;
	bool IsDocument_IDNull;
	bool IsDocument_IDDirty;

	int field_Field_ID;
	bool IsField_IDNull;
	bool IsField_IDDirty;

	int field_Parent_ID;
	bool IsParent_IDNull;
	bool IsParent_IDDirty;

	std::string field_Field_Name;
	bool IsField_NameNull;
	bool IsField_NameDirty;

	int field_Sequence;
	bool IsSequenceNull;
	bool IsSequenceDirty;

	int field_Var_Type_ID;
	bool IsVar_Type_IDNull;
	bool IsVar_Type_IDDirty;

	int field_Start_Pos;
	bool IsStart_PosNull;
	bool IsStart_PosDirty;

	int field_End_Pos;
	bool IsEnd_PosNull;
	bool IsEnd_PosDirty;

	int field_Required;
	bool IsRequiredNull;
	bool IsRequiredDirty;

	std::string field_Default_value;
	bool IsDefault_valueNull;
	bool IsDefault_valueDirty;

	std::string field_Format;
	bool IsFormatNull;
	bool IsFormatDirty;

	int field_Uni_ID;
	bool IsUni_IDNull;
	bool IsUni_IDDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsDocument_IDPrimaryKey;
	bool IsField_IDPrimaryKey;
	bool IsParent_IDPrimaryKey;
	bool IsField_NamePrimaryKey;
	bool IsSequencePrimaryKey;
	bool IsVar_Type_IDPrimaryKey;
	bool IsStart_PosPrimaryKey;
	bool IsEnd_PosPrimaryKey;
	bool IsRequiredPrimaryKey;
	bool IsDefault_valuePrimaryKey;
	bool IsFormatPrimaryKey;
	bool IsUni_IDPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class Document_DefinitionsTab : public konga::DbRecordSet {
public:
	Document_DefinitionsTab() {}

	static Document_DefinitionsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "Document_DefinitionsTab";
	}

	void DoFieldExchange(const Document_DefinitionsTabRow& row) {
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

	int GetField_ID() {
		if( m_row.IsField_IDNull )
			throw konga::DbNullException("Field_ID");
		return m_row.field_Field_ID;
	}
	void SetField_ID(int i) {
		m_row.field_Field_ID = i;
		m_row.IsField_IDNull = false;
		m_row.IsField_IDDirty = true;
	}
	bool IsField_IDNull() const {
		return m_row.IsField_IDNull;
	}
	void SetField_IDToNull() {
		m_row.IsField_IDNull = true;
		m_row.IsField_IDDirty = true;
	}

	int GetParent_ID() {
		if( m_row.IsParent_IDNull )
			throw konga::DbNullException("Parent_ID");
		return m_row.field_Parent_ID;
	}
	void SetParent_ID(int i) {
		m_row.field_Parent_ID = i;
		m_row.IsParent_IDNull = false;
		m_row.IsParent_IDDirty = true;
	}
	bool IsParent_IDNull() const {
		return m_row.IsParent_IDNull;
	}
	void SetParent_IDToNull() {
		m_row.IsParent_IDNull = true;
		m_row.IsParent_IDDirty = true;
	}

	std::string GetField_Name() {
		if( m_row.IsField_NameNull )
			throw konga::DbNullException("Field_Name");
		return m_row.field_Field_Name;
	}
	void SetField_Name(const std::string& st) {
		m_row.field_Field_Name = st;
		m_row.IsField_NameNull = false;
		m_row.IsField_NameDirty = true;
	}
	bool IsField_NameNull() const {
		return m_row.IsField_NameNull;
	}
	void SetField_NameToNull() {
		m_row.field_Field_Name = "";
		m_row.IsField_NameNull = true;
		m_row.IsField_NameDirty = true;
	}

	int GetSequence() {
		if( m_row.IsSequenceNull )
			throw konga::DbNullException("Sequence");
		return m_row.field_Sequence;
	}
	void SetSequence(int i) {
		m_row.field_Sequence = i;
		m_row.IsSequenceNull = false;
		m_row.IsSequenceDirty = true;
	}
	bool IsSequenceNull() const {
		return m_row.IsSequenceNull;
	}
	void SetSequenceToNull() {
		m_row.IsSequenceNull = true;
		m_row.IsSequenceDirty = true;
	}

	int GetVar_Type_ID() {
		if( m_row.IsVar_Type_IDNull )
			throw konga::DbNullException("Var_Type_ID");
		return m_row.field_Var_Type_ID;
	}
	void SetVar_Type_ID(int i) {
		m_row.field_Var_Type_ID = i;
		m_row.IsVar_Type_IDNull = false;
		m_row.IsVar_Type_IDDirty = true;
	}
	bool IsVar_Type_IDNull() const {
		return m_row.IsVar_Type_IDNull;
	}
	void SetVar_Type_IDToNull() {
		m_row.IsVar_Type_IDNull = true;
		m_row.IsVar_Type_IDDirty = true;
	}

	int GetStart_Pos() {
		if( m_row.IsStart_PosNull )
			throw konga::DbNullException("Start_Pos");
		return m_row.field_Start_Pos;
	}
	void SetStart_Pos(int i) {
		m_row.field_Start_Pos = i;
		m_row.IsStart_PosNull = false;
		m_row.IsStart_PosDirty = true;
	}
	bool IsStart_PosNull() const {
		return m_row.IsStart_PosNull;
	}
	void SetStart_PosToNull() {
		m_row.IsStart_PosNull = true;
		m_row.IsStart_PosDirty = true;
	}

	int GetEnd_Pos() {
		if( m_row.IsEnd_PosNull )
			throw konga::DbNullException("End_Pos");
		return m_row.field_End_Pos;
	}
	void SetEnd_Pos(int i) {
		m_row.field_End_Pos = i;
		m_row.IsEnd_PosNull = false;
		m_row.IsEnd_PosDirty = true;
	}
	bool IsEnd_PosNull() const {
		return m_row.IsEnd_PosNull;
	}
	void SetEnd_PosToNull() {
		m_row.IsEnd_PosNull = true;
		m_row.IsEnd_PosDirty = true;
	}

	int GetRequired() {
		if( m_row.IsRequiredNull )
			throw konga::DbNullException("Required");
		return m_row.field_Required;
	}
	void SetRequired(int i) {
		m_row.field_Required = i;
		m_row.IsRequiredNull = false;
		m_row.IsRequiredDirty = true;
	}
	bool IsRequiredNull() const {
		return m_row.IsRequiredNull;
	}
	void SetRequiredToNull() {
		m_row.IsRequiredNull = true;
		m_row.IsRequiredDirty = true;
	}

	std::string GetDefault_value() {
		if( m_row.IsDefault_valueNull )
			throw konga::DbNullException("Default_value");
		return m_row.field_Default_value;
	}
	void SetDefault_value(const std::string& st) {
		m_row.field_Default_value = st;
		m_row.IsDefault_valueNull = false;
		m_row.IsDefault_valueDirty = true;
	}
	bool IsDefault_valueNull() const {
		return m_row.IsDefault_valueNull;
	}
	void SetDefault_valueToNull() {
		m_row.field_Default_value = "";
		m_row.IsDefault_valueNull = true;
		m_row.IsDefault_valueDirty = true;
	}

	std::string GetFormat() {
		if( m_row.IsFormatNull )
			throw konga::DbNullException("Format");
		return m_row.field_Format;
	}
	void SetFormat(const std::string& st) {
		m_row.field_Format = st;
		m_row.IsFormatNull = false;
		m_row.IsFormatDirty = true;
	}
	bool IsFormatNull() const {
		return m_row.IsFormatNull;
	}
	void SetFormatToNull() {
		m_row.field_Format = "";
		m_row.IsFormatNull = true;
		m_row.IsFormatDirty = true;
	}

	int GetUni_ID() {
		if( m_row.IsUni_IDNull )
			throw konga::DbNullException("Uni_ID");
		return m_row.field_Uni_ID;
	}
	void SetUni_ID(int i) {
		m_row.field_Uni_ID = i;
		m_row.IsUni_IDNull = false;
		m_row.IsUni_IDDirty = true;
	}
	bool IsUni_IDNull() const {
		return m_row.IsUni_IDNull;
	}
	void SetUni_IDToNull() {
		m_row.IsUni_IDNull = true;
		m_row.IsUni_IDDirty = true;
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
	Document_DefinitionsTabRow m_row;

private:
	bool IsDocument_IDDirty() const {
		return m_row.IsDocument_IDDirty;
	};
	bool IsField_IDDirty() const {
		return m_row.IsField_IDDirty;
	};
	bool IsParent_IDDirty() const {
		return m_row.IsParent_IDDirty;
	};
	bool IsField_NameDirty() const {
		return m_row.IsField_NameDirty;
	};
	bool IsSequenceDirty() const {
		return m_row.IsSequenceDirty;
	};
	bool IsVar_Type_IDDirty() const {
		return m_row.IsVar_Type_IDDirty;
	};
	bool IsStart_PosDirty() const {
		return m_row.IsStart_PosDirty;
	};
	bool IsEnd_PosDirty() const {
		return m_row.IsEnd_PosDirty;
	};
	bool IsRequiredDirty() const {
		return m_row.IsRequiredDirty;
	};
	bool IsDefault_valueDirty() const {
		return m_row.IsDefault_valueDirty;
	};
	bool IsFormatDirty() const {
		return m_row.IsFormatDirty;
	};
	bool IsUni_IDDirty() const {
		return m_row.IsUni_IDDirty;
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
