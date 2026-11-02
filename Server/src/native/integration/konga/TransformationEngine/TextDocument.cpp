/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <konga/Utils/CrException.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/AutoFileCleanup.h>

#include <konga/CharacterEncodingConversion/Utf8Conv.h>
#include <konga/CharacterEncodingConversion/EncodingStringUtils.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/TransformationUtils/Definitions.h>

#include "SystemVariableDefs.h"
#include "crom.h"
#include "CROMData.h"
#include "resource.h"
#include "TextDocument.h"
#include "DataElementUtils.h"
#include "TreeMapper.h"
#include "TextDocReader.h"

using std::string;
using std::vector;

                                                              
TextDocument::TextDocument(const std::string& sInputFileName, CROM*pDtdRoot, const std::string& batchNodePath0, int batch_size, bool isEDIFACT, bool bIsTarget0) :
	BatchParams(batchNodePath0, batch_size),
	parser(0), pTreeMapper(0), pDtdRoot(0), debug_log(0), pDataRoot(0), m_conv(0),
	str_qual(0), num_qual(0), delimiter(0),
	bIsCsv(true), bIsFlat(true), bIsDatabase(false), bIsLdap(false), bIsFixedField(false), bIsTarget(bIsTarget0),
	m_bCsvNullable(true),
	id_field_pos(0), id_field_length(0), len(0), pos(0)
{
	pTreeMapper = pDtdRoot->getTreeMapper();
	if(pTreeMapper)debug_log = pTreeMapper->getViewLog()->GetDebugLog();

	this->pDtdRoot=pDtdRoot;
	if(pDtdRoot==0){
		throw konga::CrException("TextDocument cntr failed, structure root is missing.");
	}

	if(bIsTarget){
		bIsDatabase = pTreeMapper->GetTargetType()==DbTypeId ;
		bIsFlat = pTreeMapper->IsFlatTarget();
	}
	else {
		bIsDatabase = pTreeMapper->GetSourceType()==DbTypeId || pTreeMapper->GetSourceType()==LdapTypeId;
		bIsFlat = pTreeMapper->IsFlatSource();
	}
	bIsLdap=pDtdRoot->IsLdap();
	if(bIsDatabase){
		str_qual='"';
		num_qual=0;
		delimiter=',';
		bIsCsv = true;
		bIsFixedField=false;
	}
	else {
		CROM*pDtdNode=pDtdRoot;
		if(bIsFlat)pDtdNode=pDtdNode->GetFirstChild();
		str_qual = pDtdNode->get_strQual();
		num_qual = pDtdNode->get_numQual();
		delimiter = pDtdNode->get_delimiter();
		bIsFixedField = delimiter==0;
		bIsCsv = (delimiter!=0 && str_qual=='"' && num_qual==0);
		if(bIsFlat){
			CROM*firstChild=pDtdNode->GetFirstChild();
			assert(firstChild->IsLeaf());
			id_field_pos = 0;
			id_field_length = 0;
		}
		else {
			if(bIsFixedField){
				id_field_pos = pDtdNode->get_fieldPos()-1;
				id_field_length = pDtdNode->get_fieldLength();
			}
		}

		                                        
	}

	                                                                  

	try
	{
		{
			parser=new TextDocReader(sInputFileName, pDtdRoot, bIsFlat, bIsTarget);
			{
				PROFILING prf(pTreeMapper, "JbParser_flat");
				root=parser->parseDocumentWithStructure();
			}
		}

		if(!batchNodePath.empty()){
			prepareBatchLoading(pDtdRoot, parser);
		}
	}
	catch(const konga::CrException& e){
		std::string err="Failed in constructing TextDocument. error: "+e.GetErrorString();
		throw konga::CrException(err);
	}
}

TextDocument::TextDocument() :
	BatchParams("", 0),
	parser(0), pTreeMapper(0), pDtdRoot(0), debug_log(0), pDataRoot(0), m_conv(0),
	str_qual(0), num_qual(0), delimiter(0),
	bIsCsv(true), bIsFlat(true), bIsDatabase(false), bIsLdap(false), bIsFixedField(false), bIsTarget(false),
	m_bCsvNullable(true),
	id_field_pos(0), id_field_length(0), len(0), pos(0)
{
}



TextDocument::~TextDocument(){
	if(parser){
		PROFILING doPrf(pTreeMapper, "delete_JbParser");
		delete parser;
	}
	if(pDataRoot){
		PROFILING doPrf(pTreeMapper, "delete_DataTree");
		pDataRoot->DeleteNode ();
		                  
	}
	delete m_conv;
}

CROMData* TextDocument::createDataTree(long long& nRecords)
{
	nRecords = 0;
	if(pDataRoot)delete pDataRoot;
	pDataRoot=new CROMData(0, 0, pDtdRoot, "");
	createChildren(pDataRoot, root, nRecords);
	return pDataRoot;
}

namespace {
	CROM* findMatchingDtd(CROM*pDtdChild, const std::string& name, CROM*pDtdParent)
	{
		if(pDtdParent==0)return 0;
		if(pDtdChild==0){
			pDtdChild=pDtdParent->GetFirstChild();
		}
		bool isDatabase=pDtdParent->isDB()||pDtdParent->IsLdap();
		CROM*pDtdChild_saved=pDtdChild;
		for(; pDtdChild; pDtdChild=pDtdChild->GetNextSibling()){
			if(pDtdChild->IsLeaf())continue;
			const char* ident=isDatabase? (const char*)pDtdChild->GetName(): pDtdChild->getIdentifierString();
			if(ident!=0 && strcmp(ident, name.c_str())==0){
				return pDtdChild;
			}
		}
		for(pDtdChild=pDtdParent->GetFirstChild(); pDtdChild!=pDtdChild_saved; pDtdChild=pDtdChild->GetNextSibling()){
			if(pDtdChild->IsLeaf())continue;
			const char* ident=isDatabase? (const char*)pDtdChild->GetName(): pDtdChild->getIdentifierString();
			if(ident!=0 && strcmp(ident, name.c_str())==0){
				return pDtdChild;
			}
		}

		if(pDtdChild==0){
			pDtdChild=findMatchingDtd(pDtdParent, name, pDtdParent->GetParent());
		}
		return pDtdChild;
	}
}


void TextDocument::createChildren(CROMData* pParent, konga::JbNode*pParentJbNode, long long& nRecords){
	addLeaves(pParent, pParentJbNode);

	const std::vector<konga::JbNode*>& child_list = pParentJbNode->getChildren();
	const size_t nChild = child_list.size();

	CROM*pDtdParent=pParent->GetDTDNode();
	CROM*pDtdChild=0;
	if(nChild>0){
		int level=-1, batchInstanceCount=1, iChild=0, iChild_saved=0;
		CROM* check=0;
		if(pDtdParent->isBatchParent()){
			check=batch_dtd_child_map.find(pDtdParent)->second;
			if(check==pDtdBatchNode)batchInstanceCount=batchSize;
			level=getBatchLevelFromDtd(check);
			iChild=currentJbNodePositions[level]+1;
			for(int i=level+1;i<nBatchNodes;++i)currentJbNodePositions[i]=-1;
		}
		CROMData* pDataNode=pParent->GetLastChild();
		for(int i=0; i<nChild; ++i){
			konga::JbNode* child=child_list.at(i);
			std::string sTableName(child->getName());
			pDtdChild=findMatchingDtd(pDtdChild, sTableName, pDtdParent);
			if(pDtdChild==0){
				throw konga::CrException("TextDocument create data tree fail, the table name is not recognizable.\n"+
					parser->getStr(child->getPos(), child->getLength()));
			}
			if(pDtdChild==check){
				if(i<iChild || batchInstanceCount<=0)continue;
				--batchInstanceCount;
				iChild_saved=i;
			}
			pDataNode=new CROMData(pParent, pDataNode, pDtdChild, "");
			createChildren(pDataNode, child, nRecords);
		}
		nRecords += nChild;
		if(check){
			if(batchInstanceCount>0)iChild_saved=nChild;
			currentJbNodePositions[level]=iChild_saved;
		}
	}
}

void TextDocument::addLeaves(CROMData*element, konga::JbNode*pJbNode){
	if(pJbNode->getLength()==0)return;
	std::vector<std::string>values;
	std::vector<bool>bIsNulls;
	CROM*pDtdNode=element->GetDTDNode();
	parser->parseJbNode(pJbNode, pDtdNode, values, bIsNulls);
	std::vector<std::string>::const_iterator it1=values.begin();
	if(it1!=values.end()){
		std::vector<bool>::const_iterator it2=bIsNulls.begin();
		if(!bIsFlat && (bIsDatabase || bIsLdap)){
			if(pDtdNode->GetName()!=it1->c_str()){
				std::string errmsg="TextDocument add attribute fail, the table name is not recognizable.\n"+
					parser->getStr(pJbNode->getPos(), pJbNode->getLength());
				throw konga::CrException(errmsg);
			}
			++it1;
			++it2;
		}
		CROMData*pChild=0;
		for(CROM*pDtdLeaf=pDtdNode->GetFirstChild(); pDtdLeaf && pDtdLeaf->IsLeaf() && it1!=values.end();
			pDtdLeaf=pDtdLeaf->GetNextSibling(), ++it1, ++it2)
		{
			std::string value(*it1);
			bool bIsNull(*it2);
			pChild= new CROMData(element, pChild, pDtdLeaf, value.c_str());
			pChild->SetNullFlag(bIsNull);
			if(bIsNull && !bIsDatabase)
			{
				pChild->SetValue((const char*)pChild->GetDefaultValue());
				pChild->SetNullFlag(false);
			}
		}
	}
}

bool TextDocument::readNextBatch(CROMData*beginCROMData, CROM*generator, std::vector<CRData*>&instances)
{
	if(beginCROMData==0)return false;
	CROM *beginDtd=beginCROMData->GetDTDNode(), *dtd=generator->GetParent();
	konga::JbNode* parent=0;
	int endLevel=getBatchLevelFromDtd(generator), level;
	CROMData*parentData;
	if(instances.size()==0){
		level=getBatchLevelFromDtd(beginDtd)+1;
		parentData=beginCROMData;
		currentJbNodePositions[level]=-1;
		while(endLevel<0){
			dtd=dtd->GetParent();
			if(dtd && !dtd->isBatchParent())continue;
			endLevel=getBatchLevelFromDtd(dtd)+1;
			break;
		}
	}
	else {
		parentData=dynamic_cast<CROMData*>(instances.at(0)->GetParent());
		while(endLevel<0 && parentData!=0){
			dtd=dtd->GetParent();
			parentData=parentData->GetParent();
			assert(parentData!=0);
			if(dtd && !dtd->isBatchParent())continue;
			endLevel=getBatchLevelFromDtd(dtd)+1;
			break;
		}
		assert(parentData!=0);
		for(level=endLevel; dtd!=beginDtd && dtd; --level){
			int iChild=currentJbNodePositions[level];
			if(iChild >= 0){
				parent=getCurrentJbNode(level-1);
				assert(parent!=0);
				if(iChild < parent->getChildren().size())break;
			}
			dtd=dtd->GetParent();
			parentData=parentData->GetParent();
			assert(parentData!=0);
		}
		instances.clear();
	}

	bool instance_not_found;
	do {
		assert(level!=0);
		parent=getCurrentJbNode(level-1);
		assert(parentData!=0);
		for(CROMData*child=parentData->GetFirstChild(), *saved; child; child=saved){
			saved=child->GetNextSibling();
			child->DeleteNode();
		}
		long long llTmp = 0;	                                             
		createChildren(parentData, parent, llTmp);
		int err;
		parentData->collectInstances(instances, generator->get_Path(), 0, err);
		if(instances.size()>0)return true;
		instance_not_found=true;
		for(level=endLevel, dtd=generator->GetParent(); dtd!=beginDtd&& dtd && level>=0; --level){
			int iChild=currentJbNodePositions[level];
			if(iChild < 0)continue;
			parent=getCurrentJbNode(level-1);
			assert(parent!=0);
			if(iChild < parent->getChildren().size()){
				instance_not_found=false;
				break;
			}
		}
	} while(!instance_not_found);
	return instances.size()>0;
}

                
void TextDocument::writeCromElement(FILE*pout, CROM*pTableNode, bool bQualifierRequired, bool bIsEDIFACT, konga::Utf8Conv* conv)
{
	char str_qual = pTableNode->get_strQual();
	char num_qual = pTableNode->get_numQual();
	char delimiter = pTableNode->get_delimiter();
	bool bIsCsv = (str_qual=='"' && num_qual==0);

	if(bIsEDIFACT){
		int nTerminators=0;
		for(CROM*pChild=pTableNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling()){
			if(!pChild->IsAttribute() || pChild->IsPrecondition())continue;
			KString value(pChild->GetValue()), sType(pChild->GetValueType());
			if(pChild->IsNull())value="";
			if(stricmp(sType, "IDENTIFIER")==0)value = pChild->get_Format();
            try {etlforge::requireUtf8Text((const char*)value,value.GetLength());}
            catch(const std::exception& failure){throw konga::CrException(failure.what());}
			int len=value.GetLength();
			if(nTerminators>0 && len>0){
				for(int i=0; i<nTerminators; ++i)fputc(delimiter, pout);
				nTerminators=0;
			}
			fputs((const char*)value, pout);
			++nTerminators;
		}
		fputc('\'', pout);
	}
	else if(delimiter==0){
		int maxLength=0;
		for(CROM*pChild=pTableNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling()){
			if(!pChild->IsAttribute() || pChild->IsPrecondition())continue;
			int field_pos_end=pChild->GetFieldPosition()-1+pChild->GetFieldLength();
			if(field_pos_end>maxLength)maxLength=field_pos_end;
		}
        std::vector<std::string> line(maxLength, " ");
        for(CROM* pChild=pTableNode->GetFirstChild();pChild && !pChild->IsElement();pChild=pChild->GetNextSibling()) {
            if(!pChild->IsAttribute() || pChild->IsPrecondition())continue;
            KString value(pChild->GetValue()),sType(pChild->GetValueType());
            if(pChild->IsNull())value="";
            if(stricmp(sType,"IDENTIFIER")==0)value=pChild->get_Format();
            std::string text((const char*)value,value.GetLength());
            try {etlforge::requireUtf8Text(text.data(),text.size());}
            catch(const std::exception& failure){throw konga::CrException(failure.what());}
            int fieldLength=pChild->GetFieldLength(),position=pChild->GetFieldPosition()-1;
            int length=static_cast<int>(konga::EncodingStringUtils::Utf8CharacterSize(text));
            if(length>fieldLength)length=fieldLength;
            if(!pChild->IsKindOfString())position+=fieldLength-length;
            size_t bytePosition=0;
            for(int i=0;i<length;++i) {
                if(position+i<0 || position+i>=maxLength)throw konga::CrException("Invalid fixed-width target field position.");
                size_t next=bytePosition+1;
                while(next<text.size() && (static_cast<unsigned char>(text[next])&0xC0)==0x80)++next;
                line[position+i]=text.substr(bytePosition,next-bytePosition);bytePosition=next;
            }
        }
        for(size_t i=0;i<line.size();++i)fwrite(line[i].data(),1,line[i].size(),pout);
	}
	else {
		bool bStarted=false;
		for(CROM*pChild=pTableNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling()){
			if(!pChild->IsAttribute() || pChild->IsPrecondition())continue;
			KString value(pChild->GetValue()), sType(pChild->GetValueType());
			if(pChild->IsNull())value="";
			if(stricmp(sType, "IDENTIFIER")==0)value = pChild->get_Format();
            try {etlforge::requireUtf8Text((const char*)value,value.GetLength());}
            catch(const std::exception& failure){throw konga::CrException(failure.what());}

			if( conv && pChild->IsStringType() ) {
				value = conv->to((const char*)value);
			}

			if(bStarted){
				fputc(delimiter, pout);
			}
			else {
				bStarted=true;
			}
			if(bIsCsv){              
				const bool bUseStrQual = bQualifierRequired || (value.IsEmpty() && !pChild->IsNull());
				konga::qualifyCsvStringK(value, delimiter, bUseStrQual);
				fputs((const char*)value, pout);
				                                                                             
			}
			else {                                              
				                                                         
				                                                  
				   
				char qual=pChild->IsKindOfString()?str_qual:0;
				if(qual)fputc(qual, pout);
				fputs((const char*)value, pout);
				                                                                             
				if(qual)fputc(qual, pout);
			}
		}
	}
	fputc('\n',pout);
}

                
void TextDocument::writeODBCDataTree(CROMData*pTableNode, FILE*out, bool bQualifierRequired)
{
	bool bStarted=false;
	CROM*pDtd=pTableNode->GetDTDNode();
	bool bIsFlat=pDtd->IsFlat();
	const char delimiter=',';
	if(!bIsFlat){
		KString value=pTableNode->GetName();
		konga::qualifyCsvStringK(value, ',');
		                                                                            
		fputs((const char*)value, out);
		bStarted=true;
	}
	CROM*pDtd_expected=pDtd->GetFirstChild();
	for(CROMData*pChild=pTableNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling(), pDtd_expected=pDtd_expected->GetNextSibling()){
		pDtd = pChild->GetDTDNode();
		if(pDtd->IsPrecondition())continue;
		if(!pDtd->IsLeaf())break;
		assert(pDtd=pDtd_expected);
		KString value(pChild->GetValue());
		const bool bUseStrQual=(value.IsEmpty() && !pChild->IsNull());
		konga::qualifyCsvStringK(value, delimiter, bUseStrQual);
		if(bStarted)fputc(delimiter, out);
		                                                                            
		fputs((const char*)value, out);
		bStarted=true;
	}
	fputc('\n', out);
	for(CROMData* pChild=pTableNode->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
		if(!pChild->IsElement())continue;
		writeODBCDataTree(pChild, out, bQualifierRequired);
	}
}

                
void TextDocument::writeODBCDataTreeToText(CROMData* pDataRoot, const char* sOutputFileName, bool bQualifierRequired)
{
	FILE* out=0;
	if(sOutputFileName && sOutputFileName[0]){
		if(out)fclose(out);
		TreeMapper* pTreeMapper=pDataRoot->getTreeMapper();
		out = fopen(sOutputFileName, pTreeMapper->getWriteModeStr());
		if(!out){
			long lErr=::GetLastError();
			throw konga::CrException(std::string("TextDocument::writeDataTreeToText() failed, cannot open the output file. outputFileName=\"")+sOutputFileName+"\"\nReason: "+ErrorResolver().Resolve(lErr));
		}
	}
	else {
		throw konga::CrException("TextDocument::writeDataTreeToText() failed, output file name is empty.");
	}
	konga::AutoFileHandle adapt(out);
	for(CROMData* pChild=pDataRoot->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
		writeODBCDataTree(pChild, out, bQualifierRequired);
	}
}





