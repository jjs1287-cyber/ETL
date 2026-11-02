/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_FOLDERITEMTAB_H
#define KONGA_DTL_CRTRANDB_FOLDERITEMTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/FolderItemTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::FolderItemTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::FolderItemTabRow &r)
	{
		r.IsFolderItem_IDNull = cols["FolderItem_ID"].IsNull();
		r.IsParentFolder_IDNull = cols["ParentFolder_ID"].IsNull();
		r.IsEntityType_IDNull = cols["EntityType_ID"].IsNull();
		r.IsItem_IDNull = cols["Item_ID"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::FolderItemTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::FolderItemTabRow &r)
	{
		if( r.IsParentFolder_IDNull )
			cols["ParentFolder_ID"].SetNull();
		else
			cols["ParentFolder_ID"].ClearNull();
		if( r.IsEntityType_IDNull )
			cols["EntityType_ID"].SetNull();
		else
			cols["EntityType_ID"].ClearNull();
		if( r.IsItem_IDNull )
			cols["Item_ID"].SetNull();
		else
			cols["Item_ID"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::FolderItemTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::FolderItemTabRow &rowbuf)
	{
		cols["FolderItem_ID"] == rowbuf.field_FolderItem_ID;
		cols["ParentFolder_ID"] == rowbuf.field_ParentFolder_ID;
		cols["EntityType_ID"] == rowbuf.field_EntityType_ID;
		cols["Item_ID"] == rowbuf.field_Item_ID;
	}
};


}                     



namespace dtl_trandb {


class DtlFolderItemTabInsertBinder : public CustomBinder<TranDb::FolderItemTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::FolderItemTabRow& rowbuf = *m_pRow;
		cols["ParentFolder_ID"] << rowbuf.field_ParentFolder_ID;
		cols["EntityType_ID"] << rowbuf.field_EntityType_ID;
		cols["Item_ID"] << rowbuf.field_Item_ID;
	}
};

class DtlFolderItemTabDeleteBinder : public CustomBinder<TranDb::FolderItemTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::FolderItemTabRow& rowbuf = *m_pRow;
		cols["FolderItem_ID"] == rowbuf.field_FolderItem_ID;
	}
};

class DtlFolderItemTab : public TranDb::FolderItemTab {

public:
	DtlFolderItemTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlFolderItemTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::FolderItemTab::Edit();
		m_dtlRecSet.Edit();
	}
	void AddNew() {
		m_dtlRecSet.AddNew(&m_insertBinder);
	}
	int Delete() {
		return m_dtlRecSet.Delete();
	}
	bool IsEOF() {
		return m_dtlRecSet.IsEOF();
	}
	bool Next() {
		return m_dtlRecSet.Next();
	}
	void Close() {
		m_dtlRecSet.Close();
	}
	void SetWhereClause(const std::string& stWhereClause) {
		m_dtlRecSet.SetWhereClause(stWhereClause);
	}
	std::string GetWhereClause() const {
		return m_dtlRecSet.GetWhereClause();
	}

	std::string GetLastError() const {
		return m_dtlRecSet.GetLastError();
	}
	long GetLastErrorNumber() const {
		return m_dtlRecSet.GetLastErrorNumber();
	}

private:
	DtlFolderItemTabInsertBinder m_insertBinder;
	DtlFolderItemTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::FolderItemTab, TranDb::FolderItemTabRow> m_dtlRecSet;

};


}                            


#endif

