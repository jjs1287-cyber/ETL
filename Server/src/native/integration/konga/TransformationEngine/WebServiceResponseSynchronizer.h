/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef WEBSERVICE_RESPONSE_SYNCHRONIZER_H
#define WEBSERVICE_RESPONSE_SYNCHRONIZER_H

#include <string>
#include <map>


#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMNodePtr.h>
#include <konga/Xml/XsdConstants.h>

class CROM;
class XsdNamespaceSupport;

class DOMNodeMapKey
{
public:
	DOMNodeMapKey(konga::xml::DOMNodePtr pNode) : m_pNode(pNode) {};
	DOMNodeMapKey(const DOMNodeMapKey& k) : m_pNode(k.m_pNode) {};
	~DOMNodeMapKey() {};
	konga::xml::DOMNodePtr GetDOMNode() const { return m_pNode; }
	bool operator< (const DOMNodeMapKey& rhs) const
	{
        if(m_pNode == rhs.m_pNode)return false;
		return &m_pNode < &rhs.m_pNode;
		                                                                     
	}
private:
	konga::xml::DOMNodePtr	m_pNode;
};



                                                                                                            
                                                                                                           
                                                                                                
                                                                             

class WebServiceResponseSynchronizer
{
public:
	WebServiceResponseSynchronizer(CROM* root,
								   XsdNamespaceSupport* xsdNamespaces,
								   const std::string& stInputFilePath, 
								   const std::string& stOutputFilePath,
								   const std::string& stOutputRootName) : 
										m_root(root),
										m_xsdNamespaces(xsdNamespaces),
										m_stInputFilePath(stInputFilePath),
										m_stOutputFilePath(stOutputFilePath),
										m_stOutputRootName(stOutputRootName),
										m_pInputDoc(0), 
										m_pOutputDoc(0) {};
	~WebServiceResponseSynchronizer(void) {};

	unsigned long	Synchronize();
	std::string	GetLastError() const { return m_stLastError; }

private:
	CROM*					m_root;
	XsdNamespaceSupport*	m_xsdNamespaces;
	std::string				m_stInputFilePath;
	std::string				m_stOutputFilePath;
	std::string				m_stOutputRootName;
	std::string				m_stSchemaDefinitionNamespacePrefix;
	std::string				m_stSchemaInstanceNamespacePrefix;

	konga::xml::DOMDocument2Ptr	m_pInputDoc;
	konga::xml::DOMDocument2Ptr	m_pOutputDoc;

	                                                                                               
	                                                                                                        
	                                                                                                     
	                               
	std::map<DOMNodeMapKey, bool>	m_mapProcessedNodes;
	
	XsdConstants::SchemaVersion	m_schemaVersion;
	std::string	m_stSchemaInstanceNamespace;

	std::string	m_stLastError;

	unsigned long Initialize();
	void CollectNamespaceAndVersionInfo();
	unsigned long LoadDocuments();
	konga::xml::DOMElementPtr GetInputRoot();
	unsigned long CopyDocument(konga::xml::DOMDocument2Ptr pDoc, 
						 konga::xml::DOMNodePtr domParent,
						 CROM* cromParent,
						 konga::xml::DOMNodePtr original,
						 const std::string& stTargetNamespace);
	unsigned long CopyElement(konga::xml::DOMDocument2Ptr pDoc,
							  konga::xml::DOMNodePtr original, 
							  konga::xml::DOMNodePtr& domParent, 
							  CROM*& cromParent, 
							  bool& abort);
	CROM* GetCromNodeForElement(konga::xml::DOMNodePtr domNode,
							    CROM* cromParent, 
							    const std::string& stNodeName,
								const std::string& stNamespace);
	konga::xml::DOMElementPtr FindAnyElement(konga::xml::DOMElementPtr parentXmlNode, CROM* parentCromNode);
	void HandleAnyElement(konga::xml::DOMNodePtr original,
						  konga::xml::DOMElementPtr domParent, 
						  CROM* cromParent,
						  CROM* cromNode,
						  konga::xml::DOMDocument2Ptr pDoc);
	void HandleAnyType(konga::xml::DOMNodePtr pOriginalNode, 
					   konga::xml::DOMElementPtr pCopy,
					   CROM* pCromNode,
					   const std::string& stTypeAttributeName,
					   konga::xml::DOMDocument2Ptr pDoc);
	void HandleTypeAttribute(konga::xml::DOMNodePtr pOriginalNode, 
							 konga::xml::DOMElementPtr pCopy,
							 CROM* pCromNode,
							 const std::string& stTypeAttributeName,
							 const std::string& stTypeAttributeValue);
	bool AlreadyProcessed(konga::xml::DOMNodePtr pNode);
	void OnStructureMisMatch(CROM* pCromNode, 
							 CROM* cromParent,
							 const std::string& stNodeName, 
							 const std::string& stNamespace);
};

#endif                                                     

