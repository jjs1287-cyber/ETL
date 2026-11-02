/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WOKRFLOW_VALIDATION_H
#define KONGA_WOKRFLOW_VALIDATION_H

#include <string>

namespace konga {
	bool VerifyProcessForUserExecution(const std::string& sProcessName, int iUserId);
}                   

#endif                               
