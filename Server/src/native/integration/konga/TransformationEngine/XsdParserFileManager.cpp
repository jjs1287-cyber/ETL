/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                                                      
                                                              

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>

#include <konga/Xml/XmlConstants.h>

#include <konga/Crypto/Crypto.h>	         
#include <konga/FileTransfer/FileRetriever.h>

#include "ErrorCodes/resource.h"
#include "XSDParser.h"
#include "SchemaCollection.h"
#include "TreeMapper.h"
#include "XsdImportNode.h"

using namespace std;
using namespace konga;
using namespace konga::xml;

namespace {
	void LocateExternalSchema(const string& stCurrentDirectory, string& stSchemaLocation) {
		if (FileRetriever::IsSupportedUrl(stSchemaLocation)) {
			string sFileName(AppendPathSeparator(stCurrentDirectory) + md5(stSchemaLocation) + ".xsd");
			if (!FileExists(sFileName)) {
				FileRetriever fr;
				if (fr.GetAsFile(stSchemaLocation, sFileName) != Protocol::SUCCESS) {
					throw CrException("Failed to download the external xsd: " + stSchemaLocation
						+ ".\nReason: " + fr.LastError());
				}
			}
			stSchemaLocation = sFileName;
		} else if (!FileExists(stSchemaLocation)) {
			                                                       
			string stAbsolutePath(stCurrentDirectory);
			AppendPathSeparator(stAbsolutePath);
			stAbsolutePath += stSchemaLocation;
			if (!FileExists(stAbsolutePath)) {
				throw CrException("Failed to load the included/imported file '"
					+ stSchemaLocation + "'. Also looked for it here: '"
					+ stAbsolutePath + "'");
			}
			stSchemaLocation = stAbsolutePath;
		}
	}
}

unsigned long XSDParser::LoadSchemaFromFile(const string& stSchemaFile,
											SchemaCollection& schemaCollection, 
											bool bLoadIncludedAndImportedSchemas,
											bool bValidate,
											bool bGiveOwnerShipToCollection) {
	getTreeMapper()->getViewLog()->DebugString("XSDParser::Create --- loading the schemafile '%s'", stSchemaFile.c_str());

	                                                                
	string stError;
	m_pDOMDocument = XmlUtils::LoadDoc(stSchemaFile, stError, true);
	if (m_pDOMDocument == 0) {
		m_stLastError += "Failed to parse the schema document. " + stError+"\n";
		throw CrException(m_stLastError);
	}

	m_stSchemaFile = stSchemaFile;

	const string stCurrentDirectory( GetBranchPath(stSchemaFile) );
	return LoadSchemaInternal(schemaCollection, bLoadIncludedAndImportedSchemas,
		bValidate, bGiveOwnerShipToCollection, stCurrentDirectory);
}

unsigned long XSDParser::LoadSchema(const string& stSchemaFile, 
									SchemaCollection& schemaCollection,
									bool bLoadIncludedAndImportedSchemas,
									bool bValidate            ) {
	return LoadSchemaFromFile(stSchemaFile, schemaCollection, bLoadIncludedAndImportedSchemas, bValidate, false);
}

unsigned long XSDParser::LoadSchema(DOMDocument2Ptr pSchemaDoc,
									SchemaCollection& schemaCollection,
									bool bLoadIncludedAndImportedSchemas,
									const string& stCurrentDirectory) {
	m_pDOMDocument = pSchemaDoc;
	m_stSchemaFile = "<<No Filename>>";
	
	                                                               
	return LoadSchemaInternal(schemaCollection, bLoadIncludedAndImportedSchemas,
		false, false, stCurrentDirectory);
}

unsigned long XSDParser::LoadSchemaInternal(SchemaCollection& schemaCollection, 
											bool bLoadIncludedAndImportedSchemas,
											bool bValidate,
											bool bGiveOwnerShipToCollection,
											const string& stCurrentDirectory) {
	unsigned long hr = S_OK;
	ClearMaps(false);
	m_root = 0;
	m_pSchemaNode = XsdUtils::FindSchemaElement(m_pDOMDocument->getFirstChild());
	if (m_pSchemaNode == 0) {
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_FAILED_TO_LOCATE_SCHEMA_ELEMENT, m_stSchemaFile.c_str());
		m_stLastError += (const char*)getTreeMapper()->getErrorLog()->GetLastErr();
		                                                          
		DOMNodePtr pNode=m_pDOMDocument->getFirstChild();
		m_stLastError += "List of child node of DOMDocument\n";
		while (pNode != 0) {
			m_stLastError += pNode->getBaseName();
			pNode = pNode->getNextSibling();
		}
		m_stLastError += "End of List\n";
		return hr;
	}

	m_namespaceDeclarations = XmlNamespaceDeclarations(m_pSchemaNode);
	m_stTargetNamespace = m_namespaceDeclarations.GetTargetNamespace();

	m_pSchemaCollection = &schemaCollection;
	m_pSchemaCollection->AddSchema(this, bGiveOwnerShipToCollection);

	m_schemaVersion = XsdUtils::GetSchemaVersion(m_namespaceDeclarations);
	m_stSchemaDefinitionNamespace = XsdUtils::GetSchemaDefinitionNamespace(m_schemaVersion);
	if (m_stSchemaDefinitionNamespace.empty()) {
		m_stSchemaDefinitionNamespace = XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001;
	}
	m_stSchemaDefinitionNamespacePrefix = m_namespaceDeclarations.GetNamespacePrefix(m_stSchemaDefinitionNamespace);
	m_stSchemaInstanceNamespace = XsdUtils::GetSchemaInstanceNamespace(m_schemaVersion);
	if (m_stSchemaInstanceNamespace.empty()) {
		m_stSchemaInstanceNamespace = XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001;
	}

	                                                                                    
	if (m_stSchemaDefinitionNamespacePrefix.empty()) {
		                                                                                                  
		                                             
		m_stSchemaDefinitionXPathPrefix = "xsd";
		int nPrefix = 0;
		while (m_namespaceDeclarations.IsPrefixUsed(m_stSchemaDefinitionXPathPrefix)) {
			m_stSchemaDefinitionXPathPrefix = "xsd" + konga::toString(++nPrefix);
		}
	}
	else {
		m_stSchemaDefinitionXPathPrefix = m_stSchemaDefinitionNamespacePrefix;
	}

	m_pDOMDocument->addSelectionNamespace(m_stSchemaDefinitionXPathPrefix, m_stSchemaDefinitionNamespace);

	GetDefaultFormValues();

	m_bSchemaIsValid = true;
	m_mapSchemaValidationWarnings.clear();
	m_vstValidationWarnings.clear();
	if (bValidate) {
		hr = ValidateSchema();
		if (FAILED(hr)) {
			m_bSchemaIsValid = false;
			if (m_bContinueOnValidationError) {
				AddSchemaValidationWarning( (const char*)getTreeMapper()->getErrorLog()->GetLastErr() );
			}
			else {
				m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
				return hr;
			}
		}
	}
	FindImportNodes();
	if (bLoadIncludedAndImportedSchemas) {
		hr = LoadIncludedSchemas(bValidate, stCurrentDirectory);
		if (FAILED(hr)) {
			m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
			return hr;
		}

		hr = LoadImportedSchemas(bValidate, stCurrentDirectory);
		if (FAILED(hr)) {
			m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
			return hr;
		}
	}

	try {
		hr = CreateMapsOfGlobals();
		if (FAILED(hr)) {
			m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
			ClearMaps(false);
			return hr;
		}
	}
	catch (CrException& e) {
		string stError(e.GetErrorString());
		if (stError.empty()) {
			stError = "Unkown error";
		}
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_FATAL_ERROR, konga::GetLeafPath(m_stSchemaFile).c_str(), stError.c_str());
		m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
		return hr;
	}
	catch( ... ) {
		                                                                                        
		                                                                                    
		             
		string stValidationWarning = GetSchemaValidationWarning();
		string stError = stValidationWarning.empty() ? "Unknown error." : stValidationWarning;
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XSDPARSER_FATAL_ERROR, konga::GetLeafPath(m_stSchemaFile).c_str(), stError.c_str());
		m_stLastError += string((const char*)getTreeMapper()->getErrorLog()->GetLastErr())+"\n";
		return hr;
	}
	return hr;
}

void XSDParser::FindImportNodes() {
	DOMNodePtr pDOMNode = m_pSchemaNode->getFirstChild();
	while ((pDOMNode != 0) && (pDOMNode->getNodeType() != konga::xml::NODE_ELEMENT)) {
		pDOMNode = pDOMNode->getNextSibling();
	}
	while (pDOMNode != 0) {
		DOMElementPtr pNode = pDOMNode;
		string stBaseName = konga::XmlUtils::GetNodeBaseName(pNode);
		if (konga::Equals(stBaseName, XsdConstants::IMPORT)) {
			                                                                     
			if ( pNode->getAttribute(XmlConstants::NAMESPACE, false).compare(XmlConstants::XML_NAMESPACE_1998) != 0 ) {
				m_vImportedNodes.push_back(new XsdImportNode(*this, pNode));
			}
		}
		pDOMNode = pDOMNode->getNextSibling();
		while ((pDOMNode != 0) && (pDOMNode->getNodeType() != konga::xml::NODE_ELEMENT)) {
			pDOMNode = pDOMNode->getNextSibling();
		}
	}
}

unsigned long XSDParser::GetImportedNamespaces(vector<string>& vstNamespaces) {
	for (size_t i = 0; i < m_vImportedNodes.size(); ++i) {
		vstNamespaces.push_back(m_vImportedNodes[i]->GetImportedNamespace());
	}
	return S_OK;
}

unsigned long XSDParser::LoadImportedSchemas(bool bValidate, const string& stCurrentDirectory) {
	for (size_t i = 0; i < m_vImportedNodes.size(); ++i) {
		XsdImportNode* pImportNode = m_vImportedNodes[i];
		string stSchemaLocation = pImportNode->GetSchemaLocation();
		if (!stSchemaLocation.empty()) {
			LocateExternalSchema(stCurrentDirectory, stSchemaLocation);
			string stNamespace = pImportNode->GetImportedNamespace();
			if (!m_pSchemaCollection->Contains(stNamespace, stSchemaLocation)) {
				XSDParser* p = new XSDParser(getTreeMapper());	                                                               
				p->SetContinueOnValidationError(m_bContinueOnValidationError);

				unsigned long hr = p->LoadSchemaFromFile(stSchemaLocation, *m_pSchemaCollection, true, bValidate, true);
				if (FAILED(hr)) {
					return hr;
				}
				if (!p->IsSchemaValid()) {
					vector<string>& vstWarnings = p->GetSchemaValidationWarnings();
					for (int nW = static_cast<int>(vstWarnings.size())-1; nW >= 0; --nW) {
						AddSchemaValidationWarning( vstWarnings[nW] );
					}
				}
			}
		}
	}
	return S_OK;
}

unsigned long XSDParser::LoadIncludedSchemas(bool bValidate, const string& stCurrentDirectory) {
	DOMNodePtr pDOMNode = m_pSchemaNode->getFirstChild();
	while ((pDOMNode != 0) && (pDOMNode->getNodeType() != konga::xml::NODE_ELEMENT)) {
		pDOMNode = pDOMNode->getNextSibling();
	}
	while (pDOMNode != 0) {
		DOMElementPtr pNode = pDOMNode;
		string stBaseName = konga::XmlUtils::GetNodeBaseName(pNode);
		if (konga::Equals(stBaseName, XsdConstants::INCLUDE)) {
			string stSchemaLocation = konga::XmlUtils::GetAttributeValue(pNode, XsdConstants::SCHEMALOCATION_ATTRIBUTE);
			if (!stSchemaLocation.empty()) {
				LocateExternalSchema(stCurrentDirectory, stSchemaLocation);
				                                                                           
				if (!m_pSchemaCollection->Contains(m_stTargetNamespace, stSchemaLocation)) {
					XSDParser* p = new XSDParser(getTreeMapper());	                                                               
					p->SetContinueOnValidationError(m_bContinueOnValidationError);
					unsigned long hr = p->LoadSchemaFromFile(stSchemaLocation, *m_pSchemaCollection, true, bValidate, true);
					if (FAILED(hr)) {
						return hr;
					}
					if (!p->IsSchemaValid()) {
						vector<string>& vstWarnings = p->GetSchemaValidationWarnings();
						for (int nW = static_cast<int>(vstWarnings.size())-1; nW >= 0; --nW) {
							AddSchemaValidationWarning( vstWarnings[nW] );
						}
					}
				}
			}
		}
		pDOMNode = pDOMNode->getNextSibling();
		while ((pDOMNode != 0) && (pDOMNode->getNodeType() != konga::xml::NODE_ELEMENT)) {
			pDOMNode = pDOMNode->getNextSibling();
		}
	}
	return S_OK;
}

