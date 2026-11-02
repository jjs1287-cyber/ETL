/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_HTTP_ENDPOINT_SELECTOR_H
#define KONGA_HTTP_ENDPOINT_SELECTOR_H

#include <string>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include "HttpEndpoint.h"

namespace konga {

	class DbConnection;

	class HttpEndpointSelector : private DbConnectionHolder {
	public:
		HttpEndpointSelector(DbConnection* pDbConn = 0);
		~HttpEndpointSelector(void) {}

		HttpEndpoint* GetHttpEndpoint(long httpEndointId);
	};

}

#endif                                  


