/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <memory>	                
#include <cassert>

#include <konga/Utils/Settings.h>
#include <konga/JbParser/JbNode.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlStringBuilder.h>

#include <konga/DataElement/TransException.h>
#include <konga/DataElement/DataElement.h>

#include "CRXmlList.h"
#include "TransformXMLFunctions.h"

using namespace konga;
using std::string;
using std::vector;

string konga::DataElement::getXmlNodeName(){
	if(m_pXmlNode==0 || *m_pXmlNode==0){
		int err;
		std::string anyStr=(const char*)GetString(err);
		konga::xml::DOMNodePtr xmlNode=createDOMNode(anyStr);
		initXmlNode(&xmlNode);
	}
	return m_pXmlNode->getLocalName();
}

string konga::DataElement::getXmlNodeValue(){
	if(m_pXmlNode==0 || *m_pXmlNode==0){
		int err;
		std::string anyStr=(const char*)GetString(err);
		konga::xml::DOMNodePtr xmlNode=createDOMNode(anyStr);
		initXmlNode(&xmlNode);
	}
	return m_pXmlNode->getText();
}


string konga::getXmlString(CRData*dataNode, const KString& path){
	if(dataNode==0){
		throw ExeFcnException("The data node is null.");
	}
	else {
		CRXmlData*xmlData=dynamic_cast<CRXmlData*>(dataNode);
		if(xmlData==0){
			throw ExeFcnException("The data node is not an XML node.  This function cannot be called unless the source is XML document.");
		}

		int err;
		CRXmlData*node=xmlData->findInstance(path,err);
		if(node)return node->getXmlString();
	}

	return "";
}

bool konga::isPresent(CRData*dataNode,const KString&path){
	if(dataNode==0){
		throw ExeFcnException("The data node is null.");
	}
	CRXmlData*xmlData=dynamic_cast<CRXmlData*>(dataNode);
	if(xmlData==0){
		throw ExeFcnException("The data node is not an XML node.  This function cannot be called unless the source is XML document.");
	}
	int err;
	CRXmlData*node=xmlData->findInstance(path,err);
	while(node){
		switch(node->getType()){
			case CRXmlData::t_element:
				return node->getXml()!=0;
			case CRXmlData::t_element_list:
			case CRXmlData::t_instance_list:
				{
					CRXmlList* list=node->getList();
					if(list!=0)node=list->start();
					else return false;
				}
				break;
			default:
				node=0;
				break;
		}
	}
	return false;
}

bool konga::isNil(CRData*dataNode,const KString&path){
	if(dataNode==0){
		throw ExeFcnException("The data node is null.");
	}
	CRXmlData*xmlData=dynamic_cast<CRXmlData*>(dataNode);
	if(xmlData==0){
		throw ExeFcnException("The data node is not an XML node.  This function cannot be called unless the source is XML document.");
	}

	int err;
	CRXmlData*node=xmlData->findInstance(path,err);
	while(node){
		switch(node->getType()){
			case CRXmlData::t_element:
				{
					CRXmlData* e=(CRXmlData*)node->getAttributeNode("xsi:nil");
					if(e!=0){					
						string value=e->getValue();
						return (value=="1"||stricmp(value.c_str(),"true")==0);
					}
				}
				return false;
			case CRXmlData::t_element_list:
			case CRXmlData::t_instance_list:
				{
					CRXmlList* list=node->getList();
					if(list!=0){
						node=list->start();
					}
					else return false;
				}
				break;
			default:
				node=0;
		}
	}
	return false;
}

                                                        
DataElement* konga::SelectNodeFromXmlAny(const string& node_name, const DataElement* const any_collection){
	if ( !any_collection->isCollection() ) return 0;

	const size_t n=any_collection->getDECollectionSize();
	for(size_t i=0;i<n;++i){
		DataElement* anyInstance=any_collection->getDECollectionOf(i);
		if(anyInstance->getXmlNodeName()==node_name){
			return anyInstance;
		}
	}

	return 0;
}

namespace {

	void AddSelectionNamespaces(xml::DOMNodePtr& node, const vector<string>& vnamespaces) {
		for(vector<string>::const_iterator it=vnamespaces.begin();it!=vnamespaces.end();it++){
			const string& prefix_namespace=*it;
			const string::size_type pos=prefix_namespace.find_first_of('=');
			if(pos!=string::npos){
				node->addSelectionNamespace(prefix_namespace.substr(0,pos), prefix_namespace.substr(pos+1));
			}
		}
	}

	DataElement* ConvertToCollection(const xml::DOMNodeListPtr& nodes) {
		std::auto_ptr<DataElement> retVal( new DataElement(DataElement::t_collection) );

		const unsigned long size = nodes.getLength();
		for( unsigned long index = 0; index < size; ++index ) {
			xml::DOMNodePtr node = nodes.getItem(index);
			retVal->addToCollection( new DataElement(node) );
		}

		return retVal.release();
	}

	DataElement* SelectNodesImpl(xml::DOMNodePtr& ref_node, const string& xpath,
		const vector<string>& vnamespaces, bool bOnlyGetFirstNode)
	{
		if ( ref_node == 0 )
			return new DataElement();
		AddSelectionNamespaces(ref_node, vnamespaces);
		if ( bOnlyGetFirstNode ) {
			xml::DOMNodePtr node=ref_node.selectSingleNode(xpath);
			if(node==0) return new DataElement();
			return new DataElement(node);
		}
		else {
			xml::DOMNodeListPtr nodes = ref_node.selectNodes(xpath);
			return ConvertToCollection(nodes);
		}
	}

	DataElement* SelectNodesImpl(DataElement*de, const string& xpath,
		const vector<string>& vnamespaces, int&err, bool bOnlyGetFirstNode)
	{
		assert(de);
		if(de==0) return new DataElement();

		xml::DOMNodePtr ref_node=de->getXmlNode();
		if(ref_node==0){
			string xml_str=(const char*)de->GetString(err);
			string stErr;
			xml::DOMDocumentPtr doc = XmlUtils::LoadDocumentFromString(xml_str, stErr);
			if ( doc == 0 ) throw CrException("Failed to execute XPath query '" + xpath + "'. The selected node is not valid XML. " + stErr);
			return SelectNodesImpl(doc, xpath, vnamespaces, bOnlyGetFirstNode);
		} else {
			return SelectNodesImpl(ref_node, xpath, vnamespaces, bOnlyGetFirstNode);
		}
		assert(!"Should Never come here!!");
	}
}

konga::xml::DOMNodePtr konga::createDOMNode(const std::string& str){
	string sXmlDoc("<?xml version=\"1.0\" encoding=\"" + konga::defaultEncodingXml() + "\"?>");
	sXmlDoc += str;

	string stErr;
	xml::DOMDocumentPtr doc = XmlUtils::LoadDocumentFromString(sXmlDoc, stErr);
	if ( doc == 0 ) {
		throw ExeFcnException("Failed to load XML string for createDOMNode: " + stErr);
	}

	xml::DOMNodePtr pRoot = doc->getDocumentElement();
	return pRoot;
}

                                                              
DataElement* konga::SelectNodesFromXmlAny(const string& xpath_query,
	const DataElement* const any_collection,
	const vector<string>& vnamespaces)
{
	if ( !any_collection->isCollection() )
		return new DataElement();

	const size_t n = any_collection->getDECollectionSize();
	if( n == 0 ) return new DataElement();

	                                                
	string sXmlDoc("<?xml version=\"1.0\" encoding=\"" + konga::defaultEncodingXml() + "\"?><Root>");
	for( size_t i = 0; i < n; ++i ) {
		DataElement* deNode = any_collection->getDECollectionOf(i);
		if ( deNode != 0 ) {
			const xml::DOMNodePtr& node = deNode->getXmlNode();
			if ( node != 0 ) {
				sXmlDoc += node->getXml();
			}
			else if(deNode->getJbNode()){
				sXmlDoc+=deNode->getJbNode()->getXmlString();
			}
		}
	}
	sXmlDoc += "</Root>";

	string stErr;
	xml::DOMDocumentPtr doc = XmlUtils::LoadDocumentFromString(sXmlDoc, stErr);
	if ( doc == 0 ) {
		throw ExeFcnException("Failed to load XML string for SelectNodesFromXmlAny: " + stErr);
	}

	xml::DOMNodePtr pRoot = doc->getDocumentElement();
	return SelectNodesImpl(pRoot, xpath_query, vnamespaces, false);
}


DataElement* konga::SelectSingleNode(DataElement*de, const string& xpath, const vector<string>& vnamespaces, int&err) {
	return SelectNodesImpl(de, xpath, vnamespaces, err, true);
}

DataElement* konga::SelectNodes(DataElement* de, const string& xpath, const vector<string>& vnamespaces, int& err) {
	return SelectNodesImpl(de, xpath, vnamespaces, err, false);
}


#define konga_attribute_begin "<etlforge:ATTR "
#define konga_attribute_end "/>"
int len_konga_attribute_begin=(int)strlen(konga_attribute_begin);
int len_konga_attribute_end=(int)strlen(konga_attribute_end);

string konga::CreateNode(const char* namespace_, const char* name_,
						 const vector<string>& v)
{
	string temp_prefix("tmp");
	string xml,name;
	if(namespace_[0]!=0){
		name=temp_prefix+":"+name_;
		xml="<"+name+" xmlns:"+temp_prefix+"=\""+namespace_+"\"";
	} else {
		name=name_;
		xml="<"+name;
	}
	for(int i=0,n=(int)v.size();i<n;i++){
		const char*s=v.at(i).c_str();
		int l=(int)strlen(s);
		if(strncmp(s,konga_attribute_begin,len_konga_attribute_begin)==0 &&
			l>(len_konga_attribute_begin+len_konga_attribute_end) &&
			strcmp(&s[l-len_konga_attribute_end],konga_attribute_end)==0){
			xml+=" ";
			xml+=string(&s[len_konga_attribute_begin],l-len_konga_attribute_begin-len_konga_attribute_end);
		}
	}
	string values;
	for(int i=0,n=(int)v.size();i<n;i++){
		const char*s=v.at(i).c_str();
		int l=(int)strlen(s);
		if(strncmp(s,konga_attribute_begin,len_konga_attribute_begin)==0 &&
			l>(len_konga_attribute_begin+len_konga_attribute_end) &&
			strcmp(&s[l-len_konga_attribute_end],konga_attribute_end)==0){
				                     
		} else {
			values+=s;
		}
	}
	if(values.empty()){
		xml+="/>";
	} else {
		xml+=">"+values+"</"+name+">";
	}
	return xml;
}

string konga::Attribute(const char* name,const char* value){
	return string(konga_attribute_begin)+name+"=\""+value+"\""+konga_attribute_end;
}

namespace {
	const string JB_ARRAY("JB_Array");
	const string JB_ARRAY_ITEM("it");
	
	string ArrayToMultipleValuesXMLImpl(const DataElement& de, bool bRecursive) {
		xml::XmlStringBuilder sb;
		sb.SetProcessingInstruction("");
		sb.AddElement(JB_ARRAY);
		const size_t size = de.getDECollectionSize();
		int err = 0;
		for( size_t i = 0; i < size; ++i ) {
			sb.AddElement(JB_ARRAY_ITEM);
			const DataElement* const deItem = de.getDECollectionOf(i);
			string value;
			if ( deItem != 0 ) {
				                                                                   
				if ( deItem->isCollection() ) {
					value = ArrayToMultipleValuesXMLImpl(*deItem, true);
				}
				else {
					value = (const char*)deItem->GetString(err);
					if ( err != 0 ) value.clear();
				}
			}
			sb.SetElementValue(value);
			sb.CloseElement();
		}

		sb.CloseAllElements();
		string xml;
		sb.GetXml(xml);
		return xml;
	}

}

string konga::ArrayToMultipleValuesXML(const DataElement& de) {
	return ArrayToMultipleValuesXMLImpl(de, false);
}

