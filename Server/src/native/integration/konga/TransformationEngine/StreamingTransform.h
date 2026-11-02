/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef _STREAMING_TRANSFORM_H
#define _STREAMING_TRANSFORM_H

#include <vector>
#include <string>

#include "Transform.h"
namespace konga
{
	class DataElement;
	class Utf8Conv;
}

class StreamingTransform
{
	friend class TreeMapper;
public:
	StreamingTransform(TreeMapper*p, CROM*pTarget, const std::string& stInputFileName);
	~StreamingTransform();

	                                                                          
	int ProcessFlatStream(FILE*lnk, long long& sourceRecords, long long& targetRecords);

	                                                                             
	void StartSAXParser(const char*inFileName, CROM*generator, CROM*root, bool bValidateSource, FILE*lnk, DTDParser*dtdParser, long long& sourceRecords, long long& targetRecords);
	CTransform* getTrans() const {return m_pTrans;}

private:
	void PrepareFlatSourceDeList(std::vector<CROM*>&vSourceCromAttribute);
protected:
	int TransformRecord();
	void writeRecord(FILE*lnk, FILE* update_source_file=0);

private:
	friend class TransformSAXParserHandler;
	TreeMapper* m_pTreeMapper;
	konga::Utf8Conv *m_conv_src, *m_conv_tgt;
	CROM* m_pTarget;
	CROM* m_pPrecondition;
	
	std::vector<konga::DataElement*> m_InputDEList;
	char strQualChar, numQualChar, delimiterChar;
	bool bIsCsv, bIsDB, bIsFixField, bSwallowCR;
	bool m_bCsvNullable;
	CViewLog*view_log;
	FILE *out_file;
	std::string m_stInputFileName;
protected:
	CTransform* m_pTrans;
public:
	KString m_lastErr;
};


#endif                          

