/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringTokenizer.h>
#include <konga/DataElement/DataElement.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "DTDParser.h"
#include "Transform.h"
#include "TxtParser.h"
#include "CRXmlParser.h"
#include "CROMData.h"

                                  
#ifdef LoadString
#undef LoadString
#endif

using namespace konga;
using std::string;

unsigned long TreeMapper::DoHfMap_Write2Crom(FILE*out, CROM*targetNode, CRData*beginSourceData, int&nTargetNodeCreated, FILE* lnk, FILE*update_source_file,
											 long long& nTotalRecords, konga::Utf8Conv* conv)
{
    unsigned long hr=S_OK;
	if(!targetNode->hasMapped() && (targetNode->IsCollection() || targetNode->IsOptionalNode()))return hr;
                                       
    CROM*childNode;
    KString tmp,endPath;
    KString path,path_to_evaluate,value,dataTypeStr;
	nTargetNodeCreated=0;
	int err=0;
	CTransform* pTrans=targetNode->getTrans();
	bool hasValue=false;

	if (!targetNode->isLoopNode() || IsNoSource() ||targetNode->m_Generator==0)           
	{ 
		FillDataElements(beginSourceData, targetNode);
		pTrans->setCount(targetNode);
		pTrans->m_sourceData=beginSourceData;
		int nAttributeCreated=0, nSubNode=0;
		if(PassPreconditionCheck(targetNode, err)){
			if (FAILED(hr = AddValueAttributeCROMs(targetNode, nAttributeCreated)))
			{
				return hr;
			}
			targetNode->writeBeginRecord(out, lnk, update_source_file, hasValue, false, conv);
			                                
			                                                                  
    		for(childNode=targetNode->GetFirstChild(); childNode; childNode=childNode->GetNextSibling())
			{
				if (childNode->IsElement())
				{
					                                                                  
					                                      
					                                                                        
					int nChildNodeCreated;
    				if ((hr = DoHfMap_Write2Crom(out, childNode, beginSourceData, nChildNodeCreated, lnk, update_source_file, nTotalRecords, conv)) != S_OK)
						return hr;
					nSubNode+=nChildNodeCreated;
    			}
    		}

			                                                                    
			if(nAttributeCreated==0 && nSubNode==0 && targetNode->IsOptionalNode())
			{
				targetNode->eraseRecord(out, lnk, update_source_file);
				if(lnk){
					fprintf(lnk, "%s,%s,%s,skip\n",
						(const char*)beginSourceData->GetDTDNode()->get_Path(),
						beginSourceData->getInstanceListing().c_str(),
						(const char*)targetNode->get_Path());
				}
			}
			else {
				nTotalRecords += nSubNode;
				targetNode->writeEndRecord(out);
				if(nAttributeCreated || nSubNode)nTargetNodeCreated++;
			}
		}
		else {
			if(lnk){
				fprintf(lnk, "%s,%s,%s,skip\n",
					(const char*)beginSourceData->GetDTDNode()->get_Path(),
					beginSourceData->getInstanceListing().c_str(),
					(const char*)targetNode->get_Path());
			}
		}
		                                                                                            
		                                                    
		             
		                                                                              
		                                                                                       
		                            
		             
		   
	}
	else                
	{
		int err;
		std::vector<CRData*> instances;
		CROM* pGenerator=targetNode->m_Generator;
		beginSourceData->collectInstances(instances, pGenerator->get_Path(), targetNode, err);
		do {
			int nInstances=instances.size();
			if(nInstances==0)continue;

			pTrans->SetAutoNumber(1);
			
			if (!targetNode->m_Generator && !IsNoSource())
			{
				hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_DOHFHFMAP_GENERATOR_EMPTY, (const char*)targetNode->get_Path());
				m_lastErr=getErrorLog()->GetLastErr();
				return hr;
			}

    		for(int iInstance=0; iInstance<nInstances; iInstance++)
			{
				targetNode->setInstanceNumber(nTargetNodeCreated);
				CRData*curSourceData=instances.at(iInstance);
				pTrans->setCount(iInstance);
				pTrans->m_sourceData=curSourceData;

				if(curSourceData)FillDataElements (curSourceData, targetNode);
				int nAttributeCreated=0, nSubNode=0;
				if(PassPreconditionCheck(targetNode, err)){
					if (FAILED(hr = AddValueAttributeCROMs(targetNode, nAttributeCreated)))
						return hr;

					targetNode->writeBeginRecord(out, lnk, update_source_file, hasValue, false, conv);

    				for (childNode=targetNode->GetFirstChild(); childNode; childNode=childNode->GetNextSibling())
					{
						if (childNode->IsElement()){
							int nChildNodeCreated;
							hr=DoHfMap_Write2Crom(out, childNode, curSourceData, nChildNodeCreated, lnk, update_source_file, nTotalRecords, conv);
							if(FAILED(hr))return hr;
							nSubNode+=nChildNodeCreated;
						}
    				}

					                                                                    
					if(nAttributeCreated==0 && nSubNode==0)
					{
						targetNode->eraseRecord(out, lnk, update_source_file);
						if(lnk){
							fprintf(lnk, "%s,%s,%s,skip\n",
								(const char*)curSourceData->GetDTDNode()->get_Path(),
								curSourceData->getInstanceListing().c_str(),
								(const char*)targetNode->get_Path());
						}
					}
					else {
						nTotalRecords += nSubNode;
						pTrans->SetAutoNumber();
						targetNode->writeEndRecord(out);
						nTargetNodeCreated++;
					}
				}
    		}

		} while(beginSourceData && readNextBatch(beginSourceData, pGenerator, instances));

		if(m_targetType != LdapTypeId && nTargetNodeCreated==0 && targetNode->IsAtLeastOne()){
			hr=E_FAIL;
			KString sErr="Failed to create a target node for ["+targetNode->get_Path()+"]. Fatal error, according to the structure definition the node must have at least one instance.";
			AppendErrorMessage(sErr);
			return hr;
		}
	}

    return hr;
}

int HfLoop::gatherLoopNode(CROMData*parent,KString&token,int n_instance){
	int n=0;
	CROMData*child=parent->GetFirstChild();
	while(child){
		if(child->IsNotALeaf()&&child->GetName()==token){
			n++;
			if(n_instance==0||n==n_instance)Add(child);
		}
		child=child->GetNextSibling();
	}
	return n;
}


HfLoop*	TreeMapper::GetHfLoop(CROMData* pSrcStartNode, KString sEndPath, CROM* targetLoopNode0)
{
	CROMData	*pStartNode = pSrcStartNode;
	KString		sSearchPath;
	HfLoop		*pLoop,*pLoop0=new HfLoop(this);

	const KString& sBeginPath(pSrcStartNode->getPath());
	konga::StringTokenizer tk1(sBeginPath,"$."),tk2(sEndPath,"$.");
	char c;
	while(tk1.NextToken()){
		if(!tk2.NextToken()){
			tk1.StepBack();
			break;
		}
		if(strcmp(tk1.token(),tk2.token())){
			tk1.StepBack();
			tk2.StepBack();
			break;
		}
	}
	while(tk1.NextToken()){
		if((c=tk1.token()[0])=='.'||c=='$')continue;
		if(pStartNode==0){
			KString csErr;
			csErr.Format("TreeMapper::GetHfLoop(startNode=%s, endPath=%s) failed\n",(pSrcStartNode==0)?"<null>":(const char*)pSrcStartNode->GetName(),(const char*)sEndPath);
			AppendErrorMessage(csErr);
			return 0;
		}
		pStartNode=pStartNode->GetParent();
	}
	if(pStartNode==0){
		KString csErr;
		csErr.Format("TreeMapper::GetHfLoop(startNode=%s, endPath=%s) failed\n",(pSrcStartNode==0)?"<null>":(const char*)pSrcStartNode->GetName(),(const char*)sEndPath);
		AppendErrorMessage(csErr);
		return 0;
	}
	pLoop0->Add(pStartNode);
	while(tk2.NextToken()){
		KString token=tk2.token();
		if((c=token.GetAt(0))=='.'||c=='$')continue;
		pLoop=new HfLoop(this);
		int pos=token.Find('#'),len=(int)token.GetLength();
		if(pos<0||(pos==(len-1))){                                   
			if(pos>0)token=token.Left(pos);
			for(int i=0;i<(int)pLoop0->m_n;i++){
				pLoop->gatherLoopNode(pLoop0->GetAt(i),token);
			}
		} else {                                   
			int n_instance=1,pos1=pos+1;
			if(token.GetAt(pos1)=='_'){
				n_instance=-1;
				pos1++;
			}
			KString num=token.Mid(pos1,len-pos1);
			n_instance=n_instance*atoi(num);
			token=token.Left(pos);
			for(int i=0;i<(int)pLoop0->m_n;i++){
				if(n_instance<0){
					HfLoop tmp(this);
					tmp.gatherLoopNode(pLoop0->GetAt(i),token);
					n_instance=tmp.m_n+n_instance+1;
					if(n_instance>0)pLoop->Add(tmp.GetAt(n_instance-1));
				} else {
					pLoop->gatherLoopNode(pLoop0->GetAt(i),token,n_instance);
				}

			}
		}
		delete pLoop0;
		pLoop0=pLoop;
	}
	pLoop0->m_EndPath=sEndPath;
	if(targetLoopNode0==0)return pLoop0;
	                                                                                                       
	                                                                                                               
	                                                                                              
	                                                                 
	int n=(int)pLoop0->m_n;
	for(int i=n-1;i>=0;i--){
		CROMData*source=pLoop0->at(i);
		CROM*targetLoopNode=source->GetDTDNode()->m_Generator;
		if(targetLoopNode!=0&&targetLoopNode!=targetLoopNode0)
		{
			pLoop0->Remove(source);
		}
	}
	return pLoop0;
}

CROMData* TreeMapper::FindInstance(CROMData* pSrcStartNode, KString sEndPath)
{
	CROMData	*pStartNode = 0;
	CROMData	*pReqInstance = 0;
	KString		sSearchPath;
	HfLoop		*pLoop;

	                                                                                                   
	                                                                                                     
	                                                                                        
	KString sCurPath = pSrcStartNode->GetNodePath();
	KString sStartPath;
	konga::StringTokenizer cur(sCurPath,".$");
	konga::StringTokenizer end(sEndPath,".$");

	while (cur.NextToken() && end.NextToken())
	{
		if(strcmp(cur.token(),end.token()))
		{
			break;
		}
		sStartPath += end.token();
	}

	while(pSrcStartNode->GetParent() != 0)
	{
		sCurPath = pSrcStartNode->GetNodePath();
		if (pSrcStartNode->ArePathsEqual(sStartPath))
			break;
		pSrcStartNode = pSrcStartNode->GetParent();
	}
	                                                                                                           
	                                                       
	pStartNode = GetLoopSearchParams(pSrcStartNode, sEndPath, sSearchPath);

	                                    
	pLoop = new HfLoop(pSrcStartNode, sEndPath, sSearchPath);

	if (1 == pLoop->m_n)
	{
		pReqInstance = pLoop->GetAt(0);
	}

#ifdef _DEBUG
	if (getViewLog()->IsDebugLogEnabled())
	{
		if (0 != pReqInstance)
		{
			KString Name = pReqInstance->GetDisplayName(), Value = pReqInstance->GetValue();
			getViewLog()->DebugString("FindInstance Node = '%s' Data = '%s'\n", (const char*)Name, (const char*)Value);
		}
		else
		{
			getViewLog()->DebugString("FindInstance got 0\n");
		}
	}
#endif

	return pReqInstance;
}
                   
CROMData* TreeMapper::GetLoopSearchParams(CROMData* pSrcStartNode, KString sEndPath, KString& sSearchPath)
{
	KString sCurPath = pSrcStartNode->GetNodePath();
	KString sCommonPath;
	konga::StringTokenizer tokCurPath(sCurPath, "$.");
	konga::StringTokenizer tokEndPath(sEndPath, "$.");

	                                                                     
	while (true == tokCurPath.NextToken() && true == tokEndPath.NextToken())
	{
		                                                                              
		if (strcmp(tokCurPath.token(), tokEndPath.token()) == 0)
		{
			sCommonPath += tokCurPath.token();
			continue;
		}
		else
		{
			                                               
			break;
		}
	}

	                                       
	sSearchPath = sEndPath.Right(sEndPath.GetLength() - sCommonPath.GetLength());

	                                                                              
	CROMData* pTravel = pSrcStartNode;

	while (0 != pTravel->GetParent())
	{
		if (pTravel->GetNodePath() == sCommonPath)
			return pTravel;
		pTravel = pTravel->GetParent();
	}
	
	return pTravel;
}

                                        
unsigned long TreeMapper::AddValueAttributeCROMs(CROM*pTargetNode, int&iCreatedNodes)
{
	unsigned long hr = S_OK;
	int err,iAdd;
	KString value, tmp;

	CTransform* pTrans=pTargetNode->getTrans();
	                                                                     
	for(CROM*pChild=pTargetNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling())
	{
		if(pChild->IsPrecondition())continue;
		TALIST* talist = pChild->getTAList();
		const char* path_to_evaluate = pChild->getSourceExpr();

		err=0;
		iAdd=1;
		bool bIsMappedToNull = false;
		value.Empty();
		if(path_to_evaluate && path_to_evaluate[0])
		{
			const char* sDataTypeStr(pChild->GetValueType()), *sFormat((const char*)pChild->GetFormat());
			int sqlDataType = pChild->get_SqlDataType();
			err=pTrans->Evaluate_dbg(path_to_evaluate, talist, sDataTypeStr, sqlDataType, sFormat, value, pChild);
			if(err) {
				handleEvaluateError(err, value, pChild, iAdd, bIsMappedToNull, TargetIsDatabase(), TargetIsLDAP(), pTrans, *getErrorLog());
				m_bErrorsInOutput = (err!=0&&err!=TRANSFORM_INPUT_EMPTY);			
			}
			if(value.Find(POST_PROCESS_IDENTITY) != -1 || value.Find(POST_PROCESS_SEQUENCE) != -1)
			{
				                                                                      
				iAdd = 0;
			}
		}
		                                                
		else if(pChild->IsForeignKey())
		{
			                                                                           
			const char* csValue=pChild->GetForeignKeyValue();
			if(!csValue)
			{
				err=FOREIGNKEY_FAIL_TO_FIND_PRIMARYKEY;
				tmp.LoadString(err);
				tmp+="\nIf the primary key is one of the auto generated field, such as Identity in SQL Server or Serial in PostgreSQL, you must map the text <SQLIDENTITY> (map <SEQUENCE> for Oracle) to that primary key.\n";
				hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_ATTRIBUTENODES_GETFOREIGNKEYVALUE_FAILED,
					(const char*)tmp, (const char*)pTargetNode->GetName(), (const char*)pChild->GetName(), (const char*)pChild->get_LinkToPrimaryKey());
				m_lastErr=getErrorLog()->GetLastErr();
				return hr;
			}
			value=csValue;
			iAdd = 0;
			err = 0;
		}
		else if(pChild->hasDefault())
		{
			                                                                       
			if(!TargetIsDatabase())value = pChild->GetDefaultValue();
			iAdd = pChild->IsIdentifier()?0:1;
			                                               
			                                      
			                                          
			   
		}
		else                              
		{
			                                                                                                        
			                                                           
			err = 1;
		}

		                                                                             
                                                                                                                                                                                                  

		                                                                                                             
		                                                                                 
		                                      
		                                                                           
		   
		                                                                 
		                                                              

		                                                 
		    
		                                                                                                                                                                                              
		                             
		    
		       
		    
		                                               
		                                                  
		                 
		     
		                                                                                         
		                                      
		                                                                                              
		     
		                        
		    
		   

		                                                                                                  
		if(value.IsEmpty() && !TargetIsDatabase() && m_targetType != LdapTypeId)
			iAdd = 0;

		                   

		if(err==0)
		{
			pChild->SetValue(bIsMappedToNull?DECL_NULL:(const char*)value);
			pChild->SetNullFlag(bIsMappedToNull);
			iCreatedNodes += iAdd;
		} else if(pChild->getSourceExpr()!=0){                                                                
			pChild->SetValue(DECL_NULL);
			pChild->SetNullFlag();	                   
			iCreatedNodes += iAdd;
		}
    }
	return hr;
}

                
void TreeMapper::handleEvaluateError(int& err, KString& value, CROM* pTarget, int& iAdd, bool& bIsMappedToNull, bool targetIsDatabase, bool targetIsLdap, CTransform* pTrans, CErrorLog& errorLog) {
	                                                                                       
	if( (err == TARGET_MAPPED_TO_NULL || err == -1) && !pTarget->hasDefault()) {
		                                        
		iAdd = (targetIsDatabase || targetIsLdap) ? 1 : 0;
		err = 0;
		bIsMappedToNull = true;
		value.Empty();
	} else {
		                                                               
		                                                          
		if(pTarget->hasDefault()) {
			if( targetIsDatabase ) {
				                                                  
				                                             
				if(pTarget->IsOptionalNode()) {
					bIsMappedToNull = true;
					iAdd = 1;
				}
				else {
					pTarget->setUnmapped();
					iAdd = 0;
				}
			}
			else {
				value = pTarget->GetDefaultValue();
				iAdd = pTarget->IsIdentifier() ? 0 : 1;
			}
			err = 0;
		} else {
			if ( err != TRANSFORM_INPUT_EMPTY) {
				string errorMessage( (const char*)pTrans->m_lastErr );
				if( err != -1 ) {
					errorMessage += "\nError Number="+konga::toString(err);
				}
				errorLog.MakeError(__FILE__, __LINE__, ID_ATTRIBUTENODES_EVALUATE_FAILED,
					(const char*)pTarget->get_Path(), errorMessage.c_str());
				m_lastErr=errorLog.GetLastErr();
			}							
		}
	}
}

void TreeMapper::createHfInstance(const CROM*targetNode){
}
