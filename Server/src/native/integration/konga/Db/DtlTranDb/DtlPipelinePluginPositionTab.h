/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_TRANDB_PIPELINEPLUGINPOSITIONTab_H
#define KONGA_DTL_TRANDB_PIPELINEPLUGINPOSITIONTab_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/PipelinePluginPositionTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::PipelinePluginPositionTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::PipelinePluginPositionTabRow &r)
	{
		r.IsPipelinePluginPositionIdNull = cols["PipelinePluginPositionId"].IsNull();
		r.IsPluginIdNull = cols["PluginId"].IsNull();
		r.IsPipelinePluginInstanceIdNull = cols["PipelinePluginInstanceId"].IsNull();
		r.IsPipelineEntityTypeIdNull = cols["PipelineEntityTypeId"].IsNull();
		r.IsRelativePositionIdNull = cols["RelativePositionId"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::PipelinePluginPositionTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::PipelinePluginPositionTabRow &r)
	{
		if( r.IsPipelinePluginPositionIdNull )
			cols["PipelinePluginPositionId"].SetNull();
		else
			cols["PipelinePluginPositionId"].ClearNull();
		if( r.IsPluginIdNull )
			cols["PluginId"].SetNull();
		else
			cols["PluginId"].ClearNull();
		if( r.IsPipelinePluginInstanceIdNull )
			cols["PipelinePluginInstanceId"].SetNull();
		else
			cols["PipelinePluginInstanceId"].ClearNull();
		if( r.IsPipelineEntityTypeIdNull )
			cols["PipelineEntityTypeId"].SetNull();
		else
			cols["PipelineEntityTypeId"].ClearNull();
		if( r.IsRelativePositionIdNull )
			cols["RelativePositionId"].SetNull();
		else
			cols["RelativePositionId"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::PipelinePluginPositionTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::PipelinePluginPositionTabRow &rowbuf)
	{
		cols["PipelinePluginPositionId"] == rowbuf.Field_PipelinePluginPositionId;
		cols["PluginId"] == rowbuf.Field_PluginId;
		cols["PipelinePluginInstanceId"] == rowbuf.Field_PipelinePluginInstanceId;
		cols["PipelineEntityTypeId"] == rowbuf.Field_PipelineEntityTypeId;
		cols["RelativePositionId"] == rowbuf.Field_RelativePositionId;
	}
};


}                     



namespace dtl_trandb {

class DtlPipelinePluginPositionTabDeleteBinder : public CustomBinder<TranDb::PipelinePluginPositionTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::PipelinePluginPositionTabRow& rowbuf = *m_pRow;
		cols["PipelinePluginPositionId"] == rowbuf.Field_PipelinePluginPositionId;
	}
};

class DtlPipelinePluginPositionTab : public TranDb::PipelinePluginPositionTab {

public:
	DtlPipelinePluginPositionTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlPipelinePluginPositionTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::PipelinePluginPositionTab::Edit();
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
	DtlPipelinePluginPositionTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::PipelinePluginPositionTab, TranDb::PipelinePluginPositionTabRow> m_dtlRecSet;

};


}                            


#endif

