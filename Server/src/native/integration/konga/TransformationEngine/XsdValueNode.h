/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDVALUENODE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDVALUENODE_H

#include <string>

#include "XsdNodeWithCardinalityConstraints.h"

                                                                                     

class XsdValueNode :
	public XsdNodeWithCardinalityConstraints
{
public:
	XsdValueNode(XSDParser& xsdParser, 
				 const konga::xml::DOMElementPtr& pDOMNode, 
				 CROM* pCROMParent, 
				 CROM* pPreviousSibling,
				 XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdValueNode(void) {};

	virtual unsigned long Parse(std::string& stError) = 0;

protected:
	void MarkAsAnyType();
	std::string	GetType() const { 
		return m_stType; 
	}
	std::string	GetReference() const { 
		return m_stReference; 
	}
	std::string	GetDefaultValue() const { 
		return m_stDefaultValue; 
	}
	bool	HasDefaultValue() const { 
		return !m_stDefaultValue.empty(); 
	}
	bool	HasFixedValue() const { 
		return !m_stFixedValue.empty(); 
	}
	std::string	GetFixedValue() const { 
		return m_stFixedValue; 
	}
	bool	HasType() const { 
		return !m_stType.empty(); 
	}
	bool	HasReference() const { 
		return !m_stReference.empty(); 
	}

private:
	std::string	m_stType;
	std::string	m_stReference;
	std::string	m_stDefaultValue;
	std::string	m_stFixedValue;

};

#endif                                 

