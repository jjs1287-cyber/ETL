/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <xercesc/dom/DOMCharacterData.hpp>
#include <xercesc/dom/DOMException.hpp>

#include "DOMCharacterDataPtr.h"
#include "CrXmlException.h"
#include "XmlCommon.h"

XERCES_CPP_NAMESPACE_USE

using namespace konga;


xml::DOMCharacterDataPtr::DOMCharacterDataPtr(DOMCharacterData* cd, const RCDocPtr& doc)
	: DOMNodePtr(cd, doc), m_pxCharacterData(cd) { }

xml::DOMCharacterDataPtr::DOMCharacterDataPtr(int dummy)
    : DOMNodePtr(), m_pxCharacterData(0) { }

xml::DOMCharacterDataPtr::DOMCharacterDataPtr(const DOMNodePtr& charData) : DOMNodePtr(charData), m_pxCharacterData(0) {
	                                                   
	if ( m_p ) {
		const short type = charData->getNodeType();
		if ( type == XERCES_CPP_NAMESPACE::DOMNode::COMMENT_NODE ||
			 type == XERCES_CPP_NAMESPACE::DOMNode::TEXT_NODE ||
			 type == XERCES_CPP_NAMESPACE::DOMNode::CDATA_SECTION_NODE
			)
		{
			m_pxCharacterData = static_cast<XERCES_CPP_NAMESPACE::DOMCharacterData*>(m_p);
		}
		else {
			m_p = m_pxCharacterData = 0;
		}
	}
}

xml::DOMCharacterDataPtr::~DOMCharacterDataPtr(void) { }

                                                                          
                                                      
   

std::string xml::DOMCharacterDataPtr::getData() const {
	return ( m_pxCharacterData == 0 ) ? "" : XMLChToString( m_pxCharacterData->getData() );
}

long xml::DOMCharacterDataPtr::getLength() const {
	return ( m_pxCharacterData == 0 ) ? 0 : m_pxCharacterData->getLength();
}

void xml::DOMCharacterDataPtr::setData(const std::string& stData) {
	if ( m_pxCharacterData == 0 )
		return;

	try {
		m_pxCharacterData->setData( X(stData) );
	}
	catch( const DOMException& ex ) {
		throw CrXmlException( XMLChToString(ex.getMessage()) );
	}
}

