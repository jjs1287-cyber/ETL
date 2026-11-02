/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlXmlDtdChildrenView.h>

#include "XmlDtdChildrenView.h"

using namespace konga;
using namespace TranDb;

XmlDtdChildrenView* XmlDtdChildrenView::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlXmlDtdChildrenView(pDtlConn->GetDtlConnection());
}

void XmlDtdChildrenView::Edit()
{
	m_stUpdateFilter = "WHERE (";
	if( IsXML_IDNull() )
	{
		m_stUpdateFilter += "XML_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += "XML_ID=";
		m_stUpdateFilter += toString(GetXML_ID());
	}
	if( IsChild_IDNull() )
	{
		m_stUpdateFilter += " AND Child_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Child_ID=";
		m_stUpdateFilter += toString(GetChild_ID());
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
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

