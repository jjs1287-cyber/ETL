/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef JTRREADER_H
#define JTRREADER_H

#include <map>
#include <string>
#include <stack>

#include <xercesc/util/XercesVersion.hpp>                                
#include <xercesc/sax/HandlerBase.hpp>
#include <xercesc/parsers/SAXParser.hpp>
#include <xercesc/sax/SAXParseException.hpp>

#include <konga/Utils/StringTokenizer.h>
#include <konga/Xml/XmlStrings.h>

#include "crom.h"

typedef std::map<int,std::string> IntStrMap;
typedef std::map<std::string,CROM*> StrCromMap;

                                                                                      
                                                              
                                                            
                                                                 
                                                         
                                                 
namespace konga {	                                                                                    
	class OurErrorHandler : public XERCES_CPP_NAMESPACE::HandlerBase {
	public :
		OurErrorHandler(void) : m_bFatalError(false) {}
		virtual ~OurErrorHandler(void) {}
		
		virtual void warning(const XERCES_CPP_NAMESPACE::SAXParseException& exc);
		virtual void error(const XERCES_CPP_NAMESPACE::SAXParseException& exc);
		virtual void fatalError(const XERCES_CPP_NAMESPACE::SAXParseException& exc);

		                                       
		const std::string& GetWarnings() const;
		const std::string& GetErrors() const;

		bool hasErrors() const;
		bool hasFatalError() const;

	private:
		bool m_bFatalError;
		std::string m_sWarnings;
		std::string m_sErrors;
	};
}

class StringIndexMapReader : public konga::OurErrorHandler
{
private:
	IntStrMap &m_Names;
	const XMLCh *index, *value;
	konga::xml::StringConverter *index_, *value_;

public:
	StringIndexMapReader(IntStrMap &Names);

	~StringIndexMapReader(){
		delete index_;
		delete value_;
	}

	virtual void startElement(const XMLCh* const qName_, XERCES_CPP_NAMESPACE::AttributeList& attributes);	
};

class JtrReader : public konga::OurErrorHandler
{
private:
	enum STATUS {t_Element, t_ElementType, t_ElementTypeCROM, t_CROM, t_JmsProperties, t_ending};
	CROM::CROM_ext_type m_type;
	STATUS status;
	TreeMapper*m_pTreeMapper;
	CROM* m_root;
    CROM_Xml* m_crom_xml;
	StrCromMap& m_ElementTypes;
	StrCromMap& m_Elements;
    IntStrMap& m_Names;
	std::string ElementType_TypeName;
	std::stack<std::string> qNames;
	std::stack<CROM*> crom_stack;
	std::map<std::string,std::string>&m_JmsProperties;
	static const char*JtrTypeStr[];
	static CROM::CROM_ext_type ext_type[];
	bool m_bIsLdap;
	void create_CROM_ext(CROM*node, XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names);
	void create_CROM_attr(CROM*node, XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names);

public:
	JtrReader(TreeMapper*p, StrCromMap& ElementTypes, StrCromMap& Elements, IntStrMap& Names, std::map<std::string,std::string>&jmsProperties);
	virtual ~JtrReader(){
	}
	CROM* parse(const std::string&file_name);

	virtual void startElement(const XMLCh* const qName, XERCES_CPP_NAMESPACE::AttributeList& attributes);
	virtual void endElement(const XMLCh* const qName);
	static CROM::CROM_ext_type findType(const std::string&type);
        
private:
	void parseXmlTypeExtensions(XERCES_CPP_NAMESPACE::AttributeList& attributes);        
	void parseXmlSubstitutionGroups(XERCES_CPP_NAMESPACE::AttributeList& attributes);

	void addElementType(std::string typeName, CROM *current);
	void addElement(CROM *current);
        
	std::vector<std::string> translateIndexList(std::string indeces);
};

namespace {
 const char
		*STRINGTOMAP_ROOT  = "Strings",
		*STRINGTOMAP_ENTRY = "String",
		*STRINGTOMAP_INDEX = "idx",
		*STRINGTOMAP_VALUE = "val",
		*JTR_ELEMENT = "JTR",
		*JTR_TYPE = "Type",
		*JTR_VERSION = "Version",
		*JTR_BEGINQUOTE = "BeginQuote",
		*JTR_ENDQUOTE = "EndQuote",
		*JTR_ELEMENT_TYPES = "ElementTypes",
		*JTR_ELEMENTS = "Elements",
		*JTR_ELEMENT_TYPE = "ElementType",
		*JTR_ELEMENT_TYPE_NAME = "Name",
		*JTR_TYPE_EXTENSIONS = "Extensions",
		*JTR_TYPE_EXTENSION_NAMES = "Types",
		*JTR_SUBSTITUTION_GROUPS = "Substitutions",
		*JTR_SUBSTITUTION_GROUP_NAMES = "Types",
		*CROM_ELEMENT = "CROM",
		*CROM_TYPE = "Type",
		*CROM_NAME = "Name",
		*CROM_DISPLAY_NAME = "Display",
		*CROM_COMMENT = "Comment",
		*CROM_TEXT_ELEMENT = "Text",
		*CROM_EXT_ELEMENT = "Ext",
		*CROM_DB_ELEMENT = "DB",
		*CROM_XML_ELEMENT = "Xml",
		*CROM_XML_IS_ABSTRACT = "IsAbstract",
		*CROM_XML_NAMESPACE_QUALIFIED = "NSQual",
		*CROM_XML_MIN_USE = "Min",
		*CROM_XML_MAX_USE = "Max",
		*CROM_XML_NAMESPACE = "NS",
		*CROM_XML_SCHEMA_TYPE = "Type",
		*CROM_XML_SCHEMA_TYPE_NAMESPACE = "TypeNS",
		*CROM_XML_ARRAY_TYPE = "ArrayType",
		*CROM_XML_ANY_ELEMENT_NAMESPACE = "AnyNS",
		*CROM_TEXT_STRQUAL = "StrQual",
		*CROM_TEXT_NUMQUAL = "NumQual",
		*CROM_TEXT_DELIMITER = "Delimiter",
		*CROM_TEXT_IDENT_POS = "IdentPos",
		*CROM_TEXT_IDENT_LENGTH = "IdentLength",
		*CROM_DB_SQLFILTER = "Filter",
		*CROM_DB_SQLORDERBY = "OrderBy",
		*CROM_DB_SCHEMA = "Schema",
		*CROM_DB_TABLENAME = "TableName",
		*CROM_DB_MANUAL_SQL = "ManualSQL",
		*CROM_ATTR_ELEMENT = "Attr",
		*CROM_ATTR_NAMESPACE_QUALIFIED = "NSQual",
		*CROM_ATTR_FIELD_POSITION = "FieldPos",
		*CROM_ATTR_FIELD_LENGTH = "FieldLength",
		*CROM_ATTR_DB_COL_TYPE = "DbColType",
		*CROM_ATTR_DB_COL_SIZE = "DbColSize",
		*CROM_ATTR_DB_DEC_DIGITS = "DbDecDigits",
		*CROM_ATTR_VALUE = "Value",
		*CROM_ATTR_VALUE_TYPE = "ValType",
		*CROM_ATTR_DEFAULT_VALUE = "DefVal",
		*CROM_ATTR_FORMAT = "Format",
		*CROM_ATTR_DOMAIN = "Domain",
		*CROM_ATTR_LINK_TO_PRIMARY_KEY = "LinkToPrimaryKey",
		*CROM_ATTR_INNERJOIN = "InnerJoin",
		*CROM_ATTR_NAMESPACE = "NS",
		*CROM_ATTR_SCHEMA_TYPE = "Type",
		*CROM_ATTR_SCHEMA_TYPE_NAMESPACE = "TypeNS",
		*CROM_ATTR_SOURCE_EXPR = "SourceExpr";
	const char
		*JMS_ELEMENT="JMS",
		*JMS_PROPERTIY="Prop",
		*JMS_NAME="Name",
		*JMS_TYPE="Type";
}

int getCurrentVersion(const std::string& type);

#endif               
