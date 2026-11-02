/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_ARCHIVECRMSGDBFINAL_H
#define KONGA_DTL_CRMSGDB_ARCHIVECRMSGDBFINAL_H

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/ArchiveCRMSGDBFinal.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::ArchiveCRMSGDBFinalRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ArchiveCRMSGDBFinalRow &r)
	{
		r.IsMsgFromNull = cols["MsgFrom"].IsNull();
		r.IsMsgToNull = cols["MsgTo"].IsNull();
		r.IsMsgSubjectNull = cols["MsgSubject"].IsNull();
		r.IsProcessVariablesNull = cols["ProcessVariables"].IsNull();
		r.IsReceivedAtNull = cols["ReceivedAt"].IsNull();
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsMsgReplyToNull = cols["MsgReplyTo"].IsNull();
		r.IsMsgPriorityNull = cols["MsgPriority"].IsNull();
		r.IsOwnerNull = cols["Owner"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		r.IsMachineNameNull = cols["MachineName"].IsNull();
		r.IsCompletionStatusNull = cols["CompletionStatus"].IsNull();
		r.IsErrorTextNull = cols["ErrorText"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsDataTypeNull = cols["DataType"].IsNull();
		r.IsIntValueNull = cols["IntValue"].IsNull();
		r.IsDoubleValueNull = cols["DoubleValue"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		  
                                        
                       
           
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::ArchiveCRMSGDBFinalRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ArchiveCRMSGDBFinalRow &r)
	{
		if( r.IsMsgFromNull )
			cols["MsgFrom"].SetNull();
		else
			cols["MsgFrom"].ClearNull();
		if( r.IsMsgToNull )
			cols["MsgTo"].SetNull();
		else
			cols["MsgTo"].ClearNull();
		if( r.IsMsgSubjectNull )
			cols["MsgSubject"].SetNull();
		else
			cols["MsgSubject"].ClearNull();
		if( r.IsProcessVariablesNull )
			cols["ProcessVariables"].SetNull();
		else
			cols["ProcessVariables"].ClearNull();
		if( r.IsReceivedAtNull )
			cols["ReceivedAt"].SetNull();
		else
			cols["ReceivedAt"].ClearNull();
		if( r.IsMsgIdNull )
			cols["MsgId"].SetNull();
		else
			cols["MsgId"].ClearNull();
		if( r.IsMsgReplyToNull )
			cols["MsgReplyTo"].SetNull();
		else
			cols["MsgReplyTo"].ClearNull();
		if( r.IsMsgPriorityNull )
			cols["MsgPriority"].SetNull();
		else
			cols["MsgPriority"].ClearNull();
		if( r.IsOwnerNull )
			cols["Owner"].SetNull();
		else
			cols["Owner"].ClearNull();
		if( r.IsInsertTimeNull )
			cols["InsertTime"].SetNull();
		else
			cols["InsertTime"].ClearNull();
		if( r.IsModTimeNull )
			cols["ModTime"].SetNull();
		else
			cols["ModTime"].ClearNull();
		if( r.IsMachineNameNull )
			cols["MachineName"].SetNull();
		else
			cols["MachineName"].ClearNull();
		if( r.IsCompletionStatusNull )
			cols["CompletionStatus"].SetNull();
		else
			cols["CompletionStatus"].ClearNull();
		if( r.IsErrorTextNull )
			cols["ErrorText"].SetNull();
		else
			cols["ErrorText"].ClearNull();
		if( r.IsDataElementNameNull )
			cols["DataElementName"].SetNull();
		else
			cols["DataElementName"].ClearNull();
		if( r.IsDataTypeNull )
			cols["DataType"].SetNull();
		else
			cols["DataType"].ClearNull();
		if( r.IsIntValueNull )
			cols["IntValue"].SetNull();
		else
			cols["IntValue"].ClearNull();
		if( r.IsDoubleValueNull )
			cols["DoubleValue"].SetNull();
		else
			cols["DoubleValue"].ClearNull();
		if( r.IsStringValueNull )
			cols["StringValue"].SetNull();
		else
			cols["StringValue"].ClearNull();
		  
                                        
                       
           
    
		return true;
	}
};

template<> class DefaultBCA<MsgDb::ArchiveCRMSGDBFinalRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::ArchiveCRMSGDBFinalRow &rowbuf)
	{
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["ProcessVariables"] == rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] == rowbuf.field_ReceivedAt;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["MsgReplyTo"] == rowbuf.field_MsgReplyTo;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["Owner"] == rowbuf.field_Owner;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		cols["MachineName"] == rowbuf.field_MachineName;
		cols["CompletionStatus"] == rowbuf.field_CompletionStatus;
		cols["ErrorText"] == rowbuf.field_ErrorText;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["DataType"] == rowbuf.field_DataType;
		cols["IntValue"] == rowbuf.field_IntValue;
		cols["DoubleValue"] == rowbuf.field_DoubleValue;
		cols["StringValue"] == rowbuf.field_StringValue;
		  
                                        
                       
           
    
	}
};


}                     


class DeleteBinder : public CustomBinder<MsgDb::ArchiveCRMSGDBFinalRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ArchiveCRMSGDBFinalRow& rowbuf = *m_pRow;
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["ProcessVariables"] == rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] == rowbuf.field_ReceivedAt;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["MsgReplyTo"] == rowbuf.field_MsgReplyTo;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["Owner"] == rowbuf.field_Owner;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		cols["MachineName"] == rowbuf.field_MachineName;
		cols["CompletionStatus"] == rowbuf.field_CompletionStatus;
		cols["ErrorText"] == rowbuf.field_ErrorText;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["DataType"] == rowbuf.field_DataType;
		cols["IntValue"] == rowbuf.field_IntValue;
		cols["DoubleValue"] == rowbuf.field_DoubleValue;
		cols["StringValue"] == rowbuf.field_StringValue;
	}
};

class DtlArchiveCRMSGDBFinal : public MsgDb::ArchiveCRMSGDBFinal {

public:
	DtlArchiveCRMSGDBFinal(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArchiveCRMSGDBFinal() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::ArchiveCRMSGDBFinal::Edit();
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
	DtlDbRecordSet<MsgDb::ArchiveCRMSGDBFinal, MsgDb::ArchiveCRMSGDBFinalRow> m_dtlRecSet;

};

#endif

