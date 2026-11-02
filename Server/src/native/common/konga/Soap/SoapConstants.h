/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef CRSOAPCONSTANTS_H
#define CRSOAPCONSTANTS_H

#include <string>

namespace konga {
	namespace SoapConstants
	{
		enum Version { Version_11, Version_12 };
	
		const std::string	NAMESPACE_V11 = "http://schemas.xmlsoap.org/soap/envelope/";
		const std::string	NAMESPACE_V12 = "http://www.w3.org/2003/05/soap-envelope";
	
		const std::string ENCODING_STYLE = "encodingStyle";
		const std::string RPC = "rpc";
		const std::string DOCUMENT = "document";
		const std::string ENCODED = "encoded";
		const std::string LITERAL = "literal";
	
		enum SoapStyle { Rpc, Document };
		enum SoapBodyUse { Encoded, Literal };
	
	}
}
#endif                     

