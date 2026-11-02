/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_MSGDB_DTLSPINSERTINCOMINGMSGTAB_H
#define KONGA_DB_MSGDB_DTLSPINSERTINCOMINGMSGTAB_H

#include <string>

#include <dtl/DTL.h>
#include <dtl/nullable.h>
            
#include <konga/Db/MsgDb/SpInsertIncomingMsgTab.h>

namespace dtl_msgdb
{


class DtlSpInsertIncomingMsgTab : public MsgDb::SpInsertIncomingMsgTab
{
public:
    struct SpReturnVals
    {
                                                      
    };
    
    class SpBCA
    {
    public:
        void operator()(dtl::BoundIOs& cols, SpReturnVals& r)
        {
                                                          
        }
    };
    
    struct SpParameters
    {
		SpParameters(void) : MsgId(-1) {}

        dtl::nullable<std::string> MsgFrom;
        dtl::nullable<std::string> MsgTo;
        dtl::nullable<std::string> MsgSubject;
        dtl::nullable<std::string> ProcessVariables;
        dtl::nullable<int> ReceivedAt;
        dtl::nullable<int> IsExecuting;
        dtl::nullable<std::string> Owner;
        dtl::nullable<int> MsgPriority;
        dtl::nullable<int> NumThreads;
        int	MsgId;
    };
    
    class SpBPA
    {
    public:
        void operator()(dtl::BoundIOs& cols, SpParameters& params)
        {
            cols[0] >> params.MsgId;
            cols[1] << params.MsgFrom;
            cols[2] << params.MsgTo;
            cols[3] << params.MsgSubject;
            cols[4] << params.ProcessVariables;
            cols[5] << params.ReceivedAt;
            cols[6] << params.IsExecuting;
            cols[7] << params.Owner;
            cols[8] << params.MsgPriority;
            cols[9] << params.NumThreads;
        }
    };



    DtlSpInsertIncomingMsgTab(dtl::DBConnection* pConn) : m_pConn(pConn) {}
    virtual ~DtlSpInsertIncomingMsgTab() {}

    void SetMsgFrom(const std::string& stMsgFrom) {
        m_params.MsgFrom = stMsgFrom;
    }
    void SetMsgFromToNull() {
        m_params.MsgFrom.SetNull();
    }

    void SetMsgTo(const std::string& stMsgTo) {
        m_params.MsgTo = stMsgTo;
    }
    void SetMsgToToNull() {
        m_params.MsgTo.SetNull();
    }

    void SetMsgSubject(const std::string& stMsgSubj) {
        m_params.MsgSubject = stMsgSubj;
    }
    void SetMsgSubjectToNull() {
        m_params.MsgSubject.SetNull();
    }

    void SetProcessVariables(const std::string& stProcVars) {
        m_params.ProcessVariables = stProcVars;
    }
    void SetProcessVariablesToNull() {
        m_params.ProcessVariables.SetNull();
    }

    void SetReceivedAt(int i) {
        m_params.ReceivedAt = i;
    }
    void SetReceivedAtToNull() {
        m_params.ReceivedAt.SetNull();
    }

    void SetIsExecuting(int i) {
        m_params.IsExecuting = i;
    }
    void SetIsExecutingToNull() {
        m_params.IsExecuting.SetNull();
    }

    void SetOwner(const std::string stOwner) {
        m_params.Owner = stOwner;
    }
    void SetOwnerToNull() {
        m_params.Owner.SetNull();
    }

    void SetMsgPriority(int i) {
        m_params.MsgPriority = i;
    }
    void SetMsgPriorityToNull() {
        m_params.MsgPriority.SetNull();
    }

    void SetNumThreads(int i) {
        m_params.NumThreads = i;
    }
    void SetNumThreadsToNull() {
        m_params.NumThreads.SetNull();
    }

    int GetMsgId() const {
        return m_iMsgId;
    }

    void Execute();

    void Close() {                     }
    
private:
    dtl::DBConnection* m_pConn;
    SpParameters m_params;
    int  m_iMsgId;
};


}                           


#endif                                              

