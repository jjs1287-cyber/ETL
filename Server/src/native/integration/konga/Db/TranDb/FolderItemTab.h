/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_FOLDERITEMTAB_H
#define KONGA_DB_CRTRANDB_FOLDERITEMTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class FolderItemTabRow
{
public:
	FolderItemTabRow() : 
			field_FolderItem_ID(0),
			IsFolderItem_IDNull(true),
			IsFolderItem_IDPrimaryKey(true),
			IsFolderItem_IDDirty(false),
			field_ParentFolder_ID(0),
			IsParentFolder_IDNull(true),
			IsParentFolder_IDPrimaryKey(false),
			IsParentFolder_IDDirty(false),
			field_EntityType_ID(0),
			IsEntityType_IDNull(true),
			IsEntityType_IDPrimaryKey(false),
			IsEntityType_IDDirty(false),
			field_Item_ID(0),
			IsItem_IDNull(true),
			IsItem_IDPrimaryKey(false),
			IsItem_IDDirty(false)
	{
	}

	void Copy(const FolderItemTabRow& r)
	{
		field_FolderItem_ID = r.field_FolderItem_ID;
		IsFolderItem_IDNull = r.IsFolderItem_IDNull;
		IsFolderItem_IDDirty = r.IsFolderItem_IDDirty;

		field_ParentFolder_ID = r.field_ParentFolder_ID;
		IsParentFolder_IDNull = r.IsParentFolder_IDNull;
		IsParentFolder_IDDirty = r.IsParentFolder_IDDirty;

		field_EntityType_ID = r.field_EntityType_ID;
		IsEntityType_IDNull = r.IsEntityType_IDNull;
		IsEntityType_IDDirty = r.IsEntityType_IDDirty;

		field_Item_ID = r.field_Item_ID;
		IsItem_IDNull = r.IsItem_IDNull;
		IsItem_IDDirty = r.IsItem_IDDirty;

	}

	void CopyPKs(const FolderItemTabRow& r)
	{
		field_FolderItem_ID = r.field_FolderItem_ID;
		IsFolderItem_IDNull = r.IsFolderItem_IDNull;
		IsFolderItem_IDDirty = r.IsFolderItem_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsFolderItem_IDDirty = b;
		IsParentFolder_IDDirty = b;
		IsEntityType_IDDirty = b;
		IsItem_IDDirty = b;
	}

	bool IsDirty() const
	{
		return IsFolderItem_IDDirty ||
			IsParentFolder_IDDirty ||
			IsEntityType_IDDirty ||
			IsItem_IDDirty;
	}

	int field_FolderItem_ID;
	bool IsFolderItem_IDNull;
	bool IsFolderItem_IDDirty;

	int field_ParentFolder_ID;
	bool IsParentFolder_IDNull;
	bool IsParentFolder_IDDirty;

	int field_EntityType_ID;
	bool IsEntityType_IDNull;
	bool IsEntityType_IDDirty;

	int field_Item_ID;
	bool IsItem_IDNull;
	bool IsItem_IDDirty;


private:
	bool IsFolderItem_IDPrimaryKey;
	bool IsParentFolder_IDPrimaryKey;
	bool IsEntityType_IDPrimaryKey;
	bool IsItem_IDPrimaryKey;

};


class FolderItemTab : public konga::DbRecordSet {
public:
	FolderItemTab() {}

	static FolderItemTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "FolderItemTab";
	}

	void DoFieldExchange(const FolderItemTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetFolderItem_ID() {
		if( m_row.IsFolderItem_IDNull )
			throw konga::DbNullException("FolderItem_ID");
		return m_row.field_FolderItem_ID;
	}
	void SetFolderItem_ID(int i) {
		m_row.field_FolderItem_ID = i;
		m_row.IsFolderItem_IDNull = false;
		m_row.IsFolderItem_IDDirty = true;
	}
	bool IsFolderItem_IDNull() const {
		return m_row.IsFolderItem_IDNull;
	}
	void SetFolderItem_IDToNull() {
		m_row.IsFolderItem_IDNull = true;
		m_row.IsFolderItem_IDDirty = true;
	}

	int GetParentFolder_ID() {
		if( m_row.IsParentFolder_IDNull )
			throw konga::DbNullException("ParentFolder_ID");
		return m_row.field_ParentFolder_ID;
	}
	void SetParentFolder_ID(int i) {
		m_row.field_ParentFolder_ID = i;
		m_row.IsParentFolder_IDNull = false;
		m_row.IsParentFolder_IDDirty = true;
	}
	bool IsParentFolder_IDNull() const {
		return m_row.IsParentFolder_IDNull;
	}
	void SetParentFolder_IDToNull() {
		m_row.IsParentFolder_IDNull = true;
		m_row.IsParentFolder_IDDirty = true;
	}

	int GetEntityType_ID() {
		if( m_row.IsEntityType_IDNull )
			throw konga::DbNullException("EntityType_ID");
		return m_row.field_EntityType_ID;
	}
	void SetEntityType_ID(int i) {
		m_row.field_EntityType_ID = i;
		m_row.IsEntityType_IDNull = false;
		m_row.IsEntityType_IDDirty = true;
	}
	bool IsEntityType_IDNull() const {
		return m_row.IsEntityType_IDNull;
	}
	void SetEntityType_IDToNull() {
		m_row.IsEntityType_IDNull = true;
		m_row.IsEntityType_IDDirty = true;
	}

	int GetItem_ID() {
		if( m_row.IsItem_IDNull )
			throw konga::DbNullException("Item_ID");
		return m_row.field_Item_ID;
	}
	void SetItem_ID(int i) {
		m_row.field_Item_ID = i;
		m_row.IsItem_IDNull = false;
		m_row.IsItem_IDDirty = true;
	}
	bool IsItem_IDNull() const {
		return m_row.IsItem_IDNull;
	}
	void SetItem_IDToNull() {
		m_row.IsItem_IDNull = true;
		m_row.IsItem_IDDirty = true;
	}

protected:
	FolderItemTabRow m_row;

private:
	bool IsFolderItem_IDDirty() const {
		return m_row.IsFolderItem_IDDirty;
	};
	bool IsParentFolder_IDDirty() const {
		return m_row.IsParentFolder_IDDirty;
	};
	bool IsEntityType_IDDirty() const {
		return m_row.IsEntityType_IDDirty;
	};
	bool IsItem_IDDirty() const {
		return m_row.IsItem_IDDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
