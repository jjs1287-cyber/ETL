/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlProjectOperationsView.h>

#include "ProjectOperationsView.h"

using namespace konga;
using namespace TranDb;

ProjectOperationsView* ProjectOperationsView::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlProjectOperationsView(pDtlConn->GetDtlConnection());
}

void ProjectOperationsView::Edit()
{
	m_stUpdateFilter = "WHERE (";
	if( Isinterchangeproject_idNull() )
	{
		m_stUpdateFilter += "interchangeproject_id IS NULL";
	}
	else
	{
		m_stUpdateFilter += "interchangeproject_id=";
		m_stUpdateFilter += toString(Getinterchangeproject_id());
	}
	if( IsOperation_IDNull() )
	{
		m_stUpdateFilter += " AND Operation_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Operation_ID=";
		m_stUpdateFilter += toString(GetOperation_ID());
	}
	if( IsOperation_NameNull() )
	{
		m_stUpdateFilter += " AND Operation_Name IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Operation_Name=";
		m_stUpdateFilter += "'" + SqlEscape(GetOperation_Name()) + "'";
	}
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

