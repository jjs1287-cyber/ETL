/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef CROMDatah
#define CROMDatah
#include <konga/Db/DbUtils/DbConnection.h>

#include "RetVal.h"
#include "crom.h"
#include "CRData.h"
#include "TxtParser.h" 

class TreeMapper;
class CMFParser;
class CDbAccess;
class CRXmlData;

class CROMData : public CRData
{
	bool m_null;
	CROM*m_DTDNode;
	CROMData *m_Parent, *m_FirstChild, *m_NextSibling, *m_PrevSibling;
	union MAPPINGINFO {int iInstance; 
					   CRData* pDataGenerator;} m_MappingInfo;
	KString m_Value;
	KString m_PostProcessValue;

public:
	CROMData(CROMData *parent, CROMData *prevSibling, CROM*dtdNode, const char *value);
	CROMData(CROMData *to_be_cloned);
	CROMData(){}

	CROMData* Clone(CROMData* pParentNode);
	bool AddChildNode(CROMData* pChild);
	
	void SetParent(CROMData*obj){m_Parent=obj;}
	CROMData *GetFirstChild() const {return m_FirstChild;}
	CROMData *GetFirstSibling() const;
	void SetFirstChild(CROMData*obj) {m_FirstChild=obj;}
	CROMData *GetLastChild() const;
	CROMData *GetRoot() const;
	CROMData *GetNextSibling() const;
	CROMData *GetNextCousin() const;
	CROMData *GetFirstValueAttribute();
	CROMData *GetNextValueAttribute();
	CROMData *GetSibling(int iInstNum) const;
	void SetNextSibling(CROMData*obj);
	CROMData *GetPreviousSibling()const;
	void SetPreviousSibling(CROMData*obj){m_PrevSibling=obj;}
	int CountChildren();
	int CountSibling();
	void Clear();

	const KString& GetNodePath();
	KString GetName();
	const KString& GetDisplayName();
	KString&GetValue();
	bool IsValueEmpty() const;
	size_t GetValueLength() const;
	const char* GetValueType();
	KString GetValueTypeStr();
	void SetValueType(KString ValType);
	void SetValue(const char*str);
	int GetType();
	KString GetDefaultValue() const;
	KString GetFormat() const;
	CROM* GetDTDParent() const;
	void SetDTDNode(CROM* pDtdNode);

	bool IsOneOfNode(){return (m_DTDNode->m_Type&CROM_t_OneOf)!=0;}
	bool IsCollection(){return (m_DTDNode->m_Type&CROM_t_Collection)!=0;}
	bool IsOneOf(){return m_Parent&&(m_Parent->m_DTDNode->m_Type&CROM_t_OneOf)!=0;}
	bool IsOptional(){return (m_DTDNode->m_Type&CROM_t_Optional)!=0;}
	bool IsAtLeastOne(){return (m_DTDNode->m_Type&CROM_t_AtLeastOne)!=0;}
	bool IsElement() {
		return (m_DTDNode->m_Type&CROM_t_Element)!=0;
	}
	bool IsRoot() {
		return (m_DTDNode->m_Type&CROM_t_Root)!=0;
	}
	bool IsLeaf() {return m_DTDNode->IsLeaf();}
	bool IsAttribute() const {return (m_DTDNode->m_Type&CROM_t_Attribute)!=0;}
	bool IsNotALeaf() {return !IsLeaf();}
	bool IsCollectionOfElements();
	bool IsOneToOne();
	bool HasSubElement();
	bool HasChildren(){return m_FirstChild!=0;}

	CROMData* GetAttribute(CROM*pAttribute);
	CROMData *FindFirstObject(const char* name,int type=CROM_t_Element);
	CROMData *FindObjectInChild(const char*name,int type=CROM_t_Element);
	CROMData* FindNode(KString NodeName, KString NodeValue);

	CROMData* FindValue(const char *TableName, const char *FieldName, KString& Result, KString& DataTypeStr, CMFParser* pMFParser, bool bFlatFile);
	CROMData* FindValue(KString sSearchPath, KString sReqField);

	bool GetForeignKeyValue(const char* szPrimaryKeyName, KString& Value);
	const char* GetLinkToPrimaryKey();

	void DeleteNode();

	void write(FILE*out,bool bIncludeInstance=false);
	KString	GetTreeText(bool showNodeType=true);

	bool ArePathsEqual(KString sEndPath);
	CROMData* FindCompoundKeyNode(KString CompoundValue, CROMData* pTbl, std::vector<KString>& KeyColumns);
	CROMData* FindCompoundKeyNode(KString CompoundValue, CROMData* pTbl, STRMAP_T *pColValues);
	unsigned long GetCompoundKeyValue(CROMData* pObj, KString& CompoundValue, std::vector<KString>& KeyColumns);
	unsigned long GetCompoundKeyValue(CROMData* pObj, KString& CompoundValue, STRMAP_T *pKeyCols);

	static bool BuildInsertUpdateSQL(const char* szDriverName, const KString& TableName, const char*dbSchema,
		const std::map<KString,KString>& ColName_Value, const std::vector<CROM*>& TableCols,
		KString& InsertSql, KString& UpdateSql, KString& DeleteSql,
		std::vector<CColValType>& paramValues,
		std::vector<CColValType>& WhereValues, std::vector<CColValType>& UpdateValues,
		TreeMapper*p, const bool isTarget, const bool bTruncateCharValues);
	void SetNullFlag(bool b=true){m_null=b;}

	short GetFieldLength();
	void SetFieldLength(short iLen);
	short GetFieldPosition();
	void SetFieldPosition(short iPos);
	char GetDelimiter();
	char GetStringQualifier();
	konga::DataElement::Type get_DataElementType(){
		return GetDTDNode()->get_DataElementType();
	}

	CROMData* FindDataNode(const char* sName);

	KString GetPrintValue();
	CROMData* AddAttribute(const std::vector<std::string>&values, const std::vector<bool>&nulls,bool bIsDatabase);

	                                        
public:
	CROMData *GetParent(){return m_Parent;}
	const KString& getPath() {return m_DTDNode->get_Path();}
	void collectInstances(std::vector<CRData*>&v, const KString& sEndPath, CROM*targetLoopNode0, int&err);
	bool IsNull();
	CRData*find(const KString& path, int& err);
	CROM* GetDTDNode() const;
	std::string getValue();
	konga::xml::DOMNodePtr getXml();
	konga::JbNode* getJbNode();
	void SetDataGenerator(CRData* pGenerator){m_MappingInfo.pDataGenerator=pGenerator;}
	void SetInstanceNumber(int iInstance){m_instanceNumber=iInstance;}
	int  GetInstanceNumber();
	void getInstances(std::vector<CRData*>&list, const char*nodeName);
	TreeMapper*getTreeMapper(){return m_DTDNode->getTreeMapper();}
	CRData*getChildNode(const char*elementName, bool isLoop);
	CRData*getAttributeNode(const char*AttributeName);
	CRData*getValueNode();
	int removeChildOfCrom(CROM*childCrom);
};

#endif
