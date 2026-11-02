/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                                 
  
                                                                      

#ifdef DS_USE_SOAP

#include "SoapException.h"

                                                                      
                           
                                                                      

CSoapException::CSoapException(errCode err, std::string& cause):
m_errCode(err), m_cause(cause)
{

}

CSoapException::~CSoapException()
{

}


std::string CSoapException::GetErrorMessage()
{
	if (m_errCode == ERR_UNKNOWN)
		return m_cause;

	std::string localMsg;
	localMsg = GetErrorCodeString();
	localMsg += _T(":\n");

	switch (m_errCode) {
	case SOAP_SUCCESS:
		localMsg += _T("Call to SOAP object was successful");
		break;
	case ERR_SOAP_INVALID_PARAM_TO_SENDREQUEST:
		localMsg += _T("Invalid parameters passed to method CHttpPost::SendRequest");
		break;
	case ERR_SOAP_INTERNET_SESSION:
		localMsg += _T("Unable to create internet session object for SOAP object");
		break;
	case ERR_SOAP_SETTING_URL:
		localMsg += _T("Unable to set the URL string");
		break;
	case ERR_SOAP_INTERNET_CONNECT:
		localMsg += _T("Unable to connect to internet");
		break;
	case ERR_SOAP_REQUEST_HEADER:
		localMsg += _T("Unable to get the SOAP request header");
		break;
	case ERR_SOAP_SEND_REQUEST:
		localMsg += _T("Unable to send the SOAP request or receive SOAP response");
		break;
	case ERR_SOAP_REQUEST_THREAD:
		localMsg += _T("Error creating thread");
		break;
	case ERR_SOAP_REQUEST_TIMEOUT:
		localMsg += _T("Timeout error. Please try again later or increase the timeout interval");
		break;
	case ERR_SOAP_BAD_RESPONSE:
		localMsg += _T("Unable to process the response received from SOAP server");
		break;
	case ERR_SOAP_PROGRAM_ERROR:
		localMsg += _T("An unknown internal error occurred while sending/receiving SOAP call/response");
		break;
	case ERR_SOAP_XML_PARSE:
		localMsg += _T("Unable to parse the XML stream containing the SOAP request/response");
		break;
	case ERR_SOAP_XML_DOM:
		localMsg += _T("An internal error occurred while parsing the SOAP request/response");
		break;
	case ERR_SOAP_ARGUMENT_STRING:
		localMsg += _T("Syntax error in argument string supplied to ETLForge engine");
		break;
	case ERR_SOAP_LOCATION_STRING:
		localMsg += _T("Syntax error in location string supplied to ETLForge engine");
		break;
	case ERR_SOAP_TASK_CANCELLED:
		localMsg += _T("A task was cancelled by the user while making a call to SOAP object");
	default:
		localMsg += _T("An unknown error occurred while calling SOAP object.\n");
		localMsg += _T("Please contact ETLForge or your SOAP service provider.");
		break;
	}

	localMsg += _T("\n") + m_cause;
	return localMsg;
}


std::string CSoapException::GetErrorCodeString() {
	switch (m_errCode) {
	case SOAP_SUCCESS:
		return _T("SOAP_SUCCESS");
	case ERR_SOAP_INVALID_PARAM_TO_SENDREQUEST:
		return _T("ERR_SOAP_INVALID_PARAM_TO_SENDREQUEST");
	case ERR_SOAP_INTERNET_SESSION:
		return _T("ERR_SOAP_INTERNET_SESSION");
	case ERR_SOAP_SETTING_URL:
		return _T("ERR_SOAP_SETTING_URL");
	case ERR_SOAP_INTERNET_CONNECT:
		return _T("ERR_SOAP_INTERNET_CONNECT");
	case ERR_SOAP_REQUEST_HEADER:
		return _T("ERR_SOAP_REQUEST_HEADER");
	case ERR_SOAP_SEND_REQUEST:
		return _T("ERR_SOAP_SEND_REQUEST");
	case ERR_SOAP_REQUEST_THREAD:
		return _T("ERR_SOAP_REQUEST_THREAD");
	case ERR_SOAP_REQUEST_TIMEOUT:
		return _T("ERR_SOAP_REQUEST_TIMEOUT");
	case ERR_SOAP_BAD_RESPONSE:
		return _T("ERR_SOAP_BAD_RESPONSE");
	case ERR_SOAP_PROGRAM_ERROR:
		return _T("ERR_SOAP_PROGRAM_ERROR");
	case ERR_SOAP_XML_PARSE:
		return _T("ERR_SOAP_XML_PARSE");
	case ERR_SOAP_XML_DOM:
		return _T("ERR_SOAP_XML_DOM");
	case ERR_SOAP_ARGUMENT_STRING:
		return _T("ERR_SOAP_ARGUMENT_STRING");
	case ERR_SOAP_LOCATION_STRING:
		return _T("ERR_SOAP_LOCATION_STRING");
	case ERR_SOAP_TASK_CANCELLED:
		return _T("ERR_SOAP_TASK_CANCELLED");
	case ERR_SYSTEM:
		return _T("ERR_SYSTEM");
	default:
		return _T("ERR_UNKNOWN");
	}
}

#endif               

