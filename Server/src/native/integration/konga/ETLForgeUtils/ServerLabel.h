/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ETLFORGE_UTILS_SERVER_LABEL_H
#define KONGA_ETLFORGE_UTILS_SERVER_LABEL_H

#include <string>

namespace konga {
	namespace ETLForgeUtils {
		void SetServerLabel(const std::string& label, bool bQuiet);
		void PrintServerLabel(bool bQuiet);
	}
}

#endif                                       

