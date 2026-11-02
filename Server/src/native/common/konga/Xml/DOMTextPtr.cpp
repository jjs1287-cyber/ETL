/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <xercesc/dom/DOMText.hpp>

#include "DOMTextPtr.h"

XERCES_CPP_NAMESPACE_USE

using namespace konga;


xml::DOMTextPtr::DOMTextPtr(DOMText* pTxt, const RCDocPtr& doc)
	: DOMCharacterDataPtr(pTxt, doc), m_pxText(pTxt) { }

xml::DOMTextPtr::DOMTextPtr(int dummy)
    : DOMCharacterDataPtr(0), m_pxText(0) { }

xml::DOMTextPtr::DOMTextPtr(const DOMNodePtr& text) : DOMCharacterDataPtr(text), m_pxText(0) {
	                                                                                         
	if ( m_p ) {

		const short type = m_p->getNodeType();
		if ( type == XERCES_CPP_NAMESPACE::DOMNode::TEXT_NODE ||
			type == XERCES_CPP_NAMESPACE::DOMNode::CDATA_SECTION_NODE )
		{
			m_pxText = static_cast<XERCES_CPP_NAMESPACE::DOMText*>(m_p);
		}
		else {
			m_p = m_pxText = 0;
		}
	}
}

xml::DOMTextPtr::~DOMTextPtr(void) { }
