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

#include "BillingTabUtils.h"

using namespace std;
using namespace konga;


                                                            
                                                          
namespace A {
	string GetWhereClause(long lOperationId) {
		return string("Operation_ID = " + toString(lOperationId));
	}
	string GetWhereClause(const CrDateTime& deleteBeforeDate) {
		TIMESTAMP_STRUCT ts;
		setTimeStruct(ts, deleteBeforeDate);
		return string("InsertTime < '" + GetISO8601TimeStampString(ts) + "'");
	}
}
using namespace A;

void BillingTabUtils::DeleteOldLogs(const CrDateTime& deleteBeforeDate) {
	const string stSql("DELETE FROM BillingTab WHERE " + GetWhereClause(deleteBeforeDate));
	m_dbConnection.ExecuteSql(stSql);
}

void BillingTabUtils::ArchiveOldLogs(const CrDateTime& deleteBeforeDate) {

	AutoRollbackTransaction transaction(&m_dbConnection);

	string filter(GetWhereClause(deleteBeforeDate));

	string sql("INSERT INTO ArchiveBillingTab (BillingID, Operation_ID, Start_Date, End_Date, Total_Files, "
		"Total_Size, Total_Source_Size, Total_Target_Size, Total_Transformation_Time, BillAcc_ID, Comments, InsertTime, ModTime) "
		"SELECT BillingID, Operation_ID, Start_Date, End_Date, Total_Files, "
		"Total_Size, Total_Source_Size, Total_Target_Size, Total_Transformation_Time, BillAcc_ID, Comments, InsertTime, ModTime "
		"FROM BillingTab WHERE " + filter);
	m_dbConnection.ExecuteSql(sql);

	sql = "DELETE FROM BillingTab WHERE " + filter;
	m_dbConnection.ExecuteSql(sql);

	transaction.Commit();
}

void BillingTabUtils::DeleteLogsForOperation(long lOperationId, const CrDateTime& deleteBeforeDate) {
	const string stWhereClause(" WHERE " + GetWhereClause(lOperationId)
		+ " AND " + GetWhereClause(deleteBeforeDate));

	const string stSql("DELETE FROM BillingTab" + stWhereClause);

	m_dbConnection.ExecuteSql(stSql);
}

void BillingTabUtils::DeleteLogsForOperation(long lOperationId) {
	const string stWhereClause(" WHERE " + GetWhereClause(lOperationId));

	const string stSql("DELETE FROM BillingTab" + stWhereClause);

	m_dbConnection.ExecuteSql(stSql);
}
