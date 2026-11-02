/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef CRXmlList_H
#define CRXmlList_H

#include <string>

#include "CRXmlData.h"

                                             
class CRXmlList
{
	friend class CRXmlData;
protected:
#ifdef _DEBUG
	std::string m_tag;
#endif
	CRXmlData*m_start,*m_end,*m_current;
	CRXmlData::Type m_type;
	CRXmlData*m_parent;
	FILE*m_LOG;
public:
	CRXmlList(CRXmlData*parent, const std::string&header,FILE*LOG=0);
	CRXmlData*getParent(){return m_parent;}
	~CRXmlList();
	                                                    
	bool add(CRXmlData*p,const std::string&header);
	void remove(CRXmlData*child);
	void deleteList();
	CRXmlData*start();
	CRXmlData*next();
	CRXmlData*last();
	std::string getXmlString(bool att);
	bool hasValue();
	bool hasSubElements();
	std::string getDisplayString();
	std::string getTagString(){
#ifdef _DEBUG
		return m_tag;
#else
		return std::string();
#endif
	}
#ifdef _DEBUG
	int getLabel();
#endif
	bool collectInstance(std::vector<CRXmlData*>*ar,const char*endPath);
};
#endif              

