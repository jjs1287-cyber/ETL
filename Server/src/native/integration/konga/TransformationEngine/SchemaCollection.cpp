/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <iostream>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/ExpandedName.h>

#include "XsdUtils.h"
#include "XSDParser.h"
#include "SchemaCollection.h"

using namespace std;
using namespace konga::xml;

SchemaCollection::~SchemaCollection(void)
{
	for( int i = 0, iMax = (int)m_vOwnedParsers.size(); i < iMax; ++i )
		delete m_vOwnedParsers[i];
	for (SubstitutionGroupMap::iterator it=m_mapSubstitutionGroup.begin();it!=m_mapSubstitutionGroup.end();it++){
		delete it->second;
	}
}

bool SchemaCollection::Contains(const XsdUtils::SchemaIdentifier& id)
{
	return m_mapIdentifierToParser.find(id) != m_mapIdentifierToParser.end();
}

bool SchemaCollection::Contains(const string& stTargetNamespace, const string& stSchemaLocation)
{
	XsdUtils::SchemaIdentifier id(stTargetNamespace, stSchemaLocation);
	return Contains(id);
}

void SchemaCollection::AddSchema(XSDParser* parser, bool bTransferOwnership)
{
	string stTargetNamespace = parser->GetTargetNamespace();
	string stSchemaLocation = parser->GetSchemaFileName(); 

	XsdUtils::SchemaIdentifier id(stTargetNamespace, stSchemaLocation);
	if ( this->Contains(id) )
		return;

	m_mapIdentifierToParser[id] = parser;

	if( bTransferOwnership )
		m_vOwnedParsers.push_back(parser);

	map<string, ParserVector>::iterator it = m_mapTargetNamespaceToParsers.find(stTargetNamespace);
	if( it == m_mapTargetNamespaceToParsers.end() )
	{
		ParserVector v;
		v.push_back(parser);
		m_mapTargetNamespaceToParsers[stTargetNamespace] = v;
	}
	else
	{
		it->second.push_back(parser);
	}
	parser->m_pSchemaCollection=this;
}

void SchemaCollection::GetSchemaParsers(vector<XSDParser*>& vSchemaParsers)
{
	for( map<XsdUtils::SchemaIdentifier, XSDParser*>::const_iterator cit = m_mapIdentifierToParser.begin();
			cit != m_mapIdentifierToParser.end(); ++cit )
	{
		vSchemaParsers.push_back(cit->second);
	}
}

void SchemaCollection::GetSchemaParsers(const string stTargetNamespace, vector<XSDParser*>& vSchemaParsers)
{
	map<string, ParserVector>::const_iterator cit = m_mapTargetNamespaceToParsers.find(stTargetNamespace);
	if( cit != m_mapTargetNamespaceToParsers.end() )
	{
		for( int i = 0, iMax = (int)cit->second.size(); i < iMax; ++i )
			vSchemaParsers.push_back(cit->second[i]);
	}
}


DOMElementPtr SchemaCollection::GetElementDefinitionDOMNode(const string& stNamespace,
															const string& stLocalName,
															XSDParser* pSourceParser         )
{
	                                                                                       
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for( int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i )
	{
		if( pSourceParser != vParsers[i] )
		{
			DOMElementPtr p = vParsers[i]->LookupElementDefinitionDOMNodeInMap(stUniversalName);
			if( p != 0 )
				return p;
		}
	}
	return 0;
}
DOMElementPtr SchemaCollection::GetElementDefinitionDOMNode(const ExpandedName& expandedName,
														    XSDParser* pSourceParser         )
{
	string stNamespace = expandedName.uri();
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for( int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i )
	{
		if( pSourceParser != vParsers[i] )
		{
			DOMElementPtr p = vParsers[i]->LookupElementDefinitionDOMNodeInMap(expandedName);
			if( p != 0 )
				return p;
		}
	}
	return 0;
}

CROM* SchemaCollection::GetGlobalAttributeCROM(const string& stNamespace,
											   const string& stLocalName,
											   XSDParser* pSourceParser) {
	                                                                                   
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for (int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i) {
		if (pSourceParser != vParsers[i]) {
			CROM* c = vParsers[i]->GetAttributeDefinition(stNamespace, stLocalName, false);
			if (c != 0) {
				return c;
			}
		}
	}
	return 0;	
}


CROM* SchemaCollection::GetAttributeDefinitionCROMNode(const string& stNamespace,
													   const string& stLocalName,
													   XSDParser* pSourceParser         )
{
	                                                                                   
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for( int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i )
	{
		if( pSourceParser != vParsers[i] )
		{
			CROM* c = vParsers[i]->LookupAttributeDefinitionCROMNodeInMap(stUniversalName);
			if( c != 0 )
				return c;
		}
	}
	return 0;
}

DOMElementPtr SchemaCollection::GetAttributeDefinitionDOMNode(const string& stNamespace,
																		 const string& stLocalName,
																		 XSDParser* pSourceParser         )
{
	                                                                                       
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for( int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i )
	{
		if( pSourceParser != vParsers[i] )
		{
			DOMElementPtr p = vParsers[i]->LookupAttributeDefinitionDOMNodeInMap(stUniversalName);
			if( p != 0 )
				return p;
		}
	}
	return 0;
}

CROM* SchemaCollection::GetElementDefinition(const ExpandedName& expandedName) {
	return GetElementDefinition(expandedName.uri(), expandedName.local(), 0);
}

CROM* SchemaCollection::GetElementDefinition(const string& stNamespace,
											 const string& stLocalName,
											 XSDParser* caller)
{
	                                                                                       
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for (int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i) {
		XSDParser* p = vParsers[i];
		if (p == caller) {
			continue;
		}
		CROM* c = p->GetElementDefinition(stNamespace, stLocalName, false);
		if (c != 0) {
			return c;
		}
	}
	return 0;
}

CROM* SchemaCollection::GetGroupDefinition(const string& stNamespace,
										   const string& stLocalName,
										   XSDParser* caller)
{
	                                                                                     
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for (int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i) {
		XSDParser* p = vParsers[i];
		if (p == caller) {
			continue;
		}
		CROM* c = p->GetGroupDefinition(stNamespace, stLocalName, false);
		if (c != 0) {
			return c;
		}
	}
	return 0;
}

CROM* SchemaCollection::GetAttributeGroupDefinition(const string& stNamespace,
													const string& stLocalName,
													XSDParser* caller)
{
	                                                                                     
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for (int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i) {
		XSDParser* p = vParsers[i];
		if (p == caller) {
			continue;
		}
		CROM* c = p->GetAttributeGroupDefinition(stNamespace, stLocalName, false);
		if (c != 0) {
			return c;
		}
	}
	return 0;
}

CROM* SchemaCollection::GetSimpleTypeDefinition(const string& stNamespace,
												const string& stLocalName,
												XSDParser* caller)
{
	                                                                                       
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for (int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i) {
		XSDParser* p = vParsers[i];
		if (p == caller) {
			continue;
		}
		CROM* c = p->GetSimpleTypeDefinition(stNamespace, stLocalName, false);
		if (c != 0) {
			return c;
		}
	}
	return 0;
}

CROM* SchemaCollection::GetComplexTypeDefinition(const ExpandedName& expandedName) {
	return GetComplexTypeDefinition(expandedName.uri(), expandedName.local(), 0);
}

CROM* SchemaCollection::GetComplexTypeDefinition(const string& stNamespace, 
												 const string& stLocalName,
												 XSDParser* caller)
{
		                                                                                       
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for (int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i) {
		XSDParser* p = vParsers[i];
		if (p == caller) {
			continue;
		}
		CROM* c = p->GetComplexTypeDefinition(stNamespace, stLocalName, false);
		if (c != 0) {
			return c;
		}
	}
	return 0;
}

DOMElementPtr SchemaCollection::GetComplexTypeDOMNode(const string& stNamespace,
																 const string& stLocalName,
																 XSDParser* pSourceParser         )
{
	                                                                                    
	string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespace, stLocalName);
	ParserVector vParsers;
	GetSchemaParsers(stNamespace, vParsers);
	for( int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i )
	{
		if( pSourceParser != vParsers[i] )
		{
			DOMElementPtr p = vParsers[i]->LookupComplexTypeDOMNodeInMap(stUniversalName);
			if( p != 0 )
				return p;
		}
	}
	return 0;
}

void SchemaCollection::GetPossibleRoots(const string& stTargetNamespace, vector<string>& vRootNames)
{
	ParserVector vParsers;
	GetSchemaParsers(vParsers);
	for( int i = 0, iMax = (int)vParsers.size(); i < iMax; ++i )
	{
		vParsers[i]->GetPossibleRoots(vRootNames);
	}
}

bool SchemaCollection::ParseGlobalComplexTypes()
{
	ParserVector parsers;
	GetSchemaParsers(parsers);
	for (ParserVector::iterator it = parsers.begin(); it != parsers.end(); ++it)
	{
		unsigned long ret = (*it)->ParseGlobalComplexTypes();
		if (ret < 0)
		{
			return false;
		}
	}
	return true;
}

void SchemaCollection::addSubstitutionGroup(const std::string& stSubstitutionGroupHeader,const std::string& stSubstitutionGroup)
{
	SubstitutionGroupMap::iterator it=m_mapSubstitutionGroup.find(stSubstitutionGroupHeader);
	std::vector<std::string>*v;
	if(it==m_mapSubstitutionGroup.end()){
		v=new std::vector<std::string>();
		m_mapSubstitutionGroup[stSubstitutionGroupHeader]=v;
		v->push_back(stSubstitutionGroupHeader);
	}
	else {
		v=it->second;
	}
	v->push_back(stSubstitutionGroup);
}

std::vector<std::string>*SchemaCollection::getSubstitutionGroup(const std::string& stSubstitutionGroupUN)
{
	SubstitutionGroupMap::iterator it=m_mapSubstitutionGroup.find(stSubstitutionGroupUN);
	
	if( it==m_mapSubstitutionGroup.end())return 0;
	else return it->second;
}

