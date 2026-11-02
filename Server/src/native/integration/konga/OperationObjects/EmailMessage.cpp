/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include "EmailMessage.h"

using namespace konga;
using std::string;
using std::vector;

EmailMessage::EmailMessage(const string& sName,
						   const string& sTo,
						   const string& sFrom,
						   const string& sCC,
						   const string& sBCC,
						   const string& sReplyTo,
						   const string& sSubject,
						   const string& sAccount,
						   const string& sPassword,
						   const string& sMessageText,
						   const vector<string>& vSMTPServers) :
m_sName(sName), m_sTo(sTo), m_sFrom(sFrom), m_sCC(sCC), m_sBCC(sBCC), m_sReplyTo(sReplyTo),
m_sSubject(sSubject), m_sAccount(sAccount), m_sPassword(sPassword), m_sMessageText(sMessageText),
m_vSMTPMailServers(vSMTPServers)
{}

bool EmailMessage::getAuthentication(string& sAccount, string& sPassword) const {
	if ( m_sAccount.empty() )
		return false;
	sAccount = m_sAccount;
	sPassword = m_sPassword;
	return true;
}

