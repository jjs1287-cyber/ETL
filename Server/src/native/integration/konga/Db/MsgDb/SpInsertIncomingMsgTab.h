/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_MSGDB_SPINSERTINCOMINGMSGTAB_H
#define KONGA_DB_MSGDB_SPINSERTINCOMINGMSGTAB_H

#include <string>

namespace konga
{
    class DbConnection;
}
                     
namespace MsgDb
{

class SpInsertIncomingMsgTab
{
public:
    static SpInsertIncomingMsgTab* MakeInstance(konga::DbConnection* pConn);

    SpInsertIncomingMsgTab() {}
    virtual ~SpInsertIncomingMsgTab() {}

    virtual void SetMsgFrom(const std::string& stMsgFrom) = 0;
    virtual void SetMsgFromToNull() = 0;

    virtual void SetMsgTo(const std::string& stMsgTo) = 0;
    virtual void SetMsgToToNull() = 0;

    virtual void SetMsgSubject(const std::string& stMsgSubj) = 0;
    virtual void SetMsgSubjectToNull() = 0;

    virtual void SetProcessVariables(const std::string& stProcVars) = 0;
    virtual void SetProcessVariablesToNull() = 0;

    virtual void SetReceivedAt(int i) = 0;
    virtual void SetReceivedAtToNull() = 0;

    virtual void SetIsExecuting(int i) = 0;
    virtual void SetIsExecutingToNull() = 0;

    virtual void SetOwner(const std::string stOwner) = 0;
    virtual void SetOwnerToNull() = 0;

    virtual void SetMsgPriority(int i) = 0;
    virtual void SetMsgPriorityToNull() = 0;

    virtual void SetNumThreads(int i) = 0;
    virtual void SetNumThreadsToNull() = 0;

    virtual int GetMsgId() const = 0;

    virtual void Execute() = 0;

    virtual void Close() = 0;

};

}



#endif

