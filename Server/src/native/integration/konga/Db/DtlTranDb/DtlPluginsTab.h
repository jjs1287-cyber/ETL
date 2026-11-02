/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_TRANDB_PluginsTab_H
#define KONGA_DTL_TRANDB_PluginsTab_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/PluginsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::PluginsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::PluginsTabRow &r)
	{
		r.IsPluginIdNull = cols["PluginId"].IsNull();
		r.IsPluginNameNull = cols["PluginName"].IsNull();
		r.IsPluginPathNull = cols["PluginPath"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::PluginsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::PluginsTabRow &r)
	{
		if( r.IsPluginIdNull )
			cols["PluginId"].SetNull();
		else
			cols["PluginId"].ClearNull();
		if( r.IsPluginNameNull )
			cols["PluginName"].SetNull();
		else
			cols["PluginName"].ClearNull();
		if( r.IsPluginPathNull )
			cols["PluginPath"].SetNull();
		else
			cols["PluginPath"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::PluginsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::PluginsTabRow &rowbuf)
	{
		cols["PluginId"] == rowbuf.Field_PluginId;
		cols["PluginName"] == rowbuf.Field_PluginName;
		cols["PluginPath"] == rowbuf.Field_PluginPath;
	}
};


}                     



namespace dtl_trandb {

class DtlPluginsTabDeleteBinder : public CustomBinder<TranDb::PluginsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::PluginsTabRow& rowbuf = *m_pRow;
		cols["PluginId"] == rowbuf.Field_PluginId;
	}
};

class DtlPluginsTab : public TranDb::PluginsTab {

public:
	DtlPluginsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlPluginsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::PluginsTab::Edit();
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
	DtlPluginsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::PluginsTab, TranDb::PluginsTabRow> m_dtlRecSet;

};


}                            


#endif

