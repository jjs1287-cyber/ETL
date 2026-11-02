/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_TRANDB_TEMPLATE_SCRIPTSTAB_H
#define KONGA_DTL_TRANDB_TEMPLATE_SCRIPTSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/ScriptsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::ScriptsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::ScriptsTabRow &r)
	{
		r.IsScript_IDNull = cols["Script_ID"].IsNull();
		r.IsScript_NameNull = cols["Script_Name"].IsNull();
		r.IsScript_TextNull = cols["Script_Text"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::ScriptsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::ScriptsTabRow &r)
	{
		if( r.IsScript_IDNull )
			cols["Script_ID"].SetNull();
		else
			cols["Script_ID"].ClearNull();
		if( r.IsScript_NameNull )
			cols["Script_Name"].SetNull();
		else
			cols["Script_Name"].ClearNull();
		if( r.IsScript_TextNull )
			cols["Script_Text"].SetNull();
		else
			cols["Script_Text"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::ScriptsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::ScriptsTabRow &rowbuf)
	{
		cols["Script_ID"] == rowbuf.field_Script_ID;
		cols["Script_Name"] == rowbuf.field_Script_Name;
		cols["Script_Text"] == rowbuf.field_Script_Text;
	}
};

}                     

namespace dtl_trandb {

class DtlScriptsTabDeleteBinder : public CustomBinder<TranDb::ScriptsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::ScriptsTabRow& rowbuf = *m_pRow;
		cols["Script_ID"] == rowbuf.field_Script_ID;
	}
};

class DtlScriptsTab : public TranDb::ScriptsTab {

public:
	DtlScriptsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlScriptsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::ScriptsTab::Edit();
		m_dtlRecSet.Edit();
	}
	void AddNew() {
		m_dtlRecSet.AddNew();
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
	DtlScriptsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::ScriptsTab, TranDb::ScriptsTabRow> m_dtlRecSet;

};


}                            


#endif                                          

