/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/ExpandedName.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "XSDParser.h"
#include "crom.h"
#include "SchemaCollection.h"
#include "XsdParsingResult.h"
#include "XsdUtils.h"
#include "XsdSimpleType.h"
#include "XsdComplexType.h"
#include "XsdGroup.h"
#include "XsdAttributeGroup.h"
#include "XsdImportNode.h"
#include "XsdParserException.h"
#include "XsdNodeFactory.h"

using namespace std;
                               
using namespace konga;
using namespace konga::xml;

                                                          
                             
                                                          
XSDParser::XSDParser(TreeMapper*p) :
	m_pTreeMapper(p),
	m_anonymousGroupNumber(0),
	m_bSchemaIsValid(true), 
	m_bContinueOnValidationError(false),
	m_pLogFile(0),
	m_pSchemaCollection(0), 
	m_schemaVersion(XsdConstants::SchemaVersion_Unknown),
	m_root(0),
	m_bQualifyLocalElements(false), 
	m_bQualifyLocalAttributes(false)
{
	m_parsingResult = new XsdParsingResult(*this);
}

XSDParser::~XSDParser() {
	if (m_root != 0) {
		m_root->DeleteNode();
	}
	delete m_parsingResult;
	m_parsingResult = 0;
	ClearMaps(false);
	for (vector<XsdImportNode*>::iterator it = m_vImportedNodes.begin(); it != m_vImportedNodes.end(); ++it) {
		delete (*it);
	}
	m_vImportedNodes.clear();
	if (m_pLogFile) {
		fclose(m_pLogFile);
		m_pLogFile = 0;
	}
}

void XSDParser::LocateRoot(const string& stGivenRootName, string& stLocatedRootName, DOMElementPtr& domNode) {
	if (stGivenRootName.empty()) {
		                                                                               
		                                                                         
		if (m_globalElementsDOM.empty()) {
			m_stLastError += "The schema does not contain any avilable root elements.";
			throw CrException(m_stLastError);
		}
		else if (m_globalElementsDOM.size() > 1) {
			                                                                                                    
			                    
			string stPossibleRoots;
			for (StringDOMMapConstIterator cit = m_globalElementsDOM.begin(); cit != m_globalElementsDOM.end(); ++cit) {
				if (!stPossibleRoots.empty()) {
					stPossibleRoots += ", ";
				}
				stPossibleRoots += konga::XmlUtils::GetAttributeValue(cit->second, XsdConstants::NAME_ATTRIBUTE);
			}
			getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_AMBIGUOUS_ROOT, m_stSchemaFile.c_str(), stPossibleRoots.c_str());
			m_stLastError += string((const char*) getTreeMapper()->getErrorLog()->GetLastErr());
			throw CrException(m_stLastError);
		}
		StringDOMMapConstIterator cit = m_globalElementsDOM.begin();
		stLocatedRootName = cit->first;
		domNode = cit->second;
	}
	else {
		                                                                          
		                                                                      
		                                                                         
		                                                                      
		string::size_type pos = stGivenRootName.find('{');
		if (pos != string::npos) {
			ExpandedName expName(ExpandedName::fromString(stGivenRootName));
			stLocatedRootName = expName.toString();
			domNode = m_pSchemaCollection->GetElementDefinitionDOMNode(expName);
		}
		else {
			pos = stGivenRootName.find_last_of(':');
			if (pos != string::npos) {
				ExpandedName expName(stGivenRootName.substr(0, pos), stGivenRootName.substr(pos + 1));
				stLocatedRootName = expName.toString();
				domNode = m_pSchemaCollection->GetElementDefinitionDOMNode(expName);
			}
			else {
				ExpandedName expName(m_stTargetNamespace, stGivenRootName);
				stLocatedRootName = expName.toString();
				domNode = m_pSchemaCollection->GetElementDefinitionDOMNode(expName);
			}
		}
		if (domNode == 0) {
			getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_NO_ROOT_FOUND, m_stSchemaFile.c_str(), stLocatedRootName.c_str());
			m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
			throw CrException(m_stLastError);
		}
	}
}

unsigned long XSDParser::Parse(const string& stRootName) {
	m_parsingResult->Clear();
	unsigned long hr = S_OK;
	try {
		DOMElementPtr pDOMNode = 0;
		string stUniversalRootName;
		LocateRoot(stRootName, stUniversalRootName, pDOMNode);
		if (!m_pSchemaCollection->ParseGlobalComplexTypes()) {
			                                                               
			                                                       
			m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
			ClearMaps(false);
			return E_FAIL;
		}
		hr = ParseGlobalElement(stUniversalRootName, pDOMNode);
		                                                                        
		                      
		CROM* rootHolder = m_globalElementsCROM.find(stUniversalRootName)->second;
		m_root = rootHolder->GetFirstChild();
		m_root->SetParent(0);
		rootHolder->SetFirstChild(0);
		m_parsingResult->Build();
		rootHolder->checkForChildrenWithSameName();

#ifdef _DEBUG
		m_parsingResult->WriteXml("/tmp/xsdparsingresult-debug.xml");
#endif
		return hr;
	}
	catch (CrException& e) {
		string stError(e.GetErrorString());
		if (stError.empty()) {
			stError = "Unkown error";
		}
		ClearMaps(false);
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_FATAL_ERROR, konga::GetLeafPath(m_stSchemaFile).c_str(), stError.c_str());
		m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
		return hr;
	}
	catch (...) {
		                                                                                        
		                                                                                    
		             
		string stValidationWarning = GetSchemaValidationWarning();
		string stError = stValidationWarning.empty() ? "Unknown error." : stValidationWarning;
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_FATAL_ERROR, konga::GetLeafPath(m_stSchemaFile).c_str(), stError.c_str());
		m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
		ClearMaps(false);
		return hr;
	}
}

unsigned long XSDParser::ParseGlobalElement(const string& stExpandedName, const DOMElementPtr& pDOMNode) {
	unsigned long hr = S_OK;
	                                                      
	StringCromMapConstIterator crom_cit = m_globalElementsCROM.find(stExpandedName);
	if (crom_cit == m_globalElementsCROM.end()) {
		int iType = CROM_t_Element | CROM_t_CouldBeRoot;
		const string name(konga::XmlUtils::GetAttributeValue(pDOMNode, XsdConstants::NAME_ATTRIBUTE));
		CROM* root = new CROM(getTreeMapper(),0, 0, name.c_str(), "", iType, CROM::t_CROM_Xml);
		root->SetNamespace(m_stTargetNamespace);
		m_globalElementsCROM[stExpandedName] = root;
		hr = BuildCROMTree(pDOMNode, root, 0, XsdParserConstants::t_root);
		if (FAILED(hr)) {
			m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
			ClearMaps(false);
		}
	}
	return hr;
}

unsigned long XSDParser::ValidateSchema() {
	unsigned long hr = S_OK;
	try {
		string stError;
		if (!konga::XmlUtils::ValidateXmlSchema(m_stSchemaFile, stError)) {
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_INVALID_SCHEMA, 
									  m_stSchemaFile.c_str(), 
									  stError.c_str());
			m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
		}
	}
	catch(CrException &e) {
		string stErr = e.GetErrorString();
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_VALIDATION_FAILED, m_stSchemaFile.c_str(), stErr.c_str());
		m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
	}
	return hr;
}

void XSDParser::ClearMaps(bool keep_root) {
	StringCromMapIterator CROM_it;
	if (!m_globalElementsCROM.empty()) {
		for (CROM_it=m_globalElementsCROM.begin(); CROM_it!=m_globalElementsCROM.end(); ++CROM_it) {
			if( !(CROM_it->second->CouldBeRoot() && keep_root)) {
				CROM_it->second->DeleteNode();
			}
		}
		if (!keep_root) {
			m_globalElementsCROM.clear();
		}
	}
	if (!m_simpleTypeDefCROM.empty()) {
		for (CROM_it=m_simpleTypeDefCROM.begin(); CROM_it!=m_simpleTypeDefCROM.end(); ++CROM_it) {
			CROM_it->second->DeleteNode();
		}
		m_simpleTypeDefCROM.clear();
	}
	if (!m_complexTypeDefCROM.empty()) {
		for (CROM_it=m_complexTypeDefCROM.begin(); CROM_it!=m_complexTypeDefCROM.end(); ++CROM_it) {
			CROM_it->second->DeleteNode();
		}
		m_complexTypeDefCROM.clear();
	}
	if (!m_groupDefCROM.empty()) {
		for (CROM_it=m_groupDefCROM.begin(); CROM_it!=m_groupDefCROM.end(); ++CROM_it) {
			CROM_it->second->DeleteNode();
		}
		m_groupDefCROM.clear();
	}
	if (!m_globalAttribsCROM.empty()) {
		for (CROM_it=m_globalAttribsCROM.begin(); CROM_it!=m_globalAttribsCROM.end(); ++CROM_it) {
			CROM_it->second->DeleteNode();
		}
		m_globalAttribsCROM.clear();
	}
	if (!m_attribGroupDefCROM.empty()) {
		for (CROM_it=m_attribGroupDefCROM.begin(); CROM_it!=m_attribGroupDefCROM.end(); ++CROM_it) {
			CROM_it->second->DeleteNode();
		}
		m_attribGroupDefCROM.clear();
	}
}


void XSDParser::GetDefaultFormValues() {
	const string& stElementForm(konga::XmlUtils::GetAttributeValue(m_pSchemaNode, XsdConstants::ELEMENT_FORM_DEFAULT));
	m_bQualifyLocalElements = konga::EqualsIgnoreCase(stElementForm, XsdConstants::FORM_QUALIFIED);

	const string& stAttributeForm(konga::XmlUtils::GetAttributeValue(m_pSchemaNode, XsdConstants::ATTRIBUTE_FORM_DEFAULT));
	m_bQualifyLocalAttributes = konga::EqualsIgnoreCase(stAttributeForm, XsdConstants::FORM_QUALIFIED);
}


string XSDParser::GetNamespacePrefix(const string& stNamespace) const {
	return m_namespaceDeclarations.GetNamespacePrefix(stNamespace);
}

string XSDParser::GetNamespacePrefixCreateIfNotExist(const string& stNamespaceURI, const string& stPrefixBase) {
	return m_namespaceDeclarations.GetNamespacePrefixCreateIfNotExist(stNamespaceURI, stPrefixBase);
}

unsigned long XSDParser::CreateMapsOfGlobals() {
	unsigned long hr = S_OK;

	DOMNodePtr pDOMNode( m_pSchemaNode->getFirstChild() );
	while ((pDOMNode != 0) && (pDOMNode->getNodeType() != konga::xml::NODE_ELEMENT)) {
		pDOMNode = pDOMNode->getNextSibling();
	}
	while (pDOMNode!=0) {
		DOMElementPtr pNode = pDOMNode;
		string stBaseName;

		const string& stName = konga::XmlUtils::GetAttributeValue(pNode, XsdConstants::NAME_ATTRIBUTE);
		if (!stName.empty()) {
    		const string& stExpandedName = XsdUtils::GetExpandedNameAsString(m_stTargetNamespace, stName);
    		XsdConstants::XsdNodeType currentXsdNode = XsdUtils::GetXsdNode(pNode);
    		switch (currentXsdNode) {
    		case XsdConstants::ElementNode:
				{
    				m_globalElementsDOM[stExpandedName] = pNode;
					const string& stSubstitutionGroup = konga::XmlUtils::GetAttributeValue(pNode, XsdConstants::SUBSTITUTIONGROUP_ATTRIBUTE);
					if (!stSubstitutionGroup.empty()) {
						string stPrefix, stLocalName, stNamespaceURI;
						konga::XmlUtils::GetPrefixAndLocalName(stSubstitutionGroup,stPrefix,stLocalName);
						stNamespaceURI=GetNamespaceURI(stPrefix);
						string stSubstitutionGroupHeader = XsdUtils::GetExpandedNameAsString(stNamespaceURI, stLocalName);
						m_pSchemaCollection->addSubstitutionGroup(stSubstitutionGroupHeader,stExpandedName);
					}
				}
    			break;
    		case XsdConstants::AttributeNode:
    			m_globalAttribsDOM[stExpandedName] = pNode;
    			break;
    		case XsdConstants::GroupNode:
    			m_groupDefDOM[stExpandedName] = pNode;
    			break;
    		case XsdConstants::AttributeGroupNode:
    			m_attribGroupDefDOM[stExpandedName]=pNode;
    			break;
    		case XsdConstants::ComplexTypeNode:
   				m_complexTypeDefDOM[stExpandedName] = pNode;
    			break;
    		case XsdConstants::SimpleTypeNode:
    			{
    				CROM* typeRoot = new CROM(getTreeMapper(),0, 0, "simpleTypeRoot", "", 0, CROM::t_CROM_Xml);
    				XsdSimpleType simpleType(*this, pNode, typeRoot, 0, XsdParserConstants::t_root);				
    				hr = simpleType.Parse(m_stLastError);
					if (FAILED(hr)) {
    					return hr;
					}
    				m_simpleTypeDefCROM[stExpandedName] = typeRoot;
    			}
    			break;
			default:
				break;                           
    		}
        }

		pDOMNode = pDOMNode->getNextSibling();
		while ((pDOMNode != 0) && (pDOMNode->getNodeType() != konga::xml::NODE_ELEMENT)) {
			pDOMNode = pDOMNode->getNextSibling();
		}
	}

	return hr;
}

unsigned long XSDParser::ParseGlobalComplexTypes() {
	for (StringDOMMapConstIterator it = m_complexTypeDefDOM.begin(); it != m_complexTypeDefDOM.end(); ++it) {
		if (m_complexTypeDefCROM.find(it->first) != m_complexTypeDefCROM.end()) {
			continue;
		}
		DOMElementPtr pNode = it->second;
		XsdComplexType complexType(*this, pNode, 0, 0, XsdParserConstants::t_root);
		unsigned long hr = complexType.Parse(m_stLastError);
		if (SUCCEEDED(hr)) {
			CROM* typeRoot = complexType.GetTreeRoot();
			typeRoot->m_Type |= CROM_t_TypeDefinition;
			                                                                              
			                                                                                  
			                                                                                     
			                                                                                       
			                                                                                         
			                   
			                                                                                           
			                                                                                            
			                                               
			                                                                                             
			                                                                                                
			                                                                                             
			                  
			if (m_complexTypeDefCROM.find(it->first) == m_complexTypeDefCROM.end()) {
				m_complexTypeDefCROM[it->first] = typeRoot;
			} else {
				typeRoot->DeleteNode();
			}
		}
		else {
			return hr;
		}
	}
	return S_OK;
}

DOMElementPtr XSDParser::LookupElementDefinitionDOMNodeInMap(const string& stExpandedName) {
	StringDOMMapConstIterator cit = m_globalElementsDOM.find(stExpandedName);
	return (cit == m_globalElementsDOM.end()) ? 0 : cit->second;

}

DOMElementPtr XSDParser::LookupElementDefinitionDOMNodeInMap(const ExpandedName& expandedName) {
	return LookupElementDefinitionDOMNodeInMap(expandedName.toString());
}

namespace {
	bool StoreTypeDefInConsumer(XsdParserConstants::ComplexTypeConsumer consumer) {
		return (consumer == XsdParserConstants::Element) || 
				(consumer == XsdParserConstants::LocalRestrictedType) || 
					(consumer == XsdParserConstants::GlobalRestrictedType);
	}

	bool RegisterConsumerAsExtension(XsdParserConstants::ComplexTypeConsumer consumer) {
		return (consumer == XsdParserConstants::GlobalExtendedType) ||
				(consumer == XsdParserConstants::GlobalRestrictedType);
	}
}

unsigned long XSDParser::InsertComplexTypeDefinition(CROM* parent, 
													 const string& stNamespace, 
													 const string& stLocalName,
													 XsdParserConstants::ComplexTypeConsumer consumer) {
	CROM* pTypeRoot = GetComplexTypeDefinition(stNamespace, stLocalName);
	if (pTypeRoot != 0) {
		parent->SetAllGroup(pTypeRoot->IsAllGroup());
		if (StoreTypeDefInConsumer(consumer)) {
			parent->SetComplexTypeDefinition(pTypeRoot);
		}
		else {
			CROM::CloneCROMChildren(pTypeRoot, parent);
		}
		if (RegisterConsumerAsExtension(consumer)) {
			const string stLocalExtensionName((const char*) parent->m_Name);
			                                                                
			                                                                 
			                                 
			ExpandedName expandedName(m_stTargetNamespace, stLocalExtensionName);
			pTypeRoot->AddExtension(expandedName.toString());
		}
		return S_OK;	
	}
	else {
		        
		return E_FAIL;
	}
}

void XSDParser::GetNamespaceAndLocalName(const konga::xml::DOMElementPtr& pElement,
										 const string& stQualifiedName, 
										 string& stNamespaceURI, 
										 string& stLocalName) {
    string stPrefix;
	konga::XmlUtils::GetPrefixAndLocalName(stQualifiedName, stPrefix, stLocalName);
	stNamespaceURI = konga::XmlUtils::GetNamespaceURI(pElement, stPrefix);
}

ExpandedName XSDParser::GetExpandedName(const string& stName, bool bIsReference          ) {
	string stPrefix;
	size_t n = stName.find(':');
	if (n == string::npos) {
		if (bIsReference) {
			stPrefix = "";
		}
		else {
			return ExpandedName("", stName);
		}
	}
	else {
		stPrefix = stName.substr(0, n);
	}
	string stBase(stName.substr(n+1));
	string stURI = m_namespaceDeclarations.GetNamespaceURI(stPrefix);
	return ExpandedName(stURI, stBase);
}


unsigned long XSDParser::BuildCROMTree(const DOMNodePtr& pDOMNode, 
									   CROM*parent, 
									   CROM*prevSibling, 
									   XsdParserConstants::NodeType lastNodeType) {
	if (pDOMNode->getNodeType() != konga::xml::NODE_ELEMENT) {
		return S_OK;
	}
	DOMElementPtr pNode(pDOMNode);
	XsdConstants::XsdNodeType currentXsdNode = XsdUtils::GetXsdNode(pNode);
	if (currentXsdNode == XsdConstants::AnnotationNode) {
		return S_OK;
	}

	unsigned long hr = S_OK;
	CROM* obj = 0;
	XsdNode* pXsdNode = XsdNodeFactory::CreateXsdNode(*this, pNode, parent, prevSibling, lastNodeType);
	if (pXsdNode != 0) {
		konga::AutoPtrDelete<XsdNode> adopt_node(pXsdNode);
		hr = pXsdNode->Parse(m_stLastError);
		if (FAILED(hr) || !pXsdNode->ContinueRecursiveTraversal()) {
			return hr;
		}
		lastNodeType = pXsdNode->GetLastNodeType();
		obj = pXsdNode->GetTreeRoot();
	}

	if (lastNodeType == XsdParserConstants::t_value) {
		prevSibling = obj;
	}
	else if (obj != 0) {
		parent = obj;
		prevSibling = 0;
	}
	DOMNodePtr pChild(pDOMNode->getFirstChild());
	while (pChild != 0) {
		hr = BuildCROMTree(pChild, parent, prevSibling, lastNodeType);
		if (FAILED(hr)) {
			return hr;
		}
		pChild = pChild->getNextSibling();
		if (parent != 0) {
			prevSibling=parent->GetLastChild();
		}
	}
	return S_OK;
}

string XSDParser::GetCRDataType(const ExpandedName& expName) {
	                                             
	string stCRType(XsdUtils::GetCRType(expName));
	if (stCRType.empty()) {
		                                                                           
		StringStringMapConstIterator cit = m_mapSimpleTypeToCrType.find(expName.toString());
		if (cit != m_mapSimpleTypeToCrType.end()) {
			stCRType = cit->second;
		}
		else {
			                                                              
			stCRType = "String";
		}
	}
	return stCRType;
}


void XSDParser::GetDOMNodePath(DOMElementPtr pDOMNode, string& stPath) {
	stPath.clear();
	while (pDOMNode != 0) {
		const string stNodeName(konga::XmlUtils::GetNodeBaseName(pDOMNode));
		string stTagName(konga::XmlUtils::GetAttributeValue(pDOMNode, XsdConstants::NAME_ATTRIBUTE));
		if (stTagName.empty()) {
			stTagName = konga::XmlUtils::GetAttributeValue(pDOMNode, XsdConstants::REF_ATTRIBUTE);
		}
		string stNamePart;
		if (!stTagName.empty()) {
			ExpandedName en(GetExpandedName(stTagName));
			stNamePart = en.toString();
		}
		if (!stPath.empty()) {
			stPath.insert(stPath.begin(), '|');
		}
		stPath.insert(0, stNodeName + ":" + stNamePart);
		pDOMNode = pDOMNode->getParentNode();
	}
}

CROM* XSDParser::LookupAttributeDefinitionCROMNodeInMap(const string& stExpandedName) {
	StringCromMapConstIterator cit = m_globalAttribsCROM.find(stExpandedName);
	return ( cit == m_globalAttribsCROM.end() ) ? 0 : cit->second;
}

DOMElementPtr XSDParser::LookupAttributeDefinitionDOMNodeInMap(const string& stExpandedName) {
	StringDOMMapConstIterator cit = m_globalAttribsDOM.find(stExpandedName);
	return ( cit == m_globalAttribsDOM.end() ) ? 0 : cit->second;
}

CROM* XSDParser::GetAttributeDefinition(const string& stNamespace, 
										const string& stLocalName,
										bool askCollection          ) {
	string stExpandedName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);

	                                                           
	CROM* pCROMNode = LookupAttributeDefinitionCROMNodeInMap(stExpandedName);
	DOMElementPtr pDOMNode = 0;
	if (pCROMNode == 0) {
		                                                                               
		pDOMNode = LookupAttributeDefinitionDOMNodeInMap(stExpandedName);
		if (pDOMNode == 0 && askCollection) {
			                                                                    
			pCROMNode = m_pSchemaCollection->GetGlobalAttributeCROM(stNamespace, stLocalName, this);
		}
	}

	if (pCROMNode != 0) {
		return pCROMNode;
	}
	else if (pDOMNode != 0) {
		CROM* root = new CROM(getTreeMapper(), 0, 0, "root", "", CROM_t_Root, CROM::t_CROM_Xml);
		unsigned long hr = BuildCROMTree(pDOMNode, root, 0, XsdParserConstants::t_attribute);
		if (FAILED(hr)) {
			delete root;
			return 0;
		}
		CROM* attribute = root->GetFirstChild();
		attribute->SetNamespace(m_stTargetNamespace);
		attribute->SetParent(0);
		root->SetFirstChild(0);
		root->DeleteNode();
		m_globalAttribsCROM[stExpandedName] = attribute;
		return attribute;
	}
	return 0;
}

CROM* XSDParser::GetElementDefinition(const string& stNamespace,
									  const string& stLocalName,
									  bool checkCollection           ) {
	                                                                
	string stExpandedName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	StringCromMapConstIterator cit = m_globalElementsCROM.find(stExpandedName);
	if (cit != m_globalElementsCROM.end()) {
		return cit->second->GetFirstChild();
	}
	else {
		                                                         
		StringDOMMapConstIterator dom_cit = m_globalElementsDOM.find(stExpandedName);
		if (dom_cit != m_globalElementsDOM.end()) {
			if (SUCCEEDED( ParseGlobalElement(stExpandedName, dom_cit->second) )) {
				return m_globalElementsCROM.find(stExpandedName)->second->GetFirstChild();
			}
			else {
				return 0;
			}
		}
		else {
			if (checkCollection) {
				                                                                                
				return m_pSchemaCollection->GetElementDefinition(stNamespace, stLocalName, this);
			}
		}
	}
	return 0;
}

CROM* XSDParser::GetGroupDefinition(const string& stNamespace, const string& stLocalName, bool checkCollection           ) {
	                                                              
	string stExpandedName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	StringCromMapConstIterator cit = m_groupDefCROM.find(stExpandedName);
	if (cit != m_groupDefCROM.end()) {
		return cit->second;
	}
	else {
		                                                      
		StringDOMMapConstIterator domCit = m_groupDefDOM.find(stExpandedName);
		if (domCit != m_groupDefDOM.end()) {
			XsdGroup group(*this, domCit->second, 0, 0, XsdParserConstants::t_root);
			if (SUCCEEDED( group.Parse(m_stLastError) )) {
				CROM* groupRoot = group.GetTreeRoot();
				m_groupDefCROM[stExpandedName] = groupRoot;
				return groupRoot;
			}
		}
		else {
			                                                                                    
			if (checkCollection) {
				return m_pSchemaCollection->GetGroupDefinition(stNamespace, stLocalName, this);
			}
		}
	}
	return 0;
}

CROM* XSDParser::GetAttributeGroupDefinition(const string& stNamespace, const string& stLocalName, bool checkCollection           ) {
	                                                              
	string stExpandedName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	StringCromMapConstIterator cit = m_attribGroupDefCROM.find(stExpandedName);
	if (cit != m_attribGroupDefCROM.end()) {
		return cit->second;
	}
	else {
		                                                      
		StringDOMMapConstIterator domCit = m_attribGroupDefDOM.find(stExpandedName);
		if (domCit != m_attribGroupDefDOM.end()) {
			XsdAttributeGroup group(*this, domCit->second, 0, 0, XsdParserConstants::t_root);
			if (SUCCEEDED( group.Parse(m_stLastError) )) {
				CROM* groupRoot = group.GetTreeRoot();
				m_attribGroupDefCROM[stExpandedName] = groupRoot;
				return groupRoot;
			}
		}
		else {
			                                                                                    
			if (checkCollection) {
				return m_pSchemaCollection->GetAttributeGroupDefinition(stNamespace, stLocalName, this);
			}
		}
	}
	return 0;
}

CROM* XSDParser::GetSimpleTypeDefinition(const string& stNamespace, const string& stLocalName, bool checkCollection           ) {
	string stExpandedName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	StringCromMapConstIterator cit = m_simpleTypeDefCROM.find(stExpandedName);
	if (cit == m_simpleTypeDefCROM.end()) {
		                                                                                 
		if (checkCollection) {
			return m_pSchemaCollection->GetSimpleTypeDefinition(stNamespace, stLocalName, this);
		}
	}
	else {
		CROM* valueNode = new CROM(cit->second->GetFirstChild());
		valueNode->SetParent(0);
		return valueNode;
	}
	return 0;
}


CROM* XSDParser::GetComplexTypeDefinition(const string& stNamespace, const string& stLocalName, bool checkCollection           ) {
	string stExpandedName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	                                                      
	StringDOMMapConstIterator DOM_cit = m_complexTypeDefDOM.find(stExpandedName);
	if (DOM_cit == m_complexTypeDefDOM.end()) {
		                                                                                 
		if (checkCollection) {
			return m_pSchemaCollection->GetComplexTypeDefinition(stNamespace, stLocalName, this);
		}
	}
	else {
		StringCromMapConstIterator crom_cit = m_complexTypeDefCROM.find(stExpandedName);
		if (crom_cit != m_complexTypeDefCROM.end()) {
			return crom_cit->second;
		}
		else {
			XsdComplexType complexType(*this, DOM_cit->second, 0, 0, XsdParserConstants::t_root);
			if (SUCCEEDED(complexType.Parse(m_stLastError))) {
				CROM* typeRoot = complexType.GetTreeRoot();
				                                                                                       
				                                                                                                    
				if (m_complexTypeDefCROM.find(stExpandedName) != m_complexTypeDefCROM.end()) {
					typeRoot->DeleteNode();
					return m_complexTypeDefCROM[stExpandedName];
				} else {
					typeRoot->m_Type |= CROM_t_TypeDefinition;
					m_complexTypeDefCROM[stExpandedName] = typeRoot;
					return typeRoot;
				}
			}
		}
	}
	return 0;
}

bool XSDParser::IsAnyType(const ExpandedName& expandedTypeName) {
	ExpandedName anyType(m_stSchemaDefinitionNamespace, XsdConstants::ANY_TYPE);
	return expandedTypeName == anyType;
}

XsdParserConstants::FormAttributeValue XSDParser::GetFormAttributeValue(DOMElementPtr pNode) {
	string stForm = konga::XmlUtils::GetAttributeValue(pNode, XsdConstants::FORM);
	if (stForm.length() > 0) {
		if (konga::EqualsIgnoreCase(stForm, XsdConstants::FORM_QUALIFIED)) {
			return XsdParserConstants::FORM_ATTRIBUTE_QUALIFIED;
		}
		else if (konga::EqualsIgnoreCase(stForm, XsdConstants::FORM_UNQUALIFIED)) {
			return XsdParserConstants::FORM_ATTRIBUTE_UNQUALIFIED;
		}
	}
	return XsdParserConstants::FORM_ATTRIBUTE_NOT_SET;
}

bool XSDParser::ShouldLocalEntityBeQualified(const DOMElementPtr& pNode, konga::xml::DOMNodeType nodeType) {
	XsdParserConstants::FormAttributeValue localValue = GetFormAttributeValue(pNode);
	switch (localValue) {
	case XsdParserConstants::FORM_ATTRIBUTE_QUALIFIED:
		return true;
	case XsdParserConstants::FORM_ATTRIBUTE_UNQUALIFIED:
		return false;
	default:
		if (nodeType == konga::xml::NODE_ELEMENT) {
			return m_bQualifyLocalElements;
		}
		else if (nodeType == konga::xml::NODE_ATTRIBUTE) {
			return m_bQualifyLocalAttributes;
		}
	}
	return false;
}


unsigned long XSDParser::ValidateXML(const string& stXmlFilePath) {
	unsigned long hr = S_OK;
	try {
		string stError;
		if (!konga::XmlUtils::ValidateXmlInstanceAgainstSchema(stXmlFilePath, m_stSchemaFile, m_stTargetNamespace, stError)) {
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INVALID_SCHEMA_BASED_XML, 
									stXmlFilePath.c_str(), 
									m_stSchemaFile.c_str(), 
									stError.c_str());
			m_stLastError += getTreeMapper()->getErrorLog()->GetLastErr()+"\n";
		}
	}
	catch(CrException &e) {
		string stDescription(e.GetErrorString());
		string stErr = (stDescription.length()==0) ? "Unspecified reason." : stDescription.c_str();
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_INVALID_SCHEMA, m_stSchemaFile.c_str(), stErr.c_str());
		m_stLastError += getTreeMapper()->getErrorLog()->GetLastErr()+"\n";
	}
	return hr;
}

DOMElementPtr XSDParser::LookupComplexTypeDOMNodeInMap(const string& stExpandedName) {
	StringDOMMapConstIterator cit = m_complexTypeDefDOM.find(stExpandedName);
	return ( cit == m_complexTypeDefDOM.end() ) ? 0 : cit->second;
}

bool XSDParser::IsComplexType(const string& stNamespace, const string& stLocalName) {
	string stExpandedName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	                                                            
	if (LookupComplexTypeDOMNodeInMap(stExpandedName) != 0) {
		return true;
	}
	else {
		                                                                                 
		return m_pSchemaCollection->GetComplexTypeDOMNode(stNamespace, stLocalName, this) != 0;
	}
}


string XSDParser::GetNamespaceURI(const string& stPrefix) const {
	return m_namespaceDeclarations.GetNamespaceURI(stPrefix);
}

void XSDParser::GetPossibleRoots(vector<string>& vRootNames) {
	if (m_root != 0) {
		vRootNames.push_back((const char*)m_root->GetDisplayName());
	}
	for (StringDOMMapConstIterator cit = m_globalElementsDOM.begin(); cit != m_globalElementsDOM.end(); ++cit ) {
		DOMElementPtr node = cit->second;
		const string nodeName(konga::XmlUtils::GetAttributeValue(node, XsdConstants::NAME_ATTRIBUTE));
		if (m_stTargetNamespace.empty()) {
			vRootNames.push_back(nodeName);
		}
		else {
			ExpandedName en(m_stTargetNamespace, nodeName);
			vRootNames.push_back(en.toString());
		}
	}
}

unsigned long XSDParser::GetPossibleRoots(const string& stXsdFilePath, vector<string>& vRootNames,TreeMapper*pTreeMapper) {
	XSDParser p(pTreeMapper);
	SchemaCollection collection;
	                                      
	unsigned long hr = p.LoadSchema(stXsdFilePath, collection, true, false);
	if (SUCCEEDED(hr)) {
		collection.GetPossibleRoots(p.GetTargetNamespace(), vRootNames);
	}
	return hr;
}

int XSDParser::AddToProcessedDOMNodeMap(const string& stDOMPath) {
	ProcessedDOMNodeMapIterator it_ProcessedDOM = m_mapProcessedDOMNodes.find(stDOMPath);
	if (it_ProcessedDOM == m_mapProcessedDOMNodes.end()) {
		m_mapProcessedDOMNodes[stDOMPath] = 1;
		return 1;
	}
	else {
		return ++(it_ProcessedDOM->second);
	}
}

void XSDParser::RemoveFromProcessedDOMNodeMap(const string& stDOMPath) {
	ProcessedDOMNodeMapIterator it_ProcessedDOM = m_mapProcessedDOMNodes.find(stDOMPath);
	if (it_ProcessedDOM != m_mapProcessedDOMNodes.end()) {
		if (it_ProcessedDOM->second == 1) {
			m_mapProcessedDOMNodes.erase(it_ProcessedDOM);
		}
		else {
			--(it_ProcessedDOM->second);
		}
	}
}

void XSDParser::StartLogging(const string& stFilename) {
	if (m_pLogFile) {
		fclose(m_pLogFile);
	}
	m_pLogFile = fopen(stFilename.c_str(), "w+");
}

void XSDParser::StopLogging() {
	if (m_pLogFile) {
		fclose(m_pLogFile);
		m_pLogFile = 0;
	}
}

unsigned long XSDParser::GetUsedNamespaces(vector<string>& vstNamespaces,
									 bool bIncludeTargetNamespace             ,
									 bool bIncludeSchemaDefinitionNamespace             ) {
	map<string, bool> mapEncounteredNamespaces;
	ScanForNamespaceUse(m_pDOMDocument->getDocumentElement(),
						mapEncounteredNamespaces,
						bIncludeTargetNamespace,
						bIncludeSchemaDefinitionNamespace);
	for (map<string, bool>::const_iterator cit = mapEncounteredNamespaces.begin(); cit != mapEncounteredNamespaces.end(); ++cit ) {
		vstNamespaces.push_back(cit->first);
	}
	return S_OK;
}

void XSDParser::ScanForNamespaceUse(const DOMElementPtr& pElement, 
									map<string, bool>& mapEncounteredNamespaces,
									bool bIncludeTargetNamespace,
									bool bIncludeSchemaDefinitionNamespace) {
	XsdNode* node = XsdNodeFactory::CreateXsdNode(*this, pElement);
	if (node != 0) {
		string stNodeNamespace = node->GetNamespace();
		if (!stNodeNamespace.empty()) {
			if (mapEncounteredNamespaces.find(stNodeNamespace) == mapEncounteredNamespaces.end()) {
				bool bInclude = true;
				if (!bIncludeTargetNamespace && konga::Equals(stNodeNamespace, m_stTargetNamespace)) {
					bInclude = false;
				}
				if (!bIncludeSchemaDefinitionNamespace && konga::Equals(stNodeNamespace, m_stSchemaDefinitionNamespace)) {
					bInclude = false;
				}
				if (bInclude) {
					mapEncounteredNamespaces[stNodeNamespace] = true;
				}
			}
		}
		delete node;
	}
	DOMNodeListPtr pChildren = pElement->getChildNodes();
	for (int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i) {
		DOMNodePtr pChild = pChildren->item[i];
		if (pChild->getNodeType() == konga::xml::NODE_ELEMENT) {
			ScanForNamespaceUse(pChild, mapEncounteredNamespaces, bIncludeTargetNamespace, bIncludeSchemaDefinitionNamespace);
		}
	}
}

void XSDParser::Log(const string& stMessage) {
	Log("%s", stMessage);
}

void XSDParser::Log(const string& stFormat, const string& stMessage) {
	if (m_pLogFile) {
		fprintf(m_pLogFile, stFormat.c_str(), stMessage.c_str());
	}
}

void XSDParser::AddSchemaValidationWarning(const string& stWarningMsg) {
	if (m_mapSchemaValidationWarnings.find(stWarningMsg) == m_mapSchemaValidationWarnings.end()) {
		m_mapSchemaValidationWarnings[stWarningMsg] = true;
		m_vstValidationWarnings.push_back(stWarningMsg);
	}
}

string XSDParser::GetSchemaValidationWarning() const {
	string stMsg;
	for (size_t i = m_vstValidationWarnings.size()-1, n = 0; i >= 0; --i, ++n) {
		if (n > 0) {
			stMsg += " ";
		}
		stMsg += m_vstValidationWarnings[i];
	}
	return stMsg;
}

vector<string>* XSDParser::getSubstitutionGroup(const string& substitustionGroupHead) {
	return m_pSchemaCollection->getSubstitutionGroup(substitustionGroupHead);
}
