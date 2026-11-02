/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_KEY_VALUES_H
#define KONGA_DB_CRTRANDB_KEY_VALUES_H

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

class Key_ValuesRow
{
public:
	Key_ValuesRow() : 
			field_Key_ValuesID(0),
			IsKey_ValuesIDNull(true),
			IsKey_ValuesIDPrimaryKey(true),
			IsKey_ValuesIDDirty(false),
			IsKey_NameNull(true),
			IsKey_NamePrimaryKey(false),
			IsKey_NameDirty(false),
			IsKey_ValueNull(true),
			IsKey_ValuePrimaryKey(false),
			IsKey_ValueDirty(false),
			IsKey_TypeNull(true),
			IsKey_TypePrimaryKey(false),
			IsKey_TypeDirty(false),
			field_Group_Id(0),
			IsGroup_IdNull(true),
			IsGroup_IdPrimaryKey(false),
			IsGroup_IdDirty(false),
			IsKey_Value_DescriptionNull(true),
			IsKey_Value_DescriptionPrimaryKey(false),
			IsKey_Value_DescriptionDirty(false),
			IsKey_Display_NameNull(true),
			IsKey_Display_NamePrimaryKey(false),
			IsKey_Display_NameDirty(false),
			field_Sort_Sequence(0),
			IsSort_SequenceNull(true),
			IsSort_SequencePrimaryKey(false),
			IsSort_SequenceDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false),
			IsLong_Key_ValueNull(true),
			IsLong_Key_ValuePrimaryKey(false),
			IsLong_Key_ValueDirty(false)
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

	void Copy(const Key_ValuesRow& r)
	{
		field_Key_ValuesID = r.field_Key_ValuesID;
		IsKey_ValuesIDNull = r.IsKey_ValuesIDNull;
		IsKey_ValuesIDDirty = r.IsKey_ValuesIDDirty;

		field_Key_Name = r.field_Key_Name;
		IsKey_NameNull = r.IsKey_NameNull;
		IsKey_NameDirty = r.IsKey_NameDirty;

		field_Key_Value = r.field_Key_Value;
		IsKey_ValueNull = r.IsKey_ValueNull;
		IsKey_ValueDirty = r.IsKey_ValueDirty;

		field_Key_Type = r.field_Key_Type;
		IsKey_TypeNull = r.IsKey_TypeNull;
		IsKey_TypeDirty = r.IsKey_TypeDirty;

		field_Group_Id = r.field_Group_Id;
		IsGroup_IdNull = r.IsGroup_IdNull;
		IsGroup_IdDirty = r.IsGroup_IdDirty;

		field_Key_Value_Description = r.field_Key_Value_Description;
		IsKey_Value_DescriptionNull = r.IsKey_Value_DescriptionNull;
		IsKey_Value_DescriptionDirty = r.IsKey_Value_DescriptionDirty;

		field_Key_Display_Name = r.field_Key_Display_Name;
		IsKey_Display_NameNull = r.IsKey_Display_NameNull;
		IsKey_Display_NameDirty = r.IsKey_Display_NameDirty;

		field_Sort_Sequence = r.field_Sort_Sequence;
		IsSort_SequenceNull = r.IsSort_SequenceNull;
		IsSort_SequenceDirty = r.IsSort_SequenceDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

		field_Long_Key_Value = r.field_Long_Key_Value;
		IsLong_Key_ValueNull = r.IsLong_Key_ValueNull;
		IsLong_Key_ValueDirty = r.IsLong_Key_ValueDirty;
	}

	void CopyPKs(const Key_ValuesRow& r)
	{
		field_Key_ValuesID = r.field_Key_ValuesID;
		IsKey_ValuesIDNull = r.IsKey_ValuesIDNull;
		IsKey_ValuesIDDirty = r.IsKey_ValuesIDDirty;

	}

	void SetDirty(bool b)
	{
		IsKey_ValuesIDDirty = b;
		IsKey_NameDirty = b;
		IsKey_ValueDirty = b;
		IsKey_TypeDirty = b;
		IsGroup_IdDirty = b;
		IsKey_Value_DescriptionDirty = b;
		IsKey_Display_NameDirty = b;
		IsSort_SequenceDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
		IsLong_Key_ValueDirty = b;
	}

	bool IsDirty() const
	{
		return IsKey_ValuesIDDirty ||
			IsKey_NameDirty ||
			IsKey_ValueDirty ||
			IsKey_TypeDirty ||
			IsGroup_IdDirty ||
			IsKey_Value_DescriptionDirty ||
			IsKey_Display_NameDirty ||
			IsSort_SequenceDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty ||
			IsLong_Key_ValueDirty;
	}

	int field_Key_ValuesID;
	bool IsKey_ValuesIDNull;
	bool IsKey_ValuesIDDirty;

	std::string field_Key_Name;
	bool IsKey_NameNull;
	bool IsKey_NameDirty;

	std::string field_Key_Value;
	bool IsKey_ValueNull;
	bool IsKey_ValueDirty;

	std::string field_Key_Type;
	bool IsKey_TypeNull;
	bool IsKey_TypeDirty;

	int field_Group_Id;
	bool IsGroup_IdNull;
	bool IsGroup_IdDirty;

	std::string field_Key_Value_Description;
	bool IsKey_Value_DescriptionNull;
	bool IsKey_Value_DescriptionDirty;

	std::string field_Key_Display_Name;
	bool IsKey_Display_NameNull;
	bool IsKey_Display_NameDirty;

	int field_Sort_Sequence;
	bool IsSort_SequenceNull;
	bool IsSort_SequenceDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;

	std::string field_Long_Key_Value;
	bool IsLong_Key_ValueNull;
	bool IsLong_Key_ValueDirty;


private:
	bool IsKey_ValuesIDPrimaryKey;
	bool IsKey_NamePrimaryKey;
	bool IsKey_ValuePrimaryKey;
	bool IsKey_TypePrimaryKey;
	bool IsGroup_IdPrimaryKey;
	bool IsKey_Value_DescriptionPrimaryKey;
	bool IsKey_Display_NamePrimaryKey;
	bool IsSort_SequencePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;
	bool IsLong_Key_ValuePrimaryKey;
};


class Key_Values : public konga::DbRecordSet {
public:
	Key_Values() {}

	static Key_Values* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "Key_Values";
	}

	void DoFieldExchange(const Key_ValuesRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetKey_ValuesID() {
		if( m_row.IsKey_ValuesIDNull )
			throw konga::DbNullException("Key_ValuesID");
		return m_row.field_Key_ValuesID;
	}
	void SetKey_ValuesID(int i) {
		m_row.field_Key_ValuesID = i;
		m_row.IsKey_ValuesIDNull = false;
		m_row.IsKey_ValuesIDDirty = true;
	}
	bool IsKey_ValuesIDNull() const {
		return m_row.IsKey_ValuesIDNull;
	}
	void SetKey_ValuesIDToNull() {
		m_row.IsKey_ValuesIDNull = true;
		m_row.IsKey_ValuesIDDirty = true;
	}

	std::string GetKey_Name() {
		if( m_row.IsKey_NameNull )
			throw konga::DbNullException("Key_Name");
		return m_row.field_Key_Name;
	}
	void SetKey_Name(const std::string& st) {
		m_row.field_Key_Name = st;
		m_row.IsKey_NameNull = false;
		m_row.IsKey_NameDirty = true;
	}
	bool IsKey_NameNull() const {
		return m_row.IsKey_NameNull;
	}
	void SetKey_NameToNull() {
		m_row.field_Key_Name = "";
		m_row.IsKey_NameNull = true;
		m_row.IsKey_NameDirty = true;
	}

	std::string GetKey_Value() {
		if( m_row.IsKey_ValueNull )
			throw konga::DbNullException("Key_Value");
		return m_row.field_Key_Value;
	}
	void SetKey_Value(const std::string& st) {
		m_row.field_Key_Value = st;
		m_row.IsKey_ValueNull = false;
		m_row.IsKey_ValueDirty = true;
	}
	bool IsKey_ValueNull() const {
		return m_row.IsKey_ValueNull;
	}
	void SetKey_ValueToNull() {
		m_row.field_Key_Value = "";
		m_row.IsKey_ValueNull = true;
		m_row.IsKey_ValueDirty = true;
	}

	std::string GetKey_Type() {
		if( m_row.IsKey_TypeNull )
			throw konga::DbNullException("Key_Type");
		return m_row.field_Key_Type;
	}
	void SetKey_Type(const std::string& st) {
		m_row.field_Key_Type = st;
		m_row.IsKey_TypeNull = false;
		m_row.IsKey_TypeDirty = true;
	}
	bool IsKey_TypeNull() const {
		return m_row.IsKey_TypeNull;
	}
	void SetKey_TypeToNull() {
		m_row.field_Key_Type = "";
		m_row.IsKey_TypeNull = true;
		m_row.IsKey_TypeDirty = true;
	}

	int GetGroup_Id() {
		if( m_row.IsGroup_IdNull )
			throw konga::DbNullException("Group_Id");
		return m_row.field_Group_Id;
	}
	void SetGroup_Id(int i) {
		m_row.field_Group_Id = i;
		m_row.IsGroup_IdNull = false;
		m_row.IsGroup_IdDirty = true;
	}
	bool IsGroup_IdNull() const {
		return m_row.IsGroup_IdNull;
	}
	void SetGroup_IdToNull() {
		m_row.IsGroup_IdNull = true;
		m_row.IsGroup_IdDirty = true;
	}

	std::string GetKey_Value_Description() {
		if( m_row.IsKey_Value_DescriptionNull )
			throw konga::DbNullException("Key_Value_Description");
		return m_row.field_Key_Value_Description;
	}
	void SetKey_Value_Description(const std::string& st) {
		m_row.field_Key_Value_Description = st;
		m_row.IsKey_Value_DescriptionNull = false;
		m_row.IsKey_Value_DescriptionDirty = true;
	}
	bool IsKey_Value_DescriptionNull() const {
		return m_row.IsKey_Value_DescriptionNull;
	}
	void SetKey_Value_DescriptionToNull() {
		m_row.field_Key_Value_Description = "";
		m_row.IsKey_Value_DescriptionNull = true;
		m_row.IsKey_Value_DescriptionDirty = true;
	}

	std::string GetKey_Display_Name() {
		if( m_row.IsKey_Display_NameNull )
			throw konga::DbNullException("Key_Display_Name");
		return m_row.field_Key_Display_Name;
	}
	void SetKey_Display_Name(const std::string& st) {
		m_row.field_Key_Display_Name = st;
		m_row.IsKey_Display_NameNull = false;
		m_row.IsKey_Display_NameDirty = true;
	}
	bool IsKey_Display_NameNull() const {
		return m_row.IsKey_Display_NameNull;
	}
	void SetKey_Display_NameToNull() {
		m_row.field_Key_Display_Name = "";
		m_row.IsKey_Display_NameNull = true;
		m_row.IsKey_Display_NameDirty = true;
	}

	int GetSort_Sequence() {
		if( m_row.IsSort_SequenceNull )
			throw konga::DbNullException("Sort_Sequence");
		return m_row.field_Sort_Sequence;
	}
	void SetSort_Sequence(int i) {
		m_row.field_Sort_Sequence = i;
		m_row.IsSort_SequenceNull = false;
		m_row.IsSort_SequenceDirty = true;
	}
	bool IsSort_SequenceNull() const {
		return m_row.IsSort_SequenceNull;
	}
	void SetSort_SequenceToNull() {
		m_row.IsSort_SequenceNull = true;
		m_row.IsSort_SequenceDirty = true;
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

	std::string GetLong_Key_Value() {
		if( m_row.IsLong_Key_ValueNull )
			throw konga::DbNullException("Long_Key_Value");
		return m_row.field_Long_Key_Value;
	}
	void SetLong_Key_Value(const std::string& st) {
		m_row.field_Long_Key_Value = st;
		m_row.IsLong_Key_ValueNull = false;
		m_row.IsLong_Key_ValueDirty = true;
	}
	bool IsLong_Key_ValueNull() const {
		return m_row.IsLong_Key_ValueNull;
	}
	void SetLong_Key_ValueToNull() {
		m_row.field_Long_Key_Value = "";
		m_row.IsLong_Key_ValueNull = true;
		m_row.IsLong_Key_ValueDirty = true;
	}

protected:
	Key_ValuesRow m_row;

private:
	bool IsKey_ValuesIDDirty() const {
		return m_row.IsKey_ValuesIDDirty;
	};
	bool IsKey_NameDirty() const {
		return m_row.IsKey_NameDirty;
	};
	bool IsKey_ValueDirty() const {
		return m_row.IsKey_ValueDirty;
	};
	bool IsKey_TypeDirty() const {
		return m_row.IsKey_TypeDirty;
	};
	bool IsGroup_IdDirty() const {
		return m_row.IsGroup_IdDirty;
	};
	bool IsKey_Value_DescriptionDirty() const {
		return m_row.IsKey_Value_DescriptionDirty;
	};
	bool IsKey_Display_NameDirty() const {
		return m_row.IsKey_Display_NameDirty;
	};
	bool IsSort_SequenceDirty() const {
		return m_row.IsSort_SequenceDirty;
	};
	bool IsInsertTimeDirty() const {
		return m_row.IsInsertTimeDirty;
	};
	bool IsModTimeDirty() const {
		return m_row.IsModTimeDirty;
	};
	bool IsLong_Key_ValueDirty() const {
		return m_row.IsLong_Key_ValueDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
