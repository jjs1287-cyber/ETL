/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlOrganizationStructureTab.h>

#include "OrganizationStructureTab.h"

using namespace konga;
using namespace PDb;

OrganizationStructureTab* OrganizationStructureTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlOrganizationStructureTab(pDtlConn->GetDtlConnection());
}

void OrganizationStructureTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessName=";
	m_stUpdateFilter += "'" + GetProcessName() + "'";
	m_stUpdateFilter += " AND HierarchyName=";
	m_stUpdateFilter += "'" + GetHierarchyName() + "'";
	m_stUpdateFilter += " AND NodeId=";
	m_stUpdateFilter += toString(GetNodeId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

