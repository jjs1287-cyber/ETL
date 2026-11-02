/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SOAP_ENCODING_H
#define KONGA_SOAP_ENCODING_H

#include <string>

namespace konga {
	namespace SoapEncoding {
	
		const std::string VERSION_11_NAMESPACE = "http://schemas.xmlsoap.org/soap/encoding/";
		const std::string VERSION_12_NAMESPACE = "http://www.w3.org/2002/06/soap-envelope";
	
		enum SoapEncodingVersion {
			SoapEncoding_Version_11 = 0,
			SoapEncoding_Version_12 = 1
		};
	
		SoapEncodingVersion GetDefaultVersion();

		                                                                         
		bool GetSchemaLocation(SoapEncodingVersion version, std::string& stSchemaLocation);
	}
}

#endif                         

