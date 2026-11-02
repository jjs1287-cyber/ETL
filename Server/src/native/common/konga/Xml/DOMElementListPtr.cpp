/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "DOMElementListPtr.h"

using namespace std;
using namespace konga::xml;

DOMElementListPtr::ListItem::ListItem(const DOMNodeListPtr& nodeList)
	: m_pList( new vector<DOMElementPtr>() ) {
	for( unsigned long i = 0, n = nodeList.getLength(); i < n; ++i ) {
		DOMElementPtr element( nodeList.item[i] );
		if ( element != 0 ) {
			m_pList->push_back( element );
		}
	}
}

DOMElementListPtr::ListItem::~ListItem(void) { }

unsigned long DOMElementListPtr::ListItem::size() const {
	return m_pList->size();
}

DOMElementPtr konga::xml::DOMElementListPtr::ListItem::operator[](unsigned long index) const {
	return ( index < m_pList->size() ) ? m_pList->at(index) : 0;
}


