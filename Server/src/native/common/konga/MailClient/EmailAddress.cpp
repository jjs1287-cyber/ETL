/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string.h>	                      

#include <konga/Utils/StringUtils.h>	               
#include <konga/Utils/StringTokenizer.h>

#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>	                       

#include "EmailAddress.h"

using namespace konga;
using std::string;

void EmailAddress::parseEmail(const std::string& sEmail_in) {
	const string& sTmp = TrimString(sEmail_in);
	if ( sTmp.empty() )
		throw CrException("Email is empty.");

	StringTokenizer tokenizer(sTmp.c_str(), "\"<>");

	string sPersonalName, sEmail;
	bool bQuoted = false, bEmail = false;
	while ( tokenizer.NextToken() ) {
		const char* token = tokenizer.token();
		if ( token == 0 || *token == 0 )
			break;
		switch( token[0] ) {
			case '"':
				if ( bEmail )
					throw CrException(sEmail_in + " is not a valid email address. The email address contains a quote(\") character.");
				else
					sPersonalName += token;
				bQuoted = !bQuoted;
				break;
			case '<':
				if ( bQuoted ) {
					sPersonalName += token;
				}
				else {
					if ( bEmail )
						throw CrException(sEmail_in + " is not a valid email address. The email address contains a '<' character.");
					bEmail = true;
				}
				break;
			case '>':
				if ( bQuoted ) {
					if ( bEmail )
						throw CrException(sEmail_in + " is not a valid email address. The email address contains a '>' character.");
					else
						sPersonalName += token;
				}
				else {
					if ( !bEmail )
						throw CrException(sEmail_in + " is not a valid email address, it contains an unquoted '>' character that is not preceeded by an unquoted '<' character.");
					bEmail = false;
					goto done;
				}
				break;
			default:
				if ( bEmail )
					sEmail += token;
				else
					sPersonalName += token;
				break;
		};
	}

done:;
	if ( bQuoted )
		throw CrException(sEmail_in + " is not a valid email address, an opening quote character is not closed.");
	if ( bEmail )
		throw CrException(sEmail_in + " is not a valid email address, the closing '>' character is missing.");

	if ( sEmail.empty() ) {
		sEmail = sPersonalName;
		sPersonalName.clear();
	}

	const string::size_type posIllegal = sEmail.find_first_of("\"<>,");
	if ( posIllegal != string::npos )
		throw CrException(sEmail_in + " is not a valid email address, " \
		"the email address contains an invalid character("
		+ sEmail.substr(posIllegal, 1) + "). Did you forget to enclose the email address in <>?");

	                              
	sPersonalName = TrimString(TrimString(sPersonalName), '\"');
	sEmail = TrimString(sEmail);

	const string::size_type ipos = sEmail.find("@");
	if ( ipos == string::npos )
		throw CrException(sEmail_in + " is not a valid email address, it contains no '@' character as mailbox/host separator.");
	const string& sMailBox = sEmail.substr(0, ipos);
	const string& sHost = sEmail.substr(ipos+1);

	if ( sMailBox.empty() )
		throw CrException(sEmail_in + " is not a valid email address. No mailbox was specified.");
	if ( sHost.empty() )
		throw CrException(sEmail_in + " is not a valid email address. No host was specified.");

	m_sPersonalName = sPersonalName;
	m_sMailBox = sMailBox;
	m_sHost = sHost;
}

std::string EmailAddress::getEmailAddress() const {
	string sTmp;
	if ( !m_sPersonalName.empty() ) {
		return "\"" + m_sPersonalName + "\" <" + getMailBoxAtHost() + ">";
	}
	else
		return getMailBoxAtHost();
}

std::string EmailAddress::getMailBoxAtHost() const {
	return string(m_sMailBox + "@" + m_sHost);
}

bool EmailAddress::empty() const {
	return ( m_sMailBox.empty() && m_sHost.empty() );
}

bool konga::operator==(const EmailAddress& lhs, const EmailAddress& rhs) {
	                                                                                               
	return strcasecmp(lhs.getMailBoxAtHost().c_str(), rhs.getMailBoxAtHost().c_str()) == 0;
}

