/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_ARCHIVECRPDBFINAL_H
#define KONGA_DTL_CRPDB_ARCHIVECRPDBFINAL_H

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/ArchiveCRPDBFinal.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::ArchiveCRPDBFinalRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ArchiveCRPDBFinalRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsProcessNameNull = cols["ProcessName"].IsNull();
		r.IsDescriptionNull = cols["Description"].IsNull();
		r.IsErrorHandlerIdNull = cols["ErrorHandlerId"].IsNull();
		r.IsOwnerNull = cols["Owner"].IsNull();
		r.IsProcessTypeNull = cols["ProcessType"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<PDb::ArchiveCRPDBFinalRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ArchiveCRPDBFinalRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsProcessNameNull )
			cols["ProcessName"].SetNull();
		else
			cols["ProcessName"].ClearNull();
		if( r.IsDescriptionNull )
			cols["Description"].SetNull();
		else
			cols["Description"].ClearNull();
		if( r.IsErrorHandlerIdNull )
			cols["ErrorHandlerId"].SetNull();
		else
			cols["ErrorHandlerId"].ClearNull();
		if( r.IsOwnerNull )
			cols["Owner"].SetNull();
		else
			cols["Owner"].ClearNull();
		if( r.IsProcessTypeNull )
			cols["ProcessType"].SetNull();
		else
			cols["ProcessType"].ClearNull();
		if( r.IsInsertTimeNull )
			cols["InsertTime"].SetNull();
		else
			cols["InsertTime"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<PDb::ArchiveCRPDBFinalRow>
{
public:
	void operator()(BoundIOs &cols, PDb::ArchiveCRPDBFinalRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["ProcessName"] == rowbuf.field_ProcessName;
		cols["Description"] == rowbuf.field_Description;
		cols["ErrorHandlerId"] == rowbuf.field_ErrorHandlerId;
		cols["Owner"] == rowbuf.field_Owner;
		cols["ProcessType"] == rowbuf.field_ProcessType;
		cols["InsertTime"] == rowbuf.field_InsertTime;
	}
};


}                     


class DeleteBinder : public CustomBinder<PDb::ArchiveCRPDBFinalRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::ArchiveCRPDBFinalRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["ProcessName"] == rowbuf.field_ProcessName;
		cols["Description"] == rowbuf.field_Description;
		cols["ErrorHandlerId"] == rowbuf.field_ErrorHandlerId;
		cols["Owner"] == rowbuf.field_Owner;
		cols["ProcessType"] == rowbuf.field_ProcessType;
		cols["InsertTime"] == rowbuf.field_InsertTime;
	}
};

class DtlArchiveCRPDBFinal : public PDb::ArchiveCRPDBFinal {

public:
	DtlArchiveCRPDBFinal(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArchiveCRPDBFinal() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::ArchiveCRPDBFinal::Edit();
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
	DeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::ArchiveCRPDBFinal, PDb::ArchiveCRPDBFinalRow> m_dtlRecSet;

};

#endif

