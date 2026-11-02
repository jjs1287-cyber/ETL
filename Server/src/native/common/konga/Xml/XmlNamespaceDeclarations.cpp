/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/Utils/StringUtils.h>

#include "XmlConstants.h"
#include "XsdConstants.h"
#include "DOMElementPtr.h"
#include "DOMNamedNodeMapPtr.h"
#include "XmlUtils.h"
#include "XmlNamespaceDeclarations.h"

using namespace std;
using namespace konga;
using namespace konga::xml;

XmlNamespaceDeclarations::XmlNamespaceDeclarations(const DOMElementPtr& pRootElement)
{
	Load(pRootElement);
}

XmlNamespaceDeclarations::~XmlNamespaceDeclarations(void)
{
}

void XmlNamespaceDeclarations::Load(const DOMElementPtr& pRootElement)
{
	if( pRootElement != 0 )
	{
		string stNodeNamespace(XmlUtils::GetNodeNamespaceURI(pRootElement));
		string stNodeNamespacePrefix(XmlUtils::GetNodePrefix(pRootElement));
		m_mapPrefixToUri[stNodeNamespacePrefix] = stNodeNamespace;
		MapUriToPrefix(stNodeNamespace, stNodeNamespacePrefix);

		DOMNamedNodeMapPtr pAttributes = pRootElement->getAttributes();
		if( pAttributes != 0 ) {
			for( int i = 0, n = pAttributes->getLength(); i < n; ++i )
			{
				DOMNodePtr pa = pAttributes->item[i];
				string stAttributeName, stAttributePrefix;
				XmlUtils::GetNodeNameAndNamespacePrefix(pa, stAttributeName, stAttributePrefix);
				if( stricmp(stAttributeName.c_str(), XsdConstants::XMLNS.c_str() ) == 0 )
				{
					                                
					m_stDefaultNamespace = pa->getNodeValue();
					m_mapPrefixToUri[""] = m_stDefaultNamespace;
					MapUriToPrefix(m_stDefaultNamespace, "");
				}
				else if( stricmp(stAttributePrefix.c_str(), XsdConstants::XMLNS.c_str()) == 0 )
				{
					                                                                             
					                                                   
					string stUri = pa->getNodeValue();
					m_mapPrefixToUri[stAttributeName] = stUri;
					MapUriToPrefix(stUri, stAttributeName);
				}
				else if( stricmp(stAttributeName.c_str(), XmlConstants::TARGET_NAMESPACE.c_str()) == 0 )
				{
					                                
					m_stTargetNamespace = pa->getNodeValue();
				}
			}
		}
	}
}

void XmlNamespaceDeclarations::Clear()
{
	m_stDefaultNamespace.clear();
	m_stTargetNamespace.clear();
	m_mapPrefixToUri.clear();
	m_mapUriToPrefix.clear();
}

bool XmlNamespaceDeclarations::IsEmpty() const {
	return m_mapPrefixToUri.empty();
}

void XmlNamespaceDeclarations::AddNamespaceDeclaration(const string& stURI, const string& stPrefix)
{
	m_mapPrefixToUri[stPrefix] = stURI;
	MapUriToPrefix(stURI, stPrefix);
	if( stPrefix.empty() )
		m_stDefaultNamespace = stPrefix;
}

string XmlNamespaceDeclarations::GetNamespaceURI(const string& stPrefix) const
{
	map<string, string>::const_iterator cit = m_mapPrefixToUri.find(stPrefix);
	if (cit != m_mapPrefixToUri.end()) {
		return cit->second;
	}

	                                                                                                                          
	if (strcmp(XmlConstants::XML_NAMESPACE_1998_PREFIX.c_str(), stPrefix.c_str()) == 0) {
		return XmlConstants::XML_NAMESPACE_1998;
	}
	return "";
}

string XmlNamespaceDeclarations::GetNamespacePrefix(const string& stURI) const
{
	map<string, StringVector>::const_iterator cit = m_mapUriToPrefix.find(stURI);
	if( cit != m_mapUriToPrefix.end() )
	{
		const StringVector& v = cit->second;
		for( StringVector::const_iterator cit2 = v.begin(); cit2 != v.end(); ++cit2 )
		{
			if( (*cit2).length() > 0 )
				return *cit2;
		}
	}
	return "";
}

void XmlNamespaceDeclarations::GetNamespacePrefixes(const string& stURI, StringVector& vstPrefixes) const
{
	GetNamespacePrefixesImpl(stURI, vstPrefixes, false);
}

void XmlNamespaceDeclarations::GetNamespaceURIs(StringVector& vstURIs) const
{
	for( map<string, StringVector>::const_iterator cit = m_mapUriToPrefix.begin();
		cit != m_mapUriToPrefix.end(); ++cit )
	{
		vstURIs.push_back(cit->first);
	}
}

string XmlNamespaceDeclarations::GetNamespacePrefixCreateIfNotExist(const string& stURI, const string& stPrefixBase) const
{
	if ( stURI == XmlConstants::XML_NAMESPACE_1998 )
		return XmlConstants::XML_NAMESPACE_1998_PREFIX;

	string stPrefix(stPrefixBase);
	int nIndex = 0;
	while( IsPrefixUsed(stPrefix) )
	{
		if( strcmp( GetNamespaceURI(stPrefix).c_str(), stURI.c_str() ) == 0 )
			break;
		stPrefix = stPrefixBase + toString(++nIndex);
	}
	return stPrefix;
}


void XmlNamespaceDeclarations::MapUriToPrefix(const string& stUri, const string& stPrefix)
{
	map<string, StringVector>::iterator it = m_mapUriToPrefix.find(stUri);
	if( it == m_mapUriToPrefix.end() )
	{
		StringVector v;
		v.push_back(stPrefix);
		m_mapUriToPrefix[stUri] = v;
	}
	else
	{
		it->second.push_back(stPrefix);
	}
}

string XmlNamespaceDeclarations::ToString() const
{
	string st("Default Namespace: " + m_stDefaultNamespace);
	st += "\n";
	st += "Target Namespace: " + m_stTargetNamespace;
	st += "\n";
	for( map<string, string>::const_iterator it = m_mapPrefixToUri.begin(); it != m_mapPrefixToUri.end(); ++it )
	{
		const string prefix(it->first);
		if( !prefix.empty() )
		{
			st += prefix;
			st += ": ";
			st += it->second;
			st += "\n";
		}
	}
	return st;
}

bool XmlNamespaceDeclarations::IsExistingNamespaceMapping(const string& stPrefix, const string& stUri) const {
	StringVector prefixes;
	GetNamespacePrefixesImpl(stUri, prefixes, true);
	for (StringVector::const_iterator cit = prefixes.begin(); cit != prefixes.end(); ++cit) {
		if (konga::Equals(stPrefix, *cit)) {
			return true;
		}
	}
	return false;
}

void XmlNamespaceDeclarations::GetNamespacePrefixesImpl(const std::string& stURI, StringVector& vstPrefixes, bool acceptEmptyPrefixes) const {
	map<string, StringVector>::const_iterator cit = m_mapUriToPrefix.find(stURI);
	if (cit != m_mapUriToPrefix.end()) {
		StringVector v = cit->second;
		for (StringVector::const_iterator cit2 = v.begin(); cit2 != v.end(); ++cit2) {
			if (acceptEmptyPrefixes || ((*cit2).length() > 0)) {
				vstPrefixes.push_back(*cit2);
			}
		}
	}
}
