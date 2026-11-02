/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_IDB_DTL_SPINSERT_PROCESS_TAB_H
#define KONGA_DB_IDB_DTL_SPINSERT_PROCESS_TAB_H

#include <string>

#include <dtl/DTL.h>
#include <dtl/nullable.h>

#include <konga/Db/DbUtils/DbNullException.h>
#include <konga/Db/IDb/SpInsertProcessTab.h>


namespace dtl_idb
{

class DtlSpInsertProcessTab : public IDb::SpInsertProcessTab
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
		SpParameters(void) : ProcessInstanceId(-1) {}
                                                                                            
                                                                                  
        dtl::nullable<int> ProcessId;
        dtl::nullable<int> ProcessVersion;
        dtl::nullable<int> InitialTask;
        dtl::nullable<int> StartTime;
        dtl::nullable<int> Status;
        dtl::nullable<int> StatusTime;
        dtl::nullable<int> UpdateFlag;
		int	ProcessInstanceId;
    };
    
    
    class SpBPA
    {
    public:
         void operator()(dtl::BoundIOs &cols, SpParameters &params)
         {
			 cols[0] >> params.ProcessInstanceId;	                   
             cols[1] << params.ProcessId;
             cols[2] << params.ProcessVersion;
             cols[3] << params.InitialTask;
             cols[4] << params.StartTime;
             cols[5] << params.Status;
             cols[6] << params.StatusTime;
             cols[7] << params.UpdateFlag;
         }
    };

    DtlSpInsertProcessTab(dtl::DBConnection* pConn) : m_pConn(pConn) {}
    virtual ~DtlSpInsertProcessTab() {}

    void SetProcessId(int id) {
        m_params.ProcessId = id;
    }

    void SetProcessVersion(int id) {
        m_params.ProcessVersion = id;
    }

    void SetInitialTask(int id) {
        m_params.InitialTask = id;
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

    void SetStatusTime(int i) {
        m_params.StatusTime = i;
    }
    void SetStatusTimeToNull() {
        m_params.StatusTime.SetNull();
    }

    void SetUpdateFlag(int i) {
        m_params.UpdateFlag = i;
    }
    void SetUpdateFlagToNull() {
        m_params.UpdateFlag.SetNull();
    }

    virtual int GetProcessInstanceId() const {
        return m_iProcessInstanceId;
    }

    void Close() {
                        
    }

	void Execute();

private:
    dtl::DBConnection* m_pConn;

    SpParameters    m_params;
	int	m_iProcessInstanceId;
};

}                         


#endif                                           

