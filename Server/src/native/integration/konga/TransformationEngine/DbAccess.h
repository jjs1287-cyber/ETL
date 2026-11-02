/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANFORMATIONENGINE_DVACCESS_H
#define TRANFORMATIONENGINE_DVACCESS_H


#include <konga/Db/DbUtils/DbConnection.h>

#include "TxtParser.h"

class CROM;
class CViewLog;
class CErrorLog;
class TreeMapper;

namespace konga {
	class Utf8Conv;
}

class CDbAccess
{
	friend class OdbcSource;
public:
	CDbAccess(TreeMapper*p);
	~CDbAccess();
	TreeMapper* getTreeMapper(){return m_pTreeMapper;}

	                                    
	bool Connect(const KString& DSN, const KString& UserName, const KString& Passwd,
		const KString& Driver, const KString& Server, const KString& Database, const KString& ConnectString,
		int iTimeout, konga::TransactionIsolationLevel txnIsolationLevel);
	bool ExecSqlWithLongText(const KString& sStmt, const std::vector<CROM*>& paramCroms, bool bIsOracle, konga::Utf8Conv* conv);
	bool ExecuteSQL(KString Stmt);
	KString GetDbValue(const KString& sSQL, int& err);
	SQLLEN	 GetRowsAffected(SQLHSTMT stmt);
	SQLLEN	GetNumRowsAffected() {return m_NumRowsAffected;};
	  
	
	bool CommitTransaction();

	                 
	int	GetColumnsInfo(const KString& sTableName_in, std::vector<CROM*>& aColNames);

protected:
	void GetError(SQLSMALLINT HandleType, SQLHANDLE Handle, const KString& sOperation);

private:
	bool Disconnect();
	bool AbortTransaction();

public:
	KString m_LastErr;
	KString m_message;
	KString m_SqlState;
	KString m_BindingValueList;
	SQLLEN m_NumRowsAffected;
	bool m_bConnectionOpened;
	void SetDoCommit(bool b){m_bDoCommit=b;}

protected:
	SQLHENV m_hEnv;
	SQLHDBC m_hConn;
	SQLINTEGER m_iTimeout;

private:
	TreeMapper* m_pTreeMapper;
	CViewLog* m_viewlog;
	CErrorLog* m_errlog;
	char* m_pSQLPutDataBuffer;	                                                       

	bool	m_bAutoCommit;
	bool	m_bDoCommit;
	bool	m_bTransactionEnded;
};

#endif                                  
