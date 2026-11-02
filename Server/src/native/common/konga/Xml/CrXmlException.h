/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_CR_XML_EXCEPTION_H
#define KONGA_CR_XML_EXCEPTION_H

#include <konga/Utils/CrException.h>

namespace XERCES_CPP_NAMESPACE {
	class DOMException;
	class XMLException;
}

namespace konga {
	namespace xml {

		class CrXmlException : public CrException
		{
		public:
			CrXmlException(const std::string& strErr) : CrException(strErr) {}
			CrXmlException(const XERCES_CPP_NAMESPACE::DOMException& cause, const std::string& stPrepenMessage = "");
			CrXmlException(const XERCES_CPP_NAMESPACE::XMLException& cause, const std::string& stPrepenMessage = "");
			virtual ~CrXmlException() {}
		};
	}
}

#endif                            

