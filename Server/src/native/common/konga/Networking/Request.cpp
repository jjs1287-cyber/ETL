/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "Command.h"
#include "Request.h"

using namespace konga;
using namespace konga::networking;

Request::~Request() {
	std::vector<Command*>::iterator itCmd = m_vCommands.begin();
	for(; itCmd != m_vCommands.end(); ++itCmd)
		delete *itCmd;
}
