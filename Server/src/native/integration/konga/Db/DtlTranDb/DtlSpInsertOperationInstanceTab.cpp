/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <konga/Db/DtlDbUtils/DtlDbUtils.h>
#include "DtlSpInsertOperationInstanceTab.h"

using namespace dtl;
using namespace konga_dtl;
using namespace dtl_trandb;


void DtlSpInsertOperationInstanceTab::Execute() {

	try
	{
		DBView<SpReturnVals, SpParameters> view(
			DBView<SpReturnVals, SpParameters>::Args().
					tables("{? = CALL SpInsertOperationInstanceTab(?,?,?)}").
					conn(*m_pConn).
					bpa(SpBPA()).
					bca(SpBCA()) );

		DBView<SpReturnVals, SpParameters>::sql_iterator it = view.begin();
		it.Params() = m_params;

		*it = SpReturnVals();                                   
		++it;
		m_OperationInstanceId = it.Params().OperationInstanceId;
	}
	catch( DBException& e )
	{
		ThrowDbException(e, "", 0);
	}
	catch( RootException& e )
	{
		ThrowDbException(e, "Failed to execute the SpInsertOperationInstanceTab stored procedure. ", 1);
	}
#ifndef _DEBUG
	catch( std::exception& e )
	{
		throw konga::DbException(std::string("Failed to execute the SpInsertOperationInstanceTab stored procedure. An unexpected exception occurred: ") + e.what(), 2);
	}
	catch( ... )
	{
		throw konga::DbException("Failed to execute the SpInsertOperationInstanceTab stored procedure. An unknown error occurred.", 3);
	}
#endif

}

