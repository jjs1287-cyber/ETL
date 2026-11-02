/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include "SimpleWebServiceClientSoap12.h"

using namespace std;
using namespace konga;

SimpleWebServiceClientSoap12::SimpleWebServiceClientSoap12(void) {
	setMajorSoapVersion(1);
	setMinorSoapVersion(2);
}

SimpleWebServiceClientSoap12::~SimpleWebServiceClientSoap12(void) { }

string SimpleWebServiceClientSoap12::getHttpHeaders() const
{
	stringstream sHeaders;
	sHeaders << "Content-Type: application/soap+xml; charset=\"utf-8\"";
	string stSoapAction(getSoapAction());
	if ( stSoapAction.length() > 0 )
		sHeaders << "; action=\"" << stSoapAction << "\"";

	return sHeaders.str();
}

bool SimpleWebServiceClientSoap12::validateContentType(const std::string& stHeaderValue, std::string& stErrors) const
{
	return stHeaderValue == "application/soap+xml";
}

