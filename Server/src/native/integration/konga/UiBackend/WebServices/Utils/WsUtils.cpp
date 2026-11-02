/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Authentication/Authentication.h>	              

#include "UiBackendLogging.h"
#include "WsUtils.h"

using namespace konga;
using std::string;

void konga::AuthenticateUserOfWebService(const char* user, const char* password, int& iUserId) {
	const string stUser(user==0? "" : user);
	if( stUser.empty() )
		throw CrException("No user name was specified.");

	const string stPassword(password==0 ? "" : password);

	if ( !Authentication::IsValidUser(stUser, stPassword, iUserId) ) {
		throw Authentication::AuthenticationException("Authentication failed for user " + stUser, stUser);
	}
}

namespace {
	const char illegal[]="\001\002\003\004\005\006\007\010\013\016\017\020\021\022\023\024\025\026\027\030\031\032\033\034\035\036\037";
}

void konga::EscapeUnprintable(string& msg) {
	const string::size_type size = msg.size();
	string::size_type pos = 0;
	while ( (pos=msg.find_first_of(illegal, pos)) != string::npos ) {
		if ( msg.at(pos)== '\001' )
			msg.at(pos) = '\t';
		else
			msg.at(pos) = '?';
		++pos;
		if ( pos >= size )
			break;
	}
}

int konga::SetSoapFault(IWrapperSoapSerializer& soapSerializer, const std::string& stLocalName,
						const std::string& stUri,
						const std::string& stFaultCode,
						const std::string& stFaultString)
{
	UIBACKEND_TRACE("Exception occurred: LocalName = " + stLocalName +
		", Uri = " + stUri +
		", FaultCode = " + stFaultCode +
		", FaultString = " + stFaultString);

	std::string tmp(stFaultString);
	konga::EscapeUnprintable(tmp);
	return soapSerializer.createSoapFault(stLocalName.c_str(),
		stUri.c_str(), stFaultCode.c_str(), tmp.c_str());
}

int konga::SetSoapFault(IWrapperSoapSerializer& soapSerializer, const std::string& stLocalName, const konga::CrException& e) {
	return SetSoapFault(soapSerializer, stLocalName, "http://soapinterop.org",
		"CrException Error code = " + e.GetErrorNumberAsString(), e.GetErrorString());
}

int konga::SetSoapFault(IWrapperSoapSerializer& soapSerializer, const std::string& stLocalName, const std::exception& e) {
	return SetSoapFault(soapSerializer, stLocalName, "http://soapinterop.org", "std::exception",  e.what() ? e.what() : "Unknown exception.");
}
