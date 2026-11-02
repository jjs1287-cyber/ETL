/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <konga/Xml/XmlUtils.h>
#include <konga/Utils/StringUtils.h>

#include "SoapEnvelope.h"
#include "SoapFault_11.h"

using namespace std;
using namespace konga;

const FaultCodeValue_11 FaultCodeValue_11::VersionMismatch("VersionMismatch");
const FaultCodeValue_11 FaultCodeValue_11::MustUnderstand("MustUnderstand");
const FaultCodeValue_11 FaultCodeValue_11::Client("Client");
const FaultCodeValue_11 FaultCodeValue_11::Server("Server");

const string FaultString::NAME = "faultstring";
const string FaultCode::NAME = "faultcode";
const string FaultActor::NAME = "faultactor";
const string FaultDetail::NAME = "detail";

FaultDetail::FaultDetail(const xml::DOMElementPtr& pFaultDetailNode) : m_pFaultDetailNode(pFaultDetailNode) {
	                                                                                                                              
	const xml::DOMNodePtr p = pFaultDetailNode->getFirstChild();
	if( p != 0 ) m_stValue = p->getXml();
}

FaultActor::FaultActor(const xml::DOMElementPtr& pFaultActorNode) : m_pFaultActorNode(pFaultActorNode) {
	m_stValue = XmlUtils::GetNodeValue(pFaultActorNode);
}

FaultString::FaultString(const xml::DOMElementPtr& pFaultStringNode) : m_pFaultStringNode(pFaultStringNode) {
	m_stValue = XmlUtils::GetNodeValue(pFaultStringNode);
}

FaultCode::FaultCode(const xml::DOMElementPtr& pFaultCodeNode) : m_pFaultCodeNode(pFaultCodeNode) {
	m_stValue = XmlUtils::GetNodeValue(pFaultCodeNode);
}

SoapFault_11::SoapFault_11(const xml::DOMElementPtr& pFaultNode          ) 
	: SoapFault(pFaultNode) {
	SetFaultNode(pFaultNode);
}

SoapFault_11::SoapFault_11(const FaultCodeValue_11& faultCode,
						   const string& stFaultString,
						   const string& stFaultActor           ,
						   const string& stFaultDetail           ) : SoapFault(0) {
	SetFaultNode( CreateFaultNode(faultCode, stFaultString, stFaultActor, stFaultDetail) );
}

xml::DOMElementPtr SoapFault_11::CreateFaultNode(const FaultCodeValue_11& faultCode,
													   const string& stFaultString,
													   const string& stFaultActor,
													   const string& stFaultDetail)
{
	xml::DOMDocumentPtr pDoc;
	string stErr;
	if( XmlUtils::CreateDOMDocument2Instance(pDoc, false, stErr) != 0 )
	{
		                                                       
		return 0;
	}

	xml::DOMElementPtr pFaultNode = pDoc->createElementNS(SoapConstants::NAMESPACE_V11, SoapFault::NAME);
	xml::DOMElementPtr pCodeNode = pDoc->createElementNS(SoapConstants::NAMESPACE_V11, FaultCode::NAME);

	                                                                                     
	                                                 
	                                                                                      
	string stPrefix("soap-env");
	XmlUtils::AddNamespaceDeclaration(pCodeNode, SoapConstants::NAMESPACE_V11, stPrefix);
	                                   
	pCodeNode->setText( XmlUtils::GetQualifiedName(stPrefix, faultCode.ToString()) );
	pFaultNode->appendChild(pCodeNode);

	xml::DOMElementPtr pFaultStringNode = pDoc->createElementNS(SoapConstants::NAMESPACE_V11, FaultString::NAME);
	pFaultStringNode->setText( stFaultString );
	pFaultNode->appendChild(pFaultStringNode);

	if( !stFaultActor.empty() ) {
		xml::DOMElementPtr pActorNode = pDoc->createElementNS(SoapConstants::NAMESPACE_V11, FaultActor::NAME);
		pActorNode->setText(stFaultActor);
		pFaultNode->appendChild(pActorNode);
	}

	if( !stFaultDetail.empty() ) {
		xml::DOMElementPtr pDetailNode = pDoc->createElementNS(SoapConstants::NAMESPACE_V11, FaultDetail::NAME);
		pDetailNode->setText(stFaultDetail);
		pFaultNode->appendChild(pDetailNode);
	}
	return pFaultNode;
}

void SoapFault_11::SetFaultNode(const xml::DOMElementPtr& pFaultNode)
{
	if( pFaultNode != 0 ) {
		m_pFaultNode = pFaultNode;
		xml::DOMNodeListPtr pChildNodes = pFaultNode->getChildNodes();
		for( unsigned long i = 0, nNodes = pChildNodes->getLength(); i < nNodes; ++i )
		{
			xml::DOMNodePtr pChild = pChildNodes->item[i];
			if( pChild->getNodeType() == xml::NODE_ELEMENT )
			{
				string stBaseName = XmlUtils::GetNodeBaseName(pChild);
				if( Equals(stBaseName, FaultCode::NAME) )
					m_pFaultCode.reset( new FaultCode(pChild) );
				else if( Equals(stBaseName, FaultString::NAME) )
					m_pFaultString.reset( new FaultString(pChild) );
				else if( Equals(stBaseName, FaultActor::NAME) )
					m_pFaultActor.reset( new FaultActor(pChild) );
				else if( Equals(stBaseName, FaultDetail::NAME) )
					m_pFaultDetail.reset( new FaultDetail(pChild) );
			}
		}
	}
}

string SoapFault_11::GetShortErrorMessage() const
{
	return m_pFaultString ? m_pFaultString->GetValue() : "Unknown error";
}

string SoapFault_11::GetFormattedErrorMessage() const
{
	string stErr;
	if( m_pFaultCode )
	{
		stErr = "Code: " + m_pFaultCode->GetValue();
	}
	if( m_pFaultString )
	{
		if( !stErr.empty() )
			stErr += "\n";
		stErr += "Message: " + m_pFaultString->GetValue();
	}
	if( m_pFaultActor )
	{
		if( !stErr.empty() )
			stErr += "\n";
		stErr += "Actor: " + m_pFaultActor->GetValue();
	}
	if ( m_pFaultDetail ) {
		if( !stErr.empty() )
			stErr += "\n\n";
		stErr += "Detail: " + m_pFaultDetail->GetValue();
	}
	if( stErr.empty() )
		stErr = "Unknown error.";
	return stErr;
}

