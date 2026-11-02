/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_TRANSFERTAB_H
#define KONGA_DTL_CRPDB_TRANSFERTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/TransferTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::TransferTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::TransferTabRow &r)
	{
		r.IsTransferIdNull = cols["TransferId"].IsNull();
		r.IsProtocolNull = cols["Protocol"].IsNull();
		r.IsRemoteServerNameNull = cols["RemoteServerName"].IsNull();
		r.IsRemotePortNull = cols["RemotePort"].IsNull();
		r.IsUserNameNull = cols["UserName"].IsNull();
		r.IsPasswordNull = cols["Password"].IsNull();
		r.IsLocalPathNull = cols["LocalPath"].IsNull();
		r.IsRemotePathNull = cols["RemotePath"].IsNull();
		r.IsProxyServerNameNull = cols["ProxyServerName"].IsNull();
		r.IsTimeOutSecondsNull = cols["TimeOutSeconds"].IsNull();
		r.IsLeaveOnServerNull = cols["LeaveOnServer"].IsNull();
		r.IsDownloadConditionNull = cols["DownloadCondition"].IsNull();
		r.IsCryptoInfoIdNull = cols["CryptoInfoId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::TransferTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::TransferTabRow &r)
	{
		if( r.IsTransferIdNull )
			cols["TransferId"].SetNull();
		else
			cols["TransferId"].ClearNull();
		if( r.IsProtocolNull )
			cols["Protocol"].SetNull();
		else
			cols["Protocol"].ClearNull();
		if( r.IsRemoteServerNameNull )
			cols["RemoteServerName"].SetNull();
		else
			cols["RemoteServerName"].ClearNull();
		if( r.IsRemotePortNull )
			cols["RemotePort"].SetNull();
		else
			cols["RemotePort"].ClearNull();
		if( r.IsUserNameNull )
			cols["UserName"].SetNull();
		else
			cols["UserName"].ClearNull();
		if( r.IsPasswordNull )
			cols["Password"].SetNull();
		else
			cols["Password"].ClearNull();
		if( r.IsLocalPathNull )
			cols["LocalPath"].SetNull();
		else
			cols["LocalPath"].ClearNull();
		if( r.IsRemotePathNull )
			cols["RemotePath"].SetNull();
		else
			cols["RemotePath"].ClearNull();
		if( r.IsProxyServerNameNull )
			cols["ProxyServerName"].SetNull();
		else
			cols["ProxyServerName"].ClearNull();
		if( r.IsTimeOutSecondsNull )
			cols["TimeOutSeconds"].SetNull();
		else
			cols["TimeOutSeconds"].ClearNull();
		if( r.IsLeaveOnServerNull )
			cols["LeaveOnServer"].SetNull();
		else
			cols["LeaveOnServer"].ClearNull();
		if( r.IsDownloadConditionNull )
			cols["DownloadCondition"].SetNull();
		else
			cols["DownloadCondition"].ClearNull();
		if( r.IsCryptoInfoIdNull )
			cols["CryptoInfoId"].SetNull();
		else
			cols["CryptoInfoId"].ClearNull();
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

template<> class DefaultBCA<PDb::TransferTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::TransferTabRow &rowbuf)
	{
		cols["TransferId"] == rowbuf.field_TransferId;
		cols["Protocol"] == rowbuf.field_Protocol;
		cols["RemoteServerName"] == rowbuf.field_RemoteServerName;
		cols["RemotePort"] == rowbuf.field_RemotePort;
		cols["UserName"] == rowbuf.field_UserName;
		cols["Password"] == rowbuf.field_Password;
		cols["LocalPath"] == rowbuf.field_LocalPath;
		cols["RemotePath"] == rowbuf.field_RemotePath;
		cols["ProxyServerName"] == rowbuf.field_ProxyServerName;
		cols["TimeOutSeconds"] == rowbuf.field_TimeOutSeconds;
		cols["LeaveOnServer"] == rowbuf.field_LeaveOnServer;
		cols["DownloadCondition"] == rowbuf.field_DownloadCondition;
		cols["CryptoInfoId"] == rowbuf.field_CryptoInfoId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlTransferTabDeleteBinder : public CustomBinder<PDb::TransferTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::TransferTabRow& rowbuf = *m_pRow;
		cols["TransferId"] == rowbuf.field_TransferId;
	}
};

class DtlTransferTab : public PDb::TransferTab {

public:
	DtlTransferTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTransferTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::TransferTab::Edit();
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
	DtlTransferTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::TransferTab, PDb::TransferTabRow> m_dtlRecSet;

};


}                         


#endif

