/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/DOMNodeListPtr.h>

#include <konga/Db/TranDb/SourcesTab.h>
#include <konga/DataElement/DataElement.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "DTDParser.h"
#include "Transform.h"
#include "TxtParser.h"
#include "CRXmlParser.h"
#include "Table.h"
#include "CRXmlList.h"
#include "XmlDocument.h"

using namespace konga;
using namespace konga::xml;

#ifdef LoadString
#undef LoadString
#endif

#define DEBUG_XMLXMLMAP

const bool includeSubElements=true;   
		
int CollectAllChildNode(CROM*targetNode,std::vector<CROM*>&childNodeList)
{
	if(targetNode->isLoopNode()){
		childNodeList.push_back(targetNode);
	}
	CROM*child=targetNode->GetFirstChild();
	while(child){
		if((child->m_Type&CROM_t_AttValuePrecondition)==0){
			childNodeList.push_back(child);
		}
		child=child->GetNextSibling();
	}
	return 0;
}


bool TreeMapper::PassPreconditionCheck(CROM*pTargetNode, int& err)
{
	err=0;
	CROM* pCondition = pTargetNode->FindPrecondition();
	if(!pCondition)return true;
	CTransform* pTrans=pTargetNode->getTrans();
	const char* path_to_evaluate = pCondition->getSourceExpr();
	if(!path_to_evaluate || !path_to_evaluate[0])return true;
	TALIST* talist = pCondition->getTAList();
	KString value;

	const char* sDataTypeStr("bool"), *sFormat((const char*)pCondition->GetFormat());
	err=pTrans->Evaluate_dbg(path_to_evaluate, talist, sDataTypeStr, 0, sFormat, value, pCondition);
	if(err               )
	{
		m_bErrorsInOutput=true;
	}

	if(err!=0||(strcmp("true",(const char*)value)!=0 && strcmp("1",(const char*)value)!=0)){
		return false;
	}
	return true;
    	
}

namespace {
	bool is_mapped(CROM*targetNode){
		for(CROM*child=targetNode->GetFirstChild();child;child=child->GetNextSibling()){
			if(child->IsLeaf()){
				if(child->getSourceExpr()!=0)return true;
			} else {
				if(is_mapped(child))return true;
			}
		}
		return false;
	}
}


void TreeMapper::Create_ANY_ELEMENT(CROM*targetNode, KString&err_msg)
{
	int err=0;
	KString value;
	CTransform*pTrans=targetNode->getTrans();
	for(CROM*childNode=targetNode->GetFirstChild(); childNode && childNode->IsLeaf(); childNode=childNode->GetNextSibling()){
		if((childNode->m_Type&CROM_t_Value)==0)continue;
		err=0;
		const char* path_to_evaluate=childNode->getSourceExpr();
		if(!path_to_evaluate || !path_to_evaluate[0])continue;
		TALIST* talist=childNode->getTAList();
		err=pTrans->Evaluate_dbg(path_to_evaluate, talist, "", 0, "", value, childNode);
		if(err){
			if ( err != TRANSFORM_INPUT_EMPTY ){
				m_bErrorsInOutput=true;
				err_msg.Format("Create target ANY_ELEMENT %s failed, %s\n", (const char*)childNode->get_Path(), (const char*)pTrans->m_lastErr);
			}
		} else {
			std::string xmlHeader;
			childNode->SetValue(value);
			childNode->SetNullFlag(false);
			break;
		}		
    }
}

unsigned long TreeMapper::AddValueAttributeCROMs(CROM*pTargetNode, bool& hasAttributes, bool& hasValue)
{
	unsigned long hr = S_OK;
	hasAttributes=false;
	hasValue=false;
	int err;
	DOMNodePtr attributeDataNode=0;
	KString value;
	CTransform* pTrans=pTargetNode->getTrans();
	const char* path_to_evaluate=0;
	CROM* nilAttribute=pTargetNode->getXmlNilAttribute();
	if(nilAttribute){
		nilAttribute->SetNullFlag();
		if (nilAttribute->isMapped()) nilAttribute=0;
	}
	for(CROM*childNode=pTargetNode->GetFirstChild(); childNode && childNode->IsLeaf(); childNode=childNode->GetNextSibling()){
		err=0;
		if(!childNode->IsLeaf())continue;
		if(childNode->IsPrecondition())continue;
		if(childNode!=nilAttribute)childNode->SetNullFlag();
		path_to_evaluate=childNode->getSourceExpr();
		if(path_to_evaluate && path_to_evaluate[0]){
			const char* sDataTypeStr(childNode->GetValueType()), *sFormat((const char*)childNode->GetFormat());
			int sqlDataType=childNode->get_SqlDataType();
			TALIST* talist=childNode->getTAList();
			err=pTrans->Evaluate_dbg(path_to_evaluate, talist, sDataTypeStr, sqlDataType, sFormat, value, childNode);
			if(err){
				if ( err == TRANSFORM_INPUT_EMPTY || err==-1|| err == TARGET_MAPPED_TO_NULL ){
					if( (err == TRANSFORM_INPUT_EMPTY || err == -1) && childNode->hasDefault() )
					{
						childNode->SetValue(childNode->GetDefaultValue());
						childNode->SetNullFlag(false);
						err = 0;
					}
					else {
						childNode->SetValue("");
						childNode->SetNullFlag();
						err=0;
						                   
						                                                    
						                                       
						                                                    
						   
						if(nilAttribute && !hasValue && m_bIncludeNullXmlElement)hasAttributes=true;
					}
				}
				else {
					m_bErrorsInOutput=true;
					getErrorLog()->MakeError(__FILE__, __LINE__, ID_ATTRIBUTENODES_EVALUATE_FAILED, (const char*)childNode->get_Path(), (const char*)pTrans->m_lastErr);
					m_lastErr=getErrorLog()->GetLastErr();
				}
			} else {
				if((childNode->m_Type&CROM_t_Value)!=0){
					                                                         
					hasValue=!value.IsEmpty();
					if(m_bIncludeEmptyXmlElement)hasValue=true;
					childNode->SetValue((const char*)value);
					childNode->SetNullFlag(!hasValue);
					                   
					                                                    
					                                       
					   
					if(nilAttribute && !hasValue && m_bIncludeNullXmlElement)hasAttributes=true;
				}
				else if((childNode->m_Type&CROM_t_Attribute)!=0)
				{
					childNode->SetValue((const char*)value);
					childNode->SetNullFlag(value.IsEmpty());
					if(value.IsEmpty()==false){
						hasAttributes=true;
					};
				}
			}
		}
		else if((childNode->m_Type&CROM_t_Attribute)!=0 && childNode->IsFixedValue()){
			childNode->SetValue((const char*)childNode->GetDefaultValue());
			childNode->SetNullFlag(false);
		}
		
    }
	return hr;
}


bool TreeMapper::AddValueCROMs(CROM* targetNode)
{
	int err;
	for(CROM*childNode=targetNode->GetFirstChild(); childNode && childNode->IsLeaf(); childNode=childNode->GetNextSibling()){
		err=0;
		if(childNode->IsValueLeaf()){
			const char* path_to_evaluate=childNode->getSourceExpr();
			KString value;
			if(path_to_evaluate && path_to_evaluate[0]){
				const char* sDataTypeStr(childNode->GetValueType()), *sFormat((const char*)childNode->GetFormat());
				int sqlDataType=0;
				TALIST* talist=childNode->getTAList();
				CTransform* pTrans=targetNode->getTrans();
				err=pTrans->Evaluate_dbg(path_to_evaluate, talist, sDataTypeStr, sqlDataType, sFormat, value, childNode);
				if(err==0){
					childNode->SetValue((const char*)value);
					childNode->SetNullFlag(false);
					return true;
				}
			}
		}
	}
	return false;
}
                                                                          
                                                                      
                                                                
                      
                                                                              
                                       
                                                                                    
                                                                  
                                                               
                                                                                   
unsigned long TreeMapper::DoXmlMap(FILE*out, CROM*targetNode, CRData*beginSourceData, int& nTargetNodeCreated, FILE*lnk, FILE*update_source_file, bool bXmlIndent)
{
    unsigned long hr=S_OK;
	nTargetNodeCreated=0;
	if(!targetNode->hasMapped() && (targetNode->IsCollection() || targetNode->IsOptionalNode()))return hr;

	const int targetNodeType=targetNode->GetType();
    int err=0;
    CROM*childNode=0;
    KString path_to_evaluate,elementPath,value;
	KString tmp;
	bool bDerivedTypeHolder=(targetNodeType&CROM_t_DerivedTypeHolder)!=0;
	bool bTypeDefinition=(targetNodeType&CROM_t_TypeDefinition)!=0;
	bool bSubstitutionGroup=targetNode->GetParent()!=0&&(targetNode->GetParent()->GetType()&CROM_t_SubstitutionGroupHead)!=0;
	bool bSubstitutionGroupHead=(targetNodeType&CROM_t_SubstitutionGroupHead)!=0;
	bool bIsAnyElement=targetNode->IsAnyElement();
	CTransform* pTrans=targetNode->getTrans();
	pTrans->m_sourceData=beginSourceData;

	bool hasValueNode;

	if((targetNodeType&CROM_t_Loop)==0||targetNode->m_Generator==0){
		if(bDerivedTypeHolder||bSubstitutionGroupHead){                                 
			targetNode->writeBeginRecord(out,lnk,update_source_file, hasValueNode, bXmlIndent);
			for(childNode=targetNode->GetFirstChild();childNode;childNode=childNode->GetNextSibling()){
				if((childNode->GetType() & CROM_t_AttValuePrecondition)!=0)continue;
				int nChildDataNode;
				if((hr=DoXmlMap(out, childNode,
					beginSourceData, nChildDataNode, lnk, update_source_file, bXmlIndent))!=S_OK)return hr;
				if(nChildDataNode>0){
					nTargetNodeCreated++;
					break;
				}
			}
		}
		else if((targetNodeType&CROM_t_Element)!=0||bTypeDefinition){                                        
			if((bTypeDefinition||bSubstitutionGroup)&&!is_mapped(targetNode))return S_OK;

			FillDataElements(beginSourceData, targetNode);
			pTrans->setCount(targetNode);
			if(!PassPreconditionCheck(targetNode, err))return hr;
			if(bTypeDefinition){
				targetNode->add_xsi_type_attributes();
			}
			else if(bIsAnyElement){
				KString err_msg;
				Create_ANY_ELEMENT(targetNode, err_msg);
				if(!err_msg.IsEmpty()){
					AppendErrorMessage(err_msg);
				}
				else {
					nTargetNodeCreated++;
					targetNode->writeBeginRecord(out, lnk, update_source_file, hasValueNode, bXmlIndent);
					targetNode->writeEndRecord(out);
				}
			}

			if(!bIsAnyElement){
				bool hasAttributes;
				bool hasValue;
				if(FAILED(hr = AddValueAttributeCROMs(targetNode, hasAttributes, hasValue)))
					return hr;

				targetNode->writeBeginRecord(out, lnk, update_source_file, hasValueNode, bXmlIndent);
				
				int nSubNode=0;
				for(childNode=targetNode->GetFirstChild();childNode;childNode=childNode->GetNextSibling()){
					if(!childNode->IsLeaf()){
						int nChildDataNode;
						if(FAILED(hr=DoXmlMap(out, childNode, beginSourceData, nChildDataNode, lnk, update_source_file, bXmlIndent)))return hr;
						nSubNode+=nChildDataNode;
					}
				}

				if(!nSubNode && !hasValueNode && !hasAttributes && targetNode->IsOptionalNode()){
					targetNode->eraseRecord(out, lnk, update_source_file);
					if(lnk){
						fprintf(lnk, "%s,%s,skip\n",
							(const char*)targetNode->getSourceInstanceTag(),
							(const char*)targetNode->get_Path());
					}
				}
				else {
					if(nSubNode || hasValueNode || hasAttributes)nTargetNodeCreated++;
					targetNode->writeEndRecord(out, bXmlIndent, nSubNode>0,hasValueNode);
				}
			}
		}
		else {                   
			bool isOneOf=(targetNodeType&CROM_t_OneOf)!=0 && (m_bUseOldRuleForLoopOneOfXmlNode|| !targetNode->isLoopNode());
			FillDataElements(beginSourceData, targetNode);
			bool pass=PassPreconditionCheck(targetNode, err);
			if(!pass && !isOneOf)return hr;
			bool hasValue=false;
			int nSubNode=0;
			if(pass && isOneOf){
				hasValue=AddValueCROMs(targetNode);
			}
			if(hasValue){
				targetNode->writeBeginRecord(out, lnk, update_source_file, hasValueNode, bXmlIndent);
			}
			else {
				targetNode->writeBeginRecord(out,lnk,update_source_file, hasValueNode, bXmlIndent);
				nSubNode=0;
				for(childNode=targetNode->GetFirstChild();childNode;childNode=childNode->GetNextSibling()){
					int childType=childNode->GetType();
					if((childType&CROM_t_AttValuePrecondition)==0){
						int nChildDataNode;
    					if((hr=DoXmlMap(out, childNode, beginSourceData, nChildDataNode, lnk, update_source_file, bXmlIndent))!=S_OK)return hr;
						nSubNode+=nChildDataNode;
						if(isOneOf){
							if(nChildDataNode){
								break;
							}
							else {
								childNode->eraseRecord(out, lnk, update_source_file);
							}
						}
					}
				}
			}
			if(!nSubNode && !hasValue && targetNode->IsOptionalNode()){
				targetNode->eraseRecord(out, lnk, update_source_file);
				if(lnk){
					fprintf(lnk, "%s,%s,skip\n",
						(const char*)targetNode->getSourceInstanceTag(),
						(const char*)targetNode->get_Path());
				}
			}
			else {
				if(nSubNode || hasValue)nTargetNodeCreated++;
				targetNode->writeEndRecord(out, bXmlIndent, nSubNode>0,hasValue);
			}

		}
	} else {        
		std::vector<CRData*> instances;
		CROM* generator=targetNode->m_Generator;
		beginSourceData->collectInstances(instances, generator->get_Path(), targetNode, err);
		do {
			int nInstances=instances.size();
			if(nInstances==0)continue;
			if(pTrans)pTrans->SetAutoNumber(1);

			if(bSubstitutionGroupHead || bDerivedTypeHolder){
				int nSubNode=0;
    			for(int iInstance=0;iInstance<nInstances;iInstance++)
    			{
					targetNode->setInstanceNumber(nTargetNodeCreated);
					CRData*curSourceData=instances.at(iInstance);
					pTrans->setCount(iInstance);
					pTrans->m_sourceData=curSourceData;

					FillDataElements(curSourceData, targetNode);
					bool doThisInstance=PassPreconditionCheck(targetNode, err);
					if(!doThisInstance)continue;
					targetNode->writeBeginRecord(out, lnk, update_source_file, hasValueNode, bXmlIndent);
					int nSubNode=0;
					for(childNode=targetNode->GetFirstChild();childNode;childNode=childNode->GetNextSibling()){
						if((childNode->GetType()&CROM_t_AttValuePrecondition)!=0)continue;
						int nChildDataNode;
						if((hr=DoXmlMap(out, childNode, curSourceData, nChildDataNode, lnk, update_source_file, bXmlIndent))!=S_OK)return hr;
						nSubNode+=nChildDataNode;
						if(nChildDataNode>0)break;
					}
					if(nSubNode>0){
						pTrans->SetAutoNumber();
						nTargetNodeCreated++;
					}
					else {
						targetNode->eraseRecord(out, lnk, update_source_file);
					}
				}
			}
			else if((targetNodeType&CROM_t_Element)){                
    			for(int iInstance=0;iInstance<nInstances;iInstance++)
    			{
					targetNode->setInstanceNumber(nTargetNodeCreated);
					CRData* curSourceData=instances.at(iInstance);
					pTrans->setCount(iInstance);
					pTrans->m_sourceData=curSourceData;

					FillDataElements(curSourceData, targetNode);
					bool doThisInstance=PassPreconditionCheck(targetNode, err);

					if(doThisInstance){
						if(bIsAnyElement){
							KString err_msg;
							Create_ANY_ELEMENT(targetNode, err_msg);
							if(!err_msg.IsEmpty())AppendErrorMessage(err_msg);
							targetNode->writeBeginRecord(out, lnk, update_source_file, hasValueNode, bXmlIndent);
							nTargetNodeCreated++;
						}
						else {
							bool hasAttributes, hasValue;
							if(FAILED(hr=AddValueAttributeCROMs(targetNode, hasAttributes, hasValue))){
								return hr;
							}
							targetNode->writeBeginRecord(out, lnk, update_source_file, hasValueNode);

							int nSubNode=0;
							for(childNode=targetNode->GetFirstChild();childNode!=0;childNode=childNode->GetNextSibling()){
								if((childNode->GetType()&CROM_t_AttValuePrecondition)!=0)continue;
								int nChildDataNode;
								if((hr=DoXmlMap(out, childNode, curSourceData, nChildDataNode, lnk, update_source_file, bXmlIndent))!=S_OK)return hr;
								nSubNode+=nChildDataNode;
								if(bSubstitutionGroupHead && nChildDataNode>0){
									break;
								}
							}
							if(!hasAttributes && !hasValue && !nSubNode && targetNode->IsCollection()){
								targetNode->eraseRecord(out, lnk, update_source_file);
								if(lnk){
									fprintf(lnk, "%s,%s,skip\n",
										(const char*)targetNode->getSourceInstanceTag(),
										(const char*)targetNode->get_Path());
								}
							}
							else {
								targetNode->writeEndRecord(out, bXmlIndent, nSubNode>0,hasValue);
								if(nSubNode || hasAttributes || hasValue)nTargetNodeCreated++;
								pTrans->SetAutoNumber();
							}
						}
					}
					else {
						fprintf(lnk, "%s,%s,skip\n",
							(const char*)targetNode->getSourceInstanceTag(),
							(const char*)targetNode->get_Path());
					}
    			}
			}
			else {              
				const bool isOneOfNode=(targetNodeType&CROM_t_OneOf)!=0;
				const bool createMoreThanOneOfInstance=isOneOfNode && !m_bUseOldRuleForLoopOneOfXmlNode;
				                                                                
				pTrans->SetAutoNumber(1);
    			for(int iInstance=0;iInstance<nInstances;iInstance++)
    			{
					CRData*curSourceData=instances.at(iInstance);
					pTrans->setCount(iInstance);
					pTrans->m_sourceData=curSourceData;

					FillDataElements(curSourceData, targetNode);
					bool pass=PassPreconditionCheck(targetNode, err);
					                                                                                                   
					if(!pass)continue;

					targetNode->writeBeginRecord(out,lnk,update_source_file, hasValueNode);

					bool hasValue=false;
					int nSubNode=0;
					for(childNode=targetNode->GetFirstChild();childNode;childNode=childNode->GetNextSibling()){
						int nChildDataNode=0;
						if(childNode->IsValueLeaf()){
							if(pass){
								hasValue=AddValueCROMs(targetNode);
								targetNode->writeBeginRecord(out, lnk, update_source_file, hasValueNode);
							}
							nSubNode+=nChildDataNode;
						} else if((childNode->GetType()&CROM_t_AttValuePrecondition)==0){
							CRData*curSourceData1=curSourceData;
							if(IsFlatSource()&& targetNode->IsOneOfNode()){
								const char* childNodeName=(const char*)childNode->GetName();
								curSourceData1=curSourceData->getChildNode(childNodeName, false);
								if(curSourceData1==0)continue;
							}
							if(createMoreThanOneOfInstance){
								targetNode->writeBeginRecord(out,lnk,update_source_file, hasValueNode);
							}
							if((hr=DoXmlMap(out, childNode, curSourceData1, nChildDataNode, lnk, update_source_file, bXmlIndent))!=S_OK)return hr;
							nSubNode+=nChildDataNode;
						}
						if(createMoreThanOneOfInstance){
							if(nChildDataNode==0)
								targetNode->eraseRecord(out,lnk,update_source_file);
							else {
								nTargetNodeCreated++;
								pTrans->SetAutoNumber();
							}
						}
						else if(isOneOfNode && nChildDataNode>0)break;
					}
					if(!createMoreThanOneOfInstance){
						if(nSubNode){
							nTargetNodeCreated++;
							pTrans->SetAutoNumber();
						}
						else {
							targetNode->eraseRecord(out,lnk,update_source_file);
						}
    				}
				}
			}
		} while(readNextBatch(beginSourceData, generator, instances));
	}

    return hr;
}

void TreeMapper::createXmlInstance(const CROM*targetNode){
}

