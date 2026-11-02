/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/Dtlinterchangeprojectoperationstab.h>

#include "interchangeprojectoperationstab.h"

using namespace konga;
using namespace TranDb;

interchangeprojectoperationstab* interchangeprojectoperationstab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::Dtlinterchangeprojectoperationstab(pDtlConn->GetDtlConnection());
}

void interchangeprojectoperationstab::Edit()
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
	if( Isitem_idNull() )
	{
		m_stUpdateFilter += " AND item_id IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND item_id=";
		m_stUpdateFilter += toString(Getitem_id());
	}
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

