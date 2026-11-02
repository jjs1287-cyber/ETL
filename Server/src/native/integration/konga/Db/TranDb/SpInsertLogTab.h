/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANDB_SPINSERTLOGTAB_H
#define TRANDB_SPINSERTLOGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

namespace konga
{
    class DbConnection;
}
                     
namespace TranDb
{
	class SpInsertLogTab
	{
	public:
		static SpInsertLogTab* MakeInstance(konga::DbConnection* pConn);
	
		SpInsertLogTab() {}
		virtual ~SpInsertLogTab() {}
	
		virtual void SetOperationId(long lOperationId) = 0;
		virtual void SetTimeStamp(const TIMESTAMP_STRUCT& tTimeStamp) = 0;
		virtual void SetLogSource(const std::string& stLogSource) = 0;
		virtual void SetShortMessage(const std::string& stShortMsg) = 0;
		virtual void SetSeverity(const std::string& stSeverity) = 0;
		virtual void SetErrorCode(long lErrorCode) = 0;

		virtual int GetLogId() const = 0;

		virtual void Execute() = 0;
		virtual void Close() = 0;
	};
}
#endif                           

