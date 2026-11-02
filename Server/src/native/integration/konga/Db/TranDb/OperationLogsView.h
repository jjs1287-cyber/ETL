/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_OPERATIONLOGSVIEW_H
#define KONGA_DB_CRTRANDB_OPERATIONLOGSVIEW_H

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

class OperationLogsViewRow
{
public:
	OperationLogsViewRow() : 
			field_interchangeproject_id(0),
			Isinterchangeproject_idNull(true),
			Isinterchangeproject_idPrimaryKey(false),
			Isinterchangeproject_idDirty(false),
			field_Operation_ID(0),
			IsOperation_IDNull(true),
			IsOperation_IDPrimaryKey(false),
			IsOperation_IDDirty(false),
			IsOperation_NameNull(true),
			IsOperation_NamePrimaryKey(false),
			IsOperation_NameDirty(false),
			field_Log_ID(0),
			IsLog_IDNull(true),
			IsLog_IDPrimaryKey(false),
			IsLog_IDDirty(false),
			IsTimestampNull(true),
			IsTimestampPrimaryKey(false),
			IsTimestampDirty(false),
			IsShort_MessageNull(true),
			IsShort_MessagePrimaryKey(false),
			IsShort_MessageDirty(false),
			IsSeverityNull(true),
			IsSeverityPrimaryKey(false),
			IsSeverityDirty(false)
	{
		field_Timestamp.year = 0;
		field_Timestamp.month = 0;
		field_Timestamp.day = 0;
		field_Timestamp.hour = 0;
		field_Timestamp.minute = 0;
		field_Timestamp.second = 0;
		field_Timestamp.fraction = 0;
	}

	void Copy(const OperationLogsViewRow& r)
	{
		field_interchangeproject_id = r.field_interchangeproject_id;
		Isinterchangeproject_idNull = r.Isinterchangeproject_idNull;
		Isinterchangeproject_idDirty = r.Isinterchangeproject_idDirty;

		field_Operation_ID = r.field_Operation_ID;
		IsOperation_IDNull = r.IsOperation_IDNull;
		IsOperation_IDDirty = r.IsOperation_IDDirty;

		field_Operation_Name = r.field_Operation_Name;
		IsOperation_NameNull = r.IsOperation_NameNull;
		IsOperation_NameDirty = r.IsOperation_NameDirty;

		field_Log_ID = r.field_Log_ID;
		IsLog_IDNull = r.IsLog_IDNull;
		IsLog_IDDirty = r.IsLog_IDDirty;

		field_Timestamp = r.field_Timestamp;
		IsTimestampNull = r.IsTimestampNull;
		IsTimestampDirty = r.IsTimestampDirty;

		field_Short_Message = r.field_Short_Message;
		IsShort_MessageNull = r.IsShort_MessageNull;
		IsShort_MessageDirty = r.IsShort_MessageDirty;

		field_Severity = r.field_Severity;
		IsSeverityNull = r.IsSeverityNull;
		IsSeverityDirty = r.IsSeverityDirty;

	}

	void CopyPKs(const OperationLogsViewRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		Isinterchangeproject_idDirty = b;
		IsOperation_IDDirty = b;
		IsOperation_NameDirty = b;
		IsLog_IDDirty = b;
		IsTimestampDirty = b;
		IsShort_MessageDirty = b;
		IsSeverityDirty = b;
	}

	bool IsDirty() const
	{
		return Isinterchangeproject_idDirty ||
			IsOperation_IDDirty ||
			IsOperation_NameDirty ||
			IsLog_IDDirty ||
			IsTimestampDirty ||
			IsShort_MessageDirty ||
			IsSeverityDirty;
	}

	int field_interchangeproject_id;
	bool Isinterchangeproject_idNull;
	bool Isinterchangeproject_idDirty;

	int field_Operation_ID;
	bool IsOperation_IDNull;
	bool IsOperation_IDDirty;

	std::string field_Operation_Name;
	bool IsOperation_NameNull;
	bool IsOperation_NameDirty;

	int field_Log_ID;
	bool IsLog_IDNull;
	bool IsLog_IDDirty;

	TIMESTAMP_STRUCT field_Timestamp;
	bool IsTimestampNull;
	bool IsTimestampDirty;

	std::string field_Short_Message;
	bool IsShort_MessageNull;
	bool IsShort_MessageDirty;

	std::string field_Severity;
	bool IsSeverityNull;
	bool IsSeverityDirty;


private:
	bool Isinterchangeproject_idPrimaryKey;
	bool IsOperation_IDPrimaryKey;
	bool IsOperation_NamePrimaryKey;
	bool IsLog_IDPrimaryKey;
	bool IsTimestampPrimaryKey;
	bool IsShort_MessagePrimaryKey;
	bool IsSeverityPrimaryKey;

};


class OperationLogsView : public konga::DbRecordSet {
public:
	OperationLogsView() {}

	static OperationLogsView* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "OperationLogsView";
	}

	void DoFieldExchange(const OperationLogsViewRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int Getinterchangeproject_id() {
		if( m_row.Isinterchangeproject_idNull )
			throw konga::DbNullException("interchangeproject_id");
		return m_row.field_interchangeproject_id;
	}
	void Setinterchangeproject_id(int i) {
		m_row.field_interchangeproject_id = i;
		m_row.Isinterchangeproject_idNull = false;
		m_row.Isinterchangeproject_idDirty = true;
	}
	bool Isinterchangeproject_idNull() const {
		return m_row.Isinterchangeproject_idNull;
	}
	void Setinterchangeproject_idToNull() {
		m_row.Isinterchangeproject_idNull = true;
		m_row.Isinterchangeproject_idDirty = true;
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

	std::string GetOperation_Name() {
		if( m_row.IsOperation_NameNull )
			throw konga::DbNullException("Operation_Name");
		return m_row.field_Operation_Name;
	}
	void SetOperation_Name(const std::string& st) {
		m_row.field_Operation_Name = st;
		m_row.IsOperation_NameNull = false;
		m_row.IsOperation_NameDirty = true;
	}
	bool IsOperation_NameNull() const {
		return m_row.IsOperation_NameNull;
	}
	void SetOperation_NameToNull() {
		m_row.field_Operation_Name = "";
		m_row.IsOperation_NameNull = true;
		m_row.IsOperation_NameDirty = true;
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

protected:
	OperationLogsViewRow m_row;

private:
	bool Isinterchangeproject_idDirty() const {
		return m_row.Isinterchangeproject_idDirty;
	};
	bool IsOperation_IDDirty() const {
		return m_row.IsOperation_IDDirty;
	};
	bool IsOperation_NameDirty() const {
		return m_row.IsOperation_NameDirty;
	};
	bool IsLog_IDDirty() const {
		return m_row.IsLog_IDDirty;
	};
	bool IsTimestampDirty() const {
		return m_row.IsTimestampDirty;
	};
	bool IsShort_MessageDirty() const {
		return m_row.IsShort_MessageDirty;
	};
	bool IsSeverityDirty() const {
		return m_row.IsSeverityDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
