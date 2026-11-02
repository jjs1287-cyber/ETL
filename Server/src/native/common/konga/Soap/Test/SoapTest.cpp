/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <memory>

#include <konga/Soap/SoapEnvelope.h>
#include <konga/Soap/SoapFault.h>
#include <konga/Soap/SoapFault_11.h>
#include <konga/Soap/SoapEnvelope.h>

using namespace std;
using namespace konga;

int testSoapFault();
int createSoapFault();

int main (int argc, char *argv[])
{
                          
	return createSoapFault();
}

int createSoapFault() {

	string stFaultString("Error message?");
	string stErr("Error message?");
	SoapFault_11 soapFault(FaultCodeValue_11::Server, stFaultString);
	std::cout << soapFault.GetNode()->getXml() << std::endl << std::endl;

	xml::DOMDocumentPtr pEnvelope = SoapEnvelope::CreateSoapEnvelopeWithSoapFault(
		SoapConstants::Version_11, soapFault, stErr);
	std::cout << pEnvelope->getXml() << std::endl;

	return 0;
}

int testSoapFault() {
	const string stFileName("C:\\tmp\\t331_res.xml");
                                                      
	string err;
	std::auto_ptr<SoapFault> pSoapFault( SoapEnvelope::CheckForSoapFault(stFileName, err) );
	if ( pSoapFault.get() == 0 ) {
		                   
		if ( !err.empty() ) {
			std::cerr << err << std::endl;
		}
		return 1;
	}
	else {
		std::cout << pSoapFault->GetFormattedErrorMessage() << std::endl;
	}
	return 0;
}

int testSoapFault_old() {
	string stError;
	auto_ptr<SoapFault> pSoapFault( SoapEnvelope::CheckForSoapFault("data/SoapFault.xml", stError) );
	if ( !stError.empty() ) {
		cout << stError << endl;
		return -1;
	}
	else if ( pSoapFault.get() == 0 ) {
		cout << "Error: No soap fault found in this file." << endl;
		return -1;
	}

	cout << "Short error message found: " << pSoapFault->GetShortErrorMessage() << endl;
	cout << "FormattedErrorMessage: " << pSoapFault->GetFormattedErrorMessage() << endl;

	cout << "testSoapFault successful" << endl;
	return 0;
}

