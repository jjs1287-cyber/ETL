/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_USERSTAB_H
#define KONGA_DB_CRTRANDB_USERSTAB_H

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

class UsersTabRow
{
public:
	UsersTabRow() : 
			field_User_ID(0),
			IsUser_IDNull(true),
			IsUser_IDPrimaryKey(true),
			IsUser_IDDirty(false),
			IsFirst_NameNull(true),
			IsFirst_NamePrimaryKey(false),
			IsFirst_NameDirty(false),
			IsLast_NameNull(true),
			IsLast_NamePrimaryKey(false),
			IsLast_NameDirty(false),
			IsLoginNull(true),
			IsLoginPrimaryKey(false),
			IsLoginDirty(false),
			IsPasswordNull(true),
			IsPasswordPrimaryKey(false),
			IsPasswordDirty(false),
			IsEmailNull(true),
			IsEmailPrimaryKey(false),
			IsEmailDirty(false),
			field_Access_Level(0),
			IsAccess_LevelNull(true),
			IsAccess_LevelPrimaryKey(false),
			IsAccess_LevelDirty(false),
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

	void Copy(const UsersTabRow& r)
	{
		field_User_ID = r.field_User_ID;
		IsUser_IDNull = r.IsUser_IDNull;
		IsUser_IDDirty = r.IsUser_IDDirty;

		field_First_Name = r.field_First_Name;
		IsFirst_NameNull = r.IsFirst_NameNull;
		IsFirst_NameDirty = r.IsFirst_NameDirty;

		field_Last_Name = r.field_Last_Name;
		IsLast_NameNull = r.IsLast_NameNull;
		IsLast_NameDirty = r.IsLast_NameDirty;

		field_Login = r.field_Login;
		IsLoginNull = r.IsLoginNull;
		IsLoginDirty = r.IsLoginDirty;

		field_Password = r.field_Password;
		IsPasswordNull = r.IsPasswordNull;
		IsPasswordDirty = r.IsPasswordDirty;

		field_Email = r.field_Email;
		IsEmailNull = r.IsEmailNull;
		IsEmailDirty = r.IsEmailDirty;

		field_Access_Level = r.field_Access_Level;
		IsAccess_LevelNull = r.IsAccess_LevelNull;
		IsAccess_LevelDirty = r.IsAccess_LevelDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const UsersTabRow& r)
	{
		field_User_ID = r.field_User_ID;
		IsUser_IDNull = r.IsUser_IDNull;
		IsUser_IDDirty = r.IsUser_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsUser_IDDirty = b;
		IsFirst_NameDirty = b;
		IsLast_NameDirty = b;
		IsLoginDirty = b;
		IsPasswordDirty = b;
		IsEmailDirty = b;
		IsAccess_LevelDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsUser_IDDirty ||
			IsFirst_NameDirty ||
			IsLast_NameDirty ||
			IsLoginDirty ||
			IsPasswordDirty ||
			IsEmailDirty ||
			IsAccess_LevelDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_User_ID;
	bool IsUser_IDNull;
	bool IsUser_IDDirty;

	std::string field_First_Name;
	bool IsFirst_NameNull;
	bool IsFirst_NameDirty;

	std::string field_Last_Name;
	bool IsLast_NameNull;
	bool IsLast_NameDirty;

	std::string field_Login;
	bool IsLoginNull;
	bool IsLoginDirty;

	std::string field_Password;
	bool IsPasswordNull;
	bool IsPasswordDirty;

	std::string field_Email;
	bool IsEmailNull;
	bool IsEmailDirty;

	int field_Access_Level;
	bool IsAccess_LevelNull;
	bool IsAccess_LevelDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsUser_IDPrimaryKey;
	bool IsFirst_NamePrimaryKey;
	bool IsLast_NamePrimaryKey;
	bool IsLoginPrimaryKey;
	bool IsPasswordPrimaryKey;
	bool IsEmailPrimaryKey;
	bool IsAccess_LevelPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class UsersTab : public konga::DbRecordSet {
public:
	UsersTab() {}

	static UsersTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "UsersTab";
	}

	void DoFieldExchange(const UsersTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	std::string GetFirst_Name() {
		if( m_row.IsFirst_NameNull )
			throw konga::DbNullException("First_Name");
		return m_row.field_First_Name;
	}
	void SetFirst_Name(const std::string& st) {
		m_row.field_First_Name = st;
		m_row.IsFirst_NameNull = false;
		m_row.IsFirst_NameDirty = true;
	}
	bool IsFirst_NameNull() const {
		return m_row.IsFirst_NameNull;
	}
	void SetFirst_NameToNull() {
		m_row.field_First_Name = "";
		m_row.IsFirst_NameNull = true;
		m_row.IsFirst_NameDirty = true;
	}

	std::string GetLast_Name() {
		if( m_row.IsLast_NameNull )
			throw konga::DbNullException("Last_Name");
		return m_row.field_Last_Name;
	}
	void SetLast_Name(const std::string& st) {
		m_row.field_Last_Name = st;
		m_row.IsLast_NameNull = false;
		m_row.IsLast_NameDirty = true;
	}
	bool IsLast_NameNull() const {
		return m_row.IsLast_NameNull;
	}
	void SetLast_NameToNull() {
		m_row.field_Last_Name = "";
		m_row.IsLast_NameNull = true;
		m_row.IsLast_NameDirty = true;
	}

	std::string GetLogin() {
		if( m_row.IsLoginNull )
			throw konga::DbNullException("Login");
		return m_row.field_Login;
	}
	void SetLogin(const std::string& st) {
		m_row.field_Login = st;
		m_row.IsLoginNull = false;
		m_row.IsLoginDirty = true;
	}
	bool IsLoginNull() const {
		return m_row.IsLoginNull;
	}
	void SetLoginToNull() {
		m_row.field_Login = "";
		m_row.IsLoginNull = true;
		m_row.IsLoginDirty = true;
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

	std::string GetEmail() {
		if( m_row.IsEmailNull )
			throw konga::DbNullException("Email");
		return m_row.field_Email;
	}
	void SetEmail(const std::string& st) {
		m_row.field_Email = st;
		m_row.IsEmailNull = false;
		m_row.IsEmailDirty = true;
	}
	bool IsEmailNull() const {
		return m_row.IsEmailNull;
	}
	void SetEmailToNull() {
		m_row.field_Email = "";
		m_row.IsEmailNull = true;
		m_row.IsEmailDirty = true;
	}

	int GetAccess_Level() {
		if( m_row.IsAccess_LevelNull )
			throw konga::DbNullException("Access_Level");
		return m_row.field_Access_Level;
	}
	void SetAccess_Level(int i) {
		m_row.field_Access_Level = i;
		m_row.IsAccess_LevelNull = false;
		m_row.IsAccess_LevelDirty = true;
	}
	bool IsAccess_LevelNull() const {
		return m_row.IsAccess_LevelNull;
	}
	void SetAccess_LevelToNull() {
		m_row.IsAccess_LevelNull = true;
		m_row.IsAccess_LevelDirty = true;
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
	UsersTabRow m_row;

private:
	bool IsUser_IDDirty() const {
		return m_row.IsUser_IDDirty;
	};
	bool IsFirst_NameDirty() const {
		return m_row.IsFirst_NameDirty;
	};
	bool IsLast_NameDirty() const {
		return m_row.IsLast_NameDirty;
	};
	bool IsLoginDirty() const {
		return m_row.IsLoginDirty;
	};
	bool IsPasswordDirty() const {
		return m_row.IsPasswordDirty;
	};
	bool IsEmailDirty() const {
		return m_row.IsEmailDirty;
	};
	bool IsAccess_LevelDirty() const {
		return m_row.IsAccess_LevelDirty;
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
