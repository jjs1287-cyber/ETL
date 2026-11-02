/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_DOM_NODE_PTR_H
#define KONGA_XML_DOM_NODE_PTR_H

  
                                                     
                                                                                                       
                        
  
        
                                                                                                
                                                                         
   

#include <string>

#include <xercesc/util/XercesDefs.hpp>
#include <xercesc/dom/DOMNode.hpp>

#include "RCDocPtr.h"
#include "ComparablePtr.h"

namespace konga {
    namespace xml {
    	class DOMNamedNodeMapPtr;
    	class DOMNodeListPtr;
		class DOMElementListPtr;

		                                                     
        enum DOMNodeType {
            KONG_NON_INITIALIZED		= 0,
            ELEMENT_NODE                = 1,
            NODE_ELEMENT                = 1,
            ATTRIBUTE_NODE              = 2,
            NODE_ATTRIBUTE              = 2,
            TEXT_NODE                   = 3,
            NODE_TEXT                   = 3,
            CDATA_SECTION_NODE          = 4,
			NODE_CDATA_SECTION          = 4,
            ENTITY_REFERENCE_NODE       = 5,
			NODE_ENTITY_REFERENCE       = 5,
            ENTITY_NODE                 = 6,
            NODE_ENTITY                 = 6,
            PROCESSING_INSTRUCTION_NODE = 7,
			NODE_PROCESSING_INSTRUCTION = 7,
            COMMENT_NODE                = 8,
			NODE_COMMENT                = 8,
            DOCUMENT_NODE               = 9,
			NODE_DOCUMENT               = 9,
            DOCUMENT_TYPE_NODE          = 10,
			NODEDOCUMENT_TYPE           = 10,
            DOCUMENT_FRAGMENT_NODE      = 11,
			NODE_DOCUMENT_FRAGMENT      = 11,
            NOTATION_NODE               = 12,
			NODE_NOTATION               = 12
        };

		class DOMNodePtr : public ComparablePtr<XERCES_CPP_NAMESPACE::DOMNode>
    	{
        public:
			DOMNodePtr(XERCES_CPP_NAMESPACE::DOMNode* pNode, const RCDocPtr& rcdoc);
			                                                               
			                                    
			DOMNodePtr(int dummy = 0);

			virtual ~DOMNodePtr();

			const DOMNodePtr* operator->() const { return this; }
			DOMNodePtr* operator->() { return this; }

    		std::string getNodeName() const;
    		std::string getNodeValue() const;
			std::string getText() const;                                                     
			std::string getTextContent() const;
    		std::string getNamespaceURI() const;
    		std::string getBaseName() const;                                      
    		std::string getLocalName() const;
    		std::string getPrefix() const;
    		DOMNodeType getNodeType() const;
    		bool isElementNode() const;

            bool hasChildNodes() const;
    		DOMNodePtr getFirstChild() const;
            DOMNodePtr getLastChild() const;
            DOMNodeListPtr getChildNodes() const;

            DOMNodePtr getParentNode() const;
            DOMNodePtr getPreviousSibling() const;
            DOMNodePtr getNextSibling() const;

			bool hasAttributes() const;
    		DOMNamedNodeMapPtr getAttributes() const;

			void setNodeValue(const std::string& stValue);                                     

			void setPrefix(const std::string& stPrefix);

			void setTextContent(const std::string& stTextContent);	                                    
			void setText(const std::string& stTextContent);	                         

			std::string lookupNamespacePrefix(const std::string& namespaceURI, bool bUseDefault) const;
			std::string lookupNamespaceURI(const std::string& prefix) const;
    
    		DOMNodePtr appendChild(const DOMNodePtr& newChild);                                     
			DOMNodePtr removeChild(const DOMNodePtr& oldChild);                                     
			DOMNodePtr replaceChild(const DOMNodePtr& newChild, const DOMNodePtr& oldChild);                                     
    		DOMNodePtr insertBefore(const DOMNodePtr& newChild, const DOMNodePtr& refChild);                                     

			                                                                                 
			                                                                                                                 
    		DOMNodePtr cloneNode(bool bDeep) const;

			std::string getXml() const;

			                                                                                             
			                                                                                               
			virtual void addSelectionNamespace(const std::string& stPrefix, const std::string& stUri);

			virtual DOMNodePtr selectSingleNode(const std::string& stXPath) const;
			virtual DOMNodeListPtr selectNodes(const std::string& stXPath) const;

			friend class DOMNamedNodeMapPtr;	                                         
			friend class DOMDocumentPtr;		        

                                                                                                               
    		bool operator==(const DOMNodePtr& rhs) const;

		protected:
			RCDocPtr m_rcDoc;

		private:
			                                                                                           
			                                                                         
			                                                                                      
			                                                      
			DOMNodePtr ImportIfNeeded(const DOMNodePtr& newNode);

		protected:
#ifdef _DEBUG
#ifdef WIN32
			                                
			std::string m_sDebugNodeName;
#endif
#endif
    	};
    }
}
#endif                            

