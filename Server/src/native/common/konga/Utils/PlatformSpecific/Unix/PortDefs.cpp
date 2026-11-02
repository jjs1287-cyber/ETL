/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

                                           
  

#include <konga/Utils/PortDefs.h>

#ifndef __WINE_WINDOWS_H

	#include <errno.h>
	extern "C" unsigned long GetLastError() {
		return errno;
	}

#endif                    


