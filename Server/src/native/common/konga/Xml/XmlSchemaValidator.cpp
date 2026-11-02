/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <sstream> 	               

#include <xercesc/sax/SAXException.hpp>
#include <xercesc/dom/DOMException.hpp>
#include <xercesc/parsers/SAXParser.hpp>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/XmlInit.h>

#include "XmlCommon.h"
#include "XmlUtils.h"
#include "SimpleSAXHandler.h"
#include "XmlSchemaValidator.h"

XERCES_CPP_NAMESPACE_USE

using namespace std;
using namespace konga;

xml::XmlSchemaValidator::XmlSchemaValidator(void) {
	konga::XmlInitializer::InitXml();
}
xml::XmlSchemaValidator::~XmlSchemaValidator(void) {
}

bool xml::XmlSchemaValidator::ValidateXmlSchema(const string& stSchemaFilePath, string& stError) const {
	                                                       

	string stLocalErr;
	xml::DOMDocumentPtr pDoc = XmlUtils::LoadDoc(stSchemaFilePath, stLocalErr, false);
	if ( pDoc == 0 ) {
		stError = stLocalErr;
		return false;
	}
	return true;
}

bool xml::XmlSchemaValidator::ValidateXmlInstance(const string& stXmlFilePath, const string& stSchema,
												  const string& stNamespace, string& stError) const {
	if ( !FileExists(stXmlFilePath) ) {
		stError = "The file \n\n" + stXmlFilePath + "\n\n does not exist.";
		return false;
	}

	                                                                 
	if ( stSchema.empty() ) {
		stError = "No schema was specified.";
		return false;
	}

	try {
		SAXParser parser;
		SimpleSAXHandler handler;
		parser.setErrorHandler(&handler);
	
		parser.setDoSchema(true);
		parser.setDoNamespaces(true);
		parser.setValidationScheme(SAXParser::Val_Always);
		parser.setValidationSchemaFullChecking(true);

		string stSchemaUri(stSchema);
		konga::UrlEscape(stSchemaUri);
		if ( stNamespace.empty() )
			parser.setExternalNoNamespaceSchemaLocation(stSchemaUri.c_str());
		else {
			const string& stSchemaLoc = (stNamespace + " " + stSchemaUri);
			parser.setExternalSchemaLocation(stSchemaLoc.c_str());
		}
	
		parser.parse(stXmlFilePath.c_str());
		if ( handler.hasErrors() ) {
			stError = handler.getErrors();
		}
		if ( handler.hasWarnings() ) {
			if ( !stError.empty() )
				stError += "\n";
			stError += handler.getWarnings();
		}

		return !handler.hasErrors();
	}
	catch( const SAXException& ex ) {
		stError = "Failed to validate \n\n" + stXmlFilePath + "\n\nA SAX exception occurred:\n"
			+ XMLChToString(ex.getMessage());
	}
	catch( const XMLException& ex ) {
		stError = "Failed to validate \n\n" + stXmlFilePath + "\n\nAn XML exception occurred:\n"
			+ XMLChToString(ex.getMessage());
	}
	catch( const DOMException& ex ) {
		stError = "Failed to validate \n\n" + stXmlFilePath + "\n\nA DOM exception occurred:\n"
			+ XMLChToString(ex.getMessage());
	}
#ifndef _DEBUG
	catch(...) {
		stError = "Failed to validate \"" + stXmlFilePath + "\". An unexpected exception occurred.";
	}
#endif

	                                      
	return false;
}

