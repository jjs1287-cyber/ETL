/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <stdarg.h>

#include <cassert>
#include <string>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/RCStringTable.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>

#include <konga/Utils/PortDefs.h>	                                

#include <konga/Server/WDIPathFinder.h>

#include "ViewLog.h"
#include "ErrorLog.h"

using std::string;

#define _MAX_BUFFER_LEN 4096

CErrorLog::CErrorLog() :
	m_bInitialized(false),
	m_bErrorsEncountered(false),
	m_bLoadCalled(false),
	m_pViewLog(0),
	m_pMapStringTable(0),
	m_hrLastErrorMade(S_OK)
{
}

CErrorLog::~CErrorLog()
{
#ifndef _DEBUG
	try {
#endif
		Terminate();
#ifndef _DEBUG
	} 
	catch(...)
	{
		if( m_pViewLog )
			m_pViewLog->DebugString("CErrorLog::~CErrorLog() exception");
	}
#endif
}

namespace {
	boost::mutex g_InitStringTableMutex;
	bool g_bStringTableInitialized = false;
	std::map<int,std::string> g_mapStringTable;
}

void CErrorLog::Initialize()
{
	if( !m_bInitialized )
	{
		boost::mutex::scoped_lock lock(g_InitStringTableMutex);
		if ( !g_bStringTableInitialized ) {
			konga::RCStringTable rcs;
			const std::string
				stRCFileName( konga::GetServerHomeWithSlash()+"Resources/TransformationEngine/ErrorCodes/ErrorCodes.rc" );
			if( !rcs.Parse(stRCFileName, g_mapStringTable) ) {
				g_mapStringTable.clear();
				throw konga::CrException("Failed to parse error message resource file \""
					+ stRCFileName + "\".\nReason: " + rcs.getErrorString());
			}
			g_bStringTableInitialized = true;
		}

		                                           
		m_pMapStringTable = &g_mapStringTable;
		m_bErrorsEncountered = false;
		m_bInitialized = true;
	}
}

void CErrorLog::Terminate()
{
	m_bInitialized = false;
	m_pMapStringTable = 0;
}

std::string CErrorLog::GetLogFileName() const {
	if ( m_pViewLog )
		return m_pViewLog->GetLogFileName();
	return std::string();
}

unsigned long CErrorLog::MakeError(const char* szFileName, unsigned int uiLineNumber, long lErrorCode, ...)
{
	va_list argList;
	va_start(argList, lErrorCode);
	unsigned long hr = S_OK;
	KString sMessage = ("");

	m_bErrorsEncountered = true;

	if(!m_bInitialized)Initialize();

    if(m_bInitialized)
	{
		assert(m_pMapStringTable);
        std::map<int,std::string>::const_iterator it=m_pMapStringTable->find(lErrorCode);

        if(it==m_pMapStringTable->end()){
			m_lastErr.Format("Failed to load resource string for error id %d", lErrorCode);
			hr = E_FAIL;
        } else {
            std::string stFormat=it->second;
            sMessage.FormatV(stFormat.c_str(), argList);

                         
            while(sMessage.GetLength()>0 && sMessage.Right(1) == "\n")
            {
                sMessage = sMessage.Left(sMessage.GetLength() - 1);
            }

            sMessage += (" [CODE:");
            sMessage += konga::toString(lErrorCode);
            sMessage += ("]");
			if ( szFileName ) {
				string tmp( konga::GetLeafPath(szFileName) );
				if ( !tmp.empty() ) {
					sMessage += " file: " + tmp + ", line " + konga::toString(uiLineNumber);
				}
			}

            if( m_pViewLog )
                m_pViewLog->LogMessage( "%s", (const char*)sMessage);

            m_lastErr = sMessage;
            hr = E_FAIL | lErrorCode;
        }
	}
	else
	{
        m_lastErr = "ErrorCodes is not initialized.";
        hr = E_FAIL;
	}
	m_hrLastErrorMade = hr;
	
	va_end(argList);
	return hr;
}
