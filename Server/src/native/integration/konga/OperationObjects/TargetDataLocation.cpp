/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/ItemAccess/UserAccess.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include "SourceTargetPipelinePluginExecution.h"
#include "TargetDataLocation.h"
#include "EndpointCompression.h"

using namespace konga;

TargetDataLocation::TargetDataLocation(void) : m_pPluginSelector(0) { }
TargetDataLocation::~TargetDataLocation(void) {}

void TargetDataLocation::doPreTargetTransfer(const OperationContext& context,
											 OperationDataElementHolder& deHolder,
											 ProcessedFiles& files) const
{
	if ( m_pPluginSelector != 0 )
	konga::ExecutePipelinePluginAtSourceOrTarget(false,
		this->m_TargetData.m_lTargetId,
		this->m_TargetData.m_sTargetName,
		context, deHolder, files,
		*(this->m_pPluginSelector), 0);
 compressEndpointFiles(*this,files);
 encryptEndpointFiles(*this,files);
}

void TargetDataLocation::doPostTargetTransfer(const OperationContext& context,
											  OperationDataElementHolder& deHolder,
											  ProcessedFiles& files) const
{
	if ( m_pPluginSelector != 0 )
	konga::ExecutePipelinePluginAtSourceOrTarget(false,
		this->m_TargetData.m_lTargetId,
		this->m_TargetData.m_sTargetName,
		context, deHolder, files,
		*(this->m_pPluginSelector), 1);
}

bool TargetDataLocation::userHasReadPermission(const UserInfo& user, DbConnection* pTranDbConn) const {
	const long id = getTargetId();
	if ( id < 1 )
		return true;

	if ( pTranDbConn ) {
		UserAccess userAccess(user, *pTranDbConn);
		return userAccess.HasWritePermission(id, TargetEntityId);
	}
	else {
		return konga::UserHasWritePermission(user.GetId(), id, TargetEntityId);
	}
}

bool TargetDataLocation::userHasExecutePermission(const UserInfo& user, DbConnection* pTranDbConn) const {
	const long id = getTargetId();
	if ( id < 1 )
		return true;

	if ( pTranDbConn ) {
		UserAccess userAccess(user, *pTranDbConn);
		return userAccess.HasExecutePermission(id, TargetEntityId);
	}
	else {
		return konga::UserHasExecutePermission(user.GetId(), id, TargetEntityId);
	}
}
