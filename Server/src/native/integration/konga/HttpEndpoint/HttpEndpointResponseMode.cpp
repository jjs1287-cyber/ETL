/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             

#include "HttpEndpointResponseMode.h"

using namespace konga;

konga::HttpEndpointResponseMode konga::getHttpEndpointResponseMode(int mode) {
	switch(mode) {
		case 0:
			return ResponseMode_None;
		case 1:
			return ResponseMode_Operation;
		case 2:
			return ResponseMode_Variable;
		default:
			throw InternalErrorException("Response Mode " + toString(mode) + " is not defined!");
	};
}
