/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

                                             

#ifndef PORTABLE_DEFINITIONS_WIN32_H
#define PORTABLE_DEFINITIONS_WIN32_H

#ifndef strcasecmp
	#define strcasecmp	stricmp
#endif
#ifndef strncasecmp
	#define strncasecmp	strnicmp
#endif
                                                                          
#if defined(_MSC_VER) && _MSC_VER < 1900
#define snprintf _snprintf
#endif
#define atoll _atoi64
#define strtoll _strtoi64
#define stat64 _stat64

typedef int ssize_t;
#include <windows.h>
#ifdef GetCurrentTime
#undef GetCurrentTime
#endif
#ifdef max
	#undef max
#endif
#ifdef min
	#undef min
#endif

                         
#include <stdio.h>
#define fseek _fseeki64
#define ftell _ftelli64

#endif                                


