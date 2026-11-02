/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <xercesc/sax/HandlerBase.hpp>

#include <konga/Utils/FileUtils.h>	             
#include <konga/Utils/CrException.h>
#include <konga/Utils/XmlInit.h>
#include <konga/TransformationUtils/Definitions.h>
#include <konga/Utils/ProcessUtils.h>	               

#include "ErrorCodes/resource.h"
#include "CROM_Xml.h"
#include "CROM_Ldap.h"
#include "CROM_DB.h"
#include "CROM_Text.h"
#include "CROM_Attr.h"
#include "crom.h"
#include "JtrReader.h"
#include "TreeMapper.h"

using namespace konga::xml;
using namespace XERCES_CPP_NAMESPACE;
using std::string;
using std::vector;

const char* JtrReader::JtrTypeStr[]={"Xml", "Text", "DB", "LDAP", 0};
CROM::CROM_ext_type JtrReader::ext_type[]={CROM::t_CROM_Xml, CROM::t_CROM_Text, CROM::t_CROM_DB, CROM::t_CROM_Ldap, CROM::t_CROM_Unknown};

int getCurrentVersion(const std::string& type){
	if(type=="DB")return DB_CURRENT_VERSION;
	else if(type=="Xml")return XML_CURRENT_VERSION;
	else return 0;
}

namespace {

 	                                                                                                        
	int parseInt(const char*s, int radix=10){
		int i,k;
		unsigned char c0=*s, c;
		if(radix==16){
			if(strnicmp(s,"0x",2)==0)s=s+2;
		}
		if(c0=='-'||c0=='+')s++;
		for(i=0;(c=*s)!=0;s++){
			if(isdigit(c)){
				k=c-'0';
			} else if(isalpha(c)){
				if(isupper(c)){
					k=c-'A'+10;
				} else {
					k=c-'a'+10;
				}
			} else {
				break;
			}
			if(k<radix){
				i=i*radix+k;
			} else {
				break;
			}
		}
		return c0=='-'?(-i):i;
	}

	int XMLCh2Int(const XMLCh* a){
		std::string s=konga::xml::XMLChToString(a);
		return atoi(s.c_str());
	}

	const std::string& lookupIndexedString(int index, IntStrMap& names){
		static std::string blank;
		if (index>=0) {
			IntStrMap::iterator it=names.find(index);
			if(it==names.end())return blank;
			return it->second;
		} else {
			throw konga::CrException("Unexpected string for index of the names.");
		}
	}

	const std::string& lookupIndexedString(XERCES_CPP_NAMESPACE::AttributeList &attributes, const char* attribute_name, IntStrMap& names) {
		konga::xml::StringConverter holder(attribute_name);
		const XMLCh* pXmlCh=holder.getXMLCh();;
		int index = XMLCh2Int(attributes.getValue(pXmlCh));
		return lookupIndexedString(index, names);
	}

	bool getBool(XERCES_CPP_NAMESPACE::AttributeList& attributes, const char* attribute_name, bool default_value){
		konga::xml::StringConverter holder(attribute_name);
		std::string str(XMLChToString(attributes.getValue(holder.getXMLCh())));
		if (str.empty())return default_value;
		return str=="1" || str=="true";
	}
		
	int getInt(XERCES_CPP_NAMESPACE::AttributeList& attributes, const char* attribute_name, int default_value){
		konga::xml::StringConverter holder(attribute_name);
		std::string str(XMLChToString(attributes.getValue(holder.getXMLCh())));
		if(str.empty())return default_value;
		return parseInt(str.c_str(),10);
	}
		
	short getShort(XERCES_CPP_NAMESPACE::AttributeList& attributes, const char* attribute_name, int default_value){
		konga::xml::StringConverter holder(attribute_name);
		std::string str(XMLChToString(attributes.getValue(holder.getXMLCh())));
		if(str.empty())return default_value;
		return (short)parseInt(str.c_str());
	}
		
	std::string getString(XERCES_CPP_NAMESPACE::AttributeList& attributes, const char* attribute_name){
		konga::xml::StringConverter holder(attribute_name);
		std::string str(XMLChToString(attributes.getValue(holder.getXMLCh())));
		return str;
	}

}

                                               
void konga::OurErrorHandler::warning(const XERCES_CPP_NAMESPACE::SAXParseException& exc) {
	if ( !m_bFatalError ) {
		const size_t columnNumber = exc.getColumnNumber();
		const size_t lineNumber = exc.getLineNumber();

		const std::string& thisWarning = konga::xml::XMLChToString(exc.getMessage());
		
		if ( !m_sWarnings.empty() )
			m_sWarnings += "\n";

		m_sWarnings += "Warning: Column: " + konga::toString(columnNumber)
			+ ", Line: " + konga::toString(lineNumber); + ". " + thisWarning;
	}
}

void konga::OurErrorHandler::error(const XERCES_CPP_NAMESPACE::SAXParseException& exc) {
	const size_t columnNumber = exc.getColumnNumber();
	const size_t lineNumber = exc.getLineNumber();

	const std::string& thisErr = konga::xml::XMLChToString(exc.getMessage());
	
	if ( !m_sWarnings.empty() )
		m_sWarnings += "\n";

	m_sWarnings += "Warning: Column: " + konga::toString(columnNumber)
		+ ", Line: " + konga::toString(lineNumber); + ". " + thisErr;
}

void konga::OurErrorHandler::fatalError(const XERCES_CPP_NAMESPACE::SAXParseException& exc) {
	m_sErrors = konga::xml::XMLChToString(exc.getMessage());
	m_bFatalError = true;
}

                                       
const std::string& konga::OurErrorHandler::GetWarnings() const {
	return m_sWarnings;
}

const std::string& konga::OurErrorHandler::GetErrors() const {
	return m_sErrors;
}

bool konga::OurErrorHandler::hasErrors() const {
	return !m_sErrors.empty();
}

bool konga::OurErrorHandler::hasFatalError() const {
	return m_bFatalError;
}

                                         

StringIndexMapReader::StringIndexMapReader(IntStrMap &Names) :
	m_Names(Names)
{
	index_ = new konga::xml::StringConverter(STRINGTOMAP_INDEX);
	index=index_->getXMLCh();
	value_=new konga::xml::StringConverter(STRINGTOMAP_VALUE);
	value=value_->getXMLCh();

}

void StringIndexMapReader::startElement(const XMLCh* const qName_, XERCES_CPP_NAMESPACE::AttributeList& attributes) {
	const std::string& qName=konga::xml::XMLChToString(qName_);
    if (qName == STRINGTOMAP_ENTRY) {
        m_Names[XMLCh2Int(attributes.getValue(index))]=
			konga::xml::XMLChToString(attributes.getValue(value));
    }
}

JtrReader::JtrReader(TreeMapper*p,StrCromMap& ElementTypes, StrCromMap& Elements, IntStrMap& Names, std::map<std::string,std::string>&jmsProperties) :
	m_pTreeMapper(p),
	m_root(0),
	m_crom_xml(0),
	m_ElementTypes(ElementTypes),
	m_Elements(Elements),
	m_Names(Names),
	m_JmsProperties(jmsProperties)
{
	if(m_ElementTypes.size()>0||m_Elements.size()>0||m_Names.size()>0){
		throw konga::CrException("JtrParser initialized with some ElementType/Elements/Names defined.");
	}
	konga::XmlInitializer::InitXerces();
}

CROM* JtrReader::parse(const std::string& jtr_file_name){

	if ( jtr_file_name.empty() ) {
		throw konga::CrException("Failed to parse Jtr file. Jtr file name is empty.");
	}
	if ( !konga::FileExists(jtr_file_name) ) {
		throw konga::CrException("Failed to parse Jtr file. The specified Jtr file does not exist ("
			+ jtr_file_name + ").");
	}

	SAXParser parser;

	string error;
	for(int isleep=1; isleep<64; isleep*=2){
		StringIndexMapReader handler(m_Names);
		parser.setDocumentHandler(&handler);
		parser.setErrorHandler(&handler);
		parser.parse(jtr_file_name.c_str());

		if ( parser.getErrorCount() > 0 ) {
			error = handler.GetErrors();
			if(error.find("The primary document entity could not be opened")==string::npos) {
				throw konga::CrException("Failed to parse jtr file:\n" + error);
			}
			else {
				konga::MilliSleep(1000*isleep);
				error.clear();
			}
		}
	}
	if(!error.empty()) {
		throw konga::CrException("Failed to parse jtr file after trying several times:\n" + error);
	}

	status=t_CROM;
	parser.setDocumentHandler(this);
	parser.setErrorHandler(this);
	parser.parse(jtr_file_name.c_str());
	if ( parser.getErrorCount() > 0 ) {
		throw konga::CrException("Failed to parse jtr file (second parse):\n"
			+ this->GetErrors());
	}
	if ( m_root == 0 )
		throw konga::CrException("No root found when parsing the jtr file.");
	return m_root;
}

void JtrReader::startElement(const XMLCh* const qName_, XERCES_CPP_NAMESPACE::AttributeList& attributes){
	std::string qName=XMLChToString(qName_);
	if(status==t_ending){
		         
		return;
	}
	else if(qName==JTR_ELEMENT){
		std::string type=getString(attributes,JTR_TYPE);
		m_type=findType(type);
		m_bIsLdap=(stricmp(type.c_str(),"LDAP")==0);
	}
    else if(qName==JTR_ELEMENTS){
		status=t_Element;
	}
	else if(qName==JTR_ELEMENT_TYPES){
		status=t_ElementType;
	}
	else if(qName==JTR_ELEMENT_TYPE){
		ElementType_TypeName=lookupIndexedString(attributes, JTR_ELEMENT_TYPE_NAME, m_Names);
		status=t_ElementTypeCROM;
	}
	else if(qName==CROM_ELEMENT){
		CROM* parent=crom_stack.size()>0?crom_stack.top():0;
		CROM* current=new CROM(m_pTreeMapper, parent, attributes, m_Names, m_type);
		crom_stack.push(current);
		switch(status){
		case t_Element:
			break;
		case t_ElementType:
			                         
			break;
		case t_ElementTypeCROM:
			if(parent==0&&!ElementType_TypeName.empty()){
				addElementType(ElementType_TypeName,current);
				ElementType_TypeName="";
			}
			break;
		case t_CROM:
			if(parent==0)m_root=current;
			break;
		}
	}
	else if(qName==CROM_TEXT_ELEMENT ||qName==CROM_EXT_ELEMENT){
		create_CROM_ext(crom_stack.top(), attributes, m_Names);
	}
	else if(qName==CROM_DB_ELEMENT){
		create_CROM_ext(crom_stack.top(), attributes, m_Names);
	}
	else if(qName==CROM_XML_ELEMENT || qName==CROM_TEXT_ELEMENT ||qName==CROM_EXT_ELEMENT || qName==CROM_DB_ELEMENT){
		create_CROM_ext(crom_stack.top(), attributes, m_Names);
		if(m_type==CROM::t_CROM_Xml && status==t_Element && crom_stack.size()==1)addElement(crom_stack.top());
	}
	else if(qName==CROM_ATTR_ELEMENT){
		create_CROM_attr(crom_stack.top(), attributes, m_Names);
	}
	else if(qName==JTR_TYPE_EXTENSIONS){
        parseXmlTypeExtensions(attributes);
	}
	else if(qName==JTR_SUBSTITUTION_GROUPS){
        parseXmlSubstitutionGroups(attributes);
	}
	else if(qName==JMS_ELEMENT){
		status=t_JmsProperties;
	}
	else if(qName==JMS_PROPERTIY){
		std::string name= getString(attributes, JMS_NAME);
		std::string type= getString(attributes, JMS_TYPE);
		m_JmsProperties[name]=type;
	}
	qNames.push(qName);
}
		
void JtrReader::endElement(const XMLCh* const qName_){
	std::string qName=XMLChToString(qName_), top=qNames.top();
	if(top!=qName){
		throw konga::CrException("Internal error: JtrReader::endElement error, qName="+qName+", top:"+top);
		return;
	}
	qNames.pop();
	if(qName=="Elements"||qName==JTR_ELEMENT_TYPES){
		status=t_CROM;
	}
	else if(qName==JTR_ELEMENT_TYPE){
		status=t_ElementType;
	}
	else if(qName==CROM_ELEMENT){
		crom_stack.pop();
		if(qNames.top()=="Jtr" && crom_stack.size()==0){
			status=t_ending;
		}
	}
	else if(qName==CROM_XML_ELEMENT){
		m_crom_xml=0;
	}
}


void JtrReader::parseXmlTypeExtensions(XERCES_CPP_NAMESPACE::AttributeList& attributes) {
	konga::xml::StringConverter holder(JTR_TYPE_EXTENSION_NAMES);
	string typeString = XMLChToString(attributes.getValue(holder.getXMLCh()));
	vector<string> v(translateIndexList(typeString));
	for (vector<string>::const_iterator it=v.begin(); it!=v.end(); ++it){
        m_crom_xml->addTypeExtension(*it);
    }
}

void JtrReader::parseXmlSubstitutionGroups(XERCES_CPP_NAMESPACE::AttributeList& attributes) {
	konga::xml::StringConverter holder(JTR_SUBSTITUTION_GROUP_NAMES);
    string names = XMLChToString(attributes.getValue(holder.getXMLCh()));
	vector<string> v(translateIndexList(names));
	for (vector<string>::const_iterator it=v.begin(); it!=v.end(); ++it){
        m_crom_xml->addSubstitutionGroup(*it);
    }
}

void JtrReader::addElementType(std::string typeName, CROM* current){
	m_ElementTypes[typeName]=current;
}

void JtrReader::addElement(CROM*current){
	m_Elements[current->getUniversalName()]=current;
}



std::vector<std::string> JtrReader::translateIndexList(std::string indeces) {
    std::vector<std::string> translatedValues;
	if (indeces.length()>0) {
		konga::StringTokenizer tk(indeces.c_str(),",");
		while(tk.NextToken()){
			const char*token=tk.token();
			if(*token==',')continue;
			const std::string& value(lookupIndexedString(atoi(token),m_Names));
			translatedValues.push_back(value);
		}
    }
    return translatedValues;
}

                                                                     

CROM::CROM(TreeMapper*p,CROM*parent, XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names, CROM::CROM_ext_type type) :
	m_Type(CROM_t_Unknown),
	m_Type2(0),
	m_CROM_ext_type(type),
	m_pTreeMapper(p),
	m_Generator(0),
	m_Parent(0),
	m_FirstChild(0),
	m_NextSibling(0),
	m_PrevSibling(0),
	m_deList(0),
	m_instanceNumber(0)
{
	m_mapInfo.trans=0;
	m_Parent=parent;
	m_Type=0;
	std::string Type=getString(attributes, CROM_TYPE);
	if(Type.length()>0){
		if(Type.length()>2&&Type.at(0)=='0'&&toupper(Type.at(1))=='X')Type=Type.substr(2);
		m_Type=parseInt(Type.c_str(),16);
	}
	if(m_Parent!=0){
		m_Parent->addChild(this);
	}
	m_Name = getString(attributes, CROM_NAME);
	m_display_name = lookupIndexedString(attributes, CROM_DISPLAY_NAME, names);
	if(m_display_name=="" && m_Name.Find('@')>0){
		m_display_name=m_Name.Left(m_Name.Find('@'));

	}
	m_Comment = getString(attributes, CROM_COMMENT);
	                                          
	m_ext.xml = 0;
	std::string sourceExpr=getString(attributes, CROM_ATTR_SOURCE_EXPR);
	if(!sourceExpr.empty()){

	}
	m_mapInfo.trans=0;
}


void JtrReader::create_CROM_ext(CROM*node, XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names){
	if(node->IsLeaf()){
		throw konga::CrException("Create_CROM_ext may only be called on CROM folder nodes.");
	}
	switch(m_type){
		case CROM::t_CROM_Xml:
			m_crom_xml=new CROM_Xml(attributes, names);
			node->m_ext.xml=m_crom_xml;
			break;
		case CROM::t_CROM_Ldap:
			node->m_ext.ldap=new CROM_Ldap(attributes, names);
			break;
		case CROM::t_CROM_DB:
			node->m_ext.db=new CROM_DB(attributes, names);
			break;
		case CROM::t_CROM_Text:
			node->m_ext.text=new CROM_Text(attributes, names);
			break;
	}
}

void JtrReader::create_CROM_attr(CROM*attr, XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names)
{
	if (!attr->IsLeaf()) {
		throw konga::CrException("create_CROM_attr may only be called on CROM leaf nodes.");
	}
	CROM_Attr* crom_attr=new CROM_Attr(attributes, names, attr->get_CROM_ext_type()==CROM::t_CROM_Xml);
	attr->m_ext.attr=crom_attr;
	attr->setHasDefaultFlag();
}

CROM_Attr::CROM_Attr(XERCES_CPP_NAMESPACE::AttributeList& attributes, IntStrMap& indexedStrings, bool isXml) :
	m_fieldPos(0), 
	m_fieldLength(0),
	m_DbColType(0),
	m_Value(""),
	m_ValueType(""),
	m_DefaultValue(""),
	m_Format(""),
	m_Domain(""),
	m_LinkToPrimaryKey(""),
	m_PkLink(0),
	m_extra(0),
	m_unmap(false)
{
	set_NSQualify(getBool(attributes, CROM_ATTR_NAMESPACE_QUALIFIED,false));
	set_fieldPos(getShort(attributes, CROM_ATTR_FIELD_POSITION,0));
	set_fieldLength(getShort(attributes, CROM_ATTR_FIELD_LENGTH,0));
	set_DbColType(getInt(attributes, CROM_ATTR_DB_COL_TYPE,0));
	set_DbColSize(getInt(attributes, CROM_ATTR_DB_COL_SIZE, 0));
	set_DbDecimalDigits(getInt(attributes, CROM_ATTR_DB_DEC_DIGITS, 0));
	set_Value(getString(attributes, CROM_ATTR_VALUE));
	set_ValueType(lookupIndexedString(attributes, CROM_ATTR_VALUE_TYPE, indexedStrings));
	set_DefaultValue(getString(attributes, CROM_ATTR_DEFAULT_VALUE));
	set_Format(getString(attributes, CROM_ATTR_FORMAT));
	set_Domain(getString(attributes, CROM_ATTR_DOMAIN));
	set_LinkToPrimaryKey(getString(attributes, CROM_ATTR_LINK_TO_PRIMARY_KEY));
	set_Namespace(lookupIndexedString(attributes, CROM_ATTR_NAMESPACE, indexedStrings));
	set_SchemaType(lookupIndexedString(attributes, CROM_ATTR_SCHEMA_TYPE, indexedStrings));
	set_SchemaTypeNamespace(lookupIndexedString(attributes, CROM_ATTR_SCHEMA_TYPE_NAMESPACE, indexedStrings));
	if(isXml)patch();
}

CROM_Xml::CROM_Xml(XERCES_CPP_NAMESPACE::AttributeList& attributes, IntStrMap& indexedStrings) :
	m_isAbstract(false),
	m_iMinUse(1), 
	m_iMaxUse(1),
	m_sArrayType(""), 
	m_extra(0),
	m_pComplexTypeDefinition(0)
{
	m_extra=0;
	set_Abstract(getBool(attributes, CROM_XML_IS_ABSTRACT,false));
	set_NSQualify(getBool(attributes, CROM_XML_NAMESPACE_QUALIFIED,false));
	set_MinUse(getInt(attributes, CROM_XML_MIN_USE,1));
	set_MaxUse(getInt(attributes, CROM_XML_MAX_USE,1));
	set_ArrayType(lookupIndexedString(attributes, CROM_XML_ARRAY_TYPE, indexedStrings));
	set_Namespace(lookupIndexedString(attributes, CROM_XML_NAMESPACE, indexedStrings));
	set_SchemaType(lookupIndexedString(attributes, CROM_XML_SCHEMA_TYPE, indexedStrings));
	set_SchemaTypeNamespace(lookupIndexedString(attributes, CROM_XML_SCHEMA_TYPE_NAMESPACE, indexedStrings));
	set_AnyElementNamespace(lookupIndexedString(attributes, CROM_XML_ANY_ELEMENT_NAMESPACE, indexedStrings));
}

CROM_DB::CROM_DB(XERCES_CPP_NAMESPACE::AttributeList& attributes, IntStrMap& indexedStrings) :
	m_iFKCounter(0),
	m_insert_update(unset)
{
	set_sqlTableName(getString(attributes, CROM_DB_TABLENAME));
	set_dbSchema(getString(attributes, CROM_DB_SCHEMA));
	set_sqlFilter(getString(attributes, CROM_DB_SQLFILTER));
	set_sqlOrderBy(getString(attributes, CROM_DB_SQLORDERBY));
	set_manualSQL(getString(attributes, CROM_DB_MANUAL_SQL));
}

CROM_Text::CROM_Text(XERCES_CPP_NAMESPACE::AttributeList& attributes, IntStrMap& indexedStrings)
{
	std::string strQualStr=getString(attributes, CROM_TEXT_STRQUAL);
	set_strQual(SetQualDelimiterChar(strQualStr.c_str()));
	std::string numQualStr=getString(attributes, CROM_TEXT_NUMQUAL);
	set_numQual(SetQualDelimiterChar(numQualStr.c_str()));
	std::string delimiterStr=getString(attributes, CROM_TEXT_DELIMITER);
	set_delimiter(SetQualDelimiterChar(delimiterStr.c_str()));
	set_identPos(getInt(attributes, CROM_TEXT_IDENT_POS, 0));
	set_identLength(getInt(attributes, CROM_TEXT_IDENT_LENGTH, 0));
}

CROM::CROM_ext_type JtrReader::findType(const std::string&type){
	const char*type_str;
	for(int i=0; (type_str=JtrReader::JtrTypeStr[i])!=0;i++){
		if(stricmp(type_str,type.c_str())==0)return ext_type[i];
	}
	return CROM::t_CROM_Unknown;
}
