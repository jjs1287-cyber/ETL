/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef _KONGA_TRANSFORMATION_UTILS_INTEGRATION_DEBUG_UTILS_H
#define _KONGA_TRANSFORMATION_UTILS_INTEGRATION_DEBUG_UTILS_H

#include <string>
#include <set>

namespace konga {
	class Debuggee;

	int IntegrationDebugBreakpointHit(Debuggee* pDebuggee, const std::string& stTargetPath, long lScriptId,
		long line_number, const std::string& stInstanceString, const std::string& stGeneratorPath,
		const std::set<DataElement*>& source_de, const std::string& stResult);
}

#endif                                                         
