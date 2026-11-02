/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                     

#ifndef KONGA_JAVA_VERSION_UTILS_H
#define KONGA_JAVA_VERSION_UTILS_H

#include <string>

namespace konga {

std::string GetMaxJavaVersion(bool bCheck64Bit);
bool IsJreVersionLargeEnough(const std::string& sRequiredVersion, bool bCheck64Bit);

std::string GetJavaPath(std::string sJavaVersion, bool bCheck64Bit);
std::string GetJavaRuntimeLib(std::string sJavaVersion, bool bCheck64Bit);

}

#endif                              
