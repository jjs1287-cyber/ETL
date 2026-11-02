/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>
#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/ExpandedName.h>

#include "XsdElement.h"
#include "XsdUtils.h"
#include "crom.h"
#include "CromUtils.h"
#include "XSDParser.h"
#include "XsdParserException.h"

using namespace konga;
using namespace konga::xml;
using namespace std;
using konga::XsdParserException;


namespace {

	void ReplaceNode(CROM*source, CROM*target) {
		if((source == 0) || (target == 0)) {
			return;
		}
		CROM*parent = target->GetParent();
		CROM*prevSibling = target->GetPreviousSibling();
		CROM*nextSibling = target->GetNextSibling();
		source->SetParent(parent);
		source->SetPreviousSibling(prevSibling);
		source->SetNextSibling(nextSibling);
		if (parent != 0) {
			if (target == parent->GetFirstChild()) {
				parent->SetFirstChild(source);
			}
		}
		if (prevSibling != 0) {
			prevSibling->SetNextSibling(source);
		}
		if (nextSibling != 0) {
			nextSibling->SetPreviousSibling(source);
		}
		target->SetParent(0);
		target->SetPreviousSibling(0);
		target->SetNextSibling(0);
		target->DeleteNode();
	}

}

XsdElement::XsdElement(XSDParser& xsdParser, 
					   const DOMElementPtr& pDOMNode, 
					   CROM* pCROMParent, 
					   CROM* pPreviousSibling,
					   XsdParserConstants::NodeType lastNodeType) : 
												XsdValueNode(xsdParser, 
															 pDOMNode, 
															 pCROMParent, 
															 pPreviousSibling,
															 lastNodeType),
				 m_pTreeMapper(xsdParser.getTreeMapper())
{
	int iMinOccurs, iMaxOccurs;
	XsdUtils::GetMinMaxOccurs(pDOMNode, iMinOccurs, iMaxOccurs);
	SetMinOccurs(iMinOccurs);
	SetMaxOccurs(iMaxOccurs);
	m_isNillable = XsdUtils::IsTrue(pDOMNode, XsdConstants::NILLABLE);
	m_substitutionGroup = GetDOMNodeAttribute(XsdConstants::SUBSTITUTION_GROUP);
}

XsdElement::~XsdElement(void)
{}


unsigned long XsdElement::Parse(string& stError) {
	                       
	unsigned long hr = S_OK;
	DOMElementPtr pNode = GetDOMNode();
	CROM* pValueNode = 0;

	string stTypeNamespace, stTypeLocalName;
	string stSchemaType, stSchemaTypeNamespace, stCRType, stExpandedTypeNameString;
	bool isRecursiveStop = m_xsdParser.AddToProcessedDOMNodeMap( GetDOMPath() ) >= 2;
	bool isElementRef = !GetReference().empty();
	bool isTypeRef = false;
	bool isValueNode = false;

	bool isAbstract = XsdUtils::IsTrue(pNode, XsdConstants::ABSTRACT_ATTRIBUTE);

	string stNamespaceURI, stLocalName;
	const string stElementName = isElementRef ? GetReference() : GetName();
	GetNamespaceAndLocalName(stElementName, stNamespaceURI, stLocalName);
	const string stSubstitutionGroupHeadName(XsdUtils::GetExpandedNameAsString(stNamespaceURI, GetName()));
	vector<string>*vSubstitutionGroup=m_xsdParser.getSubstitutionGroup(stSubstitutionGroupHeadName);
	bool bSubstitutionGroup = (vSubstitutionGroup != 0) && !vSubstitutionGroup->empty();
	if (GetType().empty() && !isTypeRef) {
		                                                                             
		                                                                                 
		                                                                             
		                            
		if (!IsSubstitutionGroupElement()) {
			bool bIsAnyType = true;
			DOMNodeListPtr pChildren = GetDOMNode()->getChildNodes();
			for (int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i) {
				DOMNodePtr pChild = pChildren->item[i];
				if (pChild->getNodeType() == konga::xml::NODE_ELEMENT) {
					if (!konga::Equals(konga::XmlUtils::GetNodeBaseName(pChild), XsdConstants::ANNOTATION)) {
						bIsAnyType = false;
						break;
					}
				}
			}
			if (bIsAnyType) {
				MarkAsAnyType();
			}
		}
	}

	                                                                                
	                  
	if (!GetType().empty()) {
		GetNamespaceAndLocalName(GetType(), stTypeNamespace, stTypeLocalName);
		ExpandedName expandedTypeName(stTypeNamespace, stTypeLocalName);
		stExpandedTypeNameString = expandedTypeName.toString();
		stSchemaTypeNamespace = stTypeNamespace;
		stSchemaType = stTypeLocalName;
		if (m_xsdParser.IsComplexType(stTypeNamespace, stTypeLocalName)) {
			isTypeRef = true;
		}
		else {
			                                                    
			isValueNode = true;

			                                                 
			pValueNode = m_xsdParser.GetSimpleTypeDefinition(stTypeNamespace, stTypeLocalName);
			if (pValueNode == 0) {
				                                                                     
				stCRType = m_xsdParser.GetCRDataType(expandedTypeName);
			}
		}
	}

	SetLastNodeType(XsdParserConstants::t_element);
	int iType = CROM_t_Element | GetCROMCardinalityType();
	if(bSubstitutionGroup)iType |= CROM_t_SubstitutionGroupHead;
	CROM* root = new CROM(getTreeMapper(), GetCROMParent(), GetPreviousSibling(), stLocalName.c_str(), "", iType, CROM::t_CROM_Xml);
	root->setSchemaType( stSchemaTypeNamespace, stSchemaType );
	root->SetMinUse( GetMinOccurs() );
	root->SetMaxUse( GetMaxOccurs() );
	                                                                                                     
	if (!isElementRef) {
		root->SetNamespace(m_xsdParser.GetTargetNamespace());
		root->SetNamespaceQualified(IsGlobal() || m_xsdParser.ShouldLocalEntityBeQualified(pNode, konga::xml::NODE_ELEMENT));
	}

	try {
		if (bSubstitutionGroup) {
			CreateSubstitutionGroupElements(root, stSubstitutionGroupHeadName);
		}
		if (isElementRef && !isRecursiveStop) {
			m_xsdParser.RemoveFromProcessedDOMNodeMap( GetDOMPath() );
			AbortRecursiveTraversal();
			                                       
			return InsertElementReference(root, stNamespaceURI, stLocalName);
		}
		if (isTypeRef && !isRecursiveStop) {
			hr = m_xsdParser.InsertComplexTypeDefinition(root, stTypeNamespace, stTypeLocalName, XsdParserConstants::Element);
			if (root != 0) {
				CheckNillable(root);
				if (isAbstract) {
					root->SetAbstract();
				}
			}
			if (hr == S_OK) {
				m_xsdParser.RemoveFromProcessedDOMNodeMap( GetDOMPath() );			
			}
			AbortRecursiveTraversal();
			return hr;
		}
	} catch (const XsdParserException&) {
		root->DeleteNode();
		throw;
	}
	if (isValueNode) {
		if (pValueNode == 0) {
			pValueNode = new CROM(getTreeMapper(), root, 0, "", "", CROM_t_Value, CROM::t_CROM_Xml);
			pValueNode->SetValueType( stCRType.c_str() );
			if (stCRType=="Date") {
				const string::size_type k=stExpandedTypeNameString.find_last_of('}');
				string typeStr = (k == string::npos) ? stExpandedTypeNameString : 
					stExpandedTypeNameString.substr(k+1);
				pValueNode->SetFormat(typeStr.c_str());
			}
		} else {
			pValueNode->SetParent(root);
			root->SetFirstChild(pValueNode);
		}
		if (HasDefaultValue()) {
			pValueNode->setDefault( GetDefaultValue().c_str() );	                                                         
		} else if (HasFixedValue()) {
			pValueNode->setDefault(GetFixedValue().c_str());
		}
		CheckNillable(root, pValueNode);
		SetLastNodeType(XsdParserConstants::t_value);
	}


	                                                                                                           
	                                                                                                            
	                                
	m_xsdParser.RemoveFromProcessedDOMNodeMap( GetDOMPath() );

	if (isAbstract && (root != 0)) {
		root->SetAbstract();
	}
	SetCROMRoot(root);
	return hr;
}

void XsdElement::CheckNillable(CROM* root, CROM* valueNode        ) {
	if (m_isNillable) {
		CROM* nilAttribute = new CROM(getTreeMapper(), 0, 0, "xsi:nil", "", CROM_t_Attribute, CROM::t_CROM_Xml);
		nilAttribute->SetValueType("Boolean");
		nilAttribute->m_Type |= XsdUtils::GetCROMCardinalityType(0, 1);
		                             
		nilAttribute->setDefault("false");
		CromUtils::addAsFirstChild(root, nilAttribute);
		if (valueNode != 0) {
			valueNode->m_Type |= XsdUtils::GetCROMCardinalityType(0, 1);
		}
	}
}

unsigned long XsdElement::InsertElementReference(CROM* element,
												 const string& stNamespace,
												 const string& stLocalName) {
	CROM* ref = m_xsdParser.GetElementDefinition(stNamespace, stLocalName);
	if (ref != 0) {
		CROM* cloned_ref = new CROM(ref);

		                                                                                                 
		cloned_ref->m_Type |= element->m_Type;
		cloned_ref->SetNamespace( ref->GetNamespace() );
		                                                  

		ReplaceNode(cloned_ref, element);
		return S_OK;
	} else {
		string stError = "The referenced element '";
		stError += stLocalName;
		stError += "' in the namespace '";
		stError += stNamespace;
		stError += "' does not exist in this schema.";
		throw XsdParserException(stError);
	}
}

void XsdElement::CreateSubstitutionGroupElements(CROM* parent, const string& stUniversalName) {
	vector<string>* vSubstitutionGroup = m_xsdParser.getSubstitutionGroup(stUniversalName);
	for (vector<string>::const_iterator cit = vSubstitutionGroup->begin(); cit != vSubstitutionGroup->end(); cit++) {
		const string& stSubstitution(*cit);
		parent->AddSubstitutionGroupElement(stSubstitution);
		if (stSubstitution == stUniversalName) {
			continue;
		}
		ExpandedName expandedName(ExpandedName::fromString(stSubstitution));
		string stNamespaceURI(expandedName.uri());
		string stLocalName(expandedName.local());
		CROM* pCrom = m_xsdParser.GetElementDefinition(stNamespaceURI, stLocalName);
		if (pCrom == 0) {
			pCrom = m_xsdParser.GetComplexTypeDefinition(stNamespaceURI, stLocalName);
		}
		if (pCrom == 0) {
			string stError = "The substitutionGroup element '";
			stError += stLocalName;
			stError += "' in the namespace '";
			stError += stNamespaceURI;
			stError += "' does not exist in this schema.";
			throw XsdParserException(stError);
		}
	}
}

string XsdElement::GetNamespace()const {
	string stNamespace, stLocalName;
	if (HasType()) {
		GetNamespaceAndLocalName(GetType(), stNamespace, stLocalName);
	} else if (HasReference()) {
		GetNamespaceAndLocalName(GetReference(), stNamespace, stLocalName);
	} else {
		string stSubstitutionGroup = GetDOMNodeAttribute( XsdConstants::SUBSTITUTION_GROUP );
		if (!stSubstitutionGroup.empty()) {
			GetNamespaceAndLocalName(stSubstitutionGroup, stNamespace, stLocalName);
		} else {
			stNamespace = m_xsdParser.GetTargetNamespace();
		}
	}
	return stNamespace;
}

bool XsdElement::IsGlobal() const {
	CROM* parent = GetCROMParent();
	if (parent == 0) {
		return false;
	}
	return (parent->CouldBeRoot() || (parent->m_Type&CROM_t_Root) != 0);
}
