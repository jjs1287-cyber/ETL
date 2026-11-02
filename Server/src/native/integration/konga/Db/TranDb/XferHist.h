/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_XFERHIST_H
#define KONGA_DB_CRTRANDB_XFERHIST_H

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

class XferHistRow
{
public:
	XferHistRow() : 
			field_ID(0),
			IsIDNull(true),
			IsIDPrimaryKey(true),
			IsIDDirty(false),
			IsDirEntryNull(true),
			IsDirEntryPrimaryKey(false),
			IsDirEntryDirty(false),
			IsFileNameNull(true),
			IsFileNamePrimaryKey(false),
			IsFileNameDirty(false),
			field_OperationID(0),
			IsOperationIDNull(true),
			IsOperationIDPrimaryKey(false),
			IsOperationIDDirty(false),
			field_FileSize(0),
			IsFileSizeNull(true),
			IsFileSizePrimaryKey(false),
			IsFileSizeDirty(false),
			field_FileMonth(0),
			IsFileMonthNull(true),
			IsFileMonthPrimaryKey(false),
			IsFileMonthDirty(false),
			field_FileDay(0),
			IsFileDayNull(true),
			IsFileDayPrimaryKey(false),
			IsFileDayDirty(false),
			field_FileTimeOrYear(0),
			IsFileTimeOrYearNull(true),
			IsFileTimeOrYearPrimaryKey(false),
			IsFileTimeOrYearDirty(false),
			IsXferDateTimeNull(true),
			IsXferDateTimePrimaryKey(false),
			IsXferDateTimeDirty(false),
			field_ProgressStatus(0),
			IsProgressStatusNull(true),
			IsProgressStatusPrimaryKey(false),
			IsProgressStatusDirty(false),
			field_RetryCount(0),
			IsRetryCountNull(true),
			IsRetryCountPrimaryKey(false),
			IsRetryCountDirty(false),
			IsLocalFileNameNull(true),
			IsLocalFileNamePrimaryKey(false),
			IsLocalFileNameDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false)
	{
		field_XferDateTime.year = 0;
		field_XferDateTime.month = 0;
		field_XferDateTime.day = 0;
		field_XferDateTime.hour = 0;
		field_XferDateTime.minute = 0;
		field_XferDateTime.second = 0;
		field_XferDateTime.fraction = 0;
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

	void Copy(const XferHistRow& r)
	{
		field_ID = r.field_ID;
		IsIDNull = r.IsIDNull;
		IsIDDirty = r.IsIDDirty;

		field_DirEntry = r.field_DirEntry;
		IsDirEntryNull = r.IsDirEntryNull;
		IsDirEntryDirty = r.IsDirEntryDirty;

		field_FileName = r.field_FileName;
		IsFileNameNull = r.IsFileNameNull;
		IsFileNameDirty = r.IsFileNameDirty;

		field_OperationID = r.field_OperationID;
		IsOperationIDNull = r.IsOperationIDNull;
		IsOperationIDDirty = r.IsOperationIDDirty;

		field_FileSize = r.field_FileSize;
		IsFileSizeNull = r.IsFileSizeNull;
		IsFileSizeDirty = r.IsFileSizeDirty;

		field_FileMonth = r.field_FileMonth;
		IsFileMonthNull = r.IsFileMonthNull;
		IsFileMonthDirty = r.IsFileMonthDirty;

		field_FileDay = r.field_FileDay;
		IsFileDayNull = r.IsFileDayNull;
		IsFileDayDirty = r.IsFileDayDirty;

		field_FileTimeOrYear = r.field_FileTimeOrYear;
		IsFileTimeOrYearNull = r.IsFileTimeOrYearNull;
		IsFileTimeOrYearDirty = r.IsFileTimeOrYearDirty;

		field_XferDateTime = r.field_XferDateTime;
		IsXferDateTimeNull = r.IsXferDateTimeNull;
		IsXferDateTimeDirty = r.IsXferDateTimeDirty;

		field_ProgressStatus = r.field_ProgressStatus;
		IsProgressStatusNull = r.IsProgressStatusNull;
		IsProgressStatusDirty = r.IsProgressStatusDirty;

		field_RetryCount = r.field_RetryCount;
		IsRetryCountNull = r.IsRetryCountNull;
		IsRetryCountDirty = r.IsRetryCountDirty;

		field_LocalFileName = r.field_LocalFileName;
		IsLocalFileNameNull = r.IsLocalFileNameNull;
		IsLocalFileNameDirty = r.IsLocalFileNameDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const XferHistRow& r)
	{
		field_ID = r.field_ID;
		IsIDNull = r.IsIDNull;
		IsIDDirty = r.IsIDDirty;

	}

	void SetDirty(bool b)
	{
		IsIDDirty = b;
		IsDirEntryDirty = b;
		IsFileNameDirty = b;
		IsOperationIDDirty = b;
		IsFileSizeDirty = b;
		IsFileMonthDirty = b;
		IsFileDayDirty = b;
		IsFileTimeOrYearDirty = b;
		IsXferDateTimeDirty = b;
		IsProgressStatusDirty = b;
		IsRetryCountDirty = b;
		IsLocalFileNameDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsIDDirty ||
			IsDirEntryDirty ||
			IsFileNameDirty ||
			IsOperationIDDirty ||
			IsFileSizeDirty ||
			IsFileMonthDirty ||
			IsFileDayDirty ||
			IsFileTimeOrYearDirty ||
			IsXferDateTimeDirty ||
			IsProgressStatusDirty ||
			IsRetryCountDirty ||
			IsLocalFileNameDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ID;
	bool IsIDNull;
	bool IsIDDirty;

	std::string field_DirEntry;
	bool IsDirEntryNull;
	bool IsDirEntryDirty;

	std::string field_FileName;
	bool IsFileNameNull;
	bool IsFileNameDirty;

	int field_OperationID;
	bool IsOperationIDNull;
	bool IsOperationIDDirty;

	int field_FileSize;
	bool IsFileSizeNull;
	bool IsFileSizeDirty;

	int field_FileMonth;
	bool IsFileMonthNull;
	bool IsFileMonthDirty;

	int field_FileDay;
	bool IsFileDayNull;
	bool IsFileDayDirty;

	int field_FileTimeOrYear;
	bool IsFileTimeOrYearNull;
	bool IsFileTimeOrYearDirty;

	TIMESTAMP_STRUCT field_XferDateTime;
	bool IsXferDateTimeNull;
	bool IsXferDateTimeDirty;

	int field_ProgressStatus;
	bool IsProgressStatusNull;
	bool IsProgressStatusDirty;

	int field_RetryCount;
	bool IsRetryCountNull;
	bool IsRetryCountDirty;

	std::string field_LocalFileName;
	bool IsLocalFileNameNull;
	bool IsLocalFileNameDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsIDPrimaryKey;
	bool IsDirEntryPrimaryKey;
	bool IsFileNamePrimaryKey;
	bool IsOperationIDPrimaryKey;
	bool IsFileSizePrimaryKey;
	bool IsFileMonthPrimaryKey;
	bool IsFileDayPrimaryKey;
	bool IsFileTimeOrYearPrimaryKey;
	bool IsXferDateTimePrimaryKey;
	bool IsProgressStatusPrimaryKey;
	bool IsRetryCountPrimaryKey;
	bool IsLocalFileNamePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class XferHist : public konga::DbRecordSet {
public:
	XferHist() {}

	static XferHist* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "XferHist";
	}

	void DoFieldExchange(const XferHistRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetID() {
		if( m_row.IsIDNull )
			throw konga::DbNullException("ID");
		return m_row.field_ID;
	}
	void SetID(int i) {
		m_row.field_ID = i;
		m_row.IsIDNull = false;
		m_row.IsIDDirty = true;
	}
	bool IsIDNull() const {
		return m_row.IsIDNull;
	}
	void SetIDToNull() {
		m_row.IsIDNull = true;
		m_row.IsIDDirty = true;
	}

	std::string GetDirEntry() {
		if( m_row.IsDirEntryNull )
			throw konga::DbNullException("DirEntry");
		return m_row.field_DirEntry;
	}
	void SetDirEntry(const std::string& st) {
		m_row.field_DirEntry = st;
		m_row.IsDirEntryNull = false;
		m_row.IsDirEntryDirty = true;
	}
	bool IsDirEntryNull() const {
		return m_row.IsDirEntryNull;
	}
	void SetDirEntryToNull() {
		m_row.field_DirEntry = "";
		m_row.IsDirEntryNull = true;
		m_row.IsDirEntryDirty = true;
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

	int GetOperationID() {
		if( m_row.IsOperationIDNull )
			throw konga::DbNullException("OperationID");
		return m_row.field_OperationID;
	}
	void SetOperationID(int i) {
		m_row.field_OperationID = i;
		m_row.IsOperationIDNull = false;
		m_row.IsOperationIDDirty = true;
	}
	bool IsOperationIDNull() const {
		return m_row.IsOperationIDNull;
	}
	void SetOperationIDToNull() {
		m_row.IsOperationIDNull = true;
		m_row.IsOperationIDDirty = true;
	}

	int GetFileSize() {
		if( m_row.IsFileSizeNull )
			throw konga::DbNullException("FileSize");
		return m_row.field_FileSize;
	}
	void SetFileSize(int i) {
		m_row.field_FileSize = i;
		m_row.IsFileSizeNull = false;
		m_row.IsFileSizeDirty = true;
	}
	bool IsFileSizeNull() const {
		return m_row.IsFileSizeNull;
	}
	void SetFileSizeToNull() {
		m_row.IsFileSizeNull = true;
		m_row.IsFileSizeDirty = true;
	}

	int GetFileMonth() {
		if( m_row.IsFileMonthNull )
			throw konga::DbNullException("FileMonth");
		return m_row.field_FileMonth;
	}
	void SetFileMonth(int i) {
		m_row.field_FileMonth = i;
		m_row.IsFileMonthNull = false;
		m_row.IsFileMonthDirty = true;
	}
	bool IsFileMonthNull() const {
		return m_row.IsFileMonthNull;
	}
	void SetFileMonthToNull() {
		m_row.IsFileMonthNull = true;
		m_row.IsFileMonthDirty = true;
	}

	int GetFileDay() {
		if( m_row.IsFileDayNull )
			throw konga::DbNullException("FileDay");
		return m_row.field_FileDay;
	}
	void SetFileDay(int i) {
		m_row.field_FileDay = i;
		m_row.IsFileDayNull = false;
		m_row.IsFileDayDirty = true;
	}
	bool IsFileDayNull() const {
		return m_row.IsFileDayNull;
	}
	void SetFileDayToNull() {
		m_row.IsFileDayNull = true;
		m_row.IsFileDayDirty = true;
	}

	int GetFileTimeOrYear() {
		if( m_row.IsFileTimeOrYearNull )
			throw konga::DbNullException("FileTimeOrYear");
		return m_row.field_FileTimeOrYear;
	}
	void SetFileTimeOrYear(int i) {
		m_row.field_FileTimeOrYear = i;
		m_row.IsFileTimeOrYearNull = false;
		m_row.IsFileTimeOrYearDirty = true;
	}
	bool IsFileTimeOrYearNull() const {
		return m_row.IsFileTimeOrYearNull;
	}
	void SetFileTimeOrYearToNull() {
		m_row.IsFileTimeOrYearNull = true;
		m_row.IsFileTimeOrYearDirty = true;
	}

	TIMESTAMP_STRUCT GetXferDateTime() {
		if( m_row.IsXferDateTimeNull )
			throw konga::DbNullException("XferDateTime");
		return m_row.field_XferDateTime;
	}
	void SetXferDateTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_XferDateTime = ts;
		m_row.IsXferDateTimeNull = false;
		m_row.IsXferDateTimeDirty = true;
	}
	bool IsXferDateTimeNull() const {
		return m_row.IsXferDateTimeNull;
	}
	void SetXferDateTimeToNull() {
		m_row.IsXferDateTimeNull = true;
		m_row.IsXferDateTimeDirty = true;
	}

	int GetProgressStatus() {
		if( m_row.IsProgressStatusNull )
			throw konga::DbNullException("ProgressStatus");
		return m_row.field_ProgressStatus;
	}
	void SetProgressStatus(int i) {
		m_row.field_ProgressStatus = i;
		m_row.IsProgressStatusNull = false;
		m_row.IsProgressStatusDirty = true;
	}
	bool IsProgressStatusNull() const {
		return m_row.IsProgressStatusNull;
	}
	void SetProgressStatusToNull() {
		m_row.IsProgressStatusNull = true;
		m_row.IsProgressStatusDirty = true;
	}

	int GetRetryCount() {
		if( m_row.IsRetryCountNull )
			throw konga::DbNullException("RetryCount");
		return m_row.field_RetryCount;
	}
	void SetRetryCount(int i) {
		m_row.field_RetryCount = i;
		m_row.IsRetryCountNull = false;
		m_row.IsRetryCountDirty = true;
	}
	bool IsRetryCountNull() const {
		return m_row.IsRetryCountNull;
	}
	void SetRetryCountToNull() {
		m_row.IsRetryCountNull = true;
		m_row.IsRetryCountDirty = true;
	}

	std::string GetLocalFileName() {
		if( m_row.IsLocalFileNameNull )
			throw konga::DbNullException("LocalFileName");
		return m_row.field_LocalFileName;
	}
	void SetLocalFileName(const std::string& st) {
		m_row.field_LocalFileName = st;
		m_row.IsLocalFileNameNull = false;
		m_row.IsLocalFileNameDirty = true;
	}
	bool IsLocalFileNameNull() const {
		return m_row.IsLocalFileNameNull;
	}
	void SetLocalFileNameToNull() {
		m_row.field_LocalFileName = "";
		m_row.IsLocalFileNameNull = true;
		m_row.IsLocalFileNameDirty = true;
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
	XferHistRow m_row;

private:
	bool IsIDDirty() const {
		return m_row.IsIDDirty;
	};
	bool IsDirEntryDirty() const {
		return m_row.IsDirEntryDirty;
	};
	bool IsFileNameDirty() const {
		return m_row.IsFileNameDirty;
	};
	bool IsOperationIDDirty() const {
		return m_row.IsOperationIDDirty;
	};
	bool IsFileSizeDirty() const {
		return m_row.IsFileSizeDirty;
	};
	bool IsFileMonthDirty() const {
		return m_row.IsFileMonthDirty;
	};
	bool IsFileDayDirty() const {
		return m_row.IsFileDayDirty;
	};
	bool IsFileTimeOrYearDirty() const {
		return m_row.IsFileTimeOrYearDirty;
	};
	bool IsXferDateTimeDirty() const {
		return m_row.IsXferDateTimeDirty;
	};
	bool IsProgressStatusDirty() const {
		return m_row.IsProgressStatusDirty;
	};
	bool IsRetryCountDirty() const {
		return m_row.IsRetryCountDirty;
	};
	bool IsLocalFileNameDirty() const {
		return m_row.IsLocalFileNameDirty;
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
