/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_IDB_DTLSPINSERTTASKTAB_H
#define KONGA_DB_IDB_DTLSPINSERTTASKTAB_H

#include <string>

#include <dtl/DTL.h>
#include <dtl/nullable.h>

#include <konga/Db/DbUtils/DbNullException.h>
#include <konga/Db/IDb/SpInsertTaskTab.h>


namespace dtl_idb
{

class DtlSpInsertTaskTab : public IDb::SpInsertTaskTab
{
public:

    struct SpReturnVals
    {
		                                              
    };
    
    class SpBCA
    {
    public:
        void operator()(dtl::BoundIOs &cols, SpReturnVals &r)
        {
			                                              
        }
    };
    
    struct SpParameters
    {
		SpParameters(void) : TaskInstanceId(-1) {}
                                                                                            
                                                                                  
        dtl::nullable<int> ProcessId;
        dtl::nullable<int> ProcessInstanceId;
        dtl::nullable<int> TaskId;
        dtl::nullable<std::string> TaskUser;
        dtl::nullable<int> InitTime;
        dtl::nullable<int> StartTime;
        dtl::nullable<int> Status;
        dtl::nullable<std::string> DataElementValueList;
        dtl::nullable<std::string> FromDocDataElementValueList;
        dtl::nullable<std::string> EscalationId;
        dtl::nullable<int> EndTime;
        dtl::nullable<int> MsgId;
        dtl::nullable<int> MsgPriority;
		int	TaskInstanceId;
    };
    
    
    class SpBPA
    {
    public:
         void operator()(dtl::BoundIOs &cols, SpParameters &r)
         {
			 cols[0] >> r.TaskInstanceId;
             cols[1] << r.ProcessId;
             cols[2] << r.ProcessInstanceId;
             cols[3] << r.TaskId;
             cols[4] << r.TaskUser;
             cols[5] << r.InitTime;
             cols[6] << r.StartTime;
             cols[7] << r.Status;
             cols[8] << r.DataElementValueList;
             cols[9] << r.FromDocDataElementValueList;
             cols[10] << r.EscalationId;
             cols[11] << r.EndTime;
             cols[12] << r.MsgId;
             cols[13] << r.MsgPriority;
         }
    };

    DtlSpInsertTaskTab(dtl::DBConnection* pConn) : m_pConn(pConn) {}
    virtual ~DtlSpInsertTaskTab() {}

    void SetProcessId(int id) {
        m_params.ProcessId = id;
    }

    void SetProcessInstanceId(int id) {
        m_params.ProcessInstanceId = id;
    }

    void SetTaskId(int id) {
        m_params.TaskId = id;
    }

    void SetTaskUser(const std::string& stUser) {
        m_params.TaskUser = stUser;
    }
    void SetTaskUserToNull() {
        m_params.TaskUser.SetNull();
    }

    void SetInitTime(int i) {
        m_params.InitTime = i;
    }
    void SetInitTimeToNull() {
        m_params.InitTime.SetNull();
    }

    void SetStartTime(int i) {
        m_params.StartTime = i;
    }
    void SetStartTimeToNull() {
        m_params.StartTime.SetNull();
    }

    void SetStatus(int i) {
        m_params.Status = i;
    }
    void SetStatusToNull() {
        m_params.Status.SetNull();
    }

    void SetDataElementValueList(const std::string& s) {
        m_params.DataElementValueList = s;
    }
    void SetDataElementValueListToNull() {
        m_params.DataElementValueList.SetNull();
    }

    void SetFromDocDataElementValueList(const std::string& s) {
        m_params.FromDocDataElementValueList = s;
    }
    void SetFromDocDataElementValueListToNull() {
        m_params.FromDocDataElementValueList.SetNull();
    }

    void SetEscalationId(const std::string& s) {
        m_params.EscalationId = s;
    }
    void SetEscalationIdToNull() {
        m_params.EscalationId.SetNull();
    }

    void SetEndTime(int i) {
        m_params.EndTime = i;
    }
    void SetEndTimeToNull() {
        m_params.EndTime.SetNull();
    }

    void SetMsgId(int id) {
        m_params.MsgId = id;
    }
    void SetMsgIdToNull() {
        m_params.MsgId.SetNull();
    }

    void SetMsgPriority(int i) {
        m_params.MsgPriority = i;
    }
    void SetMsgPriorityToNull() {
        m_params.MsgPriority.SetNull();
    }

    virtual int GetTaskInstanceId() const {
        return m_iTaskInstanceId;
    }

    void Close() {
                        
    }

	void Execute();

private:
    dtl::DBConnection* m_pConn;

    SpParameters    m_params;
	int	m_iTaskInstanceId;
};

}                         


#endif                                     

