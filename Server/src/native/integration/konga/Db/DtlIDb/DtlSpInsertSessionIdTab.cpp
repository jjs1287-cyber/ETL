/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <konga/Db/DtlDbUtils/DtlDbUtils.h>

#include "DtlSpInsertSessionIdTab.h"

using namespace dtl;
using namespace konga_dtl;
using namespace dtl_idb;

void DtlSpInsertSessionIdTab::Execute()
{
    try
    {
        DBView<SpReturnVals, SpParameters> view(
            DBView<SpReturnVals, SpParameters>::Args().
					tables("{? = CALL SpInsertSessionIdTab(?)}").
                    conn(*m_pConn).
                    bpa(SpBPA()).
                    bca(SpBCA()) );

        DBView<SpReturnVals, SpParameters>::sql_iterator it = view.begin();
        it.Params() = m_params;

        *it = SpReturnVals();                                   
        ++it;
		m_iSessionId = it.Params().SessionId;
    }
    catch( DBException& ex )
    {
        ThrowDbException(ex, "", 0);
    }
    catch( RootException& ex )
    {
        ThrowDbException(ex, "Failed to execute the SpInsertSessionIdTab stored procedure. ", 1);
    }
    #ifndef _DEBUG
    catch( ... )
    {
        throw konga::DbException("Failed to execute the SpInsertSessionIdTab stored procedure. An unknown error occurred.", 2);
    }
    #endif
}
