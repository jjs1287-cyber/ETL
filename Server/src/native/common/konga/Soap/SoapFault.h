/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef SOAPFAULT_H
#define SOAPFAULT_H

#include <string>

#include <konga/Xml/DOMElementPtr.h>

#include "SoapConstants.h"

namespace konga
{

	                                              
	class SoapFault
	{
	public:
		SoapFault(const xml::DOMElementPtr& pFaultNode) : m_pFaultNode(pFaultNode) {}
		virtual ~SoapFault(void) {}
	
		xml::DOMElementPtr GetNode() const { return m_pFaultNode; }
		virtual SoapConstants::Version GetVersion() const = 0;
		virtual std::string GetShortErrorMessage() const = 0;
		virtual std::string GetFormattedErrorMessage() const = 0;
	
		static const std::string NAME;
	
	protected:
		virtual void SetFaultNode(const xml::DOMElementPtr& pFaultNode) = 0;
		xml::DOMElementPtr m_pFaultNode;
	};
}
#endif               

