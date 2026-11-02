/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTLDBRECORDSET_H
#define KONGA_DTLDBRECORDSET_H

#include <dtl/DTL.h>

#include <konga/Db/DbUtils/DbException.h>

#include "DtlTable.h"
#include "CustomBCA.h"

using namespace dtl;

template <typename T, typename R>
class DtlDbRecordSet                               
{
public:
    DtlDbRecordSet(DBConnection* pConn, 
                   const std::string& stTableName, 
                   T* pDataObj,
                   CustomBinder<R>& deleteBinder);
    virtual ~DtlDbRecordSet();

    void Select(const std::string& stWhereClause = "");
    int Delete();

    void AddNew(CustomBinder<R>* pInsertBinder = 0);
    void Edit();
    int Update(const R& dataRow);

    bool Next();
    bool IsEOF();

    void Close();

    std::string GetWhereClause() const;
    void SetWhereClause(const std::string& stWhereClause);
    std::string GetLastError() const;
    long GetLastErrorNumber() const;

private:
    T* m_pDataObj;
    R  m_rowbuf;

    CustomBCA<R>     m_deleteBCA;
    CustomBinder<R>* m_pInsertBinder;

    DtlTable<R> m_table;
    std::string m_stWhereClause;
};

template <typename T, typename R>
DtlDbRecordSet<T, R>::DtlDbRecordSet(DBConnection* pConn, 
                                  const std::string& stTableName, 
                                  T* pDataObj,
                                  CustomBinder<R>& deleteBinder) 
                                                : m_pDataObj(pDataObj),
                                                  m_table(pConn, stTableName, m_deleteBCA),
                                                  m_pInsertBinder(0)
{
    m_deleteBCA.SetBinder(&deleteBinder);
}

template <typename T, typename R>
DtlDbRecordSet<T, R>::~DtlDbRecordSet()
{
}

template <typename T, typename R>
bool DtlDbRecordSet<T, R>::IsEOF()
{ 
    return m_table.IsEOF();
}

template <typename T, typename R>
void DtlDbRecordSet<T, R>::Select(const std::string& stWhereClause) 
{
    if( !stWhereClause.empty() ) {
        m_stWhereClause = stWhereClause;
    }
    m_table.Select(m_stWhereClause);
}

template <typename T, typename R>
bool DtlDbRecordSet<T, R>::Next() 
{
    if( m_table.Next() ) {
        m_table.GetCurrentRow(m_rowbuf);
        m_rowbuf.SetDirty(false);
        m_pDataObj->DoFieldExchange(m_rowbuf);
        return true;
    }
    else {
        return false;
    }
}

template <typename T, typename R>
void DtlDbRecordSet<T, R>::Edit() 
{
    m_table.Edit();
}

template <typename T, typename R>
int DtlDbRecordSet<T, R>::Update(const R& dataRow) 
{
    if( !m_table.IsUpdateable() ) {
        throw konga::DbException("AddNew() or Edit() must be called before a call to Update().");
    }

    if( dataRow.IsDirty() )
    {
        if( m_table.IsAddNewMode() )
        {
            int nRows = (m_pInsertBinder == 0) ? m_table.Insert(dataRow) : m_table.Insert(dataRow, m_pInsertBinder);
            m_pInsertBinder = 0;
            return nRows;
        }
        else if( m_table.IsEditMode() )
        {
            int nRows = m_table.Update( dataRow, m_pDataObj->GetUpdateFilter() );
            if( nRows == 1 )
            {
                m_rowbuf.CopyPKs(dataRow);
            }
            return nRows;
        }
    }

    return 0;
}

template <typename T, typename R>
void DtlDbRecordSet<T, R>::AddNew(CustomBinder<R>* pInsertBinder       )
{
    m_pInsertBinder = pInsertBinder;
    R emptyRow;
    emptyRow.SetDirty(false);
    m_pDataObj->DoFieldExchange(emptyRow);
    m_table.AddNew();
}

template <typename T, typename R>
int DtlDbRecordSet<T, R>::Delete()
{
    int nRows = m_table.Delete(m_rowbuf);
    if( nRows == 1 )
    {
        R nullRow;
        m_pDataObj->DoFieldExchange(nullRow);
    }
    return nRows;
      
                                                                       
              
                                         
                 
      
               
}


template <typename T, typename R>
void DtlDbRecordSet<T, R>::Close() 
{
    m_table.Close();
}

template <typename T, typename R>
std::string DtlDbRecordSet<T, R>::GetWhereClause() const 
{
    return m_stWhereClause;
}

template <typename T, typename R>
void DtlDbRecordSet<T, R>::SetWhereClause(const std::string& stWhereClause) 
{
    m_stWhereClause = stWhereClause;
}

template <typename T, typename R>
std::string DtlDbRecordSet<T, R>::GetLastError() const 
{ 
    return m_table.GetLastError(); 
}

template <typename T, typename R>
long DtlDbRecordSet<T, R>::GetLastErrorNumber() const 
{ 
    return m_table.GetLastErrorNumber(); 
}

#endif                          


