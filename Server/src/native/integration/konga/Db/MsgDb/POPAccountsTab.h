/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_POPACCOUNTSTAB_H
#define KONGA_DB_CRMSGDB_POPACCOUNTSTAB_H

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

class POPAccountsTabRow
{
public:
	POPAccountsTabRow() : 
			IsPOPUserNull(true),
			IsPOPUserPrimaryKey(true),
			IsPOPUserDirty(false),
			IsPOPPasswordNull(true),
			IsPOPPasswordPrimaryKey(false),
			IsPOPPasswordDirty(false),
			IsPOPServerNull(true),
			IsPOPServerPrimaryKey(false),
			IsPOPServerDirty(false),
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

	void Copy(const POPAccountsTabRow& r)
	{
		field_POPUser = r.field_POPUser;
		IsPOPUserNull = r.IsPOPUserNull;
		IsPOPUserDirty = r.IsPOPUserDirty;

		field_POPPassword = r.field_POPPassword;
		IsPOPPasswordNull = r.IsPOPPasswordNull;
		IsPOPPasswordDirty = r.IsPOPPasswordDirty;

		field_POPServer = r.field_POPServer;
		IsPOPServerNull = r.IsPOPServerNull;
		IsPOPServerDirty = r.IsPOPServerDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const POPAccountsTabRow& r)
	{
		field_POPUser = r.field_POPUser;
		IsPOPUserNull = r.IsPOPUserNull;
		IsPOPUserDirty = r.IsPOPUserDirty;

	}

	void SetDirty(bool b)
	{
		IsPOPUserDirty = b;
		IsPOPPasswordDirty = b;
		IsPOPServerDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsPOPUserDirty ||
			IsPOPPasswordDirty ||
			IsPOPServerDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	std::string field_POPUser;
	bool IsPOPUserNull;
	bool IsPOPUserDirty;

	std::string field_POPPassword;
	bool IsPOPPasswordNull;
	bool IsPOPPasswordDirty;

	std::string field_POPServer;
	bool IsPOPServerNull;
	bool IsPOPServerDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsPOPUserPrimaryKey;
	bool IsPOPPasswordPrimaryKey;
	bool IsPOPServerPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class POPAccountsTab : public konga::DbRecordSet {
public:
	POPAccountsTab() {}

	static POPAccountsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "POPAccountsTab";
	}

	void DoFieldExchange(const POPAccountsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	std::string GetPOPUser() {
		if( m_row.IsPOPUserNull )
			throw konga::DbNullException("POPUser");
		return m_row.field_POPUser;
	}
	void SetPOPUser(const std::string& st) {
		m_row.field_POPUser = st;
		m_row.IsPOPUserNull = false;
		m_row.IsPOPUserDirty = true;
	}
	bool IsPOPUserNull() const {
		return m_row.IsPOPUserNull;
	}
	void SetPOPUserToNull() {
		m_row.field_POPUser = "";
		m_row.IsPOPUserNull = true;
		m_row.IsPOPUserDirty = true;
	}

	std::string GetPOPPassword() {
		if( m_row.IsPOPPasswordNull )
			throw konga::DbNullException("POPPassword");
		return m_row.field_POPPassword;
	}
	void SetPOPPassword(const std::string& st) {
		m_row.field_POPPassword = st;
		m_row.IsPOPPasswordNull = false;
		m_row.IsPOPPasswordDirty = true;
	}
	bool IsPOPPasswordNull() const {
		return m_row.IsPOPPasswordNull;
	}
	void SetPOPPasswordToNull() {
		m_row.field_POPPassword = "";
		m_row.IsPOPPasswordNull = true;
		m_row.IsPOPPasswordDirty = true;
	}

	std::string GetPOPServer() {
		if( m_row.IsPOPServerNull )
			throw konga::DbNullException("POPServer");
		return m_row.field_POPServer;
	}
	void SetPOPServer(const std::string& st) {
		m_row.field_POPServer = st;
		m_row.IsPOPServerNull = false;
		m_row.IsPOPServerDirty = true;
	}
	bool IsPOPServerNull() const {
		return m_row.IsPOPServerNull;
	}
	void SetPOPServerToNull() {
		m_row.field_POPServer = "";
		m_row.IsPOPServerNull = true;
		m_row.IsPOPServerDirty = true;
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
	POPAccountsTabRow m_row;

private:
	bool IsPOPUserDirty() const {
		return m_row.IsPOPUserDirty;
	};
	bool IsPOPPasswordDirty() const {
		return m_row.IsPOPPasswordDirty;
	};
	bool IsPOPServerDirty() const {
		return m_row.IsPOPServerDirty;
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
