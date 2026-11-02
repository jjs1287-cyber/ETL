/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_OBJECTS_EMAIL_MESSAGE_H
#define KONGA_OPERATION_OBJECTS_EMAIL_MESSAGE_H

#include <string>
#include <vector>

namespace konga {

	class EmailMessage {
	public:
		EmailMessage(const std::string& sName,
			const std::string& sTo, const std::string& sFrom, const std::string& sCC,
			const std::string& sBCC, const std::string& sReplyTo, const std::string& sSubject,
			const std::string& sAccount, const std::string& sPassword, const std::string& sMessageText,
			const std::vector<std::string>& vSMTPServers);
		~EmailMessage(void) {}

		std::string getName() const { return m_sName; }

		const std::vector<std::string>& getSMTPMailServers() const { return m_vSMTPMailServers; }

		std::string getTo() const { return m_sTo; }
		std::string getFrom() const { return m_sFrom; }

		                                         
		                                      
		bool getAuthentication(std::string& sAccount, std::string& sPassword) const;

		std::string getCC() const { return m_sCC; }
		std::string getBCC() const { return m_sBCC; }
		std::string getReplyTo() const { return m_sReplyTo; }

		std::string getSubject() const { return m_sSubject; }
		std::string getMessageText() const { return m_sMessageText; }

	private:
		const std::string m_sName, m_sTo, m_sFrom, m_sCC, m_sBCC, m_sReplyTo;
		const std::string m_sSubject, m_sMessageText, m_sAccount, m_sPassword, m_MessageText;
		const std::vector<std::string> m_vSMTPMailServers;
	};
}

#endif                                           

