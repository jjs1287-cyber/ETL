/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef EW_DTLTABLE_H
#define EW_DTLTABLE_H

#include <string>
#include <vector>
#include <dtl/DTL.h>

#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbException.h>

#include "CustomBCA.h"

   
                          
               
                     
                        
                           
                           
   
   
   
   

 using namespace dtl;

template <typename T>
class DtlTable {
public:

	enum QueryType { QT_NONE, QT_SELECT, QT_DELETE, QT_INSERT, QT_UPDATE };

	DtlTable(DBConnection* pConn, const std::string& stTableName, CustomBCA<T>& deleteBCA);
	virtual ~DtlTable();
	
	std::string	GetLastError() const;
	long		GetLastErrorNumber() const;
	
	bool IsEOF();
	void Close();
    void Edit();
    void AddNew();

	void Select(const std::string& stFilter = "");

    int Update(const T& newValues, const std::string& stWhereClause);
    int Update(const std::string& stSql);

    int Insert(const std::string& stSql);
    int Insert(const T& newRow);
    int Insert(const T& newRow, CustomBinder<T>* pBinder);

    int Delete(T& row);
    int Delete(const std::string& stFilter);

	bool Next();
	T GetCurrentRow();
    void GetCurrentRow(T& row);

    bool IsUpdateable() const {
        return IsEditMode() || IsAddNewMode();
    }
    bool IsEditMode() const {
        return m_bEditMode;
    }
    bool IsAddNewMode() const {
        return m_bAddNewMode;
    }

protected:
	void SetLastError(std::string stErr) { m_stLastError = stErr; }
	void SetLastErrorNumber(long lErr) { m_lLastError = lErr; }

		
private:
    void ThrowDbException(const std::string& stErr, long lErr);
    void ThrowDbException(const DBException& ex, long lErr);

	DBConnection*	m_pConn;
	std::string		m_stTableName;
    std::string     m_stPrimaryKeyList;

    CustomBCA<T>&   m_deleteBCA;

	QueryType		m_currentQueryType;

	std::vector<T>	m_selectedRows;
	int				m_iCurrentSelectIndex;

    bool            m_bEditMode;
    bool            m_bAddNewMode;

	std::string		m_stLastError;
	long			m_lLastError;
};


template <typename T>
DtlTable<T>::DtlTable(DBConnection* pConn, const std::string& stTableName,
					  CustomBCA<T>& deleteBCA) :
   				m_pConn(pConn), 
				m_stTableName(stTableName),
                m_deleteBCA(deleteBCA),
				m_currentQueryType(QT_NONE),
				m_iCurrentSelectIndex(-1),
                m_bEditMode(false),
                m_bAddNewMode(false),
				m_lLastError(0L)
{
}

template <typename T>
DtlTable<T>::~DtlTable()
{
}

template <typename T>
std::string DtlTable<T>::GetLastError() const
{
    return m_stLastError;
}

template <typename T>
long DtlTable<T>::GetLastErrorNumber() const
{
    return m_lLastError;
}

template <typename T>
bool DtlTable<T>::IsEOF()
{
    switch( m_currentQueryType ) 
    {
        case QT_NONE:
            ThrowDbException("No query is associated with this object.", 5);
        case QT_SELECT:
            return m_selectedRows.empty();
        default:
            ThrowDbException("Unsupported query.", 6);
    }
}

template <typename T>
void DtlTable<T>::Close()
{
    m_selectedRows.clear();
    m_iCurrentSelectIndex = -1;
    m_currentQueryType = QT_NONE;
}

template <typename T>
void DtlTable<T>::Select(const std::string& stFilter)
{
    Close();
    try 
    {
                                                                  
        DBView<T> view(
            typename DBView<T>::Args().
                tables(m_stTableName).
                conn(*m_pConn).
                postfix(stFilter));
        typename DBView<T>::select_iterator read_it = view.begin();
        for( ; read_it != view.end(); ++read_it ) {
            m_selectedRows.push_back(*read_it);
        };
        m_currentQueryType = QT_SELECT;
                                                                                             
    }
    catch( DBException& ex )
    {
        ThrowDbException(ex, 1006);
    }
    catch( RootException& ex ) 
    {
        std::string stErr = "QT_SELECT failed. Reported error: ";
        stErr += ex.what();
        ThrowDbException(stErr, 1001);
    }
}

template <typename T>
bool DtlTable<T>::Next()
{
    switch( m_currentQueryType ) 
    {
        case QT_NONE:
            ThrowDbException("No query is associated with this object.", 1);
        case QT_SELECT:
                                                                                   
            m_bEditMode = false;
            m_bAddNewMode = false;
            return ++m_iCurrentSelectIndex < static_cast<int>(m_selectedRows.size());
        default:
            ThrowDbException("Unsupported query.", 4);
    }
}

template <typename T>
T DtlTable<T>::GetCurrentRow()
{
    switch( m_currentQueryType ) 
    {
        case QT_NONE:
            ThrowDbException("No query is associated with this object.", 2);
        case QT_SELECT:
            if( m_iCurrentSelectIndex < 0 ) {
                ThrowDbException("Next() has not been called after executing the QT_SELECT query.", 1002);
            }
            if( m_iCurrentSelectIndex >= static_cast<int>(m_selectedRows.size()) ) {
                ThrowDbException("No more rows available in the result set.", 1003);
            }
            return m_selectedRows[m_iCurrentSelectIndex];
        default:
            ThrowDbException("Unsupported query.", 3);
    }
}

template <typename T>
void DtlTable<T>::GetCurrentRow(T& row)
{
    switch( m_currentQueryType ) 
    {
        case QT_NONE:
            ThrowDbException("No query is associated with this object.", 7);
        case QT_SELECT:
            if( m_iCurrentSelectIndex < 0 ) {
                ThrowDbException("Next() has not been called after executing the QT_SELECT query.", 1004);
            }
            if( m_iCurrentSelectIndex >= static_cast<int>(m_selectedRows.size()) ) {
                ThrowDbException("No more rows available in the result set.", 1005);
            }
            row = m_selectedRows[m_iCurrentSelectIndex];
            return;
        default:
            ThrowDbException("Unsupported query.", 8);
    }
}


template <typename T>
void DtlTable<T>::Edit()
{
    m_bEditMode = true;
    m_bAddNewMode = false;
}

template <typename T>
void DtlTable<T>::AddNew()
{
    m_bAddNewMode = true;
    m_bEditMode = false;
}

template <typename T>
int DtlTable<T>::Update(const T& newValues, const std::string& stWhereClause)
{
    try 
    {
        if( IsEditMode() )
        {
                                                      
                                                                                    
            DBView<T> view(
                typename DBView<T>::Args().
                    tables(m_stTableName).
                    conn(*m_pConn).
                    postfix(stWhereClause));

            typename DBView<T>::update_iterator updater = view;
            *updater = newValues;
            updater++;
                                             
            m_bEditMode = false;
            return updater.GetLastCount();
        }
        else if( IsAddNewMode() )
        {
                                                     
            DBView<T> view(
                typename DBView<T>::Args().
                    tables(m_stTableName).
                    conn(*m_pConn));
            typename DBView<T>::insert_iterator inserter = view;
            *inserter = newValues;
            inserter++;
            return inserter.GetLastCount();
        }
    }
    catch( DBException& ex )
    {
        long lErr = m_bEditMode ? 3006 : 2003;
        m_bEditMode = false;
        m_bAddNewMode = false;
        ThrowDbException(ex, lErr);
    }
    catch( RootException& ex ) 
    {
        m_bEditMode = false;
        m_bAddNewMode = false;
        std::string stErr = "QT_UPDATE failed. Reported error: ";
        stErr += ex.what();
        ThrowDbException(stErr, 3002);
    }

    ThrowDbException("Edit() or AddNew() must be called before a call to Update().", 3001);
}


template <typename T>
int DtlTable<T>::Update(const std::string& stSql)
{
    if( IsEditMode() )
    {
        try
        {
            DynamicDBView<> view(
                DynamicDBView<>::Args().
                    tables(stSql).
                    conn(*m_pConn));

            DynamicDBView<>::sql_iterator sql_it(view);
                                                                                   
            *sql_it = view.GetDataObj();
            return 1;
        }
        catch( DBException& ex )
        {
            ThrowDbException(ex, 3005);
        }
        catch( RootException& e )
        {
            std::string stErr = "UPDATE failed. Reported error: ";
            stErr += e.what();
            ThrowDbException(stErr, 3003);
        }
    }
    ThrowDbException("Edit() must be called before a call to Update().", 3004);
}

template <typename T>
int DtlTable<T>::Delete(T& row)
{
                                                                            
    if( m_currentQueryType != QT_SELECT && m_currentQueryType != QT_UPDATE )
    {
        ThrowDbException("Select() or Update() must be called before Delete() can be called.", 4005);
    }

    if( m_iCurrentSelectIndex < 0 )
    {
        ThrowDbException("Next() must be called before a call to Delete().", 4006);
    }
    if( m_iCurrentSelectIndex >= static_cast<int>(m_selectedRows.size()) ) {
        ThrowDbException("The recordset has advanced passed the last row. There is nothing to delete.", 4007);
    }
    DBView<T> view( typename DBView<T>::Args().tables(m_stTableName).bca(m_deleteBCA).conn(*m_pConn) );
    typename DBView<T>::delete_iterator deleter = view;
    *deleter = row;
    deleter++;
    return deleter.GetLastCount();
}

template <typename T>
int DtlTable<T>::Delete(const std::string& stFilter)
{
    if( m_currentQueryType != QT_SELECT && m_currentQueryType != QT_UPDATE )
    {
        ThrowDbException("Select() or Update() must be called before Delete() can be called.", 4002);
    }

    if( m_iCurrentSelectIndex < 0 )
    {
        ThrowDbException("Next() must be called before a call to Delete().", 4003);
    }
    if( m_iCurrentSelectIndex >= static_cast<int>(m_selectedRows.size()) ) {
        ThrowDbException("The recordset has advanced passed the last row. There is nothing to delete.", 4004);
    }

    try 
    {
                                                                  
        std::string stSql = "QT_DELETE FROM " + m_stTableName + " " + stFilter;
        DynamicDBView<> view(
            DynamicDBView<>::Args().
                tables(stSql).
                conn(*m_pConn));

        DynamicDBView<>::sql_iterator sql_it(view);
                                                                                  
        *sql_it = view.GetDataObj();
        return 1;
    }
    catch( DBException& ex ) 
    {
        ThrowDbException(ex, 4008);
    }
    catch( RootException& ex ) 
    {
        m_bEditMode = false;
        std::string stErr = "QT_DELETE failed. Reported error: ";
        stErr += ex.what();
        ThrowDbException(stErr, 4001);
    }
}

template <typename T>
int DtlTable<T>::Insert(const std::string& stSql)
{
    if( IsAddNewMode() )
    {
        try
        {
            DBStmt stmt(stSql, *m_pConn);
            stmt.Execute();
            int nRowCount = stmt.RowCount();
            std::cout << "inserted " << nRowCount << " row(s)" << std::endl;
            return nRowCount;
        }
        catch( DBException& e )
        {
            ThrowDbException(e, 2004);
        }
        catch( RootException& e )
        {
            std::string stErr = "INSERT failed. Reported error: ";
            stErr += e.what();
            ThrowDbException(stErr, 2002);
        }
    }

    ThrowDbException("AddNew() must be called before a call to Insert()", 2001);
}

template <typename T>
int DtlTable<T>::Insert(const T& newRow)
{
    if( IsAddNewMode() )
    {
        try
        {
            DBView<T> view( typename DBView<T>::Args().tables(m_stTableName).conn(*m_pConn) );
            typename DBView<T>::insert_iterator inserter = view;
            *inserter = newRow;
            return 1;
        }
        catch( DBException& e )
        {
            ThrowDbException(e, 2006);
        }
        catch( RootException& e )
        {
            std::string stErr = "INSERT failed. Reported error: ";
            stErr += e.what();
            ThrowDbException(stErr, 2007);
        }
    }
    ThrowDbException("AddNew() must be called before a call to Insert()", 2005);
}

template <typename T>
int DtlTable<T>::Insert(const T& newRow, CustomBinder<T>* pBinder)
{
    if( IsAddNewMode() )
    {
        try
        {
            CustomBCA<T> insertBCA(pBinder);
            DBView<T> view( typename DBView<T>::Args().tables(m_stTableName).conn(*m_pConn).bca(insertBCA) );
            typename DBView<T>::insert_iterator inserter = view;
            *inserter = newRow;
            return 1;
        }
        catch( DBException& e )
        {
            ThrowDbException(e, 2006);
        }
        catch( RootException& e )
        {
            std::string stErr = "INSERT failed. Reported error: ";
            stErr += e.what();
            ThrowDbException(stErr, 2007);
        }
    }
    ThrowDbException("AddNew() must be called before a call to Insert()", 2005);
}

template <typename T>
void DtlTable<T>::ThrowDbException(const std::string& stErr, long lErr)
{
    m_stLastError += stErr+"\n";
    m_lLastError = lErr;
    throw konga::DbException(stErr, lErr);
}

template <typename T>
void DtlTable<T>::ThrowDbException(const DBException& ex, long lErr)
{
    std::string stSqlState, stSqlErrMsg;
    long lNativeErr = 0L;
    std::vector<DBException::ODBCErrorInfo> vInfo = ex.GetAllODBCErrors();
                                                                                  
    if( !vInfo.empty() )
    {
        DBException::ODBCErrorInfo info = vInfo[vInfo.size()-1];
        stSqlState = info.szSQLState;
        stSqlErrMsg = info.errMsg;
        lNativeErr = info.nativeErr;
    }
    else
    {
        stSqlState = "*NOT DEFINED*";
        stSqlErrMsg = "*NOT DEFINED*";
    }

    m_stLastError += (ex.what() != 0) ? std::string(ex.what()) : ex.errmsg;
	m_stLastError += "\n";
    m_lLastError = lErr;
    throw konga::DbException(m_stLastError, stSqlState, stSqlErrMsg, lNativeErr, lErr);
}

#endif                 
