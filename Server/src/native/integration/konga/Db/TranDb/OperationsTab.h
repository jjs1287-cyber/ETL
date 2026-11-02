/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_OPERATIONSTAB_H
#define KONGA_DB_CRTRANDB_OPERATIONSTAB_H

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

class OperationsTabRow
{
public:
	OperationsTabRow() : 
			field_Operation_ID(0),
			IsOperation_IDNull(true),
			IsOperation_IDPrimaryKey(true),
			IsOperation_IDDirty(false),
			IsOperation_NameNull(true),
			IsOperation_NamePrimaryKey(false),
			IsOperation_NameDirty(false),
			field_Operation_Type_ID(0),
			IsOperation_Type_IDNull(true),
			IsOperation_Type_IDPrimaryKey(false),
			IsOperation_Type_IDDirty(false),
			field_Project_ID(0),
			IsProject_IDNull(true),
			IsProject_IDPrimaryKey(false),
			IsProject_IDDirty(false),
			field_Source_ID(0),
			IsSource_IDNull(true),
			IsSource_IDPrimaryKey(false),
			IsSource_IDDirty(false),
			field_Target_ID(0),
			IsTarget_IDNull(true),
			IsTarget_IDPrimaryKey(false),
			IsTarget_IDDirty(false),
			field_Schedule_ID(0),
			IsSchedule_IDNull(true),
			IsSchedule_IDPrimaryKey(false),
			IsSchedule_IDDirty(false),
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(false),
			IsTransformation_IDDirty(false),
			field_Success_Operation_ID(0),
			IsSuccess_Operation_IDNull(true),
			IsSuccess_Operation_IDPrimaryKey(false),
			IsSuccess_Operation_IDDirty(false),
			field_Success_Message_ID(0),
			IsSuccess_Message_IDNull(true),
			IsSuccess_Message_IDPrimaryKey(false),
			IsSuccess_Message_IDDirty(false),
			IsSuccess_WorkflowNull(true),
			IsSuccess_WorkflowPrimaryKey(false),
			IsSuccess_WorkflowDirty(false),
			field_Failure_Operation_ID(0),
			IsFailure_Operation_IDNull(true),
			IsFailure_Operation_IDPrimaryKey(false),
			IsFailure_Operation_IDDirty(false),
			field_Failure_Message_ID(0),
			IsFailure_Message_IDNull(true),
			IsFailure_Message_IDPrimaryKey(false),
			IsFailure_Message_IDDirty(false),
			IsFailure_WorkflowNull(true),
			IsFailure_WorkflowPrimaryKey(false),
			IsFailure_WorkflowDirty(false),
			field_User_ID(0),
			IsUser_IDNull(true),
			IsUser_IDPrimaryKey(false),
			IsUser_IDDirty(false),
			IsDescriptionNull(true),
			IsDescriptionPrimaryKey(false),
			IsDescriptionDirty(false),
			field_Trace(0),
			IsTraceNull(true),
			IsTracePrimaryKey(false),
			IsTraceDirty(false),
			field_DIFf(0),
			IsDIFfNull(true),
			IsDIFfPrimaryKey(false),
			IsDIFfDirty(false),
			field_Ignore_History(0),
			IsIgnore_HistoryNull(true),
			IsIgnore_HistoryPrimaryKey(false),
			IsIgnore_HistoryDirty(false),
			field_EDI_997_Ack(0),
			IsEDI_997_AckNull(true),
			IsEDI_997_AckPrimaryKey(false),
			IsEDI_997_AckDirty(false),
			field_Ignore_EDI_Rules(0),
			IsIgnore_EDI_RulesNull(true),
			IsIgnore_EDI_RulesPrimaryKey(false),
			IsIgnore_EDI_RulesDirty(false),
			IsTPAGUIDNull(true),
			IsTPAGUIDPrimaryKey(false),
			IsTPAGUIDDirty(false),
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

	void Copy(const OperationsTabRow& r)
	{
		field_Operation_ID = r.field_Operation_ID;
		IsOperation_IDNull = r.IsOperation_IDNull;
		IsOperation_IDDirty = r.IsOperation_IDDirty;

		field_Operation_Name = r.field_Operation_Name;
		IsOperation_NameNull = r.IsOperation_NameNull;
		IsOperation_NameDirty = r.IsOperation_NameDirty;

		field_Operation_Type_ID = r.field_Operation_Type_ID;
		IsOperation_Type_IDNull = r.IsOperation_Type_IDNull;
		IsOperation_Type_IDDirty = r.IsOperation_Type_IDDirty;

		field_Project_ID = r.field_Project_ID;
		IsProject_IDNull = r.IsProject_IDNull;
		IsProject_IDDirty = r.IsProject_IDDirty;

		field_Source_ID = r.field_Source_ID;
		IsSource_IDNull = r.IsSource_IDNull;
		IsSource_IDDirty = r.IsSource_IDDirty;

		field_Target_ID = r.field_Target_ID;
		IsTarget_IDNull = r.IsTarget_IDNull;
		IsTarget_IDDirty = r.IsTarget_IDDirty;

		field_Schedule_ID = r.field_Schedule_ID;
		IsSchedule_IDNull = r.IsSchedule_IDNull;
		IsSchedule_IDDirty = r.IsSchedule_IDDirty;

		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_Success_Operation_ID = r.field_Success_Operation_ID;
		IsSuccess_Operation_IDNull = r.IsSuccess_Operation_IDNull;
		IsSuccess_Operation_IDDirty = r.IsSuccess_Operation_IDDirty;

		field_Success_Message_ID = r.field_Success_Message_ID;
		IsSuccess_Message_IDNull = r.IsSuccess_Message_IDNull;
		IsSuccess_Message_IDDirty = r.IsSuccess_Message_IDDirty;

		field_Success_Workflow = r.field_Success_Workflow;
		IsSuccess_WorkflowNull = r.IsSuccess_WorkflowNull;
		IsSuccess_WorkflowDirty = r.IsSuccess_WorkflowDirty;

		field_Failure_Operation_ID = r.field_Failure_Operation_ID;
		IsFailure_Operation_IDNull = r.IsFailure_Operation_IDNull;
		IsFailure_Operation_IDDirty = r.IsFailure_Operation_IDDirty;

		field_Failure_Message_ID = r.field_Failure_Message_ID;
		IsFailure_Message_IDNull = r.IsFailure_Message_IDNull;
		IsFailure_Message_IDDirty = r.IsFailure_Message_IDDirty;

		field_Failure_Workflow = r.field_Failure_Workflow;
		IsFailure_WorkflowNull = r.IsFailure_WorkflowNull;
		IsFailure_WorkflowDirty = r.IsFailure_WorkflowDirty;

		field_User_ID = r.field_User_ID;
		IsUser_IDNull = r.IsUser_IDNull;
		IsUser_IDDirty = r.IsUser_IDDirty;

		field_Description = r.field_Description;
		IsDescriptionNull = r.IsDescriptionNull;
		IsDescriptionDirty = r.IsDescriptionDirty;

		field_Trace = r.field_Trace;
		IsTraceNull = r.IsTraceNull;
		IsTraceDirty = r.IsTraceDirty;

		field_DIFf = r.field_DIFf;
		IsDIFfNull = r.IsDIFfNull;
		IsDIFfDirty = r.IsDIFfDirty;

		field_Ignore_History = r.field_Ignore_History;
		IsIgnore_HistoryNull = r.IsIgnore_HistoryNull;
		IsIgnore_HistoryDirty = r.IsIgnore_HistoryDirty;

		field_EDI_997_Ack = r.field_EDI_997_Ack;
		IsEDI_997_AckNull = r.IsEDI_997_AckNull;
		IsEDI_997_AckDirty = r.IsEDI_997_AckDirty;

		field_Ignore_EDI_Rules = r.field_Ignore_EDI_Rules;
		IsIgnore_EDI_RulesNull = r.IsIgnore_EDI_RulesNull;
		IsIgnore_EDI_RulesDirty = r.IsIgnore_EDI_RulesDirty;

		field_TPAGUID = r.field_TPAGUID;
		IsTPAGUIDNull = r.IsTPAGUIDNull;
		IsTPAGUIDDirty = r.IsTPAGUIDDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const OperationsTabRow& r)
	{
		field_Operation_ID = r.field_Operation_ID;
		IsOperation_IDNull = r.IsOperation_IDNull;
		IsOperation_IDDirty = r.IsOperation_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsOperation_IDDirty = b;
		IsOperation_NameDirty = b;
		IsOperation_Type_IDDirty = b;
		IsProject_IDDirty = b;
		IsSource_IDDirty = b;
		IsTarget_IDDirty = b;
		IsSchedule_IDDirty = b;
		IsTransformation_IDDirty = b;
		IsSuccess_Operation_IDDirty = b;
		IsSuccess_Message_IDDirty = b;
		IsSuccess_WorkflowDirty = b;
		IsFailure_Operation_IDDirty = b;
		IsFailure_Message_IDDirty = b;
		IsFailure_WorkflowDirty = b;
		IsUser_IDDirty = b;
		IsDescriptionDirty = b;
		IsTraceDirty = b;
		IsDIFfDirty = b;
		IsIgnore_HistoryDirty = b;
		IsEDI_997_AckDirty = b;
		IsIgnore_EDI_RulesDirty = b;
		IsTPAGUIDDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsOperation_IDDirty ||
			IsOperation_NameDirty ||
			IsOperation_Type_IDDirty ||
			IsProject_IDDirty ||
			IsSource_IDDirty ||
			IsTarget_IDDirty ||
			IsSchedule_IDDirty ||
			IsTransformation_IDDirty ||
			IsSuccess_Operation_IDDirty ||
			IsSuccess_Message_IDDirty ||
			IsSuccess_WorkflowDirty ||
			IsFailure_Operation_IDDirty ||
			IsFailure_Message_IDDirty ||
			IsFailure_WorkflowDirty ||
			IsUser_IDDirty ||
			IsDescriptionDirty ||
			IsTraceDirty ||
			IsDIFfDirty ||
			IsIgnore_HistoryDirty ||
			IsEDI_997_AckDirty ||
			IsIgnore_EDI_RulesDirty ||
			IsTPAGUIDDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Operation_ID;
	bool IsOperation_IDNull;
	bool IsOperation_IDDirty;

	std::string field_Operation_Name;
	bool IsOperation_NameNull;
	bool IsOperation_NameDirty;

	int field_Operation_Type_ID;
	bool IsOperation_Type_IDNull;
	bool IsOperation_Type_IDDirty;

	int field_Project_ID;
	bool IsProject_IDNull;
	bool IsProject_IDDirty;

	int field_Source_ID;
	bool IsSource_IDNull;
	bool IsSource_IDDirty;

	int field_Target_ID;
	bool IsTarget_IDNull;
	bool IsTarget_IDDirty;

	int field_Schedule_ID;
	bool IsSchedule_IDNull;
	bool IsSchedule_IDDirty;

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	int field_Success_Operation_ID;
	bool IsSuccess_Operation_IDNull;
	bool IsSuccess_Operation_IDDirty;

	int field_Success_Message_ID;
	bool IsSuccess_Message_IDNull;
	bool IsSuccess_Message_IDDirty;

	std::string field_Success_Workflow;
	bool IsSuccess_WorkflowNull;
	bool IsSuccess_WorkflowDirty;

	int field_Failure_Operation_ID;
	bool IsFailure_Operation_IDNull;
	bool IsFailure_Operation_IDDirty;

	int field_Failure_Message_ID;
	bool IsFailure_Message_IDNull;
	bool IsFailure_Message_IDDirty;

	std::string field_Failure_Workflow;
	bool IsFailure_WorkflowNull;
	bool IsFailure_WorkflowDirty;

	int field_User_ID;
	bool IsUser_IDNull;
	bool IsUser_IDDirty;

	std::string field_Description;
	bool IsDescriptionNull;
	bool IsDescriptionDirty;

	int field_Trace;
	bool IsTraceNull;
	bool IsTraceDirty;

	int field_DIFf;
	bool IsDIFfNull;
	bool IsDIFfDirty;

	int field_Ignore_History;
	bool IsIgnore_HistoryNull;
	bool IsIgnore_HistoryDirty;

	int field_EDI_997_Ack;
	bool IsEDI_997_AckNull;
	bool IsEDI_997_AckDirty;

	int field_Ignore_EDI_Rules;
	bool IsIgnore_EDI_RulesNull;
	bool IsIgnore_EDI_RulesDirty;

	std::string field_TPAGUID;
	bool IsTPAGUIDNull;
	bool IsTPAGUIDDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsOperation_IDPrimaryKey;
	bool IsOperation_NamePrimaryKey;
	bool IsOperation_Type_IDPrimaryKey;
	bool IsProject_IDPrimaryKey;
	bool IsSource_IDPrimaryKey;
	bool IsTarget_IDPrimaryKey;
	bool IsSchedule_IDPrimaryKey;
	bool IsTransformation_IDPrimaryKey;
	bool IsSuccess_Operation_IDPrimaryKey;
	bool IsSuccess_Message_IDPrimaryKey;
	bool IsSuccess_WorkflowPrimaryKey;
	bool IsFailure_Operation_IDPrimaryKey;
	bool IsFailure_Message_IDPrimaryKey;
	bool IsFailure_WorkflowPrimaryKey;
	bool IsUser_IDPrimaryKey;
	bool IsDescriptionPrimaryKey;
	bool IsTracePrimaryKey;
	bool IsDIFfPrimaryKey;
	bool IsIgnore_HistoryPrimaryKey;
	bool IsEDI_997_AckPrimaryKey;
	bool IsIgnore_EDI_RulesPrimaryKey;
	bool IsTPAGUIDPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class OperationsTab : public konga::DbRecordSet {
public:
	OperationsTab() {}

	static OperationsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "OperationsTab";
	}

	void DoFieldExchange(const OperationsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	int GetOperation_Type_ID() {
		if( m_row.IsOperation_Type_IDNull )
			throw konga::DbNullException("Operation_Type_ID");
		return m_row.field_Operation_Type_ID;
	}
	void SetOperation_Type_ID(int i) {
		m_row.field_Operation_Type_ID = i;
		m_row.IsOperation_Type_IDNull = false;
		m_row.IsOperation_Type_IDDirty = true;
	}
	bool IsOperation_Type_IDNull() const {
		return m_row.IsOperation_Type_IDNull;
	}
	void SetOperation_Type_IDToNull() {
		m_row.IsOperation_Type_IDNull = true;
		m_row.IsOperation_Type_IDDirty = true;
	}

	int GetProject_ID() {
		if( m_row.IsProject_IDNull )
			throw konga::DbNullException("Project_ID");
		return m_row.field_Project_ID;
	}
	void SetProject_ID(int i) {
		m_row.field_Project_ID = i;
		m_row.IsProject_IDNull = false;
		m_row.IsProject_IDDirty = true;
	}
	bool IsProject_IDNull() const {
		return m_row.IsProject_IDNull;
	}
	void SetProject_IDToNull() {
		m_row.IsProject_IDNull = true;
		m_row.IsProject_IDDirty = true;
	}

	int GetSource_ID() {
		if( m_row.IsSource_IDNull )
			throw konga::DbNullException("Source_ID");
		return m_row.field_Source_ID;
	}
	void SetSource_ID(int i) {
		m_row.field_Source_ID = i;
		m_row.IsSource_IDNull = false;
		m_row.IsSource_IDDirty = true;
	}
	bool IsSource_IDNull() const {
		return m_row.IsSource_IDNull;
	}
	void SetSource_IDToNull() {
		m_row.IsSource_IDNull = true;
		m_row.IsSource_IDDirty = true;
	}

	int GetTarget_ID() {
		if( m_row.IsTarget_IDNull )
			throw konga::DbNullException("Target_ID");
		return m_row.field_Target_ID;
	}
	void SetTarget_ID(int i) {
		m_row.field_Target_ID = i;
		m_row.IsTarget_IDNull = false;
		m_row.IsTarget_IDDirty = true;
	}
	bool IsTarget_IDNull() const {
		return m_row.IsTarget_IDNull;
	}
	void SetTarget_IDToNull() {
		m_row.IsTarget_IDNull = true;
		m_row.IsTarget_IDDirty = true;
	}

	int GetSchedule_ID() {
		if( m_row.IsSchedule_IDNull )
			throw konga::DbNullException("Schedule_ID");
		return m_row.field_Schedule_ID;
	}
	void SetSchedule_ID(int i) {
		m_row.field_Schedule_ID = i;
		m_row.IsSchedule_IDNull = false;
		m_row.IsSchedule_IDDirty = true;
	}
	bool IsSchedule_IDNull() const {
		return m_row.IsSchedule_IDNull;
	}
	void SetSchedule_IDToNull() {
		m_row.IsSchedule_IDNull = true;
		m_row.IsSchedule_IDDirty = true;
	}

	int GetTransformation_ID() {
		if( m_row.IsTransformation_IDNull )
			throw konga::DbNullException("Transformation_ID");
		return m_row.field_Transformation_ID;
	}
	void SetTransformation_ID(int i) {
		m_row.field_Transformation_ID = i;
		m_row.IsTransformation_IDNull = false;
		m_row.IsTransformation_IDDirty = true;
	}
	bool IsTransformation_IDNull() const {
		return m_row.IsTransformation_IDNull;
	}
	void SetTransformation_IDToNull() {
		m_row.IsTransformation_IDNull = true;
		m_row.IsTransformation_IDDirty = true;
	}

	int GetSuccess_Operation_ID() {
		if( m_row.IsSuccess_Operation_IDNull )
			throw konga::DbNullException("Success_Operation_ID");
		return m_row.field_Success_Operation_ID;
	}
	void SetSuccess_Operation_ID(int i) {
		m_row.field_Success_Operation_ID = i;
		m_row.IsSuccess_Operation_IDNull = false;
		m_row.IsSuccess_Operation_IDDirty = true;
	}
	bool IsSuccess_Operation_IDNull() const {
		return m_row.IsSuccess_Operation_IDNull;
	}
	void SetSuccess_Operation_IDToNull() {
		m_row.IsSuccess_Operation_IDNull = true;
		m_row.IsSuccess_Operation_IDDirty = true;
	}

	int GetSuccess_Message_ID() {
		if( m_row.IsSuccess_Message_IDNull )
			throw konga::DbNullException("Success_Message_ID");
		return m_row.field_Success_Message_ID;
	}
	void SetSuccess_Message_ID(int i) {
		m_row.field_Success_Message_ID = i;
		m_row.IsSuccess_Message_IDNull = false;
		m_row.IsSuccess_Message_IDDirty = true;
	}
	bool IsSuccess_Message_IDNull() const {
		return m_row.IsSuccess_Message_IDNull;
	}
	void SetSuccess_Message_IDToNull() {
		m_row.IsSuccess_Message_IDNull = true;
		m_row.IsSuccess_Message_IDDirty = true;
	}

	std::string GetSuccess_Workflow() {
		if( m_row.IsSuccess_WorkflowNull )
			throw konga::DbNullException("Success_Workflow");
		return m_row.field_Success_Workflow;
	}
	void SetSuccess_Workflow(const std::string& st) {
		m_row.field_Success_Workflow = st;
		m_row.IsSuccess_WorkflowNull = false;
		m_row.IsSuccess_WorkflowDirty = true;
	}
	bool IsSuccess_WorkflowNull() const {
		return m_row.IsSuccess_WorkflowNull;
	}
	void SetSuccess_WorkflowToNull() {
		m_row.field_Success_Workflow = "";
		m_row.IsSuccess_WorkflowNull = true;
		m_row.IsSuccess_WorkflowDirty = true;
	}

	int GetFailure_Operation_ID() {
		if( m_row.IsFailure_Operation_IDNull )
			throw konga::DbNullException("Failure_Operation_ID");
		return m_row.field_Failure_Operation_ID;
	}
	void SetFailure_Operation_ID(int i) {
		m_row.field_Failure_Operation_ID = i;
		m_row.IsFailure_Operation_IDNull = false;
		m_row.IsFailure_Operation_IDDirty = true;
	}
	bool IsFailure_Operation_IDNull() const {
		return m_row.IsFailure_Operation_IDNull;
	}
	void SetFailure_Operation_IDToNull() {
		m_row.IsFailure_Operation_IDNull = true;
		m_row.IsFailure_Operation_IDDirty = true;
	}

	int GetFailure_Message_ID() {
		if( m_row.IsFailure_Message_IDNull )
			throw konga::DbNullException("Failure_Message_ID");
		return m_row.field_Failure_Message_ID;
	}
	void SetFailure_Message_ID(int i) {
		m_row.field_Failure_Message_ID = i;
		m_row.IsFailure_Message_IDNull = false;
		m_row.IsFailure_Message_IDDirty = true;
	}
	bool IsFailure_Message_IDNull() const {
		return m_row.IsFailure_Message_IDNull;
	}
	void SetFailure_Message_IDToNull() {
		m_row.IsFailure_Message_IDNull = true;
		m_row.IsFailure_Message_IDDirty = true;
	}

	std::string GetFailure_Workflow() {
		if( m_row.IsFailure_WorkflowNull )
			throw konga::DbNullException("Failure_Workflow");
		return m_row.field_Failure_Workflow;
	}
	void SetFailure_Workflow(const std::string& st) {
		m_row.field_Failure_Workflow = st;
		m_row.IsFailure_WorkflowNull = false;
		m_row.IsFailure_WorkflowDirty = true;
	}
	bool IsFailure_WorkflowNull() const {
		return m_row.IsFailure_WorkflowNull;
	}
	void SetFailure_WorkflowToNull() {
		m_row.field_Failure_Workflow = "";
		m_row.IsFailure_WorkflowNull = true;
		m_row.IsFailure_WorkflowDirty = true;
	}

	int GetUser_ID() {
		if( m_row.IsUser_IDNull )
			throw konga::DbNullException("User_ID");
		return m_row.field_User_ID;
	}
	void SetUser_ID(int i) {
		m_row.field_User_ID = i;
		m_row.IsUser_IDNull = false;
		m_row.IsUser_IDDirty = true;
	}
	bool IsUser_IDNull() const {
		return m_row.IsUser_IDNull;
	}
	void SetUser_IDToNull() {
		m_row.IsUser_IDNull = true;
		m_row.IsUser_IDDirty = true;
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

	int GetTrace() {
		if( m_row.IsTraceNull )
			throw konga::DbNullException("Trace");
		return m_row.field_Trace;
	}
	void SetTrace(int i) {
		m_row.field_Trace = i;
		m_row.IsTraceNull = false;
		m_row.IsTraceDirty = true;
	}
	bool IsTraceNull() const {
		return m_row.IsTraceNull;
	}
	void SetTraceToNull() {
		m_row.IsTraceNull = true;
		m_row.IsTraceDirty = true;
	}

	int GetDIFf() {
		if( m_row.IsDIFfNull )
			throw konga::DbNullException("DIFf");
		return m_row.field_DIFf;
	}
	void SetDIFf(int i) {
		m_row.field_DIFf = i;
		m_row.IsDIFfNull = false;
		m_row.IsDIFfDirty = true;
	}
	bool IsDIFfNull() const {
		return m_row.IsDIFfNull;
	}
	void SetDIFfToNull() {
		m_row.IsDIFfNull = true;
		m_row.IsDIFfDirty = true;
	}

	int GetIgnore_History() {
		if( m_row.IsIgnore_HistoryNull )
			throw konga::DbNullException("Ignore_History");
		return m_row.field_Ignore_History;
	}
	void SetIgnore_History(int i) {
		m_row.field_Ignore_History = i;
		m_row.IsIgnore_HistoryNull = false;
		m_row.IsIgnore_HistoryDirty = true;
	}
	bool IsIgnore_HistoryNull() const {
		return m_row.IsIgnore_HistoryNull;
	}
	void SetIgnore_HistoryToNull() {
		m_row.IsIgnore_HistoryNull = true;
		m_row.IsIgnore_HistoryDirty = true;
	}

	int GetEDI_997_Ack() {
		if( m_row.IsEDI_997_AckNull )
			throw konga::DbNullException("EDI_997_Ack");
		return m_row.field_EDI_997_Ack;
	}
	void SetEDI_997_Ack(int i) {
		m_row.field_EDI_997_Ack = i;
		m_row.IsEDI_997_AckNull = false;
		m_row.IsEDI_997_AckDirty = true;
	}
	bool IsEDI_997_AckNull() const {
		return m_row.IsEDI_997_AckNull;
	}
	void SetEDI_997_AckToNull() {
		m_row.IsEDI_997_AckNull = true;
		m_row.IsEDI_997_AckDirty = true;
	}

	int GetIgnore_EDI_Rules() {
		if( m_row.IsIgnore_EDI_RulesNull )
			throw konga::DbNullException("Ignore_EDI_Rules");
		return m_row.field_Ignore_EDI_Rules;
	}
	void SetIgnore_EDI_Rules(int i) {
		m_row.field_Ignore_EDI_Rules = i;
		m_row.IsIgnore_EDI_RulesNull = false;
		m_row.IsIgnore_EDI_RulesDirty = true;
	}
	bool IsIgnore_EDI_RulesNull() const {
		return m_row.IsIgnore_EDI_RulesNull;
	}
	void SetIgnore_EDI_RulesToNull() {
		m_row.IsIgnore_EDI_RulesNull = true;
		m_row.IsIgnore_EDI_RulesDirty = true;
	}

	std::string GetTPAGUID() {
		if( m_row.IsTPAGUIDNull )
			throw konga::DbNullException("TPAGUID");
		return m_row.field_TPAGUID;
	}
	void SetTPAGUID(const std::string& st) {
		m_row.field_TPAGUID = st;
		m_row.IsTPAGUIDNull = false;
		m_row.IsTPAGUIDDirty = true;
	}
	bool IsTPAGUIDNull() const {
		return m_row.IsTPAGUIDNull;
	}
	void SetTPAGUIDToNull() {
		m_row.field_TPAGUID = "";
		m_row.IsTPAGUIDNull = true;
		m_row.IsTPAGUIDDirty = true;
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
	OperationsTabRow m_row;

private:
	bool IsOperation_IDDirty() const {
		return m_row.IsOperation_IDDirty;
	};
	bool IsOperation_NameDirty() const {
		return m_row.IsOperation_NameDirty;
	};
	bool IsOperation_Type_IDDirty() const {
		return m_row.IsOperation_Type_IDDirty;
	};
	bool IsProject_IDDirty() const {
		return m_row.IsProject_IDDirty;
	};
	bool IsSource_IDDirty() const {
		return m_row.IsSource_IDDirty;
	};
	bool IsTarget_IDDirty() const {
		return m_row.IsTarget_IDDirty;
	};
	bool IsSchedule_IDDirty() const {
		return m_row.IsSchedule_IDDirty;
	};
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsSuccess_Operation_IDDirty() const {
		return m_row.IsSuccess_Operation_IDDirty;
	};
	bool IsSuccess_Message_IDDirty() const {
		return m_row.IsSuccess_Message_IDDirty;
	};
	bool IsSuccess_WorkflowDirty() const {
		return m_row.IsSuccess_WorkflowDirty;
	};
	bool IsFailure_Operation_IDDirty() const {
		return m_row.IsFailure_Operation_IDDirty;
	};
	bool IsFailure_Message_IDDirty() const {
		return m_row.IsFailure_Message_IDDirty;
	};
	bool IsFailure_WorkflowDirty() const {
		return m_row.IsFailure_WorkflowDirty;
	};
	bool IsUser_IDDirty() const {
		return m_row.IsUser_IDDirty;
	};
	bool IsDescriptionDirty() const {
		return m_row.IsDescriptionDirty;
	};
	bool IsTraceDirty() const {
		return m_row.IsTraceDirty;
	};
	bool IsDIFfDirty() const {
		return m_row.IsDIFfDirty;
	};
	bool IsIgnore_HistoryDirty() const {
		return m_row.IsIgnore_HistoryDirty;
	};
	bool IsEDI_997_AckDirty() const {
		return m_row.IsEDI_997_AckDirty;
	};
	bool IsIgnore_EDI_RulesDirty() const {
		return m_row.IsIgnore_EDI_RulesDirty;
	};
	bool IsTPAGUIDDirty() const {
		return m_row.IsTPAGUIDDirty;
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
