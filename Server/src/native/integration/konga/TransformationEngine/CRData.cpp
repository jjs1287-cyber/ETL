/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <map>
#include <cassert>

#include <konga/Utils/CrException.h>
#include <konga/Utils/KString.h>
#include <konga/DataElement/DataElement.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "DTDParser.h"
#include "Transform.h"
#include "TxtParser.h"
#include "CRXmlParser.h"
#include "CRData.h"
#include "CRXmlList.h"

#ifdef LoadString
#undef LoadString
#endif

using konga::CrFatalException;
using konga::DataElement;

void CRData::fillDataElement(CRData*node0, DataElement*ds, const char*deName, size_t start_pos0, bool bUseJbNode){
	const bool setToNull=true;
	assert(ds);
	if(node0&&node0->GetDTDNode()->IsFlat()){
		CRData*attribute=node0->getAttributeNode(deName);
		if((attribute==0 || attribute->IsNull())){                                           
			const char* defaultStr = ds->getDefaultStr();
			if(node0->getTreeMapper()->GetSourceType()==TextTypeId && defaultStr && defaultStr[0]){
				ds->setValue(defaultStr);
			}
			else {
				ds->setNull();
				ds->setError(DataElement::t_unknown);                          
				if(node0->getTreeMapper()->getViewLog()->IsDebugLogEnabled()){
					std::string errmsg(std::string("CreateDE() failed to find attribute node from flat data tree, attribute name:\"")+deName+"\"");
					node0->getTreeMapper()->getViewLog()->DebugString(errmsg.c_str());
				}
			}
		}
		else {
			const std::string& value(attribute->getValue());
			CROM*dtd=attribute->GetDTDNode();
			ds->setFormatStr(dtd->GetFormat());		             
			ds->setTypedValue(value.c_str(), dtd->GetValueType(), dtd->get_SqlDataType());
			if(attribute->IsNull()||(value.empty()&&!dtd->IsKindOfString()))ds->setError(TRANSFORM_INPUT_EMPTY);
		}
		return;
	}
	CRData*node=node0;
	for(size_t pos, start_pos=start_pos0, end_pos=strlen(deName); 
		start_pos<=end_pos && (pos=start_pos+strcspn(&deName[start_pos],"$.")) <= end_pos;
		start_pos=pos+1){
		std::string name(&deName[start_pos],pos-start_pos);
		bool isLoop=deName[pos]=='.';
		if(node!=0){
			if(deName[pos]==0){                              
				if(pos==start_pos){
					if(node==0){
						ds->setNull();
						ds->setError(DataElement::t_unknown);                          
					}
					else {
						CROM*dtd=node->GetDTDNode();
						if(dtd->IsAnyElement()){
							if(bUseJbNode){
								ds->setJbNode(node->getJbNode());
								CRData*value_node=node->getValueNode();
								assert(value_node!=0);
								ds->setValue(value_node->getValue().c_str());
							}
							else {
								ds->setXmlNode(node->getXml());
							}
						}
						else {
							CRData*value_node=node->getValueNode();
							if(value_node==0){
								const char* defaultStr = ds->getDefaultStr();
								if(defaultStr!=0 && defaultStr[0]!=0){
									ds->setValue(defaultStr);
								}
								else {
									ds->setNull();
									ds->setError(DataElement::t_unknown);                          
									if(node0->getTreeMapper()->getViewLog()->IsDebugLogEnabled()){
										std::string errmsg(std::string("CreateDE() failed to find value node, path:\"")+deName+"\"");
										node0->getTreeMapper()->getViewLog()->DebugString(errmsg.c_str());
									}
								}
							}
							else {
								const std::string& value(value_node->getValue());
								dtd=value_node->GetDTDNode();
								ds->setDefaultString(value);
								ds->setFormatStr(dtd->GetFormat());
								ds->setTypedValue(value.c_str(), dtd->GetValueType(), dtd->get_SqlDataType());
								if(value_node->IsNull()||(value.empty()&&!dtd->IsKindOfString()))ds->setError(TRANSFORM_INPUT_EMPTY);
							}
						}
					}
				} else {
					CRData*attribute=node->getAttributeNode(name.c_str());
					if((attribute==0 || attribute->IsNull())){
						const char* defaultStr = ds->getDefaultStr();
						if(defaultStr && defaultStr[0]){                                                                   
							ds->setValue(defaultStr);
						}
						else {
							ds->setNull();
							ds->setError(DataElement::t_unknown);                          
							if(node0->getTreeMapper()->getViewLog()->IsDebugLogEnabled()){
								std::string errmsg(std::string("CreateDE() failed to find attribute node, attribute name:\"")+name+"\", path:\""+deName+"\"");
								node0->getTreeMapper()->getViewLog()->DebugString(errmsg.c_str());
							}
						}
					}
					else {
						const std::string& value(attribute->getValue());
						CROM*dtd=attribute->GetDTDNode();
						ds->setFormatStr(dtd->GetFormat());		             
						ds->setTypedValue(value.c_str(), dtd->GetValueType(), dtd->get_SqlDataType());
						if(attribute->IsNull()||(value.empty()&&!dtd->IsKindOfString()))ds->setError(TRANSFORM_INPUT_EMPTY);
					}
				}
				return;
			}
			else {
				size_t sub_pos=name.find('#');
				if(sub_pos == std::string::npos){              
					CROM*dtd=node->GetDTDNode()->FindChildFolder(name.c_str(), isLoop);
					if(node!=node0 && dtd->isLoopNode()){
						                                                      
						CRData_list list(node, name.c_str());
						if(list.m_list.empty())
							ds->setNull();
						else 
							list.fillDataElementForArray(ds, deName, pos+1, bUseJbNode);
						return;
					}
					else {
						node=node->getChildNode(name.c_str(), isLoop);
						if(node==0){
							ds->setNull();
							return;
						}
					}
				}
				else if(sub_pos==(pos-1-start_pos)){                          
					name=name.substr(0,sub_pos);
					CRData_list list(node, name.c_str());
					if(list.m_list.empty()){
						ds->setNull();
						ds->initCollection(setToNull);
						node=0;
					}
					else {
						list.fillDataElementForArray(ds, deName, pos+1, bUseJbNode);
						return;
					}
				}
				else {                                                          
					std::string tmp(name.substr(sub_pos+1,pos-start_pos-sub_pos-1));
					bool negative=tmp.at(0)=='_';
					if(negative){
						if(pos<=(start_pos+sub_pos+2)){
							throw InstanceNotFoundException(std::string("CreateDE() failed to find instance with instance number. data element:\"")+deName+"\" at position:"+konga::toString(sub_pos));
						}
						tmp=tmp.substr(1);
					}
					name=name.substr(0,sub_pos);
					CRData_list list(node, name.c_str());
					int i=atoi(tmp.c_str()), n=list.size();
					i=negative?(n-i):(i-1);
					if(i>=0&&i<n)node=list.at(i);
					else node=0;
					if(node==0){
						if(node0->getTreeMapper()->getViewLog()->IsDebugLogEnabled()){
							std::string errmsg(std::string("CreateDE() failed to find instance with instance number. data element:\"")+deName+"\" at position:"+konga::toString(sub_pos)+", instance number(0 based):"+konga::toString(i)+", total:"+konga::toString(n));
							node0->getTreeMapper()->getViewLog()->DebugString(errmsg.c_str());
						}
					}
				}

				if(pos>=(end_pos-1)){                          
					if(node==0){
						ds->setNull();
						ds->setError(DataElement::t_unknown);                          
					}
					else {
						CROM*dtd=node->GetDTDNode();
						if(dtd->IsAnyElement()){
							if(bUseJbNode){
								konga::JbNode* jbNode=node->getJbNode();
								ds->setJbNode(jbNode);
							}
							else {
								konga::xml::DOMNodePtr xml=node->getXml();
								ds->setXmlNode(xml);
							}
						}
						else {
							CRData*value_node=node->getValueNode();
							if(value_node==0){
								ds->setNull();
								ds->setError(TRANSFORM_INPUT_EMPTY);
							}
							else {
								std::string value(value_node->getValue());
								if(value.empty()){
									value=(const char*)ds->getDefaultStr();
								}
								dtd=value_node->GetDTDNode();
								ds->setTypedValue(value.c_str(), dtd->GetValueType(), dtd->get_SqlDataType());
								if(value_node->IsNull()||(value.empty()&&!dtd->IsKindOfString()))ds->setError(TRANSFORM_INPUT_EMPTY);
							}
						}
					}
					return;
				}
			}
		}
		else {                                                                                                                                   
			size_t sub_pos=name.find('#');
			if(sub_pos!=std::string::npos && sub_pos==(pos-1-start_pos)){                          
				name=name.substr(0,sub_pos);
				konga::DataElement*ds1=new konga::DataElement();
				ds1->setNull();
				ds1->initCollection(setToNull);
				ds->addToCollection(ds1);
				ds=ds1;
			}
		}
	}
}


CRData*CRData::findCommonParent(const char*path,int&pos)
{
	const KString& path0(GetDTDNode()->get_Path());
	pos=path0.GetLength();
	if(strncmp((const char*)path0,path,pos)==0){
		return this;
	}
	std::stack<CRData*>parents;
	CRData*parent=this, *parent_parent;
	while(parent){
		parents.push(parent);
		parent_parent=parent->GetParent();
		if(parent_parent && parent->GetDTDNode()==parent_parent->GetDTDNode()){
			parent=parent_parent;
		}
		parent=parent->GetParent();
	}
	konga::StringTokenizer tk(path,".$");
	pos=0;
	std::string token;
	char c;
	CRData*parent_test=0;
	for(pos=0;tk.NextToken();pos+=token.size()+1, parent=parent_test){
		bool is_first_node=(pos==0);
		token=tk.token();
		if((c=token.at(0))=='.'||c=='$')continue;
		if(token.find('#')!=std::string::npos)break;
		std::string node_name(token);
		if(!tk.NextToken())break;                                               
		parent_test=parents.top();
		parents.pop();
		std::string name_test(parent_test->GetDTDNode()->get_Name());
		if(is_first_node){
			if(name_test=="" && name_test!=node_name){
				parent_test=parents.top();
				parents.pop();
				name_test=parent_test->GetDTDNode()->get_Name();
				if(name_test!=node_name)
					throw konga::CrException(std::string("Failed to find common parent, data element path:\"")+path+"\", source path:\""+(const char*)path0+"\"");
			}
		}
		else if(name_test!=node_name || parents.size()==0){
			break;
		}
	}
	return parent;
}



CRData_list::CRData_list(CRData*parent, const char*nodeName){
	if(parent)parent->getInstances(m_list, nodeName);
}


void CRData_list::fillDataElementForArray(konga::DataElement*ds, const char* deName, int start_pos, bool bUseJbNode){
	if(ds==0){
		throw new konga::CrException("Internal error: CRData_list::fillDataElementForArray() was called with null data element pointer.");
	}
	ds->initCollection();
	if(m_list.empty()){                                                                                                                 
		DataElement*ds1=new DataElement();
		CRData::fillDataElement(0, ds1, deName, start_pos, bUseJbNode);
		ds->addToCollection(ds1);
	}
	else {
		for(std::vector<CRData*>::iterator it=m_list.begin(); it!=m_list.end(); it++){
			CRData*data=*it;
			DataElement*ds1=new DataElement();
			CRData::fillDataElement(data, ds1, deName, start_pos, bUseJbNode);
			ds->addToCollection(ds1);
		}
	}
}

bool CRData::IsLoopNode(){
	return GetDTDNode()->isLoopNode();
}

std::string CRData::getInstanceListing()
{
	if(GetDTDNode()==0)return "";
	std::string csInstances;
	CROM*lastDtd=0;
	for(CRData* pNode = this; pNode; pNode = pNode->GetParent()){
		CROM*dtd=pNode->GetDTDNode();
		if(dtd->IsSubstitutionGroupHead())continue;
		if(dtd!=lastDtd && (dtd->isLoopNode()||dtd->IsSubstitutionGroup())){
			int k=pNode->GetInstanceNumber()+1;
			if(k>0)csInstances=konga::toString(k)+";"+csInstances;
		}
		lastDtd=dtd;
	}
	return csInstances;
}
