/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_MAIL_CLIENT_H
#define ETLFORGE_MAIL_CLIENT_H

#include <string>
#include <vector>

#include "EmailProperties.h"

namespace konga {

	                                  
	                                                  
	                                                                 
	                           
	class SmtpClient {
	public:
		SmtpClient() {}
		virtual ~SmtpClient(void) {}

		void addSmtpServer(const std::string& stSmtpServer);
		void addSmtpServers(const std::vector<std::string>& vSmtpServers);

		void setAuthenticationInfo(const std::string& sAccount, const std::string& sPassword) {
			m_sAccount = sAccount;
			m_sAccountPassword = sPassword;
		}

		void sendMessage(const EmailProperties& props, const std::string& sSubject,
			const std::string& stMsg);

	private:
		std::string m_sAccount, m_sAccountPassword;

		std::vector<std::string> m_vSmtpServers;
	};
}                   

#endif                          

