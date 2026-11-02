/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_RECOMMENDATIONSTAB_H
#define KONGA_DB_CRMSGDB_RECOMMENDATIONSTAB_H

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

class RecommendationsTabRow
{
public:
	RecommendationsTabRow() : 
			field_EngineErrId(0),
			IsEngineErrIdNull(true),
			IsEngineErrIdPrimaryKey(true),
			IsEngineErrIdDirty(false),
			IsEngineErrTextNull(true),
			IsEngineErrTextPrimaryKey(false),
			IsEngineErrTextDirty(false),
			IsRecommendationNull(true),
			IsRecommendationPrimaryKey(false),
			IsRecommendationDirty(false),
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

	void Copy(const RecommendationsTabRow& r)
	{
		field_EngineErrId = r.field_EngineErrId;
		IsEngineErrIdNull = r.IsEngineErrIdNull;
		IsEngineErrIdDirty = r.IsEngineErrIdDirty;

		field_EngineErrText = r.field_EngineErrText;
		IsEngineErrTextNull = r.IsEngineErrTextNull;
		IsEngineErrTextDirty = r.IsEngineErrTextDirty;

		field_Recommendation = r.field_Recommendation;
		IsRecommendationNull = r.IsRecommendationNull;
		IsRecommendationDirty = r.IsRecommendationDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const RecommendationsTabRow& r)
	{
		field_EngineErrId = r.field_EngineErrId;
		IsEngineErrIdNull = r.IsEngineErrIdNull;
		IsEngineErrIdDirty = r.IsEngineErrIdDirty;

	}

	void SetDirty(bool b)
	{
		IsEngineErrIdDirty = b;
		IsEngineErrTextDirty = b;
		IsRecommendationDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsEngineErrIdDirty ||
			IsEngineErrTextDirty ||
			IsRecommendationDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_EngineErrId;
	bool IsEngineErrIdNull;
	bool IsEngineErrIdDirty;

	std::string field_EngineErrText;
	bool IsEngineErrTextNull;
	bool IsEngineErrTextDirty;

	std::string field_Recommendation;
	bool IsRecommendationNull;
	bool IsRecommendationDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsEngineErrIdPrimaryKey;
	bool IsEngineErrTextPrimaryKey;
	bool IsRecommendationPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class RecommendationsTab : public konga::DbRecordSet {
public:
	RecommendationsTab() {}

	static RecommendationsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "RecommendationsTab";
	}

	void DoFieldExchange(const RecommendationsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetEngineErrId() {
		if( m_row.IsEngineErrIdNull )
			throw konga::DbNullException("EngineErrId");
		return m_row.field_EngineErrId;
	}
	void SetEngineErrId(int i) {
		m_row.field_EngineErrId = i;
		m_row.IsEngineErrIdNull = false;
		m_row.IsEngineErrIdDirty = true;
	}
	bool IsEngineErrIdNull() const {
		return m_row.IsEngineErrIdNull;
	}
	void SetEngineErrIdToNull() {
		m_row.IsEngineErrIdNull = true;
		m_row.IsEngineErrIdDirty = true;
	}

	std::string GetEngineErrText() {
		if( m_row.IsEngineErrTextNull )
			throw konga::DbNullException("EngineErrText");
		return m_row.field_EngineErrText;
	}
	void SetEngineErrText(const std::string& st) {
		m_row.field_EngineErrText = st;
		m_row.IsEngineErrTextNull = false;
		m_row.IsEngineErrTextDirty = true;
	}
	bool IsEngineErrTextNull() const {
		return m_row.IsEngineErrTextNull;
	}
	void SetEngineErrTextToNull() {
		m_row.field_EngineErrText = "";
		m_row.IsEngineErrTextNull = true;
		m_row.IsEngineErrTextDirty = true;
	}

	std::string GetRecommendation() {
		if( m_row.IsRecommendationNull )
			throw konga::DbNullException("Recommendation");
		return m_row.field_Recommendation;
	}
	void SetRecommendation(const std::string& st) {
		m_row.field_Recommendation = st;
		m_row.IsRecommendationNull = false;
		m_row.IsRecommendationDirty = true;
	}
	bool IsRecommendationNull() const {
		return m_row.IsRecommendationNull;
	}
	void SetRecommendationToNull() {
		m_row.field_Recommendation = "";
		m_row.IsRecommendationNull = true;
		m_row.IsRecommendationDirty = true;
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
	RecommendationsTabRow m_row;

private:
	bool IsEngineErrIdDirty() const {
		return m_row.IsEngineErrIdDirty;
	};
	bool IsEngineErrTextDirty() const {
		return m_row.IsEngineErrTextDirty;
	};
	bool IsRecommendationDirty() const {
		return m_row.IsRecommendationDirty;
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
