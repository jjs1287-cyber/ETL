/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_FILE_TRANSFER_HTTPS_H
#define KONGA_FILE_TRANSFER_HTTPS_H

#include "HttpBase.h"

namespace konga {

	class Https : public HttpBase {
	public:
		Https(HttpProxyConfiguration* pProxyConfiguration                   );
		~Https(void);

	protected:
		bool isHttps() const { return true; };

		                         
		bool isSslEnabled() const { return true; }

		                          
		virtual long doPreTransfer();

	};
}
#endif                               

