/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_IDB_SP_INSERT_PROCESS_TAB_H
#define KONGA_DB_IDB_SP_INSERT_PROCESS_TAB_H

#include <string>

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>

namespace konga
{
    class DbConnection;
}

namespace IDb
{

class SpInsertProcessTab
{
public:
    static SpInsertProcessTab* MakeInstance(konga::DbConnection* pConn);

    SpInsertProcessTab() {}
    virtual ~SpInsertProcessTab() {}

    virtual void SetProcessId(int id) = 0;

    virtual void SetProcessVersion(int id) = 0;

    virtual void SetInitialTask(int id) = 0;

    virtual void SetStartTime(int i) = 0;
    virtual void SetStartTimeToNull() = 0;

    virtual void SetStatus(int i) = 0;
    virtual void SetStatusToNull() = 0;

	virtual void SetStatusTime(int i) = 0;
    virtual void SetStatusTimeToNull() = 0;

    virtual void SetUpdateFlag(int i) = 0;
    virtual void SetUpdateFlagToNull() = 0;

	                   
    virtual int GetProcessInstanceId() const = 0;

    virtual void Execute() = 0;
    virtual void Close() = 0;
};


}                     

                     

#endif                                        
