/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef DBUTILS_ODBC_DBLOOKUP_H
#define DBUTILS_ODBC_DBLOOKUP_H

#include <string>

#include "DbConnection.h"
#include "OdbcConnectionHolder.h"
#include "DbLookup.h"

namespace konga {
	class OdbcDbLookup : public DbLookup, public OdbcConnectionHolder {
	public:
		OdbcDbLookup(const std::string& stConnectionString, const TransactionIsolationLevel txnIsolationLevel);
		~OdbcDbLookup(void);

		virtual bool IsOpen();

		                                                                                                                           
		virtual void Close();

		virtual DbLookupResultState LookupSingleValue(const std::string& stSql, std::string& value);

		virtual DbLookupResultState Lookup(const std::string& stSql, DbRowSetData& rowset,
			unsigned long nMaxRows, long timeOutInSeconds = -1);

		virtual long GetRowCount();

		virtual long UseAutoCommit(bool b);

		virtual void CommitTransaction();
		virtual void RollbackTransaction();

	private:
		void ConnectImpl();

		void SetAutoCommitAttr();

		DbLookupResultState LookupImpl(const std::string& stSql, DbRowSetData& rowset,
			unsigned long nMaxRows, long nMaxCols, long timeOutInSeconds);

	private:
		bool m_bUseAutoCommit;
		int m_iCurrentAutoCommitSetting;
		long m_RowCount;

		const TransactionIsolationLevel m_txnIsolationLevel;
	};
}

#endif                           

