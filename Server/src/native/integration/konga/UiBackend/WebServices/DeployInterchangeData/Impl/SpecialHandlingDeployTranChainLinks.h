/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SPECIAL_HANDLING_TRAN_CHAIN_LINKS_H
#define KONGA_WS_DEPLOY_SPECIAL_HANDLING_TRAN_CHAIN_LINKS_H

#include "DeployUtils.h"

#include "SpecialHandlingDeploy.h"

namespace konga {

	class GuidToIdMapper;
	class DbConnection;
	class FunctionIdMapper;

	namespace interchange_deploy {

		   
                                                                                  
                                                                               
                                                  
     
		class SpecialHandlingDeployTranChainLinks : public SpecialHandlingDeploy
		{
		public:
			SpecialHandlingDeployTranChainLinks(DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper,
												FunctionIdMapper& functionIdMapper) :
				  m_dbConnection(dbConnection), m_guidToIdMapper(guidToIdMapper), m_functionIdMapper(functionIdMapper)
			{}
			~SpecialHandlingDeployTranChainLinks(void) {}

			bool HandleRow(InterchangeDataRowStruct& row);

		private:
			long getDbTranId(const std::string& stTranId);
			long getTranIdFromWsId(long lWsId);
			long getDbOperationId(const std::string& stOperationId);

		private:
			DbConnection& m_dbConnection;
			GuidToIdMapper& m_guidToIdMapper;
			FunctionIdMapper& m_functionIdMapper;
		};
	}
}

#endif                                                       

