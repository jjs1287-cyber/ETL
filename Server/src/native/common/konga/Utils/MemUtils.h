/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 * ETLForge: use Windows x64 stack APIs instead of x86 EBP frame walking.
 */
#ifndef KONGA_MEM_UTILS_H
#define KONGA_MEM_UTILS_H
#include <windows.h>
#include <iostream>
#include <string>
namespace konga {
                                                                                     
long* GetStackBottom();
std::string FormatBytes(float d);
int GetCallsLeft();
                                                                                 
inline void PrintStackFrames() {
    void* frames[64] = {};
    const USHORT count = CaptureStackBackTrace(0, 64, frames, nullptr);
    for (USHORT i = 0; i < count; ++i)
        std::cout << "Frame " << i << ": " << frames[i] << std::endl;
}
}
inline void CalculateStackFrames() {
    void* frames[64] = {};
    CaptureStackBackTrace(0, 64, frames, nullptr);
}
#endif

