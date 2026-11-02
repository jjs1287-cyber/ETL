/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                           
  
                                                                      

#ifdef DS_USE_SOAP

#include "SoapPacket.h"
#include "SoapException.h"
#include "ErrorResolver.h"
#include "XmlUtils.h"

#include <sys/types.h>
#include <sys/stat.h>

const std::string c_str_soapCall =	"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "
										"xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\" "
										"xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\" "
										"SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
										"<SOAP-ENV:Body>"
										"</SOAP-ENV:Body>"
										"</SOAP-ENV:Envelope>";

const std::string c_str_queryMethod = "./SOAP-ENV:Envelope/SOAP-ENV:Body";
const std::string c_str_fault = "./SOAP-ENV:Envelope/SOAP-ENV:Body/SOAP-ENV:Fault";
const std::string c_str_faultcode = "./faultcode";
const std::string c_str_faultstring = "./faultstring";
const std::string c_str_faultactor = "./faultactor";
const std::string c_str_faultdetail = "./detail";

                                                                      
                           
                                                                      

CSoapPacket::CSoapPacket()
{
	VARIANT_BOOL result;

	                         
	HRESULT _hr = m_pdd_params.CreateInstance(__uuidof(MSXML::DOMDocument));
	if (FAILED(_hr)) {
		std::string errMsg = "Failed to create the SOAP request/response object\n"
			"Extended error information is:\n" + std::string(ErrorResolver().Resolve(_hr));
		throw CSoapException(CSoapException::ERR_SOAP_PROGRAM_ERROR, errMsg);
	}

	                       
	try {
		result = m_pdd_params->loadXML("<params/>");
		if (!(bool)m_pdd_params) _com_raise_error(E_POINTER);
		if (result == VARIANT_FALSE) _com_raise_error(S_FALSE);
	} catch(_com_error& ce) {
		std::string errMsg = CRXmlUtils::GetLastXMLError(m_pdd_params);
		errMsg += "\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_PARSE, errMsg);
	}
}

CSoapPacket::~CSoapPacket()
{

}


std::string CSoapPacket::GetMethodName()
{
	return m_method_name;
}

std::string CSoapPacket::GetInterfaceURI()
{
	return m_interface_URI;
}

std::vector<CSoapParam> CSoapPacket::GetAllParameters()
{
	konga::DOMNodeListPtr paramNodeList;
	try {
		paramNodeList = m_pdd_params->childNodes;
		if (!(bool)paramNodeList) _com_raise_error(E_POINTER);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to retrieve parameters from SOAP request/response\n" 
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	long nodeListLength=0;
	try {
		nodeListLength = paramNodeList->length;
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to retrieve parameters from SOAP request/response\n"
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	std::vector<CSoapParam> paramList;
	for (long i = 0; i < nodeListLength; i++) {
		CSoapParam param = GetParameterByPosition(i);
		if (param.name.empty() && param.value.empty() && param.type == _T("undefined"))
			continue;
		paramList.push_back(param);
	}
	return paramList;
}

CSoapParam CSoapPacket::GetParameterByPosition(long pos)
{
	CSoapParam retParam;
	konga::DOMNodePtr paramNode;
	std::string strQuery;
	
	char tempPos[3];
	strQuery = "./params/*[";
	strQuery += itoa(pos, tempPos, 10);
	strQuery += "]";
	try {
 		paramNode = m_pdd_params->selectSingleNode(strQuery.c_str());
		if (!(bool)paramNode) _com_raise_error(E_POINTER);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to retrieve parameters from SOAP request/response\n"
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	_bstr_t strResult;
	try {
		strResult = paramNode->nodeName;
		if (strResult.length()< 1) _com_raise_error(S_FALSE);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to get node name\n"
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	retParam.name = strResult.length()>0?(LPCTSTR)strResult:"";

	try {
		strResult = paramNode->text;
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to get the value of parameter" +
			retParam.name + ".\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}
	retParam.value = strResult.length()>0?(LPCTSTR)strResult:"";

	konga::DOMNodePtr paramType;
	strQuery = "@xsi:type";
	try {
		paramType = paramNode->selectSingleNode(strQuery.c_str());
		if (!(bool)paramType) _com_raise_error(E_POINTER);
	} catch(_com_error) {
		                                                                                                            
		                                                                                          
		                                                                 
		
		                                                                               
		retParam.type = FindDataType("xsd:string");
		return retParam;
	}

	try {
		strResult = paramType->text;
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to determine data type for parameter " +
			retParam.name + ".\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}
	retParam.type = FindDataType(strResult.length()>0?(LPCTSTR)strResult:"");

	return retParam;	
}

std::string CSoapPacket::GetParameterValue(const std::string &paramName)
{
	if (paramName.empty())
		return "";

	konga::DOMNodePtr paramNode;
	std::string strQuery= "./params/" + paramName;

	try {
 		paramNode = m_pdd_params->selectSingleNode(strQuery.c_str());
		if (!(bool)paramNode) _com_raise_error(E_POINTER);
	} catch(_com_error) {
		return "";
	}

	_bstr_t strResult;
	try {
		strResult = paramNode->text;
	} catch(_com_error) {
		return "";
	}
	return strResult.length()>0?(LPCTSTR)strResult:"";
}

std::string CSoapPacket::GetPacket()
{
	return m_packet;
}

bool CSoapPacket::PutMethodName(const std::string &methodName)
{
	m_method_name = methodName.c_str();
	return true;
}

bool CSoapPacket::PutInterfaceURI(const std::string &interfaceURI)
{
	m_interface_URI = interfaceURI.c_str();
	return true;
}

bool CSoapPacket::PutParameter(const std::string &paramName, 
							   const std::string &paramType, 
							   const std::string &paramValue)
{
	konga::DOMNodePtr paramNode;
	std::string strQuery = "./params/" + paramName;

	                                       
	try {
		paramNode = m_pdd_params->selectSingleNode(strQuery.c_str());
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to select paramter node from SOAP request/response.\n"
			"Parameter is : " + paramName + "\nExtended error message is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	if (!(bool)paramNode) {                                           
		                 
		try {
			_variant_t nodeType("element");
			paramNode = m_pdd_params->createNode(nodeType,paramName.c_str(), "");
			if (!(bool) paramNode) _com_raise_error(E_POINTER);
		} catch(_com_error& ce) {
			std::string errMsg = "Unable to create paramter node in SOAP request/response.\n"
				"Parameter is : " + paramName + "\nExtended error message is:\n" + std::string(ce.ErrorMessage());
			throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
		}

		konga::DOMElementPtr docRoot;
		try {
			docRoot = m_pdd_params->GetdocumentElement();
			if (!(bool) docRoot) _com_raise_error(E_POINTER);
		} catch(_com_error& ce) {
			std::string errMsg = "Unable to get document root in SOAP request/response.\n"
				"Parameter is : " + paramName + "\nExtended error message is:\n" + std::string(ce.ErrorMessage());
			throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
		}

		                          
		try {
			docRoot->appendChild(paramNode);
		} catch(_com_error& ce) {
			std::string errMsg = "Unable to create paramter node in SOAP request/response.\n"
				"Parameter is : " + paramName + "\nExtended error message is:\n" + std::string(ce.ErrorMessage());
			throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
		}
	}

	                                                           
	konga::DOMNodePtr paramAttr;
	try {
		_variant_t nodeType("attribute");
		paramAttr = m_pdd_params->createNode(nodeType, "xsi:type",
									"http://www.w3.org/1999/XMLSchema-instance");
		if (!(bool)paramAttr) _com_raise_error(E_POINTER);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to attach parameter type to paramter node in SOAP request/response.\n"
			"Parameter is : " + paramName + "\nExtended error message is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	const char* temp = paramType.c_str();
	try {
		if( stricmp(temp, "boolean")==0 || stricmp(temp, "bol")==0)
			paramAttr->text = "xsd:boolean";
		else if ( stricmp(temp, "double")==0 || stricmp(temp, "dbl")==0)
			paramAttr->text = "xsd:double";
		else if ( stricmp(temp, "float")==0 || stricmp(temp, "flt")==0)
			paramAttr->text = "xsd:float";
		else if ( stricmp(temp, "integer")==0 || stricmp(temp, "int")==0)
			paramAttr->text = "xsd:int";
		else if (stricmp(temp, "long_integer")==0 || 
				 stricmp(temp, "long")==0 ||
				 stricmp(temp, "lng")==0)
			paramAttr->text = "xsd:long";
		else if ( stricmp(temp, "short")==0 || stricmp(temp, "srt")==0 || stricmp(temp, "short_integer")==0)
			paramAttr->text = "xsd:short";
		else
			paramAttr->text = "xsd:string";
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to attach parameter type to paramter node in SOAP request/response.\n"
			"Parameter is : " + paramName + "\nExtended error message is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	                                         
	try {
		konga::DOMElementPtr tempNode = paramNode;
		if (!(bool)tempNode) _com_raise_error(E_POINTER);
		konga::DOMAttributePtr tempAttribute = paramAttr;
		if (!(bool)tempAttribute) _com_raise_error(E_POINTER);
		tempNode->setAttributeNode(tempAttribute);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to create paramter node in SOAP request/response.\n"
			"Parameter is : " + paramName + "\nExtended error message is:\n" + 
			std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	                           
	try {
		paramNode->text = paramValue.c_str();
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to set paramter node value in SOAP request/response.\n"
			"Parameter is : " + paramName + "\nExtended error message is:\n" + 
			std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	return true;
}

bool CSoapPacket::PutPacket(const std::string &packet)
{
	m_packet = packet.c_str();
	return true;
}

bool CSoapPacket::SavePacket()
{
	konga::DOMDocumentPtr pddSoapPacket;                            
	HRESULT _hr = pddSoapPacket.CreateInstance(__uuidof(MSXML::DOMDocument));
	if (FAILED(_hr) || !(bool)pddSoapPacket) {
		std::string errMsg = "Failed to create the SOAP request/response object\n"
			"Extended error information is:\n" + std::string(ErrorResolver().Resolve(_hr));
		throw CSoapException(CSoapException::ERR_SOAP_PROGRAM_ERROR, errMsg);
	}

	try {
		VARIANT_BOOL result;
		result = pddSoapPacket->loadXML(c_str_soapCall.c_str());
		if (result == VARIANT_FALSE) _com_raise_error(S_FALSE);
	} catch(_com_error& ce) {
		std::string errMsg = CRXmlUtils::GetLastXMLError(pddSoapPacket);
		errMsg += "\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_PARSE, errMsg);
	}

	konga::DOMNodePtr pdnBody;
	try {
		pdnBody = pddSoapPacket->selectSingleNode(c_str_queryMethod.c_str());
		if (!(bool)pdnBody) _com_raise_error(E_POINTER);
	} catch(_com_error& ce) {
		std::string errMsg = "Invalid SOAP request/response. Failed to find SOAP BODY node"
			"\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	std::string nodeName = "m:" + m_method_name;
	konga::DOMElementPtr pdeTemp;                                
	try {
		pdeTemp = pddSoapPacket->createElement(nodeName.c_str());
		if (!(bool)pdeTemp) _com_raise_error(S_FALSE);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to create SOAP method node in SOAP request"
			"\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	try {
		_variant_t attrValue(m_interface_URI.c_str());
		pdeTemp->setAttribute("xmlns:m", attrValue);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to set namespace in SOAP request"
			"\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	try {
		pdnBody->appendChild(pdeTemp);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to append SOAP method node to SOAP request\n"
			"\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	konga::DOMElementPtr pdeParams;
	try {
		pdeParams = m_pdd_params->documentElement;
		if (!(bool)pdeParams) _com_raise_error(E_POINTER);
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to get the parameter node from SOAP request\n"
			"\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	if (!CopyChildren(pdeParams, pdeTemp)) {
		std::string errMsg = "Unable to copy parameters to SOAP request\n";
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	m_packet.erase();
	try {
		_bstr_t bsXml = pddSoapPacket->xml;
		m_packet = bsXml.length()>0?(LPCTSTR)bsXml:"";
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to get XML text for SOAP request\n"
			"\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	return true;
}

bool CSoapPacket::LoadPacket()
{
	VARIANT_BOOL result;
	if (m_packet.length() <= 0) {
		try {
			result = m_pdd_params->loadXML("<params/>");
		} catch(_com_error& ce) {
			std::string errMsg = CRXmlUtils::GetLastXMLError(m_pdd_params);
			errMsg += "\nExtended error information is:\n" + std::string(ce.ErrorMessage());
			throw CSoapException(CSoapException::ERR_SOAP_XML_PARSE, errMsg);
		}
		return false;
	}
	
	                                                                          
	char* tempfn=0;
	tempfn = _tempnam("C:\\temp", "CrSoap");
	std::string tempFileName = tempfn + std::string(".xml");
	while(true) {
		free(tempfn);
		struct _stat fileInfo;
		if (_stat(tempFileName.c_str(), &fileInfo) == -1)
			break;
		else {
			tempfn = _tempnam("C:\\temp", "CrSoap");
			tempFileName = tempfn + std::string(".xml");
		}
	}
	FILE* xmlFile = fopen(tempFileName.c_str(), "w");
	if (!xmlFile) {
		std::string errMsg = "Unable to open the temporary XML file to load the SOAP message.\n"
			"Xml file name was: " + tempFileName + "\n";
		throw CSoapException(CSoapException::ERR_SOAP_PROGRAM_ERROR, errMsg);
	}
	unsigned int numBytesWritten = fwrite(m_packet.c_str(), sizeof(char), m_packet.length(), xmlFile);
	if (numBytesWritten < m_packet.length()) {
		fclose(xmlFile);
		remove(tempFileName.c_str());
		std::string errMsg = "Unable to write to the temporary Xml response file.\n"
			"Xml file name was: " + tempFileName + "\n";
		throw CSoapException(CSoapException::ERR_SOAP_PROGRAM_ERROR, errMsg);
	}
	fclose(xmlFile);

	std::string errorMessage;
	konga::DOMDocumentPtr pddSoapPacket = CRXmlUtils::LoadXml(tempFileName, errorMessage);
	if (!(bool)pddSoapPacket) {
		remove(tempFileName.c_str());
		throw CSoapException(CSoapException::ERR_SOAP_XML_PARSE, errorMessage);
	}
	remove(tempFileName.c_str());

	                                                                         
	konga::DOMElementPtr docRoot;
	try {
		docRoot = pddSoapPacket->documentElement;
		if (!(bool)docRoot) _com_raise_error(E_POINTER);
	} catch(_com_error& ce) {
		std::string errMsg = "Invalid SOAP request/response.\nUnable to select SOAP Envelope node\n"
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	                                                                 
	konga::DOMNodePtr bodyNode;
	try {
		bodyNode = docRoot->firstChild;
		while (true) {
			if (!(bool)bodyNode) _com_raise_error(E_POINTER);
			_bstr_t docNodeName = bodyNode->nodeName;
			if (bodyNode->nodeType != MSXML::NODE_ELEMENT) {
				bodyNode = bodyNode->nextSibling;
				continue;
			} else 
				break;
		}
	} catch(_com_error& ce) {
		std::string errMsg = "Invalid SOAP request/response.\nUnable to select the SOAP Body node\n"
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	                           
                              
      
                                                            
                        
                                                         
                       
                                                     
   
                          
                                                                                                                     
                                                                       
                                                                 
  
                      
                                                      
                                                                      
    

	konga::DOMNodePtr pdnMethod;
	try {
		pdnMethod = bodyNode->firstChild;
		while (true) {
			if (!(bool)pdnMethod) _com_raise_error(E_POINTER);
			_bstr_t docNodeName = pdnMethod->nodeName;
			if (pdnMethod->nodeType != MSXML::NODE_ELEMENT) {
				pdnMethod = pdnMethod->nextSibling;
				continue;
			} else 
				break;
		}
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to get method node from SOAP response\n" 
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	                     

	try {
		_bstr_t bsMethodName = pdnMethod->nodeName;
		if (bsMethodName.length()<1) _com_raise_error(S_FALSE);
		if (strstr((LPCTSTR)bsMethodName, "FAULT") != 0 || 
			strstr((LPCTSTR)bsMethodName, "Fault") != 0 ||
			strstr((LPCTSTR)bsMethodName, "fault") != 0)
		{
			std::string errStr = ProcessFaultResponse(pdnMethod);
			throw CSoapException(CSoapException::ERR_SOAP_BAD_RESPONSE, errStr);
		}
		m_method_name.erase();
		m_method_name = (LPCTSTR)bsMethodName;
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to get method name from SOAP response\n" 
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	                       
	m_interface_URI.erase();
	try {
		_bstr_t bsInterfaceURI = pdnMethod->namespaceURI;
		m_interface_URI = bsInterfaceURI.length()>0?(LPCTSTR)bsInterfaceURI:"";
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to get method namespace URI from SOAP response\n"
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	                                                             
	try {
		result = m_pdd_params->loadXML("<params/>");
		if (result == VARIANT_FALSE) _com_raise_error(S_FALSE);
	} catch(_com_error& ce) {
		std::string errMsg = CRXmlUtils::GetLastXMLError(m_pdd_params);
		errMsg = "\nExtended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_PARSE, errMsg);
	}

	konga::DOMNodePtr pdnTemp;
	try {
		pdnTemp = m_pdd_params->firstChild;
		while (true) {
			if (!(bool)pdnTemp) _com_raise_error(S_FALSE);
			_bstr_t docNodeName = pdnTemp->nodeName;
			if (pdnTemp->nodeType != MSXML::NODE_ELEMENT) {
				pdnTemp = pdnTemp->nextSibling;
				continue;
			} else 
				break;
		}
	} catch(_com_error& ce) {
		std::string errMsg = "Unable to get parameter node from SOAP response\n"
			"Extended error information is:\n" + std::string(ce.ErrorMessage());
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	if (!CopyChildren(pdnMethod, pdnTemp)) {
		std::string errMsg = "Unable to copy SOAP response parameters to SOAP response object\n";
		throw CSoapException(CSoapException::ERR_SOAP_XML_DOM, errMsg);
	}

	return true;
}

bool CSoapPacket::CopyChildren(konga::DOMNodePtr sourceNode, 
							  konga::DOMNodePtr destNode)
{
	if (!(bool)sourceNode || !(bool)destNode) 
		return false;

	konga::DOMNodeListPtr pdnlNodes;                                   
	konga::DOMNodePtr pdnTemp;
	try {
		pdnlNodes = sourceNode->childNodes;
		if (!(bool)pdnlNodes) _com_raise_error(S_FALSE);
		pdnlNodes->reset();
		pdnTemp = pdnlNodes->nextNode();
	} catch(_com_error) {
		return false;
	}

	while ((bool)pdnTemp)
	{
		try {
			destNode->appendChild(pdnTemp);
			pdnTemp = pdnlNodes->nextNode();
		} catch(_com_error) {
			return false;
		}
	}
	
	return true;
}


std::string CSoapPacket::FindDataType(const std::string& typeStr)
{
	if(typeStr.empty()) {
		return "undefined";
	}

	std::string strToSearch = typeStr;
	int index = typeStr.find(":");
	if (index != std::string::npos) {
		strToSearch = typeStr.substr(index+1);
	}

	std::string retVal = strToSearch;
	const char* temp = strToSearch.c_str();
	if (stricmp(temp, "boolean")==0 || stricmp(temp, "bol")==0)
		retVal = _T("boolean");
	else if (stricmp(temp, "double")==0 || stricmp(temp, "dbl")==0)
		retVal = _T("double");
	else if (stricmp(temp, "float")==0 || stricmp(temp, "flt")==0)
		retVal = _T("float");
	else if (stricmp(temp, "int")==0 || stricmp(temp, "integer")==0)
		retVal = _T("integer");
	else if (stricmp(temp, "long")==0 ||
			 stricmp(temp, "lng")==0 ||
			 stricmp(temp, "long_integer")==0)
		retVal = _T("long_integer");
	else if (stricmp(temp, "short")==0 || stricmp(temp, "srt")==0 || stricmp(temp, "short_integer")==0)
		retVal = _T("short");
	else if (stricmp(temp, "string")==0 || stricmp(temp, "str")==0)
		retVal = _T("string");
	else if (stricmp(temp, "variant")==0 || stricmp(temp, "var") == 0)
		retVal = _T("variant");

	return retVal;
}

std::string CSoapPacket::ProcessFaultResponse(konga::DOMNodePtr faultNode)
{
	std::string errMsg = _T("SOAP server returned with an error\n");
	if (!(bool)faultNode) {
		errMsg += "Extended error information is not available.\n";
		return errMsg;
	}

	konga::DOMNodePtr pdnNode;
	try {
		pdnNode = faultNode->selectSingleNode(c_str_faultcode.c_str());
		if (!(bool)pdnNode) _com_raise_error(E_POINTER);
	} catch(_com_error) {
		errMsg += "Extended error information is not available.\n";
		return errMsg;
	}

	_bstr_t nodeText;
	try {
		nodeText = pdnNode->text;
	} catch(_com_error) {
	}

	if (nodeText.length() > 0) 
		errMsg += "Error code: " + std::string((LPCTSTR)nodeText) + "\n";

	try {
		pdnNode = faultNode->selectSingleNode(c_str_faultstring.c_str());
		if (!(bool)pdnNode) _com_raise_error(E_POINTER);
	} catch(_com_error) {
		return errMsg;
	}
	
	try {
		nodeText = pdnNode->text;
	} catch(_com_error) {
	}
	
	if (nodeText.length() > 0)
		errMsg += "Error Message: " + std::string((LPCTSTR)nodeText) + "\n";

	try {
		pdnNode = faultNode->selectSingleNode(c_str_faultactor.c_str());
		if (!(bool)pdnNode) _com_raise_error(E_POINTER);
	} catch(_com_error) {
		return errMsg;
	}
	
	try {
		nodeText = pdnNode->text;
	} catch(_com_error) {
	}
	
	if (nodeText.length() > 0)
		errMsg += "Error Actor: " + std::string((LPCTSTR)nodeText) + "\n";

	try {
		pdnNode = faultNode->selectSingleNode(c_str_faultdetail.c_str());
		if (!(bool)pdnNode) _com_raise_error(E_POINTER);
	} catch(_com_error) {
		return errMsg;
	}
	
	try {
		nodeText = pdnNode->text;
	} catch(_com_error) {
	}
	
	if (nodeText.length() > 0)
		errMsg += "Error Details: " + std::string((LPCTSTR)nodeText) + "\n";

	return errMsg;
}

#endif               

