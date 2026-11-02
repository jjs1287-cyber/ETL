/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include "SimpleWebServiceClientSoap11.h"

using namespace std;
using namespace konga;

SimpleWebServiceClientSoap11::SimpleWebServiceClientSoap11(void) {
	setMajorSoapVersion(1);
	setMinorSoapVersion(1);
}

SimpleWebServiceClientSoap11::~SimpleWebServiceClientSoap11(void) {
}

string SimpleWebServiceClientSoap11::getHttpHeaders() const {
	stringstream sHeaders;
	sHeaders << "Content-Type: text/xml; charset=\"utf-8\"" << BR;
	                                                                               
	         
	sHeaders << "SOAPAction: \"" << getSoapAction() << "\"";

	                                                                                                          
                                    
                                      
                                               

	return sHeaders.str();
}

bool SimpleWebServiceClientSoap11::validateContentType(const string& stHeaderValue, string& stErrors) const {
	return stHeaderValue == "text/xml";
}

