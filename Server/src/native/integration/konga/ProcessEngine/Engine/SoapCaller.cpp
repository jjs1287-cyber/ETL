/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                           
  
                                                                      

#ifdef DS_US_SOAP

#include "IncomingMsg.h"	                    
#include "engine_util.h"	                                          

#include "SoapCaller.h"
#include "SoapException.h"
#include "HttpPacket.h"
#include "HttpPost.h"

                                                                      
                           
                                                                      

CSoapCaller::CSoapCaller()
{
	m_request = 0;
	m_response = 0;
}

CSoapCaller::~CSoapCaller()
{
	FreeResources();
}

void CSoapCaller::Init()
{
	if (m_request && m_response)
		return;

	m_strEndPoint = "";
	m_strNamespace = "";
	m_strPrefix = "";
	m_strInterface = "";
	m_strMethodName = "";
	m_useMPost = false;
	m_timeout = 0;

	if (!m_request) {
		m_request = new CSoapPacket();
	}
	if (!m_response) {
		m_response = new CSoapPacket();
	}
	m_returnValueDE = _T("");
}


void CSoapCaller::FreeResources()
{
	if (m_request) {delete m_request; m_request=0; }
	if (m_response) {delete m_response; m_response=0; }
}

                                                           
                                                                     
                                                                     
                                                                     
                                 
void CSoapCaller::BuildArgs(IncomingMsg* pmsg,
							const std::string& args,
							returnDEMap& returned_DEs)
{
	Init();
	std::string argString(args);

	int retVal = std::string::npos;
	int numberOfArgs = 0;
	while ((retVal = argString.find(';',++retVal)) != std::string::npos)
		++numberOfArgs;

	bool isRetValReqd = false;
	if(!(argString.substr(0, 4)).compare("RET/"))                          
	{
		argString.erase(0,4);
		isRetValReqd = true;
	}			

	for(int i=0; i<numberOfArgs && !argString.empty(); ++i)                                        
	{
		CSoapParam tempParam;

		int k = argString.find(';');
		std::string argInfo = argString.substr(0, k);

		                                                                      
		                                                
		                                                        
		                                                                     
		                                                  
		                                    
		                          
		                                       
		                                           
		                                                    

		                                                                
		                                        

		int pos = -1, numTokens = 1;
		while ((pos = argInfo.find(',',++pos)) != std::string::npos)
			++numTokens;

		if (numTokens < 3) {         
			std::string errMsg = _T("Syntax error in argument string. Argument string was:\n");
			errMsg += args;
			throw CSoapException(CSoapException::ERR_SOAP_ARGUMENT_STRING, errMsg);
		}

		if (numTokens==3) {		                                               
			if (!isRetValReqd) {                                   
				std::string errMsg = _T("Invalid syntax for return value argument. Argument string was:\n");
				errMsg += args;
				throw CSoapException(CSoapException::ERR_SOAP_ARGUMENT_STRING, errMsg);
			}
		}

		int strtIndex = 0, endIndex = 0;
		
		                                                                                       
		endIndex = argInfo.find(',', strtIndex);
		std::string argField = argInfo.substr(strtIndex, endIndex - strtIndex);
		if (i==0 && isRetValReqd) {
			if (stricmp(argField.c_str(), "str")!=0 && stricmp(argField.c_str(), "var")!=0)
			{	                                                              
				std::string errMsg = _T("Return value data element can only be of type string.\n");
				errMsg += _T("Argument string was:\n") + args;
				throw CSoapException(CSoapException::ERR_SOAP_ARGUMENT_STRING, errMsg);
			}
		}
		else {
			tempParam.name = argField;
		}

		                                                                                  
		                     
		strtIndex = endIndex;
		endIndex = argInfo.find(',', ++strtIndex);
		argField = argInfo.substr(strtIndex, endIndex - strtIndex);
		if (i==0 && isRetValReqd && numTokens == 3)                             
		{
			m_returnValueDE = argField;
		}
		else {                                          
			                                     
			const char* tempStr = argField.c_str();
			if (stricmp(tempStr, "bol") !=0 &&
				stricmp(tempStr, "dbl") !=0 &&
				stricmp(tempStr, "int") !=0 &&
				stricmp(tempStr, "flt") !=0 &&
				stricmp(tempStr, "lng") !=0 &&
				stricmp(tempStr, "srt") !=0 &&
				stricmp(tempStr, "str") !=0 &&
				stricmp(tempStr, "var") !=0)
			{                                 
				std::string errMsg = _T("Invalid data type found in argument string.\n");
				errMsg += _T("Data type found was: ") + argField;
				errMsg += _T("\nArgument string was:\n") + args;
				throw CSoapException(CSoapException::ERR_SOAP_ARGUMENT_STRING, errMsg);
			}				

			tempParam.type = argField;
		}

		                                                    
		if (numTokens == 3 && i == 0) {
			                                                  
			argString.erase(0, k+1);
			continue;
		}

		                                                                                  
		strtIndex = endIndex;
		endIndex = argInfo.find(',', ++strtIndex);
		std::string deName = argInfo.substr(strtIndex, endIndex - strtIndex);
		
		                                                                          
		strtIndex = endIndex;
		endIndex = argInfo.find(',', ++strtIndex);
		argField = argInfo.substr(strtIndex, endIndex - strtIndex);

		const char* sInOut = argField.c_str();
		bool isOutParam = false;
		if (stricmp(sInOut, "O")==0 || stricmp(sInOut, "B")==0)
		{
			if (stricmp(sInOut, "O")==0)
				isOutParam = true;
			if (!deName.empty()) {
				returned_DEs[deName] = isRetValReqd?i-1:i;
			}
		}

		                                                                             
		if (numTokens == 4) {
			argField = _T("");
		}
		else {
			strtIndex = endIndex;
			endIndex = argInfo.find(',', ++strtIndex);
			argField = argInfo.substr(strtIndex, endIndex - strtIndex);
		}
		
		if (!argField.empty()) {
			tempParam.value = argField;
			::GetDataElementValue(pmsg, argField, tempParam.value);
		}

		if (!tempParam.name.empty()) {                                               
			m_paramList.push_back(tempParam);
		}

		if (!isOutParam) {
			m_request->PutParameter(tempParam.name, tempParam.type, tempParam.value);
		}

		                                                  
		argString.erase(0, k+1);
	}
}


void CSoapCaller::ParseLocationString(const std::string& locationStr)
{
	Init();
	std::string locStr(locationStr);

	int retVal = std::string::npos;
	int numTokens = 1;
	while ((retVal = locStr.find("::",++retVal)) != std::string::npos)
		++numTokens;

	if (numTokens < 5) {         
		std::string errMsg = _T("At least five fields must be specified in location string in the following format.\n");
		errMsg += _T("ENDPOINT::INTERFACE::METHODNAME::USEMPOST::TIMEOUT");
		errMsg += _T("\nLocation string was:\n");
		errMsg += locationStr;
		throw CSoapException(CSoapException::ERR_SOAP_LOCATION_STRING, errMsg);		
	}

	for(int i=0; i<numTokens; i++)
	{
		int k = locStr.find("::");
		std::string field = locStr.substr(0, k);

		if (field.empty()) {
			std::string errMsg = _T("Location field at index ");
			char temp[3];
			errMsg += itoa(i, temp, 10);
			errMsg += _T(" is empty.\nLocation string was:\n");
			errMsg += locationStr;
			throw CSoapException(CSoapException::ERR_SOAP_LOCATION_STRING, errMsg);
		}
		if (i==0)
			m_strEndPoint = field;
		else if (i==1)
			m_strInterface = field;
		else if (i==2)
			m_strMethodName = field;

		if (i < 3) {
			                                                  
			locStr.erase(0, k+2);                 
			continue;
		}
		
		int indexOfEquals = field.find('=');
		if (indexOfEquals == std::string::npos) {         

		}
		std::string fieldName = field.substr(0, indexOfEquals);
		std::string fieldValue = field.substr(indexOfEquals+1);

		if (stricmp(fieldName.c_str(), "M-POST")==0) {
			if (stricmp(fieldValue.c_str(), "TRUE")==0)
				m_useMPost = true;
			else
				m_useMPost = false;
		}
		else if (stricmp(fieldName.c_str(), "TIMEOUT")==0) {
			m_timeout = atoi(fieldValue.c_str());
		}
		else if (stricmp(fieldName.c_str(), "NAMESPACE")==0) {
			m_strNamespace = fieldValue;
		}
		else if (stricmp(fieldName.c_str(), "PREFIX")==0) {
			m_strPrefix = fieldValue;
		}

		                                                  
		locStr.erase(0, k+2);                 
		continue;
	}
}


std::string CSoapCaller::InvokeSoapCall(IncomingMsg* pmsg, 
										const std::string& locationStr, 
										const std::string& argsStr)
{
	returnDEMap retDEMap;
	int numArgs = 0;

	try {
		BuildArgs(pmsg, argsStr, retDEMap);
		ParseLocationString(locationStr);
	}
	catch (CSoapException se) {
		FreeResources();
		throw se;
	}

	m_request->PutMethodName(m_strMethodName);
	m_request->PutInterfaceURI(m_strInterface);
	m_request->SavePacket();

	CHttpPacket httpPacket, responsePacket;
	std::string header;

	if (m_useMPost) {
		header = _T("Man: "); header += m_strNamespace;
		httpPacket.AddHeader(header);

		header = _T("ns: ");header += m_strPrefix;
		httpPacket.AddHeader(header);
	}

	httpPacket.AddHeader("Content-Type: text/xml");
	httpPacket.AddHeader("charset: UTF-8");
	
	std::string soapAction = m_strInterface; soapAction += _T("#");
	soapAction += m_strMethodName;
	std::string headerName;
	if (m_useMPost) {
		headerName = m_strPrefix + _T("-SOAPAction");
	}
	else {
		headerName = _T("SOAPAction");
	}
	headerName += _T(": ") + soapAction;
	httpPacket.AddHeader(headerName);
	httpPacket.PutMessage(m_request->GetPacket());

	CHttpPost inetSession;
	std::string errStr;
	try {
		inetSession.SendRequest(m_strEndPoint, &httpPacket, 
					&responsePacket, m_useMPost, m_timeout, (void*)pmsg);
	}
	catch (CSoapException se) {
		if (se.ErrCode() == CSoapException::ERR_SOAP_REQUEST_TIMEOUT ||
			se.ErrCode() == CSoapException::ERR_SOAP_TASK_CANCELLED)
		{
			throw se;
		}
		errStr = se.GetErrorMessage();
	}

	try {
		m_response->PutPacket(responsePacket.GetMessage());
		m_response->LoadPacket();
	}
	catch (CSoapException se) {
		errStr += _T("\n") + se.GetErrorMessage();
	}

	if (!errStr.empty()) {
		throw CSoapException(CSoapException::ERR_UNKNOWN, errStr);
	}
	
	std::string returnValue, stValue;
	for(returnDEMap::const_iterator it= retDEMap.begin(); it!=retDEMap.end(); ++it)
	{
		std::string paramName = m_paramList[it->second].name;
		returnValue += it->first;
		returnValue += _T("=");
		stValue = m_response->GetParameterValue(paramName);
		returnValue += stValue;
		returnValue += _T(";");
		pmsg->SetDataElementValue(it->first.c_str(), stValue.c_str());
	}
	if (m_returnValueDE.length() > 0) {
		returnValue += m_returnValueDE;
		returnValue += _T("=");
		stValue = responsePacket.GetMessage();
		returnValue += stValue;
		returnValue += _T(";");
		pmsg->SetDataElementValue(m_returnValueDE.c_str(), stValue.c_str());
	}

	FreeResources();
	return returnValue;
}

#endif              

