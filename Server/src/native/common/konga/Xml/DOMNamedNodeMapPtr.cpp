/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include "DOMNamedNodeMapPtr.h"
#include "XmlCommon.h"

using namespace konga;


xml::DOMNamedNodeMapPtr::DOMNamedNodeMapPtr(XERCES_CPP_NAMESPACE::DOMNamedNodeMap* px, const RCDocPtr& doc)
	: ComparablePtr<XERCES_CPP_NAMESPACE::DOMNamedNodeMap>(px), DOMNodeListBase<XERCES_CPP_NAMESPACE::DOMNamedNodeMap>(px, doc) {
	}

xml::DOMNamedNodeMapPtr::DOMNamedNodeMapPtr(int dummy)
	: ComparablePtr<XERCES_CPP_NAMESPACE::DOMNamedNodeMap>(0), DOMNodeListBase<XERCES_CPP_NAMESPACE::DOMNamedNodeMap>(0) {
	}

xml::DOMNamedNodeMapPtr::~DOMNamedNodeMapPtr(void) {}

xml::DOMNodePtr xml::DOMNamedNodeMapPtr::getNamedItem(const std::string& stName) const {
	return (m_p != 0) ? DOMNodePtr(m_p->getNamedItem(X(stName)), item.m_pDoc) : 0;
}

xml::DOMNodePtr xml::DOMNamedNodeMapPtr::getNamedItemNS(const std::string& stNamespaceUri, const std::string& stLocalName) const {
	return (m_p != 0) ? DOMNodePtr( m_p->getNamedItemNS(X(stNamespaceUri), X(stLocalName)), item.m_pDoc) : 0;
}

xml::DOMNodePtr xml::DOMNamedNodeMapPtr::setNamedItem(const DOMNodePtr& node) {
	return ( m_p != 0 ) ? DOMNodePtr(m_p->setNamedItem(node.m_p), item.m_pDoc) : 0;
}

xml::DOMNodePtr xml::DOMNamedNodeMapPtr::setNamedItemNS(const DOMNodePtr& node) {
	return ( m_p != 0 ) ? DOMNodePtr( m_p->setNamedItemNS(node.m_p), item.m_pDoc) : 0;
}

xml::DOMNodePtr xml::DOMNamedNodeMapPtr::removeNamedItem(const std::string& stName) {
	return ( m_p != 0 ) ? DOMNodePtr( m_p->removeNamedItem(X(stName)), item.m_pDoc) : 0;	
}

xml::DOMNodePtr xml::DOMNamedNodeMapPtr::removeNamedItemNS(const std::string& stNamespaceUri, const std::string& stName) {
	return ( m_p != 0 ) ? DOMNodePtr( m_p->removeNamedItemNS(X(stNamespaceUri), X(stName)), item.m_pDoc) : 0;
}

