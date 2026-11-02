/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                       
  
                                                                      

#ifdef DS_USE_SP

#include "StdAfx.h"
#include "BindParam.h"

using namespace std;

                                                                      
                           
                                                                      

CBindParam::CBindParam(int numFields, int numParams, spArgs* argList, DbConnection *pDatabase):
CdtorRecordset(pDatabase), m_numFields(numFields), m_numParams(numParams), m_argList(argList)
{
	m_nFields = numFields;
	m_nParams = numParams;
}

CBindParam::~CBindParam()
{

}


void CBindParam::DoFieldExchange(CFieldExchange* pFX)
{
	string fieldType;

	for (int i = 0; i < (m_numParams + m_numFields); i++)
	{
		switch(m_argList[i].argParamType[0])
		{
		case 'I':
			pFX->SetFieldType(CFieldExchange::inputParam);
			fieldType = "input";
			break;
		case 'O':
		case 'R':
			pFX->SetFieldType(CFieldExchange::outputParam);
			fieldType = "output";
			break;
		case 'F':
			pFX->SetFieldType(CFieldExchange::outputColumn);
			fieldType = "Column";
			break;
		default:
			pFX->SetFieldType(CFieldExchange::inoutParam);
			fieldType = "inout";
			break;
		}

		if (m_argList[i].bIsNull && m_argList[i].argParamType != "F")
		{
			SetParamNull(i);
		}


		if (!stricmp(m_argList[i].argDataType.c_str(), "INT"))
		{
			                                           
			RFX_Int(pFX, fieldType.c_str(), *((int*)(m_argList[i].data)));
		}
		else if (!stricmp(m_argList[i].argDataType.c_str(), "STR"))
			RFX_Text(pFX, fieldType.c_str(), (char*)(m_argList[i].data), m_argList[i].dataSize, SQL_VARCHAR, 0);
		else if (!stricmp(m_argList[i].argDataType.c_str(), "LNG"))
			RFX_Long(pFX, fieldType.c_str(), *((long*)(m_argList[i].data)));
		else if(!stricmp(m_argList[i].argDataType.c_str(), "FLT"))
			RFX_Single(pFX, fieldType.c_str(), *((float*)(m_argList[i].data)));
		else if(!stricmp(m_argList[i].argDataType.c_str(), "SRT"))
			RFX_Byte(pFX, fieldType.c_str(), *((BYTE*)(m_argList[i].data)));
		else if (!stricmp(m_argList[i].argDataType.c_str(), "DBL"))
			RFX_Double(pFX, fieldType.c_str(), *((double*)(m_argList[i].data)));
		else if (!stricmp(m_argList[i].argDataType.c_str(), "BOL"))
			RFX_Bool(pFX, fieldType.c_str(), *((BOOL*)(m_argList[i].data)));
		else if (!stricmp(m_argList[i].argDataType.c_str(), "DTE"))
			RFX_Date(pFX, fieldType.c_str(), *((TIMESTAMP_STRUCT*)(m_argList[i].data)));
		else
			RFX_Text(pFX, fieldType.c_str(), (char*)(m_argList[i].data), m_argList[i].dataSize, SQL_VARCHAR, 0);
	}
}

#endif             

