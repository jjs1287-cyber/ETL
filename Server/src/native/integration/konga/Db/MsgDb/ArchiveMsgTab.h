/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_ARCHIVEMSGTAB_H
#define KONGA_DB_CRMSGDB_ARCHIVEMSGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace MsgDb
{

class ArchiveMsgTabRow
{
public:
	ArchiveMsgTabRow() : 
			field_ArchiveId(0),
			IsArchiveIdNull(true),
			IsArchiveIdPrimaryKey(true),
			IsArchiveIdDirty(false),
			IsMsgFromNull(true),
			IsMsgFromPrimaryKey(false),
			IsMsgFromDirty(false),
			IsMsgToNull(true),
			IsMsgToPrimaryKey(false),
			IsMsgToDirty(false),
			IsMsgSubjectNull(true),
			IsMsgSubjectPrimaryKey(false),
			IsMsgSubjectDirty(false),
			IsProcessVariablesNull(true),
			IsProcessVariablesPrimaryKey(false),
			IsProcessVariablesDirty(false),
			field_ReceivedAt(0),
			IsReceivedAtNull(true),
			IsReceivedAtPrimaryKey(false),
			IsReceivedAtDirty(false),
			field_MsgId(0),
			IsMsgIdNull(true),
			IsMsgIdPrimaryKey(false),
			IsMsgIdDirty(false),
			IsMsgReplyToNull(true),
			IsMsgReplyToPrimaryKey(false),
			IsMsgReplyToDirty(false),
			field_MsgPriority(0),
			IsMsgPriorityNull(true),
			IsMsgPriorityPrimaryKey(false),
			IsMsgPriorityDirty(false),
			IsOwnerNull(true),
			IsOwnerPrimaryKey(false),
			IsOwnerDirty(false),
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

	void Copy(const ArchiveMsgTabRow& r)
	{
		field_ArchiveId = r.field_ArchiveId;
		IsArchiveIdNull = r.IsArchiveIdNull;
		IsArchiveIdDirty = r.IsArchiveIdDirty;

		field_MsgFrom = r.field_MsgFrom;
		IsMsgFromNull = r.IsMsgFromNull;
		IsMsgFromDirty = r.IsMsgFromDirty;

		field_MsgTo = r.field_MsgTo;
		IsMsgToNull = r.IsMsgToNull;
		IsMsgToDirty = r.IsMsgToDirty;

		field_MsgSubject = r.field_MsgSubject;
		IsMsgSubjectNull = r.IsMsgSubjectNull;
		IsMsgSubjectDirty = r.IsMsgSubjectDirty;

		field_ProcessVariables = r.field_ProcessVariables;
		IsProcessVariablesNull = r.IsProcessVariablesNull;
		IsProcessVariablesDirty = r.IsProcessVariablesDirty;

		field_ReceivedAt = r.field_ReceivedAt;
		IsReceivedAtNull = r.IsReceivedAtNull;
		IsReceivedAtDirty = r.IsReceivedAtDirty;

		field_MsgId = r.field_MsgId;
		IsMsgIdNull = r.IsMsgIdNull;
		IsMsgIdDirty = r.IsMsgIdDirty;

		field_MsgReplyTo = r.field_MsgReplyTo;
		IsMsgReplyToNull = r.IsMsgReplyToNull;
		IsMsgReplyToDirty = r.IsMsgReplyToDirty;

		field_MsgPriority = r.field_MsgPriority;
		IsMsgPriorityNull = r.IsMsgPriorityNull;
		IsMsgPriorityDirty = r.IsMsgPriorityDirty;

		field_Owner = r.field_Owner;
		IsOwnerNull = r.IsOwnerNull;
		IsOwnerDirty = r.IsOwnerDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ArchiveMsgTabRow& r)
	{
		field_ArchiveId = r.field_ArchiveId;
		IsArchiveIdNull = r.IsArchiveIdNull;
		IsArchiveIdDirty = r.IsArchiveIdDirty;

	}

	void SetDirty(bool b)
	{
		IsArchiveIdDirty = b;
		IsMsgFromDirty = b;
		IsMsgToDirty = b;
		IsMsgSubjectDirty = b;
		IsProcessVariablesDirty = b;
		IsReceivedAtDirty = b;
		IsMsgIdDirty = b;
		IsMsgReplyToDirty = b;
		IsMsgPriorityDirty = b;
		IsOwnerDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsArchiveIdDirty ||
			IsMsgFromDirty ||
			IsMsgToDirty ||
			IsMsgSubjectDirty ||
			IsProcessVariablesDirty ||
			IsReceivedAtDirty ||
			IsMsgIdDirty ||
			IsMsgReplyToDirty ||
			IsMsgPriorityDirty ||
			IsOwnerDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ArchiveId;
	bool IsArchiveIdNull;
	bool IsArchiveIdDirty;

	std::string field_MsgFrom;
	bool IsMsgFromNull;
	bool IsMsgFromDirty;

	std::string field_MsgTo;
	bool IsMsgToNull;
	bool IsMsgToDirty;

	std::string field_MsgSubject;
	bool IsMsgSubjectNull;
	bool IsMsgSubjectDirty;

	std::string field_ProcessVariables;
	bool IsProcessVariablesNull;
	bool IsProcessVariablesDirty;

	int field_ReceivedAt;
	bool IsReceivedAtNull;
	bool IsReceivedAtDirty;

	int field_MsgId;
	bool IsMsgIdNull;
	bool IsMsgIdDirty;

	std::string field_MsgReplyTo;
	bool IsMsgReplyToNull;
	bool IsMsgReplyToDirty;

	int field_MsgPriority;
	bool IsMsgPriorityNull;
	bool IsMsgPriorityDirty;

	std::string field_Owner;
	bool IsOwnerNull;
	bool IsOwnerDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsArchiveIdPrimaryKey;
	bool IsMsgFromPrimaryKey;
	bool IsMsgToPrimaryKey;
	bool IsMsgSubjectPrimaryKey;
	bool IsProcessVariablesPrimaryKey;
	bool IsReceivedAtPrimaryKey;
	bool IsMsgIdPrimaryKey;
	bool IsMsgReplyToPrimaryKey;
	bool IsMsgPriorityPrimaryKey;
	bool IsOwnerPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ArchiveMsgTab : public konga::DbRecordSet {
public:
	ArchiveMsgTab() {}

	static ArchiveMsgTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ArchiveMsgTab";
	}

	void DoFieldExchange(const ArchiveMsgTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetArchiveId() {
		if( m_row.IsArchiveIdNull )
			throw konga::DbNullException("ArchiveId");
		return m_row.field_ArchiveId;
	}
	void SetArchiveId(int i) {
		m_row.field_ArchiveId = i;
		m_row.IsArchiveIdNull = false;
		m_row.IsArchiveIdDirty = true;
	}
	bool IsArchiveIdNull() const {
		return m_row.IsArchiveIdNull;
	}
	void SetArchiveIdToNull() {
		m_row.IsArchiveIdNull = true;
		m_row.IsArchiveIdDirty = true;
	}

	std::string GetMsgFrom() {
		if( m_row.IsMsgFromNull )
			throw konga::DbNullException("MsgFrom");
		return m_row.field_MsgFrom;
	}
	void SetMsgFrom(const std::string& st) {
		m_row.field_MsgFrom = st;
		m_row.IsMsgFromNull = false;
		m_row.IsMsgFromDirty = true;
	}
	bool IsMsgFromNull() const {
		return m_row.IsMsgFromNull;
	}
	void SetMsgFromToNull() {
		m_row.field_MsgFrom = "";
		m_row.IsMsgFromNull = true;
		m_row.IsMsgFromDirty = true;
	}

	std::string GetMsgTo() {
		if( m_row.IsMsgToNull )
			throw konga::DbNullException("MsgTo");
		return m_row.field_MsgTo;
	}
	void SetMsgTo(const std::string& st) {
		m_row.field_MsgTo = st;
		m_row.IsMsgToNull = false;
		m_row.IsMsgToDirty = true;
	}
	bool IsMsgToNull() const {
		return m_row.IsMsgToNull;
	}
	void SetMsgToToNull() {
		m_row.field_MsgTo = "";
		m_row.IsMsgToNull = true;
		m_row.IsMsgToDirty = true;
	}

	std::string GetMsgSubject() {
		if( m_row.IsMsgSubjectNull )
			throw konga::DbNullException("MsgSubject");
		return m_row.field_MsgSubject;
	}
	void SetMsgSubject(const std::string& st) {
		m_row.field_MsgSubject = st;
		m_row.IsMsgSubjectNull = false;
		m_row.IsMsgSubjectDirty = true;
	}
	bool IsMsgSubjectNull() const {
		return m_row.IsMsgSubjectNull;
	}
	void SetMsgSubjectToNull() {
		m_row.field_MsgSubject = "";
		m_row.IsMsgSubjectNull = true;
		m_row.IsMsgSubjectDirty = true;
	}

	std::string GetProcessVariables() {
		if( m_row.IsProcessVariablesNull )
			throw konga::DbNullException("ProcessVariables");
		return m_row.field_ProcessVariables;
	}
	void SetProcessVariables(const std::string& st) {
		m_row.field_ProcessVariables = st;
		m_row.IsProcessVariablesNull = false;
		m_row.IsProcessVariablesDirty = true;
	}
	bool IsProcessVariablesNull() const {
		return m_row.IsProcessVariablesNull;
	}
	void SetProcessVariablesToNull() {
		m_row.field_ProcessVariables = "";
		m_row.IsProcessVariablesNull = true;
		m_row.IsProcessVariablesDirty = true;
	}

	int GetReceivedAt() {
		if( m_row.IsReceivedAtNull )
			throw konga::DbNullException("ReceivedAt");
		return m_row.field_ReceivedAt;
	}
	void SetReceivedAt(int i) {
		m_row.field_ReceivedAt = i;
		m_row.IsReceivedAtNull = false;
		m_row.IsReceivedAtDirty = true;
	}
	bool IsReceivedAtNull() const {
		return m_row.IsReceivedAtNull;
	}
	void SetReceivedAtToNull() {
		m_row.IsReceivedAtNull = true;
		m_row.IsReceivedAtDirty = true;
	}

	int GetMsgId() {
		if( m_row.IsMsgIdNull )
			throw konga::DbNullException("MsgId");
		return m_row.field_MsgId;
	}
	void SetMsgId(int i) {
		m_row.field_MsgId = i;
		m_row.IsMsgIdNull = false;
		m_row.IsMsgIdDirty = true;
	}
	bool IsMsgIdNull() const {
		return m_row.IsMsgIdNull;
	}
	void SetMsgIdToNull() {
		m_row.IsMsgIdNull = true;
		m_row.IsMsgIdDirty = true;
	}

	std::string GetMsgReplyTo() {
		if( m_row.IsMsgReplyToNull )
			throw konga::DbNullException("MsgReplyTo");
		return m_row.field_MsgReplyTo;
	}
	void SetMsgReplyTo(const std::string& st) {
		m_row.field_MsgReplyTo = st;
		m_row.IsMsgReplyToNull = false;
		m_row.IsMsgReplyToDirty = true;
	}
	bool IsMsgReplyToNull() const {
		return m_row.IsMsgReplyToNull;
	}
	void SetMsgReplyToToNull() {
		m_row.field_MsgReplyTo = "";
		m_row.IsMsgReplyToNull = true;
		m_row.IsMsgReplyToDirty = true;
	}

	int GetMsgPriority() {
		if( m_row.IsMsgPriorityNull )
			throw konga::DbNullException("MsgPriority");
		return m_row.field_MsgPriority;
	}
	void SetMsgPriority(int i) {
		m_row.field_MsgPriority = i;
		m_row.IsMsgPriorityNull = false;
		m_row.IsMsgPriorityDirty = true;
	}
	bool IsMsgPriorityNull() const {
		return m_row.IsMsgPriorityNull;
	}
	void SetMsgPriorityToNull() {
		m_row.IsMsgPriorityNull = true;
		m_row.IsMsgPriorityDirty = true;
	}

	std::string GetOwner() {
		if( m_row.IsOwnerNull )
			throw konga::DbNullException("Owner");
		return m_row.field_Owner;
	}
	void SetOwner(const std::string& st) {
		m_row.field_Owner = st;
		m_row.IsOwnerNull = false;
		m_row.IsOwnerDirty = true;
	}
	bool IsOwnerNull() const {
		return m_row.IsOwnerNull;
	}
	void SetOwnerToNull() {
		m_row.field_Owner = "";
		m_row.IsOwnerNull = true;
		m_row.IsOwnerDirty = true;
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
	ArchiveMsgTabRow m_row;

private:
	bool IsArchiveIdDirty() const {
		return m_row.IsArchiveIdDirty;
	};
	bool IsMsgFromDirty() const {
		return m_row.IsMsgFromDirty;
	};
	bool IsMsgToDirty() const {
		return m_row.IsMsgToDirty;
	};
	bool IsMsgSubjectDirty() const {
		return m_row.IsMsgSubjectDirty;
	};
	bool IsProcessVariablesDirty() const {
		return m_row.IsProcessVariablesDirty;
	};
	bool IsReceivedAtDirty() const {
		return m_row.IsReceivedAtDirty;
	};
	bool IsMsgIdDirty() const {
		return m_row.IsMsgIdDirty;
	};
	bool IsMsgReplyToDirty() const {
		return m_row.IsMsgReplyToDirty;
	};
	bool IsMsgPriorityDirty() const {
		return m_row.IsMsgPriorityDirty;
	};
	bool IsOwnerDirty() const {
		return m_row.IsOwnerDirty;
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
