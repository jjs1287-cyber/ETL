/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/Utils/CrException.h>
#include "Https.h"

using namespace konga;

Https::Https(HttpProxyConfiguration* pProxyConfiguration) : HttpBase(pProxyConfiguration)
{
}

Https::~Https(void) { }

long Https::doPreTransfer() {
	long l = HttpBase::doPreTransfer();
	if ( l != Protocol::SUCCESS )
		return l;

	return Protocol::SUCCESS;
}

