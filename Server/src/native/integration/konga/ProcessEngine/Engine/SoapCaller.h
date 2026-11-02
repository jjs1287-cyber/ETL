/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                     
  
                                                                      

#ifdef DS_US_SOAP

#ifndef _CSOAP_CALLER_H_
#define _CSOAP_CALLER_H_

#include "SoapPacket.h"

typedef std::map<std::string, int> returnDEMap;

class CSoapCaller  
{
public:
	CSoapCaller();
	virtual ~CSoapCaller();
	std::string InvokeSoapCall( IncomingMsg* pmsg, 
								const std::string& locationStr, 
								const std::string& argsStr);

                    
protected:
	void Init();
	void FreeResources();
	void ParseLocationString(const std::string& locationStr);
	void BuildArgs(	IncomingMsg* pmsg,
					const std::string& args, 
					returnDEMap& returned_DEs);

                          
protected:
	std::string		m_strEndPoint;
	std::string		m_strNamespace;
	std::string		m_strPrefix;
	std::string		m_strInterface;
	std::string		m_strMethodName;
	bool			m_useMPost;
	unsigned long	m_timeout;

	CSoapPacket*	m_request;
	CSoapPacket*	m_response;
	std::vector<CSoapParam> m_paramList;
	std::string		m_returnValueDE;
};

#endif                            

#endif              
