/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_DTLSPINSERTLOGTAB_H
#define KONGA_DB_TRANDB_DTLSPINSERTLOGTAB_H

#include <string>

#include <dtl/DTL.h>
#include <dtl/nullable.h>

#include <konga/Db/DtlDbUtils/DtlDbUtils.h>            
#include <konga/Db/TranDb/SpInsertLogTab.h>

using namespace dtl;

namespace dtl_trandb
{

class DtlSpInsertLogTab : public TranDb::SpInsertLogTab
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
		SpParameters(void) : LogId(-1) {}

		dtl::nullable<int> OperationId;
        dtl::nullable<TIMESTAMP_STRUCT> TimeStamp;
        dtl::nullable<std::string> LogSource;
        dtl::nullable<std::string> ShortMessage;
        dtl::nullable<std::string> Severity;
        dtl::nullable<int> ErrorCode;
        int	LogId;
    };
    
    class SpBPA
    {
    public:
        void operator()(dtl::BoundIOs& cols, SpParameters& params)
        {
            cols[0] >> params.LogId;
            cols[1] << params.OperationId;
            cols[2] << params.TimeStamp;
            cols[3] << params.LogSource;
            cols[4] << params.ShortMessage;
            cols[5] << params.Severity;
            cols[6] << params.ErrorCode;
        }
    };


    DtlSpInsertLogTab(dtl::DBConnection* pConn) : m_pConn(pConn) {}
    virtual ~DtlSpInsertLogTab() {}

	void SetOperationId(long lOperationId) {
		m_params.OperationId = lOperationId;
	}

	void SetTimeStamp(const TIMESTAMP_STRUCT& tTimeStamp) {
		m_params.TimeStamp = tTimeStamp;
	}

	void SetLogSource(const std::string& stLogSource) {
		m_params.LogSource = stLogSource;
	}

	void SetShortMessage(const std::string& stShortMsg) {
		m_params.ShortMessage = stShortMsg;
	}

	void SetSeverity(const std::string& stSeverity) {
		m_params.Severity = stSeverity;
	}

	void SetErrorCode(long lErrorCode) {
		m_params.ErrorCode = lErrorCode;
	}

	int GetLogId() const {
		return m_iLogId;
	}

    void Execute();
    void Close() {                     }
    
private:
    dtl::DBConnection* m_pConn;
    SpParameters m_params;
    int  m_iLogId;
};

}                           

#endif                                       

