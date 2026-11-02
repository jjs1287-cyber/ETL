/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_DOM_DOCUMENT_PTR_H
#define KONGA_XML_DOM_DOCUMENT_PTR_H

#include <string>
#include <map>

#include <boost/shared_ptr.hpp>

#include <xercesc/util/XercesVersion.hpp>                                
#include <xalanc/XercesParserLiaison/XercesDocumentWrapper.hpp>

namespace XERCES_CPP_NAMESPACE {
    class DOMDocument;
}

namespace konga {
    namespace xml {
    	   
                                                                
                                                                              
                                                        
     
                                                                                                   
                                        
     
     
                       
        
    	class DOMDocument_ptr
    	{
    	private:
    		DOMDocument_ptr(void);
		public:
    		~DOMDocument_ptr(void);

		public:
			std::map<std::string, std::string> getSelectionNamespaces() const;

		private:
    		void incrementRef();
    		void decrementRef();

    		                                   
    		bool init();
    
    		                                                   
    		bool load(const std::string& stFileName, bool bValidateOnParse,
				bool bCreateCommentNodes = false, bool bIncludeIgnorableWhitespace = false);

			                                                   
			                                              
			                                                                              
			bool load(const std::string& stXmlFileName, const std::string& stStructureFile,
				const std::string& stSchemaNamespace = "",
				bool bCreateCommentNodes = false, bool bIncludeIgnorableWhitespace = false);

			bool loadString(const std::string& stFileName, bool bValidateOnParse,
				bool bCreateCommentNodes = false, bool bIncludeIgnorableWhitespace = false);

			                                            
			bool save(const std::string& strFileName) const;
    
    		                                                
    		std::string getLastError() const;
    
    		                                                  
    		std::string getParseErrors() const;

			                                   
    		std::string getParseWarnings() const;

			friend class DOMNodePtr;
			friend class DOMDocumentPtr;		                                                                                                      
			friend class AutoCallReleaseXml;	                        
			friend class RCDocPtr;				                                           

    		void release();
    		void initXml();

    	private:
			unsigned int m_iRefCount;
			XERCES_CPP_NAMESPACE::DOMDocument* m_pxDoc;
			std::map<std::string, std::string> m_SelectionNamespaces;

    		mutable std::string m_stLastError, m_stParseErrors, m_stParseWarnings;

			                                                                                   
			                                             
			mutable boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesDocumentWrapper>
				m_pXercesDocumentWrapper;
    	};
    }
}
#endif                                

