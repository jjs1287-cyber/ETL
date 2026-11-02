/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stdarg.h>

#include <algorithm>		            
#include <cassert>

#include <boost/scoped_array.hpp>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/ErrorResolver.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/TranDb/Key_Values.h>

#include <konga/Server/WDIPathFinder.h>
#include <konga/Server/WDILogFileUtils.h>

#include <konga/Utils/PortDefs.h>

#include "ViewLog.h"
#include <konga/OperationContext/OperationContext.h>

#define LOGFILE_LIMIT (1024*1024)

using namespace TranDb;
using namespace konga;
using std::list;
using std::string;

CViewLog::CViewLog() :
	m_pErrLog(0),
	m_cleanupContext(0),
	m_pDebugLog(0),
	m_bTraceFlag(false),
	m_bLogCommitted(false),
	m_bLogSuppressed(false),
	m_bInitialized(false),
	m_bWriteLog(false),
	m_bHasWarning(false),
	m_bLimitReached(false),
	m_iThread(0),
	m_iChunk(0)
{
}

CViewLog::~CViewLog()
{
	Terminate();
}

void CViewLog::Terminate()
{
	m_bInitialized = false;
	Close();

	if(m_pDebugLog)
	{
		fclose(m_pDebugLog);
		m_pDebugLog = 0;
	}
	m_oldMsgMD5.clear();
}

FILE* CViewLog::SetDebugLogFileName(const char* sFileName)
{
	if(m_pDebugLog)
		fclose(m_pDebugLog);

	m_pDebugLog = fopen(sFileName, "wb+");
	return m_pDebugLog;
}

string CViewLog::GetChunkLogFileName() const {
	const string::size_type k=m_sErrLogFile.find(".txt");
	return string(m_sErrLogFile.substr(0,k)+"_"+konga::toString(m_iChunk)+".txt");
}

                             
                                                                           
                                            
                              
                                
bool CViewLog::Initialize(const char* sPrefix, long id, FILE*& debug_log, bool bTraceFlag, bool bWriteLog, int iThread, int iChunk, bool bChunking, const std::string& guid) {
	m_iThread=iThread;
	m_iChunk=iChunk;
	m_bWriteLog=bWriteLog;
	bool bRetVal = true;

	Reset();

	try {
		                 
		                                                                           
		                                                         
		if (bWriteLog && iThread>=iChunk) {
			if( m_pDebugLog ) {
				                                                                           
				                                                                       
				fclose(m_pDebugLog);
				m_pDebugLog=0;
			}
			KString WDIDebugPath;
			WDIPathFinder::GetWDIDebugPath(WDIDebugPath);
			WDIDebugPath+=sPrefix;
			if(id>0)WDIDebugPath+=konga::toString(id);
			WDIDebugPath+="_"+guid;
			if(bChunking)WDIDebugPath+="_T"+konga::toString(iThread);
			WDIDebugPath+=".log";

			m_pDebugLog = fopen(WDIDebugPath, "wb+");
			if (m_pDebugLog && m_cleanupContext) m_cleanupContext->registerTemporaryLogFile((const char*)WDIDebugPath);
			debug_log = m_pDebugLog;
		}

		                       
		m_bTraceFlag = bTraceFlag;
		DebugString("User Log Setting: %s Create Log file", (m_bTraceFlag) ? "Can" : "Can't" );

		if( m_pErrLog ) {
			fclose(m_pErrLog);
			m_pErrLog = 0;
			m_bLimitReached = false;
		}

		if (m_bTraceFlag) {
			if (m_sErrLogFile.empty()) {
				m_sErrLogFile = WDILogFileUtils::GenerateErrLogFileName(id, guid);
			}
			const string& sErrLogFile_= GetChunkLogFileName();
			m_pErrLog = fopen(sErrLogFile_.c_str(), "ab+");
			if (m_pErrLog && m_cleanupContext) m_cleanupContext->registerTemporaryLogFile(sErrLogFile_);
			if (!m_pErrLog) {
				DebugString("Unable to create the partial log file '%s'\n", sErrLogFile_.c_str());
			}
			else {
				DebugString("Created the partial log file '%s'", sErrLogFile_.c_str());
			}
		}
	}
	catch(const CrFatalException& e) {
		throw e;
	}
#ifndef _DEBUG
	catch(...) {
		bRetVal = false;
	}
#endif

	m_bInitialized = bRetVal;
	return bRetVal;
}

namespace {
	const unsigned int MAX_LOG_MEMORY = 50;
}

bool CViewLog::IsAlreadyLogged(const KString& str) {
	const string& stMD5 = konga::md5((const char*)str, str.GetLength());
	if ( std::find(m_oldMsgMD5.begin(), m_oldMsgMD5.end(), stMD5 ) == m_oldMsgMD5.end() ) {
		                             
		m_oldMsgMD5.push_back(stMD5);
		if ( m_oldMsgMD5.size() > MAX_LOG_MEMORY ) {
			m_oldMsgMD5.erase(m_oldMsgMD5.begin());
		}
		return false;
	}
	else {
		return true;
	}
}

bool CViewLog::MaxSizeLimitReached() {
	if (m_bLimitReached)
		return true;

	                                                           
	if (ftell(m_pErrLog) >= LOGFILE_LIMIT) {
		fprintf(m_pErrLog, "Message : Size of log file is >= %d bytes so logging has been stopped\n", LOGFILE_LIMIT);
		return (m_bLimitReached = true);
	}
	return false;
}

                             
                                                                                                     
                                                     
void CViewLog::LogMessage( const char* sMessageFmt, ...)
{
	try {
		assert(sMessageFmt);
		if(!m_pErrLog || !sMessageFmt || *sMessageFmt==0) return;

		if ( MaxSizeLimitReached() )
			return;

		va_list argList;
		va_start(argList, sMessageFmt);

		KString sOutput;
		sOutput.FormatV(sMessageFmt, argList);

		             
		while(sOutput.Mid(sOutput.GetLength() - 1) == "\n")
		{
			sOutput = sOutput.Mid(0, sOutput.GetLength() - 1);
		}

		m_sLastLogMsg = (const char*)sOutput;
		sOutput += "\n";

		const bool bLogOk = true;
		if(bLogOk)
		{
			if ( !IsAlreadyLogged(sOutput) ) {
				fprintf(m_pErrLog, "%s", (const char*)sOutput);
				fflush(m_pErrLog);
				m_bHasWarning=true;
				m_bLogCommitted = true;
			}
		}
		else
		{
			m_bLogSuppressed = true;	                 
		}

		va_end(argList);

		DebugString(sOutput);
	} 
	catch(const CrFatalException& e)
	{
		throw e;
	}
#ifndef _DEBUG
	catch(...) {}
#endif
}

                        
                                                                                              
                                                               
void CViewLog::OpLog(const char* sMessage)
{
	try {
		assert(sMessage);
		if(!m_pErrLog || !sMessage || !*sMessage) return;

		if ( MaxSizeLimitReached() )
			return;

		int len=strlen(sMessage);
		             
		while(sMessage[len-1] == '\n')len--;

		if(len==0)return;

		string sOutput(sMessage,len);

		fprintf(m_pErrLog, "%s\n", sOutput.c_str());
		fflush(m_pErrLog);
		m_bLogCommitted = true;

		DebugString(sOutput.c_str());
	} 
	catch(const CrFatalException& e)
	{
		throw e;
	}
#ifndef _DEBUG
	catch(...) {}
#endif

}

                              
                                                         
void CViewLog::DebugString(const char* sMessageFmt, ...)
{
	if( !m_bWriteLog || m_pDebugLog==0 )return;

	try {
		va_list argList;
   		va_start(argList, (const char*)sMessageFmt);

		KString sOutput;
		sOutput.FormatV((const char*)sMessageFmt, argList);
		if(sOutput.IsEmpty()) return;

		                               
		if( sOutput.GetAt(sOutput.GetLength()-1) != '\n' ) {
			sOutput += "\n";
		}

		                                  
		const string stDate(CrDateTime::GetCurrentTime().toString("HH:MM:SS") + ": ");
		sOutput.Insert(0, stDate.c_str());

		fprintf(m_pDebugLog, "%s", (const char*)sOutput);
		fflush(m_pDebugLog);

		va_end(argList);
	} 
	catch(const CrFatalException& e)
	{
		throw e;
	}
#ifndef _DEBUG
	catch(...) {
		                         
	}
#endif
}


                        
                                         
void CViewLog::Close()
{
#ifndef _DEBUG
	try {
#endif
		                         
		if (m_pErrLog)
		{
			if(IsErrLogEmpty()){
				Delete();			
			} else {
				fclose(m_pErrLog);
			}
			m_pErrLog = 0;
			m_bLimitReached = false;
		}
#ifndef _DEBUG
	} catch(...) {
		               
	}
#endif
}


                         
                                          
                              
                                
void CViewLog::Delete()
{
	                                               
	if(m_bTraceFlag)
	{
		DebugString("A> Deleteting ERRORLOG file\n");
		if( m_pErrLog != 0 )
		{
			fclose(m_pErrLog);
			m_pErrLog=0;
			m_bLimitReached=false;
		}

		try {
			long lerr;
			std::string sErrLogFile=GetChunkLogFileName();
			if(konga::GetFileSize(sErrLogFile,lerr)==0&&lerr==0) {
				konga::DeleteFileWithRetries(sErrLogFile, 5, 100);
				m_sErrLogFile.clear();
			}
		}
		catch( const CrException& e){
			printf("Can't delete the file\nfile name: %s\nreason: %s\n",m_sErrLogFile.c_str(), e.GetErrorString().c_str());
		}
	}
}

bool CViewLog::IsErrLogEmpty() const
{
	if(m_pErrLog == 0)
		return true;

	fseek(m_pErrLog, 0L, SEEK_END);
	return (ftell(m_pErrLog) == 0);
}

void CViewLog::GetLogContents(std::string& stLogContents) {
 	stLogContents.clear();
	if ( m_pErrLog ) {
		                                                                       
		fflush(m_pErrLog);
		if ( IsErrLogEmpty() )
			return;

		                     
		fseek(m_pErrLog, 0, SEEK_END);
		const size_t size = ftell(m_pErrLog);
		rewind(m_pErrLog);

		                    
		boost::scoped_array<char> buf( new char[size] );
		fread(buf.get(), 1, size, m_pErrLog);
		if ( ferror(m_pErrLog) != 0 ) {
			                
			stLogContents = "Failed to read error log!\n";
			const long lErr = GetLastError();
			stLogContents += ErrorResolver(lErr).Resolve(lErr);
		}
		else {
			stLogContents.assign(buf.get(), size);
		}

		Close();
	}
	else {
		try {
			const std::string& stLogFileName = GetLogFileName();
			if ( stLogFileName.empty() || !NonEmptyFileExists(stLogFileName) )
				return;
			
			read_file_to_string(stLogFileName, stLogContents);
		}
		catch( const CrException& ex ) {
			stLogContents = "Failed to read error log!\n" + ex.GetErrorString();
		}
	}
}

bool CViewLog::debugCopyFile(const std::string& sWhere, const std::string& stFileName, const std::string& stToPath, bool bOverWrite){
	if(stFileName.empty()){
		LogMessage("DEBUG: CopyFile failed, from_file_name is empty, to_file_name=\"%s\", called from \"%s\"\n",stToPath.c_str(), sWhere.c_str());
		return false;
	}
	if(konga::FileExists(stFileName)){
		return konga::kCopyFile(stFileName,stToPath,bOverWrite);
	}
	else {
		LogMessage("DEBUG: CopyFile failed, input file does not exists, to_file_name=\"%s\", called from \"%s\"\n",stToPath.c_str(), sWhere.c_str());
		return false;
	}
}

