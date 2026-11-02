/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef UTILS_CANCEL_HANDLER_H
#define UTILS_CANCEL_HANDLER_H

namespace konga {

	                                    
	class CancelHandler {
	public:
		CancelHandler(void) {}
		virtual ~CancelHandler(void) {}

	public:
		                                                       
		                                                    
		virtual bool cancel() = 0;

	};
}

#endif                          

