/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_HISTORYLOGSVIEW_H
#define KONGA_DB_CRTRANDB_HISTORYLOGSVIEW_H

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

class HistoryLogsViewRow
{
public:
	HistoryLogsViewRow() : 
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
			IsStart_DateNull(true),
			IsStart_DatePrimaryKey(false),
			IsStart_DateDirty(false),
			IsEnd_DateNull(true),
			IsEnd_DatePrimaryKey(false),
			IsEnd_DateDirty(false),
			field_Total_Files(0),
			IsTotal_FilesNull(true),
			IsTotal_FilesPrimaryKey(false),
			IsTotal_FilesDirty(false),
			field_Total_Size(0),
			IsTotal_SizeNull(true),
			IsTotal_SizePrimaryKey(false),
			IsTotal_SizeDirty(false)
	{
		field_Start_Date.year = 0;
		field_Start_Date.month = 0;
		field_Start_Date.day = 0;
		field_Start_Date.hour = 0;
		field_Start_Date.minute = 0;
		field_Start_Date.second = 0;
		field_Start_Date.fraction = 0;
		field_End_Date.year = 0;
		field_End_Date.month = 0;
		field_End_Date.day = 0;
		field_End_Date.hour = 0;
		field_End_Date.minute = 0;
		field_End_Date.second = 0;
		field_End_Date.fraction = 0;
	}

	void Copy(const HistoryLogsViewRow& r)
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

		field_Start_Date = r.field_Start_Date;
		IsStart_DateNull = r.IsStart_DateNull;
		IsStart_DateDirty = r.IsStart_DateDirty;

		field_End_Date = r.field_End_Date;
		IsEnd_DateNull = r.IsEnd_DateNull;
		IsEnd_DateDirty = r.IsEnd_DateDirty;

		field_Total_Files = r.field_Total_Files;
		IsTotal_FilesNull = r.IsTotal_FilesNull;
		IsTotal_FilesDirty = r.IsTotal_FilesDirty;

		field_Total_Size = r.field_Total_Size;
		IsTotal_SizeNull = r.IsTotal_SizeNull;
		IsTotal_SizeDirty = r.IsTotal_SizeDirty;

	}

	void CopyPKs(const HistoryLogsViewRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		Isinterchangeproject_idDirty = b;
		IsOperation_IDDirty = b;
		IsOperation_NameDirty = b;
		IsIDDirty = b;
		IsStart_DateDirty = b;
		IsEnd_DateDirty = b;
		IsTotal_FilesDirty = b;
		IsTotal_SizeDirty = b;
	}

	bool IsDirty() const
	{
		return Isinterchangeproject_idDirty ||
			IsOperation_IDDirty ||
			IsOperation_NameDirty ||
			IsIDDirty ||
			IsStart_DateDirty ||
			IsEnd_DateDirty ||
			IsTotal_FilesDirty ||
			IsTotal_SizeDirty;
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

	TIMESTAMP_STRUCT field_Start_Date;
	bool IsStart_DateNull;
	bool IsStart_DateDirty;

	TIMESTAMP_STRUCT field_End_Date;
	bool IsEnd_DateNull;
	bool IsEnd_DateDirty;

	int field_Total_Files;
	bool IsTotal_FilesNull;
	bool IsTotal_FilesDirty;

	int field_Total_Size;
	bool IsTotal_SizeNull;
	bool IsTotal_SizeDirty;


private:
	bool Isinterchangeproject_idPrimaryKey;
	bool IsOperation_IDPrimaryKey;
	bool IsOperation_NamePrimaryKey;
	bool IsIDPrimaryKey;
	bool IsStart_DatePrimaryKey;
	bool IsEnd_DatePrimaryKey;
	bool IsTotal_FilesPrimaryKey;
	bool IsTotal_SizePrimaryKey;

};


class HistoryLogsView : public konga::DbRecordSet {
public:
	HistoryLogsView() {}

	static HistoryLogsView* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "HistoryLogsView";
	}

	void DoFieldExchange(const HistoryLogsViewRow& row) {
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

	TIMESTAMP_STRUCT GetStart_Date() {
		if( m_row.IsStart_DateNull )
			throw konga::DbNullException("Start_Date");
		return m_row.field_Start_Date;
	}
	void SetStart_Date(const TIMESTAMP_STRUCT& ts) {
		m_row.field_Start_Date = ts;
		m_row.IsStart_DateNull = false;
		m_row.IsStart_DateDirty = true;
	}
	bool IsStart_DateNull() const {
		return m_row.IsStart_DateNull;
	}
	void SetStart_DateToNull() {
		m_row.IsStart_DateNull = true;
		m_row.IsStart_DateDirty = true;
	}

	TIMESTAMP_STRUCT GetEnd_Date() {
		if( m_row.IsEnd_DateNull )
			throw konga::DbNullException("End_Date");
		return m_row.field_End_Date;
	}
	void SetEnd_Date(const TIMESTAMP_STRUCT& ts) {
		m_row.field_End_Date = ts;
		m_row.IsEnd_DateNull = false;
		m_row.IsEnd_DateDirty = true;
	}
	bool IsEnd_DateNull() const {
		return m_row.IsEnd_DateNull;
	}
	void SetEnd_DateToNull() {
		m_row.IsEnd_DateNull = true;
		m_row.IsEnd_DateDirty = true;
	}

	int GetTotal_Files() {
		if( m_row.IsTotal_FilesNull )
			throw konga::DbNullException("Total_Files");
		return m_row.field_Total_Files;
	}
	void SetTotal_Files(int i) {
		m_row.field_Total_Files = i;
		m_row.IsTotal_FilesNull = false;
		m_row.IsTotal_FilesDirty = true;
	}
	bool IsTotal_FilesNull() const {
		return m_row.IsTotal_FilesNull;
	}
	void SetTotal_FilesToNull() {
		m_row.IsTotal_FilesNull = true;
		m_row.IsTotal_FilesDirty = true;
	}

	int GetTotal_Size() {
		if( m_row.IsTotal_SizeNull )
			throw konga::DbNullException("Total_Size");
		return m_row.field_Total_Size;
	}
	void SetTotal_Size(int i) {
		m_row.field_Total_Size = i;
		m_row.IsTotal_SizeNull = false;
		m_row.IsTotal_SizeDirty = true;
	}
	bool IsTotal_SizeNull() const {
		return m_row.IsTotal_SizeNull;
	}
	void SetTotal_SizeToNull() {
		m_row.IsTotal_SizeNull = true;
		m_row.IsTotal_SizeDirty = true;
	}

protected:
	HistoryLogsViewRow m_row;

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
	bool IsStart_DateDirty() const {
		return m_row.IsStart_DateDirty;
	};
	bool IsEnd_DateDirty() const {
		return m_row.IsEnd_DateDirty;
	};
	bool IsTotal_FilesDirty() const {
		return m_row.IsTotal_FilesDirty;
	};
	bool IsTotal_SizeDirty() const {
		return m_row.IsTotal_SizeDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
