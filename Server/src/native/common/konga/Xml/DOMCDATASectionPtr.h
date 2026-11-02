/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DOM_CDATASECTION_PTR_H
#define KONGA_DOM_CDATASECTION_PTR_H

#include "DOMTextPtr.h"

namespace XERCES_CPP_NAMESPACE {
	class DOMCDATASection;
}

namespace konga {             
    namespace xml {

    	class DOMCDATASectionPtr : public DOMTextPtr
    	{
    	public:
            DOMCDATASectionPtr(XERCES_CPP_NAMESPACE::DOMCDATASection* pCdata,
				const RCDocPtr& doc);
            DOMCDATASectionPtr(int dummy = 0);
			DOMCDATASectionPtr(const DOMNodePtr& text);
    		~DOMCDATASectionPtr(void);
    
			const DOMCDATASectionPtr* operator->() const { return this; }
			DOMCDATASectionPtr* operator->() { return this; }
    
    	private:
    		XERCES_CPP_NAMESPACE::DOMCDATASection* m_pxCdataSection;
		};
	}
}
#endif                                

