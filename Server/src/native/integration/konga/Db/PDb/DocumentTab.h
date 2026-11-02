/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_DOCUMENTTAB_H
#define KONGA_DB_CRPDB_DOCUMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace PDb
{

class DocumentTabRow
{
public:
	DocumentTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
			field_TaskId(0),
			IsTaskIdNull(true),
			IsTaskIdPrimaryKey(true),
			IsTaskIdDirty(false),
			field_DocumentId(0),
			IsDocumentIdNull(true),
			IsDocumentIdPrimaryKey(true),
			IsDocumentIdDirty(false),
			IsMsgTemplateNameNull(true),
			IsMsgTemplateNamePrimaryKey(false),
			IsMsgTemplateNameDirty(false),
			IsMsgTemplateLocationNull(true),
			IsMsgTemplateLocationPrimaryKey(false),
			IsMsgTemplateLocationDirty(false),
			IsURLNull(true),
			IsURLPrimaryKey(false),
			IsURLDirty(false),
			IsSendFormatNull(true),
			IsSendFormatPrimaryKey(false),
			IsSendFormatDirty(false),
			IsToDocDataElementListNull(true),
			IsToDocDataElementListPrimaryKey(false),
			IsToDocDataElementListDirty(false),
			IsFromDocDataElementListNull(true),
			IsFromDocDataElementListPrimaryKey(false),
			IsFromDocDataElementListDirty(false),
			IsMsgSubjectNull(true),
			IsMsgSubjectPrimaryKey(false),
			IsMsgSubjectDirty(false),
			IsMsgCCNull(true),
			IsMsgCCPrimaryKey(false),
			IsMsgCCDirty(false),
			IsMsgBCCNull(true),
			IsMsgBCCPrimaryKey(false),
			IsMsgBCCDirty(false),
			field_MsgPriority(0),
			IsMsgPriorityNull(true),
			IsMsgPriorityPrimaryKey(false),
			IsMsgPriorityDirty(false),
			IsMsgReplyToNull(true),
			IsMsgReplyToPrimaryKey(false),
			IsMsgReplyToDirty(false),
			IsMsgFromNull(true),
			IsMsgFromPrimaryKey(false),
			IsMsgFromDirty(false),
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

	void Copy(const DocumentTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_DocumentId = r.field_DocumentId;
		IsDocumentIdNull = r.IsDocumentIdNull;
		IsDocumentIdDirty = r.IsDocumentIdDirty;

		field_MsgTemplateName = r.field_MsgTemplateName;
		IsMsgTemplateNameNull = r.IsMsgTemplateNameNull;
		IsMsgTemplateNameDirty = r.IsMsgTemplateNameDirty;

		field_MsgTemplateLocation = r.field_MsgTemplateLocation;
		IsMsgTemplateLocationNull = r.IsMsgTemplateLocationNull;
		IsMsgTemplateLocationDirty = r.IsMsgTemplateLocationDirty;

		field_URL = r.field_URL;
		IsURLNull = r.IsURLNull;
		IsURLDirty = r.IsURLDirty;

		field_SendFormat = r.field_SendFormat;
		IsSendFormatNull = r.IsSendFormatNull;
		IsSendFormatDirty = r.IsSendFormatDirty;

		field_ToDocDataElementList = r.field_ToDocDataElementList;
		IsToDocDataElementListNull = r.IsToDocDataElementListNull;
		IsToDocDataElementListDirty = r.IsToDocDataElementListDirty;

		field_FromDocDataElementList = r.field_FromDocDataElementList;
		IsFromDocDataElementListNull = r.IsFromDocDataElementListNull;
		IsFromDocDataElementListDirty = r.IsFromDocDataElementListDirty;

		field_MsgSubject = r.field_MsgSubject;
		IsMsgSubjectNull = r.IsMsgSubjectNull;
		IsMsgSubjectDirty = r.IsMsgSubjectDirty;

		field_MsgCC = r.field_MsgCC;
		IsMsgCCNull = r.IsMsgCCNull;
		IsMsgCCDirty = r.IsMsgCCDirty;

		field_MsgBCC = r.field_MsgBCC;
		IsMsgBCCNull = r.IsMsgBCCNull;
		IsMsgBCCDirty = r.IsMsgBCCDirty;

		field_MsgPriority = r.field_MsgPriority;
		IsMsgPriorityNull = r.IsMsgPriorityNull;
		IsMsgPriorityDirty = r.IsMsgPriorityDirty;

		field_MsgReplyTo = r.field_MsgReplyTo;
		IsMsgReplyToNull = r.IsMsgReplyToNull;
		IsMsgReplyToDirty = r.IsMsgReplyToDirty;

		field_MsgFrom = r.field_MsgFrom;
		IsMsgFromNull = r.IsMsgFromNull;
		IsMsgFromDirty = r.IsMsgFromDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const DocumentTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_DocumentId = r.field_DocumentId;
		IsDocumentIdNull = r.IsDocumentIdNull;
		IsDocumentIdDirty = r.IsDocumentIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsTaskIdDirty = b;
		IsDocumentIdDirty = b;
		IsMsgTemplateNameDirty = b;
		IsMsgTemplateLocationDirty = b;
		IsURLDirty = b;
		IsSendFormatDirty = b;
		IsToDocDataElementListDirty = b;
		IsFromDocDataElementListDirty = b;
		IsMsgSubjectDirty = b;
		IsMsgCCDirty = b;
		IsMsgBCCDirty = b;
		IsMsgPriorityDirty = b;
		IsMsgReplyToDirty = b;
		IsMsgFromDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsTaskIdDirty ||
			IsDocumentIdDirty ||
			IsMsgTemplateNameDirty ||
			IsMsgTemplateLocationDirty ||
			IsURLDirty ||
			IsSendFormatDirty ||
			IsToDocDataElementListDirty ||
			IsFromDocDataElementListDirty ||
			IsMsgSubjectDirty ||
			IsMsgCCDirty ||
			IsMsgBCCDirty ||
			IsMsgPriorityDirty ||
			IsMsgReplyToDirty ||
			IsMsgFromDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_TaskId;
	bool IsTaskIdNull;
	bool IsTaskIdDirty;

	int field_DocumentId;
	bool IsDocumentIdNull;
	bool IsDocumentIdDirty;

	std::string field_MsgTemplateName;
	bool IsMsgTemplateNameNull;
	bool IsMsgTemplateNameDirty;

	std::string field_MsgTemplateLocation;
	bool IsMsgTemplateLocationNull;
	bool IsMsgTemplateLocationDirty;

	std::string field_URL;
	bool IsURLNull;
	bool IsURLDirty;

	std::string field_SendFormat;
	bool IsSendFormatNull;
	bool IsSendFormatDirty;

	std::string field_ToDocDataElementList;
	bool IsToDocDataElementListNull;
	bool IsToDocDataElementListDirty;

	std::string field_FromDocDataElementList;
	bool IsFromDocDataElementListNull;
	bool IsFromDocDataElementListDirty;

	std::string field_MsgSubject;
	bool IsMsgSubjectNull;
	bool IsMsgSubjectDirty;

	std::string field_MsgCC;
	bool IsMsgCCNull;
	bool IsMsgCCDirty;

	std::string field_MsgBCC;
	bool IsMsgBCCNull;
	bool IsMsgBCCDirty;

	int field_MsgPriority;
	bool IsMsgPriorityNull;
	bool IsMsgPriorityDirty;

	std::string field_MsgReplyTo;
	bool IsMsgReplyToNull;
	bool IsMsgReplyToDirty;

	std::string field_MsgFrom;
	bool IsMsgFromNull;
	bool IsMsgFromDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsTaskIdPrimaryKey;
	bool IsDocumentIdPrimaryKey;
	bool IsMsgTemplateNamePrimaryKey;
	bool IsMsgTemplateLocationPrimaryKey;
	bool IsURLPrimaryKey;
	bool IsSendFormatPrimaryKey;
	bool IsToDocDataElementListPrimaryKey;
	bool IsFromDocDataElementListPrimaryKey;
	bool IsMsgSubjectPrimaryKey;
	bool IsMsgCCPrimaryKey;
	bool IsMsgBCCPrimaryKey;
	bool IsMsgPriorityPrimaryKey;
	bool IsMsgReplyToPrimaryKey;
	bool IsMsgFromPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class DocumentTab : public konga::DbRecordSet {
public:
	DocumentTab() {}

	static DocumentTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "DocumentTab";
	}

	void DoFieldExchange(const DocumentTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	std::string GetMsgTemplateName() {
		if( m_row.IsMsgTemplateNameNull )
			throw konga::DbNullException("MsgTemplateName");
		return m_row.field_MsgTemplateName;
	}
	void SetMsgTemplateName(const std::string& st) {
		m_row.field_MsgTemplateName = st;
		m_row.IsMsgTemplateNameNull = false;
		m_row.IsMsgTemplateNameDirty = true;
	}
	bool IsMsgTemplateNameNull() const {
		return m_row.IsMsgTemplateNameNull;
	}
	void SetMsgTemplateNameToNull() {
		m_row.field_MsgTemplateName = "";
		m_row.IsMsgTemplateNameNull = true;
		m_row.IsMsgTemplateNameDirty = true;
	}

	std::string GetMsgTemplateLocation() {
		if( m_row.IsMsgTemplateLocationNull )
			throw konga::DbNullException("MsgTemplateLocation");
		return m_row.field_MsgTemplateLocation;
	}
	void SetMsgTemplateLocation(const std::string& st) {
		m_row.field_MsgTemplateLocation = st;
		m_row.IsMsgTemplateLocationNull = false;
		m_row.IsMsgTemplateLocationDirty = true;
	}
	bool IsMsgTemplateLocationNull() const {
		return m_row.IsMsgTemplateLocationNull;
	}
	void SetMsgTemplateLocationToNull() {
		m_row.field_MsgTemplateLocation = "";
		m_row.IsMsgTemplateLocationNull = true;
		m_row.IsMsgTemplateLocationDirty = true;
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

	std::string GetSendFormat() {
		if( m_row.IsSendFormatNull )
			throw konga::DbNullException("SendFormat");
		return m_row.field_SendFormat;
	}
	void SetSendFormat(const std::string& st) {
		m_row.field_SendFormat = st;
		m_row.IsSendFormatNull = false;
		m_row.IsSendFormatDirty = true;
	}
	bool IsSendFormatNull() const {
		return m_row.IsSendFormatNull;
	}
	void SetSendFormatToNull() {
		m_row.field_SendFormat = "";
		m_row.IsSendFormatNull = true;
		m_row.IsSendFormatDirty = true;
	}

	std::string GetToDocDataElementList() {
		if( m_row.IsToDocDataElementListNull )
			throw konga::DbNullException("ToDocDataElementList");
		return m_row.field_ToDocDataElementList;
	}
	void SetToDocDataElementList(const std::string& st) {
		m_row.field_ToDocDataElementList = st;
		m_row.IsToDocDataElementListNull = false;
		m_row.IsToDocDataElementListDirty = true;
	}
	bool IsToDocDataElementListNull() const {
		return m_row.IsToDocDataElementListNull;
	}
	void SetToDocDataElementListToNull() {
		m_row.field_ToDocDataElementList = "";
		m_row.IsToDocDataElementListNull = true;
		m_row.IsToDocDataElementListDirty = true;
	}

	std::string GetFromDocDataElementList() {
		if( m_row.IsFromDocDataElementListNull )
			throw konga::DbNullException("FromDocDataElementList");
		return m_row.field_FromDocDataElementList;
	}
	void SetFromDocDataElementList(const std::string& st) {
		m_row.field_FromDocDataElementList = st;
		m_row.IsFromDocDataElementListNull = false;
		m_row.IsFromDocDataElementListDirty = true;
	}
	bool IsFromDocDataElementListNull() const {
		return m_row.IsFromDocDataElementListNull;
	}
	void SetFromDocDataElementListToNull() {
		m_row.field_FromDocDataElementList = "";
		m_row.IsFromDocDataElementListNull = true;
		m_row.IsFromDocDataElementListDirty = true;
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
	DocumentTabRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsTaskIdDirty() const {
		return m_row.IsTaskIdDirty;
	};
	bool IsDocumentIdDirty() const {
		return m_row.IsDocumentIdDirty;
	};
	bool IsMsgTemplateNameDirty() const {
		return m_row.IsMsgTemplateNameDirty;
	};
	bool IsMsgTemplateLocationDirty() const {
		return m_row.IsMsgTemplateLocationDirty;
	};
	bool IsURLDirty() const {
		return m_row.IsURLDirty;
	};
	bool IsSendFormatDirty() const {
		return m_row.IsSendFormatDirty;
	};
	bool IsToDocDataElementListDirty() const {
		return m_row.IsToDocDataElementListDirty;
	};
	bool IsFromDocDataElementListDirty() const {
		return m_row.IsFromDocDataElementListDirty;
	};
	bool IsMsgSubjectDirty() const {
		return m_row.IsMsgSubjectDirty;
	};
	bool IsMsgCCDirty() const {
		return m_row.IsMsgCCDirty;
	};
	bool IsMsgBCCDirty() const {
		return m_row.IsMsgBCCDirty;
	};
	bool IsMsgPriorityDirty() const {
		return m_row.IsMsgPriorityDirty;
	};
	bool IsMsgReplyToDirty() const {
		return m_row.IsMsgReplyToDirty;
	};
	bool IsMsgFromDirty() const {
		return m_row.IsMsgFromDirty;
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
