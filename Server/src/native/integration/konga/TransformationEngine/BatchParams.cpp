/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringTokenizer.h>

#include "BatchParams.h"
#include "crom.h"
#include "NodePatternMatch.h"

BatchParams::BatchParams(const std::string& batchNodePath0, int batch_size0) :
	batchSize(batch_size0),
	batchNodeNameIndeces(0),
	currentJbNodePositions(0),
	batchNodeMatchMakers(0),
	nBatchNodes(0),
	root(0)
{
	if(!batchNodePath0.empty()){
		int len=batchNodePath0.size();
		if(batchNodePath0.at(0)=='[' && batchNodePath0.at(len-1)==']'){
			batchNodePath=batchNodePath0.substr(1, len-2);
		}
		else {
			batchNodePath=batchNodePath0;
		}
	}
}

bool BatchParams::isBatchLoading(){
	return batchSize>0 && !batchNodePath.empty();
}

void BatchParams::prepareBatchLoading(CROM*pDtdRoot, konga::JbParser*parser){
	setCromBatchStatus(pDtdRoot, 0);
	loadBatchNodeArrays(parser);
}

void BatchParams::setCromBatchStatus(CROM*dtd, int level){
	if(batchNodePath.empty()){
		dtd->setBatchStatus(CROM_t_NotBatchNode);
	}
	else {
		const char*path=(const char*)dtd->get_Path();
		if(strncmp(batchNodePath.c_str(), path, strlen(path))==0){
			if(batchNodePath==path){
				pDtdBatchNode=dtd;
				dtd->setBatchStatus(CROM_t_BatchNode);
			}
			else {
				dtd->setBatchStatus(CROM_t_BatchParent);
			}
			if(dtd->GetParent()!=0){
				batch_dtd_child_map[dtd->GetParent()]=dtd;
			}
			batch_dtd_level_map[dtd]=level;
		}
		else if(strncmp(batchNodePath.c_str(), path, batchNodePath.size())==0){
			dtd->setBatchStatus(CROM_t_BatchChild);
		}
		else {
			dtd->setBatchStatus(CROM_t_NotBatchNode);
		}
	}
	++level;
	for(CROM*child=dtd->GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf())continue;
		setCromBatchStatus(child, level);
	}
}

void BatchParams::loadBatchNodeArrays(konga::JbParser* parser){
	std::vector<unsigned short> batchNodeNameIndexList;
	if(pDtdBatchNode->isDB()){
		unsigned int index=parser->getStringIndex("");
		batchNodeNameIndexList.push_back(index);
	}
	konga::StringTokenizer tk(batchNodePath.c_str(),"[$.]");
	while(tk.NextToken()){
		const char*token=tk.token();
		switch(token[0]){
			case '.':
			case '$':
			case '[':
			case ']':
				break;
			default:
				if(isdigit(token[0])){
					throw konga::CrException("batch_node has at least one anonymous group or choice parent. batch_node path="+batchNodePath);
				}
				else {
					unsigned int index=parser->getStringIndex(token);
					for(std::vector<unsigned short>::const_iterator it=batchNodeNameIndexList.begin(); it!=batchNodeNameIndexList.end(); ++it){
						if(*it==index)
						throw konga::CrException("batch_node has two parent with the same node name. token="+std::string(token)+", batch_node path="+batchNodePath);
					}
					batchNodeNameIndexList.push_back(index);
				}
		}
	}
	nBatchNodes=batchNodeNameIndexList.size();
	batchNodeNameIndeces=new unsigned int[nBatchNodes];
	currentJbNodePositions=new int[nBatchNodes];
	batchNodeMatchMakers=new NodePatternMatch*[nBatchNodes];
	for(size_t i=0; i<nBatchNodes; ++i){
		batchNodeNameIndeces[i]=batchNodeNameIndexList.at(i);
		currentJbNodePositions[i]=-1;
		batchNodeMatchMakers[i]=0;
	}
}

int BatchParams::getBatchLevelFromDtd(CROM*dtd){
	std::map<CROM*,int>::const_iterator it=batch_dtd_level_map.find(dtd);
	if(it!=batch_dtd_level_map.end())return it->second;
	return -1;
}

konga::JbNode* BatchParams::getCurrentJbNode(int level){
	konga::JbNode* node=root;
	for(int i=1; i<=level; ++i){
		std::vector<konga::JbNode*>& children(node->getChildren());
		int nChild=children.size(), iChild=currentJbNodePositions[level];                                              
		node=(iChild<nChild)? children.at(iChild): 0;
		if(node==0)break;
	}
	return node;
}


BatchParams::~BatchParams(){
	if(batchNodeNameIndeces)delete[] batchNodeNameIndeces;
	if(currentJbNodePositions)delete[] currentJbNodePositions;
	if(batchNodeMatchMakers){
		for(size_t i=0; i<nBatchNodes; ++i){
			if(batchNodeMatchMakers[i])delete batchNodeMatchMakers[i];
		}
		delete[] batchNodeMatchMakers;
	}
}

