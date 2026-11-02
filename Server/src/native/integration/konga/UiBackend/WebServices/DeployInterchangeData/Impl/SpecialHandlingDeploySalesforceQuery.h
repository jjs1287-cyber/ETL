/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_SALESFORCE_QUERY_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_SALESFORCE_QUERY_H

#include "SpecialHandlingDeploy.h"
#include "ItemWarningHolder.h"

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

namespace interchange_deploy {

	                                              
	class SpecialHandlingDeploySalesforceQuery : public SpecialHandlingDeploy, public ItemWarningHolder {
	public:
		SpecialHandlingDeploySalesforceQuery(const xsd__string_Array& columnNames, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);
		~SpecialHandlingDeploySalesforceQuery(void) {}

		virtual bool HandleRow(InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterUpdate(long id, InterchangeDataRowStruct& row);

	private:
		unsigned int m_iSalesforceQueryIdColPos, m_iOperationIdPos;
		DbConnection& m_dbConnection;
		GuidToIdMapper& m_guidToIdMapper;
	};

}
}

#endif                                                       
