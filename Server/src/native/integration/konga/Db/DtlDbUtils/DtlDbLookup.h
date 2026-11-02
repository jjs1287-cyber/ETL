/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef DTLDBLOOKUP_H
#define DTLDBLOOKUP_H

#include <konga/Db/DbUtils/DbLookup.h>
#include <konga/Db/DbUtils/DbConnection.h>

#include "RefCountedDtlDbConnection.h"

namespace konga {

class DtlDbLookup : public DbLookup {
public:
	DtlDbLookup(const std::string& stConnectionString, const TransactionIsolationLevel txnIsolationLevel)
		: m_iAutoCommitFlag(-1), m_pConn(0), m_txnIsolationLevel(txnIsolationLevel), m_lRows(0),
		m_stConnectionString(stConnectionString) {}

	virtual ~DtlDbLookup();
	
	virtual bool IsOpen();
	virtual void Close();

	virtual DbLookupResultState LookupSingleValue(const std::string& stSql, std::string& value);

	virtual DbLookupResultState Lookup(const std::string& stSql, 
									   DbRowSetData& rowset,
									   unsigned long nMaxRows, long timeOutInSeconds = -1);

	virtual long GetRowCount();

	virtual long UseAutoCommit(bool b);
	virtual void CommitTransaction();
	virtual void RollbackTransaction();

protected:
	virtual void Connect();

	std::string GetConnectionString() const {
		return m_stConnectionString;
	}

private:
	int m_iAutoCommitFlag;
	RefCountedDtlDbConnection*	m_pConn;
	const TransactionIsolationLevel m_txnIsolationLevel;
	long m_lRows;

	std::string	m_stConnectionString;
};

}                       

#endif                 
