#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlJmsProvidersTab.h>

#include "JmsProvidersTab.h"

using namespace konga;
using namespace TranDb;

JmsProvidersTab* JmsProvidersTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlJmsProvidersTab(pDtlConn->GetDtlConnection());
}

void JmsProvidersTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProviderId=";
	m_stUpdateFilter += toString(GetProviderId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

