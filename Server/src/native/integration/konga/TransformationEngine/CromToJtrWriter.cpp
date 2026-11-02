/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string>
#include <konga/Utils/CrException.h>
#include <konga/Xml/XmlStringBuilder.h>
#include <konga/TransformationUtils/Definitions.h>
#include "crom.h"
#include "JtrReader.h"
#include "CromToJtrWriter.h"

using namespace std;
using namespace konga::xml;

void CromToJtrWriter::createJtr(CROMHeader header, const std::string& outputFilePath){
	XmlStringBuilder xml;
	xml.AddElement(JTR_ELEMENT);

	xml.AddAttribute(JTR_TYPE, header.getJtrType());
	int version=header.getVersion();
	if(version!=0)xml.AddAttribute(JTR_VERSION, konga::toString(version));
	xml.AddAttribute(JTR_BEGINQUOTE, header.getBeginQuote());
	xml.AddAttribute(JTR_ENDQUOTE, header.getEndQuote());

	WriteCromTree(header.getRoot(), xml);
	WriteStringIndexToXml(xml);
	xml.CloseElement();
	WriteXmlToOutputFile(xml, outputFilePath);
}

void CromToJtrWriter::WriteCromTree(CROM* node, XmlStringBuilder& xml) {
	xml.AddElement(CROM_ELEMENT);
	WriteBasicCromNodeInfo(node, xml);
	WriteExtensionData(node, xml);
	CROM* child = node->GetFirstChild();
	while (child != 0) {
		WriteCromTree(child, xml);
		child = child->GetNextSibling();
	}
	xml.CloseElement();
}

void CromToJtrWriter::WriteBasicCromNodeInfo(CROM* node, XmlStringBuilder& xml) {
	AddAttribute(xml, CROM_NAME, node->m_Name, false);
	if(node->m_Name!=node->m_display_name)AddAttribute(xml, CROM_DISPLAY_NAME, node->m_display_name, true);
	AddHexAttribute(xml, CROM_TYPE, node->m_Type);
	AddAttribute(xml, CROM_COMMENT, node->m_Comment, false);
}

void CromToJtrWriter::WriteExtensionData(CROM* node, XmlStringBuilder& xml) {
	if(node->IsLeaf()){
		xml.AddElement(CROM_ATTR_ELEMENT);
		AddAttribute(xml, CROM_ATTR_NAMESPACE_QUALIFIED, node->IsNamespaceQualified(), false);
		AddAttribute(xml, CROM_ATTR_FIELD_POSITION, node->get_fieldPos());
		AddAttribute(xml, CROM_ATTR_FIELD_LENGTH, node->get_fieldLength());
		AddAttribute(xml, CROM_ATTR_DB_COL_TYPE, node->get_SqlDataType());
		AddAttribute(xml, CROM_ATTR_DB_COL_SIZE, node->get_DbColSize());
		AddAttribute(xml, CROM_ATTR_DB_DEC_DIGITS, node->get_DbDecimalDigits());
		AddAttribute(xml, CROM_ATTR_VALUE_TYPE, node->GetValueType(), true);
		AddAttribute(xml, CROM_ATTR_DEFAULT_VALUE, node->get_DefaultValue(), false);
		AddAttribute(xml, CROM_ATTR_FORMAT, node->get_Format(), false);
		AddAttribute(xml, CROM_ATTR_DOMAIN, node->get_Domain(), false);
		AddAttribute(xml, CROM_ATTR_LINK_TO_PRIMARY_KEY, node->get_LinkToPrimaryKey(), false);
		AddAttribute(xml, CROM_ATTR_NAMESPACE, node->GetNamespace(), true);
		AddAttribute(xml, CROM_ATTR_SCHEMA_TYPE, node->GetSchemaType(), true);
		AddAttribute(xml, CROM_ATTR_SCHEMA_TYPE_NAMESPACE, node->GetSchemaTypeNamespace(), true);
		AddAttribute(xml, CROM_ATTR_SOURCE_EXPR, node->getSourceExpr(), true);
		xml.CloseElement();
		return;
	}
	switch(node->get_CROM_ext_type()){
		case CROM::t_CROM_Text:
			xml.AddElement(CROM_TEXT_ELEMENT);
			AddAttribute(xml, CROM_TEXT_STRQUAL, CharToString(node->get_strQual()), false);
			AddAttribute(xml, CROM_TEXT_NUMQUAL, CharToString(node->get_numQual()), false);
			AddAttribute(xml, CROM_TEXT_DELIMITER, CharToString(node->get_delimiter()), false);
			AddAttribute(xml, CROM_TEXT_IDENT_POS, node->get_fieldPos());
			AddAttribute(xml, CROM_TEXT_IDENT_LENGTH, node->get_fieldLength());
			xml.CloseElement();
			break;
		case CROM::t_CROM_DB:
			xml.AddElement(CROM_DB_ELEMENT);
			AddAttribute(xml, CROM_DB_TABLENAME, node->get_sqlTableName(), false);
			AddAttribute(xml, CROM_DB_SCHEMA, node->get_dbSchema(), false);
			AddAttribute(xml, CROM_DB_SQLFILTER, node->get_sqlFilter(), false);
			AddAttribute(xml, CROM_DB_SQLORDERBY, node->get_sqlOrderBy(), false);
			AddAttribute(xml, CROM_DB_MANUAL_SQL, node->get_manualSQL(), false);
			xml.CloseElement();
			break;
		case CROM::t_CROM_Ldap:
			break;
		case CROM::t_CROM_Xml:
			                                                          
			throw konga::CrException("Unexpected call to CromToJtrWriter::WriteExtensionData()");
			break;

	}
}

CROMHeader::CROMHeader(CROM* pDtdRoot) : root(pDtdRoot), version(0), beginQuote(""), endQuote("")
 
{
	jtrType=CROM::toString(pDtdRoot->get_CROM_ext_type());
	if(jtrType=="DB"){
		version=DB_CURRENT_VERSION;
		if(pDtdRoot){
			const char* sqlTableName=0;
			for(CROM*pTable=pDtdRoot->GetFirstChild(); pTable; pTable=pTable->GetNextSibling()){
				if(pTable->IsElement()){
					if(pTable->isManualSqlStatement()){
						return;
					}
					sqlTableName=pTable->get_sqlTableName();
					break;
				}
			}
			if(sqlTableName==0){
				throw konga::InternalErrorException("No table name in database tree.");
			}
			const int n=strlen(sqlTableName);
			if(n<=3) {
				throw konga::InternalErrorException("SQL table name has to be bigger than 3 characters long (including quote characters).");
			}
			beginQuote=std::string(sqlTableName,1);
			endQuote=std::string(&sqlTableName[n-1],1);
		}
	}
}
	
CROMHeader::CROMHeader(CROM* pDtdRoot, int version0, const std::string& beginQuote0, const std::string& endQuote0)
	: root(pDtdRoot), version(version0), beginQuote(beginQuote0), endQuote(endQuote0)
{
	jtrType=CROM::toString(pDtdRoot->get_CROM_ext_type());
}
