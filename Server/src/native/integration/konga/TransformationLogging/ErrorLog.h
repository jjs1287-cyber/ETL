/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#if !defined(AFX_ERRORLOG_H__3843DD0A_CD51_11D4_8BC8_0050DA10CC41__INCLUDED_)
#define AFX_ERRORLOG_H__3843DD0A_CD51_11D4_8BC8_0050DA10CC41__INCLUDED_

#include <string>
#include <map>

#include <konga/Utils/KString.h>

class CViewLog;

class CErrorLog  
{
public:
	CErrorLog();
	virtual ~CErrorLog();

	bool IsInitialized() const { return m_bInitialized; }
	void Initialize();
	void Terminate();

	bool IsErrorsEncountered() const { return m_bErrorsEncountered; };
	void SetFatalError(bool bFatalFlag) { m_bErrorsEncountered = !bFatalFlag; };

	KString GetLastErr() const { return m_lastErr;}
	unsigned long GetLastErrorMade() const { return m_hrLastErrorMade; }

	unsigned long MakeError(const char* szFileName, unsigned int uiLineNumber, long lErrorCode, ...);

	                                                                                                      
	void SetViewLog(CViewLog* pViewLog) { m_pViewLog = pViewLog; }

	std::string GetLogFileName() const;

private:
	bool m_bInitialized;
	bool m_bErrorsEncountered;
	bool m_bLoadCalled;

	CViewLog* m_pViewLog;
    const std::map<int,std::string>* m_pMapStringTable;

	unsigned long m_hrLastErrorMade;
	KString m_lastErr;
};

#endif                                                                             
