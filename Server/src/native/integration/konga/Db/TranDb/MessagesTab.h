/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_MessagesTab_H
#define KONGA_DB_TRANDB_MessagesTab_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class MessagesTabRow
{
public:
	MessagesTabRow() : 
			field_Message_ID(0),
			IsMessage_IDNull(true),
			IsMessage_IDPrimaryKey(true),
			IsMessage_IDDirty(false),
			IsMessage_NameNull(true),
			IsMessage_NamePrimaryKey(false),
			IsMessage_NameDirty(false),
			IsSmtp_ServersNull(true),
			IsSmtp_ServersPrimaryKey(false),
			IsSmtp_ServersDirty(false),
			IsAccountNull(true),
			IsAccountPrimaryKey(false),
			IsAccountDirty(false),
			IsAccount_PasswordNull(true),
			IsAccount_PasswordPrimaryKey(false),
			IsAccount_PasswordDirty(false),
			IsSend_ToNull(true),
			IsSend_ToPrimaryKey(false),
			IsSend_ToDirty(false),
			IsSent_FromNull(true),
			IsSent_FromPrimaryKey(false),
			IsSent_FromDirty(false),
			IsCcNull(true),
			IsCcPrimaryKey(false),
			IsCcDirty(false),
			IsBccNull(true),
			IsBccPrimaryKey(false),
			IsBccDirty(false),
			IsReply_ToNull(true),
			IsReply_ToPrimaryKey(false),
			IsReply_ToDirty(false),
			IsSubjectNull(true),
			IsSubjectPrimaryKey(false),
			IsSubjectDirty(false),
			IsMessage_TextNull(true),
			IsMessage_TextPrimaryKey(false),
			IsMessage_TextDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false)
	{
		field_InsertTime.year = 0;
		field_InsertTime.month = 0;
		field_InsertTime.day = 0;
		field_InsertTime.hour = 0;
		field_InsertTime.minute = 0;
		field_InsertTime.second = 0;
		field_InsertTime.fraction = 0;
		field_ModTime.year = 0;
		field_ModTime.month = 0;
		field_ModTime.day = 0;
		field_ModTime.hour = 0;
		field_ModTime.minute = 0;
		field_ModTime.second = 0;
		field_ModTime.fraction = 0;
	}

	void Copy(const MessagesTabRow& r)
	{
		field_Message_ID = r.field_Message_ID;
		IsMessage_IDNull = r.IsMessage_IDNull;
		IsMessage_IDDirty = r.IsMessage_IDDirty;

		field_Message_Name = r.field_Message_Name;
		IsMessage_NameNull = r.IsMessage_NameNull;
		IsMessage_NameDirty = r.IsMessage_NameDirty;

		field_Smtp_Servers = r.field_Smtp_Servers;
		IsSmtp_ServersNull = r.IsSmtp_ServersNull;
		IsSmtp_ServersDirty = r.IsSmtp_ServersDirty;

		field_Account = r.field_Account;
		IsAccountNull = r.IsAccountNull;
		IsAccountDirty = r.IsAccountDirty;

		field_Account_Password = r.field_Account_Password;
		IsAccount_PasswordNull = r.IsAccount_PasswordNull;
		IsAccount_PasswordDirty = r.IsAccount_PasswordDirty;

		field_Send_To = r.field_Send_To;
		IsSend_ToNull = r.IsSend_ToNull;
		IsSend_ToDirty = r.IsSend_ToDirty;

		field_Sent_From = r.field_Sent_From;
		IsSent_FromNull = r.IsSent_FromNull;
		IsSent_FromDirty = r.IsSent_FromDirty;

		field_Cc = r.field_Cc;
		IsCcNull = r.IsCcNull;
		IsCcDirty = r.IsCcDirty;

		field_Bcc = r.field_Bcc;
		IsBccNull = r.IsBccNull;
		IsBccDirty = r.IsBccDirty;

		field_Reply_To = r.field_Reply_To;
		IsReply_ToNull = r.IsReply_ToNull;
		IsReply_ToDirty = r.IsReply_ToDirty;

		field_Subject = r.field_Subject;
		IsSubjectNull = r.IsSubjectNull;
		IsSubjectDirty = r.IsSubjectDirty;

		field_Message_Text = r.field_Message_Text;
		IsMessage_TextNull = r.IsMessage_TextNull;
		IsMessage_TextDirty = r.IsMessage_TextDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const MessagesTabRow& r)
	{
		field_Message_ID = r.field_Message_ID;
		IsMessage_IDNull = r.IsMessage_IDNull;
		IsMessage_IDDirty = r.IsMessage_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsMessage_IDDirty = b;
		IsMessage_NameDirty = b;
		IsSmtp_ServersDirty = b;
		IsAccountDirty = b;
		IsAccount_PasswordDirty = b;
		IsSend_ToDirty = b;
		IsSent_FromDirty = b;
		IsCcDirty = b;
		IsBccDirty = b;
		IsReply_ToDirty = b;
		IsSubjectDirty = b;
		IsMessage_TextDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsMessage_IDDirty ||
			IsMessage_NameDirty ||
			IsSmtp_ServersDirty ||
			IsAccountDirty ||
			IsAccount_PasswordDirty ||
			IsSend_ToDirty ||
			IsSent_FromDirty ||
			IsCcDirty ||
			IsBccDirty ||
			IsReply_ToDirty ||
			IsSubjectDirty ||
			IsMessage_TextDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Message_ID;
	bool IsMessage_IDNull;
	bool IsMessage_IDDirty;

	std::string field_Message_Name;
	bool IsMessage_NameNull;
	bool IsMessage_NameDirty;

	std::string field_Smtp_Servers;
	bool IsSmtp_ServersNull;
	bool IsSmtp_ServersDirty;

	std::string field_Account;
	bool IsAccountNull;
	bool IsAccountDirty;

	std::string field_Account_Password;
	bool IsAccount_PasswordNull;
	bool IsAccount_PasswordDirty;

	std::string field_Send_To;
	bool IsSend_ToNull;
	bool IsSend_ToDirty;

	std::string field_Sent_From;
	bool IsSent_FromNull;
	bool IsSent_FromDirty;

	std::string field_Cc;
	bool IsCcNull;
	bool IsCcDirty;

	std::string field_Bcc;
	bool IsBccNull;
	bool IsBccDirty;

	std::string field_Reply_To;
	bool IsReply_ToNull;
	bool IsReply_ToDirty;

	std::string field_Subject;
	bool IsSubjectNull;
	bool IsSubjectDirty;

	std::string field_Message_Text;
	bool IsMessage_TextNull;
	bool IsMessage_TextDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsMessage_IDPrimaryKey;
	bool IsMessage_NamePrimaryKey;
	bool IsSmtp_ServersPrimaryKey;
	bool IsAccountPrimaryKey;
	bool IsAccount_PasswordPrimaryKey;
	bool IsSend_ToPrimaryKey;
	bool IsSent_FromPrimaryKey;
	bool IsCcPrimaryKey;
	bool IsBccPrimaryKey;
	bool IsReply_ToPrimaryKey;
	bool IsSubjectPrimaryKey;
	bool IsMessage_TextPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class MessagesTab : public konga::DbRecordSet {
public:
	MessagesTab() {}

	static MessagesTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "MessagesTab";
	}

	void DoFieldExchange(const MessagesTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetMessage_ID() {
		if( m_row.IsMessage_IDNull )
			throw konga::DbNullException("Message_ID");
		return m_row.field_Message_ID;
	}
	void SetMessage_ID(int i) {
		m_row.field_Message_ID = i;
		m_row.IsMessage_IDNull = false;
		m_row.IsMessage_IDDirty = true;
	}
	bool IsMessage_IDNull() const {
		return m_row.IsMessage_IDNull;
	}
	void SetMessage_IDToNull() {
		m_row.IsMessage_IDNull = true;
		m_row.IsMessage_IDDirty = true;
	}

	std::string GetMessage_Name() {
		if( m_row.IsMessage_NameNull )
			throw konga::DbNullException("Message_Name");
		return m_row.field_Message_Name;
	}
	void SetMessage_Name(const std::string& st) {
		m_row.field_Message_Name = st;
		m_row.IsMessage_NameNull = false;
		m_row.IsMessage_NameDirty = true;
	}
	bool IsMessage_NameNull() const {
		return m_row.IsMessage_NameNull;
	}
	void SetMessage_NameToNull() {
		m_row.field_Message_Name = "";
		m_row.IsMessage_NameNull = true;
		m_row.IsMessage_NameDirty = true;
	}

	std::string GetSmtp_Servers() {
		if( m_row.IsSmtp_ServersNull )
			throw konga::DbNullException("Smtp_Servers");
		return m_row.field_Smtp_Servers;
	}
	void SetSmtp_Servers(const std::string& st) {
		m_row.field_Smtp_Servers = st;
		m_row.IsSmtp_ServersNull = false;
		m_row.IsSmtp_ServersDirty = true;
	}
	bool IsSmtp_ServersNull() const {
		return m_row.IsSmtp_ServersNull;
	}
	void SetSmtp_ServersToNull() {
		m_row.field_Smtp_Servers = "";
		m_row.IsSmtp_ServersNull = true;
		m_row.IsSmtp_ServersDirty = true;
	}

	std::string GetAccount() {
		if( m_row.IsAccountNull )
			throw konga::DbNullException("Account");
		return m_row.field_Account;
	}
	void SetAccount(const std::string& st) {
		m_row.field_Account = st;
		m_row.IsAccountNull = false;
		m_row.IsAccountDirty = true;
	}
	bool IsAccountNull() const {
		return m_row.IsAccountNull;
	}
	void SetAccountToNull() {
		m_row.field_Account = "";
		m_row.IsAccountNull = true;
		m_row.IsAccountDirty = true;
	}

	std::string GetAccount_Password() {
		if( m_row.IsAccount_PasswordNull )
			throw konga::DbNullException("Account_Password");
		return m_row.field_Account_Password;
	}
	void SetAccount_Password(const std::string& st) {
		m_row.field_Account_Password = st;
		m_row.IsAccount_PasswordNull = false;
		m_row.IsAccount_PasswordDirty = true;
	}
	bool IsAccount_PasswordNull() const {
		return m_row.IsAccount_PasswordNull;
	}
	void SetAccount_PasswordToNull() {
		m_row.field_Account_Password = "";
		m_row.IsAccount_PasswordNull = true;
		m_row.IsAccount_PasswordDirty = true;
	}

	std::string GetSend_To() {
		if( m_row.IsSend_ToNull )
			throw konga::DbNullException("Send_To");
		return m_row.field_Send_To;
	}
	void SetSend_To(const std::string& st) {
		m_row.field_Send_To = st;
		m_row.IsSend_ToNull = false;
		m_row.IsSend_ToDirty = true;
	}
	bool IsSend_ToNull() const {
		return m_row.IsSend_ToNull;
	}
	void SetSend_ToToNull() {
		m_row.field_Send_To = "";
		m_row.IsSend_ToNull = true;
		m_row.IsSend_ToDirty = true;
	}

	std::string GetSent_From() {
		if( m_row.IsSent_FromNull )
			throw konga::DbNullException("Sent_From");
		return m_row.field_Sent_From;
	}
	void SetSent_From(const std::string& st) {
		m_row.field_Sent_From = st;
		m_row.IsSent_FromNull = false;
		m_row.IsSent_FromDirty = true;
	}
	bool IsSent_FromNull() const {
		return m_row.IsSent_FromNull;
	}
	void SetSent_FromToNull() {
		m_row.field_Sent_From = "";
		m_row.IsSent_FromNull = true;
		m_row.IsSent_FromDirty = true;
	}

	std::string GetCc() {
		if( m_row.IsCcNull )
			throw konga::DbNullException("Cc");
		return m_row.field_Cc;
	}
	void SetCc(const std::string& st) {
		m_row.field_Cc = st;
		m_row.IsCcNull = false;
		m_row.IsCcDirty = true;
	}
	bool IsCcNull() const {
		return m_row.IsCcNull;
	}
	void SetCcToNull() {
		m_row.field_Cc = "";
		m_row.IsCcNull = true;
		m_row.IsCcDirty = true;
	}

	std::string GetBcc() {
		if( m_row.IsBccNull )
			throw konga::DbNullException("Bcc");
		return m_row.field_Bcc;
	}
	void SetBcc(const std::string& st) {
		m_row.field_Bcc = st;
		m_row.IsBccNull = false;
		m_row.IsBccDirty = true;
	}
	bool IsBccNull() const {
		return m_row.IsBccNull;
	}
	void SetBccToNull() {
		m_row.field_Bcc = "";
		m_row.IsBccNull = true;
		m_row.IsBccDirty = true;
	}

	std::string GetReply_To() {
		if( m_row.IsReply_ToNull )
			throw konga::DbNullException("Reply_To");
		return m_row.field_Reply_To;
	}
	void SetReply_To(const std::string& st) {
		m_row.field_Reply_To = st;
		m_row.IsReply_ToNull = false;
		m_row.IsReply_ToDirty = true;
	}
	bool IsReply_ToNull() const {
		return m_row.IsReply_ToNull;
	}
	void SetReply_ToToNull() {
		m_row.field_Reply_To = "";
		m_row.IsReply_ToNull = true;
		m_row.IsReply_ToDirty = true;
	}

	std::string GetSubject() {
		if( m_row.IsSubjectNull )
			throw konga::DbNullException("Subject");
		return m_row.field_Subject;
	}
	void SetSubject(const std::string& st) {
		m_row.field_Subject = st;
		m_row.IsSubjectNull = false;
		m_row.IsSubjectDirty = true;
	}
	bool IsSubjectNull() const {
		return m_row.IsSubjectNull;
	}
	void SetSubjectToNull() {
		m_row.field_Subject = "";
		m_row.IsSubjectNull = true;
		m_row.IsSubjectDirty = true;
	}

	std::string GetMessage_Text() {
		if( m_row.IsMessage_TextNull )
			throw konga::DbNullException("Message_Text");
		return m_row.field_Message_Text;
	}
	void SetMessage_Text(const std::string& st) {
		m_row.field_Message_Text = st;
		m_row.IsMessage_TextNull = false;
		m_row.IsMessage_TextDirty = true;
	}
	bool IsMessage_TextNull() const {
		return m_row.IsMessage_TextNull;
	}
	void SetMessage_TextToNull() {
		m_row.field_Message_Text = "";
		m_row.IsMessage_TextNull = true;
		m_row.IsMessage_TextDirty = true;
	}

	TIMESTAMP_STRUCT GetInsertTime() {
		if( m_row.IsInsertTimeNull )
			throw konga::DbNullException("InsertTime");
		return m_row.field_InsertTime;
	}
	void SetInsertTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_InsertTime = ts;
		m_row.IsInsertTimeNull = false;
		m_row.IsInsertTimeDirty = true;
	}
	bool IsInsertTimeNull() const {
		return m_row.IsInsertTimeNull;
	}
	void SetInsertTimeToNull() {
		m_row.IsInsertTimeNull = true;
		m_row.IsInsertTimeDirty = true;
	}

	TIMESTAMP_STRUCT GetModTime() {
		if( m_row.IsModTimeNull )
			throw konga::DbNullException("ModTime");
		return m_row.field_ModTime;
	}
	void SetModTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_ModTime = ts;
		m_row.IsModTimeNull = false;
		m_row.IsModTimeDirty = true;
	}
	bool IsModTimeNull() const {
		return m_row.IsModTimeNull;
	}
	void SetModTimeToNull() {
		m_row.IsModTimeNull = true;
		m_row.IsModTimeDirty = true;
	}

protected:
	MessagesTabRow m_row;

private:
	bool IsMessage_IDDirty() const {
		return m_row.IsMessage_IDDirty;
	};
	bool IsMessage_NameDirty() const {
		return m_row.IsMessage_NameDirty;
	};
	bool IsSmtp_ServersDirty() const {
		return m_row.IsSmtp_ServersDirty;
	};
	bool IsAccountDirty() const {
		return m_row.IsAccountDirty;
	};
	bool IsAccount_PasswordDirty() const {
		return m_row.IsAccount_PasswordDirty;
	};
	bool IsSend_ToDirty() const {
		return m_row.IsSend_ToDirty;
	};
	bool IsSent_FromDirty() const {
		return m_row.IsSent_FromDirty;
	};
	bool IsCcDirty() const {
		return m_row.IsCcDirty;
	};
	bool IsBccDirty() const {
		return m_row.IsBccDirty;
	};
	bool IsReply_ToDirty() const {
		return m_row.IsReply_ToDirty;
	};
	bool IsSubjectDirty() const {
		return m_row.IsSubjectDirty;
	};
	bool IsMessage_TextDirty() const {
		return m_row.IsMessage_TextDirty;
	};
	bool IsInsertTimeDirty() const {
		return m_row.IsInsertTimeDirty;
	};
	bool IsModTimeDirty() const {
		return m_row.IsModTimeDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
