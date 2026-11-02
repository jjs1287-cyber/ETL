/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "SoapFault_12.h"

using namespace std;
using namespace konga;

                     
namespace {
	const string SOAP_ENVELOPE_NAMESPACE = "http://www.w3.org/2003/05/soap-envelope";
}

Code::Code(const xml::DOMElementPtr& pCodeNode) : m_pCodeNode(pCodeNode)
{
	xml::DOMElementPtr pValueNode = pCodeNode->selectSingleNode("soap:Value");
	if( pValueNode != 0 )
		m_stValue = pValueNode->getNodeValue();

	xml::DOMElementPtr pSubCodeNode = pCodeNode->selectSingleNode("soap:SubCode");
	if( pSubCodeNode != 0 ) {
		                      
	}
}

SoapFault_12::SoapFault_12(const xml::DOMElementPtr& pFaultNode) : SoapFault(pFaultNode) {
	SetFaultNode(pFaultNode);
}

void SoapFault_12::SetFaultNode(const xml::DOMElementPtr& pFaultNode)
{
	m_pFaultNode = pFaultNode;
	if( pFaultNode != 0 ) {
		xml::DOMElementPtr pCodeNode = pFaultNode->selectSingleNode("soap:Code");
		m_pCode.reset( new Code(pCodeNode) );
	}
}

