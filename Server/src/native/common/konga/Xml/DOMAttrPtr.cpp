/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <xercesc/dom/DOMAttr.hpp>

#include "DOMAttrPtr.h"
#include "DOMElementPtr.h"
#include "XmlCommon.h"

using namespace konga;

xml::DOMAttrPtr::DOMAttrPtr(XERCES_CPP_NAMESPACE::DOMAttr* pxAttr, const RCDocPtr& doc) : xml::DOMNodePtr(pxAttr, doc), m_pxAttr(pxAttr) { }

xml::DOMAttrPtr::DOMAttrPtr(int dummy) : DOMNodePtr(), m_pxAttr(0) { }

xml::DOMAttrPtr::DOMAttrPtr(const DOMNodePtr& node) : DOMNodePtr(node), m_pxAttr(0)
{
	                                                           
	if ( m_p ) {
		if ( m_p->getNodeType() == XERCES_CPP_NAMESPACE::DOMNode::ATTRIBUTE_NODE ) {
			m_pxAttr = static_cast<XERCES_CPP_NAMESPACE::DOMAttr*>(m_p);
		}
		else {
			m_p = m_pxAttr = 0;
		}
	}
}

xml::DOMAttrPtr::~DOMAttrPtr(void) { }

                                                        
               
   

std::string xml::DOMAttrPtr::getName() const {
	return ( m_pxAttr == 0 ) ? "" : XMLChToString(m_pxAttr->getName());
}

std::string xml::DOMAttrPtr::getValue() const {
	return ( m_pxAttr == 0 ) ? "" : XMLChToString( m_pxAttr->getValue() );
}

void xml::DOMAttrPtr::setValue(const std::string& stValue) {
	if ( m_pxAttr != 0 )
		m_pxAttr->setValue( X(stValue) );
}

xml::DOMElementPtr xml::DOMAttrPtr::getOwnerElement() const {
	return ( m_pxAttr == 0 ) ? 0 : DOMElementPtr( m_pxAttr->getOwnerElement(), m_rcDoc);
}

