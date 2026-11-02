/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef OPERATION_ENGINE_SIGNAL_HANDLER_H
#define OPERATION_ENGINE_SIGNAL_HANDLER_H

namespace konga {

	class CancelHandlerContainer;

	void InstallSignalHandler();

	CancelHandlerContainer* getCancelHandlerContainer();

}

#endif                                     

