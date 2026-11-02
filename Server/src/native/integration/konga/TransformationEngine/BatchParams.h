/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __BatchParams_H_
#define __BatchParams_H_

#include <string>
#include <map>

#include <konga/JbParser/JbParser.h>

class CROM;
class NodePatternMatch;

class BatchParams {
	                                                                                   
	                               
	                                                                        
	                                                                    
	                                     
	                                                                                                   
	                                                                                  
protected:
	std::string batchNodePath;
	int batchSize;
	unsigned int* batchNodeNameIndeces;
	int* currentJbNodePositions;
	NodePatternMatch**batchNodeMatchMakers;

	size_t nBatchNodes;

	std::map<CROM*,CROM*>batch_dtd_child_map;
	std::map<CROM*,int>batch_dtd_level_map;
	CROM* pDtdBatchNode;

	konga::JbNode*root;

public:
	BatchParams(const std::string& batchNodePath, int batchSize);
	virtual ~BatchParams();

	bool isBatchLoading();
	void prepareBatchLoading(CROM*pDtdRoot, konga::JbParser*parser);
	void setCromBatchStatus(CROM*dtd, int level);
	void loadBatchNodeArrays(konga::JbParser*parser);
	int getBatchLevelFromDtd(CROM*dtd);
	konga::JbNode* getCurrentJbNode(int level);
};

#endif                    

