/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <memory>	           
#include <map>
#include <cassert>

#include <xercesc/dom/DOMImplementationRegistry.hpp>
#include <xercesc/dom/DOMImplementation.hpp>
#include <xercesc/dom/DOMLSSerializer.hpp>
#include <xercesc/dom/DOMImplementationLS.hpp>
#include <xercesc/dom/DOMConfiguration.hpp>
#include <xercesc/dom/DOMLSOutput.hpp>
#include <xercesc/framework/MemBufFormatTarget.hpp>

#include <xercesc/dom/DOMException.hpp>
#include <xercesc/dom/DOMNode.hpp>
#include <xercesc/dom/DOMElement.hpp>
#include <xercesc/dom/DOMDocument.hpp>
#include <xercesc/util/XMLUniDefs.hpp>

#include <xalanc/XercesParserLiaison/XercesDOMSupport.hpp>
#include <xalanc/XercesParserLiaison/XercesParserLiaison.hpp>
#include <xalanc/XPath/NodeRefList.hpp>
#include <xalanc/XPath/XPathEvaluator.hpp>
#include <xalanc/PlatformSupport/XSLException.hpp>
#include <xalanc/XalanDOM/XalanDocument.hpp>

#include <konga/Utils/XmlInit.h>

#include "XmlIncludes.h"
#include "XmlCommon.h"
#include "CrXmlException.h"
#include "SimpleDOMErrorHandler.h"
#include "DOMDocument_ptr.h"
#include "DOMElementListPtr.h"
#include "SimpleNamespaceResolver.h"

using namespace konga;
using std::string;
using std::map;
using std::auto_ptr;

XERCES_CPP_NAMESPACE_USE


xml::DOMNodePtr::DOMNodePtr(XERCES_CPP_NAMESPACE::DOMNode* pNode, const RCDocPtr& rcdoc)
: ComparablePtr<DOMNode>(pNode), m_rcDoc(rcdoc) {
#ifdef _DEBUG
#ifdef WIN32
	m_sDebugNodeName = getNamespaceURI() + ":" + getLocalName();
#endif
#endif
}

xml::DOMNodePtr::DOMNodePtr(int dummy) : ComparablePtr<DOMNode>(0), m_rcDoc(0) { }

xml::DOMNodePtr::~DOMNodePtr() {}

string xml::DOMNodePtr::getNodeName() const {
	return ( m_p != 0) ? XMLChToString(m_p->getNodeName()) : "";
}

string xml::DOMNodePtr::getNodeValue() const {
	return ( m_p != 0 ) ? XMLChToString(m_p->getNodeValue()) : "";
}

string xml::DOMNodePtr::getText() const {
	return ( m_p != 0 ) ? XMLChToString(m_p->getTextContent()) : "";
}

string xml::DOMNodePtr::getTextContent() const {
	return ( m_p != 0 ) ? XMLChToString(m_p->getTextContent()) : "";
}

string xml::DOMNodePtr::getLocalName() const {
	return ( m_p != 0 ) ? XMLChToString(m_p->getLocalName()) : "";
}

string  xml::DOMNodePtr::getBaseName() const {
	                                                                                             
                         
	return ( m_p != 0 ) ? XMLChToString(m_p->getLocalName()) : "";
}

string xml::DOMNodePtr::getNamespaceURI() const {
	return ( m_p != 0 ) ? XMLChToString(m_p->getNamespaceURI()) : "";
}

string xml::DOMNodePtr::getPrefix() const {
	return ( m_p != 0 ) ? XMLChToString(m_p->getPrefix()) : "";
}

xml::DOMNodeType xml::DOMNodePtr::getNodeType() const {
	return ( m_p != 0 ) ? static_cast<xml::DOMNodeType>(m_p->getNodeType()) : KONG_NON_INITIALIZED;
}

bool xml::DOMNodePtr::isElementNode() const {
	return ( m_p != 0 ) ? m_p->getNodeType() == DOMNode::ELEMENT_NODE : false;
}

bool xml::DOMNodePtr::hasChildNodes() const {
    return ( m_p != 0) ? m_p->hasChildNodes() : false;
}

xml::DOMNodePtr xml::DOMNodePtr::getFirstChild() const {
	return ( m_p != 0) ? DOMNodePtr(m_p->getFirstChild(), m_rcDoc) : 0;
}

xml::DOMNodeListPtr xml::DOMNodePtr::getChildNodes() const {
	return ( m_p != 0) ? xml::DOMNodeListPtr(m_p->getChildNodes(), m_rcDoc) : 0;
}

xml::DOMNodePtr xml::DOMNodePtr::getParentNode() const {
	return ( m_p != 0 ) ? DOMNodePtr(m_p->getParentNode(), m_rcDoc) : 0;
}

xml::DOMNodePtr xml::DOMNodePtr::getPreviousSibling() const {
    return ( m_p != 0 ) ? DOMNodePtr(m_p->getPreviousSibling(), m_rcDoc) : 0;
}

xml::DOMNodePtr xml::DOMNodePtr::getNextSibling() const {
    return ( m_p != 0 ) ? DOMNodePtr(m_p->getNextSibling(), m_rcDoc) : 0;
}

void xml::DOMNodePtr::setNodeValue(const string& stValue) {
	try {
		if ( m_p != 0 )
			m_p->setNodeValue(X(stValue));
	}
	catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

void xml::DOMNodePtr::setPrefix(const std::string& stPrefix) {
	try {
		if ( m_p != 0 )
			m_p->setPrefix(X(stPrefix));
	}
	catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

void xml::DOMNodePtr::setTextContent(const string& stTextContent) {
	try {
		if ( m_p != 0 )
			m_p->setTextContent(X(stTextContent));
	}
	catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

std::string xml::DOMNodePtr::lookupNamespacePrefix(const std::string& namespaceURI, bool bUseDefault) const {
    if (m_p == 0 || (bUseDefault && m_p->isDefaultNamespace(X(namespaceURI)))) return string();
    return XMLChToString(m_p->lookupPrefix(X(namespaceURI)));
}

std::string xml::DOMNodePtr::lookupNamespaceURI(const std::string& prefix) const {
	if ( m_p != 0 )
		return XMLChToString(m_p->lookupNamespaceURI(X(prefix)));
	return string();
}

void xml::DOMNodePtr::setText(const string& stTextContent) {
	setTextContent(stTextContent);
}

xml::DOMNodePtr xml::DOMNodePtr::ImportIfNeeded(const xml::DOMNodePtr& newNode) {
	if ( newNode.m_p == 0 )
		throw CrException("The new node is NULL");
	if ( m_p == 0 )
		throw CrException("This node is NULL.");

	DOMDocument* pOwner = m_p->getOwnerDocument();
	if ( pOwner == 0 ) {
		                                                  
		pOwner = dynamic_cast<DOMDocument*>(m_p);
		if ( pOwner == 0 )
			throw CrException("This node has no owner.");
	}

	if ( !pOwner->isSameNode(newNode.m_p->getOwnerDocument()) ) {
		                               
		return DOMNodePtr(pOwner->importNode(newNode.m_p, true), m_rcDoc);
	}
	return newNode;
}

xml::DOMNodePtr xml::DOMNodePtr::appendChild(const DOMNodePtr& newChild) {
	try {
		DOMNodePtr newNode( ImportIfNeeded(newChild) );
		return DOMNodePtr(m_p->appendChild(newNode.m_p), m_rcDoc);
	} catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

xml::DOMNodePtr xml::DOMNodePtr::removeChild(const DOMNodePtr& oldChild) {
	try {
		return ( m_p != 0 && oldChild.m_p != 0) ? DOMNodePtr(m_p->removeChild(oldChild.m_p), m_rcDoc) : 0;
	} catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

xml::DOMNodePtr xml::DOMNodePtr::replaceChild(const DOMNodePtr& newChild, const DOMNodePtr& oldChild) {
	try {
		DOMNodePtr newNode( ImportIfNeeded(newChild) );
		return DOMNodePtr(m_p->replaceChild(newNode.m_p, oldChild.m_p), m_rcDoc);

	} catch( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

xml::DOMNodePtr xml::DOMNodePtr::insertBefore(const DOMNodePtr& newChild, const DOMNodePtr& refChild) {
	try {
		DOMNodePtr newNode( ImportIfNeeded(newChild) );
		return DOMNodePtr(m_p->insertBefore(newNode.m_p, refChild->m_p), m_rcDoc);
	} catch ( const DOMException& ex ) {
		throw CrXmlException(ex);
	}
}

xml::DOMNodePtr xml::DOMNodePtr::cloneNode(bool bDeep) const {
	return ( m_p != 0 ) ?  DOMNodePtr(m_p->cloneNode(bDeep), m_rcDoc) : 0;
}

bool xml::DOMNodePtr::hasAttributes() const {
	return ( m_p != 0 ) ? m_p->hasAttributes() : false;
}


xml::DOMNamedNodeMapPtr xml::DOMNodePtr::getAttributes() const {
    return ( m_p != 0 ) ? DOMNamedNodeMapPtr(m_p->getAttributes(), m_rcDoc) : 0;
}

string xml::DOMNodePtr::getXml() const {
	if ( m_p == 0 )
		return "";

	try {
		XmlInitializer::InitXml();
		static const XMLCh LS[] = { chLatin_L, chLatin_S, chNull };
		DOMImplementation* impl = DOMImplementationRegistry::getDOMImplementation(LS);
		if ( impl == 0 ) {
			throw CrXmlException("Internal Error: Failed to get the document as a string. The DOMImplementation could not be created. The specified feature is not supported.");
		}

		DOMImplementationLS* ls = dynamic_cast<DOMImplementationLS*>(impl);
        if (!ls) throw CrXmlException("DOM Load/Save is unavailable");
        DOMLSSerializer* writer = ls->createLSSerializer();
		if ( writer == 0 ) {
			throw CrXmlException("Internal Error: createDOMWriter() returned NULL. Failed to get document as a string");
		}

		AutoReleaseDOMWriter adopt_writer(writer);
		SimpleDOMErrorHandler errorHandler;
		errorHandler.ignoreErrors(false);
		writer->getDomConfig()->setParameter(XMLUni::fgDOMErrorHandler, &errorHandler);
		MemBufFormatTarget buffer;
        DOMLSOutput* output = ls->createLSOutput();
        if (!output) throw CrXmlException("Cannot allocate XML output");
        AutoReleaseDOMOutput adopt_output(output);
        static const XMLCh utf8[] = {'U','T','F','-','8',0};
        output->setEncoding(utf8);
        output->setByteStream(&buffer);
        if (!writer->write(m_p, output)) throw CrXmlException("XML serialization failed: " + errorHandler.getErrors());
        string stXml(reinterpret_cast<const char*>(buffer.getRawBuffer()), buffer.getLen());
		if ( errorHandler.sawErrors() ) {
			throw CrXmlException("Errors occurred while writing the document to a string." + errorHandler.getErrors());
		}
		return stXml;
	}
	catch( const DOMException& ex ) {
		throw CrXmlException("Failed to get document as string. " + XMLChToString(ex.getMessage()));
	}
}

bool xml::DOMNodePtr::operator==(const DOMNodePtr& rhs) const {
	if ( ComparablePtr<DOMNode>::operator==(rhs) )
		return true;

	return (this->m_p == 0) ? false : this->m_p->isSameNode(rhs.m_p);
}

namespace {
    struct XalanWrapperOwner {
        boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesParserLiaison> liaison;
        void operator()(XALAN_CPP_NAMESPACE::XercesDocumentWrapper* wrapper) const {
            liaison->destroyDocument(wrapper);
        }
    };
    boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesDocumentWrapper> createXalanWrapper(DOMDocument* document) {
        boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesParserLiaison> liaison(new XALAN_CPP_NAMESPACE::XercesParserLiaison);
        XALAN_CPP_NAMESPACE::XalanDocument* proxy = liaison->createDocument(document, true, true, true);
        auto wrapper = dynamic_cast<XALAN_CPP_NAMESPACE::XercesDocumentWrapper*>(proxy);
        if (!wrapper) throw konga::xml::CrXmlException("Cannot create Xalan document wrapper");
        return boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesDocumentWrapper>(wrapper, XalanWrapperOwner{liaison});
    }
    XALAN_CPP_NAMESPACE::XercesParserLiaison& wrapperLiaison(const boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesDocumentWrapper>& wrapper) {
        XalanWrapperOwner* owner = boost::get_deleter<XalanWrapperOwner>(wrapper);
        if (!owner) throw konga::xml::CrXmlException("Xalan wrapper ownership is invalid");
        return *owner->liaison;
    }
}
                                          
using XALAN_CPP_NAMESPACE::XPathEvaluator;
using XALAN_CPP_NAMESPACE::XercesDOMSupport;
using XALAN_CPP_NAMESPACE::XalanNode;
using XALAN_CPP_NAMESPACE::NodeRefList;
using XALAN_CPP_NAMESPACE::XalanDOMString;
using XALAN_CPP_NAMESPACE::XSLException;
using XALAN_CPP_NAMESPACE::XercesWrapperHelper;
using XALAN_CPP_NAMESPACE::XalanDocument;;
using XALAN_CPP_NAMESPACE::XercesDocumentWrapper;

void xml::DOMNodePtr::addSelectionNamespace(const std::string& stPrefix, const std::string& stUri) {
	if ( !m_rcDoc.isNull() && m_rcDoc->m_pxDoc != 0 ) {
		m_rcDoc->m_SelectionNamespaces[stPrefix] = stUri;
	}
}

xml::DOMNodePtr xml::DOMNodePtr::selectSingleNode(const string& stXPath) const {
	if ( m_p == 0 )
		return 0;

	bool bIsDocument = false;
	XERCES_CPP_NAMESPACE::DOMDocument* pOwnerDoc = m_p->getOwnerDocument();
	if ( pOwnerDoc == 0 ) {
		                                                          
		pOwnerDoc = dynamic_cast<XERCES_CPP_NAMESPACE::DOMDocument*>(m_p);
		if ( pOwnerDoc == 0 )
			throw CrXmlException("Failed to select single node using XPath query '" + stXPath
				+ "'. The context node does not have an owner document and it is not itself a document.");
		bIsDocument = true;
	}

	                                               
	if ( m_rcDoc.m_pDoc->m_pXercesDocumentWrapper.get() == 0 ) {
		m_rcDoc.m_pDoc->m_pXercesDocumentWrapper = createXalanWrapper(pOwnerDoc);
	}

	                            
	XercesDocumentWrapper* pXercesWrapper = m_rcDoc.m_pDoc->m_pXercesDocumentWrapper.get();

	XalanNode* contextNode = bIsDocument ? pXercesWrapper : pXercesWrapper->mapNode(m_p);
	assert(contextNode);
	try {
		XPathEvaluator evaluator;
		XercesDOMSupport domSupport(wrapperLiaison(m_rcDoc.m_pDoc->m_pXercesDocumentWrapper));

		SimpleNamespaceResolver nsResolver( m_rcDoc->getSelectionNamespaces() );
		XalanNode* selectedXalanNode = evaluator.selectSingleNode(domSupport, contextNode,
			XalanDOMString(stXPath.c_str()).c_str(), nsResolver);
		if ( selectedXalanNode == 0 )
			return 0;
		else {
			const DOMNode* selectedXercesNode = pXercesWrapper->mapNode(selectedXalanNode);
			assert(selectedXercesNode);	                              
			                                                          
			return DOMNodePtr(const_cast<DOMNode*>(selectedXercesNode), m_rcDoc);
		}
	}
	catch( const XSLException&  ex  ) {
		throw CrXmlException("The XPath expression \"" + stXPath + "\" is not valid. " + XMLChToString( XercesWrapperHelper::XalanDOMStringToXercesDOMString( ex.getMessage() )  ));
	}
#ifndef _DEBUG
	catch(...)
	{
		throw CrXmlException("The XPath expression \"" + stXPath + "\" is probably not valid (unknown exception in selectSingleNode)");
	}
#endif
}

xml::DOMNodeListPtr xml::DOMNodePtr::selectNodes(const string& stXPath) const {
	if ( m_p == 0 )
		return 0;

	bool bIsDocument = false;
	XERCES_CPP_NAMESPACE::DOMDocument* pOwnerDoc = m_p->getOwnerDocument();
	if ( pOwnerDoc == 0 ) {
		                                                          
		pOwnerDoc = dynamic_cast<XERCES_CPP_NAMESPACE::DOMDocument*>(m_p);
		if ( pOwnerDoc == 0 )
			throw CrXmlException("Failed to select nodes using XPath query '" + stXPath
				+ "'. The context node does not have an owner document.");
		bIsDocument = true;
	}
	                                               
	if ( m_rcDoc.m_pDoc->m_pXercesDocumentWrapper.get() == 0 ) {
		m_rcDoc.m_pDoc->m_pXercesDocumentWrapper = createXalanWrapper(pOwnerDoc);
	}

	                            
	XercesDocumentWrapper* pXercesWrapper = m_rcDoc.m_pDoc->m_pXercesDocumentWrapper.get();

	XalanNode* contextNode = bIsDocument ? pXercesWrapper : pXercesWrapper->mapNode(m_p);
	assert(contextNode);
	try {
		XPathEvaluator evaluator;
		XercesDOMSupport domSupport(wrapperLiaison(m_rcDoc.m_pDoc->m_pXercesDocumentWrapper));

		SimpleNamespaceResolver nsResolver( m_rcDoc->getSelectionNamespaces() );
		auto_ptr<NodeRefList> pResult(new NodeRefList);
		evaluator.selectNodeList(*pResult, domSupport, contextNode,
			XalanDOMString(stXPath.c_str()).c_str(), nsResolver);

		return xml::DOMNodeListPtr(m_rcDoc.m_pDoc->m_pXercesDocumentWrapper, pResult.release(), m_rcDoc);
	}
	catch( const XSLException& ex ) {
		throw CrXmlException("The XPath expression \"" + stXPath + "\" is not valid. " + XMLChToString( XercesWrapperHelper::XalanDOMStringToXercesDOMString( ex.getMessage() )  ));
	}
#ifndef _DEBUG
	catch(...)
	{
		throw CrXmlException("The XPath expression \"" + stXPath + "\" is probably not valid (unknown exception).");
	}
#endif
}

                                                                

