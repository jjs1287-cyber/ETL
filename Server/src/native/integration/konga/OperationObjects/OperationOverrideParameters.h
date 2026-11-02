/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_OVERRIDE_PARAMETERS_H
#define KONGA_OPERATION_OVERRIDE_PARAMETERS_H

#include <string>

namespace konga {

	class OperationOverrideParameters {
		OperationOverrideParameters(void) { }
		~OperationOverrideParameters(void) { }

		void Parse(const std::string& stOverrideParameters, char chItemSeparator = '^', char chKeyValueSeparator = '=');
	};
}

#endif                                         

