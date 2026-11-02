/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef CR_XSDUTILS_H
#define CR_XSDUTILS_H

#include <map>
#include <string>
#include <vector>

#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMNodePtr.h>
#include <konga/Xml/XsdConstants.h>

class TreeMapper;

namespace konga {
	namespace xml {
		class ExpandedName;
	}
}

class XsdUtils {
	TreeMapper*m_pTreeMapper;

public:
	XsdUtils(TreeMapper*p) : m_pTreeMapper(p) {}

	TreeMapper*getTreeMapper(){return m_pTreeMapper;}
	static XsdConstants::XsdNodeType GetXsdNode(const konga::xml::DOMElementPtr& pDOMNode);
	static void GetMinMaxOccurs(konga::xml::DOMElementPtr pNode, int& iMinUse, int& iMaxUse);
	static bool IsTrue(konga::xml::DOMElementPtr pNode, const std::string& stAttributeName);
	static std::string GetArrayItemName(const std::string& stArrayType);
	static konga::xml::DOMNodePtr FindSchemaElement(const konga::xml::DOMNodePtr& pNode);

	                                                                                        
	                                                      
	                        
	int ReplaceSoapArrayTypes(const std::string& stInputXsdFilePath, 
									 const std::string& stOutputXsdFilePath,
									 std::string& stError);
	                        
	int ReplaceSoapArrayTypes(konga::xml::DOMDocument2Ptr pSchemaDoc, std::string& stError);

	                                                                                      
	                                                                                           
	                                                         
	                         
	int SetAnyElementProcessContentsAttribute(const std::string& stInputXsdFilePath,
													 const std::string& stOutputXsdFilePath,
													 XsdConstants::ProcessContentsValue value,
													 std::string& stError);
	                        
	int SetAnyElementProcessContentsAttribute(konga::xml::DOMDocument2Ptr pSchemaDoc,
													 XsdConstants::ProcessContentsValue value,
													 std::string& stError);

	                                                                                                  
	                                                                                                     
	                                                                          
	                                                         
	static konga::xml::DOMElementPtr	CreateSchemaElementNode(konga::xml::DOMDocument2Ptr pDoc,
															XsdConstants::SchemaVersion schemaVersion,
															const std::string& stName,
															const std::string& stSchemaNamespacePrefix);

	static konga::xml::DOMElementPtr CreateSchemaComplexTypeNode(konga::xml::DOMDocument2Ptr pDoc,
																XsdConstants::SchemaVersion schemaVersion,
																const std::string& stTypeName,
																const std::string& stSchemaNamespacePrefix);

	static konga::xml::DOMElementPtr CreateSchemaSequenceNode(konga::xml::DOMDocument2Ptr pDoc,
															 XsdConstants::SchemaVersion schemaVersion,
															 const std::string& stSchemaNamespacePrefix);

	static konga::xml::DOMElementPtr CreateSchemaImportNode(konga::xml::DOMDocument2Ptr pDoc,
														   XsdConstants::SchemaVersion schemaVersion,
														   const std::string& stImportNamespace,
														   const std::string& stSchemaLocation,
														   const std::string& stSchemaNamespacePrefix);

	static konga::xml::DOMElementPtr CreateSchemaIncludeNode(konga::xml::DOMDocument2Ptr pDoc,
														    XsdConstants::SchemaVersion schemaVersion,
															const std::string& stSchemaLocation,
															const std::string& stSchemaNamespacePrefix);

	                                                                                                
	static konga::xml::DOMElementPtr CreateComplexTypeSequenceNode(konga::xml::DOMDocument2Ptr pDoc,
																  XsdConstants::SchemaVersion schemaVersion,
																  const std::string& stSchemaNamespacePrefix);

	static unsigned long InsertImportNode(konga::xml::DOMDocument2Ptr pDoc,
									konga::xml::DOMElementPtr pSchemaNode,
									XsdConstants::SchemaVersion schemaVersion,
									const std::string& stSchemaNamespacePrefix,
									const std::string& stImportedNamespace,
									const std::string& stSchemaLocation,
									std::string& stError);

	static unsigned long InsertIncludeNode(konga::xml::DOMDocument2Ptr pDoc,
									 konga::xml::DOMElementPtr pSchemaNode,
									 XsdConstants::SchemaVersion schemaVersion,
									 const std::string& stSchemaNamespacePrefix,
									 const std::string& stSchemaLocation,
									 std::string& stError);

	                                                                                                  

	
	static std::string GetSchemaDefinitionNamespace(XsdConstants::SchemaVersion version = XsdConstants::SchemaVersion_2001);

	static std::string GetSchemaInstanceNamespace(XsdConstants::SchemaVersion version = XsdConstants::SchemaVersion_2001);

	static XsdConstants::SchemaVersion GetSchemaVersion(const konga::xml::XmlNamespaceDeclarations& namespaceDeclarations);

	                                                                                        
	static inline XsdConstants::SchemaVersion GetDefaultSchemaVersion() { return XsdConstants::SchemaVersion_2001; }

	                                                                                  
	static std::string GetSchemaForSchema(XsdConstants::SchemaVersion = XsdConstants::SchemaVersion_2001);

	static std::string GetExpandedNameAsString(const std::string& stNamespace, const std::string& stLocalName);

	static bool IsAnyType(const std::string& stTypeName, const std::string& stTypeNamespace, XsdConstants::SchemaVersion schemaVersion);

	static konga::xml::DOMElementPtr FindAnyElement(konga::xml::DOMElementPtr pDOMNode, 
												   const std::string& stTargetNamespace,
												   const std::string& stAnyElementNamespace);

	static konga::xml::DOMElementPtr FindAnyElement(konga::xml::DOMElementPtr pDOMNode, 
												   const std::string& stTargetNamespace,
												   XsdConstants::AnyElementNamespace elementNamespace,
												   const std::string& stNamespaceList = "");

	static XsdConstants::AnyElementNamespace GetAnyElementNamespace(const std::string& stNamespaceString);

	                                                                                                    
	                                                                                                  
	                                                                          
	static void SynchronizeNilAndTypeAttributes(konga::xml::DOMElementPtr pRoot, const std::string& stSchemaInstancePrefix);
	static void SynchronizeNulAndTypeAttributes(konga::xml::DOMElementPtr pRoot,
												const std::string& stNilAttributeName,
												const std::string& stTypeAttributeName);
	                                                                                                    

	                                                                                   
	static std::string	GetCRType(const std::string& stUniversalSchemaTypeName);
	static std::string  GetCRType(const konga::xml::ExpandedName& expandedName);

	static bool IsSoapArrayType(const std::string& stUniversalTypeName);

	static int GetCROMCardinalityType(int iMinUse, int iMaxUse);

	                                                                                  
	static unsigned long CopyAttributes(konga::xml::DOMDocument2Ptr pDOMDoc,
								  konga::xml::DOMElementPtr pSource, 
								  konga::xml::DOMElementPtr pTarget,
								  const std::string& stSchemaInstancePrefix,
								  std::string& stError);
	                                                                                           
	                                                 
	static konga::xml::DOMElementPtr CloneElement(konga::xml::DOMDocument2Ptr pDoc,
												 konga::xml::DOMElementPtr pOriginal,
												 const std::string& stNamespaceURI,
												 const std::string& stSchemaInstancePrefix,
												 std::string& stError);
	
	static void GetImportNodes(konga::xml::DOMElementPtr pSchemaNode, 
							   std::vector<konga::xml::DOMElementPtr>& vImportNodes        );

public:
	class SchemaIdentifier 
	{
	public:
		SchemaIdentifier(const std::string& stTargetNamespace, const std::string& stLocation) : m_stTargetNamespace(stTargetNamespace), m_stSchemaLocation(stLocation) {};
		SchemaIdentifier(const SchemaIdentifier& original) : m_stTargetNamespace(original.m_stTargetNamespace), m_stSchemaLocation(original.m_stSchemaLocation) {};
		~SchemaIdentifier() {}

		bool EqualTo(const SchemaIdentifier& rhs) const;
		bool operator== (const SchemaIdentifier& rhs) const;
		bool operator< (const SchemaIdentifier& rhs) const;
		std::string GetTargetNamespace() const { return m_stTargetNamespace; }
		std::string ToString() const { return "TargetNamespace: " + m_stTargetNamespace + std::string(", Location: ") + m_stSchemaLocation; }
	private:
		std::string	m_stTargetNamespace;
		std::string	m_stSchemaLocation;
	};

                                                                                
	class SchemaNodeCreator
	{
	public:

		SchemaNodeCreator(konga::xml::DOMDocumentPtr pSchemaDoc,
						XsdConstants::SchemaVersion schemaVersion,
						const std::string& stSchemaNamespacePrefix) : m_pSchemaDoc(pSchemaDoc),
																m_schemaVersion(schemaVersion),
																m_stSchemaNamespacePrefix(stSchemaNamespacePrefix)
		{}
		~SchemaNodeCreator() {}

		konga::xml::DOMElementPtr CreateSchemaElementNode(const std::string& stName, konga::xml::DOMElementPtr pParent = 0);
		                                                                                                
		konga::xml::DOMElementPtr CreateSchemaComplexTypeNode(const std::string& stTypeName, konga::xml::DOMElementPtr pParent = 0);
		konga::xml::DOMElementPtr CreateSchemaSequenceNode(konga::xml::DOMElementPtr pParent = 0);
		konga::xml::DOMElementPtr CreateSchemaImportNode(const std::string& stImportNamespace, const std::string& stSchemaLocation);
		konga::xml::DOMElementPtr CreateSchemaIncludeNode(const std::string& stSchemaLocation);
		konga::xml::DOMElementPtr CreateComplexTypeSequenceNode(konga::xml::DOMElementPtr pParent = 0);
		                                                                        
		unsigned long InsertImportNode(const std::string& stImportedNamespace, const std::string& stSchemaLocation);
		                                                                        
		unsigned long InsertIncludeNode(const std::string& stSchemaLocation, std::string& stError);

		void SetSchemaNode(konga::xml::DOMElementPtr pSchemaNode) { m_pSchemaNode = pSchemaNode; }

		std::string GetLastError() const { return m_stLastError; }

	private:
		konga::xml::DOMDocument2Ptr	m_pSchemaDoc;
		konga::xml::DOMElementPtr	m_pSchemaNode;
		XsdConstants::SchemaVersion				m_schemaVersion;
		std::string					m_stSchemaNamespacePrefix;

		std::string	m_stLastError;
	};

};


#endif	                

