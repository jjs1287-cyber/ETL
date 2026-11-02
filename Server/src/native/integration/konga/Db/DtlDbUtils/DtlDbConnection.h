/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTLDBCONNECTION_H
#define KONGA_DTLDBCONNECTION_H

#include <dtl/DTL.h>

#include <konga/Db/DbUtils/DbConnection.h>

#include "RefCountedDtlDbConnection.h"

class DtlDbConnection : public konga::DbConnection {

       
                               
       
        

public:
	DtlDbConnection() : m_lAffectedRowCount(0L) {}
	virtual ~DtlDbConnection();

    void Open(const std::string& stConnectString);
	bool IsOpen();
	void Close();
	void SetAutoCommit(bool b);
	bool GetAutoCommit();
	void Commit();
	void Rollback();
    void ExecuteSql(const std::string& stSql);
                                                                                    
    long GetAffectedRowCount() {
        return m_lAffectedRowCount;
    }

    std::string GetConnectionString() {
        return m_conn.GetDSN();
    }

    SQLHENV GetEnvironmentHandle();
    SQLHDBC GetConnectionHandle();

    void SetTransactionIsolationLevel(konga::TransactionIsolationLevel level);

	konga::TransactionIsolationLevel GetTransactionIsolationLevel();

    void SetLoginTimeout(unsigned long nTimeoutInSecs) {
                               
    }
    void SetQueryTimeout(unsigned long nTimeoutInSecs) {
                               
    }
		
	dtl::DBConnection*	GetDtlConnection() {
		return &m_conn;
	}

protected:
	                                                               
	void CheckForOdbcError(SQLRETURN ret) {
		switch(ret) {
		case SQL_SUCCESS:
		case SQL_SUCCESS_WITH_INFO:
			return;
		case SQL_ERROR:
		case SQL_INVALID_HANDLE:
			return;
		}
	}
private:
	RefCountedDtlDbConnection m_conn;
    long m_lAffectedRowCount;
};

#endif                           

