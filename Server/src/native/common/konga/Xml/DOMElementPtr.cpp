/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <cassert>

#include <xercesc/dom/DOMElement.hpp>
#include <xercesc/dom/DOMException.hpp>

#include "DOMElementPtr.h"
#include "DOMAttrPtr.h"
#include "DOMNodeListPtr.h"
#include "XmlCommon.h"
#include "CrXmlException.h"

using namespace konga;
using namespace std;

xml::DOMElementPtr::DOMElementPtr(XERCES_CPP_NAMESPACE::DOMElement* element, const RCDocPtr& doc)
	: DOMNodePtr(element, doc), m_pxElement(element) { }

xml::DOMElementPtr::DOMElementPtr(int dummy) : DOMNodePtr(), m_pxElement(0) { }

xml::DOMElementPtr::DOMElementPtr(const DOMNodePtr& node) : xml::DOMNodePtr(node), m_pxElement(0) {
	                                            
	if ( m_p ) {
		if ( m_p->getNodeType() == XERCES_CPP_NAMESPACE::DOMNode::ELEMENT_NODE ) {
			m_pxElement = static_cast<XERCES_CPP_NAMESPACE::DOMElement*>(m_p);
		}
		else {
			m_p = m_pxElement = 0;
		}
	}
}

xml::DOMElementPtr::~DOMElementPtr(void) { }

string xml::DOMElementPtr::getAttribute(const string& stName, bool bRequired              ) const {
	if ( m_pxElement == 0 ) {
		if ( bRequired )
			throw CrXmlException("This DOM Element is null.");
		return string();	               
	}

	const XMLCh* pXmlCh = m_pxElement->getAttribute(X(stName));
	if ( pXmlCh != 0 )
		return XMLChToString(pXmlCh);

	if ( bRequired )
		throw CrXmlException("The attribute \"" + stName + "\" does not exist.");

	return string();
}

string xml::DOMElementPtr::getAttributeNS(const string& stNamespaceUri,
										  const string& stLocalName,
										  bool bRequired) const
{
	if ( m_pxElement == 0 ) {
		if ( bRequired )
			throw CrXmlException("This DOM Element is null.");
		return string();	               
	}

	const XMLCh* pXmlCh = m_pxElement->getAttributeNS(X(stNamespaceUri), X(stLocalName));
	if ( pXmlCh != 0 )
		return XMLChToString(pXmlCh);

	if ( bRequired )
		throw CrXmlException("The attribute '" + stLocalName
		+ "' in the namespace '" + stNamespaceUri + "' does not exist.");
	return string();
}

xml::DOMAttrPtr xml::DOMElementPtr::getAttributeNode(const string& stName) const {
    return (m_pxElement != 0 ) ? DOMAttrPtr(m_pxElement->getAttributeNode(X(stName)), m_rcDoc) : 0;
}

xml::DOMAttrPtr xml::DOMElementPtr::getAttributeNodeNS(const string& stNamespaceUri,
													   const string& stLocalName) const
{
    return (m_pxElement != 0 ) ? DOMAttrPtr(m_pxElement->getAttributeNodeNS(X(stNamespaceUri), X(stLocalName)), m_rcDoc) : 0;
}


long xml::DOMElementPtr::setAttribute(const string& stName, const string& stValue) {
	assert( m_pxElement != 0 && "DOM Element is null." );
	if ( m_pxElement != 0 ) {
        try {
            m_pxElement->setAttribute(X(stName), X(stValue));
            return 0;
        } catch ( const XERCES_CPP_NAMESPACE::DOMException& ex ) {
            throw CrXmlException(ex);
        }
    }
	throw CrXmlException("DOM Element is null.");
}

xml::DOMAttrPtr xml::DOMElementPtr::setAttributeNode(const DOMAttrPtr& newAttr) {
    if ( newAttr.m_pxAttr != 0 ) {
        try {
            return ( m_pxElement == 0 ) ? 0 : DOMAttrPtr(m_pxElement->setAttributeNode(newAttr.m_pxAttr), m_rcDoc);
        } catch( const XERCES_CPP_NAMESPACE::DOMException& ex ) {
            throw CrXmlException(ex);
        }
    }
    return 0;
}

void xml::DOMElementPtr::setAttributeNS(const string& stNamespaceUri,
										const string& stQualifiedName,
										const string& stValue)
{
	assert( m_pxElement != 0 && "DOM Element is null." );
	if ( m_pxElement != 0 ) {
        try {
			m_pxElement->setAttributeNS(X(stNamespaceUri), X(stQualifiedName), X(stValue));
			return;
        } catch ( const XERCES_CPP_NAMESPACE::DOMException& ex ) {
            throw CrXmlException(ex);
        }
    }
	throw CrXmlException("DOM Element is null.");
}

void xml::DOMElementPtr::removeAttribute(const string& stName) {
	try {
		if ( m_pxElement != 0 )
			m_pxElement->removeAttribute(X(stName));
	}
	catch( const XERCES_CPP_NAMESPACE::DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

void xml::DOMElementPtr::removeAttributeNS(const string& stNamespaceUri,
										   const string& stLocalName)
{
	try {
		if ( m_pxElement != 0 )
			m_pxElement->removeAttributeNS(X(stNamespaceUri), X(stLocalName));
	}
	catch( const XERCES_CPP_NAMESPACE::DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

xml::DOMAttrPtr xml::DOMElementPtr::removeAttributeNode(const DOMAttrPtr& oldAttr) {
	try {
		return ( m_pxElement == 0 ) ? 0 : DOMAttrPtr( m_pxElement->removeAttributeNode(oldAttr.m_pxAttr), m_rcDoc);
	}
	catch( const XERCES_CPP_NAMESPACE::DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

