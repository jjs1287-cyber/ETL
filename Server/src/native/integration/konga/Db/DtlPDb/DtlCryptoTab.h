/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_CRYPTOTAB_H
#define KONGA_DTL_CRPDB_CRYPTOTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/CryptoTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::CryptoTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::CryptoTabRow &r)
	{
		r.IsCryptoInfoIdNull = cols["CryptoInfoId"].IsNull();
		r.IsCryptoMethodNull = cols["CryptoMethod"].IsNull();
		r.IsEncryptionAlgoritmNull = cols["EncryptionAlgoritm"].IsNull();
		r.IsSigningAlgoritmNull = cols["SigningAlgoritm"].IsNull();
		r.IsCertificateIdNull = cols["CertificateId"].IsNull();
		r.IsPasswordNull = cols["Password"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::CryptoTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::CryptoTabRow &r)
	{
		if( r.IsCryptoInfoIdNull )
			cols["CryptoInfoId"].SetNull();
		else
			cols["CryptoInfoId"].ClearNull();
		if( r.IsCryptoMethodNull )
			cols["CryptoMethod"].SetNull();
		else
			cols["CryptoMethod"].ClearNull();
		if( r.IsEncryptionAlgoritmNull )
			cols["EncryptionAlgoritm"].SetNull();
		else
			cols["EncryptionAlgoritm"].ClearNull();
		if( r.IsSigningAlgoritmNull )
			cols["SigningAlgoritm"].SetNull();
		else
			cols["SigningAlgoritm"].ClearNull();
		if( r.IsCertificateIdNull )
			cols["CertificateId"].SetNull();
		else
			cols["CertificateId"].ClearNull();
		if( r.IsPasswordNull )
			cols["Password"].SetNull();
		else
			cols["Password"].ClearNull();
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

template<> class DefaultBCA<PDb::CryptoTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::CryptoTabRow &rowbuf)
	{
		cols["CryptoInfoId"] == rowbuf.field_CryptoInfoId;
		cols["CryptoMethod"] == rowbuf.field_CryptoMethod;
		cols["EncryptionAlgoritm"] == rowbuf.field_EncryptionAlgoritm;
		cols["SigningAlgoritm"] == rowbuf.field_SigningAlgoritm;
		cols["CertificateId"] == rowbuf.field_CertificateId;
		cols["Password"] == rowbuf.field_Password;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlCryptoTabDeleteBinder : public CustomBinder<PDb::CryptoTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::CryptoTabRow& rowbuf = *m_pRow;
		cols["CryptoInfoId"] == rowbuf.field_CryptoInfoId;
	}
};

class DtlCryptoTab : public PDb::CryptoTab {

public:
	DtlCryptoTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlCryptoTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::CryptoTab::Edit();
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
	DtlCryptoTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::CryptoTab, PDb::CryptoTabRow> m_dtlRecSet;

};


}                         


#endif

