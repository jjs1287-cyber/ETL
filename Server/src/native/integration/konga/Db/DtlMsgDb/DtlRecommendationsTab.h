/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_RECOMMENDATIONSTAB_H
#define KONGA_DTL_CRMSGDB_RECOMMENDATIONSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/RecommendationsTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::RecommendationsTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::RecommendationsTabRow &r)
	{
		r.IsEngineErrIdNull = cols["EngineErrId"].IsNull();
		r.IsEngineErrTextNull = cols["EngineErrText"].IsNull();
		r.IsRecommendationNull = cols["Recommendation"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::RecommendationsTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::RecommendationsTabRow &r)
	{
		if( r.IsEngineErrIdNull )
			cols["EngineErrId"].SetNull();
		else
			cols["EngineErrId"].ClearNull();
		if( r.IsEngineErrTextNull )
			cols["EngineErrText"].SetNull();
		else
			cols["EngineErrText"].ClearNull();
		if( r.IsRecommendationNull )
			cols["Recommendation"].SetNull();
		else
			cols["Recommendation"].ClearNull();
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

template<> class DefaultBCA<MsgDb::RecommendationsTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::RecommendationsTabRow &rowbuf)
	{
		cols["EngineErrId"] == rowbuf.field_EngineErrId;
		cols["EngineErrText"] == rowbuf.field_EngineErrText;
		cols["Recommendation"] == rowbuf.field_Recommendation;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {

class DtlRecommendationsTabDeleteBinder : public CustomBinder<MsgDb::RecommendationsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::RecommendationsTabRow& rowbuf = *m_pRow;
		cols["EngineErrId"] == rowbuf.field_EngineErrId;
	}
};

class DtlRecommendationsTab : public MsgDb::RecommendationsTab {

public:
	DtlRecommendationsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlRecommendationsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::RecommendationsTab::Edit();
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
	DtlRecommendationsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::RecommendationsTab, MsgDb::RecommendationsTabRow> m_dtlRecSet;

};


}                           


#endif

