/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_XMLDTDCHILDRENVIEW_H
#define KONGA_DB_CRTRANDB_XMLDTDCHILDRENVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class XmlDtdChildrenViewRow
{
public:
	XmlDtdChildrenViewRow() : 
			field_XML_ID(0),
			IsXML_IDNull(true),
			IsXML_IDPrimaryKey(false),
			IsXML_IDDirty(false),
			field_Child_ID(0),
			IsChild_IDNull(true),
			IsChild_IDPrimaryKey(false),
			IsChild_IDDirty(false),
			IsXML_PathNull(true),
			IsXML_PathPrimaryKey(false),
			IsXML_PathDirty(false)
	{
	}

	void Copy(const XmlDtdChildrenViewRow& r)
	{
		field_XML_ID = r.field_XML_ID;
		IsXML_IDNull = r.IsXML_IDNull;
		IsXML_IDDirty = r.IsXML_IDDirty;

		field_Child_ID = r.field_Child_ID;
		IsChild_IDNull = r.IsChild_IDNull;
		IsChild_IDDirty = r.IsChild_IDDirty;

		field_XML_Path = r.field_XML_Path;
		IsXML_PathNull = r.IsXML_PathNull;
		IsXML_PathDirty = r.IsXML_PathDirty;

	}

	void CopyPKs(const XmlDtdChildrenViewRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		IsXML_IDDirty = b;
		IsChild_IDDirty = b;
		IsXML_PathDirty = b;
	}

	bool IsDirty() const
	{
		return IsXML_IDDirty ||
			IsChild_IDDirty ||
			IsXML_PathDirty;
	}

	int field_XML_ID;
	bool IsXML_IDNull;
	bool IsXML_IDDirty;

	int field_Child_ID;
	bool IsChild_IDNull;
	bool IsChild_IDDirty;

	std::string field_XML_Path;
	bool IsXML_PathNull;
	bool IsXML_PathDirty;


private:
	bool IsXML_IDPrimaryKey;
	bool IsChild_IDPrimaryKey;
	bool IsXML_PathPrimaryKey;

};


class XmlDtdChildrenView : public konga::DbRecordSet {
public:
	XmlDtdChildrenView() {}

	static XmlDtdChildrenView* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "XmlDtdChildrenView";
	}

	void DoFieldExchange(const XmlDtdChildrenViewRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	int GetChild_ID() {
		if( m_row.IsChild_IDNull )
			throw konga::DbNullException("Child_ID");
		return m_row.field_Child_ID;
	}
	void SetChild_ID(int i) {
		m_row.field_Child_ID = i;
		m_row.IsChild_IDNull = false;
		m_row.IsChild_IDDirty = true;
	}
	bool IsChild_IDNull() const {
		return m_row.IsChild_IDNull;
	}
	void SetChild_IDToNull() {
		m_row.IsChild_IDNull = true;
		m_row.IsChild_IDDirty = true;
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

protected:
	XmlDtdChildrenViewRow m_row;

private:
	bool IsXML_IDDirty() const {
		return m_row.IsXML_IDDirty;
	};
	bool IsChild_IDDirty() const {
		return m_row.IsChild_IDDirty;
	};
	bool IsXML_PathDirty() const {
		return m_row.IsXML_PathDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
