/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_TRANSFORMATION_ENGINE_CROM_TO_JTR_WRITER_H
#define ETLFORGE_TRANSFORMATION_ENGINE_CROM_TO_JTR_WRITER_H

#include "AbstractCromToJtrWriter.h"

class CROM;

class CROMHeader {
	std::string jtrType;
	int version;
	std::string beginQuote, endQuote;
	CROM* root;
public:
	CROMHeader(CROM* pDtdRoot);
	CROMHeader(CROM* pDtdRoot, int version, const std::string& beginQuote, const std::string& endQuote);

	CROM* getRoot(){return root;}
	const std::string& getJtrType(){return jtrType;}
	int getVersion(){return version;}
	const std::string& getBeginQuote(){return beginQuote;}
	const std::string& getEndQuote(){return endQuote;}
};


namespace konga {
	namespace xml {
		class XmlStringBuilder;
	}
}

class CromToJtrWriter : public AbstractCromToJtrWriter {
public:
	CromToJtrWriter() {}
	virtual ~CromToJtrWriter() {}

public:
	void createJtr(CROMHeader header, const std::string& outputFilePath);
	void WriteCromTree(CROM* node, konga::xml::XmlStringBuilder& xml);

protected:
	virtual void WriteExtensionData(CROM* node, konga::xml::XmlStringBuilder& xml);

private:
	void WriteBasicCromNodeInfo(CROM* node, konga::xml::XmlStringBuilder& xml);
	void WriteExtAttribute(CROM* node, konga::xml::XmlStringBuilder& xml);

private:
	CROM::CROM_ext_type crom_type;
};

void WriteXmlToOutputFile(const konga::xml::XmlStringBuilder& xml, const std::string& filePath);

#endif

