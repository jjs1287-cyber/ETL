/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef SCHEMACOLLECTION_H
#define SCHEMACOLLECTION_H

#include <string>
#include <vector>
#include <map>

#include <konga/Xml/DOMElementPtr.h>

#include "XsdUtils.h"

class XSDParser;
class CROM;

namespace konga {
	namespace xml {
		class ExpandedName;
	}
}

typedef std::vector<XSDParser*> ParserVector;
typedef std::map<std::string,std::vector<std::string>*> SubstitutionGroupMap;

class SchemaCollection
{
public:
	SchemaCollection(void) {};
	~SchemaCollection(void);

	bool Contains(const XsdUtils::SchemaIdentifier& id);
	bool Contains(const std::string& stTargetNamespace, const std::string& stSchemaLocation);
	void AddSchema(XSDParser* pParser, bool bTransferOwnership);
	void GetSchemaParsers(std::vector<XSDParser*>&);
	void GetSchemaParsers(const std::string stTargetNamespace, std::vector<XSDParser*>& vSchemaParsers);

	CROM* GetElementDefinition(const konga::xml::ExpandedName& expandedName);
	CROM* GetElementDefinition(const std::string& stNamespace,
							   const std::string& stLocalName,
							   XSDParser* caller);
	
	konga::xml::DOMElementPtr GetElementDefinitionDOMNode(const std::string& stNamespace,
														 const std::string& stLocalName,
														 XSDParser* pSourceParser = 0);
	konga::xml::DOMElementPtr GetElementDefinitionDOMNode(const konga::xml::ExpandedName& expandedName,
														  XSDParser* pSourceParser = 0);

	CROM* GetGlobalAttributeCROM(const std::string& stNamespace,
								 const std::string& stLocalName,
								 XSDParser* pSourceParser = 0);

	CROM* GetAttributeDefinitionCROMNode(const std::string& stNamespace,
										 const std::string& stLocalName,
										 XSDParser* pSourceParser = 0);

	konga::xml::DOMElementPtr GetAttributeDefinitionDOMNode(const std::string& stNamespace,
														   const std::string& stLocalName,
														   XSDParser* pSourceParser = 0);

	CROM* GetGroupDefinition(const std::string& stNamespace,
							 const std::string& stLocalName,
							 XSDParser* caller);

	CROM* GetAttributeGroupDefinition(const std::string& stNamespace,
									  const std::string& stLocalName,
									  XSDParser* caller);

	CROM* GetSimpleTypeDefinition(const std::string& stNamespace,
								  const std::string& stLocalName,
								  XSDParser* caller);

	CROM* GetComplexTypeDefinition(const konga::xml::ExpandedName& expandedName);
	CROM* GetComplexTypeDefinition(const std::string& stNamespace,
								   const std::string& stLocalName,
								   XSDParser* caller);

	konga::xml::DOMElementPtr GetComplexTypeDOMNode(const std::string& stNamespace, 
												   const std::string& stLocalName,
												   XSDParser* pSourceParser = 0);

	bool ParseGlobalComplexTypes();

	void GetPossibleRoots(const std::string& stTargetNamespace, std::vector<std::string>& vRootNames);
	void addSubstitutionGroup(const std::string& stSubstitutionGroupUN,const std::string& stUniversalName);
	std::vector<std::string>* getSubstitutionGroup(const std::string& stSubstitutionGroupUN);

private:
	ParserVector	m_vOwnedParsers;
	std::map<XsdUtils::SchemaIdentifier, XSDParser*>	m_mapIdentifierToParser;
	std::map<std::string, ParserVector>					m_mapTargetNamespaceToParsers;
	SubstitutionGroupMap m_mapSubstitutionGroup;
};

#endif                      
