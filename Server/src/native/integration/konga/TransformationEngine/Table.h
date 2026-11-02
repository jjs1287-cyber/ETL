/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

                                           
  
                                                                      

#if !defined(AFX_TABLE_H__B0904EB3_ECAA_4FB1_A6A7_0C82212B3B25__INCLUDED_)
#define AFX_TABLE_H__B0904EB3_ECAA_4FB1_A6A7_0C82212B3B25__INCLUDED_

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>
#include <sql.h>

#include <konga/Utils/KString.h>
#include <konga/Utils/HashTable.h>
#include "defines.h"

class CCell
{
public:
	CCell ();
	CCell (KString strName, KString strValue, SWORD mType = SQL_VARCHAR, bool bIsNull = false);
	virtual ~CCell();

public:
	bool IsNull() const { return m_bIsNull; }
	bool IsString() const;

	KString GetValue() const { return m_strValue;}
	void SetValue (const KString& strValue) { m_strValue = strValue;}

	KString GetName() const { return m_strName;}
	void SetName (const KString& strName) { m_strName = strName;}

	SWORD GetDataType() const { return m_wDataType; }
	void SetDataType(SWORD dt) { m_wDataType = dt; }

private:
	SWORD	m_wDataType;
	bool	m_bIsNull;
	KString m_strName;
	KString m_strValue;
};

class CRow  
{
public:
	CRow(){};
	virtual ~CRow()	{
		std::vector<CCell*>::iterator it = m_List.begin();
		for( ; it != m_List.end(); ++it ) {
			if ( *it ) {
				delete *it;
				*it = 0;
			}
		}
		m_List.clear();
	}

	                          
	void AddCell(CCell* poCell) { m_List.push_back(poCell);}

	size_t GetSize() const {return m_List.size();};
	const CCell* const operator[](size_t i) const {return m_List[i];}

private:
	std::vector<CCell*> m_List;

};

class CRowList  
{
public:
	CRowList(){};
	virtual ~CRowList(){
		std::vector<CRow*>::iterator it = m_List.begin();
		for( ; it != m_List.end(); ++it ){
			if ( *it ) {
				delete *it;
				*it = 0;
			}
		}
		m_List.clear();
	}

	                          
	void AddRow(CRow* poRow){
		m_List.push_back(poRow);
	}

	size_t GetSize() const {return m_List.size();};
	const CRow* const operator[](size_t i) const {return m_List[i];};

private:
	std::vector<CRow*> m_List;
};


class HashTable;

class CTable  
{
public:
	CTable();
	CTable(const char*strTableName, const int iTableOrder,
			const int ucFieldNoStart = 0, const int m_ucFieldNoEnd = 0);

	virtual ~CTable();
protected:
	               
	int m_iTableOrder;
	KString m_strTableName;
	int m_ucFieldNoStart;               
	int m_ucFieldNoEnd;                 


public:
	               
	vString m_vFieldNameList;                                 
	vString m_vPrimaryKeyList;
	MapStr2Str m_mapPKeyToValueList;                         
	CRowList m_oRowList;
	konga::HashTable<CRow*,int>* m_pRowHash;

	                 

	int GetTableOrder() const { return m_iTableOrder;}
	void SetTableOrder (const int iTableOrder) { m_iTableOrder = iTableOrder;}

	KString GetTableName() const { return m_strTableName;}
	void SetTableName (const KString& strTableName) { m_strTableName = strTableName;}

	int GetFieldNoStart() const { return m_ucFieldNoStart;}
	void SetFieldNoStart (const int ucFieldNoStart) { m_ucFieldNoStart = ucFieldNoStart;}

	int GetFieldNoEnd() const { return m_ucFieldNoEnd;}
	void SetFieldNoEnd (const int ucFieldNoEnd) { m_ucFieldNoEnd = ucFieldNoEnd;}

public:
	               
	bool IsPrimaryKey(const KString& strFieldName);
	bool IsPrimaryKeysValuesUnique(const KString & strFieldName, const KString& strCurFieldValue);
	bool IsRowUnique(CRow*);

	static unsigned int hashFunction(CRow*);
	static bool equalFunction(CRow*, CRow*);

};

#endif                                                                          
