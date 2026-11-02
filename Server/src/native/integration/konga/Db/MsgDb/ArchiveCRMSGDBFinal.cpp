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
#include <konga/Db/DtlMsgDb/DtlArchiveCRMSGDBFinal.h>

#include "ArchiveCRMSGDBFinal.h"

using namespace konga;
using namespace MsgDb;

ArchiveCRMSGDBFinal* ArchiveCRMSGDBFinal::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new DtlArchiveCRMSGDBFinal(pDtlConn->GetDtlConnection());
}

void ArchiveCRMSGDBFinal::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "MsgFrom=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgFrom()) + "'";
	m_stUpdateFilter += " AND MsgTo=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgTo()) + "'";
	m_stUpdateFilter += " AND MsgSubject=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgSubject()) + "'";
	m_stUpdateFilter += " AND ProcessVariables=";
	m_stUpdateFilter += "'" + SqlEscape(GetProcessVariables()) + "'";
	m_stUpdateFilter += " AND ReceivedAt=";
	m_stUpdateFilter += toString(GetReceivedAt());
	m_stUpdateFilter += " AND MsgId=";
	m_stUpdateFilter += toString(GetMsgId());
	m_stUpdateFilter += " AND MsgReplyTo=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgReplyTo()) + "'";
	m_stUpdateFilter += " AND MsgPriority=";
	m_stUpdateFilter += toString(GetMsgPriority());
	m_stUpdateFilter += " AND Owner=";
	m_stUpdateFilter += "'" + GetOwner() + "'";
	m_stUpdateFilter += " AND InsertTime=";
	m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetInsertTime()) + "'";
	m_stUpdateFilter += " AND ModTime=";
	m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetModTime()) + "'";
	m_stUpdateFilter += " AND MachineName=";
	m_stUpdateFilter += "'" + SqlEscape(GetMachineName()) + "'";
	m_stUpdateFilter += " AND CompletionStatus=";
	m_stUpdateFilter += toString(GetCompletionStatus());
	m_stUpdateFilter += " AND ErrorText=";
	m_stUpdateFilter += "'" + SqlEscape(GetErrorText()) + "'";
	m_stUpdateFilter += " AND DataElementName=";
	m_stUpdateFilter += "'" + SqlEscape(GetDataElementName()) + "'";
	m_stUpdateFilter += " AND DataType=";
	m_stUpdateFilter += "'" + SqlEscape(GetDataType()) + "'";
	m_stUpdateFilter += " AND IntValue=";
	m_stUpdateFilter += toString(GetIntValue());
	m_stUpdateFilter += " AND DoubleValue=";
	m_stUpdateFilter += toString(GetDoubleValue());
	m_stUpdateFilter += " AND StringValue=";
	m_stUpdateFilter += "'" + SqlEscape(GetStringValue()) + "'";
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

