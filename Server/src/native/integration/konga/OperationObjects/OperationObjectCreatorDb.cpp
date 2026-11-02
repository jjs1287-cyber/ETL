/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Utils/Settings.h>
#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/TranDb/MessagesTab.h>

#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/OperationObjects/EmailMessage.h>

#include "OperationObjectCreatorDb.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	const string sSmtpClientSection("SmtpClient");
}

EmailMessage* OperationObjectCreatorDb::getEmailMessage(long lMessageId,
												  DbConnection& dbConnection,
												  OperationDataElementHolder& deHolder,
												  bool bUseSystemEmailSettingsAsDefault)
{
	m_pDEHolder = &deHolder;
	SmartDbPtr<TranDb::MessagesTab> pMessagesTab( TranDb::MessagesTab::MakeInstance(&dbConnection) );

	pMessagesTab->Select("WHERE Message_ID = " + toString(lMessageId));
	if ( !pMessagesTab->Next() )
		throw CrException("No email message with MessageID = " + toString(lMessageId) + " could be found." \
			" Maybe the message has been deleted without updating operations using the message.");

	const string& sMsgName = pMessagesTab->IsMessage_NameNull() ? "" : pMessagesTab->GetMessage_Name();
	string sSmtpServers = replace_de( pMessagesTab->IsSmtp_ServersNull() ? "" : pMessagesTab->GetSmtp_Servers() );
	if ( sSmtpServers.empty() && bUseSystemEmailSettingsAsDefault )
		sSmtpServers = GetOptionAsString(sSmtpClientSection, "DefaultSmtpServers");
	if ( sSmtpServers.empty() )
		throw CrException("No SMTP servers configured for email message '" + sMsgName + ".");

	vector<string> smtpServers = SplitString(sSmtpServers, ",");
	if ( smtpServers.empty() )
		throw CrException("No SMTP servers configured for email message '" + sMsgName + "'.");

	const string& sTo = pMessagesTab->IsSend_ToNull() ? "" : pMessagesTab->GetSend_To();
	if ( sTo.empty() )
		throw CrException("No recipient was specified for email message '" + sMsgName + "'.");

	string sFrom = pMessagesTab->IsSent_FromNull() ? "" : pMessagesTab->GetSent_From();
	if ( sFrom.empty() && bUseSystemEmailSettingsAsDefault )
		sFrom = konga::GetOptionAsString(sSmtpClientSection, "DefaultFromEmail");
	if ( sFrom.empty() )
		throw CrException("No sender (From field) was specified for email message '" + sMsgName + ".");

	const string& sCC = pMessagesTab->IsCcNull() ? "" : pMessagesTab->GetCc();
	const string& sBCC = pMessagesTab->IsBccNull() ? "" : pMessagesTab->GetBcc();
	const string& sReplyTo = pMessagesTab->IsReply_ToNull() ? "" : pMessagesTab->GetReply_To();
	const string& sSubject = pMessagesTab->IsSubjectNull() ? "" : pMessagesTab->GetSubject();

	const string& sMessage = pMessagesTab->IsMessage_TextNull() ? "" : pMessagesTab->GetMessage_Text();

	const string& sAccount = pMessagesTab->IsAccountNull() ? "" : pMessagesTab->GetAccount();
	string sAccountPassword;
	if ( !sAccount.empty() ) {
		sAccountPassword = pMessagesTab->IsAccount_PasswordNull() ? "" : pMessagesTab->GetAccount_Password();
		sAccountPassword = DecryptString(sAccountPassword);
	}

	return new EmailMessage(sMsgName, replace_de(sTo), replace_de(sFrom),
		replace_de(sCC), replace_de(sBCC), replace_de(sReplyTo), replace_de(sSubject),
		replace_de(sAccount), replace_de(sAccountPassword), replace_de(sMessage), smtpServers);
}

string OperationObjectCreatorDb::replace_de(const string& s) {
	assert(m_pDEHolder);
	return m_pDEHolder->replaceOperationDataElementInString(s);
}
