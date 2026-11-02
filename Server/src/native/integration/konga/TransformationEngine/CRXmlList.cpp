/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include "CRXmlData.h"
#include "CRXmlList.h"
#include "CROMData.h"

                               
CRXmlList::CRXmlList(CRXmlData*parent,const std::string&header,FILE*LOG): m_parent(parent)
{
	assert(parent!=0);
	assert(parent->m_list==0);
	m_LOG=LOG;
#ifdef _DEBUG
	m_tag=std::string("L")+konga::toString(getLabel())+"_"+CRXmlData::typeStr[parent->m_type];
#endif
	if(LOG)fprintf(LOG,"%s%s = new CRXmlList(parent=%s)\n",header.c_str(), getTagString().c_str(), m_parent->getTagString().c_str());
	m_type=parent->m_type;
	m_start=m_end=m_current=0;
	m_parent=parent;
	parent->m_list=this;
}

void CRXmlList::remove(CRXmlData*child){
	if(child==0)return;
	if(m_start==child){
		if(m_end==child){
			m_start=0;
			m_end=0;
		}
		else
			m_start=m_start->m_next;
		delete child;
		return;
	}
	for(CRXmlData*e0=m_start,*e=next();e;e0=e,e=next()){
		if(e==child){
			if(child==m_end){
				m_end=e0;
				e0->m_next=0;
			}
			else
				e0->m_next=e->m_next;
			delete child;
			return;
		}
	}
}

#ifdef _DEBUG
int CRXmlList::getLabel()
{
	if(m_parent==0)return 0;
	return CRXmlData::getLabel();
}
#endif


std::string CRXmlList::getXmlString(bool att)
{
	std::string str;
	for(CRXmlData*data=start();data;data=next()){
		CRXmlData::Type type=data->getType();
		switch(type){
		case CRXmlData::t_attribute:
			if(att)str+=data->getXmlString();
			break;
		case CRXmlData::t_instance_list:
			if(data->getList()->start()==0)break;
		default:
			if(!att)str+=data->getXmlString();
			break;
		}
	}
	return str;
}

CRXmlList::~CRXmlList(){
	deleteList();
}

void CRXmlList::deleteList(){
	CRXmlData*data=start(),*next_data;
	while(data){
		next_data=next();
		data->deleteList();
		delete data;
		data=next_data;
	}
	m_start=m_end=0;
}

std::string CRXmlList::getDisplayString(){
	std::string list_str,parent_str("null");
	if(m_parent)parent_str=m_parent->getTagString();
	if(m_end==0);
	else if(m_start==m_end)
		list_str=m_start->getTagString();
	else if(m_start->m_next==m_end)
		list_str=m_start->getTagString()+","+m_end->getTagString();
	else
		list_str=m_start->getTagString()+",...,"+m_end->getTagString();
	return getTagString()+":parent="+parent_str+",list={"+list_str+"}";
}

                                                               
                                        
                              
                     
                                                
                   
                                 
                                
               
   

bool CRXmlList::hasSubElements(){
	for(CRXmlData*data=start();data;data=next()){
		CRXmlData::Type type=data->getType();
		if(type!=CRXmlData::t_attribute){
			if(type==CRXmlData::t_instance_list){
				if(data->getList()->start()==0)continue;
			}
			return true;
		}
	}
	return false;
}


bool CRXmlList::add(CRXmlData*p,const std::string&header){
	if(m_end){
		m_end->m_next=p;
		p->m_instanceNumber=m_end->m_instanceNumber+1;
	}
	else {
		m_start=p;
		p->m_instanceNumber=0;
	}
	m_end=p;
	return true;
}
CRXmlData*CRXmlList::start(){
	return m_current=m_start;
}

CRXmlData*CRXmlList::next(){
	if(m_current==m_end)return 0;
	if(m_current)m_current=m_current->m_next;
	return m_current;
}

CRXmlData*CRXmlList::last(){
	return m_end;
}

bool CRXmlList::collectInstance(std::vector<CRXmlData*>*ar,const char*endPath){
	bool bHasInstance=false;
	for(CRXmlData*child=start(); child; child=next())
	{
		if(child->m_dtdNode->get_Path()!=endPath)continue;
		if(child->m_type==CRXmlData::t_instance_list){
			if(child->getList()->collectInstance(ar,endPath))bHasInstance=true;
		}
		else if(child->m_type!=CRXmlData::t_value){
			ar->push_back(child);
			bHasInstance=true;
		}
	}
	return bHasInstance;
}

