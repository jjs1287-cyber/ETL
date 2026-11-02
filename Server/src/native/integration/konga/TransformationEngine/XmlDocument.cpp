/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <set>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/SystemVariables/SystemVariables.h>
#include "SystemVariableDefs.h"

#include "XmlReader.h"
#include "crom.h"
#include "CRXmlData.h"
#include "CRXmlList.h"
#include "NodePatternMatch.h"
#include "TreeMapper.h"
#include "XmlDocument.h"

namespace {
	std::string pad(const std::string&s, int n0=16){
		int n=s.size();
		if(n0<=n){
			return s;
		}
		else {
			return s+std::string(n0-n, ' ');
		}
	}
}

XmlDocument::XmlDocument(const std::string& sInputFileName, DTDParser*dtdParser, const std::string& batchNodePath, int batch_size) : 
	BatchParams(batchNodePath, batch_size),
	parser(0),
	pDataRoot(0), debug_log(0),
	elementTypes(dtdParser->m_ElementType_map), elements(dtdParser->m_Element_map)
{
	pTreeMapper = dtdParser->getTreeMapper();
	if(pTreeMapper)debug_log = pTreeMapper->getViewLog()->GetDebugLog();

	pDtdRoot=dtdParser->m_root;
	if(pDtdRoot==0){
		throw konga::CrException("XmlDocument cntr failed, structure root is missing.");
	}

	try
	{
		{
			PROFILING prf(getTreeMapper(), "JBJbParser");
			parser=new konga::XmlReader(sInputFileName,konga::GetSystemVariable(TEXT_SOURCE_ENC,pTreeMapper->getOperationDataElementHolder(),""));
			root=parser->parse_all();
		}
		if(debug_log){
			const std::string stax_debug_log_fn( pTreeMapper->getDebugFileName()+".stax" );
			FILE* stax_debug_log=fopen(stax_debug_log_fn.c_str(),"w+");
			if(stax_debug_log){
				konga::AutoFileHandle adopt_stax_debug_log(stax_debug_log);
				root->writeDebugTree(stax_debug_log, "");
			}
		}
		if(!batchNodePath.empty()){
			prepareBatchLoading(pDtdRoot, parser);
		}
	}
	catch(const konga::CrException& e){
		std::string err="Failed in constructing XmlDocument. error: "+e.GetErrorString();
		throw konga::CrException(err);
	}
}

XmlDocument::~XmlDocument(){
	if(parser){
		PROFILING doPrf(getTreeMapper(), "delete_JbParser");
		delete parser;
	}
	if(pDataRoot){
		PROFILING doPrf(getTreeMapper(), "delete_DataTree");
		delete pDataRoot;
	}
}

CROM* XmlDocument::getBatchChildFromDtd(CROM*dtd){
	std::map<CROM*,CROM*>::const_iterator it=batch_dtd_child_map.find(dtd);
	if(it!=batch_dtd_child_map.end())return it->second;
	return 0;
}

namespace {
	                                                                                                                       
	CROM* findSubDtdNode(CROM*node,const char*name){
		if(name&&name[0]){
			for(CROM*child=node->GetFirstChild(); child ;child=child->GetNextSibling()){
				if(child->IsLeaf())continue;
				if(strcmp(child->GetDisplayName(),name)==0){
					return child;
				}
			}
		}
		else {
			for(CROM*child=node->GetFirstChild(); child ;child=child->GetNextSibling()){
				if(child->IsValueLeaf())return child;
			}
		}
		for(CROM*child=node->GetFirstChild(); child ;child=child->GetNextSibling()){
			if(child->IsLeaf())continue;
			if(child->IsChoiceOrAnonymousNode()||child->IsSubstitutionGroupHead()){
				CROM*test=findSubDtdNode(child,name);
				if(test!=0)return test;
			}
		}
		return 0;
	}
}

NodePatternMatch* XmlDocument::findMatchingDtdList(CROM*dtd_derived, std::vector<konga::JbNode*>&child_list){
	                      
	for(int n=child_list.size(), i=n-1; i>=0; --i){
		konga::JbNode* jbChild=child_list.at(i);
		const std::string& localName=jbChild->getLocalName();
		if(findSubDtdNode(dtd_derived, localName.c_str())==0){
			if(dtd_derived->FindChildFolder("Any")==0){
				child_list.erase(child_list.begin()+i);
				delete jbChild;
			}
		}
	}

	std::vector<std::string> name_list;
	int nChild=child_list.size();
	for(int iChild=0; iChild<nChild; ++iChild){
		name_list.push_back(child_list.at(iChild)->getLocalName());
	}

	if(dtd_derived->GetFirstChild()==0){                               
		dtd_derived->copyRecursiveParent(elementTypes, elements);
	}


	NodePatternMatch* match_maker=new NodePatternMatch(elementTypes, elements, debug_log);
	match_maker->findMatchingCrom(name_list, dtd_derived);

	if(debug_log){
		fprintf(debug_log, "Run match_maker %s\n", (const char*)dtd_derived->get_Path());
		for(size_t iChild=0; iChild<nChild; ++iChild){
			CROM*child_dtd=match_maker->get_dtd(iChild),
				*from_dtd=match_maker->get_starting_dtd(iChild);
			std::string child_path=child_dtd? ((const char*)child_dtd->get_Path()):"null";
			std::string prefix=(child_dtd && child_dtd->IsValueLeaf())? "[V]": "";
			std::string from_path=from_dtd? ((const char*)from_dtd->get_Path()):"null";
			fprintf(debug_log, "%d %s %s%s <- %s\n", iChild, pad(name_list.at(iChild)).c_str(),
				child_path.c_str(), prefix.c_str(), from_path.c_str());
		}
	}
	return match_maker;
}


void XmlDocument::createChildren(CRXmlData*pParent, konga::JbNode*pParentJbNode)
{
	CROM*dtd=pParent->GetDTDNode(), *dtd_derived=dtd;
	if(dtd->IsDerivedTypeHolder()){
		std::string xsi_type=pParentJbNode->getAttributeValue("","xsi:type");
		if(!xsi_type.empty()){
			std::string::size_type pos=xsi_type.find(':');
			                   
			                                                             
			if(pos!=std::string::npos){
				xsi_type=xsi_type.substr(pos+1);
			}
			dtd_derived=dtd->FindChildFolder(xsi_type.c_str());
			if(dtd_derived==0){
				dtd->expandHoldersTypeNode(xsi_type.c_str(), elementTypes, elements);
				dtd_derived=dtd->FindChildFolder(xsi_type.c_str());
			}
			pParent=new CRXmlData(pParent, pParentJbNode, dtd_derived, CRXmlData::t_element, "", debug_log);
		}
	}

	addLeaves(pParent, pParentJbNode);

	                   
	                                      
	const std::vector<konga::JbNode*>& child_list(pParentJbNode->getChildren());

	size_t nChild=child_list.size();
	if(nChild){
		int level=-1, batchInstanceCount=1, iChild=0, iChild_saved=0;
		CROM* check=0;
		if(dtd->isBatchParent()){
			check=batch_dtd_child_map.find(dtd)->second;
			if(check==pDtdBatchNode)batchInstanceCount=batchSize;
			level=getBatchLevelFromDtd(check);
			iChild=currentJbNodePositions[level]+1;
			for(int i=level+1;i<nBatchNodes;++i)currentJbNodePositions[i]=-1;
		}
		CRXmlData*pDataChild=0;
		
		if(iChild==0){
			NodePatternMatch*match_maker=findMatchingDtdList(dtd_derived, pParentJbNode->getChildren());
			                                                      
			for(nChild=child_list.size(); iChild<nChild; ++iChild){
				konga::JbNode* jbChild=child_list.at(iChild);
				CROM*child_dtd=match_maker->get_dtd(iChild), *from_dtd=match_maker->get_starting_dtd(iChild);
				if(child_dtd==check){
					if(batchInstanceCount<=0)continue;
					--batchInstanceCount;
					iChild_saved=iChild;
				}
				if (child_dtd==0)
				{                                       
					delete match_maker;
					throw konga::CrException(
						"Failed to find structure node name matching the data element:"+jbChild->getName ()+
						"\n the source data may be invalid. please double check it");
				}
				pDataChild=createElement(pParent, pDataChild, child_dtd, from_dtd, jbChild);
				if(pDataChild && !pDataChild->isValueNode())createChildren(pDataChild, jbChild);
			}
			if(check==0){
				delete match_maker;
			}
			else {
				if(batchNodeMatchMakers[level])delete batchNodeMatchMakers[level];
				batchNodeMatchMakers[level]=match_maker;
			}
		}
		else {
			NodePatternMatch*match_maker=batchNodeMatchMakers[level];

			for(int i=0; i<nChild; ++i){
				konga::JbNode* jbChild=child_list.at(i);
				CROM*child_dtd=match_maker->get_dtd(i), *from_dtd=match_maker->get_starting_dtd(i);
				if(child_dtd==check){
					if(i<iChild||batchInstanceCount<=0)continue;
					--batchInstanceCount;
					iChild_saved=i;
				}
				pDataChild=createElement(pParent, pDataChild, child_dtd, from_dtd, jbChild);
				if(pDataChild && !pDataChild->isValueNode())createChildren(pDataChild, jbChild);
			}
		}
		if(check){
			if(batchInstanceCount>0)iChild_saved=nChild;
			currentJbNodePositions[level]=iChild_saved;
		}
	}
	return;
}

void XmlDocument::addLeaves(CRXmlData*element, konga::JbNode*pJbNode){
	                                                                                                                                               
	if(pJbNode->getLocalName().empty())
		return;
	CROM*dtd=element->GetDTDNode();
	if(dtd->IsAnyElement())
		return;

	if(dtd->GetFirstChild()==0){                               
		dtd->copyRecursiveParent(elementTypes, elements);
	}
	for(CROM*dtdChild=dtd->GetFirstChild(); dtdChild; dtdChild=dtdChild->GetNextSibling()){
		if(dtdChild->IsValueLeaf()){
			bool found=false;
			std::string value;
			std::vector<konga::JbNode*>& children(pJbNode->getChildren());
			for(std::vector<konga::JbNode*>::iterator it=children.begin(); it!=children.end(); ++it){
				konga::JbNode* v=*it;
				if(v->IsValueNode()){
					value+=v->getValue();
					konga::convertCRLN2LN(value);
					found=true;
				}
			}
			if(!dtdChild->IsOptionalNode() || found){
				new CRXmlData(element, CRXmlData::t_value_STAX, dtdChild, value, debug_log);
			}
		}
		else if(dtdChild->IsAttribute()){
			const std::string name((const char*)dtdChild->GetDisplayName());
			                                                                                          
			std::string attr_value=pJbNode->getAttributeValue(dtdChild->GetNamespace(), name);
			if ( !attr_value.empty()) {
				new CRXmlData(element, CRXmlData::t_attribute_STAX, dtdChild, attr_value, debug_log);
			}
		}
	}
}

CRXmlData* XmlDocument::createElement(CRXmlData*pParent0, CRXmlData*pPrevChild, CROM*dtd, CROM*from_dtd, konga::JbNode*jbNode){
	bool bValueNode=jbNode->IsValueNode();
	if(bValueNode){
		CROM* dtd_parent= dtd->GetParent();
		if(dtd_parent && dtd_parent->IsElement())return 0;
	}
	std::vector<CROM*>parent_dtd_list;
	CROM*parent_dtd0=0;
	if(pParent0)parent_dtd0=pParent0->GetDTDNode();

	                                  
	for(CROM* parent_dtd=dtd; parent_dtd; parent_dtd=parent_dtd->GetParent()){
		if(parent_dtd==from_dtd)break;
		if(dtd->IsValueLeaf())continue;
		parent_dtd_list.push_back(parent_dtd);
	}

	                                        
	CRXmlData*pParent=pParent0, *pInstanceList=0;
	if(pPrevChild){
		                     
		for(pParent=pPrevChild; pParent; pParent=pParent->m_parent){
			CROM* parent_dtd=pParent->GetDTDNode();
			if(parent_dtd==from_dtd || parent_dtd==parent_dtd0)break;
			pInstanceList=pParent;
		}
		if(pInstanceList!=0){
			if(pInstanceList->getType()!=CRXmlData::t_instance_list){
				pInstanceList=0;
			}
		}
	}

	int nParents=parent_dtd_list.size();
	for(int m=nParents-1; m>=0; --m){
		CROM*dtd_=parent_dtd_list.at(m);
		if(m>0 || dtd_->isLoopNode())
			pParent=generateTransientParent(pParent, pInstanceList, dtd_, debug_log);
	}
	if(dtd->IsAnyElement()){
		if(dtd->isLoopNode() && pParent->getType()!=CRXmlData::t_instance_list)
			pParent=pParent->m_parent;
		CRXmlData*any=new CRXmlData(pParent, jbNode, dtd, CRXmlData::t_element, "", debug_log);

		                                                           
		std::string value=jbNode->getXmlString();
		konga::convertCRLN2LN(value);

		CROM* value_dtd=dtd->FindChild(CROM_t_Value, "");
		if(value_dtd==0){
			throw konga::CrException("Create xml data tree failed. Source structure tree has problem: can't find value node under xsd ANY element.");
		}
		return new CRXmlData(any, CRXmlData::t_value_STAX, value_dtd, value, debug_log);
	}
	if(bValueNode){
		std::string value=jbNode->getText();
		return new CRXmlData(pParent, CRXmlData::t_value, dtd, value, debug_log);
	}
	else {
		return new CRXmlData(pParent, jbNode, dtd, CRXmlData::t_element, "", debug_log);
	}
}

CRXmlData::CRXmlData(CRXmlData*parent, Type type, CROM*dtd, const std::string& value, FILE*DOMLoop_Debug_log) :
	m_list(0),
	m_type(type),
	m_next(0),
	m_dtdNode(dtd),
	m_value(value),
	m_parent(parent)
{
#ifdef _DEBUG
	m_tag="D_"+std::string((const char*)dtd->GetName())+"_"+typeStr[m_type]+konga::toString(getLabel());
#endif
	if(DOMLoop_Debug_log!=0){
		std::string name;
		if(type==t_value_STAX||type==t_attribute_STAX){
			name="xml_text=\""+getValue()+"\"";
		} else {
			name=std::string("path:")+(const char*)dtd->get_Path();
		}
		fprintf(DOMLoop_Debug_log,"%s = new CRXmlData(parent=%s, %s, dtd=%s)\n",
			getTagString().c_str(),
			parent? parent->getTagString().c_str(): "0",
			name.c_str(),
			getDtdPath().c_str()
		);
	}
	Init("");
}
	
CRXmlData* XmlDocument::generateTransientParent(CRXmlData*parent, CRXmlData*instance_list, CROM*dtd, FILE*debug_log){
	bool bLoopNode=dtd->isLoopNode();
	CROM*parent_dtd=parent->GetDTDNode();
	if(parent->getType()==CRXmlData::t_instance_list && parent_dtd->IsChoiceOrAnonymousNode()){
		parent=new CRXmlData(parent, CRXmlData::t_element_list, parent_dtd, "", debug_log);
	}
	switch(parent->getType()){
		case CRXmlData::t_element:
		case CRXmlData::t_element_list:
		case CRXmlData::t_instance_list:
			if(bLoopNode){
				if(instance_list && instance_list->GetDTDNode()==dtd){
					if(dtd->IsChoiceOrAnonymousNode()){
						return new CRXmlData(instance_list, CRXmlData::t_element_list, dtd, "", debug_log);
					}
					else {
						return instance_list;
					}
				}
				parent=new CRXmlData(parent, CRXmlData::t_instance_list, dtd, "", debug_log);
				if(dtd->IsChoiceOrAnonymousNode()){
					return new CRXmlData(parent, CRXmlData::t_element_list, dtd, "", debug_log);
				}
				else {
					return parent;
				}
			}
			else {
				return new CRXmlData(parent, CRXmlData::t_element_list, dtd, "", debug_log);
			}
	}
	return 0;
}

unsigned long XmlDocument::createDataTree()
{
	if(pDataRoot){
		delete pDataRoot;
	}

	pDataRoot=new CRXmlData(0, root, pDtdRoot, CRXmlData::t_element, "", debug_log);
	createChildren(pDataRoot, root);

	return S_OK;
}

bool XmlDocument::readNextBatch(CRXmlData*beginCROMData, CROM*generator, std::vector<CRData*>&instances)
{
	if(beginCROMData==0)return 0;
	CROM *beginDtd=beginCROMData->GetDTDNode(), *dtd=generator->GetParent();
	konga::JbNode* parent=0;
	int endLevel=getBatchLevelFromDtd(generator), level;
	CRXmlData*parentData;
	if(instances.size()==0){
		level=getBatchLevelFromDtd(beginDtd)+1;
		parentData=beginCROMData;
		currentJbNodePositions[level]=-1;
		while(endLevel<0){
			dtd=dtd->GetParent();
			if(dtd && !dtd->isBatchParent())continue;
			endLevel=getBatchLevelFromDtd(dtd)+1;
			break;
		}
	}
	else {
		parentData=dynamic_cast<CRXmlData*>(instances.at(0)->GetParent());
		if(parentData->GetDTDNode()!=dtd){
			parentData=parentData->m_parent;
		}
		while(endLevel<0 && parentData!=0){
			dtd=dtd->GetParent();
			while(parentData->GetDTDNode()!=dtd){
				parentData=parentData->m_parent;
			}
			assert(parentData!=0);
			if(dtd && !dtd->isBatchParent())continue;
			endLevel=getBatchLevelFromDtd(dtd)+1;
			break;
		}
		assert(parentData!=0);
		for(level=endLevel; dtd!=beginDtd && dtd && level>=0; --level){
			int iChild=currentJbNodePositions[level];
			if(iChild >= 0){
				parent=getCurrentJbNode(level-1);
				assert(parent!=0);
				if(iChild < parent->getChildren().size())break;
			}
			dtd=dtd->GetParent();			
			parentData=parentData->m_parent;
			if(parentData->GetDTDNode()!=dtd){
				parentData=parentData->m_parent;
			}
			assert(parentData!=0);
		}
		instances.clear();
	}
	bool instance_not_found;
	do {
		assert(level>0);
		parent=getCurrentJbNode(level-1);
		assert(parentData!=0);
		parentData->removeChildren();
		createChildren(parentData, parent);
		int err;
		parentData->collectInstances(instances, generator->get_Path(), 0, err);
		if(instances.size()>0)return true;
		instance_not_found=true;
		for(level=endLevel, dtd=generator->GetParent(); dtd!=beginDtd&& dtd && level>=0; --level){
			int iChild=currentJbNodePositions[level];
			if(iChild < 0)continue;
			parent=getCurrentJbNode(level-1);
			assert(parent!=0);
			if(iChild < parent->getChildren().size()){
				instance_not_found=false;
				break;
			}
		}
	} while(!instance_not_found);
	return instances.size()>0;
}


