/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "RCDocPtr.h"

using namespace konga;
using namespace konga::xml;

RCDocPtr::RCDocPtr(DOMDocument_ptr* pDOMDoc_ptr) : m_pDoc(pDOMDoc_ptr) {
	if ( m_pDoc != 0 )
		m_pDoc->incrementRef();
}

RCDocPtr::RCDocPtr(const RCDocPtr& rhs)
: m_pDoc(rhs.m_pDoc) {
	if ( m_pDoc != 0 )
	  m_pDoc->incrementRef();
}

RCDocPtr::~RCDocPtr(void) {
	if ( m_pDoc != 0 )
		m_pDoc->decrementRef();
}

RCDocPtr& RCDocPtr::operator=(const RCDocPtr& rhs) {
	if ( this != &rhs ) {
		if ( m_pDoc != 0 )
			m_pDoc->decrementRef();

		m_pDoc = rhs.m_pDoc;
		if ( m_pDoc != 0 )
			m_pDoc->incrementRef();
	}
	return *this;
}

RCDocPtr& RCDocPtr::operator=(DOMDocument_ptr* pDOMDoc_ptr) {
	if ( m_pDoc != 0 )
		m_pDoc->decrementRef();

	m_pDoc = pDOMDoc_ptr;
	if ( m_pDoc != 0 )
		m_pDoc->incrementRef();

	return *this;
}

DOMDocument_ptr* RCDocPtr::operator->() const {
	return m_pDoc;
}

DOMDocument_ptr& RCDocPtr::operator*() const {
	return *m_pDoc;
}

bool RCDocPtr::isNull() const {
	return m_pDoc == 0;
}

