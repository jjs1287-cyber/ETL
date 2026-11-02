/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

                                                                             

#ifndef PORTABLE_DEFINITIONS_UNIX_H
#define PORTABLE_DEFINITIONS_UNIX_H

#define stricmp strcasecmp
#define strnicmp strncasecmp
#define afx_msg
#define MAX_PATH 256
#ifndef TRACE
	#define TRACE printf
#endif
#ifndef __WINE_WINDOWS_H
	extern "C" unsigned long GetLastError();
#endif                    

#ifndef INFINITE
	#define INFINITE (-1)
#endif    

#ifndef INTERNET_FLAG_IGNORE_REDIRECT_TO_HTTP
	#define INTERNET_FLAG_IGNORE_CERT_CN_INVALID	0x00001000
	#define INTERNET_FLAG_IGNORE_CERT_DATE_INVALID	0x00002000
	#define INTERNET_FLAG_IGNORE_REDIRECT_TO_HTTPS	0x00004000
	#define INTERNET_FLAG_IGNORE_REDIRECT_TO_HTTP	0x00008000
#endif

#ifndef S_OK
	#define S_OK 0L
#endif
#ifndef S_FALSE
	#define S_FALSE 1L
#endif
#ifndef E_FAIL
	#define E_FAIL 0x80000000L
#endif
#ifndef FAILED
	#define FAILED(hr)  (hr&E_FAIL)
#endif
#ifndef SUCCEEDED
	#define SUCCEEDED(hr) ((hr&E_FAIL)==0)
#endif
#ifndef HRESULT_CODE
	#define HRESULT_CODE(hr) (hr&0xFFFFL)
#endif

                         

#ifndef _LARGEFILE_SOURCE
	#define _LARGEFILE_SOURCE 1
#endif
#define _FILE_OFFSET_BITS 64

#include <stdio.h>
#define fseek fseeko64
#define ftell ftello64

#endif                               

