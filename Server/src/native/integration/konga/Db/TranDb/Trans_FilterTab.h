/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_TRANS_FILTERTAB_H
#define KONGA_DB_CRTRANDB_TRANS_FILTERTAB_H

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

class Trans_FilterTabRow
{
public:
	Trans_FilterTabRow() : 
			field_Trans_Filter_ID(0),
			IsTrans_Filter_IDNull(true),
			IsTrans_Filter_IDPrimaryKey(true),
			IsTrans_Filter_IDDirty(false),
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(false),
			IsTransformation_IDDirty(false),
			field_Group_ID(0),
			IsGroup_IDNull(true),
			IsGroup_IDPrimaryKey(false),
			IsGroup_IDDirty(false),
			IsTNameNull(true),
			IsTNamePrimaryKey(false),
			IsTNameDirty(false),
			IsCNameNull(true),
			IsCNamePrimaryKey(false),
			IsCNameDirty(false),
			IsConditionNull(true),
			IsConditionPrimaryKey(false),
			IsConditionDirty(false),
			IsSortByNull(true),
			IsSortByPrimaryKey(false),
			IsSortByDirty(false),
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

	void Copy(const Trans_FilterTabRow& r)
	{
		field_Trans_Filter_ID = r.field_Trans_Filter_ID;
		IsTrans_Filter_IDNull = r.IsTrans_Filter_IDNull;
		IsTrans_Filter_IDDirty = r.IsTrans_Filter_IDDirty;

		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_Group_ID = r.field_Group_ID;
		IsGroup_IDNull = r.IsGroup_IDNull;
		IsGroup_IDDirty = r.IsGroup_IDDirty;

		field_TName = r.field_TName;
		IsTNameNull = r.IsTNameNull;
		IsTNameDirty = r.IsTNameDirty;

		field_CName = r.field_CName;
		IsCNameNull = r.IsCNameNull;
		IsCNameDirty = r.IsCNameDirty;

		field_Condition = r.field_Condition;
		IsConditionNull = r.IsConditionNull;
		IsConditionDirty = r.IsConditionDirty;

		field_SortBy = r.field_SortBy;
		IsSortByNull = r.IsSortByNull;
		IsSortByDirty = r.IsSortByDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const Trans_FilterTabRow& r)
	{
		field_Trans_Filter_ID = r.field_Trans_Filter_ID;
		IsTrans_Filter_IDNull = r.IsTrans_Filter_IDNull;
		IsTrans_Filter_IDDirty = r.IsTrans_Filter_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsTrans_Filter_IDDirty = b;
		IsTransformation_IDDirty = b;
		IsGroup_IDDirty = b;
		IsTNameDirty = b;
		IsCNameDirty = b;
		IsConditionDirty = b;
		IsSortByDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsTrans_Filter_IDDirty ||
			IsTransformation_IDDirty ||
			IsGroup_IDDirty ||
			IsTNameDirty ||
			IsCNameDirty ||
			IsConditionDirty ||
			IsSortByDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Trans_Filter_ID;
	bool IsTrans_Filter_IDNull;
	bool IsTrans_Filter_IDDirty;

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	int field_Group_ID;
	bool IsGroup_IDNull;
	bool IsGroup_IDDirty;

	std::string field_TName;
	bool IsTNameNull;
	bool IsTNameDirty;

	std::string field_CName;
	bool IsCNameNull;
	bool IsCNameDirty;

	std::string field_Condition;
	bool IsConditionNull;
	bool IsConditionDirty;

	std::string field_SortBy;
	bool IsSortByNull;
	bool IsSortByDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsTrans_Filter_IDPrimaryKey;
	bool IsTransformation_IDPrimaryKey;
	bool IsGroup_IDPrimaryKey;
	bool IsTNamePrimaryKey;
	bool IsCNamePrimaryKey;
	bool IsConditionPrimaryKey;
	bool IsSortByPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class Trans_FilterTab : public konga::DbRecordSet {
public:
	Trans_FilterTab() {}

	static Trans_FilterTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "Trans_FilterTab";
	}

	void DoFieldExchange(const Trans_FilterTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetTrans_Filter_ID() {
		if( m_row.IsTrans_Filter_IDNull )
			throw konga::DbNullException("Trans_Filter_ID");
		return m_row.field_Trans_Filter_ID;
	}
	void SetTrans_Filter_ID(int i) {
		m_row.field_Trans_Filter_ID = i;
		m_row.IsTrans_Filter_IDNull = false;
		m_row.IsTrans_Filter_IDDirty = true;
	}
	bool IsTrans_Filter_IDNull() const {
		return m_row.IsTrans_Filter_IDNull;
	}
	void SetTrans_Filter_IDToNull() {
		m_row.IsTrans_Filter_IDNull = true;
		m_row.IsTrans_Filter_IDDirty = true;
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

	int GetGroup_ID() {
		if( m_row.IsGroup_IDNull )
			throw konga::DbNullException("Group_ID");
		return m_row.field_Group_ID;
	}
	void SetGroup_ID(int i) {
		m_row.field_Group_ID = i;
		m_row.IsGroup_IDNull = false;
		m_row.IsGroup_IDDirty = true;
	}
	bool IsGroup_IDNull() const {
		return m_row.IsGroup_IDNull;
	}
	void SetGroup_IDToNull() {
		m_row.IsGroup_IDNull = true;
		m_row.IsGroup_IDDirty = true;
	}

	std::string GetTName() {
		if( m_row.IsTNameNull )
			throw konga::DbNullException("TName");
		return m_row.field_TName;
	}
	void SetTName(const std::string& st) {
		m_row.field_TName = st;
		m_row.IsTNameNull = false;
		m_row.IsTNameDirty = true;
	}
	bool IsTNameNull() const {
		return m_row.IsTNameNull;
	}
	void SetTNameToNull() {
		m_row.field_TName = "";
		m_row.IsTNameNull = true;
		m_row.IsTNameDirty = true;
	}

	std::string GetCName() {
		if( m_row.IsCNameNull )
			throw konga::DbNullException("CName");
		return m_row.field_CName;
	}
	void SetCName(const std::string& st) {
		m_row.field_CName = st;
		m_row.IsCNameNull = false;
		m_row.IsCNameDirty = true;
	}
	bool IsCNameNull() const {
		return m_row.IsCNameNull;
	}
	void SetCNameToNull() {
		m_row.field_CName = "";
		m_row.IsCNameNull = true;
		m_row.IsCNameDirty = true;
	}

	std::string GetCondition() {
		if( m_row.IsConditionNull )
			throw konga::DbNullException("Condition");
		return m_row.field_Condition;
	}
	void SetCondition(const std::string& st) {
		m_row.field_Condition = st;
		m_row.IsConditionNull = false;
		m_row.IsConditionDirty = true;
	}
	bool IsConditionNull() const {
		return m_row.IsConditionNull;
	}
	void SetConditionToNull() {
		m_row.field_Condition = "";
		m_row.IsConditionNull = true;
		m_row.IsConditionDirty = true;
	}

	std::string GetSortBy() {
		if( m_row.IsSortByNull )
			throw konga::DbNullException("SortBy");
		return m_row.field_SortBy;
	}
	void SetSortBy(const std::string& st) {
		m_row.field_SortBy = st;
		m_row.IsSortByNull = false;
		m_row.IsSortByDirty = true;
	}
	bool IsSortByNull() const {
		return m_row.IsSortByNull;
	}
	void SetSortByToNull() {
		m_row.field_SortBy = "";
		m_row.IsSortByNull = true;
		m_row.IsSortByDirty = true;
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
	Trans_FilterTabRow m_row;

private:
	bool IsTrans_Filter_IDDirty() const {
		return m_row.IsTrans_Filter_IDDirty;
	};
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsGroup_IDDirty() const {
		return m_row.IsGroup_IDDirty;
	};
	bool IsTNameDirty() const {
		return m_row.IsTNameDirty;
	};
	bool IsCNameDirty() const {
		return m_row.IsCNameDirty;
	};
	bool IsConditionDirty() const {
		return m_row.IsConditionDirty;
	};
	bool IsSortByDirty() const {
		return m_row.IsSortByDirty;
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
