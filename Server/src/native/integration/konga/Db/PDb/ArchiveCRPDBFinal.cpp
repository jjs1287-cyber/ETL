/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/DbTypeUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlArchiveCRPDBFinal.h>

#include "ArchiveCRPDBFinal.h"

using namespace konga;
using namespace PDb;

ArchiveCRPDBFinal* ArchiveCRPDBFinal::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new DtlArchiveCRPDBFinal(pDtlConn->GetDtlConnection());
}

void ArchiveCRPDBFinal::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND ProcessVersion=";
	m_stUpdateFilter += toString(GetProcessVersion());
	m_stUpdateFilter += " AND ProcessName=";
	m_stUpdateFilter += "'" + SqlEscape(GetProcessName()) + "'";
	m_stUpdateFilter += " AND Description=";
	m_stUpdateFilter += "'" + SqlEscape(GetDescription()) + "'";
	m_stUpdateFilter += " AND ErrorHandlerId=";
	m_stUpdateFilter += toString(GetErrorHandlerId());
	m_stUpdateFilter += " AND Owner=";
	m_stUpdateFilter += "'" + SqlEscape(GetOwner()) + "'";
	m_stUpdateFilter += " AND ProcessType=";
	m_stUpdateFilter += toString(GetProcessType());
	m_stUpdateFilter += " AND InsertTime=";
	m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetInsertTime()) + "'";
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

