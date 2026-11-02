/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef EW_DOM_NAMED_NODE_MAP_PTR_H
#define EW_DOM_NAMED_NODE_MAP_PTR_H

                                                                                                                          
#include <xercesc/dom/DOMNamedNodeMap.hpp>

#include "DOMNodePtr.h"
#include "ComparablePtr.h"
#include "DOMNodeListBase.h"

namespace konga {
    namespace xml {
    	class DOMNamedNodeMapPtr : public ComparablePtr<XERCES_CPP_NAMESPACE::DOMNamedNodeMap>, public DOMNodeListBase<XERCES_CPP_NAMESPACE::DOMNamedNodeMap>
    	{
        public:
            DOMNamedNodeMapPtr(XERCES_CPP_NAMESPACE::DOMNamedNodeMap* px, const RCDocPtr& doc);
            DOMNamedNodeMapPtr(int dummy = 0);
			~DOMNamedNodeMapPtr(void);
    
			const DOMNamedNodeMapPtr* operator->() const { return this; }
			DOMNamedNodeMapPtr* operator->() { return this; }
    
            DOMNodePtr getNamedItem(const std::string& stName) const;
    		DOMNodePtr getNamedItemNS(const std::string& stNamespaceUri, const std::string& stLocalName) const;

			DOMNodePtr setNamedItem(const DOMNodePtr& node);
			DOMNodePtr setNamedItemNS(const DOMNodePtr& node);
			DOMNodePtr removeNamedItem(const std::string& stName);
			DOMNodePtr removeNamedItemNS(const std::string& stNamespaceUri, const std::string& stName);
       	};
    }
}
#endif                               
