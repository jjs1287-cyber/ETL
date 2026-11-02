/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>

#include <konga/Utils/ErrorResolver.h>
#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XsdConstants.h>
#include <konga/Xml/CrXmlException.h>

#include "SoapEnvelope.h"
#include "SoapFault_11.h"
#include "SoapFault_12.h"

using namespace std;
using namespace konga;

SoapFault* SoapEnvelope::CheckForSoapFault(const string& stXmlFile, string& err)
{
	try {
		string stErr;
		xml::DOMDocumentPtr pDocument( XmlUtils::LoadDoc(stXmlFile, stErr, true) );
		if ( !stErr.empty() ) {
			err = stErr;
			return 0;
		}

		string stSoap11Prefix("soap-env11");
		string stSoap12Prefix("soap-env12");
		pDocument->addSelectionNamespace(stSoap11Prefix, SoapConstants::NAMESPACE_V11);
		pDocument->addSelectionNamespace(stSoap12Prefix, SoapConstants::NAMESPACE_V12);

		                               
		string stXPath_11("//" + stSoap11Prefix + ":Fault");
		xml::DOMNodePtr pFaultNode11 = pDocument->selectSingleNode(stXPath_11);
		if( pFaultNode11 != 0 )
			return new SoapFault_11(pFaultNode11);

		                              
		string stXPath_12("//" + stSoap12Prefix + ":Fault");
		xml::DOMNodePtr pFaultNode12 = pDocument->selectSingleNode(stXPath_12);
		if( pFaultNode12 != 0 )
			return new SoapFault_12(pFaultNode12);
	}
	catch( const xml::CrXmlException& ex )
	{
		err = ex.GetErrorString();
		return 0;
	}
	return 0;
}

string SoapEnvelope::GetSoapEnvelopeNamespaceURI(SoapConstants::Version soapVersion)
{
	return (soapVersion == SoapConstants::Version_11) ? SoapConstants::NAMESPACE_V11 : SoapConstants::NAMESPACE_V12;
}

xml::DOMDocumentPtr SoapEnvelope::CreateSoapEnvelopeWithSoapFault(SoapConstants::Version soapVersion,
																  const SoapFault& soapFault,
																  string& stError)
{
	xml::DOMDocumentPtr pDoc;
	if ( pDoc.CreateInstance() != 0 ) {
		stError = "Failed to create DOM document instance" + pDoc->getLastError();
		return 0;
	}

	string stNamespace(GetSoapEnvelopeNamespaceURI(soapVersion));
	string stPrefix("soap-env");

	                              
	xml::DOMElementPtr pEnvelope = pDoc->createElementNS(stNamespace, XmlUtils::GetQualifiedName(stPrefix, ELEMENT_NAME));
	                                   
	pDoc->appendChild(pEnvelope);
	
	                                 
	XmlUtils::AddNamespaceDeclaration(pEnvelope, XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001, "xsd");
	XmlUtils::AddNamespaceDeclaration(pEnvelope, XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001, "xsi");

	                          
	xml::DOMElementPtr pBody = pDoc->createElementNS(stNamespace, XmlUtils::GetQualifiedName(stPrefix, "Body"));
	                                    
	pEnvelope->appendChild(pBody);

	                                                         
	pBody->appendChild( soapFault.GetNode() );

	return pDoc;
}

