/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include "mod_konga_ws.h"

int konga::HandleRequest(request_rec* r) {
	int res = DECLINED;
	                                                           
	if ( r->handler && strcmp(r->handler, "axis") == 0 )
		return res;

	if( HandleHostedWebService(r, res) )
		return res;

	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	if ( HandleHttpTrigger(r, res, *pTranDb) )
		return res;

	return res;
}
