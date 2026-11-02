/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <xercesc/dom/DOMDocumentType.hpp>

#include "XmlCommon.h"
#include "DOMDocumentTypePtr.h"

using namespace konga;

xml::DOMDocumentTypePtr::DOMDocumentTypePtr(XERCES_CPP_NAMESPACE::DOMDocumentType* pxDocType, const RCDocPtr& doc)
	: DOMNodePtr(pxDocType, doc), m_pxDocType(pxDocType) { }

xml::DOMDocumentTypePtr::DOMDocumentTypePtr(int dummy)
	: DOMNodePtr(), m_pxDocType(0) { }

std::string xml::DOMDocumentTypePtr::getName() const {
	return (m_pxDocType != 0) ? XMLChToString(m_pxDocType->getName()) : 0;
}

