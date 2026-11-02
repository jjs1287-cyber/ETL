/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_TRANDB_MessagesTab_H
#define KONGA_DTL_TRANDB_MessagesTab_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/MessagesTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::MessagesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::MessagesTabRow &r)
	{
		r.IsMessage_IDNull = cols["Message_ID"].IsNull();
		r.IsMessage_NameNull = cols["Message_Name"].IsNull();
		r.IsSmtp_ServersNull = cols["Smtp_Servers"].IsNull();
		r.IsAccountNull = cols["Account"].IsNull();
		r.IsAccount_PasswordNull = cols["Account_Password"].IsNull();
		r.IsSend_ToNull = cols["Send_To"].IsNull();
		r.IsSent_FromNull = cols["Sent_From"].IsNull();
		r.IsCcNull = cols["Cc"].IsNull();
		r.IsBccNull = cols["Bcc"].IsNull();
		r.IsReply_ToNull = cols["Reply_To"].IsNull();
		r.IsSubjectNull = cols["Subject"].IsNull();
		r.IsMessage_TextNull = cols["Message_Text"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::MessagesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::MessagesTabRow &r)
	{
		if( r.IsMessage_IDNull )
			cols["Message_ID"].SetNull();
		else
			cols["Message_ID"].ClearNull();
		if( r.IsMessage_NameNull )
			cols["Message_Name"].SetNull();
		else
			cols["Message_Name"].ClearNull();
		if( r.IsSmtp_ServersNull )
			cols["Smtp_Servers"].SetNull();
		else
			cols["Smtp_Servers"].ClearNull();
		if( r.IsAccountNull )
			cols["Account"].SetNull();
		else
			cols["Account"].ClearNull();
		if( r.IsAccount_PasswordNull )
			cols["Account_Password"].SetNull();
		else
			cols["Account_Password"].ClearNull();
		if( r.IsSend_ToNull )
			cols["Send_To"].SetNull();
		else
			cols["Send_To"].ClearNull();
		if( r.IsSent_FromNull )
			cols["Sent_From"].SetNull();
		else
			cols["Sent_From"].ClearNull();
		if( r.IsCcNull )
			cols["Cc"].SetNull();
		else
			cols["Cc"].ClearNull();
		if( r.IsBccNull )
			cols["Bcc"].SetNull();
		else
			cols["Bcc"].ClearNull();
		if( r.IsReply_ToNull )
			cols["Reply_To"].SetNull();
		else
			cols["Reply_To"].ClearNull();
		if( r.IsSubjectNull )
			cols["Subject"].SetNull();
		else
			cols["Subject"].ClearNull();
		if( r.IsMessage_TextNull )
			cols["Message_Text"].SetNull();
		else
			cols["Message_Text"].ClearNull();
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

template<> class DefaultBCA<TranDb::MessagesTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::MessagesTabRow &rowbuf)
	{
		cols["Message_ID"] == rowbuf.field_Message_ID;
		cols["Message_Name"] == rowbuf.field_Message_Name;
		cols["Smtp_Servers"] == rowbuf.field_Smtp_Servers;
		cols["Account"] == rowbuf.field_Account;
		cols["Account_Password"] == rowbuf.field_Account_Password;
		cols["Send_To"] == rowbuf.field_Send_To;
		cols["Sent_From"] == rowbuf.field_Sent_From;
		cols["Cc"] == rowbuf.field_Cc;
		cols["Bcc"] == rowbuf.field_Bcc;
		cols["Reply_To"] == rowbuf.field_Reply_To;
		cols["Subject"] == rowbuf.field_Subject;
		cols["Message_Text"] == rowbuf.field_Message_Text;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
	}
};


}                     



namespace dtl_trandb {

class DtlMessagesTabDeleteBinder : public CustomBinder<TranDb::MessagesTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::MessagesTabRow& rowbuf = *m_pRow;
		cols["Message_ID"] == rowbuf.field_Message_ID;
	}
};

class DtlMessagesTab : public TranDb::MessagesTab {

public:
	DtlMessagesTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlMessagesTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::MessagesTab::Edit();
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
	DtlMessagesTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::MessagesTab, TranDb::MessagesTabRow> m_dtlRecSet;

};


}                            


#endif

