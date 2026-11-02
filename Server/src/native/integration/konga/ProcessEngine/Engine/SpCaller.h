/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                 
  
                                                                      

#ifdef DS_USE_SP

#ifndef CR_SPCALLER_H
#define CR_SPCALLER_H

#include "SpArgs.h"

class IncomingMsg;

class CSpCaller  
{
public:
	CSpCaller(std::string& scriptStr);
	CSpCaller(const char* scriptStr);

	virtual ~CSpCaller();

	std::string GetSPResults(IncomingMsg* pmsg);
	void Dumpdata();

	                                                                            
	                                                                          
	                                                   
	void InvokeStoredProcedure();

	                      
	void SetTimeout(long lTimeOut) { m_lTimeOut = lTimeOut; }

protected:
	void FreeResources();
	                                               

	                 
	                                            
	                                      
	void InvokeSP();
	void ParseScript(std::string& script);

private:
	               
	std::string	m_script;
	spArgs* m_argList;
	std::string	m_connectStr;
	std::string m_dbConnectStr;                                           
												                                                      
	std::string	m_spStr;
	int		m_numParams;
	int		m_numFields;
	long	m_lTimeOut;
};

#endif                 

#endif             

