/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_PROJECTOPERATIONSVIEW_H
#define KONGA_DB_CRTRANDB_PROJECTOPERATIONSVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class ProjectOperationsViewRow
{
public:
	ProjectOperationsViewRow() : 
			field_interchangeproject_id(0),
			Isinterchangeproject_idNull(true),
			Isinterchangeproject_idPrimaryKey(false),
			Isinterchangeproject_idDirty(false),
			field_Operation_ID(0),
			IsOperation_IDNull(true),
			IsOperation_IDPrimaryKey(false),
			IsOperation_IDDirty(false),
			IsOperation_NameNull(true),
			IsOperation_NamePrimaryKey(false),
			IsOperation_NameDirty(false)
	{
	}

	void Copy(const ProjectOperationsViewRow& r)
	{
		field_interchangeproject_id = r.field_interchangeproject_id;
		Isinterchangeproject_idNull = r.Isinterchangeproject_idNull;
		Isinterchangeproject_idDirty = r.Isinterchangeproject_idDirty;

		field_Operation_ID = r.field_Operation_ID;
		IsOperation_IDNull = r.IsOperation_IDNull;
		IsOperation_IDDirty = r.IsOperation_IDDirty;

		field_Operation_Name = r.field_Operation_Name;
		IsOperation_NameNull = r.IsOperation_NameNull;
		IsOperation_NameDirty = r.IsOperation_NameDirty;

	}

	void CopyPKs(const ProjectOperationsViewRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		Isinterchangeproject_idDirty = b;
		IsOperation_IDDirty = b;
		IsOperation_NameDirty = b;
	}

	bool IsDirty() const
	{
		return Isinterchangeproject_idDirty ||
			IsOperation_IDDirty ||
			IsOperation_NameDirty;
	}

	int field_interchangeproject_id;
	bool Isinterchangeproject_idNull;
	bool Isinterchangeproject_idDirty;

	int field_Operation_ID;
	bool IsOperation_IDNull;
	bool IsOperation_IDDirty;

	std::string field_Operation_Name;
	bool IsOperation_NameNull;
	bool IsOperation_NameDirty;


private:
	bool Isinterchangeproject_idPrimaryKey;
	bool IsOperation_IDPrimaryKey;
	bool IsOperation_NamePrimaryKey;

};


class ProjectOperationsView : public konga::DbRecordSet {
public:
	ProjectOperationsView() {}

	static ProjectOperationsView* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ProjectOperationsView";
	}

	void DoFieldExchange(const ProjectOperationsViewRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int Getinterchangeproject_id() {
		if( m_row.Isinterchangeproject_idNull )
			throw konga::DbNullException("interchangeproject_id");
		return m_row.field_interchangeproject_id;
	}
	void Setinterchangeproject_id(int i) {
		m_row.field_interchangeproject_id = i;
		m_row.Isinterchangeproject_idNull = false;
		m_row.Isinterchangeproject_idDirty = true;
	}
	bool Isinterchangeproject_idNull() const {
		return m_row.Isinterchangeproject_idNull;
	}
	void Setinterchangeproject_idToNull() {
		m_row.Isinterchangeproject_idNull = true;
		m_row.Isinterchangeproject_idDirty = true;
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

	std::string GetOperation_Name() {
		if( m_row.IsOperation_NameNull )
			throw konga::DbNullException("Operation_Name");
		return m_row.field_Operation_Name;
	}
	void SetOperation_Name(const std::string& st) {
		m_row.field_Operation_Name = st;
		m_row.IsOperation_NameNull = false;
		m_row.IsOperation_NameDirty = true;
	}
	bool IsOperation_NameNull() const {
		return m_row.IsOperation_NameNull;
	}
	void SetOperation_NameToNull() {
		m_row.field_Operation_Name = "";
		m_row.IsOperation_NameNull = true;
		m_row.IsOperation_NameDirty = true;
	}

protected:
	ProjectOperationsViewRow m_row;

private:
	bool Isinterchangeproject_idDirty() const {
		return m_row.Isinterchangeproject_idDirty;
	};
	bool IsOperation_IDDirty() const {
		return m_row.IsOperation_IDDirty;
	};
	bool IsOperation_NameDirty() const {
		return m_row.IsOperation_NameDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
