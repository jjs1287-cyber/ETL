/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_GETLOGGUIDPROCID_H
#define KONGA_DB_CRPDB_GETLOGGUIDPROCID_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace PDb
{

class GetLogGUIDProcIDRow
{
public:
	GetLogGUIDProcIDRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(false),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(false),
			IsProcessVersionDirty(false)
	{
	}

	void Copy(const GetLogGUIDProcIDRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

	}

	void CopyPKs(const GetLogGUIDProcIDRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;

};


class GetLogGUIDProcID : public konga::DbRecordSet {
public:
	GetLogGUIDProcID() {}

	static GetLogGUIDProcID* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "GetLogGUIDProcID";
	}

	void DoFieldExchange(const GetLogGUIDProcIDRow& row) {
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

protected:
	GetLogGUIDProcIDRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};


	std::string m_stUpdateFilter;

};

}                      


#endif
