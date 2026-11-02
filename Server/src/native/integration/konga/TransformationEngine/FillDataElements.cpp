/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/KString.h>
#include <konga/DataElement/DataElement.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "DTDParser.h"
#include "Transform.h"
#include "TxtParser.h"
#include "CRXmlParser.h"
#include "CRData.h"

                                  
#ifdef LoadString
#undef LoadString
#endif

using konga::CrFatalException;
using konga::DataElement;
using std::string;

struct InstanceListWithTag
{
	InstanceList*m_list;
	KString m_tag;
	InstanceListWithTag(InstanceList*list,const KString&tag){
		m_list=list;
		m_tag=tag;
	}
	~InstanceListWithTag(){
		if(m_list)delete m_list;
	}
};

namespace {
	CRData* getDeSourceData(const char*deName, CRData*curSourceData){
		for(CRData*data=curSourceData; data!=0; data=data->GetParent()){
			KString path=data->GetDTDNode()->get_Path();
			int n=path.GetLength();
			if(n==0)break;
			if(path.GetAt(0)=='['){
				if(strncmp(deName, &((const char*)path)[1], n-2)==0)return data;
			}
			else {
				if(strncmp(deName, (const char*)path, n)==0)return data;
			}
		}
		return curSourceData;
	}
}
 

                                                                                      
bool TreeMapper::FillDataElements(CRData*curSourceData0, CROM*targetNode)
{
	if(targetNode->IsLeaf())return false;
	CTransform* pTrans=targetNode->getTrans();
	std::map<konga::DataElement*,CRData*>& de_CRData_map(pTrans->de_CRData_map);
	const size_t n=pTrans->getInputDEListSize();
	const bool isCvtFromFlat = IsFlatSource();

	const string stCurSourceDataMessage=IsNoSource()?"NoSource":(curSourceData0?((const char*)curSourceData0->getPath()):"0");
	const bool bDebug = getViewLog()->IsDebugLogEnabled();
	if( bDebug ) {
		getViewLog()->DebugString("FillDataElements for targetNode=\"%s\", curSourceData0=\"%s\" px=0x%p, n=%d",
			(const char*)targetNode->GetName(),stCurSourceDataMessage.c_str(),curSourceData0,n);
	}

	bool ret = false;
	KString deName;
    DataElement*ds=0;
	bool bUseJbNode=useJbNode();
    for(size_t i=0; i<n; i++) {
		ds=pTrans->getInputDEOf(i);
		try {
			if(isCvtFromFlat){
				deName=ds->getName();
				ds->clearValue();
				ds->setNull();
				CRData::fillDataElement(curSourceData0, ds, deName, 0, bUseJbNode);
			}
			else {
				deName=ds->getName();
				std::map<konga::DataElement*,CRData*>::const_iterator it=de_CRData_map.find(ds);
				CRData* deCRData=getDeSourceData((const char*)deName,curSourceData0);
				CRData* deCRData0=it!=de_CRData_map.end()?it->second:0;
				if(deCRData==deCRData0)continue;
				de_CRData_map[ds]=deCRData;
				ds->clearValue();
				ds->setNull();
				int pos=0;
				if(curSourceData0==0) {
					throw konga::CrException(string("Cannot find common parent for data element \"") + (const char*)deName
						+ "\" in transformation \"" + getTransformationName()
						+ "\". Maybe the transformation has invalid mappings. Try to refresh the transformation and deploy it again.");
				}
				CRData* curSourceData=curSourceData0->findCommonParent((const char*)deName, pos);
				CRData::fillDataElement(curSourceData, ds, deName, pos, bUseJbNode);
				if ( bDebug ) {
					int err;
					const KString& tmp = ds->GetString(err);
					if(err==0)getViewLog()->DebugString("deName=%s, value=%s", (const char*)deName, (const char*)tmp);
				}
			}
			ret = true;
    	}
		catch(const InstanceNotFoundException&){
			m_bErrorsInOutput = true;
			ds->setNull();
			ds->setError(DataElement::t_unknown);
			if ( bDebug ) {
				int err;
				ds->GetString(err);
				if(err==0)getViewLog()->DebugString("deName=%s, value is null", (const char*)ds->getName());
			}
		}
		catch(const konga::CrException& e) {
			m_bErrorsInOutput = true;
			ds->setNull();
			ds->setError(DataElement::t_unknown);
			std::string errmsg=e.GetErrorString();
			getViewLog()->LogMessage("WARNING : FillDataElements() failed:  Source Path:\"%s\", Value Path:\"%s\" errmsg:%s\n",
				curSourceData0?(const char*)curSourceData0->GetDTDNode()->get_Path():"0",(const char*)deName, errmsg.c_str());
		}
	}
	return ret;
}


