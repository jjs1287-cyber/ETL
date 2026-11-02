/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_TRANSFERTAB_H
#define KONGA_DB_CRPDB_TRANSFERTAB_H

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

class TransferTabRow
{
public:
	TransferTabRow() : 
			field_TransferId(0),
			IsTransferIdNull(true),
			IsTransferIdPrimaryKey(true),
			IsTransferIdDirty(false),
			IsProtocolNull(true),
			IsProtocolPrimaryKey(false),
			IsProtocolDirty(false),
			IsRemoteServerNameNull(true),
			IsRemoteServerNamePrimaryKey(false),
			IsRemoteServerNameDirty(false),
			field_RemotePort(0),
			IsRemotePortNull(true),
			IsRemotePortPrimaryKey(false),
			IsRemotePortDirty(false),
			IsUserNameNull(true),
			IsUserNamePrimaryKey(false),
			IsUserNameDirty(false),
			IsPasswordNull(true),
			IsPasswordPrimaryKey(false),
			IsPasswordDirty(false),
			IsLocalPathNull(true),
			IsLocalPathPrimaryKey(false),
			IsLocalPathDirty(false),
			IsRemotePathNull(true),
			IsRemotePathPrimaryKey(false),
			IsRemotePathDirty(false),
			IsProxyServerNameNull(true),
			IsProxyServerNamePrimaryKey(false),
			IsProxyServerNameDirty(false),
			field_TimeOutSeconds(0),
			IsTimeOutSecondsNull(true),
			IsTimeOutSecondsPrimaryKey(false),
			IsTimeOutSecondsDirty(false),
			IsLeaveOnServerNull(true),
			IsLeaveOnServerPrimaryKey(false),
			IsLeaveOnServerDirty(false),
			IsDownloadConditionNull(true),
			IsDownloadConditionPrimaryKey(false),
			IsDownloadConditionDirty(false),
			field_CryptoInfoId(0),
			IsCryptoInfoIdNull(true),
			IsCryptoInfoIdPrimaryKey(false),
			IsCryptoInfoIdDirty(false),
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

	void Copy(const TransferTabRow& r)
	{
		field_TransferId = r.field_TransferId;
		IsTransferIdNull = r.IsTransferIdNull;
		IsTransferIdDirty = r.IsTransferIdDirty;

		field_Protocol = r.field_Protocol;
		IsProtocolNull = r.IsProtocolNull;
		IsProtocolDirty = r.IsProtocolDirty;

		field_RemoteServerName = r.field_RemoteServerName;
		IsRemoteServerNameNull = r.IsRemoteServerNameNull;
		IsRemoteServerNameDirty = r.IsRemoteServerNameDirty;

		field_RemotePort = r.field_RemotePort;
		IsRemotePortNull = r.IsRemotePortNull;
		IsRemotePortDirty = r.IsRemotePortDirty;

		field_UserName = r.field_UserName;
		IsUserNameNull = r.IsUserNameNull;
		IsUserNameDirty = r.IsUserNameDirty;

		field_Password = r.field_Password;
		IsPasswordNull = r.IsPasswordNull;
		IsPasswordDirty = r.IsPasswordDirty;

		field_LocalPath = r.field_LocalPath;
		IsLocalPathNull = r.IsLocalPathNull;
		IsLocalPathDirty = r.IsLocalPathDirty;

		field_RemotePath = r.field_RemotePath;
		IsRemotePathNull = r.IsRemotePathNull;
		IsRemotePathDirty = r.IsRemotePathDirty;

		field_ProxyServerName = r.field_ProxyServerName;
		IsProxyServerNameNull = r.IsProxyServerNameNull;
		IsProxyServerNameDirty = r.IsProxyServerNameDirty;

		field_TimeOutSeconds = r.field_TimeOutSeconds;
		IsTimeOutSecondsNull = r.IsTimeOutSecondsNull;
		IsTimeOutSecondsDirty = r.IsTimeOutSecondsDirty;

		field_LeaveOnServer = r.field_LeaveOnServer;
		IsLeaveOnServerNull = r.IsLeaveOnServerNull;
		IsLeaveOnServerDirty = r.IsLeaveOnServerDirty;

		field_DownloadCondition = r.field_DownloadCondition;
		IsDownloadConditionNull = r.IsDownloadConditionNull;
		IsDownloadConditionDirty = r.IsDownloadConditionDirty;

		field_CryptoInfoId = r.field_CryptoInfoId;
		IsCryptoInfoIdNull = r.IsCryptoInfoIdNull;
		IsCryptoInfoIdDirty = r.IsCryptoInfoIdDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const TransferTabRow& r)
	{
		field_TransferId = r.field_TransferId;
		IsTransferIdNull = r.IsTransferIdNull;
		IsTransferIdDirty = r.IsTransferIdDirty;

	}

	void SetDirty(bool b)
	{
		IsTransferIdDirty = b;
		IsProtocolDirty = b;
		IsRemoteServerNameDirty = b;
		IsRemotePortDirty = b;
		IsUserNameDirty = b;
		IsPasswordDirty = b;
		IsLocalPathDirty = b;
		IsRemotePathDirty = b;
		IsProxyServerNameDirty = b;
		IsTimeOutSecondsDirty = b;
		IsLeaveOnServerDirty = b;
		IsDownloadConditionDirty = b;
		IsCryptoInfoIdDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsTransferIdDirty ||
			IsProtocolDirty ||
			IsRemoteServerNameDirty ||
			IsRemotePortDirty ||
			IsUserNameDirty ||
			IsPasswordDirty ||
			IsLocalPathDirty ||
			IsRemotePathDirty ||
			IsProxyServerNameDirty ||
			IsTimeOutSecondsDirty ||
			IsLeaveOnServerDirty ||
			IsDownloadConditionDirty ||
			IsCryptoInfoIdDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_TransferId;
	bool IsTransferIdNull;
	bool IsTransferIdDirty;

	std::string field_Protocol;
	bool IsProtocolNull;
	bool IsProtocolDirty;

	std::string field_RemoteServerName;
	bool IsRemoteServerNameNull;
	bool IsRemoteServerNameDirty;

	int field_RemotePort;
	bool IsRemotePortNull;
	bool IsRemotePortDirty;

	std::string field_UserName;
	bool IsUserNameNull;
	bool IsUserNameDirty;

	std::string field_Password;
	bool IsPasswordNull;
	bool IsPasswordDirty;

	std::string field_LocalPath;
	bool IsLocalPathNull;
	bool IsLocalPathDirty;

	std::string field_RemotePath;
	bool IsRemotePathNull;
	bool IsRemotePathDirty;

	std::string field_ProxyServerName;
	bool IsProxyServerNameNull;
	bool IsProxyServerNameDirty;

	int field_TimeOutSeconds;
	bool IsTimeOutSecondsNull;
	bool IsTimeOutSecondsDirty;

	std::string field_LeaveOnServer;
	bool IsLeaveOnServerNull;
	bool IsLeaveOnServerDirty;

	std::string field_DownloadCondition;
	bool IsDownloadConditionNull;
	bool IsDownloadConditionDirty;

	int field_CryptoInfoId;
	bool IsCryptoInfoIdNull;
	bool IsCryptoInfoIdDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsTransferIdPrimaryKey;
	bool IsProtocolPrimaryKey;
	bool IsRemoteServerNamePrimaryKey;
	bool IsRemotePortPrimaryKey;
	bool IsUserNamePrimaryKey;
	bool IsPasswordPrimaryKey;
	bool IsLocalPathPrimaryKey;
	bool IsRemotePathPrimaryKey;
	bool IsProxyServerNamePrimaryKey;
	bool IsTimeOutSecondsPrimaryKey;
	bool IsLeaveOnServerPrimaryKey;
	bool IsDownloadConditionPrimaryKey;
	bool IsCryptoInfoIdPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class TransferTab : public konga::DbRecordSet {
public:
	TransferTab() {}

	static TransferTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TransferTab";
	}

	void DoFieldExchange(const TransferTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	std::string GetProtocol() {
		if( m_row.IsProtocolNull )
			throw konga::DbNullException("Protocol");
		return m_row.field_Protocol;
	}
	void SetProtocol(const std::string& st) {
		m_row.field_Protocol = st;
		m_row.IsProtocolNull = false;
		m_row.IsProtocolDirty = true;
	}
	bool IsProtocolNull() const {
		return m_row.IsProtocolNull;
	}
	void SetProtocolToNull() {
		m_row.field_Protocol = "";
		m_row.IsProtocolNull = true;
		m_row.IsProtocolDirty = true;
	}

	std::string GetRemoteServerName() {
		if( m_row.IsRemoteServerNameNull )
			throw konga::DbNullException("RemoteServerName");
		return m_row.field_RemoteServerName;
	}
	void SetRemoteServerName(const std::string& st) {
		m_row.field_RemoteServerName = st;
		m_row.IsRemoteServerNameNull = false;
		m_row.IsRemoteServerNameDirty = true;
	}
	bool IsRemoteServerNameNull() const {
		return m_row.IsRemoteServerNameNull;
	}
	void SetRemoteServerNameToNull() {
		m_row.field_RemoteServerName = "";
		m_row.IsRemoteServerNameNull = true;
		m_row.IsRemoteServerNameDirty = true;
	}

	int GetRemotePort() {
		if( m_row.IsRemotePortNull )
			throw konga::DbNullException("RemotePort");
		return m_row.field_RemotePort;
	}
	void SetRemotePort(int i) {
		m_row.field_RemotePort = i;
		m_row.IsRemotePortNull = false;
		m_row.IsRemotePortDirty = true;
	}
	bool IsRemotePortNull() const {
		return m_row.IsRemotePortNull;
	}
	void SetRemotePortToNull() {
		m_row.IsRemotePortNull = true;
		m_row.IsRemotePortDirty = true;
	}

	std::string GetUserName() {
		if( m_row.IsUserNameNull )
			throw konga::DbNullException("UserName");
		return m_row.field_UserName;
	}
	void SetUserName(const std::string& st) {
		m_row.field_UserName = st;
		m_row.IsUserNameNull = false;
		m_row.IsUserNameDirty = true;
	}
	bool IsUserNameNull() const {
		return m_row.IsUserNameNull;
	}
	void SetUserNameToNull() {
		m_row.field_UserName = "";
		m_row.IsUserNameNull = true;
		m_row.IsUserNameDirty = true;
	}

	std::string GetPassword() {
		if( m_row.IsPasswordNull )
			throw konga::DbNullException("Password");
		return m_row.field_Password;
	}
	void SetPassword(const std::string& st) {
		m_row.field_Password = st;
		m_row.IsPasswordNull = false;
		m_row.IsPasswordDirty = true;
	}
	bool IsPasswordNull() const {
		return m_row.IsPasswordNull;
	}
	void SetPasswordToNull() {
		m_row.field_Password = "";
		m_row.IsPasswordNull = true;
		m_row.IsPasswordDirty = true;
	}

	std::string GetLocalPath() {
		if( m_row.IsLocalPathNull )
			throw konga::DbNullException("LocalPath");
		return m_row.field_LocalPath;
	}
	void SetLocalPath(const std::string& st) {
		m_row.field_LocalPath = st;
		m_row.IsLocalPathNull = false;
		m_row.IsLocalPathDirty = true;
	}
	bool IsLocalPathNull() const {
		return m_row.IsLocalPathNull;
	}
	void SetLocalPathToNull() {
		m_row.field_LocalPath = "";
		m_row.IsLocalPathNull = true;
		m_row.IsLocalPathDirty = true;
	}

	std::string GetRemotePath() {
		if( m_row.IsRemotePathNull )
			throw konga::DbNullException("RemotePath");
		return m_row.field_RemotePath;
	}
	void SetRemotePath(const std::string& st) {
		m_row.field_RemotePath = st;
		m_row.IsRemotePathNull = false;
		m_row.IsRemotePathDirty = true;
	}
	bool IsRemotePathNull() const {
		return m_row.IsRemotePathNull;
	}
	void SetRemotePathToNull() {
		m_row.field_RemotePath = "";
		m_row.IsRemotePathNull = true;
		m_row.IsRemotePathDirty = true;
	}

	std::string GetProxyServerName() {
		if( m_row.IsProxyServerNameNull )
			throw konga::DbNullException("ProxyServerName");
		return m_row.field_ProxyServerName;
	}
	void SetProxyServerName(const std::string& st) {
		m_row.field_ProxyServerName = st;
		m_row.IsProxyServerNameNull = false;
		m_row.IsProxyServerNameDirty = true;
	}
	bool IsProxyServerNameNull() const {
		return m_row.IsProxyServerNameNull;
	}
	void SetProxyServerNameToNull() {
		m_row.field_ProxyServerName = "";
		m_row.IsProxyServerNameNull = true;
		m_row.IsProxyServerNameDirty = true;
	}

	int GetTimeOutSeconds() {
		if( m_row.IsTimeOutSecondsNull )
			throw konga::DbNullException("TimeOutSeconds");
		return m_row.field_TimeOutSeconds;
	}
	void SetTimeOutSeconds(int i) {
		m_row.field_TimeOutSeconds = i;
		m_row.IsTimeOutSecondsNull = false;
		m_row.IsTimeOutSecondsDirty = true;
	}
	bool IsTimeOutSecondsNull() const {
		return m_row.IsTimeOutSecondsNull;
	}
	void SetTimeOutSecondsToNull() {
		m_row.IsTimeOutSecondsNull = true;
		m_row.IsTimeOutSecondsDirty = true;
	}

	std::string GetLeaveOnServer() {
		if( m_row.IsLeaveOnServerNull )
			throw konga::DbNullException("LeaveOnServer");
		return m_row.field_LeaveOnServer;
	}
	void SetLeaveOnServer(const std::string& st) {
		m_row.field_LeaveOnServer = st;
		m_row.IsLeaveOnServerNull = false;
		m_row.IsLeaveOnServerDirty = true;
	}
	bool IsLeaveOnServerNull() const {
		return m_row.IsLeaveOnServerNull;
	}
	void SetLeaveOnServerToNull() {
		m_row.field_LeaveOnServer = "";
		m_row.IsLeaveOnServerNull = true;
		m_row.IsLeaveOnServerDirty = true;
	}

	std::string GetDownloadCondition() {
		if( m_row.IsDownloadConditionNull )
			throw konga::DbNullException("DownloadCondition");
		return m_row.field_DownloadCondition;
	}
	void SetDownloadCondition(const std::string& st) {
		m_row.field_DownloadCondition = st;
		m_row.IsDownloadConditionNull = false;
		m_row.IsDownloadConditionDirty = true;
	}
	bool IsDownloadConditionNull() const {
		return m_row.IsDownloadConditionNull;
	}
	void SetDownloadConditionToNull() {
		m_row.field_DownloadCondition = "";
		m_row.IsDownloadConditionNull = true;
		m_row.IsDownloadConditionDirty = true;
	}

	int GetCryptoInfoId() {
		if( m_row.IsCryptoInfoIdNull )
			throw konga::DbNullException("CryptoInfoId");
		return m_row.field_CryptoInfoId;
	}
	void SetCryptoInfoId(int i) {
		m_row.field_CryptoInfoId = i;
		m_row.IsCryptoInfoIdNull = false;
		m_row.IsCryptoInfoIdDirty = true;
	}
	bool IsCryptoInfoIdNull() const {
		return m_row.IsCryptoInfoIdNull;
	}
	void SetCryptoInfoIdToNull() {
		m_row.IsCryptoInfoIdNull = true;
		m_row.IsCryptoInfoIdDirty = true;
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
	TransferTabRow m_row;

private:
	bool IsTransferIdDirty() const {
		return m_row.IsTransferIdDirty;
	};
	bool IsProtocolDirty() const {
		return m_row.IsProtocolDirty;
	};
	bool IsRemoteServerNameDirty() const {
		return m_row.IsRemoteServerNameDirty;
	};
	bool IsRemotePortDirty() const {
		return m_row.IsRemotePortDirty;
	};
	bool IsUserNameDirty() const {
		return m_row.IsUserNameDirty;
	};
	bool IsPasswordDirty() const {
		return m_row.IsPasswordDirty;
	};
	bool IsLocalPathDirty() const {
		return m_row.IsLocalPathDirty;
	};
	bool IsRemotePathDirty() const {
		return m_row.IsRemotePathDirty;
	};
	bool IsProxyServerNameDirty() const {
		return m_row.IsProxyServerNameDirty;
	};
	bool IsTimeOutSecondsDirty() const {
		return m_row.IsTimeOutSecondsDirty;
	};
	bool IsLeaveOnServerDirty() const {
		return m_row.IsLeaveOnServerDirty;
	};
	bool IsDownloadConditionDirty() const {
		return m_row.IsDownloadConditionDirty;
	};
	bool IsCryptoInfoIdDirty() const {
		return m_row.IsCryptoInfoIdDirty;
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
