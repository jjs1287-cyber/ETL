/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <xercesc/dom/DOMException.hpp>
#include <xercesc/util/XMLException.hpp>

#include "CrXmlException.h"
#include "XmlCommon.h"

using namespace konga;

XERCES_CPP_NAMESPACE_USE

xml::CrXmlException::CrXmlException(const DOMException& cause, const std::string& stPrepenMessage) {
	SetErrorString(XMLChToString(cause.getMessage()));
	SetErrorNumber(cause.code);
	PrependErrorString(stPrepenMessage);
} 

xml::CrXmlException::CrXmlException(const XMLException& cause, const std::string& stPrepenMessage) {
	SetErrorString(XMLChToString(cause.getMessage()));
	SetErrorNumber(cause.getCode());
	PrependErrorString(stPrepenMessage);
}

