/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#include <xercesc/dom/DOMCDATASection.hpp>

#include "DOMCDATASectionPtr.h"

XERCES_CPP_NAMESPACE_USE

using namespace konga;


xml::DOMCDATASectionPtr::DOMCDATASectionPtr(DOMCDATASection* pCdata,
											const RCDocPtr& doc)
 : DOMTextPtr(pCdata, doc), m_pxCdataSection(pCdata) { }

xml::DOMCDATASectionPtr::DOMCDATASectionPtr(int dummy)
    : DOMTextPtr(0), m_pxCdataSection(0) { }

xml::DOMCDATASectionPtr::DOMCDATASectionPtr(const DOMNodePtr& cdata): DOMTextPtr(cdata), m_pxCdataSection(0) {
	                                             
	if ( m_p ) {
		if ( m_p->getNodeType() == XERCES_CPP_NAMESPACE::DOMNode::CDATA_SECTION_NODE ) {
			m_pxCdataSection = static_cast<XERCES_CPP_NAMESPACE::DOMCDATASection*>(m_p);
		}
		else {
			m_p = m_pxCdataSection = 0;
		}
	}
}

xml::DOMCDATASectionPtr::~DOMCDATASectionPtr(void) { }
