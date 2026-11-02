/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_TRANSFORMATION_DEFINITIONSTAB_H
#define KONGA_DB_CRTRANDB_TRANSFORMATION_DEFINITIONSTAB_H

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

class Transformation_DefinitionsTabRow
{
public:
	Transformation_DefinitionsTabRow() : 
			field_TranDiffID(0),
			IsTranDiffIDNull(true),
			IsTranDiffIDPrimaryKey(true),
			IsTranDiffIDDirty(false),
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(false),
			IsTransformation_IDDirty(false),
			IsTarget_Field_IDNull(true),
			IsTarget_Field_IDPrimaryKey(false),
			IsTarget_Field_IDDirty(false),
			IsTransformationNull(true),
			IsTransformationPrimaryKey(false),
			IsTransformationDirty(false),
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

	void Copy(const Transformation_DefinitionsTabRow& r)
	{
		field_TranDiffID = r.field_TranDiffID;
		IsTranDiffIDNull = r.IsTranDiffIDNull;
		IsTranDiffIDDirty = r.IsTranDiffIDDirty;

		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_Target_Field_ID = r.field_Target_Field_ID;
		IsTarget_Field_IDNull = r.IsTarget_Field_IDNull;
		IsTarget_Field_IDDirty = r.IsTarget_Field_IDDirty;

		field_Transformation = r.field_Transformation;
		IsTransformationNull = r.IsTransformationNull;
		IsTransformationDirty = r.IsTransformationDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const Transformation_DefinitionsTabRow& r)
	{
		field_TranDiffID = r.field_TranDiffID;
		IsTranDiffIDNull = r.IsTranDiffIDNull;
		IsTranDiffIDDirty = r.IsTranDiffIDDirty;

	}

	void SetDirty(bool b)
	{
		IsTranDiffIDDirty = b;
		IsTransformation_IDDirty = b;
		IsTarget_Field_IDDirty = b;
		IsTransformationDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsTranDiffIDDirty ||
			IsTransformation_IDDirty ||
			IsTarget_Field_IDDirty ||
			IsTransformationDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_TranDiffID;
	bool IsTranDiffIDNull;
	bool IsTranDiffIDDirty;

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	std::string field_Target_Field_ID;
	bool IsTarget_Field_IDNull;
	bool IsTarget_Field_IDDirty;

	std::string field_Transformation;
	bool IsTransformationNull;
	bool IsTransformationDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsTranDiffIDPrimaryKey;
	bool IsTransformation_IDPrimaryKey;
	bool IsTarget_Field_IDPrimaryKey;
	bool IsTransformationPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class Transformation_DefinitionsTab : public konga::DbRecordSet {
public:
	Transformation_DefinitionsTab() {}

	static Transformation_DefinitionsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "Transformation_DefinitionsTab";
	}

	void DoFieldExchange(const Transformation_DefinitionsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetTranDiffID() {
		if( m_row.IsTranDiffIDNull )
			throw konga::DbNullException("TranDiffID");
		return m_row.field_TranDiffID;
	}
	void SetTranDiffID(int i) {
		m_row.field_TranDiffID = i;
		m_row.IsTranDiffIDNull = false;
		m_row.IsTranDiffIDDirty = true;
	}
	bool IsTranDiffIDNull() const {
		return m_row.IsTranDiffIDNull;
	}
	void SetTranDiffIDToNull() {
		m_row.IsTranDiffIDNull = true;
		m_row.IsTranDiffIDDirty = true;
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

	std::string GetTarget_Field_ID() {
		if( m_row.IsTarget_Field_IDNull )
			throw konga::DbNullException("Target_Field_ID");
		return m_row.field_Target_Field_ID;
	}
	void SetTarget_Field_ID(const std::string& st) {
		m_row.field_Target_Field_ID = st;
		m_row.IsTarget_Field_IDNull = false;
		m_row.IsTarget_Field_IDDirty = true;
	}
	bool IsTarget_Field_IDNull() const {
		return m_row.IsTarget_Field_IDNull;
	}
	void SetTarget_Field_IDToNull() {
		m_row.field_Target_Field_ID = "";
		m_row.IsTarget_Field_IDNull = true;
		m_row.IsTarget_Field_IDDirty = true;
	}

	std::string GetTransformation() {
		if( m_row.IsTransformationNull )
			throw konga::DbNullException("Transformation");
		return m_row.field_Transformation;
	}
	void SetTransformation(const std::string& st) {
		m_row.field_Transformation = st;
		m_row.IsTransformationNull = false;
		m_row.IsTransformationDirty = true;
	}
	bool IsTransformationNull() const {
		return m_row.IsTransformationNull;
	}
	void SetTransformationToNull() {
		m_row.field_Transformation = "";
		m_row.IsTransformationNull = true;
		m_row.IsTransformationDirty = true;
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
	Transformation_DefinitionsTabRow m_row;

private:
	bool IsTranDiffIDDirty() const {
		return m_row.IsTranDiffIDDirty;
	};
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsTarget_Field_IDDirty() const {
		return m_row.IsTarget_Field_IDDirty;
	};
	bool IsTransformationDirty() const {
		return m_row.IsTransformationDirty;
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
