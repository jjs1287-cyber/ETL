/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "DtlSpInsertRouteTab.h"

#include <konga/Db/DtlDbUtils/DtlDbUtils.h>

using namespace dtl;
using namespace konga_dtl;
using namespace dtl_idb;

void DtlSpInsertRouteTab::Execute()
{
    try
    {
        DBView<SpReturnVals, SpParameters> view(
            DBView<SpReturnVals, SpParameters>::Args().
                    tables("{CALL SpInsertRoteTab(?,?)}").
                    conn(*m_pConn).
                    bpa(SpBPA()).
                    bca(SpBCA()) );

        DBView<SpReturnVals, SpParameters>::sql_iterator it = view.begin();
        it.Params() = m_params;
        *it = SpReturnVals();
        ++it;
    }
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 0);
    }
    catch( RootException& ex )
    {
        ThrowDbException(ex, "Failed to execute the SpInsertRoteTab stored procedure. ", 1);
    }
    #ifndef _DEBUG
    catch( ... )
    {
        throw konga::DbException("Failed to execute the SpInsertRoteTab stored procedure. An unknown error occurred.", 2);
    }
    #endif
    
}
