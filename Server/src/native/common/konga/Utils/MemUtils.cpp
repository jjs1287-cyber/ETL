/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 * ETLForge: Windows x64 stack diagnostics.
 */
#include "MemUtils.h"
#include <sstream>
long* konga::GetStackBottom() {
    ULONG_PTR low = 0, high = 0;
    GetCurrentThreadStackLimits(&low, &high);
    return reinterpret_cast<long*>(high);
}
std::string konga::FormatBytes(float bytes) {
    const unsigned int MB = 1024 * 1024, kB = 1024;
    std::stringstream text;
    text.precision(4);
    if (bytes > MB) text << (bytes / MB) << " MB";
    else if (bytes > kB) text << (bytes / kB) << " kB";
    else text << bytes << " B";
    return text.str();
}
int konga::GetCallsLeft() {
                                                                             
    return 999;
}

