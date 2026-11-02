/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_NORMALIZATION_H
#define TRANSFORMATION_NORMALIZATION_H

class CViewLog;
class CErrorLog;
class TreeMapper;
class DTDParser;
class CROM;
class CROMData;
class TextDocument;

class Normalization
{
private:
	TreeMapper*m_pTreeMapper;
	DTDParser* m_pFlatDTDParser;
	CROM* m_pFlatSourceRoot;
	CROM* m_pFlatSourceTable;
	CROM* m_pNormalizedCromRoot;
	bool m_bForceNewLowestNode;
	bool m_bIsDb;
	bool m_fixedField;
	bool m_bIsCsv;
	char m_strQual;
	char m_numQual;
	char m_delimiter;
	bool m_eof;

	int m_n;
	CROM**m_de_list;
	konga::DataElement::Type* m_de_type_list;

	std::map<CROM*,CROMData*>last_data_node_map;
	std::map<std::string,CROM*>mapName2Flat;

public:
	Normalization(DTDParser*pFlatDTDParser, CROM* pTargetRoot);
	~Normalization();

	                                        
	long long createNormalizedDataTree(CROMData*pDataRoot, const std::string& sourceDataFileName, long long& lSourceSize);
	CROM* getFlatSourceTable(){return m_pFlatSourceTable;}


private:
	CViewLog* getViewLog();
	CErrorLog* getErrorLog();

	void createNormalizedStructure(CROM* pTargetRoot);
	void FindHierarch(CROM* pTargetNode, CROM*pNewSourceParentNode, int level, CROM*fixGenerator);
	void AddAttributesFromTargetNode(CROM* pSourceNode, CROM*targetNode);
	int loadFlatDEs(TextDocument& txt, int iRecord, KString& errmsg);
	CROMData* createDataTree(CROMData*parent_node, CROMData*prev_sibling, CROM*dtd, int iInstance);
	CROMData* createDataNode(CROMData*parent_node, CROMData*prev_sibling, CROM*dtd);
	int removeDuplicateChildren(CROMData*_this);
};
#endif                                  
