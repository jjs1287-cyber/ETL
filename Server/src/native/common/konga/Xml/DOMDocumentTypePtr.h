/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_DOM_DOCUMENT_TYPE_PTR_H
#define KONGA_XML_DOM_DOCUMENT_TYPE_PTR_H

#include "DOMNodePtr.h"

namespace XERCES_CPP_NAMESPACE {
	class DOMDocumentType;
}

namespace konga {

	namespace xml {

		class DOMDocumentTypePtr : public DOMNodePtr {
		public:
			DOMDocumentTypePtr(XERCES_CPP_NAMESPACE::DOMDocumentType* element, const RCDocPtr& doc);
			DOMDocumentTypePtr(int dummy = 0);
			virtual ~DOMDocumentTypePtr(void) {}

			const DOMDocumentTypePtr* operator->() const { return this; }
			DOMDocumentTypePtr* operator->() { return this; }

			std::string getName() const;

		private:
			XERCES_CPP_NAMESPACE::DOMDocumentType* m_pxDocType;
		};
	}
}
#endif                                     

