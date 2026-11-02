/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_SALESFORCE_BULKLOAD_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_SALESFORCE_BULKLOAD_H

#include "SpecialHandlingDeploy.h"
#include "ItemWarningHolder.h"

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

namespace interchange_deploy {

	                                              
	class SpecialHandlingDeploySalesforceBulkLoad : public SpecialHandlingDeploy, public ItemWarningHolder {
	public:
		SpecialHandlingDeploySalesforceBulkLoad(const xsd__string_Array& columnNames, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);
		~SpecialHandlingDeploySalesforceBulkLoad(void) {}

		virtual bool HandleRow(InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterUpdate(long id, InterchangeDataRowStruct& row);

	private:
		unsigned int m_iSalesforceBulkLoadIdColPos, m_iOperationIdPos;
		DbConnection& m_dbConnection;
		GuidToIdMapper& m_guidToIdMapper;
	};

}
}

#endif                                                          
