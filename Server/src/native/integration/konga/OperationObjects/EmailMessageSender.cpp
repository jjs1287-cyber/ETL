/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <memory>

#include <konga/MailClient/SmtpClient.h>

#include "EmailMessage.h"
#include "OperationObjectCreatorDb.h"
#include "EmailMessageSender.h"

using namespace konga;
using std::string;

void konga::SendEmailMessage(long lMessageId, DbConnection& dbConnection, OperationDataElementHolder& deHolder, bool bUseSystemEmailSettingsAsDefault) {
	std::auto_ptr<EmailMessage> pMessage;
	OperationObjectCreatorDb creator;
	pMessage.reset(
		creator.getEmailMessage(lMessageId, dbConnection, deHolder, bUseSystemEmailSettingsAsDefault)
		);

	EmailProperties emailProps(pMessage->getFrom());
	emailProps.addCommaSeparatedToAddresses(pMessage->getTo());
	const string & sCC = pMessage->getCC();
	if ( !sCC.empty() )
		emailProps.addCommaSeparatedCCAddresses(sCC);
	const string& sBCC = pMessage->getBCC();
	if ( !sBCC.empty() )
		emailProps.addCommaSeparatedBCCAddresses(sBCC);
	const string& sReplyTo = pMessage->getReplyTo();
	if ( !sReplyTo.empty() )
		emailProps.setReplyTo(sReplyTo);
	
	SmtpClient client;
	client.addSmtpServers(pMessage->getSMTPMailServers());

	string sAccount, sAccountPassword;
	if ( pMessage->getAuthentication(sAccount, sAccountPassword) ) {
		client.setAuthenticationInfo(sAccount, sAccountPassword);
	}

	client.sendMessage(emailProps, pMessage->getSubject(), pMessage->getMessageText());

}

