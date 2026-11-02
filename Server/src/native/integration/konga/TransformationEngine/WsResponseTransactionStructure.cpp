/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include "WsResponseTransactionStructure.h"

#include "crom.h"
#include "XSDParser.h"
#include "XsdFromWsdlCreator.h"

void TransactionPartStructure::Load(CROM* pCromRoot)
{
	m_pCromRoot = pCromRoot;
	if( pCromRoot != 0 )
	{
		m_pHeaderNode = pCromRoot->FindChild(CROM_t_Element,
											 XsdFromWsdlCreator::HEADER,
											 XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
		m_pBodyNode = pCromRoot->FindChild(CROM_t_Element,
										   XsdFromWsdlCreator::BODY,
										   XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);

		m_stTargetNamespace = m_pBodyNode->GetFirstChild() != 0 ?
									m_pBodyNode->GetFirstChild()->GetNamespace() : "";
	}
}

void WsResponseTransactionStructure::Load(CROM* root)
{
	m_pTransactionNode = root->GetFirstChild();
	CROM* pResponseNode = m_pTransactionNode->FindChild(
		CROM_t_Element, XsdFromWsdlCreator::RESPONSE, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
	m_responseStructure.Load(pResponseNode);
	CROM* pRequestNode = m_pTransactionNode->FindChild(
		CROM_t_Element, XsdFromWsdlCreator::REQUEST, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
	if( pRequestNode ) {
		m_bHasRequest = true;
		m_requestStructure.Load(pRequestNode);
	}
}
