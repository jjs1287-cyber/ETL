/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_TRAN_RELATIONSTAB_H
#define KONGA_DB_CRTRANDB_TRAN_RELATIONSTAB_H

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

class Tran_RelationsTabRow
{
public:
	Tran_RelationsTabRow() : 
			field_Relation_ID(0),
			IsRelation_IDNull(true),
			IsRelation_IDPrimaryKey(true),
			IsRelation_IDDirty(false),
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(true),
			IsTransformation_IDDirty(false),
			IsDoc_TypeNull(true),
			IsDoc_TypePrimaryKey(false),
			IsDoc_TypeDirty(false),
			IsParent_NameNull(true),
			IsParent_NamePrimaryKey(false),
			IsParent_NameDirty(false),
			IsParent_FieldNull(true),
			IsParent_FieldPrimaryKey(false),
			IsParent_FieldDirty(false),
			IsChild_NameNull(true),
			IsChild_NamePrimaryKey(false),
			IsChild_NameDirty(false),
			IsChild_FieldNull(true),
			IsChild_FieldPrimaryKey(false),
			IsChild_FieldDirty(false),
			field_Outer_Join(0),
			IsOuter_JoinNull(true),
			IsOuter_JoinPrimaryKey(false),
			IsOuter_JoinDirty(false),
			field_OneToOne(0),
			IsOneToOneNull(true),
			IsOneToOnePrimaryKey(false),
			IsOneToOneDirty(false),
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

	void Copy(const Tran_RelationsTabRow& r)
	{
		field_Relation_ID = r.field_Relation_ID;
		IsRelation_IDNull = r.IsRelation_IDNull;
		IsRelation_IDDirty = r.IsRelation_IDDirty;

		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_Doc_Type = r.field_Doc_Type;
		IsDoc_TypeNull = r.IsDoc_TypeNull;
		IsDoc_TypeDirty = r.IsDoc_TypeDirty;

		field_Parent_Name = r.field_Parent_Name;
		IsParent_NameNull = r.IsParent_NameNull;
		IsParent_NameDirty = r.IsParent_NameDirty;

		field_Parent_Field = r.field_Parent_Field;
		IsParent_FieldNull = r.IsParent_FieldNull;
		IsParent_FieldDirty = r.IsParent_FieldDirty;

		field_Child_Name = r.field_Child_Name;
		IsChild_NameNull = r.IsChild_NameNull;
		IsChild_NameDirty = r.IsChild_NameDirty;

		field_Child_Field = r.field_Child_Field;
		IsChild_FieldNull = r.IsChild_FieldNull;
		IsChild_FieldDirty = r.IsChild_FieldDirty;

		field_Outer_Join = r.field_Outer_Join;
		IsOuter_JoinNull = r.IsOuter_JoinNull;
		IsOuter_JoinDirty = r.IsOuter_JoinDirty;

		field_OneToOne = r.field_OneToOne;
		IsOneToOneNull = r.IsOneToOneNull;
		IsOneToOneDirty = r.IsOneToOneDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const Tran_RelationsTabRow& r)
	{
		field_Relation_ID = r.field_Relation_ID;
		IsRelation_IDNull = r.IsRelation_IDNull;
		IsRelation_IDDirty = r.IsRelation_IDDirty;

		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsRelation_IDDirty = b;
		IsTransformation_IDDirty = b;
		IsDoc_TypeDirty = b;
		IsParent_NameDirty = b;
		IsParent_FieldDirty = b;
		IsChild_NameDirty = b;
		IsChild_FieldDirty = b;
		IsOuter_JoinDirty = b;
		IsOneToOneDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsRelation_IDDirty ||
			IsTransformation_IDDirty ||
			IsDoc_TypeDirty ||
			IsParent_NameDirty ||
			IsParent_FieldDirty ||
			IsChild_NameDirty ||
			IsChild_FieldDirty ||
			IsOuter_JoinDirty ||
			IsOneToOneDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Relation_ID;
	bool IsRelation_IDNull;
	bool IsRelation_IDDirty;

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	std::string field_Doc_Type;
	bool IsDoc_TypeNull;
	bool IsDoc_TypeDirty;

	std::string field_Parent_Name;
	bool IsParent_NameNull;
	bool IsParent_NameDirty;

	std::string field_Parent_Field;
	bool IsParent_FieldNull;
	bool IsParent_FieldDirty;

	std::string field_Child_Name;
	bool IsChild_NameNull;
	bool IsChild_NameDirty;

	std::string field_Child_Field;
	bool IsChild_FieldNull;
	bool IsChild_FieldDirty;

	int field_Outer_Join;
	bool IsOuter_JoinNull;
	bool IsOuter_JoinDirty;

	int field_OneToOne;
	bool IsOneToOneNull;
	bool IsOneToOneDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsRelation_IDPrimaryKey;
	bool IsTransformation_IDPrimaryKey;
	bool IsDoc_TypePrimaryKey;
	bool IsParent_NamePrimaryKey;
	bool IsParent_FieldPrimaryKey;
	bool IsChild_NamePrimaryKey;
	bool IsChild_FieldPrimaryKey;
	bool IsOuter_JoinPrimaryKey;
	bool IsOneToOnePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class Tran_RelationsTab : public konga::DbRecordSet {
public:
	Tran_RelationsTab() {}

	static Tran_RelationsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "Tran_RelationsTab";
	}

	void DoFieldExchange(const Tran_RelationsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetRelation_ID() {
		if( m_row.IsRelation_IDNull )
			throw konga::DbNullException("Relation_ID");
		return m_row.field_Relation_ID;
	}
	void SetRelation_ID(int i) {
		m_row.field_Relation_ID = i;
		m_row.IsRelation_IDNull = false;
		m_row.IsRelation_IDDirty = true;
	}
	bool IsRelation_IDNull() const {
		return m_row.IsRelation_IDNull;
	}
	void SetRelation_IDToNull() {
		m_row.IsRelation_IDNull = true;
		m_row.IsRelation_IDDirty = true;
	}

	int GetTransformation_ID() {
		if( m_row.IsTransformation_IDNull )
			throw konga::DbNullException("Transformation_ID");
		return m_row.field_Transformation_ID;
	}
	void SetTransformation_ID(int i) {
		m_row.field_Transformation_ID = i;
		m_row.IsTransformation_IDNull = false;
		m_row.IsTransformation_IDDirty = true;
	}
	bool IsTransformation_IDNull() const {
		return m_row.IsTransformation_IDNull;
	}
	void SetTransformation_IDToNull() {
		m_row.IsTransformation_IDNull = true;
		m_row.IsTransformation_IDDirty = true;
	}

	std::string GetDoc_Type() {
		if( m_row.IsDoc_TypeNull )
			throw konga::DbNullException("Doc_Type");
		return m_row.field_Doc_Type;
	}
	void SetDoc_Type(const std::string& st) {
		m_row.field_Doc_Type = st;
		m_row.IsDoc_TypeNull = false;
		m_row.IsDoc_TypeDirty = true;
	}
	bool IsDoc_TypeNull() const {
		return m_row.IsDoc_TypeNull;
	}
	void SetDoc_TypeToNull() {
		m_row.field_Doc_Type = "";
		m_row.IsDoc_TypeNull = true;
		m_row.IsDoc_TypeDirty = true;
	}

	std::string GetParent_Name() {
		if( m_row.IsParent_NameNull )
			throw konga::DbNullException("Parent_Name");
		return m_row.field_Parent_Name;
	}
	void SetParent_Name(const std::string& st) {
		m_row.field_Parent_Name = st;
		m_row.IsParent_NameNull = false;
		m_row.IsParent_NameDirty = true;
	}
	bool IsParent_NameNull() const {
		return m_row.IsParent_NameNull;
	}
	void SetParent_NameToNull() {
		m_row.field_Parent_Name = "";
		m_row.IsParent_NameNull = true;
		m_row.IsParent_NameDirty = true;
	}

	std::string GetParent_Field() {
		if( m_row.IsParent_FieldNull )
			throw konga::DbNullException("Parent_Field");
		return m_row.field_Parent_Field;
	}
	void SetParent_Field(const std::string& st) {
		m_row.field_Parent_Field = st;
		m_row.IsParent_FieldNull = false;
		m_row.IsParent_FieldDirty = true;
	}
	bool IsParent_FieldNull() const {
		return m_row.IsParent_FieldNull;
	}
	void SetParent_FieldToNull() {
		m_row.field_Parent_Field = "";
		m_row.IsParent_FieldNull = true;
		m_row.IsParent_FieldDirty = true;
	}

	std::string GetChild_Name() {
		if( m_row.IsChild_NameNull )
			throw konga::DbNullException("Child_Name");
		return m_row.field_Child_Name;
	}
	void SetChild_Name(const std::string& st) {
		m_row.field_Child_Name = st;
		m_row.IsChild_NameNull = false;
		m_row.IsChild_NameDirty = true;
	}
	bool IsChild_NameNull() const {
		return m_row.IsChild_NameNull;
	}
	void SetChild_NameToNull() {
		m_row.field_Child_Name = "";
		m_row.IsChild_NameNull = true;
		m_row.IsChild_NameDirty = true;
	}

	std::string GetChild_Field() {
		if( m_row.IsChild_FieldNull )
			throw konga::DbNullException("Child_Field");
		return m_row.field_Child_Field;
	}
	void SetChild_Field(const std::string& st) {
		m_row.field_Child_Field = st;
		m_row.IsChild_FieldNull = false;
		m_row.IsChild_FieldDirty = true;
	}
	bool IsChild_FieldNull() const {
		return m_row.IsChild_FieldNull;
	}
	void SetChild_FieldToNull() {
		m_row.field_Child_Field = "";
		m_row.IsChild_FieldNull = true;
		m_row.IsChild_FieldDirty = true;
	}

	int GetOuter_Join() {
		if( m_row.IsOuter_JoinNull )
			throw konga::DbNullException("Outer_Join");
		return m_row.field_Outer_Join;
	}
	void SetOuter_Join(int i) {
		m_row.field_Outer_Join = i;
		m_row.IsOuter_JoinNull = false;
		m_row.IsOuter_JoinDirty = true;
	}
	bool IsOuter_JoinNull() const {
		return m_row.IsOuter_JoinNull;
	}
	void SetOuter_JoinToNull() {
		m_row.IsOuter_JoinNull = true;
		m_row.IsOuter_JoinDirty = true;
	}

	int GetOneToOne() {
		if( m_row.IsOneToOneNull )
			throw konga::DbNullException("OneToOne");
		return m_row.field_OneToOne;
	}
	void SetOneToOne(int i) {
		m_row.field_OneToOne = i;
		m_row.IsOneToOneNull = false;
		m_row.IsOneToOneDirty = true;
	}
	bool IsOneToOneNull() const {
		return m_row.IsOneToOneNull;
	}
	void SetOneToOneToNull() {
		m_row.IsOneToOneNull = true;
		m_row.IsOneToOneDirty = true;
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
	Tran_RelationsTabRow m_row;

private:
	bool IsRelation_IDDirty() const {
		return m_row.IsRelation_IDDirty;
	};
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsDoc_TypeDirty() const {
		return m_row.IsDoc_TypeDirty;
	};
	bool IsParent_NameDirty() const {
		return m_row.IsParent_NameDirty;
	};
	bool IsParent_FieldDirty() const {
		return m_row.IsParent_FieldDirty;
	};
	bool IsChild_NameDirty() const {
		return m_row.IsChild_NameDirty;
	};
	bool IsChild_FieldDirty() const {
		return m_row.IsChild_FieldDirty;
	};
	bool IsOuter_JoinDirty() const {
		return m_row.IsOuter_JoinDirty;
	};
	bool IsOneToOneDirty() const {
		return m_row.IsOneToOneDirty;
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
