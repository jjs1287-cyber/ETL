/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_EW_DOMDOCUMENT_2_PTR_H
#define KONGA_XML_EW_DOMDOCUMENT_2_PTR_H

#include <string>

#include "DOMNodePtr.h"

namespace konga {
    namespace xml {
        class DOMProcessingInstructionPtr;
        class DOMNodeListPtr;
        class DOMElementPtr;
        class DOMAttrPtr;
		class DOMDocumentTypePtr;
		class DOMTextPtr;
		class DOMCDATASectionPtr;

		   
                                                                                                                     
                                              
     
                                              
                        
                           
                                
     
    	class DOMDocumentPtr : public DOMNodePtr
    	{
    	public:
			                                                                                                                   
			                                                            
    		DOMDocumentPtr(int i = 0);
    		virtual ~DOMDocumentPtr(void);

    	public:
			const DOMDocumentPtr* operator->() const { return this; }
			DOMDocumentPtr* operator->() { return this; }

			                                
			                             
    		long CreateInstance();
    
    		DOMElementPtr getDocumentElement() const;
			void setDocumentElement(const DOMElementPtr& element);                                                                                         
			DOMDocumentTypePtr getDocType() const;
    
            DOMElementPtr createElement(const char* tagName);
            DOMElementPtr createElement(const std::string& strTagName);

			DOMProcessingInstructionPtr createProcessingInstruction(const std::string& strTarget, const std::string& strData);
    		DOMElementPtr createElementNS(const std::string& stNamespaceURI, const std::string& stQualifiedName );

			DOMDocumentTypePtr createDocumentType(const std::string& stName);

    		DOMAttrPtr createAttribute(const std::string& stName);
    		DOMAttrPtr createAttribute(const std::string& stNamespace, const std::string& stQualifiedName);
			DOMAttrPtr createAttributeNS(const std::string& stNamespace, const std::string& stQualifiedName);

			DOMTextPtr createTextNode(const std::string& stData);
			DOMCDATASectionPtr createCDATASectionNode(const std::string& stData);

			DOMNodePtr createNode(DOMNodeType type, const std::string& stName, const std::string& stNamespaceUri);                                                                                                                                                         

			                                                                                                                                           
			                                                                    
			DOMNodePtr importNode(const DOMNodePtr& importedNode, bool bDeep);

            DOMNodeListPtr getElementsByTagName(const std::string& stTagName) const;

			                              
			                          
    		bool loadFile(const std::string& stFileName);

			                                                                          
			                                                                              
			bool loadFile(const std::string& stXmlFileName, const std::string& stSchemaFile, const std::string& stNamespace);

			bool loadString(const std::string& stXmlString);

			bool normalizeDocument();

			                                
			                          
            bool save(const std::string& strFileName) const;

			                                                                                             
			                                                         
			                                                          
			void addSelectionNamespace(const std::string& stPrefix, const std::string& stUri);

    		std::string getLastError() const;
    		std::string getParseErrors() const;
    		std::string getParseWarnings() const;

			                                                              
			std::string getLoadedDocumentPath() const;

    		bool validateOnParse;	                                         
			bool resolveExternals;	                                    
			bool async;				                                       

    	private:
            bool hasDoc() const;                                                                               
    
    	private:
    		mutable std::string m_stLastError, m_stParseWarnings, m_stParseErrors;
			std::string m_stLoadedDocumentPath;
    	};

        typedef DOMDocumentPtr DOMDocument2Ptr;                                        
    }
}
#endif                                    
