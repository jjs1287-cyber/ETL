/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DtlDbUtils/DtlDbUtils.h>                    

#include "DtlDynamicDbRecordSet.h"

using namespace dtl;
using namespace konga;
using std::string;
using std::vector;

                                                                                   
                                      
class DynInsValidate
{
public:
    bool operator() (BoundIOs& cols, variant_row& rowbuf)
    {
        return true;
    }
};


DtlDynamicDbRecordSet::DtlDynamicDbRecordSet(DBConnection* pConn, 
											 const std::string& stSql          ) :
	m_pConnection(pConn), m_pView(0), m_pNextIterator(0),
    m_nColumnCount(0), m_bLastFieldGetWasNull(false),
	m_stSql(stSql), m_lLastError(0L)
{
}

bool DtlDynamicDbRecordSet::IsEOF()
{
	if( m_pView == 0 )
	{
		ThrowException("The recordset has not been opened.", 2);
	}
	return (m_pNextIterator == 0) ? m_iterator == m_pView->end() : *m_pNextIterator == m_pView->end();
}

bool DtlDynamicDbRecordSet::Next()
{
	if( m_pView == 0 )
	{
		ThrowException("The recordset has not been opened.", 3);
	}
	if( m_pNextIterator == 0 )
	{
		m_pNextIterator = &m_iterator;
	}
	else if( *m_pNextIterator == m_pView->end() )
	{
		return false;
	}
    else
    {
        (*m_pNextIterator)++;
    }
	if( *m_pNextIterator == m_pView->end() )
	{
		return false;
	}
	
	variant_row r = **m_pNextIterator;
	m_vstColumnValues.clear();
	m_vbNullColumns.clear();
	for( unsigned int i = 0; i < r.size(); ++i )
	{
		const variant_field f = r[i];
		                                                                                 
		                                                       
		if ( f.is_type<double>() ) {
			const double dVal = (double)f;
			m_vstColumnValues.push_back(toString(dVal));
		}
		else {
			m_vstColumnValues.push_back(f.get_string());

			                                                                 
			                                                      
			  
                            
                      
                                          
         
                                         
                     
    
     
		}
		m_vbNullColumns.push_back(r[i].IsNull());
	}
	
    return true;
}

void DtlDynamicDbRecordSet::Reset()
{
	delete m_pView;
	m_pView = 0;
	m_pNextIterator = 0;
	m_vstColumnNames.clear();
	m_vlColumnTypes.clear();
	m_vstColumnValues.clear();
	m_vbNullColumns.clear();
	m_nColumnCount = 0;
}

void DtlDynamicDbRecordSet::Close()
{
	Reset();
}

void DtlDynamicDbRecordSet::Open()
{
	try
	{
		Reset();

		m_pView = new DynamicDBView<>( DynamicDBView<>::Args().tables(GetSqlStatement()).conn(*m_pConnection).InsValidate(DynInsValidate()) );
		m_iterator = m_pView->begin();

                                             
        for( size_t i = 0; i < m_iterator.GetBoundParamCount(); ++i )
        {
                                                                
            std::map<unsigned int, bool>::iterator itNull = m_mapBoundNulls.find(i);
            if( itNull != m_mapBoundNulls.end() && itNull->second )
            {
                m_iterator.Params()[i].SetNull();
            }
            else
            {
                std::map<unsigned int, std::string>::iterator it = m_mapBoundParameters.find(i);
                if( it == m_mapBoundParameters.end() )
                {
                                                                                              
                                                                                                      
                                                  
                }
                else
                {
                    m_iterator.Params()[i].ClearNull();
                    m_iterator.Params()[i] = it->second;
                    std::cout << "param: " << it->second << std::endl;
                }
            }
        }

                                                                               
        m_iterator = m_pView->GetDataObj();

		variant_row row = m_iterator;
		vector<string> vstNames = row.GetNames();
		std::vector<TypeTranslation> vTypes = row.GetTypes();
		m_nColumnCount = vstNames.size();
		for( unsigned int i = 0; i < m_nColumnCount; ++i )
		{
			m_vstColumnNames.push_back(vstNames[i]);
			TypeTranslation tt = vTypes[i];
			m_vlColumnTypes.push_back(tt.sqlType);
		}
	}
	catch( RootException& e )
	{
        std::string stErr = (e.what() != 0) ? std::string(e.what()) : e.errmsg;
		ThrowException(e.what(), 1);
	}
}

int DtlDynamicDbRecordSet::GetFieldCount()
{
	if( m_pView == 0 )
	{
		ThrowException("The recordset has not been opened.", 9);
	}
	return m_nColumnCount;
}

std::string DtlDynamicDbRecordSet::GetFieldValue(unsigned int nColIndex)
{
	if( m_pView == 0 )
	{
		ThrowException("The recordset has not been opened.", 4);
	}
	if( m_pNextIterator == 0 )
	{
		ThrowException("Next() must be called before a column value can be retrieved.", 5);
	}
	                             
	m_bLastFieldGetWasNull = m_vbNullColumns[nColIndex];
	                                                                                                     
	                                                    
	if ( m_bLastFieldGetWasNull ) return std::string();
	return m_vstColumnValues[nColIndex];
}

TIMESTAMP_STRUCT DtlDynamicDbRecordSet::GetFieldValueAsTimeStampStruct(unsigned int nColIndex)
{
	if( m_pView == 0 )
	{
		ThrowException("The recordset has not been opened.", 4);
	}
	if( m_pNextIterator == 0 )
	{
		ThrowException("Next() must be called before a column value can be retrieved.", 5);
	}
	
	variant_row r = **m_pNextIterator;
	const variant_field f = r[nColIndex];
	return (TIMESTAMP_STRUCT)f;
}

bool DtlDynamicDbRecordSet::IsFieldNull(unsigned int nColIndex)
{
    if( m_pView == 0 )
    {
        ThrowException("The recordset has not been opened.", 11);
    }
    if( m_pNextIterator == 0 )
    {
        ThrowException("Next() must be called before a column value can be retrieved.", 12);
    }
                                 
    return m_vbNullColumns[nColIndex];
}

FieldInfo DtlDynamicDbRecordSet::GetFieldInfo(unsigned int nColIndex)
{
	if( m_pView == 0 )
	{
		throw DbException("The recordset has not been opened.", 7);
	}
	                             
	FieldInfo fi;
	fi.Name = m_vstColumnNames[nColIndex];
	fi.SqlType = m_vlColumnTypes[nColIndex];	
	return fi;
}

unsigned int DtlDynamicDbRecordSet::GetFieldIndexByName(const std::string& stName)
{
	if( m_pView == 0 )
		throw DbException("The recordset has not been opened.", 8);
	for( unsigned int index = 0; index < m_nColumnCount; ++index )
	{
        if( EqualsIgnoreCase(stName, m_vstColumnNames[index]) )
            return index;
	}
	throw DbException("The name " + stName + " is not in the set of column names.", 10);
}

void DtlDynamicDbRecordSet::ClearParameters()
{
    m_mapBoundParameters.clear();
    m_mapBoundNulls.clear();
}

void DtlDynamicDbRecordSet::BindString(unsigned int nParamIndex, const std::string& stVal)
{
    m_mapBoundParameters[nParamIndex] = stVal;
    m_mapBoundNulls[nParamIndex] = false;
}

void DtlDynamicDbRecordSet::BindInteger(unsigned int nParamIndex, int iVal)
{
    m_mapBoundParameters[nParamIndex] = toString(iVal);
    m_mapBoundNulls[nParamIndex] = false;
}

void DtlDynamicDbRecordSet::BindLong(unsigned int nParamIndex, long long lVal)
{
    m_mapBoundParameters[nParamIndex] = toString(lVal);
    m_mapBoundNulls[nParamIndex] = false;
}

namespace {
	string GetTimeStampString(const TIMESTAMP_STRUCT& ts) {
		string s = toString( static_cast<int>(ts.year) ) + "-";
		if( ts.month < 10 )
			s += "0";
		s += toString( static_cast<int>(ts.month) ) + "-";
		if( ts.day < 10 )
			s += "0";
		s += toString( static_cast<int>(ts.day) ) + " ";
		if( ts.hour < 10 )
			s += "0";
		s += toString( static_cast<int>(ts.hour) ) + ":";
		if( ts.minute < 10 )
			s += "0";
		s += toString( static_cast<int>(ts.minute) ) + ":";
		if( ts.second < 10 )
			s += "0";
		s += toString( static_cast<int>(ts.second) ) + ".";
		s += toString( static_cast<int>(ts.fraction) );
		return s;
	}
}

void DtlDynamicDbRecordSet::BindTimestamp(unsigned int nParamIndex, const TIMESTAMP_STRUCT &tsVal)
{
    m_mapBoundParameters[nParamIndex] = GetTimeStampString(tsVal);
    m_mapBoundNulls[nParamIndex] = false;
}

void DtlDynamicDbRecordSet::BindNull(unsigned int nParamIndex)
{
    m_mapBoundNulls[nParamIndex] = true;
}


