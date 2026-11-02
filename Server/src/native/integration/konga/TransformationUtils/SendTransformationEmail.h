/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_SEND_TRANSFORMATION_EMAIL
#define ETLFORGE_TRANSFORMATION_ENGINE_SEND_TRANSFORMATION_EMAIL

#include <string>

namespace konga {

	class OperationDataElementHolder;
	class EmailMessage;

	void SendSystemEmail(const OperationDataElementHolder* const pDEHolder,
		const std::string& sTo,
		const std::string& sSubject, const std::string& sMessage);

	void SendEmail(const OperationDataElementHolder* const pDEHolder,
		const std::string& sFrom, const std::string& sTo,
		const std::string& sSubject, const std::string& sMessage,
		const std::string& sSmtpServers, const std::string& sAccount,
		const std::string& sAccountPwd, const std::string& sCC,
		const std::string& sBCC, const std::string& sReplyTo);

	                                                                                                             
	void SendPreDefinedEmail(const std::string& prefixed_guid, OperationDataElementHolder& deHolder);
}

#endif                                                            

