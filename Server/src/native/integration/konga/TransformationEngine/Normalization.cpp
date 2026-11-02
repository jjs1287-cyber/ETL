/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <set>

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/ErrorResolver.h>

#include "ErrorCodes/resource.h"
#include "crom.h"
#include "CROMData.h"
#include "TextDocReader.h"
#include "Normalization.h"
#include "TreeMapper.h"

#ifdef _DEBUG
#define DEBUG_FLAT_HIERARCH 1
#endif

using namespace std;

namespace {
	const int FLAT_TABLE_HASHTABLE_SIZE=1000;

	bool isLoopOneOfNode(CROM*dtd){
		return dtd->IsOneOfNode() && dtd->isLoopNode();
	}

	bool isSubElementOfOneOfNode(CROM* node) {
		if(node->GetParent()==0)return false;
		return isLoopOneOfNode(node->GetParent());
	}


	                             
	                                                      
	                                                                           
	                                
	bool twoNodesAreEqual(CROMData*node_a, CROMData*node_b)
	{
		if(node_a==node_b)return true;
		if(node_a==0 || node_b==0)return false;
		CROM*crom_a=node_a->GetDTDNode(), *crom_b=node_b->GetDTDNode();
		if(isLoopOneOfNode(crom_a) || isLoopOneOfNode(crom_b))return false;
		if(node_a==0 || node_b==0)return false;
		CROMData*parent_a=node_a->GetParent(), *parent_b=node_b->GetParent();
		if(!isLoopOneOfNode(parent_a->GetDTDNode()) || !isLoopOneOfNode(parent_b->GetDTDNode())){
			if(parent_a!=parent_b)return false;
			if(crom_a!=crom_b)return false;
		}
		
		CROMData*att_a, *att_b;
		att_a=node_a->GetFirstValueAttribute();
		att_b=node_b->GetFirstValueAttribute();

		while(att_a && att_b){
			if(att_a->getValue()!=att_b->getValue())return false;
			att_a=att_a->GetNextValueAttribute();
			att_b=att_b->GetNextValueAttribute();
		}
		return att_a==0&&att_b==0;
	}

	                 
	                                                      
	                                                                           
	                                
	bool Less(CROMData*node_a, CROMData*node_b)
	{
		if(node_a==node_b)return false;
		if(node_a==0||node_b==0)return node_a==0;
		CROM*crom_a=node_a->GetDTDNode(), *crom_b=node_b->GetDTDNode();
		if(isLoopOneOfNode(crom_a) && isLoopOneOfNode(crom_b)){
			CROMData*child_a=node_a->GetFirstChild(),*child_b=node_b->GetFirstChild();
			return Less(child_a, child_b);
		}
		else {
			CROMData* parent_a=node_a->GetParent(), *parent_b=node_b->GetParent();
			if(!isLoopOneOfNode(parent_a->GetDTDNode())|| !isLoopOneOfNode(parent_b->GetDTDNode())){
				if(parent_a!=parent_b)return parent_a<parent_b;
			}
			if(node_a->GetDTDNode()!=node_b->GetDTDNode())return node_a->GetDTDNode()<node_b->GetDTDNode();

			CROMData*att_a, *att_b;
			att_a=node_a->GetFirstValueAttribute();
			att_b=node_b->GetFirstValueAttribute();

			while(att_a && att_b){
				if(att_a->getValue()!=att_b->getValue())return att_a->getValue() < att_b->getValue();
				att_a=att_a->GetNextValueAttribute();
				att_b=att_b->GetNextValueAttribute();
			}
			return (att_a||att_b)? (att_a==0) : false;
		}
	}

}


                                                                
Normalization::Normalization(DTDParser*pFlatDTDParser, CROM*pTargetRoot) :
	m_pTreeMapper(pTargetRoot->getTreeMapper()),
	m_pFlatDTDParser(pFlatDTDParser),
	m_pFlatSourceRoot(0),
	m_pFlatSourceTable(0),
	m_pNormalizedCromRoot(0),
	m_bForceNewLowestNode(false),
	m_bIsDb(false),
	m_fixedField(false),
	m_bIsCsv(false),
	m_strQual(0),
	m_numQual(0),
	m_delimiter(0),
	m_eof(false),
	m_n(0),
	m_de_list(0),
	m_de_type_list(0)
{
	PROFILING doPrf(m_pTreeMapper,"Normalization");
	CROM* pFlatSourceRoot=pFlatDTDParser->m_root;
	if(pFlatSourceRoot==0){
		throw konga::CrException("Normalization constructor error, pFlatSourceRoot is 0.");
	}
	m_pFlatSourceRoot=pFlatSourceRoot;

	m_pFlatSourceTable=pFlatSourceRoot->GetFirstChild();
	for(CROM*pChild=m_pFlatSourceTable->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
		if(pChild->IsAttribute()){
			m_n++;
		}
		else {
			throw konga::CrException("Normalization constructor error, the flat folder contains some non attribute node.");
		}
	}

	m_bIsDb = (m_pTreeMapper->GetSourceType()==DbTypeId);
	if(m_bIsDb){
		m_strQual = '"';
		m_numQual = 0;
		m_delimiter = ',';
		m_fixedField = false;
	}
	else {
		m_strQual = m_pFlatSourceTable->get_strQual();
		m_numQual = m_pFlatSourceTable->get_numQual();
		m_delimiter = m_pFlatSourceTable->get_delimiter();
		m_fixedField = (m_delimiter==0);
	}
	m_bIsCsv = !m_fixedField && m_strQual=='"';

	if(m_n==0){
		throw konga::CrException("Normalization constructor error, the flat source has no attribute.");
	}
	m_de_list=new CROM*[m_n];
	m_de_type_list=new konga::DataElement::Type[m_n];

	CROM*att=m_pFlatSourceTable->GetFirstChild();
	for(int i=0; i<m_n; ++i, att=att->GetNextSibling()){
		const char* name=att->GetName();
		m_de_list[i]=att;
		m_de_type_list[i]=att->get_DataElementType();
		mapName2Flat[name]=att;
	}
	createNormalizedStructure(pTargetRoot);
}

Normalization::~Normalization(){
	if(m_pNormalizedCromRoot)m_pNormalizedCromRoot->DeleteNode();
	delete[] m_de_list;
	delete[] m_de_type_list;
	m_de_list=0;
}

CViewLog* Normalization::getViewLog(){return m_pTreeMapper->getViewLog();}
CErrorLog* Normalization::getErrorLog(){return m_pTreeMapper->getErrorLog();}

void Normalization::createNormalizedStructure(CROM* pTargetRoot)
{
	if(pTargetRoot==0){
		throw konga::CrException("Internal error in createNormalizedStructure, target structure root node =0.");
	}

	CROM*pTargetNode=pTargetRoot;
	if((pTargetRoot->m_Type&CROM_t_Root)!=0 && !pTargetRoot->IsXml())
		pTargetNode=pTargetRoot->GetFirstChild();

	assert(m_n>0);

	m_pNormalizedCromRoot=new CROM(m_pTreeMapper, 0, 0, 0, 0, CROM_t_Root|CROM_t_Element, m_bIsDb?CROM::t_CROM_DB:CROM::t_CROM_Text);

	for(;pTargetNode; pTargetNode=pTargetNode->GetNextSibling())
	{
		FindHierarch(pTargetNode, m_pNormalizedCromRoot, 0, 0);
	}

	m_pNormalizedCromRoot->definePath();

#if DEBUG_FLAT_HIERARCH
	getViewLog()->DebugString("createNormalizedStructure\n");
	m_pNormalizedCromRoot->write(getViewLog()->GetDebugLog());
#endif
}

void Normalization::FindHierarch(CROM* pTargetNode, CROM*pNewSourceParentNode, int level, CROM*fixGenerator)
{
	if((pTargetNode->isLoopNode()|| isSubElementOfOneOfNode(pTargetNode)) || level==0){
		if(fixGenerator==0){
			CROM*pNewSourceNode=new CROM(m_pTreeMapper, pNewSourceParentNode, pNewSourceParentNode->GetLastChild(), CROM::convertIllegalCharacters(pTargetNode->m_Name), 0, pTargetNode->m_Type, pNewSourceParentNode->get_CROM_ext_type());
			pTargetNode->m_Generator=pNewSourceNode;
			pNewSourceNode->m_Generator=pTargetNode;                                                                                                         
			AddAttributesFromTargetNode(pNewSourceNode, pTargetNode);
			pNewSourceParentNode=pNewSourceNode;
			KString targetPath=pTargetNode->get_Path();
			if(targetPath.GetAt(0)!='[')targetPath="["+targetPath+"]";
			KString& fix_generator_path(m_pTreeMapper->m_maplist[targetPath]);
			if(strncmp("Fixed ",(const char*)fix_generator_path,6)==0){
				fixGenerator=pNewSourceNode;
				fixGenerator->SetNormalizationFixGenerator();
			}
		}
		else {
			pTargetNode->m_Generator=fixGenerator;
			AddAttributesFromTargetNode(pNewSourceParentNode, pTargetNode);
			pNewSourceParentNode->m_Generator=0;
		}
	}

	for(CROM*pChild=pTargetNode->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
		if(pChild->IsLeaf())continue;
		FindHierarch(pChild, pNewSourceParentNode, level+1, fixGenerator);
	}
}



class CROMData_less {
public:
	inline bool operator()(CROMData* s1, CROMData* s2) const {
		return Less(s1, s2);
	}
};

int Normalization::removeDuplicateChildren(CROMData*_this)
{
	int n=0;
	for(CROM*childDtd=_this->GetDTDNode()->GetFirstChild(); childDtd; childDtd=childDtd->GetNextSibling())
	{
		if(!childDtd->IsLeaf()){
			if(childDtd->IsNormalizationFixGenerator())continue;
			vector<CROMData*> vChildren;
			for(CROMData*node=_this->GetFirstChild(); node; node=node->GetNextSibling()){
				if(node->GetDTDNode()==childDtd)vChildren.push_back(node);
			}
			if(vChildren.size()<2)continue;
			std::set<CROMData*,CROMData_less>set;
			for(vector<CROMData*>::iterator it=vChildren.begin(); it!=vChildren.end(); ++it){
				CROMData* data=*it;
				std::set<CROMData*,CROMData_less>::iterator sit=set.find(data);
				if(sit!=set.end()){                       
					CROMData*duplicate=data, *node=*sit, *next;
#if DEBUG_FLAT_HIERARCH
	getViewLog()->DebugString("CROMData*CROM::CreateDataNode duplicate=\"%s\" lInstance=%d\n",
		(duplicate?(const char*)duplicate->GetName():"0"), duplicate->GetInstanceNumber());
#endif
					for(CROMData*subNode=duplicate->GetFirstChild(); subNode; subNode=next){
						next=subNode->GetNextSibling();;
						if(!subNode->IsLeaf()){
							CROMData* prev=subNode->GetPreviousSibling();
							if(prev){
								prev->SetNextSibling(next);
							}
							else {
								duplicate->SetFirstChild(next);
							}
							if(next)next->SetPreviousSibling(prev);
							node->AddChildNode(subNode);
							n++;
						}
					}
					duplicate->DeleteNode();
				}
				else {
					set.insert(data);
				}
			}
		}
	}

	for(CROMData*child=_this->GetFirstChild(); child; child=child->GetNextSibling()){
		if(!child->IsLeaf()){
			n+=removeDuplicateChildren(child);
		}
	}
	return n;
}


long long Normalization::createNormalizedDataTree(CROMData*pDataRoot, const std::string& sourceDataFile, long long& lSourceSize)
{
	PROFILING doPrf(m_pTreeMapper,"createNormalizedDataTree");
	getViewLog()->DebugString("createNormalizedDataTree() begin, source data file: \"%s\".\n", sourceDataFile.c_str());
	last_data_node_map.clear();
	if(pDataRoot==0){
		throw konga::CrException("Internal error in createNormalizedDataTree(), pDataRoot=0.");
	}
	pDataRoot->Clear();
	pDataRoot->SetDTDNode(m_pNormalizedCromRoot);

	getViewLog()->DebugString("prepare to read\n");

	TextDocReader reader(sourceDataFile, m_pFlatSourceRoot, true);
	konga::JbNode*root=reader.parse_all();
	std::vector<konga::JbNode*>& flatRecords=root->getChildren();
	const int nRecords = flatRecords.size();
	CROMData*node = 0;
	std::vector<std::string> values;
	std::vector<bool> bIsNulls;

	int nRecordToDo=nRecords;
	if(nRecordToDo==0)nRecordToDo=1;

	for(long lCurrentRowNo=1; lCurrentRowNo<=nRecordToDo; ++lCurrentRowNo)
	{
		if(nRecords>0){
			konga::JbNode* child=0;
			child=flatRecords.at(lCurrentRowNo-1);
			reader.parseJbNode(child, m_pFlatSourceTable, values, bIsNulls);

			for( int i = 0 ; i < m_n ; i++ )
			{
				CROM*att = m_de_list[i] ;
				if(i<values.size()){
					att->SetNullFlag(bIsNulls.at(i));
					att->SetValue(values.at(i).c_str());
				}
				else {
					att->SetNullFlag();
				}
					                                                                      
				if ( att->IsNull() && !m_bIsDb)
				{
					att->set_Value((const char*)att->GetDefaultValue());
					att->SetNullFlag(false);
				}
			}
		}
		
		for(CROM* pNewSourceNode=m_pNormalizedCromRoot->GetFirstChild(); pNewSourceNode; pNewSourceNode=pNewSourceNode->GetNextSibling()){
			                                                       
			CROMData* pLastSibling=pDataRoot->GetLastChild();	
			node=createDataTree(pDataRoot, pLastSibling, pNewSourceNode, lCurrentRowNo);
		}
	}

	if(getViewLog()->IsDebugLogEnabled()){
	                               
		if(node==0 && nRecords==0)
		{
			getViewLog()->DebugString("warning: SOURCE file is empty");
		}
		FILE*out1=fopen((m_pTreeMapper->getDebugFileName()+".normtree1").c_str(),"w+");
		konga::AutoFileHandle adopt_out1(out1);
		if(out1){
			pDataRoot->write(out1);
		}
		getViewLog()->DebugString( "createNormalizedDataTree - SUCCESS END\n");
	}

	removeDuplicateChildren(pDataRoot);

	if(getViewLog()->IsDebugLogEnabled()){
	                               
		if(node==0 && nRecords==0)
		{
			getViewLog()->DebugString("warning: SOURCE file is empty");
		}
		FILE*out1=fopen((m_pTreeMapper->getDebugFileName()+".normtree2").c_str(),"w+");
		konga::AutoFileHandle adopt_out1(out1);
		if(out1){
			pDataRoot->write(out1);
		}
		getViewLog()->DebugString( "createNormalizedDataTree - SUCCESS END\n");
	}

	long lErr = 0;
	lSourceSize = konga::GetFileSize(sourceDataFile, lErr);
	return nRecords;
}


void Normalization::AddAttributesFromTargetNode(CROM* pNewSourceNode, CROM* pTargetNode){              
	CTransform* pTrans=pTargetNode->getTrans();
	if(pTrans==0)return;
	
	int n=pTrans->getInputDEListSize();
	if(n>0){
		CROM* pNewSourceAttribute=0;
		for(int i=0; i<n; ++i){
			konga::DataElement* pDE=pTrans->getInputDEOf(i);
			const char*name=pDE->getName();
			if(name==0)continue;
			if(pNewSourceNode->FindObjectInChild(name, CROM_t_Attribute)!=0)continue;
			std::map<std::string,CROM*>::const_iterator cit=mapName2Flat.find(name);
			if(cit==mapName2Flat.end()){
				std::string errmsg(std::string("CROM::AddAttributesFromTargetNode error, failed find attribute %s from flat table")+name);
				throw konga::CrException(errmsg);
			}
			CROM* pOldSourceAttribute=cit->second;
			pNewSourceAttribute=new CROM(m_pTreeMapper, pNewSourceNode, pNewSourceAttribute, name, 0, CROM_t_Attribute, pOldSourceAttribute->get_CROM_ext_type());
			pNewSourceAttribute->markMapFlag();
			getViewLog()->DebugString("Normalization::AddAttributesFromTargetNode create pNewSourceAttribute=%s, parent=%p %s\n",
				(const char*)pNewSourceAttribute->GetName(), pNewSourceNode, (const char*)pNewSourceNode->GetName());
			pNewSourceAttribute->set_ValueType((const char*)pOldSourceAttribute->GetValueType());
			pNewSourceAttribute->set_SqlDataType(pOldSourceAttribute->get_SqlDataType());
		}
	}
	
	for(CROM*pChild=pTargetNode->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
		if(!pChild->IsLeaf() && !(pChild->isLoopNode()|| isSubElementOfOneOfNode(pChild))){
			AddAttributesFromTargetNode(pNewSourceNode, pChild);                                     
		}
	}
}


CROMData* Normalization::createDataTree(CROMData*parent_node,
								   CROMData*prev_sibling,
								   CROM*dtd,
								   int iInstance)
{
#if DEBUG_FLAT_HIERARCH
	getViewLog()->DebugString("CROMData*CROM::CreateDataNode parent_node=\"%s\", prev_sibling=\"%s\"\n",
		parent_node?(const char*)parent_node->GetName():"0",prev_sibling?(const char*)prev_sibling->GetName():"0");
#endif

	CROMData* last_data_node=last_data_node_map[dtd];
	CROMData*data_node;
	if(last_data_node!=0 && dtd->isLoopNode()==false){
		data_node=last_data_node;
	}
	else {
		data_node=createDataNode(parent_node, prev_sibling, dtd);
	}

#if DEBUG_FLAT_HIERARCH
	getViewLog()->DebugString("new CROMData data_node=\"%s\"\n",
		(const char*)data_node->GetName());
#endif
	bool same_as_last = twoNodesAreEqual(last_data_node, data_node);
#if DEBUG_FLAT_HIERARCH
	getViewLog()->DebugString("same_as_last=\"%s\"\n",
		same_as_last?"true":"false");
#endif
	if(dtd->IsNormalizationFixGenerator() || !same_as_last){
		last_data_node_map[dtd]=data_node;
		data_node->SetInstanceNumber(iInstance);
	} else if(data_node!=last_data_node){
#if DEBUG_FLAT_HIERARCH
		getViewLog()->DebugString("deleting data_node=\"%s\" iInstance=%d\n",
		(const char*)data_node->GetName(), iInstance);
#endif
		data_node->DeleteNode();
		data_node=last_data_node;
	}

	bool bLoopOneOfChild=isLoopOneOfNode(dtd);
	for(CROM* pChild=dtd->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
		if(pChild->IsLeaf())continue;
		if(bLoopOneOfChild){
			if(data_node->GetFirstChild()){
				data_node=createDataNode(parent_node, data_node, dtd);
				data_node->SetInstanceNumber(iInstance);
			}
			createDataTree(data_node, 0, pChild, iInstance);
		}
		else {
			CROMData*child_data_node=(CROMData*)data_node->GetLastChild();
			child_data_node=createDataTree(data_node, child_data_node, pChild, iInstance);
		}
	}
	return data_node;
}

unsigned int FlatTableHashFunction(void *s1)
{
	const char*s=(const char*)s1;
	int f=0;
	for(int i=0;i<40&&s[i]!=0;i++){
		f=(f*128+toupper(s[i]))%FLAT_TABLE_HASHTABLE_SIZE;
	}
	return f;
}

                                                                                      
CROMData* Normalization::createDataNode(CROMData*parent_data_node,
										CROMData*prev_sibling,
										CROM*pCrom)
{
	CROMData* data_node=new CROMData(parent_data_node, prev_sibling, pCrom, "");
	CROMData* attribute_data_node=0;
	for(CROM*pChild=pCrom->GetFirstChild(); pChild && pChild->IsLeaf(); pChild=pChild->GetNextSibling()){
		if(pChild->IsAttribute()){
			const char*name=pChild->GetName();
			std::map<std::string,CROM*>::const_iterator cit=mapName2Flat.find(name);
			if(cit==mapName2Flat.end()){
				throw konga::CrException("Internal error, the normalized dtd node is missing from the normalized to flat CROM map");
			}
			CROM*pFlatAttribute=cit->second;
			if(pFlatAttribute->IsNull()==false){
				const char*value=pFlatAttribute->GetValue();
				attribute_data_node=new CROMData(data_node, attribute_data_node, pChild, value);
			}
		}
	}
	return data_node;
}
