/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __XMLDOCUMENT_H_
#define __XMLDOCUMENT_H_

#include <konga/JbParser/JbParser.h>

#include "BatchParams.h"
#include "XmlReader.h"

class CROM;
class CRData;
class CRXmlData;
class DTDParser;

class XmlDocument : public BatchParams
{
	TreeMapper*pTreeMapper;

	konga::XmlReader* parser;

	CROM* pDtdRoot;
	FILE* debug_log;

	                             
	CRXmlData* pDataRoot;

	std::map<std::string,CROM*>&elementTypes;
	std::map<std::string,CROM*>&elements;


private:

	void addLeaves(CRXmlData*element, konga::JbNode*node);
	void createChildren(CRXmlData*pParent0, konga::JbNode*pParentJbNode);
	CRXmlData* createElement(CRXmlData*pParent0, CRXmlData*pPrevChild, CROM*dtd, CROM*from_dtd, konga::JbNode* stax_child);
	CRXmlData* generateTransientParent(CRXmlData*data, CRXmlData* instance_list, CROM*dtd, FILE*debug_log);
	CROM* getBatchChildFromDtd(CROM*dtd);
	NodePatternMatch* findMatchingDtdList(CROM*dtd_derived, std::vector<konga::JbNode*>&child_list);


public:
	XmlDocument(const std::string& sInputFileName, DTDParser*dtdParser, const std::string& batchNodePath, int batch_size);
	virtual ~XmlDocument();

	                                                                                                                     
	unsigned long createDataTree();
	bool readNextBatch(CRXmlData*beginCROMData, CROM*generator, std::vector<CRData*>&instances);

	CRXmlData* createCRXmlDataChildren(konga::JbNode*node, CRXmlData*parent);
	CRXmlData* getDataRoot(){return pDataRoot;}
	int getBatchSize(){return batchSize;}
	TreeMapper*getTreeMapper(){return pTreeMapper;}

};


#endif                    
