/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/Utils/CrException.h>

#include "TreeMapperDebug.h"

using namespace konga;
using std::string;

konga::DebugCommand konga::GetDebugCommand(const string& sDebugCommand) {
	if ( EqualsIgnoreCase(sDebugCommand, "Run") )
		return DebugCommand_Run;
	else if ( EqualsIgnoreCase(sDebugCommand, "Continue") )
		return DebugCommand_Continue;
	else if ( EqualsIgnoreCase(sDebugCommand, "Step") )
		return DebugCommand_Step;
	throw CrException("Unknown command: \"" + sDebugCommand + "\".");

	return DebugCommand_Unknown;
}

string konga::ToString(const DebugCommand dc) {
	switch(dc) {
		case DebugCommand_Run:
			return "Run";
		case DebugCommand_Continue:
			return "Continue";
		case DebugCommand_Step:
			return "Step";
		default:
			return "Unknown";
	};
}

