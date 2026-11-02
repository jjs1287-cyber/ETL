/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_XFERHISTORYLOGSVIEW_H
#define KONGA_DB_CRTRANDB_XFERHISTORYLOGSVIEW_H

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

class XferHistoryLogsViewRow
{
public:
	XferHistoryLogsViewRow() : 
			field_interchangeproject_id(0),
			Isinterchangeproject_idNull(true),
			Isinterchangeproject_idPrimaryKey(false),
			Isinterchangeproject_idDirty(false),
			field_Operation_ID(0),
			IsOperation_IDNull(true),
			IsOperation_IDPrimaryKey(false),
			IsOperation_IDDirty(false),
			IsOperation_NameNull(true),
			IsOperation_NamePrimaryKey(false),
			IsOperation_NameDirty(false),
			field_ID(0),
			IsIDNull(true),
			IsIDPrimaryKey(false),
			IsIDDirty(false),
			IsFileNameNull(true),
			IsFileNamePrimaryKey(false),
			IsFileNameDirty(false),
			field_FileSize(0),
			IsFileSizeNull(true),
			IsFileSizePrimaryKey(false),
			IsFileSizeDirty(false),
			IsXferDateTimeNull(true),
			IsXferDateTimePrimaryKey(false),
			IsXferDateTimeDirty(false)
	{
		field_XferDateTime.year = 0;
		field_XferDateTime.month = 0;
		field_XferDateTime.day = 0;
		field_XferDateTime.hour = 0;
		field_XferDateTime.minute = 0;
		field_XferDateTime.second = 0;
		field_XferDateTime.fraction = 0;
	}

	void Copy(const XferHistoryLogsViewRow& r)
	{
		field_interchangeproject_id = r.field_interchangeproject_id;
		Isinterchangeproject_idNull = r.Isinterchangeproject_idNull;
		Isinterchangeproject_idDirty = r.Isinterchangeproject_idDirty;

		field_Operation_ID = r.field_Operation_ID;
		IsOperation_IDNull = r.IsOperation_IDNull;
		IsOperation_IDDirty = r.IsOperation_IDDirty;

		field_Operation_Name = r.field_Operation_Name;
		IsOperation_NameNull = r.IsOperation_NameNull;
		IsOperation_NameDirty = r.IsOperation_NameDirty;

		field_ID = r.field_ID;
		IsIDNull = r.IsIDNull;
		IsIDDirty = r.IsIDDirty;

		field_FileName = r.field_FileName;
		IsFileNameNull = r.IsFileNameNull;
		IsFileNameDirty = r.IsFileNameDirty;

		field_FileSize = r.field_FileSize;
		IsFileSizeNull = r.IsFileSizeNull;
		IsFileSizeDirty = r.IsFileSizeDirty;

		field_XferDateTime = r.field_XferDateTime;
		IsXferDateTimeNull = r.IsXferDateTimeNull;
		IsXferDateTimeDirty = r.IsXferDateTimeDirty;

	}

	void CopyPKs(const XferHistoryLogsViewRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		Isinterchangeproject_idDirty = b;
		IsOperation_IDDirty = b;
		IsOperation_NameDirty = b;
		IsIDDirty = b;
		IsFileNameDirty = b;
		IsFileSizeDirty = b;
		IsXferDateTimeDirty = b;
	}

	bool IsDirty() const
	{
		return Isinterchangeproject_idDirty ||
			IsOperation_IDDirty ||
			IsOperation_NameDirty ||
			IsIDDirty ||
			IsFileNameDirty ||
			IsFileSizeDirty ||
			IsXferDateTimeDirty;
	}

	int field_interchangeproject_id;
	bool Isinterchangeproject_idNull;
	bool Isinterchangeproject_idDirty;

	int field_Operation_ID;
	bool IsOperation_IDNull;
	bool IsOperation_IDDirty;

	std::string field_Operation_Name;
	bool IsOperation_NameNull;
	bool IsOperation_NameDirty;

	int field_ID;
	bool IsIDNull;
	bool IsIDDirty;

	std::string field_FileName;
	bool IsFileNameNull;
	bool IsFileNameDirty;

	int field_FileSize;
	bool IsFileSizeNull;
	bool IsFileSizeDirty;

	TIMESTAMP_STRUCT field_XferDateTime;
	bool IsXferDateTimeNull;
	bool IsXferDateTimeDirty;


private:
	bool Isinterchangeproject_idPrimaryKey;
	bool IsOperation_IDPrimaryKey;
	bool IsOperation_NamePrimaryKey;
	bool IsIDPrimaryKey;
	bool IsFileNamePrimaryKey;
	bool IsFileSizePrimaryKey;
	bool IsXferDateTimePrimaryKey;

};


class XferHistoryLogsView : public konga::DbRecordSet {
public:
	XferHistoryLogsView() {}

	static XferHistoryLogsView* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "XferHistoryLogsView";
	}

	void DoFieldExchange(const XferHistoryLogsViewRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int Getinterchangeproject_id() {
		if( m_row.Isinterchangeproject_idNull )
			throw konga::DbNullException("interchangeproject_id");
		return m_row.field_interchangeproject_id;
	}
	void Setinterchangeproject_id(int i) {
		m_row.field_interchangeproject_id = i;
		m_row.Isinterchangeproject_idNull = false;
		m_row.Isinterchangeproject_idDirty = true;
	}
	bool Isinterchangeproject_idNull() const {
		return m_row.Isinterchangeproject_idNull;
	}
	void Setinterchangeproject_idToNull() {
		m_row.Isinterchangeproject_idNull = true;
		m_row.Isinterchangeproject_idDirty = true;
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

	int GetID() {
		if( m_row.IsIDNull )
			throw konga::DbNullException("ID");
		return m_row.field_ID;
	}
	void SetID(int i) {
		m_row.field_ID = i;
		m_row.IsIDNull = false;
		m_row.IsIDDirty = true;
	}
	bool IsIDNull() const {
		return m_row.IsIDNull;
	}
	void SetIDToNull() {
		m_row.IsIDNull = true;
		m_row.IsIDDirty = true;
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

	int GetFileSize() {
		if( m_row.IsFileSizeNull )
			throw konga::DbNullException("FileSize");
		return m_row.field_FileSize;
	}
	void SetFileSize(int i) {
		m_row.field_FileSize = i;
		m_row.IsFileSizeNull = false;
		m_row.IsFileSizeDirty = true;
	}
	bool IsFileSizeNull() const {
		return m_row.IsFileSizeNull;
	}
	void SetFileSizeToNull() {
		m_row.IsFileSizeNull = true;
		m_row.IsFileSizeDirty = true;
	}

	TIMESTAMP_STRUCT GetXferDateTime() {
		if( m_row.IsXferDateTimeNull )
			throw konga::DbNullException("XferDateTime");
		return m_row.field_XferDateTime;
	}
	void SetXferDateTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_XferDateTime = ts;
		m_row.IsXferDateTimeNull = false;
		m_row.IsXferDateTimeDirty = true;
	}
	bool IsXferDateTimeNull() const {
		return m_row.IsXferDateTimeNull;
	}
	void SetXferDateTimeToNull() {
		m_row.IsXferDateTimeNull = true;
		m_row.IsXferDateTimeDirty = true;
	}

protected:
	XferHistoryLogsViewRow m_row;

private:
	bool Isinterchangeproject_idDirty() const {
		return m_row.Isinterchangeproject_idDirty;
	};
	bool IsOperation_IDDirty() const {
		return m_row.IsOperation_IDDirty;
	};
	bool IsOperation_NameDirty() const {
		return m_row.IsOperation_NameDirty;
	};
	bool IsIDDirty() const {
		return m_row.IsIDDirty;
	};
	bool IsFileNameDirty() const {
		return m_row.IsFileNameDirty;
	};
	bool IsFileSizeDirty() const {
		return m_row.IsFileSizeDirty;
	};
	bool IsXferDateTimeDirty() const {
		return m_row.IsXferDateTimeDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
