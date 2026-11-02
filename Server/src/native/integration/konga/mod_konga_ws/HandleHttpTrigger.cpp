/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Utils/StringUtils.h>

#include <konga/HttpEndpoint/HttpTrigger.h>

#include "mod_konga_ws.h"

using namespace konga;
using std::string;

bool konga::HandleHttpTrigger(request_rec* r, int& res, DbConnection& dbConn) {
	                                        
	if( !r || !r->uri ) {
		return false;
	}

	const string url(ltrim_chars(r->uri, "/\\"));

	const HttpTrigger trigger = konga::selectHttpTrigger(url, dbConn);
	if ( trigger.Type == HttpTriggerType_Unknown ) {
		return false;
	}

	if ( trigger.Type == HttpTriggerType_HttpEndpoint ) {
		if ( HandleHttpEndpoint(r, res, dbConn, trigger.ItemId) )
			return true;
	}

	return false;
}

