/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <sstream>	               
#include <memory>	           

#include <xercesc/dom/DOMDocument.hpp>
#include <xercesc/dom/DOMException.hpp>

#include "DOMDocumentPtr.h"
#include "DOMDocumentTypePtr.h"
#include "DOMProcessingInstructionPtr.h"
#include "DOMNodeListPtr.h"
#include "DOMElementPtr.h"
#include "DOMAttrPtr.h"
#include "DOMTextPtr.h"
#include "DOMCDATASectionPtr.h"
#include "CrXmlException.h"
#include "XmlCommon.h"
#include "DOMDocument_ptr.h"
#include "SimpleNamespaceResolver.h"

using namespace konga;
using std::string;
using std::auto_ptr;

XERCES_CPP_NAMESPACE_USE

xml::DOMDocumentPtr::DOMDocumentPtr(int dummy)
	: DOMNodePtr(), validateOnParse(false), resolveExternals(false), async(false) { }

xml::DOMDocumentPtr::~DOMDocumentPtr(void) {}

long xml::DOMDocumentPtr::CreateInstance() {
	auto_ptr<DOMDocument_ptr> pDoc( new DOMDocument_ptr );
	if ( !pDoc->init() ) {
		m_stLastError += pDoc->getLastError();
        m_stLastError += "\n";
		return -1;
	}
	m_stLastError.clear();
	m_stLoadedDocumentPath.clear();
	m_stParseErrors.clear();
	m_stParseWarnings.clear();

	m_rcDoc = pDoc.release();
	m_p = m_rcDoc->m_pxDoc;
	return 0;
}

bool xml::DOMDocumentPtr::loadFile(const string& stXmlFileName) {
	auto_ptr<DOMDocument_ptr> pDoc( new DOMDocument_ptr );
	if ( !pDoc->load(stXmlFileName, validateOnParse) ) {
		m_stLastError += pDoc->getLastError();
		if ( !m_stLastError.empty() )
	        m_stLastError += "\n";
		m_stParseErrors = pDoc->getParseErrors();
		m_stParseWarnings = pDoc->getParseWarnings();
		return false;
	}

	m_stLoadedDocumentPath = stXmlFileName;
	m_stParseWarnings = pDoc->getParseWarnings();
	m_rcDoc = pDoc.release();
	m_p = m_rcDoc->m_pxDoc;
	return true;
}

bool xml::DOMDocumentPtr::loadFile(const string& stXmlFileName, const string& stSchema, const string& stNamespace) {
	auto_ptr<DOMDocument_ptr> pDoc( new DOMDocument_ptr );
	if ( !pDoc->load(stXmlFileName, stSchema, stNamespace) ) {
		m_stLastError = pDoc->getLastError();
		m_stParseErrors = pDoc->getParseErrors();
		m_stParseWarnings = pDoc->getParseWarnings();
		return false;
	}

	m_stLoadedDocumentPath = stXmlFileName;
	m_stParseWarnings = pDoc->getParseWarnings();
	m_rcDoc = pDoc.release();
	m_p = m_rcDoc->m_pxDoc;
	return true;
}

bool xml::DOMDocumentPtr::loadString(const std::string& stXmlString) {
	auto_ptr<DOMDocument_ptr> pDoc( new DOMDocument_ptr );
	if ( !pDoc->loadString(stXmlString, validateOnParse) ) {
		m_stLastError = pDoc->getLastError();
		m_stParseErrors = pDoc->getParseErrors();
		m_stParseWarnings = pDoc->getParseWarnings();
		return false;
	}
	m_stLoadedDocumentPath.clear();
	m_stParseWarnings = pDoc->getParseWarnings();
	m_rcDoc = pDoc.release();
	m_p = m_rcDoc->m_pxDoc;
	return true;
}

bool xml::DOMDocumentPtr::normalizeDocument() {
	if ( !hasDoc() ) {
		m_stLastError += "No document defined.\n";
		return false;
	}

	m_rcDoc->m_pxDoc->normalizeDocument();
	return true;
}

bool xml::DOMDocumentPtr::save(const string& stFileName) const {
	if ( !hasDoc() ) {
		m_stLastError += "No document defined.\n";
		return false;
	}

	if ( !m_rcDoc->save(stFileName) ) {
		m_stLastError += m_rcDoc->getLastError();
		m_stLastError += "\n";
		return false;
	}
	return true;
}

string xml::DOMDocumentPtr::getLastError() const{
	return m_stLastError;
}

string xml::DOMDocumentPtr::getParseErrors() const {
	if ( !m_stParseErrors.empty() )
		return m_stParseErrors;

	if ( !m_rcDoc.isNull() )
		return m_rcDoc->getParseErrors();
	return "";
}

string xml::DOMDocumentPtr::getParseWarnings() const {
	if ( !m_stParseWarnings.empty() )
		return m_stParseWarnings;

	if ( !m_rcDoc.isNull() )
		return m_rcDoc->getParseWarnings();
	return "";
}

string xml::DOMDocumentPtr::getLoadedDocumentPath() const {
	return m_stLoadedDocumentPath;
}

xml::DOMElementPtr xml::DOMDocumentPtr::getDocumentElement() const {
	return hasDoc() ? DOMElementPtr(m_rcDoc->m_pxDoc->getDocumentElement(), m_rcDoc) : 0;
}

void xml::DOMDocumentPtr::setDocumentElement(const DOMElementPtr& element) {
	if ( hasDoc() ) {
		DOMNodeListPtr children = this->getChildNodes();
		                                                                       
		for( unsigned long i = 0, n = children.getLength(); i < n; ++i ) {
			DOMNodePtr node = children.item[i];
			if ( node.isElementNode() ) {
				this->replaceChild(element, node);
				return;
			}
		}

		                                                                               
		this->appendChild(element);
	}
}

xml::DOMDocumentTypePtr xml::DOMDocumentPtr::getDocType() const {
	return hasDoc() ? DOMDocumentTypePtr(m_rcDoc->m_pxDoc->getDoctype(), m_rcDoc) : 0;
}


xml::DOMProcessingInstructionPtr xml::DOMDocumentPtr::createProcessingInstruction(const string& strTarget, const string& strData) {
	return hasDoc() ? DOMProcessingInstructionPtr(m_rcDoc->m_pxDoc->createProcessingInstruction(X(strTarget), X(strData)), m_rcDoc)
                    : 0;
}

xml::DOMElementPtr xml::DOMDocumentPtr::createElement(const char* tagName) {
	if ( tagName == 0 )
		return 0;

	try {
		if ( hasDoc() ) {
			                                                                             
			DOMElementPtr retval(m_rcDoc->m_pxDoc->createElement(X(tagName)), m_rcDoc);
#ifdef WIN32
#ifdef _DEBUG
			retval.m_sDebugNodeName =  string(":") + tagName;
#endif
#endif
			return retval;
		}
		else
			return 0;
	} catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

xml::DOMElementPtr xml::DOMDocumentPtr::createElement(const string& stTagName) {
	return createElement(stTagName.c_str());
}

xml::DOMElementPtr xml::DOMDocumentPtr::createElementNS(const string& stNamespaceURI, const string& stQualifiedName ) {
	try {
		if ( hasDoc() ) {
			DOMElementPtr retval(m_rcDoc->m_pxDoc->createElementNS(X(stNamespaceURI), X(stQualifiedName)), m_rcDoc);
#ifdef WIN32
#ifdef _DEBUG
			retval.m_sDebugNodeName = stNamespaceURI + ":" + stQualifiedName;
#endif
#endif
			return retval;
		}
		else
			return 0;
	} catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}

}

xml::DOMDocumentTypePtr xml::DOMDocumentPtr::createDocumentType(const string& stName) {
	return hasDoc() ? DOMDocumentTypePtr( m_rcDoc->m_pxDoc->createDocumentType(X(stName)), m_rcDoc) : 0;
}

xml::DOMTextPtr xml::DOMDocumentPtr::createTextNode(const string& stData) {
	return hasDoc() ? DOMTextPtr( m_rcDoc->m_pxDoc->createTextNode(X(stData)), m_rcDoc) : 0;
}

xml::DOMCDATASectionPtr xml::DOMDocumentPtr::createCDATASectionNode(const string& stData) {
	return hasDoc() ? DOMCDATASectionPtr( m_rcDoc->m_pxDoc->createCDATASection(X(stData)), m_rcDoc) : 0;
}

xml::DOMNodePtr xml::DOMDocumentPtr::createNode(DOMNodeType type, const string& stName, const string& stNamespaceUri) {
	if ( hasDoc() ) {
		bool bHasNS = !stNamespaceUri.empty();
		switch( type ) {
		case ELEMENT_NODE:
			return bHasNS ? createElementNS(stNamespaceUri, stName) : createElement(stName);
		case ATTRIBUTE_NODE:
			return bHasNS ? createAttributeNS(stNamespaceUri, stName) : createAttribute(stName);
		case PROCESSING_INSTRUCTION_NODE:
			return createProcessingInstruction(stName, "");
		case DOCUMENT_TYPE_NODE:
			return createDocumentType(stName);
		case TEXT_NODE:
			return createTextNode("");	                                                                                                              
		case CDATA_SECTION_NODE:
			return createCDATASectionNode("");

		default:
			throw CrXmlException("xml::DOMDocumentPtr::createNode has not been implemented for this kind of node. DOMNodeType = " + toString(type));
		}
	}
	return 0;
}

xml::DOMNodePtr xml::DOMDocumentPtr::importNode(const DOMNodePtr& importedNode, bool bDeep) {
	try {
		return hasDoc() ? DOMNodePtr(m_rcDoc->m_pxDoc->importNode(importedNode.m_p, bDeep), m_rcDoc) : 0;
	}
	catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

xml::DOMAttrPtr xml::DOMDocumentPtr::createAttribute(const string& strName) {
	if ( hasDoc() ) {
		DOMAttrPtr retval( m_rcDoc->m_pxDoc->createAttribute(X(strName)), m_rcDoc);
#ifdef WIN32
#ifdef _DEBUG
		retval.m_sDebugNodeName = ":" + strName;
#endif
#endif
		return retval;
	}
	else
		return 0;
}

xml::DOMAttrPtr xml::DOMDocumentPtr::createAttribute(const string& stNamespace, const string& stQualifiedName) {
	return createAttributeNS(stNamespace, stQualifiedName);
}

xml::DOMAttrPtr xml::DOMDocumentPtr::createAttributeNS(const string& stNamespace, const string& stQualifiedName) {
	if ( hasDoc() ) {
		DOMAttrPtr retval( m_rcDoc->m_pxDoc->createAttributeNS(X(stNamespace), X(stQualifiedName)), m_rcDoc);
#ifdef WIN32
#ifdef _DEBUG
		retval.m_sDebugNodeName = stNamespace + ":" +stQualifiedName;
#endif
#endif
		return retval;
	}
	else
		return xml::DOMAttrPtr(0);
}

xml::DOMNodeListPtr xml::DOMDocumentPtr::getElementsByTagName(const string& stTagName) const {
    return hasDoc() ? DOMNodeListPtr(m_rcDoc->m_pxDoc->getElementsByTagName(X(stTagName)), m_rcDoc) : 0;
}

bool xml::DOMDocumentPtr::hasDoc() const {
	return m_rcDoc.isNull() ? false : (m_rcDoc->m_pxDoc != 0);
}

void xml::DOMDocumentPtr::addSelectionNamespace(const string& stPrefix, const string& stUri) {
	if ( hasDoc() ) {
		m_rcDoc->m_SelectionNamespaces[stPrefix] = stUri;
	}
}
