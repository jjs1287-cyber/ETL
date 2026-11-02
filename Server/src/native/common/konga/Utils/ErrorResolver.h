/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                                          
  
                                                                      
#ifndef ERROR_RESOLVER_H
#define ERROR_RESOLVER_H

#ifdef WIN32
#include <windows.h>
#endif

#include <string>

class ErrorResolver  
{
public:
	ErrorResolver(unsigned long hr=0) : m_hr(hr), m_isComErr(false), m_isWinErr(false),
		m_isOledbErr(false), m_isInetErr(false) {}
	virtual ~ErrorResolver() {}

	std::string Resolve(unsigned long err, bool bForceDBError=false);

	#ifdef WIN32
	bool IsComError() const { return m_isComErr; }
	bool IsWinError() const { return m_isWinErr; }
	bool IsOledbError() const { return m_isOledbErr; }
	bool IsInetErr() const { return m_isInetErr; }

	void GetLastErrorText(std::string& theError, unsigned long lErr=0, const std::string& stModule="") const;
	std::string GetLastErrorText(unsigned long lErr      , const std::string& stModule       ) const;
	#endif
	
	std::string operator()() const { return m_Error; }

private:
	unsigned long m_hr;
	bool m_isComErr, m_isWinErr, m_isOledbErr, m_isInetErr;
	std::string m_Error;
};

#ifdef WIN32
	HRESULT myHandleResult(HRESULT hrReturned, LPCWSTR pwszFile, ULONG ulLine, std::string& strErr);
#endif

#endif                    

