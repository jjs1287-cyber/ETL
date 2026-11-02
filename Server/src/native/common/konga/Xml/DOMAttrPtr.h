/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef EW_DOM_ATRRIBUTE_PTR_H
#define EW_DOM_ATRRIBUTE_PTR_H

#include "DOMNodePtr.h"

namespace XERCES_CPP_NAMESPACE {
	class DOMAttr;
}

namespace konga {
    namespace xml {

		class DOMElementPtr;

        class DOMAttrPtr : public DOMNodePtr
        {
        public:
            DOMAttrPtr(XERCES_CPP_NAMESPACE::DOMAttr* pxAtr, const RCDocPtr& doc);
            DOMAttrPtr(int dummy = 0);
            DOMAttrPtr(const DOMNodePtr& node);
            virtual ~DOMAttrPtr(void);
    
			const DOMAttrPtr* operator->() const { return this; }
			DOMAttrPtr* operator->() { return this; }

			std::string getName() const;
    
            std::string getValue() const;
            void setValue(const std::string& stValue);

			DOMElementPtr getOwnerElement() const;

            friend class DOMElementPtr;                                                
    
        private:
            XERCES_CPP_NAMESPACE::DOMAttr* m_pxAttr;
        };
    
        typedef DOMAttrPtr DOMAttributePtr;                                         
    }
}
#endif                          

