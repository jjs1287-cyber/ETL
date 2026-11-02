/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XsdConstants.h>
#include <konga/Xml/XmlConstants.h>

#include "XsdFromWsdlUtils.h"
#include "XsdUtils.h"
#include "misc.h"

using namespace std;
using namespace konga;
using namespace konga::xml;
using namespace XsdConstants;

XsdFromWsdlUtils::XsdFromWsdlUtils(void)
{
}

XsdFromWsdlUtils::~XsdFromWsdlUtils(void)
{
}


string XsdInfo::AddNamespaceDeclaration(const string& stNamespaceURI, const string& stPrefixBase)
{
	if( m_namespaces.IsNamespaceDeclared(stNamespaceURI) )
	{
		return m_namespaces.GetNamespacePrefix(stNamespaceURI);
	}
	else
	{
		string stPrefix = m_namespaces.GetNamespacePrefixCreateIfNotExist(stNamespaceURI, stPrefixBase);
		m_namespaces.AddNamespaceDeclaration(stNamespaceURI, stPrefix);
		XmlUtils::AddNamespaceDeclaration(m_pSchemaNode, stNamespaceURI, stPrefix);
		return stPrefix;
	}
}

void XsdInfo::Reset()
{
	m_stSchemaFilename = m_stTargetNamespace = m_stSchemaNamespacePrefix = "";
	m_pSchemaDoc = 0;
	m_pSchemaNode = 0;
	m_schemaVersion = XsdConstants::SchemaVersion_Unknown;
	m_namespaces.Clear();
}

void XsdInfo::SetSchemaNode(DOMElementPtr pSchemaNode)
{ 
	m_pSchemaNode = pSchemaNode; 
	m_namespaces.Load(pSchemaNode);
	m_stTargetNamespace = m_namespaces.GetTargetNamespace();
	if( m_namespaces.GetNamespacePrefix(m_stTargetNamespace).empty() )
	{
		                  
		string stPrefix("tns");
		int nPrefix = 0;
		while( m_namespaces.IsPrefixUsed(stPrefix) )
			stPrefix = "tns" + konga::toString(++nPrefix);
		string stAttribName = "xmlns:" + stPrefix;
		pSchemaNode->setAttribute( stAttribName, m_stTargetNamespace );
		m_namespaces.AddNamespaceDeclaration(m_stTargetNamespace, stPrefix);
	}

	m_schemaVersion = XsdUtils::GetSchemaVersion(m_namespaces);
	string stSchemaDefNamespace = XsdUtils::GetSchemaDefinitionNamespace(m_schemaVersion);
	if( !m_namespaces.IsDefaultNamespace(stSchemaDefNamespace) )
	{
		m_stSchemaNamespacePrefix = m_namespaces.GetNamespacePrefix(stSchemaDefNamespace);
		if( m_stSchemaNamespacePrefix.empty() )
		{
			m_stSchemaNamespacePrefix = "xsd";
			int nIndex = 0;
			while( m_namespaces.IsPrefixUsed(m_stSchemaNamespacePrefix) )
			{
				m_stSchemaNamespacePrefix = "xsd" + konga::toString(++nIndex);
			}
			string stAttribName = "xmlns:" + m_stSchemaNamespacePrefix;
			pSchemaNode->setAttribute( stAttribName, stSchemaDefNamespace );
			m_namespaces.AddNamespaceDeclaration(stSchemaDefNamespace, m_stSchemaNamespacePrefix);
		}
	}

	vector<DOMElementPtr> importNodes;
	XsdUtils::GetImportNodes(pSchemaNode, importNodes);
	for (vector<DOMElementPtr>::const_iterator it = importNodes.begin(); it != importNodes.end(); ++it)
	{
		DOMElementPtr importNode = *it;
		string stNamespace(XmlUtils::GetAttributeValue(importNode, XmlConstants::NAMESPACE));
		string stLocation(XmlUtils::GetAttributeValue(importNode, XsdConstants::SCHEMALOCATION_ATTRIBUTE));
		m_imports[stNamespace] = stLocation;
	}
}

unsigned long XsdInfo::SaveSchema(string& stError) const
{
	try
	{
		unsigned long hr = m_pSchemaDoc->save(m_stSchemaFilename);
		if( FAILED(hr) )
		{
			string stHeader = "Failed to save the schema file '";
			stHeader += m_stSchemaFilename;
			stHeader += "' to disk. ";
			stError = XmlUtils::GetLastError(m_pSchemaDoc, stHeader);
			return hr;
		}
	}
	catch( CrException& e )
	{
		string stHeader = "Failed to save the schema file '";
		stHeader += m_stSchemaFilename;
		stHeader += "' to disk. ";
		stError = stHeader+e.GetErrorString();
        return e.GetErrorNumber();
	}
	return S_OK;
}

bool XsdInfo::ImportsNamespace(const std::string& stNamespace) const
{
	map<string, string>::const_iterator it = m_imports.find(stNamespace);
	return (it != m_imports.end());
}

string XsdInfo::GetImportedSchemaLocation(const string& stImportedNamespace) const
{
	map<string, string>::const_iterator it = m_imports.find(stImportedNamespace);
	return (it == m_imports.end()) ? "" : it->second;
}

void XsdInfo::SetImportedSchemaLocation(const string& stImportedNamespace, const string& stLocation)
{
	m_imports[stImportedNamespace] = stLocation;
}


WsdlXsdCollection::~WsdlXsdCollection()
{
	for( map<string, XsdInfo*>::iterator it = m_mapFilenameToXsdInfo.begin();
			it != m_mapFilenameToXsdInfo.end(); ++it )
	{
		delete it->second;
	}
	m_mapFilenameToXsdInfo.clear();

	                                                                                              
	m_mapTargetNamespaceToXsdInfo.clear();
}


void WsdlXsdCollection::AddXsdInfo(XsdInfo* pXsdInfo)
{
	string stFilename = pXsdInfo->GetSchemaFilename();
	map<string, XsdInfo*>::iterator it = m_mapFilenameToXsdInfo.find(stFilename);
	if( it != m_mapFilenameToXsdInfo.end() )
		delete it->second;
	m_mapFilenameToXsdInfo[stFilename] = pXsdInfo;

	string stTargetNamespace = pXsdInfo->GetTargetNamespace();
	map<string, XsdInfoVector>::iterator it2 = m_mapTargetNamespaceToXsdInfo.find(stTargetNamespace);
	if( it2 == m_mapTargetNamespaceToXsdInfo.end() )
	{
		XsdInfoVector v;
		v.push_back(pXsdInfo);
		m_mapTargetNamespaceToXsdInfo[stTargetNamespace] = v;
	}
	else
	{
		XsdInfoVector v = it2->second;
		bool bAlreadyAdded = false;
		for(size_t i = 0; i < v.size(); ++i )
		{
			if( pXsdInfo == v[i] )
			{
				delete v[i];
				v[i] = pXsdInfo;
				bAlreadyAdded = true;
				break;
			}
		}
		if( !bAlreadyAdded )
			v.push_back(pXsdInfo);
	}
}

XsdInfo* WsdlXsdCollection::GetXsdInfo(const string& stFilename) const
{
	map<string, XsdInfo*>::const_iterator it = m_mapFilenameToXsdInfo.find(stFilename);
	return ( it == m_mapFilenameToXsdInfo.end() ) ? 0 : it->second;
}

void WsdlXsdCollection::GetXsdInfo(const string& stTargetNamespace, XsdInfoVector& vXsdInfo) const
{
	map<string, XsdInfoVector>::const_iterator it = m_mapTargetNamespaceToXsdInfo.find(stTargetNamespace);
	if( it != m_mapTargetNamespaceToXsdInfo.end() )
	{
		XsdInfoVector v = it->second;
		for(size_t i = 0; i < v.size(); ++i )
			vXsdInfo.push_back(v[i]);
	}
}

XsdInfo* WsdlXsdCollection::GetXsdInfo(size_t i) const
{
	if( i >= 0 && i < Size() )
	{
		size_t n = 0;
		for( map<string, XsdInfo*>::const_iterator cit = m_mapFilenameToXsdInfo.begin();
				cit != m_mapFilenameToXsdInfo.end(); ++cit, ++n )
		{
			if( n == i )
				return cit->second;
		}
	}
	return 0;
}
