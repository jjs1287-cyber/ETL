/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_UTILS_COMMAND_H
#define ETLFORGE_UTILS_COMMAND_H

#include <string>

namespace konga {

	void SendCommandToModule(const std::string& module, const std::string& command, bool bQuiet);

}

#endif                            

