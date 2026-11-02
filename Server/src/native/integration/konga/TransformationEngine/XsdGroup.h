/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDGROUP_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDGROUP_H

#include <string>

#include "XsdNode.h"

class CROM;
class TreeMapper;

class XsdGroup : public XsdNode
{
	TreeMapper*m_pTreeMapper;
	TreeMapper*getTreeMapper() const {
		return m_pTreeMapper;
	}
public:
	XsdGroup(XSDParser& xsdParser, 
			 const konga::xml::DOMElementPtr pDOMNode, 
			 CROM* pCROMParent, 
			 CROM* pPreviousSibling,
			 XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdGroup(void);

	virtual unsigned long Parse(std::string& stError);
	virtual std::string	GetNamespace() const;

protected:
	virtual CROM*	GetGroupDefinition(const std::string& stQualifiedName, std::string& stError) const;
	std::string		GetReference() const { return m_stReference; }
	bool			HasReference() const { return !m_stReference.empty(); }

private:
	std::string	m_stReference;
	int	m_iMinUse;
	int	m_iMaxUse;

	unsigned long ParseGlobalGroup(const std::string& stName, std::string& stError);
	unsigned long ParseLocalGroup(std::string& stError);
	bool IsGroupRootRequired(CROM* pParent);
	void InsertGroupRoot(CROM*& pParent, CROM*& pPrevSibling);
	void CloneChildren(CROM* pGroupDef, CROM* pParent, CROM* pPrevSibling);
};

#endif

