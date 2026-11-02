/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_JMS_MESSAGE_H
#define KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_JMS_MESSAGE_H

#include <vector>

#include "SpecialHandlingDeployDelete.h"

namespace konga {
	
class DbConnection;

namespace interchange_deploy {

	                                                                  
	class SpecialHandlingDeployDeleteJmsMessage : public SpecialHandlingDeployDelete {
	public:
		SpecialHandlingDeployDeleteJmsMessage(void) {}
		virtual ~SpecialHandlingDeployDeleteJmsMessage(void);

		virtual bool HandleRow(long id);

		void DeleteDependents(DbConnection& dbConnection);

		bool Delete() { return true; }

	private:
		std::vector<long> m_vDeletedJmsMessages;

	};
}
}

#endif                                                                

