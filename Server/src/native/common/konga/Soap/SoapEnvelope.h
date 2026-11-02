/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef SOAPENVELOPE_H
#define SOAPENVELOPE_H

#include <string>
#include <konga/Xml/DOMDocumentPtr.h>

#include "SoapConstants.h"

namespace konga {
	class SoapFault;

	namespace SoapEnvelope {

		const std::string ELEMENT_NAME = "Envelope";

		                                
		SoapFault* CheckForSoapFault(const std::string& stXmlFile, std::string& err);
	
		xml::DOMDocumentPtr CreateSoapEnvelopeWithSoapFault(SoapConstants::Version soapVersion,
																		  const SoapFault& soapFault,
																		  std::string& stError);
	
		std::string	GetSoapEnvelopeNamespaceURI(SoapConstants::Version soapVersion);	
	};
}
#endif                  

