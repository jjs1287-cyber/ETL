/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_SPINSERTOPERATIONINSTANCETAB_H
#define KONGA_DB_SPINSERTOPERATIONINSTANCETAB_H

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
	class SpInsertOperationInstanceTab
	{
	public:
		static SpInsertOperationInstanceTab* MakeInstance(konga::DbConnection* pConn);
	
		SpInsertOperationInstanceTab() {}
		virtual ~SpInsertOperationInstanceTab() {}
	
		virtual void SetOperationId(long lOperationId) = 0;
		virtual void SetParentOperationInstanceId(long long llParentOperationInstanceId) = 0;
		virtual void SetReceivedFrom(const std::string& stFrom) = 0;

		virtual long long GetOperationInstanceId() const = 0;

		virtual void Execute() = 0;
		virtual void Close() = 0;
	};
}
#endif                                           

