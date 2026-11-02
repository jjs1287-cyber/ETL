/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_ATTACHMENTTAB_H
#define KONGA_DB_CRMSGDB_ATTACHMENTTAB_H

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

class AttachmentTabRow
{
public:
	AttachmentTabRow() : 
			field_AttachmentId(0),
			IsAttachmentIdNull(true),
			IsAttachmentIdPrimaryKey(true),
			IsAttachmentIdDirty(false),
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(true),
			IsProcessInstanceIdDirty(false),
			field_TaskId(0),
			IsTaskIdNull(true),
			IsTaskIdPrimaryKey(true),
			IsTaskIdDirty(false),
			field_TaskInstanceId(0),
			IsTaskInstanceIdNull(true),
			IsTaskInstanceIdPrimaryKey(true),
			IsTaskInstanceIdDirty(false),
			IsMimeContentTypeNull(true),
			IsMimeContentTypePrimaryKey(false),
			IsMimeContentTypeDirty(false),
			IsFileNameNull(true),
			IsFileNamePrimaryKey(false),
			IsFileNameDirty(false),
			field_DeleteAfterAttached(0),
			IsDeleteAfterAttachedNull(true),
			IsDeleteAfterAttachedPrimaryKey(false),
			IsDeleteAfterAttachedDirty(false),
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

	void Copy(const AttachmentTabRow& r)
	{
		field_AttachmentId = r.field_AttachmentId;
		IsAttachmentIdNull = r.IsAttachmentIdNull;
		IsAttachmentIdDirty = r.IsAttachmentIdDirty;

		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_TaskInstanceId = r.field_TaskInstanceId;
		IsTaskInstanceIdNull = r.IsTaskInstanceIdNull;
		IsTaskInstanceIdDirty = r.IsTaskInstanceIdDirty;

		field_MimeContentType = r.field_MimeContentType;
		IsMimeContentTypeNull = r.IsMimeContentTypeNull;
		IsMimeContentTypeDirty = r.IsMimeContentTypeDirty;

		field_FileName = r.field_FileName;
		IsFileNameNull = r.IsFileNameNull;
		IsFileNameDirty = r.IsFileNameDirty;

		field_DeleteAfterAttached = r.field_DeleteAfterAttached;
		IsDeleteAfterAttachedNull = r.IsDeleteAfterAttachedNull;
		IsDeleteAfterAttachedDirty = r.IsDeleteAfterAttachedDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const AttachmentTabRow& r)
	{
		field_AttachmentId = r.field_AttachmentId;
		IsAttachmentIdNull = r.IsAttachmentIdNull;
		IsAttachmentIdDirty = r.IsAttachmentIdDirty;

		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_TaskInstanceId = r.field_TaskInstanceId;
		IsTaskInstanceIdNull = r.IsTaskInstanceIdNull;
		IsTaskInstanceIdDirty = r.IsTaskInstanceIdDirty;

	}

	void SetDirty(bool b)
	{
		IsAttachmentIdDirty = b;
		IsProcessIdDirty = b;
		IsProcessInstanceIdDirty = b;
		IsTaskIdDirty = b;
		IsTaskInstanceIdDirty = b;
		IsMimeContentTypeDirty = b;
		IsFileNameDirty = b;
		IsDeleteAfterAttachedDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsAttachmentIdDirty ||
			IsProcessIdDirty ||
			IsProcessInstanceIdDirty ||
			IsTaskIdDirty ||
			IsTaskInstanceIdDirty ||
			IsMimeContentTypeDirty ||
			IsFileNameDirty ||
			IsDeleteAfterAttachedDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_AttachmentId;
	bool IsAttachmentIdNull;
	bool IsAttachmentIdDirty;

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

	int field_TaskId;
	bool IsTaskIdNull;
	bool IsTaskIdDirty;

	int field_TaskInstanceId;
	bool IsTaskInstanceIdNull;
	bool IsTaskInstanceIdDirty;

	std::string field_MimeContentType;
	bool IsMimeContentTypeNull;
	bool IsMimeContentTypeDirty;

	std::string field_FileName;
	bool IsFileNameNull;
	bool IsFileNameDirty;

	int field_DeleteAfterAttached;
	bool IsDeleteAfterAttachedNull;
	bool IsDeleteAfterAttachedDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsAttachmentIdPrimaryKey;
	bool IsProcessIdPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsTaskIdPrimaryKey;
	bool IsTaskInstanceIdPrimaryKey;
	bool IsMimeContentTypePrimaryKey;
	bool IsFileNamePrimaryKey;
	bool IsDeleteAfterAttachedPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class AttachmentTab : public konga::DbRecordSet {
public:
	AttachmentTab() {}

	static AttachmentTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "AttachmentTab";
	}

	void DoFieldExchange(const AttachmentTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetAttachmentId() {
		if( m_row.IsAttachmentIdNull )
			throw konga::DbNullException("AttachmentId");
		return m_row.field_AttachmentId;
	}
	void SetAttachmentId(int i) {
		m_row.field_AttachmentId = i;
		m_row.IsAttachmentIdNull = false;
		m_row.IsAttachmentIdDirty = true;
	}
	bool IsAttachmentIdNull() const {
		return m_row.IsAttachmentIdNull;
	}
	void SetAttachmentIdToNull() {
		m_row.IsAttachmentIdNull = true;
		m_row.IsAttachmentIdDirty = true;
	}

	int GetProcessId() {
		if( m_row.IsProcessIdNull )
			throw konga::DbNullException("ProcessId");
		return m_row.field_ProcessId;
	}
	void SetProcessId(int i) {
		m_row.field_ProcessId = i;
		m_row.IsProcessIdNull = false;
		m_row.IsProcessIdDirty = true;
	}
	bool IsProcessIdNull() const {
		return m_row.IsProcessIdNull;
	}
	void SetProcessIdToNull() {
		m_row.IsProcessIdNull = true;
		m_row.IsProcessIdDirty = true;
	}

	int GetProcessInstanceId() {
		if( m_row.IsProcessInstanceIdNull )
			throw konga::DbNullException("ProcessInstanceId");
		return m_row.field_ProcessInstanceId;
	}
	void SetProcessInstanceId(int i) {
		m_row.field_ProcessInstanceId = i;
		m_row.IsProcessInstanceIdNull = false;
		m_row.IsProcessInstanceIdDirty = true;
	}
	bool IsProcessInstanceIdNull() const {
		return m_row.IsProcessInstanceIdNull;
	}
	void SetProcessInstanceIdToNull() {
		m_row.IsProcessInstanceIdNull = true;
		m_row.IsProcessInstanceIdDirty = true;
	}

	int GetTaskId() {
		if( m_row.IsTaskIdNull )
			throw konga::DbNullException("TaskId");
		return m_row.field_TaskId;
	}
	void SetTaskId(int i) {
		m_row.field_TaskId = i;
		m_row.IsTaskIdNull = false;
		m_row.IsTaskIdDirty = true;
	}
	bool IsTaskIdNull() const {
		return m_row.IsTaskIdNull;
	}
	void SetTaskIdToNull() {
		m_row.IsTaskIdNull = true;
		m_row.IsTaskIdDirty = true;
	}

	int GetTaskInstanceId() {
		if( m_row.IsTaskInstanceIdNull )
			throw konga::DbNullException("TaskInstanceId");
		return m_row.field_TaskInstanceId;
	}
	void SetTaskInstanceId(int i) {
		m_row.field_TaskInstanceId = i;
		m_row.IsTaskInstanceIdNull = false;
		m_row.IsTaskInstanceIdDirty = true;
	}
	bool IsTaskInstanceIdNull() const {
		return m_row.IsTaskInstanceIdNull;
	}
	void SetTaskInstanceIdToNull() {
		m_row.IsTaskInstanceIdNull = true;
		m_row.IsTaskInstanceIdDirty = true;
	}

	std::string GetMimeContentType() {
		if( m_row.IsMimeContentTypeNull )
			throw konga::DbNullException("MimeContentType");
		return m_row.field_MimeContentType;
	}
	void SetMimeContentType(const std::string& st) {
		m_row.field_MimeContentType = st;
		m_row.IsMimeContentTypeNull = false;
		m_row.IsMimeContentTypeDirty = true;
	}
	bool IsMimeContentTypeNull() const {
		return m_row.IsMimeContentTypeNull;
	}
	void SetMimeContentTypeToNull() {
		m_row.field_MimeContentType = "";
		m_row.IsMimeContentTypeNull = true;
		m_row.IsMimeContentTypeDirty = true;
	}

	std::string GetFileName() {
		if( m_row.IsFileNameNull )
			throw konga::DbNullException("FileName");
		return m_row.field_FileName;
	}
	void SetFileName(const std::string& st) {
		m_row.field_FileName = st;
		m_row.IsFileNameNull = false;
		m_row.IsFileNameDirty = true;
	}
	bool IsFileNameNull() const {
		return m_row.IsFileNameNull;
	}
	void SetFileNameToNull() {
		m_row.field_FileName = "";
		m_row.IsFileNameNull = true;
		m_row.IsFileNameDirty = true;
	}

	int GetDeleteAfterAttached() {
		if( m_row.IsDeleteAfterAttachedNull )
			throw konga::DbNullException("DeleteAfterAttached");
		return m_row.field_DeleteAfterAttached;
	}
	void SetDeleteAfterAttached(int i) {
		m_row.field_DeleteAfterAttached = i;
		m_row.IsDeleteAfterAttachedNull = false;
		m_row.IsDeleteAfterAttachedDirty = true;
	}
	bool IsDeleteAfterAttachedNull() const {
		return m_row.IsDeleteAfterAttachedNull;
	}
	void SetDeleteAfterAttachedToNull() {
		m_row.IsDeleteAfterAttachedNull = true;
		m_row.IsDeleteAfterAttachedDirty = true;
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
	AttachmentTabRow m_row;

private:
	bool IsAttachmentIdDirty() const {
		return m_row.IsAttachmentIdDirty;
	};
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessInstanceIdDirty() const {
		return m_row.IsProcessInstanceIdDirty;
	};
	bool IsTaskIdDirty() const {
		return m_row.IsTaskIdDirty;
	};
	bool IsTaskInstanceIdDirty() const {
		return m_row.IsTaskInstanceIdDirty;
	};
	bool IsMimeContentTypeDirty() const {
		return m_row.IsMimeContentTypeDirty;
	};
	bool IsFileNameDirty() const {
		return m_row.IsFileNameDirty;
	};
	bool IsDeleteAfterAttachedDirty() const {
		return m_row.IsDeleteAfterAttachedDirty;
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
