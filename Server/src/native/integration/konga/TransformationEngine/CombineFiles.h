/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef __COMBINEFILES_H_
#define __COMBINEFILES_H_

#include <string>
#include <vector>
#include <stack>
#include <map>

#include "TransacParser.h"

class TreeMapper;

class CombineFiles {
public:
	CombineFiles(TreeMapper*pTreeMapper);
	~CombineFiles();
	void processFiles(const std::string& combinedFileName, int nChunk);

private:
	TransacParser* open(const std::string& fileName);
	void updateParents(FILE*out, std::map<std::string, std::vector<std::string> >& map, konga::JbNode*node, bool isXml);
	void writeTrailer(FILE*out, std::map<std::string, std::vector<std::string> >& map, bool isXml);
	std::string getFooter(konga::JbNode*parent, konga::JbNode*transac);

private:
	TreeMapper* m_pTreeMapper;
	int typeId;
	CROM* pDtdRoot;
	CROM* pDtdTransac;
	std::vector<konga::JbNode*> transacs;
	konga::JbNode* last;
	std::map<std::string,std::string>path_map;
	std::stack<std::string> parent_name_stack;
	                                                
};


#endif                     

