/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_WSDLTAB_H
#define KONGA_DB_CRTRANDB_WSDLTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class WsdlTabRow
{
public:
	WsdlTabRow() : 
			field_Wsdl_ID(0),
			IsWsdl_IDNull(true),
			IsWsdl_IDPrimaryKey(false),
			IsWsdl_IDDirty(false),
			IsWsdlNameNull(true),
			IsWsdlNamePrimaryKey(false),
			IsWsdlNameDirty(false),
			IsWsdlURLNull(true),
			IsWsdlURLPrimaryKey(false),
			IsWsdlURLDirty(false),
			IsWsdlPathNull(true),
			IsWsdlPathPrimaryKey(false),
			IsWsdlPathDirty(false),
			field_Status(0),
			IsStatusNull(true),
			IsStatusPrimaryKey(false),
			IsStatusDirty(false),
			IsApplicationMessageNull(true),
			IsApplicationMessagePrimaryKey(false),
			IsApplicationMessageDirty(false),
			IsUserCommentNull(true),
			IsUserCommentPrimaryKey(false),
			IsUserCommentDirty(false),
			IsWsdlTypeNull(true),
			IsWsdlTypePrimaryKey(false),
			IsWsdlTypeDirty(false)
	{
	}

	void Copy(const WsdlTabRow& r)
	{
		field_Wsdl_ID = r.field_Wsdl_ID;
		IsWsdl_IDNull = r.IsWsdl_IDNull;
		IsWsdl_IDDirty = r.IsWsdl_IDDirty;

		field_WsdlName = r.field_WsdlName;
		IsWsdlNameNull = r.IsWsdlNameNull;
		IsWsdlNameDirty = r.IsWsdlNameDirty;

		field_WsdlURL = r.field_WsdlURL;
		IsWsdlURLNull = r.IsWsdlURLNull;
		IsWsdlURLDirty = r.IsWsdlURLDirty;

		field_WsdlPath = r.field_WsdlPath;
		IsWsdlPathNull = r.IsWsdlPathNull;
		IsWsdlPathDirty = r.IsWsdlPathDirty;

		field_Status = r.field_Status;
		IsStatusNull = r.IsStatusNull;
		IsStatusDirty = r.IsStatusDirty;

		field_ApplicationMessage = r.field_ApplicationMessage;
		IsApplicationMessageNull = r.IsApplicationMessageNull;
		IsApplicationMessageDirty = r.IsApplicationMessageDirty;

		field_UserComment = r.field_UserComment;
		IsUserCommentNull = r.IsUserCommentNull;
		IsUserCommentDirty = r.IsUserCommentDirty;

		field_WsdlType = r.field_WsdlType;
		IsWsdlTypeNull = r.IsWsdlTypeNull;
		IsWsdlTypeDirty = r.IsWsdlTypeDirty;
	}

	void CopyPKs(const WsdlTabRow& r)
	{
		field_Wsdl_ID = r.field_Wsdl_ID;
		IsWsdl_IDNull = r.IsWsdl_IDNull;
		IsWsdl_IDDirty = r.IsWsdl_IDDirty;
	}

	void SetDirty(bool b)
	{
		IsWsdl_IDDirty = b;
		IsWsdlNameDirty = b;
		IsWsdlURLDirty = b;
		IsWsdlPathDirty = b;
		IsStatusDirty = b;
		IsApplicationMessageDirty = b;
		IsUserCommentDirty = b;
		IsWsdlTypeDirty = b;
	}

	bool IsDirty() const
	{
		return IsWsdl_IDDirty ||
			IsWsdlNameDirty ||
			IsWsdlURLDirty ||
			IsWsdlPathDirty ||
			IsStatusDirty ||
			IsApplicationMessageDirty ||
			IsUserCommentDirty ||
			IsWsdlTypeDirty;
	}

	int field_Wsdl_ID;
	bool IsWsdl_IDNull;
	bool IsWsdl_IDDirty;

	std::string field_WsdlName;
	bool IsWsdlNameNull;
	bool IsWsdlNameDirty;

	std::string field_WsdlURL;
	bool IsWsdlURLNull;
	bool IsWsdlURLDirty;

	std::string field_WsdlPath;
	bool IsWsdlPathNull;
	bool IsWsdlPathDirty;

	int field_Status;
	bool IsStatusNull;
	bool IsStatusDirty;

	std::string field_ApplicationMessage;
	bool IsApplicationMessageNull;
	bool IsApplicationMessageDirty;

	std::string field_UserComment;
	bool IsUserCommentNull;
	bool IsUserCommentDirty;

	int field_WsdlType;
	bool IsWsdlTypeNull;
	bool IsWsdlTypeDirty;

private:
	bool IsWsdl_IDPrimaryKey;
	bool IsWsdlNamePrimaryKey;
	bool IsWsdlURLPrimaryKey;
	bool IsWsdlPathPrimaryKey;
	bool IsStatusPrimaryKey;
	bool IsApplicationMessagePrimaryKey;
	bool IsUserCommentPrimaryKey;
	bool IsWsdlTypePrimaryKey;
};


class WsdlTab : public konga::DbRecordSet {
public:
	WsdlTab() {}

	static WsdlTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "WsdlTab";
	}

	void DoFieldExchange(const WsdlTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetWsdl_ID() {
		if( m_row.IsWsdl_IDNull )
			throw konga::DbNullException("Wsdl_ID");
		return m_row.field_Wsdl_ID;
	}
	void SetWsdl_ID(int i) {
		m_row.field_Wsdl_ID = i;
		m_row.IsWsdl_IDNull = false;
		m_row.IsWsdl_IDDirty = true;
	}
	bool IsWsdl_IDNull() const {
		return m_row.IsWsdl_IDNull;
	}
	void SetWsdl_IDToNull() {
		m_row.IsWsdl_IDNull = true;
		m_row.IsWsdl_IDDirty = true;
	}

	std::string GetWsdlName() {
		if( m_row.IsWsdlNameNull )
			throw konga::DbNullException("WsdlName");
		return m_row.field_WsdlName;
	}
	void SetWsdlName(const std::string& st) {
		m_row.field_WsdlName = st;
		m_row.IsWsdlNameNull = false;
		m_row.IsWsdlNameDirty = true;
	}
	bool IsWsdlNameNull() const {
		return m_row.IsWsdlNameNull;
	}
	void SetWsdlNameToNull() {
		m_row.field_WsdlName = "";
		m_row.IsWsdlNameNull = true;
		m_row.IsWsdlNameDirty = true;
	}

	std::string GetWsdlURL() {
		if( m_row.IsWsdlURLNull )
			throw konga::DbNullException("WsdlURL");
		return m_row.field_WsdlURL;
	}
	void SetWsdlURL(const std::string& st) {
		m_row.field_WsdlURL = st;
		m_row.IsWsdlURLNull = false;
		m_row.IsWsdlURLDirty = true;
	}
	bool IsWsdlURLNull() const {
		return m_row.IsWsdlURLNull;
	}
	void SetWsdlURLToNull() {
		m_row.field_WsdlURL = "";
		m_row.IsWsdlURLNull = true;
		m_row.IsWsdlURLDirty = true;
	}

	std::string GetWsdlPath() {
		if( m_row.IsWsdlPathNull )
			throw konga::DbNullException("WsdlPath");
		return m_row.field_WsdlPath;
	}
	void SetWsdlPath(const std::string& st) {
		m_row.field_WsdlPath = st;
		m_row.IsWsdlPathNull = false;
		m_row.IsWsdlPathDirty = true;
	}
	bool IsWsdlPathNull() const {
		return m_row.IsWsdlPathNull;
	}
	void SetWsdlPathToNull() {
		m_row.field_WsdlPath = "";
		m_row.IsWsdlPathNull = true;
		m_row.IsWsdlPathDirty = true;
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

	std::string GetApplicationMessage() {
		if( m_row.IsApplicationMessageNull )
			throw konga::DbNullException("ApplicationMessage");
		return m_row.field_ApplicationMessage;
	}
	void SetApplicationMessage(const std::string& st) {
		m_row.field_ApplicationMessage = st;
		m_row.IsApplicationMessageNull = false;
		m_row.IsApplicationMessageDirty = true;
	}
	bool IsApplicationMessageNull() const {
		return m_row.IsApplicationMessageNull;
	}
	void SetApplicationMessageToNull() {
		m_row.field_ApplicationMessage = "";
		m_row.IsApplicationMessageNull = true;
		m_row.IsApplicationMessageDirty = true;
	}

	std::string GetUserComment() {
		if( m_row.IsUserCommentNull )
			throw konga::DbNullException("UserComment");
		return m_row.field_UserComment;
	}
	void SetUserComment(const std::string& st) {
		m_row.field_UserComment = st;
		m_row.IsUserCommentNull = false;
		m_row.IsUserCommentDirty = true;
	}
	bool IsUserCommentNull() const {
		return m_row.IsUserCommentNull;
	}
	void SetUserCommentToNull() {
		m_row.field_UserComment = "";
		m_row.IsUserCommentNull = true;
		m_row.IsUserCommentDirty = true;
	}

	int GetWsdlType() {
		if( m_row.IsWsdlTypeNull )
			throw konga::DbNullException("WsdlType");
		return m_row.field_WsdlType;
	}
	void SetWsdlType(int i) {
		m_row.field_WsdlType = i;
		m_row.IsWsdlTypeNull = false;
		m_row.IsWsdlTypeDirty = true;
	}
	bool IsWsdlTypeNull() const {
		return m_row.IsWsdlTypeNull;
	}
	void SetWsdlTypeToNull() {
		m_row.IsWsdlTypeNull = true;
		m_row.IsWsdlTypeDirty = true;
	}

protected:
	WsdlTabRow m_row;

private:
	bool IsWsdl_IDDirty() const {
		return m_row.IsWsdl_IDDirty;
	};
	bool IsWsdlNameDirty() const {
		return m_row.IsWsdlNameDirty;
	};
	bool IsWsdlURLDirty() const {
		return m_row.IsWsdlURLDirty;
	};
	bool IsWsdlPathDirty() const {
		return m_row.IsWsdlPathDirty;
	};
	bool IsStatusDirty() const {
		return m_row.IsStatusDirty;
	};
	bool IsApplicationMessageDirty() const {
		return m_row.IsApplicationMessageDirty;
	};
	bool IsUserCommentDirty() const {
		return m_row.IsUserCommentDirty;
	};
	bool IsWsdlTypeDirty() const {
		return m_row.IsWsdlTypeDirty;
	};

	std::string m_stUpdateFilter;

};

}                         


#endif
