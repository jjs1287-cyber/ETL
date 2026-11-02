/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __XPATHSEARCH_H_
#define __XPATHSEARCH_H_

#include <string>
#include <vector>
#include <map>
#include <stack>

#include <konga/Utils/AutoPtrCleanup.h>

#include <konga/DataElement/DataElement.h>
#include <konga/DataElement/Token.h>

#include "XmlReader.h"

namespace konga {

class XPathSearch : public XmlReader {
protected:
	std::vector<JbNode*> m_attributes;

public:
	XPathSearch(const std::string& xmlFileName);
	~XPathSearch();

	JbNode*selectSingleNode(const std::string& xpath);
	std::vector<JbNode*> selectNodes(const std::string& xpath);
	std::vector<JbNode*> selectNodes(const std::string& xpath, std::vector<JbNode*>& nodeList, JbNode*& node);
	std::string getSingleValue(const std::string& xpath, bool& exists);
	std::vector<std::string> getValues(const std::string& xpath, std::vector<JbNode*>&nodeList);

	std::string getPath(JbNode*node);

private:
	void findSubNodes(JbNode* start, std::vector<JbNode*>& nodeList, const std::string& name);
	void findSubNodesRecursive(JbNode* start, std::vector<JbNode*>& nodeList, const std::string& name);
	void findAllNodes(JbNode* start, std::vector<JbNode*>& nodeList);
	void findAttributes(std::vector<JbNode*>& startList, std::vector<JbNode*>& nodeList, const std::string& name);

	JbNode* createAttributeNode(JbNode*node, const std::string& name);
	std::string getAttributeValue(JbNode*node, const std::string& name);
	std::string getNodeValue(JbNode*node);
	bool IsAttributeNode(JbNode*node);
	bool testFilter(JbNode*node, const std::string& filter);

	int parseXPathFilter(const char* filter, std::vector<konga::Token*>& tokens);
	static void rpnOrdering(std::vector<Token*>& tokens);
	int execFunction(JbNode*node, Token*t, DataElement*& de, Token*ts[], std::vector<int>& ind, AutoDeleteStack<DataElement>& tmp);
	void handleMarker(JbNode*node, int& i, const int n, Token*& t, Token* ts[],
					DataElement*& de, DataElement*& de1, DataElement*& de2,
					std::stack<DataElement*>& st,
					AutoDeleteStack<DataElement>& tmp, int& err);
	DataElement* eval(JbNode*node, Token*ts[], int n);

	DataElement* getDataElement(JbNode*node, int ia,const Token::FNC*f,Token*ts[],std::vector<int>&ind,konga::AutoDeleteStack<DataElement>&tmp);

	int getPosition(JbNode*node);
	int getLastPosition(JbNode*node);
	bool isAtSiblingPosition(JbNode*node, int pos);
};

}                   
#endif                    

