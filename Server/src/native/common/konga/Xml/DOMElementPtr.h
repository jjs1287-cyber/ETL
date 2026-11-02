/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef EW_DOM_ELEMENT_PTR_H
#define EW_DOM_ELEMENT_PTR_H

#include "DOMNodePtr.h"

namespace XERCES_CPP_NAMESPACE {
	class DOMElement;
}

namespace konga {
    namespace xml {
        class DOMAttrPtr;

    	class DOMElementPtr : public DOMNodePtr
    	{
		public:
            DOMElementPtr(XERCES_CPP_NAMESPACE::DOMElement* element, const RCDocPtr& doc);
            DOMElementPtr(int dummy = 0);
               
                                                                       
                                                                                               
               
            DOMElementPtr(const DOMNodePtr& node);
    		virtual ~DOMElementPtr(void);
    
			const DOMElementPtr*  operator->() const { return this; }
			DOMElementPtr* operator->() { return this; }

			
			                                             
			                                                                                       
			                                                                                                             
			                                                                                                        
    		std::string getAttribute(const std::string& stName, bool bRequired = false) const;

			                                                                                          
			                                                 
			std::string getAttributeNS(const std::string& stNamespaceUri, const std::string& stLocalName, bool bRequired = false) const;

			                                                         
			DOMAttrPtr getAttributeNode(const std::string& stName) const;

			                                                           
			DOMAttrPtr getAttributeNodeNS(const std::string& stNamespaceUri, const std::string& stLocalName) const;
    
    		long setAttribute(const std::string& stName, const std::string& stValue);
            DOMAttrPtr setAttributeNode(const DOMAttrPtr& newAttr);

			void setAttributeNS(const std::string& stNamespaceUri, const std::string& stQualifiedName, const std::string& stValue);

			void removeAttribute(const std::string& stName);
			DOMAttrPtr removeAttributeNode(const DOMAttrPtr& oldAttr);

			void removeAttributeNS(const std::string& stNamespaceUri, const std::string& stLocalName);

        private:
    		XERCES_CPP_NAMESPACE::DOMElement* m_pxElement;
    	};
    }
}
#endif                       

