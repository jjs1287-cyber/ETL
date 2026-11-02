/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_FILE_TRANSFER_HTTP_H
#define KONGA_FILE_TRANSFER_HTTP_H

#include "HttpBase.h"

namespace konga {

	class Http : public HttpBase {
	public:
		Http(HttpProxyConfiguration* pProxyConfiguration                    );
		~Http(void);

	protected:
		bool isHttps() const { return false; };
	};
}
#endif                              

