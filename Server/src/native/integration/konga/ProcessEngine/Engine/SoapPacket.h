/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                     
  
                                                                      

#ifdef DS_USE_SOAP

#ifndef SOAP_PACKET_H
#define SOAP_PACKET_H

#include <string>
#include <vector>

#ifdef WIN32
                      
                                                

#else
#endif

#include <konga/Xml/DOMNodePtr.h>
#include <konga/Xml/DOMDocumentPtr.h>

struct CSoapParam
{
	std::string name;
	std::string type;
	std::string value;
};

class CSoapPacket  
{
public:
	virtual bool LoadPacket();
	virtual bool SavePacket();
	virtual bool PutPacket(const std::string& packet);
	virtual bool PutParameter(const std::string& paramName, 
							  const std::string &paramType, 
							  const std::string& paramValue);
	virtual bool PutInterfaceURI(const std::string& interfaceURI);
	virtual bool PutMethodName(const std::string& methodName);
	virtual std::string GetPacket();
	virtual std::string GetParameterValue(const std::string& paramName);
	virtual std::vector<CSoapParam> GetAllParameters();
	virtual CSoapParam GetParameterByPosition(long pos);
	virtual std::string GetInterfaceURI();
	virtual std::string GetMethodName();
	CSoapPacket();
	virtual ~CSoapPacket();

private:
	                  
	bool CopyChildren(konga::xml::DOMNodePtr sourceNode, 
							  konga::xml::DOMNodePtr destNode);

	std::string FindDataType(const std::string& typeStr);

private:
	std::string ProcessFaultResponse(konga::xml::DOMNodePtr faultNode);

	                     
	std::string m_method_name;
	std::string m_interface_URI;
	std::string m_packet;
	konga::xml::DOMDocumentPtr m_pdd_params;

};

#endif                 

#endif               

