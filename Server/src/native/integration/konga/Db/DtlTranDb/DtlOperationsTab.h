/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_OPERATIONSTAB_H
#define KONGA_DTL_CRTRANDB_OPERATIONSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/OperationsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::OperationsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::OperationsTabRow &r)
	{
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsOperation_NameNull = cols["Operation_Name"].IsNull();
		r.IsOperation_Type_IDNull = cols["Operation_Type_ID"].IsNull();
		r.IsProject_IDNull = cols["Project_ID"].IsNull();
		r.IsSource_IDNull = cols["Source_ID"].IsNull();
		r.IsTarget_IDNull = cols["Target_ID"].IsNull();
		r.IsSchedule_IDNull = cols["Schedule_ID"].IsNull();
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsSuccess_Operation_IDNull = cols["Success_Operation_ID"].IsNull();
		r.IsSuccess_Message_IDNull = cols["Success_Message_ID"].IsNull();
		r.IsSuccess_WorkflowNull = cols["Success_Workflow"].IsNull();
		r.IsFailure_Operation_IDNull = cols["Failure_Operation_ID"].IsNull();
		r.IsFailure_Message_IDNull = cols["Failure_Message_ID"].IsNull();
		r.IsFailure_WorkflowNull = cols["Failure_Workflow"].IsNull();
		r.IsUser_IDNull = cols["User_ID"].IsNull();
		r.IsDescriptionNull = cols["Description"].IsNull();
		r.IsTraceNull = cols["Trace"].IsNull();
		r.IsDIFfNull = cols["DIFf"].IsNull();
		r.IsIgnore_HistoryNull = cols["Ignore_History"].IsNull();
		r.IsEDI_997_AckNull = cols["EDI_997_Ack"].IsNull();
		r.IsIgnore_EDI_RulesNull = cols["Ignore_EDI_Rules"].IsNull();
		r.IsTPAGUIDNull = cols["TPAGUID"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::OperationsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::OperationsTabRow &r)
	{
		if( r.IsOperation_IDNull )
			cols["Operation_ID"].SetNull();
		else
			cols["Operation_ID"].ClearNull();
		if( r.IsOperation_NameNull )
			cols["Operation_Name"].SetNull();
		else
			cols["Operation_Name"].ClearNull();
		if( r.IsOperation_Type_IDNull )
			cols["Operation_Type_ID"].SetNull();
		else
			cols["Operation_Type_ID"].ClearNull();
		if( r.IsProject_IDNull )
			cols["Project_ID"].SetNull();
		else
			cols["Project_ID"].ClearNull();
		if( r.IsSource_IDNull )
			cols["Source_ID"].SetNull();
		else
			cols["Source_ID"].ClearNull();
		if( r.IsTarget_IDNull )
			cols["Target_ID"].SetNull();
		else
			cols["Target_ID"].ClearNull();
		if( r.IsSchedule_IDNull )
			cols["Schedule_ID"].SetNull();
		else
			cols["Schedule_ID"].ClearNull();
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsSuccess_Operation_IDNull )
			cols["Success_Operation_ID"].SetNull();
		else
			cols["Success_Operation_ID"].ClearNull();
		if( r.IsSuccess_Message_IDNull )
			cols["Success_Message_ID"].SetNull();
		else
			cols["Success_Message_ID"].ClearNull();
		if( r.IsSuccess_WorkflowNull )
			cols["Success_Workflow"].SetNull();
		else
			cols["Success_Workflow"].ClearNull();
		if( r.IsFailure_Operation_IDNull )
			cols["Failure_Operation_ID"].SetNull();
		else
			cols["Failure_Operation_ID"].ClearNull();
		if( r.IsFailure_Message_IDNull )
			cols["Failure_Message_ID"].SetNull();
		else
			cols["Failure_Message_ID"].ClearNull();
		if( r.IsFailure_WorkflowNull )
			cols["Failure_Workflow"].SetNull();
		else
			cols["Failure_Workflow"].ClearNull();
		if( r.IsUser_IDNull )
			cols["User_ID"].SetNull();
		else
			cols["User_ID"].ClearNull();
		if( r.IsDescriptionNull )
			cols["Description"].SetNull();
		else
			cols["Description"].ClearNull();
		if( r.IsTraceNull )
			cols["Trace"].SetNull();
		else
			cols["Trace"].ClearNull();
		if( r.IsDIFfNull )
			cols["DIFf"].SetNull();
		else
			cols["DIFf"].ClearNull();
		if( r.IsIgnore_HistoryNull )
			cols["Ignore_History"].SetNull();
		else
			cols["Ignore_History"].ClearNull();
		if( r.IsEDI_997_AckNull )
			cols["EDI_997_Ack"].SetNull();
		else
			cols["EDI_997_Ack"].ClearNull();
		if( r.IsIgnore_EDI_RulesNull )
			cols["Ignore_EDI_Rules"].SetNull();
		else
			cols["Ignore_EDI_Rules"].ClearNull();
		if( r.IsTPAGUIDNull )
			cols["TPAGUID"].SetNull();
		else
			cols["TPAGUID"].ClearNull();
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

template<> class DefaultBCA<TranDb::OperationsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::OperationsTabRow &rowbuf)
	{
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
		cols["Operation_Type_ID"] == rowbuf.field_Operation_Type_ID;
		cols["Project_ID"] == rowbuf.field_Project_ID;
		cols["Source_ID"] == rowbuf.field_Source_ID;
		cols["Target_ID"] == rowbuf.field_Target_ID;
		cols["Schedule_ID"] == rowbuf.field_Schedule_ID;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["Success_Operation_ID"] == rowbuf.field_Success_Operation_ID;
		cols["Success_Message_ID"] == rowbuf.field_Success_Message_ID;
		cols["Success_Workflow"] == rowbuf.field_Success_Workflow;
		cols["Failure_Operation_ID"] == rowbuf.field_Failure_Operation_ID;
		cols["Failure_Message_ID"] == rowbuf.field_Failure_Message_ID;
		cols["Failure_Workflow"] == rowbuf.field_Failure_Workflow;
		cols["User_ID"] == rowbuf.field_User_ID;
		cols["Description"] == rowbuf.field_Description;
		cols["Trace"] == rowbuf.field_Trace;
		cols["DIFf"] == rowbuf.field_DIFf;
		cols["Ignore_History"] == rowbuf.field_Ignore_History;
		cols["EDI_997_Ack"] == rowbuf.field_EDI_997_Ack;
		cols["Ignore_EDI_Rules"] == rowbuf.field_Ignore_EDI_Rules;
		cols["TPAGUID"] == rowbuf.field_TPAGUID;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {

class DtlOperationsTabDeleteBinder : public CustomBinder<TranDb::OperationsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::OperationsTabRow& rowbuf = *m_pRow;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
	}
};

class DtlOperationsTab : public TranDb::OperationsTab {

public:
	DtlOperationsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlOperationsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::OperationsTab::Edit();
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
	DtlOperationsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::OperationsTab, TranDb::OperationsTabRow> m_dtlRecSet;

};


}                            


#endif

