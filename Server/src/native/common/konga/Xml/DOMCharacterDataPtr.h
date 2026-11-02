/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DOM_CHARACTER_DATA_PTR_H
#define KONGA_DOM_CHARACTER_DATA_PTR_H

#include "DOMNodePtr.h"

namespace XERCES_CPP_NAMESPACE {
	class DOMCharacterData;
}

namespace konga {             
    namespace xml {

    	class DOMCharacterDataPtr : public DOMNodePtr
    	{
		public:
            DOMCharacterDataPtr(XERCES_CPP_NAMESPACE::DOMCharacterData* pi, const RCDocPtr& doc);    
            DOMCharacterDataPtr(int dummy = 0);
			DOMCharacterDataPtr(const DOMNodePtr& charData);
    		virtual ~DOMCharacterDataPtr(void);
    
			const DOMCharacterDataPtr* operator->() const { return this; }
			DOMCharacterDataPtr* operator->() { return this; }

			std::string getData() const;

			long getLength() const;

			void setData(const std::string& stData);	                                    
    
    	private:
    		XERCES_CPP_NAMESPACE::DOMCharacterData* m_pxCharacterData;
    	};
    }
}
#endif                                  

