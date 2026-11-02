/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_INTERCHANGEPROJECTOPERATIONSTAB_H
#define KONGA_DB_CRTRANDB_INTERCHANGEPROJECTOPERATIONSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class interchangeprojectoperationstabRow
{
public:
	interchangeprojectoperationstabRow() : 
			field_interchangeproject_id(0),
			Isinterchangeproject_idNull(true),
			Isinterchangeproject_idPrimaryKey(false),
			Isinterchangeproject_idDirty(false),
			field_item_id(0),
			Isitem_idNull(true),
			Isitem_idPrimaryKey(false),
			Isitem_idDirty(false)
	{
	}

	void Copy(const interchangeprojectoperationstabRow& r)
	{
		field_interchangeproject_id = r.field_interchangeproject_id;
		Isinterchangeproject_idNull = r.Isinterchangeproject_idNull;
		Isinterchangeproject_idDirty = r.Isinterchangeproject_idDirty;

		field_item_id = r.field_item_id;
		Isitem_idNull = r.Isitem_idNull;
		Isitem_idDirty = r.Isitem_idDirty;

	}

	void CopyPKs(const interchangeprojectoperationstabRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		Isinterchangeproject_idDirty = b;
		Isitem_idDirty = b;
	}

	bool IsDirty() const
	{
		return Isinterchangeproject_idDirty ||
			Isitem_idDirty;
	}

	int field_interchangeproject_id;
	bool Isinterchangeproject_idNull;
	bool Isinterchangeproject_idDirty;

	int field_item_id;
	bool Isitem_idNull;
	bool Isitem_idDirty;


private:
	bool Isinterchangeproject_idPrimaryKey;
	bool Isitem_idPrimaryKey;

};


class interchangeprojectoperationstab : public konga::DbRecordSet {
public:
	interchangeprojectoperationstab() {}

	static interchangeprojectoperationstab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "interchangeprojectoperationstab";
	}

	void DoFieldExchange(const interchangeprojectoperationstabRow& row) {
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

	int Getitem_id() {
		if( m_row.Isitem_idNull )
			throw konga::DbNullException("item_id");
		return m_row.field_item_id;
	}
	void Setitem_id(int i) {
		m_row.field_item_id = i;
		m_row.Isitem_idNull = false;
		m_row.Isitem_idDirty = true;
	}
	bool Isitem_idNull() const {
		return m_row.Isitem_idNull;
	}
	void Setitem_idToNull() {
		m_row.Isitem_idNull = true;
		m_row.Isitem_idDirty = true;
	}

protected:
	interchangeprojectoperationstabRow m_row;

private:
	bool Isinterchangeproject_idDirty() const {
		return m_row.Isinterchangeproject_idDirty;
	};
	bool Isitem_idDirty() const {
		return m_row.Isitem_idDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
