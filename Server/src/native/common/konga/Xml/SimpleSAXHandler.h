/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SIMPLE_SAX_ERROR_HANDLER_H
#define KONGA_SIMPLE_SAX_ERROR_HANDLER_H

#include <string>

#include <xercesc/sax/HandlerBase.hpp>

#include "XmlParsingErrors.h"

namespace konga {
	namespace xml {

		class SimpleSAXHandler : public XERCES_CPP_NAMESPACE::HandlerBase  {
		public:
			SimpleSAXHandler(void) { }

			virtual ~SimpleSAXHandler(void) { }

			void error(const XERCES_CPP_NAMESPACE::SAXParseException&);

			void warning(const XERCES_CPP_NAMESPACE::SAXParseException&);

			void resetErrors();

			std::string getErrors() const;
			std::string getWarnings() const;

			bool hasErrors() const;
			bool hasWarnings() const;

		private:
			XmlParsingErrors m_errors;
			XmlParsingErrors m_warnings;

		};
	}
}
#endif                                    

