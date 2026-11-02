/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <boost/filesystem/operations.hpp>
#include <boost/scoped_array.hpp>

#include <konga/Utils/AutoFileCleanup.h>	                       
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/StringUtils.h>		           
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CrException.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMDocumentTypePtr.h>
#include <konga/Xml/DOMNodePtr.h>
#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMCDATASectionPtr.h>

#include <konga/Server/Server.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "crom.h"
#include "XmlReader.h"
#include "CRXmlParser.h"

namespace {
	const char CDBegin[] = "<![CDATA[";
	const char CDEnd[] = "]]>";
	const std::string XMLNS_NS = "http://www.w3.org/2000/xmlns/";

	std::string getPrefix(konga::xml::DOMNodePtr&node, const std::string& namespaceURI, const std::string& prefix0, bool& isNew){
		if(node==0){
			isNew=prefix0.empty();
			return prefix0;
		}
		std::string prefix=node->lookupNamespacePrefix(namespaceURI, false);
		isNew=prefix.empty();
		if(!isNew)return prefix;
		std::string prefix1=prefix0.empty()? "ns": prefix0;
		prefix=prefix1;
		std::string test_namespaceURI=node->lookupNamespaceURI(prefix);
		int i=0;
		while(!test_namespaceURI.empty()){
			i++;
			prefix=prefix1+konga::toString(i);
			test_namespaceURI=node->lookupNamespaceURI(prefix);
		}
		return prefix;
	}
}


#define DEBUG_XML_FINDVALUE 0
                         
#ifdef debug_data_tree
	#define prtopen(i,dtd) if(i==1)fprintf(out,"[%s]:{{\n",(const char*)dtd->m_Path); else fprintf(out,",/%s/{\n",(const char*)dtd->m_Name);
	#define prtclose(i,dtd) fprintf(out,"/%s/%d}\n",(const char*)dtd->m_Name,i)
#else
	#define prtopen(i,dtd) if(i==1)fprintf(out,"[%s]:{{\n",(const char*)dtd->m_Path); else fprintf(out,",{\n");
	#define prtclose(i,dtd) fprintf(out,"}")
#endif
using namespace konga;
using namespace konga::xml;
using std::string;
                              

void write_dom_node(FILE*out,KString&indent,DOMNodePtr&node){
	fprintf(out,"%sELEMENT:\"%s\"\n",(const char*)indent,node->getNodeName().c_str());
	KString indent1=indent+"  ";
	DOMNodeListPtr list=node->getChildNodes();
	DOMNamedNodeMapPtr map=node->getAttributes();
	DOMNodePtr child;
	long i,n=list->getLength(),j,m=map->getLength();
	for(i=0;i<n;i++){
		child=list->item[i];
		if(child!=0){
			konga::xml::DOMNodeType node_type=child->getNodeType();
			switch(node_type){
			case konga::xml::NODE_TEXT:
				{
					fprintf(out,"%s[V]%s=\"%s\"\n",(const char*)indent1,child->getNodeName().c_str(),child->getText().c_str());
					break;
				}
			default:
				break;
			}
		}
	}
	for(j=0;j<m;j++){
		child=map->item[j];
		if(child!=0){
			konga::xml::DOMNodeType node_type=child->getNodeType();
			switch(node_type){
			case konga::xml::NODE_ATTRIBUTE:
				{
					fprintf(out,"%s[A]%s=\"%s\"\n",(const char*)indent1,child->getNodeName().c_str(),child->getText().c_str());
					break;
				}
			default:
				break;
			}
		}
	}
	for(i=0;i<n;i++){
		child=list->item[i];
		if(child!=0){
			konga::xml::DOMNodeType node_type=child->getNodeType();
			switch(node_type){
			case konga::xml::NODE_ELEMENT:
				{
					write_dom_node(out,indent1,child);
					break;
				}
			default:
				break;
			}
		}
	}

}

void CRXmlParser::setTreeMapper(TreeMapper*p, bool bIsTarget){
	m_pTreeMapper=p;
	m_pDTDParser = bIsTarget? p->GetTargetDTDParser(): p->GetSourceDTDParser();
}

unsigned long CRXmlParser::Read(int tran_id, bool isSource, const char*xmlfilename, const char* pcDTDFileName      ,const char*root_str,bool bValidate         )
{
	PROFILING doPrf(getTreeMapper(),"CRXmlParser_Read");
	                                                                             
	m_stValidationError.clear();
	if(IsDTDType())
		return ReadDTDXml(tran_id, isSource, xmlfilename, pcDTDFileName, root_str);                                                              
	else if(IsXSDType())
		return ReadXSDXml(xmlfilename, pcDTDFileName, bValidate);
	else
		return E_FAIL;
}


CRXmlParser::~CRXmlParser(){
	DeleteData();
}


void CRXmlParser::DeleteData()
{
	if(m_rootXmlData) {
		delete m_rootXmlData;
		m_rootXmlData = 0;
		m_poDoc = 0;
	}
}

unsigned long CRXmlParser::LoadDocumentSync(DOMDocumentPtr& pDoc, const char* pBURL)
{
    unsigned long hr=S_OK;

	pDoc->validateOnParse = true;
    if( !pDoc->loadFile(pBURL) ) {
        m_LastErr = pDoc->getParseErrors();
        hr=E_FAIL;
    }
    return hr;
}


unsigned long CRXmlParser::SetDtd(const char*dtdfilename, const char* systemName          )
{
	unsigned long hr = S_OK;

      
    
		m_sDTDFileName = dtdfilename;
		if (systemName != 0)
			m_sDTDSystem  = systemName;

		else
		{
			KString oSystem (dtdfilename, strlen(dtdfilename) - strlen(".dtd"));
			m_sDTDSystem = oSystem;
		}

		return hr;

		                                                            
		  
                      
                   
   
                        
                                                       
                

                                                                      
                                                   
                     
      
                                                                                                                                   
                                                               
                   
      
   

                                                                                  

                
   
                  
                                                                                      
                           
  
                                      
    
                                                                                                                                     
                                                             
              
    
   
  
                             
  
                                                                                                                       
                                                                                   
            
  

            
   
}

                   
namespace {
	bool isNum(const char*str)
	{
		int i,l=(int)strlen(str);
		for(i=0;i<l;i++)if(isdigit((unsigned char)str[i])==false)return false;
		return true;
	}
}


DOMNodePtr CRXmlParser::InsertNode(DOMNodePtr parent,
											  CROM* structureNode,
											  const char* value,
											  konga::xml::DOMNodeType type,
											  int& err)
{
	DOMNodePtr pNode = 0;
	unsigned long hr;
	err = 0;

	if( isNum(structureNode->GetDisplayName()) ){
		return parent;
	}

	try
	{
		if (m_poDoc == 0)
		{
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_FAILED, (const char*)structureNode->GetDisplayName(), (const char*)value);
				m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
				return 0;
		}
		if(parent==0){
			if((structureNode->GetType()&CROM_t_Root)!=0 && structureNode->IsAllNSQualified()){
				SetDefaultNamespace();
			}
		}

		std::string sElementName((const char*)structureNode->GetDisplayName());
		std::string sNamespaceURI;
		bool isNewPrefix=false;
		std::string stPrefix;
		if (structureNode->IsNamespaceQualified()) {
			sNamespaceURI = structureNode->GetNamespace();
			if (!sNamespaceURI.empty() && IsXSDType() && sNamespaceURI!=m_sXmlns) {
				stPrefix = getPrefix(parent, sNamespaceURI, "tns", isNewPrefix);
			}
		}

		pNode = CreateDOMNode(type, sElementName.c_str(), sNamespaceURI );
		if(pNode == 0) 
		{
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_XML_PARSER_CREATE_DOM_NODE_ERROR, sElementName.c_str(), (const char*)value);
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return 0;
		}
		if(!stPrefix.empty())pNode->setPrefix(stPrefix);

		                                                                                                
		                                                                                                  
		                                                                  

		if( parent != 0)
		{
			if(parent->appendChild(pNode) == 0)
			{
				                                                   
				hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_APPEND_NODE_ERROR, sElementName.c_str(), (const char*)value);
				m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
				return 0;
			}
		}
		else
		{	            
			DOMElementPtr ElemNode;
			ElemNode = pNode;
			if(ElemNode != 0){
				m_poDoc->setDocumentElement(ElemNode);
				                                                         
			}
			                                                                               
			m_bHasXsiTypeAttribute=false;
		}
		if(0 != value)SetValue(pNode,value);
	}
	catch(const CrException& e)
	{
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_COM_EXCEPTION, (const char*)structureNode->GetDisplayName(), (const char*)value);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr()+"\n"+e.GetErrorString();
		pNode = 0;
	}

	return pNode;	
}

DOMNodePtr CRXmlParser::InsertAnyNode(DOMNodePtr parent, konga::JbNode*node)
{
	if(node==0)return 0;
	std::string node_name=node->getName();
	std::string namespace_=node->getNamespaceURI();
	std::string node_value=node->getValue();

	DOMNodePtr pNode = 0;
	try
	{
		if (m_poDoc == 0)
		{
			getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_FAILED, node_name.c_str(), node_value.c_str());
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return 0;
		}

		std::string sElementName(node_name);
		std::string sNamespaceURI(namespace_);
		std::string stPrefix;
		bool isNewPrefix=false;		
		if( !sNamespaceURI.empty() && sNamespaceURI != m_sXmlns)
		{
			stPrefix = node->getNameIndex();
			if(stPrefix.empty())stPrefix = getPrefix(parent, sNamespaceURI, stPrefix, isNewPrefix);
		}
		

		pNode = CreateDOMNode(konga::xml::NODE_ELEMENT, sElementName.c_str(), sNamespaceURI );
		
		if(pNode == 0) 
		{
			getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_XML_PARSER_CREATE_DOM_NODE_ERROR, sElementName.c_str(), node_value.c_str());
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return 0;
		}
		if(!stPrefix.empty())pNode->setPrefix(stPrefix);

		if( parent != 0)
		{
			if(parent->appendChild(pNode) == 0)
			{
				                                                   
				getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_APPEND_NODE_ERROR, sElementName.c_str(), node_value.c_str());
				m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
				return 0;
			}
		} else {
			throw CrException("Internal error, the parent for the \"Any\" node is null.");
		}
		if(!node_value.empty())SetValue(pNode, node_value.c_str());

		konga::xml::DOMElementPtr elem(pNode);
		std::map<std::string,std::string> attributeMap;
		node->getAttributeMap(attributeMap);
		for(std::map<std::string,std::string>::const_iterator it=attributeMap.begin();
			it!=attributeMap.end();it++){
			const std::string& attribute_name(it->first), attribute_value(it->second);
			const char*name=attribute_name.c_str();
				                          
			if(strncmp(name, "xmlns:",6)==0 && strcmp(&name[6], node->getPrefix().c_str())==0)continue;
			elem->setAttribute(attribute_name, attribute_value);
		}

		for(std::vector<konga::JbNode*>::iterator it=node->getChildren().begin(); it!=node->getChildren().end(); ++it){
			konga::JbNode* child=*it;
			InsertAnyNode(pNode, child);
		}
	}
	catch(const CrException& e)
	{
		getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_COM_EXCEPTION, node->getPrefixedName().c_str(), node_value.c_str());
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr()+"\n"+e.GetErrorString();
		pNode = 0;
	}

	return pNode;	
}


DOMNodePtr CRXmlParser::InsertNodeX(DOMNodePtr parent, 
											  const char* elemName, 
											  const char* sNamespaceURI,
											  const char *value, 
											  konga::xml::DOMNodeType type,int&err)
{
	DOMNodePtr Node = 0;
	unsigned long hr;

	if ( isNum(elemName)){
		                                                                   
		return parent;
	}
	try
	{
		                                                             
		if (m_poDoc == 0)
		{
			hr=m_poDoc.CreateInstance();
            if(FAILED(hr)){
                m_LastErr=m_poDoc->getLastError();
                return 0;
            }
		}

		std::string sElementName(elemName);
		if( IsXSDType() )
		{
			std::string sPrefix = m_pDTDParser->GetNamespacePrefix(sNamespaceURI);
			if( sPrefix.length() )
				sElementName = sPrefix + std::string(":") + sElementName;
		}

		Node = CreateDOMNode(type, sElementName.c_str(), sNamespaceURI );
		if(Node == 0) 
		{
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_XML_PARSER_CREATE_DOM_NODE_ERROR, elemName, value);
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return 0;
		}

		if(parent != 0)
		{
			if(parent->appendChild(Node) == 0)
			{
				                                                   
				hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_APPEND_NODE_ERROR, elemName, value);
				m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
				return 0;
			}
		}
		else
		{	            
			DOMElementPtr ElemNode;
			ElemNode = Node;
			if(ElemNode != 0){
				m_poDoc->setDocumentElement(ElemNode);
				                                                         
			}
		}
		if(0 != value)SetValue(Node,value);
	}
	catch(const CrException& e)
	{
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_INSERTNODE_COM_EXCEPTION, elemName, value);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr()+"\n"+e.GetErrorString();
		Node = 0;
	}

	return Node;
}

DOMNodePtr CRXmlParser::SetValue(DOMNodePtr&element, const char*value)
{
	DOMNodePtr poNode=0;
	                                                                                                   
								                                                                   
								                       
	if(!value)return 0;

	try
	{
		if (konga::xml::NODE_ELEMENT == element->getNodeType())
		{
			const char*str0=value;
			int MarkUp_len;
			const char* str1=strstr(str0,CDBegin);
			while(str1!=0){
				MarkUp_len=(int)(str1-str0);
				str1+=strlen(CDBegin);
				const char*str2=strstr(str1,CDEnd);
				if(str2==0){                                         
					break;
				}
				int CDSect_len=(int)(str2-str1);
				if(MarkUp_len>0){
					poNode = m_poDoc->createNode(konga::xml::NODE_TEXT, "#text", "");
					poNode->setNodeValue(string(str0,MarkUp_len).c_str());
					element->appendChild(poNode);
				}
				poNode = m_poDoc->createCDATASectionNode(string(str1,CDSect_len).c_str());
				element->appendChild(poNode);
				str0=str2+strlen(CDEnd);
				str1=strstr(str0,CDBegin);
			}
			MarkUp_len=(int)strlen(str0);
			if(MarkUp_len>0) {
				poNode = m_poDoc->createNode(konga::xml::NODE_TEXT, "#text", "");
				poNode->setNodeValue(string(str0,MarkUp_len));
				element->appendChild(poNode);
			}
		}
		else {
			element->setNodeValue(value);
			poNode=element;
		}
	}
	catch(CrException&e)
	{
		getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_SETVALUE_COM_EXCEPTION, value);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr()+"\n"+e.GetErrorString();
		return 0;
	}

	return poNode;
}

DOMNodePtr CRXmlParser::CreateDOMNode(konga::xml::DOMNodeType type, 
												 const char *acName, 
												 const std::string& sNamespaceURI)
{
	unsigned long hr = S_OK;

	if (m_poDoc != 0)
	{
		try
		{
			return m_poDoc->createNode(type, acName, sNamespaceURI.c_str());
		}
		catch(const CrException& e)
		{
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CREATEDOMNODE_COM_EXCEPTION, acName, type);
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr()+"\n"+e.GetErrorString();
			return 0;
		}
	}
	else
	{
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CREATEDOMNODE_CALLED_ERE_CREATEDOC_ELEMENT);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
		return 0;
	}
}
                   
namespace {
	bool agree_with_attvalues(DOMNodePtr node,CROM*dtd_node){
		CROM*child=dtd_node->GetFirstChild();
		DOMNodePtr attributeNode;
		while(child){
			if(child->get_Value()[0]!=0&&(child->IsAttribute())!=0){
				std::string path("@");
				path+=(const char*)child->GetDisplayName();
				try {
					if((attributeNode=node->selectSingleNode(path))==0)return false;
					std::string value=(const char*)child->get_Value();
					if(value!=attributeNode->getText())return false;
				}
				catch(const CrException&      )
				{
				}
			}
			child=child->GetNextSibling();
		}
		return true;
	}
}

DOMNodePtr CRXmlParser::FindValue(DOMNodePtr&node,const char*const beginPath0, const char*const endPath0, KString&value)
{
	unsigned long hr = S_OK;
#if DEBUG_XML_FINDVALUE
	konga::xml::DOMNodeType node_type;
	KString node_name;
	if(node!=0){
		node_type=node->nodeType;
		node_name=node->nodeName;
	}
	getTreeMapper()->getViewLog()->DebugString("CRXmlParser::FindValue beginPath0=\"%s\", endPath0=\"%s\"\n",
		beginPath0,endPath0);
#endif
	if(m_pDTDParser==0){
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_FINDVALUE_IS_ZERO);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
		return 0;
	}

	std::vector<std::string>str_vector;
	std::vector<CROM*>condition_vector;
	if(m_pDTDParser->RestoreXPath(beginPath0,endPath0,str_vector,condition_vector)!=0)
	{
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_FINDVALUE_RESTOREPATH_FAILED,beginPath0,endPath0);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
		return 0;
	}

	if(node==0)node=m_poDoc->getDocumentElement();
	DOMNodePtr poTargetNode = node;

	CROM*dtd_node;
	KString path;
	int k_node,k_att=-1;
	int n=(int)str_vector.size();
	for(int i=0;i<n;i++){
		path=str_vector[i].c_str();
		dtd_node=condition_vector[i];
		long i_th=1;
		if(dtd_node&&isNum((const char*)dtd_node->m_Comment)){
			i_th=atoi((const char*)dtd_node->m_Comment);
			dtd_node=0;
		}
#if DEBUG_XML_FINDVALUE
		getTreeMapper()->getViewLog()->DebugString("	restored path=\"%s\", dtd_node=%s\n",path,dtd_node?dtd_node->GetName():"0");
#endif
		k_att=path.ReverseFind('@');

		if(k_att==0){
			poTargetNode=node;
		} else {
			if(k_att>0){
				k_node=k_att;
			} else {
				k_node=(int)path.GetLength();
			}
			if(k_node>0&&path.GetAt(k_node-1)=='/')k_node--;                             
			KString node_path((const char*)path,k_node);

			if(node_path.IsEmpty()||node_path==".")poTargetNode=node;
			else {
				DOMNodeListPtr listNodes=node->selectNodes((const char*)node_path);
				if(listNodes!=0){
					if(dtd_node==0){
						for(;i_th>0;i_th--){
							if((poTargetNode=listNodes->nextNode())==0)break;
						}
					} else {
						while((poTargetNode=listNodes->nextNode())!=0){
							if(dtd_node==0||agree_with_attvalues(poTargetNode,dtd_node))break;
						}
					}
				}
			}
		}
		node=poTargetNode;
	}
	if(poTargetNode==0) {
		m_LastErr.LoadString(TRANSFORM_VALUE_NULL);
		return 0;
	}
	if(k_att>=0){
		DOMNodePtr att;
		std::string att_path=&((const char*)path)[k_att];
		if((att=poTargetNode->selectSingleNode(att_path))==0)return 0;
		value = att->getText();
	} else {
		value = poTargetNode->getText();
	}
#if DEBUG_XML_FINDVALUE
	getTreeMapper()->getViewLog()->DebugString("	value=\"%s\"\n",(const char*)value);
#endif
	return poTargetNode;
}

DOMNodePtr CRXmlParser::FindValue(DOMNodePtr&beginNode,const char*beginPath,const char*endPath){	
	unsigned long hr = S_OK;
	if(beginNode==0)beginNode=m_poDoc->getDocumentElement();

	if(beginPath&&endPath&&strcmp(beginPath,endPath)==0)return beginNode;

	std::vector<std::string>str_vector;
	std::vector<CROM*>node_vector;
	if(m_pDTDParser->RestoreXPath(beginPath,endPath,str_vector,node_vector)!=0){
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_FINDINSTANCE_FAILED,beginPath,endPath);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
		return 0;
	}

	                
	DOMNodePtr node=beginNode;
	DOMNodeListPtr listNodes;
	try
	{
		int n=(int)str_vector.size();
		for(int i=0;i<n;i++){
			KString path=str_vector[i].c_str();
			int k_node=path.ReverseFind('#'),pos=k_node+1;
			int i_instance=0;
			if(k_node>=0){
				if(path.GetAt(pos)=='_')
					i_instance=-atoi(path.Mid(pos+1));
				else 
					i_instance=atoi(path.Mid(pos));
				path=path.Left(k_node);
			}
			                          
			                                                                
			                                                                    
			int l=(int)path.GetLength();
			if(path.GetAt(l-1)=='/')l--;
			path=path.Left(l);
			listNodes=node->selectNodes((const char*)path);
			node=0;
			if(listNodes!=0){
				if(i_instance<0){
					long len;
					len=listNodes->getLength();
					i_instance=len+i_instance+1;
				}
				if(i_instance>0){
					for(int j=1;j<i_instance;j++){
						listNodes->nextNode();
					}
					node=listNodes->nextNode();
				}
				else node=0;
			}
			if(node==0)break;
		}
		return node;
	}
	catch(const CrException& e)
	{
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_FINDINSTANCE_COM_EXCEPTION, beginPath, endPath);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr()+"\n"+e.GetErrorString();
	}
	return 0;
}

namespace {
	void printTabs(FILE*out,int nTabs){
		if(out==0)return;
		for(int i=0;i<nTabs;i++)fprintf(out,"\t");
	}
}

unsigned long CRXmlParser::Save(const char*fileName,const char*user_defined_path,bool remove_dtd_file_path)
{
	unsigned long hr = S_OK;
	try
	{
		if (!m_poDoc->save(fileName))
		{
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XML_SAVE_FAILED,
				fileName, m_poDoc->getLastError().c_str());
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return hr;
		}
	}
	catch(const CrException& e)
	{
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XML_SAVE_FAILED,
			fileName, e.GetErrorString().c_str());
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
		return hr;
	}

	try
	{
		KString tmp=KString(m_sDTDFileName);
		const char*dtdfilename=tmp;
		if(user_defined_path)dtdfilename=user_defined_path;
		else if(remove_dtd_file_path){
			const char*dtdfilename0,*dtdfilename1;
			dtdfilename0=strrchr(dtdfilename,'\\');
			dtdfilename1=strrchr(dtdfilename,'/');
			if(dtdfilename0<dtdfilename1)dtdfilename0=dtdfilename1;

			if(dtdfilename0)dtdfilename=dtdfilename0+1;
		}

		enum TagStatus{t_tag_open,t_tag_close,t_tag_open_close,t_tag_misc};
		FILE*in=fopen(fileName,"rb");
		if(in==0)
		{
			const unsigned long lErr = GetLastError();
			const string& reason = ErrorResolver().Resolve(lErr);
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_SAVE_CANNOT_OPEN_FILE_FOR_READING, fileName, reason.c_str());
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return hr;
		}
		AutoFileHandle adopt_in(in);

		fseek(in,0L,SEEK_END);
		int len=ftell(in);
		rewind(in);
		char* buf=new char[len+1];
		fread(buf,sizeof(char),len,in);
		buf[len]=0;

		StringTokenizer tk(buf,"<>\"");

		adopt_in.Close();

		FILE*out=fopen(fileName,"wb+");
		if(out==0)
		{
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_SAVE_CANNOT_OPEN_FILE_FOR_WRITING, fileName);
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return hr;
		}
		AutoFileHandle adopt_out(out);

		fprintf(out,"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
		if(IsDTDType())
		{
			if (m_DTD_URL.IsEmpty())
				fprintf(out,"<!DOCTYPE %s SYSTEM \"%s\" []>\r\n", (const char*)m_sDTDSystem, dtdfilename);
			else
				fprintf(out,"<!DOCTYPE %s PUBLIC \"httpfile\" \"%s\" []>\r\n", (const char*)m_sDTDSystem, (const char*)m_DTD_URL);
		}

		const char* token;
		KString str,tagName;
		bool open=false;
		std::stack<KString*>st;
		KString*tag=0;
		TagStatus statusSaved=t_tag_close;
		KString csTgtNSPrefix, csRootName;

		                                                                                                        
		const KString lineFeed("\r\n");
		while(tk.NextToken()){
			token=tk.token();
			switch(token[0]){
			case '<':
				{
					const char* str1=tk.getStringAtCurrentPosition();
					if(strncmp(str1,CDBegin,strlen(CDBegin))==0){                                         
						const char*str2=strstr(str1,CDEnd)+strlen(CDEnd);
						if(str2>str1){
							int CDATASect_len=(int)(str2-str1);
							std::string CDATASect(str1,CDATASect_len);
							str+=CDATASect;
							fprintf(out,"%s",CDATASect.c_str());
							tk.setNextPos((size_t)(str2-buf)-1);
							break;
						}
					}
					open=true;
					str=token;
				}
				break;
			case '>':
				str+=token;
				open=false;
				{
					TagStatus status=t_tag_open;
					const char* buf1=str;
					int len=(int)str.GetLength();
					switch(buf1[1]){
					case '/':
						tagName=KString(&buf1[2],len-3);
						status=t_tag_close;
						if(IsXSDType() && st.empty() && strcmp(tagName, csRootName)==0) {
							int pos = str.Find(tagName);
							str.Insert(pos, csTgtNSPrefix+":");
						}
						break;
					case '?':
					case '!':
						status=t_tag_misc;
						break;
					default:
						if(buf1[len-2]=='/')status=t_tag_open_close;
						tagName=str.SpanExcluding(" />");
						tagName.Delete(0);
					}
					switch(status){
					case t_tag_open:
						if(statusSaved==status){
							fprintf(out,"%s",(const char*)lineFeed);
						}
						if(tag){
							st.push(tag);
						}
						tag=new KString(tagName);
						printTabs(out, (int)st.size());
						fprintf(out,"%s",(const char*)str);
						str="";
						break;
					case t_tag_close:
						if(tag==0||tagName!=*tag)
						{
							while(st.size()>0){
								delete st.top();
								st.pop();
							}
							if(tag!=0)delete tag;
							hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_SAVE_CLOSING_TAG_MISMATCH, fileName,(const char*)tagName);
							m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
							return hr;
						}
						delete tag;
						if(statusSaved!=t_tag_open){
							printTabs(out, (int)st.size());
						}
						if(st.size()>0){
							tag=st.top();
							st.pop();
						} else {
							tag=0;
						}
						fprintf(out,"%s%s",(const char*)str,(const char*)lineFeed);
						str="";
						break;
					case t_tag_misc:
						                        
						break;
					default:
						if(statusSaved==t_tag_open){
							fprintf(out,"%s",(const char*)lineFeed);
						}
						if(statusSaved!=t_tag_misc){
							printTabs(out, (int)st.size()+1);
							fprintf(out,"%s%s",(const char*)str,(const char*)lineFeed);
							str="";
						}
						break;
					}
					statusSaved=status;
				}
				break;
			case '"':
				{
					if(open){
						std::string s(token);
						while(tk.NextToken()){
							token=tk.token();
							s+=token;
							if(token[0]=='"')break;
						}
						str+=s.c_str();
						break;
					}
				}
			default:
				if(open)str+=token;
				else if(statusSaved!=t_tag_misc){
					KString csTok(token);
					fprintf(out,"%s",(const char*)csTok);
				}
				break;
			}
		}
		if(tag!=0)delete tag;
		delete[]buf;
		if(str!=""){
			str.Replace("%","%%");
			fprintf(out,"%s%s",(const char*)str,(const char*)lineFeed);
		}

		                                                               
		m_iXmlSize = ftell(out);

	}
	catch(const CrException& ex )
	{
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_SAVE_FILE_EXCEPTION, fileName, ex.GetErrorString().c_str());
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
		return hr;
	}
	return hr;
}


long long CRXmlParser::GetDataSize() const
{
	return m_iXmlSize;
}

void CRXmlParser::write_tree_branch(FILE*out,CROM*pDTDNode,DOMNodePtr&pDOMNode,KString indent)
{
	                       
	                               
	KString Name=pDTDNode->GetDisplayName(),header,path;
	DOMNodePtr pDOMChild;
	CROM*pDTDChild;
	if(Name.IsEmpty()==false&&Name!=""){
		int type=pDTDNode->GetType();
		if(type==CROM_t_Unknown) header="~";
		if((type&CROM_t_Collection)!=0)header+="*";
		if((type&CROM_t_OneOf)!=0)header+="|";
		if((type&CROM_t_Optional)!=0)header+="?";
		if((type&CROM_t_AtLeastOne)!=0)header+="+";
		if((type&CROM_t_Element)!=0)header+="E";
		if((type&CROM_t_Value)!=0)header+="V";
		if((type&CROM_t_Attribute)!=0)header+="A";

		fprintf(out,"%s[%s]%s",(const char*)indent,(const char*)header,(const char*)Name);
		pDTDChild=pDTDNode->GetFirstChild();
		while(pDTDChild){
			if((pDTDChild->GetType()&CROM_t_Value)!=0){
				fprintf(out," - %s",pDOMNode->getText().c_str());
			}
			pDTDChild=pDTDChild->GetNextSibling();
		}
		fprintf(out,"\n");
		indent="   "+indent;
		pDTDChild=pDTDNode->GetFirstChild();
		while(pDTDChild){
			if((pDTDChild->GetType()&CROM_t_Attribute)!=0){
				std::string stAttribName((const char*)pDTDChild->GetDisplayName());
				DOMElementPtr pElement = pDOMNode;
				std::string attribValue = pElement->getAttribute(stAttribName);
				if(attribValue.c_str()==0)
				{
					fprintf(out,"%s[A]%s - %s\n",(const char*)indent,(const char*)pDTDChild->GetDisplayName(),attribValue.c_str());
				}
				
				path="@"+pDTDChild->GetDisplayName();
				try{
					if((pDOMChild=pDOMNode->selectSingleNode((const char*)path))!=0)                                  
						fprintf(out,"%s[A]%s - %s\n",(const char*)indent,(const char*)pDTDChild->GetDisplayName(), pDOMChild->getText().c_str());
				}
				catch(const CrFatalException& e){
					throw e;
				}				
			}
			pDTDChild=pDTDChild->GetNextSibling();
		}
	}
	pDTDChild=pDTDNode->GetFirstChild();
	while(pDTDChild){
		if((pDTDChild->GetType()&CROM_t_Element)!=0){
			                                                             
			CROM*testChild=pDTDNode->GetFirstChild();
			while(pDTDChild!=testChild&&testChild){
				if(testChild->GetDisplayName()==pDTDChild->GetDisplayName()){
					break;
				}
				testChild=testChild->GetNextSibling();
			}
			if(testChild==pDTDChild){
				DOMNodePtr pDOMChild = pDOMNode->getFirstChild();
				while( pDOMChild != 0 )
				{
					if( strcmp((const char*)pDTDChild->GetDisplayName(), pDOMChild->getBaseName().c_str()) == 0 )
						write_tree_branch(out,pDTDChild,pDOMChild,indent);
					pDOMChild = pDOMChild->getNextSibling();
				}
  
                                          
                                                                       
                                                              
         
                                                        
      
                                      
              
      
     
  
			}
		}
		pDTDChild=pDTDChild->GetNextSibling();
	}
}

void CRXmlParser::SetDtdUrl(KString sDtdUrl)
{
	m_DTD_URL = sDtdUrl;
}

KString CRXmlParser::GetDtdUrl() const
{
	return m_DTD_URL;
}

void CRXmlParser::createDataTree(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements){
	if(m_poDoc==0) throw InternalErrorException("createDataTree: Document Element unavailable!");
	if(m_pDTDParser==0) throw InternalErrorException("createDataTree: DTDParser undefined!");

	PROFILING doPrf(getTreeMapper(),"CRXmlParser_createDataTree");
	CROM*dtdRoot=m_pDTDParser->FindRoot();

	DOMNodePtr root=m_poDoc->getDocumentElement();		
	
	if(root==0) throw InternalErrorException("Root of the data tree does not exist!");

	DeleteData();
	FILE*DOMLoop_Debug_log=getTreeMapper()->getViewLog()->GetDebugLog();
	m_rootXmlData=new CRXmlData(0,root, dtdRoot, CRXmlData::t_element, "", DOMLoop_Debug_log);
	m_rootXmlData->completeChildDataTree(elementTypes, elements, "", DOMLoop_Debug_log);
}

                                                                   
                 

CDomLoop::CDomLoop(CRXmlParser*pXmlParser,DOMNodePtr beginNode, const char* beginPath, const char* endPath) :
	m_pTreeMapper(pXmlParser->getTreeMapper())
{
	m_ErrorCode = 0;
	m_self=false;
	m_loop_index=0;
	if(endPath&&endPath[0])m_EndPath=endPath;
	if(beginNode!=0){
		if(beginPath&&endPath)m_self=strcmp(beginPath,endPath)==0;
		if(m_self){
			m_node=beginNode;
			return;
		}
	} else if((beginPath==0||beginPath[0]==0)&&(endPath==0||endPath[0]==0)){
		m_self=true;
		m_node=pXmlParser->m_poDoc->getDocumentElement();
		return;
	}
	                                                         
	else if ( (beginPath==0 || beginPath[0]==0) && (endPath && endPath[0]) ) {
		KString rootNodeName = pXmlParser->m_poDoc->getDocumentElement()->getNodeName().c_str();
		rootNodeName += "$";
		if (strcmp(rootNodeName, endPath) == 0) {
			m_self = true;
			m_node = pXmlParser->m_poDoc->getDocumentElement();
			return;
		}
	}

	std::vector<std::string>str_vector;
	std::vector<CROM*>node_vector;
	if(pXmlParser->m_pDTDParser->RestoreXPath(beginPath,endPath,str_vector,node_vector)!=0){
		getTreeMapper()->getViewLog()->DebugString("CDomLoop::CDomLoop error in RestoreXPath(beginPath=\"%s\", endPath=\"%s\")\n",
			beginPath,endPath);
		m_listNodes=0;
		return;
	}

	CROM*dtd_node;
	DOMNodePtr node=beginNode;
	try
	{
		if(node==0)node=pXmlParser->m_poDoc->getDocumentElement();
		int n=(int)str_vector.size();
		for(int i=0;i<n;i++){
			KString path=str_vector[i].c_str();
			int k_node=(int)path.GetLength();
			
			if(DTDParser::hasInstanceTag(path)){
				k_node=path.Find('#');
				if(k_node<0)k_node=(int)path.GetLength();
			} else if(path.GetAt(k_node-1)=='/'){
				k_node--;                  
				if(k_node<0)k_node=0;
			}

			KString node_path((const char*)path,k_node);
			dtd_node=node_vector[i];
			m_listNodes = node->selectNodes((const char*)node_path);
			
			if(dtd_node==0)break;
			if(m_listNodes!=0){
				while((node=m_listNodes->nextNode())!=0){
					if(agree_with_attvalues(node,dtd_node))break;
				}
				m_listNodes=0;
			}
		}
		long l;
		if(m_listNodes!=0){
			l=m_listNodes->getLength();
		} else {
			l=0;
		}
		getTreeMapper()->getViewLog()->DebugString("CDomLoop::CDomLoop(%s) m_listNodes->length= %d\n",endPath,l);

	}
    catch(const CrException&      )
	{
		getTreeMapper()->getViewLog()->DebugString("Error: CDomLoop::CDomLoop creation failed: Path begin '%s' , end '%s' \n", beginPath, endPath);
		m_ErrorCode = XML_PARSER_DOM_LOOP_CREATE_ERROR;
	}
}

CDomLoop::~CDomLoop()
{
	
}

bool CDomLoop::Remove(DOMNodePtr*p){
	m_removeList.push_back(p);
	return true;
}

namespace {
	int findIndexFromVector(DOMNodePtr*p,const std::vector<DOMNodePtr*>&v){
		for(int i=0,n=(int)v.size();i<n;i++)if(p==v.at(i))return i;
		return -1;
	}
}

DOMNodePtr CDomLoop::NextNode()
{
	unsigned long hr = S_OK;
	DOMNodePtr poNode = 0;
	m_loop_index++;
	try
	{
		if(m_self){
                                                     
			if(m_loop_index==1&&findIndexFromVector(&m_node,m_removeList)<0)
				return m_node;
			else
				return 0;
		}
		
		poNode=m_listNodes->nextNode();
		while(poNode!=0&&findIndexFromVector(&poNode,m_removeList)>=0){
			poNode=m_listNodes->nextNode();
		}
		return poNode;
	}
	catch(const CrException&      )
	{
		                                                                                               
		m_ErrorCode = XML_PARSER_DOM_LOOP_NEXT_NODE_ERROR;
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_XML_PARSER_DOM_LOOP_NEXT_NODE_ERROR);
		return 0;
	}

}



void CRXmlParser::SetStructType(const char* filename)
{
	if(!filename)return;

	const char*dot=strrchr(filename,'.');
	if(!dot)return;
	if(stricmp(dot+1,"dtd")==0)
		m_DocType |= CRXml_bit_DTD;
	else if(stricmp(dot+1,"xsd")==0)
		m_DocType |= CRXml_bit_XSD;
}


unsigned long CRXmlParser::ValidateTargetXml(const char*sXmlFileName, const char*structureFile, int tran_id, const char*rootStr       )
{
	                                                                                
	m_stValidationError = "";

	if( IsXSDType() )
	{
		unsigned long hr = m_pDTDParser->DoSchemaValidation(sXmlFileName, structureFile, m_stValidationError);
		if( FAILED(hr) )
			m_LastErr = m_stValidationError.c_str();
		return hr;
	}
	else if( IsDTDType() )
	{
		return Read(tran_id, false, sXmlFileName, structureFile, rootStr);
	}
	return S_OK;
}


unsigned long CRXmlParser::ReadXSDXml(const char*sXmlFileName, const char*structureFile, bool bValidate)
{
	PROFILING doPrf(getTreeMapper(),"CRXmlParser_ReadXSDXml");
	unsigned long hr = S_OK;

	if(!sXmlFileName) {
		throw InternalErrorException("CRXmlParser::ReadXSDXml was called with sXmlFileName==0.");
	}
	if(!structureFile) {
		throw InternalErrorException("CRXmlParser::ReadXSDXml was called with structureFile==0.");
	}

	try
	{
		const std::string stXmlFile(sXmlFileName), stSchemaFile(structureFile);

		std::string stError;
		if( bValidate ) {
			const std::string& stNamespace = m_pDTDParser->FindRoot()->GetNamespace();
			m_poDoc = XmlUtils::LoadDoc(stXmlFile, stSchemaFile, stNamespace, stError);
		}
		else {
			m_poDoc = XmlUtils::LoadDoc(stXmlFile, stError, false);
		}
		if ( m_poDoc == 0 ) {
			getTreeMapper()->getViewLog()->LogMessage("%s", stError.c_str());
			m_LastErr = stError;
			return E_FAIL;
		}	
		m_poDoc->addSelectionNamespace("xsi", XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001);
	}
	catch(const CrException& e)
	{
		                     
		KString csError;
		csError.Format("Code = %08lx\nCode meaning = %s", e.GetErrorNumber(), e.GetErrorString().c_str());
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CRXMLPARSER_READXSDXML_EXCEPTION, sXmlFileName, (const char*)csError);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
	}

	return S_OK;
}


unsigned long CRXmlParser::ReadDTDXml(int tranId, bool isSource, const char*sXmlFileName, const char* pcDTDFileName        ,const char*root_str       )
{
	PROFILING doPrf(getTreeMapper(),"CRXmlParser_ReadDTDXml");
	unsigned long hr = S_OK;

	                                                      
	const bool overwrite=true;

	try
	{
		hr = m_poDoc.CreateInstance();

		                                                                   
		                                                
		if (FAILED(hr))
		{
			                                                                                                     
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CRXMLPARSER_READ_ERROR, sXmlFileName);
			m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return hr;
		}

		if(m_pDTDParser==0&&pcDTDFileName&&pcDTDFileName[0]!=0){                       
			m_pDTDParser=new DTDParser(getTreeMapper());
			m_pDTDParser->SetTarget(!isSource);
			std::string jtr_file_name=isSource?konga::GetSourceJtrFileName(tranId):konga::GetTargetJtrFileName(tranId);
			int err=m_pDTDParser->Create(pcDTDFileName,root_str,jtr_file_name.c_str(),"dtd");
			if(err){
				m_LastErr = KString("Create DTD parser failed, dtd file name: ")+
					KString(pcDTDFileName)+KString("\n")+
					m_pDTDParser->m_lastErr;
				return hr;
			}
		}

		KString strDTDFileName = "", strDTDPath = "", strDTDFileNameWithoutPath = "";
		if(pcDTDFileName)
		{
			strDTDFileName = pcDTDFileName;
			int k1=strDTDFileName.ReverseFind('\\'),k2=strDTDFileName.ReverseFind('/');
			int k=k1>k2?k1:k2;
			strDTDFileNameWithoutPath = strDTDFileName.Mid(k+1);
			strDTDPath = strDTDFileName.Left(k+1);
		}

		                                                                                                  
		                                                     
		if (pcDTDFileName && !(strnicmp(pcDTDFileName,"http:", sizeof("http:")- sizeof(char))  == 0) )
		{	
            KString strXmlFileName(sXmlFileName);
			int k1=strXmlFileName.ReverseFind('\\'),k2=strXmlFileName.ReverseFind('/');
			int k=k1>k2?k1:k2;
			KString strXmlPath(sXmlFileName,k+1);
			try {
				konga::kCopyFile((const char*)strDTDFileName, (const char*)(strXmlPath+strDTDFileNameWithoutPath), overwrite);
				if(m_pDTDParser&&!m_pDTDParser->m_extra_dtd_files.IsEmpty()){
					StringTokenizer tk((const char*)m_pDTDParser->m_extra_dtd_files,";");
					while(tk.NextToken()){
						const char*token=tk.token();
						if(token[0]==';')continue;
						KString extraFile(token);
						konga::kCopyFile((const char*)(strDTDPath+extraFile), (const char*)(strXmlPath+extraFile), overwrite);
					}
				}
			}
			catch(CrException&ex)
			{
				m_LastErr=KString("Copy dtd file(s) to working directory failed.\n")+ex.GetErrorString().c_str();
				return E_FAIL;
			}
		}


		if((hr = LoadDocumentSync(m_poDoc, sXmlFileName))!=S_OK)
		{
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CRXMLPARSER_LOADDOCUMENTSYN_ERROR, (const char*)m_LastErr);
		}

		                                                                                            
		                                              
		                                
		if(hr == S_OK && IsSource())
		{
			DOMDocumentTypePtr pDocType(m_poDoc->getDocType());
			if(pDocType != 0)
			{
				KString csRoot = pDocType->getName().c_str();
				if(root_str && strlen(root_str)!=0 && stricmp(root_str, csRoot)!=0)
				{
					hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CRXMLPARSER_ROOT_MISMATCH, sXmlFileName, (const char*)csRoot, root_str);
					m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
					return hr;
				}
				DOMNamedNodeMapPtr pAttribs = pDocType->getAttributes();
				if(pAttribs != 0)
				{
					DOMNodePtr pChild = pAttribs->nextNode();
					bool bStop = false;
					while (pChild!=0 && !bStop)
					{
						if(stricmp((const char*)pChild->getNodeName().c_str(),"SYSTEM")==0)
						{
							bStop = true;
							KString csSysName = pChild->getNodeValue();
                            int k1 = csSysName.ReverseFind('\\'), k2=csSysName.ReverseFind('/');
							int k=k1>k2?k1:k2;
                            KString csSysNameWithoutPath = csSysName.Mid(k+1);
                            if(stricmp(csSysNameWithoutPath, strDTDFileNameWithoutPath) != 0)
                            {
                                hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CRXMLPARSER_DTD_MISMATCH, sXmlFileName, (const char*)csSysNameWithoutPath, (const char*)strDTDFileNameWithoutPath);
                                m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
                                return hr;
                            }

						}
						pChild = pAttribs->nextNode();
					}
				}
			}
		}

		                                                              
		                                            
		long lErr = 0;
		m_iXmlSize = konga::GetFileSize(sXmlFileName, lErr);
	}

	catch(const CrException&      )
	{
		              
		                                                                                                      
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CRXMLPARSER_READ_EXCEPTION, sXmlFileName);
		m_LastErr = getTreeMapper()->getErrorLog()->GetLastErr();
	}   
	return hr;
}


void CRXmlParser::addXsiTypeAttribute()
{
	if(!m_bHasXsiTypeAttribute){
		konga::xml::DOMElementPtr root=0;
		if(m_poDoc !=0 && (root=m_poDoc->getDocumentElement()) !=0){
			root->setAttributeNS(XMLNS_NS, "xmlns:xsi", XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001);
			m_bHasXsiTypeAttribute=true;
		}
	}
}

void CRXmlParser::setupPatternMatchingFlag(){
	m_pDTDParser->m_root->setupPatternMatchingFlag();
}

void CRXmlParser::SetDefaultNamespace(){
	if(m_pDTDParser==0)return;
	CROM* crom=m_pDTDParser->m_root;        
	if(IsWebServiceData()){
		crom=crom->FindChildFolder("transaction");
		if(crom)crom=crom->FindChildFolder("body");
		if(crom)crom=crom->GetFirstChild();
		if(crom==0)crom=m_pDTDParser->m_root;
	}
	m_sXmlns=crom->GetNamespace();
}
