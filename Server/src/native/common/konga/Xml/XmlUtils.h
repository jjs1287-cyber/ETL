/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_XML_UTILS
#define KONGA_XML_XML_UTILS
  
                                                                 
                                           
  

#include <string>
#include <map>

#include <konga/Xml/XmlIncludes.h>

namespace konga {
    namespace XmlUtils
    {    
    	void GetNodeNameAndNamespace(const xml::DOMNodePtr& pNode,
    										std::string& stNodeName,
    										std::string& stNamespaceURI);
    
    	void GetNodeNameAndNamespacePrefix(const xml::DOMNodePtr& pNode,
    											  std::string& stNodeName,
    											  std::string& stNamespacePrefix);
		void GetPrefixAndLocalName(const std::string& stQualifiedName,
								   std::string& stNamespacePrefix,
								   std::string& stLocalName);
    
    	std::string GetNodePrefix(const xml::DOMNodePtr& pNode);

		                                                                   
		                                                                 
		int TransferNameSpace(const xml::DOMElementPtr& pSourceElement,
    						  xml::DOMElementPtr& pTargetElement,
							  bool bTransferDefaultNamespace,
							  bool bTransferDuplicateURIs,
							  std::string& stTargetNameSpacePrifix,
							  std::string& stError);
    
    	                                                                                          
    	                                                     
    	long CopyAttributes(xml::DOMDocumentPtr& pDOMDoc,
							const xml::DOMElementPtr& pSource,
							xml::DOMElementPtr& pTarget);
    
    	void GetDeclaredNamespaces(const xml::DOMElementPtr& pRoot,
    									  std::map<std::string, std::string>& mapPrefixToUri,
    									  std::map<std::string, std::string>& mapUriToPrefix);
    	
    	std::string GetNodeValue(const xml::DOMNodePtr& pNode);
    
    	                                                                                                     
    	std::string GetNodeName(const xml::DOMNodePtr& pNode);
    
    	std::string GetNodeNamespaceURI(const xml::DOMNodePtr& pNode);

		                                                                 
    	std::string GetNodeBaseName(const xml::DOMNodePtr& pNode);

		                                      
    	std::string GetAttributeValue(const xml::DOMAttributePtr& pAttribute);

		                                                    
		                                                             
		                                                                    
    	std::string GetAttributeValue(const xml::DOMElementPtr& pElement,
			const std::string& sAttributeName);

		                                                                
		std::string GetAttributeValueNS(const xml::DOMElementPtr& pElement,
			const std::string& stNamespaceUri, const std::string& stLocalName);
    
    	std::string GetLastError(const xml::DOMDocumentPtr& pDOMDocument, const std::string& stHeader = "");
    
    	xml::DOMElementPtr FindElementByTypeAndName(const xml::DOMElementPtr& pElement, const char* sType, const char* sName);

		                                              
    	xml::DOMDocumentPtr LoadDoc(const std::string& stXmlFileName, std::string& stErr, bool bValidateOnParse = true);

		                                                                                
		                                                                                            
		                                                                                          
		xml::DOMDocumentPtr LoadDoc(const std::string& stXmlFileName, const std::string& stSchema,
			const std::string& stNamespace, std::string& stErr);

		                                                                                
		                                      
		xml::DOMDocumentPtr LoadDocumentFromString(const std::string& stXmlString,
			std::string& stErr, bool bValidate = false);

		                                                       
    	long CreateDOMDocument2Instance(xml::DOMDocumentPtr& pDoc, bool bAddProcessingInstruction, std::string& stErr);

		                                                                                   
		xml::DOMDocumentPtr CreateDOMDocument(std::string& stErr, bool bAddProcessingInstruction = true);
    
    	long InsertAsFirstChild(xml::DOMElementPtr pParent, xml::DOMElementPtr pToBeInserted, std::string& stError);
    
    	                                                                                           
    	                                                                                                       
    	                                                                               
    	xml::DOMElementPtr CloneElement(xml::DOMDocumentPtr& pDoc,
										const xml::DOMElementPtr& pOriginal,
										const std::string& stNamespaceURI,
										std::string& stErr);
    
    	                                                         
		                                                               
		                                                                      
        bool ValidateXmlSchema(const std::string& stSchemaFilePath, std::string& stError);
		bool ValidateXmlInstanceAgainstSchema(const std::string& stXmlFilePath, const std::string& stSchemaFilePath,
			const std::string& stTargetNamespace, std::string& stError);

    	                                                                                      
    	                            
    	int GetIndexOfChildNode(const xml::DOMNodePtr& pParent, const xml::DOMNodePtr& pChild);
    
    	                                                                                                    
    	                                                                                                  
    	                                                                                                        
    	                                                         
    	                                                                                     
    	std::string TransferNamespacePrefix(const xml::DOMElementPtr& pSource, 
    										  xml::DOMElementPtr& pTarget,
    										  const std::string& stNamespacePrefix);
    
    	                                                                                              
    	                                                                                                 
    	                                                                                         
    	                                                                                  
    	std::string GetNamespacePrefix(const xml::DOMElementPtr& pElement,
    									 const std::string& stNamespaceURI);
    
    	                                                                                              
    	                                                                                            
    	                                                                                         
    	                                                                         
    	std::string GetNamespaceURI(const xml::DOMElementPtr& pElement,
    								  const std::string& stPrefix);
    
    	std::string GetQualifiedName(const std::string& stPrefix, const std::string& stLocalName);
    
    	                                                                                                                     
    	                                                                                         
    	                                                               
    	                                                                                       
		                               
                                                                                                                                       
    
    	void AddNamespaceDeclaration(xml::DOMElementPtr pElement, const std::string& stNamespaceURI, const std::string& stNamespacePrefix);
    
    	                                                                                               
    	                                                                                              
    	                                                                                            
    	                
    	std::string FindNamespacePrefixInChildNodes(const xml::DOMElementPtr& pElement,
    												  const std::string& stNamespaceURI);
    
    	std::string GetLocalName(const std::string& stQualifiedName);
    
    	std::string GetNamespacePrefix(const std::string& stQualifiedName);
    
    	bool HasChildrenOfType(const xml::DOMElementPtr& pElement, xml::DOMNodeType nodeType);
    
    	xml::DOMElementPtr GetChildElement(const xml::DOMElementPtr& pParent, 
    													const std::string& stChildBaseName, 
    													bool bIgnoreCase = true);

		                                              
		                                                                  
		xml::DOMElementPtr GetFirstChildElement(const xml::DOMNodePtr& parent);

		                                                              
		bool IsValueTrue(const std::string& value);
		
		                                                                                   
		bool IsTrue(konga::xml::DOMElementPtr e, const std::string& stAttributeName);

		                                                                 
		bool IsTrue(konga::xml::DOMAttributePtr a);
    }
}
#endif                       
