/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef SIMPLE_WEBSERVICE_CLIENT_SOAP_11_H
#define SIMPLE_WEBSERVICE_CLIENT_SOAP_11_H

#include "SimpleWebServiceClientSoapBase.h"

namespace konga {
	class SimpleWebServiceClientSoap11 : public SimpleWebServiceClientSoapBase {
	public:
		SimpleWebServiceClientSoap11(void);
		~SimpleWebServiceClientSoap11(void);
	
	protected:
		std::string getHttpHeaders() const;
		bool validateContentType(const std::string& stHeaderValue, std::string& stErrors) const;
	};
}
#endif                                      

