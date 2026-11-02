/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <algorithm>		              

#include <konga/Utils/StringUtils.h>	               
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/CrException.h>

#include "EmailProperties.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	void add(EmailProperties::vAddrT& v, const string& sEmail)
	{
		EmailAddress addr(sEmail);
		if ( std::find<EmailProperties::vAddrT::iterator, EmailAddress>(v.begin(), v.end(), addr) == v.end() ) {
			v.push_back(addr);
		}
	}

	void addCommaSeparated(EmailProperties::vAddrT& v,
		const std::string& stAddresses)
	{
		StringTokenizer tokenizer(stAddresses.c_str(), "\",");
		string sEmail;
		bool bQuoted = false;
		while( tokenizer.NextToken() ) {
			const char* token = tokenizer.token();
			if ( token == 0 || *token == 0 )
				continue;

			switch(token[0]) {
				case '\"':
					bQuoted = !bQuoted;
					sEmail += token;
					break;
				case ',':
					if ( bQuoted )
						sEmail += token;
					else {
						sEmail = TrimString(sEmail);
						if ( sEmail.find_first_not_of(" \t\"") != string::npos )
							add(v, sEmail);
						sEmail.clear();
					}
					break;
				default:
					sEmail += token;
					break;
			};
		}

		if ( bQuoted )
			throw CrException("The given list of email addresses ("
			+ stAddresses + ") contains an unclosed quote character.");

		sEmail = TrimString(sEmail);
		if ( sEmail.find_first_not_of(" \t\"") != string::npos )
			add(v, sEmail);
	}
}

void EmailProperties::setFrom(const std::string& sFrom) {
	EmailAddress e(sFrom);
	m_From = e;
}

void EmailProperties::setReplyTo(const std::string& sReplyTo) {
	EmailAddress e(sReplyTo);
	m_ReplyTo = e;
}

void EmailProperties::setReturnPath(const std::string& sReturnPath) {
	EmailAddress e(sReturnPath);
	m_ReturnPath = e;
}

void EmailProperties::addToAddress(const std::string& sEmail) {
	add(m_vTo, sEmail);
}

void EmailProperties::addCCAddress(const std::string& sEmail) {
	add(m_vCC, sEmail);
}

void EmailProperties::addBCCAddress(const std::string& sEmail) {
	add(m_vBCC, sEmail);
}

void EmailProperties::addCommaSeparatedToAddresses(const std::string& stAddresses) {
	addCommaSeparated(m_vTo, stAddresses);
}

void EmailProperties::addCommaSeparatedCCAddresses(const std::string& stAddresses) {
	addCommaSeparated(m_vCC, stAddresses);
}

void EmailProperties::addCommaSeparatedBCCAddresses(const std::string& stAddresses) {
	addCommaSeparated(m_vBCC, stAddresses);
}

