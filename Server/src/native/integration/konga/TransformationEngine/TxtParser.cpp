/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

                
                                                                                              
                                                                                                   
                                   

#include "ErrorCodes/resource.h"
#include "TxtParser.h"
#include "DbAccess.h"
#include "TreeMapper.h"
#include "CROMData.h"

using namespace std;
using namespace konga;

#define DEBUG_HFLOOP 1


HfLoop::HfLoop(CROMData* pStartNode, KString sEndPath, KString sSearchPath)
{
	m_EndPath = sEndPath;
	m_n = 0;
	m_i = 0;

	KString sCurPath = pStartNode->GetNodePath();

	if (sCurPath == sEndPath)
	{
		m_pNodes.push_back(pStartNode);
		m_n++;
	}
	else
	{
		GatherNodes(pStartNode, sSearchPath);
		m_n = (int)m_pNodes.size();
	}
}

bool HfLoop::Remove(CROMData*p){
	for(unsigned int i=0;i<m_n;i++){
		if(m_pNodes.at(i)==p){
			m_pNodes.erase(m_pNodes.begin()+i);
			if(i<m_i)m_i--;
			m_n--;
			return true;
		}
	}
	return false;
}

void HfLoop::copy(HfLoop&from){
	clear();
	for(int i=0,n=from.m_n; i<n; i++){
		Add(from.at(i));
	}
}

int HfLoop::GatherNodes(CROMData* pStartNode, KString sSearchPath)
{
	
	KString sLevelName;
	KString sCurPath = pStartNode->GetNodePath();
	int		iInstanceNumber = 0;
	int		n = 0;

	                                                                                          
	if (pStartNode->IsNotALeaf())
	{
		if (true == pStartNode->ArePathsEqual(m_EndPath))
		{
			m_pNodes.push_back(pStartNode);
			n++;
			return n;
		}
	}

	                                                                         
	                                                                                 
	                 
	if (false == GetLevelInfo(sSearchPath, sLevelName, iInstanceNumber))
	{
		getTreeMapper()->getViewLog()->DebugString("HfLoop::GetLeveInfo() failed for %s\n", (const char*)sSearchPath);
		return 0;		
	}


	CROMData	*pChild = pStartNode->GetFirstChild();	
	while (0 != pChild)
	{
		if (pChild->IsNotALeaf() && (pChild->GetDisplayName() == sLevelName))
		{
			                                                                                                     
			if (0 == iInstanceNumber)
			{
				n += GatherNodes(pChild, sSearchPath);
				pChild = pChild->GetNextSibling();
			}
			else 
			{
				                                            
				pChild = pChild->GetSibling(iInstanceNumber);
				n += GatherNodes(pChild, sSearchPath);
				pChild = 0;
			}
		}
		else
		{
			pChild = pChild->GetNextSibling();
		}
	}
	return n;
}


CTabNameInstNum::CTabNameInstNum()
{
	m_sTableName = "";
	m_lInstance = 1;
	m_cMapType = MAP_MULTIPLE;
}

CTabNameInstNum::CTabNameInstNum(KString TableName, long lInstance)
{
	m_sTableName = TableName;
	m_lInstance = lInstance;
}

