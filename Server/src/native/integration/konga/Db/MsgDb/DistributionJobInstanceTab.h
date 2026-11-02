/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_DISTRIBUTIONJOBINSTANCETAB_H
#define KONGA_DB_CRMSGDB_DISTRIBUTIONJOBINSTANCETAB_H

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

class DistributionJobInstanceTabRow
{
public:
	DistributionJobInstanceTabRow() : 
			field_JobInstanceId(0),
			IsJobInstanceIdNull(true),
			IsJobInstanceIdPrimaryKey(true),
			IsJobInstanceIdDirty(false),
			field_JobId(0),
			IsJobIdNull(true),
			IsJobIdPrimaryKey(false),
			IsJobIdDirty(false),
			IsMachineNameNull(true),
			IsMachineNamePrimaryKey(false),
			IsMachineNameDirty(false),
			field_CompletionStatus(0),
			IsCompletionStatusNull(true),
			IsCompletionStatusPrimaryKey(false),
			IsCompletionStatusDirty(false),
			IsErrorTextNull(true),
			IsErrorTextPrimaryKey(false),
			IsErrorTextDirty(false),
			field_MsgId(0),
			IsMsgIdNull(true),
			IsMsgIdPrimaryKey(false),
			IsMsgIdDirty(false),
			field_Flags(0),
			IsFlagsNull(true),
			IsFlagsPrimaryKey(false),
			IsFlagsDirty(false),
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

	void Copy(const DistributionJobInstanceTabRow& r)
	{
		field_JobInstanceId = r.field_JobInstanceId;
		IsJobInstanceIdNull = r.IsJobInstanceIdNull;
		IsJobInstanceIdDirty = r.IsJobInstanceIdDirty;

		field_JobId = r.field_JobId;
		IsJobIdNull = r.IsJobIdNull;
		IsJobIdDirty = r.IsJobIdDirty;

		field_MachineName = r.field_MachineName;
		IsMachineNameNull = r.IsMachineNameNull;
		IsMachineNameDirty = r.IsMachineNameDirty;

		field_CompletionStatus = r.field_CompletionStatus;
		IsCompletionStatusNull = r.IsCompletionStatusNull;
		IsCompletionStatusDirty = r.IsCompletionStatusDirty;

		field_ErrorText = r.field_ErrorText;
		IsErrorTextNull = r.IsErrorTextNull;
		IsErrorTextDirty = r.IsErrorTextDirty;

		field_MsgId = r.field_MsgId;
		IsMsgIdNull = r.IsMsgIdNull;
		IsMsgIdDirty = r.IsMsgIdDirty;

		field_Flags = r.field_Flags;
		IsFlagsNull = r.IsFlagsNull;
		IsFlagsDirty = r.IsFlagsDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const DistributionJobInstanceTabRow& r)
	{
		field_JobInstanceId = r.field_JobInstanceId;
		IsJobInstanceIdNull = r.IsJobInstanceIdNull;
		IsJobInstanceIdDirty = r.IsJobInstanceIdDirty;

	}

	void SetDirty(bool b)
	{
		IsJobInstanceIdDirty = b;
		IsJobIdDirty = b;
		IsMachineNameDirty = b;
		IsCompletionStatusDirty = b;
		IsErrorTextDirty = b;
		IsMsgIdDirty = b;
		IsFlagsDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsJobInstanceIdDirty ||
			IsJobIdDirty ||
			IsMachineNameDirty ||
			IsCompletionStatusDirty ||
			IsErrorTextDirty ||
			IsMsgIdDirty ||
			IsFlagsDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_JobInstanceId;
	bool IsJobInstanceIdNull;
	bool IsJobInstanceIdDirty;

	int field_JobId;
	bool IsJobIdNull;
	bool IsJobIdDirty;

	std::string field_MachineName;
	bool IsMachineNameNull;
	bool IsMachineNameDirty;

	int field_CompletionStatus;
	bool IsCompletionStatusNull;
	bool IsCompletionStatusDirty;

	std::string field_ErrorText;
	bool IsErrorTextNull;
	bool IsErrorTextDirty;

	int field_MsgId;
	bool IsMsgIdNull;
	bool IsMsgIdDirty;

	int field_Flags;
	bool IsFlagsNull;
	bool IsFlagsDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsJobInstanceIdPrimaryKey;
	bool IsJobIdPrimaryKey;
	bool IsMachineNamePrimaryKey;
	bool IsCompletionStatusPrimaryKey;
	bool IsErrorTextPrimaryKey;
	bool IsMsgIdPrimaryKey;
	bool IsFlagsPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class DistributionJobInstanceTab : public konga::DbRecordSet {
public:
	DistributionJobInstanceTab() {}

	static DistributionJobInstanceTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "DistributionJobInstanceTab";
	}

	void DoFieldExchange(const DistributionJobInstanceTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetJobInstanceId() {
		if( m_row.IsJobInstanceIdNull )
			throw konga::DbNullException("JobInstanceId");
		return m_row.field_JobInstanceId;
	}
	void SetJobInstanceId(int i) {
		m_row.field_JobInstanceId = i;
		m_row.IsJobInstanceIdNull = false;
		m_row.IsJobInstanceIdDirty = true;
	}
	bool IsJobInstanceIdNull() const {
		return m_row.IsJobInstanceIdNull;
	}
	void SetJobInstanceIdToNull() {
		m_row.IsJobInstanceIdNull = true;
		m_row.IsJobInstanceIdDirty = true;
	}

	int GetJobId() {
		if( m_row.IsJobIdNull )
			throw konga::DbNullException("JobId");
		return m_row.field_JobId;
	}
	void SetJobId(int i) {
		m_row.field_JobId = i;
		m_row.IsJobIdNull = false;
		m_row.IsJobIdDirty = true;
	}
	bool IsJobIdNull() const {
		return m_row.IsJobIdNull;
	}
	void SetJobIdToNull() {
		m_row.IsJobIdNull = true;
		m_row.IsJobIdDirty = true;
	}

	std::string GetMachineName() {
		if( m_row.IsMachineNameNull )
			throw konga::DbNullException("MachineName");
		return m_row.field_MachineName;
	}
	void SetMachineName(const std::string& st) {
		m_row.field_MachineName = st;
		m_row.IsMachineNameNull = false;
		m_row.IsMachineNameDirty = true;
	}
	bool IsMachineNameNull() const {
		return m_row.IsMachineNameNull;
	}
	void SetMachineNameToNull() {
		m_row.field_MachineName = "";
		m_row.IsMachineNameNull = true;
		m_row.IsMachineNameDirty = true;
	}

	int GetCompletionStatus() {
		if( m_row.IsCompletionStatusNull )
			throw konga::DbNullException("CompletionStatus");
		return m_row.field_CompletionStatus;
	}
	void SetCompletionStatus(int i) {
		m_row.field_CompletionStatus = i;
		m_row.IsCompletionStatusNull = false;
		m_row.IsCompletionStatusDirty = true;
	}
	bool IsCompletionStatusNull() const {
		return m_row.IsCompletionStatusNull;
	}
	void SetCompletionStatusToNull() {
		m_row.IsCompletionStatusNull = true;
		m_row.IsCompletionStatusDirty = true;
	}

	std::string GetErrorText() {
		if( m_row.IsErrorTextNull )
			throw konga::DbNullException("ErrorText");
		return m_row.field_ErrorText;
	}
	void SetErrorText(const std::string& st) {
		m_row.field_ErrorText = st;
		m_row.IsErrorTextNull = false;
		m_row.IsErrorTextDirty = true;
	}
	bool IsErrorTextNull() const {
		return m_row.IsErrorTextNull;
	}
	void SetErrorTextToNull() {
		m_row.field_ErrorText = "";
		m_row.IsErrorTextNull = true;
		m_row.IsErrorTextDirty = true;
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

	int GetFlags() {
		if( m_row.IsFlagsNull )
			throw konga::DbNullException("Flags");
		return m_row.field_Flags;
	}
	void SetFlags(int i) {
		m_row.field_Flags = i;
		m_row.IsFlagsNull = false;
		m_row.IsFlagsDirty = true;
	}
	bool IsFlagsNull() const {
		return m_row.IsFlagsNull;
	}
	void SetFlagsToNull() {
		m_row.IsFlagsNull = true;
		m_row.IsFlagsDirty = true;
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
	DistributionJobInstanceTabRow m_row;

private:
	bool IsJobInstanceIdDirty() const {
		return m_row.IsJobInstanceIdDirty;
	};
	bool IsJobIdDirty() const {
		return m_row.IsJobIdDirty;
	};
	bool IsMachineNameDirty() const {
		return m_row.IsMachineNameDirty;
	};
	bool IsCompletionStatusDirty() const {
		return m_row.IsCompletionStatusDirty;
	};
	bool IsErrorTextDirty() const {
		return m_row.IsErrorTextDirty;
	};
	bool IsMsgIdDirty() const {
		return m_row.IsMsgIdDirty;
	};
	bool IsFlagsDirty() const {
		return m_row.IsFlagsDirty;
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
