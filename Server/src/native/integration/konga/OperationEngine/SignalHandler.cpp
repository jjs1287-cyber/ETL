/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <signal.h>

#include <iostream>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/CancelHandlers.h>
#include <konga/Utils/CrException.h>

#include "SignalHandler.h"

using namespace konga;

                                                                            
CancelHandlers* g_cancel_handlers;
boost::mutex g_cancel_handlers_mutex;

void OperationEngineSignalHandler(int iSig) {
	boost::mutex::scoped_lock lock(g_cancel_handlers_mutex);

	switch(iSig) {
	case SIGINT:
		std::cout << "Caught Ctrl-C. Trying to exit gracefully." << std::endl;
		if ( g_cancel_handlers != 0 ) {
			g_cancel_handlers->cancelAll();
			return;
		}
		break;
	}

	exit(SIGINT);
}

void konga::InstallSignalHandler() {
	if ( g_cancel_handlers != 0 ) {
		throw InternalErrorException("A signal handler has already been installed");
	}

	g_cancel_handlers = new CancelHandlers();

	                                     
	signal(SIGINT, OperationEngineSignalHandler);
}

CancelHandlerContainer* konga::getCancelHandlerContainer() {
	return g_cancel_handlers;
}

