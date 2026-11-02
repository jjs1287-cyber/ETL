/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                             
  
                                                                      
#ifdef DS_USE_SP

#include "SpException.h"

using namespace std;

                                                                      
                           
                                                                      

CSpException::CSpException(errCode err, std::string cause):
m_errCode(err), m_cause(cause)
{

}

CSpException::~CSpException()
{

}


BOOL CSpException::GetErrorMessage(LPTSTR lpszError, UINT nMaxError,
PUINT pnHelpContext)
{
	if(!lpszError || nMaxError <= 0)
		return false;

	string tempStr = "Error Code: ";

	int errCode = static_cast<int>(m_errCode);
	char strCode[3];
	tempStr += itoa(errCode, strCode, 10);
	tempStr += ".\n";

	tempStr += m_cause;

	strncpy(lpszError, tempStr.c_str(), nMaxError);
	return true;
}

#endif             

