/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <cassert>
#include <map>

#include <xercesc/sax/HandlerBase.hpp>

#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>

#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include <konga/Xml/XercesXmlUtils.h>	                      

#include <konga/DataElement/DataElement.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/TransformationLogging/ErrorConfiguration.h>
#include <konga/TransformationUtils/Definitions.h>

#include "ErrorCodes/resource.h"
#include "crom.h"
#include "DataElementUtils.h"
#include "JtrReader.h"
#include "SystemVariableDefs.h"
#include "TextDocReader.h"
#include "TreeMapper.h"
#include "misc.h"

using namespace konga;
using namespace XERCES_CPP_NAMESPACE;
using namespace konga::xml;
using std::string;
using std::vector;
using std::map;

namespace {				
	std::string getString(const XERCES_CPP_NAMESPACE::AttributeList& attributes, const char* attribute_name, bool& bIsNil){
		const XMLCh* chValue = attributes.getValue(X(attribute_name));
		if ( chValue == 0 ) {
			bIsNil = true;
			return string();
		}
		else {
			bIsNil = false;
			return string( XMLChToString(chValue) );
		}
	}

	bool isTrue(const std::string& s){
		return (s == "1" || stricmp(s.c_str(),"true")==0);
	}
}
namespace {
	class AttributeListWrapper {
	public:
		AttributeListWrapper(const XERCES_CPP_NAMESPACE::AttributeList& attributes) : m_attributes(attributes) {
			unsigned int size = m_attributes.getLength();
			for( unsigned int i = 0; i < size; ++i ) {
				const XMLCh* xmlch = m_attributes.getName(i);
				const string& sName = XMLChToString(xmlch);
				string::size_type ipos = sName.find(':');
				if ( ipos != string::npos ) {
					m_attr_map[sName.substr(ipos+1)] = sName;
				}
			}
		}

		string getValue(const string& sName, bool& bIsNil) const {
			if ( m_attr_map.empty() ) {
				                                   
				return getString(m_attributes, sName.c_str(), bIsNil);
			}

			map<string, string>::const_iterator cit = m_attr_map.find(sName);
			if ( cit == m_attr_map.end() ) {
				return getString(m_attributes, sName.c_str(), bIsNil);
			}
			else {
				return getString(m_attributes, cit->second.c_str(), bIsNil);
			}
		}

	private:
		const XERCES_CPP_NAMESPACE::AttributeList& m_attributes;
		map<string, string> m_attr_map;	                                                          
	};
}

class TransformSAXParserHandler : public konga::OurErrorHandler
{
	friend void StreamingTransform::StartSAXParser(const char* inFileName, CROM* generator, CROM*root, bool bValidateSource, FILE*lnk, DTDParser*dtdParser, long long& sourceRecords, long long& targetRecords);

public:
	long long getTargetRecords() const { return targetRecords; }
private:
	const char* inFileName;
	FILE *lnk;
	CTransform* const pTrans;
	StreamingTransform* const pStreamingTransform;
	CROM* const root;
	CROM* current;
	CROM* target;
	CROM* const generator;
	std::stack<CROM*>parent_stack;
	std::map<CROM*,konga::DataElement*>de_map;
	const std::string generator_name, generator_path;
	long long instanceNumber;
	long long targetRecords;
	std::vector<konga::DataElement*>StringValueDE_list;
	std::map<std::string,CROM*>&elementTypes;
	std::map<std::string,CROM*>&elements;

private:
	void construct_de_map(CROM*root);
	konga::DataElement*find_de(const char*de_name);
	void load_attributes(const AttributeListWrapper& attributes);
	CROM* load_value(const char* text);
	void unload_value_attributes();
	void check_nil();
	void decentStringValueDEs();

protected:
	TransformSAXParserHandler(const char*inputFileName, StreamingTransform* pTransform, CROM* target, CROM* generator, CROM*root, FILE*lnk, std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>& elements);
	virtual ~TransformSAXParserHandler();

	virtual void startDocument();
	virtual void startElement(const XMLCh* const qName, XERCES_CPP_NAMESPACE::AttributeList& attributes);
	virtual void characters(const XMLCh* const text, const unsigned int n);
	virtual void endElement(const XMLCh* const qName);
	virtual void endDocument();
	
};

StreamingTransform::StreamingTransform(TreeMapper*pTreeMapper, CROM*pTarget, const string& stInputFileName) :
					m_pTrans(0),
					m_pTreeMapper(pTreeMapper),
					m_conv_src(0), m_conv_tgt(0),
					m_pTarget(pTarget),
					m_pPrecondition(0),
					out_file(0),
					m_stInputFileName(stInputFileName)
{
	if(pTarget->get_CROM_ext_type()==CROM::t_CROM_DB){
		strQualChar='"';
		numQualChar=0;
		delimiterChar=DB_DELIMITER_CHAR;
	} else {
		strQualChar=pTarget->get_strQual();
		numQualChar=pTarget->get_numQual();
		delimiterChar=pTarget->get_delimiter();
	}
	
	m_bCsvNullable = konga::GetSystemVariableAsBool(CSV_NULLABLE, m_pTreeMapper->getOperationDataElementHolder(), true);

	view_log=m_pTreeMapper->getViewLog();
	bSwallowCR=m_pTreeMapper->isSwallowCR();
	bIsCsv=(strQualChar=='"'&&delimiterChar!=0)||(delimiterChar==DB_DELIMITER_CHAR);
	bIsDB=m_pTreeMapper->TargetIsDatabase();
	bIsFixField=(delimiterChar==0);

	CROM*pFirst=pTarget->GetFirstChild();
	if(pFirst->IsElement()){
		m_pTarget=pFirst;
		pFirst=pFirst->GetFirstChild();
	}
	m_pTrans=m_pTarget->getTrans();
	if(m_pTrans==0){
		throw konga::CrException("Transform object has not created for StreamingTransform.");
	}
	m_pTrans->m_record=0;
	m_pTrans->SetAutoNumber(1);

if ( !m_pTreeMapper->TargetIsDatabase() ) {
		m_conv_tgt = konga::Utf8Conv::createEncoderFor(std::string("UTF-8"));
	}

	if((pFirst->GetType()&CROM_t_Precondition)!=0){
		m_pPrecondition=pFirst;
	}

	if(!m_pTreeMapper->IsNoSource()){
		if (m_stInputFileName.empty())
		{
			const std::string sErr("CTransform::StreamingTransform - Input file name not specified.");
			m_pTreeMapper->getViewLog()->DebugString("%s\n", sErr.c_str());
			throw konga::CrException(sErr);
		}
	}

	if (strlen(m_pTreeMapper->getOutputDataFileName()) == 0)
	{
		const std::string sErr("CTransform::StreamingTransform - Output file name not specified.");
		m_pTreeMapper->getViewLog()->DebugString("%s\n", sErr.c_str());
		throw konga::CrException(sErr);
	}
	else {
		const char* fn=m_pTreeMapper->getOutputDataFileName();
		out_file=fopen(fn, m_pTreeMapper->getWriteModeStr());
		if(out_file==0) {
			const long lErr=GetLastError();
			const std::string sErr(std::string("Failed to open flat target output file to write '") + fn + "'.\nReason: " + ErrorResolver(lErr).Resolve(lErr));
			m_pTreeMapper->getViewLog()->DebugString("%s\n", sErr.c_str());
			throw konga::CrException(sErr);
		}
	}

}

StreamingTransform::~StreamingTransform()
{
	if(out_file)fclose(out_file);
	                                                                                                          
		                             
		                  
	   
	m_InputDEList.clear();
	delete m_conv_src;
	delete m_conv_tgt;
}


void StreamingTransform::PrepareFlatSourceDeList(std::vector<CROM*>&vSourceCromAttribute)
{
	                                                                  
	                                                                
	                                 
	std::map<std::string,DataElement*>deMap;
	for(int i=0, n=m_pTrans->getInputDEListSize();i<n;i++){
		DataElement* de=m_pTrans->getInputDEOf(i);
		std::string name=de->getName();
		deMap[name]=de;
	}

	m_InputDEList.clear();
	CROM*pSource=m_pTreeMapper->getSourceFlatFolder();
	assert(pSource!=0 && !pSource->IsLeaf());
	vSourceCromAttribute.clear();
	bool isDataBase=pSource->isDB();
	for(CROM*child=pSource->GetFirstChild();child;child=child->GetNextSibling()){
		if(child->IsAttribute()){
			const string name((const char*)child->GetDisplayName());
			DataElement*de=deMap[name];
			if(de==0){
				de=new DataElement(name.c_str());
			}
			else {
				deMap[name]=0;
				de->setError(0);
			}
			                                        
			de->setFormatStr(child->GetFormat());
			de->setType(child->get_DataElementType());
			if(isDataBase)de->setDefaultString("");
			else de->setDefaultString(child->GetDefaultValue());
			de->setPosBegin(child->get_fieldPos());
			de->setPosLength(child->get_fieldLength());

			m_InputDEList.push_back(de);
			vSourceCromAttribute.push_back(child);
		}
	}

	for(std::map<std::string,DataElement*>::iterator it=deMap.begin();it!=deMap.end();it++){
		DataElement*de=it->second;
		delete de;
	}
}


int StreamingTransform::ProcessFlatStream(FILE*lnk, long long& sourceRecords, long long& targetRecords) {
	sourceRecords = 0;
	targetRecords = 0;
	int err=0;
	bool bRTrimCharData = m_pTreeMapper->isTrimSourceWs();
	FILE* update_source_file=0;
	if(m_pTreeMapper->IsSourceUpdateTransformation()){
		update_source_file=fopen(m_pTreeMapper->getSourceUpdateDataFileName().c_str(),"a+");
		if(update_source_file==0){
			err=TRANSFORM_CANNOT_OPEN_OUTPUT_FILE;
			m_lastErr.LoadString(err);
			view_log->LogMessage("ERROR: %s\n", (const char*)m_lastErr);
			throw konga::CrException("StreamingTransform::ProcessFlatStream failed, can't open source update file for output. file name:"+ m_pTreeMapper->getSourceUpdateDataFileName()+"\n"+(const char*)m_lastErr);
		}
	}
	AutoFileHandle adopt_update_source_file(update_source_file);

	std::vector<CROM*> vSourceCromAttribute;
	PrepareFlatSourceDeList(vSourceCromAttribute);                                                                                 

	{
		TextDocReader reader(m_stInputFileName, m_pTreeMapper->GetSourceDTDParser()->m_root, true);
		long fileSize = reader.getFileContents()->size();

		view_log->DebugString("Source file statistics: size = %d", fileSize);

		if(fileSize == 0)
		{
			m_lastErr += "The source file is empty.";
			return TRANSFORM_SOURCE_EMPTY;
		}
		m_pTrans->SetAutoNumber(1);

		CROM*pSource=m_pTreeMapper->getSourceFlatFolder();
		if(m_pTarget->m_Generator==0)m_pTarget->m_Generator=pSource;

		konga::JbNode* jbRoot=reader.parseDocumentWithStructure();
		std::vector<konga::JbNode*>& jbNode_list=jbRoot->getChildren();
		std::vector<std::string>values;
		std::vector<bool>bIsNulls;
		int nDE=(int)m_InputDEList.size();

		for(std::vector<konga::JbNode*>::iterator it=jbNode_list.begin(); it!=jbNode_list.end(); ++it){
			konga::JbNode* record=*it;
			reader.parseJbNode(record, pSource, values, bIsNulls);
			if(values.empty())continue;
			view_log->DebugString("Processing record #%d", m_pTrans->m_record+1);
			m_lastErr.Empty();

			std::vector<std::string>::iterator value_it=values.begin();
			std::vector<bool>::iterator null_it=bIsNulls.begin();
			assert(values.size()==bIsNulls.size());
			err=0;

			for(int iDE=0; iDE<nDE; iDE++, err=0){
				DataElement*de=m_InputDEList.at(iDE);
				CROM*source=vSourceCromAttribute.at(iDE);
				std::string value;
				bool bNull=true;
				if(null_it!=bIsNulls.end()){
					bNull=*null_it++;
					value=*value_it++;
					bool bTrim=bRTrimCharData && source->get_DataElementType()==DataElement::t_string;
					if(bTrim && bNull==false)value=konga::trim(value);
				}
				if(strlen(de->getName())==0)continue;
				try{ 
					source->SetValue(value.c_str());
					source->SetNullFlag(bNull);
					de->setValue(value.c_str());
					de->setNull(bNull);
					err=de->getError();
					if(de->IsNull() || err==TRANSFORM_INPUT_EMPTY){
						if(de->IsNull() && !bIsDB){
							if(	m_bCsvNullable){
								const char*defaultStr=de->getDefaultStr();
								if(defaultStr && defaultStr[0])de->setValue(defaultStr);
							}
							else {
								value="";
								de->setValue(value.c_str());
							}
						}
						if(value.empty() && err==0)	                                                                                                
							de->setError(TRANSFORM_INPUT_EMPTY);	                                                            
					}
					else {
						if(err)throw err;
					}
				} catch(int err){
					KString tmp;
					tmp.LoadString(err);
					m_pTreeMapper->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CTRANSFORM_BULKTRANSFORM_TRANSFORM_DE_READ_ERROR, (const char*)tmp,record->getContents().c_str(), de->getName());
					        
				}
			}
			if(TransformRecord()==1){
				++targetRecords;
				writeRecord(lnk, update_source_file);
				m_pTrans->SetAutoNumber();
			}
			else {
				fprintf(lnk,"%s,%d,%s,skip\n", (const char*)pSource->get_Path(),m_pTrans->m_record, (const char*)m_pTarget->get_Path());
			}
		}

		err=0;
	}

	sourceRecords = m_pTrans->m_record;

	                                                        
	if(out_file){
		fclose(out_file);
		out_file=0;
	}

	                                                                
	const char* outFileName=m_pTreeMapper->getOutputDataFileName();
	if ( err == 0 && outFileName[0] ) {
		if ( !konga::NonEmptyFileExists(outFileName) ) {
			const std::string& optionName = ErrorConfiguration::NO_TARGET_DATA_GENERATED;
			if ( ErrorConfiguration::isWarning(optionName, false) ) {
				view_log->LogMessage( "%s", "Target is empty.");
			}
			else if ( ErrorConfiguration::isError(optionName, false) ) {
				view_log->LogMessage( "%s", "Target is empty.\nThe system configuration says that this is an error.");
				err = E_FAIL;
			}
		}
	}
	return err;
}

int StreamingTransform::TransformRecord()
{
	KString value, path_to_evaluate, tmp;
	m_pTrans->setCount(m_pTrans->m_record);
	if(m_pPrecondition!=0){
		path_to_evaluate = m_pPrecondition->getSourceExpr();
		if(path_to_evaluate!=0 && path_to_evaluate[0]!=0){
			TALIST* ta_list = m_pPrecondition->getTAList();
			const char* sDataTypeStr("bool"), *sFormat("");
			int err = m_pTrans->Evaluate_dbg(path_to_evaluate, ta_list, sDataTypeStr, 0, sFormat, value, m_pPrecondition);

			if(err!=0||(strcmp("true", (const char*)value)!=0 && strcmp("1", (const char*)value)!=0)){
				m_pTrans->m_record++;
				return 0;
			}
		}
	}

	bool hasNonEmptyValue=false;
	for(CROM*pTargetColumn=m_pTarget->GetFirstChild(); pTargetColumn; pTargetColumn=pTargetColumn->GetNextSibling())
	{
		value.Empty();
		path_to_evaluate=pTargetColumn->getSourceExpr();
		TALIST*ta_list=pTargetColumn->getTAList();
		if(pTargetColumn!=m_pPrecondition){
			bool bNullValue = false;
			if( !path_to_evaluate.IsEmpty() )
			{
				const KString sDataTypeStr(pTargetColumn->GetValueType()),sFormat(pTargetColumn->GetFormat());
				const int sqlDataType = pTargetColumn->get_SqlDataType();
    			int err = m_pTrans->Evaluate_dbg(path_to_evaluate, ta_list, sDataTypeStr, sqlDataType, sFormat, value, pTargetColumn);
    			if(err) {
					int iAdd = 0;	                                
					m_pTreeMapper->handleEvaluateError(err, value, pTargetColumn, iAdd, bNullValue, bIsDB, false, m_pTrans, *m_pTreeMapper->getErrorLog());
				}
				else {
					if(value.IsEmpty()==false)hasNonEmptyValue=true;
				}
			}
			else if(pTargetColumn->hasDefault() && !bIsDB)
			{
				                                                                       
				value = pTargetColumn->GetDefaultValue();
			}
			else {
				bNullValue = true;
			}
			pTargetColumn->SetValue(value);
			pTargetColumn->SetNullFlag(bNullValue);

		}
    }
	if(hasNonEmptyValue){
		m_pTrans->m_record++;
	}
	return hasNonEmptyValue?1:0;
}

void StreamingTransform::writeRecord(FILE*lnk, FILE* update_source_file)
{
	bool hasValue=false;
	m_pTarget->writeBeginRecord(out_file, lnk, update_source_file, hasValue, false, m_conv_tgt);
}

void StreamingTransform::StartSAXParser(const char*inFileName, CROM* generator, CROM*root, bool bValidateSource, FILE*lnk, DTDParser*dtdParser, long long& sourceRecords, long long& targetRecords)
{
	TransformSAXParserHandler handler(inFileName, this, m_pTarget, generator, root, lnk, dtdParser->m_ElementType_map, dtdParser->m_Element_map);

	SAXParser parser;
	parser.setDocumentHandler(&handler);
	parser.setErrorHandler(&handler);
	konga::XercesBOMWorkaround(string(inFileName));

	                                                                                 
	                                                
                                           

	parser.parse(inFileName);
	if ( handler.hasErrors() || handler.hasFatalError() ) {
		const string& stErrors = handler.GetErrors();
		const string& stWarnings = handler.GetWarnings();
		string stMsg("Failed to parse XML document.");
		if ( stErrors.empty() && stWarnings.empty() )
			stMsg += "\nUnknown error.";
		else {
			if ( !stErrors.empty() ) {
				stMsg += "\nErrors: " + stErrors;
			}
			if ( !stWarnings.empty() ) {
				stMsg += "\nWarnings: " + stWarnings;
			}
		}
		throw CrException(stMsg);
	}
	sourceRecords = handler.instanceNumber;
	targetRecords = handler.getTargetRecords();
	if(out_file)fclose(out_file); out_file=0;
}

TransformSAXParserHandler::TransformSAXParserHandler(const char*inFileName0, StreamingTransform* transform0, CROM* target0, CROM* generator0, CROM* root0, FILE*lnk0,
					std::map<std::string,CROM*>&elementTypes0, std::map<std::string,CROM*>& elements0) :
inFileName(inFileName0),
lnk(lnk0),
pTrans(transform0->m_pTrans),
pStreamingTransform(transform0), root(root0), current(0), 
target(target0),
generator(generator0), 
generator_name((const char*)generator->GetDisplayName()), generator_path((const char*)generator->get_Path()),
instanceNumber(0), targetRecords(0),
elementTypes(elementTypes0), elements(elements0)
{
	construct_de_map(root);
}

TransformSAXParserHandler::~TransformSAXParserHandler()
{
}

namespace {
	void RemoveNamespace(string& qName) {
		const string::size_type colon_pos = qName.find(':');
		if ( colon_pos != string::npos ) {
			if ( colon_pos == qName.size()-1 )
				qName.erase();
			else
				qName.erase(0, colon_pos+1);
		}
	}

}

void TransformSAXParserHandler::startElement(const XMLCh* const qName_, XERCES_CPP_NAMESPACE::AttributeList& attributes_)
{
	string qName(XMLChToString(qName_));
	RemoveNamespace(qName);
	bool bDerivedType=false;

	AttributeListWrapper attributes(attributes_);
	
	                                             
	if(parent_stack.size()==0&&current==0){
		if(qName!=(const char*)root->GetDisplayName())
			throw konga::CrException("StartElement() error, starting element does not match with the root. qName= \""+qName+"\", root name=\""+(const char*)root->GetDisplayName()+"\"");
		current=root;
	}
	else if(current==0){                         
		parent_stack.push(current);
		return;
	}
	else {
		CROM* parent=current;
		parent_stack.push(parent);
		CROM* first_child=parent->GetFirstChild();
		if(first_child&&first_child->IsArray()){
			current=first_child;
		} else if(first_child && first_child->IsSubstitutionGroupHead()){
			parent=first_child;
			current=parent->FindChildFolder(qName.c_str());
			if(current)current->m_Generator=parent;
		} else {
			current=parent->FindChildFolder(qName.c_str());
			if(current){
				if(current->IsDerivedTypeHolder()){
					CROM*parent=current;
					bool bIsNull;
					std::string type=attributes.getValue("xsi:type", bIsNull);
					if(!bIsNull&&type.size()>0){
						RemoveNamespace(type);
						                                                                                           
						current=parent->FindChildFolder(type.c_str());
						bDerivedType=true;
						if(!current)return;
						current->m_Generator=parent;
					}
				}
			}
		}
		if(current==0){
			CROM* parent=parent_stack.top();
			if(parent!=0 && parent->IsExpanded()==false)parent->expandCROM(elementTypes, elements);
			if(parent->IsArray()){
				current=parent->GetFirstChild();
			}
			else {
				current=parent->FindFirstObject(qName.c_str(),CROM_t_Element);
			}
			if(current==0){
				                                                        
				return;
			}
		}
	}
	load_attributes(attributes);
}
void TransformSAXParserHandler::characters(const XMLCh* const text_, const unsigned int n)
{
	const std::string& text = XMLChToString(text_);
	if(text.empty()==false){
		CROM* const value_leaf = load_value(text.c_str());
	}
}

void TransformSAXParserHandler::endElement(const XMLCh* const qName_)
{
	std::string qName(XMLChToString(qName_));
	RemoveNamespace(qName);
	check_nil();

	if(current){
		CROM*valueNode=current->GetValueNode();
		if(valueNode!=0){
			std::map<CROM*,konga::DataElement*>::const_iterator it=de_map.find(valueNode);
			if(it!=de_map.end()){
				DataElement*de=it->second;
				if(de!=0 && de->IsNull() && valueNode->GetDefaultValue().IsEmpty()==false){
					de->setValue((const char*)valueNode->GetDefaultValue());
				}
			}
		}
		const KString& current_path=current->get_Path();
		int n=current_path.GetLength();
		if(current->m_Generator==generator || strncmp(generator_path.c_str(), (const char*)current_path, n)==0)
		{
			if(current==generator || current->m_Generator==generator){
			++instanceNumber;
			decentStringValueDEs();
			if(pStreamingTransform->TransformRecord()==1){
				++targetRecords;
				pStreamingTransform->writeRecord(lnk);
				pTrans->SetAutoNumber();
			}
			else {
				fprintf(lnk, "%s,%s,%s,skip\n",
					(const char*)generator->get_Path(),
					generator->getInstanceListing().c_str(),
					(const char*)target->get_Path());
			}
			}
			unload_value_attributes();
		}
		if(current->isLoopNode())current->setInstanceNumber(current->GetInstanceNumber()+1);
		if(current->IsDerivedType()){
			CROM* parent=current->GetParent();
			if(parent && parent->isLoopNode()){
				parent->setInstanceNumber(parent->GetInstanceNumber()+1);
			}
		}
	}
	if(!parent_stack.empty()) {
		current=parent_stack.top();
		parent_stack.pop();
	}
	else
		current=0;
}

void TransformSAXParserHandler::startDocument(){
	current=0;
}

void TransformSAXParserHandler::endDocument(){
}

konga::DataElement* TransformSAXParserHandler::find_de(const char*de_name){
	for(int i=0,n=(int)pTrans->getInputDEListSize();i<n;i++){
		konga::DataElement*de=pTrans->getInputDEOf(i);
		if(strcmp(de->getName(),de_name)==0)return de;
	}
	return 0;
}

void TransformSAXParserHandler::construct_de_map(CROM*folder){
	for(CROM*child=folder->GetFirstChild();child!=0;child=child->GetNextSibling()){
		if(child->IsLeaf()){
			konga::DataElement*de=find_de((const char*)child->get_Path());
			if(de!=0){
				konga::DataElement::Type type=konga::DataElement::get_type(child->GetValueType(), 0);
				de->setType(type);
				de_map[child]=de;
				de->clearValue();
				de->setNull();
				if(child->IsValueLeaf() && child->IsKindOfString()){
					StringValueDE_list.push_back(de);
				}
			}
		}
		else {
			construct_de_map(child);
		}
	}
}

void TransformSAXParserHandler::decentStringValueDEs(){
	int err;
	for(std::vector<konga::DataElement*>::iterator it=StringValueDE_list.begin(); it!=StringValueDE_list.end(); ++it){
		konga::DataElement* de=*it;
		if(de->IsNull())continue;
		std::string value=(const char*)de->GetString(err);
		konga::convertCRLN2LN(value);
		value=konga::trim(value);
		de->setValue(value.c_str());
	}
}

void TransformSAXParserHandler::load_attributes(const AttributeListWrapper& attributes)
{
	if(current==0)return;
	for(CROM*child=current->GetFirstChild();child!=0;child=child->GetNextSibling()){
		if(child->IsLeaf()){
			std::map<CROM*,konga::DataElement*>::const_iterator it=de_map.find(child);
			DataElement*de=it!=de_map.end()? it->second: 0;
			const char* attribute_name=(const char*)child->GetDisplayName();
			if(attribute_name[0]!=0){
				if(de!=0){
					de->clearValue();
					de->setNull();

					bool bIsNil = true;
					string value(attributes.getValue(attribute_name, bIsNil));
					if ( bIsNil ) {
						value=(const char*)child->GetDefaultValue();
						if ( !value.empty() )
							de->setValue(value.c_str());
					} else {
						de->setValue(value.c_str());
					}
				}
				if(konga::EndsWith(attribute_name,":nil")){
					bool bIsNil = false;
					std::string value(attributes.getValue(attribute_name, bIsNil));
					if ( bIsNil || value.empty() ) {
						value=(const char*)child->GetDefaultValue();
					}
					current->SetNillable(isTrue(value));
				}
			}
		}
	}
}

CROM* TransformSAXParserHandler::load_value(const char* text)
{
	if(current==0 || text==0 || text[0]==0)return 0;
	CROM* value_leaf=0;
	for(CROM*child=current->GetFirstChild();child!=0;child=child->GetNextSibling()){
		if(child->IsValueLeaf()){
			value_leaf=child;
			std::map<CROM*,konga::DataElement*>::const_iterator it=de_map.find(child);
			if(it!=de_map.end()){
				DataElement*de=it->second;
				if(de!=0){
					if(!de->appendStringValue(text) && text[0]){
						de->setValue(text);
					}
				}
			}
			break;
		}
	}
	return value_leaf;
}

void TransformSAXParserHandler::unload_value_attributes(){
	const KString& current_path=current->get_Path();
	int n=current_path.GetLength();
	for(std::map<CROM*,konga::DataElement*>::iterator it=de_map.begin();it!=de_map.end();it++){
		CROM* node=it->first;
		const char*path=(const char*)node->get_Path();
		if(strncmp(path, (const char*)current_path, n)==0){
			it->second->clearValue();
			it->second->setNull();
		}
	}
}

void TransformSAXParserHandler::check_nil()
{
	if(current==0)return;
	for(CROM*child=current->GetFirstChild();child!=0;child=child->GetNextSibling()){
		if(child->IsValueLeaf()){
			std::map<CROM*,konga::DataElement*>::const_iterator it=de_map.find(child);
			if(it!=de_map.end()){
				DataElement*de=it->second;
				if(de!=0){
					int err;
					if(de->GetString(err).IsEmpty()){
						if(current->IsNillable())de->setNull();
					}
				}
			}
			break;
		}
	}
}

