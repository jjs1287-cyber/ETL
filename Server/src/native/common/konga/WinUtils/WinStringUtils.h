/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WIN_STRING_UTILS_H
#define KONGA_WIN_STRING_UTILS_H

#include <windows.h>

#include <string>

namespace konga {

	long AnsiToBstr(const std::string& stIn, BSTR& bsOut);                                                    
	long BstrToAnsi(const BSTR bsIn, char*& pcOut);                                                         

}                   

#endif                            

