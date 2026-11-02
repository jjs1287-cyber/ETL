/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include "DtlDbConnection.h"
#include "DtlDbUtils.h"

using namespace dtl;
using namespace konga;
using namespace konga_dtl;

DtlDbConnection::~DtlDbConnection() {
	Close();
}

void DtlDbConnection::Open(const std::string& stConnectString)
{
	try 
    {
		m_conn.RefCountedConnect(stConnectString, true);
	}
    catch(const DBException& ex )
    {
        ThrowDbException(ex, "", 1);
    }
	catch(const RootException& ex ) 
    {
		ThrowDbException(ex, "Failed to open the database connection. ", 2);
	}
#ifndef _DEBUG
    catch( ... ) 
    {
        throw DbException("An unknown error occurred when opening the database connection.", 3);
    }
#endif          
}

bool DtlDbConnection::IsOpen()
{
	try 
    {
		return m_conn.IsConnected();
	}
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 4);
    }
	catch( RootException& ex ) 
    {
		ThrowDbException(ex, "Failed to check if the database connection is open. ", 5);
	}
    catch( ... ) 
    {
        throw DbException("Failed to check if the connection is open: An unknown error occurred.", 6);
    }
	return false;                         
}

void DtlDbConnection::Close()
{
	try 
    {
		if( IsOpen() )
			m_conn.RefCountedRelease();
	}
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 7);
    }
	catch( RootException& ex ) 
    {
		ThrowDbException(ex, "Failed to close the database connection. ", 8);
	}
    catch( ... )
    {
        throw DbException("Failed to close the connection: An unknown error occurred.", 9);
    }
}

void DtlDbConnection::SetAutoCommit(bool b)
{
	                   
	                                                                                           
	                                                             
	                                                                               
	                                      
    try 
    {
		if ( b ) {
			m_conn.CommitAll();
		}
    }
    catch( DBException& ex )
    {
        ThrowDbException(ex, "Failed to commit the transaction before swithing to SQL_AUTOCOMMIT_ON.", 10);
    }
    catch( RootException& ex ) 
    {
        ThrowDbException(ex, "Failed to commit the transaction before swithing to SQL_AUTOCOMMIT_ON. ", 11);
    }
    catch( ... )
    {
        throw DbException("Failed to commit the transaction before swithing to SQL_AUTOCOMMIT_ON. An unkown error occurred.", 12);
    }

	                                 
	                          
	HDBC hdbc = m_conn.GetHDBC();
	SQLULEN val = b ? SQL_AUTOCOMMIT_ON : SQL_AUTOCOMMIT_OFF;
	SQLSetConnectOption(hdbc, SQL_AUTOCOMMIT, val);	

	                                                                                                             
                           
}

bool DtlDbConnection::GetAutoCommit()
{
	try 
    {
		return m_conn.GetAutoCommit();
	}
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 13);
    }
	catch( RootException& ex ) 
    {
		ThrowDbException(ex, "Failed to get the autocommit mode. ", 14);
	}
    catch( ... )
    {
        throw DbException("Failed to get the autocommit mode: An unknown error occurred.", 15);
    }
	return false;                         
}

void DtlDbConnection::Commit()
{
    try 
    {
        m_conn.CommitAll();
    }
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 16);
    }
    catch( RootException& ex ) 
    {
        ThrowDbException(ex, "Failed to commit the transaction. ", 17);
    }
    catch( ... )
    {
        throw DbException("Failed to commit the transaction: An unknown error occurred.", 18);
    }
}

void DtlDbConnection::Rollback()
{
    try 
    {
        m_conn.RollbackAll();
    }
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 19);
    }
    catch( RootException& ex ) 
    {
        ThrowDbException(ex, "Failed to roll back the transaction. ", 20);
    }
    catch( ... )
    {
        throw DbException("Failed to roll back the transaction: An unknown error occurred.", 21);
    }
}

void DtlDbConnection::SetTransactionIsolationLevel(konga::TransactionIsolationLevel txnIsolationLevel) {
	if ( m_conn.IsConnected() ) {
		konga::SetTransactionIsolationLevel(m_conn.GetHDBC(), txnIsolationLevel);
	}
}

konga::TransactionIsolationLevel DtlDbConnection::GetTransactionIsolationLevel() {
	if ( m_conn.IsConnected() ) {
		SQLINTEGER level = 0;
		SQLRETURN ret = ::SQLGetConnectAttr(m_conn.GetHDBC(), SQL_ATTR_TXN_ISOLATION, static_cast<SQLPOINTER>(&level), 0, 0);
		CheckForOdbcError(ret);
		return konga::TransactionIsolationLevel(level);
	}
	return UNDEFINED;
}

void DtlDbConnection::ExecuteSql(const std::string& stSql) {
    m_lAffectedRowCount = 0L;
    if( !IsOpen() )
    {
        throw DbException("The connection has not been opened.", 22);
    }
    try
    {
        DBStmt stmt(stSql, m_conn);
        stmt.Execute();
        m_lAffectedRowCount = stmt.RowCount();
    }
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 23);
    }
    catch( RootException& ex ) 
    {
        std::string stErr = "Failed to execute the SQL statement \"";
        stErr += stSql;
        stErr += "\". ";
        ThrowDbException(ex, stErr, 24);
    }
    catch( ... )
    {
        std::string stErr = "Failed to execute the SQL statement \"";
        stErr += stSql;
        stErr += "\": An unknown error occurred.";
        throw DbException(stErr, 25);
    }
}

SQLHENV DtlDbConnection::GetEnvironmentHandle()
{
    try
    {
        return m_conn.GetHENV();
    }
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 26);
    }
    catch( RootException& ex )
    {
        ThrowDbException(ex, "Failed to get the environment handle. ", 27);
    }
    catch( ... )
    {
        throw DbException("Failed to get the environment handle: An unknown error occurred.", 28);
    }
	return 0;                         

}

SQLHDBC DtlDbConnection::GetConnectionHandle()
{
    try
    {
        return m_conn.GetHDBC();
    }
    catch( const DBException& ex )
    {
        ThrowDbException(ex, "Failed to get the connection handle. ", 29);
    }
    catch( const RootException& ex )
    {
        ThrowDbException(ex, "Failed to get the connection handle. ", 30);
    }
	catch( const std::exception& ex )
    {
        ThrowDbException(ex, "Failed to get the connection handle. ", 30);
    }
    catch( ... )
    {
        throw DbException("Failed to get the ODBC connection handle: An unknown error occurred.", 31);
    }
	return 0;                         
}

  
                                                       
 
                                        
                         
                                                                          
                                                                                  
                        
     
                                                                
                                     
                                  
                                    
     
        
     
                                     
                                      
     

                                                                                 
                                                                                  
 

                                                                                   
 
                                                                                  
                                                      
 
  
