/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <string>
#include <vector>

#include <konga/Xml/XmlIncludes.h>
#include <konga/Xml/DOMElementListPtr.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/DOMNamedNodeMapPtr.h>
#include <konga/Xml/XsdConstants.h>
#include <konga/Xml/XmlUtils.h>

#include "SoapEnvelopeCleaner.h"

using namespace konga;
using namespace konga::xml;
using std::string;
using std::vector;

SoapEnvelopeCleaner::SoapEnvelopeCleaner(void) :m_pUsedNamespaces( new XmlNamespaceDeclarations() ) {
}

SoapEnvelopeCleaner::~SoapEnvelopeCleaner(void) {
	delete m_pUsedNamespaces;
	for( NamespacesT::iterator it = m_vNamespaceDeclarations.begin(); it != m_vNamespaceDeclarations.end(); ++it ) {
		delete *it;
		*it = 0;
	}
	m_vNamespaceDeclarations.clear();
}

void SoapEnvelopeCleaner::CleanSoapEnvelope(DOMElementPtr& pEnvelopeNode) {
	m_vNamespaceDeclarations.push_back( new XmlNamespaceDeclarations(pEnvelopeNode) );
	
	FindUsedNamespacesOneLevel(pEnvelopeNode);
	DOMElementListPtr pChildren = pEnvelopeNode->getChildNodes();
	const unsigned long nChildren = pChildren.getLength();
	for( unsigned long index = 0; index < nChildren; ++index ) {
		DOMElementPtr child = pChildren.item[index];
		RemoveDuplicateNamespaces(child);
	}

	RemoveUnusedNamespaces(pEnvelopeNode);
}

void SoapEnvelopeCleaner::RemoveUnusedNamespaces(xml::DOMElementPtr& node) {
#ifdef _DEBUG
	const string debugNodeName(node.getNodeName());
#endif

	string name, prefix;
	vector<DOMAttrPtr> attributesToRemove;
	DOMNamedNodeMapPtr attributes = node->getAttributes();
	const unsigned long nAttr = attributes->getLength();
	for( unsigned long iAttr = 0; iAttr < nAttr; ++iAttr ) {
		const DOMAttrPtr attr( attributes.getItem(iAttr) );
		name.clear(); prefix.clear();
		XmlUtils::GetNodeNameAndNamespacePrefix(attr, name, prefix);
		if ( name == XsdConstants::XMLNS ) {
			                                      
		}
		else if ( prefix == XsdConstants::XMLNS ) {
			                                   
			string namespaceURI = attr.getValue();
			if ( namespaceURI != XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001 &&
				 namespaceURI != XsdConstants::SCHEMA_DEFINITION_NAMESPACE_1999 &&
				 !m_pUsedNamespaces->IsNamespaceDeclared(namespaceURI) )
			{
				attributesToRemove.push_back(attr);
			}
		}
	}

	for( vector<DOMAttrPtr>::const_iterator cit = attributesToRemove.begin(); cit != attributesToRemove.end(); ++cit ) {
		if ( *cit != 0 ) {
			node.removeAttributeNode(*cit);
		}
	}

#ifdef _DEBUG
	const unsigned long debugNAttr = node.getAttributes().getLength();
#endif

	DOMElementListPtr pChildren = node->getChildNodes();
	const unsigned long nChildren = pChildren.getLength();
	for( unsigned long index = 0; index < nChildren; ++index ) {
		DOMElementPtr child = pChildren.item[index];
		RemoveUnusedNamespaces(child);
	}
}

void SoapEnvelopeCleaner::FindUsedNamespacesOneLevel(xml::DOMElementPtr& node) {
#ifdef _DEBUG
	const string debugNodeName( node.getLocalName() );
#endif
	string namespaceURI( node.getNamespaceURI() );
	string prefix( node.getPrefix() );
	AddUniqueNamespace(prefix, namespaceURI);

	string name;
	DOMNamedNodeMapPtr attributes = node->getAttributes();
	const unsigned long nAttr = attributes->getLength();
	for( unsigned long iAttr = 0; iAttr < nAttr; ++iAttr ) {
		const DOMAttrPtr attr( attributes.getItem(iAttr) );
		name.clear(); prefix.clear();
		XmlUtils::GetNodeNameAndNamespacePrefix(attr, name, prefix);
		if ( name == XsdConstants::XMLNS ) {
			                               
		}
		else if ( prefix == XsdConstants::XMLNS ) {
			                                   
		}
		else {
			namespaceURI = GetNamespaceURI(prefix);
			AddUniqueNamespace(prefix, namespaceURI);
			if( namespaceURI == XsdConstants::SCHEMA_INSTANCE_NAMESPACE_1999 ||
				namespaceURI == XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001 )
			{
				const string qName( attr.getValue() );
				string localName; prefix.clear();
				XmlUtils::GetPrefixAndLocalName(qName, prefix, localName);
				AddUniqueNamespace(prefix, this->GetNamespaceURI(prefix));
			}
		}
	}
}

void SoapEnvelopeCleaner::AddUniqueNamespace(const std::string& prefix, const std::string& namespaceURI) {
	if ( !prefix.empty() && !namespaceURI.empty() && !m_pUsedNamespaces->IsNamespaceDeclared(namespaceURI) ) {
		m_pUsedNamespaces->AddNamespaceDeclaration(namespaceURI, prefix);
	}
}

std::string SoapEnvelopeCleaner::GetNamespaceURI(const std::string& prefix) const {
	for( NamespacesT::const_iterator cit = m_vNamespaceDeclarations.begin(); cit != m_vNamespaceDeclarations.end(); ++cit ) {
		const XmlNamespaceDeclarations& xnd = *(*cit);
		if ( xnd.IsPrefixUsed(prefix) )
			return xnd.GetNamespaceURI(prefix);
	}
	return string();
}

void SoapEnvelopeCleaner::ReplacePrefixesRecursive(const string& oldPrefix, const string& newPrefix, DOMElementPtr& node) {
#ifdef _DEBUG
	const string debugName( node.getNodeName() );
	const string debugLocalName( node.getLocalName() );
#endif

	const string prefix( node.getPrefix() );
	if ( prefix == oldPrefix ) {
		node.setPrefix(newPrefix);
	}

	DOMNamedNodeMapPtr attributes = node->getAttributes();
	const unsigned long nAttr = attributes->getLength();
	for( unsigned long iAttr = 0; iAttr < nAttr; ++iAttr ) {
		DOMAttrPtr attr( attributes.getItem(iAttr) );
		string name, prefix;
		XmlUtils::GetNodeNameAndNamespacePrefix(attr, name, prefix);
		const string namespaceURI(this->GetNamespaceURI(prefix));
		if ( namespaceURI == XsdConstants::SCHEMA_INSTANCE_NAMESPACE_1999 ||
			 namespaceURI == XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001 )
		{
			if ( name == XsdConstants::TYPE_ATTRIBUTE ) {
				const string qName(attr.getNodeValue());
				string localName;
				XmlUtils::GetPrefixAndLocalName(qName, prefix, localName);
				if ( prefix == oldPrefix ) {
					attr.setValue( newPrefix + ":" + localName);
				}
			}
		}
	}

	DOMElementListPtr pChildren = node->getChildNodes();
	const unsigned long nChildren = pChildren.getLength();
	for( unsigned long index = 0; index < nChildren; ++index ) {
		DOMElementPtr child( pChildren.item[index] );
		ReplacePrefixesRecursive(oldPrefix, newPrefix, child);
	}
}

void SoapEnvelopeCleaner::RemoveDuplicateNamespaces(DOMElementPtr& node) {
#ifdef _DEBUG
	const string& debugNode = node.getNodeName();
#endif

	                                                                  
	DOMNamedNodeMapPtr attributes = node->getAttributes();
	const unsigned long nAttr = attributes->getLength();
	for( unsigned long iAttr = 0; iAttr < nAttr; ++iAttr ) {
		const DOMAttrPtr attr( attributes.getItem(iAttr) );
		const string localName( attr.getLocalName() );
		const string prefix( attr.getPrefix() );
		const string value( attr.getNodeValue() );
		if ( prefix == XsdConstants::XMLNS ) {
			                                                    
			string currentPrefix;
			if ( AlreadyDefined(localName, value, currentPrefix) ) {
				if ( localName != currentPrefix ) {
					ReplacePrefixesRecursive(localName, currentPrefix, node);
					node.removeAttributeNode(attr);
				}
			}
		}
	}

	XmlNamespaceDeclarations* p = new XmlNamespaceDeclarations(node);
	if ( p->IsEmpty() )
		delete p;
	else
		m_vNamespaceDeclarations.push_back( p );

	DOMElementListPtr pChildren = node->getChildNodes();
	const unsigned long nChildren = pChildren.getLength();
	for( unsigned long index = 0; index < nChildren; ++index ) {
		DOMElementPtr child = pChildren.item[index];
		RemoveDuplicateNamespaces(child);
	}

	FindUsedNamespacesOneLevel(node);
}

bool SoapEnvelopeCleaner::AlreadyDefined(const string& prefix, const string& namespaceURI, string& currentPrefix) {

	for( NamespacesT::const_iterator cit = m_vNamespaceDeclarations.begin(); cit != m_vNamespaceDeclarations.end(); ++cit )
	{
		const XmlNamespaceDeclarations& xnd = *(*cit);
		if ( xnd.IsNamespaceDeclared(namespaceURI) ) {
			currentPrefix = xnd.GetNamespacePrefix(namespaceURI);
			return true;
		}
	}
	return false;
}
