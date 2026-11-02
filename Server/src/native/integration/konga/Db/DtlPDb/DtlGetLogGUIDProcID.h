/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_GETLOGGUIDPROCID_H
#define KONGA_DTL_CRPDB_GETLOGGUIDPROCID_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/GetLogGUIDProcID.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::GetLogGUIDProcIDRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::GetLogGUIDProcIDRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<PDb::GetLogGUIDProcIDRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::GetLogGUIDProcIDRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<PDb::GetLogGUIDProcIDRow>
{
public:
	void operator()(BoundIOs &cols, PDb::GetLogGUIDProcIDRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
	}
};


}                     



namespace dtl_pdb {

class DtlGetLogGUIDProcIDDeleteBinder : public CustomBinder<PDb::GetLogGUIDProcIDRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::GetLogGUIDProcIDRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
	}
};

class DtlGetLogGUIDProcID : public PDb::GetLogGUIDProcID {

public:
	DtlGetLogGUIDProcID(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlGetLogGUIDProcID() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::GetLogGUIDProcID::Edit();
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
	DtlGetLogGUIDProcIDDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::GetLogGUIDProcID, PDb::GetLogGUIDProcIDRow> m_dtlRecSet;

};


}                         


#endif

