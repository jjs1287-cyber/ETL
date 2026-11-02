/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_ARCHIVECRMSGDBFINAL_H
#define KONGA_DB_CRMSGDB_ARCHIVECRMSGDBFINAL_H

#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace MsgDb
{

class ArchiveCRMSGDBFinalRow
{
public:
	ArchiveCRMSGDBFinalRow() : 
			IsMsgFromNull(true),
			IsMsgFromPrimaryKey(false),
			IsMsgFromDirty(false),
			IsMsgToNull(true),
			IsMsgToPrimaryKey(false),
			IsMsgToDirty(false),
			IsMsgSubjectNull(true),
			IsMsgSubjectPrimaryKey(false),
			IsMsgSubjectDirty(false),
			IsProcessVariablesNull(true),
			IsProcessVariablesPrimaryKey(false),
			IsProcessVariablesDirty(false),
			IsReceivedAtNull(true),
			IsReceivedAtPrimaryKey(false),
			IsReceivedAtDirty(false),
			IsMsgIdNull(true),
			IsMsgIdPrimaryKey(false),
			IsMsgIdDirty(false),
			IsMsgReplyToNull(true),
			IsMsgReplyToPrimaryKey(false),
			IsMsgReplyToDirty(false),
			IsMsgPriorityNull(true),
			IsMsgPriorityPrimaryKey(false),
			IsMsgPriorityDirty(false),
			IsOwnerNull(true),
			IsOwnerPrimaryKey(false),
			IsOwnerDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false),
			IsMachineNameNull(true),
			IsMachineNamePrimaryKey(false),
			IsMachineNameDirty(false),
			IsCompletionStatusNull(true),
			IsCompletionStatusPrimaryKey(false),
			IsCompletionStatusDirty(false),
			IsErrorTextNull(true),
			IsErrorTextPrimaryKey(false),
			IsErrorTextDirty(false),
			IsDataElementNameNull(true),
			IsDataElementNamePrimaryKey(false),
			IsDataElementNameDirty(false),
			IsDataTypeNull(true),
			IsDataTypePrimaryKey(false),
			IsDataTypeDirty(false),
			IsIntValueNull(true),
			IsIntValuePrimaryKey(false),
			IsIntValueDirty(false),
			IsDoubleValueNull(true),
			IsDoubleValuePrimaryKey(false),
			IsDoubleValueDirty(false),
			IsStringValueNull(true),
			IsStringValuePrimaryKey(false),
			IsStringValueDirty(false)
	{}

	void Copy(const ArchiveCRMSGDBFinalRow& r)
	{
		field_MsgFrom = r.field_MsgFrom;
		IsMsgFromNull = r.IsMsgFromNull;
		IsMsgFromDirty = r.IsMsgFromDirty;

		field_MsgTo = r.field_MsgTo;
		IsMsgToNull = r.IsMsgToNull;
		IsMsgToDirty = r.IsMsgToDirty;

		field_MsgSubject = r.field_MsgSubject;
		IsMsgSubjectNull = r.IsMsgSubjectNull;
		IsMsgSubjectDirty = r.IsMsgSubjectDirty;

		field_ProcessVariables = r.field_ProcessVariables;
		IsProcessVariablesNull = r.IsProcessVariablesNull;
		IsProcessVariablesDirty = r.IsProcessVariablesDirty;

		field_ReceivedAt = r.field_ReceivedAt;
		IsReceivedAtNull = r.IsReceivedAtNull;
		IsReceivedAtDirty = r.IsReceivedAtDirty;

		field_MsgId = r.field_MsgId;
		IsMsgIdNull = r.IsMsgIdNull;
		IsMsgIdDirty = r.IsMsgIdDirty;

		field_MsgReplyTo = r.field_MsgReplyTo;
		IsMsgReplyToNull = r.IsMsgReplyToNull;
		IsMsgReplyToDirty = r.IsMsgReplyToDirty;

		field_MsgPriority = r.field_MsgPriority;
		IsMsgPriorityNull = r.IsMsgPriorityNull;
		IsMsgPriorityDirty = r.IsMsgPriorityDirty;

		field_Owner = r.field_Owner;
		IsOwnerNull = r.IsOwnerNull;
		IsOwnerDirty = r.IsOwnerDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

		field_MachineName = r.field_MachineName;
		IsMachineNameNull = r.IsMachineNameNull;
		IsMachineNameDirty = r.IsMachineNameDirty;

		field_CompletionStatus = r.field_CompletionStatus;
		IsCompletionStatusNull = r.IsCompletionStatusNull;
		IsCompletionStatusDirty = r.IsCompletionStatusDirty;

		field_ErrorText = r.field_ErrorText;
		IsErrorTextNull = r.IsErrorTextNull;
		IsErrorTextDirty = r.IsErrorTextDirty;

		field_DataElementName = r.field_DataElementName;
		IsDataElementNameNull = r.IsDataElementNameNull;
		IsDataElementNameDirty = r.IsDataElementNameDirty;

		field_DataType = r.field_DataType;
		IsDataTypeNull = r.IsDataTypeNull;
		IsDataTypeDirty = r.IsDataTypeDirty;

		field_IntValue = r.field_IntValue;
		IsIntValueNull = r.IsIntValueNull;
		IsIntValueDirty = r.IsIntValueDirty;

		field_DoubleValue = r.field_DoubleValue;
		IsDoubleValueNull = r.IsDoubleValueNull;
		IsDoubleValueDirty = r.IsDoubleValueDirty;

		field_StringValue = r.field_StringValue;
		IsStringValueNull = r.IsStringValueNull;
		IsStringValueDirty = r.IsStringValueDirty;

	}

	void CopyPKs(const ArchiveCRMSGDBFinalRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		IsMsgFromDirty = b;
		IsMsgToDirty = b;
		IsMsgSubjectDirty = b;
		IsProcessVariablesDirty = b;
		IsReceivedAtDirty = b;
		IsMsgIdDirty = b;
		IsMsgReplyToDirty = b;
		IsMsgPriorityDirty = b;
		IsOwnerDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
		IsMachineNameDirty = b;
		IsCompletionStatusDirty = b;
		IsErrorTextDirty = b;
		IsDataElementNameDirty = b;
		IsDataTypeDirty = b;
		IsIntValueDirty = b;
		IsDoubleValueDirty = b;
		IsStringValueDirty = b;
	}

	bool IsDirty() const
	{
		return IsMsgFromDirty ||
			IsMsgToDirty ||
			IsMsgSubjectDirty ||
			IsProcessVariablesDirty ||
			IsReceivedAtDirty ||
			IsMsgIdDirty ||
			IsMsgReplyToDirty ||
			IsMsgPriorityDirty ||
			IsOwnerDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty ||
			IsMachineNameDirty ||
			IsCompletionStatusDirty ||
			IsErrorTextDirty ||
			IsDataElementNameDirty ||
			IsDataTypeDirty ||
			IsIntValueDirty ||
			IsDoubleValueDirty ||
			IsStringValueDirty;
	}

	std::string field_MsgFrom;
	bool IsMsgFromNull;
	bool IsMsgFromDirty;

	std::string field_MsgTo;
	bool IsMsgToNull;
	bool IsMsgToDirty;

	std::string field_MsgSubject;
	bool IsMsgSubjectNull;
	bool IsMsgSubjectDirty;

	std::string field_ProcessVariables;
	bool IsProcessVariablesNull;
	bool IsProcessVariablesDirty;

	int field_ReceivedAt;
	bool IsReceivedAtNull;
	bool IsReceivedAtDirty;

	int field_MsgId;
	bool IsMsgIdNull;
	bool IsMsgIdDirty;

	std::string field_MsgReplyTo;
	bool IsMsgReplyToNull;
	bool IsMsgReplyToDirty;

	int field_MsgPriority;
	bool IsMsgPriorityNull;
	bool IsMsgPriorityDirty;

	std::string field_Owner;
	bool IsOwnerNull;
	bool IsOwnerDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;

	std::string field_MachineName;
	bool IsMachineNameNull;
	bool IsMachineNameDirty;

	int field_CompletionStatus;
	bool IsCompletionStatusNull;
	bool IsCompletionStatusDirty;

	std::string field_ErrorText;
	bool IsErrorTextNull;
	bool IsErrorTextDirty;

	std::string field_DataElementName;
	bool IsDataElementNameNull;
	bool IsDataElementNameDirty;

	std::string field_DataType;
	bool IsDataTypeNull;
	bool IsDataTypeDirty;

	int field_IntValue;
	bool IsIntValueNull;
	bool IsIntValueDirty;

	float field_DoubleValue;
	bool IsDoubleValueNull;
	bool IsDoubleValueDirty;

	std::string field_StringValue;
	bool IsStringValueNull;
	bool IsStringValueDirty;


private:
	bool IsMsgFromPrimaryKey;
	bool IsMsgToPrimaryKey;
	bool IsMsgSubjectPrimaryKey;
	bool IsProcessVariablesPrimaryKey;
	bool IsReceivedAtPrimaryKey;
	bool IsMsgIdPrimaryKey;
	bool IsMsgReplyToPrimaryKey;
	bool IsMsgPriorityPrimaryKey;
	bool IsOwnerPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;
	bool IsMachineNamePrimaryKey;
	bool IsCompletionStatusPrimaryKey;
	bool IsErrorTextPrimaryKey;
	bool IsDataElementNamePrimaryKey;
	bool IsDataTypePrimaryKey;
	bool IsIntValuePrimaryKey;
	bool IsDoubleValuePrimaryKey;
	bool IsStringValuePrimaryKey;

};


class ArchiveCRMSGDBFinal : public konga::DbRecordSet {
public:
	ArchiveCRMSGDBFinal() {}

	static ArchiveCRMSGDBFinal* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ArchiveCRMSGDBFinal";
	}

	void DoFieldExchange(const ArchiveCRMSGDBFinalRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	std::string GetProcessVariables() {
		if( m_row.IsProcessVariablesNull )
			throw konga::DbNullException("ProcessVariables");
		return m_row.field_ProcessVariables;
	}
	void SetProcessVariables(const std::string& st) {
		m_row.field_ProcessVariables = st;
		m_row.IsProcessVariablesNull = false;
		m_row.IsProcessVariablesDirty = true;
	}
	bool IsProcessVariablesNull() const {
		return m_row.IsProcessVariablesNull;
	}
	void SetProcessVariablesToNull() {
		m_row.field_ProcessVariables = "";
		m_row.IsProcessVariablesNull = true;
		m_row.IsProcessVariablesDirty = true;
	}

	int GetReceivedAt() {
		if( m_row.IsReceivedAtNull )
			throw konga::DbNullException("ReceivedAt");
		return m_row.field_ReceivedAt;
	}
	void SetReceivedAt(int i) {
		m_row.field_ReceivedAt = i;
		m_row.IsReceivedAtNull = false;
		m_row.IsReceivedAtDirty = true;
	}
	bool IsReceivedAtNull() const {
		return m_row.IsReceivedAtNull;
	}
	void SetReceivedAtToNull() {
		m_row.IsReceivedAtNull = true;
		m_row.IsReceivedAtDirty = true;
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

	std::string GetMachineName() {
		if( m_row.IsMachineNameNull )
			throw konga::DbNullException("MachineName");
		return m_row.field_MachineName;
	}
	void SetMachineName(const std::string& st) {
		m_row.field_MachineName = st;
		m_row.IsMachineNameNull = false;
		m_row.IsMachineNameDirty = true;
	}
	bool IsMachineNameNull() const {
		return m_row.IsMachineNameNull;
	}
	void SetMachineNameToNull() {
		m_row.field_MachineName = "";
		m_row.IsMachineNameNull = true;
		m_row.IsMachineNameDirty = true;
	}

	int GetCompletionStatus() {
		if( m_row.IsCompletionStatusNull )
			throw konga::DbNullException("CompletionStatus");
		return m_row.field_CompletionStatus;
	}
	void SetCompletionStatus(int i) {
		m_row.field_CompletionStatus = i;
		m_row.IsCompletionStatusNull = false;
		m_row.IsCompletionStatusDirty = true;
	}
	bool IsCompletionStatusNull() const {
		return m_row.IsCompletionStatusNull;
	}
	void SetCompletionStatusToNull() {
		m_row.IsCompletionStatusNull = true;
		m_row.IsCompletionStatusDirty = true;
	}

	std::string GetErrorText() {
		if( m_row.IsErrorTextNull )
			throw konga::DbNullException("ErrorText");
		return m_row.field_ErrorText;
	}
	void SetErrorText(const std::string& st) {
		m_row.field_ErrorText = st;
		m_row.IsErrorTextNull = false;
		m_row.IsErrorTextDirty = true;
	}
	bool IsErrorTextNull() const {
		return m_row.IsErrorTextNull;
	}
	void SetErrorTextToNull() {
		m_row.field_ErrorText = "";
		m_row.IsErrorTextNull = true;
		m_row.IsErrorTextDirty = true;
	}

	std::string GetDataElementName() {
		if( m_row.IsDataElementNameNull )
			throw konga::DbNullException("DataElementName");
		return m_row.field_DataElementName;
	}
	void SetDataElementName(const std::string& st) {
		m_row.field_DataElementName = st;
		m_row.IsDataElementNameNull = false;
		m_row.IsDataElementNameDirty = true;
	}
	bool IsDataElementNameNull() const {
		return m_row.IsDataElementNameNull;
	}
	void SetDataElementNameToNull() {
		m_row.field_DataElementName = "";
		m_row.IsDataElementNameNull = true;
		m_row.IsDataElementNameDirty = true;
	}

	std::string GetDataType() {
		if( m_row.IsDataTypeNull )
			throw konga::DbNullException("DataType");
		return m_row.field_DataType;
	}
	void SetDataType(const std::string& st) {
		m_row.field_DataType = st;
		m_row.IsDataTypeNull = false;
		m_row.IsDataTypeDirty = true;
	}
	bool IsDataTypeNull() const {
		return m_row.IsDataTypeNull;
	}
	void SetDataTypeToNull() {
		m_row.field_DataType = "";
		m_row.IsDataTypeNull = true;
		m_row.IsDataTypeDirty = true;
	}

	int GetIntValue() {
		if( m_row.IsIntValueNull )
			throw konga::DbNullException("IntValue");
		return m_row.field_IntValue;
	}
	void SetIntValue(int i) {
		m_row.field_IntValue = i;
		m_row.IsIntValueNull = false;
		m_row.IsIntValueDirty = true;
	}
	bool IsIntValueNull() const {
		return m_row.IsIntValueNull;
	}
	void SetIntValueToNull() {
		m_row.IsIntValueNull = true;
		m_row.IsIntValueDirty = true;
	}

	float GetDoubleValue() {
		if( m_row.IsDoubleValueNull )
			throw konga::DbNullException("DoubleValue");
		return m_row.field_DoubleValue;
	}
	void SetDoubleValue(float f) {
		m_row.field_DoubleValue = f;
		m_row.IsDoubleValueNull = false;
		m_row.IsDoubleValueDirty = true;
	}
	bool IsDoubleValueNull() const {
		return m_row.IsDoubleValueNull;
	}
	void SetDoubleValueToNull() {
		m_row.IsDoubleValueNull = true;
		m_row.IsDoubleValueDirty = true;
	}

	std::string GetStringValue() {
		if( m_row.IsStringValueNull )
			throw konga::DbNullException("StringValue");
		return m_row.field_StringValue;
	}
	void SetStringValue(const std::string& st) {
		m_row.field_StringValue = st;
		m_row.IsStringValueNull = false;
		m_row.IsStringValueDirty = true;
	}
	bool IsStringValueNull() const {
		return m_row.IsStringValueNull;
	}
	void SetStringValueToNull() {
		m_row.field_StringValue = "";
		m_row.IsStringValueNull = true;
		m_row.IsStringValueDirty = true;
	}

protected:
	ArchiveCRMSGDBFinalRow m_row;

private:
	bool IsMsgFromDirty() const {
		return m_row.IsMsgFromDirty;
	};
	bool IsMsgToDirty() const {
		return m_row.IsMsgToDirty;
	};
	bool IsMsgSubjectDirty() const {
		return m_row.IsMsgSubjectDirty;
	};
	bool IsProcessVariablesDirty() const {
		return m_row.IsProcessVariablesDirty;
	};
	bool IsReceivedAtDirty() const {
		return m_row.IsReceivedAtDirty;
	};
	bool IsMsgIdDirty() const {
		return m_row.IsMsgIdDirty;
	};
	bool IsMsgReplyToDirty() const {
		return m_row.IsMsgReplyToDirty;
	};
	bool IsMsgPriorityDirty() const {
		return m_row.IsMsgPriorityDirty;
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
	bool IsMachineNameDirty() const {
		return m_row.IsMachineNameDirty;
	};
	bool IsCompletionStatusDirty() const {
		return m_row.IsCompletionStatusDirty;
	};
	bool IsErrorTextDirty() const {
		return m_row.IsErrorTextDirty;
	};
	bool IsDataElementNameDirty() const {
		return m_row.IsDataElementNameDirty;
	};
	bool IsDataTypeDirty() const {
		return m_row.IsDataTypeDirty;
	};
	bool IsIntValueDirty() const {
		return m_row.IsIntValueDirty;
	};
	bool IsDoubleValueDirty() const {
		return m_row.IsDoubleValueDirty;
	};
	bool IsStringValueDirty() const {
		return m_row.IsStringValueDirty;
	};


	std::string m_stUpdateFilter;

};

}                        


#endif
