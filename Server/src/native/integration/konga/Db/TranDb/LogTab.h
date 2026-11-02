/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_LOGTAB_H
#define KONGA_DB_CRTRANDB_LOGTAB_H

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

class LogTabRow
{
public:
	LogTabRow() : 
			field_Log_ID(0),
			IsLog_IDNull(true),
			IsLog_IDPrimaryKey(true),
			IsLog_IDDirty(false),
			field_Operation_ID(0),
			IsOperation_IDNull(true),
			IsOperation_IDPrimaryKey(false),
			IsOperation_IDDirty(false),
			IsTimestampNull(true),
			IsTimestampPrimaryKey(false),
			IsTimestampDirty(false),
			IsLog_SourceNull(true),
			IsLog_SourcePrimaryKey(false),
			IsLog_SourceDirty(false),
			IsShort_MessageNull(true),
			IsShort_MessagePrimaryKey(false),
			IsShort_MessageDirty(false),
			IsSeverityNull(true),
			IsSeverityPrimaryKey(false),
			IsSeverityDirty(false),
			field_Error_Code(0),
			IsError_CodeNull(true),
			IsError_CodePrimaryKey(false),
			IsError_CodeDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false)
	{
		field_Timestamp.year = 0;
		field_Timestamp.month = 0;
		field_Timestamp.day = 0;
		field_Timestamp.hour = 0;
		field_Timestamp.minute = 0;
		field_Timestamp.second = 0;
		field_Timestamp.fraction = 0;
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

	void Copy(const LogTabRow& r)
	{
		field_Log_ID = r.field_Log_ID;
		IsLog_IDNull = r.IsLog_IDNull;
		IsLog_IDDirty = r.IsLog_IDDirty;

		field_Operation_ID = r.field_Operation_ID;
		IsOperation_IDNull = r.IsOperation_IDNull;
		IsOperation_IDDirty = r.IsOperation_IDDirty;

		field_Timestamp = r.field_Timestamp;
		IsTimestampNull = r.IsTimestampNull;
		IsTimestampDirty = r.IsTimestampDirty;

		field_Log_Source = r.field_Log_Source;
		IsLog_SourceNull = r.IsLog_SourceNull;
		IsLog_SourceDirty = r.IsLog_SourceDirty;

		field_Short_Message = r.field_Short_Message;
		IsShort_MessageNull = r.IsShort_MessageNull;
		IsShort_MessageDirty = r.IsShort_MessageDirty;

		field_Severity = r.field_Severity;
		IsSeverityNull = r.IsSeverityNull;
		IsSeverityDirty = r.IsSeverityDirty;

		field_Error_Code = r.field_Error_Code;
		IsError_CodeNull = r.IsError_CodeNull;
		IsError_CodeDirty = r.IsError_CodeDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const LogTabRow& r)
	{
		field_Log_ID = r.field_Log_ID;
		IsLog_IDNull = r.IsLog_IDNull;
		IsLog_IDDirty = r.IsLog_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsLog_IDDirty = b;
		IsOperation_IDDirty = b;
		IsTimestampDirty = b;
		IsLog_SourceDirty = b;
		IsShort_MessageDirty = b;
		IsSeverityDirty = b;
		IsError_CodeDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsLog_IDDirty ||
			IsOperation_IDDirty ||
			IsTimestampDirty ||
			IsLog_SourceDirty ||
			IsShort_MessageDirty ||
			IsSeverityDirty ||
			IsError_CodeDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Log_ID;
	bool IsLog_IDNull;
	bool IsLog_IDDirty;

	int field_Operation_ID;
	bool IsOperation_IDNull;
	bool IsOperation_IDDirty;

	TIMESTAMP_STRUCT field_Timestamp;
	bool IsTimestampNull;
	bool IsTimestampDirty;

	std::string field_Log_Source;
	bool IsLog_SourceNull;
	bool IsLog_SourceDirty;

	std::string field_Short_Message;
	bool IsShort_MessageNull;
	bool IsShort_MessageDirty;

	std::string field_Severity;
	bool IsSeverityNull;
	bool IsSeverityDirty;

	int field_Error_Code;
	bool IsError_CodeNull;
	bool IsError_CodeDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsLog_IDPrimaryKey;
	bool IsOperation_IDPrimaryKey;
	bool IsTimestampPrimaryKey;
	bool IsLog_SourcePrimaryKey;
	bool IsShort_MessagePrimaryKey;
	bool IsSeverityPrimaryKey;
	bool IsError_CodePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class LogTab : public konga::DbRecordSet {
public:
	LogTab() {}

	static LogTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "LogTab";
	}

	void DoFieldExchange(const LogTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetLog_ID() {
		if( m_row.IsLog_IDNull )
			throw konga::DbNullException("Log_ID");
		return m_row.field_Log_ID;
	}
	void SetLog_ID(int i) {
		m_row.field_Log_ID = i;
		m_row.IsLog_IDNull = false;
		m_row.IsLog_IDDirty = true;
	}
	bool IsLog_IDNull() const {
		return m_row.IsLog_IDNull;
	}
	void SetLog_IDToNull() {
		m_row.IsLog_IDNull = true;
		m_row.IsLog_IDDirty = true;
	}

	int GetOperation_ID() {
		if( m_row.IsOperation_IDNull )
			throw konga::DbNullException("Operation_ID");
		return m_row.field_Operation_ID;
	}
	void SetOperation_ID(int i) {
		m_row.field_Operation_ID = i;
		m_row.IsOperation_IDNull = false;
		m_row.IsOperation_IDDirty = true;
	}
	bool IsOperation_IDNull() const {
		return m_row.IsOperation_IDNull;
	}
	void SetOperation_IDToNull() {
		m_row.IsOperation_IDNull = true;
		m_row.IsOperation_IDDirty = true;
	}

	TIMESTAMP_STRUCT GetTimestamp() {
		if( m_row.IsTimestampNull )
			throw konga::DbNullException("Timestamp");
		return m_row.field_Timestamp;
	}
	void SetTimestamp(const TIMESTAMP_STRUCT& ts) {
		m_row.field_Timestamp = ts;
		m_row.IsTimestampNull = false;
		m_row.IsTimestampDirty = true;
	}
	bool IsTimestampNull() const {
		return m_row.IsTimestampNull;
	}
	void SetTimestampToNull() {
		m_row.IsTimestampNull = true;
		m_row.IsTimestampDirty = true;
	}

	std::string GetLog_Source() {
		if( m_row.IsLog_SourceNull )
			throw konga::DbNullException("Log_Source");
		return m_row.field_Log_Source;
	}
	void SetLog_Source(const std::string& st) {
		m_row.field_Log_Source = st;
		m_row.IsLog_SourceNull = false;
		m_row.IsLog_SourceDirty = true;
	}
	bool IsLog_SourceNull() const {
		return m_row.IsLog_SourceNull;
	}
	void SetLog_SourceToNull() {
		m_row.field_Log_Source = "";
		m_row.IsLog_SourceNull = true;
		m_row.IsLog_SourceDirty = true;
	}

	std::string GetShort_Message() {
		if( m_row.IsShort_MessageNull )
			throw konga::DbNullException("Short_Message");
		return m_row.field_Short_Message;
	}
	void SetShort_Message(const std::string& st) {
		m_row.field_Short_Message = st;
		m_row.IsShort_MessageNull = false;
		m_row.IsShort_MessageDirty = true;
	}
	bool IsShort_MessageNull() const {
		return m_row.IsShort_MessageNull;
	}
	void SetShort_MessageToNull() {
		m_row.field_Short_Message = "";
		m_row.IsShort_MessageNull = true;
		m_row.IsShort_MessageDirty = true;
	}

	std::string GetSeverity() {
		if( m_row.IsSeverityNull )
			throw konga::DbNullException("Severity");
		return m_row.field_Severity;
	}
	void SetSeverity(const std::string& st) {
		m_row.field_Severity = st;
		m_row.IsSeverityNull = false;
		m_row.IsSeverityDirty = true;
	}
	bool IsSeverityNull() const {
		return m_row.IsSeverityNull;
	}
	void SetSeverityToNull() {
		m_row.field_Severity = "";
		m_row.IsSeverityNull = true;
		m_row.IsSeverityDirty = true;
	}

	int GetError_Code() {
		if( m_row.IsError_CodeNull )
			throw konga::DbNullException("Error_Code");
		return m_row.field_Error_Code;
	}
	void SetError_Code(int i) {
		m_row.field_Error_Code = i;
		m_row.IsError_CodeNull = false;
		m_row.IsError_CodeDirty = true;
	}
	bool IsError_CodeNull() const {
		return m_row.IsError_CodeNull;
	}
	void SetError_CodeToNull() {
		m_row.IsError_CodeNull = true;
		m_row.IsError_CodeDirty = true;
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
	LogTabRow m_row;

private:
	bool IsLog_IDDirty() const {
		return m_row.IsLog_IDDirty;
	};
	bool IsOperation_IDDirty() const {
		return m_row.IsOperation_IDDirty;
	};
	bool IsTimestampDirty() const {
		return m_row.IsTimestampDirty;
	};
	bool IsLog_SourceDirty() const {
		return m_row.IsLog_SourceDirty;
	};
	bool IsShort_MessageDirty() const {
		return m_row.IsShort_MessageDirty;
	};
	bool IsSeverityDirty() const {
		return m_row.IsSeverityDirty;
	};
	bool IsError_CodeDirty() const {
		return m_row.IsError_CodeDirty;
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
