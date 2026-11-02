/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_RCDOCPTR_H
#define KONGA_XML_RCDOCPTR_H

#include "DOMDocument_ptr.h"

namespace konga {
	namespace xml {

		class DOMNodePtr;
		   
                                                                                             
                                                                                     
     
		class RCDocPtr {
		public:
			RCDocPtr(DOMDocument_ptr* pDOMDoc_ptr = 0);
			RCDocPtr(const RCDocPtr& rhs);
			~RCDocPtr(void);
	
			RCDocPtr& operator=(DOMDocument_ptr* pDOMDoc_ptr);
			RCDocPtr& operator=(const RCDocPtr&);
	
			DOMDocument_ptr* operator->() const;
			DOMDocument_ptr& operator*() const;

			bool isNull() const;
	
		private:
			friend class konga::xml::DOMNodePtr;
			DOMDocument_ptr* m_pDoc;
		};
	}
}
#endif                        

