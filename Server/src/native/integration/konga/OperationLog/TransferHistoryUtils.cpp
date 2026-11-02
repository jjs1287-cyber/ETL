/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/CrException.h>
#include <konga/Utils/DbTypeUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbException.h>

#include <konga/Db/DbUtils/AutoDbCleanup.h>


#include "TransferHistoryUtils.h"

using namespace std;
using namespace konga;


namespace {
	string GetWhereClause(long lOperationId) {
		return string("OperationID = " + toString(lOperationId));
	}
	string GetWhereClause(const CrDateTime& deleteBeforeDate) {
		TIMESTAMP_STRUCT ts;
		setTimeStruct(ts, deleteBeforeDate);
		return string("XferDateTime < '" + GetISO8601TimeStampString(ts) + "'");
	}
}

void TransferHistoryUtils::DeleteOldLogs(const CrDateTime& deleteBeforeDate) {
	const string stSql("DELETE FROM XferHist WHERE " + GetWhereClause(deleteBeforeDate));
	m_dbConnection.ExecuteSql(stSql);
}

void TransferHistoryUtils::ArchiveOldLogs(const CrDateTime& deleteBeforeDate) {

	AutoRollbackTransaction transaction(&m_dbConnection);

	string filter(GetWhereClause(deleteBeforeDate));

	string sql("INSERT INTO ArchiveXferHist (DirEntry, FileName, OperationID, FileSize, "
		"FileMonth, FileDay, FileTimeOrYear, XferDateTime, ProgressStatus, RetryCount, LocalFileName,InsertTime, ModTime) " 
		"SELECT DirEntry, FileName, OperationID, FileSize, "
		"FileMonth, FileDay, FileTimeOrYear, XferDateTime, ProgressStatus, RetryCount, LocalFileName,InsertTime, ModTime "
		"FROM XferHist WHERE " + filter);
	m_dbConnection.ExecuteSql(sql);

	sql = "DELETE FROM XferHist WHERE " + filter;
	m_dbConnection.ExecuteSql(sql);

	transaction.Commit();
}

void TransferHistoryUtils::DeleteLogsForOperation(long lOperationId, const CrDateTime& deleteBeforeDate) {
	const string stWhereClause(" WHERE " + GetWhereClause(lOperationId)
		+ " AND " + GetWhereClause(deleteBeforeDate));

	const string stSql("DELETE FROM XferHist" + stWhereClause);

	m_dbConnection.ExecuteSql(stSql);
}

void TransferHistoryUtils::DeleteLogsForOperation(long lOperationId) {
	const string stWhereClause(" WHERE " + GetWhereClause(lOperationId));

	const string stSql("DELETE FROM XferHist" + stWhereClause);

	m_dbConnection.ExecuteSql(stSql);
}
