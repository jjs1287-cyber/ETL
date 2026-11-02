/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_TRANSFORMATIONCHAINLINKSTAB_H
#define KONGA_DB_CRTRANDB_TRANSFORMATIONCHAINLINKSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class TransformationChainLinksTabRow
{
public:
	TransformationChainLinksTabRow() : 
			field_TransformationChainLink_ID(0),
			IsTransformationChainLink_IDNull(true),
			IsTransformationChainLink_IDPrimaryKey(true),
			IsTransformationChainLink_IDDirty(false),
			field_Operation_ID(0),
			IsOperation_IDNull(true),
			IsOperation_IDPrimaryKey(false),
			IsOperation_IDDirty(false),
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(false),
			IsTransformation_IDDirty(false),
			field_NextTransformation_ID(0),
			IsNextTransformation_IDNull(true),
			IsNextTransformation_IDPrimaryKey(false),
			IsNextTransformation_IDDirty(false)
	{
	}

	void Copy(const TransformationChainLinksTabRow& r)
	{
		field_TransformationChainLink_ID = r.field_TransformationChainLink_ID;
		IsTransformationChainLink_IDNull = r.IsTransformationChainLink_IDNull;
		IsTransformationChainLink_IDDirty = r.IsTransformationChainLink_IDDirty;

		field_Operation_ID = r.field_Operation_ID;
		IsOperation_IDNull = r.IsOperation_IDNull;
		IsOperation_IDDirty = r.IsOperation_IDDirty;

		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_NextTransformation_ID = r.field_NextTransformation_ID;
		IsNextTransformation_IDNull = r.IsNextTransformation_IDNull;
		IsNextTransformation_IDDirty = r.IsNextTransformation_IDDirty;

	}

	void CopyPKs(const TransformationChainLinksTabRow& r)
	{
		field_TransformationChainLink_ID = r.field_TransformationChainLink_ID;
		IsTransformationChainLink_IDNull = r.IsTransformationChainLink_IDNull;
		IsTransformationChainLink_IDDirty = r.IsTransformationChainLink_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsTransformationChainLink_IDDirty = b;
		IsOperation_IDDirty = b;
		IsTransformation_IDDirty = b;
		IsNextTransformation_IDDirty = b;
	}

	bool IsDirty() const
	{
		return IsTransformationChainLink_IDDirty ||
			IsOperation_IDDirty ||
			IsTransformation_IDDirty ||
			IsNextTransformation_IDDirty;
	}

	int field_TransformationChainLink_ID;
	bool IsTransformationChainLink_IDNull;
	bool IsTransformationChainLink_IDDirty;

	int field_Operation_ID;
	bool IsOperation_IDNull;
	bool IsOperation_IDDirty;

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	int field_NextTransformation_ID;
	bool IsNextTransformation_IDNull;
	bool IsNextTransformation_IDDirty;


private:
	bool IsTransformationChainLink_IDPrimaryKey;
	bool IsOperation_IDPrimaryKey;
	bool IsTransformation_IDPrimaryKey;
	bool IsNextTransformation_IDPrimaryKey;

};


class TransformationChainLinksTab : public konga::DbRecordSet {
public:
	TransformationChainLinksTab() {}

	static TransformationChainLinksTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TransformationChainLinksTab";
	}

	void DoFieldExchange(const TransformationChainLinksTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetTransformationChainLink_ID() {
		if( m_row.IsTransformationChainLink_IDNull )
			throw konga::DbNullException("TransformationChainLink_ID");
		return m_row.field_TransformationChainLink_ID;
	}
	void SetTransformationChainLink_ID(int i) {
		m_row.field_TransformationChainLink_ID = i;
		m_row.IsTransformationChainLink_IDNull = false;
		m_row.IsTransformationChainLink_IDDirty = true;
	}
	bool IsTransformationChainLink_IDNull() const {
		return m_row.IsTransformationChainLink_IDNull;
	}
	void SetTransformationChainLink_IDToNull() {
		m_row.IsTransformationChainLink_IDNull = true;
		m_row.IsTransformationChainLink_IDDirty = true;
	}

	int GetOperation_ID() {
		if( m_row.IsOperation_IDNull )
			throw konga::DbNullException("Operation_ID");
		return m_row.field_Operation_ID;
	}
	void SetOperation_ID(int i) {
		m_row.field_Operation_ID = i;
		m_row.IsOperation_IDNull = false;
		m_row.IsOperation_IDDirty = true;
	}
	bool IsOperation_IDNull() const {
		return m_row.IsOperation_IDNull;
	}
	void SetOperation_IDToNull() {
		m_row.IsOperation_IDNull = true;
		m_row.IsOperation_IDDirty = true;
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

	int GetNextTransformation_ID() {
		if( m_row.IsNextTransformation_IDNull )
			throw konga::DbNullException("NextTransformation_ID");
		return m_row.field_NextTransformation_ID;
	}
	void SetNextTransformation_ID(int i) {
		m_row.field_NextTransformation_ID = i;
		m_row.IsNextTransformation_IDNull = false;
		m_row.IsNextTransformation_IDDirty = true;
	}
	bool IsNextTransformation_IDNull() const {
		return m_row.IsNextTransformation_IDNull;
	}
	void SetNextTransformation_IDToNull() {
		m_row.IsNextTransformation_IDNull = true;
		m_row.IsNextTransformation_IDDirty = true;
	}

protected:
	TransformationChainLinksTabRow m_row;

private:
	bool IsTransformationChainLink_IDDirty() const {
		return m_row.IsTransformationChainLink_IDDirty;
	};
	bool IsOperation_IDDirty() const {
		return m_row.IsOperation_IDDirty;
	};
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsNextTransformation_IDDirty() const {
		return m_row.IsNextTransformation_IDDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
