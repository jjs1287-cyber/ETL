/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <konga/Db/DtlDbUtils/DtlDbUtils.h>
#include "DtlSpInsertLogTab.h"

using namespace dtl;
using namespace konga_dtl;
using namespace dtl_trandb;


void DtlSpInsertLogTab::Execute() {

	try
	{
		DBView<SpReturnVals, SpParameters> view(
			DBView<SpReturnVals, SpParameters>::Args().
					tables("{? = CALL SpInsertLogTab(?,?,?,?,?,?)}").
					conn(*m_pConn).
					bpa(SpBPA()).
					bca(SpBCA()) );

		DBView<SpReturnVals, SpParameters>::sql_iterator it = view.begin();
		it.Params() = m_params;

		*it = SpReturnVals();                                   
		++it;
		m_iLogId = it.Params().LogId;
	}
	catch( DBException& e )
	{
		ThrowDbException(e, "", 0);
	}
	catch( RootException& e )
	{
		ThrowDbException(e, "Failed to execute the SpInsertLogTab stored procedure. ", 1);
	}
#ifndef _DEBUG
	catch( ... )
	{
		throw konga::DbException("Failed to execute the SpInsertLogTab stored procedure. An unknown error occurred.", 2);
	}
#endif

}

