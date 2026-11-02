/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_HTTP_ENDPOINT_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_HTTP_ENDPOINT_H

#include "SpecialHandlingDeploy.h"
#include "ItemWarningHolder.h"

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

namespace interchange_deploy {

	                                                        
	class SpecialHandlingDeployHttpEndpoint : public SpecialHandlingDeploy, public ItemWarningHolder {
	public:
		SpecialHandlingDeployHttpEndpoint(const xsd__string_Array& columnNames, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);
		~SpecialHandlingDeployHttpEndpoint(void) {}

		virtual bool HandleRow(InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterUpdate(long id, InterchangeDataRowStruct& row);

	private:
		void HandleUrl(long newId, InterchangeDataRowStruct& row);

	private:
		unsigned int m_iHttpEndpointIdColPos, m_iUrlColPos;
		DbConnection& m_dbConnection;
		GuidToIdMapper& m_guidToIdMapper;
	};

}
}

#endif                                                    

