/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef CRDATA_H
#define CRDATA_H

#include <konga/Utils/CrException.h>
#include <konga/DataElement/DataElement.h>

#include "crom.h"

class CRData;
class TreeMapper;

namespace konga {
	class DataElement;
	class JbNode;
}

class CRData_list
{
	friend class CRData;
protected:
	std::vector<CRData*> m_list;

	CRData_list(CRData*data, const char*nodeName);
	CRData* at(int i){return (i>=0&&i<(int)m_list.size())?m_list.at(i):0;}
	size_t size(){return m_list.size();}
	void fillDataElementForArray(konga::DataElement*ds, const char* deName, int start_pos, bool bUseJbNode);
};


class CRData
{
public:
	CRData(void) {m_instanceNumber=-1;}
	virtual ~CRData(void) {}

public:
	bool IsLoopNode();
	std::string getInstanceListing();
protected:
	int m_instanceNumber;

public:
	virtual CRData*GetParent() = 0;
	virtual const KString& getPath() = 0;
	virtual void collectInstances(std::vector<CRData*>&v, const KString& sEndPath, CROM*targetLoopNode0, int&err) = 0;
	virtual bool IsNull() = 0;
	virtual CRData*find(const KString& path, int& err) = 0;
	virtual CROM* GetDTDNode() const = 0;
	virtual std::string getValue() = 0;
	virtual konga::xml::DOMNodePtr getXml() = 0;
	virtual konga::JbNode* getJbNode() = 0;
	virtual void SetDataGenerator(CRData* pGenerator) = 0;
	virtual void SetInstanceNumber(int i) = 0;
	virtual int GetInstanceNumber() = 0;
	virtual void getInstances(std::vector<CRData*>&list, const char*nodeName) = 0;
	virtual TreeMapper* getTreeMapper() = 0;
	virtual CRData*getChildNode(const char*elementName, bool isLoop) = 0;
	virtual CRData*getAttributeNode(const char*attributeName) = 0;
	virtual CRData*getValueNode() = 0;
	                                                                    
	CRData*findCommonParent(const char*path,int&pos);
	static void fillDataElement(CRData*node, konga::DataElement*de, const char*path, size_t pos, bool bUseJbNode);
};

class InstanceNotFoundException : public konga::CrException {
public:
	InstanceNotFoundException(const std::string& errStr) : CrException(errStr) {}
	virtual ~InstanceNotFoundException() {}
};

#endif            
