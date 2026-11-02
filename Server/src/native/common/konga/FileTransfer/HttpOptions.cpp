/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include "HttpOptions.h"

using namespace konga;

void HttpOptions::setCustomRequestHeaders(const std::string& sCustomRequestHeaders) {
	ParseRFC2822Headers(sCustomRequestHeaders, m_vCustomRequestHeaders);
}
