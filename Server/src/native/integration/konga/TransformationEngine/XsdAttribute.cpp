/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <konga/Soap/SoapEncoding.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Xml/ExpandedName.h>
#include <konga/Xml/XmlConstants.h>

#include "XsdAttribute.h"
#include "XsdUtils.h"
#include "crom.h"
#include "XSDParser.h"
#include "XsdParserException.h"

using namespace konga;
using namespace konga::xml;
using namespace std;

XsdAttribute::XsdAttribute(XSDParser& xsdParser, 
						   const DOMElementPtr& pDOMNode, 
						   CROM* pCROMParent, 
						   CROM* pPreviousSibling,
						   XsdParserConstants::NodeType lastNodeType) : 
													XsdValueNode(xsdParser, 
																 pDOMNode, 
																 pCROMParent, 
																 pPreviousSibling,
																 lastNodeType),
													m_pTreeMapper(xsdParser.getTreeMapper()) {
	string stUseValue = GetDOMNodeAttribute(XsdConstants::USE_ATTRIBUTE);
	int iMinUse = 0, iMaxUse = 1;
	if (stUseValue.length() > 0) {
		if (EqualsIgnoreCase(stUseValue, XsdConstants::REQUIRED_ATTRIBUTE)) {
			iMinUse = iMaxUse = 1;
		} else if (EqualsIgnoreCase(stUseValue, XsdConstants::PROHIBITED_ATTRIBUTE)) {
			iMinUse = iMaxUse = 0;
		}
	}
	SetMinOccurs(iMinUse);
	SetMaxOccurs(iMaxUse);
}

XsdAttribute::~XsdAttribute(void)
{}

unsigned long XsdAttribute::Parse(string& stError) {
	unsigned long hr = S_OK;
	CROM* pValueNode = 0;

	bool isRecursiveStop = m_xsdParser.AddToProcessedDOMNodeMap( GetDOMPath() ) >= 2;
	bool isArrayMarker = false;
	bool isReference = !GetReference().empty();
	bool isTypeRef = !GetType().empty();
	
	string stAttributeName;
	string stNamespaceURI, stLocalName, stUniversalRefName, stExpandedTypeNameString;
	string stTypeNamespace, stTypeLocalName;
	string stSchemaType, stSchemaTypeNamespace, stCRType;

	if (isReference) {
		stAttributeName = GetReference();
		GetNamespaceAndLocalName(stAttributeName, stNamespaceURI, stLocalName);
		stUniversalRefName = XsdUtils::GetExpandedNameAsString(stNamespaceURI, stLocalName);
		string stSoapEncArrayType(XsdUtils::GetExpandedNameAsString(SoapEncoding::VERSION_11_NAMESPACE, "arrayType"));
		isArrayMarker = EqualsIgnoreCase(stUniversalRefName, stSoapEncArrayType);
		if (isArrayMarker) {
			stAttributeName = "arrayType";
			isReference = false;
		}
	} else {
		stAttributeName = GetName();
		if (isTypeRef) {
			GetNamespaceAndLocalName(GetType(), stTypeNamespace, stTypeLocalName);
			ExpandedName expandedTypeName(stTypeNamespace, stTypeLocalName);
			stExpandedTypeNameString = expandedTypeName.toString();
			if (m_xsdParser.IsComplexType(stTypeNamespace, stTypeLocalName)) {
				isTypeRef = true;
			} else {
				pValueNode = m_xsdParser.GetSimpleTypeDefinition(stTypeNamespace, stTypeLocalName);
				if (pValueNode == 0) {
					                                                                     
					stCRType = m_xsdParser.GetCRDataType(expandedTypeName);
				}
			}
		}
	}

	CROM* root = 0;
	if (isReference && !isRecursiveStop) {
		AbortRecursiveTraversal();
		hr = InsertAttributeRef( root, GetCROMParent(), GetPreviousSibling(), stNamespaceURI, stLocalName );
		if (FAILED(hr)) {
			return hr;
		}
	} else {
		int iType = CROM_t_Attribute | GetCROMCardinalityType();
		if (pValueNode == 0) {
			root = new CROM(getTreeMapper(), GetCROMParent(), GetPreviousSibling(), stAttributeName.c_str(), "", iType, CROM::t_CROM_Xml);
			root->SetValueType( stCRType.c_str() );
			if (stCRType=="Date" ) {
				string::size_type k = stExpandedTypeNameString.find_last_of('}');
				std::string typeStr = (k == string::npos) ? stExpandedTypeNameString : 
					stExpandedTypeNameString.substr(k+1);
				root->SetFormat(typeStr.c_str());
			}
		} else {
			pValueNode->SetDisplayName((const char*)stAttributeName.c_str());
			pValueNode->m_Name = stAttributeName.c_str();
			pValueNode->m_Type = iType;
			root = pValueNode;
			root->SetParent(GetCROMParent());
			if (GetPreviousSibling() == 0) {
				GetCROMParent()->SetFirstChild(root);
			} else {
				root->SetPreviousSibling( GetPreviousSibling() );
				GetPreviousSibling()->SetNextSibling(root);
			}
		}
	}
	SetLastNodeType(XsdParserConstants::t_attribute);
	if (HasDefaultValue()) {
		root->setDefault(GetDefaultValue().c_str());
	} else if (HasFixedValue()) {
		root->setDefault(GetFixedValue().c_str());
		root->AddType(CROM_t_FixedValue);
	}
	if (isArrayMarker) {
		root->SetHidden();
	}
	if (!isReference) {
		bool isNamespaceQualified = IsGlobal() || m_xsdParser.ShouldLocalEntityBeQualified(GetDOMNode(), konga::xml::NODE_ATTRIBUTE);
		root->SetNamespaceQualified(isNamespaceQualified);
		if (isNamespaceQualified) {
			root->SetNamespace(m_xsdParser.GetTargetNamespace());
		}
	}
	m_xsdParser.RemoveFromProcessedDOMNodeMap(GetDOMPath());
	SetCROMRoot(root);
	return S_OK;
}

unsigned long XsdAttribute::InsertAttributeRef(CROM*& pAttribute, 
									     CROM* pParent,
										 CROM* pPrevSibling,
										 const std::string& stNamespace,
										 const std::string& stLocalName) {
	if (stNamespace == XmlConstants::XML_NAMESPACE_1998 ) {
		                                                                                                                   
		const string name(XmlConstants::XML_NAMESPACE_1998_PREFIX+":"+stLocalName);
		pAttribute = new CROM(m_pTreeMapper, pParent, pPrevSibling, name.c_str(), "", CROM_t_Attribute | GetCROMCardinalityType(), CROM::t_CROM_Xml);
	} else {
		CROM* attribute = m_xsdParser.GetAttributeDefinition(stNamespace, stLocalName);
		if (attribute == 0) {
			string stError = "The referenced attribute '";
			stError += stLocalName;
			stError += "' in the namespace '";
			stError += stNamespace;
			stError += "' does not exist in this schema.";
			throw XsdParserException(stError);
		}
		pAttribute = new CROM(attribute);
		pAttribute->SetParent(pParent);
		pAttribute->m_Type = CROM_t_Attribute | GetCROMCardinalityType();
	}
	if (pPrevSibling == 0) {
		pParent->SetFirstChild(pAttribute);
	} else {
		pAttribute->SetPreviousSibling(pPrevSibling);
		pPrevSibling->SetNextSibling(pAttribute);
	}
	return S_OK;
}

string XsdAttribute::GetNamespace() const {
	string stNamespace, stLocalName;
	if (HasType()) {
		GetNamespaceAndLocalName(GetType(), stNamespace, stLocalName);
	} else if (HasReference()) {
		GetNamespaceAndLocalName(GetReference(), stNamespace, stLocalName);
	} else {
		stNamespace = m_xsdParser.GetTargetNamespace();
	}
	return stNamespace;
}

bool XsdAttribute::IsGlobal() const {
	CROM* parent = GetCROMParent();
	if (parent == 0) {
		return false;
	}
	return (parent->CouldBeRoot()) || ((parent->m_Type&CROM_t_Root) != 0);
}
