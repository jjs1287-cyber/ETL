/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                           
  
                                                                      

#ifdef DS_USE_SOAP

#ifndef SOAPEXCEPTION_H_
#define SOAPEXCEPTION_H_

#include <string>

class CSoapException  
{
public:
	typedef enum errCode_
	{
		ERR_UNKNOWN = -1,
		SOAP_SUCCESS = 0,
		ERR_SOAP_INVALID_PARAM_TO_SENDREQUEST = 1,
		ERR_SOAP_INTERNET_SESSION = 2,
		ERR_SOAP_SETTING_URL = 3,
		ERR_SOAP_INTERNET_CONNECT = 4,
		ERR_SOAP_REQUEST_HEADER = 5,
		ERR_SOAP_SEND_REQUEST = 6,
		ERR_SOAP_REQUEST_THREAD = 7,
		ERR_SOAP_REQUEST_TIMEOUT = 8,
		ERR_SOAP_BAD_RESPONSE = 9,
		ERR_SOAP_PROGRAM_ERROR = 10,
		ERR_SOAP_XML_PARSE = 11,
		ERR_SOAP_XML_DOM = 12,
		ERR_SOAP_ARGUMENT_STRING = 13,
		ERR_SOAP_LOCATION_STRING = 14,
		ERR_SOAP_TASK_CANCELLED = 15,
		ERR_SYSTEM
	} errCode;

	CSoapException(errCode err, std::string& cause);
	virtual ~CSoapException();

	std::string GetErrorMessage();

	std::string Cause() const { return m_cause; }
	void SetCause(const std::string& cause) { m_cause = cause; }

	errCode ErrCode() const { return m_errCode; }
	void SetErrCode(errCode code) { m_errCode = code; }

private:
	std::string	m_cause;
	errCode m_errCode;

	std::string GetErrorCodeString();
};

#endif                    

#endif               

