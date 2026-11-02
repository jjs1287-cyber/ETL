/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#if !defined(AFX_VIEWLOG_H__D07D15A3_FD3C_11D4_8906_000000000000__INCLUDED_)
#define AFX_VIEWLOG_H__D07D15A3_FD3C_11D4_8906_000000000000__INCLUDED_

#include <string>
#include <map>
#include <list>

#include <konga/Utils/KString.h>
#include <konga/Utils/StringUtils.h>

namespace konga { class OperationContext; }


                
class CViewLog  
{
public:
	CViewLog();
	virtual ~CViewLog();

	bool IsInitialized() const { return m_bInitialized; }
	bool Initialize(const char*sPrefix, long id, FILE*& debug_log, bool bTraceFlag, bool bWriteLog, int iThread, int iChunk, bool bChunking, const std::string& guid);
	void Terminate();
	void setOperationCleanupContext(konga::OperationContext* context) { m_cleanupContext = context; }

	void Close();
	void Delete();

	void LogMessage(const char* sMessageFmt, ...);
	void OpLog(const char* sMessage);

	void DebugString(const char* sMessageFmt, ...);
	bool IsDebugLogEnabled() const { return m_pDebugLog!=0 && m_bWriteLog; };
	bool IsLogCommitted() const { return m_bLogCommitted; }
	bool IsLogSuppressed() const { return m_bLogSuppressed; }

	bool IsErrLogEmpty() const;
	void Reset() { m_bTraceFlag = m_bLogCommitted = m_bLogSuppressed = false; }

	FILE* GetDebugLog() { return m_pDebugLog; };

	FILE* SetDebugLogFileName(const char* sFileName);
	void setWriteLog(bool b){m_bWriteLog=b;}

	std::string GetLastLogMsg() const { return m_sLastLogMsg; }
	void ResetLastLogMsg(const std::string& stMessage) { m_sLastLogMsg = stMessage; }

	                                                           
	                                           
	void GetLogContents(std::string& stLogContents);

	std::string GetLogFileName() const {
		return m_sErrLogFile;
	}
	void SetLogFileName(const std::string& sErrLogFile){m_sErrLogFile=sErrLogFile;}
	bool debugCopyFile(const std::string& where, const std::string& stFileName, const std::string& stToPath, bool bOverWrite = false);

private:
	bool IsAlreadyLogged(const KString& str);
	bool MaxSizeLimitReached();

	std::string GetChunkLogFileName() const;

private:
	FILE* m_pErrLog;
	konga::OperationContext* m_cleanupContext;
	FILE* m_pDebugLog;
	bool m_bTraceFlag;
	bool m_bLogCommitted;	                              
	bool m_bLogSuppressed;	                         
	bool m_bInitialized;
	bool m_bWriteLog;
	bool m_bHasWarning;
	bool m_bLimitReached;
	int m_iThread, m_iChunk;

	std::string m_sErrLogFile;
	std::string m_sLastLogMsg;

	                                                  
	std::list<std::string> m_oldMsgMD5;
};

#endif                                                                            

