/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WDIPATHFINDER_H
#define KONGA_WDIPATHFINDER_H

#include <string>

class KString;

namespace konga {

	                             
	                                                      
	namespace WDIPathFinder {

		                                               

		                                          
		bool GetWDIPath(KString& WDIPath);
		bool GetWDIPath(std::string& WDIPath);

		                                                                          
		bool GetWDIDebugPath(KString& WDIDebugPath);
		bool GetWDIDebugPath(std::string& WDIDebugPath);
	}
}

#endif                         

