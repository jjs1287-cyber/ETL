/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlTRANSFORMATIONSTAB_V.h>

#include "TRANSFORMATIONSTAB_V.h"

using namespace konga;
using namespace TranDb;

TRANSFORMATIONSTAB_V* TRANSFORMATIONSTAB_V::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlTRANSFORMATIONSTAB_V(pDtlConn->GetDtlConnection());
}

void TRANSFORMATIONSTAB_V::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Transformation_ID=";
	m_stUpdateFilter += toString(GetTransformation_ID());
	m_stUpdateFilter += " AND Transformation_Name=";
	m_stUpdateFilter += "'" + SqlEscape(GetTransformation_Name()) + "'";
	m_stUpdateFilter += " AND Source_Type_ID=";
	m_stUpdateFilter += toString(GetSource_Type_ID());
	m_stUpdateFilter += " AND Target_Type_ID=";
	m_stUpdateFilter += toString(GetTarget_Type_ID());
	m_stUpdateFilter += " AND Source_Text_Doc=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_Text_Doc()) + "'";
	m_stUpdateFilter += " AND Source_EDI=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_EDI()) + "'";
	m_stUpdateFilter += " AND Source_EDI_Ver=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_EDI_Ver()) + "'";
	m_stUpdateFilter += " AND Source_SourceID=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_SourceID()) + "'";
	m_stUpdateFilter += " AND Target_Text_Doc=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_Text_Doc()) + "'";
	m_stUpdateFilter += " AND Target_EDI=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_EDI()) + "'";
	m_stUpdateFilter += " AND Target_EDI_Ver=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_EDI_Ver()) + "'";
	m_stUpdateFilter += " AND Target_TargetID=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_TargetID()) + "'";
	m_stUpdateFilter += " AND Source_XML=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_XML()) + "'";
	m_stUpdateFilter += " AND Target_XML=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_XML()) + "'";
	m_stUpdateFilter += " AND Source_DSN=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_DSN()) + "'";
	m_stUpdateFilter += " AND Target_DSN=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_DSN()) + "'";
	m_stUpdateFilter += " AND Source_Tables=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_Tables()) + "'";
	m_stUpdateFilter += " AND Target_Tables=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_Tables()) + "'";
	m_stUpdateFilter += " AND UpdateFields=";
	m_stUpdateFilter += "'" + SqlEscape(GetUpdateFields()) + "'";
	m_stUpdateFilter += " AND Source_SQL=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_SQL()) + "'";
	m_stUpdateFilter += " AND Target_SQL=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_SQL()) + "'";
	m_stUpdateFilter += " AND NatureOfSource=";
	m_stUpdateFilter += "'" + SqlEscape(GetNatureOfSource()) + "'";
	m_stUpdateFilter += " AND NatureOfTarget=";
	m_stUpdateFilter += "'" + SqlEscape(GetNatureOfTarget()) + "'";
	m_stUpdateFilter += " AND Source_Where=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_Where()) + "'";
	m_stUpdateFilter += " AND Insert_Mode=";
	m_stUpdateFilter += toString(GetInsert_Mode());
	m_stUpdateFilter += " AND Target_DTD_URL=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_DTD_URL()) + "'";
	m_stUpdateFilter += " AND Source_EDI_Default=";
	m_stUpdateFilter += toString(GetSource_EDI_Default());
	m_stUpdateFilter += " AND Target_EDI_Default=";
	m_stUpdateFilter += toString(GetTarget_EDI_Default());
	m_stUpdateFilter += " AND Target_UpdateKeys=";
	m_stUpdateFilter += "'" + SqlEscape(GetTarget_UpdateKeys()) + "'";
	m_stUpdateFilter += " AND Source_DiffKeys=";
	m_stUpdateFilter += "'" + SqlEscape(GetSource_DiffKeys()) + "'";
	m_stUpdateFilter += " AND IsValid=";
	m_stUpdateFilter += toString(GetIsValid());
	m_stUpdateFilter += " AND SourceDTD_Root=";
	m_stUpdateFilter += "'" + SqlEscape(GetSourceDTD_Root()) + "'";
	m_stUpdateFilter += " AND TargetDTD_Root=";
	m_stUpdateFilter += "'" + SqlEscape(GetTargetDTD_Root()) + "'";
	m_stUpdateFilter += " AND Function_ID=";
	m_stUpdateFilter += toString(GetFunction_ID());
	m_stUpdateFilter += " AND InputXSDFileName=";
	m_stUpdateFilter += "'" + SqlEscape(GetInputXSDFileName()) + "'";
	m_stUpdateFilter += " AND OutputXSDFileName=";
	m_stUpdateFilter += "'" + SqlEscape(GetOutputXSDFileName()) + "'";
	m_stUpdateFilter += " AND FunctionType_Name=";
	m_stUpdateFilter += "'" + SqlEscape(GetFunctionType_Name()) + "'";
	m_stUpdateFilter += " AND TargetJmsMessageId=";
	m_stUpdateFilter += toString(GetTargetJmsMessageId());
	m_stUpdateFilter += " AND SourceJmsMessageId=";
	m_stUpdateFilter += toString(GetSourceJmsMessageId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

