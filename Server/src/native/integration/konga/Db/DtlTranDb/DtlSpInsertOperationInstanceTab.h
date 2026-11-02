/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_DTLSPINSERTOPERATIONINSTANCETAB_H
#define KONGA_DB_TRANDB_DTLSPINSERTOPERATIONINSTANCETAB_H

#include <string>

#include <dtl/DTL.h>
#include <dtl/nullable.h>

#include <konga/Db/DtlDbUtils/DtlDbUtils.h>            
#include <konga/Db/TranDb/SpInsertOperationInstanceTab.h>

using namespace dtl;

namespace dtl_trandb
{

class DtlSpInsertOperationInstanceTab : public TranDb::SpInsertOperationInstanceTab
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
		SpParameters(void) : OperationId(-1), ParentOperationInstanceId(-1), OperationInstanceId(-1) {}

		                                                                                                                                    
		dtl::nullable<long> OperationId;
		dtl::nullable<long> ParentOperationInstanceId;
		dtl::nullable<std::string> ReceivedFrom;
        long OperationInstanceId;
    };
    
    class SpBPA
    {
    public:
        void operator()(dtl::BoundIOs& cols, SpParameters& params)
        {
            cols[0] >> params.OperationInstanceId;
            cols[1] << params.OperationId;
            cols[2] << params.ParentOperationInstanceId;
			cols[3] << params.ReceivedFrom;
        }
    };

    DtlSpInsertOperationInstanceTab(dtl::DBConnection* pConn) : m_pConn(pConn) {}
    virtual ~DtlSpInsertOperationInstanceTab() {}

	void SetOperationId(long lOperationId) {
		m_params.OperationId = lOperationId;
	}

	void SetParentOperationInstanceId(long long lParentOperationInstanceId) {
		m_params.ParentOperationInstanceId = (long)lParentOperationInstanceId;
	}

	void SetReceivedFrom(const std::string& stFrom) {
		m_params.ReceivedFrom = stFrom.substr(0, 50);	                              
	}

	long long GetOperationInstanceId() const {
		return m_OperationInstanceId;
	}

    void Execute();
    void Close() {                     }
    
private:
    dtl::DBConnection* m_pConn;
    SpParameters m_params;
    long long m_OperationInstanceId;
};

}                           

#endif                                                     

