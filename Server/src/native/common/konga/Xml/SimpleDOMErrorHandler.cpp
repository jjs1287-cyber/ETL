/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <sstream>

#include <xercesc/dom/DOMError.hpp>
#include <xercesc/dom/DOMLocator.hpp>
#include <xercesc/dom/DOMNode.hpp>

#include "SimpleDOMErrorHandler.h"
#include "XmlCommon.h"

using namespace konga;

XERCES_CPP_NAMESPACE_USE

xml::SimpleDOMErrorHandler::SimpleDOMErrorHandler(void)
	: m_bWerror(false), m_bIgnoreErrors(false), m_bSawWarnings(false), m_bSawErrors(false), m_stErrors(""), m_stWarnings("") { }

xml::SimpleDOMErrorHandler::~SimpleDOMErrorHandler(void) { }

void xml::SimpleDOMErrorHandler::treatWarningsAsErrors(bool bWerror) {
	m_bWerror = bWerror;
}

void xml::SimpleDOMErrorHandler::ignoreErrors(bool bIgnoreErrors) {
	m_bIgnoreErrors = bIgnoreErrors;
}

bool xml::SimpleDOMErrorHandler::handleError(const DOMError& domError) {
	std::stringstream s;
	std::string* pStr = 0;
	if ( domError.getSeverity() == DOMError::DOM_SEVERITY_WARNING ) {
		m_bSawWarnings = true;
		pStr = &m_stWarnings;
		s << "Warning: ";
	}
	else {
		m_bSawErrors = true;
		pStr = &m_stErrors;
		s << "Error: ";
	}
	s << XMLChToString(domError.getMessage()) << std::endl;

	DOMLocator* location = domError.getLocation();
	if ( location != 0 ) {
		s << "in document " << XMLChToString(location->getURI()) << std::endl;
		s << " at line " << location->getLineNumber() << ", column " << location->getColumnNumber();
		DOMNode* node = location->getRelatedNode();
		if ( node != 0 ) {
			std::string stNodeName = XMLChToString(node->getNodeName());
			if ( stNodeName.size() > 0 )
				s << ", node name " << stNodeName;
		}
		s << '.' << std::endl;
	}

	pStr->append(s.str());

	return (domError.getSeverity() == DOMError::DOM_SEVERITY_FATAL_ERROR) ? false : m_bIgnoreErrors;
}

void xml::SimpleDOMErrorHandler::resetErrors() {
	m_bSawErrors = false;
	m_bSawWarnings = false;
	m_stErrors.erase();
	m_stWarnings.erase();
}

bool xml::SimpleDOMErrorHandler::sawWarnings() const {
	return m_bSawWarnings;
}

bool xml::SimpleDOMErrorHandler::sawErrors() const {
	return m_bSawErrors;
}

std::string xml::SimpleDOMErrorHandler::getErrors() const {
	return m_stErrors;
}

std::string xml::SimpleDOMErrorHandler::getWarnings() const {
	return m_stWarnings;
}
