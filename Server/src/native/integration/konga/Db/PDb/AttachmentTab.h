/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_ATTACHMENTTAB_H
#define KONGA_DB_CRPDB_ATTACHMENTTAB_H

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
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
			field_DocumentId(0),
			IsDocumentIdNull(true),
			IsDocumentIdPrimaryKey(true),
			IsDocumentIdDirty(false),
			field_TransferId(0),
			IsTransferIdNull(true),
			IsTransferIdPrimaryKey(false),
			IsTransferIdDirty(false),
			IsMimeContentTypeNull(true),
			IsMimeContentTypePrimaryKey(false),
			IsMimeContentTypeDirty(false),
			IsFileNameNull(true),
			IsFileNamePrimaryKey(false),
			IsFileNameDirty(false),
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

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_DocumentId = r.field_DocumentId;
		IsDocumentIdNull = r.IsDocumentIdNull;
		IsDocumentIdDirty = r.IsDocumentIdDirty;

		field_TransferId = r.field_TransferId;
		IsTransferIdNull = r.IsTransferIdNull;
		IsTransferIdDirty = r.IsTransferIdDirty;

		field_MimeContentType = r.field_MimeContentType;
		IsMimeContentTypeNull = r.IsMimeContentTypeNull;
		IsMimeContentTypeDirty = r.IsMimeContentTypeDirty;

		field_FileName = r.field_FileName;
		IsFileNameNull = r.IsFileNameNull;
		IsFileNameDirty = r.IsFileNameDirty;

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

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_DocumentId = r.field_DocumentId;
		IsDocumentIdNull = r.IsDocumentIdNull;
		IsDocumentIdDirty = r.IsDocumentIdDirty;

	}

	void SetDirty(bool b)
	{
		IsAttachmentIdDirty = b;
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsDocumentIdDirty = b;
		IsTransferIdDirty = b;
		IsMimeContentTypeDirty = b;
		IsFileNameDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsAttachmentIdDirty ||
			IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsDocumentIdDirty ||
			IsTransferIdDirty ||
			IsMimeContentTypeDirty ||
			IsFileNameDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_AttachmentId;
	bool IsAttachmentIdNull;
	bool IsAttachmentIdDirty;

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_DocumentId;
	bool IsDocumentIdNull;
	bool IsDocumentIdDirty;

	int field_TransferId;
	bool IsTransferIdNull;
	bool IsTransferIdDirty;

	std::string field_MimeContentType;
	bool IsMimeContentTypeNull;
	bool IsMimeContentTypeDirty;

	std::string field_FileName;
	bool IsFileNameNull;
	bool IsFileNameDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsAttachmentIdPrimaryKey;
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsDocumentIdPrimaryKey;
	bool IsTransferIdPrimaryKey;
	bool IsMimeContentTypePrimaryKey;
	bool IsFileNamePrimaryKey;
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

	int GetTransferId() {
		if( m_row.IsTransferIdNull )
			throw konga::DbNullException("TransferId");
		return m_row.field_TransferId;
	}
	void SetTransferId(int i) {
		m_row.field_TransferId = i;
		m_row.IsTransferIdNull = false;
		m_row.IsTransferIdDirty = true;
	}
	bool IsTransferIdNull() const {
		return m_row.IsTransferIdNull;
	}
	void SetTransferIdToNull() {
		m_row.IsTransferIdNull = true;
		m_row.IsTransferIdDirty = true;
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
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsDocumentIdDirty() const {
		return m_row.IsDocumentIdDirty;
	};
	bool IsTransferIdDirty() const {
		return m_row.IsTransferIdDirty;
	};
	bool IsMimeContentTypeDirty() const {
		return m_row.IsMimeContentTypeDirty;
	};
	bool IsFileNameDirty() const {
		return m_row.IsFileNameDirty;
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
