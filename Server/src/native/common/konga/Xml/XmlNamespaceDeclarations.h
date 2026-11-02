/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_NAMESPACE_DECLARATIONS_H
#define KONGA_XML_NAMESPACE_DECLARATIONS_H

#include <string>
#include <map>
#include <vector>

#include <konga/Utils/StringUtils.h>

                              
                                                

namespace konga {
    namespace xml {
    
        class DOMElementPtr;
    
        typedef std::vector<std::string> StringVector;
    
                                                                          
    	class XmlNamespaceDeclarations {
    	public:
    		XmlNamespaceDeclarations() {}
    		                                                                                      
    		XmlNamespaceDeclarations(const xml::DOMElementPtr& pRootElement);
    		~XmlNamespaceDeclarations(void);
    
    		void Load(const xml::DOMElementPtr& pRootElement);
    
    		void Clear();

			bool IsEmpty() const;
    
    		void AddNamespaceDeclaration(const std::string& stURI, const std::string& stPrefix);
    
    		std::string	GetDefaultNamespace() const { return m_stDefaultNamespace; }
    		bool IsDefaultNamespace(const std::string& stURI) const { return konga::Equals(stURI, m_stDefaultNamespace); }
    		std::string	GetTargetNamespace() const { return m_stTargetNamespace; }
    
    		                                                    
    		bool IsNamespaceDeclared(const std::string& stURI) const { return m_mapUriToPrefix.find(stURI) != m_mapUriToPrefix.end(); }
    		                                                                 
    		bool IsPrefixUsed(const std::string& stPrefix) const { return m_mapPrefixToUri.find(stPrefix) != m_mapPrefixToUri.end(); }
    
    		                                                                                                     
    		                             
    		std::string GetNamespaceURI(const std::string& stPrefix) const;
    
    		                                                                                                    
    		                                                                                                         
    		                                            
    		                                                                                                       
    		std::string GetNamespacePrefix(const std::string& stURI) const;
    
    		                                                                                            
    		                                                                   
    		void GetNamespacePrefixes(const std::string& stURI, StringVector& vstPrefixes) const;
    
    		                                                                                                    
    		                                    
    		void GetNamespaceURIs(StringVector& vstURIs) const;

			                                                                                           
			bool IsExistingNamespaceMapping(const std::string& stPrefix, const std::string& stUri) const;
    
    		                                                                                       
    		                                                                                  
    		                                                                              
    		std::string GetNamespacePrefixCreateIfNotExist(const std::string& stURI, const std::string& stPrefixBase) const;
    
    		                 
    		std::string ToString() const;
    
    	private:
    		std::string									m_stDefaultNamespace;
    		std::string									m_stTargetNamespace;
    		std::map<std::string, std::string>			m_mapPrefixToUri;
    		std::map<std::string, StringVector>			m_mapUriToPrefix;
    
    		void MapUriToPrefix(const std::string& stURI, const std::string& stPrefix);

			void GetNamespacePrefixesImpl(const std::string& stURI, StringVector& vstPrefixes, bool acceptEmptyPrefixes) const;
    
    	};
    }
}
#endif                                      

