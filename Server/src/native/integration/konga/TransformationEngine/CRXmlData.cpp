/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <stack>
#include <memory>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>			                  

#include <konga/Xml/DOMNodeListPtr.h>
#include <konga/Xml/DOMAttrPtr.h>
#include <konga/Xml/XmlUtils.h>

#include <konga/Server/WDIPathFinder.h>
#include <konga/Server/WDILogFileUtils.h>

#include "ErrorCodes/resource.h"
#include "crom.h"
#include "TreeMapper.h"
#include "CRXmlList.h"
#include "CRXmlParser.h"
#include "NodePatternMatch.h"
#include "XmlReader.h"
#include "CRXmlData.h"

using namespace konga;
using std::string;

void CRXmlData::get_non_white_space_children(konga::xml::DOMNodePtr node, std::vector<konga::xml::DOMNodePtr>& list)
{
	list.clear();
	const string blank_space_chars(" \t\r\n");

	for(konga::xml::DOMNodePtr child=node->getFirstChild(); child!=0; child=child->getNextSibling()){
		if(child->getNodeType()==konga::xml::NODE_ELEMENT){
			list.push_back(child);
		} else {
			if ( child->getNodeValue().find_first_not_of(blank_space_chars) != string::npos )
				list.push_back(child);
		}
	}
}

const char*CRXmlData::typeStr[]={"v","a","E","root","IL","EL","V","A"};

std::string GetVerboseType(int Type);

                               

                                                                             
  
                                                                                           
                               
                                       
                                                                   
                                                                      
                                                      

                                                                                                          
                                          
                                                                                                             
                                                                                              
                                                                                                  
                                                                                             
                                                                           
                                                 
  

TreeMapper* CRXmlData::getTreeMapper() {return m_dtdNode->getTreeMapper();}
CViewLog* CRXmlData::getViewLog(){return getTreeMapper()->getViewLog();}
CErrorLog* CRXmlData::getErrorLog(){return getTreeMapper()->getErrorLog();}


CRXmlData::CRXmlData(CRXmlData*parent,CROM*dtd,Type type,const std::string&header,  FILE*DOMLoop_Debug_log)
 :	m_list(0),
	m_type(type),
	m_next(0),
	m_dtdNode(dtd),
	m_parent(parent)
{
#ifdef _DEBUG
	m_tag="D_"+std::string((const char*)dtd->GetName())+"_"+typeStr[m_type]+konga::toString(getLabel());
#endif
	Init(header);
}

CRXmlData::CRXmlData(CRXmlData*parent, konga::xml::DOMNodePtr domNode,CROM*dtd, Type type, const std::string&header, FILE*DOMLoop_Debug_log)
 :	m_list(0),
	m_type(type),
	m_next(0),
	m_dtdNode(dtd),
	m_parent(parent)
{
#ifdef _DEBUG
	m_tag="D_"+std::string((const char*)dtd->GetName())+"_"+typeStr[m_type]+konga::toString(getLabel());
#endif
	Init(header);
	m_domNode=domNode;
}

CRXmlData::CRXmlData(CRXmlData*parent, konga::JbNode* jbNode,CROM*dtd, Type type, const std::string&header, FILE*DOMLoop_Debug_log)
 :	m_list(0),
	m_type(type),
	m_next(0),
	m_dtdNode(dtd),
	m_parent(parent)
{
#ifdef _DEBUG
	m_tag="D_"+std::string((const char*)dtd->GetName())+"_"+typeStr[m_type]+konga::toString(getLabel());
#endif
	Init(header);
	m_jbNode=jbNode;
}


void CRXmlData::Init(const std::string&header) {
	if(m_parent==0){
	} else {
		switch(m_parent->m_type){
		case t_root:
		case t_element:
		case t_element_list:
		case t_instance_list:
			m_parent->addChildToList(this,header);
			break;
		default:
			throw InternalErrorException("Failed to initialize CRXmlData. Illegal parent type: " + toString(m_parent->m_type) + ".");
			break;
		}
	}
	m_domNode=0;
}

CRXmlData::~CRXmlData(){
	if(m_list)delete m_list;
}

#ifdef _DEBUG
namespace {int g_label=0;}

void CRXmlData::resetLabel(){
	g_label=0;
}

int CRXmlData::getLabel()
{
	return ++g_label;
}
#endif

std::string CRXmlData::getDtdPath(){
	std::string path(m_dtdNode?(const char*)m_dtdNode->get_Path():"0");
	int n;
	if((n=path.length())>20){
		return std::string("...")+path.substr(n-17);
	}
	return path;
}

void CRXmlData::writeDebugDataTree(FILE* const out0, const std::string& header){
	FILE*out=out0;
	if(out==0 && header.empty()){
		TreeMapper*pTreeMapper=GetDTDNode()->getTreeMapper();
		if(pTreeMapper->getViewLog()->GetDebugLog()!=0){
			const string& stax_debug_log_fn=pTreeMapper->getDebugFileName()+".srcrxml";
			out=fopen(stax_debug_log_fn.c_str(),"w+");
		}
	}
	if (out==0) return;

	CRXmlData::Type parent_type=t_root;
	if(m_parent!=0)parent_type=m_parent->m_type;
	switch(m_type)
	{
	case t_value:
	case t_value_STAX:
		fprintf(out,"%s[V] \"%s\"\n", header.c_str(), konga::escapeSpecialChars(getValue().c_str()).c_str());
		break;
	case t_attribute:
	case t_attribute_STAX:
		fprintf(out,"%s[A] %s=\"%s\"\n", header.c_str(), (const char*)m_dtdNode->GetName(), konga::escapeSpecialChars(getValue().c_str()).c_str());
		break;
	case t_element:
	case t_instance_list:
	case t_element_list:
		fprintf(out,"%s[%s] %s\n", header.c_str(), getTagString().c_str(), (const char*)m_dtdNode->get_Path());
		if(m_list){
			std::string header1=header+"  ";			
			for(CRXmlData*child=m_list->start(); child; child=m_list->next()){
				child->writeDebugDataTree(out, header1);
			}
		}
		break;
	}
	if(out0==0 && out!=0)fclose(out);
}

std::string CRXmlData::getXmlString(){
	std::string str,displayName;
	CROM*dtd=m_dtdNode;
	switch(m_type)
	{
	case t_value:
		if(m_dtdNode->GetParent()->IsAnyElement())str=getValue();
		else str=xml_text(getValue().c_str(), false);
		break;
	case t_attribute:
		str=std::string(" ")+(const char*)m_dtdNode->GetName()+"=\""+xml_text(getValue().c_str(), true)+"\"";
		                                                                                                       
		break;
	case t_element:
		str="<";
		displayName=(const char*)dtd->GetDisplayName();
		str+=displayName;
		if(m_list){
			str+=m_list->getXmlString(true);
			bool hasSubElements=m_list->hasSubElements();
			if(hasSubElements){
				str+=">";
				str+=m_list->getXmlString(false);
				str+="</";
				str+=displayName;
				str+=">";
			} else {
				str+="/>";
			}

		} else {
			str+="/>";
		}
		break;
	case t_instance_list:
		{
			CRXmlData*instance0=m_list->start();
			if(instance0){
				str+=m_list->getXmlString(false);
			}
		}
		break;
	case t_element_list:
		str+=m_list->getXmlString(false);
		break;
	}
	return str;
}


void CRXmlData::deleteList(){
	CRXmlList*list=getList();
	if(list){
		list->deleteList();
		delete list;
		m_list=0;
	}
}

bool CRXmlData::isNil() {
	if(getTreeMapper()->useJbNode()){
		assert(m_type==t_value_STAX || m_type==t_attribute_STAX);
		if(GetDTDNode()->IsValueLeaf() && m_parent){
			konga::JbNode*jbNode=m_parent->m_jbNode;
			bool found;
			const std::string attrValue=jbNode->getAttributeValue("xsi:nil",found);
			return (found && XmlUtils::IsValueTrue(attrValue));
		}
		return false;
	}
	else {
		konga::xml::DOMElementPtr pElement(m_domNode);
		if ( pElement == 0 ) {
			                                                                               
			                                              
			return false;
		}

		const string sNil("nil");
		xml::DOMAttributePtr pNilAttr =	pElement->getAttributeNodeNS(XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001, sNil);
		if ( pNilAttr == 0 ) {
			pNilAttr = pElement->getAttributeNodeNS(XsdConstants::SCHEMA_INSTANCE_NAMESPACE_1999, sNil);
		}

		if ( pNilAttr != 0 ) {
			return XmlUtils::IsTrue(pNilAttr);
		}
	}
	return false;
}

std::string CRXmlData::getDisplayString(){
	std::string str=getTagString()+std::string(":path=")+(const char*)m_dtdNode->get_Path()+",parent="+m_parent->getTagString();
	switch(m_type){
	case t_element_list:
	case t_instance_list:
		return str+",list="+(m_list?m_list->getDisplayString():std::string("empty"));
	case t_element:
		if(getTreeMapper()->useJbNode()){
			return str+",node_name="+m_jbNode->getName();
		}
		else {
			return str+",node_name="+m_domNode->getBaseName();
		}
	case t_attribute:
		return str+",att="+getValue();
	case t_value:
		return str+",value="+getValue();
	}
	return str;
}

void CRXmlData::addChildToList(CRXmlData*child,const std::string&header){
	if(m_list==0){
		m_list=new CRXmlList(this,header,0);
	}
	m_list->add(child,header);
}

void CRXmlData::removeChildren(){
	if(m_list==0)return;
	for(CRXmlData*child=m_list->start(); child; child=m_list->next()){
		child->removeChildren();
	}
	delete m_list;
	m_list=0;
}

void CRXmlData::removeChild(CRXmlData*child){
	if(m_list==0||child==0)return;
	m_list->remove(child);
}

CRXmlList*CRXmlData::getList(){
	if(m_type!=t_element_list&&m_type!=t_instance_list)return 0;
	return m_list;
}

CRXmlList*CRXmlData::getSubElementList(){
	if(m_type==t_element)return m_list;
	return 0;
}

                                                                              
                                                  

CRXmlData*CRXmlData::findInstance(const KString&path,int&err){
	err=0;
	const KString& path0(m_dtdNode->get_Path());
	CRXmlData*instance=this;
	if(path0==path){
		switch(m_type){
		case t_element:
		case t_element_list:
			return instance;
		case t_instance_list:
			if(instance->m_list->m_start==instance->m_list->m_end){
				CRXmlData*data=instance->m_list->start();
				if(data->m_type==t_element&&data->m_dtdNode->get_Path()==path)return data;
			}
			return instance;
		case t_value:
		case t_attribute:
			err=FINDINSTANCE_ERR_TYPE;
			return 0;
		}
	}
	KString path0_;
	konga::StringTokenizer tk0(path0,"$."),tk(path,"$.");
	KString t0,t;
	bool b0,b;

	                                                                                         
	for(b0=tk0.NextToken(),b=tk.NextToken();b0&&b;b0=tk0.NextToken(),b=tk.NextToken()){
		t0=tk0.token();
		t=tk.token();
		if(strcmp(t0,t)!=0)break;
		path0_+=t0;
	}
	                                                                                                                      
	if(b0){
		for(instance=instance->m_parent; instance; instance=instance->m_parent){
			if(instance->m_dtdNode->get_Path()==path0_)break;
		}
	}

	if(instance==0){
		err=FINDINSTANCE_ERR_PATH;
		return 0;
	}
	if(b==false){
		switch(m_type){
		case t_element:
		case t_element_list:
			return instance;
		case t_instance_list:
			if(instance->m_list->m_start==instance->m_list->m_end){
				CRXmlData*data=instance->m_list->start();
				if(data->m_type==t_element&&data->m_dtdNode->get_Path()==path)return data;
			}
			return instance;
		case t_value:
		case t_attribute:
			err=FINDINSTANCE_ERR_TYPE;
			return 0;
		}
		return instance;
	}

	tk.StepBack();
	while(tk.NextToken()&&instance!=0){
		t=tk.token();
		int k=t.Find('#');
		if(k>=0){                      
			int iInstance=0,k1=k,l=(int)t.GetLength();
			if((k+1)<l&&t[k+1]=='_')k1++;                          
			if((k1+1)<l)iInstance=atoi(t.Mid(k1+1));
			if(iInstance!=0){
				if(k1>k)iInstance=-iInstance;
				path0_+=KString(t,k);
				if(tk.NextToken()){
					t=tk.token();
					if(t[0]!='.'){
						err=FINDINSTANCE_ERR_PATH;
						return 0;
					}
					path0_+=t;
					std::auto_ptr<InstanceList>list(instance->findInstanceList(path0_,err));
					if(err==0&&list.get()!=0){
						int nInstances=(int)list->size();
						if(iInstance<0)iInstance+=nInstances;
						else iInstance--;
						
						if(iInstance>=0&&iInstance<nInstances)instance=list->at(iInstance);
						else instance=0;
					}
				} else {
					err=FINDINSTANCE_ERR_PATH;
					instance=0;
				}
			} else {                                                        
				path0_+=KString(t,k);
				if(tk.NextToken()){
					t=tk.token();
					if(t[0]!='.'){
						err=FINDINSTANCE_ERR_PATH;
						return 0;
					}
					path0_+=t;
					switch(instance->m_type){
					case t_element:
						{
							CRXmlList*list=instance->m_list;
							if(list==0)return 0;
							CRXmlData*data;
							for(data=list->start();data;data=list->next()){
								if(data->m_type!=t_value&&data->m_type!=t_attribute&&data->m_dtdNode->get_Path()==path0_){
									instance=data;
									break;
								}
							}
						}
						break;
					case t_value:
					case t_attribute:
						err=FINDINSTANCE_ERR_TYPE;
						return 0;
					case t_instance_list:
						if(instance->m_dtdNode->isLoopNode())return instance;
						if(instance->m_list->m_start==instance->m_list->m_end){
							instance=instance->m_list->start();
						} else {
							err=FINDINSTANCE_ERR_TYPE;
							return 0;
						} 
					case t_element_list:
						{
							CRXmlList*dataList=instance->m_list;
							for(instance=dataList->start();instance;instance=dataList->next()){
								if(instance->m_dtdNode->get_Path()==path0_)break;
							}
							if(instance==0){
								err=FINDINSTANCE_ERR_CHILDMISSING;
								return 0;
							}
						}
						break;
					}
				} else {
					err=FINDINSTANCE_ERR_PATH;
					instance=0;
				}
			}
		} else {
			path0_+=t;
			if(tk.NextToken()){
				t=tk.token();
				if(t[0]=='.'||t[0]=='$'){
					path0_+=t;
repeat:				switch(instance->m_type){
					case t_element:
						{
							CRXmlList*list=instance->m_list;
							if(list==0)return 0;
							CRXmlData*data;
							for(data=list->start();data;data=list->next()){
								if(data->m_type!=t_value&&data->m_type!=t_attribute&&data->m_dtdNode->get_Path()==path0_){
									instance=data;
									break;
								}
							}
							if(data==0)return 0;
						}
						break;
					case t_value:
					case t_attribute:
						err=FINDINSTANCE_ERR_TYPE;
						return 0;
					case t_instance_list:
						if(instance->m_dtdNode->isLoopNode())return instance;
						if(instance->m_list->m_start==instance->m_list->m_end){
							instance=instance->m_list->start();
							if(instance->m_type==t_element)goto repeat;
						} else {
							err=FINDINSTANCE_ERR_TYPE;
							return 0;
						}
					case t_element_list:
						{
							CRXmlList*dataList=instance->m_list;
							for(instance=dataList->start();instance;instance=dataList->next()){
								if(instance->m_dtdNode->get_Path()==path0_)break;
							}
							if(instance==0){
								err=FINDINSTANCE_ERR_CHILDMISSING;
								return 0;
							}
						}
						break;
					}
				} else {
					err=FINDINSTANCE_ERR_PATH;
					instance=0;
				}
			} else {
				err=FINDINSTANCE_ERR_PATH;
				instance=0;
			}
		}
	}

	return instance;
}

InstanceList*CRXmlData::findInstanceList(const KString&endPath0,int&err){
	std::vector<CRXmlData*>*array=0;
	KString endPath=endPath0;
	endPath.Replace("#.",".");
	CRXmlData*data=findInstance(endPath,err);
	  
                                                                                        
                                                                           
                           
                                  
                
                                       
                                 
               
   
    
	if(data==0||err)return array;
	switch(data->m_type){
	case t_value:
	case t_attribute:
		err=FINDINSTANCELIST_ERR;
		return array;
	case t_element:
			if(endPath==data->m_dtdNode->get_Path()){
				if(array==0)array=new std::vector<CRXmlData*>();
				array->push_back(data);
			} else {
				err=FINDINSTANCELIST_ERR;
			}
			break;
	case t_element_list:                                                                          
		{
			bool bHasInstance=false;
			CRXmlList*list=data->m_list;
			if(array==0)array=new std::vector<CRXmlData*>();
			if(list!=0){
				bHasInstance=list->collectInstance(array,endPath);
			}
			if(!bHasInstance){
				if(endPath==data->m_dtdNode->get_Path()){
					if(array==0)array=new std::vector<CRXmlData*>();
					array->push_back(data);
				} else {
					err=FINDINSTANCELIST_ERR;
				}
			}
		}
		break;
	case t_instance_list:
		if(endPath==data->m_dtdNode->get_Path()){
			if(array==0)array=new std::vector<CRXmlData*>();
			CRXmlList*list1=data->m_list;
			if(list1){
				for(CRXmlData*data1=list1->start();data1;data1=list1->next())
					array->push_back(data1);
			}
		}
		else if(endPath.Find((const char*)(data->m_dtdNode->get_Path()))==0){
			CRXmlList*list1=data->m_list;
			CRXmlData*data1;
			for(data1=list1->start();data1;data1=list1->next()){
				if(endPath.Find((const char*)(data1->m_dtdNode->get_Path()))==0){
					std::auto_ptr<InstanceList>array2(data1->findInstanceList(endPath,err));
					if(err==0&&array2.get()!=0){
						if(array==0){
							array=array2.release();
						} else {
							int n=(int)array2->size();
							for(int i=0;i<n;i++){
								array->push_back(array2->at(i));
							}
						}
					}
				}
			}
		} else {
			err=FINDINSTANCELIST_ERR;
		}
		break;
	}
	return array;
}

int CRXmlData::GetInstanceNumber(){
	if(m_parent&&m_parent->m_type==t_instance_list&&(m_dtdNode->isLoopNode()||m_dtdNode->IsSubstitutionGroup())){
		CRXmlList*list=m_parent->getList();
		CRXmlData*data=list->start();
		for(int i=0;data;data=list->next(),i++){
			if(data==this)return i;
		}
	}
	return 0;
}

enum dtd_compare{after_last_dtd,before_last_dtd,dtd_equal,child_of_last_dtd,parent_of_last_dtd,dtd_err};

namespace {
	dtd_compare compare(CROM*dtd1,CROM*dtd2,CROM*&dtd0){
		                                                                                              
		                                                        
		if(dtd1==dtd2){
			for(dtd0=dtd1;dtd0;dtd0=dtd0->GetParent())if(dtd0->isLoopNode())break;
			return dtd_equal;
		}
		                                            
		                                                                               
		                   
		                            
		                           
		     
		                                      
		    
		   
		const char* path1=dtd1->get_Path();
		const char* path2=dtd2->get_Path();
		int i=0,n=(int)strlen(path1);
		for(int ii=0;ii<n;ii++){
			if(path1[ii]!=path2[ii])break;
			if(path1[ii]=='.'||path1[ii]=='$')i=ii+1;
		}
		if(path1[i]&&path2[i]){
			dtd0=dtd1->GetParent();
			KString path0(path1,i);
			while(dtd0){
				if(dtd0->get_Path()==path0)break;
				dtd0=dtd0->GetParent();
			}
			if(dtd0!=0){
				if(dtd0->IsOneOfNode()){
					for(;dtd0;dtd0=dtd0->GetParent())if(dtd0->isLoopNode())break;
					return dtd_equal;
				}
				CROM*dtd1_=dtd1->getNextAncestor(dtd0);
				CROM*dtd2_=dtd2->getNextAncestor(dtd0);
				CROM*child=dtd0->GetFirstChild();
				for(child=dtd0->GetFirstChild();child;child=child->GetNextSibling()){
					if(child==dtd1_||child==dtd2_)break;
				}
				if(child==0)return dtd_err;
				dtd_compare type=(child==dtd2_)?before_last_dtd:after_last_dtd;
				if(type==before_last_dtd){
					for(;dtd0;dtd0=dtd0->GetParent())if(dtd0->isLoopNode())break;
				}
				return type;
			}
			return dtd_err;
		}
		if(path1[i]==0&&path2[i]){
			dtd0=dtd1->GetParent();
			if(dtd1->IsValueLeaf()&&dtd0&&dtd0->IsOneOfNode()){
				if(dtd2->getNextAncestor(dtd0)==dtd2){
					for(;dtd0;dtd0=dtd0->GetParent())if(dtd0->isLoopNode())break;
					return dtd_equal;
				} else
					return after_last_dtd;
			}
			return child_of_last_dtd;
		}
		else if(path2[i]==0&&path1[i]){
			dtd0=dtd2->GetParent();
			for(;dtd0;dtd0=dtd0->GetParent())if(dtd0->isLoopNode())break;
			if(dtd2->IsValueLeaf()&&dtd2->GetParent()->IsOneOfNode()){
				if(dtd1->getNextAncestor(dtd2->GetParent())==dtd1){
					return dtd_equal;
				}
				else
					return before_last_dtd;
			}
			return parent_of_last_dtd;
		} else {
			if(dtd2->GetParent()==dtd1){
				return child_of_last_dtd;
			}
			else {
				for(dtd0=dtd2;dtd0;dtd0=dtd0->GetParent())if(dtd0->isLoopNode())break;
				return parent_of_last_dtd;
			}
		}
	}
}

CRXmlData*CRXmlData::updateInstanceGroup(CRXmlData*instanceGroup,CROM*dtd_last,CROM*dtd, const std::string&header, FILE*DOMLoop_Debug_log){
	                                  
	dtd_compare type;
	CROM*dtd_=0;
	if(dtd_last==0)dtd_last=instanceGroup->m_dtdNode;
	type=compare(dtd_last,dtd,dtd_);
	if((type==dtd_equal||type==before_last_dtd) && dtd->IsSubstitutionGroup() && dtd_last->IsSubstitutionGroup())type=after_last_dtd;

	if(type==before_last_dtd){
		if(dtd_&&dtd_->IsAllGroup()){
			type=after_last_dtd;
		}
	}
	switch(type)
	{
	case before_last_dtd:
	case after_last_dtd:
		{
			while(dtd_!=instanceGroup->m_dtdNode){
				instanceGroup=instanceGroup->m_parent;
			}
			if(type==before_last_dtd){
				if(instanceGroup->m_parent->m_dtdNode==dtd_)instanceGroup=instanceGroup->m_parent;
				if(dtd_&&dtd_->IsGroupNode()){                 
					instanceGroup=new CRXmlData(instanceGroup,dtd_,t_element_list, header, DOMLoop_Debug_log);
				}
			}
		}
		                                     
	case child_of_last_dtd:
		{
			dtd_=dtd->getNextAncestor(instanceGroup->m_dtdNode);
			while(dtd_!=0&&dtd!=dtd_){
				if(dtd_->isLoopNode())
					instanceGroup=new CRXmlData(instanceGroup, dtd_, t_instance_list, header, DOMLoop_Debug_log);
				if(!dtd_->IsSubstitutionGroupHead()){
					instanceGroup=new CRXmlData(instanceGroup, dtd_, t_element_list, header, DOMLoop_Debug_log);
				}
				dtd_=dtd->getNextAncestor(dtd_);
			}
			if((instanceGroup->m_type==t_element||instanceGroup->m_type==t_element_list||instanceGroup->m_dtdNode!=dtd)&&
				!dtd->IsValueLeaf() && dtd->isLoopNode()){
				instanceGroup=new CRXmlData(instanceGroup, dtd, t_instance_list, header, DOMLoop_Debug_log);
			}
		}
		break;
	case parent_of_last_dtd:
	case dtd_equal:
		{
			while(instanceGroup&&dtd_!=instanceGroup->m_dtdNode){
				instanceGroup=instanceGroup->m_parent;
			}
			if(instanceGroup==0)break;
			                    
			if(dtd_&&dtd_->isLoopNode()){
				if(dtd==dtd_){
					if(instanceGroup->m_type==t_element&&instanceGroup->m_parent->m_dtdNode==dtd){
						instanceGroup=instanceGroup->m_parent;
					} else if(instanceGroup->m_type==t_element_list&&instanceGroup->getList()->last()){
						instanceGroup=new CRXmlData(instanceGroup->m_parent, dtd_, t_element_list, header, DOMLoop_Debug_log);
					}
				} else {
					while(dtd_&&dtd!=dtd_){
						if(instanceGroup->m_dtdNode!=dtd_)
							instanceGroup=new CRXmlData(instanceGroup, dtd_, t_instance_list, header, DOMLoop_Debug_log);
						else if(instanceGroup->m_type==t_element_list&&instanceGroup->m_parent->m_dtdNode==dtd_)
							instanceGroup=instanceGroup->m_parent;
						instanceGroup=new CRXmlData(instanceGroup, dtd_, t_element_list, header, DOMLoop_Debug_log);
						dtd_=dtd->getNextAncestor(dtd_);
					}
					if(instanceGroup->m_type==t_element){
						instanceGroup=new CRXmlData(instanceGroup, dtd_, t_instance_list, header, DOMLoop_Debug_log);
                                                        
                                                
                              
                                                                                         
					}
				}
			}
		}
		break;
	}
	return instanceGroup;
}

void CRXmlData::completeChildDataTree1(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, const std::string& header2,  FILE*DOMLoop_Debug_log){
	switch(m_type){
	case t_instance_list:
	case t_element_list:
		completeChildDataTree(elementTypes, elements, header2, DOMLoop_Debug_log);
		break;
	case t_element:
		{
			if(m_list){
				for(CRXmlData*e=m_list->start();e;e=m_list->next()){
					Type type=e->m_type;
					if(type!=t_value&&type!=t_attribute)e->completeChildDataTree(elementTypes, elements, header2, DOMLoop_Debug_log);
				}
			} else {
				addCRXmlDataForLeaves(m_dtdNode, elementTypes, elements, 0, header2, DOMLoop_Debug_log);
			}
		}
		break;
	}
}


void CRXmlData::completeChildDataTree(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, const std::string& header, FILE*DOMLoop_Debug_log){
	std::string header1=header+"  ";
	CROM*dtd=m_dtdNode;
	CRXmlData*_this_=this;
	                                                                                        
                                                         
	switch(m_type){
	case t_element:
		{
			std::auto_ptr<EnterAccumulativeScope> scope_profiling(
				getTreeMapper()->m_pProfilingKit == 0 ? 0 :
				new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "TransformationEngine", "completeChildDataTree_element")
			);
			konga::xml::DOMNodePtr xmlNode=getXml(),xmlChild=0;
			std::string xml_name=xmlNode->getBaseName();
			if((dtd->m_Type&CROM_t_DerivedTypeHolder)!=0){
				konga::xml::DOMElementPtr xmlElement(xmlNode);
				assert(xmlElement!=0);
				string name,namespaceURI;
				const string& type=xmlElement->getAttribute("xsi:type",true);
				if(type.empty()){
					name=dtd->GetSchemaType();
					namespaceURI=dtd->GetSchemaTypeNamespace();
				}
				else {
					const string& prefix=XmlUtils::GetNamespacePrefix(type);
					name=XmlUtils::GetLocalName(type);
					namespaceURI=prefix.empty()?xmlElement->getNamespaceURI():xmlElement->lookupNamespaceURI(prefix);
				}
				for(int iFindDerivedType=0; iFindDerivedType<2; ++iFindDerivedType){
					for(CROM*dtdChild=dtd->GetFirstChild();dtdChild;dtdChild=dtdChild->GetNextSibling()){
						if((dtdChild->m_Type&CROM_t_TypeDefinition)!=0){
							std::string namespace_1(dtdChild->GetNamespace());
							if(namespace_1.empty())namespace_1=dtdChild->GetSchemaTypeNamespace();
							if((const char*)dtdChild->m_Name==name                                                                                                                                                                            ){
								dtd=dtdChild;
								_this_=new CRXmlData(this, xmlNode, dtd, t_element, header1, DOMLoop_Debug_log);
								iFindDerivedType=2;
								break;
							}
						}
					}
					if(iFindDerivedType==0){
						dtd->expandHoldersTypeNode(name.c_str(), elementTypes, elements);
					}
				}
			}
			else if((dtd->m_Type&CROM_t_SubstitutionGroupHead)!=0){
				konga::xml::DOMElementPtr xmlElement(xmlNode);
				assert(xmlElement!=0);
				std::string stLocalName=xmlElement->getLocalName();
				std::string namespaceURI=xmlElement->getNamespaceURI();
				for(CROM*dtdChild=dtd->GetFirstChild();dtdChild;dtdChild=dtdChild->GetNextSibling()){
					std::string namespace_1(dtdChild->GetNamespace());
					if(namespace_1.empty())namespace_1=dtdChild->GetSchemaTypeNamespace();
					if((const char*)dtdChild->m_Name==stLocalName && namespace_1==namespaceURI){
						dtd=dtdChild;
						_this_=new CRXmlData(this, xmlNode, dtd, t_element, header1, DOMLoop_Debug_log);
						break;
					}
				}
			}
		}
		{
			konga::xml::DOMNodePtr beginXml=_this_->getXml();
			std::vector<konga::xml::DOMNodePtr> node_list;
			get_non_white_space_children(beginXml, node_list);
			                                                                  
			                                                                                        
			_this_->addCRXmlDataForLeaves(dtd, elementTypes, elements, &node_list, header, DOMLoop_Debug_log);
			_this_->createCRXmlDataForSubElements(elementTypes,elements,node_list,header, DOMLoop_Debug_log);
			CRXmlList* list=_this_->m_list;
			if(list!=0){
				for(CRXmlData*child=list->start();child;child=list->next()){
					child->completeChildDataTree(elementTypes, elements, header1, DOMLoop_Debug_log);
				}
			}
		}
		break;
	case t_instance_list:
	case t_element_list:
		if(m_list!=0){
			for(CRXmlData*e=m_list->start();e;e=m_list->next()){
				e->completeChildDataTree(elementTypes, elements, header1, DOMLoop_Debug_log);
			}
		}
		break;
	}
}

void CRXmlData::addCRXmlDataForLeaves(CROM*dtd, std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, std::vector<konga::xml::DOMNodePtr>*pNodeList, const std::string& header, FILE*DOMLoop_Debug_log){
	assert(getTreeMapper()->useJbNode()==false);
	std::auto_ptr<EnterAccumulativeScope> scope_profiling(
		getTreeMapper()->m_pProfilingKit == 0 ? 0 :
		new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "TransformationEngine", "CRXmlData_addCRXmlDataForLeaves")
	);
	if(dtd->IsAnyElement())
		return;
	if ( m_domNode->getNodeType() != konga::xml::ELEMENT_NODE )
		return;

	konga::xml::DOMElementPtr element(m_domNode);

	konga::xml::DOMNodePtr xmlAttribute;

	if(dtd->GetFirstChild()==0){                               
		dtd->copyRecursiveParent(elementTypes,elements);
	}

	for(CROM*dtdChild=dtd->GetFirstChild();dtdChild;dtdChild=dtdChild->GetNextSibling()){
		if(dtdChild->IsValueLeaf()){
			if(dtdChild->IsOptionalNode()){
				if(pNodeList==0 || pNodeList->size()==0)continue;
				konga::xml::DOMNodePtr first=pNodeList->at(0);
				if(first->getNodeType()!=konga::xml::NODE_TEXT)continue;
			}
			new CRXmlData(this, element, dtdChild, t_value, header, DOMLoop_Debug_log);
		}
		else if(dtdChild->IsAttribute()){
			const string& ns = dtdChild->GetNamespace();
			const string name((const char*)dtdChild->GetDisplayName());
			if ( ns.empty() ) {
				xmlAttribute = element->getAttributeNode(name);
			}
			else {
				xmlAttribute = element->getAttributeNodeNS(ns, name);
				                                                                                          
				if ( xmlAttribute == 0 ) {
					xmlAttribute = element->getAttributeNode(name);
				}
			}
			if ( xmlAttribute != 0 ) {
				new CRXmlData(this, xmlAttribute, dtdChild, t_attribute, header, DOMLoop_Debug_log);
			}
		}
	}
}


                                                          
                                                                                                                  
                              
                                                                                                       
CRXmlData*CRXmlData::createCRXmlDataForSubElements(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, std::vector<konga::xml::DOMNodePtr>&node_list, const std::string&header, FILE*DOMLoop_Debug_log)
{
	std::auto_ptr<EnterAccumulativeScope> scope_profiling(
		getTreeMapper()->m_pProfilingKit == 0 ? 0 :
		new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "TransformationEngine", "CRXmlData_createCRXmlDataForSubElements")
	);

	if(m_type!=t_element)return 0;
	std::string header1=header+"  ", header2=header1+"  ";
	CROM*groupDtd0=0;

	                                   
	                                                                                                             
	int nElement=0;
	if(m_dtdNode->GetFirstChild()==0){                               
		m_dtdNode->copyRecursiveParent(elementTypes, elements);
	}
	for(CROM*dtdChild=m_dtdNode->GetFirstChild();dtdChild;dtdChild=dtdChild->GetNextSibling()){
		if(!dtdChild->IsLeaf()){
			nElement++;
			if(dtdChild->IsGroupNode()){
				groupDtd0=dtdChild;
			}
		}
	}
	bool bIsGroup=(nElement==1&&groupDtd0!=0);
	if(!bIsGroup&&nElement==0)return 0;
	if(node_list.empty())return 0;

	konga::xml::DOMNodePtr node;
	int i,n=(unsigned int)node_list.size();
	if(!bIsGroup)groupDtd0=m_dtdNode;
	CROM*groupDtd1=groupDtd0;
	if(m_dtdNode->IsDerivedTypeHolder()){
		konga::xml::DOMElementPtr elm(m_domNode);
		if(elm!=0){
			konga::xml::DOMAttributePtr attr=elm->getAttributeNode("xsi:type");
			if(attr!=0){
				std::string xsi_type=attr->getValue();
				if(!xsi_type.empty()){
					std::string::size_type pos=xsi_type.find(':');
					if(pos!=std::string::npos){
						xsi_type=xsi_type.substr(pos+1);
					}
					groupDtd1=groupDtd0->FindChildFolder(xsi_type.c_str());
				}
			}
		}
	}

	NodePatternMatch match_maker(elementTypes, elements, 0);
	std::vector<std::string>name_list;
	for(i=0; i<n; i++)name_list.push_back(node_list.at(i)->getBaseName());
	match_maker.findMatchingCrom(name_list, groupDtd1);
	CRXmlData*parent;
	if(bIsGroup&&groupDtd0->isLoopNode()){
		parent=new CRXmlData(this, groupDtd0, t_instance_list, header1, DOMLoop_Debug_log);
	} else {
		parent=this;                                     
	}

	CROM*dtd_last=parent->m_dtdNode, *dtd;
	for(i=0;i<n;i++){
		node=node_list.at(i);
		dtd=match_maker.get_dtd(i);
		if(dtd){
			                   
			                                                                                                                               
			                        
			if(dtd->GetParent()->IsAllGroup()){
				                                      
				new CRXmlData(parent, node, dtd, t_element, header1, DOMLoop_Debug_log);
				continue;
			}
			CRXmlData*parent_save=parent;
			parent=updateInstanceGroup(parent,dtd_last,dtd,header1, DOMLoop_Debug_log);
			if(parent==0){
				if(dtd->IsValueLeaf()){
					parent=parent_save;
					continue;
				}
				return 0;
			}
			if(dtd->IsValueLeaf()){
				if(bIsGroup){
					                                                                                        
					while(parent){
						if(parent->m_dtdNode==dtd->GetParent()&&parent->m_type==t_instance_list)break;
						if(parent->m_dtdNode==dtd->GetParent())break;
						parent=parent->m_parent;
					}
					CRXmlData* element_list=(parent->m_type!=t_element_list)? new CRXmlData(parent, dtd, t_element_list, header1, DOMLoop_Debug_log): parent;
					new CRXmlData(element_list,node,dtd,t_value,header1, DOMLoop_Debug_log);
				}
			} else if(dtd->IsAnyElement()){
				CRXmlData*any=new CRXmlData(parent, node, dtd, t_element, header1, DOMLoop_Debug_log);
				                                                           
				new CRXmlData(any, node, dtd->FindChild(CROM_t_Value,""), t_value, header1, DOMLoop_Debug_log);
			} else {
				if(parent->m_dtdNode->IsGroupNode()&&parent->m_type!=t_element_list){
					parent=new CRXmlData(parent, parent->m_dtdNode, t_element_list, header1, DOMLoop_Debug_log);
				}
				new CRXmlData(parent, node, dtd, t_element, header1, DOMLoop_Debug_log);
			}
			dtd_last=dtd;
		} else {               
			                                
			           
			           
		}
	}
	return parent;
}

bool CRXmlData::isTestTransform()
{
	return getTreeMapper()->isTestTransformation();
}

                                        

const KString& CRXmlData::getPath() {
	static const KString blank("");
	return m_dtdNode?m_dtdNode->get_Path():blank;
}


CRData*CRXmlData::find(const KString&path,int&err){
	int k=path.ReverseFind('.');
	int k1=path.ReverseFind('$');
	if(k1>k)k=k1;
	if(k==0){
		err=CRXMLDATA_FIND_ERR_MISSING_FOLDER_CHAR;
		return 0;
	}

	KString path_=path.Left(k+1);                                        
	int p1=path_.Find('#'),p2;
	while(p1>0){
		p2=path_.Find('.',p1);
		path_=path_.Left(p1)+path_.Mid(p2);
		p1=path_.Find('#', p1+1);
	}
	CRXmlData*node=findInstance(path_,err),*child;
	if(err||node==0)return 0;
	if(node->m_dtdNode->get_Path()==path_)
	{
rep:	switch(node->m_type){
		case t_element:
			{
				CRXmlList*list=node->m_list;
				if(list==0)return 0;
				KString pathCopy(path);
				p1=pathCopy.Find('#');
				while(p1>0){
					p2=pathCopy.Find('.',p1);
					pathCopy=pathCopy.Left(p1)+pathCopy.Mid(p2);
					p1=pathCopy.Find('#',p1+1);
				}
				for(child=list->start();child;child=list->next()){
					if(child->m_dtdNode->get_Path()==pathCopy)return child;
				}
			}
			break;
		case t_element_list:
			if(node->m_list!=0){                       
				CRXmlData*value=node->m_list->start();
				if(value&&value->m_dtdNode->IsValueLeaf())return value;
			}
			break;
		case t_instance_list:
			if(node->m_list!=0){
				node=node->m_list->m_start;
				goto rep;
			}
		}
	}
	err=CRXMLDATA_FIND_ERR_MISSING_ATTRIBUTE;
	return 0;
}

namespace {
	const string getValue_chars_to_trim("\r\n\t ");
}

std::string CRXmlData::getValue(){
	if(m_type==t_value_STAX || m_type==t_attribute_STAX){
		return m_value;
	}
	std::string value;
	if(m_type==t_value||m_type==t_attribute){
		if(m_dtdNode->GetParent()->IsAnyElement()) {
			value=m_domNode->getXml();
		}
		else {
			konga::xml::DOMNodeType type = m_domNode->getNodeType();
			using namespace konga::xml;
			if ( type == ELEMENT_NODE ) {
				                                         
				DOMNodePtr child = m_domNode->getFirstChild();
				while( child != 0 ) {
					type = child->getNodeType();
					if ( type == TEXT_NODE || type==CDATA_SECTION_NODE) {
						value += child->getNodeValue();
					}
					child = child->getNextSibling();
				}
			}
			else {
				value = m_domNode->getNodeValue();
			}
		}
		return konga::trim_chars(value, getValue_chars_to_trim);
	}
	return value;
}

konga::xml::DOMNodePtr CRXmlData::getXml(){
	                                                              
	assert(getTreeMapper()->useJbNode()==false);
	return m_domNode;
}

konga::JbNode* CRXmlData::getJbNode(){
	assert(getTreeMapper()->useJbNode());
	return m_jbNode;
}

void CRXmlData::collectInstances(std::vector<CRData*>&v, const KString& sEndPath, CROM*targetLoopNode0, int&err){
	std::auto_ptr<InstanceList> list(findInstanceList(sEndPath,err));
	if(list.get()==0)return;
	for(int i=0, n=list->size(); i<n; i++){
		v.push_back(list->at(i));
	}
}

void CRXmlData::getInstances(std::vector<CRData*>&v, const char*nodeName){
	CRXmlData* this_=this;
	if(getType()==t_element){
		if(m_list==0)return;
		CRXmlList* list=m_list;
		for(CRXmlData*e=list->start(); e!=0; e=list->next()){
			if(strcmp(e->GetDTDNode()->get_Name(),nodeName)==0){
				this_=e;
				break;
			}
		}
	}
	if(this_->getType()==t_element){
		if(strcmp(this_->GetDTDNode()->get_Name(),nodeName)==0)v.push_back(this_);
	}
	else {
		CRXmlList* list=this_->getList();
		if(list==0)return;
		for(CRXmlData*instance=list->start(); instance!=0; instance=list->next()){
			if(strcmp(instance->GetDTDNode()->get_Name(),nodeName)==0)
				v.push_back(instance);
		}
	}
}

CRData*CRXmlData::getChildNode(const char*name, bool isLoop)
{
	if(getType()==t_instance_list){
		std::string errmsg(std::string("CRXmlData::findChildNode(\"")+name+"\") called from an instance_list node.");
		if(getTreeMapper()->getViewLog()->IsDebugLogEnabled()){
			getTreeMapper()->getViewLog()->DebugString(errmsg.c_str());
		}
		throw InstanceNotFoundException(errmsg);
	}
	if(m_list==0)return 0;
	for(CRXmlData*child=m_list->start(); child; child=m_list->next())
	{
		if(strcmp(child->GetDTDNode()->get_Name(),name)==0){
			Type type=child->getType();
			if(isLoop){
				if(type==t_instance_list)return child;
			}
			else {
				if(type==t_element || type==t_element_list)return child;
			}
		}
	}
	return 0;
}

CRData*CRXmlData::getAttributeNode(const char*name)
{
	if(getType()!=t_element){
		std::string errmsg(std::string("CRXmlData::findAttributeNode(\"")+name+"\") called from a non element node.");
		if(getTreeMapper()->getViewLog()->IsDebugLogEnabled()){
			getTreeMapper()->getViewLog()->DebugString(errmsg.c_str());
		}
		throw InstanceNotFoundException(errmsg);
	}
	if(m_list){
		for(CRXmlData*child=m_list->start(); child; child=m_list->next())
		{
			Type type=child->getType();
			
			if((type==t_attribute||type==t_attribute_STAX) && strcmp(child->GetDTDNode()->get_Name(),name)==0)
				return child;
		}
	}
	return 0;
}

CRData*CRXmlData::getValueNode()
{
                                                            
	if(getType()!=t_element){
		CROM*crom=GetDTDNode();
		if(!crom->IsOneOfNode() && !(crom->IsValueLeaf() && crom->GetParent()->IsOneOfNode())){
			std::string errmsg(std::string("CRXmlData::findValueNode() called from a non element node."));
			if(getTreeMapper()->getViewLog()->IsDebugLogEnabled()){
				getTreeMapper()->getViewLog()->DebugString(errmsg.c_str());
			}
			throw konga::CrException(errmsg);
		}
	}

	if(m_list==0)return 0;
	for(CRXmlData*child=m_list->start(); child; child=m_list->next())
	{
		Type type=child->getType();
		if(type==t_value||type==t_value_STAX)return child;
	}
	return 0;
}

bool CRXmlData::isEmpty()
{
	Type type=getType();
	if(type==CRXmlData::t_attribute || type==CRXmlData::t_value){
		bool empty=getValue().length()==0;
		if(!empty)return false;
	}
	if(m_list){
		for(CRXmlData*child=m_list->start();child;child=m_list->next()){
			bool empty=child->isEmpty();
			if(!empty)return false;
		}
	}
	return true;
}

