/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef DEPLOY_SPECIAL_HANDLING_DEPLOY_JMS_MESSAGE_H
#define DEPLOY_SPECIAL_HANDLING_DEPLOY_JMS_MESSAGE_H

#include <string>
#include <map>

#include "SpecialHandlingDeploy.h"

namespace konga {

	class GuidToIdMapper;
	class UserInfo;
	class DbConnection;

namespace interchange_deploy {
	                                                                                  
	class SpecialHandlingDeployJmsMessage : public SpecialHandlingDeploy {
	public:
		SpecialHandlingDeployJmsMessage(GuidToIdMapper& guidMapper,
			const UserInfo& userInfo,
			InterchangeDataTableStruct& dataElementsTab,
			DbConnection& dbConnection);
		virtual ~SpecialHandlingDeployJmsMessage() {}

		virtual bool HandleRow(InterchangeDataRowStruct& row);

		virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row);
		virtual bool HandleRowAfterUpdate(long newId, InterchangeDataRowStruct& row);

	private:
		bool HandleRowAfterInsertOrUpdateImpl(long newId, InterchangeDataRowStruct& row);
		void HandleDocumentEntityType(InterchangeDataRowStruct& row);
		void HandleXmlEntityType(InterchangeDataRowStruct& row);
		void HandleNoPayloadType(InterchangeDataRowStruct& row);

	private:
		struct DE {
			std::string name, type, value;
		};

		typedef std::vector<DE> DEVector;

	private:
		int m_iCurrentDataElementId;
		GuidToIdMapper& m_guidMapper;
		const UserInfo& m_userInfo;
		DbConnection& m_dbConnection;
		                                                   
		typedef std::map<std::string, DEVector> DEVectorMapT;
		DEVectorMapT m_dataElements;
	};
}                      
}                   

#endif                                                

