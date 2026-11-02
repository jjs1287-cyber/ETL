/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_TASKTAB_H
#define KONGA_DB_CRPDB_TASKTAB_H

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

class TaskTabRow
{
public:
	TaskTabRow() : 
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
			IsTaskNameNull(true),
			IsTaskNamePrimaryKey(false),
			IsTaskNameDirty(false),
			IsDescriptionNull(true),
			IsDescriptionPrimaryKey(false),
			IsDescriptionDirty(false),
			field_ErrorHandlerId(0),
			IsErrorHandlerIdNull(true),
			IsErrorHandlerIdPrimaryKey(false),
			IsErrorHandlerIdDirty(false),
			IsTaskRoleNull(true),
			IsTaskRolePrimaryKey(false),
			IsTaskRoleDirty(false),
			IsDelegationRoleNull(true),
			IsDelegationRolePrimaryKey(false),
			IsDelegationRoleDirty(false),
			field_RoutingRuleId(0),
			IsRoutingRuleIdNull(true),
			IsRoutingRuleIdPrimaryKey(false),
			IsRoutingRuleIdDirty(false),
			IsDocumentIdListNull(true),
			IsDocumentIdListPrimaryKey(false),
			IsDocumentIdListDirty(false),
			field_ActivityRuleId(0),
			IsActivityRuleIdNull(true),
			IsActivityRuleIdPrimaryKey(false),
			IsActivityRuleIdDirty(false),
			field_PreRoleActivityRuleId(0),
			IsPreRoleActivityRuleIdNull(true),
			IsPreRoleActivityRuleIdPrimaryKey(false),
			IsPreRoleActivityRuleIdDirty(false),
			field_TaskType(0),
			IsTaskTypeNull(true),
			IsTaskTypePrimaryKey(false),
			IsTaskTypeDirty(false),
			field_TaskPriority(0),
			IsTaskPriorityNull(true),
			IsTaskPriorityPrimaryKey(false),
			IsTaskPriorityDirty(false),
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

	void Copy(const TaskTabRow& r)
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

		field_TaskName = r.field_TaskName;
		IsTaskNameNull = r.IsTaskNameNull;
		IsTaskNameDirty = r.IsTaskNameDirty;

		field_Description = r.field_Description;
		IsDescriptionNull = r.IsDescriptionNull;
		IsDescriptionDirty = r.IsDescriptionDirty;

		field_ErrorHandlerId = r.field_ErrorHandlerId;
		IsErrorHandlerIdNull = r.IsErrorHandlerIdNull;
		IsErrorHandlerIdDirty = r.IsErrorHandlerIdDirty;

		field_TaskRole = r.field_TaskRole;
		IsTaskRoleNull = r.IsTaskRoleNull;
		IsTaskRoleDirty = r.IsTaskRoleDirty;

		field_DelegationRole = r.field_DelegationRole;
		IsDelegationRoleNull = r.IsDelegationRoleNull;
		IsDelegationRoleDirty = r.IsDelegationRoleDirty;

		field_RoutingRuleId = r.field_RoutingRuleId;
		IsRoutingRuleIdNull = r.IsRoutingRuleIdNull;
		IsRoutingRuleIdDirty = r.IsRoutingRuleIdDirty;

		field_DocumentIdList = r.field_DocumentIdList;
		IsDocumentIdListNull = r.IsDocumentIdListNull;
		IsDocumentIdListDirty = r.IsDocumentIdListDirty;

		field_ActivityRuleId = r.field_ActivityRuleId;
		IsActivityRuleIdNull = r.IsActivityRuleIdNull;
		IsActivityRuleIdDirty = r.IsActivityRuleIdDirty;

		field_PreRoleActivityRuleId = r.field_PreRoleActivityRuleId;
		IsPreRoleActivityRuleIdNull = r.IsPreRoleActivityRuleIdNull;
		IsPreRoleActivityRuleIdDirty = r.IsPreRoleActivityRuleIdDirty;

		field_TaskType = r.field_TaskType;
		IsTaskTypeNull = r.IsTaskTypeNull;
		IsTaskTypeDirty = r.IsTaskTypeDirty;

		field_TaskPriority = r.field_TaskPriority;
		IsTaskPriorityNull = r.IsTaskPriorityNull;
		IsTaskPriorityDirty = r.IsTaskPriorityDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const TaskTabRow& r)
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

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsTaskIdDirty = b;
		IsTaskNameDirty = b;
		IsDescriptionDirty = b;
		IsErrorHandlerIdDirty = b;
		IsTaskRoleDirty = b;
		IsDelegationRoleDirty = b;
		IsRoutingRuleIdDirty = b;
		IsDocumentIdListDirty = b;
		IsActivityRuleIdDirty = b;
		IsPreRoleActivityRuleIdDirty = b;
		IsTaskTypeDirty = b;
		IsTaskPriorityDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsTaskIdDirty ||
			IsTaskNameDirty ||
			IsDescriptionDirty ||
			IsErrorHandlerIdDirty ||
			IsTaskRoleDirty ||
			IsDelegationRoleDirty ||
			IsRoutingRuleIdDirty ||
			IsDocumentIdListDirty ||
			IsActivityRuleIdDirty ||
			IsPreRoleActivityRuleIdDirty ||
			IsTaskTypeDirty ||
			IsTaskPriorityDirty ||
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

	std::string field_TaskName;
	bool IsTaskNameNull;
	bool IsTaskNameDirty;

	std::string field_Description;
	bool IsDescriptionNull;
	bool IsDescriptionDirty;

	int field_ErrorHandlerId;
	bool IsErrorHandlerIdNull;
	bool IsErrorHandlerIdDirty;

	std::string field_TaskRole;
	bool IsTaskRoleNull;
	bool IsTaskRoleDirty;

	std::string field_DelegationRole;
	bool IsDelegationRoleNull;
	bool IsDelegationRoleDirty;

	int field_RoutingRuleId;
	bool IsRoutingRuleIdNull;
	bool IsRoutingRuleIdDirty;

	std::string field_DocumentIdList;
	bool IsDocumentIdListNull;
	bool IsDocumentIdListDirty;

	int field_ActivityRuleId;
	bool IsActivityRuleIdNull;
	bool IsActivityRuleIdDirty;

	int field_PreRoleActivityRuleId;
	bool IsPreRoleActivityRuleIdNull;
	bool IsPreRoleActivityRuleIdDirty;

	int field_TaskType;
	bool IsTaskTypeNull;
	bool IsTaskTypeDirty;

	int field_TaskPriority;
	bool IsTaskPriorityNull;
	bool IsTaskPriorityDirty;

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
	bool IsTaskNamePrimaryKey;
	bool IsDescriptionPrimaryKey;
	bool IsErrorHandlerIdPrimaryKey;
	bool IsTaskRolePrimaryKey;
	bool IsDelegationRolePrimaryKey;
	bool IsRoutingRuleIdPrimaryKey;
	bool IsDocumentIdListPrimaryKey;
	bool IsActivityRuleIdPrimaryKey;
	bool IsPreRoleActivityRuleIdPrimaryKey;
	bool IsTaskTypePrimaryKey;
	bool IsTaskPriorityPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class TaskTab : public konga::DbRecordSet {
public:
	TaskTab() {}

	static TaskTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TaskTab";
	}

	void DoFieldExchange(const TaskTabRow& row) {
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

	std::string GetTaskName() {
		if( m_row.IsTaskNameNull )
			throw konga::DbNullException("TaskName");
		return m_row.field_TaskName;
	}
	void SetTaskName(const std::string& st) {
		m_row.field_TaskName = st;
		m_row.IsTaskNameNull = false;
		m_row.IsTaskNameDirty = true;
	}
	bool IsTaskNameNull() const {
		return m_row.IsTaskNameNull;
	}
	void SetTaskNameToNull() {
		m_row.field_TaskName = "";
		m_row.IsTaskNameNull = true;
		m_row.IsTaskNameDirty = true;
	}

	std::string GetDescription() {
		if( m_row.IsDescriptionNull )
			throw konga::DbNullException("Description");
		return m_row.field_Description;
	}
	void SetDescription(const std::string& st) {
		m_row.field_Description = st;
		m_row.IsDescriptionNull = false;
		m_row.IsDescriptionDirty = true;
	}
	bool IsDescriptionNull() const {
		return m_row.IsDescriptionNull;
	}
	void SetDescriptionToNull() {
		m_row.field_Description = "";
		m_row.IsDescriptionNull = true;
		m_row.IsDescriptionDirty = true;
	}

	int GetErrorHandlerId() {
		if( m_row.IsErrorHandlerIdNull )
			throw konga::DbNullException("ErrorHandlerId");
		return m_row.field_ErrorHandlerId;
	}
	void SetErrorHandlerId(int i) {
		m_row.field_ErrorHandlerId = i;
		m_row.IsErrorHandlerIdNull = false;
		m_row.IsErrorHandlerIdDirty = true;
	}
	bool IsErrorHandlerIdNull() const {
		return m_row.IsErrorHandlerIdNull;
	}
	void SetErrorHandlerIdToNull() {
		m_row.IsErrorHandlerIdNull = true;
		m_row.IsErrorHandlerIdDirty = true;
	}

	std::string GetTaskRole() {
		if( m_row.IsTaskRoleNull )
			throw konga::DbNullException("TaskRole");
		return m_row.field_TaskRole;
	}
	void SetTaskRole(const std::string& st) {
		m_row.field_TaskRole = st;
		m_row.IsTaskRoleNull = false;
		m_row.IsTaskRoleDirty = true;
	}
	bool IsTaskRoleNull() const {
		return m_row.IsTaskRoleNull;
	}
	void SetTaskRoleToNull() {
		m_row.field_TaskRole = "";
		m_row.IsTaskRoleNull = true;
		m_row.IsTaskRoleDirty = true;
	}

	std::string GetDelegationRole() {
		if( m_row.IsDelegationRoleNull )
			throw konga::DbNullException("DelegationRole");
		return m_row.field_DelegationRole;
	}
	void SetDelegationRole(const std::string& st) {
		m_row.field_DelegationRole = st;
		m_row.IsDelegationRoleNull = false;
		m_row.IsDelegationRoleDirty = true;
	}
	bool IsDelegationRoleNull() const {
		return m_row.IsDelegationRoleNull;
	}
	void SetDelegationRoleToNull() {
		m_row.field_DelegationRole = "";
		m_row.IsDelegationRoleNull = true;
		m_row.IsDelegationRoleDirty = true;
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

	std::string GetDocumentIdList() {
		if( m_row.IsDocumentIdListNull )
			throw konga::DbNullException("DocumentIdList");
		return m_row.field_DocumentIdList;
	}
	void SetDocumentIdList(const std::string& st) {
		m_row.field_DocumentIdList = st;
		m_row.IsDocumentIdListNull = false;
		m_row.IsDocumentIdListDirty = true;
	}
	bool IsDocumentIdListNull() const {
		return m_row.IsDocumentIdListNull;
	}
	void SetDocumentIdListToNull() {
		m_row.field_DocumentIdList = "";
		m_row.IsDocumentIdListNull = true;
		m_row.IsDocumentIdListDirty = true;
	}

	int GetActivityRuleId() {
		if( m_row.IsActivityRuleIdNull )
			throw konga::DbNullException("ActivityRuleId");
		return m_row.field_ActivityRuleId;
	}
	void SetActivityRuleId(int i) {
		m_row.field_ActivityRuleId = i;
		m_row.IsActivityRuleIdNull = false;
		m_row.IsActivityRuleIdDirty = true;
	}
	bool IsActivityRuleIdNull() const {
		return m_row.IsActivityRuleIdNull;
	}
	void SetActivityRuleIdToNull() {
		m_row.IsActivityRuleIdNull = true;
		m_row.IsActivityRuleIdDirty = true;
	}

	int GetPreRoleActivityRuleId() {
		if( m_row.IsPreRoleActivityRuleIdNull )
			throw konga::DbNullException("PreRoleActivityRuleId");
		return m_row.field_PreRoleActivityRuleId;
	}
	void SetPreRoleActivityRuleId(int i) {
		m_row.field_PreRoleActivityRuleId = i;
		m_row.IsPreRoleActivityRuleIdNull = false;
		m_row.IsPreRoleActivityRuleIdDirty = true;
	}
	bool IsPreRoleActivityRuleIdNull() const {
		return m_row.IsPreRoleActivityRuleIdNull;
	}
	void SetPreRoleActivityRuleIdToNull() {
		m_row.IsPreRoleActivityRuleIdNull = true;
		m_row.IsPreRoleActivityRuleIdDirty = true;
	}

	int GetTaskType() {
		if( m_row.IsTaskTypeNull )
			throw konga::DbNullException("TaskType");
		return m_row.field_TaskType;
	}
	void SetTaskType(int i) {
		m_row.field_TaskType = i;
		m_row.IsTaskTypeNull = false;
		m_row.IsTaskTypeDirty = true;
	}
	bool IsTaskTypeNull() const {
		return m_row.IsTaskTypeNull;
	}
	void SetTaskTypeToNull() {
		m_row.IsTaskTypeNull = true;
		m_row.IsTaskTypeDirty = true;
	}

	int GetTaskPriority() {
		if( m_row.IsTaskPriorityNull )
			throw konga::DbNullException("TaskPriority");
		return m_row.field_TaskPriority;
	}
	void SetTaskPriority(int i) {
		m_row.field_TaskPriority = i;
		m_row.IsTaskPriorityNull = false;
		m_row.IsTaskPriorityDirty = true;
	}
	bool IsTaskPriorityNull() const {
		return m_row.IsTaskPriorityNull;
	}
	void SetTaskPriorityToNull() {
		m_row.IsTaskPriorityNull = true;
		m_row.IsTaskPriorityDirty = true;
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
	TaskTabRow m_row;

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
	bool IsTaskNameDirty() const {
		return m_row.IsTaskNameDirty;
	};
	bool IsDescriptionDirty() const {
		return m_row.IsDescriptionDirty;
	};
	bool IsErrorHandlerIdDirty() const {
		return m_row.IsErrorHandlerIdDirty;
	};
	bool IsTaskRoleDirty() const {
		return m_row.IsTaskRoleDirty;
	};
	bool IsDelegationRoleDirty() const {
		return m_row.IsDelegationRoleDirty;
	};
	bool IsRoutingRuleIdDirty() const {
		return m_row.IsRoutingRuleIdDirty;
	};
	bool IsDocumentIdListDirty() const {
		return m_row.IsDocumentIdListDirty;
	};
	bool IsActivityRuleIdDirty() const {
		return m_row.IsActivityRuleIdDirty;
	};
	bool IsPreRoleActivityRuleIdDirty() const {
		return m_row.IsPreRoleActivityRuleIdDirty;
	};
	bool IsTaskTypeDirty() const {
		return m_row.IsTaskTypeDirty;
	};
	bool IsTaskPriorityDirty() const {
		return m_row.IsTaskPriorityDirty;
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
