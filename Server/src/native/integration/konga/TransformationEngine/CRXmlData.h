/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef TANSFORMATION_ENGINE_CRXML_DATA_H
#define TANSFORMATION_ENGINE_CRXML_DATA_H

#include <string>

#include <konga/Xml/DOMNodePtr.h>

#include "CRData.h"
#include "misc.h"
#include "resource.h"                      

class CRXmlData;
class CRXmlList;
class CRXmlParser;
class CROM;
class CROMData;
class TreeMapper;
class CViewLog;
class CErrorLog;

typedef std::vector<CRXmlData*> InstanceList;

namespace konga {
class JbNode;
}

class CRXmlData : public CRData
{
public:
	enum Type {t_value,t_attribute,t_element,t_root,t_instance_list,t_element_list, t_value_STAX, t_attribute_STAX};
	friend class CRXmlList;

protected:
#ifdef _DEBUG
	std::string m_tag;
#endif
	konga::xml::DOMNodePtr m_domNode;
	konga::JbNode* m_jbNode;
	std::string m_value;
	CRXmlList* m_list;
	Type m_type;
	CRXmlData*m_next;

public:
	static const char*typeStr[];
	CROM*m_dtdNode;
	CRXmlData*m_parent;
	union MAPPINGINFO {int iInstance; CRData* pDataGenerator;} m_MappingInfo;

	CRXmlData(CRXmlData*parent, CROM*dtd, Type type, const std::string&header, FILE*DOMLoop_Debug_log);
	CRXmlData(CRXmlData*parent, konga::JbNode* jbNode, CROM*dtd,Type type, const std::string&header, FILE*DOMLoop_Debug_log);
	CRXmlData(CRXmlData*parent, konga::xml::DOMNodePtr xml, CROM*dtd,Type type, const std::string&header, FILE*DOMLoop_Debug_log);
	CRXmlData(CRXmlData*parent, Type type, CROM*dtd, const std::string& value, FILE*DOMLoop_Debug_log);
	~CRXmlData();

	Type getType(){return m_type;}
	CRXmlList* getList();
	CRXmlList* getChildren() {return m_list;}
	CRXmlList* getSubElementList();
	void deleteList();
	void addCRXmlDataForLeaves(CROM*dtd, std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, std::vector<konga::xml::DOMNodePtr>*pNodeList, const std::string&header, FILE*DOMLoop_Debug_log);
	CRXmlData* createCRXmlDataForSubElements(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, std::vector<konga::xml::DOMNodePtr>&node_list, const std::string&header, FILE*DOMLoop_Debug_log);
	void completeChildDataTree1(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, const std::string&header, FILE*DOMLoop_Debug_log);
	void completeChildDataTree(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, const std::string&header, FILE*DOMLoop_Debug_log);
	void addChildToList(CRXmlData*child,const std::string&header);
	void removeChildren();
	void removeChild(CRXmlData*child);

	CRXmlData*findInstance(const KString&path,int&err);
	                                                                              
	InstanceList*findInstanceList(const KString&endPath,int&err);

	int addToParent(const std::string&header,CRXmlParser*xmlParser);
	std::string getXmlString();
	bool isSimple(){return m_type==t_element;}
	bool isValueNode(){return (m_type==t_value || m_type==t_value_STAX);}
	bool isNil();
	std::string getDisplayString();
	std::string getTagString(){
#ifdef _DEBUG
		return m_tag;
#else
		return std::string();
#endif
	}

#ifdef _DEBUG
	static void resetLabel();
	static int getLabel();
#endif

	std::string getDtdPath();
	bool isEmpty();

private:
	CViewLog* getViewLog();
	CErrorLog* getErrorLog();
	CRXmlData*updateInstanceGroup(CRXmlData*instanceGroup,CROM*dtd0,CROM*dtd,const std::string&header, FILE*DOMLoop_Debug_log);
	bool isTestTransform();

	                           
	void Init(const std::string&header);

	                                        
public:
	CRData*GetParent(){return m_parent;}
	const KString& getPath();
	void collectInstances(std::vector<CRData*>&v, const KString& sEndPath, CROM*targetLoopNode0, int&err);
	bool IsNull(){return isNil();}
	CRData*find(const KString& path, int& err);
	CROM* GetDTDNode() const {return m_dtdNode;}
	std::string getValue();
	konga::xml::DOMNodePtr getXml();
	konga::JbNode* getJbNode();
	void SetDataGenerator(CRData*pGenerator){m_MappingInfo.pDataGenerator=pGenerator;}
	void SetInstanceNumber(int i){m_MappingInfo.iInstance=i;}
	int GetInstanceNumber();
	void getInstances(std::vector<CRData*>&list, const char*nodeName);
	TreeMapper*getTreeMapper();
	CRData*getChildNode(const char*elementName, bool isLoop);
	CRData*getAttributeNode(const char*AttributeName);
	CRData*getValueNode();
	void get_non_white_space_children(konga::xml::DOMNodePtr node, std::vector<konga::xml::DOMNodePtr>& node_list);
	void writeDebugDataTree(FILE*out, const std::string& header);
};


#endif                                     

