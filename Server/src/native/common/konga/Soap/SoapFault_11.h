/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef SOAPFAULT_11_H
#define SOAPFAULT_11_H
                                            

#include <string>
#include <vector>

#include <boost/shared_ptr.hpp>

#include <konga/Xml/DOMElementPtr.h>

#include "SoapFault.h"

namespace konga
{
	class FaultCodeValue_11
	{
	public:
		std::string ToString() const { return m_stStringRepresentation; }
	
		static const FaultCodeValue_11 VersionMismatch;
		static const FaultCodeValue_11 MustUnderstand;
		static const FaultCodeValue_11 Client;
		static const FaultCodeValue_11 Server;
	
	private:
		FaultCodeValue_11(const std::string& stStringRep) : m_stStringRepresentation(stStringRep) {};
		std::string m_stStringRepresentation;
	};
	
	class FaultDetail
	{
	public:
		FaultDetail(const xml::DOMElementPtr& pFaultDetailNode);
		~FaultDetail(void) {};

		std::string	GetValue() const { return m_stValue; }
	
		static const std::string NAME;
	
	private:
		xml::DOMElementPtr m_pFaultDetailNode;
		std::string m_stValue;
	};
	
	
	class FaultActor
	{
	public:
		FaultActor(const xml::DOMElementPtr& pFaultActorNode);
		~FaultActor(void) {};
	
		std::string	GetValue() const { return m_stValue; }
	
		static const std::string NAME;
	
	private:
		xml::DOMElementPtr m_pFaultActorNode;
		std::string m_stValue;
	};
	
	class FaultString
	{
	public:
		FaultString(const xml::DOMElementPtr& pFaultStringNode);
		~FaultString(void) {};
	
		std::string	GetValue() const { return m_stValue; }
	
		static const std::string NAME;
	
	private:
		xml::DOMElementPtr m_pFaultStringNode;
		std::string m_stValue;
	};
	
	
	class FaultCode
	{
	public:
		FaultCode(const xml::DOMElementPtr& pFaultCodeNode);
		~FaultCode(void) {};
	
		std::string	GetValue() const { return m_stValue; }
	
		static const std::string NAME;
	
	private:
		xml::DOMElementPtr m_pFaultCodeNode;
		std::string m_stValue;
	};
	
	
	class SoapFault_11 : public SoapFault
	{
	public:
		SoapFault_11(const xml::DOMElementPtr& pSoapNode = 0);
		SoapFault_11(const FaultCodeValue_11& faultCode,
					 const std::string& stFaultString,
					 const std::string& stFaultActor = "",
					 const std::string& stFaultDetail = "");
		~SoapFault_11(void) {}
	
		virtual SoapConstants::Version GetVersion() const { return SoapConstants::Version_11; }
		virtual std::string GetShortErrorMessage() const;
		virtual std::string GetFormattedErrorMessage() const;
	
	protected:
		virtual void SetFaultNode(const xml::DOMElementPtr& pFaultNode);
	
	private:
		boost::shared_ptr<FaultCode>	m_pFaultCode;
		boost::shared_ptr<FaultString>	m_pFaultString;
		boost::shared_ptr<FaultActor>	m_pFaultActor;
		boost::shared_ptr<FaultDetail>	m_pFaultDetail;
	
		xml::DOMElementPtr CreateFaultNode(const FaultCodeValue_11& faultCode,
										   const std::string& stFaultString,
										   const std::string& stFaultActor,
										   const std::string& stFaultDetail);
	};
}                   
#endif                  

