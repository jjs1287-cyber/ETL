#ifndef KONGA_DTL_TRANDB_JmsProvidersTab_H
#define KONGA_DTL_TRANDB_JmsProvidersTab_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/JmsProvidersTab.h>

  
                               
                                    
                         
                         
  

namespace dtl {

template<> class DefaultSelValidate<TranDb::JmsProvidersTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::JmsProvidersTabRow &r)
	{
		r.IsProviderIdNull = cols["ProviderId"].IsNull();
		r.IsProviderNameNull = cols["ProviderName"].IsNull();
		r.IsClasspathNull = cols["Classpath"].IsNull();
		r.IsProviderDescriptionNull = cols["ProviderDescription"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::JmsProvidersTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::JmsProvidersTabRow &r)
	{
		if( r.IsProviderIdNull )
			cols["ProviderId"].SetNull();
		else
			cols["ProviderId"].ClearNull();
		if( r.IsProviderNameNull )
			cols["ProviderName"].SetNull();
		else
			cols["ProviderName"].ClearNull();
		if( r.IsClasspathNull )
			cols["Classpath"].SetNull();
		else
			cols["Classpath"].ClearNull();
		if( r.IsProviderDescriptionNull )
			cols["ProviderDescription"].SetNull();
		else
			cols["ProviderDescription"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::JmsProvidersTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::JmsProvidersTabRow &rowbuf)
	{
		cols["ProviderId"] == rowbuf.field_ProviderId;
		cols["ProviderName"] == rowbuf.field_ProviderName;
		cols["Classpath"] == rowbuf.field_Classpath;
		cols["ProviderDescription"] == rowbuf.field_ProviderDescription;
	}
};


}                     



namespace dtl_trandb {

class DtlJmsProvidersTabDeleteBinder : public CustomBinder<TranDb::JmsProvidersTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::JmsProvidersTabRow& rowbuf = *m_pRow;
		cols["ProviderId"] == rowbuf.field_ProviderId;
	}
};

class DtlJmsProvidersTab : public TranDb::JmsProvidersTab {

public:
	DtlJmsProvidersTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlJmsProvidersTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::JmsProvidersTab::Edit();
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
	DtlJmsProvidersTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::JmsProvidersTab, TranDb::JmsProvidersTabRow> m_dtlRecSet;

};


}                            


#endif

