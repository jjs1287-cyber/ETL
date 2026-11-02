/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Utils/HashTable.h>
#include <konga/Utils/KString.h>

#include <konga/DataElement/DataElement.h>	                 
#include <konga/TransformationLogging/ViewLog.h>

#include "defines.h"
#include "Table.h"

using namespace std;
using namespace konga;

CCell::CCell()
	: m_wDataType(SQL_VARCHAR), m_bIsNull(false) { }

CCell::CCell(KString strName, KString strValue, SWORD mType                , bool bIsNull) 
				: m_wDataType(mType), m_bIsNull(bIsNull), m_strName(strName), m_strValue(strValue) { }

bool CCell::IsString() const
{
	if (m_wDataType == SQL_VARCHAR || m_wDataType == SQL_CHAR)
		return true;
	else
		return false;
}

CCell::~CCell() {
}


                                     

                                        

unsigned int CTable::hashFunction(CRow*pRowInfo)
{
	unsigned int uHashVal = 0;

	                                                                
	if (0 != pRowInfo)
	{
		int iRowSize = (int)pRowInfo->GetSize();
		for ( int iRowCounter = 0; iRowCounter < iRowSize; iRowCounter++)
		{
			const CCell* pCell = (*pRowInfo)[iRowCounter];
			
			if (0 != pCell)
			{
				KString sCellVal;
				sCellVal = pCell->GetValue();
				int iValLen = (int)sCellVal.GetLength();
				for (int iCellValueCounter = 0; iCellValueCounter < iValLen; iCellValueCounter++)
				{
					uHashVal = (uHashVal*128 + toupper(sCellVal[iCellValueCounter])) % HASHTABLE_SIZE;
				}
			}
		}
	}

	return uHashVal;
}

bool CTable::equalFunction(CRow* pRowInfo1, CRow* pRowInfo2)
{
	                                                     
	if (0 != pRowInfo1 && 0 != pRowInfo2)
	{
		                                                                                            
		if (pRowInfo1->GetSize() != pRowInfo2->GetSize())
			return false;

		                                                                    
		int iNumOfCols = (int)pRowInfo1->GetSize();
		for (int iColCounter = 0; iColCounter < iNumOfCols; iColCounter++)
		{
			const CCell* const pCell1 = (*pRowInfo1)[iColCounter], *pCell2 = (*pRowInfo2)[iColCounter];
			if (0 != pCell1 && 0 != pCell2)
			{
				                                                                    
				if (pCell1->GetValue() != pCell2->GetValue())
					return false;
			}
		}

		                                                                              
		return true;
	}

	return false;
}

CTable::CTable()
{
	m_iTableOrder			= 0;
	m_strTableName			= "";
	m_ucFieldNoStart		= 0;
	m_ucFieldNoEnd			= 0;
	m_pRowHash = new konga::HashTable<CRow*,int>(HASHTABLE_SIZE, CTable::hashFunction, CTable::equalFunction);
	
}

CTable::CTable(const char* strTableName, const int iTableOrder,
			const int ucFieldNoStart        , const int ucFieldNoEnd        )
			: m_iTableOrder(iTableOrder),  m_ucFieldNoStart(ucFieldNoStart), m_ucFieldNoEnd(ucFieldNoEnd)
{
	m_strTableName=KString(strTableName);
	m_pRowHash = new konga::HashTable<CRow*,int>(HASHTABLE_SIZE, CTable::hashFunction, CTable::equalFunction);
}


CTable::~CTable()
{
	  
                          
                           
                              
                        
   	
	delete m_pRowHash;
}


                                      
                     
                                                                                  
                                                                                                      
                                                       
bool CTable::IsRowUnique(CRow* pRow)
{
	                                                                                  
	if (m_pRowHash->get(pRow) == -1)
	{
		                                                                             
		m_pRowHash->put(pRow, 0);

		return true;
	}
	
	                                                                                           
	return false;
}

bool CTable::IsPrimaryKey (const KString &strFieldName)
{
	itvString it;
	for (it = m_vPrimaryKeyList.begin(); it!= m_vPrimaryKeyList.end (); it++)
	{
		string sFieldName, sKeyName;
		sFieldName = string(strFieldName);
		sKeyName = (*it).c_str();
		if (sFieldName == sKeyName)
			return true;
	}
	return false;
}


                                                    
                                                                                                            
                                                                                                 
                                                        
                       
                                                                                                       
                         
                                                     
bool CTable::IsPrimaryKeysValuesUnique(const KString & strFieldName, const KString& strCurFieldValue)
{
	                                                            
	bool bIsNewFieldValue = true;

	                                                                                                      
	if (m_mapPKeyToValueList.find(strFieldName) != m_mapPKeyToValueList.end())
	{
		                                                                                     
		MapStr2StrIt lb = m_mapPKeyToValueList.lower_bound(strFieldName);
		MapStr2StrIt ub = m_mapPKeyToValueList.upper_bound(strFieldName);
		KString strLastValue;

		                                                               
		for (MapStr2StrIt it = lb; it != ub;  ++it)
		{
			strLastValue = (*it).second;

			                                                                             
			                                      
			if(strCurFieldValue == strLastValue)                     
			{
				bIsNewFieldValue = false;
				break;
			}
		}
	}
	
	                                                                                           
	                           
	if (true == bIsNewFieldValue)
	{
		m_mapPKeyToValueList.insert(pair<KString, KString>(strFieldName,strCurFieldValue));
	}

	return bIsNewFieldValue;
}

                                    
