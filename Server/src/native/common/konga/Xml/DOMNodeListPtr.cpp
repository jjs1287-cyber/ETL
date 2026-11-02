/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <xercesc/dom/DOMNode.hpp>
#include <xercesc/dom/DOMElement.hpp>

#include <xalanc/XalanDOM/XalanNode.hpp>

#include "DOMElementPtr.h"
#include "DOMNodeListPtr.h"

using namespace konga;

xml::DOMNodeListPtr::DOMNodeListPtr(XERCES_CPP_NAMESPACE::DOMNodeList* px, const RCDocPtr& doc)
	: ComparablePtr<XERCES_CPP_NAMESPACE::DOMNodeList>(px), item(px,doc) { }

xml::DOMNodeListPtr::DOMNodeListPtr(boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesDocumentWrapper> pXercesDocumentWrapper,
									XALAN_CPP_NAMESPACE::NodeRefList* pXalanNodeRefList,
									const RCDocPtr& doc)
	: ComparablePtr<XERCES_CPP_NAMESPACE::DOMNodeList>(0),
	item(pXercesDocumentWrapper, pXalanNodeRefList, doc) { }

xml::DOMNodeListPtr::DOMNodeListPtr(int dummy)
	: ComparablePtr<XERCES_CPP_NAMESPACE::DOMNodeList>(0) { }

xml::DOMNodeListPtr::~DOMNodeListPtr(void) {}

unsigned long xml::DOMNodeListPtr::ListItem::getLength() const {
	if ( m_pxList != 0 )
		return m_pxList->getLength();
	else if ( m_pNodeRefList !=0 && m_pXercesDocumentWrapper != 0 )
		return m_pNodeRefList->getLength();
	else
		return 0;
}

xml::DOMNodePtr xml::DOMNodeListPtr::ListItem::operator[](unsigned long index) const {
	if ( m_pxList != 0 )
		return DOMNodePtr( m_pxList->item(index), m_pDoc);
	else if ( m_pNodeRefList !=0 && m_pXercesDocumentWrapper != 0 ) {
		using XALAN_CPP_NAMESPACE::XalanNode;
		XalanNode* pXalanNode = m_pNodeRefList->item(index);
		return ( pXalanNode == 0 ) ? 0 : DOMNodePtr( const_cast<XERCES_CPP_NAMESPACE::DOMNode*>( m_pXercesDocumentWrapper->mapNode(pXalanNode)), m_pDoc);
	}
	else
		return 0;
}

xml::DOMNodePtr xml::DOMNodeListPtr::ListItem::nextNode() {
	return ( m_currentIndex < getLength() ) ? operator[](m_currentIndex++) : 0;
}

void xml::DOMNodeListPtr::ListItem::reset() {
	m_currentIndex = 0;
}

bool xml::DOMNodeListPtr::operator!() const {
	if ( item.m_pXercesDocumentWrapper.get() == 0 )
		return ComparablePtr<XERCES_CPP_NAMESPACE::DOMNodeList>::operator!();

	return item.m_pXercesDocumentWrapper.get() == 0;
}

bool xml::DOMNodeListPtr::operator==(const xml::DOMNodeListPtr& rhs) const {
	if ( item.m_pXercesDocumentWrapper.get() == 0 )
		return ComparablePtr<XERCES_CPP_NAMESPACE::DOMNodeList>::operator==(rhs);

	return item.m_pXercesDocumentWrapper.get() == rhs.item.m_pXercesDocumentWrapper.get();
}

bool xml::DOMNodeListPtr::operator==(int rhs) const {
	if ( item.m_pXercesDocumentWrapper.get() == 0)
		return ComparablePtr<XERCES_CPP_NAMESPACE::DOMNodeList>::operator==(rhs);

	return false;
}
