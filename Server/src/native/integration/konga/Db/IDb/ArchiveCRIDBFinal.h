/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_ARCHIVECRIDBFINAL_H
#define KONGA_DB_CRIDB_ARCHIVECRIDBFINAL_H

#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace IDb
{

class ArchiveCRIDBFinalRow
{
public:
	ArchiveCRIDBFinalRow() : 
			IsStringValueNull(true),
			IsStringValuePrimaryKey(false),
			IsStringValueDirty(false),
			IsDoubleValueNull(true),
			IsDoubleValuePrimaryKey(false),
			IsDoubleValueDirty(false),
			IsIntValueNull(true),
			IsIntValuePrimaryKey(false),
			IsIntValueDirty(false),
			IsDataElementNameNull(true),
			IsDataElementNamePrimaryKey(false),
			IsDataElementNameDirty(false),
			IsDataTypeNull(true),
			IsDataTypePrimaryKey(false),
			IsDataTypeDirty(false),
			IsTaskUserNull(true),
			IsTaskUserPrimaryKey(false),
			IsTaskUserDirty(false),
			IsDataElementValueListNull(true),
			IsDataElementValueListPrimaryKey(false),
			IsDataElementValueListDirty(false),
			IsFromDocDataElementValueListNull(true),
			IsFromDocDataElementValueListPrimaryKey(false),
			IsFromDocDataElementValueListDirty(false),
			IsRouteUserNull(true),
			IsRouteUserPrimaryKey(false),
			IsRouteUserDirty(false),
			IsMsgPriorityNull(true),
			IsMsgPriorityPrimaryKey(false),
			IsMsgPriorityDirty(false),
			IsInitialTaskNull(true),
			IsInitialTaskPrimaryKey(false),
			IsInitialTaskDirty(false),
			IsUpdateFlagNull(true),
			IsUpdateFlagPrimaryKey(false),
			IsUpdateFlagDirty(false),
			IsProcessDataElementValueListNull(true),
			IsProcessDataElementValueListPrimaryKey(false),
			IsProcessDataElementValueListDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false),
			IsRouteInstanceIdNull(true),
			IsRouteInstanceIdPrimaryKey(false),
			IsRouteInstanceIdDirty(false),
			IsSentTimeNull(true),
			IsSentTimePrimaryKey(false),
			IsSentTimeDirty(false),
			IsStatusTimeNull(true),
			IsStatusTimePrimaryKey(false),
			IsStatusTimeDirty(false),
			IsStatusNull(true),
			IsStatusPrimaryKey(false),
			IsStatusDirty(false),
			IsEndTimeNull(true),
			IsEndTimePrimaryKey(false),
			IsEndTimeDirty(false),
			IsStartTimeNull(true),
			IsStartTimePrimaryKey(false),
			IsStartTimeDirty(false),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(false),
			IsProcessInstanceIdDirty(false),
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
			IsSentAtNull(true),
			IsSentAtPrimaryKey(false),
			IsSentAtDirty(false),
			IsURLNull(true),
			IsURLPrimaryKey(false),
			IsURLDirty(false),
			IsMsgCCNull(true),
			IsMsgCCPrimaryKey(false),
			IsMsgCCDirty(false),
			IsMsgBCCNull(true),
			IsMsgBCCPrimaryKey(false),
			IsMsgBCCDirty(false),
			IsMhtmlUrlNull(true),
			IsMhtmlUrlPrimaryKey(false),
			IsMhtmlUrlDirty(false),
			IsMsgReplyToNull(true),
			IsMsgReplyToPrimaryKey(false),
			IsMsgReplyToDirty(false),
			IsOwnerNull(true),
			IsOwnerPrimaryKey(false),
			IsOwnerDirty(false),
			IsMimeContentTypeNull(true),
			IsMimeContentTypePrimaryKey(false),
			IsMimeContentTypeDirty(false),
			IsFileNameNull(true),
			IsFileNamePrimaryKey(false),
			IsFileNameDirty(false)
	{}

	void Copy(const ArchiveCRIDBFinalRow& r)
	{
		field_StringValue = r.field_StringValue;
		IsStringValueNull = r.IsStringValueNull;
		IsStringValueDirty = r.IsStringValueDirty;

		field_DoubleValue = r.field_DoubleValue;
		IsDoubleValueNull = r.IsDoubleValueNull;
		IsDoubleValueDirty = r.IsDoubleValueDirty;

		field_IntValue = r.field_IntValue;
		IsIntValueNull = r.IsIntValueNull;
		IsIntValueDirty = r.IsIntValueDirty;

		field_DataElementName = r.field_DataElementName;
		IsDataElementNameNull = r.IsDataElementNameNull;
		IsDataElementNameDirty = r.IsDataElementNameDirty;

		field_DataType = r.field_DataType;
		IsDataTypeNull = r.IsDataTypeNull;
		IsDataTypeDirty = r.IsDataTypeDirty;

		field_TaskUser = r.field_TaskUser;
		IsTaskUserNull = r.IsTaskUserNull;
		IsTaskUserDirty = r.IsTaskUserDirty;

		field_DataElementValueList = r.field_DataElementValueList;
		IsDataElementValueListNull = r.IsDataElementValueListNull;
		IsDataElementValueListDirty = r.IsDataElementValueListDirty;

		field_FromDocDataElementValueList = r.field_FromDocDataElementValueList;
		IsFromDocDataElementValueListNull = r.IsFromDocDataElementValueListNull;
		IsFromDocDataElementValueListDirty = r.IsFromDocDataElementValueListDirty;

		field_RouteUser = r.field_RouteUser;
		IsRouteUserNull = r.IsRouteUserNull;
		IsRouteUserDirty = r.IsRouteUserDirty;

		field_MsgPriority = r.field_MsgPriority;
		IsMsgPriorityNull = r.IsMsgPriorityNull;
		IsMsgPriorityDirty = r.IsMsgPriorityDirty;

		field_InitialTask = r.field_InitialTask;
		IsInitialTaskNull = r.IsInitialTaskNull;
		IsInitialTaskDirty = r.IsInitialTaskDirty;

		field_UpdateFlag = r.field_UpdateFlag;
		IsUpdateFlagNull = r.IsUpdateFlagNull;
		IsUpdateFlagDirty = r.IsUpdateFlagDirty;

		field_ProcessDataElementValueList = r.field_ProcessDataElementValueList;
		IsProcessDataElementValueListNull = r.IsProcessDataElementValueListNull;
		IsProcessDataElementValueListDirty = r.IsProcessDataElementValueListDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

		field_RouteInstanceId = r.field_RouteInstanceId;
		IsRouteInstanceIdNull = r.IsRouteInstanceIdNull;
		IsRouteInstanceIdDirty = r.IsRouteInstanceIdDirty;

		field_SentTime = r.field_SentTime;
		IsSentTimeNull = r.IsSentTimeNull;
		IsSentTimeDirty = r.IsSentTimeDirty;

		field_StatusTime = r.field_StatusTime;
		IsStatusTimeNull = r.IsStatusTimeNull;
		IsStatusTimeDirty = r.IsStatusTimeDirty;

		field_Status = r.field_Status;
		IsStatusNull = r.IsStatusNull;
		IsStatusDirty = r.IsStatusDirty;

		field_EndTime = r.field_EndTime;
		IsEndTimeNull = r.IsEndTimeNull;
		IsEndTimeDirty = r.IsEndTimeDirty;

		field_StartTime = r.field_StartTime;
		IsStartTimeNull = r.IsStartTimeNull;
		IsStartTimeDirty = r.IsStartTimeDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

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

		field_SentAt = r.field_SentAt;
		IsSentAtNull = r.IsSentAtNull;
		IsSentAtDirty = r.IsSentAtDirty;

		field_URL = r.field_URL;
		IsURLNull = r.IsURLNull;
		IsURLDirty = r.IsURLDirty;

		field_MsgCC = r.field_MsgCC;
		IsMsgCCNull = r.IsMsgCCNull;
		IsMsgCCDirty = r.IsMsgCCDirty;

		field_MsgBCC = r.field_MsgBCC;
		IsMsgBCCNull = r.IsMsgBCCNull;
		IsMsgBCCDirty = r.IsMsgBCCDirty;

		field_MhtmlUrl = r.field_MhtmlUrl;
		IsMhtmlUrlNull = r.IsMhtmlUrlNull;
		IsMhtmlUrlDirty = r.IsMhtmlUrlDirty;

		field_MsgReplyTo = r.field_MsgReplyTo;
		IsMsgReplyToNull = r.IsMsgReplyToNull;
		IsMsgReplyToDirty = r.IsMsgReplyToDirty;

		field_Owner = r.field_Owner;
		IsOwnerNull = r.IsOwnerNull;
		IsOwnerDirty = r.IsOwnerDirty;

		field_MimeContentType = r.field_MimeContentType;
		IsMimeContentTypeNull = r.IsMimeContentTypeNull;
		IsMimeContentTypeDirty = r.IsMimeContentTypeDirty;

		field_FileName = r.field_FileName;
		IsFileNameNull = r.IsFileNameNull;
		IsFileNameDirty = r.IsFileNameDirty;

	}

	void CopyPKs(const ArchiveCRIDBFinalRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		IsStringValueDirty = b;
		IsDoubleValueDirty = b;
		IsIntValueDirty = b;
		IsDataElementNameDirty = b;
		IsDataTypeDirty = b;
		IsTaskUserDirty = b;
		IsDataElementValueListDirty = b;
		IsFromDocDataElementValueListDirty = b;
		IsRouteUserDirty = b;
		IsMsgPriorityDirty = b;
		IsInitialTaskDirty = b;
		IsUpdateFlagDirty = b;
		IsProcessDataElementValueListDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
		IsRouteInstanceIdDirty = b;
		IsSentTimeDirty = b;
		IsStatusTimeDirty = b;
		IsStatusDirty = b;
		IsEndTimeDirty = b;
		IsStartTimeDirty = b;
		IsProcessInstanceIdDirty = b;
		IsMsgFromDirty = b;
		IsMsgToDirty = b;
		IsMsgSubjectDirty = b;
		IsMessageFileNameDirty = b;
		IsFormatDirty = b;
		IsSentAtDirty = b;
		IsURLDirty = b;
		IsMsgCCDirty = b;
		IsMsgBCCDirty = b;
		IsMhtmlUrlDirty = b;
		IsMsgReplyToDirty = b;
		IsOwnerDirty = b;
		IsMimeContentTypeDirty = b;
		IsFileNameDirty = b;
	}

	bool IsDirty() const
	{
		return IsStringValueDirty ||
			IsDoubleValueDirty ||
			IsIntValueDirty ||
			IsDataElementNameDirty ||
			IsDataTypeDirty ||
			IsTaskUserDirty ||
			IsDataElementValueListDirty ||
			IsFromDocDataElementValueListDirty ||
			IsRouteUserDirty ||
			IsMsgPriorityDirty ||
			IsInitialTaskDirty ||
			IsUpdateFlagDirty ||
			IsProcessDataElementValueListDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty ||
			IsRouteInstanceIdDirty ||
			IsSentTimeDirty ||
			IsStatusTimeDirty ||
			IsStatusDirty ||
			IsEndTimeDirty ||
			IsStartTimeDirty ||
			IsProcessInstanceIdDirty ||
			IsMsgFromDirty ||
			IsMsgToDirty ||
			IsMsgSubjectDirty ||
			IsMessageFileNameDirty ||
			IsFormatDirty ||
			IsSentAtDirty ||
			IsURLDirty ||
			IsMsgCCDirty ||
			IsMsgBCCDirty ||
			IsMhtmlUrlDirty ||
			IsMsgReplyToDirty ||
			IsOwnerDirty ||
			IsMimeContentTypeDirty ||
			IsFileNameDirty;
	}

	std::string field_StringValue;
	bool IsStringValueNull;
	bool IsStringValueDirty;

	float field_DoubleValue;
	bool IsDoubleValueNull;
	bool IsDoubleValueDirty;

	int field_IntValue;
	bool IsIntValueNull;
	bool IsIntValueDirty;

	std::string field_DataElementName;
	bool IsDataElementNameNull;
	bool IsDataElementNameDirty;

	int field_DataType;
	bool IsDataTypeNull;
	bool IsDataTypeDirty;

	std::string field_TaskUser;
	bool IsTaskUserNull;
	bool IsTaskUserDirty;

	std::string field_DataElementValueList;
	bool IsDataElementValueListNull;
	bool IsDataElementValueListDirty;

	std::string field_FromDocDataElementValueList;
	bool IsFromDocDataElementValueListNull;
	bool IsFromDocDataElementValueListDirty;

	std::string field_RouteUser;
	bool IsRouteUserNull;
	bool IsRouteUserDirty;

	int field_MsgPriority;
	bool IsMsgPriorityNull;
	bool IsMsgPriorityDirty;

	int field_InitialTask;
	bool IsInitialTaskNull;
	bool IsInitialTaskDirty;

	int field_UpdateFlag;
	bool IsUpdateFlagNull;
	bool IsUpdateFlagDirty;

	std::string field_ProcessDataElementValueList;
	bool IsProcessDataElementValueListNull;
	bool IsProcessDataElementValueListDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;

	int field_RouteInstanceId;
	bool IsRouteInstanceIdNull;
	bool IsRouteInstanceIdDirty;

	int field_SentTime;
	bool IsSentTimeNull;
	bool IsSentTimeDirty;

	int field_StatusTime;
	bool IsStatusTimeNull;
	bool IsStatusTimeDirty;

	int field_Status;
	bool IsStatusNull;
	bool IsStatusDirty;

	int field_EndTime;
	bool IsEndTimeNull;
	bool IsEndTimeDirty;

	int field_StartTime;
	bool IsStartTimeNull;
	bool IsStartTimeDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

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

	std::string field_SentAt;
	bool IsSentAtNull;
	bool IsSentAtDirty;

	std::string field_URL;
	bool IsURLNull;
	bool IsURLDirty;

	std::string field_MsgCC;
	bool IsMsgCCNull;
	bool IsMsgCCDirty;

	std::string field_MsgBCC;
	bool IsMsgBCCNull;
	bool IsMsgBCCDirty;

	std::string field_MhtmlUrl;
	bool IsMhtmlUrlNull;
	bool IsMhtmlUrlDirty;

	std::string field_MsgReplyTo;
	bool IsMsgReplyToNull;
	bool IsMsgReplyToDirty;

	std::string field_Owner;
	bool IsOwnerNull;
	bool IsOwnerDirty;

	std::string field_MimeContentType;
	bool IsMimeContentTypeNull;
	bool IsMimeContentTypeDirty;

	std::string field_FileName;
	bool IsFileNameNull;
	bool IsFileNameDirty;


private:
	bool IsStringValuePrimaryKey;
	bool IsDoubleValuePrimaryKey;
	bool IsIntValuePrimaryKey;
	bool IsDataElementNamePrimaryKey;
	bool IsDataTypePrimaryKey;
	bool IsTaskUserPrimaryKey;
	bool IsDataElementValueListPrimaryKey;
	bool IsFromDocDataElementValueListPrimaryKey;
	bool IsRouteUserPrimaryKey;
	bool IsMsgPriorityPrimaryKey;
	bool IsInitialTaskPrimaryKey;
	bool IsUpdateFlagPrimaryKey;
	bool IsProcessDataElementValueListPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;
	bool IsRouteInstanceIdPrimaryKey;
	bool IsSentTimePrimaryKey;
	bool IsStatusTimePrimaryKey;
	bool IsStatusPrimaryKey;
	bool IsEndTimePrimaryKey;
	bool IsStartTimePrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsMsgFromPrimaryKey;
	bool IsMsgToPrimaryKey;
	bool IsMsgSubjectPrimaryKey;
	bool IsMessageFileNamePrimaryKey;
	bool IsFormatPrimaryKey;
	bool IsSentAtPrimaryKey;
	bool IsURLPrimaryKey;
	bool IsMsgCCPrimaryKey;
	bool IsMsgBCCPrimaryKey;
	bool IsMhtmlUrlPrimaryKey;
	bool IsMsgReplyToPrimaryKey;
	bool IsOwnerPrimaryKey;
	bool IsMimeContentTypePrimaryKey;
	bool IsFileNamePrimaryKey;

};


class ArchiveCRIDBFinal : public konga::DbRecordSet {
public:
	ArchiveCRIDBFinal() {}

	static ArchiveCRIDBFinal* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ArchiveCRIDBFinal";
	}

	void DoFieldExchange(const ArchiveCRIDBFinalRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	int GetDataType() {
		if( m_row.IsDataTypeNull )
			throw konga::DbNullException("DataType");
		return m_row.field_DataType;
	}
	void SetDataType(int i) {
		m_row.field_DataType = i;
		m_row.IsDataTypeNull = false;
		m_row.IsDataTypeDirty = true;
	}
	bool IsDataTypeNull() const {
		return m_row.IsDataTypeNull;
	}
	void SetDataTypeToNull() {
		m_row.IsDataTypeNull = true;
		m_row.IsDataTypeDirty = true;
	}

	std::string GetTaskUser() {
		if( m_row.IsTaskUserNull )
			throw konga::DbNullException("TaskUser");
		return m_row.field_TaskUser;
	}
	void SetTaskUser(const std::string& st) {
		m_row.field_TaskUser = st;
		m_row.IsTaskUserNull = false;
		m_row.IsTaskUserDirty = true;
	}
	bool IsTaskUserNull() const {
		return m_row.IsTaskUserNull;
	}
	void SetTaskUserToNull() {
		m_row.field_TaskUser = "";
		m_row.IsTaskUserNull = true;
		m_row.IsTaskUserDirty = true;
	}

	std::string GetDataElementValueList() {
		if( m_row.IsDataElementValueListNull )
			throw konga::DbNullException("DataElementValueList");
		return m_row.field_DataElementValueList;
	}
	void SetDataElementValueList(const std::string& st) {
		m_row.field_DataElementValueList = st;
		m_row.IsDataElementValueListNull = false;
		m_row.IsDataElementValueListDirty = true;
	}
	bool IsDataElementValueListNull() const {
		return m_row.IsDataElementValueListNull;
	}
	void SetDataElementValueListToNull() {
		m_row.field_DataElementValueList = "";
		m_row.IsDataElementValueListNull = true;
		m_row.IsDataElementValueListDirty = true;
	}

	std::string GetFromDocDataElementValueList() {
		if( m_row.IsFromDocDataElementValueListNull )
			throw konga::DbNullException("FromDocDataElementValueList");
		return m_row.field_FromDocDataElementValueList;
	}
	void SetFromDocDataElementValueList(const std::string& st) {
		m_row.field_FromDocDataElementValueList = st;
		m_row.IsFromDocDataElementValueListNull = false;
		m_row.IsFromDocDataElementValueListDirty = true;
	}
	bool IsFromDocDataElementValueListNull() const {
		return m_row.IsFromDocDataElementValueListNull;
	}
	void SetFromDocDataElementValueListToNull() {
		m_row.field_FromDocDataElementValueList = "";
		m_row.IsFromDocDataElementValueListNull = true;
		m_row.IsFromDocDataElementValueListDirty = true;
	}

	std::string GetRouteUser() {
		if( m_row.IsRouteUserNull )
			throw konga::DbNullException("RouteUser");
		return m_row.field_RouteUser;
	}
	void SetRouteUser(const std::string& st) {
		m_row.field_RouteUser = st;
		m_row.IsRouteUserNull = false;
		m_row.IsRouteUserDirty = true;
	}
	bool IsRouteUserNull() const {
		return m_row.IsRouteUserNull;
	}
	void SetRouteUserToNull() {
		m_row.field_RouteUser = "";
		m_row.IsRouteUserNull = true;
		m_row.IsRouteUserDirty = true;
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

	int GetInitialTask() {
		if( m_row.IsInitialTaskNull )
			throw konga::DbNullException("InitialTask");
		return m_row.field_InitialTask;
	}
	void SetInitialTask(int i) {
		m_row.field_InitialTask = i;
		m_row.IsInitialTaskNull = false;
		m_row.IsInitialTaskDirty = true;
	}
	bool IsInitialTaskNull() const {
		return m_row.IsInitialTaskNull;
	}
	void SetInitialTaskToNull() {
		m_row.IsInitialTaskNull = true;
		m_row.IsInitialTaskDirty = true;
	}

	int GetUpdateFlag() {
		if( m_row.IsUpdateFlagNull )
			throw konga::DbNullException("UpdateFlag");
		return m_row.field_UpdateFlag;
	}
	void SetUpdateFlag(int i) {
		m_row.field_UpdateFlag = i;
		m_row.IsUpdateFlagNull = false;
		m_row.IsUpdateFlagDirty = true;
	}
	bool IsUpdateFlagNull() const {
		return m_row.IsUpdateFlagNull;
	}
	void SetUpdateFlagToNull() {
		m_row.IsUpdateFlagNull = true;
		m_row.IsUpdateFlagDirty = true;
	}

	std::string GetProcessDataElementValueList() {
		if( m_row.IsProcessDataElementValueListNull )
			throw konga::DbNullException("ProcessDataElementValueList");
		return m_row.field_ProcessDataElementValueList;
	}
	void SetProcessDataElementValueList(const std::string& st) {
		m_row.field_ProcessDataElementValueList = st;
		m_row.IsProcessDataElementValueListNull = false;
		m_row.IsProcessDataElementValueListDirty = true;
	}
	bool IsProcessDataElementValueListNull() const {
		return m_row.IsProcessDataElementValueListNull;
	}
	void SetProcessDataElementValueListToNull() {
		m_row.field_ProcessDataElementValueList = "";
		m_row.IsProcessDataElementValueListNull = true;
		m_row.IsProcessDataElementValueListDirty = true;
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

	int GetRouteInstanceId() {
		if( m_row.IsRouteInstanceIdNull )
			throw konga::DbNullException("RouteInstanceId");
		return m_row.field_RouteInstanceId;
	}
	void SetRouteInstanceId(int i) {
		m_row.field_RouteInstanceId = i;
		m_row.IsRouteInstanceIdNull = false;
		m_row.IsRouteInstanceIdDirty = true;
	}
	bool IsRouteInstanceIdNull() const {
		return m_row.IsRouteInstanceIdNull;
	}
	void SetRouteInstanceIdToNull() {
		m_row.IsRouteInstanceIdNull = true;
		m_row.IsRouteInstanceIdDirty = true;
	}

	int GetSentTime() {
		if( m_row.IsSentTimeNull )
			throw konga::DbNullException("SentTime");
		return m_row.field_SentTime;
	}
	void SetSentTime(int i) {
		m_row.field_SentTime = i;
		m_row.IsSentTimeNull = false;
		m_row.IsSentTimeDirty = true;
	}
	bool IsSentTimeNull() const {
		return m_row.IsSentTimeNull;
	}
	void SetSentTimeToNull() {
		m_row.IsSentTimeNull = true;
		m_row.IsSentTimeDirty = true;
	}

	int GetStatusTime() {
		if( m_row.IsStatusTimeNull )
			throw konga::DbNullException("StatusTime");
		return m_row.field_StatusTime;
	}
	void SetStatusTime(int i) {
		m_row.field_StatusTime = i;
		m_row.IsStatusTimeNull = false;
		m_row.IsStatusTimeDirty = true;
	}
	bool IsStatusTimeNull() const {
		return m_row.IsStatusTimeNull;
	}
	void SetStatusTimeToNull() {
		m_row.IsStatusTimeNull = true;
		m_row.IsStatusTimeDirty = true;
	}

	int GetStatus() {
		if( m_row.IsStatusNull )
			throw konga::DbNullException("Status");
		return m_row.field_Status;
	}
	void SetStatus(int i) {
		m_row.field_Status = i;
		m_row.IsStatusNull = false;
		m_row.IsStatusDirty = true;
	}
	bool IsStatusNull() const {
		return m_row.IsStatusNull;
	}
	void SetStatusToNull() {
		m_row.IsStatusNull = true;
		m_row.IsStatusDirty = true;
	}

	int GetEndTime() {
		if( m_row.IsEndTimeNull )
			throw konga::DbNullException("EndTime");
		return m_row.field_EndTime;
	}
	void SetEndTime(int i) {
		m_row.field_EndTime = i;
		m_row.IsEndTimeNull = false;
		m_row.IsEndTimeDirty = true;
	}
	bool IsEndTimeNull() const {
		return m_row.IsEndTimeNull;
	}
	void SetEndTimeToNull() {
		m_row.IsEndTimeNull = true;
		m_row.IsEndTimeDirty = true;
	}

	int GetStartTime() {
		if( m_row.IsStartTimeNull )
			throw konga::DbNullException("StartTime");
		return m_row.field_StartTime;
	}
	void SetStartTime(int i) {
		m_row.field_StartTime = i;
		m_row.IsStartTimeNull = false;
		m_row.IsStartTimeDirty = true;
	}
	bool IsStartTimeNull() const {
		return m_row.IsStartTimeNull;
	}
	void SetStartTimeToNull() {
		m_row.IsStartTimeNull = true;
		m_row.IsStartTimeDirty = true;
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

	std::string GetSentAt() {
		if( m_row.IsSentAtNull )
			throw konga::DbNullException("SentAt");
		return m_row.field_SentAt;
	}
	void SetSentAt(const std::string& st) {
		m_row.field_SentAt = st;
		m_row.IsSentAtNull = false;
		m_row.IsSentAtDirty = true;
	}
	bool IsSentAtNull() const {
		return m_row.IsSentAtNull;
	}
	void SetSentAtToNull() {
		m_row.field_SentAt = "";
		m_row.IsSentAtNull = true;
		m_row.IsSentAtDirty = true;
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

protected:
	ArchiveCRIDBFinalRow m_row;

private:
	bool IsStringValueDirty() const {
		return m_row.IsStringValueDirty;
	};
	bool IsDoubleValueDirty() const {
		return m_row.IsDoubleValueDirty;
	};
	bool IsIntValueDirty() const {
		return m_row.IsIntValueDirty;
	};
	bool IsDataElementNameDirty() const {
		return m_row.IsDataElementNameDirty;
	};
	bool IsDataTypeDirty() const {
		return m_row.IsDataTypeDirty;
	};
	bool IsTaskUserDirty() const {
		return m_row.IsTaskUserDirty;
	};
	bool IsDataElementValueListDirty() const {
		return m_row.IsDataElementValueListDirty;
	};
	bool IsFromDocDataElementValueListDirty() const {
		return m_row.IsFromDocDataElementValueListDirty;
	};
	bool IsRouteUserDirty() const {
		return m_row.IsRouteUserDirty;
	};
	bool IsMsgPriorityDirty() const {
		return m_row.IsMsgPriorityDirty;
	};
	bool IsInitialTaskDirty() const {
		return m_row.IsInitialTaskDirty;
	};
	bool IsUpdateFlagDirty() const {
		return m_row.IsUpdateFlagDirty;
	};
	bool IsProcessDataElementValueListDirty() const {
		return m_row.IsProcessDataElementValueListDirty;
	};
	bool IsInsertTimeDirty() const {
		return m_row.IsInsertTimeDirty;
	};
	bool IsModTimeDirty() const {
		return m_row.IsModTimeDirty;
	};
	bool IsRouteInstanceIdDirty() const {
		return m_row.IsRouteInstanceIdDirty;
	};
	bool IsSentTimeDirty() const {
		return m_row.IsSentTimeDirty;
	};
	bool IsStatusTimeDirty() const {
		return m_row.IsStatusTimeDirty;
	};
	bool IsStatusDirty() const {
		return m_row.IsStatusDirty;
	};
	bool IsEndTimeDirty() const {
		return m_row.IsEndTimeDirty;
	};
	bool IsStartTimeDirty() const {
		return m_row.IsStartTimeDirty;
	};
	bool IsProcessInstanceIdDirty() const {
		return m_row.IsProcessInstanceIdDirty;
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
	bool IsSentAtDirty() const {
		return m_row.IsSentAtDirty;
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
	bool IsMhtmlUrlDirty() const {
		return m_row.IsMhtmlUrlDirty;
	};
	bool IsMsgReplyToDirty() const {
		return m_row.IsMsgReplyToDirty;
	};
	bool IsOwnerDirty() const {
		return m_row.IsOwnerDirty;
	};
	bool IsMimeContentTypeDirty() const {
		return m_row.IsMimeContentTypeDirty;
	};
	bool IsFileNameDirty() const {
		return m_row.IsFileNameDirty;
	};


	std::string m_stUpdateFilter;

};

}                      


#endif
