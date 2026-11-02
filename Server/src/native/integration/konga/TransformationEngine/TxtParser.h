/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef _INFORMATIONFILE_H_
#define _INFORMATIONFILE_H_

#include <konga/Utils/StringTokenizer.h>

#include "misc.h"

class CROMData;

#define MAP_MULTIPLE '.'

#define DECL_VARCHAR "varchar"
#define TABLE_HEADER "<Header>"
#define DECL_FUNCTION "<!function>"
#define DECL_NULL "<0>"

#define POST_PROCESS_IDENTITY "<SQLIDENTITY>"
#define POST_PROCESS_SEQUENCE "<SEQUENCE>"
#define POST_PROCESS_USERDEFINED_SEQUENCE "<UDSEQUENCE>"
#define USER_DEFINED_FUNCTION "<UDF>"

#define DEFAULT_ODBCTIMEOUT		(5*60)

#define CHUNK_SIZE		4096
#define SQLERR(retcode) (retcode == SQL_ERROR)

class TreeMapper;

class CColValType {
public:
	CColValType(void) : m_iOrdinal(0), m_iColType(0), m_iCharSize(0) {}
	explicit CColValType(const KString& sColVal) : m_iOrdinal(0), m_iColType(0), m_iCharSize(0), m_sColVal(sColVal) {}
	~CColValType(void) {}

public:
	int m_iOrdinal;
	int m_iColType;
	int m_iCharSize;
	KString m_sColVal;
};

class CTabNameInstNum
{
public:
	CTabNameInstNum();
	CTabNameInstNum(KString TableName, long lInstance = 1);
	KString m_sTableName;
	long	m_lInstance;
	char	m_cMapType;
};

class HfLoop
{
	std::vector<CROMData*>m_pNodes;
	TreeMapper*m_pTreeMapper;

public:
	unsigned int m_n,m_i;
	KString m_EndPath;
	HfLoop(TreeMapper*p){m_n=0;m_i=0;}
	HfLoop(CROMData* pStartNode, KString sEndPath, KString sSearchPath);

	TreeMapper*getTreeMapper(){return m_pTreeMapper;};

	int GatherNodes(CROMData* pStartNode, KString sEndPath);
	CROMData*NextNode()
	{
		if(m_i<m_n&&m_i>=0)return m_pNodes[m_i++];
		return 0;
	}
	CROMData*GetAt(int i){
		if(i>=0 && i<(int)m_n)
			return m_pNodes[i];
		else
			return 0;
	}
	CROMData*at(int i){return m_pNodes.at(i);}
	void RemoveAt(int i){
		if(i>=0 && i<(int)m_n){
			m_pNodes.erase(m_pNodes.begin()+i);
			m_n--;
		}
	}
	size_t GetSize(){return m_pNodes.size();}
	size_t size(){return m_pNodes.size();}
	bool Remove(CROMData*p);
	void Add(CROMData*data){m_pNodes.push_back(data); m_n++;}
	void push_back(CROMData*data){m_pNodes.push_back(data); m_n++;}
	int gatherLoopNode(CROMData*parent,KString&name,int n_instance=0);
	void copy(HfLoop&from);
	void clear(){m_pNodes.clear(); m_n=0;}
};

#endif                       

