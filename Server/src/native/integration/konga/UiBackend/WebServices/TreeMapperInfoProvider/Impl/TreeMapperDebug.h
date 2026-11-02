/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef TREE_MAPPER_INFO_PROVIDER_DEBUG_H
#define TREE_MAPPER_INFO_PROVIDER_DEBUG_H

#include <string>

namespace konga {

	enum DebugCommand {
		DebugCommand_Unknown = 0,
		DebugCommand_Run = 1,
		DebugCommand_Continue = 2,
		DebugCommand_Step = 3,
	};

	DebugCommand GetDebugCommand(const std::string& sDebugCommand);

	std::string ToString(const DebugCommand);

}

#endif                                     

