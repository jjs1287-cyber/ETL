/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_SETTINGSTAB_H
#define KONGA_DB_CRMSGDB_SETTINGSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace MsgDb
{

class SettingsTabRow
{
public:
	SettingsTabRow() : 
			field_SettingsId(0),
			IsSettingsIdNull(true),
			IsSettingsIdPrimaryKey(true),
			IsSettingsIdDirty(false),
			IsRootNull(true),
			IsRootPrimaryKey(false),
			IsRootDirty(false),
			IsIncomingRootNull(true),
			IsIncomingRootPrimaryKey(false),
			IsIncomingRootDirty(false),
			IsSMTPServerNull(true),
			IsSMTPServerPrimaryKey(false),
			IsSMTPServerDirty(false),
			IsGarbageDirNull(true),
			IsGarbageDirPrimaryKey(false),
			IsGarbageDirDirty(false),
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

	void Copy(const SettingsTabRow& r)
	{
		field_SettingsId = r.field_SettingsId;
		IsSettingsIdNull = r.IsSettingsIdNull;
		IsSettingsIdDirty = r.IsSettingsIdDirty;

		field_Root = r.field_Root;
		IsRootNull = r.IsRootNull;
		IsRootDirty = r.IsRootDirty;

		field_IncomingRoot = r.field_IncomingRoot;
		IsIncomingRootNull = r.IsIncomingRootNull;
		IsIncomingRootDirty = r.IsIncomingRootDirty;

		field_SMTPServer = r.field_SMTPServer;
		IsSMTPServerNull = r.IsSMTPServerNull;
		IsSMTPServerDirty = r.IsSMTPServerDirty;

		field_GarbageDir = r.field_GarbageDir;
		IsGarbageDirNull = r.IsGarbageDirNull;
		IsGarbageDirDirty = r.IsGarbageDirDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const SettingsTabRow& r)
	{
		field_SettingsId = r.field_SettingsId;
		IsSettingsIdNull = r.IsSettingsIdNull;
		IsSettingsIdDirty = r.IsSettingsIdDirty;

	}

	void SetDirty(bool b)
	{
		IsSettingsIdDirty = b;
		IsRootDirty = b;
		IsIncomingRootDirty = b;
		IsSMTPServerDirty = b;
		IsGarbageDirDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsSettingsIdDirty ||
			IsRootDirty ||
			IsIncomingRootDirty ||
			IsSMTPServerDirty ||
			IsGarbageDirDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_SettingsId;
	bool IsSettingsIdNull;
	bool IsSettingsIdDirty;

	std::string field_Root;
	bool IsRootNull;
	bool IsRootDirty;

	std::string field_IncomingRoot;
	bool IsIncomingRootNull;
	bool IsIncomingRootDirty;

	std::string field_SMTPServer;
	bool IsSMTPServerNull;
	bool IsSMTPServerDirty;

	std::string field_GarbageDir;
	bool IsGarbageDirNull;
	bool IsGarbageDirDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsSettingsIdPrimaryKey;
	bool IsRootPrimaryKey;
	bool IsIncomingRootPrimaryKey;
	bool IsSMTPServerPrimaryKey;
	bool IsGarbageDirPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class SettingsTab : public konga::DbRecordSet {
public:
	SettingsTab() {}

	static SettingsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "SettingsTab";
	}

	void DoFieldExchange(const SettingsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetSettingsId() {
		if( m_row.IsSettingsIdNull )
			throw konga::DbNullException("SettingsId");
		return m_row.field_SettingsId;
	}
	void SetSettingsId(int i) {
		m_row.field_SettingsId = i;
		m_row.IsSettingsIdNull = false;
		m_row.IsSettingsIdDirty = true;
	}
	bool IsSettingsIdNull() const {
		return m_row.IsSettingsIdNull;
	}
	void SetSettingsIdToNull() {
		m_row.IsSettingsIdNull = true;
		m_row.IsSettingsIdDirty = true;
	}

	std::string GetRoot() {
		if( m_row.IsRootNull )
			throw konga::DbNullException("Root");
		return m_row.field_Root;
	}
	void SetRoot(const std::string& st) {
		m_row.field_Root = st;
		m_row.IsRootNull = false;
		m_row.IsRootDirty = true;
	}
	bool IsRootNull() const {
		return m_row.IsRootNull;
	}
	void SetRootToNull() {
		m_row.field_Root = "";
		m_row.IsRootNull = true;
		m_row.IsRootDirty = true;
	}

	std::string GetIncomingRoot() {
		if( m_row.IsIncomingRootNull )
			throw konga::DbNullException("IncomingRoot");
		return m_row.field_IncomingRoot;
	}
	void SetIncomingRoot(const std::string& st) {
		m_row.field_IncomingRoot = st;
		m_row.IsIncomingRootNull = false;
		m_row.IsIncomingRootDirty = true;
	}
	bool IsIncomingRootNull() const {
		return m_row.IsIncomingRootNull;
	}
	void SetIncomingRootToNull() {
		m_row.field_IncomingRoot = "";
		m_row.IsIncomingRootNull = true;
		m_row.IsIncomingRootDirty = true;
	}

	std::string GetSMTPServer() {
		if( m_row.IsSMTPServerNull )
			throw konga::DbNullException("SMTPServer");
		return m_row.field_SMTPServer;
	}
	void SetSMTPServer(const std::string& st) {
		m_row.field_SMTPServer = st;
		m_row.IsSMTPServerNull = false;
		m_row.IsSMTPServerDirty = true;
	}
	bool IsSMTPServerNull() const {
		return m_row.IsSMTPServerNull;
	}
	void SetSMTPServerToNull() {
		m_row.field_SMTPServer = "";
		m_row.IsSMTPServerNull = true;
		m_row.IsSMTPServerDirty = true;
	}

	std::string GetGarbageDir() {
		if( m_row.IsGarbageDirNull )
			throw konga::DbNullException("GarbageDir");
		return m_row.field_GarbageDir;
	}
	void SetGarbageDir(const std::string& st) {
		m_row.field_GarbageDir = st;
		m_row.IsGarbageDirNull = false;
		m_row.IsGarbageDirDirty = true;
	}
	bool IsGarbageDirNull() const {
		return m_row.IsGarbageDirNull;
	}
	void SetGarbageDirToNull() {
		m_row.field_GarbageDir = "";
		m_row.IsGarbageDirNull = true;
		m_row.IsGarbageDirDirty = true;
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
	SettingsTabRow m_row;

private:
	bool IsSettingsIdDirty() const {
		return m_row.IsSettingsIdDirty;
	};
	bool IsRootDirty() const {
		return m_row.IsRootDirty;
	};
	bool IsIncomingRootDirty() const {
		return m_row.IsIncomingRootDirty;
	};
	bool IsSMTPServerDirty() const {
		return m_row.IsSMTPServerDirty;
	};
	bool IsGarbageDirDirty() const {
		return m_row.IsGarbageDirDirty;
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
