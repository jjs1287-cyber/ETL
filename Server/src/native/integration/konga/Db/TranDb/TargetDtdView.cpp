/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlTargetDtdView.h>

#include "TargetDtdView.h"

using namespace konga;
using namespace TranDb;

TargetDtdView* TargetDtdView::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlTargetDtdView(pDtlConn->GetDtlConnection());
}

void TargetDtdView::Edit()
{
	m_stUpdateFilter = "WHERE (";
	if( IsTransformation_IDNull() )
	{
		m_stUpdateFilter += "Transformation_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += "Transformation_ID=";
		m_stUpdateFilter += toString(GetTransformation_ID());
	}
	if( IsXML_PathNull() )
	{
		m_stUpdateFilter += " AND XML_Path IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND XML_Path=";
		m_stUpdateFilter += "'" + SqlEscape(GetXML_Path()) + "'";
	}
	if( IsXML_IDNull() )
	{
		m_stUpdateFilter += " AND XML_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND XML_ID=";
		m_stUpdateFilter += toString(GetXML_ID());
	}
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

