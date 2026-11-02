/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                       
  
                                                                      
#ifdef DS_USE_SP

#ifndef CR_SPEXCEPTION_H
#define CR_SPEXCEPTION_H

#include "SpArgs.h"

class CSpException  
{
public:

	typedef enum errCode_
	{
		ERR_BAD_SCRIPT = 1,
		ERR_NO_SQL_STMNT = 2,
		ERR_NO_SP_CALL = 3,
		ERR_DB_CONNECTION = 4,
		ERR_BAD_DATE_FORMAT = 5,
		ERR_SYSTEM
	} errCode;

	CSpException(errCode err, std::string cause);
	virtual ~CSpException();

	BOOL GetErrorMessage(LPTSTR lpszError, UINT nMaxError, PUINT pnHelpContext = 0);

	std::string Cause() const { return m_cause; }
	errCode ErrCode() const { return m_errCode; }

private:
	std::string	m_cause;
	errCode m_errCode;
};

#endif                    

#endif             

