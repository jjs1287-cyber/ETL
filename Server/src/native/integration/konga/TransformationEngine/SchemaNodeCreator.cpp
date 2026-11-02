/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>

#include "misc.h"
#include "XsdUtils.h"

using namespace konga::xml;
using namespace std;

                                          
  

DOMElementPtr XsdUtils::SchemaNodeCreator::CreateSchemaElementNode(const string& stName, DOMElementPtr pParent        )
{
	try
	{
		DOMElementPtr p = XsdUtils::CreateSchemaElementNode(m_pSchemaDoc, m_schemaVersion, stName, m_stSchemaNamespacePrefix);
		if( pParent != 0 )
			pParent->appendChild(p);
		return p;
	}
	catch( const konga::CrException& e )
	{
		m_stLastError += e.GetErrorString()+"\n";
		return 0;
	}
}

                                                                                                
DOMElementPtr XsdUtils::SchemaNodeCreator::CreateSchemaComplexTypeNode(const string& stTypeName, DOMElementPtr pParent        )
{
	try
	{
		DOMElementPtr p = XsdUtils::CreateSchemaComplexTypeNode(m_pSchemaDoc, m_schemaVersion, stTypeName, m_stSchemaNamespacePrefix);
		if( pParent != 0 )
			pParent->appendChild(p);
		return p;
	}
	catch( const konga::CrException& e )
	{
		m_stLastError += e.GetErrorString()+"\n";
		return 0;
	}
}

DOMElementPtr XsdUtils::SchemaNodeCreator::CreateSchemaSequenceNode(DOMElementPtr pParent        )
{
	try
	{
		DOMElementPtr p = XsdUtils::CreateSchemaSequenceNode(m_pSchemaDoc, m_schemaVersion, m_stSchemaNamespacePrefix);
		if( pParent != 0 )
			pParent->appendChild(p);
		return p;
	}
	catch( const konga::CrException& e )
	{
		m_stLastError += e.GetErrorString()+"\n";
		return 0;
	}
}

DOMElementPtr XsdUtils::SchemaNodeCreator::CreateSchemaImportNode(const string& stImportNamespace, 
																   const string& stSchemaLocation)
{
	try
	{
		return XsdUtils::CreateSchemaImportNode(m_pSchemaDoc, m_schemaVersion, stImportNamespace, stSchemaLocation, m_stSchemaNamespacePrefix);
	}
	catch( const konga::CrException& e )
	{
		m_stLastError += e.GetErrorString()+"\n";
		return 0;
	}
}

DOMElementPtr XsdUtils::SchemaNodeCreator::CreateSchemaIncludeNode(const string& stSchemaLocation)
{
	try
	{
		return XsdUtils::CreateSchemaIncludeNode(m_pSchemaDoc, m_schemaVersion, stSchemaLocation, m_stSchemaNamespacePrefix);
	}
	catch( const konga::CrException& e )
	{
		m_stLastError += e.GetErrorString()+"\n";
		return 0;
	}
}

DOMElementPtr XsdUtils::SchemaNodeCreator::CreateComplexTypeSequenceNode(DOMElementPtr pParent        )
{
	try
	{
		DOMElementPtr p = XsdUtils::CreateComplexTypeSequenceNode(m_pSchemaDoc, m_schemaVersion, m_stSchemaNamespacePrefix);
		if( pParent != 0 )
			pParent->appendChild(p);
		return p;
	}
	catch( const konga::CrException& e )
	{
		m_stLastError += e.GetErrorString()+"\n";
		return 0;
	}
}

                                                                        
unsigned long XsdUtils::SchemaNodeCreator::InsertImportNode(const string& stImportedNamespace, const string& stSchemaLocation)
{
	try
	{
		return XsdUtils::InsertImportNode(m_pSchemaDoc, 
										  m_pSchemaNode, 
										  m_schemaVersion, 
										  m_stSchemaNamespacePrefix, 
										  stImportedNamespace, 
										  stSchemaLocation,
										  m_stLastError);
	}
	catch( const konga::CrException& e )
	{
		m_stLastError += e.GetErrorString()+"\n";
		return E_FAIL;
	}
}

                                                                        
unsigned long XsdUtils::SchemaNodeCreator::InsertIncludeNode(const string& stSchemaLocation, string& stError)
{
	try
	{
		return XsdUtils::InsertIncludeNode(m_pSchemaDoc, 
										   m_pSchemaNode, 
										   m_schemaVersion, 
										   m_stSchemaNamespacePrefix, 
										   stSchemaLocation,
										   m_stLastError);
	}
	catch( const konga::CrException& e )
	{
		m_stLastError += e.GetErrorString()+"\n";
		return E_FAIL;
	}
}


