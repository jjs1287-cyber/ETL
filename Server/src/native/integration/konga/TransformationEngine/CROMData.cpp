/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqlext.h>

#include <map>
#include <algorithm>		            

#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/date.h>
#include <konga/Utils/Settings.h>		                      

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DataElement/DataElement.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ErrorConfiguration.h>
#include <konga/TransformationUtils/DateFormatUtils.h>
#include <konga/TransformationUtils/Definitions.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "TxtParser.h"
#include "Transform.h"
#include "CROMData.h"
#include "DbAccess.h"
#include "CRXmlData.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

bool DataTreeIsTarget(DataTreeType treeType);

CROMData::CROMData(CROMData *parent, CROMData *prevSibling, CROM*dtdNode, const char *value)
	:	m_null(false),
	    m_DTDNode(dtdNode), m_Parent(parent), m_FirstChild(0), m_NextSibling(0), m_PrevSibling(prevSibling),
		m_Value(value)
{
	m_MappingInfo.iInstance=0; m_MappingInfo.pDataGenerator=0; m_MappingInfo.pDataGenerator=0;
	if(m_PrevSibling){
		CROMData*tmp=m_PrevSibling->m_NextSibling;
		m_PrevSibling->m_NextSibling=this;
		m_NextSibling=tmp;
		if(tmp)tmp->m_PrevSibling=this;
	} else if(m_Parent){
		m_NextSibling=m_Parent->m_FirstChild;
		m_Parent->m_FirstChild=this;
		if(m_NextSibling)m_NextSibling->m_PrevSibling=this;
	} else {
		m_NextSibling=0;
		m_PrevSibling=0;
	}
}

CROMData::CROMData(CROMData *to_be_cloned)
	: m_null(to_be_cloned->m_null), 
	m_DTDNode(to_be_cloned->m_DTDNode),
	m_Parent(0), m_FirstChild(0), m_NextSibling(0), m_PrevSibling(0),
	m_MappingInfo(to_be_cloned->m_MappingInfo),
	m_Value(to_be_cloned->m_Value)
{
	CROMData*_child=to_be_cloned->GetFirstChild(),*cloned_child=0,
		*prev_cloned_child;
	while(_child){
		prev_cloned_child=cloned_child;
		cloned_child=new CROMData(_child);
		cloned_child->SetParent(this);
		if(prev_cloned_child==0)m_FirstChild=cloned_child;
		else prev_cloned_child->SetNextSibling(cloned_child);
		cloned_child->SetPreviousSibling(prev_cloned_child);
		_child=_child->GetNextSibling();
	}
}

CROMData *CROMData::GetLastChild() const
{
	CROMData*child_saved=0;
	for(CROMData*child=GetFirstChild(); child; child=child->GetNextSibling()){
		child_saved=child;
	}
	return child_saved;
}

CROMData*CROMData::GetFirstSibling() const {
	if(m_Parent)return m_Parent->GetFirstChild();
	CROMData*child=const_cast<CROMData*>(this),*first_child=0;
	while(child){
		first_child=child; 
		child=child->GetPreviousSibling();
	}
	return first_child;
}

CROMData *CROMData::GetRoot() const
{
	CROMData*parent=m_Parent,*tmp=const_cast<CROMData*>(this);
	while(parent){
		tmp=parent;
		parent=parent->GetParent();
	}
	return tmp;
}

void CROMData::Clear(){
	for(CROMData *pChild=GetFirstChild(),*next; pChild; pChild=next){
		next=pChild->GetNextSibling();
		pChild->DeleteNode();
	}
}

int CROMData::CountChildren(){
	return m_FirstChild?m_FirstChild->CountSibling():0;
}

int CROMData::CountSibling(){
	int n=1;
	CROMData*child=m_PrevSibling;
	while(child){
		n++;
		child=child->m_PrevSibling;
	}
	child=m_NextSibling;
	while(child){
		n++;
		child=child->m_NextSibling;
	}
	return n;
}

namespace {
	const KString empty_str("");
}

CROMData* CROMData::GetAttribute(CROM*pAttribute)
{
	for(CROMData*child=GetFirstChild(); child; child=child->GetNextSibling())
	{
		if(child->m_DTDNode == pAttribute)return child;		
	}
	return 0;
}

CROMData* CROMData::GetFirstValueAttribute(){
	CROMData*child=GetFirstChild();
	if(child==0)return 0;
	if((child->GetType() & CROM_t_AttV)!=0)return child;
	if(child->IsElement())return 0;
	return child->GetNextValueAttribute();
}

CROMData* CROMData::GetNextValueAttribute(){
	for(CROMData*sibling=GetNextSibling(); sibling; sibling=sibling->GetNextSibling()){
		if((sibling->GetType() & CROM_t_AttV)!=0)return sibling;
		if(!sibling->IsLeaf())return 0;
	}
	return 0;
}

CROMData*CROMData::FindFirstObject(const char* name,int type)
{
	if((m_DTDNode->m_Type&type) != 0){
		                                                                      
		                                                                              
		                                                                                     
		                                                                                                     
		                                                                           
		if(m_DTDNode->GetName() == name || m_DTDNode->GetDisplayName() == name)
			return this;
	}
	CROMData*child=GetFirstChild(),*obj;
	while(child){
		if((obj=child->FindFirstObject(name,type))!=0)return obj;
		child=child->GetNextSibling();
	}
	return child;                      
}

CROMData*CROMData::FindObjectInChild(const char* name,int type)
{
	for(CROMData*child=GetFirstChild(); child; child=child->GetNextSibling())
	{
		CROM*dtdNode=child->m_DTDNode;
		if((dtdNode->m_Type&type)==0)continue;
		                                                                      
		                                                                              
		                                                                                     
		                                                                                                     
		                                                                           
		if (dtdNode->GetName() == name || dtdNode->GetDisplayName() == name)return child;
	}
	return 0;                      
}

void CROMData::DeleteNode()
{
	CROMData*child;
	while( (child=GetFirstChild()) != 0 ){
		child->DeleteNode();
	}
	if(m_NextSibling)m_NextSibling->m_PrevSibling=m_PrevSibling;
	if(m_PrevSibling){
		m_PrevSibling->m_NextSibling=m_NextSibling;
	} else if(m_Parent){
		m_Parent->m_FirstChild=m_NextSibling;
	}
	delete this;
}

KString GetVerboseType(int Type);

void CROMData::write(FILE*out,bool bIncludeInstance          )
{
	CROMData*parent=m_Parent;
	KString header;
	while(parent)
	{
		header="   "+header;
		parent=parent->GetParent();
	}

	if (0 != out){
		KString treeText(GetTreeText());
		fprintf(out,"%s%s 0x%X(%s)",(const char*)header, (const char*)treeText, GetType(), (const char*)GetVerboseType(GetType()));
		if(bIncludeInstance&&m_MappingInfo.iInstance!=0){
			fprintf(out," -- Instance: %d", m_MappingInfo.iInstance);
		}
		fprintf(out,"\n");
	}

	CROMData*child=GetFirstChild();
	while(child)
	{
		child->write(out,bIncludeInstance);
		child=child->GetNextSibling();
	}
}

KString CROMData::GetName()
{
	if (m_DTDNode)
		return m_DTDNode->GetName();
	else
		return "";
}

namespace {
	const KString emptyString;
}

const KString& CROMData::GetDisplayName()
{
	if (m_DTDNode)
		return m_DTDNode->GetDisplayName();
	else
		return emptyString;
}

bool CROMData::IsValueEmpty() const {
	return m_Value.IsEmpty();
}

size_t CROMData::GetValueLength() const {
	return m_Value.GetLength();
}

KString& CROMData::GetValue()
{
	return m_Value;
}

const char* CROMData::GetValueType()
{
	if (m_DTDNode)
		return m_DTDNode->GetValueType();
	else
		return "";
}

void CROMData::SetValueType(KString ValType)
{
	if (m_DTDNode)
		m_DTDNode->SetValueType(ValType);
}

void CROMData::SetValue(const char*str)
{
	m_Value=str;
}

int CROMData::GetType()
{
	if (m_DTDNode)
		return m_DTDNode->m_Type;
	else
		return 0;
}

KString	CROMData::GetTreeText(bool showNodeType)
{
	KString TreeText;

	if (m_DTDNode)
		TreeText = m_DTDNode->GetTreeText(m_Value,showNodeType);

	return TreeText;
}

short CROMData::GetFieldLength() 
{ 
	if (m_DTDNode)
		return m_DTDNode->GetFieldLength();
	else
		return 0;
}

void CROMData::SetFieldLength(short iLen) 
{ 
	if (m_DTDNode)
		m_DTDNode->SetFieldLength(iLen);
}

short CROMData::GetFieldPosition() 
{ 
	if (m_DTDNode)
		return m_DTDNode->GetFieldPosition();
	else
		return -1;
}

void CROMData::SetFieldPosition(short iPos) 
{ 
	if (m_DTDNode)
		m_DTDNode->SetFieldPosition(iPos);
}

char CROMData::GetDelimiter() 
{ 
	return m_DTDNode->get_delimiter();
}

char CROMData::GetStringQualifier() 
{ 
	return m_DTDNode->get_strQual();
}

const char* CROMData::GetLinkToPrimaryKey() 
{
	return m_DTDNode->get_LinkToPrimaryKey();
}

CROM* CROMData::GetDTDParent()  const
{
	return m_DTDNode->GetParent();
}

void CROMData::SetDTDNode(CROM* pDtdNode)
{
	m_DTDNode = pDtdNode;
}

CROMData* CROMData::GetNextSibling() const
{
	return m_NextSibling;
}

CROMData* CROMData::GetNextCousin() const {
	for(CROMData* p0=GetNextSibling(); p0; p0=p0->GetNextSibling()){
		if(p0->m_DTDNode == m_DTDNode)return p0;
	}
	if(m_Parent==0)return 0;
	for(CROMData*p1=m_Parent->GetNextCousin(); p1; p1=p1->GetNextCousin()){
		for(CROMData*p0=p1->GetFirstChild(); p0; p0=p0->GetNextSibling()){
			if(p0->m_DTDNode == m_DTDNode)return p0;
		}
	}
	return 0;
}

void CROMData::SetNextSibling(CROMData*obj)
{
	m_NextSibling=obj;
}

CROMData* CROMData::GetPreviousSibling() const
{
	return m_PrevSibling;
}

                                 
                                                                                                            
                                                       
                                                               
                                                         
                                                                                                               
                                                                                                               
  
unsigned long CROMData::GetCompoundKeyValue(CROMData* pObj, KString& CompoundValue, STRMAP_T *pKeyCols)
{
	unsigned long hr = S_OK;

	                                                          
	STRMAP_T::const_iterator cit = pKeyCols->begin();

	while(cit != pKeyCols->end())
	{
		KString ColumnName = cit->first;
		CROMData *pChild = 0;
		                                  
		pChild = (CROMData*) pObj->FindFirstObject(ColumnName, CROM_t_Element|CROM_t_Attribute);
		if (0 == pChild)
		{
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_GETCOMPOUNDKEY_UNABLE_TO_FIND_KEY_COLUMN_FOR_TABLE0, (const char*)ColumnName, (const char*)pObj->GetName());
			getTreeMapper()->m_lastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return hr;
		}
		                                                
		CompoundValue += pChild->m_Value + " ";
		cit++;
	}

	return hr;
}

                                 
                                                                                                            
                                                       
                                                                 
                                                         
                                                                                                               
                                                                                                               
  
unsigned long CROMData::GetCompoundKeyValue(CROMData* pObj, KString& CompoundValue, vector<KString>& KeyColumns)
{
	unsigned long hr = S_OK;

	                                                          
	for (int j = 0; j < (int)KeyColumns.size(); j++)
	{
		CROMData *pChild = 0;
		                                  
		pChild = (CROMData*) pObj->FindFirstObject(KeyColumns[j], CROM_t_Attribute);
		if (0 == pChild)
		{			
			hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_GETCOMPOUNDKEY_UNABLE_TO_FIND_KEY_COLUMN_FOR_TABLE1,(const char*) KeyColumns[j],(const char*)pObj->GetName());
			getTreeMapper()->m_lastErr = getTreeMapper()->getErrorLog()->GetLastErr();
			return hr;
		}
		                                                
		CompoundValue += pChild->m_Value + " ";
	}
	return hr;
}

                                 
                                                      
                                                      
                                                      
                                                            
                                                                                                       
  
CROMData* CROMData::FindCompoundKeyNode(KString CompoundValue, CROMData* pTbl, STRMAP_T *pColValues)
{
	CROMData* pChildNode = (CROMData*) pTbl->GetFirstChild();

	if (0 == pChildNode)
	{
		getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_THERE_IS_NO_DATA_FOR_TABLE0, (const char*)pTbl->GetName());
		getTreeMapper()->m_lastErr = getTreeMapper()->getErrorLog()->GetLastErr();
		return 0;
	}

	                                 
	while (pChildNode != 0)
	{
		                                       
		KString ChildValue;
		if(GetCompoundKeyValue(pChildNode, ChildValue, pColValues)!=S_OK)
			return 0;
		
		                                                                                 
		if (ChildValue == CompoundValue)
			return pChildNode;

		                       
		pChildNode = (CROMData*) pChildNode->GetNextSibling();
	}
	return 0;
}

                                 
                                                      
                                                      
                                                      
                                                            
                                                                                                       
  
CROMData* CROMData::FindCompoundKeyNode(KString CompoundValue, CROMData* pTbl, vector<KString>& KeyColumns)
{
	CROMData* pChildNode = (CROMData*) pTbl->GetFirstChild();

	if (0 == pChildNode)
	{
		getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_THERE_IS_NO_DATA_FOR_TABLE1, (const char*)pTbl->GetName());
		getTreeMapper()->m_lastErr = getTreeMapper()->getErrorLog()->GetLastErr();
		return 0;
	}

	                                 
	while (pChildNode != 0)
	{
		                                       
		KString ChildValue;
		if(GetCompoundKeyValue(pChildNode, ChildValue, KeyColumns)!=S_OK)
			return 0;
		
		                                                                                 
		if (ChildValue == CompoundValue)
			return pChildNode;

		                       
		pChildNode = (CROMData*) pChildNode->GetNextSibling();
	}
	return 0;
}


bool CROMData::AddChildNode(CROMData* pNewChild)
{
	CROMData* pLastChild = GetFirstChild();
	                                                                                            
	if (0 != pLastChild)
	{
		while (0 != pLastChild->GetNextSibling())
		{
			pLastChild = pLastChild->GetNextSibling();
		}
	}
	else
	{
		SetFirstChild(pNewChild);
	}

	pNewChild->SetParent(this);
	pNewChild->SetPreviousSibling(pLastChild);
	pNewChild->SetNextSibling(0);
	if (0 != pLastChild)
	{
		pLastChild->SetNextSibling(pNewChild);
	}

	return true;
}


                                        
                                                                                                                 
                            
                                                 
const KString& CROMData::GetNodePath()
{
	static KString blank("");

	if (m_DTDNode)
		return m_DTDNode->get_Path();
	else
		return blank;
}


                                
                                                                                       
                                                              
                                                               
                                                                                           
                                                                                     
                                                                                     
                                                                                                  
                                                                                    
                                                                                          
                                                                
bool CROMData::GetForeignKeyValue(const char* szPrimaryKeyName, KString& Value)
{
	CROMData* pNode = this;

	if(this->IsAttribute())
	{
		                                                            
		pNode = pNode->GetParent();
		if(pNode == 0)
			return false;
	}

	CROMData* pParent = pNode->GetParent();
	if(pParent == 0)
		return false;

	KString PKeyName = szPrimaryKeyName;
	while(pParent != 0)
	{
		CROMData* pTemp = pParent->FindObjectInChild(PKeyName, CROM_t_Attribute);
		if(pTemp != 0)
		{
			PKeyName = pTemp->GetLinkToPrimaryKey();
			if(PKeyName.IsEmpty())
			{
				Value = pTemp->GetValue();
				return true;
			}
		}

		pParent = pParent->GetParent();
		while(pParent != 0 && !pParent->IsElement())
			pParent = pParent->GetParent();
	}

	return false;
}


CROMData*	CROMData::GetSibling(int iInstNum) const
{
	
	if (0 < iInstNum)
	{
		                                                                              
		int iCurNum = 1;
		CROMData *pTravel = const_cast<CROMData*>(this);
		while (iCurNum != iInstNum)
		{
			pTravel = pTravel->GetNextSibling();
			if (0 == pTravel)
				return 0;
			iCurNum++;
		}
		return pTravel;		
	} else if (0 > iInstNum)
	{
		                                                                            
		CROMData *pTravel = const_cast<CROMData*>(this);
		int iCurNum = -1;

		                                        
		while(0 != pTravel->GetNextSibling())
		{
			pTravel = pTravel->GetNextSibling();
		}

		while (iCurNum != iInstNum)
		{
			pTravel = pTravel->GetPreviousSibling();
			if (0 == pTravel)
				return 0;
			iCurNum--;
		}
		return pTravel;		
	}
	return 0;
}


                                     
                                                                                                
                                  
                                                                                               
                              
                                                             
                                                               
CROMData* CROMData::FindNode(KString NodeName, KString NodeValue)
{
	if((m_DTDNode->GetName() == NodeName) && (m_Value == NodeValue))
		return this;

	CROMData *child = (CROMData*) GetFirstChild(),*obj;

	while(child)
	{
		if((obj=child->FindNode(NodeName, NodeValue))!=0)
			return obj;
		child = (CROMData*) child->GetNextSibling();
	}
	return child;                      
}

bool CROMData::ArePathsEqual(KString sEndPath)
{
	KString sCurPath = GetNodePath();
	sCurPath.Replace('$', '.');
	sEndPath.Replace('$', '.');

	konga::StringTokenizer tokCur(sCurPath,".");
	konga::StringTokenizer tokEnd(sEndPath,".");
	KString EndTok;
	KString CurTok;
	bool bRetVal1, bRetVal2;

	bRetVal1 = tokEnd.NextToken();
	bRetVal2 = tokCur.NextToken();

	while ( bRetVal1 || bRetVal2)
	{
		
		CurTok = tokCur.token();
		EndTok = tokEnd.token();
		
		int iInstChar = EndTok.Find("#");

		if (-1 != iInstChar)
		{
			EndTok = EndTok.Left(iInstChar);
		}

		                                                           
		if (strcmp(EndTok, CurTok) != 0)
		{
			return false;
		}

		bRetVal1 = tokEnd.NextToken();
		bRetVal2 = tokCur.NextToken();
	}
	
	return true;
}

                                  
                                                                                                 
                
                                                            
                                                                                 
CROMData* CROMData::Clone(CROMData* pParentNode)
{
	CROMData *pNewChildNode = 0, *pLastChild = 0;

	                                   
	bool bFirstChild = true;

	                                                                                        
	CROMData*child = GetFirstSibling();
	while (child)
	{
		                                                            
		pNewChildNode = new CROMData();

		*pNewChildNode = *((CROMData*)child);
		pNewChildNode->SetParent(pParentNode);

		                                                                               
		if (bFirstChild)
		{
			pParentNode->SetFirstChild(pNewChildNode);
			bFirstChild = false;
		}

		pNewChildNode->SetFirstChild(0);

		if (pLastChild != 0)
		{
			pNewChildNode->SetPreviousSibling(pLastChild);
			pLastChild->SetNextSibling(pNewChildNode);
		}
		
		pLastChild = pNewChildNode;

		                                           
		CROMData* pFirstChild = ((CROMData*)child->GetFirstChild());
		if (pFirstChild != 0) {
			pFirstChild->Clone(pNewChildNode);
		}

		                         
		child=child->GetNextSibling();
	}
	
	return pNewChildNode;
}


CROMData* CROMData::FindValue(const char *TableName, const char *FieldName, KString& Result, KString& DataTypeStr, CMFParser* pMFParser, bool bFlatFile)
{
	CROMData* pTravPtr = this;

	while (0 != pTravPtr)
	{
		                                                     
		if (true == pTravPtr->IsCollectionOfElements())
		{

			                                          
			if (pTravPtr->GetName() == TableName)
			{
 				                                                                                                             
				CROMData *pReqField = 0;
				pReqField = pTravPtr->FindObjectInChild(FieldName, CROM_t_Attribute);

				                                                                                                
				if (0 != pReqField)
				{
					DataTypeStr = pReqField->GetValueTypeStr();
					Result = pReqField->GetValue();

					if (pReqField->IsNull())
					{
						getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_TRANSFORM_VALUE_NULL);
						getTreeMapper()->m_lastErr = getTreeMapper()->getErrorLog()->GetLastErr();
						return 0;
					}
					return pReqField;
				}
				else
				{
					                                                                                   
					                              
				}
			}			
		}

		                                                                     
		pTravPtr = pTravPtr->GetParent();
	}

	                                                                        
	getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_UNABLE_TO_FIND_FIELD);
	getTreeMapper()->m_lastErr = getTreeMapper()->getErrorLog()->GetLastErr();
	return 0;
}

bool CROMData::IsCollectionOfElements()
{
	bool Result = (GetType() & CROM_t_Collection) || (GetType() & CROM_t_Element);
	return Result;
}

KString CROMData::GetValueTypeStr()
{
	KString DataTypeStr;
	DataTypeStr = (const char*)(KString(GetValueType()).SpanExcluding("[]"));
	return DataTypeStr;
}

CROMData* CROMData::FindValue(KString sSearchPath, KString sReqField)
{
	CROMData *pReqField = 0;

	                                                                                       
	                                                               
	if (sSearchPath.IsEmpty())
	{
		pReqField = FindObjectInChild(sReqField, CROM_t_Attribute);
		return pReqField;
	}

	                                                                  
	KString sLevelName;
	int	iInstanceNumber;

	GetLevelInfo(sSearchPath, sLevelName, iInstanceNumber);

	CROMData	*pChild = GetFirstChild();
	while (0 != pChild)
	{
		if (pChild->IsElement() && pChild->GetDisplayName() == sLevelName)
		{
			                                                                                                         
			if (0 == iInstanceNumber)
			{
				pReqField = FindValue(sSearchPath, sReqField);
				pChild = pChild->GetNextSibling();
			}
			else 
			{
				                                            
				pChild = pChild->GetSibling(iInstanceNumber);
				pReqField = FindValue(sSearchPath, sReqField);
				pChild = 0;
			}
		}
		else
		{
			pChild = pChild->GetNextSibling();
		}
	}

	return pReqField;
}

namespace {
	void TruncateNotIn(const vector<int>& updateKeyOrdinals, vector<CColValType>& values) {
		for( vector<CColValType>::iterator it = values.begin(); it != values.end(); ++it ) {
			CColValType& value = *it;
			if ( value.m_iCharSize > 0 && std::find(updateKeyOrdinals.begin(), updateKeyOrdinals.end(), value.m_iOrdinal) == updateKeyOrdinals.end() ) {
				if ( value.m_sColVal.GetLength() > (unsigned)value.m_iCharSize ) {
					value.m_sColVal = value.m_sColVal.Left(value.m_iCharSize);
				}
			}
		}
	}

	void TruncateCharacterColValues(vector<CColValType>& ParamValues,
		const vector<CColValType>& WhereValues,
		vector<CColValType>& UpdateValues)
	{
		                                                              
		vector<int> updateKeyOrdinals;
		for( vector<CColValType>::const_iterator it = WhereValues.begin(); it != WhereValues.end(); ++it ) {
			updateKeyOrdinals.push_back(it->m_iOrdinal);
		}
		TruncateNotIn(updateKeyOrdinals, ParamValues);
		TruncateNotIn(updateKeyOrdinals, UpdateValues);
	}
}

namespace {
	void AddCommaIfNeeded(KString& ColNames, KString& ColValues) {
		if (!ColValues.IsEmpty()) {
			ColNames += ",";
			ColValues += ",";
		}
	}

	void AddCommaIfNeededSetClause(KString& SetClause, bool bPrimaryKey, bool bOraSeqNo) {
		if (!SetClause.IsEmpty()) {
			if (!bPrimaryKey && !bOraSeqNo)
				SetClause += ",";
		}
	}

	KString AddBeginEndQuote(const string& sBeginName, const KString& ColName, const string& sEndName) {
        return string(sBeginName + (const char*)ColName + sEndName);
	}
}

CROMData* CROMData::FindDataNode(const char* sName)
{
	if(!IsLeaf() && strcmp(sName, (const char*)GetDisplayName()) == 0)
	{
		return this;
	}

	CROMData* pNode = GetFirstChild();
	while(pNode)
	{
		CROMData* pWanted = pNode->FindDataNode(sName);
		if(pWanted != 0)
			return pWanted;
		pNode = pNode->GetNextSibling();
	}

	return 0;
}

bool CROMData::IsOneToOne()
{
	if(m_DTDNode)
		return m_DTDNode->IsOneToOne();

	return false;
}

                         
#ifdef debug_data_tree
	#define prtopen(i,dtd) if(i==1)fprintf(out,"[%s]:{{\n",dtd->get_Path()); else fprintf(out,",/%s/{\n",dtd->m_Name);
	#define prtclose(i,dtd) fprintf(out,"/%s/%d}\n",(const char*)dtd->m_Name,i)
#else
	#define prtopen(i,dtd) if(i==1)fprintf(out,"[%s]:{{\n",(const char*)dtd->get_Path()); else fprintf(out,",{\n");
	#define prtclose(i,dtd) fprintf(out,"}")
#endif

int CROMData::GetInstanceNumber()
{
	CROM* dtd=GetDTDNode();
	if(m_instanceNumber<0){
		if(m_Parent==0) {
			m_instanceNumber = -1;
			return m_instanceNumber;
		}

		int count=0;
		for(CROMData*pChild=m_Parent->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
			if(pChild->GetDTDNode()==dtd){
				pChild->m_instanceNumber=count++;
			}
		}
	}
	return m_instanceNumber;
}


KString CROMData::GetDefaultValue() const
{
	return m_DTDNode? m_DTDNode->GetDefaultValue(): "";
}

KString CROMData::GetFormat() const
{
	return m_DTDNode? m_DTDNode->GetFormat(): "";
}

bool CROMData::HasSubElement()
{
	if(m_DTDNode)
		return m_DTDNode->HasSubElement();
	return false;
}

bool DataTreeIsTarget(DataTreeType treeType)
{
	return treeType != t_Source;
}

KString CROMData::GetPrintValue()
{
	return GetValue();
}

CROMData* CROMData::AddAttribute(const vector<string>&values, const vector<bool>&nulls,bool bIsDatabase)
{
	CROM*pCROM=GetDTDNode();
	if(!pCROM->IsElement()){
		throw konga::CrException(string("AddAttribute() failed. Cannot add attribute to a non element node. node_name=\"")+(const char*)GetDisplayName()+"\"");
	}
	int i=0, n=values.size();
	if(bIsDatabase)i++;
	CROMData*pAttribute=0;
	for(CROM*pChild=pCROM->GetFirstChild(); pChild && i<n; pChild=pChild->GetNextSibling()){
		if(pChild->IsAttribute()){
			pAttribute=new CROMData(this, pAttribute, pChild, values[i].c_str());
			if(nulls[i++])pAttribute->SetNullFlag();
		}
	}
	return pAttribute;
}

bool CROMData::IsNull()
{
	return m_null;
}

CROM* CROMData::GetDTDNode() const {
	return m_DTDNode;
}

string CROMData::getValue(){
	if(m_null)return string();
	else return (const char*)m_Value;
}

konga::xml::DOMNodePtr CROMData::getXml(){
	return 0;
}

konga::JbNode* CROMData::getJbNode(){
	return 0;
}


void CROMData::getInstances(vector<CRData*>&list, const char*nodeName){
	for(CROMData*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsNotALeaf()&&child->GetName()==nodeName){
			list.push_back(child);
		}	
	}
}

void CROMData::collectInstances(vector<CRData*>& vec, const KString& sEndPath, CROM*targetLoopNode0, int&err){
	TreeMapper* pTreeMapper=getTreeMapper();
	HfLoop		loop(pTreeMapper), loop0(pTreeMapper);
	vec.clear();

	CROMData* pStartNode=this;
	CROM* dtd=GetDTDNode();

	if(dtd->IsFlat() && dtd->GetParent()==0 && sEndPath==""){
		for(CROMData*child=GetFirstChild(); child; child=child->GetNextSibling()){
			vec.push_back(child);
		}
		return;
	}

	const char*sourcePath=(const char*)dtd->get_Path();
	int pos=sEndPath.Find(sourcePath);
	if(pos!=0){
		throw konga::CrException(string("collectInstances called with illegal path:\"")+
			(const char*)sEndPath+"\", source path:\""+
			(const char*)GetDTDNode()->get_Path());
	}
	loop0.Add(pStartNode);
	if(dtd->IsFlat()){
		CROM* firstChild=dtd->GetFirstChild();
		if(firstChild && !firstChild->IsLeaf())dtd=firstChild;
		KString token(dtd->GetName());
		loop.gatherLoopNode(loop0.GetAt(0),token);
		loop0.copy(loop);
	}
	else {
		konga::StringTokenizer tk2(&((const char*)sEndPath)[strlen(sourcePath)],"$.");
		while(tk2.NextToken()){
			KString token=tk2.token();
			char c=token.GetAt(0);
			if(c=='.'||c=='$')continue;
			loop.clear();
			for(int i=0;i<(int)loop0.m_n;i++){
				loop.gatherLoopNode(loop0.GetAt(i),token);
			}
			loop0.copy(loop);
		}
	}
	loop0.m_EndPath=sEndPath;
	int n=(int)loop0.m_n;
	if(targetLoopNode0==0){
		for(int i=0;i<n;i++){
			vec.push_back(loop0.at(i));
		}
	}
	else {
		                                                                                                       
		                                                                                                               
		                                                                                              
		                                                                 
		for(int i=0;i<n;i++){
			CROMData*source=loop0.at(i);
			if(source==0)continue;
			CROM*targetLoopNode=source->GetDTDNode()->m_Generator;
			if(targetLoopNode!=0&&targetLoopNode!=targetLoopNode0)
			{
				             
			}
			else {
				vec.push_back(source);
			}
		}
	}
	return;
}

CRData* CROMData::find(const KString& ReqValue, int& err){
	CROMData*pStartNode=this;
	konga::StringTokenizer tk1((const char*)getPath(),"$."),tk2(ReqValue,"$.");
	char c;
	while(tk1.NextToken()&&tk2.NextToken()){
		if(strcmp(tk1.token(),tk2.token())){
			tk1.StepBack();
			tk2.StepBack();
			break;
		}
	}
	while(tk1.NextToken()){
		if((c=tk1.token()[0])!='.'&&c!='$'){
			if(pStartNode==0){
				KString sErr;
				sErr.Format("CROMData::find(startPath=%s, endPath=%s) failed 1", (const char*)getPath(), (const char*)ReqValue);
				getTreeMapper()->AppendErrorMessage(sErr);
				return 0;
			}
			pStartNode=pStartNode->GetParent();
		}
	}
	if(pStartNode==0){
		KString sErr;
		sErr.Format("CROMData::find(startPath=%s, endPath=%s) failed 2", (const char*)getPath(), (const char*)ReqValue);
		getTreeMapper()->AppendErrorMessage(sErr);
		return 0;
	}
	while(tk2.NextToken()){
		KString token=tk2.token();
		if((c=token.GetAt(0))=='.'||c=='$')continue;
		int pos=token.Find('#');
		size_t len=token.GetLength();
		if(pos<0||(pos==(len-1))){                                   
			if(pos>0)token=token.Left(pos);
			if(pStartNode->getPath().IsEmpty()){
				CROMData* child=pStartNode->GetFirstChild();
				if(child!=0)pStartNode=child;
				if(pStartNode->GetDTDNode()->m_Name!=token)
					pStartNode=pStartNode->FindObjectInChild(token,CROM_t_AnyThing);
			} else {
				pStartNode=pStartNode->FindObjectInChild(token,CROM_t_AnyThing);
			}
		} else {                                   
			int n_instance=1,pos1=pos+1;
			if(token.GetAt(pos1)=='_'){
				n_instance=-1;
				pos1++;
			}
			KString num=token.Mid(pos1,len-pos1);
			n_instance=n_instance*atoi(num);
			token=token.Left(pos);
			HfLoop tmp(getTreeMapper());
			tmp.gatherLoopNode(pStartNode,token);
			if(n_instance<0){
				n_instance=n_instance+1+tmp.m_n;
			}
			if(n_instance>0&&n_instance <= (int)tmp.GetSize()){
				pStartNode=tmp.GetAt(n_instance-1);
			} else pStartNode=0;
		}
		if(pStartNode==0)break;
	}
	if(pStartNode&&pStartNode->IsNotALeaf()){
		pStartNode=pStartNode->FindObjectInChild("",CROM_t_Value);
	}
	if(pStartNode==0){
		KString sErr;
		sErr.Format("CROMData::find(startPath=%s, endPath=%s) failed 3", (const char*)getPath(), (const char*)ReqValue);
		getTreeMapper()->AppendErrorMessage(sErr);
		return 0;
	}
	return pStartNode;
}

CRData* CROMData::getChildNode(const char*name, bool isLoop)
{
	for(CROMData*child=m_FirstChild; child; child=child->m_NextSibling)
	{
		if(child->IsLeaf())continue;
		if(strcmp(child->GetDTDNode()->get_Name(),name)==0 && child->IsLoopNode()==isLoop)
			return child;
	}
	return 0;
}

CRData* CROMData::getAttributeNode(const char*name)
{
	for(CROMData*child=m_FirstChild; child; child=child->m_NextSibling)
	{
		if(!child->IsLeaf())continue;
		if(strcmp(child->GetDTDNode()->get_Name(),name)==0)
			return child;
	}
	return 0;
}

CRData* CROMData::getValueNode()
{
	for(CROMData*child=m_FirstChild; child; child=child->m_NextSibling)
	{
		if(!child->IsLeaf())continue;
		if(child->GetDTDNode()->get_Name()[0]==0)return child;
	}
	return 0;
}




int CROMData::removeChildOfCrom(CROM*childCrom){
	int i=0;
	for(CROMData*child=m_FirstChild, *next; child; child=next){
		next=child->m_NextSibling;
		if(child->m_DTDNode==childCrom){
			child->DeleteNode();
			i++;
		}
	}
	return i;
}
