/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SOURCE_TARGET_XML_HANDLER_H
#define KONGA_WS_DEPLOY_SOURCE_TARGET_XML_HANDLER_H

#include "InterchangeDataStructs.h"

namespace konga {

class DbConnection;
class FunctionIdMapper;
class UserInfo;
class UserAccess;
class GuidToIdMapper;

namespace interchange_deploy {

	class SourceTargetXmlHandler {
	public:
		SourceTargetXmlHandler(DbConnection& dbConnection,
			FunctionIdMapper& functionIdMapper,
			UserInfo& userInfo,
			UserAccess& userAccess,
			GuidToIdMapper& guidToIdMapper) :
		m_dbConnection(dbConnection),
		m_functionIdMapper(functionIdMapper),
		m_userInfo(userInfo),
		m_userAccess(userAccess),
		m_guidToIdMapper(guidToIdMapper)
		{}

		~SourceTargetXmlHandler(void) {}

		void HandleTransformations(InterchangeDataTableStruct& tranTabStruct);

	protected:
		std::string GetXmlSchemaPath(const std::string& stGuid);

	private:
		DbConnection& m_dbConnection;
		FunctionIdMapper& m_functionIdMapper;
		UserInfo m_userInfo;
		UserAccess& m_userAccess;
		GuidToIdMapper& m_guidToIdMapper;
	};

}                   
}                                


#endif                                               

