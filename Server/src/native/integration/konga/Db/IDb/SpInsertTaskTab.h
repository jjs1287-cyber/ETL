/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_IDB_SPINSERTTASKTAB_H
#define KONGA_DB_IDB_SPINSERTTASKTAB_H

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

class SpInsertTaskTab
{
public:
    static SpInsertTaskTab* MakeInstance(konga::DbConnection* pConn);

    SpInsertTaskTab() {}
    virtual ~SpInsertTaskTab() {}

    virtual void SetProcessId(int id) = 0;

    virtual void SetProcessInstanceId(int id) = 0;

    virtual void SetTaskId(int id) = 0;

    virtual void SetTaskUser(const std::string& stUser) = 0;
    virtual void SetTaskUserToNull() = 0;

    virtual void SetInitTime(int i) = 0;
    virtual void SetInitTimeToNull() = 0;

    virtual void SetStartTime(int i) = 0;
    virtual void SetStartTimeToNull() = 0;

    virtual void SetStatus(int i) = 0;
    virtual void SetStatusToNull() = 0;

    virtual void SetDataElementValueList(const std::string& s) = 0;
    virtual void SetDataElementValueListToNull() = 0;

    virtual void SetFromDocDataElementValueList(const std::string& s) = 0;
    virtual void SetFromDocDataElementValueListToNull() = 0;

    virtual void SetEscalationId(const std::string& s) = 0;
    virtual void SetEscalationIdToNull() = 0;

    virtual void SetEndTime(int i) = 0;
    virtual void SetEndTimeToNull() = 0;

    virtual void SetMsgId(int id) = 0;
    virtual void SetMsgIdToNull() = 0;

    virtual void SetMsgPriority(int i) = 0;
    virtual void SetMsgPriorityToNull() = 0;

	                   
    virtual int GetTaskInstanceId() const = 0;

    virtual void Execute() = 0;
    virtual void Close() = 0;
};


}                     

                     

#endif                                  
