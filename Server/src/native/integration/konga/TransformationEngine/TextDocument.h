/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_TEXTDOCUMENT_H
#define TRANSFORMATION_TEXTDOCUMENT_H

#include <konga/DataElement/DataElement.h>
#include "XmlDocument.h"
#include "TextParser.h"

class CROM;
class TextDocReader;

namespace konga {
	class Utf8Conv;
}

class TextDocument : public BatchParams
{
public:
	TextDocument();
	TextDocument(const std::string& sInputFileName, CROM*pDtdRoot, const std::string& batchNodePath0, int batch_size, bool isEDIFACT, bool bIsTarget);
	~TextDocument();

	                                                                                                       
	                                                                      
	CROMData* createDataTree(long long& nRecords);
	bool readNextBatch(CROMData*beginCROMData, CROM*generator, std::vector<CRData*>&instances);

	static void writeODBCDataTreeToText(CROMData* pDataRoot, const char* sOutputFileName, bool bQualifierRequired);
	konga::JbNode* getRoot(){return root;}
	void setCROMDataRoot(CROMData*pDataRoot){this->pDataRoot=pDataRoot;}

	static void writeCromElement(FILE*out, CROM*pTableNode, bool bQualifierRequired, bool bIsEDIFACT, konga::Utf8Conv* conv);
	bool isFixedField(){return bIsFixedField;}

private:
	static void writeODBCDataTree(CROMData*pTableNode, FILE*out, bool bQualifierRequired);
	void createChildren(CROMData*pParent, konga::JbNode*pParentJbNode, long long& nRecords);
	void addLeaves(CROMData*pParent, konga::JbNode*pParentJbNode);

private:
	TreeMapper* pTreeMapper;
	
	TextDocReader* parser;

	CROM* pDtdRoot;
	FILE* debug_log;

	                              
	CROMData* pDataRoot;

	konga::Utf8Conv* m_conv;
	char str_qual, num_qual, delimiter, SEGMENT_TERMINATOR;
	bool bIsCsv;
	bool bIsFlat;
	bool bIsDatabase;
	bool bIsLdap;
	bool bIsFixedField;
	bool bIsTarget;
	bool m_bCsvNullable;

	int id_field_pos;
	int id_field_length;
	int len;
	int pos;

};

#endif                                 
