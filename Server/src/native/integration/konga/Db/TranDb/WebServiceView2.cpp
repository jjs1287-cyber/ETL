/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlWebServiceView2.h>

#include "WebServiceView2.h"

using namespace konga;
using namespace TranDb;

WebServiceView2* WebServiceView2::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlWebServiceView2(pDtlConn->GetDtlConnection());
}

void WebServiceView2::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Function_ID=";
	m_stUpdateFilter += toString(GetFunction_ID());
	m_stUpdateFilter += " AND WebService_ID=";
	m_stUpdateFilter += toString(GetWebService_ID());
	m_stUpdateFilter += " AND WebService_Url=";
	m_stUpdateFilter += "'" + SqlEscape(GetWebService_Url()) + "'";
	m_stUpdateFilter += " AND SoapAction=";
	m_stUpdateFilter += "'" + SqlEscape(GetSoapAction()) + "'";
	m_stUpdateFilter += " AND SoapStyle=";
	m_stUpdateFilter += "'" + SqlEscape(GetSoapStyle()) + "'";
	m_stUpdateFilter += " AND SoapBodyUse=";
	m_stUpdateFilter += "'" + SqlEscape(GetSoapBodyUse()) + "'";
	m_stUpdateFilter += " AND TargetNamespace=";
	m_stUpdateFilter += "'" + SqlEscape(GetTargetNamespace()) + "'";
	m_stUpdateFilter += " AND OtherNamespaces=";
	m_stUpdateFilter += "'" + SqlEscape(GetOtherNamespaces()) + "'";
	m_stUpdateFilter += " AND InputXSDFileName=";
	m_stUpdateFilter += "'" + SqlEscape(GetInputXSDFileName()) + "'";
	m_stUpdateFilter += " AND OutputXSDFileName=";
	m_stUpdateFilter += "'" + SqlEscape(GetOutputXSDFileName()) + "'";
	m_stUpdateFilter += " AND FunctionType_ID=";
	m_stUpdateFilter += toString(GetFunctionType_ID());
	m_stUpdateFilter += " AND HttpUserName=";
	m_stUpdateFilter += "'" + SqlEscape(GetHttpUserName()) + "'";
	m_stUpdateFilter += " AND HttpPassword=";
	m_stUpdateFilter += "'" + SqlEscape(GetHttpPassword()) + "'";
	m_stUpdateFilter += " AND MinCallDelay_ms=";
	m_stUpdateFilter += toString(GetMinCallDelay_ms());
	m_stUpdateFilter += " AND MaxSize_bytes=";
	m_stUpdateFilter += toString(GetMaxSize_bytes());
	m_stUpdateFilter += " AND Wsdl_ID=";
	m_stUpdateFilter += toString(GetWsdl_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

