/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_HTTP_ENDPOINT_RESPONSE_MODE_H
#define KONGA_HTTP_ENDPOINT_RESPONSE_MODE_H

namespace konga {
	enum HttpEndpointResponseMode {
		ResponseMode_None = 0,
		ResponseMode_Operation = 1,
		ResponseMode_Variable = 2
	};

	HttpEndpointResponseMode getHttpEndpointResponseMode(int mode);
}

#endif                                       

