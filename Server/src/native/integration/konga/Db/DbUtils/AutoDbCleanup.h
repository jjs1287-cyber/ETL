/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef AUTODBCLEANUP_H
#define AUTODBCLEANUP_H

#include <string>

#include "DbConnection.h"

namespace konga {

class AutoRollbackTransaction {
public:
	AutoRollbackTransaction(DbConnection* pDb) : m_pDb(pDb) {
		if ( pDb ) {
			pDb->SetAutoCommit(false);
		}
	}

	~AutoRollbackTransaction(void) {
		try {
			Rollback();
		}
		catch(...) {
			                                 
		}
	}

	void Rollback() {
		if( m_pDb ) {
			try {
				m_pDb->Rollback();
				m_pDb->SetAutoCommit(true);
			}
			catch(...) {
				                                             
				m_pDb = 0;
				throw;
			}
			m_pDb = 0;
		}
	}

	void Commit() {
		if ( m_pDb ) {
			try {
				m_pDb->Commit();
				m_pDb->SetAutoCommit(true);
			}
			catch(...) {
				                                             
				m_pDb = 0;
				throw;
			}
			m_pDb = 0;
		}
	}

	void Ignore() {
		if ( m_pDb ) {
			try {
				m_pDb->SetAutoCommit(true);
			}
			catch(...) {
				                                             
				m_pDb = 0;
				throw;
			}
			m_pDb = 0;
		}
	}

private:
	DbConnection* m_pDb;
};

class AutoDbHandle {
public:
	AutoDbHandle(SQLHDBC hdbc) : m_hdbc(hdbc) {}
	virtual ~AutoDbHandle() {
		if( m_hdbc )
			SQLFreeHandle(SQL_HANDLE_DBC, m_hdbc);
	}
	void Release() { m_hdbc = 0; }
private:
	SQLHDBC m_hdbc;
};

class AutoDbEnvHandle {
public:
	AutoDbEnvHandle(SQLHENV henv) : m_henv(henv) {}
	virtual ~AutoDbEnvHandle() {
		if( m_henv )
			SQLFreeHandle(SQL_HANDLE_ENV, m_henv);
	}
	void Release() { m_henv = 0; }
private:
	SQLHENV m_henv;
};

}                   

#endif                   
