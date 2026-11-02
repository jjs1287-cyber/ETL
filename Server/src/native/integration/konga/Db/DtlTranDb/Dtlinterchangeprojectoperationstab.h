/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_INTERCHANGEPROJECTOPERATIONSTAB_H
#define KONGA_DTL_CRTRANDB_INTERCHANGEPROJECTOPERATIONSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/interchangeprojectoperationstab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::interchangeprojectoperationstabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::interchangeprojectoperationstabRow &r)
	{
		r.Isinterchangeproject_idNull = cols["interchangeproject_id"].IsNull();
		r.Isitem_idNull = cols["item_id"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::interchangeprojectoperationstabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::interchangeprojectoperationstabRow &r)
	{
		if( r.Isinterchangeproject_idNull )
			cols["interchangeproject_id"].SetNull();
		else
			cols["interchangeproject_id"].ClearNull();
		if( r.Isitem_idNull )
			cols["item_id"].SetNull();
		else
			cols["item_id"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::interchangeprojectoperationstabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::interchangeprojectoperationstabRow &rowbuf)
	{
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["item_id"] == rowbuf.field_item_id;
	}
};


}                     



namespace dtl_trandb {

class DtlinterchangeprojectoperationstabDeleteBinder : public CustomBinder<TranDb::interchangeprojectoperationstabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::interchangeprojectoperationstabRow& rowbuf = *m_pRow;
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["item_id"] == rowbuf.field_item_id;
	}
};

class Dtlinterchangeprojectoperationstab : public TranDb::interchangeprojectoperationstab {

public:
	Dtlinterchangeprojectoperationstab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~Dtlinterchangeprojectoperationstab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::interchangeprojectoperationstab::Edit();
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
	DtlinterchangeprojectoperationstabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::interchangeprojectoperationstab, TranDb::interchangeprojectoperationstabRow> m_dtlRecSet;

};


}                            


#endif

