/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_SALESFORCE_CREATE_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_SALESFORCE_CREATE_H

#include "SpecialHandlingDeploy.h"
#include "ItemWarningHolder.h"

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

namespace interchange_deploy {

	                                              
	class SpecialHandlingDeploySalesforceCreate : public SpecialHandlingDeploy, public ItemWarningHolder {
	public:
		SpecialHandlingDeploySalesforceCreate(const xsd__string_Array& columnNames, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);
		~SpecialHandlingDeploySalesforceCreate(void) {}

		virtual bool HandleRow(InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterUpdate(long id, InterchangeDataRowStruct& row);

	private:
		unsigned int m_iSalesforceCreateIdColPos, m_iOperationIdPos;
		DbConnection& m_dbConnection;
		GuidToIdMapper& m_guidToIdMapper;
	};

}
}

#endif                                                        
