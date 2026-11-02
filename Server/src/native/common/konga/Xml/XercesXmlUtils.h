/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_XERCES_XML_UTILS_H
#define KONGA_XML_XERCES_XML_UTILS_H

#include <stack>
#include <string>

#include <xercesc/dom/DOMNode.hpp>

namespace konga {

	                                                                                 
	class XercesNodeTraverser {
	public:
		XercesNodeTraverser(XERCES_CPP_NAMESPACE::DOMNode* pStartNode);
		~XercesNodeTraverser(void) {}

		XERCES_CPP_NAMESPACE::DOMNode* GetNextNode();

	private:
		XERCES_CPP_NAMESPACE::DOMNode* m_pCurrentNode;
		typedef std::stack<XERCES_CPP_NAMESPACE::DOMNode*> nodeStackT;
		nodeStackT m_parent_stack;		
	};

	XERCES_CPP_NAMESPACE::DOMNode* FindSameNode(XERCES_CPP_NAMESPACE::DOMNode* pStartNode,
		const XERCES_CPP_NAMESPACE::DOMNode* pContextNode);

	                                                                     
	                                             
	void XercesBOMWorkaround(const std::string& stFileName);
}

#endif                                

