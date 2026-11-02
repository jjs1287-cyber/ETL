/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                             

#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDPARSER_H_
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDPARSER_H_

#include "resource.h"                      

#include <map>
#include <vector>
#include <string>

#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/XsdConstants.h>

#include "XsdParserConstants.h"

class CROM;
class TreeMapper;
class XsdImportNode;
class XsdParsingResult;

namespace konga {
	namespace xml {
		class ExpandedName;
	}
}

typedef std::map<std::string, std::string> StringStringMap;
typedef std::map<std::string, std::string>::const_iterator StringStringMapConstIterator;

typedef std::map<std::string, CROM*> StringCromMap;
typedef std::map<std::string, CROM*>::iterator StringCromMapIterator;
typedef std::map<std::string, CROM*>::const_iterator StringCromMapConstIterator;

typedef std::map<std::string, konga::xml::DOMNodePtr> StringDOMMap;
typedef std::map<std::string, konga::xml::DOMNodePtr>::const_iterator StringDOMMapConstIterator;

typedef std::map<std::string, int> ProcessedDOMNodeMap;
typedef std::map<std::string, int>::iterator ProcessedDOMNodeMapIterator;

class SchemaCollection;

class XSDParser
{
public:
	friend class SchemaCollection;
	friend class XsdNode;
	friend class XsdAttribute;
	friend class XsdElement;
	friend class XsdAnyNode;
	friend class XsdSequence;
	friend class XsdSimpleType;
	friend class XsdComplexType;
	friend class XsdExtensionNode;
	friend class XsdRestriction;
	friend class XsdGroup;
	friend class XsdAttributeGroup;

	XSDParser(TreeMapper*p);
	~XSDParser();

	bool	IsSchemaValid() const { return m_bSchemaIsValid; }
	std::string	GetSchemaValidationWarning() const;
	void	SetContinueOnValidationError(bool bContinueOnValidationError) { m_bContinueOnValidationError = bContinueOnValidationError; }
	unsigned long LoadSchema(const std::string& stSchemaFile, 
					   SchemaCollection& schemaCollection, 
					   bool bLoadIncludedAndImportedSchemas,
					   bool bValidate = true);
	unsigned long LoadSchema(konga::xml::DOMDocument2Ptr pSchemaDoc,
					   SchemaCollection& schemaCollection,
					   bool bLoadIncludedAndImportedSchemas,
					   const std::string& stCurrentDirectory);
	unsigned int IncreaseAndGetAnonymousGroupNumber() { return ++m_anonymousGroupNumber; }

	TreeMapper*getTreeMapper(){return m_pTreeMapper;}
	void setTreeMapper(TreeMapper* p){m_pTreeMapper=p;}

	unsigned long Parse(const std::string& stRootName);
	const XsdParsingResult& GetParsingResult() const { 
		return *m_parsingResult; 
	}

	konga::xml::DOMDocument2Ptr	GetSchemaDOMDocument() const { 
		return m_pDOMDocument; 
	}
	konga::xml::DOMElementPtr	GetSchemaNode() const { 
		return m_pSchemaNode; 
	}
	CROM* GetRoot() const {
		return m_root;
	}

	void SetRoot(CROM* root) { 
		m_root=root;
	}
	std::string GetTargetNamespace() const {
		return m_stTargetNamespace;
	}
	std::string GetNamespacePrefix(const std::string& stNamespace) const;
	std::string GetNamespacePrefixCreateIfNotExist(const std::string& stNamespaceURI, const std::string& stPrefixBase);
	std::string GetSchemaDefinitionNamespacePrefix() const { 
		return m_stSchemaDefinitionNamespacePrefix; 
	}
	std::string GetNamespaceURI(const std::string& sPrefix) const;
	std::string GetSchemaFileName() const {
		return m_stSchemaFile;
	}
	std::string GetRootName() const {
		return m_stRootName;
	}
	std::string GetLastError() const { 
		return m_stLastError; 
	}

	                                                        
                                                                      
	                                                 
                                                                        
	                                                       
	unsigned long ValidateXML(const std::string& sXmlFilePath);

	                                                                         
	void GetPossibleRoots(std::vector<std::string>& vRootNames);
	                                          
	static unsigned long GetPossibleRoots(const std::string& stXsdFilePath, std::vector<std::string>& vRootNames, TreeMapper*pTreeMapper);

	XsdConstants::SchemaVersion	GetSchemaVersion() const { 
		return m_schemaVersion; 
	}

	                                                                                       
	                                                                   
	unsigned long		GetUsedNamespaces(std::vector<std::string>& vstNamespaces, 
								  bool bIncludeTargetNamespace = false,
								  bool bIncludeSchemaDefinitionNamespace = false);

	                                                                   
	unsigned long		GetImportedNamespaces(std::vector<std::string>& vstNamespaces);

	void			StartLogging(const std::string& stFilename);
	void			StopLogging();
	std::vector<std::string>*getSubstitutionGroup(const std::string&substitustionGroupHead);
	SchemaCollection* GetSchemaCollection() const {
		return m_pSchemaCollection;
	}

private:
	unsigned long ValidateSchema();
	void	GetDefaultFormValues();
	void	FindSchemaNamespaces();
	unsigned long CreateMapsOfGlobals();
	void	ClearMaps(bool keep_root);
	unsigned long ParseGlobalComplexTypes();

	void	AddSchemaValidationWarning(const std::string& stWarningMsg);
	std::vector<std::string>& GetSchemaValidationWarnings() { return m_vstValidationWarnings; }

	unsigned long LoadIncludedSchemas(bool bValidate, const std::string& stCurrentDirectory);
	unsigned long LoadImportedSchemas(bool bValidate, const std::string& stCurrentDirectory);

	unsigned long BuildCROMTree(const konga::xml::DOMNodePtr& pDOMNode, 
						  CROM*parent, 
						  CROM*prevSibling, 
						  XsdParserConstants::NodeType lastNodeType);

	CROM*		GetElementDefinition(const std::string& stNamespace, 
									 const std::string& stLocalName,
									 bool checkCollection = true);

	CROM*		GetAttributeDefinition(const std::string& stNamespace, const std::string& stLocalName, bool askCollection = true);

	CROM*		GetSimpleTypeDefinition(const std::string& stNamespace,
										const std::string& stLocalName,
										bool checkCollection = true);

	unsigned long InsertComplexTypeDefinition(CROM* parent, 
											  const std::string& stNamespace, 
											  const std::string& stLocalName,
											  XsdParserConstants::ComplexTypeConsumer consumer);
	CROM*		GetComplexTypeDefinition(const std::string& stNamespace, 
										 const std::string& stLocalName,
										 bool checkCollection = true);

	CROM*		GetGroupDefinition(const std::string& stNamespace,
								   const std::string& stLocalName,
								   bool checkCollection = true);
	CROM*		GetAttributeGroupDefinition(const std::string& stNamespace,
											const std::string& stLocalName,
											bool checkCollection = true);

	konga::xml::ExpandedName GetExpandedName(const std::string& stName, bool bIsReference = true);
	void GetNamespaceAndLocalName(const konga::xml::DOMElementPtr& pElement,
								  const std::string& stQualifiedName, 
								  std::string& stNamespaceURI, 
								  std::string& stLocalName);

	std::string GetCRDataType(const konga::xml::ExpandedName& expName);
	void GetDOMNodePath(konga::xml::DOMElementPtr pDOMNode, std::string& stPath);


	bool IsSchemaType(const std::string& sType);
	bool IsComplexType(const std::string& stNamespace, const std::string& stLocalName);

	bool IsAnyType(const konga::xml::ExpandedName& expandedTypeName);

	static XsdParserConstants::FormAttributeValue GetFormAttributeValue(konga::xml::DOMElementPtr pNode);

	bool ShouldLocalEntityBeQualified(const konga::xml::DOMElementPtr& pNode, konga::xml::DOMNodeType nodeType);
	
	konga::xml::DOMElementPtr LookupElementDefinitionDOMNodeInMap(const std::string& stExpandedName);
	konga::xml::DOMElementPtr LookupElementDefinitionDOMNodeInMap(const konga::xml::ExpandedName& expandedName);

	CROM* LookupAttributeDefinitionCROMNodeInMap(const std::string& stExpandedName);
	konga::xml::DOMElementPtr LookupAttributeDefinitionDOMNodeInMap(const std::string& stExpandedName);

	konga::xml::DOMElementPtr LookupComplexTypeDOMNodeInMap(const std::string& stExpandedName);

	                                                         
	unsigned long LoadSchemaFromFile(const std::string& stSchemaFile,
							   SchemaCollection& schemaCollection, 
							   bool bLoadIncludedAndImportedSchemas,
							   bool bValidate,
							   bool bGiveOwnerShipToCollection);

	                                                                             
	unsigned long LoadSchemaInternal(SchemaCollection& schemaCollection, 
							   bool bLoadIncludedAndImportedSchemas,
							   bool bValidate,
							   bool bGiveOwnerShipToCollection,
							   const std::string& stCurrentDirectory = "");

	int		AddToProcessedDOMNodeMap(const std::string& stDOMPath);
	void	RemoveFromProcessedDOMNodeMap(const std::string& stDOMPath);

	void	AddSimpleTypeMapping(const std::string& stUniversalTypeName, const std::string& stCRType)
	{
		m_mapSimpleTypeToCrType[stUniversalTypeName] = stCRType;
	}
	std::string	GetCRTypeFromSimpleType(const std::string& stUniversalTypeName)
	{
		StringStringMapConstIterator cit = m_mapSimpleTypeToCrType.find(stUniversalTypeName);
		return ( cit == m_mapSimpleTypeToCrType.end() ) ? "" : cit->second;
	}
	
	void Log(const std::string& stMessage);
	void Log(const std::string& stFormat, const std::string& stMessage);

	unsigned long ParseGlobalElement(const std::string& stExpandedName, const konga::xml::DOMElementPtr& pDOMNode);

	void ScanForNamespaceUse(const konga::xml::DOMElementPtr& pElement, 
							 std::map<std::string, bool>& mapEncounteredNamespaces,
							 bool bIncludeTargetNamespace,
							 bool bIncludeSchemaDefinitionNamespace);

	void FindImportNodes();
	void LocateRoot(const std::string& stGivenRootName, std::string& stLocatedRootName, konga::xml::DOMElementPtr& domNode);

private:
	TreeMapper*m_pTreeMapper;

	std::string		m_stSchemaFile;
	std::string		m_stRootName;
	std::string		m_stTargetNamespace;
	std::string		m_stLastError;

	unsigned int	m_anonymousGroupNumber;

	bool	m_bSchemaIsValid;
	bool	m_bContinueOnValidationError;
	                                          
	std::map<std::string, bool>	m_mapSchemaValidationWarnings;
	std::vector<std::string>	m_vstValidationWarnings;

	FILE*			m_pLogFile;

	SchemaCollection*			m_pSchemaCollection;

	XsdConstants::SchemaVersion				m_schemaVersion;
	konga::xml::XmlNamespaceDeclarations	m_namespaceDeclarations;
	std::string					m_stSchemaDefinitionNamespace;
	std::string					m_stSchemaDefinitionNamespacePrefix;
	std::string					m_stSchemaInstanceNamespace;

	std::string		m_stSchemaDefinitionXPathPrefix;
		
	konga::xml::DOMDocument2Ptr			m_pDOMDocument;
	konga::xml::DOMElementPtr			m_pSchemaNode;

	XsdParsingResult*	m_parsingResult;
	CROM*				m_root;

	StringDOMMap	m_globalElementsDOM, 
					m_globalAttribsDOM, 
					m_complexTypeDefDOM, 
					m_groupDefDOM, 
					m_attribGroupDefDOM;
	StringCromMap	m_globalElementsCROM, 
					m_globalAttribsCROM, 
					m_simpleTypeDefCROM,
					m_complexTypeDefCROM, 
					m_groupDefCROM, 
					m_attribGroupDefCROM;

	bool		m_bQualifyLocalElements;
	bool		m_bQualifyLocalAttributes;

	StringStringMap		m_mapSimpleTypeToCrType;

	ProcessedDOMNodeMap			m_mapProcessedDOMNodes;

	std::vector<XsdImportNode*>	m_vImportedNodes;

};

bool SearchForValueContentNode(CROM* node);

#endif
