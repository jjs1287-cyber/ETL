/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DOM_TEXT_PTR_H
#define KONGA_DOM_TEXT_PTR_H

#include "DOMCharacterDataPtr.h"

namespace XERCES_CPP_NAMESPACE {
	class DOMText;
}

namespace konga {
    namespace xml {

    	class DOMTextPtr : public DOMCharacterDataPtr
    	{
    	public:
            DOMTextPtr(XERCES_CPP_NAMESPACE::DOMText* pText, const RCDocPtr& doc);
            DOMTextPtr(int dummy = 0);
			DOMTextPtr(const DOMNodePtr& text);
    		~DOMTextPtr(void);
    
			const DOMTextPtr* operator->() const { return this; }
			DOMTextPtr* operator->() { return this; }
    
    	private:
    		XERCES_CPP_NAMESPACE::DOMText* m_pxText;
    	};
    }
}
#endif                        

