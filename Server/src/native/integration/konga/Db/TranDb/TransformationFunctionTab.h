/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_TRANSFORMATIONFUNCTIONTAB_H
#define KONGA_DB_CRTRANDB_TRANSFORMATIONFUNCTIONTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class TransformationFunctionTabRow
{
public:
	TransformationFunctionTabRow() : 
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(true),
			IsTransformation_IDDirty(false),
			field_Function_ID(0),
			IsFunction_IDNull(true),
			IsFunction_IDPrimaryKey(false),
			IsFunction_IDDirty(false)
	{
	}

	void Copy(const TransformationFunctionTabRow& r)
	{
		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_Function_ID = r.field_Function_ID;
		IsFunction_IDNull = r.IsFunction_IDNull;
		IsFunction_IDDirty = r.IsFunction_IDDirty;

	}

	void CopyPKs(const TransformationFunctionTabRow& r)
	{
		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsTransformation_IDDirty = b;
		IsFunction_IDDirty = b;
	}

	bool IsDirty() const
	{
		return IsTransformation_IDDirty ||
			IsFunction_IDDirty;
	}

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	int field_Function_ID;
	bool IsFunction_IDNull;
	bool IsFunction_IDDirty;


private:
	bool IsTransformation_IDPrimaryKey;
	bool IsFunction_IDPrimaryKey;

};


class TransformationFunctionTab : public konga::DbRecordSet {
public:
	TransformationFunctionTab() {}

	static TransformationFunctionTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TransformationFunctionTab";
	}

	void DoFieldExchange(const TransformationFunctionTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	int GetFunction_ID() {
		if( m_row.IsFunction_IDNull )
			throw konga::DbNullException("Function_ID");
		return m_row.field_Function_ID;
	}
	void SetFunction_ID(int i) {
		m_row.field_Function_ID = i;
		m_row.IsFunction_IDNull = false;
		m_row.IsFunction_IDDirty = true;
	}
	bool IsFunction_IDNull() const {
		return m_row.IsFunction_IDNull;
	}
	void SetFunction_IDToNull() {
		m_row.IsFunction_IDNull = true;
		m_row.IsFunction_IDDirty = true;
	}

protected:
	TransformationFunctionTabRow m_row;

private:
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsFunction_IDDirty() const {
		return m_row.IsFunction_IDDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
