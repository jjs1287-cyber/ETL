/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_ROUTINGRULETAB_H
#define KONGA_DB_CRPDB_ROUTINGRULETAB_H

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

class RoutingRuleTabRow
{
public:
	RoutingRuleTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
			field_RoutingRuleId(0),
			IsRoutingRuleIdNull(true),
			IsRoutingRuleIdPrimaryKey(true),
			IsRoutingRuleIdDirty(false),
			IsSequenceStackNull(true),
			IsSequenceStackPrimaryKey(false),
			IsSequenceStackDirty(false),
			field_DestinationTaskId(0),
			IsDestinationTaskIdNull(true),
			IsDestinationTaskIdPrimaryKey(false),
			IsDestinationTaskIdDirty(false),
			IsTimeToEscalationNull(true),
			IsTimeToEscalationPrimaryKey(false),
			IsTimeToEscalationDirty(false),
			field_EscalationDestinationTaskId(0),
			IsEscalationDestinationTaskIdNull(true),
			IsEscalationDestinationTaskIdPrimaryKey(false),
			IsEscalationDestinationTaskIdDirty(false),
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

	void Copy(const RoutingRuleTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_RoutingRuleId = r.field_RoutingRuleId;
		IsRoutingRuleIdNull = r.IsRoutingRuleIdNull;
		IsRoutingRuleIdDirty = r.IsRoutingRuleIdDirty;

		field_SequenceStack = r.field_SequenceStack;
		IsSequenceStackNull = r.IsSequenceStackNull;
		IsSequenceStackDirty = r.IsSequenceStackDirty;

		field_DestinationTaskId = r.field_DestinationTaskId;
		IsDestinationTaskIdNull = r.IsDestinationTaskIdNull;
		IsDestinationTaskIdDirty = r.IsDestinationTaskIdDirty;

		field_TimeToEscalation = r.field_TimeToEscalation;
		IsTimeToEscalationNull = r.IsTimeToEscalationNull;
		IsTimeToEscalationDirty = r.IsTimeToEscalationDirty;

		field_EscalationDestinationTaskId = r.field_EscalationDestinationTaskId;
		IsEscalationDestinationTaskIdNull = r.IsEscalationDestinationTaskIdNull;
		IsEscalationDestinationTaskIdDirty = r.IsEscalationDestinationTaskIdDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const RoutingRuleTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_RoutingRuleId = r.field_RoutingRuleId;
		IsRoutingRuleIdNull = r.IsRoutingRuleIdNull;
		IsRoutingRuleIdDirty = r.IsRoutingRuleIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsRoutingRuleIdDirty = b;
		IsSequenceStackDirty = b;
		IsDestinationTaskIdDirty = b;
		IsTimeToEscalationDirty = b;
		IsEscalationDestinationTaskIdDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsRoutingRuleIdDirty ||
			IsSequenceStackDirty ||
			IsDestinationTaskIdDirty ||
			IsTimeToEscalationDirty ||
			IsEscalationDestinationTaskIdDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_RoutingRuleId;
	bool IsRoutingRuleIdNull;
	bool IsRoutingRuleIdDirty;

	std::string field_SequenceStack;
	bool IsSequenceStackNull;
	bool IsSequenceStackDirty;

	int field_DestinationTaskId;
	bool IsDestinationTaskIdNull;
	bool IsDestinationTaskIdDirty;

	std::string field_TimeToEscalation;
	bool IsTimeToEscalationNull;
	bool IsTimeToEscalationDirty;

	int field_EscalationDestinationTaskId;
	bool IsEscalationDestinationTaskIdNull;
	bool IsEscalationDestinationTaskIdDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsRoutingRuleIdPrimaryKey;
	bool IsSequenceStackPrimaryKey;
	bool IsDestinationTaskIdPrimaryKey;
	bool IsTimeToEscalationPrimaryKey;
	bool IsEscalationDestinationTaskIdPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class RoutingRuleTab : public konga::DbRecordSet {
public:
	RoutingRuleTab() {}

	static RoutingRuleTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "RoutingRuleTab";
	}

	void DoFieldExchange(const RoutingRuleTabRow& row) {
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

	int GetRoutingRuleId() {
		if( m_row.IsRoutingRuleIdNull )
			throw konga::DbNullException("RoutingRuleId");
		return m_row.field_RoutingRuleId;
	}
	void SetRoutingRuleId(int i) {
		m_row.field_RoutingRuleId = i;
		m_row.IsRoutingRuleIdNull = false;
		m_row.IsRoutingRuleIdDirty = true;
	}
	bool IsRoutingRuleIdNull() const {
		return m_row.IsRoutingRuleIdNull;
	}
	void SetRoutingRuleIdToNull() {
		m_row.IsRoutingRuleIdNull = true;
		m_row.IsRoutingRuleIdDirty = true;
	}

	std::string GetSequenceStack() {
		if( m_row.IsSequenceStackNull )
			throw konga::DbNullException("SequenceStack");
		return m_row.field_SequenceStack;
	}
	void SetSequenceStack(const std::string& st) {
		m_row.field_SequenceStack = st;
		m_row.IsSequenceStackNull = false;
		m_row.IsSequenceStackDirty = true;
	}
	bool IsSequenceStackNull() const {
		return m_row.IsSequenceStackNull;
	}
	void SetSequenceStackToNull() {
		m_row.field_SequenceStack = "";
		m_row.IsSequenceStackNull = true;
		m_row.IsSequenceStackDirty = true;
	}

	int GetDestinationTaskId() {
		if( m_row.IsDestinationTaskIdNull )
			throw konga::DbNullException("DestinationTaskId");
		return m_row.field_DestinationTaskId;
	}
	void SetDestinationTaskId(int i) {
		m_row.field_DestinationTaskId = i;
		m_row.IsDestinationTaskIdNull = false;
		m_row.IsDestinationTaskIdDirty = true;
	}
	bool IsDestinationTaskIdNull() const {
		return m_row.IsDestinationTaskIdNull;
	}
	void SetDestinationTaskIdToNull() {
		m_row.IsDestinationTaskIdNull = true;
		m_row.IsDestinationTaskIdDirty = true;
	}

	std::string GetTimeToEscalation() {
		if( m_row.IsTimeToEscalationNull )
			throw konga::DbNullException("TimeToEscalation");
		return m_row.field_TimeToEscalation;
	}
	void SetTimeToEscalation(const std::string& st) {
		m_row.field_TimeToEscalation = st;
		m_row.IsTimeToEscalationNull = false;
		m_row.IsTimeToEscalationDirty = true;
	}
	bool IsTimeToEscalationNull() const {
		return m_row.IsTimeToEscalationNull;
	}
	void SetTimeToEscalationToNull() {
		m_row.field_TimeToEscalation = "";
		m_row.IsTimeToEscalationNull = true;
		m_row.IsTimeToEscalationDirty = true;
	}

	int GetEscalationDestinationTaskId() {
		if( m_row.IsEscalationDestinationTaskIdNull )
			throw konga::DbNullException("EscalationDestinationTaskId");
		return m_row.field_EscalationDestinationTaskId;
	}
	void SetEscalationDestinationTaskId(int i) {
		m_row.field_EscalationDestinationTaskId = i;
		m_row.IsEscalationDestinationTaskIdNull = false;
		m_row.IsEscalationDestinationTaskIdDirty = true;
	}
	bool IsEscalationDestinationTaskIdNull() const {
		return m_row.IsEscalationDestinationTaskIdNull;
	}
	void SetEscalationDestinationTaskIdToNull() {
		m_row.IsEscalationDestinationTaskIdNull = true;
		m_row.IsEscalationDestinationTaskIdDirty = true;
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
	RoutingRuleTabRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsRoutingRuleIdDirty() const {
		return m_row.IsRoutingRuleIdDirty;
	};
	bool IsSequenceStackDirty() const {
		return m_row.IsSequenceStackDirty;
	};
	bool IsDestinationTaskIdDirty() const {
		return m_row.IsDestinationTaskIdDirty;
	};
	bool IsTimeToEscalationDirty() const {
		return m_row.IsTimeToEscalationDirty;
	};
	bool IsEscalationDestinationTaskIdDirty() const {
		return m_row.IsEscalationDestinationTaskIdDirty;
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
