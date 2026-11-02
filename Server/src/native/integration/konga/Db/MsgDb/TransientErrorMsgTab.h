/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_TRANSIENTERRORMSGTAB_H
#define KONGA_DB_CRMSGDB_TRANSIENTERRORMSGTAB_H

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

class TransientErrorMsgTabRow
{
public:
	TransientErrorMsgTabRow() : 
			field_TranErrMsgId(0),
			IsTranErrMsgIdNull(true),
			IsTranErrMsgIdPrimaryKey(true),
			IsTranErrMsgIdDirty(false),
			field_MsgId(0),
			IsMsgIdNull(true),
			IsMsgIdPrimaryKey(false),
			IsMsgIdDirty(false),
			IsMsgFromNull(true),
			IsMsgFromPrimaryKey(false),
			IsMsgFromDirty(false),
			IsMsgToNull(true),
			IsMsgToPrimaryKey(false),
			IsMsgToDirty(false),
			IsMsgSubjectNull(true),
			IsMsgSubjectPrimaryKey(false),
			IsMsgSubjectDirty(false),
			IsMessageFileNameNull(true),
			IsMessageFileNamePrimaryKey(false),
			IsMessageFileNameDirty(false),
			IsFormatNull(true),
			IsFormatPrimaryKey(false),
			IsFormatDirty(false),
			IsTriedAtNull(true),
			IsTriedAtPrimaryKey(false),
			IsTriedAtDirty(false),
			IsURLNull(true),
			IsURLPrimaryKey(false),
			IsURLDirty(false),
			IsMsgCCNull(true),
			IsMsgCCPrimaryKey(false),
			IsMsgCCDirty(false),
			IsMsgBCCNull(true),
			IsMsgBCCPrimaryKey(false),
			IsMsgBCCDirty(false),
			field_DocumentId(0),
			IsDocumentIdNull(true),
			IsDocumentIdPrimaryKey(false),
			IsDocumentIdDirty(false),
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(false),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(false),
			IsProcessVersionDirty(false),
			IsMhtmlUrlNull(true),
			IsMhtmlUrlPrimaryKey(false),
			IsMhtmlUrlDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(false),
			IsProcessInstanceIdDirty(false),
			field_TaskId(0),
			IsTaskIdNull(true),
			IsTaskIdPrimaryKey(false),
			IsTaskIdDirty(false),
			field_TaskInstanceId(0),
			IsTaskInstanceIdNull(true),
			IsTaskInstanceIdPrimaryKey(false),
			IsTaskInstanceIdDirty(false),
			IsMsgReplyToNull(true),
			IsMsgReplyToPrimaryKey(false),
			IsMsgReplyToDirty(false),
			field_MsgPriority(0),
			IsMsgPriorityNull(true),
			IsMsgPriorityPrimaryKey(false),
			IsMsgPriorityDirty(false),
			field_RetryCounter(0),
			IsRetryCounterNull(true),
			IsRetryCounterPrimaryKey(false),
			IsRetryCounterDirty(false),
			IsStatusNull(true),
			IsStatusPrimaryKey(false),
			IsStatusDirty(false),
			IsErrorStringNull(true),
			IsErrorStringPrimaryKey(false),
			IsErrorStringDirty(false),
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

	void Copy(const TransientErrorMsgTabRow& r)
	{
		field_TranErrMsgId = r.field_TranErrMsgId;
		IsTranErrMsgIdNull = r.IsTranErrMsgIdNull;
		IsTranErrMsgIdDirty = r.IsTranErrMsgIdDirty;

		field_MsgId = r.field_MsgId;
		IsMsgIdNull = r.IsMsgIdNull;
		IsMsgIdDirty = r.IsMsgIdDirty;

		field_MsgFrom = r.field_MsgFrom;
		IsMsgFromNull = r.IsMsgFromNull;
		IsMsgFromDirty = r.IsMsgFromDirty;

		field_MsgTo = r.field_MsgTo;
		IsMsgToNull = r.IsMsgToNull;
		IsMsgToDirty = r.IsMsgToDirty;

		field_MsgSubject = r.field_MsgSubject;
		IsMsgSubjectNull = r.IsMsgSubjectNull;
		IsMsgSubjectDirty = r.IsMsgSubjectDirty;

		field_MessageFileName = r.field_MessageFileName;
		IsMessageFileNameNull = r.IsMessageFileNameNull;
		IsMessageFileNameDirty = r.IsMessageFileNameDirty;

		field_Format = r.field_Format;
		IsFormatNull = r.IsFormatNull;
		IsFormatDirty = r.IsFormatDirty;

		field_TriedAt = r.field_TriedAt;
		IsTriedAtNull = r.IsTriedAtNull;
		IsTriedAtDirty = r.IsTriedAtDirty;

		field_URL = r.field_URL;
		IsURLNull = r.IsURLNull;
		IsURLDirty = r.IsURLDirty;

		field_MsgCC = r.field_MsgCC;
		IsMsgCCNull = r.IsMsgCCNull;
		IsMsgCCDirty = r.IsMsgCCDirty;

		field_MsgBCC = r.field_MsgBCC;
		IsMsgBCCNull = r.IsMsgBCCNull;
		IsMsgBCCDirty = r.IsMsgBCCDirty;

		field_DocumentId = r.field_DocumentId;
		IsDocumentIdNull = r.IsDocumentIdNull;
		IsDocumentIdDirty = r.IsDocumentIdDirty;

		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_MhtmlUrl = r.field_MhtmlUrl;
		IsMhtmlUrlNull = r.IsMhtmlUrlNull;
		IsMhtmlUrlDirty = r.IsMhtmlUrlDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_TaskInstanceId = r.field_TaskInstanceId;
		IsTaskInstanceIdNull = r.IsTaskInstanceIdNull;
		IsTaskInstanceIdDirty = r.IsTaskInstanceIdDirty;

		field_MsgReplyTo = r.field_MsgReplyTo;
		IsMsgReplyToNull = r.IsMsgReplyToNull;
		IsMsgReplyToDirty = r.IsMsgReplyToDirty;

		field_MsgPriority = r.field_MsgPriority;
		IsMsgPriorityNull = r.IsMsgPriorityNull;
		IsMsgPriorityDirty = r.IsMsgPriorityDirty;

		field_RetryCounter = r.field_RetryCounter;
		IsRetryCounterNull = r.IsRetryCounterNull;
		IsRetryCounterDirty = r.IsRetryCounterDirty;

		field_Status = r.field_Status;
		IsStatusNull = r.IsStatusNull;
		IsStatusDirty = r.IsStatusDirty;

		field_ErrorString = r.field_ErrorString;
		IsErrorStringNull = r.IsErrorStringNull;
		IsErrorStringDirty = r.IsErrorStringDirty;

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

	void CopyPKs(const TransientErrorMsgTabRow& r)
	{
		field_TranErrMsgId = r.field_TranErrMsgId;
		IsTranErrMsgIdNull = r.IsTranErrMsgIdNull;
		IsTranErrMsgIdDirty = r.IsTranErrMsgIdDirty;

	}

	void SetDirty(bool b)
	{
		IsTranErrMsgIdDirty = b;
		IsMsgIdDirty = b;
		IsMsgFromDirty = b;
		IsMsgToDirty = b;
		IsMsgSubjectDirty = b;
		IsMessageFileNameDirty = b;
		IsFormatDirty = b;
		IsTriedAtDirty = b;
		IsURLDirty = b;
		IsMsgCCDirty = b;
		IsMsgBCCDirty = b;
		IsDocumentIdDirty = b;
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsMhtmlUrlDirty = b;
		IsProcessInstanceIdDirty = b;
		IsTaskIdDirty = b;
		IsTaskInstanceIdDirty = b;
		IsMsgReplyToDirty = b;
		IsMsgPriorityDirty = b;
		IsRetryCounterDirty = b;
		IsStatusDirty = b;
		IsErrorStringDirty = b;
		IsOwnerDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsTranErrMsgIdDirty ||
			IsMsgIdDirty ||
			IsMsgFromDirty ||
			IsMsgToDirty ||
			IsMsgSubjectDirty ||
			IsMessageFileNameDirty ||
			IsFormatDirty ||
			IsTriedAtDirty ||
			IsURLDirty ||
			IsMsgCCDirty ||
			IsMsgBCCDirty ||
			IsDocumentIdDirty ||
			IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsMhtmlUrlDirty ||
			IsProcessInstanceIdDirty ||
			IsTaskIdDirty ||
			IsTaskInstanceIdDirty ||
			IsMsgReplyToDirty ||
			IsMsgPriorityDirty ||
			IsRetryCounterDirty ||
			IsStatusDirty ||
			IsErrorStringDirty ||
			IsOwnerDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_TranErrMsgId;
	bool IsTranErrMsgIdNull;
	bool IsTranErrMsgIdDirty;

	int field_MsgId;
	bool IsMsgIdNull;
	bool IsMsgIdDirty;

	std::string field_MsgFrom;
	bool IsMsgFromNull;
	bool IsMsgFromDirty;

	std::string field_MsgTo;
	bool IsMsgToNull;
	bool IsMsgToDirty;

	std::string field_MsgSubject;
	bool IsMsgSubjectNull;
	bool IsMsgSubjectDirty;

	std::string field_MessageFileName;
	bool IsMessageFileNameNull;
	bool IsMessageFileNameDirty;

	std::string field_Format;
	bool IsFormatNull;
	bool IsFormatDirty;

	std::string field_TriedAt;
	bool IsTriedAtNull;
	bool IsTriedAtDirty;

	std::string field_URL;
	bool IsURLNull;
	bool IsURLDirty;

	std::string field_MsgCC;
	bool IsMsgCCNull;
	bool IsMsgCCDirty;

	std::string field_MsgBCC;
	bool IsMsgBCCNull;
	bool IsMsgBCCDirty;

	int field_DocumentId;
	bool IsDocumentIdNull;
	bool IsDocumentIdDirty;

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	std::string field_MhtmlUrl;
	bool IsMhtmlUrlNull;
	bool IsMhtmlUrlDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

	int field_TaskId;
	bool IsTaskIdNull;
	bool IsTaskIdDirty;

	int field_TaskInstanceId;
	bool IsTaskInstanceIdNull;
	bool IsTaskInstanceIdDirty;

	std::string field_MsgReplyTo;
	bool IsMsgReplyToNull;
	bool IsMsgReplyToDirty;

	int field_MsgPriority;
	bool IsMsgPriorityNull;
	bool IsMsgPriorityDirty;

	int field_RetryCounter;
	bool IsRetryCounterNull;
	bool IsRetryCounterDirty;

	std::string field_Status;
	bool IsStatusNull;
	bool IsStatusDirty;

	std::string field_ErrorString;
	bool IsErrorStringNull;
	bool IsErrorStringDirty;

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
	bool IsTranErrMsgIdPrimaryKey;
	bool IsMsgIdPrimaryKey;
	bool IsMsgFromPrimaryKey;
	bool IsMsgToPrimaryKey;
	bool IsMsgSubjectPrimaryKey;
	bool IsMessageFileNamePrimaryKey;
	bool IsFormatPrimaryKey;
	bool IsTriedAtPrimaryKey;
	bool IsURLPrimaryKey;
	bool IsMsgCCPrimaryKey;
	bool IsMsgBCCPrimaryKey;
	bool IsDocumentIdPrimaryKey;
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsMhtmlUrlPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsTaskIdPrimaryKey;
	bool IsTaskInstanceIdPrimaryKey;
	bool IsMsgReplyToPrimaryKey;
	bool IsMsgPriorityPrimaryKey;
	bool IsRetryCounterPrimaryKey;
	bool IsStatusPrimaryKey;
	bool IsErrorStringPrimaryKey;
	bool IsOwnerPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class TransientErrorMsgTab : public konga::DbRecordSet {
public:
	TransientErrorMsgTab() {}

	static TransientErrorMsgTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TransientErrorMsgTab";
	}

	void DoFieldExchange(const TransientErrorMsgTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetTranErrMsgId() {
		if( m_row.IsTranErrMsgIdNull )
			throw konga::DbNullException("TranErrMsgId");
		return m_row.field_TranErrMsgId;
	}
	void SetTranErrMsgId(int i) {
		m_row.field_TranErrMsgId = i;
		m_row.IsTranErrMsgIdNull = false;
		m_row.IsTranErrMsgIdDirty = true;
	}
	bool IsTranErrMsgIdNull() const {
		return m_row.IsTranErrMsgIdNull;
	}
	void SetTranErrMsgIdToNull() {
		m_row.IsTranErrMsgIdNull = true;
		m_row.IsTranErrMsgIdDirty = true;
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

	std::string GetMessageFileName() {
		if( m_row.IsMessageFileNameNull )
			throw konga::DbNullException("MessageFileName");
		return m_row.field_MessageFileName;
	}
	void SetMessageFileName(const std::string& st) {
		m_row.field_MessageFileName = st;
		m_row.IsMessageFileNameNull = false;
		m_row.IsMessageFileNameDirty = true;
	}
	bool IsMessageFileNameNull() const {
		return m_row.IsMessageFileNameNull;
	}
	void SetMessageFileNameToNull() {
		m_row.field_MessageFileName = "";
		m_row.IsMessageFileNameNull = true;
		m_row.IsMessageFileNameDirty = true;
	}

	std::string GetFormat() {
		if( m_row.IsFormatNull )
			throw konga::DbNullException("Format");
		return m_row.field_Format;
	}
	void SetFormat(const std::string& st) {
		m_row.field_Format = st;
		m_row.IsFormatNull = false;
		m_row.IsFormatDirty = true;
	}
	bool IsFormatNull() const {
		return m_row.IsFormatNull;
	}
	void SetFormatToNull() {
		m_row.field_Format = "";
		m_row.IsFormatNull = true;
		m_row.IsFormatDirty = true;
	}

	std::string GetTriedAt() {
		if( m_row.IsTriedAtNull )
			throw konga::DbNullException("TriedAt");
		return m_row.field_TriedAt;
	}
	void SetTriedAt(const std::string& st) {
		m_row.field_TriedAt = st;
		m_row.IsTriedAtNull = false;
		m_row.IsTriedAtDirty = true;
	}
	bool IsTriedAtNull() const {
		return m_row.IsTriedAtNull;
	}
	void SetTriedAtToNull() {
		m_row.field_TriedAt = "";
		m_row.IsTriedAtNull = true;
		m_row.IsTriedAtDirty = true;
	}

	std::string GetURL() {
		if( m_row.IsURLNull )
			throw konga::DbNullException("URL");
		return m_row.field_URL;
	}
	void SetURL(const std::string& st) {
		m_row.field_URL = st;
		m_row.IsURLNull = false;
		m_row.IsURLDirty = true;
	}
	bool IsURLNull() const {
		return m_row.IsURLNull;
	}
	void SetURLToNull() {
		m_row.field_URL = "";
		m_row.IsURLNull = true;
		m_row.IsURLDirty = true;
	}

	std::string GetMsgCC() {
		if( m_row.IsMsgCCNull )
			throw konga::DbNullException("MsgCC");
		return m_row.field_MsgCC;
	}
	void SetMsgCC(const std::string& st) {
		m_row.field_MsgCC = st;
		m_row.IsMsgCCNull = false;
		m_row.IsMsgCCDirty = true;
	}
	bool IsMsgCCNull() const {
		return m_row.IsMsgCCNull;
	}
	void SetMsgCCToNull() {
		m_row.field_MsgCC = "";
		m_row.IsMsgCCNull = true;
		m_row.IsMsgCCDirty = true;
	}

	std::string GetMsgBCC() {
		if( m_row.IsMsgBCCNull )
			throw konga::DbNullException("MsgBCC");
		return m_row.field_MsgBCC;
	}
	void SetMsgBCC(const std::string& st) {
		m_row.field_MsgBCC = st;
		m_row.IsMsgBCCNull = false;
		m_row.IsMsgBCCDirty = true;
	}
	bool IsMsgBCCNull() const {
		return m_row.IsMsgBCCNull;
	}
	void SetMsgBCCToNull() {
		m_row.field_MsgBCC = "";
		m_row.IsMsgBCCNull = true;
		m_row.IsMsgBCCDirty = true;
	}

	int GetDocumentId() {
		if( m_row.IsDocumentIdNull )
			throw konga::DbNullException("DocumentId");
		return m_row.field_DocumentId;
	}
	void SetDocumentId(int i) {
		m_row.field_DocumentId = i;
		m_row.IsDocumentIdNull = false;
		m_row.IsDocumentIdDirty = true;
	}
	bool IsDocumentIdNull() const {
		return m_row.IsDocumentIdNull;
	}
	void SetDocumentIdToNull() {
		m_row.IsDocumentIdNull = true;
		m_row.IsDocumentIdDirty = true;
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

	int GetProcessVersion() {
		if( m_row.IsProcessVersionNull )
			throw konga::DbNullException("ProcessVersion");
		return m_row.field_ProcessVersion;
	}
	void SetProcessVersion(int i) {
		m_row.field_ProcessVersion = i;
		m_row.IsProcessVersionNull = false;
		m_row.IsProcessVersionDirty = true;
	}
	bool IsProcessVersionNull() const {
		return m_row.IsProcessVersionNull;
	}
	void SetProcessVersionToNull() {
		m_row.IsProcessVersionNull = true;
		m_row.IsProcessVersionDirty = true;
	}

	std::string GetMhtmlUrl() {
		if( m_row.IsMhtmlUrlNull )
			throw konga::DbNullException("MhtmlUrl");
		return m_row.field_MhtmlUrl;
	}
	void SetMhtmlUrl(const std::string& st) {
		m_row.field_MhtmlUrl = st;
		m_row.IsMhtmlUrlNull = false;
		m_row.IsMhtmlUrlDirty = true;
	}
	bool IsMhtmlUrlNull() const {
		return m_row.IsMhtmlUrlNull;
	}
	void SetMhtmlUrlToNull() {
		m_row.field_MhtmlUrl = "";
		m_row.IsMhtmlUrlNull = true;
		m_row.IsMhtmlUrlDirty = true;
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

	int GetRetryCounter() {
		if( m_row.IsRetryCounterNull )
			throw konga::DbNullException("RetryCounter");
		return m_row.field_RetryCounter;
	}
	void SetRetryCounter(int i) {
		m_row.field_RetryCounter = i;
		m_row.IsRetryCounterNull = false;
		m_row.IsRetryCounterDirty = true;
	}
	bool IsRetryCounterNull() const {
		return m_row.IsRetryCounterNull;
	}
	void SetRetryCounterToNull() {
		m_row.IsRetryCounterNull = true;
		m_row.IsRetryCounterDirty = true;
	}

	std::string GetStatus() {
		if( m_row.IsStatusNull )
			throw konga::DbNullException("Status");
		return m_row.field_Status;
	}
	void SetStatus(const std::string& st) {
		m_row.field_Status = st;
		m_row.IsStatusNull = false;
		m_row.IsStatusDirty = true;
	}
	bool IsStatusNull() const {
		return m_row.IsStatusNull;
	}
	void SetStatusToNull() {
		m_row.field_Status = "";
		m_row.IsStatusNull = true;
		m_row.IsStatusDirty = true;
	}

	std::string GetErrorString() {
		if( m_row.IsErrorStringNull )
			throw konga::DbNullException("ErrorString");
		return m_row.field_ErrorString;
	}
	void SetErrorString(const std::string& st) {
		m_row.field_ErrorString = st;
		m_row.IsErrorStringNull = false;
		m_row.IsErrorStringDirty = true;
	}
	bool IsErrorStringNull() const {
		return m_row.IsErrorStringNull;
	}
	void SetErrorStringToNull() {
		m_row.field_ErrorString = "";
		m_row.IsErrorStringNull = true;
		m_row.IsErrorStringDirty = true;
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
	TransientErrorMsgTabRow m_row;

private:
	bool IsTranErrMsgIdDirty() const {
		return m_row.IsTranErrMsgIdDirty;
	};
	bool IsMsgIdDirty() const {
		return m_row.IsMsgIdDirty;
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
	bool IsMessageFileNameDirty() const {
		return m_row.IsMessageFileNameDirty;
	};
	bool IsFormatDirty() const {
		return m_row.IsFormatDirty;
	};
	bool IsTriedAtDirty() const {
		return m_row.IsTriedAtDirty;
	};
	bool IsURLDirty() const {
		return m_row.IsURLDirty;
	};
	bool IsMsgCCDirty() const {
		return m_row.IsMsgCCDirty;
	};
	bool IsMsgBCCDirty() const {
		return m_row.IsMsgBCCDirty;
	};
	bool IsDocumentIdDirty() const {
		return m_row.IsDocumentIdDirty;
	};
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsMhtmlUrlDirty() const {
		return m_row.IsMhtmlUrlDirty;
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
	bool IsMsgReplyToDirty() const {
		return m_row.IsMsgReplyToDirty;
	};
	bool IsMsgPriorityDirty() const {
		return m_row.IsMsgPriorityDirty;
	};
	bool IsRetryCounterDirty() const {
		return m_row.IsRetryCounterDirty;
	};
	bool IsStatusDirty() const {
		return m_row.IsStatusDirty;
	};
	bool IsErrorStringDirty() const {
		return m_row.IsErrorStringDirty;
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
