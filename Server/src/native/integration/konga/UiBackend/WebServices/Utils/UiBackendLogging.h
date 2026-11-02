/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_WSUTILS_UI_BACKEND_LOGGING_H
#define KONGA_WS_WSUTILS_UI_BACKEND_LOGGING_H

#include <string>

namespace konga {

	                                                    
	bool isDebugLogEnabled_();

	                           
	void debugLog_(const std::string& stMsg);
	       

	                           
	void auditLog(const std::string& stMsg);

}

#define UIBACKEND_TRACE(X) if(konga::isDebugLogEnabled_()) {konga::debugLog_(X);}

#endif                                         
