/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_SOURCEDTDVIEW_H
#define KONGA_DB_CRTRANDB_SOURCEDTDVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class SourceDtdViewRow
{
public:
	SourceDtdViewRow() : 
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(false),
			IsTransformation_IDDirty(false),
			IsXML_PathNull(true),
			IsXML_PathPrimaryKey(false),
			IsXML_PathDirty(false),
			field_XML_ID(0),
			IsXML_IDNull(true),
			IsXML_IDPrimaryKey(false),
			IsXML_IDDirty(false)
	{
	}

	void Copy(const SourceDtdViewRow& r)
	{
		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_XML_Path = r.field_XML_Path;
		IsXML_PathNull = r.IsXML_PathNull;
		IsXML_PathDirty = r.IsXML_PathDirty;

		field_XML_ID = r.field_XML_ID;
		IsXML_IDNull = r.IsXML_IDNull;
		IsXML_IDDirty = r.IsXML_IDDirty;

	}

	void CopyPKs(const SourceDtdViewRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		IsTransformation_IDDirty = b;
		IsXML_PathDirty = b;
		IsXML_IDDirty = b;
	}

	bool IsDirty() const
	{
		return IsTransformation_IDDirty ||
			IsXML_PathDirty ||
			IsXML_IDDirty;
	}

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	std::string field_XML_Path;
	bool IsXML_PathNull;
	bool IsXML_PathDirty;

	int field_XML_ID;
	bool IsXML_IDNull;
	bool IsXML_IDDirty;


private:
	bool IsTransformation_IDPrimaryKey;
	bool IsXML_PathPrimaryKey;
	bool IsXML_IDPrimaryKey;

};


class SourceDtdView : public konga::DbRecordSet {
public:
	SourceDtdView() {}

	static SourceDtdView* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "SourceDtdView";
	}

	void DoFieldExchange(const SourceDtdViewRow& row) {
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

	std::string GetXML_Path() {
		if( m_row.IsXML_PathNull )
			throw konga::DbNullException("XML_Path");
		return m_row.field_XML_Path;
	}
	void SetXML_Path(const std::string& st) {
		m_row.field_XML_Path = st;
		m_row.IsXML_PathNull = false;
		m_row.IsXML_PathDirty = true;
	}
	bool IsXML_PathNull() const {
		return m_row.IsXML_PathNull;
	}
	void SetXML_PathToNull() {
		m_row.field_XML_Path = "";
		m_row.IsXML_PathNull = true;
		m_row.IsXML_PathDirty = true;
	}

	int GetXML_ID() {
		if( m_row.IsXML_IDNull )
			throw konga::DbNullException("XML_ID");
		return m_row.field_XML_ID;
	}
	void SetXML_ID(int i) {
		m_row.field_XML_ID = i;
		m_row.IsXML_IDNull = false;
		m_row.IsXML_IDDirty = true;
	}
	bool IsXML_IDNull() const {
		return m_row.IsXML_IDNull;
	}
	void SetXML_IDToNull() {
		m_row.IsXML_IDNull = true;
		m_row.IsXML_IDDirty = true;
	}

protected:
	SourceDtdViewRow m_row;

private:
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsXML_PathDirty() const {
		return m_row.IsXML_PathDirty;
	};
	bool IsXML_IDDirty() const {
		return m_row.IsXML_IDDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
