/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_DTLSPINSERTROUTETAB_H
#define KONGA_DB_DTLSPINSERTROUTETAB_H

#include <dtl/DTL.h>
#include <dtl/nullable.h>

#include <konga/Db/IDb/SpInsertRouteTab.h>

namespace dtl_idb
{


class DtlSpInsertRouteTab : public IDb::SpInsertRouteTab
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
        dtl::nullable<int> OldDestinationTaskInstanceId;
        dtl::nullable<int> NewDestinationTaskInstanceId;
    };
    
    class SpBPA
    {
    public:
        void operator()(dtl::BoundIOs& cols, SpParameters& params)
        {
            cols[0] << params.OldDestinationTaskInstanceId;
            cols[1] << params.NewDestinationTaskInstanceId;
        }
    };


    DtlSpInsertRouteTab(dtl::DBConnection* pConn) : m_pConn(pConn)
    {}
    virtual ~DtlSpInsertRouteTab() {}

    void SetOldDestinationTaskInstanceId(int id) {
        m_params.OldDestinationTaskInstanceId = id;
    }
    void SetNewDestinationTaskInstanceId(int id) {
        m_params.NewDestinationTaskInstanceId = id;
    }

    void Execute();

    void Close() {
                        
    }


private:
    dtl::DBConnection* m_pConn;

    SpParameters m_params;
};


}                         



#endif                                  

