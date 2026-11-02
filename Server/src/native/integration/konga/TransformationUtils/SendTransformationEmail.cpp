/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <vector>

#include <konga/Utils/StringUtils.h>	                   
#include <konga/Utils/Settings.h>		                      
#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/MailClient/SmtpClient.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/OperationObjects/EmailMessageSender.h>

#include "SendTransformationEmail.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	const string sSmtpClientSection("SmtpClient");

	void send(const EmailProperties& props, const string& sSmtpServers_in,
		const string& sSubject, const string& sMessage,
		const string& sAccount = "", const string& sAccountPwd = "")
	{
		string sSmtpServers(sSmtpServers_in);
		if ( sSmtpServers.empty() )
			sSmtpServers = GetOptionAsString(sSmtpClientSection, "DefaultSmtpServers");

		const vector<string> smtpServers( SplitString(sSmtpServers, ",") );
		if ( sSmtpServers.empty() || smtpServers.empty() )
			throw CrException("Failed to send email. No outgoing smtp servers have been configured.");

		SmtpClient client;
		for( vector<string>::const_iterator cit = smtpServers.begin(); cit != smtpServers.end(); ++cit ) {
			client.addSmtpServer(*cit);
		}

		if ( !sAccount.empty() )
			client.setAuthenticationInfo(sAccount, sAccountPwd);

		client.sendMessage(props, sSubject, sMessage);
	}
}

void konga::SendSystemEmail(const OperationDataElementHolder* const pDEHolder,
							const string& sTo,
							const string& sSubject,
							const string& sMessage)
{
	if ( sTo.empty() )
		throw CrException("Failed to send system email in transformation. No To-address was specified.");

	const string& sFrom = GetOptionAsString(sSmtpClientSection, "DefaultFromEmail");
	if ( sFrom.empty() )
		throw CrException("Failed to send system email. No default From-address has been configured.");
	
	EmailProperties props(sFrom);
	props.addCommaSeparatedToAddresses(pDEHolder->replaceOperationDataElementInString(sTo));

	const string& sAccount = GetOptionAsString(sSmtpClientSection, "DefaultAccount");
	const string& sAccountPwd = GetOptionAsString(sSmtpClientSection, "DefaultAccountPassword");

	send(props, "", sSubject, sMessage, sAccount, sAccountPwd);
}

void konga::SendEmail(const OperationDataElementHolder* const pDEHolder,
					  const string& sFrom, const string& sTo,
					  const string& sSubject, const string& sMessage,
					  const string& sSmtpServers, const string& sAccount,
					  const string& sAccountPwd, const string& sCC,
					  const string& sBCC, const string& sReplyTo)
{
	if ( sFrom.empty() )
		throw CrException("Failed to send email. No From-address was specified.");
	if ( sTo.empty() )
		throw CrException("Failed to send email. No To-address was specified.");

	EmailProperties props(sFrom);
	props.addCommaSeparatedToAddresses(sTo);
	if ( !sCC.empty() )
		props.addCommaSeparatedCCAddresses(sCC);
	if ( !sBCC.empty() )
		props.addCommaSeparatedBCCAddresses(sBCC);

	if ( !sReplyTo.empty() )
		props.setReplyTo(sReplyTo);

	send(props,
		pDEHolder->replaceOperationDataElementInString(sSmtpServers),
		pDEHolder->replaceOperationDataElementInString(sSubject),
		pDEHolder->replaceOperationDataElementInString(sMessage),
		pDEHolder->replaceOperationDataElementInString(sAccount),
		pDEHolder->replaceOperationDataElementInString(sAccountPwd)
		);
}

namespace {
	const string MSG_PREFIX("m.");
}

void konga::SendPreDefinedEmail(const std::string& prefixed_guid, OperationDataElementHolder& deHolder) {
	const string::size_type ipos = prefixed_guid.find(MSG_PREFIX);
	if ( ipos != 0 )
		throw InternalErrorException("The Email Message guid string does not start with \"" + MSG_PREFIX + "\".");

	const string guid( prefixed_guid.substr(MSG_PREFIX.size()) );

	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

	DbGuidToIdMapper guidMapper(pTranDb);

	const long lMessageId = guidMapper.GetId(guid, EmailMessageEntityId);
	if ( lMessageId == GuidToIdMapper::invalid_id ) {
		throw CrException("There is no Email Message with GUID = '" + guid + "' deployed to this server.");
	}

	konga::SendEmailMessage(lMessageId, *pTranDb, deHolder, true);
}

