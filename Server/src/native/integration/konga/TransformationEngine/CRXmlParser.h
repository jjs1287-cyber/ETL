/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef __CRXMLPARSER_H_
#define __CRXMLPARSER_H_

#include <cassert>

#include <konga/Xml/DOMNodePtr.h>
#include <konga/Xml/DOMNodeListPtr.h>

#include "resource.h"                      
#include "DTDParser.h"
#include "CRXmlData.h"
#define CRXml_bit_Unknown		0x0
#define CRXml_bit_Source		0x1
#define CRXml_bit_Target		0x2
#define CRXml_bit_DTD			0x4
#define CRXml_bit_XSD			0x8
#define CRXml_bit_WebService	0x10

namespace konga
{
	class JbNode;
}

class TreeMapper;
                                                                             
              
class CRXmlParser 
{
	TreeMapper*m_pTreeMapper;
public:                         
	konga::xml::DOMDocumentPtr m_poDoc;
	KString m_sDTDFileName;
	KString m_sDTDSystem;
	KString  m_LastErr;
	DTDParser*m_pDTDParser;
	CRXmlData*m_rootXmlData;
	std::string m_sXmlns;
	bool m_bHasXsiTypeAttribute;

	CRXmlParser(const std::string& stName = "") : m_poDoc(0), m_rootXmlData(0), m_stParserName(stName)
	{
		init();
	}
	void init(){
		DeleteData();
		m_pTreeMapper=0;
		m_pDTDParser=0;
		m_iXmlSize = 0;
		m_rootXmlData=0;
		m_DocType = CRXml_bit_Unknown;
		m_bHasXsiTypeAttribute=false;
		m_poDoc=0;
	}
	virtual ~CRXmlParser();

	TreeMapper*getTreeMapper() {
		if ( m_pTreeMapper )return m_pTreeMapper;
		return 0;
	}

	void setTreeMapper(TreeMapper*p, bool bIsTarget);

public:
	konga::xml::DOMDocument2Ptr GetDOMDocument() { return m_poDoc; }

	                                             
	unsigned long SetDtd(const char*dtdfilename, const char* systemName = 0);

	konga::xml::DOMNodePtr InsertNode(konga::xml::DOMNodePtr parent,
									 CROM* structureNode,
									 const char* value,
									 konga::xml::DOMNodeType type,
									 int& err);

	konga::xml::DOMNodePtr InsertAnyNode(konga::xml::DOMNodePtr parent,
									konga::JbNode*node);

	konga::xml::DOMNodePtr InsertNodeX(konga::xml::DOMNodePtr parent, 
									 const char* elemName,
									 const char* sNamespaceURI,
									 const char *value,
									 konga::xml::DOMNodeType type,
									 int&err);

	konga::xml::DOMNodePtr SetValue(konga::xml::DOMNodePtr& element, const char*value);
	void SetDTDParserPtr(DTDParser*p){m_pDTDParser=p;}
	void write_tree_branch(FILE*out, CROM*pDTDNode, konga::xml::DOMNodePtr&pDOMNode,KString indent);

	void SetDocType(int iType){m_DocType = iType;}
	void AddDocType(int iType){m_DocType |= iType;}
	int  GetDocType() const {return m_DocType;}
	bool IsSource() const {return (m_DocType&CRXml_bit_Source)!=0;}
	bool IsTarget() const {return (m_DocType&CRXml_bit_Target)!=0;}
	bool IsDTDType() const {return (m_DocType&CRXml_bit_DTD)!=0;}
	bool IsXSDType() const {return (m_DocType&CRXml_bit_XSD)!=0;}
	bool IsWebServiceData() const { return (m_DocType & CRXml_bit_WebService) != 0; }
	void SetStructType(const char* filename);

	unsigned long Read (int tran_id, bool isSource, const char*xmlfilename, const char* pcDTDFileName = 0,const char*root_str=0,bool bValidate=true);
	unsigned long Save(const char*fileName,const char*user_defined_path=0,bool remove_dtd_file_path=true);
	unsigned long ValidateTargetXml(const char*xmlFileName, const char*structureFile, int tran_id, const char*rootStr=0);
	unsigned long ReadDTDXml(int tranId, bool isSource, const char*xmlfilename, const char* pcDTDFileName = 0,const char*root_str=0);
	unsigned long ReadXSDXml(const char*xmlFileName, const char*structureFile, bool bValidate);

	void DeleteData();

public:
	konga::xml::DOMNodePtr FindValue(konga::xml::DOMNodePtr&node,const char*const beginPath, const char*const endPath, KString&value);
	                                                                            
	konga::xml::DOMNodePtr FindValue(konga::xml::DOMNodePtr&beginData,const char*beginPath,const char*endPath);

	long long GetDataSize() const;
	void SetDtdUrl(KString sDtdUrl);
	KString GetDtdUrl() const;

	void createDataTree(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements);

	bool IsValidXml() const { return m_stValidationError.empty(); }
	std::string GetValidationError() const { return m_stValidationError; }
	void addXsiTypeAttribute();
	void setupPatternMatchingFlag();

	void SetDefaultNamespace();

                                
protected:
	long long m_iXmlSize;
	konga::xml::DOMNodePtr CreateDOMNode(konga::xml::DOMNodeType type, 
									    const char *acName, 
										const std::string& sNamespaceURI = "");
	unsigned long LoadDocumentSync(konga::xml::DOMDocumentPtr& pDoc, const char* pBURL);
	KString m_DTD_URL;

	int		m_DocType;

	std::string m_stValidationError;

	                 
	std::string m_stParserName;
};

class CDomLoop  
{
	TreeMapper*m_pTreeMapper;
protected:
	konga::xml::DOMNodeListPtr m_listNodes;
	int m_loop_index;                   
	konga::xml::DOMNodePtr m_node;                                    
	bool m_self;                                   
	std::vector<konga::xml::DOMNodePtr*>m_removeList;

public:
	CDomLoop(CRXmlParser*pXmlParser, konga::xml::DOMNodePtr beginNode, const char* beginPath, const char* endPath);
	virtual ~CDomLoop();
	TreeMapper*getTreeMapper(){return m_pTreeMapper;}
	void reset(){m_loop_index=0;m_listNodes->reset();}
	konga::xml::DOMNodePtr NextNode();
	bool Remove(konga::xml::DOMNodePtr*p);

	int m_ErrorCode;
	KString m_EndPath;
};


#endif                   
