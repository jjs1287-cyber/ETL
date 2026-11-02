/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include "XmlCommon.h"
#include "SimpleSAXHandler.h"

using namespace std;
using namespace konga;
using namespace konga::xml;

XERCES_CPP_NAMESPACE_USE


void SimpleSAXHandler::error(const SAXParseException& ex) {
	const string stSystemId( XMLChToString(ex.getSystemId()) );

	const XMLSSize_t line = ex.getLineNumber();
	const XMLSSize_t col = ex.getColumnNumber();
	const string msg(XMLChToString(ex.getMessage()));
	m_errors.AddError(stSystemId, line, col, msg);
}

void SimpleSAXHandler::warning(const SAXParseException& ex) {
	const string stSystemId( XMLChToString(ex.getSystemId()) );
	const XMLSSize_t line = ex.getLineNumber();
	const XMLSSize_t col = ex.getColumnNumber();
	const string msg(XMLChToString(ex.getMessage()));
	m_warnings.AddError(stSystemId, line, col, msg);
}

void SimpleSAXHandler::resetErrors() {
	HandlerBase::resetErrors();
	m_errors.resetErrors();
	m_warnings.resetErrors();
}

std::string SimpleSAXHandler::getErrors() const {
	return m_errors.getPrettyErrorString(true);
}

std::string SimpleSAXHandler::getWarnings() const {
	return m_warnings.getPrettyErrorString(false);
}

bool SimpleSAXHandler::hasErrors() const {
	return !m_errors.empty();
}

bool SimpleSAXHandler::hasWarnings() const {
	return !m_errors.empty();
}

