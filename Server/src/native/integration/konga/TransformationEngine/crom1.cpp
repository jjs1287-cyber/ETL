/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/CsvUtils.h>

#include "TreeMapper.h"
#include "crom.h"
#include "DbWriter.h"
#include "LdapDataConverter.h"
#include "TextDocument.h"
#include "CROMData.h"
#include "RetVal.h"

#include "CROM_Xml.h"

namespace {
	void doXmlIndent(FILE*out, CROM* node){
		CROM*parent=node->GetParent();
		for(fputc('\n', out); parent; parent=parent->GetParent()){
			if(!parent->IsSubstitutionGroupHead() && !parent->IsDerivedTypeHolder() && !parent->IsChoiceOrAnonymousNode())
				fputc('\t', out);
		}
	}
}


void CROM::SetNormalOrder(){
	                                                                       
	                                                    
	CROM*pValueNode=0;
	std::vector<CROM*>vAttribute;
	std::vector<CROM*>vElement;
	for(CROM*pChild=GetFirstChild(); pChild!=0; pChild=pChild->GetNextSibling()){
		if(pChild->IsValueLeaf()){
			pValueNode=pChild;
		} else if(pChild->IsAttribute()){
			vAttribute.push_back(pChild);
		} else {
			vElement.push_back(pChild);
		}
	}
	if(pValueNode!=0||vAttribute.size()>0){
		CROM*pPreviousSibling=0;
		if(pValueNode!=0){
			SetFirstChild(pValueNode);
			pValueNode->SetPreviousSibling(pPreviousSibling);
			pPreviousSibling=pValueNode;
			pValueNode->SetNextSibling(0);
		}
		for(std::vector<CROM*>::iterator it=vAttribute.begin();it!=vAttribute.end();it++){
			CROM*pAttribute=*it;
			if(pPreviousSibling==0)SetFirstChild(pAttribute);
			pAttribute->SetPreviousSibling(pPreviousSibling);
			pAttribute->SetNextSibling(0);
			if(pPreviousSibling!=0)
				pPreviousSibling->SetNextSibling(pAttribute);
			pPreviousSibling=pAttribute;
		}
		for(std::vector<CROM*>::iterator it=vElement.begin();it!=vElement.end();it++){
			CROM*pElement=*it;
			if(pPreviousSibling==0)SetFirstChild(pElement);
			pElement->SetPreviousSibling(pPreviousSibling);
			pElement->SetNextSibling(0);
			if(pPreviousSibling!=0)
				pPreviousSibling->SetNextSibling(pElement);
			pPreviousSibling=pElement;
		}
	}
	for(CROM*pChild=GetFirstChild(); pChild!=0; pChild=pChild->GetNextSibling()){
		if(!pChild->IsLeaf())
			pChild->SetNormalOrder();
	}
}

void CROM::savedTargetFilePositions(FILE*out, FILE*lnk, FILE* update_source_file){
	if(out)pos_out=ftell(out);
	if(lnk)pos_lnk=ftell(lnk);
	if(update_source_file)pos_update_source=ftell(update_source_file);
}

KString CROM::getSourceInstanceTag(){
	KString csGeneratorPath, csInstanceStr;
	if(getTreeMapper()->IsFlatSource()){
		if(getTreeMapper()->IsNormalizationTransformation()){
			csGeneratorPath=getTreeMapper()->getNormalizationFlatSourceTable()->get_Path();
			csInstanceStr.Format("%d", getTrans()->getSourceCRData()->GetInstanceNumber());
		}
		else {
			if(getTreeMapper()->doStreaming()){
				csGeneratorPath=(m_Generator? m_Generator->get_Path(): "");
				csInstanceStr.Format("%d", getTrans()->getInstanceNumber());
			}
			else {
				CRData*source=getTrans()->getSourceCRData();
				if(source){
					csGeneratorPath=source->getPath();
					csInstanceStr=source->getInstanceListing();
				}
			}
		}
	}
	else if(getTreeMapper()->doStreaming()){
		if(m_Generator){
			csGeneratorPath=m_Generator->get_Path();
			csInstanceStr=m_Generator->getInstanceListing();
		}
	}
	else {
		CRData*source=getTrans()->getSourceCRData();
		if(source){
			CROM*dtd=source->GetDTDNode();
			if(dtd){
				csGeneratorPath=source->getPath();
				csInstanceStr=source->getInstanceListing();
			}
		}
	}
	return csGeneratorPath+","+csInstanceStr;
}

void CROM::writeBeginRecord(FILE*out, FILE*lnk, FILE* update_source_file, bool& hasValue, bool bXmlIndent, konga::Utf8Conv* conv)
{
	savedTargetFilePositions(out, lnk, update_source_file);

	bool bParentIsDerivedTypeHolder=false;
	if(m_Parent!=0 && m_Parent->IsDerivedTypeHolder()){
		bParentIsDerivedTypeHolder=true;
	}

	hasValue=false;

	bool bDriverTypeHolder=IsDerivedTypeHolder();
	bool bSubstitutionGroupHead=IsSubstitutionGroupHead();

	if(!bDriverTypeHolder && !bSubstitutionGroupHead){
		switch(m_CROM_ext_type){
			case t_CROM_DB:
				DbWriter::writeCromElement(out, this, getTreeMapper()->IsFlatTarget(), false);
				break;
			case t_CROM_Ldap:
				LdapDataConverter::writeCromElement(out, this);
				break;
			case t_CROM_Text:
				TextDocument::writeCromElement(out, this, m_pTreeMapper->isQualifierRequired(), m_pTreeMapper->isEDIFACT(), conv);
				break;
			case t_CROM_Xml:
				if(IsAnyElement()){
					writeAnyElement(out);
				}
				else {
					writeXmlBody(out, hasValue, bXmlIndent);
				}
				break;
		}
	}


	if(lnk){
		std::string targetInstanceStr;
		if(getTreeMapper()->doStreaming()){
			targetInstanceStr=konga::toString(getTrans()->GetRecord())+";";
		}
		else {
			targetInstanceStr=getInstanceListing();
		}
		fprintf(lnk, "%s,%s,%s\n",
			(const char*)getSourceInstanceTag(),
			(const char*)get_Path(),
			targetInstanceStr.c_str());
	}

	if(update_source_file && m_Generator){
		bool started=false;
		for(CROM*pAttr=m_Generator->GetFirstChild(); pAttr && pAttr->IsLeaf(); pAttr=pAttr->GetNextSibling()){
			if(!(pAttr->IsPrimaryKey() || pAttr->IsUpdateKey()) && !pAttr->IsSourceUpdateColumn())continue;
			if(started)fputc(',', update_source_file);
			else started=true;
			if(pAttr->IsPrimaryKey() || pAttr->IsUpdateKey()){
				konga::writeCsvData(update_source_file, (const char*)pAttr->GetValue(), ',');
			}
			else if(pAttr->IsSourceUpdateColumn()){
				konga::writeCsvData(update_source_file, (const char*)pAttr->m_Comment, ',');
			}
		}
		fputc('\n',update_source_file);
	}
}

namespace {
long xml_end_tag_offset=0;
}


void CROM::writeEndRecord(FILE*out, bool bXmlIndent, bool hasSubelement,bool hasValue)
{
	if(IsXml()){
		if(IsSubstitutionGroupHead() || IsDerivedTypeHolder() || IsAnyElement() || IsChoiceOrAnonymousNode()){
			return;
		}
		std::string xsi_nil;
		if (!hasSubelement && !hasValue) {
			CROM*nilAttribute=getXmlNilAttribute();
			if (nilAttribute && (!nilAttribute->isMapped())) {
				xsi_nil=" xsi:nil=\"true\"";
			}
		}
		bool useShortXmlEndTag=ftell(out)==end_pos_out;
		if(useShortXmlEndTag || !xsi_nil.empty()){
			size_t pos=end_pos_out-xml_end_tag_offset;
			fseek(out, pos, SEEK_SET);
			if (!xsi_nil.empty()) fputs(xsi_nil.c_str(), out);
			fputs("/>", out);
		}
		else {
			if(hasSubelement && bXmlIndent)doXmlIndent(out, this);
			if(IsDerivedType()){
				fprintf(out,"</%s>", m_Parent->getXmlElementName().c_str());
			}
			else {
				fprintf(out,"</%s>", getXmlElementName().c_str());
			}
		}
	}

}

void CROM::eraseRecord(FILE* out, FILE* lnk, FILE* update_source){
	fseek(out, pos_out, SEEK_SET);
	if(lnk)fseek(lnk, pos_lnk, SEEK_SET);
	if(update_source)fseek(update_source, pos_update_source, SEEK_SET);
}

void CROM::setTokenArrayListItem(TokenArrayListItem* p)
{
	m_mapInfo.item = p;
}

CTransform* CROM::getTrans()
{
	if(IsLeaf()){
		throw konga::CrException("Internal error, CROM::getTrans() cannot be called from a leaf CROM.");
	}
	                           
	if(m_mapInfo.trans==0){
		m_mapInfo.trans = new CTransform(getTreeMapper(), this, getTreeMapper()->getSourceDeSet(), getTreeMapper()->IsFlatSource());
	}
	return m_mapInfo.trans;
}


const char* CROM::getSourceExpr() const
{
	if(!IsLeaf()){
		throw konga::CrException("Internal error, CROM::getSourceExpr() cannot be called from a non leaf CROM.");
	}
	if(m_mapInfo.item==0)return 0;
	return m_mapInfo.item->getSourceExpr();
}

TALIST* CROM::getTAList() const
{
	if(!IsLeaf()){
		throw konga::CrException("Internal error, CROM::getTAList() cannot be called from a non leaf CROM.");
	}
	if(m_mapInfo.item==0)return 0;
	return m_mapInfo.item->getTAList();
}

std::string CROM::getInstanceListing() const
{
	std::string csInstances;
	
	for(const CROM* pNode = this; pNode; pNode = pNode->GetParent()){
		if(pNode->isLoopNode()){
			int k=pNode->GetInstanceNumber()+1;
			csInstances=konga::toString(k)+";"+csInstances;
		}
	}
	return csInstances;
}

void CROM::setInstanceNumber(int i)
{
	if(!isLoopNode())return;
	m_instanceNumber=i;
	resetChildInstanceNumbers();
}


void CROM::resetChildInstanceNumbers()
{
	for(CROM* pChild=GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
		if(pChild->IsLeaf())continue;
		if(pChild->isLoopNode())pChild->m_instanceNumber=0;
		pChild->resetChildInstanceNumbers();
	}
}

bool CROM::isMapped() const
{
	return getSourceExpr()!=0;
}

namespace {
const char
		*USER_DEFINED_SQLIDENTITY="<SQLIDENTITY>",
		*USER_DEFINED_SQLSEQUENCE="<SEQUENCE>",
		*USER_DEFINED_NEXTVAL="<NEXTVAL>",
		*USER_DEFINED_CURRVAL="<CURRVAL>";
}

bool CROM::isSqlIdentity() const
{
	                                  
	const char* exp=getSourceExpr();
	if(exp!=0 && (strcmp(exp, USER_DEFINED_SQLIDENTITY)==0 || strcmp(exp, USER_DEFINED_SQLSEQUENCE)==0))return true;
	if(isSqlCurrVal())return true;
	if(isMapped()==false && IsForeignKey()==false){
		const char* dataType(GetValueType());
		if(strstr(dataType, "identity") || strstr(dataType, "serial"))return true;
	}
	return false;
}

bool CROM::isSqlNextVal() const
{
	                                  
	const KString& exp(GetValue());
	return strncmp((const char*)exp, USER_DEFINED_NEXTVAL, strlen(USER_DEFINED_NEXTVAL))==0;
}

KString CROM::getNextValSql() const
{
	const char* exp=GetValue();
	return KString(&exp[strlen(USER_DEFINED_NEXTVAL)]);
}

bool CROM::isSqlCurrVal() const
{
	                                  
	const KString& exp(GetValue());
	return strncmp((const char*)exp, USER_DEFINED_CURRVAL, strlen(USER_DEFINED_CURRVAL))==0;
}

KString CROM::getCurrValSql() const
{
	const char* exp=GetValue();
	return KString(&exp[strlen(USER_DEFINED_CURRVAL)]);
}


bool CROM::isUserDefinedSequence() const
{
	if(!IsPrimaryKey())return false;
	const char* value=GetValue();
	return strncmp(value,POST_PROCESS_USERDEFINED_SEQUENCE, strlen(POST_PROCESS_USERDEFINED_SEQUENCE))==0;
}

bool CROM::isParentOf(CROM*node){
	if(node==0)return false;
	for(CROM*parent=node->m_Parent; parent; parent=parent->m_Parent){
		if(parent==this)return true;
	}
	return false;
}
CROM* CROM::findTargetLoopNode(CROM*source){
	                                                                                                                                   
	assert(source->isLoopNode());
	std::vector<CROM*>list;
	CROM*tln;
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(!child->IsLeaf()){
			if(child->isLoopNode()){
				CROM* generator=child->m_Generator;
				if(generator==source)return child;
				if(source->isParentOf(generator))list.push_back(child);
			}
			tln=child->findTargetLoopNode(source, list);
			if(tln)return tln;
		}
	}
	tln=0;
	if(list.size()>0){
		tln=list.at(0);
		for(int i=1, n=list.size(); i<n; i++){
			if(!tln->isParentOf(list.at(i)))tln=list.at(i);
		}
	}
	return tln;
}
CROM* CROM::findTargetLoopNode(CROM*source,std::vector<CROM*>&list){
	CROM*tln;
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(!child->IsLeaf()){
			if(child->isLoopNode()){
				CROM* generator=child->m_Generator;
				if(generator==source)return child;
				if(source->isParentOf(generator))list.push_back(child);
			}
			tln=child->findTargetLoopNode(source, list);
			if(tln)return tln;
		}
	}
	return 0;
}

CROM* CROM::getXmlNilAttribute(){
	if(m_pTreeMapper==0)return 0;
	DTDParser* dtdParser=m_pTreeMapper->GetTargetDTDParser();
	if(dtdParser==0)return 0;
	if(!IsExpanded())expandCROM(dtdParser->m_ElementType_map,dtdParser->m_Element_map);
	for(CROM*child=GetFirstChild(); child && child->IsLeaf(); child=child->GetNextSibling()){
		if(child && child->m_Name=="xsi:nil")return child;
	}
	return 0;
}

bool CROM::useXsiPrefix(){
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf()){
			if(strncmp((const char*)child->GetName(),"xsi:",4)==0)return true;
		}
		else {
			if(child->useXsiPrefix())return true;
		}
	}
	return false;
}

void CROM::defineNamespacePrefix(CROM*root, const std::string& defaultNamespace){
	setPrefix("");
	if(IsNamespaceQualified()){
		std::string namespaceUri(GetNamespace());
		if(!namespaceUri.empty() && namespaceUri != defaultNamespace){
			bool found=false;
			std::string prefix0("ns"), prefix(prefix0);
			int iPrefix=0;
			                                                                             
			const std::map<std::string,std::string>& map=root->getXmlAttributeMap();
			for(std::map<std::string,std::string>::const_iterator cit=map.begin(); cit!=map.end(); ++cit){
				if(strncmp(cit->first.c_str(), "xmlns:", 6)==0){
					if(namespaceUri==cit->second){
						found=true;
						prefix=cit->first.substr(6).c_str();
						break;
					}
					if(prefix==cit->first.substr(6)){
						iPrefix++;
						prefix=prefix0+konga::toString(iPrefix);
					}
				}
			}
			if(!found){
				root->addXmlAttribute("xmlns:"+prefix, namespaceUri);	
			}
			setPrefix(prefix);
		}
	}
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(!child->IsLeaf()){
			child->defineNamespacePrefix(root, defaultNamespace);
		}
	}
}

void CROM::setDefaultNamspace(){
	assert(m_Parent==0);
	if(hasUnqualifiedElement())return;

	CROM* crom=this;
	if(getTreeMapper()->IsTargetWebServiceType()){
		crom=crom->FindChildFolder("transaction");
		if(crom)crom=crom->FindChildFolder("body");
		if(crom)crom=crom->GetFirstChild();
		if(crom==0)crom=this;
	}
	std::string defaultNamespace=crom->GetNamespace();
	getTreeMapper()->setTargetDefaultNamespace(defaultNamespace);
	if(defaultNamespace.empty()==false)addXmlAttribute("xmlns", defaultNamespace);

}

bool CROM::hasUnqualifiedElement(){
	if(IsElement() && !IsNamespaceQualified())return true;
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(IsLeaf())continue;
		if(child->hasUnqualifiedElement())return true;
	}
	return false;
}

std::string CROM::getXmlElementName(){
	if(getPrefix()=="")return (const char*)GetDisplayName();
	return getPrefix()+":"+(const char*)GetDisplayName();
}

void CROM::writeAnyElement(FILE*out){
	for(CROM* value=GetFirstChild(); value; value=value->GetNextSibling()){
		if(value->IsValueLeaf()){
			fprintf(out,"%s", (const char*)value->GetValue());
			break;
		}
	}
}

void CROM::writeXmlBody(FILE *out, bool& hasValue, bool bXmlIndent){
	if(m_Parent==0){
		fprintf(out,"%s", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		if(!bXmlIndent)fputc('\n',out);
		if(!getTreeMapper()->getSchemaLocation().empty()){
			addXmlAttribute("xmlns:schemaLocation", getTreeMapper()->getSchemaLocation());
		}
		if(useXsiPrefix()){
			std::map<std::string,std::string>& attrNS_prefix_map(getTreeMapper()->GetTargetDTDParser()->attrNS_prefix_map);
			attrNS_prefix_map[XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001.c_str()]="xsi";
		}
		setDefaultNamspace();
		defineNamespacePrefix(this, getTreeMapper()->getTargetDefaultNamespace());
	}

	if(IsValueLeaf()){
		const KString& val(GetValue());
		fprintf(out, "%s", konga::xml_text((const char*)val, false).c_str());
		return;
	}

	if(IsSubstitutionGroupHead() || IsDerivedTypeHolder() ){
		return;
	}

	bool isXmlElement=!IsChoiceOrAnonymousNode();

	CROM*value=0;
	hasValue=false;

	if(isXmlElement){
		if(bXmlIndent)doXmlIndent(out, this);
		if(IsDerivedType()){
			fprintf(out,"<%s", m_Parent->getXmlElementName().c_str());
			appendAttributeXsiType();
		}
		else {
			fprintf(out,"<%s", getXmlElementName().c_str());
		}

		const std::map<std::string,std::string>& attrMap = getXmlAttributeMap();
		for(std::map<std::string,std::string>::const_iterator mit=attrMap.begin(); mit!=attrMap.end(); ++mit){
			fprintf(out, " %s=\"%s\"", mit->first.c_str(), konga::xml_text(mit->second.c_str(), true).c_str());
		}

		if(m_Parent==0){
			getTreeMapper()->GetTargetDTDParser()->CreateAttrNS2PrefixMap();
		}
		std::map<std::string,std::string>& attrNS_prefix_map(getTreeMapper()->GetTargetDTDParser()->attrNS_prefix_map);
		if(m_Parent==0){
			for(std::map<std::string,std::string>::const_iterator mit=attrNS_prefix_map.begin(); mit!=attrNS_prefix_map.end(); ++mit){
				fprintf(out, " xmlns:%s=\"%s\"", mit->second.c_str(), mit->first.c_str());
			}
		}

		for(CROM*attr=GetFirstChild(); attr && attr->IsLeaf(); attr=attr->GetNextSibling()){
			if(attr->IsPrecondition() || attr->IsNull())continue;
			if(attr->IsAttribute()){
				const KString& val(attr->GetValue());
				std::string prefix;
				if(attr->IsNamespaceQualified()){
					const std::string& ns(attr->GetNamespace());
					if(!ns.empty()){
						if(attrNS_prefix_map.find(ns)==attrNS_prefix_map.end()){
							throw konga::CrException("Internal error, cannot find attribute prefix for namespace:"+ns);
						}
						prefix=attrNS_prefix_map[ns]+":";
					}
				}
				fprintf(out, " %s%s=\"%s\"", prefix.c_str(), (const char*)attr->GetDisplayName(), 
					konga::xml_text((const char*)val, true).c_str());
			}
			else if(attr->IsValueLeaf() && !attr->IsNull()){
				value=attr;
				hasValue=true;
			}
		}

		fputs(">",out);
	}

	if(xml_end_tag_offset==0){
		                                                             
		size_t pos0=ftell(out);
		fputs(">",out);
		xml_end_tag_offset=ftell(out)-pos0;
		fseek(out,pos0,SEEK_SET);
	}
	end_pos_out=ftell(out);
	if(value){
		const KString& val(value->GetValue());
		fprintf(out, "%s", konga::xml_text((const char*)val, false).c_str());
	}
}


bool CROM::IsDerivedType(){
	return m_Parent && m_Parent->IsDerivedTypeHolder();
}
bool CROM::IsSubstitutionGroup(){
	return m_Parent && m_Parent->IsSubstitutionGroupHead();
}

const std::map<std::string,std::string>& CROM::getXmlAttributeMap(){
	assert(m_CROM_ext_type==t_CROM_Xml && !IsLeaf());
	return m_ext.xml->get_AttributeMap();
}

void CROM::addXmlAttribute(const std::string& name, const std::string& value){
	assert(m_CROM_ext_type==t_CROM_Xml && !IsLeaf());
	m_ext.xml->addAttribute(name, value);
}

std::string CROM::getPrefix(const std::string& namespaceURI, const std::string& prefix0){
	                                                
	DTDParser* targetDTDParser=getTreeMapper()->GetTargetDTDParser();
	std::map<std::string,std::string>& attrNS_prefix_map(targetDTDParser->attrNS_prefix_map);
	if(attrNS_prefix_map.find(namespaceURI)!=attrNS_prefix_map.end())return attrNS_prefix_map[namespaceURI];
	if(namespaceURI==m_pTreeMapper->getTargetDefaultNamespace())return "";
	std::string prefix=prefix0;
	int iPrefix=0;
	                                                                             
	for(CROM* node=this; node; node=node->GetParent()){
		const std::map<std::string,std::string>& map=node->getXmlAttributeMap();
		for(std::map<std::string,std::string>::const_iterator cit=map.begin(); cit!=map.end(); ++cit){
			if(strncmp(cit->first.c_str(), "xmlns:", 6)==0){
				if(namespaceURI==cit->second)return cit->first.substr(6);
				if(prefix==cit->first.substr(6)){
					iPrefix++;
					prefix=prefix0+konga::toString(iPrefix);
				}
			}
		}
	}

	                          
	addXmlAttribute("xmlns:"+prefix, namespaceURI);

	return prefix;
}

void CROM::appendAttributeXsiType(){
	getPrefix(XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001, "xsi");
	const std::string& namespaceURI(GetSchemaTypeNamespace());
	const std::string& type_name(GetSchemaType());
	
	if(namespaceURI.empty()){
		addXmlAttribute("xsi:type", type_name);
	}
	else {
		const std::string prefix(getPrefix(namespaceURI,"ns"));
		if(prefix.empty())
			addXmlAttribute("xsi:type", type_name);
		else
			addXmlAttribute("xsi:type", prefix+":"+type_name);
	}

}

const std::string& CROM::getPrefix(){
	if(m_CROM_ext_type!=t_CROM_Xml || IsLeaf()){
		throw konga::CrException("getPrefix() should not be called with non xml or leaf node");
	}
	return m_ext.xml->getPrefix();
}

void CROM::setPrefix(const std::string& prefix){
	if(m_CROM_ext_type!=t_CROM_Xml || IsLeaf()){
		throw konga::CrException("setPrefix() should not be called with non xml or leaf node");
	}
	if(IsElement()){
		return m_ext.xml->setPrefix(prefix);
	}
}

void CROM::add_xsi_type_attributes()
{
	std::string namespaceURI(GetSchemaTypeNamespace());
	std::string type_name(GetSchemaType());

	const std::string& prefix=getPrefix(namespaceURI, "ns");
	if(!prefix.empty())type_name = prefix + ":" + type_name;
	addXmlAttribute("xsi:type", type_name);
}

void CROM::setDbMode(CROM::DB_Mode mode){
	if(m_CROM_ext_type==t_CROM_DB){
		switch(mode){
			case DB_Insert: 
				if(IsElement()){
					m_ext.db->set_MODE(CROM_DB::set_insert);
				}
				break;
			case DB_Update:
				if(IsElement()){
					m_ext.db->set_MODE(CROM_DB::set_update);
				}
				break;
		}
	}
}

void CROM::setUnmapped(bool flag){
	if(m_CROM_ext_type==t_CROM_DB && !IsElement()){
		m_ext.attr->set_unmapped(flag);
	}
}

CROM_DB::MODE CROM::getDbMode(){
	if(m_CROM_ext_type==t_CROM_DB && IsElement()){
		return m_ext.db->get_MODE();
	}
	else return CROM_DB::unset;
}

bool CROM::isUnmapped(){
	if(m_CROM_ext_type==t_CROM_DB && IsAttribute()){
		return m_ext.attr->isUnmapped();
	}
	return false;
}

void CROM::setBatchStatus(unsigned short status){
	m_Type2 &= ~CROM_t_BatchBits;
	m_Type2 |= status;
}

CROM::BatchStatus CROM::getBatchStatus(){
	switch(m_Type2&CROM_t_BatchBits){
		case CROM_t_BatchNode: return t_BatchNode;
		case CROM_t_BatchChild: return t_BatchChildNode;
		case CROM_t_BatchParent: return t_BatchParent;
		case CROM_t_NotBatchNode: return t_NotBatchNode;
		default: return t_UndefinedBatchNode;
	}

}

bool CROM::isBatchNode(){
	return (m_Type2&CROM_t_BatchNode)!=0;
}
bool CROM::isBatchParent(){
	return (m_Type2&CROM_t_BatchParent)!=0;
}

const char* CROM::getIdentifierString() const {
	if(m_CROM_ext_type==t_CROM_Text && !IsLeaf()){
		for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
			if(child->IsIdentifier()){
				return (const char*)child->GetDefaultValue();
			}
		}
		return 0;
	}
	return 0;
}





