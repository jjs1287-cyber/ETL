/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SIMPLE_WEBSERVICE_CLIENT_SOAP_12_H
#define KONGA_SIMPLE_WEBSERVICE_CLIENT_SOAP_12_H

#include "SimpleWebServiceClientSoapBase.h"

namespace konga {
	class SimpleWebServiceClientSoap12 : public SimpleWebServiceClientSoapBase {
	public:
		SimpleWebServiceClientSoap12(void);
		~SimpleWebServiceClientSoap12(void);
	
	protected:
		std::string getHttpHeaders() const;
		bool validateContentType(const std::string& stHeaderValue, std::string& stErrors) const;
	};
}
#endif                                            

