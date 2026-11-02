/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_MULTISERVERSETUPTAB_H
#define KONGA_DTL_CRPDB_MULTISERVERSETUPTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/MultiServerSetupTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::MultiServerSetupTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::MultiServerSetupTabRow &r)
	{
		r.IsMachineNameNull = cols["MachineName"].IsNull();
		r.IsEnabledNull = cols["Enabled"].IsNull();
		r.IsCurrentRoleNull = cols["CurrentRole"].IsNull();
		r.IsCapabilityNull = cols["Capability"].IsNull();
		r.IsAssetTagNull = cols["AssetTag"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::MultiServerSetupTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::MultiServerSetupTabRow &r)
	{
		if( r.IsMachineNameNull )
			cols["MachineName"].SetNull();
		else
			cols["MachineName"].ClearNull();
		if( r.IsEnabledNull )
			cols["Enabled"].SetNull();
		else
			cols["Enabled"].ClearNull();
		if( r.IsCurrentRoleNull )
			cols["CurrentRole"].SetNull();
		else
			cols["CurrentRole"].ClearNull();
		if( r.IsCapabilityNull )
			cols["Capability"].SetNull();
		else
			cols["Capability"].ClearNull();
		if( r.IsAssetTagNull )
			cols["AssetTag"].SetNull();
		else
			cols["AssetTag"].ClearNull();
		if( r.IsInsertTimeNull )
			cols["InsertTime"].SetNull();
		else
			cols["InsertTime"].ClearNull();
		if( r.IsModTimeNull )
			cols["ModTime"].SetNull();
		else
			cols["ModTime"].ClearNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultBCA<PDb::MultiServerSetupTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::MultiServerSetupTabRow &rowbuf)
	{
		cols["MachineName"] == rowbuf.field_MachineName;
		cols["Enabled"] == rowbuf.field_Enabled;
		cols["CurrentRole"] == rowbuf.field_CurrentRole;
		cols["Capability"] == rowbuf.field_Capability;
		cols["AssetTag"] == rowbuf.field_AssetTag;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlMultiServerSetupTabDeleteBinder : public CustomBinder<PDb::MultiServerSetupTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::MultiServerSetupTabRow& rowbuf = *m_pRow;
		cols["AssetTag"] == rowbuf.field_AssetTag;
	}
};

class DtlMultiServerSetupTab : public PDb::MultiServerSetupTab {

public:
	DtlMultiServerSetupTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlMultiServerSetupTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::MultiServerSetupTab::Edit();
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
	DtlMultiServerSetupTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::MultiServerSetupTab, PDb::MultiServerSetupTabRow> m_dtlRecSet;

};


}                         


#endif

