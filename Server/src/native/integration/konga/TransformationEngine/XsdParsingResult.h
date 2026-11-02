/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDPARSINGRESULT_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDPARSINGRESULT_H

#include <map>
#include <string>

class CROM;
class XSDParser;

class XsdParsingResult {

public:
	XsdParsingResult(const XSDParser& parser) : m_hasValueContent(false), m_parser(parser) {}
	virtual ~XsdParsingResult() {}
	void Clear();
	void Build();
	void WriteXml(const std::string& stOutputFilePath) const;
	void GetXml(std::string& receiver) const;
	bool HasValueContent() const {
		return m_hasValueContent;
	}

	                                                           
	                                                              
	                                                                   
	                                 
	                                             
	CROM* GetRoot() const;

	typedef std::map<std::string, CROM*> NodeMap;

	                                                             
	                         
	                                                             
	                               
	const NodeMap& GetComplexTypes() const {
		return m_referencedTypes;
	}
	                                                                   
	                                  
	                                                             
	                               
	const NodeMap& GetSubstitutionGroupElements() const {
		return m_substitutionElements;
	}

private:
	bool					m_hasValueContent;
	const XSDParser&		m_parser;
	std::map<CROM*, bool>	m_processedNodes;
	NodeMap					m_referencedTypes;
	NodeMap					m_substitutionElements;

	void HandleArrays();
	void CollectReferences(CROM* node);
	void HandleSubstitutionGroups(CROM* node);
	void CheckEmptySubstitutionElement(CROM* head, CROM* sub);
	void HandleComplexTypes(CROM* node);
	void HandleDerivedTypes(CROM* baseType);
	bool IsAlreadyProcessed(CROM* node);
	bool HasNodeBeenProcessed(CROM* node) const;
	bool HasTypeBeenProcessed(const std::string& typeName) const;
	void ClearComplexTypes() const;
};

#endif                                                     

