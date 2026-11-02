/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlDocument_DefinitionsTab.h>

#include "Document_DefinitionsTab.h"

using namespace konga;
using namespace TranDb;

Document_DefinitionsTab* Document_DefinitionsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlDocument_DefinitionsTab(pDtlConn->GetDtlConnection());
}

void Document_DefinitionsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Document_ID=";
	m_stUpdateFilter += toString(GetDocument_ID());
	m_stUpdateFilter += " AND Field_ID=";
	m_stUpdateFilter += toString(GetField_ID());
	m_stUpdateFilter += " AND Parent_ID=";
	m_stUpdateFilter += toString(GetParent_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

