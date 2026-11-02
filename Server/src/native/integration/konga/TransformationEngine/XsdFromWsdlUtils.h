/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef XSDFROMWSDLUTILS_H
#define XSDFROMWSDLUTILS_H

#include <string>
#include <vector>
#include <map>

#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/XsdConstants.h>

class XsdInfo
{
public:
	XsdInfo() : m_pSchemaDoc(0), m_pSchemaNode(0) {};
	XsdInfo(konga::xml::DOMDocument2Ptr pSchemaDoc, konga::xml::DOMElementPtr pSchemaNode) : 
			m_pSchemaDoc(pSchemaDoc) 
	{
		SetSchemaNode(pSchemaNode);
	}
	~XsdInfo() {};

	void	Reset();
	std::string	GetSchemaFilename() const { return m_stSchemaFilename; }
	void	SetSchemaFilename(const std::string& stFilename) { m_stSchemaFilename = stFilename; }
	std::string	GetTargetNamespace() const { return m_stTargetNamespace; }
	konga::xml::DOMDocument2Ptr	GetSchemaDocument() const { return m_pSchemaDoc; }
	void	SetSchemaDocument(konga::xml::DOMDocument2Ptr pDoc) { m_pSchemaDoc = pDoc; }
	konga::xml::DOMElementPtr	GetSchemaNode() const { return m_pSchemaNode; }
	void	SetSchemaNode(konga::xml::DOMElementPtr pSchemaNode);
	std::string	GetSchemaNamespacePrefix() const { return m_stSchemaNamespacePrefix; }
	XsdConstants::SchemaVersion	GetSchemaVersion() const { return m_schemaVersion; }
	unsigned long	SaveSchema(std::string& stError) const;

	                                                                                                    
	                                                                                                         
	                                            
	                                                                                                       
	std::string GetNamespacePrefix(const std::string& uri) const { return m_namespaces.GetNamespacePrefix(uri); }

	                                                                                                  
	                                                    
	std::string	GetNamespaceURI(const std::string& stPrefix) const { return m_namespaces.GetNamespaceURI(stPrefix); }

	std::string	AddNamespaceDeclaration(const std::string& stNamespaceURI, const std::string& stPrefixBase);

	std::string GetImportedSchemaLocation(const std::string& stImportedNamespace) const;
	bool ImportsNamespace(const std::string& stNamespace) const;
	void SetImportedSchemaLocation(const std::string& stImportedNamespace, const std::string& stLocation);

private:
	std::string	m_stSchemaFilename;
	std::string	m_stTargetNamespace;

	konga::xml::DOMDocument2Ptr	m_pSchemaDoc;
	konga::xml::DOMElementPtr	m_pSchemaNode;

	konga::xml::XmlNamespaceDeclarations	m_namespaces;
	XsdConstants::SchemaVersion				m_schemaVersion;
	std::string								m_stSchemaNamespacePrefix;
	                                             
	std::map<std::string, std::string>		m_imports;
};

typedef std::vector<XsdInfo*>	XsdInfoVector;

class WsdlXsdCollection
{
public:
	WsdlXsdCollection() {};
	~WsdlXsdCollection();

	void	AddXsdInfo(XsdInfo* pXsdInfo);
	XsdInfo*GetXsdInfo(const std::string& stFilename) const;
	void	GetXsdInfo(const std::string& stTargetNamespace, XsdInfoVector& vXsdInfo) const;
	size_t		Size() const { return m_mapFilenameToXsdInfo.size(); }
	XsdInfo*GetXsdInfo(size_t i) const;

private:
	std::map<std::string, XsdInfoVector>	m_mapTargetNamespaceToXsdInfo;
	std::map<std::string, XsdInfo*>			m_mapFilenameToXsdInfo;
};

enum InputOutputEnum { WS_INPUT, WS_OUTPUT };

enum PurposeEnum { TRANSFORMATION, HOSTED_WEBSERVICE, DISPLAY };

struct StructureMode
{
public:
	InputOutputEnum	IO;
	PurposeEnum		Purpose;

	bool	CreateTransactionNode() const { return Purpose == TRANSFORMATION || Purpose == HOSTED_WEBSERVICE; }
	bool	CreateRequestResponseNodes() const { return IO == WS_OUTPUT && Purpose == TRANSFORMATION; }
};

class XsdFromWsdlUtils
{
public:
	XsdFromWsdlUtils(void);
	~XsdFromWsdlUtils(void);
};

#endif                                     
