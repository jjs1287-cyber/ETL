/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_MESSAGING_MESSAGE_SENDER_H
#define ETLFORGE_MESSAGING_MESSAGE_SENDER_H

#include <string>

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Db/DbUtils/DbConnectionHolder.h>

namespace konga {

	class ProcessMessage;
	class JmsMessage;

	class MessageSender : private DbConnectionHolder {
	public:
		MessageSender(DbConnection* pMsgDbConnection = 0);
		virtual ~MessageSender(void);

		                                                               
		                                                                           
		                                                         
		                                   
		                     
		int AddMessageToQueue(const ProcessMessage& processMessage, bool bCommitTransaction = true);

		                                                                                       
		void AddMessageToQueue(const JmsMessage& jmsMessage, std::string& sWarning);

		void EnableValidation(bool bEnable = true);

	private:
		void Validate(const ProcessMessage& message);

	private:	
		bool m_bShouldValidate;
                                  
	};

}                   

#endif                                       
