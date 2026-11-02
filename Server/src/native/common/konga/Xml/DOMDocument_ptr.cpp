/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <exception>

#include <xercesc/util/OutOfMemoryException.hpp>
#include <xercesc/dom/DOMImplementation.hpp>
#include <xercesc/dom/DOMException.hpp>
#include <xercesc/dom/DOMImplementationRegistry.hpp>
#include <xercesc/dom/DOMDocument.hpp>
#include <xercesc/dom/DOMLSParser.hpp>
#include <xercesc/dom/DOMLSSerializer.hpp>
#include <xercesc/dom/DOMImplementationLS.hpp>
#include <xercesc/dom/DOMConfiguration.hpp>
#include <xercesc/dom/DOMLSOutput.hpp>

#include <xercesc/framework/LocalFileFormatTarget.hpp>
#include <xercesc/framework/MemBufInputSource.hpp>
#include <xercesc/parsers/XercesDOMParser.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/XmlInit.h>

#include "XmlCommon.h"
#include "XmlConstants.h"
#include "XmlSchemaUtils.h"
#include "SimpleDOMErrorHandler.h"
#include "SimpleSAXHandler.h"
#include "XercesXmlUtils.h"	                      
#include "DOMDocument_ptr.h"

using namespace konga;
using std::string;

XERCES_CPP_NAMESPACE_USE

xml::DOMDocument_ptr::DOMDocument_ptr(void) : m_iRefCount(0), m_pxDoc(0) {
	                                             
	m_SelectionNamespaces.insert( std::map<string, string>::value_type(XmlConstants::XML_NAMESPACE_1998_PREFIX, XmlConstants::XML_NAMESPACE_1998) );
}

xml::DOMDocument_ptr::~DOMDocument_ptr(void) {
	release();
}

void xml::DOMDocument_ptr::initXml() {
	release();
	konga::XmlInitializer::InitXml();
}

void xml::DOMDocument_ptr::release() {
    m_pXercesDocumentWrapper.reset();
	if ( m_pxDoc != 0 ) {
		assert(m_iRefCount==0);
		m_pxDoc->release();
		m_pxDoc = 0;
		m_iRefCount = 0;
		m_stLastError.erase();
	}
}

bool xml::DOMDocument_ptr::init() {
	initXml();

    DOMImplementation* impl = DOMImplementationRegistry::getDOMImplementation(X("Core"));
    if (impl == 0) {
		m_stLastError += "Internal Error: Requested DOM implementation is not supported\n";
		return false;
	}

	try {
		m_pxDoc = impl->createDocument();
		if ( m_pxDoc == 0 ) {
			m_stLastError += "Internal Error: createDocument() returned NULL.\n";
			return false;
		}
		return true;
	}
	catch (const OutOfMemoryException&)
	{
		m_stLastError += "Not enough memory available to initialize the document.\n";
		return false;
	}
	catch (const DOMException& e)
	{
		m_stLastError += XMLChToString(e.getMessage())+"\n";
		return false;
	}
	catch( const std::bad_alloc& ex ) {
		m_stLastError += "Not enough memory available to initialize the document (bad allocaction). Details: ";
		m_stLastError += ex.what();
		return false;
	}
	catch( const std::exception& ex ) {
		m_stLastError += "Unexpected std::exception occurred while trying to initialize XML DOM document. Details: ";
		m_stLastError += ex.what();
		return false;
	}
#ifndef _DEBUG
	catch (...)
	{
		m_stLastError += "An unexpected exception was thrown while creating the DOMDocument\n";
		return false;
	}
#endif
}

namespace {
	void ConfigureParser(XercesDOMParser& parser, bool bValidateOnParse,
		bool bCreateCommentNodes, bool bIncludeIgnorableWhitespace)
	{

		                                                                                           
		if ( bValidateOnParse ) {
			parser.setValidationScheme(AbstractDOMParser::Val_Auto);
			                                                                         
			parser.setIdentityConstraintChecking(false);
			parser.setValidationSchemaFullChecking(false);
		}
		else {
			parser.setValidationScheme(AbstractDOMParser::Val_Never);
			parser.setLoadExternalDTD(false);
		}

		parser.setDoSchema(bValidateOnParse);

		parser.setDoNamespaces(true);	                     
		parser.setIncludeIgnorableWhitespace(bIncludeIgnorableWhitespace);
		parser.setCreateCommentNodes(bCreateCommentNodes);
	}
}

bool xml::DOMDocument_ptr::load(const string& stFileName, bool bValidateOnParse, bool bCreateCommentNodes, bool bIncludeIgnorableWhitespace) {
	initXml();

	XercesDOMParser parser;
	SimpleSAXHandler errorHandler;
	parser.setErrorHandler(&errorHandler);

	ConfigureParser(parser, bValidateOnParse, bCreateCommentNodes, bIncludeIgnorableWhitespace);

	konga::XercesBOMWorkaround(stFileName);
	try {
		parser.parse(stFileName.c_str());
	}
	catch( const SAXException& ex ) {
		string sErr("Fatal SAX error occurred while parsing the document \"" + stFileName + "\". Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const XMLException& ex ) {
		string sErr("Fatal XML error occurred while parsing the document \"" + stFileName + "\". Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const DOMException& ex ) {
		string sErr("Fatal DOM error occurred while parsing the document \"" + stFileName + "\". Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const std::bad_alloc& ex ) {
		string sErr("Out-of-memory exception while parsing the document \"" + stFileName + "\". Reason:\n");
		sErr += ex.what();
		m_stParseErrors = sErr;
		return false;
	}
	catch( const std::exception& ex ) {
		string sErr("Unexpected std::exception occurred while parsing the document \"" + stFileName + "\". Reason:\n");
		sErr += ex.what();
		m_stParseErrors = sErr;
		return false;
	}
#ifndef _DEBUG
	catch (...)
	{
		m_stParseErrors = "An unexpected exception was thrown while parsing the file \"" + stFileName + "\".";
		return false;
	}
#endif

	if ( errorHandler.hasErrors() )
		m_stParseErrors = errorHandler.getErrors();
	if ( errorHandler.hasWarnings() )
		m_stParseWarnings = errorHandler.getWarnings();

	if ( errorHandler.hasErrors() )
		return false;

	                                                                         
	m_pxDoc = parser.adoptDocument();
	return true;
}

bool xml::DOMDocument_ptr::load(const string& stXmlFileName, const string& stStructureFile,
								const string& stSchemaNamespace,
								bool bCreateCommentNodes, bool bIncludeIgnorableWhitespace) {
	initXml();

	                             
	XercesDOMParser parser;
	SimpleSAXHandler errorHandler;
	parser.setErrorHandler(&errorHandler);

	ConfigureParser(parser, true, bCreateCommentNodes, bIncludeIgnorableWhitespace);

	                                                                                         
	                                                     
	parser.setValidationScheme(AbstractDOMParser::Val_Always);

	                                                                  
	XmlSchemaUtils::XmlSchemaType type = XmlSchemaUtils::XML_DTD;
	try {
		type = XmlSchemaUtils::GetSchemaType(stStructureFile);
	}
	catch( const CrException& ex ) {
		m_stParseErrors = "Failed to determine grammar type for the given structure file. Reason: ";
		m_stParseErrors += ex.GetErrorString();
		return false;
	}

	if ( type == XmlSchemaUtils::XML_DTD )
		throw CrException("Validating an external DTD is not implemented. Use DOCTYPE instead.");

	if ( stSchemaNamespace.empty() )
		parser.setExternalNoNamespaceSchemaLocation(stStructureFile.c_str());
	else {
		string stFileUrl(stStructureFile);
		konga::UrlEscape(stFileUrl);
		const string& stArg = (stSchemaNamespace + " " + stFileUrl);
		parser.setExternalSchemaLocation(stArg.c_str());
	}

	konga::XercesBOMWorkaround(stXmlFileName);
	try {
		parser.parse(stXmlFileName.c_str());
	}
	catch( const SAXException& ex ) {
		string sErr("Fatal SAX error occurred while parsing the document \"" + stXmlFileName + "\". Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const XMLException& ex ) {
		string sErr("Fatal XML error occurred while parsing the document \"" + stXmlFileName + "\". Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const DOMException& ex ) {
		string sErr("Fatal DOM error occurred while parsing the document \"" + stXmlFileName + "\". Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const std::bad_alloc& ex ) {
		string sErr("Out-of-memory exception while parsing the document \"" + stXmlFileName + "\". Reason:\n");
		sErr += ex.what();
		m_stParseErrors = sErr;
		return false;
	}
	catch( const std::exception& ex ) {
		string sErr("Unexpected std::exception occurred while parsing the document \"" + stXmlFileName + "\". Reason:\n");
		sErr += ex.what();
		m_stParseErrors = sErr;
		return false;
	}
#ifndef _DEBUG
	catch (...)
	{
		m_stParseErrors = "An unexpected exception was thrown while parsing the file \"" + stXmlFileName + "\".";
		return false;
	}
#endif

	if ( errorHandler.hasErrors() )
		m_stParseErrors = errorHandler.getErrors();
	if ( errorHandler.hasWarnings() )
		m_stParseWarnings = errorHandler.getWarnings();

	if ( errorHandler.hasErrors() )
		return false;

	                                                                         
	m_pxDoc = parser.adoptDocument();
	return true;
}

bool xml::DOMDocument_ptr::loadString(const string& stXmlString, bool bValidateOnParse,
				bool bCreateCommentNodes, bool bIncludeIgnorableWhitespace)
{
	initXml();

	XercesDOMParser parser;
	SimpleSAXHandler errorHandler;
	parser.setErrorHandler(&errorHandler);

	ConfigureParser(parser, bValidateOnParse, bCreateCommentNodes, bIncludeIgnorableWhitespace);

	try {
		MemBufInputSource src(reinterpret_cast<const XMLByte*>(stXmlString.c_str()),
			stXmlString.size(), "XML String");
		parser.parse(src);
	}
	catch( const SAXException& ex ) {
		string sErr("Fatal SAX error occurred while parsing XML string. Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const XMLException& ex ) {
		string sErr("Fatal XML error occurred while parsing XML string. Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const DOMException& ex ) {
		string sErr("Fatal DOM error occurred while parsing XML string. Reason:\n");
		sErr += XMLChToString( ex.getMessage() );
		m_stParseErrors = sErr;
		return false;
	}
	catch( const std::bad_alloc& ex ) {
		string sErr("Out-of-memory exception while parsing XML string. Reason:\n");
		sErr += ex.what();
		m_stParseErrors = sErr;
		return false;
	}
	catch( const std::exception& ex ) {
		string sErr("Unexpected std::exception occurred while parsing XML string. Reason:\n");
		sErr += ex.what();
		m_stParseErrors = sErr;
		return false;
	}
#ifndef _DEBUG
	catch (...)
	{
		m_stParseErrors = "An unexpected exception was thrown while parsing XML string.";
		return false;
	}
#endif

	if ( errorHandler.hasErrors() )
		m_stParseErrors = errorHandler.getErrors();
	if ( errorHandler.hasWarnings() )
		m_stParseWarnings = errorHandler.getWarnings();

	if ( errorHandler.hasErrors() )
		return false;

	                                                                         
	m_pxDoc = parser.adoptDocument();
	return true;
}

bool xml::DOMDocument_ptr::save(const string& strFileName) const {
	try {
		static const XMLCh LS[] = { chLatin_L, chLatin_S, chNull };
		DOMImplementation* impl = DOMImplementationRegistry::getDOMImplementation(LS);
		if ( impl == 0 ) {
			m_stLastError += "Internal Error: Failed to write document to file. The DOMImplementation could not be created. The specified feature is not supported.\n";
			return false;
		}

		DOMImplementationLS* ls = dynamic_cast<DOMImplementationLS*>(impl);
        if (!ls) { m_stLastError = "DOM Load/Save is unavailable"; return false; }
        DOMLSSerializer* writer = ls->createLSSerializer();
		if ( writer == 0 ) {
			m_stLastError += "Internal Error: createDOMWriter() returned NULL. Failed to write document to file.\n";
			return false;
		}
		AutoReleaseDOMWriter adopt_writer(writer);
		SimpleDOMErrorHandler errorHandler;
		LocalFileFormatTarget file(strFileName.c_str());
		DOMLSOutput* output = ls->createLSOutput();
        if (!output) { m_stLastError = "Cannot allocate XML output"; return false; }
        AutoReleaseDOMOutput adopt_output(output);
        static const XMLCh utf8[] = {'U','T','F','-','8',0};
        output->setEncoding(utf8);
        output->setByteStream(&file);
        writer->getDomConfig()->setParameter(XMLUni::fgDOMErrorHandler, &errorHandler);
        if (!writer->write(m_pxDoc, output)) {
			m_stLastError += "Errors occurred while writing the document to file. ";
			m_stLastError += errorHandler.getErrors();
            m_stLastError += "\n";
			return false;
		}
		return true;
	}
	                                                                        
                                        
     
  
   
	catch( const DOMException& ex ) {
		string sErr("Failed to write document to file. " + XMLChToString( ex.getMessage() ));
		m_stLastError += sErr;
        m_stLastError += "\n";
		return false;
	}
	catch ( const std::bad_alloc& ex ) {
		string sErr("Out-of-memory condition while saving the DOM document to the file \"" + strFileName + "\". Details: ");
		sErr += ex.what();
		m_stLastError = sErr;
		return false;
	}
	catch ( const std::exception& ex ) {
		m_stLastError = "An unexpected std::exception was thrown while saving the DOM document to the file \"" + strFileName + "\": " + string(ex.what());
		return false;
	}
#ifndef _DEBUG
	catch (...)
	{
		m_stLastError = "An unexpected exception was thrown while saving the DOM document to the file \"" + strFileName + "\". Most likely the process ran out of resources.";
		return false;
	}
#endif
}

void xml::DOMDocument_ptr::incrementRef() {
	if ( m_pxDoc != 0 ) {
		assert(m_iRefCount>=0);
		++m_iRefCount;
	}
}

void xml::DOMDocument_ptr::decrementRef() {
	if ( m_pxDoc != 0 ) {
		assert(m_iRefCount>0);
		if ( --m_iRefCount == 0 )
			delete this;
	}
}

string xml::DOMDocument_ptr::getLastError() const {
	return m_stLastError;
}

string xml::DOMDocument_ptr::getParseErrors() const {
	return m_stParseErrors;
}

string xml::DOMDocument_ptr::getParseWarnings() const {
	return m_stParseWarnings;
}

std::map<string, string> xml::DOMDocument_ptr::getSelectionNamespaces() const {
	return m_SelectionNamespaces;
}
