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
#include <konga/Db/DtlIDb/DtlArchiveCRIDBFinal.h>

#include "ArchiveCRIDBFinal.h"

using namespace konga;
using namespace IDb;

ArchiveCRIDBFinal* ArchiveCRIDBFinal::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new DtlArchiveCRIDBFinal(pDtlConn->GetDtlConnection());
}

void ArchiveCRIDBFinal::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "StringValue=";
	m_stUpdateFilter += "'" + SqlEscape(GetStringValue()) + "'";
	m_stUpdateFilter += " AND DoubleValue=";
	m_stUpdateFilter += toString(GetDoubleValue());
	m_stUpdateFilter += " AND IntValue=";
	m_stUpdateFilter += toString(GetIntValue());
	m_stUpdateFilter += " AND DataElementName=";
	m_stUpdateFilter += "'" + SqlEscape(GetDataElementName()) + "'";
	m_stUpdateFilter += " AND DataType=";
	m_stUpdateFilter += toString(GetDataType());
	m_stUpdateFilter += " AND TaskUser=";
	m_stUpdateFilter += "'" + SqlEscape(GetTaskUser()) + "'";
	m_stUpdateFilter += " AND DataElementValueList=";
	m_stUpdateFilter += "'" + SqlEscape(GetDataElementValueList()) + "'";
	m_stUpdateFilter += " AND FromDocDataElementValueList=";
	m_stUpdateFilter += "'" + SqlEscape(GetFromDocDataElementValueList()) + "'";
	m_stUpdateFilter += " AND RouteUser=";
	m_stUpdateFilter += "'" + GetRouteUser() + "'";
	m_stUpdateFilter += " AND MsgPriority=";
	m_stUpdateFilter += toString(GetMsgPriority());
	m_stUpdateFilter += " AND InitialTask=";
	m_stUpdateFilter += toString(GetInitialTask());
	m_stUpdateFilter += " AND UpdateFlag=";
	m_stUpdateFilter += toString(GetUpdateFlag());
	m_stUpdateFilter += " AND ProcessDataElementValueList=";
	m_stUpdateFilter += "'" + SqlEscape(GetProcessDataElementValueList()) + "'";
	m_stUpdateFilter += " AND InsertTime=";
	m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetInsertTime()) + "'";
	m_stUpdateFilter += " AND ModTime=";
	m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetModTime()) + "'";
	m_stUpdateFilter += " AND RouteInstanceId=";
	m_stUpdateFilter += toString(GetRouteInstanceId());
	m_stUpdateFilter += " AND SentTime=";
	m_stUpdateFilter += toString(GetSentTime());
	m_stUpdateFilter += " AND StatusTime=";
	m_stUpdateFilter += toString(GetStatusTime());
	m_stUpdateFilter += " AND Status=";
	m_stUpdateFilter += toString(GetStatus());
	m_stUpdateFilter += " AND EndTime=";
	m_stUpdateFilter += toString(GetEndTime());
	m_stUpdateFilter += " AND StartTime=";
	m_stUpdateFilter += toString(GetStartTime());
	m_stUpdateFilter += " AND ProcessInstanceId=";
	m_stUpdateFilter += toString(GetProcessInstanceId());
	m_stUpdateFilter += " AND MsgFrom=";
	m_stUpdateFilter += "'" + GetMsgFrom() + "'";
	m_stUpdateFilter += " AND MsgTo=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgTo()) + "'";
	m_stUpdateFilter += " AND MsgSubject=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgSubject()) + "'";
	m_stUpdateFilter += " AND MessageFileName=";
	m_stUpdateFilter += "'" + SqlEscape(GetMessageFileName()) + "'";
	m_stUpdateFilter += " AND Format=";
	m_stUpdateFilter += "'" + SqlEscape(GetFormat()) + "'";
	m_stUpdateFilter += " AND SentAt=";
	m_stUpdateFilter += "'" + SqlEscape(GetSentAt()) + "'";
	m_stUpdateFilter += " AND URL=";
	m_stUpdateFilter += "'" + SqlEscape(GetURL()) + "'";
	m_stUpdateFilter += " AND MsgCC=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgCC()) + "'";
	m_stUpdateFilter += " AND MsgBCC=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgBCC()) + "'";
	m_stUpdateFilter += " AND MhtmlUrl=";
	m_stUpdateFilter += "'" + SqlEscape(GetMhtmlUrl()) + "'";
	m_stUpdateFilter += " AND MsgReplyTo=";
	m_stUpdateFilter += "'" + SqlEscape(GetMsgReplyTo()) + "'";
	m_stUpdateFilter += " AND Owner=";
	m_stUpdateFilter += "'" + SqlEscape(GetOwner()) + "'";
	m_stUpdateFilter += " AND MimeContentType=";
	m_stUpdateFilter += "'" + SqlEscape(GetMimeContentType()) + "'";
	m_stUpdateFilter += " AND FileName=";
	m_stUpdateFilter += "'" + SqlEscape(GetFileName()) + "'";
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

