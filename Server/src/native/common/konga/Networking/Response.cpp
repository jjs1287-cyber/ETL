/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "Result.h"
#include "Response.h"

using namespace konga;
using namespace konga::networking;

Response::~Response() {
	std::vector<Result*>::iterator itCmd = m_vResults.begin();
	for(; itCmd != m_vResults.end(); ++itCmd)
		delete *itCmd;
}
