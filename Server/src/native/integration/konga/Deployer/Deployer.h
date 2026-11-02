/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef KONGA_DEPLOYER_H
#define KONGA_DEPLOYER_H

#include <string>

namespace konga {

void DeployJitterpak(const std::string& user, const std::string& pwdHash, const std::string& host,
					const std::string& jitterpakFileName, const std::string& jarFileName,
					const std::string& jvmArgs = "");

}

#endif                    
