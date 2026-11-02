/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_TRANDB_PipelinePluginInstanceTab_H
#define KONGA_DTL_TRANDB_PipelinePluginInstanceTab_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/PipelinePluginInstanceTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::PipelinePluginInstanceTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::PipelinePluginInstanceTabRow &r)
	{
		r.IsPipelinePluginInstanceIdNull = cols["PipelinePluginInstanceId"].IsNull();
		r.IsPluginIdNull = cols["PluginId"].IsNull();
		r.IsEntityTypeIdNull = cols["EntityTypeId"].IsNull();
		r.IsEntityIdNull = cols["EntityId"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::PipelinePluginInstanceTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::PipelinePluginInstanceTabRow &r)
	{
		if( r.IsPipelinePluginInstanceIdNull )
			cols["PipelinePluginInstanceId"].SetNull();
		else
			cols["PipelinePluginInstanceId"].ClearNull();
		if( r.IsPluginIdNull )
			cols["PluginId"].SetNull();
		else
			cols["PluginId"].ClearNull();
		if( r.IsEntityTypeIdNull )
			cols["EntityTypeId"].SetNull();
		else
			cols["EntityTypeId"].ClearNull();
		if( r.IsEntityIdNull )
			cols["EntityId"].SetNull();
		else
			cols["EntityId"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::PipelinePluginInstanceTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::PipelinePluginInstanceTabRow &rowbuf)
	{
		cols["PipelinePluginInstanceId"] == rowbuf.Field_PipelinePluginInstanceId;
		cols["PluginId"] == rowbuf.Field_PluginId;
		cols["EntityTypeId"] == rowbuf.Field_EntityTypeId;
		cols["EntityId"] == rowbuf.Field_EntityId;
	}
};


}                     



namespace dtl_trandb {

class DtlPipelinePluginInstanceTabDeleteBinder : public CustomBinder<TranDb::PipelinePluginInstanceTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::PipelinePluginInstanceTabRow& rowbuf = *m_pRow;
		cols["PipelinePluginInstanceId"] == rowbuf.Field_PipelinePluginInstanceId;
	}
};

class DtlPipelinePluginInstanceTab : public TranDb::PipelinePluginInstanceTab {

public:
	DtlPipelinePluginInstanceTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlPipelinePluginInstanceTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::PipelinePluginInstanceTab::Edit();
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
	DtlPipelinePluginInstanceTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::PipelinePluginInstanceTab, TranDb::PipelinePluginInstanceTabRow> m_dtlRecSet;

};


}                            


#endif

