/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/DateUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include "OperationProgress.h"

using namespace konga;
using std::string;
using std::map;

OperationProgress::OperationProgress(DbConnection& dbConnection, long long llOperationInstanceId)
	: m_bEntryExists(false), m_llOperationInstanceId(llOperationInstanceId), m_dbConnection(dbConnection) {
}

OperationProgress::~OperationProgress(void) {
}

void OperationProgress::UpdateProgress(const std::string& stSection, const std::string& stMessage,
									   int percentage_done, long long estimated_time_left_sec)
{
	if ( percentage_done >= 100 ) {
		percentage_done = 100;
	}
	else if ( percentage_done >= 0 ) {
		percentage_done = percentage_done;
	}
	else {
		percentage_done = -1;
	}

	if ( estimated_time_left_sec < 0 ) {
		estimated_time_left_sec = -1;
	}

	string stTimeStampSql("null");
	if ( estimated_time_left_sec >= 0 ) {
		CrDateTime dt = CrDateTime::GetCurrentUtcTime();
		dt += CrTimeDuration(0, 0, estimated_time_left_sec, 0);
		stTimeStampSql = "'" + dt.toString("yyyy-mm-dd HH:MM:SS") + "'";
	}

	string sql;
	const string& stOID = toString(m_llOperationInstanceId);
	const string& stSectionSql = "'" + konga::SqlEscape(stSection) + "'";
	const string& stMessageSql = "'" + konga::SqlEscape(stMessage) + "'";
	const string& stPercentageDone = toString(percentage_done);
	if ( EntryKnownToExists() ) {
		sql = "UPDATE operation_progress_tab SET section = " + stSectionSql + ", message = " + stMessageSql
			+ ", percentage_done = " + stPercentageDone + ", estimated_end_time = " + stTimeStampSql
			+ " WHERE operation_instance_id = " + stOID;
		m_dbConnection.ExecuteSql(sql);
	}
	else {
		sql = "INSERT INTO operation_progress_tab (operation_instance_id, section, message, percentage_done, estimated_end_time) VALUES ("
			+ stOID + "," + stSectionSql + "," + stMessageSql + "," + stPercentageDone + "," + stTimeStampSql + ")";
		m_dbConnection.ExecuteSql(sql);
		m_bEntryExists = true;
	}
}

bool OperationProgress::EntryKnownToExists() {
	if ( m_bEntryExists ) return true;

	const string sql("SELECT count(*) FROM operation_progress_tab WHERE operation_instance_id = " + toString(m_llOperationInstanceId));
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&m_dbConnection, sql) );
	rs->Open();
	if ( !rs->Next() ) return false;
	if ( rs->GetFieldValue(0).compare("0") != 0 ) {
		return (m_bEntryExists = true);
	}
	else {
		return false;
	}
}
