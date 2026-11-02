/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Xml/XsdConstants.h>
#include <konga/Xml/XmlUtils.h>

#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "crom.h"
#include "NoValueContentXmlCreator.h"
#include "XsdNamespaceSupport.h"

using namespace std;
using namespace konga;
using namespace konga::xml;

namespace {

	bool SearchForValueContentNode(CROM* node) {
		if (node->IsValueLeaf() || node->IsAttribute()) {
			return true;
		}

		CROM* child = node->GetFirstChild();
		while (child) {
			if (SearchForValueContentNode(child)) {
				return true;
			}
			child = child->GetNextSibling();
		}
		return false;
	}
	
}

NoValueContentXmlCreator::NoValueContentXmlCreator(CErrorLog* errorLog, const XsdNamespaceSupport& ns) :
		m_errorLog(errorLog),
		m_xsdNamespaces(ns)
{}

bool NoValueContentXmlCreator::HasValueContent(CROM* root) {
	return SearchForValueContentNode(root);
}

void NoValueContentXmlCreator::CreateNoValueContentXML(CROM* structureRoot,
													   const string& stNameOfRootToUse, 
													   const string& stNamespaceUri, 
													   const string& stOutputFile) {
	CROM* root_to_use = 0;
	if (EqualsIgnoreCase(stNameOfRootToUse, (const char*)structureRoot->GetDisplayName()) &&
			Equals(stNamespaceUri, structureRoot->GetNamespace())) {
				root_to_use = structureRoot;
	}
	else {
		root_to_use = structureRoot->FindChild(CROM_t_Element, stNameOfRootToUse, stNamespaceUri);
	}
	if (root_to_use == 0) {
		m_errorLog->MakeError(__FILE__, __LINE__, ID_CREATENOVALUECONTENTXML_INVALID_ROOTNAME,
			stNameOfRootToUse.c_str(), stNamespaceUri.c_str());
		throw CrException((const char*)m_errorLog->GetLastErr());
	}

	DOMDocument2Ptr pDoc;
	string stErr;
	long hr = konga::XmlUtils::CreateDOMDocument2Instance(pDoc, true, stErr);
	if (hr != 0) {
		m_errorLog->MakeError(__FILE__, __LINE__, ID_CREATENOVALUECONTENTXML_DOMDOC_ERROR, stErr.c_str());
		throw CrException((const char*) m_errorLog->GetLastErr());
	}

	                     
	DOMElementPtr pRoot = CreateNoValueContentDOMTree(pDoc, root_to_use, 0);
	if (pRoot == 0) {
		throw CrException((const char*) m_errorLog->GetLastErr());
	}
	                                                                                             
	string stSchemaDefPrefix(m_xsdNamespaces.GetSchemaDefinitionNamespacePrefix());
	string stSchemaInstPrefix(m_xsdNamespaces.GetSchemaInstanceNamespacePrefix());
	string stAttributeName = stSchemaDefPrefix.length() > 0 ? "xmlns:" + stSchemaDefPrefix : "xmlns";
	pRoot->setAttribute(stAttributeName.c_str(), m_xsdNamespaces.GetSchemaDefinitionNamespace().c_str());
	stAttributeName = stSchemaInstPrefix.length() > 0 ? "xmlns:" + stSchemaInstPrefix : "xmlns";
	pRoot->setAttribute(stAttributeName.c_str(), m_xsdNamespaces.GetSchemaInstanceNamespace().c_str());

	pDoc->appendChild(pRoot);
	bool savedComplete = pDoc->save(stOutputFile.c_str());
	if (!savedComplete) {
		m_errorLog->MakeError(__FILE__, __LINE__, ID_CREATENOVALUECONTENTXML_SAVE_ERROR, 
			stOutputFile.c_str(), konga::XmlUtils::GetLastError(pDoc).c_str());
		throw CrException((const char*) m_errorLog->GetLastErr());
	}
}

DOMElementPtr NoValueContentXmlCreator::CreateNoValueContentDOMTree(DOMDocument2Ptr& pDoc, CROM* structureNode, DOMNodePtr pParent) {
	string stNamespaceURI(structureNode->GetNamespace());
	string stPrefix(GetNamespacePrefix(stNamespaceURI));
	string stElementName(XmlUtils::GetQualifiedName(stPrefix, (const char*)structureNode->GetDisplayName()));

	DOMElementPtr pElement(0);
	pElement = pDoc->createNode(konga::xml::NODE_ELEMENT, stElementName.c_str(), stNamespaceURI.c_str());
	if (structureNode->GetSchemaType().length() > 0) {
		string stAttributePrefix(GetSchemaInstanceNamespacePrefix());
		string stAttributeName(XmlUtils::GetQualifiedName(stAttributePrefix, XsdConstants::TYPE));

		DOMAttributePtr pAttr = pDoc->createNode(
			konga::xml::NODE_ATTRIBUTE, stAttributeName.c_str(), GetSchemaInstanceNamespace().c_str());

		if (pAttr == 0) {
			string stError("Failed to add the type attribute to the element node \"");
			stError += stElementName; 
			stError += "\"";
			m_errorLog->MakeError(__FILE__, __LINE__, ID_CREATENOVALUECONTENT_DOMTREE_FAILED, stError.c_str());
			throw CrException((const char*) m_errorLog->GetLastErr());
		}
		string stTypeNamePrefix(GetNamespacePrefix(structureNode->GetSchemaTypeNamespace()));
		string stAttributeValue(XmlUtils::GetQualifiedName(stTypeNamePrefix, structureNode->GetSchemaType()));
		pAttr->setValue(stAttributeValue);
		pElement->setAttributeNode(pAttr);
	}
	if (pParent != 0) {
		pParent->appendChild(pElement);
	}
	
	CROM* child = structureNode->GetFirstChild();
	while (child != 0) {
		if (child->IsElement()) {
			DOMNodePtr p = CreateNoValueContentDOMTree(pDoc, child, pElement);
			if (p == 0) {
				return 0;
			}
		}
		child = child->GetNextSibling();
	}
	return pElement;
}


string NoValueContentXmlCreator::GetNamespacePrefix(const string& uri) const {
	return m_xsdNamespaces.GetNamespacePrefix(uri);
}

string NoValueContentXmlCreator::GetSchemaInstanceNamespace() const {
	return m_xsdNamespaces.GetSchemaInstanceNamespace();
}

string NoValueContentXmlCreator::GetSchemaInstanceNamespacePrefix() const {
	return m_xsdNamespaces.GetSchemaInstanceNamespacePrefix();
}
