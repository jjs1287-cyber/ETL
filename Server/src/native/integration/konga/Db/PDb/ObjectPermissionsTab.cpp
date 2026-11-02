/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlObjectPermissionsTab.h>

#include "ObjectPermissionsTab.h"

using namespace konga;
using namespace PDb;

ObjectPermissionsTab* ObjectPermissionsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlObjectPermissionsTab(pDtlConn->GetDtlConnection());
}

void ObjectPermissionsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ObjectId=";
	m_stUpdateFilter += toString(GetObjectId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

