/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_DTLDYNAMICDBRECORDSET_H
#define KONGA_DB_DTLDYNAMICDBRECORDSET_H

#include <string>
#include <vector>
#include <map>

#include <dtl/DTL.h>

#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/DbException.h>

                       
   
      
   
   

class DtlDynamicDbRecordSet : public konga::DynamicDbRecordSet
{
public:
	DtlDynamicDbRecordSet(dtl::DBConnection* pConn, const std::string& stSql = "");
	virtual ~DtlDynamicDbRecordSet() { Close(); }

	bool IsEOF();
	bool Next();
    void Open();
	void Close();

	int GetFieldCount();

    std::string GetFieldValue(unsigned int nColIndex);
	TIMESTAMP_STRUCT GetFieldValueAsTimeStampStruct(unsigned int nColIndex);

    bool IsFieldNull(unsigned int nColIndex);

    konga::FieldInfo GetFieldInfo(unsigned int nColIndex);

    unsigned int GetFieldIndexByName(const std::string& stName);

    void SetSqlStatement(const std::string& stSql) {
        m_stSql = stSql;
    }

    std::string GetSqlStatement() const {
        return m_stSql;
    }

    void ClearParameters();
    void BindString(unsigned int nParamIndex, const std::string& stVal);
    void BindInteger(unsigned int nParamIndex, int iVal);
    void BindLong(unsigned int nParamIndex, long long lVal);
    void BindTimestamp(unsigned int nParamIndex, const TIMESTAMP_STRUCT &tsVal);
    void BindNull(unsigned int nParamIndex);

    std::string GetLastError() const {
        return m_stLastError;
    }
    long GetLastErrorNumber() const {
        return m_lLastError;
    }


private:
    void Reset();
    void ThrowException(const std::string& stErr, long lErr) {
        m_stLastError += stErr+"\n";
        m_lLastError = lErr;
        throw konga::DbException(stErr, lErr);
    }

	dtl::DBConnection*					m_pConnection;
	dtl::DynamicDBView<>*				m_pView;
	dtl::DynamicDBView<>::sql_iterator*	m_pNextIterator;
	dtl::DynamicDBView<>::sql_iterator	m_iterator;
	
	unsigned int				m_nColumnCount;
	std::vector<std::string>	m_vstColumnNames;
	std::vector<long>			m_vlColumnTypes;
	std::vector<std::string>	m_vstColumnValues;
	std::vector<bool>			m_vbNullColumns;
	
	bool				m_bLastFieldGetWasNull;

    std::string	m_stSql;

    std::map<unsigned int, std::string> m_mapBoundParameters;
    std::map<unsigned int, bool>        m_mapBoundNulls;

    std::string	m_stLastError;
    long		m_lLastError;
};


#endif                                    
