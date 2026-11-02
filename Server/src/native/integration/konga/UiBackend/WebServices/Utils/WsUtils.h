/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_WSUTILS_H
#define KONGA_WS_WSUTILS_H

#include "time.h"		         

#include <string>
#include <exception>

#include <axis/AxisWrapperAPI.hpp>                          

#include <konga/Utils/CrException.h>

AXIS_CPP_NAMESPACE_USE

namespace konga {

	                                                                         
	void EscapeUnprintable(std::string& msg);

	   
                                                              
                                                   
                                                          
    
	void AuthenticateUserOfWebService(const char* user, const char* password, int& iUserId);

	int SetSoapFault(IWrapperSoapSerializer& soapSerializer, const std::string& stLocalName,
															 const std::string& stUri,
															 const std::string& stFaultCode,
															 const std::string& stFaultString);

	int SetSoapFault(IWrapperSoapSerializer& soapSerializer, const std::string& stLocalName, const konga::CrException& e);

	int SetSoapFault(IWrapperSoapSerializer& soapSerializer, const std::string& stLocalName, const std::exception& e);

                  
namespace {

	                                                       
	std::string toString(const xsd__string& xsdString) {
		if ( xsdString == 0 )
			return std::string("");
		return std::string(xsdString);
	}

	bool isNonEmptyString(const xsd__string& s) {
		return ( s && *s );
	}

	time_t GetServerTimeFromClientTime(const xsd__long& llClientTime) {
		                                                                           
		long long llServer = llClientTime/1000;
		if ( llServer < 0 )
			llServer = 0;
		return static_cast<time_t>(llServer);
	}

	xsd__long GetClientTimeFromServerTime(const time_t& t) {
		xsd__long llClient = t*1000;
		if ( llClient < 0 )
			llClient = 0;
		return llClient;
	}

	int GetArraySize(const Axis_Array& arr) {
		int n = 0;
		XSDTYPE type;
		arr.get(n, type);
		return n;
	}
}                       
}                   

#endif                      

