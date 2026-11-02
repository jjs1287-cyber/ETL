/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SOAP_FAULT_12
#define KONGA_SOAP_FAULT_12

#include <string>

#include <boost/shared_ptr.hpp>

#include "SoapFault.h"

                                            
namespace konga {
	class Reason
	{
	public:
		Reason(const xml::DOMElementPtr& pReason);
		~Reason(void) { }
	};
	
	class SubCode
	{
	public:
		SubCode(const xml::DOMElementPtr& pSubCodeNode);
		~SubCode(void) { }
	
	private:
		boost::shared_ptr<SubCode> m_pSubCode;
	};
	
	class Code
	{
	public:
		Code(const xml::DOMElementPtr& pCodeNode);
		~Code(void) { }
	
	private:
		xml::DOMElementPtr	m_pCodeNode;
		std::string			m_stValue;
		SubCode*			m_pSubCode;
	};
	
	class SoapFault_12 : public SoapFault
	{
	public:
		SoapFault_12(const xml::DOMElementPtr& pFaultNode);
		~SoapFault_12(void) {}
	
		virtual SoapConstants::Version GetVersion() const { return SoapConstants::Version_12; }
		virtual std::string GetShortErrorMessage() const { return ""; }
		virtual std::string GetFormattedErrorMessage() const { return ""; }
	
	protected:
		virtual void	SetFaultNode(const xml::DOMElementPtr& pFaultNode);
	
	private:
		boost::shared_ptr<Code>		m_pCode;
		boost::shared_ptr<Reason>	m_pReason;
	};
}                   
#endif                       

